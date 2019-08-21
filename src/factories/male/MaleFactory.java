package factories.male;

import factories.AbstractFactory;
import factories.Human;

public class MaleFactory implements AbstractFactory {

    public Human getPerson(int age) {
        if (age >= 0 && age <= KidBoy.MAX_AGE) {
            return new KidBoy();
        } else if (age > KidBoy.MAX_AGE && age <= TeenBoy.MAX_AGE) {
            return new TeenBoy();
        }
        return new Man();
    }
}
