package Phase3.Controller;
import javafx.scene.control.Alert;
import Phase3.Util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.ResultSet;
public class RegisterController {

    @FXML private TextField idField;
    @FXML private TextField usernameField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    public void register(ActionEvent event) {
        String idText = idField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String city = cityField.getText();
        String street = streetField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String username = usernameField.getText();
        
        if(idText.isBlank() ||
        		   username.isBlank() ||
        		   firstName.isBlank() ||
        		   lastName.isBlank() ||
        		   city.isBlank() ||
        		   street.isBlank() ||
        		   email.isBlank() ||
        		   phone.isBlank() ||
        		   password.isBlank() ||
        		   confirmPassword.isBlank()){

        	Alert alert = new Alert(Alert.AlertType.WARNING);
        	alert.setHeaderText(null);
        	alert.setContentText("Please fill all fields.");
        	alert.showAndWait();
        	return; 
        		}
        if (!password.equals(confirmPassword)) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setHeaderText(null);
        	alert.setContentText("Passwords do not match.");
        	alert.showAndWait();
        	return;
        }

        int userId;

        try{

            userId = Integer.parseInt(idText);

        }catch(NumberFormatException e){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("National ID must contain numbers only.");
            alert.showAndWait();
            return;
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
            return;
        }
        String checkSQL =
                "SELECT * FROM Users WHERE UserID=? OR UserName=?";

        
        String insertUserSQL = "INSERT INTO Users (UserID, UserName, FirstName, LastName, UserEmail, CityName, StreetName, UserPhone, UserPassword, UserRole) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'CUSTOMER')";
        String insertCustomerSQL = "INSERT INTO Customer (UserID, RegistrationDate) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement check =
                    conn.prepareStatement(checkSQL);

            check.setInt(1, userId);
            check.setString(2, username);

            ResultSet rs = check.executeQuery();
            
            if(!idText.matches("\\d{9}")){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("National ID must contain exactly 9 digits.");
                alert.showAndWait();
                return;
            }

            if(!phone.matches("\\d{10}")){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Phone number must contain exactly 10 digits.");
                alert.showAndWait();
                return;
            }

            if(rs.next()){

            	Alert alert = new Alert(Alert.AlertType.ERROR);
            	alert.setHeaderText(null);
            	alert.setContentText("User ID or Username already exists.");
            	alert.showAndWait();
            	return;
            }
         
        	conn.setAutoCommit(false);

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSQL);
                 PreparedStatement customerStmt = conn.prepareStatement(insertCustomerSQL)) {
                
                // 1. الجدول العام للمستخدمين
                userStmt.setInt(1, userId);
                userStmt.setString(2, username);
                userStmt.setString(3, firstName);
                userStmt.setString(4, lastName);
                userStmt.setString(5, email);
                userStmt.setString(6, city);
                userStmt.setString(7, street);
                userStmt.setString(8, phone);
                userStmt.setString(9, password);
                userStmt.executeUpdate();   customerStmt.setInt(1, userId);
                customerStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                customerStmt.executeUpdate();

                conn.commit(); 
                System.out.println("Account Created Successfully inside DB!");

               FXMLLoader loader = new FXMLLoader(getClass().getResource("/Phase3/View/customerLogin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.show();

            } catch (SQLException e) {
                conn.rollback(); 
                throw e;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/customerLogin.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }
}