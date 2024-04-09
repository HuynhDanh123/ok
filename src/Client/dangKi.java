package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class dangKi extends JFrame {

	private JPanel contentPane;
	private JTextField ht;
	private JTextField gt;
	private JPasswordField mk;
	Socket socket;
	private JTextField em;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dangKi frame = new dangKi();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public dangKi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 479);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		panel.setBounds(0, 104, 597, 343);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Tên đăng nhập*:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 10, 138, 41);
		panel.add(lblNewLabel);

		ht = new JTextField();
		ht.setBounds(151, 10, 436, 41);
		panel.add(ht);
		ht.setColumns(10);

		JLabel lblMtKhu = new JLabel("Mật Khẩu* :");
		lblMtKhu.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblMtKhu.setBounds(10, 71, 138, 41);
		panel.add(lblMtKhu);

		gt = new JTextField();
		gt.setColumns(10);
		gt.setBounds(151, 137, 436, 41);
		panel.add(gt);

		JLabel lblEmail = new JLabel("Giới Tính*: ");
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblEmail.setBounds(10, 135, 138, 41);
		panel.add(lblEmail);

		JLabel lblNewLabel_1 = new JLabel("Bạn đã có tài khoản?");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dangNhap lo = new dangNhap();
				lo.setLocationRelativeTo(null);
				lo.setVisible(true);
				dispose();

			}
		});
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(179, 258, 214, 23);
		panel.add(lblNewLabel_1);

		JButton dangki = new JButton("Đăng Kí");
		dangki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dangNhap hl = new dangNhap();
				String Hoten1 = ht.getText();
				String pass3 = new String(mk.getPassword());
				String gioitinh4 = gt.getText();
				String email5 = em.getText();

				if (Hoten1.equals("") || pass3.equals("") || gioitinh4.equals("")) {
					JOptionPane.showMessageDialog(contentPane, "Bạn cần điền đầy đủ vào *", "Thông báo",
							JOptionPane.ERROR_MESSAGE);				}
				else {
					try {
						Socket client = new Socket("localhost", 3333);
						System.out.println("Đã kết nối");
						DataInputStream inFromServer = new DataInputStream(client.getInputStream());
						DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());

						outToServer.writeUTF("register");
						String Hotendk = ht.getText();
						outToServer.writeUTF(Hotendk);
						String passdk = new String(mk.getPassword());
						outToServer.writeUTF(passdk);
						String gioitinh = gt.getText();
						outToServer.writeUTF(gioitinh);
						String email = em.getText();
						outToServer.writeUTF(email);

						String c = inFromServer.readUTF();

						if (c.equals("Đăng kí thành công")) {
							JOptionPane.showMessageDialog(contentPane, "Chúc mừng bạn đã tạo tài khoản thành công",
									"Thông báo", JOptionPane.CLOSED_OPTION);
							hl.setVisible(true);
							setVisible(false);
						} else {
							JOptionPane.showMessageDialog(contentPane, "Người dùng đã được đăng kí", "Thông báo",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			}
		});
		dangki.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		dangki.setBounds(212, 291, 148, 41);
		panel.add(dangki);

		mk = new JPasswordField();
		mk.setBounds(151, 73, 436, 41);
		panel.add(mk);

		em = new JTextField();
		em.setColumns(10);
		em.setBounds(151, 201, 436, 41);
		panel.add(em);

		JLabel lblGiiTnh = new JLabel("Email:");
		lblGiiTnh.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblGiiTnh.setBounds(10, 199, 138, 41);
		panel.add(lblGiiTnh);

		JLabel lblNewLabel_3 = new JLabel("(Tên sẽ được hiển thị trong chat)");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 8));
		lblNewLabel_3.setBounds(10, 41, 138, 23);
		panel.add(lblNewLabel_3);

		JLabel lblNewLabel_2 = new JLabel("Chào mừng bạn đến với Box Chat");
		lblNewLabel_2.setBackground(Color.ORANGE);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel_2.setBounds(10, 10, 577, 86);
		contentPane.add(lblNewLabel_2);
	}
}
