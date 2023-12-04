package com.at17.kma.yogaone.CoachFuntion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.at17.kma.yogaone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClassActivity extends AppCompatActivity {

    private EditText classNameEditText, teacherNameEditText, locationEditText;
    private TimePicker startTimePicker, endTimePicker;
    private DatePicker startDatePicker, endDatePicker;
    private Spinner scheduleSpinner;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Ánh xạ các thành phần từ layout
        classNameEditText = findViewById(R.id.editTextClassName);
        teacherNameEditText = findViewById(R.id.editTextTeacherName);
        locationEditText = findViewById(R.id.editTextLocation);
        startTimePicker = findViewById(R.id.timePickerStartTime);
        endTimePicker = findViewById(R.id.timePickerEndTime);
        startDatePicker = findViewById(R.id.datePickerStartDate);
        endDatePicker = findViewById(R.id.datePickerEndDate);
        scheduleSpinner = findViewById(R.id.spinnerSchedule);
        addButton = findViewById(R.id.buttonAddClass);

        // Thiết lập Spinner cho thời gian học trong tuần
        setupScheduleSpinner();

        // Xử lý sự kiện khi nhấn nút "Thêm lớp học"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin từ các thành phần nhập liệu
                String className = classNameEditText.getText().toString();
                String teacherName = teacherNameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();
                String selectedDay = scheduleSpinner.getSelectedItem().toString();

                // Lấy thông tin từ DatePicker
                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDay = startDatePicker.getDayOfMonth();
                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDay = endDatePicker.getDayOfMonth();

                // Thêm thông tin lớp học vào Firestore
                addClassToFirestore(className, teacherName, location, startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute, selectedDay);
            }
        });
    }

    private void setupScheduleSpinner() {
        // Tạo danh sách ngày trong tuần
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Thứ 2");
        daysOfWeek.add("Thứ 3");
        daysOfWeek.add("Thứ 4");
        daysOfWeek.add("Thứ 5");
        daysOfWeek.add("Thứ 6");
        daysOfWeek.add("Thứ 7");
        daysOfWeek.add("Chủ Nhật");

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysOfWeek);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Thiết lập ArrayAdapter cho Spinner
        scheduleSpinner.setAdapter(adapter);
    }

    private void addClassToFirestore(String className, String teacherName, String location,
                                     int startYear, int startMonth, int startDay, int startHour, int startMinute,
                                     int endYear, int endMonth, int endDay, int endHour, int endMinute, String selectedDay) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một đối tượng Map để lưu thông tin lớp học
        Map<String, Object> classData = new HashMap<>();
        classData.put("className", className);
        classData.put("teacherName", teacherName);
        classData.put("location", location);
        classData.put("startDay", getDateTimeInMillis(startYear, startMonth, startDay, 0, 0));
        classData.put("endDay", getDateTimeInMillis(endYear, endMonth, endDay, 0, 0));

        classData.put("dayOfWeek", selectedDay);
//        Date date = new Date(getDateTimeInMillis(startYear, startMonth, startDay, 0, 0));
//
//        // Sử dụng SimpleDateFormat để định dạng ngày tháng năm
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//        // Chuyển đối thành chuỗi định dạng và hiển thị trong TextView hoặc nơi khác
//        String formattedDate = sdf.format(date);
//
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        // Thêm dữ liệu vào Firestore
        db.collection("classes")
                .add(classData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddClassActivity.this, "Lớp học đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddClassActivity.this, "Lỗi khi thêm lớp học: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private long getDateTimeInMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0); // Đặt giờ và phút thành 0 để chỉ lưu ngày tháng năm
        return calendar.getTimeInMillis();
    }

}
