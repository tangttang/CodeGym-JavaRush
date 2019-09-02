package refactoring.user;

import java.util.concurrent.atomic.AtomicInteger;

public class UserHelper {
    private User userAnya = new User("Anna", "Smirnova", 10);
    private User userRoma = new User("Roma", "Vinogradov", 30);

    public void printUsers() {
        userAnya.printInfo();
        userAnya.printAdditionalInfo();

        userRoma.printInfo();
        userRoma.printAdditionalInfo();
    }

    public int calculateAverageAge() {
        User userUra = new User("Yuri", "Karp", 28);
        return (userAnya.getAge() + userRoma.getAge() + userUra.getAge()) / 3;
    }

    public int calculateRate(AtomicInteger base, int age, boolean hasWork, boolean hasHouse) {
        return (int) ((base.get() + age / 100) * (hasWork ? 1.1 : 0.9) * (hasHouse ? 1.1 : 0.9));
    }

    public String getBossName(User user) {
        return user.getBoss();
    }
}
