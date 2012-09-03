package org.aprilsecond.asremind.UI;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.Calendar.ASEntry;
import org.aprilsecond.customuicomponents.ActivityPanel.Activity;
import org.aprilsecond.customuicomponents.ActivityPanel.ActivityMessage;
import org.aprilsecond.customuicomponents.ActivityPanel.ActivityPanel;
import org.aprilsecond.customuicomponents.BasePanels.BaseContentPanel;
import org.aprilsecond.customuicomponents.String.DateString;
import org.aprilsecond.customuicomponents.Utils.CalendarTime;
import org.aprilsecond.customuicomponents.clock.Clock;
import org.aprilsecond.customuicomponents.clock.ClockSelected;
import org.aprilsecond.customuicomponents.clock.ClockSelectedListener;
import org.aprilsecond.customuicomponents.dividers.Divider;

/**
 * This class stores the UI layer for the Activities page
 * displayed when the user selects the Activities page.
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class ActivitiesPane extends BaseContentPanel {
    
    /**
     * stores the clock component
     */
    private Clock clock ;
    
    /**
     * stores the vertical divider
     */
    private Divider verticalDivider ;
    
    /**
     * stores the horizontal divider
     */
    private Divider horizontalDivider ;
    
    /**
     * stores the date string for the application
     */
    private DateString dateString ;
    
    /**
     * stores the activity panel for the top component
     */
    private ActivityPanel currentActivityPanel ;
    
    /**
     * stores the activity panel for the lower activity panel
     */
    private ActivityPanel next2ActivitiesPanel ;
    
    /**
     * stores the layout for the component
     */
    private GroupLayout layout ;
    
    /**
     * stores the current activity
     */
    private Activity currentActivity ;
       
    /**
     * stores the first upcoming activity
     */
    private Activity comingUp1Activity ;
    
    /**
     * stores the second upcoming activity
     */
    private Activity comingUp2Activity ;
           
    /**
     * specifies that the time period is for a specific
     * time
     */
    private static final int SPECIFIC = 1 ;
    
    /**
     * specifies that the time period is for a range of 
     * times
     */
    private static final int RANGE = 2 ;
    
    /**
     * null constructor
     */
    public ActivitiesPane() {
        // draw the background
        super(); 
        
        // initialize the clock component & set the dimensions 
        // for the component
        clock = new Clock() ;
        clock.setPreferredSize(new Dimension(168, 188));
        clock.setMaximumSize(new Dimension(168, 188));
        clock.setMinimumSize(new Dimension(168, 188));
        
        // add the clockSelected listener
        clock.addClockSelectedListener(new ClockListener());
        
        // initialize the dividers
        verticalDivider 
                = new Divider(Divider.VERTICAL, new Dimension(265,2)) ;
        verticalDivider.setPreferredSize(new Dimension(265,2));
        
        horizontalDivider 
                = new  Divider(Divider.HORIZONTAL, new Dimension(2,253)) ;
        horizontalDivider.setPreferredSize(new Dimension(2,253));
        
        // create the current time string
        String currDate = CalendarTime.getCurrentDay() 
                + ", "
                + CalendarTime.getCurrentDate() ;
        
        // add the time string to the date String
        dateString = new DateString(currDate) ;
        dateString.setPreferredSize(new Dimension(168, 58));
        
        // initialize the current activity panel
        currentActivityPanel = new ActivityPanel() ;
        currentActivityPanel.setPreferredSize(new Dimension(227, 96));
        
        // intialize the activity panel for the 2 coming up 
        // activities
        next2ActivitiesPanel = new ActivityPanel() ;
        next2ActivitiesPanel.setPreferredSize(new Dimension(227, 126));
        
        // set the layout for the component
        layout = new GroupLayout(this) ;
    }
    
    /**
     * initializes the UI for the component
     */
    public void buildUI() {
        // initialize the currently activity Panel
        Calendar defaultTime = new GregorianCalendar(0,0,0,0,0) ;
                
        // initialize all the activities for the component
        currentActivity = new Activity(Activity.TITLE_ACTIVITY, 
                        defaultTime, defaultTime, 
                        "Loading current activity...") ;
        
        comingUp1Activity = new Activity(Activity.HIGHLIGHTED_ACTIVITY, 
                        defaultTime, defaultTime, 
                        "Loading...") ;
        
        comingUp2Activity = new Activity(Activity.HIGHLIGHTED_ACTIVITY, 
                        defaultTime, defaultTime, 
                        "Loading...") ;
                        
        // create the message from the activity
        ActivityMessage defaultMessage1 = new ActivityMessage(currentActivity) ;
        defaultMessage1.setActivityTitle("Currently");
        defaultMessage1.drawLowerBorder(false);
        
        // add the default activity to the current activity message
        currentActivityPanel.add(defaultMessage1) ;
        
        // add the default activity message to the next 2 activities panel
        ActivityMessage defaultMessage2 = new ActivityMessage(comingUp1Activity) ;
        ActivityMessage defaultMessage3 = new ActivityMessage(comingUp2Activity) ;
        defaultMessage3.drawLowerBorder(false);
        next2ActivitiesPanel.setLayout(new BoxLayout(next2ActivitiesPanel, 
                BoxLayout.Y_AXIS));
        next2ActivitiesPanel.add(defaultMessage2); 
        next2ActivitiesPanel.add(defaultMessage3) ;
        
        // create the panel to store the left side of the activities 
        // pane
        JPanel leftPanel = new JPanel() ;
        leftPanel.setOpaque(false);
        leftPanel.setLayout(null);
        leftPanel.setPreferredSize(new Dimension(230, 280));
        leftPanel.add(clock) ;
        leftPanel.add(dateString) ;
        clock.setBounds(16, 22, 168, 188);
        dateString.setBounds(18, 216, 189, 38);
        
        // create the top right panel
        JPanel topRightPanel = new JPanel() ;
        topRightPanel.setOpaque(false);
        topRightPanel.add(currentActivityPanel) ;
        topRightPanel.setPreferredSize(new Dimension(227, 96));    
        
        // create the bottom right panel below the the 
        // vertical divider
        JPanel bottomRightPanel = new JPanel() ;
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(next2ActivitiesPanel) ;
        bottomRightPanel.setPreferredSize(new Dimension(227, 116));
        
        // add the panel container with the topRight, 
        // divider and bottomLeft panels
        JPanel rPane = new JPanel() ;
        rPane.setOpaque(false);
        rPane.setPreferredSize(new Dimension(259, 253));
        rPane.setLayout(new BoxLayout(rPane, BoxLayout.Y_AXIS));
        rPane.add(topRightPanel) ;
        rPane.add(verticalDivider) ;
        rPane.add(bottomRightPanel) ;
        
        // set the Horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                // add the group with the clock and the date time string
                .addComponent(leftPanel)
                .addComponent(horizontalDivider)
                // add the group with the 2 activity panes and the vertical 
                // divider
                .addComponent(rPane)              
        );
        
        // set the vertical layout
        layout.setVerticalGroup(layout.createParallelGroup()
                // adds the group with the clock and date string
                .addComponent(leftPanel)
                // adds the group with the activity panels
                .addComponent(horizontalDivider)
                .addComponent(rPane)
                );
        
        // tick the clock
        clock.tick();
    }
    
    /**
     * overrides paint component to add the shadow effect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // create the overlapping 
        Rectangle currentArea = g.getClipBounds() ;
        Area area = new Area(currentArea) ;
        
        // create the ellipse
        Ellipse2D elipse = new Ellipse2D.Float(-63, -84, 661, 222) ;
        Area areaEllipse = new Area(elipse) ;
        area.subtract(areaEllipse);
        
        // draw the ellipse on the bg
        Graphics2D graphics = (Graphics2D) g ;
        
        // set antialias
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        // create a 2 stop linear gradient
        GradientPaint grad = new GradientPaint(0, 0, new Color(209, 216, 222), 
                0, getHeight(), new Color(0, 0, 0)) ;
        graphics.setPaint(grad);
        
        // set the transparency composite
        AlphaComposite initialComposite = (AlphaComposite) graphics.getComposite() ;
        AlphaComposite composite 
                = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3); 
        
        // fill the shape
        graphics.setComposite(composite);
        graphics.fill(area);
        
        // restore the composite
        graphics.setComposite(initialComposite);
    }
    
    /**
     * This method populates the activities panel with the content for the
     * various components in this panel. It gets the current activity 
     * and two activities that are coming up.
     * @param calendarsInfo contains an arraylist of all calendars
     */
    public void updatePane(ArrayList<ASCalendar> calendarsInfo) {        
        // create a default entry
        ASEntry defaultEntry = new ASEntry("No activity :(", Calendar.getInstance(), 
                    Calendar.getInstance(), null) ;
        
        // get the entry that is currently being undertaken
        // get the current time
        Calendar [] currTime = {Calendar.getInstance()} ;
        ArrayList<ASEntry> currEntry = getTimePeriodEntries(ActivitiesPane.SPECIFIC, 
                currTime, 1, 0, calendarsInfo) ;
        
        // add default entry if there is no 
        // default entry
        if(currEntry.isEmpty()) {            
            currEntry.add(defaultEntry);            
        }
        
        // get the next 2 upcoming activities
        // // set the end time to 1 year from now
        Calendar timeAfter2Months = Calendar.getInstance() ;
        timeAfter2Months.add(Calendar.YEAR, 1) ;
                 
        Calendar [] periodTimes = {Calendar.getInstance(), timeAfter2Months} ;
        ArrayList<ASEntry> next2Entries = getTimePeriodEntries(ActivitiesPane.RANGE, 
                periodTimes, 2, 1, calendarsInfo) ;
        
        // add default entries if there are no results
        if(next2Entries.isEmpty()) {
            next2Entries.add(defaultEntry);
            next2Entries.add(defaultEntry);
        } else if (next2Entries.size() == 1 ) {
            next2Entries.add(defaultEntry);
        }
         
        // set the current activity details
        currentActivity.setActivityMessage(currEntry.get(0).getTitle());
        currentActivity.setActivityStartTime(currEntry.get(0).getStartTime());
        currentActivity.setActivityStopTime(currEntry.get(0).getStopTime());
        
        // set the first coming up activity's details
        comingUp1Activity.setActivityMessage(next2Entries.get(0).getTitle());
        comingUp1Activity.setActivityStartTime(next2Entries.get(0).getStartTime());
        comingUp1Activity.setActivityStopTime(next2Entries.get(0).getStopTime());
        
        // set the second coming up activity's details
        comingUp2Activity.setActivityMessage(next2Entries.get(1).getTitle());
        comingUp2Activity.setActivityStartTime(next2Entries.get(1).getStartTime());
        comingUp2Activity.setActivityStopTime(next2Entries.get(1).getStopTime());  
        
        // show  that the panel has been updated
        MainAppPanel.activitiesPanelLoaded = true ;
    }
    
    /**
     * this method obtains a specified number of ASEntries that 
     * are being undertaken in the specified duration and
     * that are available in a specific ASCalendar object.
     * @param calendars arraylist of information for all the user's calendars
     * @param numberOfEntries Number of entries that we wish to obtain
     * @param  numberOfOffsetEntries Number of initial entries that we
     * should disregard
     * @param  periodTimes start and stop times for the period during which we
     * want to obtain calendar entries
     * @param timePeriodKind the kind of period for which we want the 
     * calendar entries.  It may be for a specific time or for a range of
     * times
     */
    private ArrayList<ASEntry> getTimePeriodEntries(int timePeriodKind, 
           Calendar[] periodTimes, int numberOfEntries, 
           int numberOfOffsetEntries, ArrayList<ASCalendar> calendars) {
        
        ArrayList<ASEntry> resultantEntries = new ArrayList<ASEntry>() ;
        
        if(null == calendars|| periodTimes.length < 1 ) {
            System.out.println("Calendars " + calendars 
                    +  " periodTimes.length : " + periodTimes.length) ;
            return null ;
        }
        
        // stores the start and stop times
        Calendar startTime ; 
        Calendar stopTime ;
        
        // extract the start and stop times
        if (timePeriodKind == ActivitiesPane.SPECIFIC) {
            startTime = stopTime = periodTimes[0] ;
        } else if (timePeriodKind == ActivitiesPane.RANGE) {
            if (periodTimes.length < 2) return null ;
            startTime = periodTimes[0] ;
            stopTime = periodTimes[1] ;
        } else {
            System.out.println("There is an error with the specified "
                    + "period times") ;
            return null ;
        }
        
        // loop through and obtain the ASEntries that are in the extracted start and stop times
        for (int calendarsCount = 0 ; calendarsCount < 
                calendars.size() ; calendarsCount++) {
            
            // get the entry details for the current calendar
            ArrayList<ASEntry> calEntries = calendars.get(calendarsCount).getEntries();
            
            for (int entriesCounter = 0 ; entriesCounter 
                    < calEntries.size() ; 
                    entriesCounter++) {
                ASEntry currEntry = calEntries.get(entriesCounter) ;
                
                // check if the entry is within the specified time 
                // range
                if (timePeriodKind == ActivitiesPane.RANGE) {
                    if (compareCalendarTimes(startTime, currEntry.getStartTime()) >= 0 && 
                            compareCalendarTimes(stopTime, currEntry.getStopTime()) <= 0) {
                        
                        // throw away the offset entries
                        if(entriesCounter < numberOfOffsetEntries) continue ;

                        // add an ASEntry that fits the criteria
                        resultantEntries.add(currEntry);

                        // check if the required number of entries has been obtained
                        if (resultantEntries.size() == numberOfEntries) {
                            break;
                        }
                    }        
                } else if (timePeriodKind == ActivitiesPane.SPECIFIC) {
                    if (compareCalendarTimes(startTime, currEntry.getStartTime()) <= 0 && 
                            compareCalendarTimes(stopTime, currEntry.getStopTime()) >= 0) {
                        
                        // throw away the offset entries
                        if(entriesCounter < numberOfOffsetEntries) continue ;

                        // add an ASEntry that fits the criteria
                        resultantEntries.add(currEntry);

                        // check if the required number of entries has been obtained
                        if (resultantEntries.size() == numberOfEntries) {
                            break;
                        }
                    }
                }
            }
        }
        
        return resultantEntries ;
    }
    
    /**
     * this method compares two calendar entries (to bypass bug with DatetypeConverter
     * that does not generate time offset field that is used in the 
     * compareTo method. It returns 0 if the two are equal, -1 if time2 is less
     * than time1 and 1 if time2 is greater than time1
     * @param time1 datum time
     * @param time2 compared to time
     */
    public int compareCalendarTimes(Calendar time1, Calendar time2) {
        int returnedValue = 0 ;
        
        // check the years
        if(time2.get(Calendar.YEAR) > time1.get(Calendar.YEAR)) {
            return 1 ;           
        } else if (time2.get(Calendar.YEAR) < time1.get(Calendar.YEAR)) {
            return -1 ;  
        } else if (time2.get(Calendar.YEAR) == time1.get(Calendar.YEAR)) {
            // check the months
            if (time2.get(Calendar.MONTH) > time1.get(Calendar.MONTH))  {
              return 1 ;   
            } else if (time2.get(Calendar.MONTH) < time1.get(Calendar.MONTH)) {
                return  -1 ; 
            } else if (time2.get(Calendar.MONTH) == time1.get(Calendar.MONTH)) {
                // check dates
                if (time2.get(Calendar.DATE) > time1.get(Calendar.DATE)) {
                    return  1 ; 
                } else if (time2.get(Calendar.DATE) < time1.get(Calendar.DATE)) {
                    return -1 ; 
                } else if (time2.get(Calendar.DATE) == time1.get(Calendar.DATE)) {
                    // check hours
                    if (time2.get(Calendar.HOUR_OF_DAY) > time1.get(Calendar.HOUR_OF_DAY)) {
                        return 1 ;
                    } else if (time2.get(Calendar.HOUR_OF_DAY) < time1.get(Calendar.HOUR_OF_DAY)) {
                        return -1 ;
                    } else if (time2.get(Calendar.HOUR_OF_DAY) == time1.get(Calendar.HOUR_OF_DAY)) {
                        // check minutes
                        if (time2.get(Calendar.MINUTE) > time1.get(Calendar.MINUTE)) {
                            return 1 ;
                        } else if (time2.get(Calendar.MINUTE) < time1.get(Calendar.MINUTE)) {
                            return -1 ;
                        } else if (time2.get(Calendar.MINUTE) == time1.get(Calendar.MINUTE)) {
                            return 0 ; // the 2 times are the same
                        }
                    }
                }
            }   
        }
        
        return returnedValue ;                
    }

    /**
     * makes the sound of the clock turn on and off when the clock icon is
     * selected     
     * @param cs ClockSelectedEvent
     */
    private class ClockListener
            implements ClockSelectedListener {

        @Override
        public void clockSelected(ClockSelected cs) {
            Clock c = (Clock) cs.getSource();
            c.toggleClockSound();
        }
    } 
    
     /**
     * test
     */
    public static void main(String[] args) {
        Runnable displayPane = new Runnable() {

            @Override
            public void run() {
                ActivitiesPane pane = new ActivitiesPane() ;
                pane.buildUI();
                
                JFrame frame = new JFrame("Activities Pane") ;
                frame.add(pane) ;
                frame.setSize(552, 320);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }; 
        
        EventQueue.invokeLater(displayPane);
    }
}
