package Utils;

public class PasswordUtil
{
    
    public static final String specialCharacters = "$%!?*";
    public static final String numbers = "0123456789";

    public static boolean PasswordCheck(String password)
    {
        boolean isSpecialChar = false;
        boolean isFiveChar = false;
        boolean isNumericChar = false;

        for(Character character : password.toCharArray())
        {
            if(password.length() >= 5)
            {
                isFiveChar = true;
            }

            if(specialCharacters.contains(character.toString()))
            {
                isSpecialChar = true;
            }

            if(numbers.contains(character.toString()))
            {
                isNumericChar = true;
            }
        }

        if(isSpecialChar && isFiveChar && isNumericChar)
        {
            return true;
        }
        else
        {
            ConsoleUtil.consoleClear();
            System.out.println("= = = = = | Password does not meet the criteria | = = = = = =");
            if(isSpecialChar == false)
            {
                System.out.println("Password does not contain a special character");
            }
            if(isNumericChar == false)
            {
                System.out.println("Password does not contain a numeric character");
            }
            if(isFiveChar == false)
            {
                System.out.println("Password is to short. Needs to be at least 5 characters long");
            }
            System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");

            return false;
        }
    }
}
