package Library.entity;

import java.util.ArrayList;
import java.util.List;

public class Author extends Person {
    private Long authorId;
    private List<LibraryBook> libraryBooks;

    public Author(String name, List<LibraryBook> libraryBooks) {
        super(name);
        this.libraryBooks = libraryBooks;
    }

    @Override
    public String whoYouAre() {
        return "Author";
    }

    public void newBook(LibraryBook libraryBook){
        if(libraryBooks == null){
            libraryBooks = new ArrayList<>();
        }
        libraryBooks.add(libraryBook);
    }

    public List<LibraryBook> showBook(){
        return this.libraryBooks;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
