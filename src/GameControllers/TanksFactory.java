
package GameControllers;

import GameControllers.logic.ViewPortManager;
import GameModel.IPlayer;
import GameModel.IExplodingProjectile;
import GameModel.MissileModel;
import GameModel.TankModel;
import App.TanksAppAdapter;
import GameControllers.entitycontrols.MissileControl;
import GameControllers.entitycontrols.PowerupControl;
import GameControllers.entitycontrols.TanksVehicleControl;
import GameControllers.logic.GameAppState;
import GameModel.GameSettings;
import GameModel.ITanks;
import GameModel.TanksGameModel;
import GameModel.Player;
import GameModel.HastePowerup;
import GameModel.IPowerup;
import GameModel.IArmedVehicle;
import GameModel.ISpawningPoints;
import GameModel.SpawningPoint;
import GameUtilities.Util;
import GameView.GUI.HealthView;
import GameView.GUI.PowerupSlotView;
import GameView.GUI.TimerView;
import GameView.Map.GameWorld1;
import GameView.Map.IGameWorld;
import GameView.gameEntity.MissileProjectileEntity;
import GameView.gameEntity.PowerupEntity;
import GameView.gameEntity.TankEntity;
import GameView.viewPort.VehicleCamera;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Manages controls.
 * 
 * @author Daniel
 */
public final class TanksFactory {
    
    private TanksFactory() {}
    
