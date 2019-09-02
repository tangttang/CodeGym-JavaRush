package refactoring.human;

import java.util.Date;

public class Student extends UniversityPerson {
    private double averageGrade;
    private Date beginningOfSession;
    private Date endOfSession;
    private int course;

    public Student(String name, int age, double averageGrade) {
        super(name, age);
        this.averageGrade = averageGrade;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getCourse() {
        return this.course;
    }

    public void live() {
        learn();
    }

    @Override
    public String getPosition() {
        return "Student";
    }

    public void learn() {
    }

    public void incAverageGrade(double delta) {
        setAverageGrade(delta + getAverageGrade());
    }

    public void setCourse(double value) {
        course = (int) value;
    }


    public void setAverageGrade(double value) {
        averageGrade = value;
    }

    public void setBeginningOfSession(Date date) {
        beginningOfSession = date;
    }

    public void setEndOfSession(Date date) {
        endOfSession = date;
    }

    public double getAverageGrade() {
        return averageGrade;
    }
}