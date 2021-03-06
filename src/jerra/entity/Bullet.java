package jerra.entity;

import jerra.core.Rect;
import jerra.core.Vector;

import jerra.presence.Presence;
import jerra.presence.DefaultPresence;

/**
 * Bullet
 */
public class Bullet extends Projectile {

    /**
     * Constructs a Presence based on position and velocity
     * Bullets have a fixed lifetime
     */
    private static Presence constructPresence(Rect position, Vector velocity) {
        return new DefaultPresence(position, velocity);
    }

    public Bullet(Rect position, Vector velocity, int lifetime, char team) {
        // Create projectile with predetermined presence
        super(constructPresence(position, velocity), lifetime);
        this.setTeam(team);
    }

    @Override
    public Entity copy() {
        return new Bullet(this.getPosition(), this.getPresence().getVelocity(), this.lifetime, this.getTeam());
    }

    @Override
    public String getName() {
        return "Bullet (" + this.age + ")";
    }

    @Override
    public void interact(Entity other) {
        // Only interact if not friendly
        if (!this.friendly(other)) {
            // Kill other entity
            other.kill(true);
            // Kill this (remove for penetrating)
            this.kill(true);
        }
    }

}
