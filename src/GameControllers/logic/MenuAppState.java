
package GameControllers.logic;

import App.TanksAppAdapter;
import GameModel.EApplicationState;
import GameView.Sounds.ESounds;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * An app state representing the main menu.
 * 
 * @author Johan Backman, Daniel Bäckström, Albin Garpetun, Per Thoresson
 */
public class MenuAppState extends AbstractAppState implements ScreenController {
    
    private static MenuAppState instance;
    
    private Nifty nifty;
    private Element currentElement, musicToggle, fxToggle;
    private SoundHandle sound;
    
    private final List<String> playerNames = new ArrayList<String>();

    /**
     *  Create a new main menu app state.
     */
    private MenuAppState() {
        nifty = GUIManager.INSTANCE.getNifty();
        nifty.fromXml("Interface/Nifty/MainMenu.xml", "start", this);
        
        nifty.getSoundSystem().addSound("hooverSound", "Sounds/click.ogg");

        sound = nifty.getSoundSystem().getSound("hooverSound");
        sound.setVolume(1.0f);
    }
    
    /**
     * Get the instance to the current singleton.
     * 
     * @return instance.
     */
    public static synchronized MenuAppState getInstance() {
        if (instance == null) {
            instance = new MenuAppState();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateAttached(AppStateManager stateManager) {
        TanksAppAdapter.INSTANCE.setCursorVisible(true);
        goToMainMenu();
        EApplicationState.setGameState(EApplicationState.MAIN_MENU);
        playerNames.clear();
        TanksAppAdapter.INSTANCE.attachAppState(GlobalInputAppState.getInstance());
        if (!SoundManager.INSTANCE.isMusicMuted()) {
            SoundManager.INSTANCE.play(ESounds.MENU_SOUND);
        }
        musicToggle = nifty.getScreen("settings").findElementByName("main_music_toggle");
        fxToggle = nifty.getScreen("settings").findElementByName("main_fx_toggle");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    /**
     * Go to the main menu.
     */
    public void goToMainMenu() {
        nifty.gotoScreen("start");
    }
    
    /**
     * Go to the settings menu.
     */
    public void goToSettingsScreen() {
        nifty.gotoScreen("settings");
        updateSettingsOptions();
    }
    
    /**
     * Go to the player choosing menu.
     */
    public void goToMultiplayerScreen() {
       nifty.gotoScreen("multi");
    }

    /**
     * Toggle music on/off.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void toggleMusic() {
        SoundManager.INSTANCE.toggleMusic();
        updateSettingsOptions();
    }
    
    /**
     * Toggle FX on/off.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void toggleFX() {
        SoundManager.INSTANCE.toggleFX();
        updateSettingsOptions();
    }
    
    /**
     * Hover effects for menu items.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     * 
     * @param id nifty element id.
     * @param event nifty mouse event.
     */
    @NiftyEventSubscriber(pattern = "main.*")
    public void onHover(String id, NiftyMouseMovedEvent event) {

        if (currentElement == null) { //initial element
            if (event.getElement().getRenderer(TextRenderer.class) != null) {
                currentElement = event.getElement();

                // hover
                TextRenderer renderer1 = currentElement.getRenderer(TextRenderer.class);
                renderer1.setColor(Color.BLACK);

                if (!SoundManager.INSTANCE.isSoundFXMuted()) {
                    sound.play();
                }
            }
        } else {
            if (event.getElement() != currentElement) {
                currentElement.getRenderer(TextRenderer.class).setColor(Color.WHITE);
                currentElement = null;
            }
        }
    }

    /**
     * Bind the screen to Nifty.
     * 
     * @param nifty nifty instance.
     * @param screen screen to bind.
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    /**
     * Exit the game.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void exit() {
        TanksAppAdapter.INSTANCE.stop();
    }
    
    /**
     * Update the visual representation in the settings menu.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void updateSettingsOptions() {
        if (SoundManager.INSTANCE.isMusicMuted()) {
            musicToggle.getRenderer(TextRenderer.class).setText("MUSIC OFF");
        } else {
            musicToggle.getRenderer(TextRenderer.class).setText("MUSIC ON");
        }
        if (SoundManager.INSTANCE.isSoundFXMuted()) {
            fxToggle.getRenderer(TextRenderer.class).setText("SOUND EFFECTS OFF");
        } else {
            fxToggle.getRenderer(TextRenderer.class).setText("SOUND EFFECTS ON");
        }
    }
    
    /**
     * Load the game with one player.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void loadOnePlayerGame() {
        playerNames.add("Player1");
        GUIManager.INSTANCE.showLoadingScreen();
        TanksAppAdapter.INSTANCE.detachAppState(this);
    }
    
    /**
     * Load the game with two players.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void loadTwoPlayerGame() {
        playerNames.add("Player1");
        playerNames.add("Player2");
        GUIManager.INSTANCE.showLoadingScreen();
        TanksAppAdapter.INSTANCE.detachAppState(this);
    }
    
    /**
     * Load the game with three players.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void loadThreePlayerGame() {
        playerNames.add("Player1");
        playerNames.add("Player2");
        playerNames.add("Player3");
        GUIManager.INSTANCE.showLoadingScreen();
        TanksAppAdapter.INSTANCE.detachAppState(this);
    }
    
    /**
     * Load the game with four players.
     * 
     * NOTE: Used by the Nifty screen. Shouldn't be used anywhere else.
     */
    public void loadFourPlayerGame() {
        playerNames.add("Player1");
        playerNames.add("Player2");
        playerNames.add("Player3");
        playerNames.add("Player4");
        GUIManager.INSTANCE.showLoadingScreen();
        TanksAppAdapter.INSTANCE.detachAppState(this);
    }
    
    /**
     * Get the names of the players in the game.
     * 
     * @return list of player names.
     */
    public ArrayList<String> getPlayerNames() {
        return new ArrayList<String>(playerNames);
    }
}
