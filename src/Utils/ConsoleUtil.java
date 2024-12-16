package Utils;

import java.util.Scanner;

public class ConsoleUtil
{

    // Method responsible for creating a new line 20 times
    public static void consoleClear()
    {
        int amoutOfNewLines = 20;

        for(int i = 0; i < amoutOfNewLines; i++)
        {
            System.out.println();
        }
    }

    // Overload method of consoleClear. This time you can have custom amount of new lines
    public static void consoleClear(int spaces)
    {
        int amoutOfNewLines = spaces;

        for(int i = 0; i < amoutOfNewLines; i++)
        {
            System.out.println();
        }
    }

    // A method used to format string to fit in a specified length column
    public static String columnBoxHelper(String detail, int spaceLength)
    {

        StringBuilder builder = new StringBuilder(detail);
        // if the provided string exceeds the length of spaceLength we cut the string so it doesn't exceed the limit
        if (detail.length() > spaceLength)
        {
            return detail.substring(0, spaceLength);
        }
        else
        {
            // else we simply added the necessary spaces to make it fit the column
            while (builder.length() < spaceLength)
            {
                builder.append(" ");
            }
            return builder.toString();
        }
    }


    private static String[] testInputs;
    private static int testInputIndex = 0;

    /*
    Method responsible for setting up and storing the test inputs which will be used
    to simulate user input
     */
    public static void setTestInput(String[] inputs)
    {
        testInputs = inputs;
        testInputIndex = 0;
    }

    /*
    Method responsible for getting the test user input
     */
    public static String getUserInput(String prompt)
    {
        // If testinputs is not empty and the index is less than the length of testinputs
        if (testInputs != null && testInputIndex < testInputs.length)
        {
            // we show the prompt that needs to be entered
            System.out.print(prompt + ": ");
            // Get the test input entered and go to the next index that contains the next input
            String input = testInputs[testInputIndex++];
            System.out.println(input);
            return input;
        }

        // if there are no testinputs found we get the user input directly
        return getUserInputFromUser(prompt);
    }

    /*
    Method responsible for directly getting the input from the user
     */
    private static String getUserInputFromUser(String type)
    {
        Scanner scanner = new Scanner(System.in);

        // if the user enters an empty string we reask the user
        String input;
        do
        {
            System.out.print(type + ": ");
            input = scanner.nextLine();
        }
        while (input.isEmpty());

        // return the user input
        return input;
    }




}
