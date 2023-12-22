package com.at17.kma.yogaone.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassInfo> classList;
    private List<ClassInfo> originalList; // Dùng để sao lưu dữ liệu gốc
    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView, dayOfWeekTextView, timeStartTextView ,timeEndTextView, locationTextView, teacherNameTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            timeStartTextView = itemView.findViewById(R.id.timeStartTextView);
            timeEndTextView = itemView.findViewById(R.id.timeEndTextView);
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
        this.originalList = new ArrayList<>(classList); // Sao lưu dữ liệu gốc
    }
    public void searchClasses(final String keyword) {
        List<ClassInfo> filteredList = new ArrayList<>();
        for (ClassInfo classInfo : originalList) {
            if (classInfo.getClassName().toLowerCase().contains(keyword.toLowerCase()) ||
                    classInfo.getTeacherName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(classInfo);
            }
        }
        filterList(filteredList);
    }

    public void filterList(List<ClassInfo> filteredList) {
        classList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }
    // Trong ClassAdapter
    public int getItemBackgroundColor(int position) {
        ClassInfo classInfo = classList.get(position);
        return classInfo.isConflict() ? R.color.conflictColor : android.R.color.transparent;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassInfo classInfo = classList.get(position);

        holder.classNameTextView.setText(classInfo.getClassName());
        holder.dayOfWeekTextView.setText(TextUtils.join(", ", classInfo.getDayOfWeek()));
        holder.teacherNameTextView.setText(classInfo.getTeacherName());
        holder.timeStartTextView.setText(classInfo.getTimeStringStart());
        holder.timeEndTextView.setText(classInfo.getTimeStringEnd());
        // Lấy thông tin địa điểm từ đối tượng classInfo
        String location = classInfo.getLocation();

        // Tìm vị trí của dấu gạch nối hoặc dấu chấm trong địa điểm
        int separatorIndex = location.indexOf("-");
        if (separatorIndex == -1) {
            separatorIndex = location.indexOf(".");
        }

        // Lấy phần đầu tiên của địa điểm
        String firstPartOfLocation = (separatorIndex != -1) ? location.substring(0, separatorIndex).trim() : location;

        // Đặt giá trị vào TextView
        holder.locationTextView.setText(firstPartOfLocation);
        int backgroundColor = getItemBackgroundColor(position);
        holder.itemView.setBackgroundResource(backgroundColor);
    }
    public void clear() {
        classList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ClassInfo> classes) {
        classList.addAll(classes);
        notifyDataSetChanged();
    }

    public void removeItems(List<String> conflictingClassIds) {
        List<ClassInfo> updatedList = new ArrayList<>();

        for (ClassInfo classInfo : classList) {
            if (!conflictingClassIds.contains(classInfo.getDocumentId())) {
                updatedList.add(classInfo);
            }
        }

        classList = updatedList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return classList.size();
    }
    public interface ItemClickListener {
        void onItemClick(ClassInfo classInfo);
    }

}
