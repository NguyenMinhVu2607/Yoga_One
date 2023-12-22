package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.animsh.materialsearchbar.MaterialSearchBar;
import com.animsh.materialsearchbar.SimpleOnSearchActionListener;
import com.at17.kma.yogaone.Adapter.ClassAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CourseFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton btnAddClass;
    private List<ClassInfo> classList;
    private ClassAdapter classAdapter;
    private MaterialSearchBar materialSearchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList); // You should adjust the constructor based on your ClassAdapter implementation
        materialSearchBar = view.findViewById(R.id.materialSearchBar);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString().trim());

            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewClassesStudent);
        recyclerView.setAdapter(classAdapter);
        setupRecyclerViewClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDataFromFirebase();
        return view;
    }
    private void filter(String text) {
        List<ClassInfo> filteredList = new ArrayList<>();

        for (ClassInfo classInfo : classList) {
            if (classInfo.getClassName().toLowerCase().contains(text.toLowerCase()) ||
                    classInfo.getTeacherName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(classInfo);
            }
        }

        classAdapter.filterList(filteredList);
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
            intent.putExtra("idClassSTD", classInfo.getDocumentId());
            Log.d("idClassSTD",""+ classInfo.getDocumentId());
            startActivity(intent);


        });

    }
}
