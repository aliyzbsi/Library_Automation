package Library.validations;

import Library.entity.LibraryBook;

import java.util.List;

public class ReaderValidations {


    public static void bookNullCheck(LibraryBook libraryBook) {
        if (libraryBook == null) {
            throw new IllegalArgumentException("Kitap bilgisi boş olamaz!");
        }
    }

    public static void myBooksContainsCheck(List<LibraryBook> myLibraryBooks, LibraryBook libraryBook) {
        if (!myLibraryBooks.contains(libraryBook)) {
            throw new RuntimeException("Bu kitap sizde değil!");
        }

    }

    public static void maksBookCheck(List<LibraryBook> borrowedLibraryBooks) {
        if (borrowedLibraryBooks.size() >= 5) {
            throw new RuntimeException("Maksimum ödünç alma limitine ulaştınız! (Limit: " + 5 + ")");
        }
    }

    public static void myBooksContainsAddBookCheck(List<LibraryBook> myBooks, LibraryBook book) {
        boolean bookExists = myBooks.stream()
                .anyMatch(myBook -> myBook.getBookName().equalsIgnoreCase(book.getBookName()));

        if (bookExists) {
            throw new RuntimeException(book.getBookName() + " adlı kitap sizde mevcut, birden fazla alınamaz!");
        }
    }
}
