package org.aprilsecond.asremind.utils.NotifyingThread;

/**
 * This is an interface that is implemented by threads to show that 
 * they are completely run 
 * @author Reagan Mbitiru <reaganmbitiru@yahoo.com>
 */
public interface ThreadCompleteListener {
    void notifyOfThreadComplete(final Runnable thread, boolean statusOfOperation) ;
}
