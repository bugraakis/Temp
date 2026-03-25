package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class LaserBeam {
    private int x, y;
    private int spawnTick;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;
    private static final Color LASER_COLOR = new Color(255, 140, 190);
    private static final Color FLOOR_BG = new Color(30, 28, 50);

    public LaserBeam(int x, int y, int spawnTick) {
        this.x = x;
        this.y = y;
        this.spawnTick = spawnTick;
    }

    public void draw(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, '+', new TextAttributes(LASER_COLOR, FLOOR_BG));
    }

    public void erase(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, ' ', new TextAttributes(FLOOR_BG, FLOOR_BG));
    }

    public boolean isExpired(int currentTick) { return currentTick - spawnTick >= 100; }
    public int getX() { return x; }
    public int getY() { return y; }
}
