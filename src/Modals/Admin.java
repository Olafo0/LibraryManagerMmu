package Modals;

public class Admin extends User
{
    // Originally was meant ot contain admin specific features but was never implemented
    public Admin(int id, String role, String Username, String Password, String Firstname, String Lastname)
    {
        super(id, role, Username, Password, Firstname, Lastname);
    }

}

