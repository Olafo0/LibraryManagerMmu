package Services;

import Modals.*;
import Utils.*;

import java.awt.color.ICC_ColorSpace;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LibraryService
{

    ArrayList<User> members = new ArrayList<User>();
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayList<BookRecord> bookRecords = new ArrayList<BookRecord>();


    // () () () User methods - - - - - - - - - - - - - -
    public void Initilise()
    {
        members = CsvService.getAllMembers();
        books = CsvService.getAllBooks();
        bookRecords = CsvService.getAllBookBorrows(books, members);
        System.out.println("Loaded");
    }


    public void viewAllUsers()
    {
        int choice = 0;
        String query = "";
        do
        {
            System.out.println(" + - - - | All users | - - - + ");
            System.out.println("| Role    | Username                      |");
            // Function responsible for printing out
            userFilter(choice, query);

            ConsoleUtil.consoleClear(2);
            System.out.println(" | = Filter = = = = = = = = = = = = = = = | ");
            System.out.println(" 1 - View all members");
            System.out.println(" 2 - View all admins");
            System.out.println(" 3 - Custom Search");
            System.out.println(" 4 - Exit");

            try
            {
                choice = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));
            }
            catch (Exception e)
            {
                System.out.println("ERROR: Invalid input, please enter a number between 1 and 4");
            }


            if(choice == 3)
            {
                System.out.println("You can enter a Username, Role, Firstname, or even a Lastname");
                query = ConsoleUtil.getUserInput("Enter");
            }
            else
            {
                query = "";
            }
        }
        while(choice != 4);
    }


    public void userFilter(int filterBy, String query)
    {
        ArrayList<User> memberToIterate = new ArrayList<>();
        if(filterBy == 0)
        {
            memberToIterate = members;
        }
        else if(filterBy == 1)
        {
            for(User user : members)
            {
                if(user.getRole().equals("Member"))
                {
                    memberToIterate.add(user);
                }
            }
        }
        else if(filterBy == 2)
        {
            for(User user : members)
            {
                if(user.getRole().equals("Admin"))
                {
                    memberToIterate.add(user);
                }
            }
        }
        else if(filterBy == 3)
        {
            for(User user : members)
            {
                if(user.getUsername().toLowerCase().contains(query.toLowerCase()) || user.getRole().toLowerCase().contains(query.toLowerCase())
                        || user.getFirstname().toLowerCase().contains(query.toLowerCase())
                        || user.getLastname().toLowerCase().contains(query.toLowerCase()))
                {
                    memberToIterate.add(user);
                }
            }
        }

        for (User user : memberToIterate)
        {
            System.out.println(ConsoleUtil.columnBoxHelper("| " + user.getRole(), 10) + ConsoleUtil.columnBoxHelper("| " + user.getUsername(), 30) + "|");
        }
    }

    public void createNewUser()
    {
        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.consoleClear();

        // First section
        System.out.println(" = = = | Creating new account | = = = ");
        System.out.println(" * Personal details 1 of 2 ");
        System.out.println(" NOTE: All details are required");

        String firstName = ConsoleUtil.getUserInput("First name");

        String lastName = ConsoleUtil.getUserInput("Last name");

        String institution = ConsoleUtil.getUserInput("Institution");

        System.out.println();
        System.out.println();


        ConsoleUtil.consoleClear();
        // Second section
        System.out.println(" * Create account 2 of 2 ");
        // Making sure that usernames are unique
        String username;
        boolean isUsernameValid;
        do
        {
            isUsernameValid = true;

            System.out.print("Username: ");
            username = scanner.nextLine();

            if(members.isEmpty() == false)
            {
                for (User member : members) {
                    if (username.equals(member.getUsername())) {
                        isUsernameValid = false;
                        System.out.println("Username has already been taken");
                    }
                }
            }
        }
        while(isUsernameValid == false);

        boolean isPasswordValid = false;
        String password;

        // Sort out their order
        System.out.println(" = * = * | Password criteria | * = * =");
        System.out.println(" - At least 5 characters long");
        System.out.println(" - Contains a numerical character");
        System.out.println(" - Contains a special character");
        System.out.println(" = * = * = = = = = = = = = = = * = * =");
        do
        {
            System.out.print("Password: ");
            password = scanner.nextLine();
            isPasswordValid = PasswordUtil.PasswordCheck(password);

        }
        while(isPasswordValid == false);

        int id;
        if(members.isEmpty())
        {
            System.out.println("We here cuzz");
            id = 1;
        }
        else
        {
            id = members.getLast().getId() + 1;
        }
        System.out.println("ID NUMBER: " + id);
        Member newUser = new Member(id, "Member", username, password, firstName, lastName);
        members.add(newUser);

        // Add new member to a CSV file
        CsvService.addNewMember(newUser);
        System.out.println("Your new account has been created");
        System.out.println("Please log in");
        ConsoleUtil.consoleClear();
    }

    public User userLogin()
    {
        Scanner scanner = new Scanner(System.in);

        String username = ConsoleUtil.getUserInput("Username");
        String password = ConsoleUtil.getUserInput("Password");

        if(members.isEmpty())
        {
            return null;
        }

        for(User member : members)
        {
            if (username.equals(member.getUsername()) && password.equals(member.getPassword()))
            {
                System.out.println("Logged in");

                if(member.getRole().equals("Member"))
                {
                    return (new Member(member.id, member.Role ,member.Username, member.Password, member.Firstname, member.Lastname));
                }
                else if(member.getRole().equals("Admin"))
                {
                    return(new Admin(member.id, member.Role ,member.Username, member.Password, member.Firstname, member.Lastname));
                }
            }
        }
        return null;
    }


    // () () () Library methods - - - - - - - - - - - - - -

    public void addNewBook()
    {
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
        books.add(newBook);
        CsvService.addNewBook(newBook);
        System.out.println("Your new book has been added to the system");

    }

    public void viewAllBooks()
    {
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
                if(book1.Borrowed)
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
                    books.remove(book);
                    CsvService.removeABook(books);
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
                bookRecords.add(newBookRecord);
                CsvService.insertNewBookRecord(newBookRecord);
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
                bookRecords.remove(record);
                CsvService.removeBookRecord(bookRecords);
                System.out.println("Book has been returned");
                break;
            }
        }
    }

    public void viewBorrowedBooks(User member)
    {
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
