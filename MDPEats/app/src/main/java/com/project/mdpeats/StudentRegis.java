package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Models.Student;

public class StudentRegis extends AppCompatActivity {

    EditText edtStudentID, edtStudentEmail, edtStudentName, edtStudentPassword, edtSecurityQuestion;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_regis);

        edtStudentID = (EditText) findViewById(R.id.CStudentID);
        edtStudentEmail = (EditText) findViewById(R.id.CStudentEmailAddress);
        edtStudentName = (EditText) findViewById(R.id.CStudentName);
        edtStudentPassword = (EditText) findViewById(R.id.CStudentPassword);
        edtSecurityQuestion = (EditText) findViewById(R.id.CStudentQuestion);

        btnSignup = (Button) findViewById(R.id.btnStudentSignUp);

        //Initiate Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("Student");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    ProgressDialog pDialog = new ProgressDialog(StudentRegis.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(edtStudentID.getText().toString()).exists()) {
                                pDialog.dismiss();
                                Toast.makeText(StudentRegis.this, "Account Already Exist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                pDialog.dismiss();
                                String studentEmail = edtStudentEmail.getText().toString();
                                if (studentEmail.contains("@nottingham.edu.my") && edtStudentID.getText().toString().matches("\\d{8}")) { // check for valid email
                                    // Valid email format
                                    Student student = new Student(studentEmail,
                                            edtStudentName.getText().toString(),
                                            edtStudentPassword.getText().toString(),
                                            edtSecurityQuestion.getText().toString());
                                    table_user.child(edtStudentID.getText().toString()).setValue(student);
                                    Toast.makeText(StudentRegis.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Invalid email format
                                    Toast.makeText(StudentRegis.this, "Invalid Email Format or Student ID", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(StudentRegis.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


        });

    }
}