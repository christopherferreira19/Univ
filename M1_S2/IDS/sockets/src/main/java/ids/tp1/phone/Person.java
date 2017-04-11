package ids.tp1.phone;

import java.io.Serializable;

public class Person implements Serializable {
    String name;
    String phoneNumber;

    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumber + ")";
    }
}
