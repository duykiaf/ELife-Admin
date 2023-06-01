package t3h.android.elifeadmin.models;

import androidx.annotation.NonNull;

public class DropdownItem {
    private int hiddenValue;

    private String displayText;

    public DropdownItem(int hiddenValue, String displayText) {
        this.hiddenValue = hiddenValue;
        this.displayText = displayText;
    }

    public int getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(int hiddenValue) {
        this.hiddenValue = hiddenValue;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @NonNull
    @Override
    public String toString() {
        return displayText;
    }
}
