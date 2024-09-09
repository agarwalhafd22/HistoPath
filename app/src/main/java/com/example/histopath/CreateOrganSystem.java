package com.example.histopath;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateOrganSystem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private ImageView showImage, selectImageButton;
    private Button uploadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organ_system);

        // Initialize Firebase storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Initialize UI elements
        showImage = findViewById(R.id.showImage);
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);

        // Set button click listeners
        selectImageButton.setOnClickListener(v -> openFileChooser());
        uploadImageButton.setOnClickListener(v -> uploadImageToFirebase());
    }

    // Open gallery to select an image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Get the result after the user has selected an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            showImage.setImageURI(imageUri);  // Display the selected image in an ImageView
        }
    }

    // Upload the image to Firebase Storage
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Generate a unique name for the image
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            // Upload the file to Firebase Storage
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(CreateOrganSystem.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        // Get the download URL of the uploaded image
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Save imageUrl to Firebase Realtime Database or Firestore if needed
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(CreateOrganSystem.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Get the file extension of the selected image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
