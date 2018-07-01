import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.*;

public class AddUser {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("添加用户");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Box vBox = Box.createVerticalBox();
        map = new HashMap<>();

        for (String name : fieldName) {
            JPanel pn = new JPanel();
            pn.add(new JLabel(name));
            JTextField txt = new JTextField(10);
            map.put(name, txt);
            pn.add(txt);
            vBox.add(pn);
        }

        JPanel submitPanel = new JPanel();
        JButton submitBtn = new JButton("添加");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uid = map.get("用户名").getText();
                String pwd = map.get("密　码").getText();
                String blc = map.get("余　额").getText();
                double balance = Double.parseDouble(blc);

                // TODO
                // System.out.println(uid + ' ' + pwd + ' ' + balance);

                User u = new User(uid, pwd, balance);
                if ((new Mysqlutil()).insertUser(u) > 0) {
                    JOptionPane.showMessageDialog(null, "您已经成功添加用户！", "添加成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加用户失败！", "添加失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton returnBtn = new JButton("返回");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminMenu.run(jf, username);
            }
        });
        submitPanel.add(submitBtn);
        submitPanel.add(returnBtn);
        vBox.add(submitPanel);

        jf.setContentPane(vBox);

        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        jf.pack();
    }

    private static String[] fieldName = {
            "用户名",
            "密　码",
            "余　额",
    };

    private static Map<String, JTextField> map;
}
