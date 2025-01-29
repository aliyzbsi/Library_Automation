package Library.entity;

public class Librarian extends Person{
    private  Long librarianId;
    private String password="iamlibrarian";

    public Librarian(String name, Long librarianId) {
        super(name);
        this.librarianId = librarianId;

    }

    @Override
    public String whoYouAre() {
        return "Librarien";
    }

    public Long getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Long librarianId) {
        this.librarianId = librarianId;
    }

    public String getPassword() {
        return password;
    }

    public boolean verifyPassword(String password) {
        return password.equals("iamlibrarian");
    }
}
