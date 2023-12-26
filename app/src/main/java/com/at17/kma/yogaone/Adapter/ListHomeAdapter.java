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

import java.util.ArrayList;
import java.util.List;

public class ListHomeAdapter extends RecyclerView.Adapter<ListHomeAdapter.ListHomeViewHolder> {

    private List<ClassInfo> classList;
    private Context context;

    public ListHomeAdapter(List<ClassInfo> classList, Context context) {
        this.classList = classList != null ? classList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ListHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHomeViewHolder holder, int position) {
        ClassInfo classInfo = classList.get(position);
        holder.textViewClassName.setText(classInfo.getClassName());
        holder.textViewTeacherName.setText(classInfo.getTeacherName());
        holder.tvtimeStringStart.setText(classInfo.getTimeStringStart());
        holder.tvtimeStringEnd.setText(classInfo.getTimeStringEnd());

    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0;
    }
    // Method to add a single item
    public void addItem(ClassInfo classInfo) {
        classList.add(classInfo);
        notifyDataSetChanged();
    }

    // Method to clear all items
    public void clear() {
        classList.clear();
        notifyDataSetChanged();
    }

    private ClassAdapter.ItemClickListener itemClickListener;
    public void setItemClickListener(ClassAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(ClassInfo classInfo);
    }
    public class ListHomeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClassName;
        TextView textViewTeacherName;
        TextView tvtimeStringStart;
        TextView tvtimeStringEnd;

        public ListHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClassName = itemView.findViewById(R.id.textViewClassName);
            textViewTeacherName = itemView.findViewById(R.id.textViewTeacherName);
            tvtimeStringStart = itemView.findViewById(R.id.tvtimeStringStart);
            tvtimeStringEnd = itemView.findViewById(R.id.tvtimeStringEnd);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(classList.get(position));
                    }
                }
            });
        }
    }
}
