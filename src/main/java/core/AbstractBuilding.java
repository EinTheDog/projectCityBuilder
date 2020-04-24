package core;

import output.AbstractBuildingOutput;

public abstract class AbstractBuilding {
    protected double x;
    protected double y;
    protected FieldCore field;
    protected double picWidth;
    protected double picHeight;
    protected int scale; // множитель, определяющий сколько клеток в одном измерении занимает здание
    protected int width;
    protected int length;
    protected double opacity;
    protected int cellArea;

    //конструктор
    public AbstractBuilding (double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        //задаем параметры
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.scale = scale;
        this.field = field;
        this.opacity = opacity;
        cellArea = width * length * scale * scale;
        picWidth = getPicWidth();
        picHeight = getPicHeight();
        //вычисляем параметры

        //отрисовываем здание
        field.getOutput().add(this.getOutput());
        this.getOutput().relocate(x - picWidth / 2, y - picHeight) ;
    }

    //перерисовываем здание (нужно чтобы перисовывать поверх новго здания старые, находящие по перспективе ближе к игроку)
    public void redraw() {
        if (getOutput() != null) field.getOutput().add(getOutput());
    }

    public void delete() {
        if (getOutput() != null) field.getOutput().getChildren().remove(getOutput());
    }

    public void setOpacity(double opacity) { getOutput().setOpacity(opacity); }

    public void move(double x, double y) {
        this.x = x;
        this.y = y;
        this.getOutput().relocate(x - picWidth / 2, y - picHeight);
    }

    //метод обязательного создания графической оболчки
    abstract protected AbstractBuildingOutput getOutput();

    public  abstract AbstractBuilding copy();

    //getters
    public double getX() {return x;}
    public double getY() {return y;}
    public int getWidth() {return width;}
    public int getLength() {return length;}
    abstract public double getPicWidth();
    abstract public double getPicHeight();
    public int getScale() {return scale;}
    public FieldCore getParentField() {return field;}
    public double getOpacity() {return opacity;}
    public int getCellArea() {return cellArea;}
}