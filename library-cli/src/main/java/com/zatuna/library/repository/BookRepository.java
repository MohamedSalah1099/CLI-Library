package com.zatuna.library.repository;

import com.zatuna.library.model.Book;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface BookRepository {
    static BookRepository create(Connection connection) {
        return new JdbcBookRepository(connection);
    }
    List<Book> findAll() throws SQLException;
    Book findById(int id) throws SQLException;
    void save(String title, String author) throws SQLException;
    void updateAvailability(int bookId, boolean isAvailable) throws SQLException;

    // Member & Loan operations consolidated for simplicity in this small project
    void saveMember(String name, String email) throws SQLException;
    void createLoan(int bookId, int memberId, LocalDate dueDate) throws SQLException;
    void markAsReturned(int bookId) throws SQLException;
}

class JdbcBookRepository implements BookRepository {
    private final Connection conn;

    public JdbcBookRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("is_available")));
            }
        }
        return books;
    }

    @Override
    public Book findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("is_available"));
                }
            }
        }
        return null;
    }

    @Override
    public void save(String title, String author) throws SQLException {
        String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateAvailability(int bookId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isAvailable);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    @Override
    public void saveMember(String name, String email) throws SQLException {
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    @Override
    public void createLoan(int bookId, int memberId, LocalDate dueDate) throws SQLException {
        String sql = "INSERT INTO loans (book_id, member_id, due_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.setDate(3, Date.valueOf(dueDate));
            ps.executeUpdate();
        }
    }

    @Override
    public void markAsReturned(int bookId) throws SQLException {
        String sql = "UPDATE loans SET return_date = CURRENT_DATE WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
    }
}
