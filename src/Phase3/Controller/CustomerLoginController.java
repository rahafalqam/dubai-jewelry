package Phase3.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert;
import Phase3.Util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
public class CustomerLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String query = "SELECT * FROM Users WHERE UserName = ? AND UserPassword = ? AND UserRole = 'CUSTOMER'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	Session.customerID = rs.getInt("UserID");
                
            	Session.customerName =
            	        rs.getString("FirstName") + " " + rs.getString("LastName");

            	Session.customerPhone =
            	        rs.getString("UserPhone");

            	Session.customerAddress =
            	        rs.getString("CityName") + " - " + rs.getString("StreetName");
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("/Phase3/View/home.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 650);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();
                System.out.println("Customer Login Successful!");
            } else {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
            	alert.setHeaderText(null);
            	alert.setContentText("Invalid username or password.");
            	alert.showAndWait();  }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openRegister(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/register.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }
}