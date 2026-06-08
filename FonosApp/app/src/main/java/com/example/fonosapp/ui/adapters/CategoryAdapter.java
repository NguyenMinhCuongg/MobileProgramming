package com.example.fonosapp.ui.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fonosapp.R;
import com.example.fonosapp.data.models.Category;
import com.example.fonosapp.utils.ViewExtensions;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvName.setText(category.getNameVi());
        
        if (category.getGradientColors() != null && category.getGradientColors().size() >= 2) {
            try {
                int color1 = Color.parseColor(category.getGradientColors().get(0));
                int color2 = Color.parseColor(category.getGradientColors().get(1));
                
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {color1, color2});
                gd.setCornerRadius(30f);
                holder.vBackground.setBackground(gd);
                holder.tvName.setTextColor(Color.WHITE);
            } catch (Exception e) {
                holder.vBackground.setBackgroundColor(Color.LTGRAY);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            ViewExtensions.pulse(v);
            v.postDelayed(() -> listener.onCategoryClick(category), 200);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public View vBackground;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            vBackground = itemView.findViewById(R.id.vBackground);
        }
    }
}
