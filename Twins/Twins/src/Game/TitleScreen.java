package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class TitleScreen {

    private static final Color BG = new Color(20, 20, 40);

    private final String[] logoLines = {
        " ___________   __          __   _____   ____  _____     _______ ",
        "|           | |  |        |  | |     | |    \\|     |   |       |",
        "|____   ____| |  |   __   |  | |     | |           |   |   ____|",
        "     | |      |  |  |  |  |  | |     | |           |   |  |___  ",
        "     | |      |  |__|  |__|  | |     | |     |\\    |   |___   | ",
        "     | |      |              | |     | |     | \\   |    ___|  | ",
        "     |_|      |______________| |_____| |_____|  \\__|   |______| "
    };

    private final Color[] lineColors = {
        new Color(180, 150, 255),
        new Color(160, 180, 255),
        new Color(150, 210, 255),
        new Color(150, 230, 220),
        new Color(160, 240, 190),
        new Color(200, 230, 160),
        new Color(240, 220, 160)
    };

    public void drawLogo(Console cn, int startX, int startY) {
        for (int i = 0; i < logoLines.length; i++) {
            TextAttributes attr = new TextAttributes(lineColors[i], BG);
            for (int j = 0; j < logoLines[i].length(); j++)
                cn.getTextWindow().output(startX + j, startY + i, logoLines[i].charAt(j), attr);
        }
    }
}
