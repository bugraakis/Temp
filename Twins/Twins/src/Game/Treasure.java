package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class Treasure {
    private int x, y, value;
    private boolean collected;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public Treasure(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.collected = false;
    }

    public int getPlayerPoints() {
        if (value == 1) return 3;
        if (value == 2) return 10;
        return 30;
    }

    public int getComputerPoints() {
        if (value == 1) return 9;
        if (value == 2) return 30;
        return 90;
    }

    public void draw(Console cn) {
        if (collected) return;
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, (char)('0' + value), new TextAttributes(Color.YELLOW, Color.BLACK));
    }

    public void erase(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, ' ');
    }

    public boolean checkPickup(int px, int py) {
        if (!collected && x == px && y == py) { collected = true; return true; }
        return false;
    }

    public boolean isCollected() { return collected; }
    public int getX() { return x; }
    public int getY() { return y; }
}
