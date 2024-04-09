package Model;

public class Message {
	private String hoTen;
	private String content;
	private String date_send;

	public Message(String hoTen, String content, String date_send) {
		this.hoTen = hoTen;
		this.content = content;
		this.date_send = date_send;
	}

	public String getHoTen() {
		return hoTen;
	}

	public String getContent() {
		return content;
	}

	public String getdate_send() {
		return date_send;
	}
}
