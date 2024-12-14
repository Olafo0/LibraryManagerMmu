package Services;

import Data.DataRepo;
import Modals.*;
import Utils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/*
    a service which handles all the library logic part of the program
    this class is the main part of the app
 */
public class LibraryService
{
    private DataRepo repository;

    /*
     When the libraryService is first initilised

     We also initilise the DataRepo class which is responsible for storing and accessing our data
     that is used through the program

     */
    public LibraryService()
    {
        DataRepo.instance();
    }

    /*
    A method that adds a new book to the library.
    It gets the details required and adds it to our csv file
     */
    public void addNewBook()
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();
        ConsoleUtil.consoleClear();

        System.out.println(" = - = - | Adding a new book | - = - = ");
        String bookTitle = ConsoleUtil.getUserInput("Book Title");
        String bookAuthor = ConsoleUtil.getUserInput("Author");
        String bookISBM = ConsoleUtil.getUserInput("ISBM");
        String bookGenre = ConsoleUtil.getUserInput("Genre");

        Random random = new Random();

        // checks if bookid is taken
        int newBookid = 0;
        boolean bookIdTaken = false;
        /*
        This  while loop is responsible for generating and checking a bookid.
        It preforms  a check to ensure that no duplicate id is generated and if a duplicate id
        is generated it repeats the process
         */
        do
        {
            bookIdTaken = false;
            int randomIdNumber = random.nextInt(750);

            for(Book book : books)
            {
                if(Integer.parseInt(book.getBookId()) == randomIdNumber)
                {
                    bookIdTaken = true;
                    break;
                }
            }

            if(bookIdTaken == false)
            {
                newBookid = randomIdNumber;
            }

        }
        while(bookIdTaken);

