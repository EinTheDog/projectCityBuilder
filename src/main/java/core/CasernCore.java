package core;

import output.AbstractBuildingOutput;
import output.CasernOutput;

public class CasernCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 128.0;
    private static final double PIC_HEIGHT = 197.0;
    public CasernCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new CasernOutput(this);
        return output;
    }
    @Override
    public CasernCore copy() {
        return new CasernCore(x, y, width, length, scale, field, opacity);
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth()/ picWidth * scale;
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * scale;
    }
}
