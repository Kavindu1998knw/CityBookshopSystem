package filesManager;
import com.opencsv.CSVWriter;
import tm.item.Book;
import tm.user.Cashier;
import tm.user.Manager;
import tm.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserFileManager {
    public static List<User> getAllUsers(String fileName) throws IOException{
        List<User> users = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String userName = parts[0];
            String email = parts[1];
            String password = parts[2];
            String loginType = parts[3];
            User user = null;
            if (loginType.equals("Manager")) {
                user = new Manager(userName,email,password,loginType);
            } else if (loginType.equals("Cashier")) {
                user = new Cashier(userName,email,password,loginType);
            }
            if (user != null) {
                users.add(user);
            }
        }
        scanner.close();
        return users;
    }

    public static void addUser(User user, String fileName) throws IOException {
        FileWriter fileWriter=new FileWriter(fileName,true);
        CSVWriter csvWriter = new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{user.getUserName(),user.getEmail(), user.getPassword(),user.getClass().getSimpleName()});
        csvWriter.writeAll(data);
        csvWriter.close();
    }

    public static void addAllUsers(List<User> users, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
        List<String[]> list=new ArrayList<>();
        for (User user:users){
            list.add(new String[]{user.getUserName(),user.getEmail(),user.getPassword(),user.getUserType()});
        }
        csvWriter.writeAll(list);
        csvWriter.close();
    }
}
