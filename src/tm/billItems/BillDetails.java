package tm.billItems;

public class BillDetails {
    private String billId;
    private String paymentOption;

    public BillDetails() {
    }

    public BillDetails(String billId, String paymentOption) {
        this.setBillId(billId);
        this.setPaymentOption(paymentOption);
    }


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String toString(){
        return billId+"             "+paymentOption;
    }
}
