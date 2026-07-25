package Phase3.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class CategoriesController {

    private void openProducts(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Phase3/View/products.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public void openRings(ActionEvent event) throws IOException {

        Session.selectedCategoryID = 1;
        openProducts(event);

    }

    public void openBracelets(ActionEvent event) throws IOException {

        Session.selectedCategoryID = 3;
        openProducts(event);

    }

    public void openNecklaces(ActionEvent event) throws IOException {

        Session.selectedCategoryID = 2;
        openProducts(event);

    }
    @FXML
    private void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/home.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }

}