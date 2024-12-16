package Modals;

import java.time.LocalDate;
import java.util.UUID;

public class BookRecord
{
    private Book book;
    private User member;
    private LocalDate returnDate;

    public BookRecord(Book book, Member member, LocalDate returnDate)
    {
        // Generates a unique identifier
        this.book = book;
        this.member = member;
        this.returnDate = returnDate;
    }

    public Book getBook()
    {
        return book;
    }

    public User getMember()
    {
        return member;
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
