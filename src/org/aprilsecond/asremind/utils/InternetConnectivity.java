package org.aprilsecond.asremind.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.aprilsecond.asremind.utils.NotifyingThread.NotifyingThread;
import org.aprilsecond.asremind.utils.NotifyingThread.ThreadCompleteListener;

/**
 * <p>
 * This class tests whether an application can access the 
 * internet by using the InetAddress.isReachable API and the 
 * domain http://google.com for the testing. Thread scheduling
 * and notification on completion available adapted from 
 * http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished
 * </p>
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class InternetConnectivity extends NotifyingThread {
    
    /**
     * stores the test string domain
     */
    private static final String TEST_DOMAIN = "google.com" ;
        
    /**
     * indicates the time out period (ms)
     */
    private final static int TIMEOUT = 5000 ;
    
    /**
     * stores instance of class
     */
    private static InternetConnectivity thisInstance ;
    
    /**
     * Stores the ThreadCompleteListener
     */
    private ThreadCompleteListener listener ;
    
    /**
     * prevent initialization
     */
    private InternetConnectivity() {}
    
    /**
     * gets the instance 
     */
    public static InternetConnectivity getInstance() {
        thisInstance = new InternetConnectivity() ;        
        return thisInstance ;
    }
    /**
     * tests for connectivity
     */
    public static boolean isInternetReachable() {

        boolean isConnected = false ; 
        
        try {
            // try to access the domain
            InetAddress inetAddress = InetAddress.getByName(TEST_DOMAIN) ;
            
            // test if reachable
            inetAddress.isReachable(TIMEOUT);
            
            isConnected = true ;
             
        } catch (UnknownHostException ex) {
            System.out.println("Error accessing the test domain " + ex);
        } catch (IOException ex) {
            System.out.println("Error opening port to test domain " + ex);
        }
        
        return isConnected ;
    }       

    /**
     * override the run method that is called by 
     * run() and determines if there is connectivity
     * @return 
     */
    @Override
    public boolean doRun() {
        return InternetConnectivity.isInternetReachable() ;
    }
    
     /** 
     * test
     */
    public static void main(String[] args) {
       
        // start the thread that checks if the connection 
        // to the internet is valid
        InternetConnectivity ins = InternetConnectivity.getInstance() ;
        ins.start();
    }
}
