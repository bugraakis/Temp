package Game;

import java.util.Random;

public class GameInputSystem {

    private Random rnd = new Random();
    private RandomSpawner spawner = new RandomSpawner();

    public void spawnElement(char[][] map, TreasureManager tm, LaserManager lm, EnemyManager em) {
        int roll = rnd.nextInt(11);
        int[] pos = spawner.getSpawnPoint(map);

        if (roll < 2) tm.addTreasure(pos[0], pos[1], 1);
        else if (roll < 4) tm.addTreasure(pos[0], pos[1], 2);
        else if (roll < 6) tm.addTreasure(pos[0], pos[1], 3);
        else if (roll < 9) lm.spawnPackedLaser(map, spawner);
        else if (roll < 10) em.addCRobot(pos[0], pos[1]);
        else em.addXRobot(pos[0], pos[1]);
    }
}
