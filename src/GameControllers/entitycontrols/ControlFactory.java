
package GameControllers.entitycontrols;

import GameControllers.logic.ViewPortManager;
import GameModel.Player.IPlayer;
import GameModel.gameEntity.Projectile.IExplodingProjectile;
import GameModel.gameEntity.Projectile.MissileModel;
import GameModel.gameEntity.Vehicle.IArmedVehicle;
import GameModel.gameEntity.Vehicle.TankModel;
import GameUtilities.TankAppAdapter;
import GameView.gameEntity.EGameEntities;
import GameView.gameEntity.GameEntityFactory;
import GameView.gameEntity.IGameEntity;
import GameView.gameEntity.MissileProjectileEntity;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.Control;

/**
 * Manages controls.
 * 
 * @author Daniel
 */
public final class ControlFactory {
    
    private ControlFactory() {}
    
    public static TanksVehicleControl getTankControl(IGameEntity entity, IPlayer player) {
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        compoundShape.addChildShape(entity.getCollisionShape(), new Vector3f(0, 1, 0));
        
        TanksVehicleControl vehicle = new TanksVehicleControl(compoundShape, TankModel.TANK_MASS, entity, player);
        vehicle.setSuspensionCompression(TankModel.TANK_COMP_VALUE * 2.0f
                                * FastMath.sqrt(TankModel.TANK_STIFFNESS));
        vehicle.setSuspensionDamping(TankModel.TANK_DAMP_VALUE * 2.0f 
                                * FastMath.sqrt(TankModel.TANK_STIFFNESS));
        vehicle.setSuspensionStiffness(TankModel.TANK_STIFFNESS);
        vehicle.setMaxSuspensionForce(TankModel.TANK_MAX_SUSPENSION_FORCE);

        vehicle.addWheel(null, new Vector3f(-TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS,
                TankModel.TANK_WHEEL_REST_LENGTH, TankModel.TANK_WHEEL_RADIUS,
                true);

        vehicle.addWheel(null, new Vector3f(TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS,
                TankModel.TANK_WHEEL_REST_LENGTH, TankModel.TANK_WHEEL_RADIUS,
                true);

        vehicle.addWheel(null, new Vector3f(-TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, -TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS,
                TankModel.TANK_WHEEL_REST_LENGTH, TankModel.TANK_WHEEL_RADIUS,
                false);

        vehicle.addWheel(null, new Vector3f(TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, -TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS,
                TankModel.TANK_WHEEL_REST_LENGTH, TankModel.TANK_WHEEL_RADIUS,
                false);
        
        entity.addControl(vehicle);
        
        // Get the right viewport for the player and enable it
        ViewPort viewPort = ViewPortManager.INSTANCE.getViewportForPlayer(player);
        viewPort.setEnabled(true);
        // Give the tank a refernce to the camera of the viewport
        vehicle.setCamera(viewPort.getCamera());
        
        return vehicle;
    }
    
    public static void createNewMissile(Vector3f position, Vector3f direction, Quaternion rotation) {
        IExplodingProjectile projectileModel = new MissileModel(position, direction, rotation);
        
        MissileProjectileEntity projectileEntity = (MissileProjectileEntity) GameEntityFactory.create(EGameEntities.MISSILE_PROJECTILE);
        projectileEntity.setModel(projectileModel);
        
        MissileControl control = new MissileControl(projectileEntity, projectileModel);
        
        control.setCcdMotionThreshold(0.1f);
        control.setKinematic(true);
        
        TankAppAdapter.INSTANCE.addPhysiscsCollisionListener(control);
        TankAppAdapter.INSTANCE.addToPhysicsSpace(control);
        
        projectileEntity.addControl(control);
    }
}
