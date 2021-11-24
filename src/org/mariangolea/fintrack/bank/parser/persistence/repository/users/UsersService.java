package org.mariangolea.fintrack.bank.parser.persistence.repository.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    
    @Autowired
    private UserPreferencesRepository prefsRepo;
    
    @Autowired
    private UserRepository usersRepo;
    
    public UserPreferences getPreferences(final Integer userID){
        User user = usersRepo.findById(userID).get();
        return user.getPreferences();
    }
    
    public void storePreferences(final Integer userID, final UserPreferences prefs){
    	User user = usersRepo.findById(userID).get();
        user.setPreferences(prefs);
    }
}
