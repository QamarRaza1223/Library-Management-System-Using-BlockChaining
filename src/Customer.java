import java.util.ArrayList;

public class Customer {
    private String name;
    private int id;
    private int signature;

    private ArrayList<Book> borrowedBooks = new ArrayList<Book>();

    public Customer(String name, int ID) {
        this.name = name;
        this.id = ID;
        signature = (name + ID).hashCode();
    }

    /**
     * @param signature
     * @return true if parameter is equal to the signature
     */
    public boolean verifySignature(int signature) {
        return (this.signature == signature);
    }

    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public String toString() {
        return name + " > " + id + " > " + borrowedBooks;
    }


}
