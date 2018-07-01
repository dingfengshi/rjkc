import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BorrowBook {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("借阅图书");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Box vBox = Box.createVerticalBox();

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("关键字"));
        searchText = new JTextField(10);
        searchPanel.add(searchText);
        JButton searchBtn = new JButton("查询");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stext = searchText.getText();
                bookList = (new Mysqlutil()).selectBookByAll(stext);
                page = 0;
                showResult();
                jf.pack();
            }
        });
        searchPanel.add(searchBtn);

        JButton prevBtn = new JButton("上一页");
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (page > 0) {
                    page--;
                    showResult();
                    jf.pack();
                }

            }
        });
        searchPanel.add(prevBtn);

        JButton nextBtn = new JButton("下一页");
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ((page + 1) * pageSize < bookList.size()) {
                    page++;
                    showResult();
                    jf.pack();
                }

            }
        });
        searchPanel.add(nextBtn);

        vBox.add(searchPanel);

        for (int i = 0; i < pageSize; i++) {
            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            result[i] = new JCheckBox("");
            result[i].setSelected(false);
            result[i].setEnabled(false);
            result[i].setVisible(false);
            resultPanel.add(result[i]);
            vBox.add(resultPanel);
        }

        JPanel submitPanel = new JPanel();

        JButton submitBtn = new JButton("借阅");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean has_fail = false;
                boolean has_succeed = false;
                Mysqlutil utill = new Mysqlutil();

                /*检查借阅数量是否过多*/
                ArrayList<Record> records = utill.selectRecordByUid(username);
                int selectedcount = 0;
                for (JCheckBox r : result) {
                    if (r.isSelected()) {
                        selectedcount++;
                    }
                }
                if (selectedcount == 0) {
                    JOptionPane.showMessageDialog(null, "请选择要借的书籍！", "所选为空", JOptionPane.ERROR_MESSAGE);
                    refresh(jf);
                    return;
                }
                if (records.size() + selectedcount > utill.bookLimit) {
                    JOptionPane.showMessageDialog(null, "借阅书的数量超过限制", "借阅失败", JOptionPane.WARNING_MESSAGE);
                    refresh(jf);
                    return;
                }

                for (JCheckBox r : result) {
                    if (r.isSelected()) {
                        String s = r.getText();
                        String id = s.split("/", 2)[0];
                        int bid = Integer.parseInt(id);

                        // System.our.println(s + ' ' + bid);

                        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

                        Record rec = new Record(username, bid, now);
                        int state = (new Mysqlutil()).insertRecord(rec);
                        if (state <= 0) {
                            switch (state) {
                                case 0:
                                    JOptionPane.showMessageDialog(null, '《' + s.split("/")[1] + "》借阅失败，图书已经借阅！", "借阅失败", JOptionPane.WARNING_MESSAGE);
                                    break;
                                case -2:
                                    JOptionPane.showMessageDialog(null, '《' + s.split("/")[1] + "》借阅失败，图书余量不足！", "借阅失败", JOptionPane.WARNING_MESSAGE);
                                    break;
                                case -3:
                                    JOptionPane.showMessageDialog(null, '《' + s.split("/")[1] + "》借阅失败，您已欠费，请先充值！", "借阅失败", JOptionPane.WARNING_MESSAGE);
                                    break;
                                case -4:
                                    JOptionPane.showMessageDialog(null, '《' + s.split("/")[1] + "》借阅失败，数据库操作发生异常！", "借阅失败", JOptionPane.WARNING_MESSAGE);
                                    break;

                            }
                            has_fail = false;
                        } else {
                            has_succeed = true;
                        }

                    }
                }

                if (has_succeed) {
                    if (has_fail) {
                        JOptionPane.showMessageDialog(null, "您已经成功借阅所选的其余书籍！", "借阅成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "您已经成功借阅所选书籍！", "借阅成功", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                refresh(jf);
            }
        });
        submitPanel.add(submitBtn);
        JButton returnBtn = new JButton("返回");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserMenu.run(jf, username);
            }
        });
        submitPanel.add(returnBtn);
        vBox.add(submitPanel);

        jf.setContentPane(vBox);

        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        jf.pack();
        refresh(jf);
    }

    private static void showResult() {
        int i = 0;

        for (JCheckBox r : result) {
            r.setSelected(false);
            if (page * pageSize + i < bookList.size()) {
                Books bi = bookList.get(page * pageSize + i);
                String text = String.valueOf(bi.getBid()) + '/' + bi.getBname() + '/' + bi.getAuthor() + '/' + bi.getPublisher() + "    剩余量:" + bi.getQuantity();
                r.setVisible(true);
                r.setEnabled(true);
                r.setText(text);
                i++;
            } else {
                r.setVisible(false);
                r.setEnabled(false);
                r.setText("");
            }
        }
    }

    private static void refresh(JFrame jf) {
        String stext = searchText.getText();
        bookList = (new Mysqlutil()).selectBookByName(stext);
        //page = 0;
        showResult();
        jf.pack();
    }

    private static JTextField searchText;
    private static int page = 0;
    private static int pageSize = 5;
    private static JCheckBox[] result = new JCheckBox[pageSize];
    private static ArrayList<Books> bookList = new ArrayList<>();

}
