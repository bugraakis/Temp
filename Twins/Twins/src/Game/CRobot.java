package Game;

import enigma.console.Console;

public class CRobot {
    public int x;
    public int y;
    public int lifePoints = 1000;

    public CRobot(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(GameBoard board, EnemyManager enemies, int px, int py, BCharacter twin, TreasureManager tm) {
        int[] target = tm.findNearestTreasure(x, y);
        if (target == null) return;

        int tx = target[0];
        int ty = target[1];

        // Move towards nearest treasure (Manhattan)
        int nextX = x;
        int nextY = y;

        int dx = tx - x;
        int dy = ty - y;

        // Try horizontal first, then vertical
        if (Math.abs(dx) >= Math.abs(dy)) {
            if (dx > 0) nextX++;
            else if (dx < 0) nextX--;
        } else {
            if (dy > 0) nextY++;
            else if (dy < 0) nextY--;
        }

        CollisionControl cc = new CollisionControl();
        if (cc.canRobotMove(board.getMap(), nextX, nextY, enemies, px, py, twin)) {
            x = nextX;
            y = nextY;
        } else {
            // Try the other direction
            nextX = x;
            nextY = y;
            if (Math.abs(dx) >= Math.abs(dy)) {
                if (dy > 0) nextY++;
                else if (dy < 0) nextY--;
            } else {
                if (dx > 0) nextX++;
                else if (dx < 0) nextX--;
            }
            if (cc.canRobotMove(board.getMap(), nextX, nextY, enemies, px, py, twin)) {
                x = nextX;
                y = nextY;
            }
        }
    }

    public void draw(Console cn) {
        cn.getTextWindow().output((x * 2) + 4, y + 2, 'C');
    }
}
