package Presentation.Controllers;

import DataService.DatabaseTree;
import DataService.Tree;
import Domain.DataObjects.Database;
import MongoRepository.MongoDataRepository;
import com.mongodb.CommandResult;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class mainController {
    @FXML public TreeView<Database> Databases;

    @FXML public TabPane tabPanel;

    @FXML public TextArea cmdWindow;

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

    private Database databaseContext;

    public void initialize()
    {
        TreeItem<Database> root = new TreeItem<Database>(new Database("Mongo instances","","","",""));
        Databases.setRoot(root);
    }

    @FXML
    private void mongoTree() throws IOException
    {
        Stage stage = new Stage();
        URL location = getClass().getResource("/Presentation/loginWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent view = (Parent) loader.load();
        loginController c = loader.getController();

        stage.setScene(new Scene(view,400,300));
        stage.showAndWait();
        Database connection = c.getConnectionParameters();


        Tree<Database> collections = new DatabaseTree(new MongoDataRepository()).GetDatabaseTree(connection);
        TreeItem<Database> root = new TreeItem<Database>(collections.getRoot());
        GenerateChildren(root, collections.getChildren());

        Databases.setRoot(root);

    }

    private void GenerateChildren(TreeItem<Database> root,List<Tree.Node<Database>> children) {
        for( Tree.Node<Database> item: children)
        {
            TreeItem<Database> treeItem = new TreeItem<Database>(item.getData());
            if(item.getChildren().size() > 0)
            {
                GenerateChildren(treeItem, item.getChildren());
            }
            root.getChildren().add(treeItem);
        }
    }

    private void setUpContextMenu()
    {
        Databases.setCellFactory(new Callback<TreeView<Database>, TreeCell<Database>>() {
            @Override
            public TreeCell<Database> call(TreeView<Database> stringTreeView) {
                TextFieldTreeCell<Database> cell = new TextFieldTreeCell<Database>()
                {
                  @Override
                public void updateItem(Database item, boolean empty)
                  {
                      super.updateItem(item,empty);
                        if (!empty &&  getTreeItem().isLeaf())
                        {
                            ContextMenu menu = new ContextMenu();
                            MenuItem itemMenu = new MenuItem("Open Table View...");
                            MenuItem treeMenu = new MenuItem("Open DataService.Tree View...");
                            itemMenu.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    mongoTabularView(getItem());
                                }
                            });
                            treeMenu.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    mongoTreeView(getItem());
                                }
                            });
                            menu.getItems().addAll(itemMenu, treeMenu);
                            setContextMenu(menu);


                        }

                  }
                };

                return cell;
            }
        });


    }
    private void mongoTreeView(Database database)
    {
        try
        {
            URL location = getClass().getResource("/Presentation/mongoTreeView.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) loader.load();
            MongoTreeViewController c = loader.getController();

            c.initialize(database);
            Tab tab = new Tab(database.CollectionName);
            tab.setContent(root);
            tabPanel.getTabs().add(tab);
        }
        catch(Exception ex)
        {}
    }

    @FXML public void keyReleased(KeyEvent event)
    {
         KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);
      if (combo.match(event)) {

          try
          {
            MongoClient mongoClient = new MongoClient(databaseContext.getInstanceAddress() + ':' + databaseContext.getDatabasePort());
            DB db = mongoClient.getDB("reviews");
              Object result = db.eval(cmdWindow.getText().trim().replace("\n", ""));
              cmdWindow.setText(result.toString());
          }
          catch(Exception ex)
          {}
        }

    }


    private void mongoTabularView(Database database)  {
        try
        {
        URL location = getClass().getResource("/Presentation/mongoTabularView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) loader.load();
        MongoTabularViewController c = loader.getController();

        c.initialize(database);
        Tab tab = new Tab(database.CollectionName);
        tab.setContent(root);
        tabPanel.getTabs().add(tab);
        }
        catch(Exception ex)
        {}
    }
}

