package logic;


import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Building  extends Polygon {
    private double height;
    private double side;
    private double pi = Math.PI;
    private Field parentField;

    //конструктор
    public Building (double side, double height, Field field) {
        parentField = field;
        this.side = side;
        this.height = height;
        this.getPoints().addAll(
                side * Math.cos(pi/ 6) , height + side * Math.sin(pi / 6),
                0.0, height,
                0.0 , 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6),
                side * (1 + Math.cos(pi/ 6)), height+ side * Math.sin(pi / 6)
        );
        this.setFill(Color.rgb(0,0,0));
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            field.findCell(x, y).buildBuilding();
        });
    }

    public double getHeight() {return height;}
}
