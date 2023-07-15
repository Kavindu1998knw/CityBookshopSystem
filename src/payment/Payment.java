package payment;

import java.io.IOException;

public abstract class Payment {
    public abstract void onlinePayment() throws IOException;
    public abstract void cashPayment() throws IOException;
}
