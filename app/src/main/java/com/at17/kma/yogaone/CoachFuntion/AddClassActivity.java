package com.at17.kma.yogaone.CoachFuntion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.at17.kma.yogaone.MainActivity;
import com.at17.kma.yogaone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private CheckBox checkboxMonday,checkboxTuesday,checkboxWednesday,checkboxThursday,checkboxFriday,checkboxSaturday,checkboxSunday;
    private Button addButton;
    private List<CheckBox> dayCheckBoxes;
    private FirebaseAuth auth;
    private  String name,teacherUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        auth =FirebaseAuth.getInstance();
        // Ánh xạ các thành phần từ layout
        classNameEditText = findViewById(R.id.editTextClassName);
        teacherNameEditText = findViewById(R.id.editTextTeacherName);
        locationEditText = findViewById(R.id.editTextLocation);
        startTimePicker = findViewById(R.id.timePickerStartTime);
        endTimePicker = findViewById(R.id.timePickerEndTime);
        startDatePicker = findViewById(R.id.datePickerStartDate);
        endDatePicker = findViewById(R.id.datePickerEndDate);
//        scheduleSpinner = findViewById(R.id.spinnerSchedule);
        addButton = findViewById(R.id.buttonAddClass);
        //


        checkboxMonday = findViewById(R.id.checkboxMonday);
        checkboxTuesday = findViewById(R.id.checkboxTuesday);
        checkboxWednesday = findViewById(R.id.checkboxWednesday);
        checkboxThursday = findViewById(R.id.checkboxThursday);
        checkboxFriday = findViewById(R.id.checkboxFriday);
        checkboxSaturday = findViewById(R.id.checkboxSaturday);
        checkboxSunday = findViewById(R.id.checkboxSunday);

        dayCheckBoxes = new ArrayList<>();
        dayCheckBoxes.add(checkboxMonday);
        dayCheckBoxes.add(checkboxTuesday);
        dayCheckBoxes.add(checkboxWednesday);
        dayCheckBoxes.add(checkboxThursday);
        dayCheckBoxes.add(checkboxFriday);
        dayCheckBoxes.add(checkboxSaturday);
        dayCheckBoxes.add(checkboxSunday);

        // Thiết lập Spinner cho thời gian học trong tuần
//        setupScheduleSpinner();

        // Xử lý sự kiện khi nhấn nút "Thêm lớp học"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( checkInfoClass()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Name, email address, and profile photo Url
                        name = user.getDisplayName();
                        teacherUID = user.getUid();


                    }
                    // Lấy thông tin từ các thành phần nhập liệu
                    String className = classNameEditText.getText().toString();
                    String teacherName = name;
                    String teacherId = teacherUID;
                    String location = locationEditText.getText().toString();
                    int startHour = startTimePicker.getHour();
                    int startMinute = startTimePicker.getMinute();
                    int endHour = endTimePicker.getHour();
                    int endMinute = endTimePicker.getMinute();
//                String selectedDay = scheduleSpinner.getSelectedItem().toString();


                    //Set giờ và phút
                    // Lấy giờ và phút từ TimePicker
                    int hourEnd = endTimePicker.getHour();
                    int minuteEnd = endTimePicker.getMinute();
                    int hourStart = startTimePicker.getHour();
                    int minuteStart = startTimePicker.getMinute();

                    // Chuyển đổi thành chuỗi
                    String timeStringEnd = String.format("%02d:%02d", hourEnd, minuteEnd);
                    String timeStringStart = String.format("%02d:%02d", hourStart, minuteStart);
                    // Lấy thông tin từ DatePicker
                    int startYear = startDatePicker.getYear();
                    int startMonth = startDatePicker.getMonth();
                    int startDay = startDatePicker.getDayOfMonth();
                    int endYear = endDatePicker.getYear();
                    int endMonth = endDatePicker.getMonth();
                    int endDay = endDatePicker.getDayOfMonth();

                    //
                    List<String> selectedDays = getSelectedDays();
                    Log.d("TAG","selectedDays :"+selectedDays) ;
                    // Thêm thông tin lớp học vào Firestore
                    addClassToFirestore(teacherId,className, teacherName, location,timeStringEnd,timeStringStart, startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute, selectedDays);

                }
               }
        });
    }
    public boolean checkInfoClass() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name = user.getDisplayName();
        teacherUID = user.getUid();
        // Lấy thông tin từ các thành phần nhập liệu
        String className = classNameEditText.getText().toString().trim();
        String teacherName = name;
        String teacherId = teacherUID;
        String location = locationEditText.getText().toString().trim();

        if (isEmptyOrNull(className) || isEmptyOrNull(teacherName) || isEmptyOrNull(teacherId) || isEmptyOrNull(location)) {
            Toast.makeText(AddClassActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Lấy giờ và phút từ TimePicker
        int hourEnd = endTimePicker.getHour();
        int minuteEnd = endTimePicker.getMinute();
        int hourStart = startTimePicker.getHour();
        int minuteStart = startTimePicker.getMinute();

        if (hourEnd < hourStart || (hourEnd == hourStart && minuteEnd <= minuteStart)) {
            Toast.makeText(AddClassActivity.this, "Thời gian kết thúc phải sau thời gian bắt đầu", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Lấy thông tin từ DatePicker
        int startYear = startDatePicker.getYear();
        int startMonth = startDatePicker.getMonth();
        int startDay = startDatePicker.getDayOfMonth();
        int endYear = endDatePicker.getYear();
        int endMonth = endDatePicker.getMonth();
        int endDay = endDatePicker.getDayOfMonth();

        if (endYear < startYear || (endYear == startYear && (endMonth < startMonth || (endMonth == startMonth && endDay < startDay)))) {
            Toast.makeText(AddClassActivity.this, "Ngày kết thúc phải sau ngày bắt đầu", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    // Phương thức kiểm tra null hoặc rỗng cho chuỗi
    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }
    private List<String> getSelectedDays() {
        List<String> selectedDays = new ArrayList<>();
        for (CheckBox checkBox : dayCheckBoxes) {
            Log.d("CheckBox", "CheckBox Text: " + checkBox.getText() + ", isChecked: " + checkBox.isChecked());
            if (checkBox.isChecked()) {
                String dayText = checkBox.getText().toString().trim();
                if (!dayText.isEmpty()) {
                    selectedDays.add(dayText);
                }
            }
        }
        return selectedDays;
    }



    private void addClassToFirestore(String teacherId,String className, String teacherName, String location,String timeStringEnd,String timeStringStart,
                                     int startYear, int startMonth, int startDay, int startHour, int startMinute,
                                     int endYear, int endMonth, int endDay, int endHour, int endMinute, List<String>  selectedDays) {
        // Khởi tạo Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một đối tượng Map để lưu thông tin lớp học
        Map<String, Object> classData = new HashMap<>();
        classData.put("className", className);
        classData.put("teacherName", teacherName);
        classData.put("teacherId", teacherId);
        classData.put("location", location);
        classData.put("timeStringEnd", timeStringEnd);
        classData.put("timeStringStart", timeStringStart);
        classData.put("startDay", getDateTimeInMillis(startYear, startMonth, startDay, 0, 0));
        classData.put("endDay", getDateTimeInMillis(endYear, endMonth, endDay, 0, 0));

        classData.put("dayOfWeek", selectedDays);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = (currentUser != null) ? currentUser.getUid() : "";

        // Thêm dữ liệu vào Firestore
        db.collection("classes")
                .add(classData)  // Sử dụng add để Firestore tự tạo ID mới cho tài liệu
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
