package com.example.histopath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;

public class StudentLogin extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailLoginPage, passwordLoginPage;
    TextView teacherTextView, signUpStudentLoginTextView;
    Button loginButton;
    ProgressBar progressBar;
    ImageView imageView9;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();
            if (currentUserEmail != null) {
                String formattedEmail = currentUserEmail.replace(".", ","); // Replace '.' with ',' for Firebase
                showProgressBar();
                imageView9.setVisibility(View.VISIBLE);
                checkUserType(formattedEmail);
            }
        }
    }

    private void checkUserType(String email) {
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("StudentDB");
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("TeacherDB");

        studentRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //hideProgressBar();
                    //imageView9.setVisibility(View.INVISIBLE);
                    Intent studentIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(studentIntent);
                    finish();
                    hideProgressBar();
                } else {
                    checkInTeacherDB(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
                imageView9.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkInTeacherDB(String email) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("TeacherDB");

        teacherRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //hideProgressBar();
                //imageView9.setVisibility(View.INVISIBLE);
                if (snapshot.exists()) {
                    Intent teacherIntent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(teacherIntent);
                    finish();
                    hideProgressBar();
                } else {
                    Toast.makeText(getApplicationContext(), "User not found in Student or Teacher database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
                imageView9.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        mAuth = FirebaseAuth.getInstance();

        emailLoginPage = findViewById(R.id.emailLoginPage);
        passwordLoginPage = findViewById(R.id.passwordLoginPage);
        teacherTextView = findViewById(R.id.teacherTextView);
        signUpStudentLoginTextView = findViewById(R.id.signUpStudentLoginTextView);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.spin_kit);
        imageView9=findViewById(R.id.imageView9);
        imageView9.setVisibility(View.INVISIBLE);

        // Initialize ProgressBar sprite
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        //showProgressBar();

        teacherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent = new Intent(StudentLogin.this, TeacherLogin.class);
                startActivity(intent);
                finish();
            }
        });

        signUpStudentLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent = new Intent(StudentLogin.this, StudentSignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String email = String.valueOf(emailLoginPage.getText());
                String password = String.valueOf(passwordLoginPage.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(StudentLogin.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        hideProgressBar();
                                        Toast.makeText(StudentLogin.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        hideProgressBar();
                                        Toast.makeText(StudentLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void showProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
