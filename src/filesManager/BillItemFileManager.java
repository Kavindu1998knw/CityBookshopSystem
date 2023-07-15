package filesManager;

import com.opencsv.CSVWriter;
import javafx.collections.ObservableList;
import tm.billItems.BillItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillItemFileManager {
    public static void addBillItem(String fileName, ObservableList<BillItem> list) throws IOException {
        FileWriter fileWriter=new FileWriter(fileName,true);
        CSVWriter csvWriter = new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        List<String[]> data = new ArrayList<>();
        for (BillItem billItem:list){
            data.add(new String[]{billItem.getBillId(),billItem.getBookName(), billItem.getQuantity()+"",billItem.getRatePrice()+"",billItem.getPrice()+""});
            System.out.println("xx");
        }
        csvWriter.writeAll(data);
        csvWriter.close();
    }

    public static List<BillItem> getAllBillItems(String fileName) throws FileNotFoundException {
        List<BillItem> billItems = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String billId = parts[0];
            String bookName = parts[1];
            String quantity = parts[2];
            String ratePrice = parts[3];
            String price = parts[4];
            billItems.add(new BillItem(billId,bookName,Integer.parseInt(quantity),Double.parseDouble(ratePrice),Double.parseDouble(price)));
        }
        scanner.close();
        return billItems;
    }
}
