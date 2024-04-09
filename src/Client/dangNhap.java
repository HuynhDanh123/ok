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

import com.mysql.cj.xdevapi.Client;

public class dangNhap extends JFrame {

	private JPanel contentPane;
	private JTextField txtname;
	private JPasswordField txtpass;
	private Client client;
	private DataOutputStream out;
	private DataInputStream in;
	private Socket sc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dangNhap frame = new dangNhap();
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

	public dangNhap() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 367);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Box Chat xin chào!");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		lblNewLabel.setBounds(10, 10, 535, 70);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1  = new JLabel("Tên đăng nhập: ");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(10, 60, 160, 48);
		contentPane.add(lblNewLabel_1);

		txtname = new JTextField();
		txtname.setBounds(10, 101, 535, 37);
		contentPane.add(txtname);
		txtname.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("Mật khẩu:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(20, 140, 115, 48);
		contentPane.add(lblNewLabel_1_1);

		txtpass = new JPasswordField();
		txtpass.setBounds(10, 180, 535, 37);
		contentPane.add(txtpass);

		JButton dangnhap = new JButton("Đăng Nhập");
		dangnhap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String username = txtname.getText();

				String password = new String(txtpass.getPassword());

				ChatBox hl = new ChatBox();

				if (username.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(contentPane, "Tên đăng nhập hoặc mật khẩu trống", "Thông báo",
							JOptionPane.ERROR_MESSAGE);
				} else {

					try {
						Socket client = new Socket("localhost", 3333);
						System.out.println("Đã kết nối");

						in = new DataInputStream(client.getInputStream());
						out = new DataOutputStream(client.getOutputStream());

						out.writeUTF("login");
						String HoTen = txtname.getText();
						String pass = new String(txtpass.getPassword());
						out.writeUTF(HoTen);
						out.writeUTF(pass);

						String c = in.readUTF();
						if (c.equals("Đăng nhập không thành công")) {
							JOptionPane.showMessageDialog(contentPane, "Tên đăng nhập hoặc mất khẩu sai!!!",
									"Thông báo", JOptionPane.ERROR_MESSAGE);
						} else {
							hl.initFrame(username, 3333);
							hl.setVisible(true);
							dispose();
						}

					} catch (IOException ex) {
						JOptionPane.showMessageDialog(contentPane, "Không thể kết nối đến máy chủ, vui lòng thử lại sau.!",
								"Kết nối thất bại", JOptionPane.ERROR_MESSAGE);	
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		dangnhap.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		dangnhap.setBounds(147, 237, 251, 42);
		contentPane.add(dangnhap);

		JLabel lblNewLabel_2 = new JLabel("Bạn chưa có tài khoản?");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.ITALIC, 15));
		lblNewLabel_2.setBounds(10, 279, 156, 41);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("Đăng kí tài khoản tại đây");
		lblNewLabel_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dangKi dk = new dangKi();
				dk.setVisible(true);
				dk.setLocationRelativeTo(null);
				dispose();
			}
		});
		lblNewLabel_2_1.setFont(new Font("Times New Roman", Font.ITALIC, 18));
		lblNewLabel_2_1.setBounds(167, 279, 195, 41);
		contentPane.add(lblNewLabel_2_1);
	}
}
