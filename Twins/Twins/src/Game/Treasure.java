package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class Treasure {
    private int x, y, value;
    private boolean collected;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;
    private static final Color FLOOR_BG = new Color(30, 28, 50);
    private static final Color T1_COLOR = new Color(255, 240, 160);
    private static final Color T2_COLOR = new Color(160, 210, 255);
    private static final Color T3_COLOR = new Color(220, 170, 255);

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

    private Color getColor() {
        if (value == 1) return T1_COLOR;
        if (value == 2) return T2_COLOR;
        return T3_COLOR;
    }

    public void draw(Console cn) {
        if (collected) return;
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, (char)('0' + value), new TextAttributes(getColor(), FLOOR_BG));
    }

    public void erase(Console cn) {
        cn.getTextWindow().output((x * 2) + OFFSET_X, y + OFFSET_Y, ' ', new TextAttributes(FLOOR_BG, FLOOR_BG));
    }

    public boolean checkPickup(int px, int py) {
        if (!collected && x == px && y == py) { collected = true; return true; }
        return false;
    }

    public boolean isCollected() { return collected; }
    public int getX() { return x; }
    public int getY() { return y; }
}
