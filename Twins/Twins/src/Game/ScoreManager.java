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

    private static final Color BG = new Color(20, 20, 40);
    private static final Color LABEL = new Color(200, 195, 220);
    private static final Color SCORE_P = new Color(150, 255, 200);
    private static final Color AMMO_C = new Color(255, 220, 130);
    private static final Color ENEMY_C = new Color(255, 150, 150);
    private static final Color HP_HIGH = new Color(130, 230, 170);
    private static final Color HP_MID = new Color(255, 220, 130);
    private static final Color HP_LOW = new Color(255, 140, 140);
    private static final Color HUD_KEY = new Color(170, 160, 220);
    private static final Color GAMEOVER = new Color(255, 130, 150);
    private static final Color FINAL_SC = new Color(255, 230, 160);

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
        Color hpColor;
        if (health > 600) hpColor = HP_HIGH;
        else if (health > 300) hpColor = HP_MID;
        else hpColor = HP_LOW;

        clearHUDArea(cn);

        drawString(cn, HUD_X, HUD_Y, "Time : " + (ticks / 20), LABEL);
        drawString(cn, HUD_X, HUD_Y + 2, "P.Score : " + score, SCORE_P);
        drawString(cn, HUD_X, HUD_Y + 3, "P.Life  : " + health, hpColor);
        drawString(cn, HUD_X, HUD_Y + 4, "P.Laser : " + ammo, AMMO_C);
        drawString(cn, HUD_X, HUD_Y + 6, "C.Score : " + computerScore, ENEMY_C);
        drawString(cn, HUD_X, HUD_Y + 7, "C-Robots: " + cRobots, LABEL);
        drawString(cn, HUD_X, HUD_Y + 8, "X-Robots: " + xRobots, LABEL);
        drawString(cn, HUD_X, HUD_Y + 10, "[SPACE] Shoot", HUD_KEY);
        drawString(cn, HUD_X, HUD_Y + 11, "[M] Toggle Mode", HUD_KEY);
        drawString(cn, HUD_X, HUD_Y + 12, "[ESC] Save/Quit", HUD_KEY);
    }

    public void drawGameOver(Console cn) {
        drawString(cn, HUD_X, HUD_Y + 15, "GAME OVER!", GAMEOVER);
        drawString(cn, HUD_X, HUD_Y + 16, "Final Score: " + score, FINAL_SC);
        drawString(cn, HUD_X, HUD_Y + 18, "Press ESC to exit", LABEL);
    }

    private void clearHUDArea(Console cn) {
        TextAttributes bgAttr = new TextAttributes(BG, BG);
        for (int row = HUD_Y; row < HUD_Y + 20; row++)
            for (int col = HUD_X; col < HUD_X + 30; col++)
                cn.getTextWindow().output(col, row, ' ', bgAttr);
    }

    private void drawString(Console cn, int x, int y, String text, Color fg) {
        TextAttributes attr = new TextAttributes(fg, BG);
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }
}
