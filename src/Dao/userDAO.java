package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Helper.DataBaseConnect;

public class userDAO {

//đăng nhập
	public boolean authenticate(String hoTen, String pass) throws Exception {
		try (Connection connection = DataBaseConnect.openConnection()) {
			String query = "SELECT * FROM ta_tch_user WHERE HoTen = ? AND pass = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, hoTen);
			statement.setString(2, pass);

			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

//check người dùng đã tồn tại chưa
	public boolean checkUserExists(String hoTen) throws Exception {
		try (Connection connection = DataBaseConnect.openConnection()) {
			String query = "SELECT COUNT(*) FROM ta_tch_user WHERE HoTen = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, hoTen);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// đăng kí
	public boolean register(String hoTen, String pass, String gioitinh, String email) throws Exception {
		if (checkUserExists(hoTen)) {
			System.out.println("Người dùng đã tồn tại");
			return false;
		}

		try (Connection connection = DataBaseConnect.openConnection()) {
			String query = "INSERT INTO ta_tch_user(HoTen, pass, giotinh, email) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, hoTen);
			statement.setString(2, pass);
			statement.setString(3, gioitinh);
			statement.setString(4, email);

			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
