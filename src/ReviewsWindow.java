import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReviewsWindow {

    private JFrame frame;
    private JPanel mainPanel;
    private JPanel scrollPanel;
    private Book book;

    public static final int width = 500;
    public static final int height = 300;
    public static final int xCoord = Frame.width;
    public static final int yCoord = DetailsWindow.height;

    public ReviewsWindow(Book book) {
        this.book = book;
        System.out.println("From Review Window");

        initializeFrame();
        initializeScrollPanel();

        // adding mainPanel to frame
        frame.getContentPane().add(mainPanel);
    }

    private void initializeFrame(){
        // Frame Settings
        ImageIcon icon = new ImageIcon("src/PNGS/reviews.png");
        frame = new JFrame("Reviews");

        // This code is not necessary, but it makes it so that reviews and borrow windows close when the details window is closed
        frame.setIconImage(icon.getImage());
        frame.setLocation(xCoord,yCoord);
        frame.setSize(width, height);
        frame.setVisible(true);

        // Creating Main panel that has border layout
        // All other panels with be added to the mainPanel
        mainPanel = new JPanel(new BorderLayout());

    }


    private void initializeScrollPanel(){

        // Text for the book details
        LinkedList<Review> reviews = book.getReviews();





        // Using grid layout
        scrollPanel = new JPanel(new GridLayout(reviews.size(),1));
        scrollPanel.setBackground(Color.BLACK);

        // adding labels to the scrollPanel
        for(Review label: reviews){
            JLabel labels = new JLabel();
            // Use whatever font
            Font labelFont = new Font("Arial", Font.BOLD, 14);
            labels.setFont(labelFont);
            labels.setForeground(Color.WHITE);

            labels.setText(label.toString());
            scrollPanel.add(labels);

        }
        // creating and adding scroll pane
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void kill(){
        frame.dispose();
    }
}
