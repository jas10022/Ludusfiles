package edu.self.ludus;

import android.app.Application;

import com.parse.Parse;


/**
 * Created by sbakshi on 11/22/2015.
 */
public class App extends Application{

        @Override public void onCreate() {
            super.onCreate();
            Parse.initialize(this, "do9w1so1gUfvhy6gKj4QdE8TXYLka0lfk086BTHV", "1cM6OveW0I06ouCefOuVj5hlUkmCM1H6Z4c7ccJX");

        }
}
