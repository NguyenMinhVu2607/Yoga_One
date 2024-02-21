package com.hunre.dh10c6.yogaone.Fragment_Coach;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hunre.dh10c6.yogaone.Adapter.ClassAdapter;
import com.hunre.dh10c6.yogaone.CoachFuntion.AddClassActivity;
import com.hunre.dh10c6.yogaone.ModelClassInfo.ClassInfo;
import com.hunre.dh10c6.yogaone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

// ManageCourseFragment.java
// ... (imports)

public class ManageCourseFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddClass;
    private List<ClassInfo> classList;
    private ClassAdapter classAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_course, container, false);
        btnAddClass = view.findViewById(R.id.btnAddClass);
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList);

        recyclerView = view.findViewById(R.id.recyclerViewClasses);
        recyclerView.setAdapter(classAdapter);
        setupRecyclerViewClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddClassActivity.class));
            }
        });
        loadDataFromFirebase();
        return view;
    }

    private void loadDataFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid(); // Lấy ID của người đăng nhập

        db.collection("classes")
                .whereEqualTo("teacherId", currentUserId) // Chỉ lấy các lớp học của giáo viên đang đăng nhập
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        classList.clear(); // Clear existing data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ClassInfo classInfo = document.toObject(ClassInfo.class);
                            classInfo.setDocumentId(document.getId()); // Set Document ID
                            classList.add(classInfo);
                        }
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void setupRecyclerViewClickListener() {
        classAdapter.setItemClickListener(classInfo -> {
            Intent intent = new Intent(getActivity(), DetailClassActivityCoach.class);
            intent.putExtra("idClassCoach", classInfo.getDocumentId());
            startActivity(intent);
//
//            Bundle bundle = new Bundle();
//            bundle.putString("idClass", classInfo.getDocumentId());
//            RequestStudentFragment fragment = new RequestStudentFragment();
//            fragment.setArguments(bundle);
        });
    }
}
