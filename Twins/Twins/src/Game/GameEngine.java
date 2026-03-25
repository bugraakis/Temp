package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameEngine {
    private Console cn;
    private GameBoard board;
    private GameControls controls;
    private Timer timer;
    private TrailManager trailManager;
    private EnemyManager enemyManager;
    private BModeManager modeManager;
    private BCharacter twin;
    private MoveTrack tracker;
    private ModeMenuUI modeMenu;
    private TitleScreen gameLogo;
    private LaserManager laserManager;
    private ScoreManager scoreManager;
    private TreasureManager treasureManager;
    private GameInputSystem inputSystem;
    private int px, py;
    private int selectedModeOption = 1;
    private boolean firstLaunch = true;

    private static final Color PLAYER_COLOR = new Color(130, 230, 170);
    private static final Color FLOOR_BG = new Color(30, 28, 50);
    private static final Color BG = new Color(20, 20, 40);

    public GameEngine() throws Exception {
        cn = Enigma.getConsole("Twins - Maze Game", 200, 50, 12);
        controls = new GameControls(cn);
        timer = new Timer();
        trailManager = new TrailManager();
        enemyManager = new EnemyManager();
        modeManager = new BModeManager();
        tracker = new MoveTrack();
        modeMenu = new ModeMenuUI();
        gameLogo = new TitleScreen();
        laserManager = new LaserManager();
        scoreManager = new ScoreManager();
        treasureManager = new TreasureManager();
        inputSystem = new GameInputSystem();
    }

    private void clearScreen() {
        TextAttributes bgAttr = new TextAttributes(BG, BG);
        for (int y = 0; y < 50; y++)
            for (int x = 0; x < 200; x++)
                cn.getTextWindow().output(x, y, ' ', bgAttr);
    }

    private void drawText(int x, int y, String text) {
        TextAttributes attr = new TextAttributes(new Color(200, 195, 220), BG);
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }

    private void drawColorText(int x, int y, String text, Color fg) {
        TextAttributes attr = new TextAttributes(fg, BG);
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }

    private void showSizeConfirmation() throws InterruptedException {
        clearScreen();
        Color title = new Color(180, 150, 255);
        Color info = new Color(150, 210, 255);
        Color hint = new Color(170, 165, 200);
        Color accent = new Color(160, 240, 190);

        drawColorText(75, 12, "--- CONSOLE SETUP ---", title);
        drawColorText(68, 15, "Please resize your console window to:", info);
        drawColorText(78, 17, "200 columns x 50 rows", accent);
        drawColorText(79, 18, "Font size: 12", accent);
        drawColorText(65, 21, "Make sure the window fits your screen properly.", hint);
        drawColorText(70, 23, "Press [ENTER] when ready to continue", hint);

        while (controls.consumeKey() != KeyEvent.VK_ENTER) Thread.sleep(50);
        clearScreen();
    }

    private void drawAll(int currentTick) {
        cn.getTextWindow().output((px * 2) + 4, py + 2, 'A', new TextAttributes(PLAYER_COLOR, FLOOR_BG));
        twin.draw(cn, px, py);
        enemyManager.drawRobots(cn);
        treasureManager.drawTreasures(cn);
        laserManager.drawLasers(cn);
        laserManager.drawPacks(cn);
        scoreManager.drawHUD(cn, laserManager.getAmmo(), enemyManager.getComputerScore(),
                enemyManager.getCRobotCount(), enemyManager.getRobotCount(), currentTick);
    }

    private void resetState() {
        enemyManager = new EnemyManager();
        timer = new Timer();
        trailManager = new TrailManager();
        modeManager = new BModeManager();
        tracker = new MoveTrack();
    }

    public void start() throws InterruptedException, IOException {
        if (firstLaunch) {
            showSizeConfirmation();
            firstLaunch = false;
        }

        while (true) {
            boolean inMenu = true;
            int lastOption = 0;
            selectedModeOption = 1;

            while (inMenu) {
                if (selectedModeOption != lastOption) {
                    gameLogo.drawLogo(cn, 67, 10);
                    modeMenu.drawMenu(cn, selectedModeOption, 80, 20);
                    lastOption = selectedModeOption;
                }
                int key = controls.consumeKey();
                if (key != 0) {
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                        selectedModeOption = (selectedModeOption == 1) ? 2 : 1;
                    else if (key == KeyEvent.VK_ENTER) inMenu = false;
                }
                Thread.sleep(50);
            }

            clearScreen();
            laserManager = new LaserManager();
            scoreManager = new ScoreManager();
            treasureManager = new TreasureManager();
            inputSystem = new GameInputSystem();
            RandomSpawner spawner = new RandomSpawner();

            if (selectedModeOption == 1) {
                LoadingScreen loading = new LoadingScreen();
                loading.show(cn);
                board = new GameBoard(55, 25);
                int[] playerSpawn = spawner.getSpawnPoint(board.getMap());
                px = playerSpawn[0]; py = playerSpawn[1];
                twin = new BCharacter(px, py);
                for (int i = 0; i < 10; i++)
                    inputSystem.spawnElement(board.getMap(), treasureManager, laserManager, enemyManager);
            } else {
                if (!SaveLoad.saveExists()) {
                    drawText(70, 22, "No old maps please change your option");
                    drawText(70, 24, "Press ESC");
                    while (controls.consumeKey() != KeyEvent.VK_ESCAPE) Thread.sleep(50);
                    clearScreen();
                    resetState();
                    continue;
                }

                int[] result = new int[7];
                int[] xRobotX = new int[100], xRobotY = new int[100], xRobotLife = new int[100];
                char[][] loadedMap = new char[25][55];
                SaveLoad.loadGame(result, xRobotX, xRobotY, xRobotLife, loadedMap);

                px = result[0]; py = result[1];
                twin = new BCharacter(result[2], result[3]);
                modeManager.setMode(result[4]);
                timer.setTicks(result[5]);
                board = new GameBoard(loadedMap);
                for (int i = 0; i < result[6]; i++)
                    enemyManager.addXRobot(xRobotX[i], xRobotY[i], xRobotLife[i]);
                for (int i = 0; i < 10; i++)
                    inputSystem.spawnElement(board.getMap(), treasureManager, laserManager, enemyManager);
            }

            board.printBoard(cn);
            drawAll(timer.getTicks());
            boolean gameOver = false;

            while (true) {
                timer.Play();
                int currentTick = timer.getTicks();
                int key = controls.consumeKey();
                int nextX = px, nextY = py, moveX = 0, moveY = 0;
                boolean playerMoved = false;

                if (key != 0) {
                    if (key == KeyEvent.VK_ESCAPE) {
                        if (!gameOver) {
                            int rc = enemyManager.getRobotCount();
                            int[] rxArr = new int[rc], ryArr = new int[rc], rLifeArr = new int[rc];
                            for (int i = 0; i < rc; i++) {
                                rxArr[i] = enemyManager.getRobotX(i);
                                ryArr[i] = enemyManager.getRobotY(i);
                                rLifeArr[i] = enemyManager.getRobotLife(i);
                            }
                            SaveLoad.saveGame(px, py, twin.getX(), twin.getY(), modeManager.getMode(),
                                    currentTick, rxArr, ryArr, rLifeArr, rc, board.getMap());
                        }
                        resetState();
                        clearScreen();
                        break;
                    }

                    if (gameOver) continue;

                    if (key == KeyEvent.VK_M || key == 'm' || key == 'M') modeManager.toggleMode();
                    if (key == KeyEvent.VK_SPACE) laserManager.fireLaser(px, py, twin.getX(), twin.getY(), currentTick);

                    if (key == KeyEvent.VK_LEFT)       { nextX--; moveX = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_RIGHT)  { nextX++; moveX =  1; playerMoved = true; }
                    else if (key == KeyEvent.VK_UP)     { nextY--; moveY = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_DOWN)   { nextY++; moveY =  1; playerMoved = true; }

                    tracker.setLastMove(moveX, moveY);
                    CollisionControl cd = new CollisionControl();
                    if (playerMoved && cd.canPlayerMove(board.getMap(), nextX, nextY, enemyManager)) {
                        trailManager.addTrail(px, py, currentTick);
                        px = nextX; py = nextY;
                    }
                    if (playerMoved && (moveX != 0 || moveY != 0))
                        twin.move(tracker, board, modeManager.getMode(), trailManager, currentTick, enemyManager);

                    laserManager.checkPickups(px, py, cn);
                    laserManager.checkPickupB(twin.getX(), twin.getY(), cn);
                    int tp = treasureManager.checkPlayerPickup(px, py, cn);
                    if (tp > 0) scoreManager.addScore(tp);
                    tp = treasureManager.checkPlayerPickup(twin.getX(), twin.getY(), cn);
                    if (tp > 0) scoreManager.addScore(tp);
                }

                if (!gameOver) {
                    laserManager.updateFiring(board.getMap(), currentTick);
                    int kills = laserManager.checkNeighborDamage(enemyManager, cn);
                    if (kills > 0) scoreManager.addScore(100 * kills);
                    laserManager.cleanExpiredLasers(cn, currentTick);

                    if (timer.isRobotTurn())
                        enemyManager.moveRobots(board, trailManager, currentTick, px, py, twin, treasureManager);
                    if (enemyManager.isAdjacentToPlayer(px, py)) scoreManager.takeDamage(50);

                    if (!scoreManager.isAlive()) {
                        gameOver = true;
                        scoreManager.drawGameOver(cn);
                    }

                    if (currentTick % 20 == 0 && currentTick > 0)
                        inputSystem.spawnElement(board.getMap(), treasureManager, laserManager, enemyManager);
                }

                trailManager.clearOldTrails(cn, currentTick);
                drawAll(currentTick);
            }
        }
    }
}
