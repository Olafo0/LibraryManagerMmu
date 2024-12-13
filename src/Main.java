import Modals.Admin;
import Modals.Member;
import Modals.User;
import Services.LibraryService;
import Services.UserService;
import Utils.ConsoleUtil;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    private static LibraryService libraryService;
    private static UserService userService;

    public static void main(String[] args)
    {
        libraryService = new LibraryService();
        userService = new UserService();

        mainMenu();

    }

    public static void InitData()
    {

    }

    public static void mainMenu()
    {

        boolean isAppRunning = true;
        while(isAppRunning)
        {
            System.out.println("= * = * = | Library System | = * = * = ");
            System.out.println("1 - Login");
            System.out.println("2 - Register");
            System.out.println("3 - Exit");

            try
            {
                int choice = 0;
                System.out.print("Enter: ");
                Scanner scanner = new Scanner(System.in);
                choice = scanner.nextInt();
                // use enums
                switch (choice)
                {
                    case 1:
                        User validUser = userService.userLogin();
                        if (validUser != null)
                        {
                            userMainMenu(validUser);
                        }
                        else
                        {
                            ConsoleUtil.consoleClear();
                            System.out.println("NOTE: Username or Password incorrect");
                        }
                        break;
                    case 2:
                        System.out.println("Create account");
                        userService.createNewUser();
                        break;
                    case 3:
                        isAppRunning = false;
                        break;
                }
            }
            catch(InputMismatchException e)
            {
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-3");
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

        int choice = 0;
        do
        {
            try
            {
                System.out.println("= * = * = | Admin Menu | = * = * = ");
                System.out.println("1 - View books");
                System.out.println("2 - Add a new book");
                System.out.println("3 - Remove a book");
                System.out.println("4 - View book borrowings");
                System.out.println("5 - View Users");
                System.out.println("6 - Logout");
                System.out.println("7 - Quit Application");


                System.out.print("Enter: ");
                choice = scanner.nextInt();

                switch (choice)
                {
                    case 1:
                        libraryService.bookFilter();
                        break;
                    case 2:
                        libraryService.addNewBook();
                        break;
                    case 3:
                        libraryService.removeABook();
                        break;
                    case 4:
                        libraryService.viewBorrowedBooks(signedUser);
                        break;
                    case 5:
                        userService.viewAllUsers();
                        break;

                }
            }
            catch(InputMismatchException e)
            {
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-7");
                scanner.nextLine();
            }
        }
        while (choice != 6 && choice != 7);

        ConsoleUtil.consoleClear();
        if (choice == 7)
        {
            System.exit(0);
        }
    }


    public static void memberMenu(Member signedUser)
    {
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        do
        {
            try
            {
                System.out.println("= * = * = | Member Menu | = * = * = ");
                System.out.println("1 - View books");
                System.out.println("2 - View borrowed books");
                System.out.println("3 - Take out a book");
                System.out.println("4 - Return a book");
                System.out.println("5 - Logout");
                System.out.println("6 - Quit Application");


                System.out.print("Enter: ");
                choice = scanner.nextInt();

                switch (choice)
                {
                    case 1:
                        libraryService.bookFilter();
                        break;
                    case 2:
                        libraryService.viewBorrowedBooks(signedUser);
                        break;
                    case 3:
                        libraryService.takeOutBook(signedUser);
                        break;
                    case 4:
                        libraryService.returnBook(signedUser);
                        break;
                }
            }
            catch(InputMismatchException e)
            {
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-6");
                scanner.nextLine();
            }
        }
        while(choice != 5 && choice != 6);

        ConsoleUtil.consoleClear();
        if(choice == 6)
        {
            System.exit(0);
        }
    }
}