package sample3d;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.Arrays;

public class Hexagon extends Group {

    public static final double WEIRD_POS = Math.cos(Math.PI / 180 * (double) 30);

    private static final Material BORDER_MATERIAL = new PhongMaterial(Color.rgb(20, 20, 20));

    public static final Material VOLCANO = new PhongMaterial(Color.RED);
    public static final Material PLAIN = new PhongMaterial(Color.GREEN);
    public static final Material JUNGLE = new PhongMaterial(Color.DARKGREEN);
    public static final Material LAKE = new PhongMaterial(Color.BLUE);
    public static final Material MOUNTAIN = new PhongMaterial(Color.GRAY);

    public static final Material[] MATERIALS = new Material[] {
            VOLCANO,
            PLAIN,
            JUNGLE,
            LAKE,
            MOUNTAIN
    };


    public Hexagon(Point3D center, int radius, int height, Material material) {
        TriangleMesh shapeMesh = new TriangleMesh();
        shapeMesh.getTexCoords().addAll(0, 0);

        Point3D centerTop = center.subtract(0, 0, height);
        shapeMesh.getPoints().addAll(
                (float) centerTop.getX(),
                (float) centerTop.getY(),
                (float) centerTop.getZ() + 8f);

        float x0 = 0;
        float y1 = (float) radius / 2;
        float x1 = (float) WEIRD_POS * radius;
        float y2 = radius;
        float zt = -30;
        float zb = 0;

        shapeMesh.getPoints().addAll(
                x0, x0, zt,

                x1, y1, zt,
                x0, y2, zt,
                -x1, y1, zt,

                -x1, -y1, zt,
                x0, -y2, zt,
                x1, -y1, zt,

                x0, x0, zb,

                x1, y1, zb,
                x0, y2, zb,
                -x1, y1, zb,

                -x1, -y1, zb,
                x0, -y2, zb,
                x1, -y1, zb);

        shapeMesh.getFaces().addAll(
                0, 0, 2, 0, 1, 0,
                0, 0, 3, 0, 2, 0,
                0, 0, 4, 0, 3, 0,
                0, 0, 5, 0, 4, 0,
                0, 0, 6, 0, 5, 0,
                0, 0, 1, 0, 6, 0,

                7, 0, 8, 0, 9, 0,
                7, 0, 9, 0, 10, 0,
                7, 0, 10, 0, 11, 0,
                7, 0, 11, 0, 12, 0,
                7, 0, 12, 0, 13, 0,
                7, 0, 13, 0, 8, 0,

                1, 0, 2, 0, 8, 0,
                9, 0, 8, 0, 2, 0,

                2, 0, 3, 0, 9, 0,
                10, 0, 9, 0, 3, 0,

                3, 0, 4, 0, 10, 0,
                11, 0, 10, 0, 4, 0,

                4, 0, 5, 0, 11, 0,
                12, 0, 11, 0, 5, 0,

                5, 0, 6, 0, 12, 0,
                13, 0, 12, 0, 6, 0,

                6, 0, 1, 0, 13, 0,
                8, 0, 13, 0, 1, 0);

        MeshView shapeView = new MeshView(shapeMesh);
        shapeView.setCullFace(CullFace.BACK);
        shapeView.setMaterial(material);

        getChildren().addAll(shapeView);
    }

    private static Point3D hexCorner(Point3D center, double size, int i) {
        double angleDeg = 60 * i + 30;
        double angleRad = Math.PI / 180 * angleDeg;
        return new Point3D(
                center.getX() + size * Math.cos(angleRad),
                center.getY() + size * Math.sin(angleRad),
                center.getZ());
    }
}
