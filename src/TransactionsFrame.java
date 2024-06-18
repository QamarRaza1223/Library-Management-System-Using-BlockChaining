import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class TransactionsFrame extends JFrame {

    private JTextArea transactionsTextArea;

    TransactionsFrame() {
        setTitle("Transaction History");
        setSize(800, 800);
        setResizable(false);

        transactionsTextArea = new JTextArea();
        transactionsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(transactionsTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                transactionsTextArea.setText(""); // Clear transactions when closing the window
                setVisible(false);
            }
        });
    }

    void updateTransactions(ArrayList<Block> blockChain) {
        transactionsTextArea.setText(""); // Clear existing transactions
        if (blockChain.isEmpty()) {
            transactionsTextArea.append("No transactions available.");
        } else {
            for (Block block : blockChain) {
                transactionsTextArea.append(block.getTransactions() + "\n");
            }
            transactionsTextArea.append((BlockChain.getChain().isChainValid()));
        }
    }
}