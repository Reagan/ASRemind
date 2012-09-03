package org.aprilsecond.asremind.localcache;

import java.util.Calendar;

/**
 * This class defines track messages as stored
 * in the cache (Cache/Track/track.xml). 
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class TrackMessage {
    
    /**
     * stores the time
     */
    private Calendar messageTime ;
    
    /**
     * stores the summary
     */
    private String messageSummary ;
    
    /**
     * constructor initializes track message components
     */
    public  TrackMessage(Calendar _messageTime, String _messageSummary) {
        messageTime = _messageTime ;
        messageSummary = _messageSummary ;
    } 
    
    /**
     * sets the message time
     */
    public void setMessageTime(Calendar _messageTime) {
        messageTime = _messageTime ;
    }
    
    /**
     * gets the message time
     */
    public Calendar getMessageTime() {
        return messageTime ;
    }
    /**
     * sets the message summary
     */
    public void setMessageSummary(String _messageSummary) {
        messageSummary = _messageSummary ;
    }
    
    /**
     * gets the message summary
     */
    public String getMessageSummary() {
        return messageSummary ;
    }
}
