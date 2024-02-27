package com.hunre.dh10c6.yogaone.Fragment_Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hunre.dh10c6.yogaone.CoachFuntion.AddClassActivity;
import com.hunre.dh10c6.yogaone.MainActivity;
import com.hunre.dh10c6.yogaone.R;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
     EditText editTextUsername,editTextEmail,editTextPhone;
    Button buttonSaveChanges;
    ImageView backEditProfile;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 1;

    private StorageReference storageReference;
    CircleImageView Edit_profileImageStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        Edit_profileImageStudent = findViewById(R.id.Edit_profileImageStudent);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        backEditProfile = findViewById(R.id.backEditProfile);
        backEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        Edit_profileImageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        loadUserInfo();
    }
    // Chọn ảnh từ bộ nhớ local
    // Tạo một Intent để mở bộ sưu tập ảnh
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả trả về từ bộ sưu tập ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    // Tải ảnh lên Firebase Storage
    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            // Tạo một tham chiếu đến vị trí mà bạn muốn lưu trữ tệp trong Firebase Storage
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy URL của ảnh
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();
                                    // Lưu imageURL vào Firebase Database
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("avatars");
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    databaseReference.child(userId).setValue(imageURL)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Ảnh đã được lưu vào Firebase Database thành công
                                                    // Bạn có thể thông báo cho người dùng biết rằng ảnh đã được cập nhật
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi có lỗi xảy ra trong quá trình lưu URL của ảnh vào Firebase Database
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi có lỗi xảy ra trong quá trình tải ảnh lên Firebase Storage
                        }
                    });

        }
    }


    private void loadUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        if (user != null) {
            // Load username and email
            editTextUsername.setHint(user.getDisplayName());
            editTextEmail.setHint(user.getEmail());
            Log.d("mail",""+user.getEmail());

            // Set PhoneNumber
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("Users").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String phoneNumber = documentSnapshot.getString("PhoneNumber");
                        // Set giá trị PhoneNumber lên EditText
                        editTextPhone.setHint(phoneNumber);
                        Log.d("phoneNumber", "phoneNumber :"+phoneNumber);
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "Error getting user document", e);
                }
            });
        }
        StorageReference profileImageRef = storageReference.child("images/" + userId + "/profile.jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Thành công, tải ảnh về và hiển thị vào ImageView
                Picasso.get().load(uri).into(Edit_profileImageStudent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi tải ảnh không thành công
                Toast.makeText(getApplicationContext(), "Failed to load profile image.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveChanges() {
        // Lấy nội dung từ EditText
        String newUsername = editTextUsername.getText().toString().trim();
        String newPhoneNumber = editTextPhone.getText().toString().trim();

        // Kiểm tra xem nội dung có tồn tại không
        if (!newUsername.isEmpty() || !newPhoneNumber.isEmpty()) {
            // Cập nhật tên người dùng trên Firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Tạo một UserProfileChangeRequest.Builder mới chỉ nếu có sự thay đổi về tên
                UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();
                if (!newUsername.isEmpty()) {
                    profileUpdatesBuilder.setDisplayName(newUsername);
                }

                // Thực hiện cập nhật tên người dùng nếu có sự thay đổi
                user.updateProfile(profileUpdatesBuilder.build())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditProfileActivity.this, "Đã đổi tên thành công ", Toast.LENGTH_SHORT).show();
                                    Log.d("EditProfileActivity", "Username updated.");
                                } else {
                                    Log.e("EditProfileActivity", "Failed to update username.", task.getException());
                                }
                            }
                        });

                if (!newUsername.isEmpty() || !newPhoneNumber.isEmpty()) {
                    // Cập nhật thông tin người dùng trên Firebase
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("Users").document(userId);

                        // Tạo một HashMap để chứa dữ liệu cần cập nhật
                        Map<String, Object> updates = new HashMap<>();
                        if (!newUsername.isEmpty()) {
                            updates.put("FullName", newUsername);
                        }
                        if (!newPhoneNumber.isEmpty() && newPhoneNumber.length() == 10) {
                            updates.put("PhoneNumber", newPhoneNumber);
                        }

                        // Thực hiện cập nhật dữ liệu vào Firestore
                        userRef.update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditProfileActivity.this, "Đã cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                        Log.d("EditProfileActivity", "Thông tin đã được cập nhật thành công.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("EditProfileActivity", "Lỗi khi cập nhật thông tin.", e);
                                    }
                                });
                    }
                }
            }
        }
    }

}