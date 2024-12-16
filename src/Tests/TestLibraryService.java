package Tests;

import Data.DataRepo;
import Modals.*;
import Services.LibraryService;
import Utils.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;



/*
A test class which tests out the main functionalities of the libraryService and some csvServices which are contained
within the libraryService methods
 */

public class TestLibraryService
{

    private LibraryService libraryService;
    private DataRepo dataRepo;

    @Test
    @Order(1)
    void testAddNewBook()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        ConsoleUtil.setTestInput(new String[]{"Test Title", "Test Author", "123456789", "Fiction"});
        libraryService.addNewBook();

        assertEquals(1, dataRepo.getAllBooks().size());
        dataRepo.clearAll();
    }

    @Test
    @Order(2)
    void testViewAllBooks()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));

        assertDoesNotThrow(() -> libraryService.viewAllBooks(DataRepo.instance().getAllBooks()));
        dataRepo.clearAll();
    }

    @Test
    @Order(3)
    void testRemoveABook()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        Book book = new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction");

        dataRepo.addBook(book);
        ConsoleUtil.setTestInput(new String[]{"1"});
        libraryService.removeABook();

        assertEquals(0, dataRepo.getAllBooks().size());
        dataRepo.clearAll();
    }

    @Test
    @Order(3)
    void testBorrowBook()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        Book book = new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction");
        Member member = new Member(1, "Member", "Olxf", "qwe?123","Olaf","Pala");

        dataRepo.addBook(book);
        ConsoleUtil.setTestInput(new String[]{"1"});
        libraryService.takeOutBook(member);

        assertTrue(book.getBorrowed());
        dataRepo.clearAll();
    }

    @Test
    @Order(4)
    void testReturnBook()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        Book book = new Book("1", "Test Title", "Test Author", "123456789", true, "Fiction");
        Member member = new Member(1, "Member", "Olxf", "qwe?123","Olaf","Pala");

        BookRecord record = new BookRecord(book, member, LocalDate.now().plusMonths(1));
        dataRepo.addBookRecord(record);
        ConsoleUtil.setTestInput(new String[]{"1"});
        libraryService.returnBook(member);

        assertFalse(book.getBorrowed());
        dataRepo.clearAll();
    }

    @Test
    @Order(5)
    void testViewBorrowedBooks()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        Member member = new Member(1, "Member", "Olxf", "qwe?123","Olaf","Pala");
        Book book = new Book("1", "Test Title", "Test Author", "123456789", true, "Fiction");

        BookRecord record = new BookRecord(book, member, LocalDate.now().plusMonths(1));
        dataRepo.addBookRecord(record);

        assertDoesNotThrow(() -> libraryService.viewBorrowedBooks(member));
        dataRepo.clearAll();
    }

    // Search functionality
    @Test
    @Order(6)
    void testSearchBookById()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        String query = "1";

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));
        dataRepo.addBook(new Book("23", "Dummy Title", "Dummy Author", "987654321", false, "Tea"));

        ArrayList<Book> filteredbook = libraryService.bookFilter(query);

        for (Book book : filteredbook)
        {
            assertEquals(query, book.getBookId());
        }
        dataRepo.clearAll();
    }

    @Test
    @Order(7)
    void testSearchBookByTitle()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        String query = "Test Title";

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));
        dataRepo.addBook(new Book("23", "Dummy Title", "Dummy Author", "987654321", false, "Tea"));
        ArrayList<Book> filteredbook = libraryService.bookFilter(query);

        for(Book book : filteredbook)
        {
            assertEquals(query, book.getTitle());
        }
        dataRepo.clearAll();
    }

    @Test
    @Order(8)
    void testSearchBookByAuthor()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        String query = "Test Author";

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));
        dataRepo.addBook(new Book("23", "Dummy Title", "Dummy Author", "987654321", false, "Tea"));

        ArrayList<Book> filteredbook = libraryService.bookFilter(query);

        for (Book book : filteredbook)
        {
            assertEquals(query, book.getAuthor());
        }
        dataRepo.clearAll();
    }

    @Test
    @Order(9)
    void testSearchBookByISBM()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        String query = "987654321";

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));
        dataRepo.addBook(new Book("23", "Dummy Title", "Dummy Author", "987654321", false, "Tea"));

        ArrayList<Book> filteredbook = libraryService.bookFilter(query);

        for (Book book : filteredbook)
        {
            assertEquals(query, book.getISBM());
        }
        dataRepo.clearAll();
    }

    @Test
    @Order(9)
    void testSearchBookByGenre()
    {
        dataRepo = DataRepo.instance();
        libraryService = new LibraryService();

        String query = "Tea";

        dataRepo.addBook(new Book("1", "Test Title", "Test Author", "123456789", false, "Fiction"));
        dataRepo.addBook(new Book("23", "Dummy Title", "Dummy Author", "987654321", false, "Tea"));

        ArrayList<Book> filteredbook = libraryService.bookFilter(query);

        for (Book book : filteredbook)
        {
            assertEquals(query, book.getGenre());
        }
        dataRepo.clearAll();
    }

}
