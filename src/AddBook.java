import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.*;

public class AddBook {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("添加图书");
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
//				String id = map.get("编　号").getText();
                String na = map.get("书　名").getText();
                String au = map.get("作　者").getText();
                String pu = map.get("出版社").getText();
                String pr = map.get("单　价").getText();
                String qu = map.get("数　量").getText();

//				int bid = Integer.parseInt(id);
                double price = Double.parseDouble(pr);
                int quantity = Integer.parseInt(qu);

                // TODO
                // System.out.println(bid + ' ' + na + ' ' + au + ' ' + pu + ' ' + price + ' ' + quantity);

                Books b = new Books(na, au, pu, price, quantity);
                if ((new Mysqlutil()).insertBook(b) > 0) {
                    JOptionPane.showMessageDialog(null, "添加图书成功！", "添加成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加图书失败！", "添加失败", JOptionPane.WARNING_MESSAGE);

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
//		"编　号",
            "书　名",
            "作　者",
            "出版社",
            "单　价",
            "数　量",
    };

    private static Map<String, JTextField> map;
}
