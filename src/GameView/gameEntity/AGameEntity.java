package GameView.gameEntity;

import application.TanksAppAdapter;
import GameControllers.gameManagers.GraphicManager;
import model.IWorldObject;
import utilities.Commands;
import utilities.Constants;
import GameView.graphics.EGraphics;
import com.jme3.bounding.BoundingBox;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

/**
 * An abstract game entity.
 * 
 * @author Daniel
 */
public abstract class AGameEntity implements IGameEntity {
    final Spatial spatial;
    final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final IWorldObject worldObject;

    /**
     * Creates a game enity with a loaded spatial.
     * 
     * @param graphic The graphical objekt to be loaded.
     */
    AGameEntity(IWorldObject worldObject, EGraphics graphic) {
        this.worldObject = worldObject;
        
        spatial = GraphicManager.INSTANCE.createSpatial(graphic);
        spatial.setShadowMode(RenderQueue.ShadowMode.Cast);
        
        spatial.setUserData(Constants.USER_DATA_MODEL, worldObject);
   
        worldObject.addObserver(this);
        hideFromWorld();
        TanksAppAdapter.INSTANCE.attachChildToRootNode(spatial);
    }

    /**
     * Adds an appropriate control to this view.
     * @param control 
     */
    @Override
    public final void addControl(Control control) {
        spatial.addControl(control);
    }
    
    /**
     * Removes a control of this view.
     */
    @Override
    public final void removeControl(Control control) {
        spatial.removeControl(control);
    }

    /**
     * Returns the spatial of this game entity.
     * 
     * @return The spatial of this game entity.
     */
    @Override
    public final Spatial getSpatial() {
        return spatial;
    }
    
    /**
     * Help metohd used to get the boundingbox of the spatial.
     * @return 
     */
    final Vector3f getExtents() {
        return ((BoundingBox) spatial.getWorldBound()).getExtent(null);
    }
    
    /**
     * Shows the effects that this methods gets in, at the position given.
     * 
     * @param effects The effects that gets shown
     * @param position The position at which the effects get shown
     */
    synchronized void showEffects(Collection<ParticleEmitter> effects, Vector3f position) {
        if (spatial.getParent() != null) {
            for (ParticleEmitter effect : effects) {
                if (effect != null) {
                    effect.setLocalTranslation(position);
                    spatial.getParent().attachChild(effect);
                    effect.emitAllParticles();
                }
            }
        }
    }
    
    /**
     * Hides the given effects.
     * 
     * @param effects The effects to be hidden
     */
    synchronized void hideEffects(Collection<ParticleEmitter> effects) {
        if (spatial.getParent() != null) {
            for (ParticleEmitter effect : effects) {
                if (effect != null) {
                    spatial.getParent().detachChild(effect);
                }
            }
        }
    }
    
    @Override
    public final void showInWorld() {
        spatial.setCullHint(Spatial.CullHint.Dynamic);
    }
    
    @Override
    public final void hideFromWorld() {
        spatial.setCullHint(Spatial.CullHint.Always);
    }
    
    /**
     * @inheritdoc 
     */
    @Override
    public final void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * @inheritdoc
     */
    @Override
    public final void removeObserver(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    /**
     * @inheritdoc
     */
    @Override
    public void cleanup() {
        TanksAppAdapter.INSTANCE.detachChildFromRootNode(spatial);
        worldObject.removeObserver(this);
    } 
    
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Commands.SHOW)) {
            showInWorld();
            updatePosition();
            updateRotation();
        } else if (evt.getPropertyName().equals(Commands.ROTATE)) {
            spatial.setLocalRotation((Quaternion)evt.getNewValue());
        } else if (evt.getPropertyName().equals(Commands.CLEANUP)) {
            cleanup();
        } else if (evt.getPropertyName().equals(Commands.HIDE)) {
            hideFromWorld();
        }
        pcs.firePropertyChange(evt);
    }
    
    void updatePosition() {
        spatial.setLocalTranslation(worldObject.getPosition());
    }
    
    void updateRotation() {
        spatial.setLocalRotation(worldObject.getRotation());
    }
}
