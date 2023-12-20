package com.at17.kma.yogaone.Fragment_Coach;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.Adapter.StudentAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.ModelClassInfo.StudentInfo;
import com.at17.kma.yogaone.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListStudentClassFragment extends Fragment {

    RecyclerView recyclerViewListStudent;
    private StudentAdapter listStudentAdapter;
    private boolean isFragmentResumed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_student_class, container, false);
        recyclerViewListStudent = view.findViewById(R.id.recyclerviewListStudent);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String classId = bundle.getString("classId");
            loadClassInfoFromFirestore(classId);
            Toast.makeText(getContext(), classId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentResumed = true;
        loadClassInfoIfVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentResumed = false;
    }
    private void loadClassInfoFromFirestore(String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference classRef = db.collection("classes").document(classId);

        classRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ClassInfo classInfo = documentSnapshot.toObject(ClassInfo.class);
                        displayStudents(classInfo.getStudents());
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy thông tin lớp học", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error reading class info", e);
                });
    }  
    private void loadClassInfoIfVisible() {
        if (isFragmentResumed && isVisible()) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String classId = bundle.getString("classId");
                loadClassInfoFromFirestore(classId);
                Toast.makeText(getContext(), classId, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void displayStudents(List<StudentInfo> students) {
        // Hiển thị danh sách sinh viên trong RecyclerView
        listStudentAdapter = new StudentAdapter(students);
        recyclerViewListStudent.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewListStudent.setAdapter(listStudentAdapter);
    }
}
