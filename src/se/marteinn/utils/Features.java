package se.marteinn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;

/**
 * Loads and parses a features from a .properties file.
 * Inspiration from http://bit.ly/1gkLLeO
 * 
 * @author martinsandstrom
 *
 */
public class Features {
    public static final String TAG = Features.class.toString();
    
    // Members
    private Properties mProperties;
    private static Features sInstance;
    
    static public Features getInstance() {
        return sInstance;
    }
    
    /**
     * Creates singleton class instance.
     * @param context
     * @param debugResource
     * @param liveResource
     * @return
     */
    static public Features createInstance(Context context, int debugResource, 
            int liveResource) {
        
        sInstance = new Features(context, debugResource, liveResource);
        return sInstance;
    }
    
    /**
     * Returns singleton class instance.
     * @param context
     * @param debugResource
     * @param liveResource
     */
    public Features(Context context, int debugResource, int liveResource) {
        mProperties = loadFeatures(context, debugResource, liveResource);
    }
    
    /**
     * Loads and parses features from resource, uses debug resource if debuggable
     * is on.
     * @param context
     * @param debugResource
     * @param liveResource
     * @return
     */
    public Properties loadFeatures(Context context,  int debugResource, 
            int liveResource) {
        
        int resourceId = 0;
        Properties parsedProperties = null;
        
        // Check either live or debug resource
        resourceId = ! isDebuggable(context) ? liveResource : debugResource;
        
        // Load and parse resource
        try {
            InputStream rawResource;
            rawResource = context.getResources().openRawResource(resourceId);
            
            parsedProperties = new Properties();
            parsedProperties.load(rawResource);
        } catch (NotFoundException e) {
            System.err.println("Could not find features resource: "+e);
        } catch (IOException e) {
            System.err.println("Failed to features file: "+e);
        }
        
        return parsedProperties;
    }
    
    /**
     * Checks if feature is active or not.
     * @param name
     * @return
     */
    public Boolean hasFeature(String name) {
        if (! mProperties.containsKey(name)) {
            return false;
        }
        
        int feature = Integer.parseInt((String) mProperties.get(name));
        return feature == 1;
    }
    
    /**
     * Check if app is running with under debug.
     * Inspiration/code from: http://bit.ly/1gmLMm3
     * @param context
     * @return
     */
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;
        PackageManager pm = context.getPackageManager();
        
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(
                    context.getPackageName(), 0);
            
            debuggable = (0 != (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        } catch(NameNotFoundException e) {
            /*debuggable variable will remain false*/
        }
         
        return debuggable;
    }
}
