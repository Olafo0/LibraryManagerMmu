package Modals;

public class Book
{
    private String bookId;
    private String Title;
    private String Author;
    private String ISBM;
    private boolean Borrowed;
    private String Genre;


    public Book(String bookId ,String Title, String Author, String ISBM, boolean Borrowed, String Genre)
    {
        this.bookId = bookId;
        this.Title = Title;
        this.Author = Author;
        this.ISBM = ISBM;
        this.Borrowed = Borrowed;
        this.Genre = Genre;
    }

    public String getBookId()
    {
        return bookId;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getAuthor()
    {
        return Author;
    }

    public String getISBM()
    {
        return ISBM;
    }

    public boolean getBorrowed()
    {
        return Borrowed;
    }

    public void setBorrowed(boolean borrowed)
    {
        Borrowed = borrowed;
    }

    public String getGenre()
    {
        return Genre;
    }

}
