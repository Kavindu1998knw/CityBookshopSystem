package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class CashierController {
    public AnchorPane root;
    public StackPane subRoot;
    public ImageView imgSideImage;
    public JFXButton btnItemsSale;
    public JFXButton btnBillHistory;

    public void initialize() throws IOException {
        loadSubRoot("../view/SaleForm.fxml");
        Image image = new Image("img/City.png");
        imgSideImage.setImage(image);
    }

    public void btnBillHistoryOnAction(ActionEvent actionEvent) throws IOException {
        loadSubRoot("../view/BillListForm.fxml");
        btnItemsSale.setStyle("-fx-background-color:#0a9ccd;");
        btnBillHistory.setStyle("-fx-background-color:#1200ff;");
    }

    private void loadSubRoot(String fileName) throws IOException {
        Parent parent= FXMLLoader.load(this.getClass().getResource(fileName));
        subRoot.getChildren().clear();
        subRoot.getChildren().addAll(parent);
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want To Log out?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Parent parent=FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage= (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        }
    }

    public void btnItemsSaleOnAction(ActionEvent actionEvent) throws IOException {
        loadSubRoot("../view/SaleForm.fxml");
        btnItemsSale.setStyle("-fx-background-color:#1200ff;");
        btnBillHistory.setStyle("-fx-background-color:#0a9ccd;");
    }
}
