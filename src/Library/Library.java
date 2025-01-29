package Library;


import Library.entity.*;

import java.util.*;

public class Library {
    private static Library instance;
    private final Map<Long, LibraryBook> libraryBook;
    private final Map<Long, Reader> libraryReaders;
    private final Map<Long, Librarian> librariens;
    private final Map<Long,Author> authors;

    public Library() {
        this.libraryBook=new HashMap<>();
        this.libraryReaders=new HashMap<>();
        this.librariens=new HashMap<>();
        this.authors=new HashMap<>();
    }
    private Long getNextBookId(){
        if(libraryBook.isEmpty()){
            return 1L;
        }
        return Collections.max(libraryBook.keySet())+1;
    }

    private Long getNextAuthorId() {
        if (authors.isEmpty()) {
            return 1L;
        }
        return Collections.max(authors.keySet()) + 1;
    }

    private Long getNextReaderId() {
        if (libraryReaders.isEmpty()) {
            return 1L;
        }
        return Collections.max(libraryReaders.keySet()) + 1;
    }
    private Long getNextLibrarianId() {
        if (librariens.isEmpty()) {
            return 1L;
        }
        return Collections.max(librariens.keySet()) + 1;
    }

    public static Library getInstance() {
        if(instance==null){
            instance=new Library();
        }
        return instance;
    }

    public void addBook(LibraryBook libraryBook){
        if(libraryBook.getBookId()==null){
            libraryBook.setBookId(getNextBookId());
        }

        Author author = libraryBook.getAuthor();
        if (author.getAuthorId() == null) {
            author.setAuthorId(getNextAuthorId());
        }
        authors.putIfAbsent(author.getAuthorId(), author);

        this.libraryBook.put(libraryBook.getBookId(), libraryBook);
    }

    public void updatedBook(LibraryBook book){
        if(book==null||book.getBookId()==null){
            throw new RuntimeException("Güncellenecek kitap bilgisi geçersiz!");
        }
        LibraryBook existingBook=getBookById(book.getBookId());
        if (existingBook.getStatus()!= Status.AVAILABLE){
            throw new RuntimeException("Kitap şu anda " +
                    (existingBook.getStatus() == Status.BORROWED ? "ödünç alınmış" : "satılmış") +
                    " durumda olduğu için güncellenemez!");
        }
        Author updatedAuthor=book.getAuthor();
        if (updatedAuthor != null) {
            if (updatedAuthor.getAuthorId() == null) {
                updatedAuthor.setAuthorId(getNextAuthorId());
            }
            authors.putIfAbsent(updatedAuthor.getAuthorId(), updatedAuthor);
        }
        libraryBook.put(book.getBookId(),book);

    }

    public void removeBook(Long bookId){

        LibraryBook book=getBookById(bookId);
        if (book.getStatus() != Status.AVAILABLE) {
            throw new RuntimeException("Kitap şu anda " +
                    (book.getStatus() == Status.BORROWED ? "ödünç alınmış" : "satılmış") +
                    " durumda olduğu için silinemez!");
        }
        libraryBook.remove(book.getBookId(),book);
    }

    public void removeAuthor(Long authorId) {
        Author author = getAuthorById(authorId);

        boolean hasBooks = libraryBook.values().stream()
                .anyMatch(book -> book.getAuthor().getAuthorId().equals(authorId));

        if (hasBooks) {
            throw new RuntimeException("Bu yazarın kitapları olduğu için silinemez!");
        }

        authors.remove(authorId);
    }

    public void updateAuthor(Author updatedAuthor) {

        Author existingAuthor = getAuthorById(updatedAuthor.getAuthorId());
        authors.put(updatedAuthor.getAuthorId(), updatedAuthor);


        libraryBook.values().stream()
                .filter(book -> book.getAuthor().getAuthorId().equals(updatedAuthor.getAuthorId()))
                .forEach(book -> book.setAuthor(updatedAuthor));
    }

    private Author getAuthorById(Long authorId) {
        return Optional.ofNullable(authors.get(authorId))
                .orElseThrow(() -> new RuntimeException("ID: " + authorId + " olan yazar bulunamadı!"));
    }

    public LibraryBook getBookById(Long bookId) {
        return Optional.ofNullable(libraryBook.get(bookId))
                .orElseThrow(() -> new RuntimeException("ID: " + bookId + " olan kitap kütüphanede bulunamadı!"));
    }

    public List<LibraryBook> searchBooksByName(String bookName) {
        return libraryBook.values()
                .stream()
                .filter(libraryBook -> libraryBook.getBookName().toLowerCase().contains(bookName.toLowerCase()))
                .toList();
    }

    public List<LibraryBook> searchBooksByAuthor(String authorName) {
        return libraryBook.values()
                .stream()
                .filter(libraryBook -> libraryBook.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .toList();
    }

    public Optional<Author> findAuthorByName(String authorName){
         return authors.values().stream().
                 filter(yazar->yazar.getName().equalsIgnoreCase(authorName.trim()))
                 .findFirst();
    }

    public List<LibraryBook> getAllBooks(){

        return new ArrayList<>(libraryBook.values());
    }

    public void addReader(Reader reader){

        if(reader.getReaderId()==null){
            reader.setReaderId(getNextReaderId());
        }
        libraryReaders.put(reader.getReaderId(),reader);

    }
    public void removeReader(Long removedReaderId) {
        Reader reader= libraryReaders.get(removedReaderId);

       List<LibraryBook> book=reader.getBorrowedBooks();
       for (LibraryBook book1:book){
           reader.returnBook(book1);
       }
       libraryReaders.remove(reader.getReaderId(),reader);
    }
    public void updateReader(Reader updatedReader) {
        Reader existingReader = getReader(updatedReader.getReaderId());
        libraryReaders.put(updatedReader.getReaderId(), updatedReader);


    }

    public void addLibrarian(Librarian librarian){
        if (librarian.getLibrarianId()==null){
            librarian.setLibrarianId(getNextLibrarianId());
        }
        librariens.put(librarian.getLibrarianId(),librarian);
    }

    public Reader getReader(Long readerId){
        return libraryReaders.get(readerId);
    }

    public Librarian getLibrarian(Long librarianId){
        return librariens.get(librarianId);
    }
    public List<Author> getAllAuthors(){
        return new ArrayList<>(authors.values());
    }



}
