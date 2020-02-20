/**
 * Entity
 */
public class DefaultEntity implements Entity {

    private Presence presence;

    public DefaultEntity(Presence presence) {
        this.presence = presence;
    }

    public String getName() {
        return "ENTITY";
    }

    public Presence getPresence() {
        return this.presence;
    }

    public Rect getPosition() {
        return this.presence.getPosition();
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    public boolean collides(Entity other) {
        return this.getPresence().collides(other.getPresence());
    }

    public void update(String command) {
        this.presence.update(command);
    }

    public String toString() {
        return this.getName() + ": " + this.presence.toString();
    }

    public boolean alive() {
        return true;
    }

}
