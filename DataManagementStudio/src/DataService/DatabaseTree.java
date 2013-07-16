package DataService;

import Domain.DataObjects.Database;
import Repository.IDataRepository;

import java.util.List;

public class DatabaseTree {
    private IDataRepository _dataRepository;

    public DatabaseTree(IDataRepository dataRepository) {
        _dataRepository = dataRepository;
    }

    public Tree<Database> GetDatabaseTree(Database connectionString)
    {
        List<String> dbs =_dataRepository.getDbs(connectionString.getInstanceAddress() + ":" + connectionString.getDatabasePort());
         connectionString.setProperties(_dataRepository.getInstanceProperties(connectionString.getInstanceAddress() + ":" + connectionString.getDatabasePort()));
        Tree<Database> databaseTree = new Tree<Database>(connectionString);


        for(String item: dbs)
        {
            Tree.Node node = new Tree.Node(new Database(item,connectionString.getInstanceAddress(),connectionString.getDatabasePort(),"",""));

            List<String> collections = _dataRepository.getCollectionNames(connectionString.getInstanceAddress() + ":" + connectionString.getDatabasePort(), item);
            for (String collection: collections)
            {
                Database object = new Database(item,connectionString.getInstanceAddress(),connectionString.getDatabasePort(),"","");
                object.CollectionName = collection;
                node.getChildren().add(new Tree.Node(object));
            }
            databaseTree.getChildren().add(node);
        }
        return databaseTree;
    }
}
