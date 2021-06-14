package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ContactsController implements Initializable{
	
	@FXML
	private ListView<String> listView;
	@FXML
	TextField newContact;
	private static String username;
	private String contactName;
	private Stage stage;
	private Scene scene;
	private Parent root;
	public void setName(String name, Stage stage) throws ClassNotFoundException, SQLException {
		username = name;
		System.out.println("hello");
		init(stage);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	public void init(Stage stage) throws ClassNotFoundException, SQLException {
		System.out.print(username);
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/phpmyadmin","root","toor"); 
		PreparedStatement stmt=con.prepareStatement("select * from contacts where person1 = ?");
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			listView.getItems().add(rs.getString("person2"));
		}
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				contactName = listView.getSelectionModel().getSelectedItem();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
				try {
					root = loader.load();
					ChatController chatController = loader.getController();
					chatController.setContact(contactName);
					chatController.setName(username);
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
		});
	}
	public void add(ActionEvent event) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/phpmyadmin","root","toor"); 
		PreparedStatement stmt=con.prepareStatement("insert into contacts values(?,?)");
		stmt.setString(1, username);
		stmt.setString(2, newContact.getText());
		stmt.executeUpdate();
		listView.getItems().add(newContact.getText());
	}
}
