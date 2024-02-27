package com.hunre.dh10c6.yogaone.Fragment_Coach;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hunre.dh10c6.yogaone.Adapter.DetailFragmentStudentAdapter;
import com.hunre.dh10c6.yogaone.Adapter.StudentRequestAdapter;
import com.hunre.dh10c6.yogaone.ModelClassInfo.ClassInfo;
import com.hunre.dh10c6.yogaone.ModelClassInfo.StudentInfo;
import com.hunre.dh10c6.yogaone.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailClassActivityCoach extends AppCompatActivity {

    private TextView textClassName;
    private TextView textDayOfWeek;
    private TextView textLocation;
    private TextView textteacherName;
    private TextView timeTextViewCoach;
    private ImageView delete, edit;
    //    private RecyclerView recyclerViewStudents;
    private StudentRequestAdapter studentAdapter;
    String classId;

    ImageButton backDetailCoach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        // Ánh xạ các thành phần giao diện
        ViewPager viewPager = findViewById(R.id.viewPagerStudent);
        TabLayout tabLayout = findViewById(R.id.tabLayoutListStudent);
        textClassName = findViewById(R.id.textClassName);
        textDayOfWeek = findViewById(R.id.textDayOfWeekCoach);
        timeTextViewCoach = findViewById(R.id.timeTextViewCoach);
        textLocation = findViewById(R.id.textLocation);
        backDetailCoach = findViewById(R.id.backDetailCoach);
        textteacherName = findViewById(R.id.textteacherName);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailClassActivityCoach.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn xóa lớp học không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi phương thức để xóa lớp học
                        deleteClassFromDatabase(classId);
                        Toast.makeText(DetailClassActivityCoach.this, "Lớp học đã được xóa", Toast.LENGTH_SHORT).show();
                        // Cập nhật giao diện hoặc thực hiện các hành động khác sau khi xóa lớp học thành công
                    }
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì nếu người dùng chọn hủy bỏ
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 Tạo Intent để chuyển đến màn hình chỉnh sửa thông tin lớp học
                Intent intent = new Intent(DetailClassActivityCoach.this, EditClassActivity.class);
//                 Truyền classId sang Activity chỉnh sửa
                intent.putExtra("classId", classId);
                startActivity(intent);
            }
        });

        backDetailCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DetailFragmentStudentAdapter detailFragmentStudentAdapter  = new DetailFragmentStudentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(detailFragmentStudentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idClassCoach")) {
            classId = intent.getStringExtra("idClassCoach");
            loadClassInfoFromFirestore(classId);
            Bundle bundle = new Bundle();
            bundle.putString("classId", classId);

            // Thêm bundle vào Adapter và sau đó nó sẽ được chuyển tới Fragment
            detailFragmentStudentAdapter.setBundle(bundle);
        }
        else {
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
                        if (classInfo != null && classInfo.getStudents() != null) {
                            for (StudentInfo student : classInfo.getStudents()) {
                                if (student != null) {
                                    Log.d("StudentInfo", "Student ID: " + student.getId() + ", Name: " + student.getName());
                                } else {
                                    Log.d("StudentInfo", "Student is null");
                                }
                            }
                        } else {
                            Log.d("StudentInfo", "ClassInfo or Students is null");
                        }

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
    private void deleteClassFromDatabase(String classId) {
        // Thực hiện xóa lớp học từ cơ sở dữ liệu
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("classes").document(classId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xóa lớp học thành công
                        Log.d(TAG, "Lớp học đã được xóa thành công từ cơ sở dữ liệu");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xảy ra lỗi khi xóa lớp học từ cơ sở dữ liệu
                        Log.w(TAG, "Không thể xóa lớp học từ cơ sở dữ liệu", e);
                    }
                });
    }

    private void displayClassInfo(ClassInfo classInfo) {
        // Hiển thị thông tin lớp học trên giao diện
        textClassName.setText("Class Name: " + classInfo.getClassName());
        textDayOfWeek.setText("Day of Week: " + TextUtils.join(", ", classInfo.getDayOfWeek()));
        textLocation.setText("Location: " + classInfo.getLocation());
        timeTextViewCoach.setText("Time : " + classInfo.getTimeStringStart() + " - " +classInfo.getTimeStringEnd());
        textteacherName.setText("Name Teacher: " + classInfo.getTeacherName());

        // Thêm mã code để hiển thị các thông tin khác nếu cần
    }
}
