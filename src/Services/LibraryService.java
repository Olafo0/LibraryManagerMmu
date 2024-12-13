package Services;

import Data.DataRepo;
import Modals.*;
import Utils.*;

import java.awt.color.ICC_ColorSpace;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LibraryService
{

    private DataRepo repository;

    public LibraryService()
    {
        DataRepo.instance();
    }



    // () () () Library methods - - - - - - - - - - - - - -

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
        do
        {
            bookIdTaken = false;
            int randomIdNumber = random.nextInt(10);

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

        Book newBook = new Book(String.valueOf(newBookid) ,bookTitle, bookAuthor, bookISBM, false, bookGenre);
        DataRepo.instance().addBook(newBook);

        System.out.println("Your new book has been added to the system");

    }

    public void viewAllBooks()
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();

        for(int i = 0; i < books.size(); i += 2)
        {
            Book book1 = books.get(i);

            Book book2 = null;
            if(i + 1 < books.size())
            {
                book2 = books.get(i + 1);
            }

            int spaceLength = 40;
            System.out.println("+---------------------------------------------+");

            System.out.print(ConsoleUtil.columnBoxHelper("| Book ID: " + book1.getBookId(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Book Id: " + book2.getBookId(), spaceLength));
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Title: " + book1.getTitle(), spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Title: " + book2.getTitle(), spaceLength));
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book1.Author, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Author: " + book2.getAuthor(), spaceLength));
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book1.Genre, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| Genre: " + book2.getGenre(), spaceLength));
            }
            System.out.println();

            System.out.print(ConsoleUtil.columnBoxHelper( "| ISBM: " + book1.ISBM, spaceLength));
            if(book2 != null)
            {
                System.out.print(ConsoleUtil.columnBoxHelper("| ISBM: "+ book2.getISBM(), spaceLength));
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
                    System.out.print(ConsoleUtil.columnBoxHelper("| Availability: Available", spaceLength));
                }
            }

            System.out.println();
            System.out.println("+---------------------------------------------+");
        }
    }

    public void removeABook()
    {
        ArrayList<Book> books = DataRepo.instance().getAllBooks();

        if(books.isEmpty())
        {
            System.out.println("No books to remove");
        }
        else
        {
            String bookId = ConsoleUtil.getUserInput("Book ID");
            for (Book book : books)
            {
                if (book.getBookId().equals(bookId))
                {
//                    books.remove(book);
//                    CsvService.removeABook(books);
                    DataRepo.instance().removeBook(book);
                    System.out.println("Book removed");
                } else
                {
                    System.out.println("Book not found");
                }
            }
        }
    }


    public void takeOutBook(Member member)
    {

        ArrayList<Book> books = DataRepo.instance().getAllBooks();
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();

        viewAllBooks();

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
//                CsvService.insertNewBookRecord(newBookRecord);
                hasUserBorrowedBook = true;
                break;
            }
        }

        if(hasUserBorrowedBook)
        {
            System.out.println("Book has been borrowed. The return date is (" + returnByDate + ")");
        }
        else
        {
            System.out.println("Book not available or not found");
        }
    }


    public void returnBook(Member member)
    {
        ArrayList<BookRecord> bookRecords = DataRepo.instance().getAllBookRecords();

        viewBorrowedBooks(member);

        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.consoleClear(3);
        System.out.println("Enter the book ID to return it");
        String bookId = ConsoleUtil.getUserInput("Enter: ");

        for(BookRecord record : bookRecords)
        {
            // Check if ID and signed in member match the book borrowed
            if(record.getBook().getBookId().equals(bookId) && record.getMember().getId() == member.getId())
            {
                record.getBook().setBorrowed(false);
                DataRepo.instance().removeBookRecord(record);
                System.out.println("Book has been returned");
                break;
            }
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
