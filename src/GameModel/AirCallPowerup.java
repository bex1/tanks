/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

import GameUtilities.Commands;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author backman
 */
public class AirCallPowerup extends APowerup {

    private List<IExplodingProjectile> bombs;
    private long activateTimerStart;
    private boolean isActive;
    
    private static final long END_TIME = 10000;
    private static final long INITIAL_DELAY = 200;
    private static final float INTERVAL = 0.0001f;
    
    private static final int DROP_HEIGHT = 10;
    
    private float counter;
    
    private IPlayer player;
    private IArmedVehicle vehicle;

    @Override
    public void usePowerup(IPlayer player) {
        super.usePowerup(player);
        this.player = player;
        vehicle = player.getVehicle();
        
        activateTimerStart = System.currentTimeMillis();
        isActive = true;
    }
    
    public void addBombType(List<IExplodingProjectile> bombs) {
        this.bombs = bombs;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (isActive) {
            counter = counter + tpf;
            
            if (counter >= INTERVAL) {
                dropBomb();
                counter = 0;
            }
            if (System.currentTimeMillis() - activateTimerStart >= END_TIME) {
                isActive = false;
            }
        }

    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void dropBomb() {
        int tmpRandomOne = (int) (Math.random() * 2);
        int tmpRandomTwo = (int) (Math.random() * 2);
        System.out.println(tmpRandomOne);
        System.out.println(tmpRandomTwo);
        
        float zRandom = (float) (Math.random() * 200);
        float xRandom = (float) (Math.random() * 200);
        
        if (tmpRandomOne == 1) {
            zRandom *= -1;
        }
        if (tmpRandomTwo == 1) {
            xRandom *= -1;
        }
        Vector3f initPos = new Vector3f(xRandom, DROP_HEIGHT, zRandom);
        for (int i = 0; i < bombs.size(); i++) {
            if (!bombs.get(i).isShownInWorld()) {
                
                bombs.get(i).launchProjectile(initPos, new Vector3f(0, -1, 0).multLocal(100), Quaternion.ZERO, player);
                return; 
            }
        }
    }
}
