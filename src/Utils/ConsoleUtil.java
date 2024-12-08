package Utils;

import java.util.Scanner;

public class ConsoleUtil
{
    public static void consoleClear()
    {
        int amoutOfNewLines = 10;

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

        return input;
    }



}
