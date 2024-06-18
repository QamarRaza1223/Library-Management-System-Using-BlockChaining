import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Library {

    private static Library instance;

    private Hashtable<String, Book> books;
    private int bookCount;
    private Book[] latestList;
    private int latestDeleted;
    private Book[] mostPopularity;
    private Book[] leastPopularity;
    private ArrayList<Book>[] genreList;            // 0-9 index where each is a genre. Genre is an enum

    // @ Qamar = TreeSet seems ideal for popularity as inorder traversal returns sorted by highest popularity
    // Also TreeSet allows us to reverse in O(n) hence we don't need 2 separate Data Structure for most and least popular
    private TreeSet<Book> popularity;
    public Hashtable<String, Customer> customers;
    private ArrayList<Review> randomReviews;
    private ArrayList<Block> transactions = new ArrayList<Block>();
    private BlockChain chain;

    /**
     * genre is an array of arraylists
     * each index corresponds to a specific genre
     * the index for every genre is the same as its enum value
     * <p>
     * latest is a simple array which contains books published in 2020 or 2021
     */
    private Library() {
        initializeLists();
        populateRandomReviews();
        populateBooks();
        populateCustomers();
        updateMostPopular();
        updateLeastPopular();

        System.out.println("Book count: " + bookCount);
    }

    public BlockChain getChain() {
        return chain;
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private void initializeLists() {
        books = new Hashtable<>();
        bookCount = 0;
        customers = new Hashtable<>();
        genreList = new ArrayList[10];
        latestList = new Book[30];
        latestDeleted = 0;
        mostPopularity = new Book[25];
        leastPopularity = new Book[25];
        randomReviews = new ArrayList<>();
        //@ Qamar =  To convert the TreeSet into a MaxHeap we need a comparator
        // reversing a TreeSet creates a Max HeapTree(I think)
        popularity = new TreeSet<>(new ReversedMaxHeapComparator());

        for (int i = 0; i < genreList.length; i++) {
            genreList[i] = new ArrayList<>();
        }
    }

    /**
     * reads the text file
     * adds a new book to the lists
     */
    private void populateBooks() {
        try {
            File file = new File("src/data/books.txt");
            Scanner in = new Scanner(file);

            if (file.exists()) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();

                    if (line.contains(", ")) {
                        continue;                   // goes to the next iteration if the line contains ", " to avoid conflicts in splitting
                    }

                    String[] attributes = line.split(",");
                    addBook(attributes);
                }

            } else {
                System.out.println("Library/populateBooks()         File does not exist.");
            }

            in.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Library/populateBooks()         File Not Found Exception.");
        }

    }

    /**
     * reads reviews from a text file and fills an array
     * the array contains reviews which can be assigned to books
     */
    private void populateRandomReviews() {
        try {
            File file = new File("src/data/reviews.txt");
            Scanner in = new Scanner(file);

            if (file.exists()) {
                while (in.hasNextLine()) {
                    String[] reviewElements = in.nextLine().split(">");
                    randomReviews.add(new Review(reviewElements[0], Integer.parseInt(reviewElements[1])));
                }
            }

            in.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Library/generateReview()    Review file not found");
        }
    }

    /**
     * Reads customers text file and populates customers hashmap
     *
     * @ NEED TO FIX
     * NOT ALL CUSTOMERS ARE BEING ADDED
     */
    public void populateCustomers() {

        try {
            File file = new File("src/data/customers.txt");
            Scanner in = new Scanner(file);

            if (file.exists()) {
                while (in.hasNextLine()) {
                    String[] customerElements = in.nextLine().split(",");

                    // Adding Customers to customers HashMap
                    // Key made using customers name+ID
                    Customer customer = new Customer(customerElements[0], Integer.parseInt(customerElements[1]));
                    customers.put((customerElements[0] + customerElements[1]), customer);
                }
            }

            in.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Library/populateCustomers()    Customers file not found");
        }
    }

    /**
     * calls addBook()
     *
     * @param attributes array containing attributes of a book
     */
    private void addBook(String[] attributes) {
        if (attributes.length == 6) {
            addBook(attributes[0], attributes[1], attributes[2], attributes[3], attributes[4], attributes[5]);
        } else {
//            System.out.println("Library/addBook()        Invalid array");
        }
    }

    /**
     * add to the Big General Storage
     * checks the book's publishing date, genre and popularity and updates the pointers in those arrays appropriately
     */
    public void addBook(String isbn, String title, String author, String pubDate, String publisher, String genre) {
        Book book = new Book(isbn, title, author, pubDate, publisher, getGenre(genre));
        bookCount++;

        // adding to main storage
//        books.putIfAbsent(calculateKey(title), book);
        books.putIfAbsent(book.getTitle(), book);
//        System.out.println(books.putIfAbsent(calculateKey(title), book));

        /**
         SOME BOOKS ARE NOT BEING ADDED TO THE BOOK
         THEY GET REVIEWS AND A RATING ASSIGNED BUT THEY BECOME "NaN" FOR SOME REASON
         THIS MESSES UP THE MOST POPULAR ARRAY
         THESE BOOKS DO EXIST IN OTHER ARRAYS I.E. GENRE AND POPULARITY BUT NOT IN THE BGS
         **/

//        if (books.get(calculateKey(book.getTitle())) != null) {     // ///// !!!!!! TEMPORARY CONDITION !!!!!! /////

        // updating latest
        if (isLatest(pubDate)) {
            for (int i = 0; i < latestList.length; i++) {
                if (latestList[i] == null) {                    // fills a null node
                    latestList[i] = book;
                    break;
                }
            }
        }

        // updating genre
        switch (getGenre(genre)) {
            case ScienceFiction -> this.genreList[0].add(book);
            case Fantasy -> this.genreList[1].add(book);
            case Mystery -> this.genreList[2].add(book);
            case Thriller -> this.genreList[3].add(book);
            case HistoricalFiction -> this.genreList[4].add(book);
            case Horror -> this.genreList[5].add(book);
            case Biography -> this.genreList[6].add(book);
            case Selfhelp -> this.genreList[7].add(book);
            case Romance -> this.genreList[8].add(book);
            case YoungAdult -> this.genreList[9].add(book);
        }

        // @ Qamar = I change the number of reviews to from 3-5 inclusive
        // these leads to greater variation of average rating and hence better sorting by rating
        int reviewAmount = (int) (3 + (Math.random() * 6));
        for (int i = 0; i < reviewAmount; i++) {
            getBook(book.getTitle()).addReview(getRandomReview());
        }

        // @ Qamar  =  calculating average rating of the book
        // I changed Shaz's average rating method.
        book.calculateRating();

        // @ Qamar = adding book to popularity
        popularity.add(book);

       /* System.out.println("Library/addBooks()  added >" + book.toString());
        System.out.println("Library/addBooks()  TITLE >" + getBook(book.getTitle()).getTitle());
        System.out.println("Library/addBooks()  TITLE GENRE >" + genreList[Objects.requireNonNull(getGenre(genre)).getValue()].get(genreList[getGenre(genre).getValue()].size() - 1));

        */
//        }

        // For the addition by popularity the Heaptree and the review addition needs to be done first. This as lazy as I am, will leave to you guys.:D
    }

    public void deleteBook(String title) {
        //@ Qamar = first remove from popularity then remove from BGS
        // other way round gives null error

//        popularity.remove(books.get(calculateKey(title)));
        popularity.remove(title);
//        books.remove(calculateKey(title));
        books.remove(title);
        latestDeleted++;
        updateLatest();
        updatePopularLists();
    }

