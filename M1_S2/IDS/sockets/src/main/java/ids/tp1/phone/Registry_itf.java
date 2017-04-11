package ids.tp1.phone;

public interface Registry_itf {

    void add(Person p);
    String getPhone(String name);
    Iterable<Person> getAll();
    Person search(String name);
}
