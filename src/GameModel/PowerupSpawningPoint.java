
package GameModel;

import com.jme3.math.Vector3f;

/**
 * An IPowerups spawning point.
 * 
 * @author perthoresson
 */
public class PowerupSpawningPoint implements ISpawningPoints {
    
    private boolean inUse;
    
    private Vector3f position;

    /**
     * Instantiates the object.
     * 
     * @param inUse if the spawning point is in use or not.
     */
    public PowerupSpawningPoint (boolean inUse, Vector3f position){
        this.inUse = inUse;
        this.position = position;
    }
    
    /**
     * Returns the boolean isInUse.
     * @return true if it is in use, false if it's not.
     */
    @Override
    public boolean isInUse() {
        return inUse;
    }
    
    /**
     * {@inheritdoc} 
     */
    @Override
    public void setInUse(boolean inUse){
        this.inUse = inUse;
    }

    /**
     * {@inheritdoc} 
     */
    @Override
    public Vector3f getPosition() {
        return position;
    }
    
    /**
     * Returns a string representation of the object, based on position and inUse.
     * 
     * @return inUse and position in the format: PlayerSpawningPoint{inUse=xxxx,
     *  position=xxxf}
     */
    @Override
    public String toString() {
        return "PlayerSpawningPoint{" + "inUse=" + inUse + ", "
                + "position=" + position + '}';
    }
    
    /**
     * Returns hash code based on the position.
     * 
     * @return hash code based on the position.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.position != null ? this.position.hashCode() : 0);
        return hash;
    }
    
    /**
     * Equals method that compares the position of the PlayerSpawningPoint.
     *
     * @param obj the reference object with which to compare.
     * @return true if this PlayerSpawningPoint is the same as the obj argument
     * ; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PowerupSpawningPoint other = (PowerupSpawningPoint) obj;
        if (this.position != other.position && (this.position == null || !this.position.equals(other.position))) {
            return false;
        }
        return true;
    }
}