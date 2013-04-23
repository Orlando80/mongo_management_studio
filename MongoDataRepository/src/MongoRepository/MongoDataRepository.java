package MongoRepository;

import Repository.IDataRepository;
import com.mongodb.MongoClient;

import java.util.ArrayList;
import java.util.List;

public class MongoDataRepository implements IDataRepository {
    @Override
    public List<String> getDbs(String connectionString) {
        try
        {
        MongoClient mongoClient = new MongoClient(connectionString);
            return mongoClient.getDatabaseNames();
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    @Override
    public List<String> getCollectionNames(String connectionString, String db) {
        try
        {
            MongoClient mongoClient = new MongoClient(connectionString);
            return new ArrayList<String>(mongoClient.getDB(db).getCollectionNames());
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
