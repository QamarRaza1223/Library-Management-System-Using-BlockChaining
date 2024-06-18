import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Frame extends JFrame implements ActionListener, MouseListener {

    private JButton surprise, submit;
    private Timer successTimer;
    private JToggleButton showTransactionsButton;
    private JPanel transactionPanel;
    private JButton logoutButton = new JButton("Confirm");
    private JToggleButton title;
    private JToggleButton genre;
    private JToggleButton mostPopular;
    private JToggleButton leastPopular;
    private JToggleButton latest;
    private JToggleButton login;
    private JToggleButton borrowedBooksHistory;
    private ButtonGroup buttonGroup;
    private JTextField searchBar, nameField, passwordField;
    private JLabel errorMessage = new JLabel("The username or Password is incorrect");
    private boolean loggedIn = false;
    JPanel loginPanel;
    private TransactionsFrame transactionsFrame;
    private JScrollPane scrollPane;
    private DetailsWindow detailsWindow;
    private JWindow borrowWindow;

    private ProfileWindow profileWindow;
    private JWindow loginWindow = new JWindow(this);
    private JWindow logoutWindow = new JWindow(this);
    private JPanel borrowPanel;
    private JComboBox<String> genreMenu;
    private JList jList;

    public static final int xCoord = 0;
    public static final int yCoord = 0;
    public static final int width = 900;
    public static final int height = 600;
    private ImageIcon loginPNG;
    private ImageIcon logoutPNG;

    private int toggleX = 10;
    private int toggleY = 45;
    private int toggleWidth = 135;
    private int toggleHeight = 25;
    private Customer loggedInAs;
    private Book[] showCasedBooks;

    private Library library;
    private BlockChain chain;

    Frame() {
        this.setTitle("Library Management System");
        this.setVisible(true);
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        errorMessage.setBounds(70, 180, 300, 20);

        chain = BlockChain.getChain();
        library = chain.getLibrary();

        ImageIcon icon = new ImageIcon("src/PNGS/icon.png");
        loginPNG =  new ImageIcon(new ImageIcon("src/PNGS/Login.png").getImage().getScaledInstance(30, 25, Image.SCALE_DEFAULT));
        logoutPNG = new ImageIcon(new ImageIcon("src/PNGS/Logout.png").getImage().getScaledInstance(30, 25, Image.SCALE_DEFAULT));
        this.setIconImage(icon.getImage());
//        this.getContentPane().setBackground(new Color(184, 149, 96));

        addElements();
        addTransactionsButton();
        transactionsFrame = new TransactionsFrame();
    }

//    public static void main(String[] args) {
//        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//
//        System.out.println("Available Fonts:");
//        for (String font : fonts) {
//            System.out.println(font);
//        }
//    }

    private void addElements() {
        addButtons();

        searchBar = new JTextField();
        searchBar.setBounds(10, 10, 720, 30);
        searchBar.setVisible(true);
        this.add(searchBar);

//        JTextArea textArea = new JTextArea();
//        textArea.setFont(new Font("Calibri", Font.PLAIN, 14));

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 80, 800, 480);
        scrollPane.setVisible(true);
        scrollPane.setFont(new Font("Impact", Font.PLAIN, 14));     /// PROBLEM ERROR doesnt work
        this.add(scrollPane);

        ImageIcon ii = new ImageIcon();      /////////////////// temp ISSUE PROBLEM ERROR
        JLabel jlab = new JLabel(ii);
        jlab.setBounds(300, 300, 500, 500);
        jlab.setVisible(true);
        this.add(jlab);

        // Refresh the frame
        this.revalidate();
        this.repaint();
    }

    private void addButtons() {
        surprise = new JButton();
        surprise.setBounds(750, 10, toggleWidth, toggleHeight);
        surprise.setText("Surprise Me!");
        surprise.setFocusable(false);
        surprise.addActionListener(this);
        this.add(surprise);

        buttonGroup = new ButtonGroup();

        title = new JToggleButton();
        title.setBounds(toggleX, toggleY, toggleWidth, toggleHeight);
        title.setText("Title");
        setTButtonProperties(title);

//        author = new JToggleButton();
//        author.setBounds(toggleX + title.getX() + title.getWidth(), toggleY, toggleWidth, toggleHeight);
//        author.setText("Author");
//        setTButtonProperties(author);
//        author.setVisible(false);
//
        genre = new JToggleButton();
        genre.setBounds(toggleX + title.getX() + title.getWidth(), toggleY, toggleWidth, toggleHeight);
//        genre.setBounds(toggleX + author.getX() + author.getWidth(), toggleY, toggleWidth, toggleHeight);
        genre.setText("Genre");
        setTButtonProperties(genre);

        String[] genres = {"Science Fiction", "Fantasy", "Mystery", "Thriller", "Historical Fiction", "Horror", "Biography", "Self Help", "Romance", "Young Adult"};
        genreMenu = new JComboBox<>(genres);
        genreMenu.setBounds(toggleX + genre.getX() + genre.getWidth(), toggleY, toggleWidth, toggleHeight);
        genreMenu.setToolTipText("Genres");
        genreMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the genre selection event here
                String selectedGenre = (String) genreMenu.getSelectedItem();
                System.out.println("Selected Genre: " + selectedGenre);

                // You can perform additional actions based on the selected genre if needed
                // For example, update the displayed books based on the selected genre
                int genreIndex = genreMenu.getSelectedIndex();
                if (genreIndex != -1) {
                    populateScrollPane(library.getGenreBooks(genreIndex));
                }
            }
        });
        this.add(genreMenu);

        latest = new JToggleButton();
        latest.setBounds(toggleX + genreMenu.getX() + genreMenu.getWidth(), toggleY, toggleWidth, toggleHeight);
        latest.setText("Latest");
        setTButtonProperties(latest);

        mostPopular = new JToggleButton();
        mostPopular.setBounds(toggleX + latest.getX() + latest.getWidth(), toggleY, toggleWidth, toggleHeight);
        mostPopular.setText("Most Popular");
        setTButtonProperties(mostPopular);

        leastPopular = new JToggleButton();
        leastPopular.setBounds(toggleX + mostPopular.getX() + mostPopular.getWidth(), toggleY, toggleWidth, toggleHeight);
        leastPopular.setText("Least Popular");
        setTButtonProperties(leastPopular);

        login = new JToggleButton();
        login.setBounds(810, 490, (toggleWidth / 2) + 5, toggleHeight);
        login.setIcon(loginPNG);
        login.setText("Log\nIn");
        setTButtonProperties(login);

        borrowedBooksHistory = new JToggleButton();
        borrowedBooksHistory.setBounds(810, 450, (toggleWidth / 2) + 5, toggleHeight);
        borrowedBooksHistory.setIcon(logoutPNG);
        borrowedBooksHistory.setText("Books");
        setTButtonProperties(borrowedBooksHistory);


        surprise.setBounds(leastPopular.getX(), 12, toggleWidth, toggleHeight);
    }

    private void searchByTitle(String line) {
        Book[] books = new Book[1];
        books[0] = library.getBook(line);

        if (books[0] == null) {
            books = library.searchBook(line).toArray(new Book[0]);
        }
        populateScrollPane(books);
    }

    private void clearShowCasedBooks() {
        showCasedBooks = null;
    }

    private void populateScrollPane(Book[] books) {
        DefaultListModel<Book> model = new DefaultListModel<>();

        // Populate the model with book information
        for (Book book : books) {
            model.addElement(book); // Assuming Book has a getTitle() method, replace it with the actual method
        }

        // Check if the JList is already created

        jList = new JList(model);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                        if (detailsWindow == null) {
                            detailsWindow = new DetailsWindow(selectedBook);
                            detailsWindow.borrowButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    openBorrowMenu(selectedBook);
                                }
                            });
                        } else {
                            detailsWindow.kill();
                            detailsWindow = new DetailsWindow(selectedBook);
                            detailsWindow.borrowButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    openBorrowMenu(selectedBook);
                                }
                            });
                        }
                    }
                }
            }
        });

        scrollPane.setViewportView(jList);
    }

    private void closeBorrowMenu() {
        if (borrowWindow != null && borrowWindow.isVisible()) {
            borrowPanel.setVisible(false);
            borrowWindow.setVisible(false);
        }
    }
    private void addTransactionsButton() {
        showTransactionsButton = new JToggleButton();
        showTransactionsButton.setBounds(810, 340, (toggleWidth / 2) + 5, toggleHeight);
        showTransactionsButton.setText("Chain");
        setTButtonProperties(showTransactionsButton);

        showTransactionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleTransactionPanel();
            }
        });

        this.add(showTransactionsButton);

        // Create the transaction panel
        transactionPanel = new JPanel();
        transactionPanel.setLayout(null);
        transactionPanel.setBounds(10, 80, 800, 480);
        transactionPanel.setVisible(false);
        this.add(transactionPanel);
    }

    private void toggleTransactionPanel() {
        if (transactionsFrame.isVisible()) {
            transactionsFrame.setVisible(false);
            showTransactionsButton.setSelected(false);
        } else {
            showTransactions();
            transactionsFrame.setVisible(true);
            showTransactionsButton.setSelected(true);
        }
    }

    private void showTransactions() {
        ArrayList<Block> blockChain = BlockChain.getChain().getBlockChain();
        transactionsFrame.updateTransactions(blockChain);
    }
    private void openBorrowMenu(Book book) {

        borrowWindow = new JWindow(this);
        borrowWindow.setBounds(200, 200, 400, 200);
        borrowPanel = new JPanel();
        borrowPanel.setLayout(null);
        borrowPanel.setBackground(Color.BLACK);
        borrowPanel.setBounds(200, 200, 400, 200);

        if (loggedInAs == null) {
            JLabel confirmationText = new JLabel("You must Log in to borrow this Book");
            confirmationText.setForeground(Color.white);
            confirmationText.setBounds(87, 30, 300, 20);

            JButton confirmationButton = new JButton("Close");
            confirmationButton.setBounds(147, 110, 100, 30);
            confirmationButton.setBackground(Color.RED);
            confirmationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    borrowPanel.setVisible(false);
                    borrowWindow.setVisible(false);
                }


            });
            borrowPanel.add(confirmationText);
            borrowPanel.add(confirmationButton);
        } else {
            JLabel confirmationText = new JLabel("Are you sure you want to borrow this book?");
            confirmationText.setForeground(Color.white);
            confirmationText.setBounds(66, 35, 300, 20);
            JButton closeButton = new JButton("Close");
            closeButton.setBounds(250, 100, 100, 30);
            closeButton.setBackground(Color.RED);
            JButton confirmationButton = new JButton("Confirm");
            confirmationButton.setBackground(Color.GREEN);
            confirmationButton.setBounds(45, 100, 100, 30);
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    borrowPanel.setVisible(false);
                    borrowWindow.setVisible(false);
                }


            });
            confirmationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    // Refreshing Panel
                    borrowPanel.removeAll();
                    borrowPanel.repaint();
                    borrowPanel.revalidate();
                    borrowPanel.setBackground(Color.BLACK);

                    if (book.isBorrowed()) {
                        JLabel confirmationText = new JLabel("Book is Already Borrowed!");
                        confirmationText.setForeground(Color.red);
                        confirmationText.setBounds(120, 60, 250, 20);
                        borrowPanel.add(confirmationText);
                    } else {
                        chain.addBorrowTransaction(loggedInAs, book);
                        JLabel confirmationText = new JLabel("Success!");
                        confirmationText.setForeground(Color.green);
                        confirmationText.setBounds(170, 40, 300, 50);
                        borrowPanel.add(confirmationText);
                    }

                }


            });
            borrowPanel.add(closeButton);
            borrowPanel.add(confirmationText);
            borrowPanel.add(confirmationButton);
        }

        borrowPanel.setVisible(true);
        borrowWindow.add(borrowPanel);
        borrowWindow.setVisible(true);
        borrowWindow.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        successTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowPanel.setVisible(false);
                borrowWindow.setVisible(false);
                successTimer.stop();  // Stop the timer when the action is performed
            }
        });
        successTimer.setRepeats(false);  // Set it to fire only once
        successTimer.start();

        // This code make the X button on confirmation windows functions
        borrowWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                borrowPanel.setVisible(false);
                borrowWindow.setVisible(false);
            }
        });


        this.revalidate();
        this.repaint();
    }

    private void setTButtonProperties(JToggleButton jtb) {
        jtb.setVisible(true);
        jtb.setFocusable(false);
        jtb.addActionListener(this);
        this.add(jtb);
        buttonGroup.add(jtb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        library = Library.getInstance();

        try {
            if (surprise.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
                clearShowCasedBooks();
//            searchByTitle(searchBar.getText());
                populateScrollPane(library.getRandomBooks());
//                System.out.println(title.isSelected());

            } else if (title.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
                clearShowCasedBooks();
                searchByTitle(searchBar.getText());
//                System.out.println(title.isSelected());
//            System.out.println(author.isSelected());

//        } else if (author.equals(source)) {
//            closeLoginWindow();
//            clearShowCasedBooks();

            } else if (genre.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
//            populateScrollPane(library.getSortedByGenreBooks(Genre.Horror));
                genreMenu.setPopupVisible(true);

            } else if (latest.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
                populateScrollPane(library.getLatestBookList());
//                System.out.println("Latest");

            } else if (mostPopular.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
                populateScrollPane(library.getMostPopular());
//                System.out.println("MOST POP");
            } else if (leastPopular.equals(source)) {
                closeLoginWindow();
                closeLogoutWindow();
                closeBorrowMenu();
                populateScrollPane(library.getLeastPopular());
//        } else if (author.equals(source)) {
//            closeLoginWindow();
            } else if (login.equals(source)) {
                if (loggedInAs == null) {

                    openLoginWindow();
                } else {
                    openLogoutWindow();
                }

            } else if (borrowedBooksHistory.equals(source)) {
                if (loggedInAs != null) {
                    profileWindow = new ProfileWindow(loggedInAs);
                }

            } else if (submit.equals(source)) {
//            System.out.println("Works");
                loggedInAs = library.getCustomer(nameField.getText(), passwordField.getText());
                System.out.println(nameField.getText() + " " + passwordField.getText());
                if ((loggedInAs == null)) {
//                System.out.println("Its working");
                    loginPanel.add(errorMessage);
                    loginWindow.repaint();
                } else {
                    System.out.println("Successfully logged in");
                    this.setTitle("Library Management System: " + loggedInAs.getName());
                    login.setText("Logout");
                    closeLoginWindow();
                }
            } else if (logoutButton.equals(source)) {
//                System.out.println("works");
                login.setText("Login");
                loggedInAs = null;
                this.setTitle("Library Management System");
                logoutWindow.setVisible(false);

            }
        } catch (NullPointerException ex) {

        }
    }

    public void closeLoginWindow() {
        if (loginWindow.isVisible()) {
            loginWindow.setVisible(false);
            loginWindow = new JWindow(this);
            loginPanel = new JPanel();
        }
    }

    public void closeLogoutWindow() {
        if (logoutWindow.isVisible()) {
            logoutWindow.setVisible(false);
            logoutWindow = new JWindow(this);
        }
    }

    public void openLogoutWindow() {
        logoutWindow.setBounds(200, 200, 300, 200);
        JPanel panel = new JPanel();
        panel.setLayout(null); // Use null layout for manual component placement
        JLabel confirmationText = new JLabel("Do you want to Log Out?");
        confirmationText.setBounds(40, 20, 300, 50);

        logoutButton.setBounds(100, 100, 100, 50);
        panel.add(confirmationText);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        panel.add(logoutButton);
        logoutButton.addActionListener(this);
        logoutWindow.add(panel);
        logoutWindow.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        logoutWindow.setVisible(true);


    }

    public void openLoginWindow() {

        loginWindow.setBounds(200, 200, 400, 200);

        // Create text fields for name and password
        nameField = new JTextField();
        passwordField = new JPasswordField();
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore the event if the character is not a digit
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // No action needed for keyPressed
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No action needed for keyReleased
            }
        });
        // Create a JPanel for the login window content
        loginPanel = new JPanel();
        loginPanel.setLayout(null); // Use null layout for manual component placement

        // Add components to the login window content
        JLabel titleLabel = new JLabel("Login Window");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(150, 10, 100, 20); // Adjust the bounds as needed
        loginPanel.add(titleLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 50, 20); // Adjust the bounds as needed
        loginPanel.add(nameLabel);
        nameField.setBounds(120, 50, 200, 20); // Adjust the bounds as needed
        loginPanel.add(nameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 80, 70, 20); // Adjust the bounds as needed
        loginPanel.add(passwordLabel);
        passwordField.setBounds(120, 80, 200, 20); // Adjust the bounds as needed
        loginPanel.add(passwordField);

        submit = new JButton("Submit");
        submit.setBounds(140, 120, 100, 50);
        loginPanel.add(submit);
        submit.addActionListener(this);
        // Explicitly request focus for the text fields
        // Customize the login window content as needed
        // ...
        nameField.addMouseListener(this);
        passwordField.addMouseListener(this);
        // Set a border for the login panel to create a boundary
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Add the login panel to the content pane of the login window
        loginWindow.getContentPane().add(loginPanel);
        // Explicitly request focus for the text fields
        nameField.requestFocusInWindow();
        passwordField.requestFocusInWindow();

        // Set the visibility of the login window to true

        loginWindow.setVisible(true);

    }


    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
