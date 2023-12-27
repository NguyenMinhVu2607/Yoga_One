package com.at17.kma.yogaone.Fragment_Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.at17.kma.yogaone.Adapter.StudentAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.ModelClassInfo.StudentInfo;
import com.at17.kma.yogaone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailClassActivityStudent extends AppCompatActivity {

    private TextView textClassName;
    CircleImageView openBrowserButton;
    private TextView classStatus,classRecomment;
    private TextView textDayOfWeek;
    private TextView textLocation;
    private TextView timeTextViewStudent;
    private TextView textteacherName;
    private FloatingActionButton addToClassRequest;
    private RecyclerView recyclerViewListStudent;
    private StudentAdapter listStudentAdapter;
    private boolean isActivityResumed;
    String classId;
    ImageButton backDetail;
    ConstraintLayout layout_null;
    LinearLayoutManager main_content;
    LinearLayoutCompat layoutInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class_student);

        // Ánh xạ các thành phần giao diện
        openBrowserButton = findViewById(R.id.openBrowserButton);
        layout_null = findViewById(R.id.layout_null);
//        main_content = findViewById(R.id.main_content);

        textClassName = findViewById(R.id.textClassNameStudent);
        textClassName = findViewById(R.id.textClassNameStudent);
        classStatus = findViewById(R.id.classStatus);
        classRecomment = findViewById(R.id.classRecomment);
        layoutInfo = findViewById(R.id.layoutInfo);
        textDayOfWeek = findViewById(R.id.textDayOfWeekStudent);
        backDetail = findViewById(R.id.backDetail);
        textLocation = findViewById(R.id.textLocationStudent);
        textteacherName = findViewById(R.id.textteacherNameStudent);
        timeTextViewStudent = findViewById(R.id.timeTextViewStudent);
        addToClassRequest = findViewById(R.id.addToClassRequest);
        recyclerViewListStudent = findViewById(R.id.recyclerviewListStudent1);

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        boolean isConflict = getIntent().getBooleanExtra("isConflict", false);

        // Check if the intent has "idClassSTDHome" extra
        if (intent != null && intent.hasExtra("idClassSTDHome")) {
            String idClassSTDHome = intent.getStringExtra("idClassSTDHome");
            addToClassRequest.setVisibility(View.GONE);
            loadClassInfoFromFirestore(idClassSTDHome);
        } else if (intent != null && intent.hasExtra("idClassSTD")) {
            // If "idClassSTDHome" is not present, check for "idClassSTD"
            classId = intent.getStringExtra("idClassSTD");
            // Gọi hàm để lấy thông tin từ Firestore
            loadClassInfoFromFirestore(classId);
        } else if (intent != null && intent.hasExtra("idClassSTDCF")) {
            addToClassRequest.setVisibility(View.GONE);

            classId = intent.getStringExtra("idClassSTDCF");
            // Gọi hàm để lấy thông tin từ Firestore
            loadClassInfoFromFirestore(classId);
        } else {
            // Xử lý khi không có dữ liệu từ Intent
            Toast.makeText(this, "Không có thông tin lớp học", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(!isConflict){
            classStatus.setText("Bạn có thể gửi yêu cầu tham gia lớp học này ❗");
            classRecomment.setText("Click vào nút bên dưới để có thể về đội của nhà Yoga Kma❗");
            classStatus.setTextColor(R.color.cpp_text);
            classRecomment.setTextColor(R.color.cpp_text);


        } else {
            classStatus.setText("Lớp học này đã trùng lịch bạn không thể tham gia lớp học này ❗");
            classRecomment.setText("Vui lòng chọn sang 1 lớp khác để có thể tham gia vào lớp học ❗");


        }
        backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addToClassRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.isEmailVerified()) {
                    Snackbar snackbar = Snackbar
                            .make(layoutInfo, "Tài khoản của bạn chưa được xác thực ! ", Snackbar.LENGTH_LONG)
                            .setAction("Xác thực ngay", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Thông tin xác thực đã được gửi thành công đến Email của bạn !", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Thông tin xác thực chưa được gửi :)", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                    snackbar.show();

                } else {
                    if(!isConflict){
                        sendJoinRequest(classId);
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Lớp học này đã bị trùng lịch ❗", Snackbar.LENGTH_LONG);
                        snackbar.show();;
                    }
                }

            }
        });
        openBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở trình duyệt với liên kết https://actvn.edu.vn/
                String url = "https://actvn.edu.vn/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityResumed = true;
        loadClassInfoIfVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityResumed = false;
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
                        // Hiển thị danh sách sinh viên nếu có
                        displayStudents(classInfo.getStudents());
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
        timeTextViewStudent .setText("Time : " + classInfo.getTimeStringStart() + " - " +classInfo.getTimeStringEnd());

        // Thêm mã code để hiển thị các thông tin khác nếu cần
    }

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
                                // Reload class info and display students after sending join request
                                loadClassInfoFromFirestore(classId);
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
    private boolean isStudentListEmpty(List<StudentInfo> students) {
        return students == null || students.isEmpty();
    }

    private void loadClassInfoIfVisible() {
        if (isActivityResumed) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("idClassSTD")) {
                String classId = intent.getStringExtra("idClassSTD");
                loadClassInfoFromFirestore(classId);
//                Toast.makeText(getApplicationContext(), classId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayStudents(List<StudentInfo> students) {
        // Hiển thị danh sách sinh viên trong RecyclerView
        listStudentAdapter = new StudentAdapter(students);
        recyclerViewListStudent.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListStudent.setAdapter(listStudentAdapter);
        if (isStudentListEmpty(students)) {
            recyclerViewListStudent.setVisibility(View.GONE);
            layout_null.setVisibility(View.VISIBLE);
        }
    }
}