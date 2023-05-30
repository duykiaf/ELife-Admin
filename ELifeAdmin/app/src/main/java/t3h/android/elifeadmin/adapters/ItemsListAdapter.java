package t3h.android.elifeadmin.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.databinding.ItemListLayoutBinding;
import t3h.android.elifeadmin.listener.OnBindViewListener;

public class ItemsListAdapter<T> extends RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder> {
    private List<T> data;
    private OnBindViewListener<T> onBindViewListener;

    public ItemsListAdapter() {
        data = new ArrayList<>();
    }

    public void bindAdapter(OnBindViewListener<T> onBindViewListener) {
        this.onBindViewListener = onBindViewListener;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder<>(ItemListLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext())
                , parent
                , false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindView(data.get(position), onBindViewListener);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static class ItemViewHolder<T> extends RecyclerView.ViewHolder {
        ItemListLayoutBinding binding;

        public ItemViewHolder(@NonNull ItemListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(T model, OnBindViewListener<T> listener) {
            listener.onBindView(model, binding);
        }
    }
}
