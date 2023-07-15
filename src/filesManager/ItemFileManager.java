package filesManager;

import com.opencsv.CSVWriter;
import tm.item.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ItemFileManager {
    public static List<Book> getAllBooks(String fileName) throws FileNotFoundException {
        List<Book> books=new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String line=scanner.nextLine();
            String[] parts=line.split(",");
            String name=parts[0];
            String author=parts[1];
            String price=parts[2];
            String quantity=parts[3];
            String category=parts[4];
            Book book=null;
            if (category.equals("Adventure")){
                book=new Adventure(name,author,Double.parseDouble(price),Integer.parseInt(quantity),"Adventure");
            }else if (category.equals("Fantasy")){
                book=new Fantasy(name,author,Double.parseDouble(price),Integer.parseInt(quantity),"Fantasy");
            }else if (category.equals("Historical Fiction")){
                book=new HistoricalFiction(name,author,Double.parseDouble(price),Integer.parseInt(quantity),"Historical Fiction");
            }else if (category.equals("Mystery")){
                book=new Mystery(name,author,Double.parseDouble(price),Integer.parseInt(quantity),"Mystery");
            }else if (category.equals("Science")){
                book=new Science(name,author,Double.parseDouble(price),Integer.parseInt(quantity),"Science");
            }
            if (book!=null){
                books.add(book);
            }
        }
        scanner.close();
        return books;
    }

    public static void addBook(Book book,String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        CSVWriter csvWriter=new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeNext(new String[]{book.getName(),book.getAuthor(),""+book.getPrice(),""+book.getQuantity(),book.getCategory()});
        csvWriter.close();
    }

    public static void addAllBooks(List<Book> books,String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        List<String[]> list=new ArrayList<>();
        for (Book book:books){
            list.add(new String[]{book.getName(),book.getAuthor(),book.getPrice()+"",book.getQuantity()+"",book.getCategory()});
        }
        csvWriter.writeAll(list);
        csvWriter.close();
    }
}
