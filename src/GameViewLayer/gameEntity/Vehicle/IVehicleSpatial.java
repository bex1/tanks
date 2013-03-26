
package GameViewLayer.gameEntity.Vehicle;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Interface describing a vehical spatial with a node connection.
 *
 * @author Daniel, Per, Johan, Albin
 */
public interface IVehicleSpatial {
    /**
     * Getter for the spatial of the vehicle.
     *
     * @return Spatial of the vehicle.
     */
    Spatial getVehicleSpatial();
    /**
     * Getter for the vehicle node.
     *
     * @return Node that the vehicle spatial is connected to.
     */
    Node getVehicleNode();
    
    /**
     * Gets the direction of the vehicle in the 3d room.
     *
     * @return The direction of the vehicle
     */
    Vector3f getDirection();
    
    /**
     * Gets the position of the vehicle in the 3d room.
     * 
     * @return The position of the vehicle
     */
    Vector3f getPosition();
    
    /**
     * Sets the direction of the vehicle in the 3d room.
     *
     * @param direction The direction of the vehicle in the 3d room
     */
    void setDirection(Vector3f direction);
    
    /**
     * Sets the position of the vehicle in the 3d room.
     * 
     * @param position The position of the vehicle in the 3d room
     */
    void setPosition(Vector3f position);
}