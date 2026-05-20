package com.zatuna.library.model;

import java.time.LocalDate;

public record Book(int id, String title, String author, boolean isAvailable) {}

record Member(int id, String name, String email) {}

record Loan(int id, int bookId, int memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {}
