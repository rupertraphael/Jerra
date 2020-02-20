/**
 * Entity
 */
public interface Entity extends Updatable {

    public Presence getPresence();
    public void setPresence(Presence presence);

    public Rect getPosition();

    public boolean collides(Entity other);

    public void update(String command);
    public String toString();

    public boolean alive();
    
}