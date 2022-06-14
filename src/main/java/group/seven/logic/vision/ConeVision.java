package group.seven.logic.vision;

import group.seven.enums.Cardinal;
import group.seven.logic.geometric.XY;
import group.seven.model.agents.Agent;
import group.seven.model.environment.Scenario;
import group.seven.model.environment.Tile;

import java.util.LinkedList;
import java.util.List;

import static group.seven.enums.TileType.WALL;
import static group.seven.logic.vision.Vision.Type.CONE;
import static group.seven.model.environment.Scenario.TILE_MAP;

public class ConeVision implements Vision {

    public Type type = CONE;
    private Agent agent;
    public static int counterBIG = 0;

    public ConeVision() {

    }

    public ConeVision(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void observe(int x, int y, List<Tile> observedTile, Agent agent) {
        TILE_MAP.getTile(x,y).setExplored(agent);
        observedTile.add(TILE_MAP.getTile(x,y));
    }


    @Override
    public List<Tile> updateAndGetVisionAgent(Agent agent) {
        List<Tile> observedTiles = new LinkedList<>();
        int xCoordinate = agent.getX();
        int yCoordinate = agent.getY();
        int distance = Scenario.VIEW_DISTANCE;   // shortens the view distance if wall is encountered
        Cardinal directionAgent = agent.getDirection();

        switch (directionAgent) {
            // north is 90 degrees, so u need a ray at 45 degrees and 135 degrees
            case NORTH -> {
                List<Integer> blockedTiles = new LinkedList<>();
                int counter = 1;
                int x_counter = 1;
                int see_wall = 0;
                // <= or <, ceck the for loops with debugging
                for (int y = yCoordinate - counter; y >= yCoordinate - distance; y--) {
                    for (int x = xCoordinate - x_counter; x <= xCoordinate + x_counter; x++) {
                        if (y < Scenario.HEIGHT && x < Scenario.WIDTH && x > 0 && y > 0) {
                            if (blockedTiles.contains(x)) {
                                break;
                            } else if (TILE_MAP.getTile(x, y).getType() == WALL) {
                                blockedTiles.add(x);
                                break;
                            } else {
                                observe(x, y, observedTiles, agent);
                            }
                            // see_wall++;
                            // for (int i = y; y <= yCoordinate - Scenario.VIEW_DISTANCE; i--) {
                            //     blockedTiles.add(x - see_wall * i);
                            //     blockedTiles.add(x + see_wall * i);
                            //     System.out.println("STUCK HERE 1");
                            // }
                        }
                    }
                    x_counter++;
                }
                blockedTiles.clear();
            }
            case SOUTH -> {
                List<Integer> blockedTiles = new LinkedList<>();
                int counter = 1;
                int x_counter = 1;
                int see_wall = 0;
                for (int y = yCoordinate + counter; y <= yCoordinate + distance; y++) {
                    for (int x = xCoordinate - x_counter; x <= xCoordinate + x_counter; x++) {
                        if (y < Scenario.HEIGHT && x < Scenario.WIDTH && x > 0 && y > 0) {
                            if (blockedTiles.contains(x)) {
                                break;
                            } else if (TILE_MAP.getTile(x, y).getType() == WALL) {
                                blockedTiles.add(x);
                                break;
                            } else {
                                observe(x, y, observedTiles, agent);
                            }
                            // see_wall++;
                            // for (int i = y; y <= yCoordinate - Scenario.VIEW_DISTANCE; i--) {
                            //     blockedTiles.add(x - see_wall * i);
                            //     blockedTiles.add(x + see_wall * i);
                            //     System.out.println("STUCK HERE 1");
                            // }
                        }
                    }
                    x_counter++;
                }
                blockedTiles.clear();
            }
            case EAST -> {
                List<Integer> blockedTiles = new LinkedList<>();
                int counter = 1;
                int y_counter = 1;
                int see_wall = 0;
                for (int x = xCoordinate + counter; x <= xCoordinate + distance; x++) {
                    for (int y = yCoordinate - y_counter; y <= yCoordinate + y_counter; y++) {
                        if (y < Scenario.HEIGHT && x < Scenario.WIDTH && x > 0 && y > 0) {
                            if (blockedTiles.contains(x)) {
                                break;
                            } else if (TILE_MAP.getTile(x, y).getType() == WALL) {
                                blockedTiles.add(x);
                                break;
                            } else {
                                observe(x, y, observedTiles, agent);
                            }
                            // see_wall++;
                            // for (int i = y; y <= yCoordinate - Scenario.VIEW_DISTANCE; i--) {
                            //     blockedTiles.add(x - see_wall * i);
                            //     blockedTiles.add(x + see_wall * i);
                            //     System.out.println("STUCK HERE 1");
                            // }
                        }
                    }
                    y_counter++;
                }
                blockedTiles.clear();
            }
            //create rays
            case WEST -> {
                List<Integer> blockedTiles = new LinkedList<>();
                int counter = 1;
                int y_counter = 1;
                int see_wall = 0;
                for (int x = xCoordinate - counter; x >= xCoordinate - distance; x--) {
                    for (int y = yCoordinate - y_counter; y <= yCoordinate + y_counter; y++) {
                        if (y < Scenario.HEIGHT && x < Scenario.WIDTH && x > 0 && y > 0) {
                            if (blockedTiles.contains(x)) {
                                break;
                            } else if (TILE_MAP.getTile(x, y).getType() == WALL) {
                                blockedTiles.add(x);
                                break;
                            } else {
                                observe(x, y, observedTiles, agent);
                            }
                            // see_wall++;
                            // for (int i = y; y <= yCoordinate - Scenario.VIEW_DISTANCE; i--) {
                            //     blockedTiles.add(x - see_wall * i);
                            //     blockedTiles.add(x + see_wall * i);
                            //     System.out.println("STUCK HERE 1");
                            // }
                        }
                    }
                    y_counter++;
                }
                blockedTiles.clear();
            }
        }

        System.out.println("Size "+observedTiles.size());
        System.out.println("count "+counterBIG);
        counterBIG++;
        return observedTiles;
    }

    @Override
    public List<Tile> updateAndGetVisionAgent () {
        return updateAndGetVisionAgent(agent);
    }

    @Override
    public Type getType () {
        return type;
    }
}
