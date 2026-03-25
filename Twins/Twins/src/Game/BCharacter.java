package Game;

import enigma.console.Console;

public class BCharacter {
    private int x, y;

    public BCharacter(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(MoveTrack tracker, GameBoard board, int currentMode, TrailManager tm, int currentTick, EnemyManager enemyManager) {
        int nextX = this.x + (currentMode == 1 ? tracker.getSameDirX() : tracker.getOppositeDirX());
        int nextY = this.y + (currentMode == 1 ? tracker.getSameDirY() : tracker.getOppositeDirY());

        CollisionControl collision = new CollisionControl();
        if (collision.canPlayerMove(board.getMap(), nextX, nextY, enemyManager)) {
            tm.addTrail(this.x, this.y, currentTick);
            this.x = nextX;
            this.y = nextY;
        }
    }

    public void draw(Console cn, int playerX, int playerY) {
        if (this.x != playerX || this.y != playerY)
            cn.getTextWindow().output((this.x * 2) + 4, this.y + 2, 'B');
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
}
