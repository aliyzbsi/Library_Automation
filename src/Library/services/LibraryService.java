package Library.services;

import Library.Library;
import Library.entity.Person;

import Library.entity.Reader;
import Library.entity.Librarian;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryService implements ILibraryService {
    private final Library library;
    private final Scanner scanner;
    private Person currentUser;
    private final ReaderService readerService;
    private final LibrarianService librarianService;
    private boolean running=true;

    public LibraryService() {
        this.library=Library.getInstance();
        this.scanner=new Scanner(System.in);
        this.readerService=new ReaderService(scanner,library);
        this.librarianService=new LibrarianService(scanner,library);
    }

    @Override
    public void start() {
        while (running){
            try {
                if (!isAuthenticated()){
                    login();
                }else {
                    showMainMenu();
                }
            } catch (RuntimeException e) {
                System.out.println("Hata: "+e.getMessage());
            } catch (Exception e) {
                System.out.println("BeklenmeyenHata: "+e.getMessage());
                scanner.nextLine();
            }
        }
    }



    @Override
    public void login() {
        while (running&&!isAuthenticated()){

            try {
                System.out.println("\n=== Kütüphane Sistemi Giriş ===");
                System.out.println("1. Okuyucu Girişi");
                System.out.println("2. Kütüphaneci Girişi");
                System.out.println("3. Çıkış");
                System.out.println("\nGiriş tipi seçiniz: ");
                int choice=scanner.nextInt();
                scanner.nextLine();
                switch (choice){
                    case 1:
                        readerLogin();
                        break;
                    case 2:
                        librarianLogin();
                        break;
                    case 3:
                        running=false;
                        System.out.println("Sistemden çıkılıyor..");
                        System.exit(0);

                    default:
                        System.out.println("Geçersiz seçim!");
                }
            }catch (InputMismatchException e) {
                System.out.print("Lütfen geçerli bir sayı giriniz: ");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Beklenmeyen Hata: "+e.getMessage());
                scanner.nextLine();
            }

        }

    }

private void readerLogin() {
        try {
            System.out.print("Okuyucu ID: ");
            Long readerId = Long.valueOf(scanner.nextLine());
            System.out.println("Şifre: ");
            String password=scanner.nextLine();

            Reader reader = library.getReader(readerId);

            if (reader != null&&reader.verifyPassword(password)) {
                currentUser = reader;
                readerService.setCurrentUser(reader);
                System.out.println("Hoş geldiniz, " + reader.getName());
            } else {
                System.out.println("Geçersiz Kullanıcı Bilgileri!");
            }
        }catch (RuntimeException e) {
            System.out.println("Hata: "+e.getMessage());
        } catch (Exception e) {
            System.out.println("BeklenmeyenHata: "+e.getMessage());
            scanner.nextLine();
        }

}
private void librarianLogin() {
        try {
            System.out.print("Kütüphaneci ID: ");
            Long librarianId = Long.valueOf(scanner.nextLine());
            System.out.print("Şifre: ");
            String password = scanner.nextLine();

            Librarian librarian = library.getLibrarian(librarianId);
            if (librarian != null && librarian.verifyPassword(password)) {
                currentUser = librarian;
                librarianService.currentUser(librarian);
                System.out.println("Hoş geldiniz, " + librarian.getName());

            } else {
                System.out.println("Geçersiz kullanıcı bilgileri!");
            }
        }catch (RuntimeException e) {
            System.out.println("Hata: "+e.getMessage());
        } catch (Exception e) {
            System.out.println("BeklenmeyenHata: "+e.getMessage());
            scanner.nextLine();
        }

}

    @Override
    public void logout() {
        if (currentUser != null) {
            System.out.println("Çıkış yapıldı, Güle Güle: " + currentUser.getName());

            if (currentUser instanceof Librarian) {
                librarianService.currentUser(null);
            } else if (currentUser instanceof Reader) {
                readerService.setCurrentUser(null);
            }
            currentUser = null;
        }
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser!=null;
    }


    private void showMainMenu() {
        while (true) {
            if (currentUser instanceof Librarian) {
                librarianService.showLibrarianMenu();

                if (!librarianService.isAuthenticated()) {
                    logout();
                    return;
                }
            } else if (currentUser instanceof Reader) {
                readerService.showReaderMenu();

                if (!readerService.isAuthenticated()) {
                    logout();
                    return;
                }
            }
        }
    }


}
