package GameModel;

import GameModel.IExplodingProjectile;
import GameUtilities.Commands;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Model for a tank vehicle.
 * 
 * @author Daniel
 */
public final class TankModel implements IArmedVehicle {
    
    private int health;
    private IArmedVehicle.VehicleState vehicleState;
    private Vector3f position;
    private Vector3f direction;
    private Quaternion rotation;
    
    private boolean isInWorld;
   
    private float steeringValue;
    private float accelerationValue;
    private float steeringChangeValue;
    private float acceleration;
    private float currentVehicleSpeedKmHour;
   
    private final float mass;
    private float currentMaxSpeed;
    private float currentAccelerationForce;
    private final float defaultMaxSpeed;
    private final float defaultAccelerationForce;
    private final float brakeForce;
    private final float frictionForce;
    private final float backMaxSpeed;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private List<CanonBallModel> canonBalls;
    private List<MissileModel> missiles;
    
    /**
     *
     * @param canonBalls
     * @param missiles
     */
    public TankModel(List<CanonBallModel> canonBalls, List<MissileModel> missiles) {
        this.canonBalls = canonBalls;
        this.missiles = missiles;
        mass = 600.0f;
        health = 100;
        defaultMaxSpeed = 80.0f;
        currentMaxSpeed = defaultMaxSpeed;
        backMaxSpeed = 30.0f;
        defaultAccelerationForce = 3000.0f;
        currentAccelerationForce = defaultAccelerationForce;
        brakeForce = 10000.0f;
        frictionForce = 10.0f;
        steeringChangeValue = 0.4f;
        position = Vector3f.ZERO;
        direction = Vector3f.ZERO;
        rotation = Quaternion.ZERO;
    }

    /**
     * @inheritdoc
     */
    @Override
    public IArmedVehicle.VehicleState getVehicleState() {
        return vehicleState;
    }
    
    @Override
    public void applyDamage(int hp) {
        if (hp != 0) {
            int oldHP = health;
            if (health - hp < 0) {
                health = 0;
            } else {
                health -= hp;
            }
            pcs.firePropertyChange(Commands.HEALTH, oldHP, health);
        }
    }

    @Override
    public synchronized void shoot() {
        for (CanonBallModel canonBall : canonBalls) {
            if (!canonBall.isShownInWorld()) {
                canonBall.launchProjectile(getFirePosition(),
                        direction.multLocal(100), rotation);
                pcs.firePropertyChange(Commands.SHOOT, null, null);
                return;
            }
        }
    }
    
    /**
     *
     */
    @Override
    public synchronized void shootMissile() {
        for (MissileModel missile : missiles) {
            if (!missile.isShownInWorld()) {
                missile.launchProjectile(new Vector3f(position).addLocal(0, 4, 0),
                        new Vector3f(0, 10, 0), rotation);
                pcs.firePropertyChange(Commands.SHOOT, null, null);
                return;
            }
        }
    }
    
    @Override
    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    
    @Override
    public void removeObserver(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        float oldAcceleration = accelerationValue;
        float maxSpeed = (acceleration >= 0 ? this.currentMaxSpeed : -backMaxSpeed);
        float speedFactor = (maxSpeed - currentVehicleSpeedKmHour) / maxSpeed;
        accelerationValue = acceleration * speedFactor;
        pcs.firePropertyChange(Commands.ACCELERATE, oldAcceleration, accelerationValue);

        pcs.firePropertyChange(Commands.SMOKE, null, null);
    }

    /**
     *
     */
    @Override
    public void accelerateForward() {
        this.acceleration += currentAccelerationForce;
    }

    /**
     *
     */
    @Override
    public void accelerateBack() {
       this.acceleration -= currentAccelerationForce;
    }

    /**
     *
     */
    @Override
    public void steerLeft() {
        float oldSteeringValue = steeringValue;
        this.steeringValue += steeringChangeValue;
        pcs.firePropertyChange(Commands.STEER, oldSteeringValue, steeringValue);
    }

