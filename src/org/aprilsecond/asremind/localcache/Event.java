package org.aprilsecond.asremind.localcache;

import java.util.Calendar;

/**
 * This class represents an Event as stored in the XML file
 * with the Google calendar events.
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class Event {
    
    /**
     * stores the start time
     */
    private Calendar _startTime ;
    
    /**
     * stores the end time
     */
    private Calendar _stopTime ;
    
    /**
     * stores the activity message
     */
    private String _activity ;
    
    /**
     * stores the calendar
     */
    private int _calendarID ;
    
    /**
     * stores the reminder
     */
    private Calendar _reminderTime ;
    
    /**
     * null constructor ... will allow chaining
     */
    public Event(Calendar startTime, Calendar stopTime, String activity,
            int calendarID, Calendar reminderTime) {
        _startTime  = startTime ;
        _stopTime  = stopTime ;
        _activity = activity ;
        _calendarID = calendarID ;
        _reminderTime  = reminderTime ;
    }
    
    /**
     * get the start time
     */
    public Calendar getStartTime() {
        return _startTime ;
    }
    
    /**
     * set the start time
     */
    public void setStartTime(Calendar startTime) {
        _startTime = startTime ;
    }
    
    /**
     * get the stop time
     */
    public Calendar getStopTime() {
        return _stopTime ;
    }
    
    /**
     * set the stop time
     */
    public void setStopTime(Calendar stopTime) {
        _stopTime = stopTime ;
    }
    
    /**
     * get the activity message
     */
    public String getActivityMessage() {
        return _activity ;
    }
    
    /**
     * set the activity message
     */
    public void setActivityMessage(String activityMessage) {
        _activity = activityMessage ;
    }
    
    
    /**
     * get the calendar ID
     */
    public int getCalendarID() {
        return _calendarID ;
    }
    
    /**
     * set the calendar ID
     */
    public void setCalendarID(int calendarID) {
        _calendarID = calendarID ;
    }
    
    /**
     * get the reminder time 
     */
    public Calendar getReminderTime() {
        return _reminderTime ;
    }
    
    /**
     * set the reminder time
     */
    public void setReminderTime(Calendar reminderTime) {
        _reminderTime = reminderTime ;
    }
}
