package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class ModeMenuUI {

    private static final Color BG = new Color(20, 20, 40);
    private static final Color SELECTED = new Color(180, 150, 255);
    private static final Color UNSELECTED = new Color(100, 95, 130);
    private static final Color TITLE = new Color(150, 210, 255);
    private static final Color HINT = new Color(170, 165, 200);

    private void drawText(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }

    public void drawMenu(Console cn, int selectedOption, int startX, int startY) {
        TextAttributes titleAttr = new TextAttributes(TITLE, BG);
        TextAttributes selAttr   = new TextAttributes(SELECTED, BG);
        TextAttributes unselAttr = new TextAttributes(UNSELECTED, BG);
        TextAttributes hintAttr  = new TextAttributes(HINT, BG);

        drawText(cn, startX, startY, "   --- GAME MODE SELECTION ---   ", titleAttr);
        drawText(cn, startX, startY + 3, selectedOption == 1 ? "       >> Randomized Map <<       " : "          Randomized Map          ", selectedOption == 1 ? selAttr : unselAttr);
        drawText(cn, startX, startY + 5, selectedOption == 2 ? "        >>  Export Map  <<        " : "            Export Map            ", selectedOption == 2 ? selAttr : unselAttr);
        drawText(cn, startX - 5, startY + 8, "---------------------------------------", hintAttr);
        drawText(cn, startX - 5, startY + 9, " [UP/DOWN] to navigate, [ENTER] to select ", hintAttr);
    }
}
