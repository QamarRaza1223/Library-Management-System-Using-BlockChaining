import java.util.ArrayList;
import java.util.LinkedList;

class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final String pubDate;
    private final String publisher;
    private final Genre genre;
    private LinkedList<Review> reviews;
    private float rating;
    private int sumRating;
    private boolean borrowed = false;
    // A local list of blocks to keep track of a certain book's borrow history
    private ArrayList<Block> transactions = new ArrayList<Block>();


    public Book(String isbn, String title, String author, String pubDate, String publisher, Genre genre) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.publisher = publisher;
        this.genre = genre;
        reviews = new LinkedList<>();
        rating = 0;
        sumRating = 0;
    }

    /**
     * adds a review to the list of reviews
     * updates the average rating by adjusting it with the new rating
     *
     * @param review
     */
    public void addReview(Review review) {
        reviews.add(review);
        calculateRating();
//        System.out.println("CALRATING: " + this.getReviewRating());
//        this.rating = (((reviews.size()-1) * this.getReviewRating()) + review.getRating())/ (reviews.size());
//        this.rating = ((this.rating * (this.reviews.size() - 2)) + rating) / this.reviews.size()-1;
//        this.rating = ((this.rating * (reviews.size() - 1)) + review.getRating()) / reviews.size();
//        System.out.println("NEWRATING: " + this.getReviewRating());
    }

    /**
     * @ Qamar
     * Calculates an average rating for the book
     * Called after the reviews arraylist is populated
     * Used for popularity heaptree
     */
    void calculateRating() {
        float sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        this.rating = sum / (float) reviews.size();
    }

    public void borrowBook(Customer CustomerDetails) {
        if (borrowed == true) {
            System.out.println("This book is already borrowed");
            return;
        }

        this.borrowed = true;
        if (transactions.size() == 0) {
            // Stores the value of the first transaction in the arrayList, the first transaction has previous blockHash 0
            transactions.add(new Block(CustomerDetails.getName() + " has borrowed the book " + this.title + " by " + this.author + " on time " + System.currentTimeMillis(), 0));
            // This adds the borrowedBook in the customer's ArrayList
            CustomerDetails.addBorrowedBook(this);
            return;
        }

        // All the other transaction's previous Hashcode is dependent on the previous input, creating a chain.
        transactions.add(new Block(CustomerDetails.getName() + " has borrowed the book " + this.title + " by " + this.author + " on time " + System.currentTimeMillis(), transactions.size() - 1));
        CustomerDetails.addBorrowedBook(this);
    }

    public void returnBook(Customer CustomerDetails) {
        transactions.add(new Block(CustomerDetails.getName() + " has returned the book " + this.title + " by" + this.author + " on time" + System.currentTimeMillis(), transactions.size() - 1));
        borrowed = false;
    }

    public void addReview(String review, int rating) {
        addReview(new Review(review, rating));
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public String getISBN() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre.toString();
    }

    public LinkedList<Review> getReviews() {
        return reviews;
    }


    public float getReviewRating() {
        return rating;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void printTransactions() {
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i).getTransactions());
        }
    }

    @Override
    public String toString() {
        // Reviews not returned
//        return "" + isbn + ", " + title + ", " + author + ", " + pubDate + ", " + publisher + ", " + getGenre() + reviews + ", " + getReviewRating() + "";
//        return "" + isbn + ", " + title + ", " + author + ", " + pubDate + ", " + publisher + ", " + getGenre() + ", " + getReviewRating() + "";
        return title + "  -  " + author;
    }

    public String toString(int i) {
        return ">> " + isbn + " > " + title + " > " + author + " > " + pubDate + " > " + publisher + " > " + getGenre() + " > " + getReviewRating() + "<<";
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }
}
