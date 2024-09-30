package programming;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ChangeListener implements javafx.beans.value.ChangeListener<String> {

    private int maxLength;
    private TextField textField;

    public ChangeListener(TextField textField, int maxLength) {
        this.textField= textField;
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void changed(ObservableValue<? extends String> ov, String oldValue,
    String newValue) {

        if (newValue == null) {
            return;
        }

        if (newValue.length() > maxLength) {
            textField.setText(oldValue);
        } else {
            textField.setText(newValue);
        }
    }
}// End of Class
