/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameViewLayer.Vehicle;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Daniel
 */
public interface ITankSpatial {
 
    Spatial getSpatial();
    Node getVehicleNode();
    Node getRightFrontWheel();
    Node getLeftFrontWheel();
    Node getRightBackWheel();
    Node getLeftBackWheel();
}
