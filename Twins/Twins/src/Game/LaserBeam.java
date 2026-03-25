package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class LaserBeam {
    private int x, y;
    private int spawnTick;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public LaserBeam(int x, int y, int spawnTick) {
        this.x = x;
        this.y = y;
        this.spawnTick = spawnTick;
    }

    public void draw(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, '+', new TextAttributes(Color.RED, Color.BLACK));
    }

    public void erase(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, ' ');
    }

    public boolean isExpired(int currentTick) { return currentTick - spawnTick >= 100; }
    public int getX() { return x; }
    public int getY() { return y; }
}
