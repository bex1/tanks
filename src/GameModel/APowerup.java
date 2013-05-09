package GameModel;

import GameUtilities.Commands;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Per
 */
public abstract class APowerup implements IPowerup {
    
    private Vector3f position;
    private Quaternion rotation;
    private boolean isHeldByPlayer;
    private boolean isInWorld;

    private static final float MASS = 10f;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /**
     *
     */
    public APowerup(){
        position = Vector3f.ZERO;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void showInWorld(){
        boolean wasInWorld = isInWorld;
        isInWorld = true;
        pcs.firePropertyChange(Commands.SHOW, wasInWorld, isInWorld);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void hideFromWorld() {
        boolean wasInWorld = isInWorld;
        isInWorld = false;
        pcs.firePropertyChange(Commands.HIDE, wasInWorld, isInWorld);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void powerupWasPickedUp() {
        this.setHeldByPlayer(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getMass(){
        return MASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3f getPosition(){
        return position.clone();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObserver(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(Vector3f position) {
        this.position = position.clone();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHeldByPlayer() {
        return isHeldByPlayer;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShownInWorld() {
        return isInWorld;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeldByPlayer(boolean held) {
        isHeldByPlayer = held;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override 
    public void cleanup() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(float tpf) {
    }      
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void usePowerup(IPlayer player) {
        setHeldByPlayer(false);
    }
}
