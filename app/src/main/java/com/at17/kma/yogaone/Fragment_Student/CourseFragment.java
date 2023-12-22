package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.animsh.materialsearchbar.MaterialSearchBar;
import com.at17.kma.yogaone.Adapter.ClassAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore fFirestore;
    private List<ClassInfo> classList;
    private ClassAdapter classAdapter;
    private MaterialSearchBar materialSearchBar;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        classList = new ArrayList<>();
        fFirestore = FirebaseFirestore.getInstance();
        classAdapter = new ClassAdapter(classList);

        materialSearchBar = view.findViewById(R.id.materialSearchBar);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString().trim());
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewClassesStudent);
        recyclerView.setAdapter(classAdapter);
        setupRecyclerViewClickListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            // Load danh sách các lớp đã tham gia và kiểm tra xung đột
            loadDataClassAddedFromFirebase();
        }

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

    private void loadDataClassAddedFromFirebase() {
        DocumentReference documentReference = fFirestore.collection("Users").document(uid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.contains("ListClassAdded")) {
                        List<String> classIds = getClassIdsFromDocument(document);
                        // Gọi hàm loadDataFromFirebase với danh sách classIds để lấy thông tin chi tiết các lớp đã tham gia
                        loadDataFromFirebase(classIds);
                    } else {
                        Log.d("TAG", "Field 'ListClassAdded' does not exist");
                    }
                } else {
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private List<String> getClassIdsFromDocument(DocumentSnapshot document) {
        List<String> classIds = new ArrayList<>();
        List<Map<String, String>> listClassAdded = (List<Map<String, String>>) document.get("ListClassAdded");
        for (Map<String, String> classMap : listClassAdded) {
            if (classMap.containsKey("classId")) {
                String classId = classMap.get("classId");
                classIds.add(classId);
            }
        }
        return classIds;
    }

    private void loadDataFromFirebase(List<String> classIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("classes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ClassInfo> allClasses = getAllClassesFromQuerySnapshot(task.getResult());
                        // Kiểm tra và lọc danh sách lớp có xung đột với lớp đã tham gia
                        List<String> conflictingClassIds = findConflictingClasses(classIds, allClasses);
                        // conflictingClassIds chứa danh sách các lớp trùng lịch
                        Log.d("Conflicting Classes", conflictingClassIds.toString());

                        // Làm mới dữ liệu trong adapter với danh sách mới
                        updateClassAdapterWithNewData(allClasses);
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    private List<ClassInfo> getAllClassesFromQuerySnapshot(QuerySnapshot querySnapshot) {
        List<ClassInfo> allClasses = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot) {
            ClassInfo classInfo = document.toObject(ClassInfo.class);
            classInfo.setDocumentId(document.getId());
            allClasses.add(classInfo);
        }
        return allClasses;
    }

    private void updateClassAdapterWithNewData(List<ClassInfo> allClasses) {
        classAdapter.clear();
        classAdapter.addAll(allClasses);

        // Đặt màu nền cho các lớp trùng lịch
        for (ClassInfo classInfo : allClasses) {
            if (classInfo.isConflict()) {
                // Đặt màu nền cho các lớp trùng lịch (màu hồng, ví dụ)
                // Lưu ý: Bạn có thể thay đổi màu nền tùy thuộc vào yêu cầu của bạn
                classInfo.setBackgroundColor(R.color.conflictColor);

                // Đặt Log để kiểm tra các lớp đã trùng lịch
                Log.d("Conflict", "Class ID: " + classInfo.getDocumentId() +
                        ", Class Name: " + classInfo.getClassName());
            }
        }

        classAdapter.notifyDataSetChanged();
    }


    private List<String> findConflictingClasses(List<String> classIds, List<ClassInfo> allClasses) {
        List<String> conflictingClassIds = new ArrayList<>();
        for (String classId : classIds) {
            // Lấy thông tin chi tiết của lớp đã tham gia
            ClassInfo addedClass = findClassById(classId, allClasses);
            if (addedClass != null) {
                // Kiểm tra xem lớp đã tham gia có trùng lịch với các lớp khác không
                findAndAddConflictingClassIds(addedClass, classId, allClasses, conflictingClassIds);
            }
        }
        return conflictingClassIds;
    }

    private ClassInfo findClassById(String classId, List<ClassInfo> allClasses) {
        for (ClassInfo classInfo : allClasses) {
            if (classInfo.getDocumentId().equals(classId)) {
                return classInfo;
            }
        }
        return null;
    }

    private void findAndAddConflictingClassIds(ClassInfo addedClass, String classId, List<ClassInfo> allClasses, List<String> conflictingClassIds) {
        for (ClassInfo otherClass : allClasses) {
            if (!otherClass.getDocumentId().equals(classId) && haveScheduleConflict(addedClass, otherClass)) {
                conflictingClassIds.add(otherClass.getDocumentId());
            }
        }
    }

    private boolean haveScheduleConflict(ClassInfo class1, ClassInfo class2) {
        long addedClassStartDay = class1.getStartDay();
        long addedClassEndDay = class1.getEndDay();
        if (class2.getStartDay() >= addedClassStartDay && class2.getEndDay() <= addedClassEndDay) {
            for (String day : class2.getDayOfWeek()) {
                if (class1.getDayOfWeek().contains(day)) {
                    if (isTimeConflict(class1, class2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isTimeConflict(ClassInfo class1, ClassInfo class2) {
        String class1StartTime = class1.getTimeStringStart();
        String class1EndTime = class1.getTimeStringEnd();
        String class2StartTime = class2.getTimeStringStart();
        String class2EndTime = class2.getTimeStringEnd();

        LocalTime startTime1 = LocalTime.parse(class1StartTime);
        LocalTime endTime1 = LocalTime.parse(class1EndTime);
        LocalTime startTime2 = LocalTime.parse(class2StartTime);
        LocalTime endTime2 = LocalTime.parse(class2EndTime);

        return (startTime1.isBefore(endTime2) || startTime1.equals(endTime2)) &&
                (endTime1.isAfter(startTime2) || endTime1.equals(startTime2));
    }

    private void setupRecyclerViewClickListener() {
        classAdapter.setItemClickListener(classInfo -> {
            Intent intent = new Intent(getActivity(), DetailClassActivityStudent.class);
            intent.putExtra("idClassSTD", classInfo.getDocumentId());
            Log.d("idClassSTD", "" + classInfo.getDocumentId());
            startActivity(intent);
        });
    }
}
