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

    public void addScore(int points) { score += points; }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() { return health > 0; }

    public void drawHUD(Console cn, int ammo, int computerScore, int cRobots, int xRobots, int ticks) {
        TextAttributes healthColor;
        if (health > 600) healthColor = new TextAttributes(Color.GREEN, Color.BLACK);
        else if (health > 300) healthColor = new TextAttributes(Color.YELLOW, Color.BLACK);
        else healthColor = new TextAttributes(Color.RED, Color.BLACK);

        TextAttributes labelColor = new TextAttributes(Color.WHITE, Color.BLACK);
        TextAttributes hudColor = new TextAttributes(Color.CYAN, Color.BLACK);

        clearHUDArea(cn);

        drawString(cn, HUD_X, HUD_Y, "Time : " + (ticks / 20), labelColor);
        drawString(cn, HUD_X, HUD_Y + 2, "P.Score : " + score, new TextAttributes(Color.GREEN, Color.BLACK));
        drawString(cn, HUD_X, HUD_Y + 3, "P.Life  : " + health, healthColor);
        drawString(cn, HUD_X, HUD_Y + 4, "P.Laser : " + ammo, new TextAttributes(Color.YELLOW, Color.BLACK));
        drawString(cn, HUD_X, HUD_Y + 6, "C.Score : " + computerScore, new TextAttributes(Color.RED, Color.BLACK));
        drawString(cn, HUD_X, HUD_Y + 7, "C-Robots: " + cRobots, labelColor);
        drawString(cn, HUD_X, HUD_Y + 8, "X-Robots: " + xRobots, labelColor);
        drawString(cn, HUD_X, HUD_Y + 10, "[SPACE] Shoot", hudColor);
        drawString(cn, HUD_X, HUD_Y + 11, "[M] Toggle Mode", hudColor);
        drawString(cn, HUD_X, HUD_Y + 12, "[ESC] Save/Quit", hudColor);
    }

    public void drawGameOver(Console cn) {
        drawString(cn, HUD_X, HUD_Y + 15, "GAME OVER!", new TextAttributes(Color.RED, Color.BLACK));
        drawString(cn, HUD_X, HUD_Y + 16, "Final Score: " + score, new TextAttributes(Color.YELLOW, Color.BLACK));
        drawString(cn, HUD_X, HUD_Y + 18, "Press ESC to exit", new TextAttributes(Color.WHITE, Color.BLACK));
    }

    private void clearHUDArea(Console cn) {
        for (int row = HUD_Y; row < HUD_Y + 20; row++)
            for (int col = HUD_X; col < HUD_X + 30; col++)
                cn.getTextWindow().output(col, row, ' ');
    }

    private void drawString(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }
}
