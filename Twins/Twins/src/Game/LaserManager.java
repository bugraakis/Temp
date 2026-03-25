package Game;

import enigma.console.Console;

public class LaserManager {

    private LaserBeam[] lasers = new LaserBeam[500];
    private int laserCount = 0;
    private PackedLaser[] packs = new PackedLaser[20];
    private int packCount = 0;
    private int ammo = 0;

    private int[] lineX = new int[100];
    private int[] lineY = new int[100];
    private int lineLength = 0;
    private int lineIndex = 0;
    private int fireStartTick = -1;
    private boolean firing = false;

    public void fireLaser(int ax, int ay, int bx, int by, int tick) {
        if (ammo <= 0 || (ax == bx && ay == by)) return;

        lineLength = 0;
        int x0 = ax, y0 = ay;
        int dx = Math.abs(bx - x0), dy = Math.abs(by - y0);
        int sx = x0 < bx ? 1 : -1, sy = y0 < by ? 1 : -1;
        int err = dx - dy;

        while (!(x0 == bx && y0 == by)) {
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx) { err += dx; y0 += sy; }
            if (lineLength < lineX.length) {
                lineX[lineLength] = x0;
                lineY[lineLength] = y0;
                lineLength++;
            }
        }

        if (lineLength > 0) {
            fireStartTick = tick;
            lineIndex = 0;
            firing = true;
            ammo--;
        }
    }

    public void updateFiring(char[][] map, int currentTick) {
        if (!firing) return;
        int ticksElapsed = currentTick - fireStartTick;
        while (lineIndex < lineLength && lineIndex <= ticksElapsed) {
            int lx = lineX[lineIndex], ly = lineY[lineIndex];
            if (ly >= 0 && ly < map.length && lx >= 0 && lx < map[0].length && map[ly][lx] != '#') {
                if (laserCount < lasers.length) {
                    lasers[laserCount++] = new LaserBeam(lx, ly, currentTick);
                }
            }
            lineIndex++;
        }
        if (lineIndex >= lineLength) firing = false;
    }

    public int checkNeighborDamage(EnemyManager enemies, Console cn) {
        int kills = 0;
        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}};
        for (int i = 0; i < laserCount; i++) {
            int lx = lasers[i].getX(), ly = lasers[i].getY();
            for (int[] d : dirs) {
                int nx = lx + d[0], ny = ly + d[1];
                if (enemies.isRobotAt(nx, ny))
                    kills += enemies.damageRobotAt(nx, ny, 50, cn);
            }
        }
        return kills;
    }

    public void cleanExpiredLasers(Console cn, int currentTick) {
        int writeIdx = 0;
        for (int i = 0; i < laserCount; i++) {
            if (!lasers[i].isExpired(currentTick)) {
                lasers[writeIdx++] = lasers[i];
            } else {
                lasers[i].erase(cn);
            }
        }
        laserCount = writeIdx;
    }

    public void drawLasers(Console cn) {
        for (int i = 0; i < laserCount; i++) lasers[i].draw(cn);
    }

    public void spawnPackedLaser(char[][] map, RandomSpawner spawner) {
        if (packCount < packs.length) {
            int[] pos = spawner.getSpawnPoint(map);
            packs[packCount++] = new PackedLaser(pos[0], pos[1]);
        }
    }

    public int checkPickups(int playerX, int playerY, Console cn) {
        int picked = 0;
        for (int i = 0; i < packCount; i++) {
            if (!packs[i].isCollected() && packs[i].checkPickup(playerX, playerY)) {
                packs[i].erase(cn);
                ammo++;
                picked++;
            }
        }
        return picked;
    }

    public boolean checkPickupB(int bx, int by, Console cn) {
        for (int i = 0; i < packCount; i++) {
            if (!packs[i].isCollected() && packs[i].checkPickup(bx, by)) {
                packs[i].erase(cn);
                ammo++;
                return true;
            }
        }
        return false;
    }

    public void drawPacks(Console cn) {
        for (int i = 0; i < packCount; i++)
            if (!packs[i].isCollected()) packs[i].draw(cn);
    }

    public int getAmmo() { return ammo; }
    public int getPackCount() { return packCount; }
}
