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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StudentLogin extends AppCompatActivity {

    FirebaseAuth  mAuth;

    EditText emailLoginPage, passwordLoginPage;
    TextView teacherTextView, signUpStudentLoginTextView;

    Button loginButton;


    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        mAuth= FirebaseAuth.getInstance();


        emailLoginPage=findViewById(R.id.emailLoginPage);
        passwordLoginPage=findViewById(R.id.passwordLoginPage);
        teacherTextView=findViewById(R.id.teacherTextView);
        signUpStudentLoginTextView=findViewById(R.id.signUpStudentLoginTextView);

        loginButton=findViewById(R.id.loginButton);

        teacherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent=new Intent(StudentLogin.this, TeacherLogin.class);
                startActivity(intent);
                finish();
            }
        });

        signUpStudentLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent=new Intent(StudentLogin.this, StudentSignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String email=String.valueOf(emailLoginPage.getText());
                String password=String.valueOf(passwordLoginPage.getText());
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(StudentLogin.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String sanitizedEmail = email.replace(".", ",");
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference studentRef = db.getReference("StudentDB");

                    studentRef.child(sanitizedEmail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DataSnapshot snapshot=task.getResult();
                                if(snapshot.exists())
                                {
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(StudentLogin.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(StudentLogin.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(StudentLogin.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(StudentLogin.this, "Failed to check database. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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