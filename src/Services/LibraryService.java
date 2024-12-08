package Services;

import Modals.Member;
import Utils.*;

import java.util.ArrayList;
import java.util.Scanner;

public class LibraryService
{

    ArrayList<Member> members = new ArrayList<Member>();


    // User methods - - - - - - - - - - - - - -

    public void Initilise()
    {
        members = CsvService.getAllMembers();
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
                for (Member member : members) {
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
            id = 1;
        }
        else
        {
            id = members.get(members.size() - 1).id;
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

    public Member userLogin()
    {
        Scanner scanner = new Scanner(System.in);

        String username = ConsoleUtil.getUserInput("Username");
        String password = ConsoleUtil.getUserInput("Password");

        if(members.isEmpty())
        {
            return null;
        }

        for(Member member : members)
        {
            if (username.equals(member.getUsername()) && password.equals(member.getPassword())) {
                System.out.println("Logged in");

                return member;
            }
        }
        return null;
    }
}
