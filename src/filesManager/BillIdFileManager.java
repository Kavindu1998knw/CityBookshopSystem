package filesManager;

import com.opencsv.CSVWriter;
import tm.billItems.BillDetails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillIdFileManager {
    public static void addBillId(String fileName, BillDetails BillDetails) throws IOException {
        FileWriter fileWriter=new FileWriter(fileName,true);
        CSVWriter csvWriter = new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{BillDetails.getBillId(), BillDetails.getPaymentOption()});
        csvWriter.writeAll(data);
        csvWriter.close();
    }

    public static List<BillDetails> getAllBillIds(String fileName) throws FileNotFoundException {
        List<BillDetails> billDetailsList = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            billDetailsList.add(new BillDetails(parts[0],parts[1]));
        }
        scanner.close();
        return billDetailsList;
    }
}
