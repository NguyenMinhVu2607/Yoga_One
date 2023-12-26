package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.Adapter.ClassAdapter;
import com.at17.kma.yogaone.Adapter.ClassAddedAdapter;
import com.at17.kma.yogaone.Fragment_Student.DetailClassActivityStudent;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.ModelClassInfo.Classroom;
import com.at17.kma.yogaone.ModelClassInfo.User;
import com.at17.kma.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListClassConfirmFragment extends Fragment {
    private FirebaseFirestore fFirestore;
    private String uid;

    private RecyclerView recyclerView;
    private List<ClassInfo> classList;
    private ClassAdapter classAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_class_confirm, container, false);

        fFirestore = FirebaseFirestore.getInstance();
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList);

        recyclerView = view.findViewById(R.id.recyclerviewListClassCF);
        recyclerView.setAdapter(classAdapter);
        setupRecyclerViewClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            loadDataClassAddedFromFirebase();
        }

        return view;
    }

    private void loadDataClassAddedFromFirebase() {
        DocumentReference documentReference = fFirestore.collection("Users").document(uid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> classIds = new ArrayList<>();
                    if (document.contains("ListClassAdded")) {
                        List<Map<String, String>> listClassAdded = (List<Map<String, String>>) document.get("ListClassAdded");
                        for (Map<String, String> classMap : listClassAdded) {
                            if (classMap.containsKey("classId")) {
                                String classId = classMap.get("classId");
                                classIds.add(classId);
                            }
                        }
                    }
                    filterAndDisplayClasses(classIds);
                } else {
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void filterAndDisplayClasses(List<String> classIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("classes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ClassInfo> filteredClassList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ClassInfo classInfo = document.toObject(ClassInfo.class);
                            classInfo.setDocumentId(document.getId());


                            if (classIds.contains(classInfo.getDocumentId())) {
                                filteredClassList.add(classInfo);
                            }
                        }
                        classList.clear();
                        classList.addAll(filteredClassList);
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private void setupRecyclerViewClickListener() {
        classAdapter.setItemClickListener(classInfo -> {
            Intent intent = new Intent(getActivity(), DetailClassActivityStudent.class);
            intent.putExtra("idClassSTDCF", classInfo.getDocumentId());
            Log.d("idClass3", "" + classInfo.getDocumentId());
            startActivity(intent);
        });
    }
}
