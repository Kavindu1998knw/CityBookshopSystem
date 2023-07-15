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

public class ManagerController {
    public AnchorPane root;
    public StackPane subRoot;
    public ImageView imgLogo;
    public JFXButton btnCashierManage;
    public JFXButton btnBooks;
    public JFXButton btnBillHistory;
    public JFXButton btnDeletedBooks;

    public void initialize() throws IOException {
        btnCashiersManageOnAction();
        Image image = new Image("img/City.png");
        imgLogo.setImage(image);
        btnBillHistory.setStyle("-fx-background-color:#0a9ccd;");
        btnDeletedBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnCashierManage.setStyle("-fx-background-color:#1200ff;");
    }

    public void btnCashiersManageOnAction() throws IOException {
        loadSubRoot("../view/CashierManageForm.fxml");
        btnCashierManage.setStyle("-fx-background-color:#1200ff;");
        btnBillHistory.setStyle("-fx-background-color:#0a9ccd;");
        btnDeletedBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnBooks.setStyle("-fx-background-color:#0a9ccd;");
    }

    public void btnBooksOnAction() throws IOException {
        loadSubRoot("../view/BooksForm.fxml");
        btnBooks.setStyle("-fx-background-color:#1200ff;");
        btnBillHistory.setStyle("-fx-background-color:#0a9ccd;");
        btnDeletedBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnCashierManage.setStyle("-fx-background-color:#0a9ccd;");
    }

    public void btnDeletedBooksOnAction() throws IOException {
        loadSubRoot("../view/DeletedBooksForm.fxml");
        btnDeletedBooks.setStyle("-fx-background-color:#1200ff;");
        btnBillHistory.setStyle("-fx-background-color:#0a9ccd;");
        btnBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnCashierManage.setStyle("-fx-background-color:#0a9ccd;");
    }

    public void btnBillHistory(ActionEvent actionEvent) throws IOException {
        loadSubRoot("../view/BillListForm.fxml");
        btnBillHistory.setStyle("-fx-background-color:#1200ff;");
        btnDeletedBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnBooks.setStyle("-fx-background-color:#0a9ccd;");
        btnCashierManage.setStyle("-fx-background-color:#0a9ccd;");
    }
    public void loadSubRoot(String fileName) throws IOException {
        Parent parent= FXMLLoader.load(this.getClass().getResource(fileName));
        subRoot.getChildren().clear();
        subRoot.getChildren().addAll(parent);
    }

    public void btnLogOutOnAction() throws IOException {
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




}
