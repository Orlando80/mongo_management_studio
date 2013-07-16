package Presentation.Controllers;

import DataService.DatabaseTree;
import DataService.Tree;
import Domain.DataObjects.Database;
import Domain.DataObjects.Property;
import MongoRepository.MongoDataRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import java.util.Hashtable;
import java.util.List;

public class mainController {
    @FXML public TreeView<Database> Databases;

    @FXML public TabPane tabPanel;

    @FXML public TableView tblProperties;
    @FXML public TextArea cmdWindow;

    @FXML public void openConnection(ActionEvent event)
    {
        try {
            mongoTree();
            setUpContextMenu();
        }
        catch(Exception ex)
        {
        }
    }

    public void initialize()
    {
        TreeItem<Database> root = new TreeItem<Database>(new Database("Mongo instances","","","",""));
        Databases.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Database>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Database>> observableValue, TreeItem<Database> databaseTreeItem, TreeItem<Database> databaseTreeItem2) {
                if ( databaseTreeItem2.getValue().getProperties() != null)
                {
                ObservableList<Property> data = FXCollections.observableArrayList(databaseTreeItem2.getValue().getProperties());
                tblProperties.setItems(data);
                }
                else
                {
                    tblProperties.setItems(null);
                }
            }
        });
        TableColumn<Property,String> columnProperty = new TableColumn<Property, String>("Property");
        columnProperty.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Property, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Property, String> propertyStringCellDataFeatures) {
                return new SimpleStringProperty(propertyStringCellDataFeatures.getValue().getPropertyName());
            }
        });
        TableColumn<Property,String> columnValue = new TableColumn<Property, String>("Value");
        columnValue.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Property, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Property, String> propertyStringCellDataFeatures) {
                return new SimpleStringProperty(propertyStringCellDataFeatures.getValue().getPropertyValue());
            }
        });
        columnProperty.prefWidthProperty().bind(tblProperties.widthProperty().divide(2));
        columnValue.prefWidthProperty().bind(tblProperties.widthProperty().divide(2));
        tblProperties.getColumns().addAll(columnProperty,columnValue);
        Databases.setRoot(root);
    }

    private void mongoTree() throws IOException
    {
        Stage stage = new Stage();
        FXMLLoader loader = getFxmlLoader("/Presentation/loginWindow.fxml");
        Parent view = (Parent) loader.load();
        loginController c = loader.getController();

        stage.setScene(new Scene(view,400,300));
        stage.showAndWait();
        Database connection = c.getConnectionParameters();

        Tree<Database> collections = new DatabaseTree(new MongoDataRepository()).GetDatabaseTree(connection);
        TreeItem<Database> root = new TreeItem<Database>(collections.getRoot());
        GenerateChildren(root, collections.getChildren());

        Databases.getRoot().getChildren().add(root);
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
                            MenuItem treeMenu = new MenuItem("Open Tree View...");
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
            FXMLLoader loader = getFxmlLoader("/Presentation/mongoTreeView.fxml");
            Parent root = (Parent) loader.load();
            MongoTreeViewController c = loader.getController();

            c.initialize(database);
            String tabName;
            if(database.CollectionName.length() > 20) {
                tabName = database.CollectionName.substring(0,20) + "...";
            }
            else
            {
                tabName = database.CollectionName;
            }
            Tab tab = new Tab(tabName);
            tab.setStyle("-fx-background-color: #8bc6fc");
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

        }

    }


    private void mongoTabularView(Database database)  {
        try
        {
        FXMLLoader loader = getFxmlLoader("/Presentation/mongoTabularView.fxml");
        Parent root = (Parent) loader.load();
        MongoTabularViewController c = loader.getController();

        c.initialize(database);
        String tabName;
        if(database.CollectionName.length() > 20) {
            tabName = database.CollectionName.substring(0,20) + "...";
        }
            else
        {
            tabName = database.CollectionName;
        }
        Tab tab = new Tab(tabName);
        tab.setContent(root);
        tab.setStyle("-fx-background-color: #e1bcfb");
        tabPanel.getTabs().add(tab);
        }
        catch(Exception ex)
        {}
    }

    private FXMLLoader getFxmlLoader(String path) {
        URL location = getClass().getResource(path);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        return loader;
    }
}

