package com.ajithkp.application.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Java class used to read the property file keys and value
 *
 * @author Integra
 */
public class PropertyReader {
    private static final String TAG = PropertyReader.class.getName();
    private static PropertyReader assetspropertyReader;

    private PropertyReader() {
    }

    /**
     * Method to get the Single instance
     *
     * @return
     */
    public static PropertyReader getInstance() {

        if (assetspropertyReader == null) {
            assetspropertyReader = new PropertyReader();
        }
        return assetspropertyReader;
    }

    /**
     * Method used to get the property file object
     *
     * @param fileName - property file name
     * @param context  - Context
     * @return
     */
    public Properties getProperties(String fileName, Context context) {
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
        return properties;

    }
}
