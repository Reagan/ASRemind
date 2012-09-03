package org.aprilsecond.asremind.UI;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.Calendar.ASEntry;
import org.aprilsecond.customuicomponents.BasePanels.BaseContentPanel;
import org.aprilsecond.customuicomponents.ContainerPanels.MinimalBasePanel;
import org.aprilsecond.customuicomponents.List.CustomList.SimpleList;
import org.aprilsecond.customuicomponents.List.CustomList.SimpleListModel;
import org.aprilsecond.customuicomponents.List.ListItem.SimpleListItem;
import org.aprilsecond.customuicomponents.ScrollPane.ItemSelected;
import org.aprilsecond.customuicomponents.ScrollPane.ItemSelectedListener;
import org.aprilsecond.customuicomponents.ScrollPane.ScrollPane;
import org.aprilsecond.customuicomponents.TimeTable.Activity;
import org.aprilsecond.customuicomponents.TimeTable.ActivityItem;
import org.aprilsecond.customuicomponents.TimeTable.Timetable;
import org.aprilsecond.customuicomponents.TimeTable.TimetableModel;
import org.aprilsecond.customuicomponents.Utils.CalendarTime;
import org.aprilsecond.customuicomponents.checkboxdropdown.CheckBoxDropDown;
import org.aprilsecond.customuicomponents.checkboxdropdown.CheckBoxDropDownModel;
import org.aprilsecond.customuicomponents.checkboxdropdown.CheckBoxPopupItemSelected;
import org.aprilsecond.customuicomponents.checkboxdropdown.CheckBoxPopupItemSelectedListener;

/**
 * This class extends the BaseContentPanel and implements the schedule
 * page for the application
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com
 */
public class SchedulePane extends BaseContentPanel {
    
    /**
     * stores the timetable component
     */
    private Timetable scheduleTable ;
    
    /**
     * stores the custom list
     */
    private SimpleList daysList ;
    
    /**
     * stores the drop down menu with the list of 
     * calendars 
     */
    private CheckBoxDropDown calendarsDropDown ;
    
    /**
     * stores the model for the drop down combo
     */
    private CheckBoxDropDownModel calendarsDropDownModel ;
    
    /**
     * stores the title for the calendars drop down
     */
    private String dropDownTitle = "calendars" ;
    
    /**
     * stores the right panel with the drop down and the custom list
     */
    private JPanel rightPanel ;
 
    /**
     * stores the scroll pane with the timetable component
     */
    private ScrollPane scrollPane ;
    
    /**
     * stores the scroll pane for the days list
     */
    private ScrollPane listScrollPane ;
    
    /**
     * stores the components for the timetable model
     */
    private ArrayList<Activity> components ;
    
    /**
     * stores the displayed list for the simple list
     */
    private ArrayList<String> actualDaysList = new ArrayList<String>() ;
    
    /**
     * stores the model for the timetable component
     */
    private TimetableModel model ;
    
    /**
     * stores the current day that the schedule pane is 
     * displaying entries for. Defaults to the current
     * day for the application
     */
    private Calendar currentDay = Calendar.getInstance() ;    
    
    /**
     * stores the calendars displayed on the time table 
     * component
     */
    private ArrayList<String> calendarNames ; 
    
    /**
     * stores the data for the component
     */
    private ArrayList<ASCalendar> scheduleData ;
    
    /**
     * stores the displayed minimal panel
     */
    private MinimalBasePanel timetableItemPanel ;
    
    /**
     * constructor initializes the component
     */
    public SchedulePane() {
        // draw the background
        super() ;
        
        // initialize the activities list
        components = new ArrayList<Activity> () ;
        
        // initialize the scroll pane
        scrollPane = new ScrollPane() ;
        
        // initialize the right panel
        rightPanel = new JPanel(new BorderLayout()) ;
        
        // set the dimensions for the right panel
        rightPanel.setPreferredSize(new Dimension(163, 280));        
        
        // initialize the days list
        ArrayList<String> loadingString = new ArrayList<String>(1) ;
        loadingString.add("Loading...") ;
        
        // add the days list to the simple list
        daysList = new SimpleList() ;
        daysList.setListDimensions(new Dimension(163, 258));
        
        // initialize the container that will store all the info
        scheduleData = new ArrayList<ASCalendar>() ;
   }    
    
