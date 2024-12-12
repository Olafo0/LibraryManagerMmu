package Data;

import Modals.Book;
import Modals.BookRecord;
import Modals.User;
import Services.CsvService;

import java.util.ArrayList;

public class DataRepo
{
    private static DataRepo instance;
    private ArrayList<User> users;
    private ArrayList<Book> books;
    private ArrayList<BookRecord> bookRecords;

    public static DataRepo instance()
    {
        if (instance == null)
        {
            instance = new DataRepo();
        }
        return instance;
    }
    private DataRepo()
    {
        users = CsvService.getAllMembers();
        books = CsvService.getAllBooks();
        bookRecords = CsvService.getAllBookBorrows(books, users);
    }

    public ArrayList<User> getAllUsers()
    {
        return users;
    }

    public ArrayList<Book> getAllBooks()
    {
        return books;
    }

    public ArrayList<BookRecord> getAllBookRecords()
    {
        return bookRecords;
    }

    public void addUser(User user)
    {
        users.add(user);
        CsvService.addNewMember(user);
    }

    public void addBook(Book book)
    {
        books.add(book);
        CsvService.addNewBook(book);
    }

    public void removeBook(Book book)
    {
        books.remove(book);
        CsvService.removeABook(books);
    }

    public void addBookRecord(BookRecord record)
    {
        bookRecords.add(record);
        CsvService.insertNewBookRecord(record);
    }

    public void removeBookRecord(BookRecord record)
    {
        bookRecords.remove(record);
        CsvService.removeBookRecord(bookRecords);
    }
}
