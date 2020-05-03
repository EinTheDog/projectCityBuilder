package core;

import javafx.scene.paint.Color;
import output.CellOutput;

public class CellCore {
    private final double x;
    private final double y;
    private final Color borderColor;
    private final double side;
    private final CellOutput output;
    private final FieldCore field;
    private final double width;
    private final double height;
    private AbstractBuilding building;
    private AbstractBuilding buildingGhost;
    private final int indX;
    private final int indY;

    //конструктор
    public CellCore (double x, double y, double side, double width, double height, Color borderColor, FieldCore field, int i, int j) {
        //задаем значения параметров
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        indX = j;
        indY = i;
        //создаем графическую оболочку
        output = new CellOutput(this);
    }


    //метод для удаления здания
    public void removeGhostForArea() {
        if (buildingGhost != null) {
            for (CellCore neighbour: field.getCellsUnderBuilding(this, buildingGhost)) {
                neighbour.setBuildingGhost(null);
            }
        }
    }

    public void removeBuilding() {
        setBuilding(null);
    }

    //метод для установки здания на все клетки
    public void setBuildingForArea(AbstractBuilding building) {
        for (CellCore neighbour: field.getCellsUnderBuilding(this, building)) {
            neighbour.setBuilding(building);
        }
    }

    //метод для создания призрака здания (на некоторой площади из клеток)
    public void setBuildingGhostForArea(AbstractBuilding buildingGhost) {
        for (CellCore neighbour: field.getCellsUnderBuilding(this, buildingGhost)) {
            neighbour.setBuildingGhost(buildingGhost);
        }
    }

    //проверка свободности соседей
    public boolean neighboursFree(AbstractBuilding building) {
        for (CellCore neighbour: field.getCellsUnderBuilding(this, building)) {
            if (neighbour.getBuilding() != null) return false;
        }
        return true;
    }


    //метод для присвоения клетке уже существующего здания (нужен, чтобы задать здание, занимающее более 1 клетки)
    private void setBuilding (AbstractBuilding building) {
        this.building = building;
    }

    //метод для присвоения клетке уже существующего призрака здания
    public void setBuildingGhost (AbstractBuilding building) {
        this.buildingGhost = building;
    }

    //getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSide() { return side; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getBorderColor() { return borderColor;}
    public FieldCore getField() {
        return field;
    }
    public AbstractBuilding getBuilding () {
        return building;
    }
    public AbstractBuilding getBuildingGhost () {
        return buildingGhost;
    }
    public int getIndX() { return indX; }
    public int getIndY() { return indY; }
    public CellOutput getOutput() {return output;}
}
