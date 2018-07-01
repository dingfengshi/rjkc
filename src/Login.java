import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame());
    }

    public static void run(JFrame jf) {
        jf.setTitle("用户登录");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setResizable(false);

        JPanel userPanel = new JPanel();
        userPanel.add(new JLabel("用户名"));
        userField = new JTextField(10);
        userPanel.add(userField);

        JPanel passwdPanel = new JPanel();
        passwdPanel.add(new JLabel("密　码"));
        passwdField = new JPasswordField(10);
        passwdPanel.add(passwdField);

        JPanel loginPanel = new JPanel();
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwdField.getPassword());
                Mysqlutil util = new Mysqlutil();
                User user = util.selectUser(username, password);
                if (user != null) {
                    if (username.equals("admin")) // 如果是管理员帐号admin
                        AdminMenu.run(jf, username);
                    else if ((new Mysqlutil()).selectUser(username, password) != null)
                        UserMenu.run(jf, username);
                } else {
                    JOptionPane.showMessageDialog(null, "用户名或者密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        loginPanel.add(loginButton);

        Box vBox = Box.createVerticalBox();
        vBox.add(userPanel);
        vBox.add(passwdPanel);
        vBox.add(loginPanel);

        jf.setContentPane(vBox);

        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        jf.pack();
    }

    private static JTextField userField;
    private static JPasswordField passwdField;
}
