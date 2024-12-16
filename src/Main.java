import Modals.Admin;
import Modals.Member;
import Modals.User;
import Services.LibraryService;
import Services.UserService;
import Utils.ConsoleUtil;

import java.util.Scanner;

public class Main
{
    private static LibraryService libraryService;
    private static UserService userService;


    /*
    Main method
    Library Service - Responsible for handling everything library related
    User Service - Responsible for everything user related
    Contains methods for specific functions that the program offers
    boots up the main menu for the user
     */
    public static void main(String[] args)
    {
        // Creating an instance of the services which we will use
        libraryService = new LibraryService();
        userService = new UserService();

        // Main menu of the application
        mainMenu();
    }

    // Main menu which is shown when the application is run
    public static void mainMenu()
    {
        /*
        A loop which basically is responsible for keeping the application running.
        This loop keeps repeating itself until the user decides to quit the app
         */
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
                // Getting the users input and converting it to an int
                choice = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));

                /*
                Using a switch case statement to determine which
                function to run depending on the user input
                 */
                switch (choice)
                {
                    // Case 1 - Login
                    case 1:
                        /*
                        Running the userLogin function which is responsible checking and returning a valid
                        user depending on the credentials provided.

                        The function asks for user input for their username and password
                         */
                        User validUser = userService.userLogin();

                        /*
                         If the user is not null meaning that provided credentials match
                         the ones in the csv file boots up the userMainMenu
                         */
                        if (validUser != null)
                        {
                            userMainMenu(validUser);
                        }
                        /*
                        if validUser is null that means that entered credentials don't match
                        any in the csv file. We return a message to the user telling them that
                        either username or password is wrong
                        */
                        else
                        {
                            ConsoleUtil.consoleClear();
                            System.out.println("NOTE: Username or Password incorrect");
                        }
                        break;

                     // Case 2 - Create an account
                    case 2:
                        System.out.println("Create account");
                        // Function responsible for creating a new user
                        userService.createNewUser();
                        break;


                    // Case 3 - Quit the app
                    case 3:
                        /*
                         when appRunning is false this mean the while loop will finish running
                        resulting the app to quit
                         */
                        isAppRunning = false;
                        break;
                }
            }
            /*
             Prevents the app from crashing if user enters in an char or a string
             on line 58 which is the input for getting what section of program they want to run
             */
            catch(NumberFormatException e)
            {
                // A function which clears the console
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-3");
            }
        }
    }

    /*
     This function is responsible for deciding what menu to show to the user depending on their role.
     */
    public static void userMainMenu(User signedUser)
    {
        // Getting the user roles and then casting the signedUser which is passed from mainMenu
        if(signedUser.getRole().equals("Admin"))
        {
            // If user is an admin we run the admin menu and  cast the signed user to admin
            adminMenu((Admin) signedUser);
        }
        else if(signedUser.getRole().equals("Member"))
        {
            // If user is a member we run the member menu and cast the signed user to member
            memberMenu((Member) signedUser);
        }
        else
        {
            // If we fail to identify what role the user have output this error message
            System.out.println("ERROR: Invalid Role");
        }
    }

    // The Admin menu
    /*
    This adminMenu allows Admins to access different parts of the program with some additional functionalities compared to the
    member one
     */
    public static void adminMenu(Admin signedUser)
    {
        Scanner scanner = new Scanner(System.in);

        //
        int choice = 0;
        do
        {
            try
            {
                /*
                 The admin menu is in a do while loop because when a function stops running, for example, like view books
                 rather than going back to the main menu which will require the user to login again it simply shows
                 the member menu again
                */
                System.out.println("= * = * = | Admin Menu | = * = * = ");
                System.out.println("1 - View books");
                System.out.println("2 - Add a new book");
                System.out.println("3 - Remove a book");
                System.out.println("4 - View book borrowings");
                System.out.println("5 - View Users");
                System.out.println("6 - Logout");
                System.out.println("7 - Quit Application");


                choice = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));

                switch (choice)
                {
                    case 1:
                        libraryService.booksToView();
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
            // Preventing the program from crashing
            catch(NumberFormatException e)
            {
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-7");
            }
        }
        /*
         if the choice is not equals to 6 or 7 will keep repeating the do while loop
         to prevent it from going back to the starter screen
         */
        while (choice != 6 && choice != 7);

        ConsoleUtil.consoleClear();
        if (choice == 7)
        {
            System.exit(0);
        }
    }


    // The member menu
    /*
    This memberMenu allows members to access different parts of the program which it offers
     */
    public static void memberMenu(Member signedUser)
    {
        // Initilising scanner
        Scanner scanner = new Scanner(System.in);

        // Initilising choice so it is not empty

        /*
          Same concept as the admin menu
         */
        int choice = 0;
        do
        {
            try
            {
                // Showing the user their menu
                System.out.println("= * = * = | Member Menu | = * = * = ");
                System.out.println("1 - View books");
                System.out.println("2 - View borrowed books");
                System.out.println("3 - Take out a book");
                System.out.println("4 - Return a book");
                System.out.println("5 - Logout");
                System.out.println("6 - Quit Application");

                // Gets the user input of what section of the program they want to access
                choice = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));

                switch (choice)
                {
                    // Case 1 - Viewing the book collection
                    case 1:
                        // runs the booksToView which shows the entire book collection with additional functionality
                        libraryService.booksToView();
                        break;
                    case 2:
                        // Allows the user to check their borrowed books
                        libraryService.viewBorrowedBooks(signedUser);
                        break;
                    case 3:
                        // Allows the user to take out an available book
                        libraryService.takeOutBook(signedUser);
                        break;
                    case 4:
                        // Allows the user to return a book which they have borrowed
                        libraryService.returnBook(signedUser);
                        break;
                }
            }
            // Prevents the program from crashing if they enter in a value on line 238 that is not a number
            catch(NumberFormatException e)
            {
                ConsoleUtil.consoleClear();
                System.out.println("ERROR: Expecting a number. Please choose an option between 1-6");
            }
        }
        // if the choice is not equals to 5 or 6 will keep repeating the do while loop
        while(choice != 5 && choice != 6);

        ConsoleUtil.consoleClear();

        // If the user entered the choice 6 it will exit the program meaning it will show itself down
        if(choice == 6)
        {
            System.exit(0);
        }
    }
}