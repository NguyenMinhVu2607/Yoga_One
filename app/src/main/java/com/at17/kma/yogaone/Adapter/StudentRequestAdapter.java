package com.at17.kma.yogaone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.ModelClassInfo.StudentRequestInfo;
import com.at17.kma.yogaone.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class StudentRequestAdapter extends RecyclerView.Adapter<StudentRequestAdapter.ViewHolder> {

    private List<StudentRequestInfo> studentList;
    private OnItemClickListener itemClickListener;

    // Interface để xử lý sự kiện khi người dùng click vào các icon
    public interface OnItemClickListener {
        void onConfirmClick(int position);
        void onCancelClick(int position);
    }

    public StudentRequestAdapter(List<StudentRequestInfo> studentList, OnItemClickListener itemClickListener) {
        this.studentList = studentList;
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textStudentName;
        private ImageView iconConfirm;
        private ImageView iconCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            iconConfirm = itemView.findViewById(R.id.iconConfirm);
            iconCancel = itemView.findViewById(R.id.iconCancel);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentRequestInfo studentInfo = studentList.get(position);
        holder.textStudentName.setText(studentInfo.getName());

        // Thêm sự kiện xác nhận và hủy bỏ yêu cầu
        holder.iconConfirm.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onConfirmClick(position);
            }
        });

        holder.iconCancel.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onCancelClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // Hàm này để cập nhật dữ liệu mới cho adapter
    public void updateData(List<StudentRequestInfo> newStudentList) {
        studentList.clear();
        studentList.addAll(newStudentList);
        notifyDataSetChanged();
    }
}
