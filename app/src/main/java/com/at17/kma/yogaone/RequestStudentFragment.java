package com.at17.kma.yogaone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.at17.kma.yogaone.Adapter.StudentRequestAdapter;
import com.at17.kma.yogaone.ModelClassInfo.StudentRequestInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestStudentFragment extends Fragment {

    private RecyclerView recyclerViewStudents;

    private StudentRequestAdapter studentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_studnet, container, false);
        recyclerViewStudents = view.findViewById(R.id.recyclerViewStudents);
        // Nhận Bundle từ Activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Lấy giá trị từ Bundle
            String classId = bundle.getString("classId");
            Log.d("classIdRQ","classId :"+classId);
            Toast.makeText(getContext(), classId, Toast.LENGTH_SHORT).show();
            loadStudentRequests(classId);
            } else {
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }

        return view;

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
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(getContext()));
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
                        Toast.makeText(getContext(), "Không tìm thấy thông tin lớp học", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi đọc dữ liệu thất bại
                    Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error reading class info", e);
//                    finish();
                });
    }
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
                            Toast.makeText(getContext(), "Xác nhận yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi khi cập nhật trạng thái sinh viên trong UserClasses
                            Toast.makeText(getContext(), "Lỗi khi xác nhận yêu cầu", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "Xác nhận yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi khi cập nhật trạng thái sinh viên trong UserClasses
                            Toast.makeText(getContext(), "Lỗi khi xác nhận yêu cầu", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Thêm sinh viên vào lớp học thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi thêm sinh viên vào lớp học trong classes
                    Toast.makeText(getContext(), "Lỗi khi thêm sinh viên vào lớp học", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error updating students array in classes", e);
                });
    }

}