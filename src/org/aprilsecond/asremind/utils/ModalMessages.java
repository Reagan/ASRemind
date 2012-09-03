package org.aprilsecond.asremind.utils;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * This class creates modal messages that cover the dimensions
 * of the application and display a specific message with a loading 
 * icon
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class ModalMessages extends JComponent {
    
    /**
     * stores the color for the overlay
     */
    private final Color OVERLAY_COLOR = new Color(1.0f, 1.0f, 1.0f) ;
    
    /**
     * stores the rounded rectangle color
     */
    private final Color ROUNDED_RECTANGLE_COLOR = new Color(198, 226, 89) ;
    
    /**
     * stores the color for the loading string message
     */
    private final Color MESSAGE_COLOR = new Color(255, 255, 255) ;
    
    /**
     * stores the font for the message
     */
    private final Font MESSAGE_FONT = new Font(Font.SANS_SERIF, 
            Font.BOLD, 16) ;
    
    /**
     * stores the JFrame to receive the  modal message
     */
    private static JFrame frame ;
    
    /**
     * stores an instance of this class
     */
    private static ModalMessages thisInstance = null ;
    
    /**
     * stores the modal message to be displayed
     */
    private String modalMessage = "" ;
    
    /** 
     * stores the loading gif
     */
    private String loadingImagePath = "resources" 
            + java.io.File.separator
            +  "images" 
            + java.io.File.separator 
            + "loading.gif";
    
    /**
     * stores the buffered image
     */
    private Image loadingImage ;
    
    /**
     * constructor initializes JFrame
     */
    private ModalMessages(JFrame _frame) {
        frame = _frame ;
        
        // initialize the loading image            
        loadingImage = Toolkit.getDefaultToolkit()
                .getImage(loadingImagePath);

        // load the GIFs to an image tracker that loads 
        // each of the frames 
        MediaTracker mediaTracker = new MediaTracker(this);
        mediaTracker.addImage(loadingImage, 0);
        try {
            mediaTracker.waitForAll();
        } catch (InterruptedException ex) {
            System.out.println("Unable to access loading image " 
                + ex);
        }
        
        // consume all mouse events
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});
        
        // prevent keyboard actions from propagating
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });
        
        // prevent user from tabbing across components
        setFocusTraversalKeysEnabled(false);
    }
    
    /**
     * gets an instance of this class
     */
    public static ModalMessages getInstance(JFrame _frame) {
        if (null == thisInstance) {
            thisInstance = new ModalMessages(_frame) ;
        }
        
        return thisInstance ;
    }
    
    /**
     * displays the modal message
     */
    public void displayModalMessage(String message) {
        // set the modal message
        setModalMessage(message);
        
        // display the message
        frame.setGlassPane(thisInstance);
        frame.getGlassPane().setVisible(true);
    }
    
    /**
     * stops the display of the modal message
     */
    public void hideModalMessage() {
        // hide the pane
        frame.getGlassPane().setVisible(false);        
    }
    
    /**
     * sets the message displayed
     */
    public void setModalMessage(String message) {
        modalMessage = message ;
    }
    
    /**
     * gets the message displayed
     */
    public String getModalMessage() {
        return modalMessage ;
    }
    
    /**
     * overrides the paint component method
     */
    @Override
    public void paintComponent(Graphics g) {
       
        // get the Graphics2D object
        Graphics2D graphics =  (Graphics2D) g ;        
        
        // add anti aliasing
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        // create translucency
        AlphaComposite initialComposite 
                = (AlphaComposite) graphics.getComposite() ;
        
        AlphaComposite composite 
                = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.7); 
        graphics.setComposite(composite);
        
        // fill with color
        graphics.setColor(OVERLAY_COLOR);
        graphics.fillRect(7 , 7, 
                getWidth() - 14 , getHeight() - 14);
                
        // replace the composite
        graphics.setComposite(initialComposite);
        
        // draw the rounded rectangle
        int messageBgWidth ; // stores the width to the background image
        graphics.setColor(ROUNDED_RECTANGLE_COLOR);
        
        // calculate the width for the component
        graphics.setFont(MESSAGE_FONT);
        FontMetrics metrics = graphics.getFontMetrics(graphics.getFont()) ;
        messageBgWidth = (int) metrics.getStringBounds(modalMessage, 
                graphics).getWidth() ;
            
        // draw the rect to enclose the rect
        graphics.fillRoundRect(getWidth()/2 - 75, getHeight()/2 - 20 , 
                messageBgWidth + 50, 30, 20, 20);
         
        // draw the loading string message                    
        graphics.setColor(MESSAGE_COLOR);
        graphics.drawString(modalMessage, 
                getWidth()/2 - 38, getHeight()/2  ); 
        
        // draw the loading image
        graphics.drawImage(loadingImage, 
                getWidth()/2 - 70, getHeight()/2 - 17, this);
    }
}
