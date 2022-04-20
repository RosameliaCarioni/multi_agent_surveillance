package group.seven.model.environment;

import group.seven.enums.TileType;
import group.seven.logic.geometric.XY;
import group.seven.model.agents.Agent;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

import static group.seven.enums.TileType.EMPTY;

public class Tile {
    //Type
    TileType type;
    XY xy; //or Point2D, or int x, y

    //Graph
    //Tile[] adjacent; // added Adjacent record --> can we delete this & old setAdjacent method? (Mischa)
    Adjacent adjacent;

    public Tile() {
        type = EMPTY;
    }

    public Tile(int x, int y) {
        this();
        xy = new XY(x, y);
    }

    public Tile(TileType type, int x, int y) {
        this(x, y);
        this.type = type;
    }

    public int getX() {
        return xy.x();
    }
    public int getY() {
        return xy.y();
    }

    //Actionable
    // void doAction() {}
    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    //public void setAdjacent(Tile[] adjacent) { this.adjacent = adjacent;}
    public void setAdjacent(Tile north, Tile east, Tile south, Tile west, Tile target) {this.adjacent = new Adjacent(north,east,south,west,target);}

    //Exploration Status, also not sure about this
    //boolean explored for guard and agent for calculating coverage
    private final ObservableBooleanValue exploredGuard = new SimpleBooleanProperty(false);
    private final ObservableBooleanValue exploredAgent = new SimpleBooleanProperty(false);
    public ObservableBooleanValue exploredGuardProperty() {
        return exploredGuard;
    }
    public ObservableBooleanValue exploredAgentProperty() {
        return exploredAgent;
    }
    public boolean getExploredGuard() {
        return exploredGuard.get();
    }
    public boolean getExploredAgent() {
        return exploredAgent.get();
    }


}
