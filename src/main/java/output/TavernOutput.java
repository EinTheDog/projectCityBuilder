package output;

import core.AbstractBuilding;

public class TavernOutput extends AbstractBuildingOutput {
    public TavernOutput(AbstractBuilding core) { super(core); }

    @Override
    protected String getImgPath() { return "/textures/tavern.png"; }
}