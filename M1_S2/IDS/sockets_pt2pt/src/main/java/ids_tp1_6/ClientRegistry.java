package ids_tp1_6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRegistry {

    private final Map<String, Client> map;

    public ClientRegistry() {
        this.map = new HashMap<>();
    }

    public void add(Client c, String name) {
        map.put(name, c);
    }

    public Client getClient(String name) {
        return map.get(name);
    }

    public List<String> getAll() {
        List<String> result = new ArrayList<>();
        result.addAll(map.keySet());
        return result;
    }
}
