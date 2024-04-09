package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Server extends JFrame {

	private JPanel contentPane;
	private JTextField jTextField;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	Thread t;
	serverThread serverThread;
	/** Chat List **/
	public Vector socketList = new Vector();
	public Vector clientList = new Vector();
	/** File Sharing List **/
	public Vector clientFileSharingUsername = new Vector();
	public Vector clientFileSharingSocket = new Vector();
	/** Server **/
	ServerSocket server;
	private JButton jButton1;
	private JButton jButton2;
	private JTextArea jTextArea1;
	private JTextField jTextField1;
	private User usr;

	public void appendMessage(String msg) {
		Date date = new Date();
		jTextArea1.append(sdf.format(date) + ": " + msg + "\n");
		jTextArea1.setCaretPosition(jTextArea1.getText().length() - 1);
	}

	public void setSocketList(Socket socket) {
		try {
			socketList.add(socket);
			appendMessage("[setSocketList]: Được thêm");
		} catch (Exception e) {
			appendMessage("[setSocketList]: " + e.getMessage());
		}
	}

	public void setClientList(String client) {
		try {
			clientList.add(client);
			appendMessage("[setClientList]: Được thêm");
		} catch (Exception e) {
			appendMessage("[setClientList]: " + e.getMessage());
		}
	}

	public void setClientFileSharingUsername(String user) {
		try {
			clientFileSharingUsername.add(user);
		} catch (Exception e) {
		}
	}

	public void setClientFileSharingSocket(Socket soc) {
		try {
			clientFileSharingSocket.add(soc);
		} catch (Exception e) {
		}
	}

	public Socket getClientList(String client) {
		Socket tsoc = null;
		for (int x = 0; x < clientList.size(); x++) {
			if (clientList.get(x).equals(client)) {
				tsoc = (Socket) socketList.get(x);
				break;
			}
		}
		return tsoc;
	}

	public void removeFromTheList(String client) {
		try {
			for (int x = 0; x < clientList.size(); x++) {
				if (clientList.elementAt(x).equals(client)) {
					clientList.removeElementAt(x);
					socketList.removeElementAt(x);
					appendMessage("[Removed]: " + client);
					break;
				}
			}
		} catch (Exception e) {
			appendMessage("[RemovedException]: " + e.getMessage());
		}
	}

	public Socket getClientFileSharingSocket(String username) {
		Socket tsoc = null;
		for (int x = 0; x < clientFileSharingUsername.size(); x++) {
			if (clientFileSharingUsername.elementAt(x).equals(username)) {
				tsoc = (Socket) clientFileSharingSocket.elementAt(x);
				break;
			}
		}
		return tsoc;
	}

	public void removeClientFileSharing(String username) {
		for (int x = 0; x < clientFileSharingUsername.size(); x++) {
			if (clientFileSharingUsername.elementAt(x).equals(username)) {
				try {
					Socket rSock = getClientFileSharingSocket(username);
					if (rSock != null) {
						rSock.close();
					}
					clientFileSharingUsername.removeElementAt(x);
					clientFileSharingSocket.removeElementAt(x);
					appendMessage("[FileSharing]: Hủy bỏ " + username);
				} catch (IOException e) {
					appendMessage("[FileSharing]: " + e.getMessage());
					appendMessage("[FileSharing]: Không thể hủy bỏ " + username);
				}
				break;
			}
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 582, 382);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("SERVER");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 44));
		lblNewLabel.setBounds(49, 10, 475, 52);
		contentPane.add(lblNewLabel);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(10, 61, 548, 221);
		contentPane.add(jScrollPane1);

		jTextArea1 = new JTextArea();
		jTextArea1.setEditable(false);
		jTextArea1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		jScrollPane1.setViewportView(jTextArea1);

		JLabel lblNewLabel_1 = new JLabel("Port:  ");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel_1.setBounds(10, 287, 54, 45);
		contentPane.add(lblNewLabel_1);

		jTextField1 = new JTextField();
		jTextField1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		jTextField1.setText("3333");
		jTextField1.setBounds(61, 292, 217, 35);
		contentPane.add(jTextField1);
		jTextField1.setColumns(10);

		jButton1 = new JButton("Khởi động Server");

		jButton1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		jButton1.setBounds(288, 292, 130, 35);
		contentPane.add(jButton1);

		jButton2 = new JButton("Ngừng Server");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Đóng Máy Chủ.?");
				if (confirm == 0) {
					serverThread.stop();
				}
			}
		});
		jButton2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		jButton2.setBounds(428, 292, 130, 35);
		contentPane.add(jButton2);
		if (t == null) {
			jButton1.setEnabled(true);
			jButton2.setEnabled(false);
		}
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(jTextField1.getText());
				serverThread = new serverThread(port, Server.this);
				t = new Thread(serverThread);
				t.start();

				new Thread(new danhSachOnline(Server.this)).start();

				jButton1.setEnabled(false);
				jButton2.setEnabled(true);
			}

		});

	}

}
