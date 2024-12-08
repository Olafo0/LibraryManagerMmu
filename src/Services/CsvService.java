package Services;

import Modals.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvService
{

    private final static String projectPath = System.getProperty("user.dir");

    private final static String userTablePath = Paths.get(projectPath, "src/Tables/member.csv").toString();


    // Get the list of all registered users from the member.csv
    public static ArrayList<Member> getAllMembers()
    {
        ArrayList<Member> fetchedMembers = new ArrayList<Member>();
        try
        {
            Scanner sc = new Scanner(new File(userTablePath));

            sc.useDelimiter("\n");

            // Skipping headers
            if(sc.hasNext())
            {
                sc.next();
            }
            else
            {
                System.out.println("No members found");
                return fetchedMembers;
            }

            while(sc.hasNext())
            {
                String[] fetchedUser = sc.next().split(",");

                int id = Integer.parseInt(fetchedUser[0]);
                String role = fetchedUser[1];
                String username = fetchedUser[2];
                String password = fetchedUser[3];
                String firstName = fetchedUser[4];
                String lastName = fetchedUser[5];

                fetchedMembers.add(new Member(id, role, username, password, firstName, lastName));
            }
            sc.close();

            return fetchedMembers;
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }

        return null;
    }

    public static void addNewMember(Member newUser)
    {

        File file = new File(userTablePath);

        try
        {
            if (file.exists())
            {
                System.out.println("File already exists");
            }
            else
            {
                System.out.println("Creating new file");
                file.createNewFile();
                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id,Role,Username,Password,Firstname,Lastname");
                fw.append("\n");

                fw.flush();
                fw.close();
            }
        }
        catch(IOException e)
        {
            System.out.println("Error creating file. ");
        }
        try
        {
            FileWriter fileWriter = new FileWriter(userTablePath, true);
            fileWriter.append(newUser.id + "," + newUser.Role + "," + newUser.Username + "," + newUser.Password + "," + newUser.Firstname + "," + newUser.Lastname);
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();

        }
        catch(IOException e)
        {
            System.out.println("Error appending to file. ");
        }

    }

}
