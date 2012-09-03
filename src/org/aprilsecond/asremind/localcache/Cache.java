package org.aprilsecond.asremind.localcache;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import org.aprilsecond.asremind.ASRemind;
import org.aprilsecond.asremind.Calendar.ASCalendar;
import org.aprilsecond.asremind.utils.Directory;
import org.aprilsecond.xmlfile.XMLFile;

/**
 * <p>
 * This class stores a local cache for :
 * <ol>
 *  <li>the calendar data downloaded for the application</li>
 *  <li>Track messages entered by a user</li>
 * </ol>
 * </p>
 * 
 * <p>
 * The local cache for 
 * the application is a an XML file that contains events and reminders
 * generated for each of a user's calendar. The name of the XML file 
 * represents the cache for a specific day. The XML cache file is 
 * entitled {@link EventsCache.xml }
 * </p>
 * 
 * <p>
 * The XML has the format: 
 * 
 * <pre>
 *      <?xml version="1.0" encoding="UTF-8" ?>
 *       <calendars>
 *           <calendar title="Google Doodles" color="#A32929" id="http://www.google...">
 *		<entries>
 *			<entry title="Tennis with Beth">
 *				<start>2006-05-17T15:00:0000Z</start>
 *				<stop>2006-05-17T15:00:0000Z</stop>
 *                              <reminder minutes="10" />
 *			</entry>
 *                  </entries>
 *          </calendar>
 *      </calendars>
 * </pre>
 * 
 * </p>
 * <p>
 * This class also manages the cache to the Track messages. These track messages are 
 * stored in an XML file entitled TrackCache.xml. This XML file has the structure :
 * </p>
 * 
 * <p>
 * <pre>
 * <?xml version="1.0" encoding="UTF-8" ?>
 * <messages>
 *	<message>
 *		<time>2006-04-03T15:00:000Z</time>
 *		<message>Visit to Auntie Macharia's House</message>
 *	</message>
 * </messages>
 * </pre>
 * 
 * </p>
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class Cache {
    
    /**
     * stores application local cache
     */
    private static Cache thisInstance ;
    
    /**
     * stores path to calendar cache
     */
    private static final String CALENDAR_CACHE 
            = ("cache/events/events.xml").replace('/',
                    File.separatorChar) ;
    
    /**
     * stores path to track cache
     */
    private static final String TRACK_CACHE 
            = ("cache/track/track.xml").replace('/',
                    File.separatorChar) ;    
    
    /**
     * stores the directory object for the events
     */
    private static Directory calendarsDir ;
    
    /**
     * stores the directory object for the track
     * messages
     */
    private static Directory trackDir ;
    
    /**
     * stores the calendar events
     */
    private ArrayList<ASCalendar> calendars ;
    
    /**
     * stores the track messages
     */
    private ArrayList<TrackMessage> trackMessages ;
    
    /**
     * cannot initialize
     */
    private Cache() {
        // check for the events and track xmls
        // create if not available
        
        trackDir = initializeCache(Cache.TRACK_CACHE) ;
        calendarsDir =  initializeCache(Cache.CALENDAR_CACHE)  ;
        
        // make sure that the events xml file is available
        if ( null == calendarsDir ) {
            System.out.println("Cache file for Calendar events not available. "
                    + "Creating events cache file") ; 
            
            // write to events xml file
            if (null == writeEventsXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                System.out.println("There was an error creating "
                        + "the events cache file");
            }
        } 
                
        // make sure that the track xml file is available
        if ( null == trackDir ) {
             System.out.println("Cache file for the track events is not available. "
                    + "Creating track cache file") ;
             
             // write to tracks XML file
             if (null == writeTracksXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                 System.out.println("There was an error creating "
                         + "the tracks cache file") ;
             }
        }
        
        // get the events 
        calendars = loadCalendarInfo() ;
        
        // get the track messages
        trackMessages = loadTrackMessages() ;
        
        // show that the track messages and calendar information 
        // has been obtained
        ASRemind.allCacheCalendarAndTrackMessageEntriesObtained = true ;
   }
    
    /**
     * creates the events xml file
     */
    private File writeEventsXML(String xmlContent) {
        return calendarsDir.writeToFile(xmlContent) ;
    }
    
    /**
     * creates the tracks xml file
     */
    private File writeTracksXML(String xmlContent) {
        return trackDir.writeToFile(xmlContent) ;
    }
    
    /**
     * get instance of local cache
     */
    public static Cache loadCache() {
        
        // initialize instance 
        if (thisInstance == null) {
            thisInstance = new Cache() ;
        }
        
        return thisInstance ;
    }
    
    /**
     * this method writes a set of calendar entries to the application's cache
     * from an ArrayList of calendars info
     */
    public boolean saveCalendarCache(ArrayList<ASCalendar> calendars) {
        
        boolean dataWritten = false ;
        
        // create object to store the created XML
        String calToXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><calendars>";
        
        // create XStream object
        XStream xmlCacheStream = new XStream() ;
        
        // loop through each of the calendars serializing the 
        // calendar object to XML
        for (int calendarsCounter = 0 ; calendarsCounter < calendars.size() ;
                calendarsCounter ++) {
            
            // get the current calendar object
            ASCalendar currCal = calendars.get(calendarsCounter) ;
            
            // serialise current calendar object to XML
            StringWriter sw = new StringWriter() ;
            xmlCacheStream.marshal(currCal, new CompactWriter(sw)) ;
            calToXML += sw.toString() ;
        }
        
        // close all the records
        calToXML += "</calendars>" ;
        
        if(!"".equals(calToXML)) {
            // make sure that the data is written to file
            if(null != writeEventsXML(calToXML)) {                
                dataWritten = true ; 
            }                        
        }
            
        return dataWritten ;
    }
    
    /**
     * this method writes a set of track messages onto the application's
     * cache using a serialized ArrayList to store the information 
     */
    public boolean saveTrackCache(ArrayList<TrackMessage> trackMessages) {
        boolean dataWritten = false ;
        
        // create object to store the created XML
        String tracksToXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><messages>";
        
        // create XStream object
        XStream xmlCacheStream = new XStream() ;
        
        // loop through each of the track messages and serialize it
        for (int trackMessagesCounter = 0 ; trackMessagesCounter < trackMessages.size() ;
                trackMessagesCounter ++) {
            
            // get the track message object
            TrackMessage currTrackMessage = trackMessages.get(trackMessagesCounter) ;
            
            // serialise current track message object to XML
            StringWriter sw = new StringWriter() ;
            xmlCacheStream.marshal(currTrackMessage, new CompactWriter(sw)) ;
            tracksToXML += sw.toString() ;
        }
        
        // close all the records
        tracksToXML += "</messages>" ;
        
        if(!"".equals(tracksToXML)) {
            // make sure that the data is written to file
            if(null != writeTracksXML(tracksToXML)) {                
                dataWritten = true ; 
            }                        
        }           
        
        return dataWritten ;
    }
    
    /**
     * read cache for a specific date
     */
    public final Directory initializeCache(String cache) {
        
        // load the directory and initialize the 
        // file
        Directory cacheLoadStatus = new Directory() ;
        cacheLoadStatus.initializeFile(cache) ;        
        return cacheLoadStatus ;
    }    
    
    /**
     * gets the calendar events
     */
    public final ArrayList<ASCalendar> loadCalendarInfo() {
                
        // return deserialized instance of calendar objects
        XStream calStream = new XStream() ;
        calStream.alias("calendars", ArrayList.class);
        calStream.alias("org.aprilsecond.asremind.Calendar.ASCalendar", 
                 ASCalendar.class);
        
        return (ArrayList<ASCalendar>) 
                calStream.fromXML(calendarsDir.readFile()) ;       
    }
           
    /**
     * gets the track messages
     */
    public final ArrayList<TrackMessage> loadTrackMessages() {
        
        // return the deserialized track messages
        XStream trackMessagesStream = new XStream() ;
        trackMessagesStream.alias("messages", ArrayList.class);
        trackMessagesStream.alias("org.aprilsecond.asremind.localcache.TrackMessage", TrackMessage.class);
        
        return (ArrayList<TrackMessage>) 
                trackMessagesStream.fromXML(trackDir.readFile()) ;        
    }
    
    /**
     * gets the calendars and the calendars information
     */
    public ArrayList<ASCalendar> getCalendarsCache() {
        return calendars;
    }
    
    /**
     * gets the track messages information 
    */
    public ArrayList<TrackMessage> getTrackCache() {
        return trackMessages ;
    }
}