//    public ArrayList<Book> search(String title) {
//
//    }

    /**
     * goes through the popularity arrays
     * calls the update function if they contain a null pointer
     */
    private void updatePopularLists() {
        for (int i = 0; i < mostPopularity.length; i++) {
            if (mostPopularity[i] == null) {
                updateMostPopular();
            }
        }
        for (int i = 0; i < leastPopularity.length; i++) {
            if (leastPopularity[i] == null) {
                updateLeastPopular();
            }
        }
    }

    /**
     * @ Qamar
     * this fills the mostPopular array(10) with the most popular books
     */
    private void updateMostPopular() {
        int index = 0;
        for (Book book : popularity) {
            if (book.getReviewRating() > 0) {
                mostPopularity[index++] = book;
            }

            if (index == mostPopularity.length) {
                break;
            }
        }
    }

    /**
     * @ Qamar
     * this fills the leastPopular array(10) with the most popular books
     */

    private void updateLeastPopular() {
        int index = 0;
        // @ Qamar = this converts the TreeSet which by default is sorted by most popular to sorted by least popular
        // this method is O(1)
        TreeSet<Book> descendingSet = (TreeSet<Book>) popularity.descendingSet();
        for (Book book : descendingSet) {
            leastPopularity[index++] = book;

            if (index == leastPopularity.length) {
                break;
            }
        }
    }


    /**
     * goes through the latestList to check if an entry is null
     * if a null entry is found, the node is updated to a new latest book
     */
    private void updateLatest() {
        if (latestDeleted > 5) {
            for (int i = 0; i < latestList.length; i++) {
                if (latestList[i] == null) {
                    while (true) {
                        int randomGenre = (int) (Math.random() * 10);
                        int genreLength = genreList[randomGenre].size();
                        Book randomBook = genreList[randomGenre].get((int) (Math.random() * genreLength));
                        if (isLatest(randomBook.getPubDate())) {
                            latestList[i] = randomBook;
                            latestDeleted--;
                            return;
                        }
                    }
                }
            }
        } else {
            // We have room to not update the list
        }

    }

    /**
     * goes through all the books stored to get books containing the parameter
     *
     * @param title
     * @return arraylist of books containing the paramter in their title
     */
    public ArrayList<Book> searchBook(String title) {
        ArrayList<Book> search = new ArrayList<>();

        for (int i = 0; i < genreList.length; i++) {
            for (int j = 0; j < genreList[i].size(); j++) {
                if (genreList[i].get(j).getTitle().contains(title)) {
                    search.add(genreList[i].get(j));
                }
            }
        }
        return search;
    }

    /**
     * checks if the pubDate input is latest i.e. 2002 or newer
     *
     * @param pubDate
     * @return true if latest, false otherwise
     */
    private boolean isLatest(String pubDate) {
        if (Integer.parseInt(pubDate) > 2002) {
            return true;
        }
        return false;
    }

