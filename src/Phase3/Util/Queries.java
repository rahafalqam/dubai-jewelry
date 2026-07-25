package Phase3.Util;

public class Queries {

    private Queries() {
    }

    //adm.
    public static final String ADMIN_LOGIN =
            "SELECT * " +
                    "FROM Users" +
                    " WHERE UserName = ? AND UserPassword = ? AND UserRole = 'ADMIN'";

    public static final String CUSTOMER_LOGIN =
            "SELECT * FROM Users WHERE UserName = ? AND UserPassword = ? AND UserRole = 'CUSTOMER'";

    //cus.
    public static final String ADMIN_LOAD_PRODUCTS =
            "SELECT Product.ProductID, Product.ProductName, Karat.KaratName, Category.CategoryName, " +
                    "Product.ProductWeight, Karat.PricePerGram, Category.WorkmanshipFee, " +
                    "(Product.ProductWeight * Karat.PricePerGram + Category.WorkmanshipFee) AS FinalPrice " +
                    "FROM Product, Karat, Category " +
                    "WHERE Product.KaratID = Karat.KaratID AND Product.CategoryID = Category.CategoryID";

    //add product
    public static final String INSERT_PRODUCT =
            "INSERT INTO Product(ProductID, ProductName, ProductWeight, StockQuantity, KaratID, CategoryID) " +
                    "VALUES(?,?,?,?,?,?)";
//delete product
    public static final String DELETE_PRODUCT =
            "DELETE FROM Product WHERE ProductID = ?";

    //updte
    public static final String UPDATE_PRODUCT =
            "UPDATE Product SET ProductName = ?, ProductWeight = ?, KaratID = ?, CategoryID = ? WHERE ProductID = ?";

    //cus.
    public static final String LOAD_PRODUCTS_BY_KARAT_AND_CATEGORY =
            "SELECT Product.ProductID, Product.ProductName, Karat.KaratName, Category.CategoryName, " +
                    "Product.ProductWeight, Product.StockQuantity, " +
                    "(Product.ProductWeight * Karat.PricePerGram + Category.WorkmanshipFee) AS FinalPrice " +
                    "FROM Product " +
                    "JOIN Karat ON Product.KaratID = Karat.KaratID " +
                    "JOIN Category ON Product.CategoryID = Category.CategoryID " +
                    "WHERE Product.KaratID = ? AND Product.CategoryID = ?";


   //cus.
    public static final String INSERT_ORDER =
            "INSERT INTO Orders(CustomerID,AdminID,PaymentMethodID,OrderDate,TotalPrice," +
                    "ReceiverName,ReceiverPhone,DeliveryAddress)" +
                    " VALUES(?,?,?,?,?,?,?,?)";

    // cus. place order
    public static final String INSERT_ORDER_LIST =
            "INSERT INTO OrderList(OrderID,ProductID,Amount,PriceAtTime) VALUES(?,?,?,?)";

    //cus. when many order
    public static final String UPDATE_PRODUCT_STOCK =
            "UPDATE Product SET StockQuantity = StockQuantity - ? WHERE ProductID = ?";

    // advance Q.
    // adm. view orders
    public static final String VIEW_ORDERS =
            "SELECT o.OrderID, " +
                    "CONCAT(u.FirstName, ' ', u.LastName) AS CustomerName, " +
                    "pm.PaymentMethodName, " +
                    "o.OrderDate, " +
                    "o.TotalPrice " +
                    "FROM Orders o " +
                    "JOIN Users u ON o.CustomerID = u.UserID " +
                    "JOIN PaymentMethod pm ON o.PaymentMethodID = pm.PaymentMethodID " +
                    "ORDER BY o.OrderDate DESC";

    // advance Q.
    // adm. order details
    public static final String VIEW_ORDER_DETAILS =
            "SELECT o.OrderID, " +
                    "p.ProductName, " +
                    "ol.Amount, " +
                    "ol.PriceAtTime, " +
                    "(ol.Amount * ol.PriceAtTime) AS SubTotal " +
                    "FROM OrderList ol " +
                    "JOIN Orders o ON ol.OrderID = o.OrderID " +
                    "JOIN Product p ON ol.ProductID = p.ProductID " +
                    "WHERE o.OrderID = ?";

    // advance Q.
    // adm. sales by type
    public static final String SALES_BY_CATEGORY =
            "SELECT c.CategoryName, " +
                    "SUM(ol.Amount) AS TotalQuantitySold, " +
                    "SUM(ol.Amount * ol.PriceAtTime) AS TotalSales " +
                    "FROM OrderList ol " +
                    "JOIN Product p ON ol.ProductID = p.ProductID " +
                    "JOIN Category c ON p.CategoryID = c.CategoryID " +
                    "GROUP BY c.CategoryID, c.CategoryName " +
                    "ORDER BY TotalSales DESC";

    // advance Q.
    // adm. daily sales
    public static final String DAILY_SALES =
            "SELECT OrderDate, COUNT(OrderID) AS NumberOfOrders, SUM(TotalPrice) AS DailySales " +
                    "FROM Orders " +
                    "GROUP BY OrderDate " +
                    "ORDER BY OrderDate";

    // advance Q.
    // adm. monthly sales
    public static final String MONTHLY_SALES =
            "SELECT YEAR(OrderDate) AS SalesYear, " +
                    "MONTH(OrderDate) AS SalesMonth, " +
                    "COUNT(OrderID) AS NumberOfOrders, " +
                    "SUM(TotalPrice) AS MonthlySales " +
                    "FROM Orders " +
                    "GROUP BY YEAR(OrderDate), MONTH(OrderDate) " +
                    "ORDER BY SalesYear, SalesMonth";

    //check if we have
    public static final String CHECK_USER_EXISTS =
            "SELECT * FROM Users WHERE UserID=? OR UserName=?";

    //don't have acc.
    public static final String INSERT_USER =
            "INSERT INTO Users (UserID, UserName, FirstName, LastName, UserEmail, CityName, StreetName, UserPhone, UserPassword, UserRole) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'CUSTOMER')";

    // add at cus. table
    public static final String INSERT_CUSTOMER =
            "INSERT INTO Customer (UserID, RegistrationDate) VALUES (?, ?)";
}
