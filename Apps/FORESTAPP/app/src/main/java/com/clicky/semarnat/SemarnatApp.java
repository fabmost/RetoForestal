package com.clicky.semarnat;

import android.app.Application;

import com.clicky.semarnat.data.Documentos;
import com.clicky.semarnat.data.MateriaPrima;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 *
 * Created by Clicky on 4/2/15.
 *
 */
public class SemarnatApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Documentos.class);
        ParseObject.registerSubclass(MateriaPrima.class);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "xJPG4xeiwqTF0RKZgHboZDTzl2aWn7lDBTM8iZQh", "m0pyQ5srV4GBm7IElt3XdYpqhzWnXOUyFpq10b83");
    }

}
