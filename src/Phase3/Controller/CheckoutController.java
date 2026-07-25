package Phase3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Phase3.Util.DatabaseConnection;
import Phase3.Model.CartItem;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class CheckoutController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private ChoiceBox<String> paymentChoice;

    @FXML
    private Label totalLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	nameField.setText(Session.customerName);

    	phoneField.setText(Session.customerPhone);

    	addressField.setText(Session.customerAddress);
    	nameField.setEditable(false);

    	phoneField.setEditable(false);

    	addressField.setEditable(false);
        paymentChoice.getItems().addAll(
                "Cash",
                "Credit Card",
                "Jawwal Pay"
        );

        paymentChoice.setValue("Cash");

        double total = 0;

        for(var item : Session.cart){

            total += item.getSubTotal();

        }

        totalLabel.setText("Grand Total : " + total + " JOD");

    }
  
    @FXML
    private void placeOrder(ActionEvent event) {

        if(nameField.getText().isBlank() ||
           phoneField.getText().isBlank() ||
           addressField.getText().isBlank()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }

        if(Session.cart.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty.");
            alert.showAndWait();
            return;
        }

        Connection con = null;

        try{

            double total = 0;

            for(CartItem item : Session.cart){
                total += item.getSubTotal();
            }

            con = DatabaseConnection.getConnection();

            con.setAutoCommit(false);
            
            PreparedStatement stockCheck = con.prepareStatement(
            	    "SELECT StockQuantity FROM Product WHERE ProductID=?");

            int paymentID = 1;

            if(paymentChoice.getValue().equals("Credit Card"))
                paymentID = 2;
            else if(paymentChoice.getValue().equals("Jawwal Pay"))
                paymentID = 3;
            
            for(CartItem item : Session.cart){

                stockCheck.setInt(1, item.getProduct().getProductID());

                ResultSet rs = stockCheck.executeQuery();

                if(rs.next()){

                    int stock = rs.getInt("StockQuantity");

                    if(stock < item.getQuantity()){

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText(
                            item.getProduct().getProductName()
                            + "\nOnly "
                            + stock
                            + " item(s) left in stock.");

                        alert.showAndWait();

                        return;
                    }
                }
            }
            String sql =
            		"INSERT INTO Orders(CustomerID,AdminID,PaymentMethodID,OrderDate,TotalPrice,"
            		+ "ReceiverName,ReceiverPhone,DeliveryAddress)"
            		+ " VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Session.customerID);
            ps.setInt(2, 1);
            ps.setInt(3, paymentID);
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.setDouble(5, total);

            ps.setString(6, nameField.getText());
            ps.setString(7, phoneField.getText());
            ps.setString(8, addressField.getText());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            keys.next();

            int orderID = keys.getInt(1);

            PreparedStatement orderPS = con.prepareStatement(
                    "INSERT INTO OrderList(OrderID,ProductID,Amount,PriceAtTime) VALUES(?,?,?,?)");
            PreparedStatement stockStmt = con.prepareStatement(
                    "UPDATE Product SET StockQuantity = StockQuantity - ? WHERE ProductID = ?");
            for(CartItem item : Session.cart){

                orderPS.setInt(1, orderID);
                orderPS.setInt(2, item.getProduct().getProductID());
                orderPS.setInt(3, item.getQuantity());
                orderPS.setDouble(4, item.getProduct().getFinalPrice());

                orderPS.executeUpdate();

                // خصم الكمية من المخزون
                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setInt(2, item.getProduct().getProductID());
                stockStmt.executeUpdate();
            }

            con.commit();

            Session.cart.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Order Placed Successfully!");
            alert.showAndWait();

        }
        catch(Exception e){

            try{
                if(con != null)
                    con.rollback();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }
    

    
    @FXML
    private void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/cart.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }
    

}