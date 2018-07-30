import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.*;

public class DeleteUser {

	public static void main(String[] args) {
		run(new JFrame(), null);
	}

	public static void run(JFrame jf, String username) {
		jf.setTitle("删除用户");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Box vBox = Box.createVerticalBox();
		map = new HashMap<>();

		for (String name:fieldName) {
			JPanel pn = new JPanel();
			pn.add(new JLabel(name));
			JTextField txt = new JTextField(10);
			map.put(name, txt);
			pn.add(txt);
			vBox.add(pn);
		}

		JPanel submitPanel = new JPanel();
		JButton submitBtn = new JButton("删除");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = map.get("用户名").getText();

				// TODO
				// System.out.println(id);

				if ((new Mysqlutil()).deleteUser(id)>0){
					JOptionPane.showMessageDialog(null, "您已经成功删除用户！", "删除成功", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "删除用户失败！", "删除失败", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton returnBtn = new JButton("返回");
		returnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminMenu.run(jf, username);
				jf.pack();
			}
		});
		submitPanel.add(submitBtn);
		submitPanel.add(returnBtn);
		vBox.add(submitPanel);

		jf.setContentPane(vBox);

		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

	private static String[] fieldName = {
		"用户名",
	};

	private static Map<String, JTextField> map;
}
