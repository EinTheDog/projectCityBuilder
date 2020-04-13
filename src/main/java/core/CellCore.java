package core;

import controller.Controller;
import javafx.scene.paint.Color;
import output.BuildingOutput;
import output.CellOutput;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput cellOutput;

    //конструктор
    public CellCore (double x, double y, double side, CellOutput cellOutput) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.cellOutput = cellOutput;
    }

    //метод для добавления здания
    public void buildBuilding () {
        BuildingOutput house = new BuildingOutput(x, y, side, 2 * side, cellOutput.getParentField());
        cellOutput.getParentField().add(house);
    }
}
