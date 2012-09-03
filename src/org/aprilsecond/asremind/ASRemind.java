package org.aprilsecond.asremind;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.UI.MainAppPanel;
import org.aprilsecond.asremind.UI.Panes;
import org.aprilsecond.asremind.configurations.Configurations;
import org.aprilsecond.asremind.localcache.Cache;
import org.aprilsecond.asremind.localcache.TrackMessage;
import org.aprilsecond.asremind.reminder.Reminder;
import org.aprilsecond.asremind.utils.Alerter;
import org.aprilsecond.asremind.utils.InternetConnectivity;
import org.aprilsecond.asremind.utils.ModalMessages;
import org.aprilsecond.asremind.utils.NotifyingThread.ThreadCompleteListener;
import org.aprilsecond.googleoauthwrapper.GoogleCalendar;
 
/**
 * <p>
 * This is the main class and application entry point to ASRemind.
 * It contains the EventQueue that calls all files. All the operations 
 * contained here are in the file {@link documents/Workflows.dia} in the
 * workflow entitled "APPLICATION LOADING".
 * </p>
 * 
 * <p>
 * The look for this application has been inspired by a UI for a concept
 * for automating a home or building {@link http://www.altia.com/demos.php}
 * </p>
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class ASRemind {

    /**
     * stores whether the application has initialized or not
     */
    public static boolean initialized = false ;
    
    /**
     * stores on whether or not the application
     * has access to the internet
     */
    public static boolean isConnectedToInternet = false ;
    
    /**
     * determines whether or not the status on whether the 
     * application has access to the internet
     */
    public static boolean internetConnectionStatusDetermined = false ;
    
    /**
     * stores whether or not all calendar items have 
     * been obtained
     */
    public static boolean allCacheCalendarAndTrackMessageEntriesObtained = false ; 
    
    /**
     * stores the current active page
     */
    public static int currPage = Panes.ACTIVITIES_PANE ;
    
    /**
     * stores the loading message
     */
    public final String LOADING_MESSAGE = "Loading" ;
    
    /**
     * stores the checking network status message
     */
    public final String CHECK_INTERNET_ACCESS_MESSAGE = "there internet?" ;
    
    /**
     * stores the retrieving entries message
     */
    public final String GETTING_CALENDAR_ENTRIES_MESSAGE = "getting 'em entries" ;
    
    /** 
     * stores the populating messages entries
     */
    public final String POPULATING_CALENDAR_ENTRIES_MESSAGE = "got 'em! loading the UI" ;
    
    /**
     * stores the message indicating that we are loading from the cache
     */
    public final String INITIALIZING_LOCAL_CACHE_MESSAGE = "loading cache :/" ;
    
    /**
     * stores the Modal Messages dialog class
     */
    public ModalMessages modal ;
    
    /**
     * stores the main application base panel
     */
    private MainAppPanel baseAppPanel ;
    
    /**
     * stores the configuration object
     */
    private Configurations configs ;
    
    /**
     * stores the Google API authentication object
     */
    private GoogleCalendar gCalendar ;
    
    /**
     * stores the Access token property granted for a request
     */
    private String ACCESS_TOKEN_PROPERTY = "ACCESS_TOKEN" ;
    
    /**
     * stores the refresh token property name 
     */
    private String REFRESH_TOKEN_PROPERTY = "REFRESH_TOKEN" ;
    
    /**
     * stores the path to the config file 
     * with the client ID and secret to the application
     */
    private final static String APP_CONFIG
            = "app.config" ;    
    /**
     * stores the path to the config file to the application
     */
    private static String APP_CONFIG_FILE_PATH 
            = ("settings/configs/" + APP_CONFIG).replace('/',
                    File.separatorChar) ;
    
    /**
     * stores the config for the delay 
     * before the UI repaints
     */
    private String REPAINT_PERIOD_PROPERTY = "REPAINT_PERIOD" ;
    
    /**
     * stores the repaint period
     */
    private int repaintPeriod = 10 ;
    
    /**
     * stores the object to test for connectivity
     */
    private static InternetConnectivity internetTest ;
    
    /**
     * stores the local cache for the application
     */
    private Cache localCache ;
    
    /**
     * stores calendars and calendar information for the current 
     * user
     */
    private ArrayList<ASCalendar> calendars ; 
    
    /**
     * stores the track messages as stored in the local
     * cache
     */
    private ArrayList<TrackMessage> trackMessages ;
    
    /**
     * stores the reminder module
     */
    private Reminder reminder ;
       
    public ASRemind() {        
        baseAppPanel = new MainAppPanel() ;        
        modal = ModalMessages.getInstance(baseAppPanel) ;    
        configs = Configurations.getInstance(APP_CONFIG_FILE_PATH) ;        
        repaintPeriod = getRepaintPeriod() ;            
    }        
    
     /**
     * initializes the application
     */
    private void startApplication() {
        
        baseAppPanel.setVisible(true);        
        displayModalMessage(LOADING_MESSAGE);    
        System.out.println("Repaint called " + repaintPeriod) ;
                
        Timer repaint = new Timer(repaintPeriod, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                                                
                Thread r = new Thread() {                    
                    @Override
                    public void run() {
                        update();
                    }
                };                
                r.start();    
            }
        }) ;
        
        repaint.setInitialDelay(0);
        repaint.start() ; 
       
    }
            
    /**
     * this application loads all the data for the application
     * loading it either from the internet or the local 
     * cache where internet access is not present
     */
    private void update() {
                
        displayModalMessage(CHECK_INTERNET_ACCESS_MESSAGE);        
        testInternetConnectivity();                
        
        displayModalMessage(INITIALIZING_LOCAL_CACHE_MESSAGE);
        loadCache();
        trackMessages = localCache.getTrackCache() ;                
                               
        if (hasGoogleCalendarProfile()) {            
            if (isConnectedToInternet) {                               
                gCalendar = GoogleCalendar.getInstance(configs) ;                                                
                allCacheCalendarAndTrackMessageEntriesObtained = false ;                                    
                displayModalMessage(GETTING_CALENDAR_ENTRIES_MESSAGE);  
                getActualCalendarInfo();
                populateUI() ;                                                                
                localCache.saveCalendarCache(calendars) ;
                                                
            } else if (!isConnectedToInternet) {                                                
                calendars = localCache.getCalendarsCache() ;                
                populateUI();                                
            }   
            
            loadReminderModule();            
            gCalendar = null ;
            
        } else {            
           if (isConnectedToInternet) {
                gCalendar = GoogleCalendar.getInstance(configs) ;
                getActualCalendarInfo();
                populateUI() ;                                                                
                localCache.saveCalendarCache(calendars) ;                
                
                loadReminderModule();
                gCalendar = null ;
            }  else if (!isConnectedToInternet) {
                Alerter.displayErrorMessage("Internet Access required",
                        "Please connect to the internet "
                        + "to update your calendar");                
            } 
        }      
    }       
    
    /**
     * this method tests for internet connectivity
     */
    private void testInternetConnectivity() {
        
        internetTest = InternetConnectivity.getInstance();
        
        internetTest.addListener(new ThreadCompleteListener() {
            @Override
            public void notifyOfThreadComplete(Runnable thread, boolean statusOfOperation) {
                isConnectedToInternet = statusOfOperation;
                internetConnectionStatusDetermined = true;
                System.out.println("\nThread complete [status] " + statusOfOperation);
            }
        });
        
        internetTest.start();
        try {
            internetTest.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ASRemind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * gets the actual calendar entries
     */
    private void getActualCalendarInfo() {       
        
        // getGoogleCalendarInstance();        
        Thread getCalendarInfo = new Thread() {
            @Override
            public void run() {
                calendars = gCalendar.getCalendarsForUser();   
                allCacheCalendarAndTrackMessageEntriesObtained = true ;
            }
        };

        getCalendarInfo.start();
        try {
            getCalendarInfo.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ASRemind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadReminderModule() {
        reminder = new Reminder(calendars) ; 
        reminder.activateReminders();    
    }
    
    private boolean hasGoogleCalendarProfile() {
        return !("".equals(configs.getValueOf(ACCESS_TOKEN_PROPERTY)))
                && !("".equals(configs.getValueOf(REFRESH_TOKEN_PROPERTY))) ;
    }
    
    private void populateUI() {    
        
        displayModalMessage(POPULATING_CALENDAR_ENTRIES_MESSAGE);
        
        Thread updatePanes = new Thread() {
            @Override
            public void run() {        
                baseAppPanel.updatePanes(calendars, trackMessages);
            }
        };

        updatePanes.start();
        try {
            updatePanes.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ASRemind.class.getName()).log(Level.SEVERE, null, ex);
        }
                
       
        ASRemind.initialized = true ;
        
        hideModalMessage();
    }
    
    private int convertMinutesToMilliseconds(int minutes) {
        return minutes * 60 * 1000 ;
    }
        
    private int getRepaintPeriod() {
        int rPeriod = Integer
                    .parseInt(configs.getValueOf(REPAINT_PERIOD_PROPERTY)) ;
        
        if(rPeriod > 0) {
            repaintPeriod = rPeriod ;
        } 
        
        return convertMinutesToMilliseconds(repaintPeriod) ;
    }
    
    /**
     * initializes and loads the current stored cache for the 
     * application. Caches are XML files with calendar and track 
     * information
     */
    private Cache loadCache() {        
        localCache = Cache.loadCache() ;
        return localCache ;
    }
    
    private void displayModalMessage(String message) {
        if (!ASRemind.initialized) {
            modal.displayModalMessage(message);
        }        
    }
    
    private void hideModalMessage() {
        modal.hideModalMessage();
    }
    
    /**
     * Run the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
       Runnable app = new Runnable () {
            @Override
            public void run() {
                // initialize the ASRemind application
                ASRemind app = new ASRemind() ;
                
                // initialize the application
                app.startApplication() ;                
            }           
       };
       
       EventQueue.invokeLater(app);
    }        
}
