package GameLogicLayer.viewPort;

import GameLogicLayer.Effects.EffectsManager;
import GameLogicLayer.Game.GameManager;
import GameLogicLayer.AppStates.TanksGame;
import GameModelLayer.Player.Player;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Manager for the game Tanks that handles viewports.
 *
 * @author Daniel
 */
public class ViewPortManager {
    private static ViewPortManager instance;
    
    private HashMap<Player, EViewPorts> views = new HashMap<Player, EViewPorts>();
    private Node rootNode;
    private GameManager gameManager;
    
    /**
     * Creates a new viewport manager.
     */
    private ViewPortManager() {
        TanksGame app = TanksGame.getApp();
        this.gameManager = GameManager.getInstance();
        this.rootNode = app.getRootNode();
    }
    
    public static synchronized ViewPortManager getInstance() {
        if (instance == null) {
            instance = new ViewPortManager();
        }
        return instance;
    }

    private void initViews() {
        List<Player> players = gameManager.getPlayers();
        switch(players.size()) {
            case 0:   
            case 1:
                views.put(players.get(0), EViewPorts.CENTER);
                break;
            case 2:
                views.put(players.get(0), EViewPorts.TOP);
                views.put(players.get(1), EViewPorts.BOTTOM);
                break;
            case 3:
                views.put(players.get(0),EViewPorts.TOP_LEFT);
                views.put(players.get(1), EViewPorts.TOP_RIGHT);
                views.put(players.get(2), EViewPorts.BOTTOM);
                break;
            default:
                views.put(players.get(0), EViewPorts.TOP_LEFT);
                views.put(players.get(1), EViewPorts.TOP_RIGHT);
                views.put(players.get(2), EViewPorts.BOTTOM_Left);
                views.put(players.get(3), EViewPorts.BOTTOM_RIGHT);
                break;
        };
    }
    
    /**
     * Loads the right viewports determined by number of players.
     */
    public void load() {
        initViews();
        for (EViewPorts view : views.values()) {
            view.getViewPort().attachScene(rootNode);
        }
    };
    
    /**
     * Returns the viewport for the specified player.
     * 
     * @param p The player which you want the viewport for
     * @return the viewport for the specified player
     */
    public ViewPort getViewportForPlayer(Player p) {
        return views.get(p).getViewPort();
    }
    
    /**
     * Remove this later --> dont share the reference to the collection
     * @return 
     */
    public Collection<EViewPorts> getViews() {
        return views.values();
    }

    /**
     * Releases the resources contained by this manager.
     */
    public void cleanup() {
        views.clear();
    }
}