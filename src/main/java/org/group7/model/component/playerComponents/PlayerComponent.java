package org.group7.model.component.playerComponents;

import org.group7.agentVision.BasicVision;
import org.group7.enums.Actions;
import org.group7.enums.AlgorithmEnum;
import org.group7.enums.Orientation;
import org.group7.geometric.Area;
import org.group7.geometric.Point;
import org.group7.geometric.Vector2D;
import org.group7.model.Grid;
import org.group7.model.Scenario;
import org.group7.model.algorithms.AStar;
import org.group7.model.algorithms.ActionTuple;
import org.group7.model.algorithms.Algorithm;
import org.group7.model.component.Component;
import org.group7.utils.Config;

import java.util.ArrayList;
import java.util.List;

import static org.group7.enums.AlgorithmEnum.A_STAR;

/**
 * This class is made as a super class for all the possible component that can be considered player, i.e. agents and intruders.
 * it contains the coordinates of the position of the agent.
 */
public abstract class PlayerComponent extends Component {
    public Vector2D direction;
    public Vector2D viewField;
    private final Point initialPosition;
    private double directionAngle; //same as direction as double representation in radians
    private double viewFieldLength;
    private double viewFieldAngle; //how wide the visual range is
    public BasicVision simpleRay;
    private Area movingSound;
    private AlgorithmEnum algorithmValue = A_STAR;
    private Algorithm algorithm;
    private boolean hasTeleported = false;
    private List<Grid> agentsCurrentVision = new ArrayList<>();
    private final int id;
    private static int counter = 0;
    private Orientation orientation;

    private double baseSpeed;
    private double smellingDistance;


    public PlayerComponent(Point point1, Point point2, double directionAngle, Scenario scenario) {
        super(point1, point2, scenario);
        id = counter++;
        this.directionAngle = directionAngle;
        //TODO: make this orientation random as start
        this.orientation = Orientation.LEFT;
        this.viewFieldAngle = Config.DEFAULT_VIEW_FIELD_ANGLE;

        direction = new Vector2D(this.directionAngle);
        viewField = new Vector2D(viewFieldAngle);
        this.simpleRay = new BasicVision(scenario);
        initialPosition = new Point(getX(),getY());
        initializeAlgorithm();
    }

    public PlayerComponent(Point point1, Point point2, double directionAngle,  Scenario scenario, double baseSpeed, double distanceViewing, double smellingDistance) {
        this(point1, point2, directionAngle, scenario);
        this.viewField = new Vector2D(viewFieldAngle);
        this.simpleRay = new BasicVision(scenario);
        this.baseSpeed =baseSpeed;
        this.viewFieldLength =distanceViewing;
        this.smellingDistance =smellingDistance;
    }

    public Point getCoordinates(){
        return getArea().getTopLeft();
    }

    public double getX() {
        return getArea().getTopLeft().x;
    }

    public double getY() {
        return getArea().getTopLeft().y;
    }


    /**
     * method that queries the algorithm for a move that it should execute.
     */
    public ActionTuple calculateMove(){
        return algorithm.calculateMovement();
    }

    public double getDirectionAngle() {return directionAngle;}

    public Vector2D getDirection() {return direction;}

    public void setDirectionAngle(double directionAngle) {this.directionAngle = directionAngle;}

    public double getViewFieldLength() {return viewFieldLength;}

    public double getViewFieldAngle() {return viewFieldAngle;}

    public Orientation getOrientation() {return orientation;}

    public List<Grid> getAgentsCurrentVision() {return agentsCurrentVision;}

    public Scenario updateVision() {
        Scenario newScenario = simpleRay.calculateAgentVision(this);
        agentsCurrentVision.clear();
        agentsCurrentVision = simpleRay.getFurthestFrontierGrid();
        return newScenario;
    }

    public void turn(double angle){
        setDirectionAngle(this.directionAngle+angle);
        direction = new Vector2D(getDirectionAngle());
    }

