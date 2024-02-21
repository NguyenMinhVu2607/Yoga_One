package com.hunre.dh10c6.yogaone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// AddressListActivity.java
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.hunre.dh10c6.yogaone.Adapter.AddressAdapter;

// AddressListActivity.java
// AddressListActivity.java
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {
    ImageButton backAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        // Nhận danh sách địa chỉ từ resources
        String[] addressesArray  = getResources().getStringArray(R.array.location_array);
        List<String> addressesList = Arrays.asList(addressesArray);

        // Hiển thị danh sách địa chỉ trong RecyclerView
        RecyclerView recyclerView = findViewById(R.id.addressRecyclerView);
        backAddress = findViewById(R.id.backAddress);
        AddressAdapter adapter = new AddressAdapter(addressesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Thiết lập sự kiện click cho adapter
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String address) {
                Toast.makeText(AddressListActivity.this, "FFF", Toast.LENGTH_SHORT).show();
                openMapActivity(AddressListActivity.this, address);
            }
        });
        backAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void openMapActivity(Context context, String address) {
        Log.d("AddressListActivity", "Opening MapActivity for address: " + address);
        // Chuyển sang MapActivity và truyền địa chỉ
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }
}
