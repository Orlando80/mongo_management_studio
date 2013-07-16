package Presentation.Controllers;

import Domain.DataObjects.Database;
import com.mongodb.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

public class MongoTreeViewController {
    @FXML private TreeView<String> treeView;

    private Database database;

    public void initialize(Database database) throws IOException
    {
        this.database = database;

        FillTable();
    }


    public void FillTable() throws IOException
    {
        MongoClient mongoClient = new MongoClient(database.getInstanceAddress());
        DB db = mongoClient.getDB(database.getDatabaseName());
        DBCollection collection = db.getCollection(database.CollectionName);
        DBCursor dbObject = collection.find().limit(1000);
        int i = 0;
        TreeItem<String> root = new TreeItem<String>(database.CollectionName);
        while(dbObject.hasNext())
        {
            DBObject obj  = dbObject.next();
            TreeItem<String> singleItem = new TreeItem<String>("{" + i + "} ...");
            for (String key: obj.keySet())
            {
                TreeItem<String> item = new TreeItem<String>("{ " + key + " : " + obj.get(key) + " [ "+ obj.get(key).getClass().getSimpleName() +" ] }");
                singleItem.getChildren().add(item);
            }
            i++;
            root.getChildren().add(singleItem);
        }

            treeView.setRoot(root);
    }
}
