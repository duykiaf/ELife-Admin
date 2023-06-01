package t3h.android.elifeadmin.helper;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.models.DropdownItem;

public class DropdownListHelper {
    public static List<DropdownItem> statusDropdown() {
        List<DropdownItem> status = new ArrayList<>();
        status.add(new DropdownItem(0, "Inactive"));
        status.add(new DropdownItem(1, "Active"));
        return status;
    }
}
