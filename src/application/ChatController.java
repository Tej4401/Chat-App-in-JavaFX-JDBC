package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ChatController {
	@FXML
	private VBox chats;
	@FXML
	Label title;
	@FXML
	TextArea msg;
	private String contactName;
	private String name;
	private int index;
	public void setContact(String name) {
		contactName = name;
	}
	public void setName(String name) throws ClassNotFoundException, SQLException {
		this.name = name;
		init();
	}
	private void runWithInterval(long millis) {
	    Runnable task = () -> {
	        try {
	        	System.out.println("here");
	            while (true) {
	                Thread.sleep(millis);
	                init();
	            }
	        } catch(InterruptedException e) {
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    };

	    Thread thread = new Thread(task);
	    thread.setDaemon(true);
	    thread.start();
	}
	public void init() throws SQLException, ClassNotFoundException {
		title.setText(contactName);
		chats.getChildren().clear();
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/phpmyadmin","root","toor"); 
		PreparedStatement stmt=con.prepareStatement("select * from (select * from messages where sender = ? and receiver = ? union select * from messages where sender = ? and receiver = ?) a order by sequence");
		stmt.setString(1, name);
		stmt.setString(2, contactName);
		stmt.setString(3, contactName);
		stmt.setString(4, name);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Label label = new Label(rs.getString("message"));
			HBox hbox = new HBox(label);
			if(rs.getString("sender").equals(contactName))
//			label.setAlignment(Pos.CENTER_LEFT);
			hbox.setAlignment(Pos.CENTER_LEFT);
			else
			hbox.setAlignment(Pos.CENTER_RIGHT);	
			chats.getChildren().add(hbox);
			index = rs.getInt("sequence");
		}
	}
	public void send(ActionEvent event) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/phpmyadmin","root","toor");
//	 stmt=con.prepareStatement("select max(sequence) as maxseq from messages where sender = ? and receiver = ? or sender = ? and receiver = ?");
//		stmt.setString(1, name);
//		stmt.setString(2, contactName);
//		stmt.setString(3, contactName);
//		stmt.setString(4, name);
//		ResultSet rs = stmt.executeQuery();	
		PreparedStatement stmt=con.prepareStatement("insert into messages values(?,?,?,?)");
		stmt.setString(1, name);
		stmt.setString(2, contactName);
		stmt.setString(3, msg.getText());
		stmt.setInt(4, index + 1);
		stmt.executeUpdate();
	}
}
