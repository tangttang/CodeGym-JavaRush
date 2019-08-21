package factories;

import factories.female.FemaleFactory;
import factories.male.MaleFactory;

public class FactoryProducer {

    public enum HumanFactoryType {
        MALE,
        FEMALE
    }

    public static AbstractFactory getFactory(HumanFactoryType type) {
        if (type == HumanFactoryType.MALE) {
            return new MaleFactory();
        } else {
            return new FemaleFactory();
        }
    }
}