    public static void createTank(IPlayer player, Vector3f startPos) {
        // Create a tank for each player
        TankEntity entity = new TankEntity(player.getVehicle());
        // Move to startpos
        entity.setPosition(startPos);
        
        Node carNode = (Node)entity.getSpatial();

        TanksVehicleControl vehicle = new TanksVehicleControl(entity, player);
        vehicle.setSuspensionCompression(TankModel.TANK_COMP_VALUE * 2.0f
                                * FastMath.sqrt(TankModel.TANK_STIFFNESS));
        vehicle.setSuspensionDamping(TankModel.TANK_DAMP_VALUE * 2.0f 
                                * FastMath.sqrt(TankModel.TANK_STIFFNESS));
        vehicle.setSuspensionStiffness(TankModel.TANK_STIFFNESS);
        vehicle.setMaxSuspensionForce(TankModel.TANK_MAX_SUSPENSION_FORCE);
        
        Geometry wheel_fr = Util.findGeom(carNode, "WheelFrontRight");
        wheel_fr.center();
        BoundingBox box = (BoundingBox) wheel_fr.getModelBound();
        float wheelRadius = box.getYExtent();
        vehicle.addWheel(wheel_fr.getParent(), new Vector3f(TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS, 
                TankModel.TANK_WHEEL_REST_LENGTH, wheelRadius, true);

        Geometry wheel_fl = Util.findGeom(carNode, "WheelFrontLeft");
        wheel_fl.center();
        box = (BoundingBox) wheel_fl.getModelBound();
        vehicle.addWheel(wheel_fl.getParent(), new Vector3f(-TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS, 
                TankModel.TANK_WHEEL_REST_LENGTH, wheelRadius, true);

        Geometry wheel_br = Util.findGeom(carNode, "WheelBackRight");
        wheel_br.center();
        box = (BoundingBox) wheel_br.getModelBound();
        vehicle.addWheel(wheel_br.getParent(), new Vector3f(TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, -TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS, 
                TankModel.TANK_WHEEL_REST_LENGTH, wheelRadius, false);

        Geometry wheel_bl = Util.findGeom(carNode, "WheelBackLeft");
        wheel_bl.center();
        box = (BoundingBox) wheel_bl.getModelBound();
        vehicle.addWheel(wheel_bl.getParent(), new Vector3f(-TankModel.TANK_WHEEL_X_OFF,
                TankModel.TANK_WHEEL_Y_OFF, -TankModel.TANK_WHEEL_Z_OFF),
                TankModel.TANK_WHEEL_DIRECTION, TankModel.TANK_WHEEL_AXIS, 
                TankModel.TANK_WHEEL_REST_LENGTH, wheelRadius, false);
        entity.addControl(vehicle);
        
        TanksAppAdapter.INSTANCE.addPhysiscsCollisionListener(vehicle);
        
        // Get the right viewport for the player and enable it
        ViewPort viewPort = ViewPortManager.INSTANCE.getViewportForPlayer(player.getName());
        viewPort.setEnabled(true);
        // Give the tank a refernce to the camera of the viewport
        vehicle.setCamera(viewPort.getCamera());
    }
    
    public static void createNewMissile(Vector3f position, Vector3f direction, Quaternion rotation) {
        IExplodingProjectile projectileModel = new MissileModel(position, direction, rotation);
        
        MissileProjectileEntity projectileEntity = new MissileProjectileEntity(projectileModel);
        
        MissileControl control = new MissileControl(projectileEntity, projectileModel);
        
        control.setCcdMotionThreshold(0.1f);
        control.setKinematic(true);
        
        TanksAppAdapter.INSTANCE.addPhysiscsCollisionListener(control);
        TanksAppAdapter.INSTANCE.addToPhysicsSpace(control);
        
        projectileEntity.addControl(control);
    }
    
    public static IPowerup getNewPowerup() {
        IPowerup model = new HastePowerup();
        //model.setPosition(position);
        PowerupEntity view = new PowerupEntity(model);
        PowerupControl control = new PowerupControl(view, model);
        
        control.setKinematic(true);

        TanksAppAdapter.INSTANCE.addPhysiscsCollisionListener(control);
        TanksAppAdapter.INSTANCE.addToPhysicsSpace(control);
        
        view.addControl(control);
        return model;
    }
    
    public static VehicleCamera getVehicleChaseCamera(Camera cam, Spatial spatial) {
        VehicleCamera chaseCam = new VehicleCamera(cam, spatial, TanksAppAdapter.INSTANCE.getInputManager());
        chaseCam.setMaxDistance(25);
        chaseCam.setMinDistance(15);
        chaseCam.setDefaultDistance(20);
        chaseCam.setChasingSensitivity(50f);
        chaseCam.setSmoothMotion(true); //automatic following
        chaseCam.setUpVector(Vector3f.UNIT_Y);
        chaseCam.setTrailingEnabled(true);
        chaseCam.setDefaultVerticalRotation(0.3f);
        return chaseCam;
    }
    
    public static GameAppState getNewGame(int intWorld, Collection<String> playerNames) {
        
        // temp for testing
        List<ISpawningPoints> playerSpawningPoints = new ArrayList<ISpawningPoints>();
        playerSpawningPoints.add(new SpawningPoint(new Vector3f(10, 3, 10)));
        playerSpawningPoints.add(new SpawningPoint(new Vector3f(10, 3, 12)));
        playerSpawningPoints.add(new SpawningPoint(new Vector3f(30, 3, 10)));
        playerSpawningPoints.add(new SpawningPoint(new Vector3f(35, 3, 9)));
        
        List<ISpawningPoints> powerupSpawningPoints = new ArrayList<ISpawningPoints>();
        powerupSpawningPoints.add(new SpawningPoint(new Vector3f(4, 3, 7)));
        powerupSpawningPoints.add(new SpawningPoint(new Vector3f(6, 3, 7)));
        powerupSpawningPoints.add(new SpawningPoint(new Vector3f(8, 3, 7)));
        powerupSpawningPoints.add(new SpawningPoint(new Vector3f(20, 3, 20)));
        
        List<IPowerup> powerups = new ArrayList<IPowerup>();
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        powerups.add(getNewPowerup());
        
        GameSettings settings = new GameSettings(100f, 10);

        int numberOfPlayers = playerNames.size();
        List<IPlayer> players = new ArrayList<IPlayer>();
        // Create one player for each name
        for (String name : playerNames) {
            // Create one vehicleModel per player
            IArmedVehicle vehicle = new TankModel();
            Player player = new Player(name, vehicle);
            
            // set up gui for each player
            PowerupSlotView pView = new PowerupSlotView(player, 
                    ViewPortManager.INSTANCE.getViewportForPlayer(player.getName()), numberOfPlayers);
            HealthView v = new HealthView(player, 
                    ViewPortManager.INSTANCE.getViewportForPlayer(player.getName()), numberOfPlayers);
            pView.show();
            v.show();
            
            players.add(player);
        }
        ITanks game = new TanksGameModel(players, powerups, powerupSpawningPoints, playerSpawningPoints, settings);
        
        IGameWorld gameWorld = null;
        switch (intWorld) {
            case 1:
                gameWorld = new GameWorld1(game, new MapOne());
                break;
            default: 
                gameWorld = new GameWorld1(game, new MapOne());
                break;
        }
        
        // set up timerView
        TimerView timerView = new TimerView(game);
        timerView.show();

        return new GameAppState(game, gameWorld);
    }
}
