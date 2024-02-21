package com.hunre.dh10c6.yogaone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hunre.dh10c6.yogaone.R;

import java.util.List;

public class ClassAddedAdapter extends RecyclerView.Adapter<ClassAddedAdapter.ViewHolder> {

    private List<String> classIds;

    public ClassAddedAdapter(List<String> classIds) {
        this.classIds = classIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_confirm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String classId = classIds.get(position);
        holder.bind(classId);
    }

    @Override
    public int getItemCount() {
        return classIds != null ? classIds.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView classIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classIdTextView = itemView.findViewById(R.id.classIdTextView);
        }

        public void bind(String classId) {
            classIdTextView.setText(classId);
        }
    }
}
