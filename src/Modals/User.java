package Modals;


/*
 An abstract user class which defines common properties that will be common between other users.
 For example this class is inherited by member and admin which originally were meant to have additional features
 not contained within this class. However, due to the time I never implemented that

 Contains getters and setters
 */
public abstract class User
{
    private int id;
    private String Role;
    private String Username;
    private String Password;
    private String Firstname;
    private String Lastname;

    public User(int id, String role,String Username, String Password, String Firstname, String Lastname)
    {
        this.id = id;
        this.Role = role;
        this.Username = Username;
        this.Password = Password;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
    }


    public int getId()
    {
        return id;
    }

    public String getRole()
    {
        return Role;
    }

    public String getUsername()
    {
        return Username;
    }

    public String setUsername(String newUsername)
    {
        return Username = newUsername;
    }

    public String getPassword()
    {
        return Password;
    }

    public void setPassword(String newPassword)
    {
        Password = newPassword;
    }

    public String getFirstname()
    {
        return Firstname;
    }

    public void setFirstName(String newFirstname)
    {
        Firstname = newFirstname;
    }

    public String getLastname()
    {
        return Lastname;
    }

    public void setLastname(String newLastname)
    {
        Lastname = newLastname;
    }

}
