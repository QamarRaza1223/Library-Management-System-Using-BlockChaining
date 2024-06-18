import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileWindow {

    private Customer user;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel southPanel;

    private Book[] bookList;

    private JPanel centerPanel;
    private JScrollPane scrollPane;

    private JList jList;
    public static final int width = 500;
    public static final int xCoord = 400;
    public static final int yCoord = 50;
    public static final int height = 400;


    public ProfileWindow(Customer user) {
        this.user = user;

        initializeFrame();
        initializeScrollPane();  // Call this method to set up the scroll pane
        frame.add(mainPanel);    // Add the mainPanel to the frame after initializing components
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);   // Add this line to close the program when the frame is closed

    }

    private void initializeFrame(){
        frame = new JFrame(user.getName());
        frame.setLocation(xCoord, yCoord);
        frame.setSize(width, height);
        frame.setVisible(true);

        mainPanel = new JPanel(new BorderLayout());

    }
    private void initializeScrollPane(){

        DefaultListModel<Book> model = new DefaultListModel<>();
mainPanel.setBounds(100,100,100,100);

        // Populate the model with book information
        for (Book book : user.getBorrowedBooks()) {
            model.addElement(book); // Assuming Book has a getTitle() method, replace it with the actual method
        }
        jList = new JList(model);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect a double-click
                    int selectedIndex = jList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Book selectedBook = model.getElementAt(selectedIndex);
                        // Prompt the user to confirm book return
                        int choice = JOptionPane.showConfirmDialog(frame,
                                "Do you want to return the book?\n" + selectedBook,
                                "Return Book",
                                JOptionPane.YES_NO_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            // Remove the book from the user's borrowed books
                            BlockChain.getChain().addReturnTransaction(user,selectedBook.getTitle());
                            // Remove the book from the JList model
                            model.removeElement(selectedBook);
                        }
                    }
                }
            }
        });
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Handle the selection change
                    int selectedIndex = jList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Book selectedBook = model.getElementAt(selectedIndex);
                        // Perform an action based on the selected element
                        System.out.println("Clicked on: " + selectedBook);
                    }
                }
            }
        });
        // Create a JScrollPane and set the JList as its viewport view
        scrollPane = new JScrollPane(jList);

        // Add a vertical scroll bar to the scroll pane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setViewportView(jList);
        mainPanel.add(scrollPane,BorderLayout.CENTER);
    }

}
