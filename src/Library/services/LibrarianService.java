package Library.services;

import Library.Library;
import Library.entity.*;
import Library.validations.BookValidations;

import java.util.*;

public class LibrarianService implements ILibrarianService {
    private final Scanner scanner;
    private final Library library;

    private Librarian currentUser;


    public LibrarianService(Scanner scanner, Library library) {
        this.scanner=scanner;
        this.library=library;

    }

    public void currentUser(Librarian librarian) {
        this.currentUser=librarian;
    }

    @Override
    public void showLibrarianMenu() {
        while (isAuthenticated()) {
            try {
                displayMenu();
                int choice = getValidMenuChoice();

                if (!processMenuChoice(choice)) {
                    break;
                }
            } catch (RuntimeException e) {
                System.out.println("Hata: " + e.getMessage());
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Beklenmeyen Hata: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Kütüphaneci Menüsü ===");
        System.out.println("1. Kitap Ekle");
        System.out.println("2. Kitap Sil");
        System.out.println("3. Kitap Güncelle");
        System.out.println("4. Tüm Kitapları Listele");
        System.out.println("5. Okuyucu Ekle");
        System.out.println("6. Okuyucu Sil");
        System.out.println("7. Okuyucu Güncelle");
        System.out.println("8. Yazara göre kitap bul");
        System.out.println("9. Kütüphaneci Ekle");
        System.out.println("10. Çıkış Yap");
        System.out.print("\nSeçiminiz: ");
    }
    private int getValidMenuChoice() {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 10) {
                    return choice;
                } else {
                    System.out.print("Lütfen 1-10 arası bir sayı giriniz: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Lütfen geçerli bir sayı giriniz: ");
                scanner.nextLine();
            }
        }
    }

