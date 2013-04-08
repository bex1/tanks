/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameViewLayer.GUI;

/**
 *
 * @author Barre
 */
public enum EFonts {
    
    LOADINGSCREEN("Interface/Fonts/loadingFont.fnt"),
    
    FLOATINGNAMES("Interface/Fonts/loadingFont.fnt"),
    
    MAINMENU("Interface/Fonts/loadingFont.fnt"),
    
    INGAME("Interface/Fonts/loadingFont.fnt");
    
    private String path;
    
    private EFonts(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
}