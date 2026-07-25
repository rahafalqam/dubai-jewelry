package Phase3.Controller;

import Phase3.Util.DatabaseConnection;
import Phase3.Model.Product;
import Phase3.Model.CartItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsController {

    @FXML
    private FlowPane productsFlowPane;

    private int selectedProductId = -1;
    private VBox selectedCard = null;

    public void initialize() {
        if (productsFlowPane != null) {
            loadProducts();
        }
    }
    private void loadProducts() {
        productsFlowPane.getChildren().clear();
        selectedProductId = -1;
        selectedCard = null;

        String query =
        		"SELECT Product.ProductID, Product.ProductName, Karat.KaratName, Category.CategoryName, " +
        		"Product.ProductWeight, Product.StockQuantity, " +
        		"(Product.ProductWeight * Karat.PricePerGram + Category.WorkmanshipFee) AS FinalPrice " +
        		"FROM Product " +
        		"JOIN Karat ON Product.KaratID = Karat.KaratID " +
        		"JOIN Category ON Product.CategoryID = Category.CategoryID " +
        		"WHERE Product.KaratID = ? AND Product.CategoryID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        	     PreparedStatement stmt = conn.prepareStatement(query)) {

        	    stmt.setInt(1, Session.selectedKaratID);
        	    stmt.setInt(2, Session.selectedCategoryID);

        	    ResultSet rs = stmt.executeQuery();
        		while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("ProductName");
                String karat = rs.getString("KaratName");
                double weight = rs.getDouble("ProductWeight");
                double finalPrice = rs.getDouble("FinalPrice");
                String category = rs.getString("CategoryName");

                VBox card = new VBox(10);
                card.setAlignment(Pos.CENTER);
                card.setStyle("-fx-background-color: #111111; -fx-padding: 20; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #d4af37; -fx-border-width: 2;");
                
               
                String imagePath = getProductImagePath(name, category);
                ImageView imageView = new ImageView();
                try {
                    Image img = new Image(getClass().getResourceAsStream(imagePath));
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.out.println("Image not found: " + imagePath);
                  
                    imageView.setImage(new Image(getClass().getResourceAsStream("/Phase3/Images/Ring1.png")));
                }
                imageView.setFitWidth(220);
                imageView.setFitHeight(260);
                imageView.setPreserveRatio(true);

                Label nameLabel = new Label(name);
                nameLabel.setStyle("-fx-text-fill: #d4af37; -fx-font-size: 20px; -fx-font-weight: bold;");

                Label idLabel = new Label("Product ID: " + String.format("%06d", id));
                idLabel.setStyle("-fx-text-fill: white;");

                Label weightLabel = new Label("Weight: " + weight + " g (" + karat + ")");
                weightLabel.setStyle("-fx-text-fill: white;");

                Label priceLabel = new Label("Price: " + finalPrice + " JOD");
                priceLabel.setStyle("-fx-text-fill: #ffdf00; -fx-font-weight: bold;");

                Button addToCartButton = new Button("🛒 Add To Cart");
                addToCartButton.setPrefWidth(180);

                addToCartButton.setOnAction(e -> {

                    Product product = new Product(
                            id,
                            name,
                            karat,
                            category,
                            weight,
                            10,       
                            finalPrice
                    );

                    boolean found = false;

                    for (CartItem item : Session.cart) {

                        if (item.getProduct().getProductID() == id) {

                            item.setQuantity(item.getQuantity() + 1);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Session.cart.add(new CartItem(product, 1));
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText(name + " added to cart.");
                    alert.showAndWait();
                });

                card.getChildren().addAll(
                        imageView,
                        nameLabel,
                        idLabel,
                        weightLabel,
                        priceLabel,
                        addToCartButton
                );  
                card.setOnMouseClicked(e -> {
                    if (selectedCard != null) {
                        selectedCard.setStyle("-fx-background-color: #111111; -fx-padding: 20; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #d4af37; -fx-border-width: 2;");
                    }
                    selectedCard = card;
                    selectedProductId = id;
                    card.setStyle("-fx-background-color: #111111; -fx-padding: 20; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #ffffff; -fx-border-width: 3;");
                });

                productsFlowPane.getChildren().add(card);
            }
            if (productsFlowPane.getChildren().isEmpty()) {
            	Label emptyLabel = new Label(
            	        "No Products Found ✨\n\n" +
            	        "Unfortunately, we couldn't find any products\n" +
            	        "matching your selected karat and category.\n\n" +
            	        "Please choose another category or karat."
            	);

            	emptyLabel.setStyle(
            	        "-fx-text-fill:#d4af37;" +
            	        "-fx-font-size:26px;" +
            	        "-fx-font-weight:bold;" +
            	        "-fx-alignment:center;" +
            	        "-fx-text-alignment:center;"
            	);

            	productsFlowPane.setAlignment(Pos.CENTER);
            	productsFlowPane.getChildren().add(emptyLabel); }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getProductImagePath(String productName, String categoryName) {
        String nameLower = productName.toLowerCase();
        String catLower = categoryName.toLowerCase();

        if (catLower.contains("neck") || nameLower.contains("neck")) {
            if (nameLower.contains("heart")) {
                return "/Phase3/Images/heartShapeNeclase.jpeg";
            }
            return "/Phase3/Images/necklace.jpeg";
        } 
         if (catLower.contains("brace") || nameLower.contains("brace") || nameLower.contains("bracklet")) {
            if (nameLower.contains("multi") || nameLower.contains("lira2")) {
                return "/Phase3/Images/Lira2.png";
            }
            return "/Phase3/Images/Lira.png";
        } 
        
         if (catLower.contains("ring") || nameLower.contains("ring")) {
            if (nameLower.contains("snake")) {
                return "/Phase3/Images/snake.png";
            }
            return "/Phase3/Images/Ring1.png";
        }

        return "/Phase3/Images/Ring1.png";
    }
    @FXML
    private void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/categories.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    @FXML
    private void openCart(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/Phase3/View/cart.fxml"));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }
   
}