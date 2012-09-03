package org.aprilsecond.asremind.reminder;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import org.aprilsecond.asremind.Calendar.ASEntry;
import org.aprilsecond.customuicomponents.ContainerPanels.MinimalBasePanel;

/**
 * This class represents a single task that displays a reminder 
 * window when a specific entry time is up for display 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class ReminderWindowDisplayTask implements Runnable {

    /**
     * stores the calendar entry for which a reminder window
     * will be displayed
     */
    private ASEntry winEntry ;
    
    /**
     * stores the displayed minimal panel
     */
    private MinimalBasePanel displayedPanel ;
    
    /**
     * stores the duration at which the task should 
     * be run from the current time in seconds
     */
    private int minsFromNowToDisplayWindow = 0 ;
    
    /**
     * stores the location at which the panel is to 
     * be displayed
     */
    public Point displayLocation = new Point(0, 0) ;
    
    /**
     * stores the title of the window panel
     */
    private final String PANEL_TITLE = "!Reminder" ;
    
    /**
     * constructor initializes task and the 
     * location at which the window is to be
     * displayed
     */
    public ReminderWindowDisplayTask(ASEntry entry,
            Point dLocation) {
        // initialize the entry
        winEntry = entry ;
        
        // initialize the displayed location
        displayLocation = dLocation ;
        
        // set up the panel
        setUpPanel(PANEL_TITLE, entry.getTitle(),displayLocation) ;
       
        // get the number of seconds to wait before 
        // displaying the window timer
        setNumberOfMillsToWait() ;
    }
    
    /**
     * this method sets up the displayed panel
     */
    private void setUpPanel(String title, String message,
            Point dLocation) {
        
        // initialize the panel to be displayed
        displayedPanel = new MinimalBasePanel(title, message, 
                dLocation) ;                      
        
        // display the minimal pane
        displayedPanel.displayPanel();
                        
        // adds a window listener to the displayed window
        // that hides the window when it is selected
        displayedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                displayedPanel.setVisible(false);
                displayedPanel.dispose();
            }
        });
        
        // add to the set of windows shown to be displayed        
        Reminder.displayedReminderPanels.add(displayedPanel) ;
    }
    
    /**
     * this method obtains the number of milliseconds to 
     * wait before running a task. it loops through the various 
     * reminders for the entry and obtains the time to wait 
     * for the popup reminder. If a popup reminder is not found,
     * then this task is nulled
     */
    private void setNumberOfMillsToWait() {                 
        
        // get the start time and subtract reminder time
        Calendar reminderTime = winEntry.getStartTime();
        reminderTime.add(Calendar.MINUTE, -minsFromNowToDisplayWindow) ;
     
        int mins = (int) ((reminderTime.getTimeInMillis()
                - Calendar.getInstance().getTimeInMillis() 
                )/1000)/60 ;
        
        // set the delay time
        minsFromNowToDisplayWindow = mins ; 
        //minsFromNowToDisplayWindow = 1 ;
    }
    
    /**
     * gets the delay for task. This is calculated from the time
     * at which the event is meant to occur
     */
    public int getDelay() {
        
        return minsFromNowToDisplayWindow ;
    }
    
    /**
     * this method displays a reminder window. This reminder 
     * window is displayed based on the position of the currently 
     * active reminder window
     */
    @Override
    public void run() {
        displayedPanel.setVisible(true);
    }
    
}
