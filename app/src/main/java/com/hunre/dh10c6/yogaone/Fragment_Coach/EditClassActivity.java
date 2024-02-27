package com.hunre.dh10c6.yogaone.Fragment_Coach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hunre.dh10c6.yogaone.ModelClassInfo.ClassInfo;
import com.hunre.dh10c6.yogaone.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditClassActivity extends AppCompatActivity {

    private EditText editTextClassName_edit;
    private EditText editTextDayOfWeek;
    private EditText editTextLocation;
    private EditText editTextTeacherName;
    private Button buttonSave;
    CheckBox checkboxMonday, checkboxTuesday,checkboxWednesday,checkboxThursday,checkboxFriday,checkboxSaturday,checkboxSunday;
    private TimePicker timePickerStartTime_edt; // Sửa lại đoạn này
    private TimePicker timePickerEndTime_edt; // Sửa lại đoạn này

    private String classId;
    private List<CheckBox> dayCheckBoxes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        // Ánh xạ các thành phần giao diện
        editTextClassName_edit = findViewById(R.id.editTextClassName_edit);
        timePickerEndTime_edt = findViewById(R.id.timePickerEndTime_edt);
        timePickerStartTime_edt = findViewById(R.id.timePickerStartTime_edt); // Thêm Spinner cho việc chọn địa điểm
        buttonSave = findViewById(R.id.save); // Thêm Spinner cho việc chọn địa điểm



        checkboxMonday = findViewById(R.id.checkboxMonday_edt);
        checkboxTuesday = findViewById(R.id.checkboxTuesday_edt);
        checkboxWednesday = findViewById(R.id.checkboxWednesday_edt);
        checkboxThursday = findViewById(R.id.checkboxThursday_edt);
        checkboxFriday = findViewById(R.id.checkboxFriday_edt);
        checkboxSaturday = findViewById(R.id.checkboxSaturday_edt);
        checkboxSunday = findViewById(R.id.checkboxSunday_edt);

        dayCheckBoxes = new ArrayList<>();
        dayCheckBoxes.add(checkboxMonday);
        dayCheckBoxes.add(checkboxTuesday);
        dayCheckBoxes.add(checkboxWednesday);
        dayCheckBoxes.add(checkboxThursday);
        dayCheckBoxes.add(checkboxFriday);
        dayCheckBoxes.add(checkboxSaturday);
        dayCheckBoxes.add(checkboxSunday);


        // Lấy classId từ Intent
        classId = getIntent().getStringExtra("classId");

        // Load thông tin lớp học hiện tại từ Firebase
        loadClassInfoFromFirebase(classId);

        // Xử lý sự kiện khi nhấn nút Lưu
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClassInfoInFirebase(classId);
            }
        });
    }

    // Phương thức để load thông tin lớp học từ Firebase
    private void loadClassInfoFromFirebase(String classId) {
        // TODO: Thực hiện việc lấy thông tin lớp học từ Firebase và hiển thị lên giao diện
    }

    // Phương thức để cập nhật thông tin lớp học lên Firebase
    private void updateClassInfoInFirebase(String classId) {
        // Kiểm tra thông tin lớp học trước khi cập nhật

        // Tạo đối tượng ClassInfo mới từ các giá trị nhập vào từ giao diện
        ClassInfo updatedClassInfo = new ClassInfo();
        updatedClassInfo.setClassName(editTextClassName_edit.getText().toString().trim());

        // Tạo mảng dayOfWeek từ các checkbox
        ArrayList<String> dayOfWeek = new ArrayList<>();
        for (CheckBox checkBox : dayCheckBoxes) {
            if (checkBox.isChecked()) {
                dayOfWeek.add(checkBox.getText().toString());
            }
        }
        updatedClassInfo.setDayOfWeek(dayOfWeek);

        updatedClassInfo.setLocation("Yoga CS2 - 238B Lê Văn Sỹ, P. 1, Q. Tân Bình, TP. Hồ Chí Minh");

        // Lấy giá trị từ TimePicker
        int hourStart = timePickerStartTime_edt.getHour();
        int minuteStart = timePickerStartTime_edt.getMinute();
        String timeStart = String.format("%02d:%02d", hourStart, minuteStart);
        updatedClassInfo.setTimeStringStart(timeStart);

        int hourEnd = timePickerEndTime_edt.getHour();
        int minuteEnd = timePickerEndTime_edt.getMinute();
        String timeEnd = String.format("%02d:%02d", hourEnd, minuteEnd);
        updatedClassInfo.setTimeStringEnd(timeEnd);

        // Thực hiện cập nhật thông tin lớp học lên Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference classRef = db.collection("classes").document(classId);

        classRef.update("className", updatedClassInfo.getClassName(),
                        "dayOfWeek", updatedClassInfo.getDayOfWeek(),
                        "location", updatedClassInfo.getLocation(),
                        "timeStringStart", updatedClassInfo.getTimeStringStart(),
                        "timeStringEnd", updatedClassInfo.getTimeStringEnd())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật thành công
                        Toast.makeText(EditClassActivity.this, "Cập nhật thông tin lớp học thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xảy ra lỗi khi cập nhật
                        Toast.makeText(EditClassActivity.this, "Lỗi khi cập nhật thông tin lớp học", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
