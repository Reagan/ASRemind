package org.aprilsecond.asremind.utils;

import javax.swing.JOptionPane;

/**
 * This class extends JOptionPane to display an alert message
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class Alerter extends JOptionPane {
    private Alerter(){}
    
    public static void displayErrorMessage(String errorTitle,
            String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, errorTitle,
                        JOptionPane.ERROR_MESSAGE);
    }
}
