package GameControllers.logic;

import App.TanksAppAdapter;
import GameModel.ITanks;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import java.util.List;

/**
 *
 * @author Daniel
 */
public enum GUIManager {
    /**
     *
     */
    INSTANCE;

    private Nifty nifty;
    private List<Spatial> hudElements;
    
    /**
     *
     */
    private GUIManager() {
        initialiseNifty();
    }
    
    private void initialiseNifty() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(TanksAppAdapter.INSTANCE.getAssetManager(),
                TanksAppAdapter.INSTANCE.getInputManager(), TanksAppAdapter.INSTANCE.getAudioRenderer(), 
                TanksAppAdapter.INSTANCE.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.enableAutoScaling(1280, 720);
        TanksAppAdapter.INSTANCE.addGuiViewProcessor(niftyDisplay);
    }
    
    /**
     *
     */
    public void showMainMenu() {
        TanksAppAdapter.INSTANCE.attachAppState(MenuAppState.getInstance());
    }

    /**
     *
     */
    public void showLoadingScreen() {
        TanksAppAdapter.INSTANCE.attachAppState(LoadingScreenAppState.getInstance());
    }
    
    /**
     *
     * @param gameModel 
     */
    public void showPauseMenu(ITanks gameModel) {
        PauseMenuAppState.getInstance().setGameToPause(gameModel);
        hudElements = TanksAppAdapter.INSTANCE.getGuiChildren();
        TanksAppAdapter.INSTANCE.detachAllGUIChildren(); // Hide all HUD components
        TanksAppAdapter.INSTANCE.attachAppState(PauseMenuAppState.getInstance());
    }
    
    /**
     *
     * @return
     */
    public Nifty getNifty() {
        return nifty;
    }
    
    /**
     *
     */
    public void cleanup() {
        TanksAppAdapter.INSTANCE.detachAllGUIChildren();
    }
    
    /**
     * Returns the HUD elements last attached to the guiNode since last paus.
     * 
     * @return hud elements attached to guiNode.
     */
    public List<Spatial> getHudElements() {
        return hudElements;
    }
}
