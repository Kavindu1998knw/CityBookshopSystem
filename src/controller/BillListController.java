package controller;

import filesManager.BillIdFileManager;
import filesManager.BillItemFileManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tm.billItems.BillDetails;
import tm.billItems.BillItem;

import java.io.FileNotFoundException;
import java.util.List;

public class BillListController {

    public ListView<BillDetails> lstBookIds;
    public TableView<BillItem> tblBookList;
    public Label lblTotal;

    public void initialize() throws FileNotFoundException {
        loadList();
        tblBookList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BillItem>() {
            @Override
            public void changed(ObservableValue<? extends BillItem> observable, BillItem oldValue, BillItem newValue) {
                if (tblBookList.getSelectionModel().getSelectedItem()==null){
                    return;
                }
            }
        });

        lstBookIds.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BillDetails>() {
            @Override
            public void changed(ObservableValue<? extends BillDetails> observable, BillDetails oldValue, BillDetails newValue) {
                if (lstBookIds.getSelectionModel().getSelectedItems()==null){
                    return;
                }
                try {
                    loadTable();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        tblBookList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookName"));
        tblBookList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblBookList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("ratePrice"));
        tblBookList.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public void loadList() throws FileNotFoundException {
        ObservableList<BillDetails> items = lstBookIds.getItems();
        items.clear();
        List<BillDetails> allBillIds = BillIdFileManager.getAllBillIds("billId.csv");
        for (BillDetails BillDetails :allBillIds){
            items.add(BillDetails);
        }
    }

    public void loadTable() throws FileNotFoundException {
        ObservableList<BillItem> items = tblBookList.getItems();
        items.clear();
        List<BillItem> allBillItems = BillItemFileManager.getAllBillItems("billItems.csv");
        double total=0;
        for (BillItem billItem:allBillItems){
            if (billItem.getBillId().equals(lstBookIds.getSelectionModel().getSelectedItem().getBillId())){
                items.add(billItem);
                total+=billItem.getPrice();
            }
        }
        lblTotal.setText(total+"");
    }
}
