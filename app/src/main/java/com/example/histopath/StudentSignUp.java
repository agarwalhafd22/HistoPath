package com.example.histopath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSignUp extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;

    FirebaseAuth  mAuth;
    EditText nameEditText, collegeEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;

    Button signUpButton;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
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
        setContentView(R.layout.activity_student_sign_up);
        mAuth=FirebaseAuth.getInstance();

        nameEditText=findViewById(R.id.nameEditText);
        collegeEditText=findViewById(R.id.collegeEditText);
        emailEditText=findViewById(R.id.emailEditText);
        phoneEditText=findViewById(R.id.phoneEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmPasswordEditText);

        signUpButton=findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String name=nameEditText.getText().toString();
                String college=collegeEditText.getText().toString();
                String phone=phoneEditText.getText().toString();
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                String confirmPassword=confirmPasswordEditText.getText().toString();

                if(name.isEmpty()||college.isEmpty()||phone.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty())
                    Toast.makeText(StudentSignUp.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirmPassword)){
                    Toast.makeText(StudentSignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(StudentSignUp.this, "Account created", Toast.LENGTH_SHORT).show();
                                String sanitizedEmail = email.replace(".", ",");
                                StudentDB studentDB = new StudentDB(name, college, phone, email, password);
                                db=FirebaseDatabase.getInstance();
                                reference = db.getReference("StudentDB");
                                reference.child(sanitizedEmail).setValue(studentDB);
                                Intent intent = new Intent(StudentSignUp.this, StudentLogin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(StudentSignUp.this, "Authentication failed", Toast.LENGTH_SHORT).show();
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
