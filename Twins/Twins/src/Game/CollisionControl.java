package Game;

public class CollisionControl {

    private boolean isWall(char[][] map, int x, int y) {
        if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) return true;
        return map[y][x] == '#';
    }

    public boolean canPlayerMove(char[][] map, int nextX, int nextY, EnemyManager enemies) {
        return !isWall(map, nextX, nextY) && !enemies.isRobotAt(nextX, nextY);
    }

    public boolean canRobotMove(char[][] map, int nextX, int nextY, EnemyManager enemies, int px, int py, BCharacter twin) {
        if (isWall(map, nextX, nextY)) return false;
        if (nextX == px && nextY == py) return false;
        if (twin != null && nextX == twin.getX() && nextY == twin.getY()) return false;
        return !enemies.isRobotAt(nextX, nextY);
    }
}
