package Modals;

public abstract class User
{
    public int id;
    public String Role;
    public String Username;
    public String Password;
    public String Firstname;
    public String Lastname;

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
