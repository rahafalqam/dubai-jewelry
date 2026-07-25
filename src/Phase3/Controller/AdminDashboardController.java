package Phase3.Controller;

import Phase3.Util.DatabaseConnection;
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

public class AdminDashboardController {

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

        String query = "SELECT Product.ProductID, Product.ProductName, Karat.KaratName, Category.CategoryName, " +
                "Product.ProductWeight, Karat.PricePerGram, Category.WorkmanshipFee, " +
                "(Product.ProductWeight * Karat.PricePerGram + Category.WorkmanshipFee) AS FinalPrice " +
                "FROM Product, Karat, Category " +
                "WHERE Product.KaratID = Karat.KaratID AND Product.CategoryID = Category.CategoryID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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

                card.getChildren().addAll(imageView, nameLabel, idLabel, weightLabel, priceLabel);

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
    public void addProduct(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details:");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField weightField = new TextField();

        ChoiceBox<String> karatChoice = new ChoiceBox<>();
        karatChoice.getItems().addAll("1 (21K)", "2 (18K)", "3 (14K)");
        karatChoice.setValue("1 (21K)");

        ChoiceBox<String> categoryChoice = new ChoiceBox<>();
        categoryChoice.getItems().addAll("1 (Rings)", "2 (Necklaces)", "3 (Bracelets)", "4 (Earrings)");
        categoryChoice.setValue("1 (Rings)");

        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Product Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Weight (g):"), 0, 2);
        grid.add(weightField, 1, 2);
        grid.add(new Label("Karat:"), 0, 3);
        grid.add(karatChoice, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryChoice, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == addButtonType) {
            	String insertSQL =
            	        "INSERT INTO Product(ProductID, ProductName, ProductWeight, StockQuantity, KaratID, CategoryID) " +
            	        "VALUES(?,?,?,?,?,?)";

            	try (Connection conn = DatabaseConnection.getConnection();
            	     PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            	    int productId = Integer.parseInt(idField.getText());
            	    String productName = nameField.getText();
            	    double productWeight =
            	            Double.parseDouble(weightField.getText().replace(",", "."));

            	    stmt.setInt(1, productId);
            	    stmt.setString(2, productName);
            	    stmt.setDouble(3, productWeight);
            	    stmt.setInt(4, 10);   // StockQuantity
            	    stmt.setInt(5, Character.getNumericValue(karatChoice.getValue().charAt(0)));
            	    stmt.setInt(6, Character.getNumericValue(categoryChoice.getValue().charAt(0)));

            	    int rows = stmt.executeUpdate();

            	    System.out.println("Rows inserted = " + rows);

            	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
            	    alert.setHeaderText(null);
            	    alert.setContentText("Product added successfully.");
            	    alert.showAndWait();

            	    loadProducts();

            	}
            	catch (NumberFormatException e) {

            	    Alert alert = new Alert(Alert.AlertType.ERROR);
            	    alert.setContentText("Please enter valid numbers.");
            	    alert.showAndWait();

            	}
            	catch (SQLException e) {

            	    e.printStackTrace();

            	    Alert alert = new Alert(Alert.AlertType.ERROR);
            	    alert.setHeaderText(null);
            	    alert.setContentText(e.getMessage());
            	    alert.showAndWait();
            	}
            	
            }
            });
       
                    
                
            
        
    }

    @FXML
    public void deleteProduct(ActionEvent event) {
        if (selectedProductId == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product first by clicking on it!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String deleteSQL = "DELETE FROM Product WHERE ProductID = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

                    stmt.setInt(1, selectedProductId);
                    stmt.executeUpdate();
                    loadProducts();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void updateProduct(ActionEvent event) {
        if (selectedProductId == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product first by clicking on it to edit it!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String fetchSQL = "SELECT ProductName, ProductWeight, KaratID, CategoryID FROM Product WHERE ProductID = ?";
        String currentName = "";
        double currentWeight = 0.0;
        int currentKaratId = 1;
        int currentCategoryId = 1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchSQL)) {

            stmt.setInt(1, selectedProductId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentName = rs.getString("ProductName");
                    currentWeight = rs.getDouble("ProductWeight");
                    currentKaratId = rs.getInt("KaratID");
                    currentCategoryId = rs.getInt("CategoryID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update the Details of the Item ");
        dialog.setHeaderText("with ID: " + String.format("%06d", selectedProductId));

        ButtonType updateButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(currentName);
        TextField weightField = new TextField(String.valueOf(currentWeight));

        ChoiceBox<String> karatChoice = new ChoiceBox<>();
        karatChoice.getItems().addAll("1 (21K)", "2 (18K)", "3 (14K)");
        if (currentKaratId == 1) karatChoice.setValue("1 (21K)");
        else if (currentKaratId == 2) karatChoice.setValue("2 (18K)");
        else if (currentKaratId == 3) karatChoice.setValue("3 (14K)");

        ChoiceBox<String> categoryChoice = new ChoiceBox<>();
        categoryChoice.getItems().addAll("1 (Rings)", "2 (Necklaces)", "3 (Bracelets)", "4 (Earrings)");
        if (currentCategoryId == 1) categoryChoice.setValue("1 (Rings)");
        else if (currentCategoryId == 2) categoryChoice.setValue("2 (Necklaces)");
        else if (currentCategoryId == 3) categoryChoice.setValue("3 (Bracelets)");
        else if (currentCategoryId == 4) categoryChoice.setValue("4 (Earrings)");

        grid.add(new Label("New item name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("New weight(g):"), 0, 1);
        grid.add(weightField, 1, 1);
        grid.add(new Label("New Kerat "), 0, 2);
        grid.add(karatChoice, 1, 2);
        grid.add(new Label("New CategoryChoice"), 0, 3);
        grid.add(categoryChoice, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == updateButtonType) {
                String updateSQL = "UPDATE Product SET ProductName = ?, ProductWeight = ?, KaratID = ?, CategoryID = ? WHERE ProductID = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

                    String newName = nameField.getText();
                    double newWeight = Double.parseDouble(weightField.getText().replace(",", "."));
                    int newKaratId = Character.getNumericValue(karatChoice.getValue().charAt(0));
                    int newCategoryId = Character.getNumericValue(categoryChoice.getValue().charAt(0));

                    stmt.setString(1, newName);
                    stmt.setDouble(2, newWeight);
                    stmt.setInt(3, newKaratId);
                    stmt.setInt(4, newCategoryId);
                    stmt.setInt(5, selectedProductId);

                    stmt.executeUpdate();
                    System.out.println("✅Updated !!");

                    loadProducts();

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "❌ Please make sure you enter the correct numbers in the weight field.", ButtonType.OK);
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Phase3/View/welcome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 650);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    @FXML
    public void viewOrders(ActionEvent event) {
        String query =
                "SELECT o.OrderID, " +
                        "CONCAT(u.FirstName, ' ', u.LastName) AS CustomerName, " +
                        "pm.PaymentMethodName, " +
                        "o.OrderDate, " +
                        "o.TotalPrice " +
                        "FROM Orders o " +
                        "JOIN Users u ON o.CustomerID = u.UserID " +
                        "JOIN PaymentMethod pm ON o.PaymentMethodID = pm.PaymentMethodID " +
                        "ORDER BY o.OrderDate DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                result.append("Order ID: ").append(rs.getInt("OrderID")).append("\n")
                        .append("Customer: ").append(rs.getString("CustomerName")).append("\n")
                        .append("Payment: ").append(rs.getString("PaymentMethodName")).append("\n")
                        .append("Date: ").append(rs.getDate("OrderDate")).append("\n")
                        .append("Total Price: ").append(rs.getDouble("TotalPrice")).append(" JOD\n")
                        .append("--------------------------------------\n");
            }

            if (result.length() == 0) {
                result.append("No orders found.");
            }

            TextArea textArea = new TextArea(result.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("View Orders");
            alert.setHeaderText("All Customer Orders");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not load orders.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void viewOrderDetails(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Order Details");
        inputDialog.setHeaderText("Enter Order ID");
        inputDialog.setContentText("Order ID:");

        inputDialog.showAndWait().ifPresent(orderIdText -> {
            try {
                int orderId = Integer.parseInt(orderIdText);

                String query =
                        "SELECT o.OrderID, " +
                                "p.ProductName, " +
                                "ol.Amount, " +
                                "ol.PriceAtTime, " +
                                "(ol.Amount * ol.PriceAtTime) AS SubTotal " +
                                "FROM OrderList ol " +
                                "JOIN Orders o ON ol.OrderID = o.OrderID " +
                                "JOIN Product p ON ol.ProductID = p.ProductID " +
                                "WHERE o.OrderID = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, orderId);

                    try (ResultSet rs = stmt.executeQuery()) {
                        StringBuilder result = new StringBuilder();

                        while (rs.next()) {
                            result.append("Order ID: ").append(rs.getInt("OrderID")).append("\n")
                                    .append("Product: ").append(rs.getString("ProductName")).append("\n")
                                    .append("Quantity: ").append(rs.getInt("Amount")).append("\n")
                                    .append("Price At Time: ").append(rs.getDouble("PriceAtTime")).append(" JOD\n")
                                    .append("SubTotal: ").append(rs.getDouble("SubTotal")).append(" JOD\n")
                                    .append("--------------------------------------\n");
                        }

                        if (result.length() == 0) {
                            result.append("No details found for this order.");
                        }

                        TextArea textArea = new TextArea(result.toString());
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        textArea.setPrefWidth(600);
                        textArea.setPrefHeight(400);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Order Details");
                        alert.setHeaderText("Details for Order ID: " + orderId);
                        alert.getDialogPane().setContent(textArea);
                        alert.showAndWait();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Could not load order details.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Order ID must be a number.");
                alert.setContentText("Please enter a valid numeric Order ID.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void viewSalesByCategory(ActionEvent event) {
        String query =
                "SELECT c.CategoryName, " +
                        "SUM(ol.Amount) AS TotalQuantitySold, " +
                        "SUM(ol.Amount * ol.PriceAtTime) AS TotalSales " +
                        "FROM OrderList ol " +
                        "JOIN Product p ON ol.ProductID = p.ProductID " +
                        "JOIN Category c ON p.CategoryID = c.CategoryID " +
                        "GROUP BY c.CategoryID, c.CategoryName " +
                        "ORDER BY TotalSales DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                result.append("Type: ").append(rs.getString("CategoryName")).append("\n")
                        .append("Quantity Sold: ").append(rs.getInt("TotalQuantitySold")).append("\n")
                        .append("Total Sales: ").append(rs.getDouble("TotalSales")).append(" JOD\n")
                        .append("--------------------------------------\n");
            }

            if (result.length() == 0) {
                result.append("No sales found.");
            }

            TextArea textArea = new TextArea(result.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sales By Type");
            alert.setHeaderText("Total Sales for Each Jewelry Type");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not load sales by type.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void viewDailySales(ActionEvent event) {
        String query =
                "SELECT OrderDate, COUNT(OrderID) AS NumberOfOrders, SUM(TotalPrice) AS DailySales " +
                        "FROM Orders " +
                        "GROUP BY OrderDate " +
                        "ORDER BY OrderDate";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                result.append("Date: ").append(rs.getDate("OrderDate")).append("\n")
                        .append("Number of Orders: ").append(rs.getInt("NumberOfOrders")).append("\n")
                        .append("Daily Sales: ").append(rs.getDouble("DailySales")).append(" JOD\n")
                        .append("--------------------------------------\n");
            }

            if (result.length() == 0) {
                result.append("No sales found.");
            }

            TextArea textArea = new TextArea(result.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Daily Sales");
            alert.setHeaderText("Daily Sales Report");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not load daily sales.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void viewMonthlySales(ActionEvent event) {
        String query =
                "SELECT YEAR(OrderDate) AS SalesYear, " +
                        "MONTH(OrderDate) AS SalesMonth, " +
                        "COUNT(OrderID) AS NumberOfOrders, " +
                        "SUM(TotalPrice) AS MonthlySales " +
                        "FROM Orders " +
                        "GROUP BY YEAR(OrderDate), MONTH(OrderDate) " +
                        "ORDER BY SalesYear, SalesMonth";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                result.append("Year: ").append(rs.getInt("SalesYear")).append("\n")
                        .append("Month: ").append(rs.getInt("SalesMonth")).append("\n")
                        .append("Number of Orders: ").append(rs.getInt("NumberOfOrders")).append("\n")
                        .append("Monthly Sales: ").append(rs.getDouble("MonthlySales")).append(" JOD\n")
                        .append("--------------------------------------\n");
            }

            if (result.length() == 0) {
                result.append("No monthly sales found.");
            }

            TextArea textArea = new TextArea(result.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(600);
            textArea.setPrefHeight(400);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Monthly Sales");
            alert.setHeaderText("Monthly Sales Report");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not load monthly sales.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}