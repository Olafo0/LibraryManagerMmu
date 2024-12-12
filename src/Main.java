import Modals.Admin;
import Modals.Member;
import Modals.User;
import Services.LibraryService;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.Scanner;

public class Main
{
    private static LibraryService library;

    public static void main(String[] args)
    {
        library = new LibraryService();
        // Read table data
        library.Initilise();

        // Prepare program

        // Load login menu

        mainMenu(library);

    }

    public static void InitData()
    {

    }

    public static void mainMenu(LibraryService library)
    {

        boolean isAppRunning = true;
        while(isAppRunning)
        {
            System.out.println("= * = * = | Library System | = * = * = ");
            System.out.println("1 - Login");
            System.out.println("2 - Register");
            System.out.println("3 - Exit");

            System.out.print("Enter: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            // use enums
            switch(choice)
            {
                case 1:
                    System.out.println("Login");
                    User validUser = library.userLogin();
                    if(validUser != null)
                    {
                        // log user
                        userMainMenu(validUser);
                    }
                    else
                    {
                        System.out.println("Invalid User");
                    }
                    break;
                case 2:
                    System.out.println("Create account");
                    library.createNewUser();
                    break;
                case 3:
                    isAppRunning = false;
                    break;
            }
        }
    }

    public static void userMainMenu(User signedUser)
    {
        if(signedUser.getRole().equals("Admin"))
        {
            adminMenu((Admin) signedUser);
        }
        else if(signedUser.getRole().equals("Member"))
        {
            memberMenu((Member) signedUser);
        }
        else
        {
            System.out.println("Invalid Role");
        }
    }

    public static void adminMenu(Admin signedUser)
    {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do
        {
            System.out.println("= * = * = | Admin Menu | = * = * = ");
            System.out.println("1 - View all books");
            System.out.println("2 - Add a new book");
            System.out.println("3 - Remove a book");
            System.out.println("4 - View book borrowings");
            System.out.println("5 - View Users");
            System.out.println("6 - Logout");
            System.out.println("7 - Quit Application");


            System.out.print("Enter: ");
            choice = scanner.nextInt();

            switch(choice)
            {
                case 1:
                    library.viewAllBooks();
                    break;
                case 2:
                    library.addNewBook();
                    break;
                case 3:
                    library.removeABook();
                    break;
                case 4:
                    library.viewBorrowedBooks(signedUser);
                    break;
                case 5:
                    library.viewAllUsers();
                    break;
            }
        }
        while(choice != 6 && choice != 7);

        if(choice == 7 )
        {
            System.exit(0);
        }
    }

    public static void memberMenu(Member signedUser)
    {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do
        {
            System.out.println("= * = * = | Member Menu | = * = * = ");
            System.out.println("1 - View all books");
            System.out.println("2 - View borrowed books");
            System.out.println("3 - Take out a book");
            System.out.println("4 - Return a book");
            System.out.println("5 - Logout");
            System.out.println("6 - Quit Application");


            System.out.print("Enter: ");
            choice = scanner.nextInt();

            switch(choice)
            {
                case 1:
                    library.viewAllBooks();
                    break;
                case 2:
                    library.viewBorrowedBooks(signedUser);
                    break;
                case 3:
                    library.takeOutBook(signedUser);
                    break;
                case 4:
                    library.returnBook(signedUser);
                    break;
            }
        }
        while(choice != 5 && choice != 6);

        if(choice == 6)
        {
            System.exit(0);
        }

    }
}