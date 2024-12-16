package Services;

import Modals.Book;
import Modals.BookRecord;
import Modals.Member;
import Modals.User;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvService
{
    // Paths to the different CSVs that are used
    private final static String projectPath = System.getProperty("user.dir");
    private final static String userTablePath = Paths.get(projectPath, "src/Tables/member.csv").toString();
    private final static String bookTablePath = Paths.get(projectPath, "src/Tables/book.csv").toString();
    private final static String borrowTablePath = Paths.get(projectPath, "src/Tables/borrowed.csv").toString();

    // Get the list of all registered users from the member.csv
    public static ArrayList<User> getAllUsers()
    {
        ArrayList<User> fetchedUsers = new ArrayList<User>();
        try
        {
            Scanner sc = new Scanner(new File(userTablePath));
            sc.useDelimiter("\n");

            // Skipping headers
            if(sc.hasNext())
            {
                sc.next();
            }
            // if there are no headers / column names we add them
            else
            {
                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
                fw.append("\n");

                fw.flush();
                fw.close();

                System.out.println("No members found");
                return fetchedUsers;
            }

            // iterate through each row and make an object so it can added to the fetchedUsers
            while(sc.hasNext())
            {
                String[] fetchedUser = sc.next().split(";");

                int id = Integer.parseInt(fetchedUser[0]);
                String role = fetchedUser[1];
                String username = fetchedUser[2];
                String password = fetchedUser[3];
                String firstName = fetchedUser[4];
                String lastName = fetchedUser[5];

                fetchedUsers.add(new Member(id, role, username, password, firstName, lastName));
            }
            sc.close();

            // return the list of all users from the memebrs.csv
            return fetchedUsers;
        }
        /*
        If the following file cannot be found we create it and add the headers / column names
         */
        catch(FileNotFoundException e)
        {
            try
            {
                System.out.println("= = = = = = = = = = = = = = = = =");
                System.out.println("ERROR: File not found");
                System.out.println("Resolving issue...");

                // Craete the file
                File file = new File(userTablePath);
                file.createNewFile();

                // add headers
                FileWriter fw = new FileWriter(userTablePath);
                fw.append("Id; Role; Username; Password; Firstname; Lastname");
                fw.append("\n");

                fw.flush();
                fw.close();

                // if file created tell that to the user
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
                // if a file can't be created tell the user
                System.out.println("ERROR: Issues in creating file");
                System.out.println("More information: " + ex.getMessage());
            }

            return fetchedUsers;
        }
        catch(IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        return fetchedUsers;
    }


    /*
    This method is responsible for fetching all of the books that exist in the book.csv
    same concept as the getAllUsers
     */
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
                // if missing headers / column names add them
                FileWriter fw = new FileWriter(bookTablePath);
                fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                fw.append("\n");

                fw.flush();
                fw.close();

                System.out.println("No books found");
                return fetchedBooks;
            }

            // Iterate through each fetched book and create an object that can be added to a fetchedBooks
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
            // if file not found create it with appropriate headers / column names
            System.out.println("= = = = = = = = = = = = = = = =");
            try
            {
                System.out.println("ERROR: File not found");
                System.out.println("Resolving issue...");
                File f = new File(bookTablePath);

                // create the file
                f.createNewFile();


                // add the missing headers
                FileWriter fw = new FileWriter(bookTablePath);
                fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                fw.append("\n");

                fw.flush();
                fw.close();

                // tell the user what happened
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

    /*
    Method responsible for adding a new registered user to the member.csv
     */
    public static void addNewMember(User newUser)
    {

        File file = new File(userTablePath);

        try
        {
            // Check if file exists
            if (file.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                br.close();

                // Ensures that the csv file has column headers
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
                // if file doesn't exist create it with the column headers
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
            /*
            create an instance of the fileWriter which is responsible for writing to a desired file.
            Also we set the second parameter to true so it appends it to the end of the file rather than recreating the file
             */
            FileWriter fileWriter = new FileWriter(userTablePath, true);
            fileWriter.append(newUser.getId() + ";" + newUser.getRole() + ";" + newUser.getUsername() + ";" + newUser.getPassword() + ";" + newUser.getFirstname() + ";" + newUser.getLastname());
            // we go onto the next line
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();

        }
        catch(IOException e)
        {
            System.out.println("Error appending to file. ");
        }

    }

     /*
    Method responsible for adding a new registered user to the member.csv
     */
    public static void addNewBook(Book newBook)
    {
        File file = new File(bookTablePath);
        try
        {
            // check if file exists
            if(file.exists())
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                br.close();

                // add missing column headers if file empty
                if(firstLine == null)
                {
                    FileWriter fw = new FileWriter(file, true);
                    fw.append("BookId; Title; Author; ISBM; Borrowed; Genre");
                    fw.append("\n");
                    fw.flush();
                    fw.close();
                }
            }
            // if file is missing create it
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
            // Add the new book to the book.csv
            FileWriter fw = new FileWriter(bookTablePath, true);
            fw.append(newBook.getBookId() + ";" + newBook.getTitle() + ";" + newBook.getAuthor() + ";" + newBook.getISBM() + ";" + newBook.getBorrowed() + ";" + newBook.getGenre());
            fw.append("\n");
            fw.flush();
            fw.close();
        }
        catch(IOException e)
        {
            // Tell the user if unsuccessful
            System.out.println("Error accessing file. ");
        }

    }


    /*
    Method responsible for removing a book from the book.csv
     */
    public static void removeABook(ArrayList<Book> newBookList)
    {

        /*
        Clearing the file, so we can add the updated arrayList to it.
        Originally, I wanted to only remove the book that was requested to be removed from the csv file however,
        I couldn't find a solution on how to specifically remove a single book, so I decided to update the arrayList
        and rewrite the entire arrayList. If a bigger file were to be used it would affect performance
         */
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

            /*
             Iterating through the updated arrayList and appending it to the fresh file so it is similar
             to the old one but contains the changes that have done
             */
            for(Book book : newBookList)
            {
                fw.append(book.getBookId() + ";" + book.getTitle() + ";" + book.getAuthor() + ";" + book.getISBM() + ";" + book.getBorrowed() + ";" + book.getGenre());
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


   /*
   A method responsible for fetching and returning all of the borrowed books from the borrowed.csv.
   It matches the borrowed books with the user, so we can access it easily
    */
    public static ArrayList<BookRecord> getAllBookBorrows(ArrayList<Book> books, ArrayList<User> members)
    {
        // initilising the borrowed list
        ArrayList<BookRecord> brList = new ArrayList<>();
        try
        {
            // initilising the bufferedReader so we can read data from the borrow.csv file
            BufferedReader br = new BufferedReader(new FileReader(borrowTablePath));

            // Skipping the headers
            br.readLine();

            // Reading the next line
            String lineRead = br.readLine();

            // if the line is not empty continue reading, otherwise make column headers
            if (lineRead != null)
            {

                while (lineRead != null)
                {
                    // Fetch the data on the line currently on
                    String[] fetchedData = lineRead.split(";");

                    // Separate the book id, user id, and return date
                    String bookId = fetchedData[0];
                    int userId = Integer.parseInt(fetchedData[1]);
                    LocalDate returnBy = LocalDate.parse(fetchedData[2]);

                    Book bookToUse = null;
                    User userToUse = null;

                    /*
                    Start iterating through all of the books until we find a matching id from all of our
                    book collection.
                     Then we get the entire object and store it for later
                     We repeat the same thing for members.
                     */
                    for(Book book : books)
                    {
                        if(book.getBookId().equals(bookId))
                        {
                            bookToUse = book;
                        }
                    }

                    for(User user : members)
                    {
                        if(user.getId() == userId)
                        {
                            userToUse = user;
                        }
                    }

                    /*
                    When we have both values  we add the Two objects (Not ids) to the brList which will
                    contain all of the borrowed books and the entire object of the member and book so we can
                    access the data easily
                     */
                    if(userToUse != null || bookToUse != null)
                    {
                        brList.add(new BookRecord(bookToUse, (Member) userToUse, returnBy));
                    }
                    // We move on to the next line to repeat the process
                    lineRead = br.readLine();
                }
                br.close();

                // We return the entire list which contains the entire book collection
                return brList;
            }
            else
            {
                // If file is missing column headers we add them
                FileWriter fw = new FileWriter(borrowTablePath);
                fw.append("BookID; UserID; returnBy");
                fw.append("\n");

                fw.flush();
                fw.close();
                return brList;
            }
        }
        // Handling the error when a file is not found
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


    /*
    Method for inserting a new book record to borrowed csv file
     */
    public static void insertNewBookRecord(BookRecord newBookRecord)
    {

        try
        {
            // Check if file is empty, if it is add the missing column headers
            File file = new File(borrowTablePath);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String firstLine = br.readLine();
            br.close();

            if (firstLine == null)
            {
                FileWriter fw = new FileWriter(file, true); // Open in append mode
                fw.append("BookID; UserID; returnBy");
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
            // Initialise filewriter so we can append to borrowed.csv
            FileWriter fw = new FileWriter(borrowTablePath, true);

            fw.append(newBookRecord.getBook().getBookId() + ";" + newBookRecord.getMember().getId() + ";" + newBookRecord.getReturnDate());
            fw.append("\n");

            fw.flush();
            fw.close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }


    }

    /*
    A method responsible for removing a record from the book record.
    Again, wanted to specifically remove the book but couldn't find how.
    So, I removed it from the arrayList where we store bookrecords and rewrote the
    entire file with the new arrayList
     */
    public static void removeBookRecord(ArrayList<BookRecord> newBookRecordList)
    {

        try
        {
            // Clearing the entire borrow.csv file
            new FileWriter(borrowTablePath).close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        try
        {
            // adding the missing colum headers
            FileWriter fw = new FileWriter(borrowTablePath, true);
            fw.append("BookID; UserID; returnBy");
            fw.append("\n");

            //iterating through the entire updated book record list and appending the data to the file
            for(BookRecord record : newBookRecordList)
            {
                fw.append(record.getBook().getBookId() + ";" + record.getMember().getId() + ";" + record.getReturnDate());
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

    /*
     method that is responsible for clearing all of the csv files (Making them empty).
     Used for testing
     */

    public static void clearAllCsv()
    {
        try
        {
            new FileWriter(userTablePath).close();
            new FileWriter(bookTablePath).close();
            new FileWriter(borrowTablePath).close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}
