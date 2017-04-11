package ids.tp1.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryServer implements Registry_itf {

    private final Map<String, Person> map;

    public RegistryServer() {
        this.map = new HashMap<>();
        map.put("Vincent", new Person("Vincent", "06xxxxxxx"));
        map.put("Christopher", new Person("Christopher", "06xxxxxxx"));
    }

    public void add(Person p) {
        map.put(p.name, p);
    }

    public String getPhone(String name) {
        return map.get(name).phoneNumber;
    }

    public Iterable<Person> getAll() {
        List<Person> serializable = new ArrayList<>();
        serializable.addAll(map.values());
        return serializable;
    }

    public Person search(String name) {
        return map.get(name);
    }
}
