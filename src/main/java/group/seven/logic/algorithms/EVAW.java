package group.seven.logic.algorithms;

import group.seven.enums.Action;
import group.seven.enums.AlgorithmType;
import group.seven.enums.Cardinal;
import group.seven.model.agents.Agent;
import group.seven.model.agents.Move;
import group.seven.model.agents.TileNode;
import group.seven.model.environment.Adjacent;
import group.seven.model.environment.Pheromone;
import group.seven.model.environment.Scenario;

import java.util.LinkedList;
import java.util.List;

import static group.seven.enums.AlgorithmType.EVAW;
import static group.seven.enums.Cardinal.*;

public class EVAW implements Algorithm{

    Agent agent;
    public EVAW(Agent a) {
        this.agent = a;
    }

    List<Move> moves = new LinkedList<>();
    /**
     * This method will check if
     * @return the next move to be executed by the game runner
     */
    @Override
    public Move getNext() {

        if(moves.isEmpty()){
            calculateMove();
        }
        if(moves.isEmpty()){
            return new Move(Action.NOTHING, 0,agent);
        }
        Move nextMove = moves.get(0);
        moves.remove(0);
        return nextMove;

    }


    /**
     * This method will find the next action  to do based upon the Ants algorithm proposed in:
     * Theoretical Study of ant-based Algorithms for Multi-Agent Patrolling. by Arnaud Glad, Olivier Simonin,
     * Olivier Buffet and François Charpillet.
     */
    public void calculateMove(){
        Adjacent<TileNode> a = agent.getMapPosition(agent.x,agent.y).getAdjacent();
        Difference d = new Difference();
        d.target=a.north();
        d.targetOrientation=NORTH;

        checkDifference(d, a.east(),EAST);
        checkDifference(d, a.south(),SOUTH);
        checkDifference(d, a.west(),WEST);

//        d.target.getPheromone().setStrength(Pheromone.maxStrength);
        Scenario.TILE_MAP.dropPheromone(d.target.getX(),d.target.getY());

        if(agent.getDirection()!=d.targetOrientation){
            switch (d.targetOrientation){
                case NORTH -> moves.add(new Move(Action.TURN_UP,0,agent));
                case EAST -> moves.add(new Move(Action.TURN_RIGHT,0,agent));
                case SOUTH -> moves.add(new Move(Action.TURN_DOWN,0,agent));
                case WEST -> moves.add(new Move(Action.TURN_LEFT,0,agent));
            }
        }
        moves.add(new Move(Action.MOVE_FORWARD,1,agent));
    }

    /**
     * inner class to store the new target orientation and target node.
     */
    static class Difference{
        TileNode target;
        Cardinal targetOrientation;
    }

    /**
     * This method will check if the new tile has a lower pheromone strength compared to the current lowest.
     *
     * @param difference an inner class that stores the current lowest value pheromone tile
     * @param compare The tile to which the current lowest is compared
     * @param compareOrientation the orientation of the tile {@code compare} with respect to the initial position.
     */
    public void checkDifference(Difference difference, TileNode compare, Cardinal compareOrientation){
        if(difference.target==null){
            difference.target=compare;
            difference.targetOrientation = compareOrientation;
        }
        else if(compare!=null && compare.getPheromoneStrength()<=difference.target.getPheromoneStrength()){
            if(compare.getPheromoneStrength()==difference.target.getPheromoneStrength()){
                if(Math.random()>0.5){
                    difference.target = compare;
                    difference.targetOrientation = compareOrientation;
                }
            }
            else{
                difference.target = compare;
                difference.targetOrientation = compareOrientation;
            }
        }
    }

    @Override
    public AlgorithmType getType() {
        return EVAW;
    }
}
