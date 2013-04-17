package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {
    @FXML public TreeView<String> Databases;

    @FXML
    private void openConnection(ActionEvent event)
    {
        try {
        mongoTree();
        }
        catch(Exception ex)
        {}
    }
    @FXML
    private void mongoTree() throws UnknownHostException
    {
        TreeItem<String> root = new TreeItem<String>("Localhost:27017");
        MongoClient mongoClient = new MongoClient("localhost");
        List<String> dbs = mongoClient.getDatabaseNames();
        for(String item: dbs)
        {
            root.getChildren().add(new TreeItem<String>(item));
        }

        Databases.setRoot(root);
    }
}
