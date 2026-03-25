package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class LoadingScreen {

    private static final Color BG = new Color(20, 20, 40);
    private static final Color WALL_FG = new Color(140, 120, 180);
    private static final Color WALL_BG = new Color(80, 70, 110);
    private static final Color PLAYER_FG = new Color(130, 230, 170);
    private static final Color FLOOR_BG = new Color(30, 28, 50);
    private static final Color SPARKLE = new Color(255, 220, 150);
    private static final Color TITLE_1 = new Color(180, 150, 255);
    private static final Color TITLE_2 = new Color(150, 200, 255);
    private static final Color BAR_FILL = new Color(160, 220, 180);
    private static final Color BAR_EMPTY = new Color(60, 55, 80);
    private static final Color TREASURE_1 = new Color(255, 240, 160);
    private static final Color TREASURE_2 = new Color(160, 210, 255);
    private static final Color TREASURE_3 = new Color(220, 170, 255);
    private static final Color DOT_COLOR = new Color(200, 190, 230);
    private static final Color DOOR_COLOR = new Color(180, 140, 100);

    private static final int RX = 70;
    private static final int RY = 15;
    private static final int RW = 40;
    private static final int RH = 12;
    private static final int DOOR_Y = RY + 5;

    public void show(Console cn) throws InterruptedException {
        clearScreen(cn);
        drawTitle(cn);
        drawRoom(cn);
        drawTreasuresInRoom(cn);

        int playerX = RX + 2;
        int playerY = RY + 6;
        int dir = 1;
        int totalFrames = 200;

        int[] sparkleX = new int[5];
        int[] sparkleY = new int[5];
        int[] sparkleLife = new int[5];

        for (int frame = 0; frame < totalFrames; frame++) {
            TextAttributes floorAttr = new TextAttributes(FLOOR_BG, FLOOR_BG);
            cn.getTextWindow().output(playerX, playerY, ' ', floorAttr);

            if (frame % 2 == 0) {
                for (int s = 4; s > 0; s--) {
                    sparkleX[s] = sparkleX[s - 1];
                    sparkleY[s] = sparkleY[s - 1];
                    sparkleLife[s] = sparkleLife[s - 1] - 1;
                }
                sparkleX[0] = playerX;
                sparkleY[0] = playerY;
                sparkleLife[0] = 4;
            }

            playerX += dir;
            if (playerX >= RX + RW - 2) { dir = -1; playerX = RX + RW - 2; }
            if (playerX <= RX + 1) { dir = 1; playerX = RX + 1; }

            if (frame % 40 < 10) playerY = RY + 4;
            else if (frame % 40 < 20) playerY = RY + 6;
            else if (frame % 40 < 30) playerY = RY + 8;
            else playerY = RY + 6;

            char pChar = (frame % 4 < 2) ? 'A' : 'a';
            cn.getTextWindow().output(playerX, playerY, pChar, new TextAttributes(PLAYER_FG, FLOOR_BG));

            for (int s = 0; s < 5; s++) {
                if (sparkleLife[s] > 0 && !(sparkleX[s] == playerX && sparkleY[s] == playerY)) {
                    char sc = (sparkleLife[s] > 2) ? '*' : '.';
                    cn.getTextWindow().output(sparkleX[s], sparkleY[s], sc, new TextAttributes(SPARKLE, FLOOR_BG));
                } else if (sparkleX[s] > 0) {
                    cn.getTextWindow().output(sparkleX[s], sparkleY[s], ' ', floorAttr);
                }
            }

            drawLoadingBar(cn, frame, totalFrames);

            int dots = (frame / 8) % 4;
            String loadText = "Loading" + "...".substring(0, dots) + "   ".substring(0, 3 - dots);
            drawString(cn, RX + RW / 2 - 5, RY + RH + 3, loadText, new TextAttributes(DOT_COLOR, BG));

            Thread.sleep(50);
        }

        drawString(cn, RX + RW / 2 - 5, RY + RH + 3, "Ready!    ", new TextAttributes(BAR_FILL, BG));
        Thread.sleep(600);

        playExitAnimation(cn, playerX, playerY);
        clearScreen(cn);
    }

    private void playExitAnimation(Console cn, int playerX, int playerY) throws InterruptedException {
        TextAttributes floorAttr = new TextAttributes(FLOOR_BG, FLOOR_BG);
        TextAttributes wallAttr = new TextAttributes(WALL_FG, WALL_BG);
        TextAttributes doorAttr = new TextAttributes(DOOR_COLOR, FLOOR_BG);

        int doorX = RX + RW;

        cn.getTextWindow().output(doorX, DOOR_Y, '|', doorAttr);
        cn.getTextWindow().output(doorX, DOOR_Y + 1, '|', doorAttr);
        cn.getTextWindow().output(doorX, DOOR_Y + 2, '|', doorAttr);
        Thread.sleep(300);

        cn.getTextWindow().output(doorX, DOOR_Y, '/', doorAttr);
        cn.getTextWindow().output(doorX, DOOR_Y + 1, ' ', new TextAttributes(FLOOR_BG, FLOOR_BG));
        cn.getTextWindow().output(doorX, DOOR_Y + 2, ' ', new TextAttributes(FLOOR_BG, FLOOR_BG));
        Thread.sleep(300);

        int targetY = DOOR_Y + 1;
        while (playerY != targetY) {
            cn.getTextWindow().output(playerX, playerY, ' ', floorAttr);
            if (playerY < targetY) playerY++;
            else playerY--;
            cn.getTextWindow().output(playerX, playerY, 'A', new TextAttributes(PLAYER_FG, FLOOR_BG));
            Thread.sleep(60);
        }

        while (playerX < doorX + 3) {
            cn.getTextWindow().output(playerX, playerY, ' ', floorAttr);
            playerX++;

            if (playerX <= doorX) {
                cn.getTextWindow().output(playerX, playerY, 'A', new TextAttributes(PLAYER_FG, FLOOR_BG));
            } else if (playerX == doorX + 1) {
                cn.getTextWindow().output(playerX, playerY, 'A', new TextAttributes(PLAYER_FG, BG));
            } else {
                cn.getTextWindow().output(playerX, playerY, 'a', new TextAttributes(new Color(80, 140, 100), BG));
            }
            Thread.sleep(80);
        }

        cn.getTextWindow().output(playerX, playerY, ' ', new TextAttributes(BG, BG));
        Thread.sleep(200);

        cn.getTextWindow().output(doorX, DOOR_Y, '#', wallAttr);
        cn.getTextWindow().output(doorX, DOOR_Y + 1, '#', wallAttr);
        cn.getTextWindow().output(doorX, DOOR_Y + 2, '#', wallAttr);
        Thread.sleep(400);

        drawString(cn, RX + RW / 2 - 8, RY + RH + 3, "Entering the maze...", new TextAttributes(DOT_COLOR, BG));
        Thread.sleep(1200);
    }

    private void drawTitle(Console cn) {
        String[] title = {
            " _____          _            ",
            "|_   _|_      _(_)_ __  ___  ",
            "  | | \\ \\ /\\ / / | '_ \\/ __| ",
            "  | |  \\ V  V /| | | | \\__ \\ ",
            "  |_|   \\_/\\_/ |_|_| |_|___/ "
        };
        Color[] colors = {TITLE_1, TITLE_2, TITLE_1, TITLE_2, TITLE_1};
        for (int i = 0; i < title.length; i++)
            drawString(cn, RX + 5, RY - 7 + i, title[i], new TextAttributes(colors[i], BG));
    }

    private void drawRoom(Console cn) {
        TextAttributes wallAttr = new TextAttributes(WALL_FG, WALL_BG);
        TextAttributes floorAttr = new TextAttributes(FLOOR_BG, FLOOR_BG);

        for (int x = RX; x <= RX + RW; x++) {
            cn.getTextWindow().output(x, RY, '#', wallAttr);
            cn.getTextWindow().output(x, RY + RH, '#', wallAttr);
        }
        for (int y = RY; y <= RY + RH; y++) {
            cn.getTextWindow().output(RX, y, '#', wallAttr);
            cn.getTextWindow().output(RX + RW, y, '#', wallAttr);
        }
        for (int y = RY + 1; y < RY + RH; y++)
            for (int x = RX + 1; x < RX + RW; x++)
                cn.getTextWindow().output(x, y, ' ', floorAttr);
    }

    private void drawTreasuresInRoom(Console cn) {
        cn.getTextWindow().output(RX + 8, RY + 3, '1', new TextAttributes(TREASURE_1, FLOOR_BG));
        cn.getTextWindow().output(RX + 20, RY + 4, '2', new TextAttributes(TREASURE_2, FLOOR_BG));
        cn.getTextWindow().output(RX + 32, RY + 2, '3', new TextAttributes(TREASURE_3, FLOOR_BG));
        cn.getTextWindow().output(RX + 15, RY + 8, '@', new TextAttributes(SPARKLE, FLOOR_BG));
        cn.getTextWindow().output(RX + 28, RY + 9, '@', new TextAttributes(SPARKLE, FLOOR_BG));
        cn.getTextWindow().output(RX + 5, RY + 10, '1', new TextAttributes(TREASURE_1, FLOOR_BG));
        cn.getTextWindow().output(RX + 35, RY + 7, '2', new TextAttributes(TREASURE_2, FLOOR_BG));
    }

    private void drawLoadingBar(Console cn, int frame, int total) {
        int barLen = 30;
        int filled = (frame * barLen) / total;
        int bx = RX + RW / 2 - barLen / 2;
        int by = RY + RH + 1;

        drawString(cn, bx - 1, by, "[", new TextAttributes(DOT_COLOR, BG));
        for (int i = 0; i < barLen; i++) {
            if (i < filled)
                cn.getTextWindow().output(bx + i, by, '=', new TextAttributes(BAR_FILL, BG));
            else
                cn.getTextWindow().output(bx + i, by, '.', new TextAttributes(BAR_EMPTY, BG));
        }
        drawString(cn, bx + barLen, by, "]", new TextAttributes(DOT_COLOR, BG));
    }

    private void clearScreen(Console cn) {
        TextAttributes bgAttr = new TextAttributes(BG, BG);
        for (int y = 0; y < 50; y++)
            for (int x = 0; x < 200; x++)
                cn.getTextWindow().output(x, y, ' ', bgAttr);
    }

    private void drawString(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }
}
