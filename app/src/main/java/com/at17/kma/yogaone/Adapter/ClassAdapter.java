package com.at17.kma.yogaone.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassInfo> classList;
    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView, dayOfWeekTextView, timeTextView, locationTextView, teacherNameTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            teacherNameTextView = itemView.findViewById(R.id.teacherNameTextView);
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

    public ClassAdapter(List<ClassInfo> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassInfo classInfo = classList.get(position);

        holder.classNameTextView.setText(classInfo.getClassName());
        holder.dayOfWeekTextView.setText(TextUtils.join(", ", classInfo.getDayOfWeek()));
        holder.teacherNameTextView.setText(classInfo.getTeacherName());
        holder.locationTextView.setText(classInfo.getLocation());
        holder.timeTextView.setText(classInfo.getTimeStringEnd());

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
    public interface ItemClickListener {
        void onItemClick(ClassInfo classInfo);
    }

}
