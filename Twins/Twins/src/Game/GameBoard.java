package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class GameBoard {

    private char[][] map;
    private int mapWidth;
    private int mapHeight;

    public GameBoard(int width, int height) {
        mapWidth = width;
        mapHeight = height;
        map = new char[height][width];
        MazeGenerator mazer = new MazeGenerator(map);
        map = mazer.initializeMap();
    }

    public GameBoard(char[][] loadedMap) {
        this.mapHeight = loadedMap.length;
        this.mapWidth  = loadedMap[0].length;
        this.map       = loadedMap;
    }

    public void printBoard(Console console) {
        TextAttributes wallColor  = new TextAttributes(Color.WHITE, Color.WHITE);
        TextAttributes emptyColor = new TextAttributes(Color.BLACK, Color.BLACK);

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                int screenX = (j * 2) + 4;
                int screenY = i + 2;
                TextAttributes c = (map[i][j] == '#') ? wallColor : emptyColor;
                console.getTextWindow().output(screenX, screenY, ' ', c);
                console.getTextWindow().output(screenX + 1, screenY, ' ', c);
            }
        }
    }

    public char[][] getMap() { return map; }
}
