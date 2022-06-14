module gamepackage.game2048 {
    requires javafx.controls;
    requires javafx.fxml;


    opens gamepackage.game2048 to javafx.fxml;
    exports gamepackage.game2048;
}