package com.at17.kma.yogaone.Fragment_Coach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.at17.kma.yogaone.Adapter.DetailFragmentStudentAdapter;
import com.at17.kma.yogaone.Adapter.StudentRequestAdapter;
import com.at17.kma.yogaone.AddressListActivity;
import com.at17.kma.yogaone.MapActivity;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.ModelClassInfo.StudentInfo;
import com.at17.kma.yogaone.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailClassActivityCoach extends AppCompatActivity {

    private TextView textClassName;
    private TextView textDayOfWeek;
    private TextView textLocation;
    private TextView textteacherName;
//    private RecyclerView recyclerViewStudents;
    private StudentRequestAdapter studentAdapter;
ImageButton backDetailCoach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        // Ánh xạ các thành phần giao diện
        ViewPager viewPager = findViewById(R.id.viewPagerStudent);
        TabLayout tabLayout = findViewById(R.id.tabLayoutListStudent);
        textClassName = findViewById(R.id.textClassName);
        textDayOfWeek = findViewById(R.id.textDayOfWeek);
        textLocation = findViewById(R.id.textLocation);
        backDetailCoach = findViewById(R.id.backDetailCoach);
        textteacherName = findViewById(R.id.textteacherName);
       backDetailCoach.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
//        textLocation.setOnClickListener(new View.OnClickListener() {
//            String addresses = textLocation.getText().toString().trim();
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("addresses", addresses);
//                startActivity(intent);
//            }
//        });
        DetailFragmentStudentAdapter detailFragmentStudentAdapter  = new DetailFragmentStudentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(detailFragmentStudentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idClassCoach")) {
            String classId = intent.getStringExtra("idClassCoach");
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

        private void displayClassInfo(ClassInfo classInfo) {
        // Hiển thị thông tin lớp học trên giao diện
        textClassName.setText("Class Name: " + classInfo.getClassName());
        textDayOfWeek.setText("Day of Week: " + TextUtils.join(", ", classInfo.getDayOfWeek()));
        textLocation.setText("Location: " + classInfo.getLocation());
        textteacherName.setText("Name Teacher: " + classInfo.getTeacherName());

        // Thêm mã code để hiển thị các thông tin khác nếu cần
    }
}
