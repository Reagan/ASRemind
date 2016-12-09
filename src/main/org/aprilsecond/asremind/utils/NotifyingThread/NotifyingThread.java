package org.aprilsecond.asremind.utils.NotifyingThread;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Base thread class that is implemented
 *
 *  * @author Reagan
 */
public abstract class NotifyingThread extends Thread {

    private final Set<ThreadCompleteListener> 
            listeners 
            = new CopyOnWriteArraySet<ThreadCompleteListener>();

    public final void addListener(final 
            ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final 
            ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners(boolean status) {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this, status);
        }
    }

    @Override
    public final void run() {
        boolean status = false;
        try {
            status = doRun();
        } finally {
            notifyListeners(status);
        }
    }

    public abstract boolean doRun();
}

