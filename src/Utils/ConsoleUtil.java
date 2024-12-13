package Utils;

import java.util.Scanner;

public class ConsoleUtil
{
    public static void consoleClear()
    {
        int amoutOfNewLines = 20;

        for(int i = 0; i < amoutOfNewLines; i++)
        {
            System.out.println();
        }
    }

    public static void consoleClear(int spaces)
    {
        int amoutOfNewLines = spaces;

        for(int i = 0; i < amoutOfNewLines; i++)
        {
            System.out.println();
        }
    }


    public static String getUserInput(String type)
    {
        Scanner scanner = new Scanner(System.in);

        String input;
        do
        {
            System.out.print(type + ": ");
            input = scanner.nextLine();
        }
        while(input.isEmpty());

        //scanner.close();

        return input;
    }

    public static String columnBoxHelper(String detail, int spaceLength)
    {

        StringBuilder builder = new StringBuilder(detail);
        if (detail.length() > spaceLength)
        {
            System.out.println("Crash out");
        }
        else
        {
            while (builder.length() < spaceLength)
            {
                builder.append(" ");
            }
            return builder.toString();
        }

        return null;
    }



}
