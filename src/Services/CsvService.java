package Services;

import Modals.Book;
import Modals.BookRecord;
import Modals.Member;
import Modals.User;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class CsvService
{
    // Paths to the different CSVs that are used
    private final static String projectPath = System.getProperty("user.dir");
    private final static String userTablePath = Paths.get(projectPath, "src/Tables/member.csv").toString();
    private final static String bookTablePath = Paths.get(projectPath, "src/Tables/book.csv").toString();
    private final static String borrowTablePath = Paths.get(projectPath, "src/Tables/borrowed.csv").toString();

    // Get the list of all registered users from the member.csv
    public static ArrayList<User> getAllMembers()
    {
        ArrayList<User> fetchedMembers = new ArrayList<User>();
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
                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
                fw.append("\n");

                fw.flush();
                fw.close();

                System.out.println("No members found");
                return fetchedMembers;
            }

            while(sc.hasNext())
            {
                String[] fetchedUser = sc.next().split(";");

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
            try
            {
                System.out.println("= = = = = = = = = = = = = = = = =");
                System.out.println("ERROR: File not found");
                System.out.println("Resolving issue...");

                File file = new File(userTablePath);
                file.createNewFile();

                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
                fw.append("\n");

                fw.flush();
                fw.close();

                if(file.exists())
                {
                    System.out.println("Issue resolved");
                }
                else
                {
                    System.out.println("Failed");
                }
                System.out.println("= = = = = = = = = = = = = = = = =");
            }
            catch(IOException ex)
            {
                System.out.println("ERROR: Issues in creating file");
                System.out.println("More information: " + ex.getMessage());
            }

            return fetchedMembers;
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        return fetchedMembers;
    }


    public static ArrayList<Book> getAllBooks()
    {
        ArrayList<Book> fetchedBooks = new ArrayList<>();

        try
        {
            Scanner scanner = new Scanner(new File(bookTablePath));
            scanner.useDelimiter("\n");

            // Skipping headers
            if (scanner.hasNext())
            {
                scanner.next();
            }
            else
            {
                FileWriter fw = new FileWriter(bookTablePath);
                fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                fw.append("\n");

                fw.flush();
                fw.close();

                System.out.println("No books found");
                return fetchedBooks;
            }

            while (scanner.hasNext())
            {
                String[] fetchedBook = scanner.next().split(";");

                String bookId = fetchedBook[0];
                String title = fetchedBook[1];
                String author = fetchedBook[2];
                String ISBM = fetchedBook[3];
                Boolean isBorrowed = fetchedBook[4] != null ? Boolean.parseBoolean(fetchedBook[4]) : false;
                String genre = fetchedBook[5];

                fetchedBooks.add(new Book(bookId, title, author, ISBM, isBorrowed, genre));
            }

            scanner.close();

            return fetchedBooks;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("= = = = = = = = = = = = = = = =");
            try
            {
                System.out.println("ERROR: File not found");
                System.out.println("Resolving issue...");
                File f = new File(bookTablePath);

                f.createNewFile();

                FileWriter fw = new FileWriter(bookTablePath);
                fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                fw.append("\n");

                fw.flush();
                fw.close();

                if (f.exists())
                {
                    System.out.println("Issue resolved..");
                }
                else
                {
                    System.out.println("Failed");
                }
            }
            catch (IOException ex)
            {
                System.out.println("ERROR: Issue with creating file.");
                System.out.println("More information: " + ex.getMessage());
            }
            System.out.println("= = = = = = = = = = = = = = = =");

            return fetchedBooks;
        }
        catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        return fetchedBooks;
    }

    public static void addNewMember(User newUser)
    {

        File file = new File(userTablePath);

        try
        {
            if (file.exists())
            {
                // Ensures that the csv file has column headers
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                br.close();

                if(firstLine == null)
                {
                    FileWriter fw = new FileWriter(file, true); // Open in append mode
                    fw.append("Id; Role; Username; Password; Firstname; Lastname");
                    fw.append("\n");
                    fw.flush();
                    fw.close();
                }

            }
            else
            {
                System.out.println("Creating new file");
                file.createNewFile();
                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
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
            fileWriter.append(newUser.id + ";" + newUser.Role + ";" + newUser.Username + ";" + newUser.Password + ";" + newUser.Firstname + ";" + newUser.Lastname);
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();

        }
        catch(IOException e)
        {
            System.out.println("Error appending to file. ");
        }

    }

    public static void addNewBook(Book newBook)
    {
        File file = new File(bookTablePath);
        try
        {
            if(file.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                br.close();

                if(firstLine == null)
                {
                    FileWriter fw = new FileWriter(file, true);
                    fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                    fw.append("\n");
                    fw.flush();
                    fw.close();
                }
            }
            else if(file.exists() == false)
            {
                System.out.println("Creating a new file");
                file.createNewFile();
                FileWriter fw = new FileWriter(bookTablePath);
                fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
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
            FileWriter fw = new FileWriter(bookTablePath, true);
            fw.append(newBook.getBookId() + ";" + newBook.Title + ";" + newBook.Author + ";" + newBook.ISBM + ";" + newBook.Borrowed + ";" + newBook.Genre);
            fw.append("\n");
            fw.flush();
            fw.close();
        }
        catch(IOException e)
        {
            System.out.println("Error accessing file. ");
        }

    }

    public static void removeABook(ArrayList<Book> newBookList)
    {

        // Clearing the file
        try
        {
            new FileWriter(userTablePath, false).close();
        }
        catch(IOException e)
        {
            System.out.println("Error closing file. ");
        }
        // Writing to the file
        try
        {
            FileWriter fw = new FileWriter(bookTablePath, true);
            fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
            fw.append("\n");

            for(Book book : newBookList)
            {
                fw.append(book.getBookId() + ";" + book.Title + ";" + book.Author + ";" + book.ISBM + ";" + book.Borrowed + ";" + book.Genre);
                fw.append("\n");
            }

            fw.flush();
            fw.close();
        }
        catch(IOException e)
        {
            System.out.println("Error closing file. ");
        }
    }



    public static ArrayList<BookRecord> getAllBookBorrows(ArrayList<Book> books, ArrayList<User> members)
    {
        ArrayList<BookRecord> brList = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(borrowTablePath));

            br.readLine();

            String lineRead = br.readLine();

            if (lineRead != null)
            {
                while (lineRead != null)
                {
                    String[] fetchedData = lineRead.split(";");

                    String bookId = fetchedData[0];
                    String UserId = fetchedData[1];
                    LocalDate returnBy = LocalDate.parse(fetchedData[2]);

                    Book bookToUse = null;
                    User userToUse = null;
                    for(Book book : books)
                    {
                        if(book.getISBM().equals(bookId))
                        {
                            bookToUse = book;
                        }
                    }

                    for(User user : members)
                    {
                        if(user.getUsername().equals(UserId))
                        {
                            userToUse = user;
                        }
                    }


                    if(userToUse != null || bookToUse != null)
                    {
                        brList.add(new BookRecord(bookToUse, userToUse, returnBy));
                    }
                    lineRead = br.readLine();
                }
                br.close();

                return brList;
            }
            else
            {
                FileWriter fw = new FileWriter(borrowTablePath);
                fw.append("BookID; UserID; returnBy");
                fw.append("\n");

                fw.flush();
                fw.close();
                return brList;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("= = = = = = = = = = = = = = = =");
            try
            {
                System.out.println("ERROR: File not found");
                System.out.println("Resolving issue...");
                File f = new File(borrowTablePath);

                f.createNewFile();

                FileWriter fw = new FileWriter(borrowTablePath);
                fw.append("BookID; UserID; returnBy");
                fw.append("\n");

                fw.flush();
                fw.close();

                if (f.exists())
                {
                    System.out.println("Issue resolved..");
                }
                else
                {
                    System.out.println("Failed");
                }
            }
            catch (IOException ex)
            {
                System.out.println("ERROR: Issue with creating file.");
                System.out.println("More information: " + ex.getMessage());
            }
            System.out.println("= = = = = = = = = = = = = = = =");
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        return brList;
    }


    public static void insertNewBookRecord(BookRecord newBookRecord)
    {

        try
        {
            File file = new File(borrowTablePath);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String firstLine = br.readLine();
            br.close();

            if (firstLine == null)
            {
                FileWriter fw = new FileWriter(file, true); // Open in append mode
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
                fw.append("\n");
                fw.flush();
                fw.close();
            }
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        try
        {
            FileWriter fw = new FileWriter(borrowTablePath, true);

            fw.append(newBookRecord.getBook().getISBM() + ";" + newBookRecord.getMember().getUsername() + ";" + newBookRecord.getReturnDate());
            fw.append("\n");

            fw.flush();
            fw.close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }


    }

    public static void removeBookRecord(ArrayList<BookRecord> newBookRecordList)
    {

        try
        {
            // Clearing the file
            new FileWriter(borrowTablePath).close();
        } catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        try
        {
            FileWriter fw = new FileWriter(borrowTablePath, true);
            fw.append("BookID; UserID; returnBy");
            fw.append("\n");

            for(BookRecord record : newBookRecordList)
            {
                fw.append(record.getBook().getISBM() + ";" + record.getMember().getUsername() + ";" + record.getReturnDate());
                fw.append("\n");
            }

            fw.flush();
            fw.close();
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }


    }

}
