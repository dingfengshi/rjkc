import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.*;

public class RechargeUser {

	public static void main(String[] args) {
		UIStyle.systemStyle();
		run(new JFrame(), null);
	}

	public static void run(JFrame jf, String username) {
		jf.setTitle("用户充值");
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
		JButton submitBtn = new JButton("充值");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = map.get("用户名").getText();
				String mo = map.get("金　额").getText();
				double money = Double.parseDouble(mo);

				// TODO
				// System.out.println(id);

				if ((new Mysqlutil()).recharge(id, money)>0){
					JOptionPane.showMessageDialog(null, "您已经成功为" + id + "充值" + money + "！", "充值成功", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "充值失败！", "充值失败", JOptionPane.ERROR_MESSAGE);
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

		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

	private static String[] fieldName = {
		"用户名",
		"金　额",
	};

	private static Map<String, JTextField> map;
}
