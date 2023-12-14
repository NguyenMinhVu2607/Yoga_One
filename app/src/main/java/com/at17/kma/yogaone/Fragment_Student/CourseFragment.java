package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.Adapter.ClassAdapter;
import com.at17.kma.yogaone.CoachFuntion.AddClassActivity;
import com.at17.kma.yogaone.DetailClassActivityCoach;
import com.at17.kma.yogaone.DetailClassActivityStudent;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CourseFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton btnAddClass;
    private List<ClassInfo> classList;
    private ClassAdapter classAdapter;


    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList); // You should adjust the constructor based on your ClassAdapter implementation

        recyclerView = view.findViewById(R.id.recyclerViewClassesStudent);
        recyclerView.setAdapter(classAdapter);
        setupRecyclerViewClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDataFromFirebase();
        return view;
    }

    private void loadDataFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("classes")
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
            Intent intent = new Intent(getActivity(), DetailClassActivityStudent.class);
            intent.putExtra("idClass", classInfo.getDocumentId());
            Log.d("idClass3",""+ classInfo.getDocumentId());

            startActivity(intent);
        });

    }
}