    /**
     * initializes the UI for the component
     */
    public void buildUI() {
        
        // initialize a default model
        initializeScheduleModel();
        
        // create a model for the timetable component
        if ( null != model) {
            scheduleTable = new Timetable(model) ;
        }
        
        // set the schedule as the view port 
        // for the scrollpane
        scrollPane.setViewport(scheduleTable);
        
        // set the dimensions for the scroll pane
        scrollPane.setPreferredSize(new Dimension(373, 281));
        
        // add the events listener to the table 
        scrollPane.addItemSelectedListener(new TimeTableItemSelected()) ;
        
        // set the simple list to its scroll pane
        listScrollPane = new ScrollPane() ;
        listScrollPane.setViewport(daysList);
        
         // add the events listener to the list of dates
        listScrollPane.addItemSelectedListener(new ListDateItemSelected());
        
        // initialise the list of calendars 
        calendarsDropDown = new CheckBoxDropDown() ;
        calendarsDropDown
                .addCheckBoxPopupItemSelectedListener(new PopupItemSelectedListener());
        
        // add the drop down and the simple list to the 
        // right panel
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);    // make sure that the right panel is 
                                        // transparent
        rightPanel.add(calendarsDropDown, BorderLayout.NORTH) ;
        rightPanel.add(listScrollPane, BorderLayout.CENTER) ;
        
        // set the layout for the entire component
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        // add the timetable component
        add(scrollPane) ;
        
