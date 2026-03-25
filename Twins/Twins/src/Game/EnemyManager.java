package Game;

import enigma.console.Console;

public class EnemyManager {

    private XRobot[] xRobots = new XRobot[100];
    private int xRobotCount = 0;

    private CRobot[] cRobots = new CRobot[100];
    private int cRobotCount = 0;

    private int computerScore = 0;

    public void addXRobot(int x, int y) {
        if (xRobotCount < xRobots.length) {
            xRobots[xRobotCount] = new XRobot(x, y);
            xRobotCount++;
        }
    }

    public void addXRobot(int x, int y, int life) {
        if (xRobotCount < xRobots.length) {
            xRobots[xRobotCount] = new XRobot(x, y, life);
            xRobotCount++;
        }
    }

    public void addCRobot(int x, int y) {
        if (cRobotCount < cRobots.length) {
            cRobots[cRobotCount] = new CRobot(x, y);
            cRobotCount++;
        }
    }

    public int getRobotCount() {
        return xRobotCount;
    }

    public int getCRobotCount() {
        return cRobotCount;
    }

    public int getXRobotCount() {
        return xRobotCount;
    }

    public int getRobotX(int i) {
        return xRobots[i].x;
    }

    public int getRobotY(int i) {
        return xRobots[i].y;
    }

    public int getRobotLife(int i) {
        return xRobots[i].lifePoints;
    }

    public void moveRobots(GameBoard board, TrailManager tm, int currentTick, int playerX, int playerY, BCharacter twin, TreasureManager treasureManager) {
        for (int i = 0; i < xRobotCount; i++) {
            tm.addTrail(xRobots[i].x, xRobots[i].y, currentTick);
            xRobots[i].move(board, this, playerX, playerY, twin);
            // X-Robot picks up treasure
            int points = treasureManager.checkRobotPickup(xRobots[i].x, xRobots[i].y, null);
            computerScore += points;
        }
        for (int i = 0; i < cRobotCount; i++) {
            tm.addTrail(cRobots[i].x, cRobots[i].y, currentTick);
            cRobots[i].move(board, this, playerX, playerY, twin, treasureManager);
            // C-Robot picks up treasure
            int points = treasureManager.checkRobotPickup(cRobots[i].x, cRobots[i].y, null);
            computerScore += points;
        }
    }

    public void drawRobots(Console cn) {
        for (int i = 0; i < xRobotCount; i++) {
            xRobots[i].draw(cn);
        }
        for (int i = 0; i < cRobotCount; i++) {
            cRobots[i].draw(cn);
        }
    }

    public boolean isRobotAt(int targetX, int targetY) {
        for (int i = 0; i < xRobotCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                return true;
            }
        }
        for (int i = 0; i < cRobotCount; i++) {
            if (cRobots[i].x == targetX && cRobots[i].y == targetY) {
                return true;
            }
        }
        return false;
    }

    public int damageRobotAt(int targetX, int targetY, int damage, Console cn) {
        for (int i = 0; i < xRobotCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                xRobots[i].lifePoints -= damage;
                if (xRobots[i].lifePoints <= 0) {
                    if (cn != null) cn.getTextWindow().output((xRobots[i].x * 2) + 4, xRobots[i].y + 2, ' ');
                    for (int j = i; j < xRobotCount - 1; j++) {
                        xRobots[j] = xRobots[j + 1];
                    }
                    xRobots[xRobotCount - 1] = null;
                    xRobotCount--;
                    return 1;
                }
                return 0;
            }
        }
        for (int i = 0; i < cRobotCount; i++) {
            if (cRobots[i].x == targetX && cRobots[i].y == targetY) {
                cRobots[i].lifePoints -= damage;
                if (cRobots[i].lifePoints <= 0) {
                    if (cn != null) cn.getTextWindow().output((cRobots[i].x * 2) + 4, cRobots[i].y + 2, ' ');
                    for (int j = i; j < cRobotCount - 1; j++) {
                        cRobots[j] = cRobots[j + 1];
                    }
                    cRobots[cRobotCount - 1] = null;
                    cRobotCount--;
                    return 1;
                }
                return 0;
            }
        }
        return 0;
    }

    public boolean isAdjacentToPlayer(int playerX, int playerY) {
        for (int i = 0; i < xRobotCount; i++) {
            int dx = xRobots[i].x - playerX;
            int dy = xRobots[i].y - playerY;
            // 4 directions only (not diagonal)
            if ((dx == 0 && (dy == 1 || dy == -1)) || (dy == 0 && (dx == 1 || dx == -1))) {
                return true;
            }
        }
        for (int i = 0; i < cRobotCount; i++) {
            int dx = cRobots[i].x - playerX;
            int dy = cRobots[i].y - playerY;
            if ((dx == 0 && (dy == 1 || dy == -1)) || (dy == 0 && (dx == 1 || dx == -1))) {
                return true;
            }
        }
        return false;
    }

    public int getComputerScore() { return computerScore; }
}
