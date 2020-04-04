package logic;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Field extends Pane implements EventHandler<KeyEvent> {

    //обработка нажатия клавиши
    @Override
    public void handle(KeyEvent event) {
        System.out.println(event.getEventType().toString());

        if(event.getSource() == getOnKeyPressed()) {
            keyPressed(event);
        }
        if(event.getSource() == getOnKeyReleased()) {
            keyReleased(event);
        }

        System.out.println(((KeyEvent) event).getCode());
    }

    private void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.W) {
            dx = 1;
            dy = 1;
        }
    }
    private void keyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.W) {
            dx = 0;
            dy = 0;
        }
    }

    Cell[][] cellsArray;
    private int size;
    private double cellSide;
    private double cellHeight;
    private double intend;
    private double dx = 0;
    private double dy = 0;
    Color cellColor = Color.rgb(178, 178, 177 );

    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            move();
        }
    };

    public Field(int size, double x, double y, double cellSide, Color color, double intend) {
        cellsArray = new Cell[size][size];
        this.intend = intend;
        this.size = size;
        this.cellSide = cellSide;
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize( cellSide * size + cellSide * Math.cos(Math.PI / 6), cellHeight * size);
        this.setBackground(new Background(new BackgroundFill(Color.rgb(133, 106, 84  ), null, null)));
        createCells();

        timer = new Timer(true);
        timer.schedule(timerTask, 100);
    }

    private void move () {
        this.setLayoutX(this.getLayoutX() + dx);
        this.setLayoutY(this.getLayoutY() + dy);
    }



    private void createCells () {
        double indentX = intend;
        double indentY = intend;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                Cell cell = new Cell(x, y, cellSide, cellColor,this);
                cell.relocate(x, y);
                cellsArray[j][i] = cell;
                this.getChildren().add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    public Cell findCell (double x1, double y1) {
        double x = x1 - intend;
        double y = y1 - intend;
        int indX = (int) ((x - (1/Math.tan(Math.PI/6)) * y) / cellSide);
        double reqY = 0;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        int indY = 0;
        while (y > reqY) {
            reqY += cellHeight;
            indY++;
        }
        indY--;
        return cellsArray[indX][indY];
    }

    public double getIntend() { return intend;}



}
