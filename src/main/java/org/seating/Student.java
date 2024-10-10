package org.seating;

public class Student {
    private final String rollNumber;
    private String name;
    private String studentClass;

    public Student(String rollNumber, String name, String studentClass) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.studentClass = studentClass;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getStudentClass() {
        return studentClass;
    }
}
