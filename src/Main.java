import Services.LibraryService;

import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        LibraryService library = new LibraryService();
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
                    boolean validUser = library.userLogin();
                    if(validUser)
                    {
                        // log user
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
}