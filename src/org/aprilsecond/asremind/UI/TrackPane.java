package org.aprilsecond.asremind.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.aprilsecond.asremind.localcache.Cache;
import org.aprilsecond.asremind.localcache.TrackMessage;
import org.aprilsecond.customuicomponents.BasePanels.BaseContentPanel;
import org.aprilsecond.customuicomponents.List.CustomList.*;
import org.aprilsecond.customuicomponents.List.ListItem.CalendarListItem;
import org.aprilsecond.customuicomponents.List.ListItem.SimpleListItem;
import org.aprilsecond.customuicomponents.ScrollPane.*;
import org.aprilsecond.customuicomponents.ScrollPane.ScrollPane;

/**
 * This class implements the Track Pane that is displayed when a 
 * user views the Track page of the application
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class TrackPane extends BaseContentPanel {
    
    /**
     * stores the calendar list for the component
     */
    private CalendarList calendarList ;
    
    /**
     * stores the scroll pane that hosts the calendar list 
     */
    private ScrollPane scrollPane ;
    
    /**
     * stores the text area with the track information
     */
    private JTextArea textArea ;
    
    /**
     * stores the number of messages label
     */
    private JLabel numberOfMessagesLabel ;
    
    /**
     * stores the panel with the calendar list and 
     * the text area
     */
    private JPanel rightPanel ;
    
    /**
     * stores the panel with the messages and the simple
     * list with the add and remove buttons
     */
    private JPanel leftPanel ;
    
    /**
     * stores the Add & delete options
     */
    private SimpleList optionsList ;
    
    /**
     * stores scroll pane for the simple list
     */
    private ScrollPane simpleListScrollPane ;
    
    /**
     * stores the scroll pane for the text area
     */
    private JScrollPane txtAreaScrollPane ;
    
    /**
     * stores the ADD option
     */
    private static final String ADD_OPTION = "Add" ;
    
    /**
     * stores the DELETE option
     */
    private static final String DELETE_OPTION = "Delete" ;
    
    /**
     * stores the default text displayed in the text area
     */
    private final String DEFAULT_TEXT_AREA_TEXT 
            = "<Please insert track message here>" ;
    
    /**
     * stores the messages for the UI for the component
     */
    private ArrayList<TrackMessage> messages ; 
    
    /**
     * stores the font for the messages list
     */
    private final Font MESSAGES_NUMBER_FONT 
            = new Font(Font.SANS_SERIF, Font.PLAIN, 20) ;
    
    /**
     * stores the color for the 
     */
    private final Color MESSAGES_NUMBER_FONT_COLOR 
            = new Color(255, 255, 255) ;
    
    /**
     * stores the local cache object
     */
    private Cache localCache ;
    
    /**
     * stores the selected calendar list item
     */
    private CalendarListItem selectedListItem ;
    
    /**
     * constructor initializes components
     */
    public TrackPane() {
        
        // initialize the left panel
        leftPanel = new JPanel(new BorderLayout()); 
        
        // initialize the right panel
        rightPanel = new JPanel(new BorderLayout()) ;
        
        // initialise the scroll pane
        scrollPane = new ScrollPane() ;
        
        // initialize the messages label
        numberOfMessagesLabel = new JLabel() ;
        numberOfMessagesLabel.setFont(MESSAGES_NUMBER_FONT);
        numberOfMessagesLabel.setForeground(MESSAGES_NUMBER_FONT_COLOR);
        numberOfMessagesLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        numberOfMessagesLabel.setPreferredSize(new Dimension(163, 50));
        
        // initialize the text area
        textArea = new JTextArea(DEFAULT_TEXT_AREA_TEXT) ;
        textArea.setPreferredSize(new Dimension(320, 110));
        textArea.addFocusListener(new TextAreaFocusListener());
        textArea.addKeyListener(new EnterSelectedInTextAreaListener());
         
        // initialize the scroll pane for the txt area
        txtAreaScrollPane = new JScrollPane() ;
        txtAreaScrollPane.setViewportView(textArea);
        
        // intialize the simple list scroll pane
        simpleListScrollPane = new ScrollPane() ;
        simpleListScrollPane
                .addItemSelectedListener(new AddorDeleteItemSelectedListener());
    }
    
    /**
     * finalizes the UI for the Track pane
     */
    public void buildUI() {
        
        // initialize the simple list
        optionsList = new SimpleList() ;
        optionsList.setListDimensions(new Dimension(160, 200)) ;
        
        // add the simple list to the scroll pane
        simpleListScrollPane.setViewport(optionsList);
        
        // add the entries to the calendar list
        calendarList = new CalendarList() ;
        calendarList.setListDimensions(new Dimension(375, 279)) ;
        
        // set the dimensions for the scroll pane
        scrollPane.setPreferredSize(new Dimension(373, 281));
        
        // set the scroll pane view port
        scrollPane.setViewport(calendarList);
        scrollPane.addItemSelectedListener(new CalendarListItemSelected()) ;
        
        // add items to the left Panel
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER) ;
        leftPanel.add(txtAreaScrollPane, BorderLayout.SOUTH) ;
        
        // set the right panel properties
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);    // make sure that the right panel is 
                                        // transparent
        
        // add items to the right panel
        rightPanel.add(numberOfMessagesLabel, BorderLayout.NORTH) ;
        rightPanel.add(simpleListScrollPane, BorderLayout.CENTER) ;
        
        // set the layout for the entire component
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        // add the timetable component
        add(leftPanel) ;
        
        // add the right Panel
        add(rightPanel) ;
    }
    
    /**
     * This method populates the activities panel with the content for the
     * various components
     */
    public void updatePane(ArrayList<TrackMessage> trackMessages) {
        
        // set the messages
        messages = trackMessages ;
        
        // show the number of messages
        updateNumberOfMessagesLabel(messages.size()) ;
        
        // adds the options 
        addOptionsPanel() ;
        
        // retrieves the list of all the track events
        updateTrackList(messages) ;
        
        // show that the track pane has been updated
        MainAppPanel.trackPaneLoaded = true ;
    }
    
    /**
     * shows the number of messages retrieved or currently 
     * on the cache
     */
    public void updateNumberOfMessagesLabel(int noOfMessages) {
        numberOfMessagesLabel.setText(noOfMessages + " Messages");
    }
    
    /**
     * this method adds the 'add' and 'delete'
     * options that appear on the right of the track
     * pane
     */
    private void addOptionsPanel() {
        // create the items
        ArrayList <String> optionsItems = 
                new ArrayList<String>() ;
        optionsItems.add(ADD_OPTION);
        optionsItems.add(DELETE_OPTION);
        
        // add them to the model
        SimpleListModel optionsModel = new SimpleListModel(optionsItems) ;
        optionsList.setSimpleListModel(optionsModel);
    }
            
    /**
     * loops through the returned 
     * events, retrieves and adds all the tracked events
     */
    private void updateTrackList(ArrayList<TrackMessage> trackMessages) {
        ArrayList<Calendar> extractedTimes = new ArrayList<Calendar>() ;
        ArrayList<String> extractedMessages = new ArrayList<String>() ;
        
        for (int trackMessagesCounter = 0; trackMessagesCounter < trackMessages.size();
                trackMessagesCounter++) {
            TrackMessage currMessage = trackMessages.get(trackMessagesCounter) ;
            extractedTimes.add(currMessage.getMessageTime());
            extractedMessages.add(currMessage.getMessageSummary());
        }
        
         CalendarListModel model = new CalendarListModel(extractedMessages, 
                 extractedTimes) ;
         calendarList.setCalendarListModel(model);
    }
    
    /**
     * this method updates the track cache with the messages 
     * entered by the user for  persistent storage
     */
    private void updateTrackCache(ArrayList<TrackMessage> trackMessages){
        localCache = Cache.loadCache() ;
        localCache.saveTrackCache(trackMessages);
    }
    
    /**
     * this adds a message to the track messages list and 
     * to the cache as well. This removes the extra /r/n 
     * added to the track message when a user hits VK_ENTER
     */
    private void addMessageToTrackMessagesStore(String message) {
        message = message.trim() ;
        TrackMessage currMessage = new TrackMessage(Calendar.getInstance(), 
                message) ;
        messages.add(currMessage);       
    }
    
    private void updateTextArea(String text) {
        textArea.setText(text);
    }
    
    /**
     * deletes an item from the messages store
     * based on its component ID
     */
    private void deleteItemFromMessages(String componentIDForItemToDelete) {
        for (int trackMessagesCounter = 0 ; trackMessagesCounter < messages.size(); 
                trackMessagesCounter++) {
            TrackMessage currMessage = messages.get(trackMessagesCounter) ;
            
            if (formatCurrentItemToCalendarFormat(currMessage).equals(componentIDForItemToDelete)) {
                messages.remove(currMessage);
                break ;
            }
        }
    }
    
    /**
     * formats the current message to a form similar to that 
     * returned from the CalendarListItem.getComponentID() 
     */
    private String formatCurrentItemToCalendarFormat(TrackMessage message) {        
        String AM_or_PM = (message.getMessageTime().get(Calendar.AM_PM) == 0) ? "AM" : "PM";
        String formattedTimeString = "[" + message.getMessageTime().get(Calendar.HOUR)
                + ":" + message.getMessageTime().get(Calendar.MINUTE)
                + " " + AM_or_PM + "] ";

        // add the activity message
        formattedTimeString += message.getMessageSummary();

        // return the time string
        return formattedTimeString;
    }
    
    /**
     * triggered when the ADD or DELETE options are 
     * selected from the 
     * @param evt selected event 
     */
    private class AddorDeleteItemSelectedListener
        implements ItemSelectedListener {
        
        @Override
        public void itemSelected(ItemSelected evt) {
            // get the event obj
            SimpleListItem sListItem = (SimpleListItem) evt.getSource();

            // get the selected item
            String selectedItem = sListItem.getComponentID();
            
            // process the selected option
            if (selectedItem.equals(TrackPane.ADD_OPTION)) {
                
                // find out if there is any selected item in the trackpane
                addMessageToTrackMessagesStore(textArea.getText());
                updateTextArea(DEFAULT_TEXT_AREA_TEXT);
                updateTrackList(messages);
                updateNumberOfMessagesLabel(messages.size());
                updateTrackCache(messages) ;
                
            } else if (selectedItem.equals(TrackPane.DELETE_OPTION)) {
                
                if(null != selectedListItem) {                    
                    deleteItemFromMessages(selectedListItem.getComponentID()) ;
                    updateTrackList(messages);
                    updateNumberOfMessagesLabel(messages.size());
                    updateTrackCache(messages) ;
                }
            }
        }
    }
                   
    /**
     * makes sure that when the text-area gets focus, the 
     * default displayed text disappears
     * @param e  focus event
     */
    private class TextAreaFocusListener 
        implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (textArea.getText().equals(DEFAULT_TEXT_AREA_TEXT)) {
                updateTextArea("");
            }
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            if (textArea.getText().equals("")) {
                updateTextArea(DEFAULT_TEXT_AREA_TEXT);
            }
        }
    }
              
    /**
     * this action is activated when VK_ENTER is selected
     * while in the text-area and submits entered text to 
     * list of track messages
     */
    private class EnterSelectedInTextAreaListener
        extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER
                    && e.getSource() == textArea) {
                addMessageToTrackMessagesStore(textArea.getText());
                updateTextArea(DEFAULT_TEXT_AREA_TEXT);
                updateTrackList(messages);
                updateNumberOfMessagesLabel(messages.size());
                updateTrackCache(messages) ;
            }
        }       
    }
    
    /**
     * this class is processed when any of the calendar 
     * list items is selected
     */
    private class CalendarListItemSelected
        implements ItemSelectedListener {
            @Override
             public void itemSelected(ItemSelected evt) {
            selectedListItem =  (CalendarListItem) evt.getSource();
        }
    }
    
    /**
     * test
     */
    public static void main(String[] args) {
        Runnable displayPane = new Runnable() {

            @Override
            public void run() {
                TrackPane pane = new TrackPane() ;
                pane.buildUI();
                
                JFrame frame = new JFrame("Track Pane") ;
                frame.add(pane) ;
                frame.setSize(552, 320);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }; 
        
        EventQueue.invokeLater(displayPane);
    }        
}
