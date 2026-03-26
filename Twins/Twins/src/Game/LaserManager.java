package Game;

import enigma.console.Console;

public class LaserManager {

    private LaserBeam[] lasers = new LaserBeam[200];
    private int laserCount = 0;

    private PackedLaser[] packs = new PackedLaser[20];
    private int packCount = 0;

    private int ammo = 0;

    // Spread path for active laser fire
    private int[][] spreadPath = new int[200][2];
    private int spreadLength = 0;
    private int spreadIndex = 0;
    private boolean spreading = false;

    public void fireLaser(int ax, int ay, int bx, int by, int tick) {
        if (ammo <= 0) return;
        if (ax == bx && ay == by) return;
        if (spreading) return;

        // Compute path from A to B using simple step-by-step line
        spreadLength = 0;
        int currentX = ax;
        int currentY = ay;

        // Calculate total distance in X and Y
        int totalDX = bx - ax;
        int totalDY = by - ay;

        // Find step direction for X and Y
        int stepX = 0;
        if (totalDX > 0) stepX = 1;
        if (totalDX < 0) stepX = -1;

        int stepY = 0;
        if (totalDY > 0) stepY = 1;
        if (totalDY < 0) stepY = -1;

        int absDX = Math.abs(totalDX);
        int absDY = Math.abs(totalDY);

        // Use the longer axis to determine number of steps
        int steps = absDX;
        if (absDY > absDX) {
            steps = absDY;
        }

        for (int step = 0; step < steps; step++) {
            // Move along the longer axis each step
            // Move along the shorter axis proportionally
            if (absDX >= absDY) {
                currentX = currentX + stepX;
                // Check if we need to move Y this step
                if (absDY > 0 && step * absDY / absDX != (step + 1) * absDY / absDX) {
                    currentY = currentY + stepY;
                }
            } else {
                currentY = currentY + stepY;
                // Check if we need to move X this step
                if (absDX > 0 && step * absDX / absDY != (step + 1) * absDX / absDY) {
                    currentX = currentX + stepX;
                }
            }

            // Skip the B position (don't place laser on B)
            if (currentX == bx && currentY == by) break;

            if (spreadLength < spreadPath.length) {
                spreadPath[spreadLength][0] = currentX;
                spreadPath[spreadLength][1] = currentY;
                spreadLength++;
            }
        }

        if (spreadLength > 0) {
            spreading = true;
            spreadIndex = 0;
            ammo--;
        }
    }

    public void updateSpread(char[][] map, int currentTick) {
        if (!spreading) return;

        if (spreadIndex < spreadLength) {
            int x = spreadPath[spreadIndex][0];
            int y = spreadPath[spreadIndex][1];
            // Only place on empty squares
            if (y >= 0 && y < map.length && x >= 0 && x < map[0].length && map[y][x] != '#') {
                if (laserCount < lasers.length) {
                    lasers[laserCount] = new LaserBeam(x, y, currentTick);
                    laserCount++;
                }
            }
            spreadIndex++;
        }

        if (spreadIndex >= spreadLength) {
            spreading = false;
        }
    }

    public int neighborHarm(EnemyManager enemies, Console cn) {
        int kills = 0;
        int robotCount = enemies.getRobotCount();
        for (int r = robotCount - 1; r >= 0; r--) {
            int rx = enemies.getRobotX(r);
            int ry = enemies.getRobotY(r);
            int adjacentCount = 0;
            for (int i = 0; i < laserCount; i++) {
                if (!lasers[i].isActive()) continue;
                int lx = lasers[i].getX();
                int ly = lasers[i].getY();
                int diffX = Math.abs(lx - rx);
                int diffY = Math.abs(ly - ry);
                if ((diffX == 1 && diffY == 0) || (diffX == 0 && diffY == 1)) {
                    adjacentCount++;
                }
            }
            if (adjacentCount > 0) {
                kills += enemies.damageRobotAt(rx, ry, adjacentCount * 50, cn);
            }
        }
        return kills;
    }

    public void cleanExpiredLasers(Console cn, int currentTick) {
        int writeIdx = 0;
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive() && !lasers[i].isExpired(currentTick)) {
                lasers[writeIdx] = lasers[i];
                writeIdx++;
            } else {
                lasers[i].erase(cn);
            }
        }
        laserCount = writeIdx;
    }

    public void drawLasers(Console cn) {
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive()) {
                lasers[i].draw(cn);
            }
        }
    }

    public void spawnPackedLaser(char[][] map, RandomSpawner spawner) {
        if (packCount < packs.length) {
            int[] pos = spawner.getSpawnPoint(map);
            packs[packCount] = new PackedLaser(pos[0], pos[1]);
            packCount++;
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

    public void drawPacks(Console cn) {
        for (int i = 0; i < packCount; i++) {
            if (!packs[i].isCollected()) {
                packs[i].draw(cn);
            }
        }
    }

    public int getAmmo() { return ammo; }
    public int getPackCount() { return packCount; }
}