        // Creating the new book
        Book newBook = new Book(String.valueOf(newBookid) ,bookTitle, bookAuthor, bookISBM, false, bookGenre);
        // adding the new book to a list and appending it in the csv file through the centralised class
        DataRepo.instance().addBook(newBook);
        ConsoleUtil.consoleClear();
        System.out.println("NOTE: Your new book has been added to the system");

    }


    /*
        a bookFilter function is responsbile for getting and applying filters
         when showing the user books
     */
    public void bookFilter()
    {
        // Display to the user all of the available books
        viewAllBooks(DataRepo.instance().getAllAvailableBooks());

        int choice = 0;
        do
        {
            ConsoleUtil.consoleClear(2);
            // Provide the user with potential filters
            System.out.println(" 1 - Search by Title, Author, BookID, Genre, IBSM");
            System.out.println(" 2 - View all available books");
            System.out.println(" 3 - View all books");
            System.out.println(" 4 - Exit");

            choice = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));

            switch(choice)
            {
                // Case 1 - Search
                case 1:
                    /*
                       This in a way is a custom filter. It allows the user to enter the allowed
                       whatever they want as long as it fits one of the criteria and returns book(s)
                       that match to whatever is found in the csv files
                     */
                    System.out.println("You can enter the following Title, Author, BookID, Genre and IBSM");
                    String query = ConsoleUtil.getUserInput("Enter");
                    //booksToView is responsvile for fetching all the books that try to fit the user criteria
                    ArrayList<Book> filteredBooks = booksToView(query);
                    // display the books that have been fetched
                    viewAllBooks(filteredBooks);
                    break;

                // Case 2 - All available books
                case 2:
                    // Run a method that exists in the DataRepo for fetching all of the available books
                    viewAllBooks(DataRepo.instance().getAllAvailableBooks());
                    break;
                case 3:
                    // Display our entire book collection (even if a book is unavailable)
                    viewAllBooks(DataRepo.instance().getAllBooks());
            }


        }
        while(choice != 4);

        ConsoleUtil.consoleClear();
    }

    //bookFilter
    public ArrayList<Book> booksToView(String query)
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();

        ArrayList<Book> filteredBooks = new ArrayList<>();
        for(Book book : books)
        {
            if (book.getTitle().contains(query.toLowerCase()) || book.getAuthor().toLowerCase().contains(query.toLowerCase())
                    || book.getBookId().toLowerCase().contains(query.toLowerCase())
                    || book.getISBM().toLowerCase().contains(query.toLowerCase())
                    || book.getGenre().toLowerCase().contains(query.toLowerCase()))
            {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }



    public void viewAllBooks(ArrayList<Book> books)
    {
        ConsoleUtil.consoleClear();

        System.out.println("+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +");
        System.out.println("+                                Book collection                                  +");

        for(int i = 0; i < books.size(); i += 2)
        {
            Book book1 = books.get(i);
            Book book2 = null;
            if(i + 1 < books.size())
            {
                book2 = books.get(i + 1);
            }

            int spaceLength = 40;

            String header1 = ConsoleUtil.columnBoxHelper(String.valueOf(i + 1), 5);

            String header2 = "";
            if(book2 != null)
            {
                header2 = ConsoleUtil.columnBoxHelper(String.valueOf(i + 2), 5);
            }

            if(book2 != null)
            {
                System.out.println("+==( " + header1 + " )============================|=====( " + header2 + " )===========================+");
            }
            else
            {
                System.out.println("+==( " + header1 + " )============================|");
            }

            System.out.print(ConsoleUtil.columnBoxHelper("| * BOOK ID: " + book1.getBookId(), spaceLength));

            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| * BOOK ID: " + book2.getBookId(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Title: " + book1.getTitle(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Title: " + book2.getTitle(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book1.Author, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book2.getAuthor(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book1.Genre, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book2.getGenre(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| ISBM: " + book1.ISBM, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| ISBM: " + book2.getISBM(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            if(book1.Borrowed)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Not available", spaceLength));
            }
            else
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Available", spaceLength));
            }

            if(book2 != null)
            {
                if(book2.Borrowed)
                {
                    System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Not available", spaceLength));
                }
                else
                {
                    System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Available", spaceLength) + "  |");
                }
            }
            else
            {
                System.out.print("|");
            }

            System.out.println();

            if(book2 != null)
            {
                System.out.println("+=======================================|=========================================+");
            }
            else
            {
                System.out.println("+=======================================|");

            }
        }
    }

    public void removeABook()
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();
        ConsoleUtil.consoleClear();

        if(books.isEmpty())
        {
            System.out.println("NOTE: No books to remove");
        }
        else
        {
            viewAllBooks(DataRepo.instance().getAllAvailableBooks());

            ConsoleUtil.consoleClear(2);
            boolean bookFound = false;
            System.out.println("Please the book id of the book you want to remove");
            String bookId = ConsoleUtil.getUserInput("Book ID");
            for (Book book : books)
            {
                if (book.getBookId().equals(bookId))
                {
                    DataRepo.instance().removeBook(book);
                    bookFound = true;
                    ConsoleUtil.consoleClear();
                    System.out.println("Book has been removed");
                    break;
                }
            }
            if(bookFound == false)
            {
                ConsoleUtil.consoleClear();
                System.out.println("NOTE: Book can't be found. Either incorrect Book Id or it doesn't exist");
            }
        }
    }


    public void takeOutBook(Member member)
    {
        ConsoleUtil.consoleClear();
        ArrayList<Book> books = DataRepo.instance().getAllBooks();

        viewAllBooks(DataRepo.instance().getAllAvailableBooks());

        boolean hasUserBorrowedBook = false;
        LocalDate returnByDate = LocalDate.now().plusMonths(1);
        System.out.println("Please enter the book ID");
        String bookId = ConsoleUtil.getUserInput("Book ID");

        for(Book book : books)
        {
            if(book.getBookId().equals(bookId) && book.getBorrowed() == false)
            {
                book.setBorrowed(true);
                BookRecord newBookRecord = new BookRecord(book, member, returnByDate);
                DataRepo.instance().addBookRecord(newBookRecord);
                hasUserBorrowedBook = true;
                break;
            }
        }

        if(hasUserBorrowedBook)
        {
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book has been borrowed. The return date is (" + returnByDate + ")");
        }
        else
        {
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book not available or not found");
        }
    }


    public void returnBook(Member member)
    {
        ConsoleUtil.consoleClear();
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();
        viewBorrowedBooks(member);

        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.consoleClear(3);
        System.out.println("Enter the book ID to return it");
        String bookId = ConsoleUtil.getUserInput("Enter: ");

        boolean bookFound = false;
        for(BookRecord record : bookRecords)
        {
            // Check if ID and signed in member match the book borrowed
            if(record.getBook().getBookId().equals(bookId) && record.getMember().getId() == member.getId())
            {
                record.getBook().setBorrowed(false);
                DataRepo.instance().removeBookRecord(record);
                bookFound = true;
                ConsoleUtil.consoleClear();
                System.out.println("NOTE: Book has been returned");
                break;
            }
        }

        if(bookFound == false)
        {
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book not found. Incorrect book Id entered");
        }
    }

    public void viewBorrowedBooks(User member)
    {
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();
        ConsoleUtil.consoleClear();

        if(member instanceof Member)
        {
            System.out.println("+ - - - - - - - - | Borrowed Books | - - - - - - - - - - - - - -  + ");
            System.out.println("| Current date: " + LocalDate.now() + "                                        |");
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
            System.out.println("| BOOK ID  | BOOK NAME                  | RETURN BY               |");
            System.out.println("|----------|----------------------------|-------------------------|");

            for (BookRecord currentBookRecord : bookRecords)
            {
                if (currentBookRecord.getMember().getId() == member.getId())
                {
                    System.out.println(ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getBookId(), 11) +
                            ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getTitle(), 29) +
                            ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getReturnDate(), 25) + " |");
                    System.out.println("|----------|----------------------------|-------------------------|");
                }
            }
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
        }
        else if(member instanceof Admin)
        {
            System.out.println("+ - - - - - - - - | Borrowed Books | - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  + ");
            System.out.println("| Current date: " + LocalDate.now() + "                                                                        |");
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
            System.out.println("| BOOK ID  | BOOK NAME                  | RETURN BY         | BORROWED BY              | User ID  |");
            System.out.println("|----------|----------------------------|-------------------|--------------------------|----------|");


            for (BookRecord currentBookRecord : bookRecords)
            {
                String lastname = currentBookRecord.getMember().getLastname();
                String lastnameTrim = lastname.substring(0, lastname.length() - 1);

                System.out.println(ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getBookId(), 11) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getTitle(), 29) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getReturnDate(), 20) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getMember().Firstname + " " + lastnameTrim, 27) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getMember().getId(),10) + " |");
                System.out.println("|----------|----------------------------|-------------------|--------------------------|----------|");
            }
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
        }
    }
}
