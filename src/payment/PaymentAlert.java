package payment;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;

public class PaymentAlert extends Payment{

    @Override
    public void onlinePayment() throws IOException {
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"Online Payment Successfully..", ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void cashPayment() throws IOException {
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"Cash Payment Successfully..", ButtonType.OK);
        alert.showAndWait();
    }

}
