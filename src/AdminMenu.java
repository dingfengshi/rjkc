import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminMenu {

	public static void main(String[] args) {
		UIStyle.systemStyle();
		run(new JFrame(), null);
	}

	public static void run(JFrame jf, String username) {
		jf.setTitle("管理员菜单");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel bookPanel = new JPanel();
		JButton addBookBtn = new JButton("添加图书");
		addBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddBook.run(jf, username);
			}
		});
		bookPanel.add(addBookBtn);


		JButton deleteBookBtn = new JButton("删除图书");
		deleteBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteBook.run(jf, username);
			}
		});
		bookPanel.add(deleteBookBtn);

		JPanel userPanel = new JPanel();
		JButton addUserBtn = new JButton("添加用户");
		addUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddUser.run(jf, username);
			}
		});
		userPanel.add(addUserBtn);

		JButton deleteUserBtn = new JButton("删除用户");
		deleteUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteUser.run(jf, username);
			}
		});
		userPanel.add(deleteUserBtn);

		JButton rechargeUserBtn = new JButton("用户充值");
		rechargeUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RechargeUser.run(jf, username);
			}
		});
		userPanel.add(rechargeUserBtn);

		JPanel logoutPanel = new JPanel();
		JButton logoutBtn = new JButton("返回");
		logoutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Login.run(jf);
			}
		});
		logoutPanel.add(logoutBtn);

		Box vBox = Box.createVerticalBox();
		vBox.add(bookPanel);
		vBox.add(userPanel);
		vBox.add(logoutPanel);

		jf.setContentPane(vBox);

		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
}
