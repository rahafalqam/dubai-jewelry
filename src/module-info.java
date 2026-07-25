/**
 * 
 */
/**
 * 
 */
module Phase3 {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports Phase3;
    exports Phase3.Controller;
    exports Phase3.Model;

    opens Phase3.Controller to javafx.fxml;
    opens Phase3.Model to javafx.base;
}