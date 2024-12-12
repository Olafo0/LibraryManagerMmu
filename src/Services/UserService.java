package Services;

import Data.DataRepo;
import Modals.Admin;
import Modals.Member;
import Modals.User;
import Utils.ConsoleUtil;
import Utils.PasswordUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class UserService
{
    ArrayList<User> users = new ArrayList<User>();

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
        ArrayList<User> users = DataRepo.instance().getAllUsers();
        ArrayList<User> memberToIterate = new ArrayList<>();
        if(filterBy == 0)
        {
            memberToIterate = users;
        }
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
        else if(filterBy == 3)
        {
            for(User user : users)
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
        ArrayList<User> users = DataRepo.instance().getAllUsers();
        Scanner scanner = new Scanner(System.in);

        ConsoleUtil.consoleClear();

        boolean adminAllow = false;
        System.out.print("Do you want to create a admin account (y/n");
        String userRoleChoice = ConsoleUtil.getUserInput("Enter").toLowerCase();
        if(userRoleChoice.equals("y"))
        {
            //Code to create an Admin account
            System.out.print("Code is required for Admin");
            int secretCode = Integer.parseInt(ConsoleUtil.getUserInput("Enter"));
            if(secretCode == 331)
            {
                adminAllow = true;
            }
            else
            {
                System.out.print("Incorrect, good luck next time.");
            }

        }

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

            if(users.isEmpty() == false)
            {
                for (User member : users) {
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
        if(users.isEmpty())
        {
            System.out.println("We here cuzz");
            id = 1;
        }
        else
        {
            id = users.getLast().getId() + 1;
        }
        String userRole = "Member";
        if(adminAllow)
        {
            userRole = "Admin";
        }

        Member newUser = new Member(id, userRole, username, password, firstName, lastName);
        DataRepo.instance().addUser(newUser);

        // Add new member to a CSV file
//        CsvService.addNewMember(newUser);
        System.out.println("Your new account has been created");
        System.out.println("Please log in");
        ConsoleUtil.consoleClear();
    }

    public User userLogin()
    {
        ArrayList<User> users = DataRepo.instance().getAllUsers();
        Scanner scanner = new Scanner(System.in);

        String username = ConsoleUtil.getUserInput("Username");
        String password = ConsoleUtil.getUserInput("Password");

        if(users.isEmpty())
        {
            return null;
        }

        for(User member : users)
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
}
