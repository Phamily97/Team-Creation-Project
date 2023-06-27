package com.teamb13.teamcreater.module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Student {

    public String id;

    public String startTime;

    public String completionTime;

    public String email;

    public String name;

    public String studentID;

    public HashSet<String> workShops;

    public HashSet<String> preferStudents;

    public HashSet<String> preferProject;

    public HashSet<String> preLanguages;

    public String timeZone;

    public HashSet<String> preferWeekDays;

    public HashSet<String> preferDays;

    public ArrayList<String> rawData;

    public Student() {
        id = "";
        startTime = "";
        completionTime = "";
        email = "";
        name = "";
        studentID = "";
        workShops = new HashSet<>();
        preferStudents = new HashSet<>();
        preferProject = new HashSet<>();
        preLanguages = new HashSet<>();
        timeZone = "";
        preferWeekDays = new HashSet<>();
        preferDays = new HashSet<>();
        rawData = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentID, student.studentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime,
                completionTime, email, name, studentID,
                workShops, preferStudents, preLanguages,
                timeZone, preferWeekDays, rawData);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", studentID='" + studentID + '\'' +
                ", workShops=" + workShops +
                ", preferStudents=" + preferStudents +
                ", preferProject=" + preferProject +
                ", technologies=" + preLanguages +
                ", timeZone='" + timeZone + '\'' +
                ", preferredDay=" + preferWeekDays +
                ", rawData=" + rawData +
                '}';
    }
}
