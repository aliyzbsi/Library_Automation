package Library.services;

import Library.entity.Reader;

public interface IReaderService {
    void showReaderMenu();
    void searchBook();
    void borrowBookProcess();
    void returnBookProcess();
    void purchaseBookProcess();
    void showBorrowedBooks(Reader reader);
    void showPurchasedBooks(Reader reader);
    void showAllBooks();

    void addBalance(Reader reader);
    double getBalance();
}
