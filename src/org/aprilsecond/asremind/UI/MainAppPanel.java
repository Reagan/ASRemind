package org.aprilsecond.asremind.UI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.aprilsecond.asremind.ASRemind;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.localcache.TrackMessage;
import org.aprilsecond.customuicomponents.BasePanels.BaseContentPanel;
import org.aprilsecond.customuicomponents.BasePanels.MenuPanel;
import org.aprilsecond.customuicomponents.ContainerPanels.BasePanel;

/**
 * This stores the main panel for the application. It is a container of
 * the Base Panel
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class MainAppPanel extends BasePanel {
    
    /**
     * stores the properties for the location for the main panel
     */
     private Toolkit toolKit = null; 
     
     /**
      * stores the width to the panel
      */
     private int screenHeight ;    
     
     /**
      * stores the height to the panel
      */
      private int screenWidth ;

    /**
     * stores the Menu Bar
     */
    private MenuBar menuBar ;
    
    /**
     * stores the activities pane
     */
    private ActivitiesPane activitiesPane ;
    
    /**
     * set the name of the activities pane
     */
    private final static String ACTIVITIES_PANEL
            = "Activities" ;
    
    /**
     * stores whether or not all information for the activities 
     * panel has been loaded
     */
    public static boolean activitiesPanelLoaded = false ;
    
    /**
     * stores the schedule pane
     */
    private SchedulePane schedulePane ;
    
    /**
     * set the name for the schedule pane
     */
    private final static String SCHEDULE_PANEL
            = "Schedule" ;
    
    /**
     * stores whether or not all information for the
     * schedule panel has been loaded
     */
    public static boolean schedulePanelLoaded = false ;
    
    /**
     * stores the track pane
     */
    private TrackPane trackPane ;
    
    /**
     * sets the name for the track pane
     */
    private final static String TRACK_PANE
            = "Track" ;
    
    /**
     * stores whether or not the track pane has 
     * been loaded
     */
    public static boolean trackPaneLoaded = false ;
    
    /**
     * stores the lower panel that displays the
     * currently selected panel
     */
    public BaseContentPanel mainContentPanel  ;
    
    /**
     * stores the application icon path
     */
    private String appIconPath = "resources" 
            + File.separator 
            + "images" 
            + File.separator
            + "appIcon.png";
    
    /**
     * constructor loads up the application
     */
    public MainAppPanel() {
        // stores the screen size that the application is in
        Dimension screenSize ;       
        
        // initialize the Toolkit application
        toolKit = Toolkit.getDefaultToolkit(); 
        screenSize = toolKit.getScreenSize();
        screenHeight = screenSize.height;
        screenWidth = screenSize.width;                      
        
        // initialize the lower content section pane
        mainContentPanel = new BaseContentPanel() ;
        mainContentPanel.setLayout(new CardLayout());
        
         // intialize the menu bar
        menuBar = new MenuBar(this, mainContentPanel) ;
        
        // set the application icon
        Image appIcon = null;
        try {
            appIcon = ImageIO.read(new File(appIconPath));
            setIconImage(appIcon);
        } catch (IOException ex) {
            System.out.println("Error accessing application icon image "
                    + appIconPath);
        }        
     
        // build the look
        buildUI();
    }
    
    /**
     * builds the content pane for the application
     */
    public final void buildUI () {
        
        // set the location for the panel to the center right
        setLocation(screenWidth - BasePanel.PANEL_WIDTH, screenHeight/3);
        
        // add the menu bar from the panel
        MenuPanel menuBarContainer = new MenuPanel() ;  
        
        // add the menu bar
        menuBarContainer.add(menuBar, BorderLayout.CENTER) ;
                
        // add the menu bar to the frame
        addMenuBar(menuBarContainer);
                      
        // build the content panes for the activities, Schedule and track pages 
        buildContentPanes() ;
        
        // adds the panes to the main pane
        // package the panes
        ArrayList<BaseContentPanel> allPanels = new ArrayList<BaseContentPanel>() ;
        allPanels.add(activitiesPane);
        allPanels.add(schedulePane) ;
        allPanels.add(trackPane) ;
        
        // package the names for the panels 
        ArrayList<String> panelNames = new ArrayList<String>() ;
        panelNames.add(ACTIVITIES_PANEL);
        panelNames.add(SCHEDULE_PANEL);
        panelNames.add(TRACK_PANE) ;
        
        // add the panes to the lower main content section
        appendContentPanes(mainContentPanel, allPanels, panelNames ) ;
          
        // add the lower content pane to the lower
        // section of the pane
        addContentPane(mainContentPanel);
                
        // add the base content pane with the default as the
        // activities pane
        if (!ASRemind.initialized) {
            
            // set the initialized state to true
            // ASRemind.initialized = true ;
            
            // set the current pane displayed
            ASRemind.currPage = Panes.ACTIVITIES_PANE ;
        }
    }
    
    /**
     * adds the base content panes to the lower content pane
     */
    private void appendContentPanes(BaseContentPanel basePanel, 
            ArrayList<BaseContentPanel> allPanels, ArrayList<String> panelNames ) {
        for (int panelsCounter = 0 ; panelsCounter < allPanels.size() ;
                panelsCounter++) {
            
            // add the panel with its name
            basePanel.add(allPanels.get(panelsCounter), 
                    panelNames.get(panelsCounter)) ;
        }
    }
        
    /**
     * This method builds the content panes for the activities, 
     * schedule and track pages
     */
    private void buildContentPanes() {
        // initialize the activities Pane
        activitiesPane = new ActivitiesPane() ;
        activitiesPane.buildUI();
        
        // initialize the schedule pane
        schedulePane = new SchedulePane() ;
        schedulePane.buildUI();
        
        // initialize the track pane
        trackPane = new TrackPane() ;
        trackPane.buildUI();
    }
    
    /**
     * This method updates all the various UI panes
     * for the components
     */
    public void updatePanes(ArrayList<ASCalendar> calendarInfo,
            ArrayList<TrackMessage> trackMessages) {                      
        // update the various panes
        activitiesPane.updatePane(calendarInfo);
        schedulePane.updatePane(Calendar.getInstance(), calendarInfo);
        trackPane.updatePane(trackMessages);
    }
}