        // add the right Panel
        add(rightPanel) ;
    }
    
    /**
     * adds a  model to the time table component
     */
    public void setTimetableModel(TimetableModel scheduleModel) {
        if (null != scheduleModel) {
            scheduleTable.setTimetableModel(scheduleModel);
        }
    }
    
    /**
     * sets the list of days displayed on the simple list
     */
    public void setDaysForList(ArrayList<String> days)  {
        if (days != null && days.size() > 0)  {
            actualDaysList = days ;
        }
    }
    
    /**
     * initializes the model  with dummy data
     */
    private void initializeScheduleModel() {       
        // add the components array list to the components
        model = new TimetableModel() ;         
    }
    
    /**
     * gets the list of days displayed on the simple list
     */
    public ArrayList<String> getDaysForList() {
        return actualDaysList ;
    }
    
    /**
     * This method populates the activities panel with the content for the
     * various components for the specific day requested
     * @param calendarInfo This is an arraylist of all the calendars' content
     * for the user
     */
    public void updatePane(Calendar day, ArrayList<ASCalendar> calendarsInfo) {
        
        // update the current day 
        currentDay = day ;
        
        // store calendar info
        scheduleData = calendarsInfo ;
        
        // update the custom drop down with the list of calendars
        // and their calendars information
        updateCalendarsListAndAddToPopup(calendarsInfo) ;
        
        // update the timetable component
        updateTimetable(currentDay, calendarNames,
                calendarsInfo) ;
        
        // create unreferenced instance of current day
        Calendar newDate = cloneDate(currentDay);
        
        // update the side pane
        updateSideList(newDate) ;

        // show that the schedule pane has been updated
        MainAppPanel.schedulePanelLoaded = true ;
    }
    
    /**
     * this clones the current date
     */
    private Calendar cloneDate(Calendar day) {
        return new GregorianCalendar(day.get(Calendar.YEAR),
                day.get(Calendar.MONTH), day.get(Calendar.DATE)) ;  
    }
    
    /**
     * this updates the timetable component
     * @param day
     * @param calendarsInfo 
     */
    private void updateTimetable(Calendar day,
            ArrayList<String> cNames, ArrayList<ASCalendar> calendarsInfo) {
        
        // set the current day
        currentDay = day ; 
        
        // create & set timetable model from events
        TimetableModel scheduleModel 
                = createTimetableModelForDayFromEvents(currentDay, 
                cNames, calendarsInfo) ;
        
        // add the new model to the table
        setTimetableModel(scheduleModel);   
        
        // redraw the table based on the new model
        scheduleTable.drawTable();
    }
    
    /**
     * this method obtains the activities for the day from the 
     * total list of calendar events
     */
    private TimetableModel
            createTimetableModelForDayFromEvents(Calendar day, 
            ArrayList<String> calendar, ArrayList<ASCalendar> calendarsInfo) {
        
        // stores the resultant model
        TimetableModel newModel  ;
        
        // create the store for the created activities
        ArrayList<Activity> daysActivities = new ArrayList<Activity>() ;
        
        // loop through obtaining all entries for the specific day
        ArrayList<ASEntry> daysEntries = getEntriesForDay(day,
                calendar, calendarsInfo) ;
        
        // loop through the obtained entries
        // casting them to activities and adding them
        // to the activities arraylist
        for (int entriesCounter = 0 ;entriesCounter < daysEntries.size() ;
                    entriesCounter++) {
            ASEntry currEntry = daysEntries.get(entriesCounter) ;
            Activity currActivity  = new Activity(currEntry.getTitle(), 
                    currEntry.getStartTime(), currEntry.getStopTime()) ;
            
            // add to the result
            daysActivities.add(currActivity);
        }      
        
        // create the timetable model
        newModel = new TimetableModel(daysActivities) ;
        return newModel ;
    }
    
    /**
     * This method updates the side list to display
     * a list of the days for the week that the user is currently
     * in.
     * @param currDate current date
     */
    private void updateSideList(Calendar currDate) {
       
        // get the starting day for week
        int startOfTheWeekDate = getStartingDayOfWeek(currDate);
        
        // stores the days of the week
        ArrayList<String> items = new ArrayList<String>() ;
        
        // stores the aliases for the simple list items
        ArrayList<Object> aliasDatesForItems = new ArrayList<Object>() ;
        
        Calendar clonedDate = cloneDate(currDate) ;
        
        // loop through displaying the dates for 
        // the current week
        for ( int weekDaysCounter = 0 ; weekDaysCounter < 7 ;
                weekDaysCounter++, startOfTheWeekDate++) {
            
            String currMonth = CalendarTime
                    .MONTHS_OF_THE_YEAR[clonedDate.get(Calendar.MONTH)];
            
            String currDay = CalendarTime.DAYS_OF_WEEK[weekDaysCounter]
                    + " ("
                    + String.valueOf(startOfTheWeekDate)
                    + " "
                    + currMonth.substring(0, 3)
                    + ")";
            
            // add to container
            items.add(currDay);  
            
            // clone current date for item and add it 
            Calendar cDate = cloneDate(clonedDate) ;
            aliasDatesForItems.add(cDate);
            
            // increment the date
            clonedDate.add(Calendar.DATE, +1);
        }
  
        // initialize the model
        SimpleListModel simpleListModel = new SimpleListModel(items, aliasDatesForItems);
       
        // add the model to the days List
        daysList.setSimpleListModel(simpleListModel);
    }
    
    /**
     * get the starting day of the week
     */
    private int getStartingDayOfWeek(Calendar currDate) {
        // for the week that the user is currently in 
        int week = currDate.get(Calendar.WEEK_OF_YEAR);
        int year = currDate.get(Calendar.YEAR);

        // reset the calendar to the current week
        currDate.setFirstDayOfWeek(Calendar.SUNDAY);
        currDate.clear();
        currDate.set(Calendar.WEEK_OF_YEAR, week);
        currDate.set(Calendar.YEAR, year);

        // get the start of the week date
        int startOfTheWeekDate = currDate.get(Calendar.DATE);
        return startOfTheWeekDate ;
    }
    
    /**
     * This method updates the list of drop down menus
     * to show all the calendar names for the user
     * @param calendars contains information on the calendars
     * for the user
     */
    private void 
            updateCalendarsListAndAddToPopup(ArrayList<ASCalendar> calendars) {
        
        ConcurrentHashMap<String,Color> calendarListItems 
                =  new ConcurrentHashMap<String, Color>() ;
        
        // initialize all the calendar names
        calendarNames = new ArrayList<String>() ;
        
        // loop through and get the list of calendars
        for (int calendarsCounter = 0 ; calendarsCounter < calendars.size() ;
                calendarsCounter++) {
            
            // get the calendar name and color
            String currCalendarName = calendars.get(calendarsCounter).getTitle() ;
            Color currCalendarColor = calendars.get(calendarsCounter).getColor() ;
            
            // add them to collection
            calendarListItems.put(currCalendarName,currCalendarColor);
            
            // add to the list of calendar names
            calendarNames.add(currCalendarName);
        }
        
        // add the calendars names to model
        calendarsDropDownModel = new CheckBoxDropDownModel(dropDownTitle, 
                calendarListItems) ;
        
        // add model to component
        calendarsDropDown.setCompModel(calendarsDropDownModel);
    }
    
    /**
     * This method gets the calendar entries for a specific day
     * @param calendarsInfo list of all calendars and information 
     * on them for a specific user
     */
    public ArrayList<ASEntry> 
            getEntriesForDay(Calendar day, 
            ArrayList<String> displayedCalendars, ArrayList<ASCalendar> calendars) {
        
        // stores the resultant entries
        ArrayList<ASEntry> resultantEntries = new ArrayList<ASEntry>() ;
        
        // loop through obtaining the entries for the specified day
        for (int calendarsCount = 0 ; calendarsCount < 
                calendars.size() ; calendarsCount++) {
            
            // get current calendar
            ASCalendar currCal = calendars.get(calendarsCount);
            
            if (displayedCalendars.contains(currCal.getTitle())) {                
                
                // get the entry details for the current calendar
                ArrayList<ASEntry> calEntries = currCal.getEntries();

                for (int entriesCounter = 0 ; entriesCounter 
                        < calEntries.size() ; 
                        entriesCounter++) {
                    ASEntry currEntry = calEntries.get(entriesCounter) ;
                    
                    // check if the entry is within the specified time 
                    // range
                    if(isOnSameDay(currEntry.getStartTime(),day)) {
                        resultantEntries.add(currEntry);
                    }                
                }            
            }
        }
        
        return resultantEntries ;
    }
    
    /**
     * this method tests two calendar entries to determine if they 
     * fall on the same day
     */
    public boolean isOnSameDay(Calendar time1, Calendar time2) {    
        if (time1.get(Calendar.YEAR) == time2.get(Calendar.YEAR)) {            
            if (time1.get(Calendar.MONTH) == time2.get(Calendar.MONTH)) {                
                if (time1.get(Calendar.WEEK_OF_MONTH) == time2.get(Calendar.WEEK_OF_MONTH)) {                    
                    if (time1.get(Calendar.DAY_OF_WEEK) == time2.get(Calendar.DAY_OF_WEEK)) {                        
                        return true ;
                    }
                }
            }
        }
        
        return false ;
    }
       
    /**
     * Adds the set of calendars required for display 
     * and removes not for display
     * @param evt checkbox selected event  
     */
    private class PopupItemSelectedListener 
        implements CheckBoxPopupItemSelectedListener  {

        @Override
        public void checkBoxPopupItemSelected(CheckBoxPopupItemSelected evt) {
            String selectedCal = evt.getItemSelected() ;

            if (evt.getSelectedStatus()
                    == CheckBoxPopupItemSelected.SELECTED) {            
                if (!calendarNames.contains(selectedCal)) {
                    calendarNames.add(selectedCal);                
                }
            } else if (evt.getSelectedStatus()
                    == CheckBoxPopupItemSelected.DESELECTED) {
                if(calendarNames.contains(selectedCal)) {
                    calendarNames.remove(selectedCal);
                }
            }

            updateTimetable(currentDay, calendarNames, scheduleData);
        }
    }
    
    /**
     * displays more information on the timetable item
     * that has been selected
     */
    private void displayItemDetails(String startAndStopTime, String activity, Point location ) {
        timetableItemPanel = new MinimalBasePanel(startAndStopTime, activity, location) ;
        timetableItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                MinimalBasePanel panel = (MinimalBasePanel) me.getSource() ;
                panel.setVisible(false);
                panel.dispose();
            }
        });
        
        timetableItemPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent ev) {
                MinimalBasePanel panel = (MinimalBasePanel) ev.getSource() ;
                panel.setVisible(false);
                panel.dispose();
            }
        });
        timetableItemPanel.displayPanel();
    }
    
    /**
     * Class responsible for behaviors when a list item is
     * selected 
     * @param evt event generated when an event is selected
     */
    private class ListDateItemSelected 
      implements ItemSelectedListener {
        
        @Override
        public void itemSelected(ItemSelected evt) {
            // get the selected calendar Item
            SimpleListItem sListItem = (SimpleListItem) evt.getSource();

            // get the item date
            Calendar itemDate = (Calendar) sListItem.getComponentIDAlias();

            // make sure the timetable reflects the change
            updateTimetable(itemDate, calendarNames, scheduleData);
        }
    }
    
    /**
     * Class responsible for behaviors when any of the items
     * in the timetable is selected
     */
    private class TimeTableItemSelected
        implements ItemSelectedListener {
        
        @Override
        public void itemSelected(ItemSelected evt) {
            ActivityItem p = (ActivityItem) evt.getSource() ;
            
            // get the item properties
            Calendar startTime = p.getActivity().getStartTime() ;
            Calendar stopTime = p.getActivity().getStopTime() ;
            String message = p.getActivity().getActivity() ;
            
            // concatenate the start & stop times
            String startAndStopTime
                    = CalendarTime.displayPrettyStartAndStopTime(startTime, stopTime) ;
            
            // display the times 
            displayItemDetails(startAndStopTime, message, new Point(0,0));
        }
    }              
        
     /**
     * test
     */
    public static void main(String[] args) {
        Runnable displayPane = new Runnable() {

            @Override
            public void run() {
                SchedulePane pane = new SchedulePane() ;
                pane.buildUI();
                
                JFrame frame = new JFrame("Schedule Pane") ;
                frame.add(pane) ;
                frame.setSize(552, 320);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }; 
        
        EventQueue.invokeLater(displayPane);
    }    
}
