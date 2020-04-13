package output;

import controller.Controller;
import core.CellCore;
import core.FieldCore;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CellOutput extends Polygon {
    private double x;
    private double y;
    private double side;
    private double pi = Math.PI;
    private Color color;
    private FieldOutput parentField;
    CellCore cellCore;

    //конструктор
    public CellOutput (double x, double y, double side, Color color, FieldOutput field) {
        //задаем параметры клетки
        this.relocate(x, y);
        parentField = field;
        this.side = side;
        this.color = color;
        //создаем core
        cellCore = new CellCore(x, y, side, this);
        //отрисовывем клетку
        this.getPoints().addAll(
                side * Math.cos(pi/ 6) , side * Math.sin(pi / 6),
                0.0, 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6)
        );
        this.setStroke(color);
        this.setFill(Color.rgb(0,0,0, 0));

        //доабвляем оработчик события для щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.buildBuilding(cellCore);
            event.consume();
        });
    }

    //getters
    public CellCore getCore () {return cellCore;}
    public FieldOutput getParentField () {return parentField;}


}
