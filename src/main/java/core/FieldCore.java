package core;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import output.CellOutput;
import output.FieldOutput;
import render.Main;


public class FieldCore {
    CellCore[][] cellsArray;
    private double fieldX;
    private double fieldY;
    private double size = 0; //кол-во клеток на одной стороне игрвого поля
    private double cellSide;
    private double moveRange;
    private final double moveSpeedDenom = 8.0; //постоянная, отвечающая за скорость перемещения камеры (делитель)
    private double scaleValue = 1.0;
    private FieldOutput output;
    private Color cellColor;
    private double cellHeight;
    private double cellWidth;
    private double fieldSide;
    private double indent;
    private Pane parentPane;
    private double width;
    private double height;

    public FieldCore (int size, double cellSide, double fieldSide, Color cellColor, Pane parentPane, double indent) {
        //задаем параметры поля
        this.parentPane = parentPane;
        fieldX = indent;
        fieldY = indent;
        this.indent = indent;
        this.size = size;
        this.cellSide = cellSide;
        this.fieldSide = fieldSide;
        this.parentPane = parentPane;
        cellHeight = 2 * cellSide * Math.sin(Math.PI / 6);
        cellWidth = 2 * cellSide * Math.cos(Math.PI / 6);
        this.cellColor = cellColor;
        moveRange = cellSide / moveSpeedDenom;
        this.width = 2 * fieldSide * Math.cos(Math.PI / 6);
        this.height = 2 * fieldSide * Math.sin(Math.PI / 6);
        output = new FieldOutput(this);

        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];
        //создаем клетки
        createCells();
    }

    public void zoom (double scrollValue) {
        scaleValue += scrollValue / 100;
        output.zoom(scaleValue);
        moveRange = cellSide * scaleValue / moveSpeedDenom;
    }

    // метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldX += dx;
        fieldY += dy;
        output.move(fieldX, fieldY, Math.abs(dx), Math.abs(dy));
    }

    // метод заполнения поля клетками
    private void createCells() {
        // переменные, которые отвечают за расположение клеток на поле
        //вспомогательная переменные
        double cellIndentX = cellWidth / 2;
        double cellIndentY = fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellWidth / 2 + cellIndentX;
                double y = cellIndentY - j * cellHeight / 2;
                //создаем клетку
                CellCore cell = new CellCore(x, y, cellSide, cellWidth, cellHeight, cellColor, this.getOutput());
                cellsArray[j][i] = cell; //добавляем ее в массив
                output.add(cell.getOutput()); // отрисовывем ее
            }
            cellIndentX += (cellWidth / 2);
            cellIndentY += (cellHeight / 2);
        }
    }

    //метод для нахождения клетки по координатам (используется в Controller.clickOnBuilding)
    public CellCore findCell(double x, double y) {
        int indX = (int) Math.floor(x / (cellWidth / 2));
        int indY = (int) Math.floor((y - (cellHeight / 2) * Math.abs((size - indX))) / (cellHeight / 2));
        if (indX >= 0 && indY >= 0 && indX < size && indY < size) return cellsArray[indX][indY];
        else return null;
    }

    public FieldOutput getOutput() { return output;}

    public double getMoveRange() {return moveRange;}
    public double getX() {return fieldX;}
    public double getY() {return  fieldY;}
    public Pane getParentPane() {return  parentPane;}
    public double getIndent() {return indent;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}
}
