package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class TrailManager {

    private static final Color FLOOR_BG = new Color(30, 28, 50);

    private class Trail {
        int x, y, spawnTick;
        Trail(int x, int y, int tick) { this.x = x; this.y = y; this.spawnTick = tick; }
    }

    private Trail[] trails = new Trail[2000];
    private int trailCount = 0;

    public void addTrail(int x, int y, int currentTick) {
        if (trailCount < trails.length)
            trails[trailCount++] = new Trail(x, y, currentTick);
    }

    public void clearOldTrails(Console cn, int currentTick) {
        TextAttributes floorAttr = new TextAttributes(FLOOR_BG, FLOOR_BG);
        for (int i = 0; i < trailCount; i++) {
            if (currentTick - trails[i].spawnTick >= 2) {
                cn.getTextWindow().output((trails[i].x * 2) + 4, trails[i].y + 2, ' ', floorAttr);
                trails[i] = trails[--trailCount];
                trails[trailCount] = null;
                i--;
            }
        }
    }
}
