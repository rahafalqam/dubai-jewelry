// WelcomeController.java

package Phase3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class WelcomeController {

	public void openCustomerLogin(ActionEvent event) throws IOException {

	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/Phase3/View/customerLogin.fxml")
	    );

	    Parent root = loader.load();

	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

	    Scene scene = new Scene(root,1100, 650);

	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    stage.show();
	}
	public void openAdminLogin(ActionEvent event) throws IOException {

	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/Phase3/View/adminLogin.fxml")
	    );

	    Parent root = loader.load();

	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

	    Scene scene = new Scene(root, 900, 650);

	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    stage.show();
	}
}
