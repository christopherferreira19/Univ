package ids.visu;

import ids.World;
import ids.entity.Region;
import ids.entity.Registry;
import ids.msg.UserRegister;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Visu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static final int SIZE = 8;
    private final World world;

    private Stage stage;
    private Scene scene;
    private VisuPane visuPane;
    private VisuTable visuTable;

    public Visu() {
        this.world = new World(SIZE);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.visuPane = new VisuPane(SIZE);
        this.visuTable = new VisuTable();

        for (Region[] regions : world.getRegions()) {
            for (Region region : regions) {
                region.addListener(visuPane.getCanvas());
                region.addListener(visuTable);
            }
        }
        for (Registry registry : world.getRegistries()) {
            registry.addListener(visuPane.getCanvas());
            registry.addListener(visuTable);
        }

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Visu", visuPane));
        tabPane.getTabs().add(new Tab("Table", visuTable));
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setSide(Side.LEFT);

        this.scene = new Scene(tabPane, 800, 600);
        stage.setResizable(true);
        stage.setX(Math.max(0, stage.getX() - (scene.getWidth() - stage.getWidth()) / 2));
        stage.setY(Math.max(0, stage.getY() - (scene.getHeight() - stage.getHeight()) / 2));
        stage.setWidth(scene.getWidth());
        stage.setHeight(scene.getHeight());
        stage.setScene(scene);
        if (!stage.isShowing()) {
            stage.show();
        }
    }
}