//    /**
//     * calculates the hash key of the input
//     *
//     * @param title
//     * @return hash key
//     */
//    private int calculateKey(String title) {
//        int key = 0;
//        for (int i = 0; i < title.length(); i++) {
//            key += title.codePointAt(i);
//        }
//
//        return key;
//    }

    /**
     * @return a random review from the list of reviews
     */
    private Review getRandomReview() {
        return randomReviews.get((int) (Math.random() * randomReviews.size()));
    }


    /**
     * returns an array of 10 books according to the genre input
     * <p>
     * the array is filled by going through the arrayList of the genre input
     * random books from the array are selected
     *
     * @param genre
     */
    public Book[] getSortedByGenreBooks(Genre genre) {
        int value = genre.getValue();
        Book[] sortedBooks = new Book[10];

        for (int i = 0; i < sortedBooks.length; i++) {
            sortedBooks[i] = genreList[value].get((int) (Math.random() * genreList[value].size()));
        }

        return sortedBooks;
    }

    /**
     * @return array of 25 random books
     */
    public Book[] getRandomBooks() {
        Book[] books = new Book[25];
        for (int i = 0; i < books.length; i++) {
            int genre = (int) (Math.random() * genreList.length);         // random genre
            books[i] = genreList[genre].get((int) (Math.random() * genreList[genre].size()));
        }

        return books;
    }

    public Book[] getGenreBooks(int genre) {
        return genreList[genre].toArray(new Book[0]);
    }

    private Genre getGenre(String genre) {
        if (genre.equalsIgnoreCase("Science Fiction")) {
            return Genre.ScienceFiction;
        } else if (genre.equalsIgnoreCase("Fantasy")) {
            return Genre.Fantasy;
        } else if (genre.equalsIgnoreCase("Mystery")) {
            return Genre.Mystery;
        } else if (genre.equalsIgnoreCase("Thriller")) {
            return Genre.Thriller;
        } else if (genre.equalsIgnoreCase("Historical Fiction")) {
            return Genre.HistoricalFiction;
        } else if (genre.equalsIgnoreCase("Horror")) {
            return Genre.Horror;
        } else if (genre.equalsIgnoreCase("Biography")) {
            return Genre.Biography;
        } else if (genre.equalsIgnoreCase("Self-help")) {
            return Genre.Selfhelp;
        } else if (genre.equalsIgnoreCase("Romance")) {
            return Genre.Romance;
        } else if (genre.equalsIgnoreCase("Young Adult")) {
            return Genre.YoungAdult;
        }
        return null;
    }

    /**
     * hashes the title and returns the book from the books hashTable
     */
    public Book getBook(String title) {
//        return books.get(calculateKey(title));
        return books.get(title);
    }

    public Customer getCustomer(String name, String id) {
        for (Customer customer : customers.values()) {
            if ((customer.getName() + customer.getID()).hashCode() == (name + id).hashCode()) {
                System.out.println(customer);
                return customer;
            }
        }

        return null;
    }

    /**
     * @return 25 books to display in Frame
     */
    public Book[] getLatestBookList() {
        Book[] temp = new Book[25];

        for (int i = 0; i < latestList.length; i++) {
            if (latestList[i] != null) {
                temp[i] = latestList[i];
            }
            if (temp[temp.length - 1] != null) {
                break;
            }
        }
        return temp;
    }

    public Book[] getLatestBooks() {
        return latestList;
    }

    public Book[] getMostPopular() {
        return mostPopularity;
    }

    public Book[] getLeastPopular() {
        return leastPopularity;
    }

    public void printBooks() {
        books.toString();
    }

    private static class ReversedMaxHeapComparator implements Comparator<Book> {
        @Override
        public int compare(Book o1, Book o2) {
            // Compare function called by prebuilt TreeSet when traversing
            float diff = o2.getReviewRating() - o1.getReviewRating();

            // TreeSet does not store duplicate floats so if average rating of both books are same we return difference of hashcode
            // no real purpose of HashCodes it just is a way to differentiate between books that have same average rating
            if (diff == 0) {
                return o1.hashCode() - o2.hashCode();
            }


            if (diff > 0) {
                return 1;
            } else {
                return -1;
            }

        }
    }

}

