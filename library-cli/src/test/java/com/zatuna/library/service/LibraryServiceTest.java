package com.zatuna.library.service;

import com.zatuna.library.model.Book;
import com.zatuna.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private LibraryService service;

    @Test
    void addBook_Valid_CallsRepo() throws Exception {
        service.addBook("Title", "Author");
        verify(repository).save("Title", "Author");
    }

    @Test
    void addBook_EmptyTitle_Throws() {
        assertThrows(Exception.class, () -> service.addBook("", "Author"));
    }

    @Test
    void checkout_Valid_CallsRepo() throws Exception {
        Book book = new Book(1, "T", "A", true);
        when(repository.findById(1)).thenReturn(book);

        service.checkout(1, 101);

        verify(repository).createLoan(eq(1), eq(101), any());
        verify(repository).updateAvailability(1, false);
    }

    @Test
    void checkout_NotifiesObservers() throws Exception {
        Book book = new Book(1, "T", "A", true);
        when(repository.findById(1)).thenReturn(book);
        LibraryService.LoanObserver observer = mock(LibraryService.LoanObserver.class);
        service.addObserver(observer);

        service.checkout(1, 101);

        verify(observer).onCheckout(1, 101);
    }

    @Test
    void listBooks_UsesStrategy() throws Exception {
        List<Book> books = List.of(new Book(1, "T", "A", true));
        when(repository.findAll()).thenReturn(books);
        LibraryService.OutputStrategy strategy = mock(LibraryService.OutputStrategy.class);

        service.listBooks(strategy);

        verify(strategy).display(books);
    }

    @Test
    void returnBook_Valid_CallsRepo() throws Exception {
        Book book = new Book(1, "T", "A", false);
        when(repository.findById(1)).thenReturn(book);

        service.returnBook(1);

        verify(repository).markAsReturned(1);
        verify(repository).updateAvailability(1, true);
    }
    }
