package factories.female;

import factories.AbstractFactory;
import factories.Human;

public class FemaleFactory implements AbstractFactory {

    public Human getPerson(int age) {
        if (age >= 0 && age <= KidGirl.MAX_AGE) {
            return new KidGirl();
        } else if (age > KidGirl.MAX_AGE && age <= TeenGirl.MAX_AGE) {
            return new TeenGirl();
        }
        return new Woman();
    }
}
