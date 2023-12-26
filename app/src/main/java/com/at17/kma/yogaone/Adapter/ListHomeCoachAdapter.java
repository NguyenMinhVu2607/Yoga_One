package com.at17.kma.yogaone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;

import java.util.List;

public class ListHomeCoachAdapter extends RecyclerView.Adapter<ListHomeCoachAdapter.ClassViewHolder> {

    private List<ClassInfo> classList;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener; // Interface for click events

    public ListHomeCoachAdapter(Context context, List<ClassInfo> classList) {
        this.inflater = LayoutInflater.from(context);
        this.classList = classList;
    }

    // Interface for click events
    public interface ItemClickListener {
        void onItemClick(ClassInfo classInfo);
    }

    // Set the click listener from outside
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void clearList() {
        classList.clear();
        notifyDataSetChanged();
    }

    // Method to add a single item
    public void addItem(ClassInfo classInfo) {
        classList.add(classInfo);
        notifyItemInserted(classList.size() - 1);
    }
    public void addToList(ClassInfo classInfo) {
        classList.add(classInfo);
        notifyDataSetChanged();
    }
    public void setClassList(List<ClassInfo> classList) {
        this.classList = classList;
        notifyDataSetChanged();
    }
    // Method to add a list of items
    public void addAll(List<ClassInfo> items) {
        int oldSize = classList.size();
        classList.addAll(items);
        notifyItemRangeInserted(oldSize, items.size());
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassInfo classInfo = classList.get(position);

        // Bind data to your ViewHolder views
        holder.classNameTextView.setText(classInfo.getClassName());
        holder.teacherNameTextView.setText(classInfo.getTeacherName());
        // Add other views as needed

        // Implement click listener
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(classInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;
        TextView teacherNameTextView;
        // Add other views as needed

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.textViewClassName);
            teacherNameTextView = itemView.findViewById(R.id.textViewTeacherName);
            // Initialize other views
        }
    }
}
