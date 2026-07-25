package Phase3.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeController {

	public void open21K(ActionEvent event) throws IOException {

	    Session.selectedKaratID = 1;

	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/Phase3/View/categories.fxml"));

	    Parent root = loader.load();

	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

	    Scene scene = new Scene(root);

	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    stage.setFullScreenExitHint("");
	    stage.show();
	}
	
	public void open18K(ActionEvent event) throws IOException {

	    Session.selectedKaratID = 2;

	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/Phase3/View/categories.fxml"));

	    Parent root = loader.load();

	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

	    Scene scene = new Scene(root);

	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    stage.setFullScreenExitHint("");
	    stage.show();
	}
	
	public void open14K(ActionEvent event) throws IOException {

	    Session.selectedKaratID = 3;

	    FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/Phase3/View/categories.fxml"));

	    Parent root = loader.load();

	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

	    Scene scene = new Scene(root);

	    stage.setScene(scene);
	    stage.setFullScreen(true);
	    stage.show();
	}
}