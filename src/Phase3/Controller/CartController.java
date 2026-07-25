package Phase3.Controller;

import Phase3.Model.CartItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, String> productColumn;

    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItem, Double> priceColumn;

    @FXML
    private TableColumn<CartItem, Double> subtotalColumn;

    @FXML
    private Label totalLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getProduct().getProductName()));

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        priceColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getProduct().getFinalPrice()).asObject());

        subtotalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getSubTotal()).asObject());

        cartTable.setItems(Session.cart);

        updateTotal();
    }

    private void updateTotal() {

        double total = 0;

        for (CartItem item : Session.cart) {

            total += item.getSubTotal();

        }

        totalLabel.setText("Grand Total : " + total + " JD");

    }
    @FXML
    private void removeItem(ActionEvent event){

        CartItem selected = cartTable.getSelectionModel().getSelectedItem();

        if(selected==null){
            return;
        }

        Session.cart.remove(selected);

        updateTotal();
    }
    @FXML
    private void continueShopping(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/categories.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }
    @FXML
    private void checkout(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/checkout.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }

}