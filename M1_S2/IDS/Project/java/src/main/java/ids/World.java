package ids;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ids.entity.Region;
import ids.entity.Registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class World {

    private final int size;
    private final int levelsCount;

    private final Connection connection;
    private final Region[][] regions;
    private final List<Registry> registries;

    public World(int size) {
        this.size = size;
        this.levelsCount = 31 - Integer.numberOfLeadingZeros(size);

        try {
            ConnectionFactory factory = new ConnectionFactory();
            this.connection = factory.newConnection();
        }
        catch (IOException | TimeoutException exc) {
            throw new RuntimeException(exc);
        }

        this.regions = new Region[size][size];
        this.registries = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Region region = new Region(x, y, levelsCount, connection);
                regions[x][y] = region;
            }
        }

        for (int level = 0, levelSize = 1; level < levelsCount; level++, levelSize <<= 1) {
            for (int x = 0; x < levelSize; x++) {
                for (int y = 0; y < levelSize; y++) {
                    Registry registry = new Registry(x, y, level, levelsCount, connection);
                    registries.add(registry);
                }
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Region[][] getRegions() {
        return regions;
    }

    public List<Registry> getRegistries() {
        return registries;
    }
}
