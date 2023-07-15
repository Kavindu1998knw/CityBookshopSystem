package controller;

import filesManager.UserFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.user.User;
import java.io.IOException;
import java.util.List;

public class LoginController {
    public PasswordField txtUserPassword;
    public TextField txtUserName;
    public Label lblUserNameOrPasswordIncorrect;
    public AnchorPane root;
    public ChoiceBox<String> chcLoginType;
    public AnchorPane root2;
    public ImageView imgLoginIcon;

    public void initialize(){
        lblUserNameOrPasswordIncorrect.setVisible(false);
        root2.setDisable(true);
        chcLoginType.getItems().clear();
        chcLoginType.getItems().addAll("Manager","Cashier");
        chcLoginType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(chcLoginType.getSelectionModel().getSelectedItem()==null){
                    return;
                }else{
                    root2.setDisable(false);
                    txtUserName.requestFocus();
                }
            }
        });
        Image image = new Image("img/loginIcon.png");
        imgLoginIcon.setImage(image);
    }

    public void btnOkOnAction() throws IOException {
        String userName = txtUserName.getText();
        String userPassword = txtUserPassword.getText();
        List<User> allUsers = UserFileManager.getAllUsers("./users.csv");
        boolean validUser = false;
        for (User user:allUsers){
            if(user.getUserName().equals(userName)&&user.getPassword().equals(userPassword)&&user.getClass().getSimpleName().equals(chcLoginType.getSelectionModel().getSelectedItem())) {
                validUser=true;
                lblUserNameOrPasswordIncorrect.setVisible(false);
                txtUserName.setStyle("-fx-border-color:transparent;");
                txtUserPassword.setStyle("-fx-border-color:transparent;");
                if (chcLoginType.getSelectionModel().getSelectedItem().equals("Cashier")){
                    loadForm("../view/CashierForm.fxml");
                }else {
                    loadForm("../view/ManagerForm.fxml");
                }

            }
        }
        if (!validUser){
            lblUserNameOrPasswordIncorrect.setVisible(true);
            txtUserPassword.clear();
            txtUserName.clear();
            txtUserName.setStyle("-fx-border-color:red;");
            txtUserPassword.setStyle("-fx-border-color:red;");
            txtUserName.requestFocus();
        }
    }

    public void loadForm(String fileName) throws IOException {
        Parent parent=FXMLLoader.load(this.getClass().getResource(fileName));
        Scene scene = new Scene(parent);
        Stage primaryStage= (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtUserPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) throws IOException {
        btnOkOnAction();
    }
}
