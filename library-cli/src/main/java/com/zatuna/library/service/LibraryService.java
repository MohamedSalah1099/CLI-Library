package com.zatuna.library.service;

import com.zatuna.library.model.Book;
import com.zatuna.library.repository.BookRepository;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private final BookRepository repository;
    private final List<LoanObserver> observers = new ArrayList<>();

    // Pattern 2: Observer
    public interface LoanObserver {
        void onCheckout(int bookId, int memberId);
    }

    // Pattern 3: Strategy
    public interface OutputStrategy {
        void display(List<Book> books);
    }

    public LibraryService(BookRepository repository) {
        this.repository = repository;
    }

    public void addObserver(LoanObserver observer) {
        observers.add(observer);
    }

    public void listBooks(OutputStrategy strategy) throws SQLException {
        strategy.display(repository.findAll());
    }

    public void addBook(String title, String author) throws Exception {
        if (title == null || title.isBlank()) throw new Exception("Title cannot be empty");
        repository.save(title, author);
    }

    public List<Book> getAllBooks() throws SQLException {
        return repository.findAll();
    }

    public void registerMember(String name, String email) throws Exception {
        if (email == null || !email.contains("@")) throw new Exception("Invalid email");
        repository.saveMember(name, email);
    }

    public void checkout(int bookId, int memberId) throws Exception {
        Book book = repository.findById(bookId);
        if (book == null) throw new Exception("Book not found");
        if (!book.isAvailable()) throw new Exception("Book already checked out");

        repository.createLoan(bookId, memberId, LocalDate.now().plusDays(14));
        repository.updateAvailability(bookId, false);
        observers.forEach(o -> o.onCheckout(bookId, memberId));
    }

    public void returnBook(int bookId) throws Exception {
        Book book = repository.findById(bookId);
        if (book == null) throw new Exception("Book not found");
        repository.markAsReturned(bookId);
        repository.updateAvailability(bookId, true);
    }
}
