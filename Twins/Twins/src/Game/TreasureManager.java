package Game;

import enigma.console.Console;

public class TreasureManager {

    private Treasure[] treasures = new Treasure[200];
    private int treasureCount = 0;

    public void addTreasure(int x, int y, int value) {
        if (treasureCount < treasures.length) {
            treasures[treasureCount] = new Treasure(x, y, value);
            treasureCount++;
        }
    }

    public int checkPlayerPickup(int px, int py, Console cn) {
        for (int i = 0; i < treasureCount; i++) {
            if (!treasures[i].isCollected() && treasures[i].checkPickup(px, py)) {
                treasures[i].erase(cn);
                return treasures[i].getPlayerPoints();
            }
        }
        return 0;
    }

    public int checkRobotPickup(int rx, int ry, Console cn) {
        for (int i = 0; i < treasureCount; i++) {
            if (!treasures[i].isCollected() && treasures[i].checkPickup(rx, ry)) {
                if (cn != null) treasures[i].erase(cn);
                return treasures[i].getComputerPoints();
            }
        }
        return 0;
    }

    // Find nearest treasure for C-Robot (Manhattan distance)
    public int[] findNearestTreasure(int rx, int ry) {
        int bestDist = Integer.MAX_VALUE;
        int bestX = -1;
        int bestY = -1;
        for (int i = 0; i < treasureCount; i++) {
            if (!treasures[i].isCollected()) {
                int dist = Math.abs(treasures[i].getX() - rx) + Math.abs(treasures[i].getY() - ry);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestX = treasures[i].getX();
                    bestY = treasures[i].getY();
                }
            }
        }
        if (bestX == -1) return null;
        return new int[]{bestX, bestY};
    }

    public void drawTreasures(Console cn) {
        for (int i = 0; i < treasureCount; i++) {
            if (!treasures[i].isCollected()) {
                treasures[i].draw(cn);
            }
        }
    }
}
