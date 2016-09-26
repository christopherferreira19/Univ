package sample3d;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Random;

public class Hex3D extends Application {

    private final Rotate cameraRotateX;
    private final Rotate cameraRotateY;
    private final Translate cameraTranslate;

    private double mouseDownX;
    private double mouseDownY;

    public Hex3D() {
        this.cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
        this.cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
        this.cameraTranslate = new Translate(0, 0, -1000);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        cameraRotateX.setAngle(40);
        cameraTranslate.setZ(-1800);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);

        final Group world = new Group();
        PointLight light = new PointLight();
        light.setTranslateZ(-200);
        world.getChildren().add(light);
        Random rand = new Random();
        for (int d = -4; d <= 4; d++) {
            for (int l = -4; l <= 4; l++) {
                if (Math.abs(l + d) > 4) {
                    break;
                }

                Material material = Hexagon.MATERIALS[
                        rand.nextInt(Hexagon.MATERIALS.length)];
                double heightRand = rand.nextDouble();
                int heightMax = heightRand > 0.9
                        ? 2
                        : heightRand > 0.65 ? 1 : 0;
                for (int height = 0; height <= heightMax; height++) {
                    world.getChildren().add(createHexView(material, l, d, height));
                }
            }
        }

        final SubScene subScene = new SubScene(world, 1000, 1000, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        final StackPane stackPane = new StackPane(subScene);
        StackPane.setAlignment(subScene, Pos.CENTER);

        final Scene scene = new Scene(stackPane, 1000, 1000);
        scene.setFill(Color.web("#7C99B9"));
        scene.setOnMousePressed(this::mousePressed);
        scene.setOnMouseDragged(this::mouseDragged);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private Node createHexView(Material material, int l, int d, int level) {
        int size = 50;
        Hexagon hex = new Hexagon(new Point3D(0, 0, 0), size, 30, material);
        hex.setTranslateX(l * 2 * Hexagon.WEIRD_POS * size
                + d * Hexagon.WEIRD_POS * size);
        hex.setTranslateY(d * (size + size / 2));
        hex.setTranslateZ(-level * 30);
        return hex;
    }

    private void mousePressed(MouseEvent event) {
        mouseDownX = event.getSceneX();
        mouseDownY = event.getSceneY();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        double mouseDeltaX = mouseEvent.getSceneX() - mouseDownX;
        double mouseDeltaY = mouseEvent.getSceneY() - mouseDownY;

        if (mouseEvent.isPrimaryButtonDown()) {
            cameraRotateY.setAngle(cameraRotateY.getAngle() + mouseDeltaX);
            cameraRotateX.setAngle(cameraRotateX.getAngle() - mouseDeltaY);
        }
        else {
            cameraTranslate.setZ(cameraTranslate.getZ() + mouseDeltaY * 5);
        }

        mouseDownX = mouseEvent.getSceneX();
        mouseDownY = mouseEvent.getSceneY();
    }
}