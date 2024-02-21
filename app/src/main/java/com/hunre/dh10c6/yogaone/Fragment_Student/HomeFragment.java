package com.hunre.dh10c6.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.KalendarView;
import com.hunre.dh10c6.yogaone.Adapter.ListHomeAdapter;
import com.hunre.dh10c6.yogaone.ModelClassInfo.ClassInfo;
import com.hunre.dh10c6.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    private KalendarView kalendarView;
    private RecyclerView recyclerView;
    private ListHomeAdapter listHomeAdapter;
    private List<ClassInfo> classList;
    private FirebaseFirestore fFirestore;
    private String uid;
    private TextView textviewNameStudents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fFirestore = FirebaseFirestore.getInstance();

        kalendarView = view.findViewById(R.id.kalendarViewStudent);
        textviewNameStudents = view.findViewById(R.id.textviewNameStudents);
        listHomeAdapter = new ListHomeAdapter(new ArrayList<>(), getContext());

        recyclerView = view.findViewById(R.id.recyclerViewClassesStudent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listHomeAdapter = new ListHomeAdapter(classList, getContext());
        recyclerView.setAdapter(listHomeAdapter);

        kalendarView.setInitialSelectedDate(new Date());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String nameStudents = currentUser.getDisplayName();
        textviewNameStudents.setText(nameStudents);

        classList = new ArrayList<>();

        if (currentUser != null) {
            uid = currentUser.getUid();
            loadDataFromFirebase();
        }

        kalendarView.setDateSelector(selectedDate -> {
            Log.d("DateSel", selectedDate.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            displayClassesForSelectedDay(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        });
        // Trong phương thức onCreateView của HomeFragment
        listHomeAdapter.setItemClickListener(classInfo -> {
            if (classInfo != null) {
                Intent intent = new Intent(getActivity(), DetailClassActivityStudent.class);
                intent.putExtra("idClassSTDHome", classInfo.getDocumentId());
                Log.d("idClassSTDHome", "BF " + classInfo.getDocumentId());
                startActivity(intent);
            } else {
                Log.e("HomeFragment", "ClassInfo is null");
            }
        });

        return view;
    }

    private void loadDataFromFirebase() {
        DocumentReference documentReference = fFirestore.collection("Users").document(uid);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    List<String> classIds = new ArrayList<>();
                    if (document.contains("ListClassAdded")) {
                        List<Map<String, String>> listClassAdded = (List<Map<String, String>>) document.get("ListClassAdded");
                        for (Map<String, String> classMap : listClassAdded) {
                            if (classMap.containsKey("classId")) {
                                String classId = classMap.get("classId");
                                classIds.add(classId);
                            }
                        }

                        Log.d("TAG", "Class IDs: " + classIds);
                        queryClassesFromFirestore(classIds);
                    }
                } else {
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void queryClassesFromFirestore(List<String> classIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("classes")
                .whereIn(FieldPath.documentId(), classIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        classList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ClassInfo classInfo = document.toObject(ClassInfo.class);
                            // Lưu documentID vào classInfo
                            classInfo.setDocumentId(document.getId());
                            classList.add(classInfo);
                        }

                        Log.d("Firestore", "Class list size: " + classIds);
                        displayClassesForSelectedDay(
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        );
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }


    // In HomeFragment.java
    private void displayClassesForSelectedDay(int year, int month, int dayOfMonth) {
        listHomeAdapter.clear();

        String selectedDayOfWeek = getDayOfWeek(year, month, dayOfMonth);

        boolean classesFound = false;

        for (ClassInfo classInfo : classList) {
            List<String> classDaysOfWeek = classInfo.getDayOfWeek();

            if (classDaysOfWeek.contains(selectedDayOfWeek)) {
                // ... (your existing code)

                listHomeAdapter.addItem(classInfo); // Use addItem instead of add

                classesFound = true;
            }
        }

        if (!classesFound) {
            Toast.makeText(getContext(), "Không có lớp học nào trong hôm nay", Toast.LENGTH_SHORT).show();
//            listHomeAdapter.addItem(new ClassInfo("Không có lớp học nào cho ngày này.")); // Example, adjust as needed
        }
    }

    private boolean isClassWithinSelectedDay(long startTime, long endTime, int year, int month, int dayOfMonth) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);

        Calendar selectedDayCalendar = Calendar.getInstance();
        selectedDayCalendar.set(year, month, dayOfMonth, 0, 0, 0);

        return selectedDayCalendar.getTimeInMillis() >= startCalendar.getTimeInMillis() &&
                selectedDayCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis();
    }

    private String convertMillisToDate(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return sdf.format(calendar.getTime());
    }

    private String getDayOfWeek(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        String[] daysOfWeek = {"Chủ Nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return daysOfWeek[dayOfWeek - 1];
    }
}
