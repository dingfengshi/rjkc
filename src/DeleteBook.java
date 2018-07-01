import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DeleteBook {

    public static void main(String[] args) {
        UIStyle.systemStyle();
        run(new JFrame(), null);
    }

    public static void run(JFrame jf, String username) {
        jf.setTitle("删除图书");
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

        JButton submitBtn = new JButton("删除");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

		boolean has_select = false;
		boolean has_failure = false;
                Mysqlutil utill = new Mysqlutil();

                for (JCheckBox r : result) {
                    if (r.isSelected()) {
			has_select = true;
                        String s = r.getText();
                        String id = s.split("/", 2)[0];
                        int bid = Integer.parseInt(id);

			if ((new Mysqlutil()).deleteBook(bid) <= 0) {
				has_failure = true;
			}
                    }
                }

		if (has_select) {
                    if (has_failure) {
		        JOptionPane.showMessageDialog(null, "部分书籍删除失败！", "删除失败", JOptionPane.ERROR_MESSAGE);
                    } else {
		        JOptionPane.showMessageDialog(null, "所选书籍删除成功！", "删除成功", JOptionPane.INFORMATION_MESSAGE);
                    }
		} else {
                    JOptionPane.showMessageDialog(null, "请选择要删除的书籍！", "所选为空", JOptionPane.ERROR_MESSAGE);
		}
                refresh(jf);
            }
        });
        submitPanel.add(submitBtn);
        JButton returnBtn = new JButton("返回");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminMenu.run(jf, username);
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
