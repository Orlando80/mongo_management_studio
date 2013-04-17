package Presentation.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class mainController {
    @FXML public TreeView<String> Databases;

    @FXML public TabPane tabPanel;

    @FXML
    public void openConnection(ActionEvent event)
    {
        try {
        mongoTree();
            setUpContextMenu();
        }
        catch(Exception ex)
        {
            System.out.print(ex.getMessage());
        }
    }
    @FXML
    private void mongoTree() throws IOException
    {
        Stage stage = new Stage();
        Parent view = FXMLLoader.load(getClass().getResource("/Presentation/loginWindow.fxml"));
         stage.setScene(new Scene(view,300,200));
        stage.showAndWait();
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

    }

    private void setUpContextMenu()
    {
        Databases.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TextFieldTreeCell<String> cell = new TextFieldTreeCell<String>()
                {
                  @Override
                public void updateItem(String item, boolean empty)
                  {
                      super.updateItem(item,empty);
                        if (!empty &&  getTreeItem().isLeaf())
                        {
                            ContextMenu menu = new ContextMenu();
                            MenuItem itemMenu = new MenuItem("Open Table View...");
                            itemMenu.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    mongoTabularView(getItem());
                                }
                            });
                            menu.getItems().add(itemMenu);
                            setContextMenu(menu);


                        }

                  }
                };

                return cell;
            }
        });


    }


    private void mongoTabularView(String collectionName)  {
        try
        {
        URL location = getClass().getResource("/Presentation/mongoTabularView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) loader.load();
        MongoTabularViewController c = loader.getController();

        c.initialize("localhost",collectionName);
        Tab tab = new Tab(collectionName);
        tab.setContent(root);
        tabPanel.getTabs().add(tab);
        }
        catch(Exception ex)
        {}
    }
}

