import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        setUpUI();
        Library library = Library.getInstance();

//        Library library = new Library();
//           printBookArray(library.getLatestBooks());
//
//        // @ Qamar = this retrieves a 10 book array from mostPopular array and prints
//        printBookArray(library.getMostPopularity());
//
//        // @ Qamar = this retrieves a 10 book array from leastPopular array and prints
//        printBookArray(library.getLeastPopularity());
//
////        printBookArray(library.getSortedByGenreBooks(Genre.Mystery));y
//
//        System.out.println();
//        Customer customer = new Customer("Muddasir", 15);
//        BlockChain chain = BlockChain.getChain();
//        chain.addBorrowTransaction(customer,"Scarlet Letter");
//        chain.addBorrowTransaction(customer,"Scarlet Letter");
//        chain.addBorrowTransaction(customer,"Secret History");
//        chain.addBorrowTransaction(customer,"Deception Point");
//        chain.addReturnTransaction(customer,"Scarlet Letter");
//        chain.addReturnTransaction(customer,"dasdasd");
//        System.out.println(customer.getBorrowedBooks());

//        printBookArray(library.getLatestBooks());

//        Book wildAnimus = library.getBook("Wild Animus");
//        wildAnimus.isBorrowed();

    }

    private static void printBookArray(Book[] books) {
        System.out.println("\n\n PRINTING \n");
        for (Book book : books) {
            System.out.println(book.toString(1));
        }
    }

    private static void setUpUI() {
        GUI gui = new GUI();
    }

}