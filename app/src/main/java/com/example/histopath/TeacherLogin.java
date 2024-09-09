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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherLogin extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText emailEditTextTeacherLogin, passwordEditTextTeacherLogin;

    TextView studentTextView, signUpTeacherLoginTextView;
    Button signInButtonTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        mAuth= FirebaseAuth.getInstance();

        studentTextView=findViewById(R.id.studentTextView);
        signUpTeacherLoginTextView=findViewById(R.id.signUpTeacherLoginTextView);
        signInButtonTeacher=findViewById(R.id.signInButtonTeacher);
        emailEditTextTeacherLogin=findViewById(R.id.emailEditTextTeacherLogin);
        passwordEditTextTeacherLogin=findViewById(R.id.passwordEditTextTeacherLogin);

        studentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent=new Intent(TeacherLogin.this, StudentLogin.class);
                startActivity(intent);
                finish();
            }
        });

        signUpTeacherLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Intent intent=new Intent(TeacherLogin.this, TeacherSignUp.class);
                startActivity(intent);
            }
        });

        signInButtonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String email=String.valueOf(emailEditTextTeacherLogin.getText());
                String password=String.valueOf(passwordEditTextTeacherLogin.getText());
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(TeacherLogin.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String sanitizedEmail = email.replace(".", ",");
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference studentRef = db.getReference("TeacherDB");

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
                                                        Toast.makeText(TeacherLogin.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(TeacherLogin.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(TeacherLogin.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(TeacherLogin.this, "Failed to check database. Please try again.", Toast.LENGTH_SHORT).show();
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