package com.zatuna.library;

import com.zatuna.library.config.DatabaseConfig;
import com.zatuna.library.repository.BookRepository;
import com.zatuna.library.service.LibraryService;
import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            var repo = BookRepository.create(DatabaseConfig.getConnection());
            var service = new LibraryService(repo);

            // Pattern 2: Observer
            service.addObserver((bid, mid) ->
                System.out.println("[OBSERVER] Notification: Book ID " + bid + " checked out by Member " + mid));

            System.out.println("=== Library CLI ===");
            while (true) {
                System.out.println("\n1. Add Book | 2. Register Member | 3. Checkout | 4. Return | 5. List (Strategy) | 6. Exit");
                System.out.print("> ");
                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Title: "); String t = scanner.nextLine();
                            System.out.print("Author: "); String a = scanner.nextLine();
                            service.addBook(t, a);
                        }
                        case "2" -> {
                            System.out.print("Name: "); String n = scanner.nextLine();
                            System.out.print("Email: "); String e = scanner.nextLine();
                            service.registerMember(n, e);
                        }
                        case "3" -> {
                            System.out.print("Book ID: "); int b = Integer.parseInt(scanner.nextLine());
                            System.out.print("Member ID: "); int m = Integer.parseInt(scanner.nextLine());
                            service.checkout(b, m);
                        }
                        case "4" -> {
                            System.out.print("Book ID: "); int b = Integer.parseInt(scanner.nextLine());
                            service.returnBook(b);
                        }
                        case "5" -> service.listBooks(books -> {
                            System.out.println("--- Current Inventory (Strategy: Simple List) ---");
                            books.forEach(System.out::println);
                        });
                        case "6" -> System.exit(0);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal: " + e.getMessage());
        }
    }
}
