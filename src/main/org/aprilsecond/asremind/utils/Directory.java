package org.aprilsecond.asremind.utils;

import java.io.*;

/**
 * This class obtains files from the directory of
 * the application
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class Directory {
    
    /**
     * stores the path to the file being checked
     */
    private String filePath ;
    
    /**
     * stores the input stream
     */
    private InputStream in ;
    
    /**
     * stores the Buffered reader obj
     */
    private BufferedReader reader ;
    
    /**
     * stores the BufferedWriter obj
     */
    private BufferedWriter writer ;
    
    /**
     * stores a File
     */
    public File dir ;
    
    /**
     * singleton pattern
     */
    public Directory() {    
    }
    
    /**
     * initializes a directory object
     */
    public File initializeFile (String fPath) {
       
         // initialize file path
        filePath = fPath ;
            
        // initialize the directory file object
        dir = new File(filePath) ;
        
        try {
            // initialize the input stream object
            in = new FileInputStream(dir) ;
          
            // create buffered reader object
            reader = new BufferedReader(new InputStreamReader(in));

            // check to ensure that the file 
            // exists
            if (dir.isFile()) {

                // return the file
                return dir;
            }
            
            // close the reader object
            reader.close();
            in.close();
            
            // free reader 
            reader = null ;
            in = null ;

        } catch (FileNotFoundException ex) {
            System.out.println("File not found error : " + ex.getMessage()) ;
            return null ;
        } catch (IOException ex) {
            System.out.println("Error closing reader object :" + ex.getMessage()) ;
            return null ;
        }

        // return null
        return null ;
    }
    
    /**
     * creates a file with the desired content
     */
    public File writeToFile(String stringContent) {
        try {
            // initialize the Buffered Writer obj
            writer = new BufferedWriter(new FileWriter(dir)) ;
            
            // write to the file
            writer.write(stringContent);
            writer.flush();
            
            // close the writer
            writer.close();
            
            // return the file
            return dir ;
            
        } catch (IOException ex) {
            System.out.println("Error writing to file " + dir + ex);
            return null ;
        }
    }
    
    /**
     * appends to file
     */
    public File appendToFile(String stringContent) {
        try {
            // initialize the Buffered Writer obj
            writer = new BufferedWriter(new FileWriter(dir)) ;
            
            // write to the file
            writer.append(stringContent);
            
            // close the writer
            writer.close();
            
            // return the file
            return dir ;
            
        } catch (IOException ex) {
            System.out.println("Error appending to file " + ex);
            return null ;
        }
    }
    
    /**
     * reads from a file and returns a string with the 
     * context of the file
     */
    public String readFile() {
        
        String fileContent = "" ;
                    
        // initialize the directory file object
        dir = new File(filePath) ;
        
        try {
            // initialize the input stream object
            in = new FileInputStream(dir) ;
          
            // create buffered reader object
            reader = new BufferedReader(new InputStreamReader(in));

            // check to ensure that the file 
            // exists
            if (dir.isFile()) {

                // read from the file and add them to the resultant string
                String res  ;
                while((res = reader.readLine())!=null) {
                    fileContent = res ;
                }                                
            }
            
            // close the reader object
            reader.close();
            in.close();
            
            // free reader 
            reader = null ;
            in = null ;

        } catch (FileNotFoundException ex) {
            System.out.println("File not found error : " + ex.getMessage()) ;
            return null ;
        } catch (IOException ex) {
            System.out.println("Error closing reader object :" + ex.getMessage()) ;
            return null ;
        }
        
        return fileContent ;
    }
}
