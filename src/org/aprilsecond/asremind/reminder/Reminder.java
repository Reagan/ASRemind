package org.aprilsecond.asremind.reminder;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.Calendar.ASEntry;
import org.aprilsecond.customuicomponents.ContainerPanels.MinimalBasePanel;

/**
 * This class :-
 * <ol>
 *  <li>Manages the calendar entries' times</li>
 *  <li>Monitors the calendar entries' reminders</li>
 *  <li>Manages the display of the minimal reminder window application</li>
 * </ol>
 * By default, this class displays a minimal panel when the main window 
 * application is minimized displaying the current activity being tracked.
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class Reminder {
    
    /**
     * stores the calendar entries for the calendars
     */
    private ArrayList<ASCalendar> calendars ;
    
    /**
     * stores the number of displayed reminder windows
     */
    public static ArrayList<MinimalBasePanel> displayedReminderPanels ;
    
    /**
     * stores tasks that will display panel
     */
    private ArrayList<ReminderWindowDisplayTask> tasksList ;
    
    /**
     * stores the executor service that will run tasks to 
     * display the various windows
     */
    private ScheduledExecutorService threadExecutor ; 
    
    /**
     * stores toolkit object to calculate available screen 
     * area to display reminder window
     */
    private Toolkit toolKit ;
    
    /**
     * stores the dimensions for the screen height
     */
    private Dimension screenSize ;
    
    /**
     * stores the width of screen
     */
    private int screenHeight ; 
    
    /**
     * stores the height of the screen
     */
    private int screenWidth ;
    
    /**
     * constructor initializes calendar entries
     */
    public Reminder(ArrayList<ASCalendar> calendarsInfo) {
        
        // initialize the calendar information
        calendars = calendarsInfo ;
        
        // initialize the store for the displayed panels
        displayedReminderPanels = new ArrayList<MinimalBasePanel>() ;
        
        // initialize task list
        tasksList = new ArrayList<ReminderWindowDisplayTask>() ;
        
        // initialize the Toolkit application
        toolKit = Toolkit.getDefaultToolkit(); 
        screenSize = toolKit.getScreenSize();
        screenHeight = screenSize.height;
        screenWidth = screenSize.width;
        
        // initialize the thread executor
        initializeThreadPoolExecutor() ;       
    }
    
    /**
     * this method initializes a thread pool executor. code 
     * adapted from @link{http://www.javacodegeeks.com/2011/12/using-threadpoolexecutor-to-parallelize.html}
     */
    private void initializeThreadPoolExecutor() {
        // calculate the number of threads to be created 
        int scaleFactor = 1 ; // each cpu should only run one thread at a time
        int cpus = Runtime.getRuntime().availableProcessors() ;
        int maxThreads = ((cpus * scaleFactor) > 0 ) ? 
                (cpus * scaleFactor) : 1 ;
        
        // initialize the thread pool
        threadExecutor = Executors.newScheduledThreadPool(maxThreads) ;
    }
    
    /**
     * this method tracks the various calendar entries and 
     * displays a reminder window when the activities are due based on
     * the various reminders for the entries
     */
    public void activateReminders() {
        
        // loop througn the reminders and obtain 
        // the entries for the calendars
        for (int calendarsCounter = 0 ; calendarsCounter < calendars.size() ;
                calendarsCounter++) {
            
            // get a list of entries for this calendar
            ArrayList<ASEntry> calEntries 
                    = calendars.get(calendarsCounter).getEntries() ;
            
            // loop through the entries creating tasks
            // for them if there are reminders for them
            for (int entriesCounter = 0 ; entriesCounter < calEntries.size();
                    entriesCounter++) {
               
                // get the current entry
                ASEntry currEntry = calEntries.get(entriesCounter) ;
                if (currEntry.getReminders().size() > 0
                        && currEntry.getStartTime().after(Calendar.getInstance())) {
                    
                    // create the runnable task reminder
                    Point displayedLoc = getAvailableScreenSpace() ;
                    ReminderWindowDisplayTask task = 
                            new ReminderWindowDisplayTask(currEntry,
                            displayedLoc) ;
                    
                    // add to task list
                    tasksList.add(task);
                    
                    // add it to the executor service
                    threadExecutor.schedule(task, task.getDelay(), TimeUnit.MINUTES);
                }
            }
        }
        
        // make sure that the used threads shutdown
        threadExecutor.shutdown();
    }   
    
     /**
     * this method gets available screen space to display 
     * a MinimalBasePanel
     */
    private Point getAvailableScreenSpace() {
        Point availableLocation = new Point(0, -112) ;
        
        // find out if there is any space vertically
        for (int tasksCounter = 0 ; 
                tasksCounter < tasksList.size();
                tasksCounter++) {
            Point currLocation = tasksList.get(tasksCounter).displayLocation ;
            
            // get the next dimensions for displayed task page 
            availableLocation.y = (currLocation.y > availableLocation.y) ? currLocation.y : 
                   availableLocation.y ;
            availableLocation.x = (currLocation.x > availableLocation.x) ? currLocation.x : 
                    availableLocation.x ;
                  
        }
        
         availableLocation.y += 112 ;
         
        // increment the Y dimension
        // @info height for each base panel is 102
        if ((availableLocation.y + 10) > screenHeight) {
            // then display the window on the X axis
            availableLocation.y = 0;
            availableLocation.x += 10 ;
        }   
        
        // find out if there is any space horizontally
        return availableLocation ;
    }
}
