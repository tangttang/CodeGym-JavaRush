package refactoring.human;

import java.util.ArrayList;
import java.util.List;

public class StudentsDataBase {
    public static List<Student> students = new ArrayList<>();

    public static void addInfoAboutStudent(Student student) {
        students.add(student);
        printInfoAboutStudent(student);

    }

    public static void printInfoAboutStudent(Student student) {
        System.out.println("Name: " + student.getName() + " Age: " + student.getAge());
    }

    public static void removeStudent(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
        }
    }

    public static void findDimaOrSasha() {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getName().equals("Dima")) {
                System.out.println("Student Dima is in the database.");
                break;
            }

            if (students.get(i).getName().equals("Sasha")) {
                System.out.println("Студент Sasha is in the database.");
                break;
            }
        }
    }
}
