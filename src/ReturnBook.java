import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ReturnBook {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("归还图书");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Box vBox = Box.createVerticalBox();

        JPanel searchPanel = new JPanel();
        JButton searchBtn = new JButton("查询已借图书");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordList = (new Mysqlutil()).selectRecordByUid(username);
                page = 0;
                if (recordList.size() == 0) {
                    JOptionPane.showMessageDialog(null, "您目前还没有借阅图书！", "查无信息", JOptionPane.INFORMATION_MESSAGE);
                }
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

                if ((page + 1) * pageSize < recordList.size()) {
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
            result[i] = new JCheckBox("图书" + i);
            resultPanel.add(result[i]);
            result[i].setVisible(false);
            vBox.add(resultPanel);
        }

        JPanel submitPanel = new JPanel();
        JButton submitBtn = new JButton("归还");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mysqlutil utill = new Mysqlutil();

                boolean has_fail = false;
                boolean has_succeed = false;
                if (result.length == 0) {
                    JOptionPane.showMessageDialog(null, "请选择要归还的书籍！", "所选为空", JOptionPane.ERROR_MESSAGE);
                    refresh(jf, username);
                    return;
                }
                for (JCheckBox r : result) {
                    if (r.isSelected()) {
                        String s = r.getText();
                        String id = s.split("/", 2)[0];
                        int bid = Integer.parseInt(id);

                        // System.our.println(s + ' ' + bid);
                        if (utill.deleteRecord(username, bid) <= 0) {
                            JOptionPane.showMessageDialog(null, s.split("/")[1] + "归还失败！", "归还失败", JOptionPane.INFORMATION_MESSAGE);
                            has_fail = true;
                        } else {
                            has_succeed = true;
                        }
                    }
                }
                if (has_succeed) {
                    if (has_fail) {
                        JOptionPane.showMessageDialog(null, "您已经成功归还所选的其余书籍！", "归还成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "您已经成功归还所选书籍！", "归还成功", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                refresh(jf, username);
            }
        });
        submitPanel.add(submitBtn);
        JButton returnBtn = new JButton("返回");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserMenu.run(jf, username);
                jf.pack();
            }
        });
        submitPanel.add(returnBtn);
        vBox.add(submitPanel);

        jf.setContentPane(vBox);

        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    private static void showResult() {
        int i = 0;

        for (JCheckBox r : result) {
            r.setSelected(false);
            if (page * pageSize + i < recordList.size()) {
                Record ri = recordList.get(page * pageSize + i);
                Books bi = (new Mysqlutil()).selectBookById(ri.getBid());
                String text = String.valueOf(bi.getBid()) + '/' + bi.getBname() + '/' + bi.getAuthor() + '/' + bi.getPublisher();
                // TODO
                // System.out.println(text);

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

    private static void refresh(JFrame jf, String username) {
        recordList = (new Mysqlutil()).selectRecordByUid(username);
        page = 0;
        showResult();
        jf.pack();
    }

    private static int page = 0;
    private static int pageSize = 5;
    private static JCheckBox[] result = new JCheckBox[pageSize];
    private static ArrayList<Record> recordList = new ArrayList<>();

}
