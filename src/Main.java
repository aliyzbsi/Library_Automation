import Library.Library;
import Library.services.LibraryService;
import Library.entity.Reader;
import Library.entity.LibraryBook;
import Library.entity.Status;
import Library.entity.Author;
import Library.entity.Librarian;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // Singleton library instance'ını al
        Library library = Library.getInstance();

        // Test verilerini oluştur
        Author author = new Author("George Orwell", new ArrayList<>());
        LibraryBook book = new LibraryBook(1L, "1984", author, 29.99, 1 );

        // Kütüphaneye kitap ekle
        library.addBook(book);

        // Reader oluştur ve kütüphaneye ekle
        Reader reader = new Reader(1L, "Ali", 100, library);
        library.addReader(reader);

        // Kütüphaneci oluştur ve ekle
        Librarian librarian = new Librarian("Ayşe", 1L);
        library.addLibrarian(librarian);



        // LibraryService ile test
        LibraryService libraryService = new LibraryService();
        libraryService.start();
        libraryService.logout();
        libraryService.isAuthenticated();
        libraryService.login();

    }
}