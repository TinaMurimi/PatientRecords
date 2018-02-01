package patientrecords.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;


public class DBAuthentication {
    private final String uName, uPassword;
    private boolean auth;
    private final String dbName = "EyGlas";
    private MongoClient mongo;
    private String errorMessage;
    private MongoDatabase db;
    // Stores the username, authentication, and db connected
    // private final HashMap<String, HashMap<String, Object>> conn = new HashMap<String, HashMap<String, Object>>();
    private final HashMap<String, HashMap<String, Object>> conn = new HashMap<>();
    

    // Constructor
    public DBAuthentication(String uName, String uPassword) {
        this.uName = uName;
        this.uPassword = uPassword;

        dbAuth();
    }

    private void dbAuth() {
        try {
            MongoCredential userAuth = MongoCredential.createScramSha1Credential(uName, "admin",
                    uPassword.toCharArray());
            List<MongoCredential> auths = new ArrayList<>();
            auths.add(userAuth);

            ServerAddress serverAddress = new ServerAddress("localhost", 27017);
            mongo = new MongoClient(serverAddress, auths);
            db = mongo.getDatabase(dbName);

            // TODO: Check if user isActive

            // Try if the connection is successful
            // db.getCollection("");
            auth = true;

        } catch (MongoException e) {
            auth = false;
            errorMessage = "Authentication failed";
            Logger.getLogger(DBAuthentication.class.getName()).log(Level.WARNING, errorMessage, e);
        }

        // conn.put(uName, new HashMap<String, Object>());
        conn.put(uName, new HashMap<>());
        conn.get(uName).put("username", uName);
        conn.get(uName).put("database", db);
        conn.get(uName).put("auth", auth);
    }

    public HashMap getConn() {
        return conn;
    }
}
