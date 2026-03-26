package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class ScoreManager {

    private int score;
    private int health;
    private int maxHealth;

    private static final int HUD_X = 120;
    private static final int HUD_Y = 2;

    public ScoreManager() {
        this.score = 0;
        this.health = 1000;
        this.maxHealth = 1000;
    }

    public void addScore(int points) {
        score += points;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void drawHUD(Console cn, int ammo) {
        TextAttributes hudColor = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        TextAttributes scoreColor = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
        TextAttributes healthColor;

        if (health > 600) {
            healthColor = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
        } else if (health > 300) {
            healthColor = new TextAttributes(new Color(255, 255, 0), Color.BLACK);
        } else {
            healthColor = new TextAttributes(new Color(255, 50, 50), Color.BLACK);
        }

        TextAttributes ammoColor = new TextAttributes(new Color(255, 255, 0), Color.BLACK);
        TextAttributes labelColor = new TextAttributes(new Color(0, 255, 255), Color.BLACK);

        clearHUDArea(cn);

        drawString(cn, HUD_X, HUD_Y, "SCORE: ", labelColor);
        drawString(cn, HUD_X + 7, HUD_Y, "" + score, scoreColor);

        drawString(cn, HUD_X, HUD_Y + 2, "HEALTH: ", labelColor);
        drawString(cn, HUD_X + 8, HUD_Y + 2, "" + health + "/" + maxHealth, healthColor);

        drawHealthBar(cn, HUD_X, HUD_Y + 3, healthColor);

        drawString(cn, HUD_X, HUD_Y + 5, "AMMO: ", labelColor);
        drawString(cn, HUD_X + 6, HUD_Y + 5, "" + ammo, ammoColor);

        drawString(cn, HUD_X, HUD_Y + 8, "[SPACE] Shoot", hudColor);
        drawString(cn, HUD_X, HUD_Y + 9, "[M] Toggle Mode", hudColor);
        drawString(cn, HUD_X, HUD_Y + 10, "[ESC] Save/Quit", hudColor);
        drawString(cn, HUD_X, HUD_Y + 12, "@ = Laser Pack", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
    }

    private void drawHealthBar(Console cn, int x, int y, TextAttributes color) {
        TextAttributes emptyColor = new TextAttributes(new Color(60, 60, 60), Color.BLACK);
        int barLength = 20;
        int filled = (health * barLength) / maxHealth;

        drawString(cn, x, y, "[", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                drawChar(cn, x + 1 + i, y, '=', color);
            } else {
                drawChar(cn, x + 1 + i, y, '.', emptyColor);
            }
        }
        drawString(cn, x + 1 + barLength, y, "]", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
    }

    public void drawGameOver(Console cn) {
        TextAttributes gameOverColor = new TextAttributes(new Color(255, 50, 50), Color.BLACK);
        TextAttributes scoreColor = new TextAttributes(new Color(255, 255, 0), Color.BLACK);

        drawString(cn, HUD_X, HUD_Y + 15, "GAME OVER!", gameOverColor);
        drawString(cn, HUD_X, HUD_Y + 16, "Final Score: " + score, scoreColor);
        drawString(cn, HUD_X, HUD_Y + 18, "Press ESC to exit", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
    }

    private void clearHUDArea(Console cn) {
        for (int row = HUD_Y; row < HUD_Y + 20; row++) {
            for (int col = HUD_X; col < HUD_X + 30; col++) {
                cn.getTextWindow().output(col, row, ' ');
            }
        }
    }

    private void drawString(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++) {
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
        }
    }

    private void drawChar(Console cn, int x, int y, char c, TextAttributes attr) {
        cn.getTextWindow().output(x, y, c, attr);
    }

    public int getScore() { return score; }
    public int getHealth() { return health; }
}
