package Game;

import java.util.Random;

public class GameInputSystem {

    private Random rnd = new Random();
    private RandomSpawner spawner = new RandomSpawner();

    // Spawn one random element based on probability table
    // 1: 2/11, 2: 2/11, 3: 2/11, @: 3/11, C: 1/11, X: 1/11
    public void spawnElement(char[][] map, TreasureManager tm, LaserManager lm, EnemyManager em) {
        int roll = rnd.nextInt(11);
        int[] pos = spawner.getSpawnPoint(map);

        if (roll < 2) {
            // Treasure 1
            tm.addTreasure(pos[0], pos[1], 1);
        } else if (roll < 4) {
            // Treasure 2
            tm.addTreasure(pos[0], pos[1], 2);
        } else if (roll < 6) {
            // Treasure 3
            tm.addTreasure(pos[0], pos[1], 3);
        } else if (roll < 9) {
            // Packed laser @
            lm.spawnPackedLaser(map, spawner);
        } else if (roll < 10) {
            // C-Robot
            em.addCRobot(pos[0], pos[1]);
        } else {
            // X-Robot
            em.addXRobot(pos[0], pos[1]);
        }
    }
}
