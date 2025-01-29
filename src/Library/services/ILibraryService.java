package Library.services;

public interface ILibraryService {
    void start();
    void login();
    void logout();
    boolean isAuthenticated();
}