    //TODO: maybe one method only left or right momvement
    public void applyAction(Actions action) {
        switch (action) {
            case TURN_LEFT -> {
                setDirectionAngle(Math.toRadians(180));
                direction = new Vector2D(getDirectionAngle());
                this.orientation = Orientation.LEFT;
            }
            case TURN_RIGHT -> {
                setDirectionAngle(Math.toRadians(0));
                direction = new Vector2D(getDirectionAngle());
                this.orientation = Orientation.RIGHT;
            }
            case TURN_UP -> {
                setDirectionAngle(Math.toRadians(90));
                direction = new Vector2D(getDirectionAngle());
                this.orientation = Orientation.UP;
            }
            case TURN_DOWN -> {
                setDirectionAngle(Math.toRadians(270));
                direction = new Vector2D(getDirectionAngle());
                this.orientation = Orientation.DOWN;
            }
            case MOVE_FORWARD -> {
                moveAgentForward(action);
            }
        }
    }

    public void moveAgentForward(Actions action){
        if(action == Actions.MOVE_FORWARD) {
            switch (this.orientation) {
                case UP : changePositionCoordinates(0,-1); //Elena said this
                    break;
                case DOWN: changePositionCoordinates(0,1); //Elena said this
                    break;
                case LEFT: changePositionCoordinates(-1,0);
                    break;
                case RIGHT: changePositionCoordinates(1,0);
                    break;
                }
            }
        }

    public void changePositionCoordinates(double dx, double dy){
        getArea().getTopLeft().x += dx;
        getArea().getBottomRight().x += dx;
        getArea().getTopLeft().y += dy;
        getArea().getBottomRight().y += dy;
    }

    public boolean collision(Component c, double distance){
        for(int i = 0; i<distance; i++){
            double dx = Math.cos(directionAngle)*i;
            double dy = Math.sin(directionAngle)*i;
            if(c.getArea().isHit(new Point(this.getArea().getTopLeft().x + dx, this.getArea().getTopLeft().y + dy))){
                return true;
            }
        }
        double dx = Math.cos(directionAngle)*distance;
        double dy = Math.sin(directionAngle)*distance;
        return c.getArea().isHit(new Point(this.getArea().getTopLeft().x + dx, this.getArea().getTopLeft().y + dy));
    }

    public void setPosition(Point p){
        setArea(new Area(p, p.clone()));
    }

    public void setViewFieldLength(double viewFieldLength) { this.viewFieldLength = viewFieldLength; }


    public void setAlgorithmValue(AlgorithmEnum algorithmValue){
        this.algorithmValue = algorithmValue;
    }

    private void initializeAlgorithm() {
        switch(algorithmValue){
            case A_STAR -> algorithm = new AStar((int) initialPosition.x, (int) initialPosition.y, getScenario().getMap(), this);
            case WALL_FOLLOWING -> {
                //TODO implement constructor of wall following algorithm
            }
            case FLOOD_FILL -> {
                //TODO implement constructor of flood fill algorithm
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setMovingSound() {
        Area initArea = this.getArea();
        Point topLeft = new Point((initArea.getTopLeft().x - (0.25*Config.DEFAULT_SOUND_DISTANCE)),initArea.getTopLeft().y - (0.25*Config.DEFAULT_SOUND_DISTANCE));
        Point bottomRight = new Point(initArea.getTopLeft().x + (0.25*Config.DEFAULT_SOUND_DISTANCE),initArea.getTopLeft().y + (0.25*Config.DEFAULT_SOUND_DISTANCE));
        this.movingSound = new Area(topLeft,bottomRight);
    }

    public Area getMovingSound() { return movingSound;}

    public double getBaseSpeed() { return baseSpeed; }

    public void teleport(Point target) {
        double x = target.x;
        double y = target.y;
        getArea().getTopLeft().x = x;
        getArea().getBottomRight().x = x;
        getArea().getTopLeft().y = y;
        getArea().getBottomRight().y = y;
        hasTeleported = true;
    }

    public boolean isHasTeleported() {
        return hasTeleported;
    }

    public void setHasTeleported(boolean hasTeleported) {
        this.hasTeleported = hasTeleported;
    }
}

/*    OLD MOVE METHODS


    public void move(double distance){
        double dx = Math.cos(directionAngle)*distance;
        double dy = Math.sin(directionAngle)*distance;
        move(dx,dy);
    }
 */