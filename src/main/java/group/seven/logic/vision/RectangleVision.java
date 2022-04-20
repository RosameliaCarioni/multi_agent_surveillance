package group.seven.logic.vision;

import group.seven.enums.Cardinal;
import group.seven.enums.TileType;
import group.seven.model.agents.Agent;
import group.seven.model.environment.Scenario;
import group.seven.model.environment.Tile;
import group.seven.model.environment.TileMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Class implements a rectangular vision for agent of  size [3 x distanceViewing]
 */
public class RectangleVision implements Vision {
    Scenario scenario;
    TileMap map;
    int distanceViewing;

    /**
     * Constructor (initializing the map and distanceViewing just because they are used a lot)
     * @param scenario
     */
    public RectangleVision(Scenario scenario) {
        this.scenario = scenario;
        this.map = scenario.get().TILE_MAP;
        this.distanceViewing = scenario.get().VIEW_DISTANCE;
    }


    @Override
    public void observe(int x, int y, List<Tile> observedTile, Agent agent){
        map.getTile(x,y).setExplored(agent);
        observedTile.add(map.getTile(x,y));
    }

    @Override
    public List<Tile> updateAndGetVisionAgent (Agent agent) {
        List<Tile> observedTiles = new LinkedList<>(); // list contains all the tiles seen by agent
        //get position of agent
        int xCoordinate = agent.getX();
        int yCoordinate = agent.getY();
        Cardinal directionAgent = agent.getDirection(); //get direction of agent

        switch (directionAgent) {
            case NORTH -> {
                for (int y = yCoordinate; y > yCoordinate - distanceViewing; y--) { //check straight
                    if (y >= 0) { //can't go lower than y=0, so if the number is negative is out of bound
                        // set that the player saw the tile
                        observe(xCoordinate,y, observedTiles, agent);
                        //CHECK COLLISIONS with walls
                        if (map.getTile(xCoordinate,y).getType() == TileType.WALL) {
                            //if the agent sees a wall, we break as it cant see any further
                            break;
                        }
                    } else { //out of bound for edges of map
                        observe(xCoordinate,y+1, observedTiles, agent);
                        break;
                    }
                }
                if (xCoordinate - 1 >= 0) { //if it is possible to be one of the left without going out of bound
                    for (int y = yCoordinate; y > yCoordinate - distanceViewing; y--) { //check one left
                        if (y >= 0) { //cant go higher than y=0, so if the number is positive is out of bound
                            observe(xCoordinate-1,y, observedTiles, agent);

                            //CHECK COLLISIONS with walls
                            if (map.getTile(xCoordinate - 1,y).getType() == TileType.WALL) { // TODO: check this
                                //if the agent sees a wall, we break as it cant see any further
                                break;
                            }
                        } else { //out of bound for edges of map
                            observe(xCoordinate-1,y+1, observedTiles, agent);

                            break;
                        }
                    }
                }
                if (xCoordinate + 1 < scenario.get().WIDTH) { //if it is possible to move one step to the right
                    for (int y = yCoordinate; y > yCoordinate - distanceViewing; y--) { //check one right
                        if (y >= 0) { //cant go higher than y=0, so if the number is positive is out of bound
                            observe(xCoordinate+1,y, observedTiles, agent);

                            //CHECK COLLISIONS with walls
                            if (map.getTile(xCoordinate + 1,y).getType() == TileType.WALL) {
                                break;
                            }
                        } else { //out of bound for edges of map
                            observe(xCoordinate+1,y+1, observedTiles, agent);
                            break;
                        }
                    }
                }
            }

            case SOUTH -> {
                for (int y = yCoordinate; y < yCoordinate + distanceViewing; y++) { //check straight
                    if (y < scenario.get().HEIGHT) { //cant go lower than y=map.height, so if the number is larger is out of bound
                        observe(xCoordinate,y, observedTiles, agent);

                        //CHECK COLLISIONS with walls
                        if (map.getTile(xCoordinate,y).getType() == TileType.WALL) {
                            break;
                        }
                    } else { //out of bound for edges of map
                        observe(xCoordinate,y-1, observedTiles, agent);

                        break;
                    }
                }
                if (xCoordinate - 1 >= 0) { //if it is possible to be one of the left without going out of bound
                    for (int y = yCoordinate; y < yCoordinate + distanceViewing; y++) { //check one left
                        if (y < scenario.get().HEIGHT) { //cant go lower than y=map.height, so if the number is larger is out of bound
                            observe(xCoordinate-1,y, observedTiles, agent);

                            //CHECK COLLISIONS with walls
                            if (map.getTile(xCoordinate-1,y).getType() == TileType.WALL) {
                                break;
                            }
                        } else { //out of bound for edges of map
                            observe(xCoordinate-1,y-1, observedTiles, agent);

                            break;
                        }
                    }
                }
                if (xCoordinate + 1 < scenario.get().WIDTH) { //if it is possible to move one step to the right
                    for (int y = yCoordinate; y < yCoordinate + distanceViewing; y++) { //check one right
                        if (y < scenario.get().HEIGHT) { //cant go lower than y=map.height, so if the number is larger is out of bound
                            observe(xCoordinate+1,y, observedTiles, agent);
                            //CHECK COLLISIONS with walls
                            if (map.getTile(xCoordinate+1,y).getType() == TileType.WALL) {
                                break;
                            }
                        } else {
                            observe(xCoordinate+1,y-1, observedTiles, agent);
                            break;
                        }
                    }
                }
            }
            case EAST -> {
                for (int x = xCoordinate; x < xCoordinate + distanceViewing; x++) { //check straight
                    if (x < scenario.get().WIDTH) {
                        observe(x,yCoordinate, observedTiles, agent);

                        //CHECK COLLISIONS with walls
                        if (map.getTile(x,yCoordinate).getType() == TileType.WALL) {
                            break;
                        }
                    } else { //out of bound for edges of map
                        observe(x-1,yCoordinate, observedTiles, agent);

                        break;
                    }
                }
                if (yCoordinate - 1 >= 0) {
                    for (int x = xCoordinate; x < xCoordinate + distanceViewing; x++) {
                        if (x < scenario.get().WIDTH) { //cant go higher than y=0, so if the number is positive is out of bound
                            observe(x,yCoordinate-1, observedTiles, agent);
                            //CHECK COLLISIONS with walls
                            if (map.getTile(x,yCoordinate-1).getType() == TileType.WALL) {
                                break;
                            }
                        } else {
                            observe(x-1,yCoordinate-1, observedTiles, agent);
                            break;
                        }
                    }
                }
                if (yCoordinate + 1 < scenario.get().HEIGHT) {
                    for (int x = xCoordinate; x < xCoordinate + distanceViewing; x++) {
                        if (x < scenario.get().WIDTH) {
                            observe(x,yCoordinate+1, observedTiles, agent);
                            //CHECK COLLISIONS with walls
                            if (map.getTile(x,yCoordinate+1).getType() == TileType.WALL) {
                                break;
                            }
                        } else {
                            observe(x-1,yCoordinate+1, observedTiles, agent);
                            break;
                        }
                    }
                }
            }
            case WEST -> {
                for (int x = xCoordinate; x > xCoordinate - distanceViewing; x--) { //check straight
                    if (x >= 0) {
                        observe(x,yCoordinate, observedTiles, agent);

                        //CHECK COLLISIONS with walls
                        if (map.getTile(x,yCoordinate).getType() == TileType.WALL) {
                            break;
                        }
                    } else { //out of bound for edges of map
                        observe(x+1,yCoordinate, observedTiles, agent);

                        break;
                    }
                }
                if (yCoordinate - 1 >= 0) {
                    for (int x = xCoordinate; x > xCoordinate - distanceViewing; x--) {
                        if (x >= 0) { //cant go higher than y=0, so if the number is positive is out of bound
                            observe(x,yCoordinate-1, observedTiles, agent);
                            //CHECK COLLISIONS with walls
                            if (map.getTile(x,yCoordinate-1).getType() == TileType.WALL) {
                                break;
                            }
                        } else {
                            observe(x+1,yCoordinate-1, observedTiles, agent);
                            break;
                        }
                    }
                }
                if (yCoordinate + 1 < scenario.get().HEIGHT) {
                    for (int x = xCoordinate; x > xCoordinate - distanceViewing; x--) {
                        if (x >= 0) {
                            observe(x,yCoordinate+1, observedTiles, agent);

                            //CHECK COLLISIONS with walls
                            if (map.getTile(x,yCoordinate+1).getType() == TileType.WALL) {
                                break;
                            }
                        } else {
                            observe(x+1,yCoordinate+1, observedTiles, agent);
                            break;
                        }
                    }
                }
            }
        }
        return observedTiles;
    }

}
