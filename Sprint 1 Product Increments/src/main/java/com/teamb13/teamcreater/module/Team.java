package com.teamb13.teamcreater.module;

import java.util.ArrayList;
import java.util.Objects;

public class Team {

    private final String name;

    private final ArrayList<Student> students;

    public Team(String name) {
        this.name = name;
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(Objects.requireNonNull(student));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    public int numOfStudents() {
        return students.size();
    }

    @Override
    public String toString() {
        return "Team{" +
                "students=" + students +
                '}';
    }
}
