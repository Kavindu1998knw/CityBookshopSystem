package controller;

import filesManager.UserFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import tm.user.Cashier;
import tm.user.Manager;
import tm.user.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CashierManageController {

    public Pane root1;
    public TextField txtUserName;
    public TextField txtEmail;
    public ChoiceBox<String> chcAddLoginType;
    public PasswordField txtPassword;
    public PasswordField txtConfirmPassword;
    public Label lblPasswordNotMatch;
    public Pane root2;
    public ChoiceBox<String> chcUserType;
    public Label lblName;
    public Label lblEmail;
    public ChoiceBox<String> chcUserTypeFilter;
    public TableView<User> tblList;
    public Button btnAddUser;

    public void initialize() throws IOException {
        lblPasswordNotMatch.setVisible(false);
        root1.setDisable(true);
        root2.setDisable(true);
        chcAddLoginType.getItems().addAll("Manager","Cashier");
        chcUserTypeFilter.getItems().addAll("All","Manager","Cashier");
        chcUserType.getItems().addAll("Manager","Cashier");
        loadTable();
        chcAddLoginType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(chcAddLoginType.getSelectionModel().getSelectedItem()==null){
                    return;
                }
            }
        });
        tblList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                if(tblList.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                else {
                    root2.setDisable(false);
                    root1.setDisable(true);
                    User selectedItem = tblList.getSelectionModel().getSelectedItem();
                    lblName.setText(selectedItem.getUserName());
                    lblEmail.setText(selectedItem.getEmail());
                    chcUserType.setValue(selectedItem.getUserType());
                    btnAddUser.setDisable(false);
                }
            }
        });
        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("userType"));
        chcUserType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (chcUserType.getSelectionModel().getSelectedItem()==null){
                    return;
                }

            }
        });

        chcUserTypeFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (chcUserTypeFilter.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                try {
                    chcFilterOnActon();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTable() throws IOException {
        btnAddUser.setDisable(false);
        root2.setDisable(true);
        root1.setDisable(true);
        tblList.getSelectionModel().clearSelection();
        lblName.setText("");
        lblEmail.setText("");
        chcUserType.getSelectionModel().clearSelection();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtEmail.clear();
        txtUserName.clear();
        lblPasswordNotMatch.setVisible(false);
        chcAddLoginType.getSelectionModel().clearSelection();
        txtPassword.setStyle("-fx-border-color:transparent;");
        txtConfirmPassword.setStyle("-fx-border-color:transparent;");
        List<User> allUsers = UserFileManager.getAllUsers("users.csv");
        ObservableList<User> items = tblList.getItems();
        items.clear();
        for (User user:allUsers){
            items.add(user);
        }
        tblList.refresh();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        List<User> allUsers = UserFileManager.getAllUsers("users.csv");
        for (User user:allUsers){
            if (user.getUserName().equals(tblList.getSelectionModel().getSelectedItem().getUserName())&&user.getEmail().equals(tblList.getSelectionModel().getSelectedItem().getEmail())){
                user.setUserType(chcUserType.getSelectionModel().getSelectedItem());
            }
        }
        UserFileManager.addAllUsers(allUsers, "users.csv");
        loadTable();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete User?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            List<User> allUsers = UserFileManager.getAllUsers("users.csv");
            List<User> users=new ArrayList<>();
            for (User user:allUsers){
                if (user.getUserName().equals(tblList.getSelectionModel().getSelectedItem().getUserName())&&user.getEmail().equals(tblList.getSelectionModel().getSelectedItem().getEmail())){

                }else {
                    users.add(user);
                }
            }
            UserFileManager.addAllUsers(users, "users.csv");
            loadTable();
        }
    }

    public void chcFilterOnActon() throws IOException {
        List<User> allUsers = UserFileManager.getAllUsers("users.csv");
        ObservableList<User> items = tblList.getItems();
        tblList.getItems().clear();
        for (User user:allUsers){
            if (chcUserTypeFilter.getSelectionModel().getSelectedItem().equals("All")){
                loadTable();
            }else if(chcUserTypeFilter.getSelectionModel().getSelectedItem().equals(user.getUserType())) {
                items.add(user);
            }
        }
    }

    public void btnAddUserOnAction(ActionEvent actionEvent) {
        root1.setDisable(false);
        btnAddUser.setDisable(true);
        tblList.getSelectionModel().clearSelection();
        lblName.setText("");
        lblEmail.setText("");
        chcUserType.getSelectionModel().clearSelection();
        root2.setDisable(true);
    }

    public void btnOkOnAction() throws IOException {
        if (txtUserName.getText().isEmpty()){
            txtUserName.requestFocus();
        }else if (txtEmail.getText().isEmpty()){
            txtEmail.requestFocus();
        }else if (txtPassword.getText().isEmpty()){
            txtPassword.requestFocus();
        }else if (txtConfirmPassword.getText().isEmpty()){
            txtConfirmPassword.requestFocus();
        }else if (txtConfirmPassword.getText().trim().equals(txtPassword.getText().trim())){
            String userName = txtUserName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            User user=null;
            if (chcAddLoginType.getSelectionModel().getSelectedItem().equals("Manager")){
                user=new Manager(userName,email,password,chcAddLoginType.getSelectionModel().getSelectedItem());
            }else if(chcAddLoginType.getSelectionModel().getSelectedItem().equals("Cashier")){
                user=new Cashier(userName,email,password,chcAddLoginType.getSelectionModel().getSelectedItem());
            }else {
                System.out.println("not support user");
            }
            UserFileManager.addUser(user, "./users.csv");
            loadTable();
        }else {
            lblPasswordNotMatch.setVisible(true);
            txtPassword.clear();
            txtPassword.requestFocus();
            txtConfirmPassword.clear();
            txtPassword.setStyle("-fx-border-color:red;");
            txtConfirmPassword.setStyle("-fx-border-color:red;");
        }
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtEmail.requestFocus();
    }

    public void txtEmailOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        txtConfirmPassword.requestFocus();
    }

    public void txtConfirmPasswordOnAction(ActionEvent actionEvent) {
        chcAddLoginType.show();
    }
}