    private boolean processMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> addBookForLibrary();
                case 2 -> removeBookForLibrary();
                case 3 -> updateBook();
                case 4 -> listAllBooks();
                case 5 -> addReader();
                case 6 -> removeReader();
                case 7 -> updateReader();
                case 8 -> authorForProcess();
                case 9 -> addLibrarian();
                case 10 -> {
                    logout();
                    return false;
                }
                default -> throw new RuntimeException("Geçersiz seçim!");
            }
        } catch (Exception e) {
            System.out.println("İşlem sırasında hata oluştu: " + e.getMessage());
        }
        return true; // Menü döngüsüne devam et
    }

    private void logout() {
        System.out.println("Çıkış yapılıyor...");
        currentUser = null;
    }

    @Override
    public void authorForProcess() {
        try {
            System.out.println("Yazar ismi giriniz: ");
            String authorName=scanner.nextLine();

            Optional<Author> author=library.findAuthorByName(authorName);
            if (author.isPresent()){
                Author foundAuthor=author.get();
                displayAuthorDetails(foundAuthor);
                System.out.println("\n=== Yazar İşlemleri ===");
                System.out.println("1. Yazar Sil (Bütün kitapları silinir!) ");
                System.out.println("2. Yazar Güncelle");
                System.out.println("3. Geri gel");
                int choice=scanner.nextInt();
                scanner.nextLine();
                switch (choice){
                    case 1->{
                        List<LibraryBook> authorBooks=library.searchBooksByAuthor(foundAuthor.getName());

                        System.out.print("\nYazarı ve Kitaplarını silmek istediğinize emin misiniz? (E/H): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("E")) {

                            for (LibraryBook removedBook:authorBooks){
                                library.removeBook(removedBook.getBookId());
                            }
                            library.removeAuthor(foundAuthor.getAuthorId());
                            System.out.println("Yazar ve kitapları başarıyla silindi!");
                        } else {
                            System.out.println("Silme işlemi iptal edildi.");
                        }
                    }
                    case 2->{
                        System.out.println("\n Güncel Yazar Adı:");
                        String newAuthorName=scanner.nextLine();
                        foundAuthor.setName(newAuthorName);
                        library.updateAuthor(foundAuthor);
                        System.out.println("Yazar başarıyla güncellendi!"+foundAuthor);
                    }
                    case 3->{
                        System.out.println("Ana menüye dönülüyor...");
                    }
                    default -> throw new RuntimeException("Geçersiz işlem!");
                }
            }else {
                System.out.println("Yazar bulunamadı!");
            }
        }catch (InputMismatchException e) {
            System.out.print("Lütfen istenen tipte input giriniz: ");
            scanner.nextLine();
        }catch (Exception e){
            System.out.println("Beklenmeyen hata:"+e.getMessage());
            scanner.nextLine();
        }
    }


    @Override
    public void addBookForLibrary() {
        System.out.println("\n Yeni Kitap Ekle : ");
        try {

            System.out.println("Kitap adı: ");
            String bookName=scanner.nextLine();
            BookValidations.bookNameCheck(bookName);
            System.out.println("Yazar adı: ");
            String authorName=scanner.nextLine();
            Author author= library.findAuthorByName(authorName)
                    .orElseGet(() -> createNewAuthor(authorName));

            System.out.println("Fiyat: ");
            double bookPrice=scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Baskı: ");
            int edition=scanner.nextInt();
            BookValidations.getValidIntegerInput(edition);
            scanner.nextLine();


            LibraryBook newBook=new LibraryBook(null,bookName,author,bookPrice,edition);
            library.addBook(newBook);
            author.newBook(newBook);
            System.out.println("Kitap eklendi. Kitap ID: "+newBook.getBookId());
        }catch (InputMismatchException e) {
            System.out.print("Lütfen geçerli bir input giriniz: "+e.getMessage());
            scanner.nextLine();
        }
        catch (Exception e){
            System.out.println("Kitap eklenirken beklenmeyen hata oluştu: "+e.getMessage());
        }

    }

    private Author createNewAuthor(String authorName) {
        return new Author(authorName,new ArrayList<>());

    }


    @Override
    public void removeBookForLibrary() {
        try {
            System.out.println("Silinmesini istediğiniz kitabın ID'si: ");
            Long removedBookId=scanner.nextLong();
            scanner.nextLine();
            LibraryBook book=library.getBookById(removedBookId);
            displayBookDetails(book);
            System.out.print("\nKitabı silmek istediğinize emin misiniz? (E/H): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("E")) {
                library.removeBook(removedBookId);
                System.out.println("Kitap başarıyla silindi!");
            } else {
                System.out.println("Silme işlemi iptal edildi.");
            }
        }catch (RuntimeException e){
            System.out.println("Kitap silinirken hata oluştu : "+e.getMessage());
        }catch (Exception e){
            System.out.println("Beklenmeyen hata: "+e.getMessage());
            scanner.nextLine();
        }
    }

    @Override
    public void updateBook() {
        try {
            System.out.println("\nGüncellemek istediğiniz kitabın ID'si");
            Long updatedBookId=scanner.nextLong();
            LibraryBook book=library.getBookById(updatedBookId);
            System.out.println("\nMevcut kitap adı :"+book.getBookName());
            String newBookName=scanner.nextLine();
            book.setBookName(newBookName);
            System.out.println("\nMevcut kitap yazarı: "+book.getAuthor());
            System.out.println("\nYazar adı giriniz: ");
            String authorName=scanner.nextLine();
            Author author=new Author(authorName,new ArrayList<>());
            book.setAuthor(author);
            System.out.println("\nMevcut kitap fiyatı: "+book.getBookPrice());
            System.out.println("\n Yeni fiyat giriniz: ");
            double newPrice= scanner.nextDouble();
            scanner.nextLine();
            book.setBookPrice(newPrice);
            System.out.println("\n Mevcut kitap baskısı: "+book.getEdition());
            System.out.println("\nYeni Baskı Numarası: "+book.getEdition());
            int newEdition= scanner.nextInt();
            scanner.nextLine();
            book.setEdition(newEdition);

            LibraryBook updatedBook=new LibraryBook(null,newBookName,author,newPrice,newEdition);
            library.updatedBook(updatedBook);
            System.out.println(book+" adlı kitap güncellendi!");
        }catch (RuntimeException e){
            System.out.println("Kitap güncellenirken hata oluştu : "+e.getMessage());
        }
    }

    @Override
    public void listAllBooks() {
        try {
            System.out.println("=== Bütün Kitaplar ===");
            List<LibraryBook> libraryBooks=library.getAllBooks();
            displayBooks(libraryBooks);

        }catch (RuntimeException e){
            System.out.println("Hata: "+e.getMessage());
        }
    }

    private void displayBooks(List<LibraryBook> libraryBooks) {
        System.out.println("\nKitap Detayları:");
        for (LibraryBook book:libraryBooks){
            System.out.println("==============");
            System.out.println("ID: " + book.getBookId());
            System.out.println("Ad: " + book.getBookName());
            System.out.println("Yazar: " + book.getAuthor().getName());
            System.out.println("Fiyat: " + book.getBookPrice());
            System.out.println("Durum: " + book.getStatus());
            System.out.println("Baskı: " + book.getEdition());
            System.out.println("==============");
        }

    }

    @Override
    public void addReader() {
        System.out.println("=== Okuyucu Kaydet ===");
        try {
            System.out.println("Okuyucu Adı: ");
            String readerName=scanner.nextLine();
            System.out.println("Bakiye Ekle: ");
            double readerBalance=scanner.nextDouble();
            Library library1=Library.getInstance();
            Reader reader=new Reader(null,readerName,readerBalance,library1);
            library.addReader(reader);

        }catch (RuntimeException e){
            System.out.println("Hata :" +e.getMessage());
        }catch (Exception e){
            System.out.println("Beklenmeyen Hata: "+e.getMessage());
        }
    }

    @Override
    public void addLibrarian() {
        System.out.println("=== Yeni Kütüphaneci Ekle ===");
        try {
            System.out.println("Kütüphaneci adı: ");
            String librarianName=scanner.nextLine();
            Librarian librarian=new Librarian(librarianName,null);
            library.addLibrarian(librarian);
            displayLibrarianDetails(librarian);
        }catch (RuntimeException e){
            System.out.println("Hata: "+e.getMessage());
        }catch (Exception e){
            System.out.println("Beklenmeyen hata: "+e.getMessage());
        }
    }
    @Override
    public void removeReader() {
        try {
            System.out.println("Silinmesini istediğiniz okuyucunun ID'si: ");
            Long removedReaderId=scanner.nextLong();
            scanner.nextLine();
            Reader reader=library.getReader(removedReaderId);
            displayReaderDetails(reader);
            System.out.print("\nOkuyucuyu silmek istediğinize emin misiniz? (E/H): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("E")) {
                library.removeReader(removedReaderId);
                System.out.println("Okuyucu başarıyla silindi!");
            } else {
                System.out.println("Silme işlemi iptal edildi.");
            }

        }catch (RuntimeException e){
            System.out.println("Okuyucu silinirken hata oluştu : "+e.getMessage());
        }catch (Exception e){
            System.out.println("Beklenmeyen hata: "+e.getMessage());
            scanner.nextLine();
        }

    }
    @Override
    public void updateReader() {
        try {
            System.out.println("Güncellemek istediğiniz okuyucunun ID'si: ");
            Long updateReaderId=scanner.nextLong();
            scanner.nextLine();
            Reader reader=library.getReader(updateReaderId);
            System.out.println("\nMevcut kitap adı :"+reader.getName());
            String newReaderName=scanner.nextLine();
            reader.setName(newReaderName);
            Reader updatedReader=new Reader(reader.getReaderId(),newReaderName,reader.getBalance(),reader.getLibrary());


            System.out.print("\nOkuyucuyu güncellemek istediğinize emin misiniz? (E/H): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("E")) {
                library.updateReader(updatedReader);
                System.out.println("Okuyucu başarıyla güncellendi!");
            } else {
                System.out.println("Silme işlemi iptal edildi.");
            }

        }catch (RuntimeException e){
            System.out.println("Okuyucu güncellenirken hata oluştu : "+e.getMessage());
        }catch (Exception e){
            System.out.println("Beklenmeyen hata: "+e.getMessage());
            scanner.nextLine();
        }

    }







    private void displayLibrarianDetails(Librarian librarian) {
        System.out.println("\nKütüphaneci Detayları:");
        System.out.println("ID: " + librarian.getLibrarianId());
        System.out.println("Ad: " + librarian.getName());
    }



    private void displayReaderDetails(Reader reader) {
        System.out.println("\nOkuyucu Detayları:");
        System.out.println("ID: " + reader.getReaderId());
        System.out.println("Ad: " + reader.getName());
    }


    public void displayBookDetails(LibraryBook book) {
        System.out.println("\nKitap Detayları:");
        System.out.println("ID: " + book.getBookId());
        System.out.println("Ad: " + book.getBookName());
        System.out.println("Yazar: " + book.getAuthor().getName());
        System.out.println("Fiyat: " + book.getBookPrice());
        System.out.println("Durum: " + book.getStatus());
        System.out.println("Baskı: " + book.getEdition());
    }
    public void displayAuthorDetails(Author author) {
        System.out.println("\nKitap Detayları:");

        for (LibraryBook book:author.showBook()){
            System.out.println("========== ");
            displayBookDetails(book);
        }


    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
