package t3h.android.elifeadmin.listener;

import t3h.android.elifeadmin.databinding.ItemListLayoutBinding;

public interface OnBindViewListener<T> {
    void onBindView(T model, ItemListLayoutBinding view);
}
