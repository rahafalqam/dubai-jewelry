package Phase3.Model;

public class Product {

    private int productID;
    private String productName;
    private String karatName;
    private String categoryName;
    private double productWeight;
    private int stockQuantity;
    private double finalPrice;

    public Product(int productID,
                   String productName,
                   String karatName,
                   String categoryName,
                   double productWeight,
                   int stockQuantity,
                   double finalPrice) {

        this.productID = productID;
        this.productName = productName;
        this.karatName = karatName;
        this.categoryName = categoryName;
        this.productWeight = productWeight;
        this.stockQuantity = stockQuantity;
        this.finalPrice = finalPrice;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getKaratName() {
        return karatName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

}
