package Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class DataBaseConnect {
public static Connection openConnection() throws Exception {
	String url = "jdbc:mysql://localhost:3306/ta_tch_chatbox ";
	String user="root";
	String pass="Hiep12062004@";
    Connection con = DriverManager.getConnection(url, user, pass);
	return con;
}
}
