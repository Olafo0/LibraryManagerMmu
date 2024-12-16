package Utils;

public class PasswordUtil
{

    // predefining the allowed charaters in the password
    public static final String specialCharacters = "$%!?*";
    public static final String numbers = "0123456789";

    /*
    This method is responsible for checking if the users password meets the
    specified requirements
     */
    public static boolean PasswordCheck(String password)
    {
        // boolean values used to check if they are met at the end of the program
        boolean isSpecialChar = false;
        boolean isFiveChar = false;
        boolean isNumericChar = false;

        // iterate through each charater of the password
        for(Character character : password.toCharArray())
        {
            // if passwords length is greater or equals to 5 set isFiveChar to true as it is met
            if(password.length() >= 5)
            {
                isFiveChar = true;
            }

            // if password contains any of the predefined special charaters set isSpecialChar to true
            if(specialCharacters.contains(character.toString()))
            {
                isSpecialChar = true;
            }

            // if password contains any numbers set isNumericChar to true
            if(numbers.contains(character.toString()))
            {
                isNumericChar = true;
            }

            // otherwise they are false
        }

        // if all of the booleans are true return true as password meets the requirements
        if(isSpecialChar && isFiveChar && isNumericChar)
        {
            return true;
        }
        // if the password doesn't meet the requirements print out the requirements not met
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
