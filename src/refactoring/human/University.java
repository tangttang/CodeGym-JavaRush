package refactoring.human;

import java.util.ArrayList;
import java.util.List;

public class University {
    private String name;
    private int age;
    private List<Student> students = new ArrayList<>();


    public University(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Student getStudentWithAverageGrade(double averageGrade) {
        Student right = null;
        for (Student student : students) {
            if (student.getAverageGrade() == averageGrade) {
                right = student;
            }
        }
        return right;
    }

    public Student getStudentWithMaxAverageGrade() {
        double max = 0;
        Student right = null;
        for (Student student : students) {
            if (student.getAverageGrade() > max) {
                max = student.getAverageGrade();
                right = student;
            }
        }
        return right;
    }

    public Student getStudentWithMinAverageGrade() {
        Student maxGrade = getStudentWithMaxAverageGrade();
        double max = maxGrade.getAverageGrade();
        Student right = null;
        for (Student student : students) {
            if (student.getAverageGrade() < max) {
                max = student.getAverageGrade();
                right = student;
            }
        }
        return right;
    }

    public void expel(Student student) {
        students.remove(student);
    }
}
