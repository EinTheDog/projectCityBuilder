package core.buildings;

import core.Aura;
import core.FieldCore;
import output.AbstractBuildingOutput;
import output.TavernOutput;

public class TavernCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 128.0;
    private static final double PIC_HEIGHT = 158.0;

    public TavernCore(double x, double y, int width, int length, int size, FieldCore field, double opacity) {
        super(x, y, width, length, size, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new TavernOutput(this);
        return output;
    }

    @Override
    public AbstractBuilding copy() {
        return new TavernCore(x, y, width, length, size, field, opacity);
    }

    @Override
    public int getGoldProfit() {
        return 10;
    }

    @Override
    public int getGoldCost() {
        return 100;
    }

    @Override
    public int getForceProfit() {
        return 0;
    }

    @Override
    public int getPeopleChange() {
        return -5;
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * size;
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth() / PIC_WIDTH * size;
    }

    @Override
    public String getPicPath() {
        return "/textures/tavern.png";
    }

    @Override
    public String getName() {
        return "Tavern";
    }

    @Override
    public Aura getOwnAura() {
        return Aura.TAVERN;
    }
}
