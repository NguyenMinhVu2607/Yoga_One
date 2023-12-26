package com.at17.kma.yogaone.Fragment_Coach;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.ColoredDate;
import com.ak.KalendarView;
import com.at17.kma.yogaone.Adapter.ListHomeCoachAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeCoachFragment extends Fragment {

    private KalendarView kalendarView;
    private RecyclerView recyclerView;
    private ListHomeCoachAdapter listHomeCoachAdapter;
    private List<ClassInfo> classList;
    private SparseBooleanArray markedDays;
    private String currentUserId;
    private TextView textviewNameCoach;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);
        kalendarView = view.findViewById(R.id.kalendarView);
        recyclerView = view.findViewById(R.id.recyclerViewClassesCoach);
        textviewNameCoach = view.findViewById(R.id.textviewNameCoach);
        kalendarView.setInitialSelectedDate(new Date());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }
        String nameCoach = currentUser.getDisplayName();
        textviewNameCoach.setText(nameCoach);
        classList = new ArrayList<>();
        markedDays = new SparseBooleanArray();

        // Initialize RecyclerView and its adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listHomeCoachAdapter = new ListHomeCoachAdapter(getContext(), classList);
        recyclerView.setAdapter(listHomeCoachAdapter);

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
        queryClassesFromFirestore();
        return view;
    }

    private void queryClassesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("classes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ClassInfo classInfo = document.toObject(ClassInfo.class);
                            if (classInfo.getTeacherId().equals(currentUserId)) {
                                classList.add(classInfo);
                                markDaysWithClasses();
                            }
                        }
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

    private void markDaysWithClasses() {
        List<ColoredDate> datesColors = new ArrayList<>();

        for (ClassInfo classInfo : classList) {
            long startTime = classInfo.getStartDay();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            markedDays.put(dayOfYear, true);
        }

        kalendarView.setColoredDates(datesColors);
    }

    // ... (existing code)

    private void displayClassesForSelectedDay(int year, int month, int dayOfMonth) {
        if (listHomeCoachAdapter != null) {
            String selectedDayOfWeek = getDayOfWeek(year, month, dayOfMonth);

            List<ClassInfo> classesForSelectedDay = new ArrayList<>();

            for (ClassInfo classInfo : classList) {
                List<String> classDaysOfWeek = classInfo.getDayOfWeek();
                if (classDaysOfWeek.contains(selectedDayOfWeek)) {
                    long startTimeMillis = classInfo.getStartDay();
                    long endTimeMillis = classInfo.getEndDay();

                    if (isClassWithinSelectedDay(startTimeMillis, endTimeMillis, year, month, dayOfMonth)) {
                        classesForSelectedDay.add(classInfo);
                    }
                }
            }

            if (classesForSelectedDay.isEmpty()) {
                // If there are no classes for the selected day, clear the list
                listHomeCoachAdapter.clearList();
            } else {
                // If there are classes for the selected day, update the adapter
                listHomeCoachAdapter.setClassList(classesForSelectedDay);
            }
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
