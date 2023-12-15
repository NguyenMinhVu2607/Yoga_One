package com.at17.kma.yogaone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.at17.kma.yogaone.Adapter.StudentRequestAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.ModelClassInfo.StudentRequestInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailClassActivityCoach extends AppCompatActivity {

    private TextView textClassName;
    private TextView textDayOfWeek;
    private TextView textLocation;
    private TextView textteacherName;
    private RecyclerView recyclerViewStudents;
    private StudentRequestAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        // Ánh xạ các thành phần giao diện
        textClassName = findViewById(R.id.textClassName);
        textDayOfWeek = findViewById(R.id.textDayOfWeek);
        textLocation = findViewById(R.id.textLocation);
        textteacherName = findViewById(R.id.textteacherName);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
//        displayStudentRequests();
        // Lấy thông tin từ Intent
        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idClass")) {
            String classId = intent.getStringExtra("idClass");
            // Gọi hàm để lấy thông tin từ Firestore
            loadStudentRequests(classId);
            loadClassInfoFromFirestore(classId);
        } else if (intent != null && intent.hasExtra("idClass1")) {
            String classId = intent.getStringExtra("idClass1");
            Log.d("idClass1", "" + classId);
            loadClassInfoFromFirestore(classId);
            loadStudentRequests(classId);
        } else {
            // Xử lý khi không có dữ liệu từ Intent
            Toast.makeText(this, "Không có thông tin lớp học", Toast.LENGTH_SHORT).show();
            finish();
        }

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

    private void loadStudentRequests(String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference classRef = db.collection("UserClasses").document(classId);

        classRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Parse danh sách yêu cầu từ Firestore
                        List<StudentRequestInfo> studentRequests = parseStudentRequests(documentSnapshot);
                        // Hiển thị danh sách yêu cầu trên giao diện
                        displayStudentRequests(studentRequests, classId);
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

    private void displayStudentRequests(List<StudentRequestInfo> studentRequests, String classId) {
        // Hiển thị danh sách yêu cầu trên giao diện sử dụng StudentRequestAdapter
        studentAdapter = new StudentRequestAdapter(studentRequests, new StudentRequestAdapter.OnItemClickListener() {
            @Override
            public void onConfirmClick(int position) {
                // Xử lý khi người dùng xác nhận yêu cầu
//                confirmRequest(studentRequests.get(position), classId);
                confirmAndAddToClass(studentRequests.get(position), classId);
                studentAdapter.removeStudent(position);

            }


            @Override
            public void onCancelClick(int position) {
                cancelRequest(studentRequests.get(position), classId);
                studentAdapter.removeStudent(position);

            }
        });

        recyclerViewStudents.setAdapter(studentAdapter);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<StudentRequestInfo> parseStudentRequests(DocumentSnapshot documentSnapshot) {
        List<StudentRequestInfo> studentRequests = new ArrayList<>();

        // Kiểm tra xem document có chứa trường "students" hay không
        if (documentSnapshot.contains("students")) {
            // Lấy danh sách sinh viên từ trường "students"
            List<Map<String, Object>> studentsList = (List<Map<String, Object>>) documentSnapshot.get("students");

            // Duyệt qua từng sinh viên và thêm vào danh sách yêu cầu
            for (Map<String, Object> studentMap : studentsList) {
                String userId = (String) studentMap.get("id");
                String name = (String) studentMap.get("name");
                String status = (String) studentMap.get("status");

                // Tạo đối tượng StudentRequestInfo và thêm vào danh sách
                StudentRequestInfo studentRequest = new StudentRequestInfo(userId, name, status);
                studentRequests.add(studentRequest);
            }
        }

        return studentRequests;
    }

    // Hàm này để gọi 2 hàm thêm sinh viên vào classes và đổi trạng thái từ Pending thành Confirm
    private void confirmAndAddToClass(StudentRequestInfo studentRequest, String classId) {
        confirmRequest(studentRequest, classId);
        addStudentToClass(studentRequest, classId);
    }
    //Đổi trạng thái trong UserClass
    private void confirmRequest(StudentRequestInfo studentRequest, String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Đường dẫn đến document của lớp học trong UserClasses
        DocumentReference userClassRef = db.collection("UserClasses").document(classId);

        // Lấy danh sách sinh viên từ tài liệu UserClasses
        userClassRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Map<String, Object>> studentsList = (List<Map<String, Object>>) documentSnapshot.get("students");

                // Tìm sinh viên cần cập nhật trong danh sách
                for (Map<String, Object> student : studentsList) {
                    String studentId = (String) student.get("id");
                    if (studentId != null && studentId.equals(studentRequest.getId())) {
                        // Sửa đổi trạng thái của sinh viên cần cập nhật
                        student.put("status", "confirm");
                        break;  // Kết thúc vòng lặp khi đã tìm thấy sinh viên cần cập nhật
                    }
                }

                // Cập nhật lại danh sách sinh viên trong UserClasses
                userClassRef.update("students", studentsList)
                        .addOnSuccessListener(aVoid -> {
                            // Xử lý thành công
                            Toast.makeText(this, "Xác nhận yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi khi cập nhật trạng thái sinh viên trong UserClasses
                            Toast.makeText(this, "Lỗi khi xác nhận yêu cầu", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore", "Error updating status in UserClasses", e);
                        });
            }
        });
    }
    private void cancelRequest(StudentRequestInfo studentRequest, String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Đường dẫn đến document của lớp học trong UserClasses
        DocumentReference userClassRef = db.collection("UserClasses").document(classId);

        // Lấy danh sách sinh viên từ tài liệu UserClasses
        userClassRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Map<String, Object>> studentsList = (List<Map<String, Object>>) documentSnapshot.get("students");

                // Tìm sinh viên cần cập nhật trong danh sách
                for (Map<String, Object> student : studentsList) {
                    String studentId = (String) student.get("id");
                    if (studentId != null && studentId.equals(studentRequest.getId())) {
                        // Sửa đổi trạng thái của sinh viên cần cập nhật
                        student.put("status", "cancel");
                        break;  // Kết thúc vòng lặp khi đã tìm thấy sinh viên cần cập nhật
                    }
                }

                // Cập nhật lại danh sách sinh viên trong UserClasses
                userClassRef.update("students", studentsList)
                        .addOnSuccessListener(aVoid -> {
                            // Xử lý thành công
                            Toast.makeText(this, "Xác nhận yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi khi cập nhật trạng thái sinh viên trong UserClasses
                            Toast.makeText(this, "Lỗi khi xác nhận yêu cầu", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore", "Error updating status in UserClasses", e);
                        });
            }
        });
    }
    private void addStudentToClass(StudentRequestInfo studentRequest, String classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Đường dẫn đến document của lớp học trong Classes
        DocumentReference classRef = db.collection("classes").document(classId);

        // Thêm thông tin sinh viên vào lớp học trong classes
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("id", studentRequest.getId());
        studentData.put("name", studentRequest.getName());

        // Thực hiện cập nhật thông tin sinh viên trong classes
        classRef.update("students", FieldValue.arrayUnion(studentData))
                .addOnSuccessListener(aVoid -> {
                    // Xử lý thành công
                    Toast.makeText(this, "Thêm sinh viên vào lớp học thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi thêm sinh viên vào lớp học trong classes
                    Toast.makeText(this, "Lỗi khi thêm sinh viên vào lớp học", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error updating students array in classes", e);
                });
    }
}
