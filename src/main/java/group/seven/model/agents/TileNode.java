package group.seven.model.agents;

import group.seven.enums.TileType;
import group.seven.model.environment.Adjacent;
import group.seven.model.environment.Marker;
import group.seven.model.environment.Scenario;
import group.seven.model.environment.Tile;

import java.util.ArrayList;
import java.util.List;

import static group.seven.enums.TileType.PORTAL;
import static group.seven.enums.TileType.TARGET;

public class TileNode {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //from agent's perspective
    int x, y;
    //Or Point2D?

    TileType type;
    Agent agent;
    TileType agentType = null;
    List<Marker> markers;
    Adjacent<TileNode> adjacent;

    public TileNode(Tile tile, Agent a){ //internal representation for agents map  with 'adjacent'
        x = tile.getX();
        y = tile.getY();
        this.agent = a;
        for(Agent agent: Scenario.TILE_MAP.agents){
            if(agent!=null && agent.x==tile.getX()&& agent.y==tile.getY()){
                this.agentType = agent.agentType;
                break;
            }
        }
        updateAdjacent();

        markers = new ArrayList<>();
        for(Marker marker : Scenario.TILE_MAP.markers){
            if(marker.getXY().x()==x && marker.getXY().y()==y){
                markers.add(marker);
            }
        }
        type  = tile.getType();
        x = tile.getX();
        y = tile.getY();
    }

    //updates one node
    public void update(){
        agentType = null;
        markers.clear();
        for(Agent agent: Scenario.TILE_MAP.agents){
            if(agent.x==x&& agent.y==y){
                this.agentType = agent.agentType;
                break;
            }
        }
        for(Marker marker : Scenario.TILE_MAP.markers){
            if(marker.getXY().x()==x && marker.getXY().y()==y){
                markers.add(marker);
            }
        }
        updateAdjacent();

    }

    public void updateAdjacent(){
        TileNode north = agent.getMapPosition(agent.x,agent.y-1);
        TileNode east = agent.getMapPosition(agent.x+1,agent.y);
        TileNode south = agent.getMapPosition(agent.x,agent.y+1);
        TileNode west = agent.getMapPosition(agent.x-1,agent.y);
        TileNode target = null;
        if(type==PORTAL){
            System.out.println("position "+Scenario.TILE_MAP.getTile(x,y));
            //int xTar=Scenario.TILE_MAP.getTile(x,y).adjacent.targetLocation().getX(); // taken inside , was outside before
            //int yTar=Scenario.TILE_MAP.getTile(x,y).adjacent.targetLocation().getY();
            int xTar=Scenario.targetArea.getX(); // taken inside , was outside before
            int yTar=Scenario.targetArea.getY();
            target = agent.getMapPosition(xTar,yTar);
        }

        adjacent = new Adjacent<>(north,east,south,west,target);
    }

    public TileType getType(){
        return type;
    }
}
