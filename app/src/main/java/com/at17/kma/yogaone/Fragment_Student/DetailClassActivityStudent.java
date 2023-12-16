package com.at17.kma.yogaone.Fragment_Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DetailClassActivityStudent extends AppCompatActivity {

    private TextView textClassName;
    private TextView textDayOfWeek;
    private TextView textLocation;
    private TextView textteacherName;
   private Button addToClassRequest;
    String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class_student);

        // Ánh xạ các thành phần giao diện
        textClassName = findViewById(R.id.textClassNameStudent);
        textDayOfWeek = findViewById(R.id.textDayOfWeekStudent);
        textLocation = findViewById(R.id.textLocationStudent);
        textteacherName = findViewById(R.id.textteacherNameStudent);
        addToClassRequest = findViewById(R.id.addToClassRequest);

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idClassSTD")) {
             classId = intent.getStringExtra("idClassSTD");
            // Gọi hàm để lấy thông tin từ Firestore
            loadClassInfoFromFirestore(classId);
        }
        else {
            // Xử lý khi không có dữ liệu từ Intent
            Toast.makeText(this, "Không có thông tin lớp học", Toast.LENGTH_SHORT).show();
            finish();
        }
        addToClassRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJoinRequest(classId);
            }
        });
    }

    private void loadClassInfoFromFirestore(String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference classRef = db.collection("classes").document(classId);

        classRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Parse thông tin lớp học từ Firestore
                        ClassInfo classInfo = documentSnapshot.toObject(ClassInfo.class);
                        // Hiển thị thông tin lớp học trên giao diện
                        displayClassInfo(classInfo);
                    } else {
                        // Xử lý khi không có dữ liệu
                        Toast.makeText(this, "Không tìm thấy thông tin lớp học", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi đọc dữ liệu thất bại
                    Toast.makeText(this, "Lỗi khi đọc dữ liệu từ Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error reading class info", e);
                    finish();
                });
    }

    private void displayClassInfo(ClassInfo classInfo) {
        // Hiển thị thông tin lớp học trên giao diện
        textClassName.setText("Class Name: " + classInfo.getClassName());
        textDayOfWeek.setText("Day of Week: " + TextUtils.join(", ", classInfo.getDayOfWeek()));
        textLocation.setText("Location: " + classInfo.getLocation());
        textteacherName.setText("Name Teacher: " + classInfo.getTeacherName());

        // Thêm mã code để hiển thị các thông tin khác nếu cần
    }

    // Thêm
    private void sendJoinRequest(String classId) {
        // Lấy UserID của người đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = (currentUser != null) ? currentUser.getUid() : "";
        String nameStudent = currentUser.getDisplayName();
        // Tạo một đối tượng Map để lưu thông tin của sinh viên
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("name", nameStudent); // Thay thế bằng tên thực của sinh viên
        studentData.put("id", userId);
        studentData.put("status", "pending"); // Trạng thái ban đầu

        // Thêm thông tin của sinh viên vào mảng students của tài liệu lớp học
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference classRef = db.collection("UserClasses").document(classId);

        // Sử dụng set() với merge để tạo mới document nếu chưa tồn tại
        classRef.set(new HashMap<>(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Thêm thông tin của sinh viên vào mảng students
                    classRef.update("students", FieldValue.arrayUnion(studentData))
                            .addOnSuccessListener(aVoid1 -> {
                                // Xử lý thành công
                                Toast.makeText(DetailClassActivityStudent.this, "Yêu cầu đã được gửi", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý lỗi khi thêm thông tin sinh viên vào mảng students
                                Log.w("Firestore", "Error updating students array", e);
                                Toast.makeText(DetailClassActivityStudent.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi tạo mới document lớp học
                    Log.w("Firestore", "Error creating class document", e);
                    Toast.makeText(DetailClassActivityStudent.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                });
    }

    private void createClassAndAddStudent(String classId, Map<String, Object> studentData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserClasses").document(classId)
                .set(studentData)
                .addOnSuccessListener(aVoid -> {
                    // Tạo mới lớp học thành công, thêm thông tin sinh viên vào mảng students
                    Toast.makeText(DetailClassActivityStudent.this, "Yêu cầu đã được gửi", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi tạo mới lớp học
                    Log.w("Firestore", "Error creating class and adding student", e);
                    Toast.makeText(DetailClassActivityStudent.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                });
    }


}
