package Library.entity;

import java.time.LocalDate;
import java.util.Objects;

public class LibraryBook {
    private  Long bookId;
    private  Author author;
    private  String bookName;
    private double bookPrice;
    private Status status;
    private  int edition;
    private LocalDate dateOfPurchase;


    public LibraryBook(Long bookId, String bookName, Author author, double bookPrice, int edition) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.bookPrice = bookPrice;
        this.edition = edition;
        this.status = Status.AVAILABLE;
        this.dateOfPurchase = null;
    }

    public Author getAuthor() {
        return author;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        if (this.bookId != null) {
            throw new IllegalStateException("ID sadece bir kez atanabilir!");
        }
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public int getEdition() {
        return edition;
    }

    public Status getStatus() {
        return status;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    @Override
    public String toString() {
        return "Kitap{" +
                ", Kitap ID=" + bookId +
                ", Kitap Adı='" + bookName + '\'' +
                ", Yazar=" + author +
                ", Kitap Fiyatı=" + bookPrice +
                ", Durum=" + status + " " +
                edition +".Baskı=" +
                ", Satılma tarihi=" + dateOfPurchase +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        LibraryBook book = (LibraryBook) object;
        return Objects.equals(bookName, book.bookName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookName);
    }
}
