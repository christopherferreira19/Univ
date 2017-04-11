package ids.tp1.phone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryClient implements Registry_itf {

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public RegistryClient(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void add(Person p) {
        try {
            out.writeByte((byte) Opcode.ADD.ordinal());
            out.writeObject(p);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPhone(String name) {
        try {
            out.writeByte((byte) Opcode.GET_PHONE.ordinal());
            out.writeObject(name);
            return (String) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Person> getAll() {
        try {
            out.writeByte((byte) Opcode.GET_ALL.ordinal());
            out.flush();
            @SuppressWarnings("unchecked")
            Iterable<Person> result = (Iterable<Person>) in.readObject();
            return result;
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Person search(String name) {
        try {
            out.writeByte((byte) Opcode.SEARCH.ordinal());
            out.writeObject(name);
            return (Person) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
