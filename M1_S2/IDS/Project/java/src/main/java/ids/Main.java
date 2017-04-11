package ids;

import ids.entity.Region;
import ids.entity.Registry;
import ids.listener.LoggingListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length < 1) {
            System.out.println("Missing size argument !");
            System.exit(1);
        }

        int size = Integer.parseInt(args[0]);
        World world = new World(size);
        LoggingListener loggingListener = new LoggingListener();
        for (Region[] regions : world.getRegions()) {
            for (Region region : regions) {
                region.addListener(loggingListener);
            }
        }
        for (Registry registry : world.getRegistries()) {
            registry.addListener(loggingListener);
        }

        System.out.println("... Ready !");
        loggingListener.header();
    }
}
