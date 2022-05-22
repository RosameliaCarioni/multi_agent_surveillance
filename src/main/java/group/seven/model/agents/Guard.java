package group.seven.model.agents;


import group.seven.enums.AlgorithmType;
import group.seven.enums.MarkerType;
import group.seven.enums.PheromoneType;
import group.seven.logic.algorithms.*;
import group.seven.logic.algorithms.Algorithm;
import group.seven.logic.algorithms.RandomMoves;
import group.seven.logic.algorithms.RandomTest;
import group.seven.logic.vision.RectangleVision;
import group.seven.logic.vision.Vision;
import group.seven.model.environment.Marker;
import group.seven.model.environment.Pheromone;
import group.seven.model.environment.Scenario;
import group.seven.model.environment.Tile;
import group.seven.utils.Config;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

import static group.seven.enums.Cardinal.NORTH;
import static group.seven.enums.TileType.GUARD;

public class Guard extends Agent {

    public final int PHEROMONELIFETIME = 20;
    private final int ID;
    private final int maxSpeed = (int) Scenario.GUARD_SPRINT_SPEED;
    public int currentSpeed;
    Algorithm algorithm;
    private Vision vision;
    private ArrayList<Marker> markers = new ArrayList<>();
    private final ArrayList<Pheromone> pheromones = new ArrayList<>();

    public Guard(int x, int y, Algorithm algorithm, int startSpeed, Vision vision, ArrayList<Marker> markers) {
        this(x, y);
        this.algorithm = algorithm;
        this.currentSpeed = startSpeed;
        this.vision = vision;
        this.markers = markers;
    }

    public Guard(int x, int y, AlgorithmType algorithmType) {
        this(x, y);
        algorithm = initAlgo(algorithmType);
        System.out.println(algorithmType);
    }

    public void createAlgorithm(AlgorithmType algorithmType){
        switch (algorithmType){
            case BRICK_AND_MORTAR -> this.algorithm=new BrickAndMortar(this);
            case EVAW -> this.algorithm=new EVAW(this);
            case ANT_PURSUIT -> this.algorithm= new AntsPursuit(this);
        }
    }



    public Guard(int x, int y) {
        super(x,y);
        ID = newID();
        agentType = GUARD;
        currentSpeed = 3; //DEFAULT //TODO base speed?
        direction = NORTH; //DEFAULT
        algorithm = initAlgo(Config.ALGORITHM_GUARD); //DEFAULT
        vision = new RectangleVision(this); //DEFAULT

       // algorithm = new RandomTest(this);
    }

    public Algorithm initAlgo(AlgorithmType type) {
        return switch (type) {
            case EVAW -> this.algorithm=new EVAW(this);
            case ANT_PURSUIT -> this.algorithm= new AntsPursuit(this);
            case ANT -> new Ant(this);
            case BRICK_AND_MORTAR -> new BrickAndMortar(this);
            default -> new RandomTest(this);
        };
    }

    @Override
    public void updateVision() {
        List<Tile> newTiles = vision.updateAndGetVisionAgent(this);
        seenTiles = duplicatedTiles(seenTiles,newTiles);
        //print(seenTiles);
    }

    @Override
    public Move calculateMove() {
        return algorithm.getNext();
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public int getCurrentSpeed() {
        return currentSpeed;
    }


    @Override
    public String toString() {
        return "Guard{" +
                "ID=" + ID +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", speed=" + currentSpeed +
                ", algorithm=" + algorithm.getType() +
                '}';
    }
}
