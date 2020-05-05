package core;

import controller.Controller;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import output.FieldOutput;
import render.GameApplication;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FieldCore {
    CellCore[][] cellsArray;
    private double fieldX;
    private double fieldY;
    private double fieldMoveX;
    private double fieldMoveY;
    private final int size; //кол-во клеток на одной стороне игрвого поля
    private final double cellSide;
    private FieldOutput output;
    private final Color cellBorderColor;
    private final Color cellFillColor;
    private final double cellHeight;
    private final double cellWidth;
    private final double fieldSide;
    private final double indent;
    private final Pane parentPane;
    private double width;
    private double height;
    private final List<AbstractBuilding> buildingList;
    private int gold;
    private int force;
    private int people;
    private static final int START_GOLD = 300;
    private static final int START_FORCE = 0;
    private static final int START_PEOPLE = 0;
    //для каждого поля у нас своё положение камеры, а значит у каждого поля должны быть свои параметры scale
    // и скорости перемщения камеры
    private double moveRange;
    private double scaleValue = 1.0;

    public FieldCore (int size, double cellSide, double fieldSide, Color cellColor, Color cellFillColor, Pane parentPane, double indent) {
        //задаем параметры для перемещения поля
        fieldMoveX = 0;
        fieldMoveY = 0;
        this.indent = indent;
        //задаем параметры размера и клеток
        this.size = size;
        this.fieldSide = fieldSide;
        this.cellSide = cellSide;
        this.cellBorderColor = cellColor;
        this.cellFillColor = cellFillColor;
        //задаем игроввые параметры
        gold = START_GOLD;
        force = START_FORCE;
        people = START_PEOPLE;
        //задаем панель, нак которой находимся и тип клетки
        this.parentPane = parentPane;
        //раситываем параметры
        cellHeight = 2 * cellSide * Math.sin(Math.PI / 6);
        cellWidth = 2 * cellSide * Math.cos(Math.PI / 6);
        moveRange = cellSide / Controller.moveSpeedDenom;
        this.width = 2 * fieldSide * Math.cos(Math.PI / 6);
        this.height = 2 * fieldSide * Math.sin(Math.PI / 6);
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + indent - GameApplication.mainWindowWidth / 2) * scaleValue + GameApplication.mainWindowWidth / 2;
        fieldY = (fieldMoveY + indent - GameApplication.mainWindowHeight / 2) * scaleValue + GameApplication.mainWindowHeight / 2;
        //создаем список для хранения построенных зданий
        buildingList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];
    }

    //метод для приближения камеры
    public void zoom (double scrollValue) {
        //увеличивая значение scale создаем эфект приближения камеры
        if (scaleValue + scrollValue / Controller.BASE_SCROLL > 0 && scaleValue + scrollValue / Controller.BASE_SCROLL < 20)
            scaleValue += scrollValue / Controller.BASE_SCROLL;
        output.zoom(scaleValue);
        //вычисляем новую ширину и высоту
        width = GameApplication.paneWidth * scaleValue;
        height = GameApplication.paneHeight * scaleValue;
        //перемещаем поле таким образом, чтобы поле не перемещалось относительно камеры при масштабировании
        move(0, 0);
        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = cellSide * scaleValue / Controller.moveSpeedDenom;
    }

    //метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldMoveX += dx;
        fieldMoveY += dy;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + indent - GameApplication.mainWindowWidth / 2) * scaleValue + GameApplication.mainWindowWidth / 2;
        fieldY = (fieldMoveY + indent - GameApplication.mainWindowHeight / 2) * scaleValue + GameApplication.mainWindowHeight / 2;
        output.move(fieldX, fieldY);
    }

    // метод заполнения поля клетками
    public void createCells() {
        //вспомогательная переменные
        double cellIndentX = cellWidth / 2;
        double cellIndentY = fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellWidth / 2 + cellIndentX;
                double y = cellIndentY - j * cellHeight / 2;
                //создаем клетку
                CellCore cell = new CellCore(x, y, cellSide, cellWidth, cellHeight, cellBorderColor, cellFillColor, this, i, j);
                cell.draw();
                cellsArray[j][i] = cell; //добавляем ее в массив
            }
            cellIndentX += (cellWidth / 2);
            cellIndentY += (cellHeight / 2);
        }
    }

    //метод для нахождения клетки по координатам (используется в Controller.moveCursor)
    public CellCore findCell(double x, double y) {
        //изменяем координаты курсора так, чтобы его положение правильно вопронималось относительно клеток при масштабировании поля
        double cursorX = (x - GameApplication.mainWindowWidth / 2) / scaleValue + GameApplication.mainWindowWidth / 2 - indent;
        double cursorY = (y - GameApplication.mainWindowHeight / 2) / scaleValue + GameApplication.mainWindowHeight / 2 - indent;
        //ищем клетку
        int j = -1;
        cursorX -= cellWidth / 2;
        cursorY -= fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        while (cursorY <= Math.tan(Math.PI / 6) * cursorX - j * cellHeight) { j++; }
        j--;
        int i = -1;
        while (cursorY >= - Math.tan(Math.PI / 6) * cursorX + i * cellHeight) { i++; }
        if (i >= 0 && j >= 0 && i < size && j < size) return cellsArray[j][i];
        else return null;
    }



    //метод для получения клеток того же здания
    public List<CellCore> getCellsUnderBuilding(CellCore cell, AbstractBuilding building) {
        List<CellCore> neighbours = new ArrayList<>();
        int cellX = cell.getIndX();
        int cellY = cell.getIndY();
        for (int i = cellY + 1 - building.getSize() * building.getLength(); i <= cellY; i ++) {
            for(int j = cellX; j <= cellX - 1 + building.getSize() * building.getWidth(); j ++) {
                if (i >= 0 && j >= 0 && i < size && j < size ) {
                    neighbours.add(cellsArray[j][i]);
                }
            }
        }
        return neighbours;
    }

    //метод для добавления нового здания
    //вспомогательный метод для определения какое здание на каком плане находится
    private int getVerticalShift(AbstractBuilding building) {
        CellCore cell = building.getCells().get(0);
        int x = cell.getIndX();
        int y = cell.getIndY();
        return y - x;
    }
    public void addBuilding(AbstractBuilding newBuilding) {
        buildingList.add(newBuilding);
        buildingList.sort(Comparator.comparingInt(this::getVerticalShift));
        int k = buildingList.indexOf(newBuilding);
        for (int i = k; i < buildingList.size(); i++) {
            buildingList.get(i).draw();
        }
    }

    //метод для удаления здания
    public void removeBuilding(AbstractBuilding building) {
        buildingList.remove(building);
    }

    //метод для получения золота со зданий
    public void gainResources () {
        int forceIncome = 0;
        for (AbstractBuilding building: buildingList) {
            gold += building.getGoldProfit();
            forceIncome += building.getForceProfit();
        }
        force = force + forceIncome > 0? force + forceIncome: 0;
        GameApplication.updateResources(gold, force, people);
    }

    //метод для покупки здания
    public void buyBuilding (AbstractBuilding building) {
        gold -= building.getGoldCost();
        people += building.getPeopleChange();
        GameApplication.updateResources(gold, force, people);
    }

    //метод для создания графической оболочки
    public void draw () {
        output = new FieldOutput(this);
        move(0, 0);
    }

    public void pay (int cost) {
        gold = gold - cost > 0? gold - cost: 0;
        GameApplication.updateResources(gold, force, people);
    }

    //getters
    public FieldOutput getOutput() { return output;}
    public double getMoveRange() {return moveRange;}
    public double getX() {return fieldMoveX;}
    public double getY() {return  fieldMoveY;}
    public Pane getParentPane() {return  parentPane;}
    public double getIndent() {return indent;}
    public double getWidth() {return width;}
    public double getScale() {return scaleValue;}
    public double getHeight() {return height;}
    public double getCellWidth() {return cellWidth;}
    public double getCellHeight() {return cellHeight;}
    public double getCellScale() {return cellsArray[0][0].getOutput().getScaleX();}
    public List<AbstractBuilding> getBuildingsList() { return buildingList;}

    public int getGold() {return gold;}
    public int getForce() {return force;}
    public int getPeople() {return people;}
}
