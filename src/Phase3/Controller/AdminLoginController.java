package Phase3.Controller;

import Phase3.Util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String query = "SELECT * FROM Users WHERE UserName = ? AND UserPassword = ? AND UserRole = 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
              
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Phase3/View/adminDashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();
                System.out.println("Admin Login Successful!");
            } else {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
            	alert.setHeaderText(null);
            	alert.setContentText("Invalid username or password.");
            	alert.showAndWait();  }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}