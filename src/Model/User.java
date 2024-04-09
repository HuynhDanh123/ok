package Model;

import java.net.Socket;

public class User {
	private String HoTen, pass, gioitinh, email;

	public User(String hoTen, String pass, String gioitinh, String email) {
		super();
		HoTen = hoTen;
		this.pass = pass;
		this.gioitinh = gioitinh;
		this.email = email;
	}

	public String getHoTen() {
		return HoTen;
	}

	public void setHoTen(String hoTen) {
		HoTen = hoTen;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getGioitinh() {
		return gioitinh;
	}

	public void setGioitinh(String gioitinh) {
		this.gioitinh = gioitinh;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [HoTen=" + HoTen + ", pass=" + pass + ", gioitinh=" + gioitinh + ", email=" + email + "]";
	}

}
