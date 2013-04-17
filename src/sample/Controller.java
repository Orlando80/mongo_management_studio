package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller {
    @FXML public TreeView<String> Databases;

    @FXML public TabPane tabPanel;

    @FXML
    public void openConnection(ActionEvent event)
    {
        try {
        mongoTree();
        }
        catch(Exception ex)
        {
            System.out.print(ex.getMessage());
        }
    }
    @FXML
    private void mongoTree() throws IOException
    {

        TreeItem <String> root = new TreeItem<String>("Localhost:27017");


        MongoClient mongoClient = new MongoClient("localhost");
        List<String> dbs = mongoClient.getDatabaseNames();
        for(String item: dbs)
        {
            TreeItem<String> treeItem = new TreeItem<String>(item);
            DB db = mongoClient.getDB(item);
            Set<String> collections = db.getCollectionNames();
            for (String collection: collections)
            {
               treeItem.getChildren().add(new TreeItem<String>(collection));
            }
            root.getChildren().add(treeItem);
        }
        Databases.setRoot(root);

        mongoTabularView();

    }

    private void mongoTabularView() throws IOException {
        URL location = getClass().getResource("mongoTabularView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) loader.load();
        MongoTabularViewController c = loader.getController();

        c.initialize("localhost");
        Tab tab = new Tab("Test");
        tab.setContent(root);
        tabPanel.getTabs().add(tab);
    }
}
