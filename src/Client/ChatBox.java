package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Dao.messageDAO;
import Helper.kieuChat;
import Model.Message;

public class ChatBox extends JFrame {

	private JPanel contentPane;
	private JTextField jTextField1;
	String username;
	String host;
	int port;
	Socket socket;
	DataOutputStream dos;
	DataInputStream dis;
	public boolean attachmentOpen = false;
	private boolean isConnected = false;
	private String mydownloadfolder = "D:\\";
	private JTextPane jTextPane1;
	private JTextPane txtpane2;
	private JButton jButton1;

	public void initFrame(String username, int port) throws Exception {
		this.username = username;
		this.port = port;
		setTitle("Bạn đang được đăng nhập với tên: " + username);
		// Kết nối
		connect();
	}

	public void connect() throws Exception {
		appendMessage(" Đang kết nối...", "Trạng thái", Color.PINK, Color.PINK);
		try {
			socket = new Socket("Localhost", port);
			dos = new DataOutputStream(socket.getOutputStream());
			// gửi username đang kết nối
			dos.writeUTF("CMD_JOIN " + username);
			appendMessage(" Đã kết nối", "Trạng thái", Color.PINK, Color.PINK);
			appendMessage(" Gửi tin nhắn bây giờ!", "Trạng thái", Color.PINK, Color.PINK);

			// Khởi động Client Thread
			new Thread(new ClientThread(socket, ChatBox.this)).start();
			jButton1.setEnabled(true);
			// đã được kết nối
			messageDAO daoMessage = new messageDAO();
			List<Message> recentMessages = daoMessage.getRecentMessages();
			if (recentMessages.isEmpty()) {
				appendMessage("Chưa có tin nhắn gần đây", "Thông báo: ", Color.PINK, Color.PINK);
			} else {
				for (Message message : recentMessages) {
					String a = message.getHoTen();
					String b = message.getContent();
					appendMessage(b, a,Color.BLACK,Color.DARK_GRAY	);
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối đến máy chủ, vui lòng thử lại sau.!",
					"Kết nối thất bại", JOptionPane.ERROR_MESSAGE);
			appendMessage("[IOException]: " + e.getMessage(), "Lỗi", Color.RED, Color.RED);
		}
	}

	// Hiển thị Message

	public void appendMessage(String msg, String header, Color headerColor, Color contentColor) {
		jTextPane1.setEditable(true);
		getMsgHeader(header, headerColor);
		getMsgContent(msg, contentColor);
		jTextPane1.setEditable(false);
	}

	/*
	 * Tin nhắn chat
	 */
	public void appendMyMessage(String msg, String header) {
		jTextPane1.setEditable(true);
		getMsgHeader(header, Color.GREEN);
		getMsgContent(msg, Color.BLACK);
		jTextPane1.setEditable(false);
	}

	// Tiêu đề tin nhắn

	public void getMsgHeader(String header, Color color) {
		int len = jTextPane1.getDocument().getLength();
		jTextPane1.setCaretPosition(len);
		jTextPane1.setCharacterAttributes(kieuChat.styleMessageContent(color, "Impact", 13), false);
		jTextPane1.replaceSelection(header + ":");
	}

	// Nội dung tin nhắn

	public void getMsgContent(String msg, Color color) {
		int len = jTextPane1.getDocument().getLength();
		jTextPane1.setCaretPosition(len);
		jTextPane1.setCharacterAttributes(kieuChat.styleMessageContent(color, "Arial", 12), false);
		jTextPane1.replaceSelection(msg + "\n\n");
	}

	public void appendOnlineList(Vector list) {
		sampleOnlineList(list);
	}

	// Hiển thị danh sách đang online

	public void showOnLineList(Vector list) {
		try {
			txtpane2.setEditable(true);
			txtpane2.setContentType("text/html");
			StringBuilder sb = new StringBuilder();
			Iterator it = list.iterator();
			sb.append("<html><table>");
			while (it.hasNext()) {
				Object e = it.next();
				URL url = getImageFile();
				Icon icon = new ImageIcon(this.getClass().getResource("online.png"));
				sb.append("<tr><td><b>></b></td><td>").append(e).append("</td></tr>");
				System.out.println("Online: " + e);
			}
			sb.append("</table></body></html>");
			txtpane2.removeAll();
			txtpane2.setText(sb.toString());
			txtpane2.setEditable(false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

// Hiển thị danh sách online 
	private void sampleOnlineList(Vector list) {
		txtpane2.setEditable(true);
		txtpane2.removeAll();
		txtpane2.setText("");
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object e = i.next();
			/* Hiển thị Username Online */
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setBackground(Color.white);

			Icon icon = new ImageIcon(this.getClass().getResource("online.png"));
			JLabel label = new JLabel(icon);
			label.setText(" " + e);
			panel.add(label);
			int len = txtpane2.getDocument().getLength();
			txtpane2.setCaretPosition(len);
			txtpane2.insertComponent(panel);
			/* Append Next Line */
			sampleAppend();
		}
		txtpane2.setEditable(false);
	}

	private void sampleAppend() {
		int len = txtpane2.getDocument().getLength();
		txtpane2.setCaretPosition(len);
		txtpane2.replaceSelection("\n");
	}
	/*
	 ************************************ Show Online Sample *********************************************
	 */

	// Get image file path

	public URL getImageFile() {
		URL url = this.getClass().getResource("online.png");
		return url;
	}

	// Set myTitle

	public void setMyTitle(String s) {
		setTitle(s);
	}

	/*
	 * Phương thức tải get download
	 */
	public String getMyDownloadFolder() {
		return this.mydownloadfolder;
	}


	/*
	 * Phương thức nhận My Username
	 */
	public String getMyUsername() {
		return this.username;
	}

	/*
	 * Cập nhật Attachment
	 */
	public void updateAttachment(boolean b) {
		this.attachmentOpen = b;
	}

	/*
	 * Hàm này sẽ mở 1 file chooser
	 */
	public void openFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int open = chooser.showDialog(this, "Mở Thư Mục");
		if (open == chooser.APPROVE_OPTION) {
			mydownloadfolder = chooser.getSelectedFile().toString() + "\\";
		} else {
			mydownloadfolder = "D:\\";
		}
	}

	/**
	 * Launch the application.
	 */
	public void main() {
		ChatBox frame = new ChatBox();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Create the frame.
	 */
	public ChatBox() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 465);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 639, 22);
		contentPane.add(menuBar);

		JMenu mnNewMenu = new JMenu("   ≡   ");
		menuBar.add(mnNewMenu);

		JMenuItem LogoutMenu = new JMenuItem("Đăng xuất");
		LogoutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc đăng xuất không ?");
				if (confirm == 0) {
					try {
						dos.writeUTF("logout");
						dos.writeUTF(username);
						socket.close();
						setVisible(false);
						/** Login Form **/
						new dangNhap().setVisible(true);
					} catch (IOException ea) {
						System.out.println(ea.getMessage());
					}
				}
			}
		});
		mnNewMenu.add(LogoutMenu);

		JSeparator separator_1 = new JSeparator();
		mnNewMenu.add(separator_1);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Thoát");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_3);

		JMenu mnNewMenu_1 = new JMenu("Chia sẻ File");
		menuBar.add(mnNewMenu_1);

		JMenuItem sendFileMenu = new JMenuItem("Gửi File");
		sendFileMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!attachmentOpen) {
					guiFile s = new guiFile();
					if (s.prepare(username, host, port, ChatBox.this)) {
						s.setLocationRelativeTo(null);
						s.setVisible(true);
						attachmentOpen = true;
					} else {
						JOptionPane.showMessageDialog(ChatBox.this,
								"Không thể thiết lập Chia sẻ File tại thời điểm này, xin vui lòng thử lại sau.!", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnNewMenu_1.add(sendFileMenu);

		JSeparator separator = new JSeparator();
		mnNewMenu_1.add(separator);

		JMenuItem jMenuItem3 = new JMenuItem("Tải xuống");
		jMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int browse = chooser.showOpenDialog(ChatBox.this);
					if (browse == chooser.APPROVE_OPTION) {
						mydownloadfolder = chooser.getSelectedFile().toString() + "\\";
					}
				} catch (HeadlessException ea) {
				}
			}
		});
		mnNewMenu_1.add(jMenuItem3);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(10, 32, 448, 335);
		contentPane.add(jScrollPane1);

		jTextPane1 = new JTextPane();
		jTextPane1.setEditable(false);
		jScrollPane1.setViewportView(jTextPane1);

		JScrollPane jScrollPane3 = new JScrollPane();
		jScrollPane3.setBounds(468, 82, 163, 285);
		contentPane.add(jScrollPane3);

		txtpane2 = new JTextPane();
		txtpane2.setEditable(false);
		jScrollPane3.setViewportView(txtpane2);

		jTextField1 = new JTextField();
		jTextField1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (jTextField1.getText().equals("")) {
						JOptionPane.showMessageDialog(contentPane, "Nội dung trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
					}else {
					String content = username + " " + jTextField1.getText();
					dos.writeUTF("CHAT_TONG " + content);
					appendMyMessage(" " + jTextField1.getText(), username);
					jTextField1.setText("");
				}} catch (IOException ea) {
					appendMessage(
							" Không thể gửi tin nhắn đi bây giờ, không thể kết nối đến Máy Chủ tại thời điểm này, xin vui lòng thử lại sau hoặc khởi động lại ứng dụng này.!",
							"Lỗi", Color.RED, Color.RED);
				}
			}
		});
		jTextField1.setBounds(10, 377, 448, 44);
		contentPane.add(jTextField1);
		jTextField1.setColumns(10);

		jButton1 = new JButton("Gửi");
		jButton1.setEnabled(false);
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String noidung = jTextField1.getText();
					if (noidung.equals("")) {
						JOptionPane.showMessageDialog(contentPane, "Nội dung trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
					} else {
						String content = username + " " + noidung;
						dos.writeUTF("CHAT_TONG " + content);

						appendMyMessage(" " + jTextField1.getText(), username);
						jTextField1.setText("");
					}
				} catch (IOException ea) {
					appendMessage(
							" Không thể gửi tin nhắn đi bây giờ, không thể kết nối đến Máy Chủ tại thời điểm này, xin vui lòng thử lại sau hoặc khởi động lại ứng dụng này.!",
							"Lỗi", Color.RED, Color.RED);
				}
			}
		});
		jButton1.setBounds(468, 377, 163, 47);
		contentPane.add(jButton1);

		JLabel lblNewLabel = new JLabel("Danh Sách Online");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(468, 32, 163, 44);
		contentPane.add(lblNewLabel);
	}
}
