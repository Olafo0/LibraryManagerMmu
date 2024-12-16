package Modals;

public class Member extends User
{
    // Originally was meant ot contain admin specific features but was never implemented
    public Member(int id, String Role,String Username, String Password, String Firstname, String Lastname)
    {
        super(id, Role, Username, Password, Firstname, Lastname);
    }
}
