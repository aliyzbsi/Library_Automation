package Library.services;

import Library.Library;
import Library.entity.LibraryBook;
import Library.entity.Person;
import Library.entity.Reader;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ReaderService implements IReaderService {
    private final Scanner scanner;
    private final Library library;
    private Reader currentUser;

    public ReaderService(Scanner scanner, Library library) {
        this.scanner = scanner;
        this.library = library;

    }

    public void setCurrentUser(Reader reader) {
        this.currentUser=reader;
    }
    
    public void showReaderMenu() {
        while (isAuthenticated()) {  // Menu döngüsü sadece kullanıcı authenticate iken çalışır
            try {
                displayMenu();
                int choice = getValidMenuChoice();

                if (!processMenuChoice(choice)) {
                    break;  // Çıkış seçeneği seçildiğinde döngüden çık
                }
            } catch (RuntimeException e) {
                System.out.println("Hata: " + e.getMessage());
                scanner.nextLine(); // Buffer'ı temizle
            } catch (Exception e) {
                System.out.println("Beklenmeyen Hata: " + e.getMessage());
                scanner.nextLine(); // Buffer'ı temizle
            }
        }
    }
    private void displayMenu(){
        System.out.println("\n=== Okuyucu Menüsü ===");
        System.out.println("1. Kitap Ara");
        System.out.println("2. Kitap Ödünç Al");
        System.out.println("3. Kitap İade Et");
        System.out.println("4. Kitap Satın Al");
        System.out.println("5. Ödünç Alınan Kitapları Listele");
        System.out.println("6. Satın Alınan Kitapları Listele");
        System.out.println("7. Bakiye Ekle");
        System.out.println("8. Bakiye Görüntüle");
        System.out.println("9. Çıkış");
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
            Reader reader=currentUser;
            switch (choice) {
                case 1 -> searchBook();
                case 2 -> borrowBookProcess();
                case 3 -> returnBookProcess();
                case 4 -> purchaseBookProcess();
                case 5 -> showBorrowedBooks(reader);
                case 6 -> showPurchasedBooks(reader);
                case 7 -> addBalance(reader);
                case 8 -> System.out.println("Mevcut bakiye: " + reader.getBalance());
                case 9 -> {
                    currentUser = null;
                    return false;
                }
                case 10->showAllBooks();

                default -> throw new RuntimeException("Geçersiz seçim!");
            }
        } catch (RuntimeException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Beklenmeyen hata: " + e.getMessage());
            scanner.nextLine();
        }
        return true;
    }

    @Override
    public void searchBook() {
        System.out.println("\nKitap Arama");
        System.out.println("1. ID ile ara");
        System.out.println("2. İsim ile ara");
        System.out.println("3. Yazar ile ara");

        int searchChoice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (searchChoice) {
                case 1 -> {
                    System.out.print("Kitap ID: ");
                    Long bookId = scanner.nextLong();
                    LibraryBook book = library.getBookById(bookId);
                    displayBookDetails(book);
                }
                case 2 -> {
                    System.out.print("Kitap adı: ");
                    String bookName = scanner.nextLine();
                    List<LibraryBook> books = library.searchBooksByName(bookName);
                    displayBooks(books);
                }
                case 3 -> {
                    System.out.print("Yazar adı: ");
                    String authorName = scanner.nextLine();
                    List<LibraryBook> books = library.searchBooksByAuthor(authorName);
                    displayBooks(books);
                }
                default -> throw new RuntimeException("Geçersiz seçim!");
            }
        } catch (Exception e) {
            System.out.println("Kitap bulunamadı: " + e.getMessage());
        }
    }

    private void displayBooks(List<LibraryBook> books) {
        if (books.isEmpty()) {
            System.out.println("Kitap bulunamadı!");
        } else {
            books.forEach(this::displayBookDetails);
        }
    }

    public void showAllBooks() {
        try {
            List<LibraryBook> books=library.getAllBooks();
            System.out.println(books);
        }catch (RuntimeException e){
            System.out.println("Kitapları görüntülerken hata oluştu: "+e.getMessage());
        }
    }



    public void returnBookProcess(){
        try {
            Reader reader=currentUser;
            System.out.println("\nİade edilebilecek kitaplarınız:");
            reader.getMyBooks().forEach(this::displayBookDetails);

            System.out.println("\nİade etmek istediğiniz kitabın ID'si: ");
            Long bookId=scanner.nextLong();

            LibraryBook bookToReturn=reader.getMyBooks().stream()
                    .filter(b->b.getBookId().equals(bookId))
                    .findFirst()
                    .orElseThrow(()->new RuntimeException("Bu kitap sizde değil!"));
            String result=reader.returnBook(bookToReturn);
            System.out.println(result);

        }catch (RuntimeException e){
            System.out.println("İade Etme Hatası: "+e.getMessage());
        }
    }
    public void borrowBookProcess() {
        try {
            System.out.println("Kiralamak istediğiniz kitabın ID'si: ");
            Long bookId=scanner.nextLong();
            LibraryBook book=library.getBookById(bookId);

            Reader reader= currentUser;
            reader.borrowBook(book);
            System.out.println(book.getBookName()+" adlı kitap kiralandı.");

        }catch (RuntimeException e){
            System.out.println("Kiralama Hatası:"+e.getMessage());
        }
    }

    public void purchaseBookProcess(){
        try {
            System.out.println("Satın almak istediğiniz kitabın ID'si: ");
            Long bookId=scanner.nextLong();
            LibraryBook book=library.getBookById(bookId);

            Reader reader=currentUser;
            reader.purchaseBook(book);
            System.out.println(book.getBookName()+" adlı kitap satın alındı.");

        }catch (RuntimeException e){
            System.out.println("Satın Alma Hatası:"+e.getMessage());
        }
    }

    public void showBorrowedBooks(Reader reader){
        List<LibraryBook> borrowedBook=reader.getBorrowedBooks();
        if (borrowedBook.isEmpty()){
            System.out.println("Kiraladığınız kitap bulunmamaktadır.");
        }else {
            System.out.println("\n=== Kiralanan Kitaplar ===");
            borrowedBook.forEach(this::displayBookDetails);
        }
    }
    public void showPurchasedBooks(Reader reader){
        List<LibraryBook> purchasedBooks=reader.getPurchasedBooks();
        if (purchasedBooks.isEmpty()){
            System.out.println("Satın aldığınız kitap bulunmamaktadır.");
        }else {
            System.out.println("\n=== Satın Alınan Kitaplar ===");
            purchasedBooks.forEach(this::displayBookDetails);
        }
    }

    @Override
    public void addBalance(Reader reader) {
        System.out.print("Eklenecek miktar: ");
        double amount = scanner.nextDouble();
        reader.addBalance(amount);
    }

    @Override
    public double getBalance() {
        return 0;
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


    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
