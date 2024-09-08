package com.example.histopath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class TeacherSignUp extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;

    FirebaseAuth  mAuth;
    EditText nameEditTextTeacher, collegeEditTextTeacher, emailEditTextTeacher, phoneEditTextTeacher, passwordEditTextTeacher, confirmPasswordEditTextTeacher;

    Button signUpButtonTeacher;

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
        setContentView(R.layout.activity_teacher_sign_up);
        mAuth=FirebaseAuth.getInstance();

        nameEditTextTeacher=findViewById(R.id.nameEditTextTeacher);
        collegeEditTextTeacher=findViewById(R.id.collegeEditTextTeacher);
        emailEditTextTeacher=findViewById(R.id.emailEditTextTeacher);
        phoneEditTextTeacher=findViewById(R.id.phoneEditTextTeacher);
        passwordEditTextTeacher=findViewById(R.id.passwordEditTextTeacher);
        confirmPasswordEditTextTeacher=findViewById(R.id.confirmPasswordEditTextTeacher);

        signUpButtonTeacher=findViewById(R.id.signUpButtonTeacher);

        signUpButtonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameEditTextTeacher.getText().toString();
                String college=collegeEditTextTeacher.getText().toString();
                String phone=phoneEditTextTeacher.getText().toString();
                String email=emailEditTextTeacher.getText().toString();
                String password=passwordEditTextTeacher.getText().toString();
                String confirmPassword=confirmPasswordEditTextTeacher.getText().toString();

                if(name.isEmpty()||college.isEmpty()||phone.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty())
                    Toast.makeText(TeacherSignUp.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirmPassword)){
                    Toast.makeText(TeacherSignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(TeacherSignUp.this, "Account created", Toast.LENGTH_SHORT).show();
                                String sanitizedEmail = email.replace(".", ",");
                                TeacherDB teacherDB = new TeacherDB(name, college, phone, email, password);
                                db=FirebaseDatabase.getInstance();
                                reference = db.getReference("TeacherDB");
                                reference.child(sanitizedEmail).setValue(teacherDB);
                                Intent intent = new Intent(TeacherSignUp.this, TeacherLogin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(TeacherSignUp.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
