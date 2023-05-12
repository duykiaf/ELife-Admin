package t3h.android.elifeadmin.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import t3h.android.elifeadmin.R;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewHolder> {
    int[] colorsList;

    public ColorsAdapter(int[] colorsArr) {
        colorsList = colorsArr;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_item_layout, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.setBackgroundColor(colorsList[position]);
    }

    @Override
    public int getItemCount() {
        return colorsList.length;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{
        private AppCompatButton colorBtn;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorBtn = itemView.findViewById(R.id.colorBtn);
        }

        public void setBackgroundColor(int color){
            colorBtn.setBackgroundColor(color);
        }
    }
}
