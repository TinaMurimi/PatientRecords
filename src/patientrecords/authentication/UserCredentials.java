package patientrecords.authentication;

import com.mongodb.DB;

public class UserCredentials {
    public final String uName;
    public final DB db;

    public UserCredentials(String uName, DB db) {
        this.uName = uName;
        this.db = db;
    }

    UserCredentials uCreds(Object UserCred) {
        UserCredentials userCreds = new UserCredentials(uName, db);
        return userCreds;
    }
}