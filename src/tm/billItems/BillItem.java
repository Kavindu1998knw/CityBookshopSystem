package tm.billItems;

public class BillItem {
    private String billId;
    private String bookName;
    private int quantity;
    private double ratePrice;
    private double price;

    public BillItem(String billId, String bookName, int quantity, double ratePrice, double price) {
        this.billId = billId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.ratePrice = ratePrice;
        this.price = price;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(double ratePrice) {
        this.ratePrice = ratePrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
