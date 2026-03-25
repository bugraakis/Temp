package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class PackedLaser {
    private int x, y;
    private boolean collected;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public PackedLaser(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public void draw(Console cn) {
        if (collected) return;
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, '@', new TextAttributes(Color.YELLOW, Color.BLACK));
    }

    public void erase(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, ' ');
    }

    public boolean checkPickup(int playerX, int playerY) {
        if (!collected && x == playerX && y == playerY) { collected = true; return true; }
        return false;
    }

    public boolean isCollected() { return collected; }
}
