package controller;

import filesManager.ItemFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import tm.item.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BooksController {
    public TableView<Book> tblList;
    public Pane root1;
    public TextField txtAddBookName;
    public TextField txtAddQuantity;
    public TextField txtAddAuthorName;
    public Pane root2;
    public Label lblName;
    public TextField txtQuantity;
    public TextField txtPrice;
    public Label lblAuthor;
    public ChoiceBox<String> chcCategory;
    public ChoiceBox<String> chcAddCategory;
    public TextField txtAddPrice;
    public ChoiceBox<String> chcCategoryFilter;
    public TextField txtSearchBook;
    public Button btnAddBook;

    public void initialize() throws FileNotFoundException {
        root1.setDisable(true);
        root2.setDisable(true);
        chcAddCategory.getItems().addAll("Adventure","Fantasy","Historical Fiction","Mystery","Science");
        chcCategory.getItems().addAll("Adventure","Fantasy","Historical Fiction","Mystery","Science");

        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblList.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("category"));

        chcAddCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (chcAddCategory.getSelectionModel().getSelectedItem()==null){
                    return;
                }
            }
        });
        loadTable();
        tblList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                if(tblList.getSelectionModel().getSelectedItem()==null){
                    return;
                }else {
                    Book selectedBook = tblList.getSelectionModel().getSelectedItem();
                    lblName.setText(selectedBook.getName());
                    lblAuthor.setText(selectedBook.getAuthor());
                    txtQuantity.clear();
                    txtQuantity.setText(selectedBook.getQuantity()+"");
                    txtPrice.clear();
                    txtPrice.setText(selectedBook.getPrice()+"");
                    chcCategory.setValue(selectedBook.getCategory());
                    root2.setDisable(false);
                    root1.setDisable(true);
                    txtAddBookName.clear();
                    txtAddAuthorName.clear();
                    txtAddPrice.clear();
                    txtAddQuantity.clear();
                    chcAddCategory.getSelectionModel().clearSelection();
                    btnAddBook.setDisable(false);
                }

            }
        });
        chcCategoryFilter.getItems().addAll("All","Adventure","Fantasy","Historical Fiction","Mystery","Science");
        chcCategoryFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (chcCategoryFilter.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                try {
                    categoryFilterLoadTable();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void btnAddBookOnAction(ActionEvent actionEvent) throws FileNotFoundException {
        root1.setDisable(false);
        root2.setDisable(true);
        btnAddBook.setDisable(true);
    }

    public void btnOkOnAction(ActionEvent actionEvent) throws IOException {
        addBook();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        Book selectedItem = tblList.getSelectionModel().getSelectedItem();
        for (Book book:allBooks){
            if (selectedItem.getCategory().equals(book.getCategory())&&selectedItem.getName().equals(book.getName())&&selectedItem.getAuthor().equals(book.getAuthor())&&(selectedItem.getQuantity()==book.getQuantity())&&(selectedItem.getPrice()==book.getPrice())){
                book.setQuantity(Integer.parseInt(txtQuantity.getText()));
                book.setPrice(Double.parseDouble(txtPrice.getText()));
                book.setCategory(chcCategory.getSelectionModel().getSelectedItem());
            }
        }
        ItemFileManager.addAllBooks(allBooks, "books.csv");
        loadTable();
        root2.setDisable(true);
        chcCategoryFilter.getSelectionModel().clearSelection();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete book?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
            Book selectedItem = tblList.getSelectionModel().getSelectedItem();
            for (Book book:allBooks){
                if (selectedItem.getCategory().equals(book.getCategory())&&selectedItem.getName().equals(book.getName())&&selectedItem.getAuthor().equals(book.getAuthor())&&(selectedItem.getQuantity()==book.getQuantity())&&(selectedItem.getPrice()==book.getPrice())){
                    book.setQuantity(0);
                }
            }
            ItemFileManager.addAllBooks(allBooks, "books.csv");
            tblList.getSelectionModel().clearSelection();
            loadTable();
            root2.setDisable(true);
            chcCategoryFilter.getSelectionModel().clearSelection();
        }
    }

    private void categoryFilterLoadTable() throws FileNotFoundException {
        String selectedItem = chcCategoryFilter.getSelectionModel().getSelectedItem();
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
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
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        String searchBook = txtSearchBook.getText();
        for (Book book:allBooks){
            if (book.getName().contains(searchBook)&&book.getQuantity()!=0){
                items.add(book);
            }
        }
    }

    public void addBook() throws IOException {
        if (txtAddBookName.getText().isEmpty()){
            txtAddBookName.requestFocus();
        }else if (txtAddAuthorName.getText().isEmpty()){
            txtAddAuthorName.requestFocus();
        }else if (txtAddPrice.getText().isEmpty()){
            txtAddPrice.requestFocus();
        }else if (txtAddQuantity.getText().isEmpty()){
            txtAddQuantity.requestFocus();
        } else if (chcAddCategory.getSelectionModel().getSelectedItem().isEmpty()) {
            chcAddCategory.show();
        }else {
            Double price=Double.parseDouble(txtAddPrice.getText());
            int quantity=Integer.parseInt(txtAddQuantity.getText());
            String author = txtAddAuthorName.getText();
            String name = txtAddBookName.getText();
            String selectedItem = chcAddCategory.getSelectionModel().getSelectedItem();
            Book book=null;
            if(selectedItem.equals("Adventure")){
                book=new Adventure(name,author,price,quantity,"Adventure");
            }else if(selectedItem.equals("Fantasy")){
                book=new Fantasy(name,author,price,quantity,"Fantasy");
            }else if(selectedItem.equals("Historical Fiction")){
                book=new HistoricalFiction(name,author,price,quantity,"Historical Fiction");
            }else if(selectedItem.equals("Mystery")){
                book=new Mystery(name,author,price,quantity,"Mystery");
            }else if(selectedItem.equals("Science")){
                book=new Science(name,author,price,quantity,"Science");
            }
            if(book!=null){
                ItemFileManager.addBook(book, "books.csv");
            }
            loadTable();
        }
    }

    void loadTable() throws FileNotFoundException {
        btnAddBook.setDisable(false);
        lblAuthor.setText("");
        lblName.setText("");
        txtQuantity.clear();
        txtPrice.clear();
        chcCategory.getSelectionModel().clearSelection();
        txtAddBookName.clear();
        txtAddAuthorName.clear();
        txtAddPrice.clear();
        txtAddQuantity.clear();
        chcAddCategory.getSelectionModel().clearSelection();
        root1.setDisable(true);
        root2.setDisable(true);
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        for (Book book:allBooks){
            if (book.getQuantity()!=0){
                items.add(book);
            }
        }
        tblList.refresh();
    }

    public void txtSearchOnMouseClicked(MouseEvent mouseEvent) throws FileNotFoundException {
        if (txtSearchBook.getText().isEmpty()){
            chcCategoryFilter.getSelectionModel().clearSelection();
            loadTable();
        }
    }

    public void txtAddBookNameOnAction(ActionEvent actionEvent) {
        txtAddAuthorName.requestFocus();
    }

    public void txtAddQuantityOnAction(ActionEvent actionEvent) {
        chcAddCategory.show();
    }

    public void txtAddPriceOnAction(ActionEvent actionEvent) {
        txtAddQuantity.requestFocus();
    }

    public void txtAddAuthorNameOnAction(ActionEvent actionEvent) {
        txtAddPrice.requestFocus();
    }
}
