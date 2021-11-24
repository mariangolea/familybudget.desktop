package org.mariangolea.fintrack.bank.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class UserPreferencesTestFactory implements UserPreferencesAbstractFactory {
    private UserPreferencesHandler handler;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    public UserPreferencesTestFactory(){
        try {
            folder.create();
            handler = new UserPreferencesHandler(Utilities.createFolder(folder, "prefsTest"));
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesTestFactory.class.getName()).log(Level.SEVERE, null, ex);
            handler = new UserPreferencesHandler("prefsTest");
        }
    }
    
    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        return handler;
    }
}
