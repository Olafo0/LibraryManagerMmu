package Modals;

import java.time.LocalDate;
import java.util.UUID;

public class BookRecord
{
    private String ReocrdID;
    private Book book;
    private User member;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BookRecord(Book book, User member, LocalDate returnDate)
    {
        // Generates a unique identifier
        this.ReocrdID = UUID.randomUUID().toString();
        this.book = book;
        this.member = member;
//        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getReocrdID()
    {
        return ReocrdID;
    }

    public Book getBook()
    {
        return book;
    }

    public User getMember()
    {
        return member;
    }

    public LocalDate getBorrowDate()
    {
        return borrowDate;
    }

    public LocalDate getReturnDate()
    {
        return returnDate;
    }

    @Override
    public String toString()
    {
        return "x";
    }
}
