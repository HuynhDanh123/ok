package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Helper.DataBaseConnect;
import Model.Message;

public class messageDAO {
		private static final int RECENT_HOURS = 12;

//lưu tin nhắn
	public boolean saveMess(String hoTen, String content) throws Exception {
		try (Connection connection = DataBaseConnect.openConnection()) {
			String query = "INSERT INTO ta_tch_message(Hoten,content)" + "  VALUES (?, ? )";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, hoTen);
			statement.setString(2, content);

			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

//hiển thị tin nhắn khoảng 12h đổ lại
	public List<Message> getRecentMessages() throws Exception {
		List<Message> messages = new ArrayList<>();

		try (Connection connection = DataBaseConnect.openConnection()) {
			// Lấy ngày giờ hiện tại và ngày giờ 12 giờ trước
			Date now = new Date();
			Date recentTime = new Date(now.getTime() - (RECENT_HOURS * 60 * 60 * 1000));

			// Format định dạng ngày giờ
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String query = "SELECT HoTen, content, date_send FROM ta_tch_message WHERE date_send >= ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setTimestamp(1, new Timestamp(recentTime.getTime()));

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String hoTen = resultSet.getString("HoTen");
				String content = resultSet.getString("content");
				Timestamp timestamp = resultSet.getTimestamp("date_send");

				// Format ngày giờ thành chuỗi
				String formattedTime = dateFormat.format(timestamp);

				Message message = new Message(hoTen, content, formattedTime);
				messages.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return messages;
	}
}
