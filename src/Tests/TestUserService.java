package Tests;

import Data.DataRepo;
import Modals.*;
import Services.UserService;
import Utils.ConsoleUtil;
import Utils.PasswordUtil;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/*
A test class used to test out the functionalities of userService, passwordUtil, and csvServices which are contained within
some of the methods
 */

class UserServiceTest
{
    private static UserService userService;
    private static PasswordUtil passwordUtil;
    private static DataRepo dataRepo;


    @Test
    @Order(1)
    void testCreateNewUserMember()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        dataRepo.clearAll();
        ConsoleUtil.setTestInput(new String[]{"n","John", "Doe", "University", "johndoe", "Pass123!"});
        userService.createNewUser();

        ArrayList<User> users = dataRepo.getAllUsers();
        assertEquals("johndoe", users.get(0).getUsername());
        dataRepo.clearAll();
    }


    @Test
    @Order(2)
    void testCreateNewUserAdmin()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        dataRepo.clearAll();
        ConsoleUtil.setTestInput(new String[]{"y","331","Jane", "Smith", "University", "janesmith", "Pass123!"});
        userService.createNewUser();

        ArrayList<User> users = dataRepo.getAllUsers();
        assertEquals("janesmith", users.get(0).getUsername());
        dataRepo.clearAll();
    }

    @Test
    @Order(3)
    void testValidPassword()
    {
        passwordUtil = new PasswordUtil();

        boolean result = passwordUtil.PasswordCheck("Password123?");

        assertTrue(result);
    }

    @Test
    @Order(4)
    void testInvalidPasswordNoNumChar()
    {
        passwordUtil = new PasswordUtil();

        boolean result = passwordUtil.PasswordCheck("fffffff?");

        // Should fail because password doesn't contain any numerical characters
        assertFalse(result);
    }

    @Test
    @Order(5)
    void testInvalidPasswordNoSpecialChar()
    {
        passwordUtil = new PasswordUtil();

        boolean result = passwordUtil.PasswordCheck("asdas");

        // Should fail because password doesn't contain any special characters
        assertFalse(result);
    }

    @Test
    @Order(6)
    void testInvalidPasswordLength()
    {
        passwordUtil = new PasswordUtil();

        boolean result = passwordUtil.PasswordCheck("a1?");

        // Should fail because password doesn't contain any special characters
        assertFalse(result);
    }



    @Test
    @Order(7)
    void testUserLoginSuccess()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        User newUser = new Member(1, "Member", "johnny", "Pass123!", "Johhny", "Smith");
        dataRepo.addUser(newUser);

        ConsoleUtil.setTestInput(new String[]{"johnny", "Pass123!"});
        User loggedInUser = userService.userLogin();

        assertNotNull(loggedInUser);
        dataRepo.clearAll();

    }

    @Test
    @Order(8)
    void testUserLoginFailure()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        ConsoleUtil.setTestInput(new String[]{"test", "test"});
        User loggedInUser = userService.userLogin();
        assertNull(loggedInUser);
    }

    @Test
    @Order(9)
    void testFilterAllUsers()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        dataRepo.addUser(new Member(1, "Member", "member1", "Pass123!", "Josh", "Josh"));
        dataRepo.addUser(new Member(2, "Member", "memberTwo", "Pass123!", "Olaf", "Olaf"));
        dataRepo.addUser(new Admin(3, "Admin", "admin1", "Admin123!", "Laura", "Laura"));

        ArrayList<User> filteredUser = userService.userFilter(0,"");

        assertEquals(3,filteredUser.size());
        dataRepo.clearAll();
    }

    @Test
    @Order(10)
    void testFilterAllMembers()
    {
        dataRepo = DataRepo.instance();
        userService = new UserService();

        dataRepo.addUser(new Member(1, "Member", "member1", "Pass123!", "Josh", "Josh"));
        dataRepo.addUser(new Member(2, "Member", "memberTwo", "Pass123!", "Olaf", "Olaf"));
        dataRepo.addUser(new Admin(3, "Admin", "admin1", "Admin123!", "Laura", "Laura"));

        ArrayList<User> filteredUser = userService.userFilter(1,"");

        assertEquals(2,filteredUser.size());
        dataRepo.clearAll();
    }
}
