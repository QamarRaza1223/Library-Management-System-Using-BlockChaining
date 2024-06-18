class Review {

    private String review;
    private int rating;

    public Review(String review, int rating) {
        if (rating >= 0 && rating <= 5) {
            this.review = review;
            this.rating = rating;
        } else {
            System.out.println("Review/Review()      invalid review // not adding");
        }
    }

    public String getReview() {
        return review;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return ">> " + review + " > " + rating + " <<";
    }
}
