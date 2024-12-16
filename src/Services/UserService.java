package Services;

import Data.DataRepo;
import Modals.Admin;
import Modals.Member;
import Modals.User;
import Utils.ConsoleUtil;
import Utils.PasswordUtil;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class UserService
{
    ArrayList<User> users = new ArrayList<User>();


    /*
    Method responsible for displaying users stored in the member.csv.
    User can apply filters to return desired members
     */
    public void viewAllUsers()
    {
        int choice = 0;
        String query = "";

        /*
        A do while loop which keeps repeating itself unless the user wants the exit it.
         */
        do
        {
            System.out.println(" + - - - | All users | - - - + ");
            System.out.println("| Role    | Username                    | Firstname                   | Lastname                    |");

            /*
             Function responsible for getting users depending on the choice and query.
             When this is run for the first time we show the user the entire user list stored in our user array
             */
            ArrayList<User> memberToIterate = userFilter(choice, query);

            // prints out the user in a nice row
            for (User user : memberToIterate)
            {
                System.out.println(ConsoleUtil.columnBoxHelper("| " + user.getRole(), 10) + ConsoleUtil.columnBoxHelper("| " + user.getUsername(), 30) + ConsoleUtil.columnBoxHelper("| " + user.getFirstname(), 30) + ConsoleUtil.columnBoxHelper("| " + user.getLastname(), 30) + "|");
            }

            // Method which prints out two new lines
            ConsoleUtil.consoleClear(2);

            // printing out the possible filters to the user
            System.out.println(" | = Filter = = = = = = = = = = = = = = = | ");
            System.out.println(" 1 - View all members");
            System.out.println(" 2 - View all admins");
            System.out.println(" 3 - Custom Search");
            System.out.println(" 4 - Exit");

            // getting the user input and ensuring that it is an integer. If not we tell the user
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
                // gets the user input and when the loop repeats itself it passes the query through the parameter
                System.out.println("You can enter a Username, Firstname, or even a Lastname");
                query = ConsoleUtil.getUserInput("Enter");
            }
            else
            {
                query = "";
            }
        }
        // if the choice is 4 stops the while loop and returns the user to the admin menu
        while(choice != 4);
    }


    /*
    Function responsible for retrieving the correct users to show depending on the admins choice and query.
    This function is used for viewAllUsers method
     */
    public ArrayList<User> userFilter(int filterBy, String query)
    {
        // we get all the users
        ArrayList<User> users = DataRepo.instance().getAllUsers();

        /*
         this arrayList will contain all of the desired users from our filter and query.
         Which will be returned at the end so it can be printed out
         */
        ArrayList<User> memberToIterate = new ArrayList<>();

        // Filter 0 - all users
        /*
        memberToIterate is assigned all of the users from our main user array
         */
        if(filterBy == 0)
        {
            memberToIterate = users;
        }
        // Filter 1 - All members
        /*
        if role equals to member add it to memberToIterate.
        This means that this array will contain only members
         */
        else if(filterBy == 1)
        {
            for(User user : users)
            {
                if(user.getRole().equals("Member"))
                {
                    memberToIterate.add(user);
                }
            }
        }
        // Filter 1 - All admins
        /*
        if role equals to admin add it to memberToIterate.
        This means that this array will contain only admins
         */
        else if(filterBy == 2)
        {
            for(User user : users)
            {
                if(user.getRole().equals("Admin"))
                {
                    memberToIterate.add(user);
                }
            }
        }
        // Filter 3 - custom search
        /*
        We get any user which matches the entered query from the user
         */
        else if(filterBy == 3)
        {
            for(User user : users)
            {
                if(user.getUsername().toLowerCase().contains(query.toLowerCase())
                        || user.getFirstname().toLowerCase().contains(query.toLowerCase())
                        || user.getLastname().toLowerCase().contains(query.toLowerCase()))
                {
                    memberToIterate.add(user);
                }
            }
        }

        // return it so it can be printed out
        return memberToIterate;

    }

    /*
    Method responsible for creating a new user.
    User is prompted to enter necessary details to create an account
     */
    public void createNewUser()
    {
        ConsoleUtil.consoleClear();
        // Getting all of the current users which will be used to verify unique usernames
        ArrayList<User> users = DataRepo.instance().getAllUsers();

        ConsoleUtil.consoleClear();

        // a boolean which is used to determine whether the user wants to create a admin account
        boolean adminAllow = false;

        try
        {
            // ask the user if they want to create an admin account
            System.out.println("Do you want to create a admin account (y/n)");
            String userRoleChoice = ConsoleUtil.getUserInput("Enter").toLowerCase();

            //if they do they need to match the secret code otherwise it will create a normal member account
            if (userRoleChoice.equals("y"))
            {
                //Code to create an Admin account
                System.out.println("Code is required for Admin");
                int secretCode = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));
                if (secretCode == 331)
                {
                    adminAllow = true;
                } else
                {
                    System.out.print("Incorrect, good luck next time.");
                }

            }
        }
        // Ensuring that the input is a number
        catch (NumberFormatException e)
        {
            System.out.println("ERROR: Expecting a number.");
        }

        // First section - we get personal details
        System.out.println(" = = = | Creating new account | = = = ");
        System.out.println(" * Personal details 1 of 2 ");
        System.out.println(" NOTE: All details are required");

        String firstName = ConsoleUtil.getUserInput("First name");

        String lastName = ConsoleUtil.getUserInput("Last name");

        String institution = ConsoleUtil.getUserInput("Institution");

        ConsoleUtil.consoleClear();

        // Second section - we asking for a unique username and a password
        System.out.println(" * Create account 2 of 2 ");


        // Making sure that username provided by the user is unique
        String username;
        boolean isUsernameValid;
        do
        {
            // all usernames are valid at the beginning
            isUsernameValid = true;

            // get the username from the user
            username = ConsoleUtil.getUserInput("Username");


            /*
             if user is not empty.
             we check if there are any taken usernames
             */
            if(users.isEmpty() == false)
            {
                for (User member : users) {
                    // if username is taken we tell the user and prompt them to think of a new one
                    if (username.toLowerCase().equals(member.getUsername().toLowerCase())) {
                        isUsernameValid = false;
                        System.out.println("Username has already been taken");
                    }
                }
            }
        }
        while(isUsernameValid == false);


        // Password section
        boolean isPasswordValid = false;
        String password;

        // Tell the user the password requirements
        System.out.println(" = * = * | Password criteria | * = * =");
        System.out.println(" - At least 5 characters long");
        System.out.println(" - Contains a numerical character");
        System.out.println(" - Contains a special character");
        System.out.println(" = * = * = = = = = = = = = = = * = * =");
        do
        {
            /*
            Get the password and run a methof which verifies that it meets the requirements.
            If it doesn't meet some, it will only tell the user which ones it doesn't meet
             */
            password = ConsoleUtil.getUserInput("Password");
            isPasswordValid = PasswordUtil.PasswordCheck(password);

        }
        // This is repeated while password is not valid
        while(isPasswordValid == false);


        /*
        We give the user a unique id
         */
        int id;
        if(users.isEmpty())
        {
            id = 1;
        }
        else
        {
            // gets the last user from the array and adds a 1
            id = users.getLast().getId() + 1;
        }


        /*
         when creating a new user the user role is Member by default.
         If the user code the secret code corrcet it assigns adminAllowed to true
         so when it comes to this part it will put the user role as admin
         */
        String userRole = "Member";
        // we create a new object and use the datarepo to append it to tbe member.csv and user list
        if(adminAllow)
        {
            userRole = "Admin";
            Admin newUser = new Admin(id, userRole, username, password, firstName, lastName);
            DataRepo.instance().addUser(newUser);
        }
        else
        {
            Member newUser = new Member(id, userRole, username, password, firstName, lastName);
            DataRepo.instance().addUser(newUser);
        }

        System.out.println("Your new account has been created");
        System.out.println("Please log in");
        ConsoleUtil.consoleClear();
    }


    /*
    Method responsible for logging in the user to the main application.
    This method gets the username and password which is then checked to ensure it matches the one from user arrayList
    if it matches it returns the user object that has loggeed in
     */
    public User userLogin()
    {
        ConsoleUtil.consoleClear();
        System.out.println("+ - - - - - - | Login | - - - - - - +");

        //get all of the users
        ArrayList<User> users = DataRepo.instance().getAllUsers();


        // get the username and password from the user
        String username = ConsoleUtil.getUserInput("Username");
        String password = ConsoleUtil.getUserInput("Password");


        // If there are no users return null
        if(users.isEmpty())
        {
            return null;
        }

        // iterate through each user in the user arraylist
        for(User user : users)
        {
            // If the username and password match return a new object based on the users role
            if (username.equals(user.getUsername()) && password.equals(user.getPassword()))
            {
                ConsoleUtil.consoleClear();
                System.out.println("Logged in");

                if(user.getRole().equals("Member"))
                {
                    return (new Member(user.getId(), user.getRole() ,user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname()));
                }
                else if(user.getRole().equals("Admin"))
                {
                    return(new Admin(user.getId(), user.getRole() ,user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname()));
                }
            }
        }
        // if no user is found return null
        return null;
    }
}
