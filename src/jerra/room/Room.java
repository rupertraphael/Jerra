package jerra.room;

import java.util.Set;

import jerra.api.Updatable;
import jerra.api.Interactive;

import jerra.entity.Entity;
import jerra.entity.Spawner;
import jerra.entity.Player;
import jerra.entity.Shooter;

/**
 * Room
 */
public interface Room extends Updatable, Interactive {

    /**
     * Spawns (inserts) a general Entity in the Room
     * @param entity The Entity that the Room will now track (as a general Entity)
     */
    public void spawnEntity(Entity entity);
    /**
     * Spawns (inserts) a general Spawner in the Room
     * @param spawner The Spawner that the Room will now tack (as a general Spawner)
     */
    public void spawnSpawner(Spawner<Entity> spawner);
    /**
     * Spawns (inserts) a Player in the Room, allowing the Room to have finer control
     * @param player The Player that the Room will now track (as a Player)
     */
    public void spawnPlayer(Player player);
    /**
     * Spawns (insets) a Shooter in the room, to be tracked as an Entity and Spawner
     * @param shooter the Shooter the room will now track
     */
    public void spawnShooter(Shooter spawner);
    /**
     * Spawns (inserts) a Shooter spawner in the Room
     * @param spawner The Spawner that the Room will now track, adding its produces as Shooters
     */
    public void spawnShooterSpawner(Spawner<Shooter> spawner);
    
    public Set<Entity> getEntities();

    @Override
    public String toString();

}