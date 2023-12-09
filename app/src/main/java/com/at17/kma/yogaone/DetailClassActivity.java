package com.at17.kma.yogaone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DetailClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);
        Intent intent = getIntent();
        // Kiểm tra xem Intent có chứa "idClass" không
        if (intent.hasExtra("idClass")) {
            // Lấy giá trị "idClass" từ Intent
            String classId = intent.getStringExtra("idClass");
            Toast.makeText(this, "ID : "+classId, Toast.LENGTH_SHORT).show();

            // Bạn có thể sử dụng giá trị classId ở đây để thực hiện các công việc cần thiết
        } else {
            // Nếu Intent không chứa "idClass", xử lý theo ý của bạn, có thể hiển thị thông báo lỗi, v.v.
            Toast.makeText(this, "Không có ID lớp học được truyền", Toast.LENGTH_SHORT).show();
            // Kết thúc Activity hoặc thực hiện hành động khác...
            finish();
        }
    }
}