package Data;

import Modals.Book;
import Modals.BookRecord;
import Modals.User;
import Services.CsvService;

import java.util.ArrayList;

/*
A centralised class that allows the program to manage all of the data used throughout the program.
 */
public class DataRepo
{
    // setting up all of the private variables which will be used throughout the clas
    private static DataRepo instance;
    private ArrayList<User> users;
    private ArrayList<Book> books;
    private ArrayList<BookRecord> bookRecords;

    // Method responsible for initializing data repo if not initialised
    public static DataRepo instance()
    {
        if (instance == null)
        {
            instance = new DataRepo();
        }
        return instance;
    }

    /*
    a constructor responsbile for getting all of the users, books, and bookRecords from the
    csv's.
    It uses the csvService in order to accomplish that
     */
    private DataRepo()
    {
        users = CsvService.getAllUsers();
        books = CsvService.getAllBooks();
        bookRecords = CsvService.getAllBookBorrows(books, users);
    }

    // Returns all of the users
    public ArrayList<User> getAllUsers()
    {
        return users;
    }

    // Returns all of the books
    public ArrayList<Book> getAllBooks()
    {
        return books;
    }

    // Returns only available books
    public ArrayList<Book> getAllAvailableBooks()
    {
        ArrayList<Book> availableBooks = new ArrayList<>();
        for(Book book : books)
        {
            // if books are not borrowed add it to available books
            if(book.getBorrowed() != true)
            {
                availableBooks.add(book);
            }
        }

        // return an array containing available books
        return availableBooks;
    }

    // returns all of the book records
    public ArrayList<BookRecord> getAllBookRecords()
    {
        return bookRecords;
    }

    // Adds the passed user to the users arrayList and uses the csvService to add it to the user csv file
    public void addUser(User user)
    {
        users.add(user);
        CsvService.addNewMember(user);
    }

    // Adds the passed book to the books arrayList and uses the csvService to add it to the book csv file
    public void addBook(Book book)
    {
        books.add(book);
        CsvService.addNewBook(book);
    }

    // Removes the passed book from the books arrayList and uses the csvService to remove it from book csv file
    public void removeBook(Book book)
    {
        books.remove(book);
        CsvService.removeABook(books);
    }

    // Adds the passed bookRecord to the bookRecords arrayList and uses the csvService to add it to the borrowed csv file
    public void addBookRecord(BookRecord record)
    {
        bookRecords.add(record);
        CsvService.insertNewBookRecord(record);
    }

    // Removes the passed record from the bookRecords arrayList and uses the csvService to remove it from borrowed csv file
    public void removeBookRecord(BookRecord record)
    {
        bookRecords.remove(record);
        CsvService.removeBookRecord(bookRecords);
    }

    // clears all of the arrays and calls the csvservice to clear all of the files - used for testing
    public void clearAll()
    {
        users.clear();
        books.clear();
        bookRecords.clear();
        CsvService.clearAllCsv();
    }
}