    /**
     *
     */
    @Override
    public void steerRight() {
        float oldSteeringValue = steeringValue;
        this.steeringValue -= steeringChangeValue;
        pcs.firePropertyChange(Commands.STEER, oldSteeringValue, steeringValue);
    }

    /**
     *
     * @param currentVehicleSpeedKmHour
     */
    @Override
    public void updateCurrentVehicleSpeedKmHour(float currentVehicleSpeedKmHour) {
        this.currentVehicleSpeedKmHour = currentVehicleSpeedKmHour;
    }

    /**
     *
     * @return
     */
    @Override
    public float getMass() {
        return mass;
    }

    /**
     *
     * @param pos
     */
    @Override
    public void updatePosition(Vector3f pos) {
        this.position = new Vector3f(pos);
    }

    /**
     *
     * @return
     */
    @Override
    public synchronized Vector3f getFirePosition() {
        return new Vector3f(position).addLocal(0, 0.9f, 0).addLocal(direction.multLocal(1.5f));
    }
    
    /**
     *
     * @return
     */
    @Override
    public Vector3f getSmokePosition() {
        return new Vector3f(position).addLocal(0, 2.05f, 0).subtractLocal(direction.multLocal(1.5f));
    }

    /**
     *
     * @param forwardVector
     */
    @Override
    public void updateDirection(Vector3f forwardVector) {
        direction = forwardVector.clone();
    }

    /**
     *
     * @return
     */
    @Override
    public Vector3f getDirection() {
        return direction.clone();
    }

    /**
     *
     * @return
     */
    @Override
    public Quaternion getRotation() {
        return rotation.clone();
    }

    /**
     *
     * @param rotation
     */
    @Override
    public void updateRotation(Quaternion rotation) {
        this.rotation = rotation.clone();
    }

    /**
     *
     */
    @Override
    public void applyFriction() {
        pcs.firePropertyChange(Commands.FRICTION, null, frictionForce);
    }

    /**
     *
     * @param maxSpeed
     */
    @Override
    public void setMaxSpeed(float maxSpeed) {
        this.currentMaxSpeed = maxSpeed;
    }

    /**
     *
     * @param accelerationForce
     */
    @Override
    public void setAccelerationForce(float accelerationForce) {
        this.currentAccelerationForce = accelerationForce;
    }
    
    /**
     *
     * @return
     */
    @Override
    public float getDefaultMaxSpeed(){
        return defaultMaxSpeed;
    }

    /**
     *
     * @param projectile
     */
    @Override
    public void gotHitBy(IExplodingProjectile projectile) {
        this.applyDamage(projectile.getDamageOnImpact());
        if (health<=0){
            hideFromWorld();
        }
    }

    /**
     *
     */
    @Override
    public void cleanup() {
        pcs.firePropertyChange(Commands.CLEANUP, null, null);
    }

    /**
     *
     * @param position
     */
    @Override
    public void setPosition(Vector3f position) {
        this.position = new Vector3f(position);
    }

    @Override
    public void showInWorld() {
        health = 100;
        pcs.firePropertyChange(Commands.HEALTH, null, health);
        vehicleState = VehicleState.ALIVE;
        boolean wasInWorld = isInWorld;
        isInWorld = true;
        pcs.firePropertyChange(Commands.SHOW, wasInWorld, isInWorld);
    }

    @Override
    public void hideFromWorld() {
        boolean wasInWorld = isInWorld;
        isInWorld = false;
        vehicleState = VehicleState.DESTROYED;
        pcs.firePropertyChange(Commands.HIDE, wasInWorld, isInWorld);
    }

    /**
     *
     * @return
     */
    @Override
    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isShownInWorld() {
        return this.isInWorld;
    }

    /**
     *
     */
    @Override
    public void resetSpeedValues() {
        currentMaxSpeed = defaultMaxSpeed;
    }

    /**
     *
     * @param ex
     * @throws IOException
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param im
     * @throws IOException
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
