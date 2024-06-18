enum Genre {

    // If there is a book with multiple genres then we insert it in each genre index. the integers are the index values.
    ScienceFiction (0),
    Fantasy (1),
    Mystery (2),
    Thriller (3),
    HistoricalFiction (4),
    Horror (5),
    Biography (6),
    Selfhelp (7),
    Romance (8),
    YoungAdult (9);

    private int value;

    Genre (int value){
        this.value=value;
    }

    public int getValue() {
        return this.value;
    }

}
