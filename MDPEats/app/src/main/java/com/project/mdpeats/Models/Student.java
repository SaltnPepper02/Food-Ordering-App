package com.project.mdpeats.Models;

public class Student {
    private String Email;
    private String Name;
    private String Password;
    private String StudentID;


    private String SecurityQuestion;



    public Student() {
    }

    public Student(String email, String name, String password, String securityQuestion) {
        Email = email;
        Name = name;
        Password = password;
        SecurityQuestion = securityQuestion;
    }
    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecurityQuestion() {
        return SecurityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        SecurityQuestion = securityQuestion;
    }
}
