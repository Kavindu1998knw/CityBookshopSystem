package controller;

import filesManager.ItemFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import tm.item.Book;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeletedBooksController {
    public TableView<Book> tblList;
    public Pane root1;
    public TextField txtQuantity;
    public Label lblBookName;
    public Label lblAuthor;
    public ChoiceBox<String> chcCategoryFilter;
    public TextField txtSearchBook;
    public Label lblPrice;
    public Label lblCategory;
    public Label lblPleaseUpdateQuantity;

    public void initialize() throws FileNotFoundException {
        root1.setDisable(true);
        lblPleaseUpdateQuantity.setVisible(false);
        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("category"));
        tblList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                if(tblList.getSelectionModel().getSelectedItem()==null){
                    return;
                }else {
                    root1.setDisable(false);
                    Book selectedBook = tblList.getSelectionModel().getSelectedItem();
                    lblBookName.setText(selectedBook.getName());
                    lblAuthor.setText(selectedBook.getAuthor());
                    txtQuantity.clear();
                    txtQuantity.setText(selectedBook.getQuantity()+"");
                    lblPrice.setText(selectedBook.getPrice()+"");
                    lblCategory.setText(selectedBook.getCategory());
                }

            }
        });
        loadTable();

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

    public void btnAddToListOnAction(ActionEvent actionEvent) throws IOException {
        if (txtQuantity.getText().equals("0")){
            lblPleaseUpdateQuantity.setVisible(true);
        }else {
            lblPleaseUpdateQuantity.setVisible(false);
            List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
            Book selectedItem = tblList.getSelectionModel().getSelectedItem();
            for (Book book:allBooks){
                if (selectedItem.getCategory().equals(book.getCategory())&&selectedItem.getName().equals(book.getName())&&selectedItem.getAuthor().equals(book.getAuthor())&&(selectedItem.getQuantity()==book.getQuantity())&&(selectedItem.getPrice()==book.getPrice())){
                    book.setQuantity(Integer.parseInt(txtQuantity.getText()));
                }
            }
            ItemFileManager.addAllBooks(allBooks, "books.csv");
            loadTable();
        }
    }

    public void txtSearchBookOnKeyReleased(KeyEvent keyEvent) throws FileNotFoundException {
        tblList.getItems().clear();
        List<Book> items = ItemFileManager.getAllBooks("books.csv");
        for (Book book:items){
            if (book.getName().contains(txtSearchBook.getText())&&book.getQuantity()==0){
                tblList.getItems().add(book);
            }
        }
    }

    void loadTable() throws FileNotFoundException {
        root1.setDisable(true);
        lblPrice.setText("");
        lblCategory.setText("");
        lblBookName.setText("");
        lblAuthor.setText("");
        txtQuantity.clear();
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        for (Book book:allBooks){
            if (book.getQuantity()==0){
                items.add(book);
            }
        }
        tblList.refresh();
    }

    public void categoryFilterLoadTable() throws FileNotFoundException {
        String selectedItem = chcCategoryFilter.getSelectionModel().getSelectedItem();
        List<Book> allBooks = ItemFileManager.getAllBooks("books.csv");
        ObservableList<Book> items = tblList.getItems();
        items.clear();
        for (Book book:allBooks){
            String category = book.getCategory();
            if (selectedItem.equals("All")){
                loadTable();
            }else if (category.equals(selectedItem)&&book.getQuantity()==0){
                items.add(book);
            }
        }
        tblList.refresh();
    }

}
