package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class StudentLogin extends AppCompatActivity { // this class is for users to login to their student account
    EditText edtStudentID, edtStudentPassword, edtForgotStudentID, edtForgotSecurityQuestion;
    Button btnStudentSignIn, btnStudentRegister, btnForgotPass;

    FirebaseDatabase database;
    DatabaseReference table_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        // Initialize variable
        edtStudentID = (EditText)findViewById(R.id.StudentID);
        edtStudentPassword = (EditText)findViewById(R.id.StudentPassword);
        btnStudentSignIn = (Button) findViewById(R.id.btnStudentLogin);
        btnStudentRegister = (Button) findViewById(R.id.btnStudentRegister);
        btnForgotPass = (Button) findViewById(R.id.btnStudentForgotPass);

        //Initiate Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Student");

        // this button signs in student
        btnStudentSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isConnectedToInternet(getBaseContext())) {

                    // Dialog
                    ProgressDialog pDialog = new ProgressDialog(StudentLogin.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //check if student exist in database
                            try {
                                if (snapshot.child(edtStudentID.getText().toString()).exists()) {
                                    //get Student info here
                                    pDialog.dismiss();
                                    Student student = snapshot.child(edtStudentID.getText().toString()).getValue(Student.class);
                                    student.setStudentID(edtStudentID.getText().toString());// set studentID
                                    if (student.getPassword().equals(edtStudentPassword.getText().toString())) {
                                        Intent studentHomeIntent = new Intent(StudentLogin.this, StudentHome.class);
                                        Common.currentStudent = student;
                                        startActivity(studentHomeIntent);
                                        finish();
                                        //Toast.makeText(StudentLogin.this, "Sign In success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(StudentLogin.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(StudentLogin.this, "Student doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(StudentLogin.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        // goes to register student
        btnStudentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StudentRegis = new Intent(StudentLogin.this, StudentRegis.class);
                startActivity(StudentRegis);
            }
        });

        // goes to forgot password
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSecurityPassDialog();
            }
        });

    }

    private void startSecurityPassDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(StudentLogin.this);
        alert.setTitle("Forgot Password");
        alert.setMessage("Please fill StudentID and Answer Security Question");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_pass_layout = inflater.inflate(R.layout.forgot_pass_checker, null);

        alert.setView(forgot_pass_layout);
        alert.setIcon(R.drawable.baseline_lock_24);

        edtForgotStudentID = forgot_pass_layout.findViewById(R.id.edtStudentID);
        edtForgotSecurityQuestion = forgot_pass_layout.findViewById(R.id.edtSecurityQuestion);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Student student = snapshot.child(edtStudentID.getText().toString())
                                .getValue(Student.class);

                        if(student.getSecurityQuestion().equals(edtForgotSecurityQuestion.getText().toString())){
                            Intent studentForgotPass = new Intent(StudentLogin.this, StudentForgotPass.class);
                            studentForgotPass.putExtra("StudentID", edtStudentID.getText().toString());
                            System.out.println(edtStudentID.getText().toString());
                            startActivity(studentForgotPass);
                        }
                        else {
                            Toast.makeText(StudentLogin.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}
