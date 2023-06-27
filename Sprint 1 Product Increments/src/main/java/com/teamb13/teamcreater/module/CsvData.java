package com.teamb13.teamcreater.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class CsvData {

    public Header header;

    public ArrayList<Student> students;

    public CsvData(ArrayList<String> header, ArrayList<ArrayList<String>> students) {
        this.header = new Header();
        for (var h : header) {
            this.header.addHeader(h);
        }

        this.students = new ArrayList<>();
        toStudent(students);
    }

    private void toStudent(ArrayList<ArrayList<String>> rows) {
        for (var row : rows) {
            Student student = new Student();

            // Assign each col;
            student.id = row.get(0).trim();
            student.startTime = row.get(1).trim();
            student.completionTime = row.get(2).trim();
            student.email = row.get(3).trim();
            student.name = row.get(4).trim();
            student.studentID = row.get(6).trim();

            student.workShops = getWorkShop(row.get(7));
            student.preferStudents = getPreStu(row.get(8));
            student.preferProject = getPreProject(row.get(9));
            student.preLanguages = getPreLan(row.get(10));
            student.timeZone = row.get(11).trim();
            student.preferWeekDays = getPreWeekDays(row.get(12));
            student.preferDays = getPreDays(row.get(13));
            student.rawData = row;

            this.students.add(student);
        }
    }

    private static HashSet<String> getWorkShop(String str) {
        String[] newStr = str.trim().replace("WRK", "").split("/");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    private static HashSet<String> getPreStu(String str) {
        if (str.equals("NONE")) {
            return new HashSet<>();
        }

        if (str.contains(",")) {
            String[] newStr = str.split(",");
            return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
        }

        String[] newStr = str.split(";");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    private static HashSet<String> getPreProject(String str) {
        String[] newStr = str.split(";");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    private static HashSet<String> getPreWeekDays(String str) {
        String[] newStr = str.split(";");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    private static HashSet<String> getPreLan(String str) {
        String[] newStr = str.split(";");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    private static HashSet<String> getPreDays(String str) {
        String[] newStr = str.split(";");
        return new HashSet<>(Arrays.stream(newStr).map(String::trim).toList());
    }

    @Override
    public String toString() {
        return "CsvData{" +
                "header=" + header +
                ", row=" + students +
                '}';
    }
}
