import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserMenu {

    public static void main(String[] args) {
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("用户菜单");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel bookPanel = new JPanel();
        JButton borrowBookBtn = new JButton("借阅图书");
        borrowBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BorrowBook.run(jf, username);
                jf.pack();
            }
        });
        bookPanel.add(borrowBookBtn);

        JButton returnBookBtn = new JButton("归还图书");
        returnBookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnBook.run(jf, username);
                jf.pack();
            }
        });
        bookPanel.add(returnBookBtn);

        JPanel logoutPanel = new JPanel();
        JButton logoutBtn = new JButton("返回");
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login.run(jf);
                jf.pack();
            }
        });
        logoutPanel.add(logoutBtn);

        Box vBox = Box.createVerticalBox();
        vBox.add(bookPanel);
        vBox.add(logoutPanel);

        jf.setContentPane(vBox);

        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }
}