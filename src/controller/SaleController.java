package controller;

import filesManager.BillIdFileManager;
import filesManager.BillItemFileManager;
import filesManager.ItemFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import payment.Payment;
import payment.PaymentAlert;
import tm.billItems.BillDetails;
import tm.billItems.BillItem;
import tm.item.Book;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaleController {

    public TableView<Book> tblList;
    public ChoiceBox<String> chcCategoryFilter;
    public TextField txtSearchBook;
    public Spinner<Integer> spnQuantity;
    public Label lblBookName;
    public Button btnDeleteFromBill;
    public Button btnUpDateQuantity;
    public Label lblBillId;
    public Label lblTotal;
    public TableView<BillItem> tblBillList;
    public Label lblPrice;
    public Button btnPayment;
    public AnchorPane root;
    public Label lblBookBalance;
    public Label lblBookTotalPrice;
    public TextField txtCashPrice;
    public ChoiceBox<String> chcPaymentOption;
    public Pane root2;
    public Pane root1;
    public Pane cashRoot;
    public Button btnAddToBill;
    public Label lblTotalRs;
    public Button btnCancelBill;


    boolean paymentSuccessfully=false;

    public void initialize() throws IOException {
        btnCancelBill.setDisable(true);
        btnPayment.setDisable(true);
        btnDeleteFromBill.setDisable(true);
        btnUpDateQuantity.setDisable(true);
        cashRoot.setVisible(false);
        lblBookTotalPrice.setDisable(true);
        tblBillList.setDisable(true);
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        ItemFileManager.addAllBooks(allBooks, "tem.csv");
        loadTable();
        lblTotalRs.setVisible(false);
        root1.setDisable(true);
        root2.setDisable(true);
        root2.setVisible(false);
        lblBillId.setText(autoGenerateId());
        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblList.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("category"));

        tblBillList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookName"));
        tblBillList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblBillList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("ratePrice"));
        tblBillList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("price"));

        tblList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                if(tblList.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                btnAddToBill.setDisable(false);
                btnDeleteFromBill.setDisable(true);
                btnUpDateQuantity.setDisable(true);
                root1IsHide(false);
                SpinnerValueFactory<Integer> spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,tblList.getSelectionModel().getSelectedItem().getQuantity());
                spinnerValueFactory.setValue(1);
                spnQuantity.setValueFactory(spinnerValueFactory);
                lblBookName.setText(tblList.getSelectionModel().getSelectedItem().getName());
                lblPrice.setText(tblList.getSelectionModel().getSelectedItem().getPrice()+"");
                spnQuantity.setDisable(false);
            }
        });

        tblBillList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BillItem>() {
            @Override
            public void changed(ObservableValue<? extends BillItem> observable, BillItem oldValue, BillItem newValue) {
                if(tblBillList.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                btnAddToBill.setDisable(true);
                btnDeleteFromBill.setDisable(false);
                btnUpDateQuantity.setDisable(false);
                int quantity = 0;
                try {
                    List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
                    for (Book book:allBooks){
                        if (tblBillList.getSelectionModel().getSelectedItem().getBookName().equals(book.getName())){
                            quantity=book.getQuantity()+tblBillList.getSelectionModel().getSelectedItem().getQuantity();
                        }
                    }
                    SpinnerValueFactory<Integer> spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,quantity);
                    spinnerValueFactory.setValue(tblBillList.getSelectionModel().getSelectedItem().getQuantity());
                    spnQuantity.setValueFactory(spinnerValueFactory);
                    lblBookName.setText(tblBillList.getSelectionModel().getSelectedItem().getBookName());
                    lblPrice.setText(tblBillList.getSelectionModel().getSelectedItem().getPrice()+"");
                    spnQuantity.setDisable(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                
            }
        });
        chcCategoryFilter.getItems().addAll("All","Adventure","Fantasy","Historical Fiction","Mystery","Science");
        chcCategoryFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(chcCategoryFilter.getSelectionModel().getSelectedItem()==null){
                    return;
                }else {
                    try {
                        categoryFilterLoadTable();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        chcPaymentOption.getItems().addAll("Online Payment","Cash Payment");
        chcPaymentOption.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (chcPaymentOption.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                else if (chcPaymentOption.getSelectionModel().getSelectedItem().equals("Online Payment")){
                    lblBookTotalPrice.setDisable(false);
                    cashRoot.setVisible(false);
                }else if (chcPaymentOption.getSelectionModel().getSelectedItem().equals("Cash Payment")){
                    lblBookTotalPrice.setDisable(false);
                    cashRoot.setVisible(true);
                }
            }
        });
    }

    private void loadTable() throws FileNotFoundException {
        clearDetails();
        tblList.getItems().clear();
        List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
        for (Book book:allBooks){
            if (book.getQuantity()!=0){
                tblList.getItems().addAll(book);
            }
        }
    }

    public void categoryFilterLoadTable() throws FileNotFoundException {
        String selectedItem = chcCategoryFilter.getSelectionModel().getSelectedItem();
        List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        for (Book book:allBooks){
            String category = book.getCategory();
            if (selectedItem.equals("All")){
                loadTable();
            }else if (category.equals(selectedItem)){
                items.add(book);
            }
        }
        tblList.refresh();
    }

    public void txtSearchBookOnKeyReleased(KeyEvent keyEvent) throws FileNotFoundException {
        List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        String searchBook = txtSearchBook.getText();
        for (Book book:allBooks){
            if (book.getName().contains(searchBook)&&book.getQuantity()!=0){
                items.add(book);
            }
        }
    }

    public void btnAddToBillOnAction(ActionEvent actionEvent) throws IOException {
        btnAddToBill.setDisable(true);
        if (tblList.getSelectionModel().getSelectedItem()==null){
            return;
        }else {
            ObservableList<BillItem> items = tblBillList.getItems();
            boolean alReadyAdded=false;
            for (BillItem book:items){
                if (book.getBookName().equals(lblBookName.getText())){
                    alReadyAdded=true;
                }
            }
            if (!alReadyAdded){
                btnCancelBill.setDisable(false);
                btnPayment.setDisable(false);
                Book selectedItem = tblList.getSelectionModel().getSelectedItem();
                BillItem billItem = new BillItem(lblBillId.getText(), selectedItem.getName(), spnQuantity.getValue(), selectedItem.getPrice(), selectedItem.getPrice() *  spnQuantity.getValue());
                tblBillList.getItems().add(billItem);
                totalPrice();
                tblBillList.setDisable(false);
                lblTotalRs.setVisible(true);
                List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
                for (Book book:allBooks){
                    if (selectedItem.getName().equals(book.getName())){
                        book.setQuantity(book.getQuantity()-spnQuantity.getValue());
                    }
                }
                ItemFileManager.addAllBooks(allBooks,"tem.csv");
                loadTable();
            }else {
                Alert alert=new Alert(Alert.AlertType.INFORMATION,"Already Added to Bill list",ButtonType.OK);
                alert.showAndWait();
            }
            clearDetails();
        }
    }

    public void btnDeleteFromBillOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete book from bill?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            ObservableList<BillItem> items = tblBillList.getItems();
            List<BillItem> tmList=new ArrayList<>();
            BillItem selectedItem = tblBillList.getSelectionModel().getSelectedItem();
            for (BillItem billItem:items){
                if (!selectedItem.equals(billItem)){
                    tmList.add(billItem);
                }
            }
            List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
            for (Book book:allBooks){
                if (book.getName().equals(selectedItem.getBookName())){
                    book.setQuantity(book.getQuantity()+selectedItem.getQuantity());
                }
            }
            ItemFileManager.addAllBooks(allBooks,"tem.csv");
            loadTable();
            loadBillTable(tmList);
        }
    }

    public void btnUpDateQuantityOnAction(ActionEvent actionEvent) throws IOException {
        List<Book> allBooks = ItemFileManager.getAllBooks("tem.csv");
        BillItem selectedItem = tblBillList.getSelectionModel().getSelectedItem();
        for (Book book:allBooks){
            if (selectedItem.getBookName().equals(book.getName())){
                book.setQuantity(book.getQuantity()+selectedItem.getQuantity()-spnQuantity.getValue());
            }
        }
        ItemFileManager.addAllBooks(allBooks,"tem.csv");
        ObservableList<BillItem> items = tblBillList.getItems();
        List<BillItem> listBill=new ArrayList<>();
        for (BillItem billItem:items){
            if (billItem.getBookName().equals(selectedItem.getBookName())){
                billItem.setQuantity(spnQuantity.getValue());
            }
            listBill.add(billItem);
        }
        loadBillTable(listBill);
        loadTable();
        clearDetails();
    }

    public void totalPrice(){
        ObservableList<BillItem> items = tblBillList.getItems();
        double total=0;
        for(BillItem book:items){
            total+=book.getPrice();
        }
        lblTotal.setText(""+total);
    }

    public String autoGenerateId() throws FileNotFoundException {
        String id;
        List<BillDetails> allBillDetails = BillIdFileManager.getAllBillIds("billId.csv");
        if (!allBillDetails.isEmpty()){
            BillDetails BillDetails = allBillDetails.get(allBillDetails.size() - 1);
            String billId = BillDetails.getBillId();
            String substring = billId.substring(1, billId.length());
            int intId = Integer.parseInt(substring);
            intId++;
            if(intId<10){
                id="B00"+intId;
            } else if (intId<100) {
                id="B0"+intId;
            } else{
                id="B"+intId;
            }
        }else {
            id="B001";
        }
        return id;
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        root1.setDisable(true);
        root1.setVisible(false);
        root2.setDisable(false);
        root2.setVisible(true);
        lblBookTotalPrice.setText(lblTotal.getText());
    }

    public void loadBillTable(List<BillItem> billItems){
        clearDetails();
        tblBillList.getItems().clear();
        for (BillItem billItem:billItems){
            tblBillList.getItems().add(billItem);
        }
        tblBillList.refresh();
    }

    private void clearDetails(){
        lblBookName.setText("");
        lblPrice.setText("");
        SpinnerValueFactory<Integer> i=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,0);
        spnQuantity.setValueFactory(i);
    }

    public void btnOkOnAction(ActionEvent actionEvent) throws IOException {
        if (chcPaymentOption.getSelectionModel().getSelectedItem().equals("Cash Payment")&&txtCashPrice.getText().isEmpty()){
            txtCashPrice.setStyle("-fx-border-color:red;");
            txtCashPrice.requestFocus();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want to pay?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)){
                Payment payment=new PaymentAlert();
                if (chcPaymentOption.getSelectionModel().getSelectedItem().equals("Online Payment")){
                    payment.onlinePayment();
                    paymentSuccessfully=true;
                    writeData();
                }else if (chcPaymentOption.getSelectionModel().getSelectedItem().equals("Cash Payment")){
                    if (Double.parseDouble(lblTotal.getText())<=Double.parseDouble(txtCashPrice.getText())){
                        payment.cashPayment();
                        paymentSuccessfully=true;
                        writeData();
                    }else {
                        txtCashPrice.setStyle("-fx-border-color:red;");
                    }
                }
                if (paymentSuccessfully){
                    BillIdFileManager.addBillId("billId.csv",new BillDetails(lblBillId.getText(),chcPaymentOption.getSelectionModel().getSelectedItem()));
                    ObservableList<BillItem> items = tblBillList.getItems();
                    BillItemFileManager.addBillItem("billItems.csv",items);
                    tblBillList.getItems().clear();
                    lblBillId.setText(autoGenerateId());
                }
            }else {
                clearPayment();
            }
        }
    }

    public void txtCashOnKeyReleased(KeyEvent keyEvent) {
        double cash = Double.parseDouble(txtCashPrice.getText());
        double total = Double.parseDouble(lblBookTotalPrice.getText());
        lblBookBalance.setText((cash-total)+"");
    }

    public void clearPayment() throws IOException {
        lblTotal.setText("");
        clearDetails();
        root1IsHide(false);
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        ItemFileManager.addAllBooks(allBooks, "tem.csv");
        loadTable();
    }

    public void btnCancelBillOnAction(ActionEvent actionEvent) throws IOException {
        clearPayment();
        tblBillList.getItems().clear();
        tblBillList.refresh();
        btnCancelBill.setDisable(true);
        btnPayment.setDisable(true);
    }

    private void writeData() throws IOException {
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        ObservableList<Book> items = tblList.getItems();
        for (Book book:allBooks){
            for (Book tblBook:items){
                if (book.getName().equals(tblBook.getName())){
                    book.setQuantity(tblBook.getQuantity());
                }
            }
        }
        ItemFileManager.addAllBooks(allBooks, "books.csv");
        loadTable();
        clearPayment();
        lblBillId.setText(autoGenerateId());
    }

    public void root1IsHide(boolean a){
        root2.setDisable(!a);
        root2.setVisible(a);
        root1.setDisable(a);
        root1.setVisible(!a);
    }
}