package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class Treasure {
    private int x;
    private int y;
    private int value; // 1, 2, or 3
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
        if (value == 3) return 30;
        return 0;
    }

    public int getComputerPoints() {
        if (value == 1) return 9;
        if (value == 2) return 30;
        if (value == 3) return 90;
        return 0;
    }

    public void draw(Console cn) {
        if (collected) return;
        TextAttributes color = new TextAttributes(Color.YELLOW, Color.BLACK);
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, (char)('0' + value), color);
    }

    public void erase(Console cn) {
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, ' ');
    }

    public boolean checkPickup(int px, int py) {
        if (!collected && x == px && y == py) {
            collected = true;
            return true;
        }
        return false;
    }

    public boolean isCollected() { return collected; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getValue() { return value; }
    public void collect() { collected = true; }
}
