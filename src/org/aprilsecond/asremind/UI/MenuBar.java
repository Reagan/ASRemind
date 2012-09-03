package org.aprilsecond.asremind.UI;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.aprilsecond.customuicomponents.AnimatedMenu.AnimatedMenuBar;
import org.aprilsecond.customuicomponents.AnimatedMenu.AnimatedMenuItem;
import org.aprilsecond.customuicomponents.AnimatedMenu.MenuItemSelected;
import org.aprilsecond.customuicomponents.AnimatedMenu.MenuItemSelectedListener;
import org.aprilsecond.customuicomponents.BasePanels.BaseContentPanel;

/**
 * This stores an instance to the menu bar for the application
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class MenuBar extends AnimatedMenuBar {
    
    /**
     * stores the frame for the component
     */
    private JFrame frame ;
    
    /**
     * this stores the panel with the panes
     * that have a card layout
     */
    private BaseContentPanel contentPane ;
    
    /**
     * stores the items for the menu item
     */
    private String[] menuItems = {"Activities" , "Schedule" , "Track"} ;
    /**
     * constructor initializes frame
     */
    public MenuBar(JFrame appFrame, BaseContentPanel contentPanel) {
        super(appFrame) ;
        
        // set the frame 
        frame = appFrame ;
        
        // set the content pane
        contentPane = contentPanel ;
        
        // add items to menu bar
        addMenuItems() ;
    }
    
    /**
     * adds menu items to the menu bar
     */
    private void addMenuItems() {
        for (int menuItemsCounter = 0 ; menuItemsCounter < menuItems.length;
                menuItemsCounter ++ ) {
            
            // create the menu item
            AnimatedMenuItem menuItem 
                    = new AnimatedMenuItem(menuItems[menuItemsCounter]) ;
            
            // add the item to the menu bar
            addMenuItem(menuItem);
            
            // load the selected panel when an item in the menu bar is 
            // selected
            // add an event listener to the component            
            addMenuItemSelectedListener(new MenuItemSelectedListener() {

                @Override
                public void menuItemSelected(MenuItemSelected evt) {
                    // get the selected menu item
                    AnimatedMenuItem menuItem = (AnimatedMenuItem) evt.getSource();
                                        
                    // set the requested panel as the main panel
                    for (int panelsCounter = 0 ; panelsCounter < menuItems.length ;
                            panelsCounter++) {
                        if(menuItem.getActionName().equals(menuItems[panelsCounter])) {
                            CardLayout layout 
                                    = (CardLayout) (contentPane.getLayout()) ;
                            layout.show(contentPane, menuItem.getActionName()) ;                            
                        }
                    }
                }
            });
        }
    }
}
