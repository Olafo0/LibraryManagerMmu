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

    public LibraryService()
    {
        /*
        We also initilise the DataRepo class which is responsible for storing and accessing our data
        that is used through the program
        */
        DataRepo.instance();
    }


    // A method which is responsible for getting the necessary details and adding a new book to the library
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

        int newBookid = 0;
        boolean bookIdTaken = false;
        /*
        This while loop is responsible for generating a unique book id for a new book.
        if a duplicate book id is generated it retrys until a unique one is generated
         */
        do
        {
            bookIdTaken = false; // set to false as we still don't know if bookid is taken
            int randomIdNumber = random.nextInt(750);

            for(Book book : books) // iterate through each book
            {
                if(Integer.parseInt(book.getBookId()) == randomIdNumber) // if a book matches an id we generated
                {
                    bookIdTaken = true; // set variable to true so the while loop can repeat itself
                    break;
                }
            }

            if(bookIdTaken == false) // Otherwise store the new bookid
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
     bookFilter method responsible for showing the user the correct books when viewing all books
     */
    public void booksToView()
    {
        // Display to the user all of the available books
        viewAllBooks(DataRepo.instance().getAllAvailableBooks());

        int choice = 0;
        do
        {
            ConsoleUtil.consoleClear(2);
            // Provide the user with potential filters that they want to apply
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
                    Users can enter the book title, Author, bookid, genre or IBSM which will return matching books
                     */
                    System.out.println("You can enter the following Title, Author, BookID, Genre and IBSM");
                    System.out.println("NOTE: IBSM and Book ID need to be an exact match");
                    String query = ConsoleUtil.getUserInput("Enter");

                    //booksToView is responsible for fetching all the books that try to fit the user criteria
                    ArrayList<Book> filteredBooks = bookFilter(query);
                    // display the books that have been fetched
                    viewAllBooks(filteredBooks);
                    break;

                // Case 2 - All available books
                case 2:
                    /*
                     Run a method that exists in the DataRepo for fetching all of the available books.
                     Which then runs a method to display all of the books
                     */
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

    //bookFilter fetches books that match the user query
    public ArrayList<Book> bookFilter(String query)
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();

        ArrayList<Book> filteredBooks = new ArrayList<>();
        for(Book book : books) // we iterate through each book
        {
            // if statement responsible for getting books that match the query
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(query.trim().toLowerCase())
                    || book.getBookId().equals(query)
                    || book.getISBM().equals(query)
                    || book.getGenre().toLowerCase().contains(query.trim().toLowerCase())) {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }

    /*
     * This for loop iterates over the list of books in pairs.
     * It handles two books at a time (book1 and book2), ensuring that if there is an odd number
     * of books, the last book is displayed alone.
     */


    /*
     viewAllBooks method is responsible for showing the user books that have been passed through
     the parameter
     */
    public void viewAllBooks(ArrayList<Book> books)
    {
        ConsoleUtil.consoleClear();

        System.out.println("+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +");
        System.out.println("+                                Book collection                                  +");

        /*
        This for loop iterates over the list that has been passed through the parameter.
        it manages two books at the time (book1 and book2). This is because we want to have two columns outputted in a row
         */
        for(int i = 0; i < books.size(); i += 2)
        {
            Book book1 = books.get(i); // Get the first book which comes in the pair
            Book book2 = null; // Set the book2 to a null because there might not be a second book which prevents it from crashing

            // if there is a second book for the pair we want to get it
            if(i + 1 < books.size())
            {
                book2 = books.get(i + 1);
            }


            int spaceLength = 40;

            // Formatting and outputting the information about the books
            String header1 = ConsoleUtil.columnBoxHelper(String.valueOf(i + 1), 5);

            String header2 = "";
            if(book2 != null)
            {
                header2 = ConsoleUtil.columnBoxHelper(String.valueOf(i + 2), 5);
            }

            // If there is a pair for book1 we output an additional column for book2
            if(book2 != null)
            {
                System.out.println("+==( " + header1 + " )============================|=====( " + header2 + " )===========================+");
            }
            //Otherwise we just output one column which is for book1
            else
            {
                System.out.println("+==( " + header1 + " )============================|");
            }

            /*
            We format and output the following property.
            This is repeated for each of the books property that is outputted
             */
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

            System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book1.getAuthor(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book2.getAuthor(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book1.getGenre(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book2.getGenre(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| ISBM: " + book1.getISBM(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| ISBM: " + book2.getISBM(), spaceLength) + "  |");
            }
            else
            {
                System.out.print("|");
            }
            System.out.println();

            if(book1.getBorrowed())
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Not available", spaceLength));
            }
            else
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Available", spaceLength));
            }

            if(book2 != null)
            {
                if(book2.getBorrowed())
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

    /*
    A method responsible for removing the requested book from the csv file and the arrayList
    which stores book
     */
    public void removeABook()
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();
        ConsoleUtil.consoleClear();

        // If the books arrayList is empty there is no books that can be removed
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
            String bookId = ConsoleUtil.getUserInput("Book ID"); // Get what book the user wants to remove
            /*
            Iterate through each book which we have and if it matches the book id that the user entered
            remove it from the arrayList and csv file
             */
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
            if(bookFound == false) // if there isn't any book found that matches the bookid tell the user
            {
                ConsoleUtil.consoleClear();
                System.out.println("NOTE: Book can't be found. Either incorrect Book Id or it doesn't exist");
            }
        }
    }

    /*
    Method responsible for allowing the user (member) to take out an available book
     */
    public void takeOutBook(Member member)
    {
        ConsoleUtil.consoleClear();

        // get all of the available books
        ArrayList<Book> books = DataRepo.instance().getAllAvailableBooks();

        //Display all of the available books
        viewAllBooks(DataRepo.instance().getAllAvailableBooks());

        boolean hasUserBorrowedBook = false;
        // get todays date and add a month to it which gives us a return date for the book
        LocalDate returnByDate = LocalDate.now().plusMonths(1);

        // get the book that the user wants to borrow
        System.out.println("Please enter the book ID");
        String bookId = ConsoleUtil.getUserInput("Book ID");

        /*
        We iterate through each book and see if it matches the bookid that the user entered.
        We also double check that the book is not borrowed.
         */
        for(Book book : books)
        {
            if(book.getBookId().equals(bookId) && book.getBorrowed() == false)
            {
                /*
                When a book is found that matches the bookId.
                we set its borrowed property to true which means that it has been taken out.
                We also add it to an array responsible for storing borrowed Books and we append it to the
                borrowed.csv
                 */
                book.setBorrowed(true);
                BookRecord newBookRecord = new BookRecord(book, member, returnByDate);
                DataRepo.instance().addBookRecord(newBookRecord);
                hasUserBorrowedBook = true;
                break;
            }
        }

        if(hasUserBorrowedBook)
        {
            /*
             Once the user has borrowed the book we tell them the return date.
             Note this can be accessed through the borrowed books menu
             */
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book has been borrowed. The return date is (" + returnByDate + ")");
        }
        else
        {
            // if book not found we tell the user
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book not available or not found");
        }
    }


    /*
    A method which is responsible for returning a borrowed book from the user (member).
     */
    public void returnBook(Member member)
    {
        ConsoleUtil.consoleClear();
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();

        // This function is responsible for outputting all of the borrowed books for that specific user
        viewBorrowedBooks(member);

        ConsoleUtil.consoleClear(3);

        // Ask the user for what book they want to return
        System.out.println("Enter the book ID to return it");
        String bookId = ConsoleUtil.getUserInput("Enter: ");

        boolean bookFound = false;
        // Iterate through each book record - (a table which contains all of the borrowed books)
        for(BookRecord record : bookRecords)
        {
            // Check if ID and signed in member match the book borrowed
            if(record.getBook().getBookId().equals(bookId) && record.getMember().getId() == member.getId())
            {
                // set the property of borrowed to false as the book is returned
                record.getBook().setBorrowed(false);

                // We remove the specific book record which stored the borrowed book from the table
                DataRepo.instance().removeBookRecord(record);
                bookFound = true;
                ConsoleUtil.consoleClear();
                // we notify the user
                System.out.println("NOTE: Book has been returned");
                break;
            }
        }

        if(bookFound == false)
        {
            // If user enters in an incorrcet book id we tell the user that it coant be found
            ConsoleUtil.consoleClear();
            System.out.println("NOTE: Book not found. Incorrect book Id entered");
        }
    }

    /*
    A method which has two purposes.
    Member - shows a member the books they have borrowed.
    Admin -  shows an admin the books that have been borrowed from all members
     */
    public void viewBorrowedBooks(User user)
    {
        // get all of the book records
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();
        ConsoleUtil.consoleClear();

        /*
         if the passed user from the paremter is an instance of member meaning that he object was made /
         casted to member it will run the following if statement.
         */
        if(user instanceof Member)
        {
            System.out.println("+ - - - - - - - - | Borrowed Books | - - - - - - - - - - - - - -  + ");
            System.out.println("| Current date: " + LocalDate.now() + "                                        |");
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
            System.out.println("| BOOK ID  | BOOK NAME                  | RETURN BY               |");
            System.out.println("|----------|----------------------------|-------------------------|");

            // We iterate through each book record which matches the members id, format it and output it to the user
            for (BookRecord currentBookRecord : bookRecords)
            {
                if (currentBookRecord.getMember().getId() == user.getId())
                {
                    System.out.println(ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getBookId(), 11) +
                            ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getTitle(), 29) +
                            ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getReturnDate(), 25) + " |");
                    System.out.println("|----------|----------------------------|-------------------------|");
                }
            }
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
        }
        // Same thing what happens on the first if statement however this time if it is an instance of an admin
        else if(user instanceof Admin)
        {
            System.out.println("+ - - - - - - - - | Borrowed Books | - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  + ");
            System.out.println("| Current date: " + LocalDate.now() + "                                                                        |");
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
            System.out.println("| BOOK ID  | BOOK NAME                  | RETURN BY         | BORROWED BY              | User ID  |");
            System.out.println("|----------|----------------------------|-------------------|--------------------------|----------|");

            /*
             Iterate through each book Record and output it to the admin.
             This means they can see all of the borrowed books
             */
            for (BookRecord currentBookRecord : bookRecords)
            {
                String lastname = currentBookRecord.getMember().getLastname();
                String lastnameTrim = lastname.substring(0, lastname.length() - 1);

                System.out.println(ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getBookId(), 11) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getBook().getTitle(), 29) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getReturnDate(), 20) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getMember().getFirstname() + " " + lastnameTrim, 27) +
                        ConsoleUtil.columnBoxHelper("| " + currentBookRecord.getMember().getId(),10) + " |");
                System.out.println("|----------|----------------------------|-------------------|--------------------------|----------|");
            }
            System.out.println("| = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = |");
        }
    }
}
