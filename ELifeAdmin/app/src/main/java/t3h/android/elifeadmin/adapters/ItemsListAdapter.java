package t3h.android.elifeadmin.adapters;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import t3h.android.elifeadmin.databinding.ItemListLayoutBinding;
import t3h.android.elifeadmin.listener.OnBindViewListener;
import t3h.android.elifeadmin.listener.OnItemListClickListener;
import t3h.android.elifeadmin.models.Audio;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.models.Topic;

public class ItemsListAdapter<T> extends RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder> {
    private List<T> data, dataSource;
    private OnBindViewListener<T> onBindViewListener;
    private OnItemListClickListener<T> onItemListClickListener;
    private Timer timer;

    public ItemsListAdapter() {
        data = new ArrayList<>();
    }

    public void bindAdapter(OnBindViewListener<T> onBindViewListener) {
        this.onBindViewListener = onBindViewListener;
    }

    public void setData(List<T> data) {
        this.data = data;
        dataSource = data;
        notifyDataSetChanged();
    }

    public void setOnItemListClickListener(OnItemListClickListener<T> listener) {
        this.onItemListClickListener = listener;
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

    public class ItemViewHolder<T> extends RecyclerView.ViewHolder {
        ItemListLayoutBinding binding;

        public ItemViewHolder(@NonNull ItemListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.itemListLayout.setOnClickListener(v -> {
                if (onItemListClickListener != null) {
                    onItemListClickListener.onItemClicked(data.get(getAdapterPosition()));
                }
            });
        }

        public void bindView(T model, OnBindViewListener<T> listener) {
            listener.onBindView(model, binding);
        }
    }

    public void searchList(String keyword, List<T> modelList) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (keyword.trim().isEmpty()) {
                    data = dataSource;
                } else {
                    List<T> temp = new ArrayList<>();
                    for (T item : modelList) {
                        if (item instanceof Category && ((Category) item).getName().toLowerCase().contains(keyword.toLowerCase())) {
                            temp.add(item);
                        } else if (item instanceof Topic && ((Topic) item).getName().toLowerCase().contains(keyword.toLowerCase())) {
                            temp.add(item);
                        } else if (item instanceof Audio && ((Audio) item).getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                            temp.add(item);
                        }
                    }
                    data = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
