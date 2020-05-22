package view;

import core.Aura;
import core.CellCore;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class CellView extends Polygon{
    private final Color BASE_COLOR = Color.rgb(10, 106, 84);
    private final Color TAVERN_AURA_COLOR = Color.rgb(250,210,0, 0.3);
    private final Color CASTLE_AURA_COLOR = Color.rgb(200,144,255, 0.3);
    Color borderColor = Color.rgb(220, 220, 220);
    public BooleanProperty isChosen = new SimpleBooleanProperty();
    public BooleanProperty isClicked = new SimpleBooleanProperty();
    private final CellCore cellCore;

    public CellView(double width, double height, CellCore cellCore) {
        this.cellCore = cellCore;
        this.getPoints().addAll(
                0.0 , 0.0,
                - width / 2, - height / 2,
                0.0 , - height,
                width / 2, - height / 2
        );
        //отрисовывем клетку
        setStroke(borderColor);
        setFill(BASE_COLOR);

        isChosen.setValue(false);
        isClicked.setValue(false);
    }

    public void setAuraColor(Aura aura) {
        switch (aura) {
            case TAVERN: setFill(TAVERN_AURA_COLOR); break;
            case CASTLE: setFill(CASTLE_AURA_COLOR); break;
        }
    }

    public void clearAuraColor() {
        this.setFill(BASE_COLOR);
    }

    public CellCore getCore() {
        return  cellCore;
    }
}
