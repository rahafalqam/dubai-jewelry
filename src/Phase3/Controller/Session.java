package Phase3.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Phase3.Model.CartItem;

public class Session {

    public static int customerID;

    public static int selectedKaratID;

    public static int selectedCategoryID;
    public static String customerName;
    public static String customerPhone;
    public static String customerAddress;

    public static ObservableList<CartItem> cart =
            FXCollections.observableArrayList();

}