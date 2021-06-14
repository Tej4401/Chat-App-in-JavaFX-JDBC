package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	TextField username;
	@FXML
	PasswordField password;
	@FXML
	Label error;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void login(ActionEvent event) throws ClassNotFoundException,SQLException, IOException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/phpmyadmin","root","toor"); 
		Statement stmt=con.createStatement();
		String uname = username.getText();
		String pass = password.getText();
		ResultSet rs=stmt.executeQuery("select * from users");
		while(rs.next()){  
			if(rs.getString("username").equals(uname)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("contacts.fxml"));
				root = loader.load();
				ContactsController contactsController = loader.getController();
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				contactsController.setName(uname,stage);
				stage.show();
				return;
			}
		}
		error.setTextFill(Color.RED);
		error.setText("Incorrect username/password");
	}
	
	public void change(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();		
	}
}
