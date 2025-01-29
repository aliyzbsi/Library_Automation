package Library.entity;

import Library.Library;
import Library.validations.BookValidations;
import Library.validations.ReaderValidations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader extends Person{
    private  Long readerId;
    private final List<LibraryBook> purchasedLibraryBooks;
    private final List<LibraryBook> borrowedLibraryBooks;
    private final List<LibraryBook> myLibraryBooks;
    private double balance;
    private final Library library;
    private String password;

    public Reader(Long readerId, String name, double balance, Library library) {
        super(name);
        this.readerId = readerId;
        this.balance = balance;
        this.library = library;

        this.purchasedLibraryBooks = new ArrayList<>();
        this.borrowedLibraryBooks = new ArrayList<>();
        this.myLibraryBooks = new ArrayList<>();
    }

    @Override
    public String whoYouAre() {
        return "Reader";
    };
    public boolean verifyPassword(String password) {
        return password.equals("iamreader");
    }


    public void addBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Geçersiz miktar!");
        }
        this.balance += amount;
    }

    public boolean hasEnoughBalance(double amount) {
        return this.balance >= amount;
    }


    public void purchaseBook(LibraryBook book){
       BookValidations.bookInfoNullCheck(book);
       BookValidations.bookExistCheck(library,book);

        LibraryBook libraryBook = library.getBookById(book.getBookId());

        BookValidations.bookPurchaseStatusCheck(libraryBook,book);

        if (!hasEnoughBalance(book.getBookPrice())) {
            throw new RuntimeException("Yetersiz bakiye!"+"Kitap Değeri : "+book.getBookPrice()+" Sizin bakiyeniz : "+balance);
        }
        purchasedLibraryBooks.add(book);
        myLibraryBooks.add(book);
        balance -= book.getBookPrice();
        book.setStatus(Status.PURCHASED);
        book.setDateOfPurchase(LocalDate.now());

    };

    public void borrowBook(LibraryBook book) {

        BookValidations.bookInfoNullCheck(book);

        ReaderValidations.maksBookCheck(borrowedLibraryBooks);

        BookValidations.bookExistCheck(library,book);


        LibraryBook libraryBook = library.getBookById(book.getBookId());
        BookValidations.bookBorrowStatusCheck(libraryBook);

        if (!hasEnoughBalance(book.getBookPrice() / 2)) {
            throw new RuntimeException("Yetersiz bakiye!");
        }

        borrowedLibraryBooks.add(book);
        myLibraryBooks.add(book);
        balance -= (book.getBookPrice() / 2);
        book.setStatus(Status.BORROWED);

    }

    public String returnBook(LibraryBook libraryBook){

        ReaderValidations.bookNullCheck(libraryBook);
        ReaderValidations.myBooksContainsCheck(myLibraryBooks,libraryBook);


        double refundAmount;
        if (libraryBook.getStatus() == Status.PURCHASED) {
            refundAmount = libraryBook.getBookPrice() * 0.75;
        } else {
            refundAmount = libraryBook.getBookPrice() / 2;
        }
        balance += refundAmount;
        libraryBook.setStatus(Status.AVAILABLE);
        libraryBook.setBookPrice(libraryBook.getBookPrice() * 0.75);

        myLibraryBooks.remove(libraryBook);
        if (borrowedLibraryBooks.contains(libraryBook)) {
            borrowedLibraryBooks.remove(libraryBook);
        }
        if (purchasedLibraryBooks.contains(libraryBook)) {
            purchasedLibraryBooks.remove(libraryBook);
        }
        return String.format("%s adlı kitap iade edildi. İade edilen tutar: %.2f TL",
                libraryBook.getBookName(), refundAmount);
    }

    public void showBooks(){
        System.out.println("=== Satın Alınan Kitaplar ===");
        purchasedLibraryBooks.forEach(libraryBook ->
                System.out.println(libraryBook.getBookName() + " - " + libraryBook.getBookPrice() + " TL"));

        System.out.println("\n=== Ödünç Alınan Kitaplar ===");
        borrowedLibraryBooks.forEach(libraryBook ->
                System.out.println(libraryBook.getBookName() + " - Teslim Tarihi: " +
                        libraryBook.getDateOfPurchase().plusDays(14)));
    }


    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        if (this.readerId != null) {
            throw new IllegalStateException("ID sadece bir kez atanabilir!");
        }
        this.readerId = readerId;
    }

    public double getBalance() {
        return balance;
    }


    public List<LibraryBook> getBorrowedBooks() {
        return new ArrayList<>(borrowedLibraryBooks);
    }
    public List<LibraryBook> getPurchasedBooks() {
        return new ArrayList<>(purchasedLibraryBooks);
    }
    public Library getLibrary() {
        return library;
    }
    public List<LibraryBook> getMyBooks(){
        return new ArrayList<>(myLibraryBooks);
    }

}
