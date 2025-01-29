package Library.validations;

import Library.Library;
import Library.entity.LibraryBook;
import Library.entity.Status;

import java.util.Scanner;

public class BookValidations {

    public static void bookBorrowStatusCheck(LibraryBook libraryBook) {
        if (libraryBook.getStatus() != Status.AVAILABLE) {
            String status = libraryBook.getStatus() == Status.PURCHASED ? "Satılmış" : "Ödünç alınmış";
            throw new RuntimeException("Kitap ödünç alınamaz durumda: " + status);
        }
    }



    public static void bookExistCheck(Library library, LibraryBook book) {
        if (!library.getAllBooks().contains(book)) {
            throw new RuntimeException("Kitap kütüphanede bulunamadı!");
        }
    }

    public static void bookInfoNullCheck(LibraryBook book) {
        if (book == null) {
            throw new IllegalArgumentException("Kitap bilgisi boş olamaz!");
        }
    }

    public static void bookPurchaseStatusCheck(LibraryBook libraryBook, LibraryBook book) {
        if (libraryBook.getStatus() != Status.AVAILABLE) {
            String kitapDurumu = book.getStatus() == Status.PURCHASED ? "Satılmış!" : "Ödünç alınmış!";
            throw new RuntimeException("Kitap satın alınamaz durumda! : "+kitapDurumu);
        }
    }

    public static void bookNameCheck(String bookName) {
        if(bookName.trim().isEmpty()){
            throw new RuntimeException("Kitap adı boş olamaz!");
        }
    }

    public static void getValidIntegerInput(Integer edition) {

            try {
                if (edition <= 0) {
                    throw new RuntimeException("Değer 0'dan büyük olmalıdır!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lütfen geçerli bir tam sayı giriniz!");
            } catch (RuntimeException e) {
                System.out.println("Hata: " + e.getMessage());
            }
    }
}
