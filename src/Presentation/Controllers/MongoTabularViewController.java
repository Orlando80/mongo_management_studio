package Presentation.Controllers;

import Domain.DataObjects.Database;
import com.mongodb.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Set;


public class MongoTabularViewController {

    private int pageSize = 20;
    @FXML public Label pageNumberControl;
    @FXML public TextField pageSizeControl;
    private int page = 0;
    private Database database;
    @FXML
    public TableView tableView;
    @FXML public void previous(ActionEvent event)
    {
          if(page> 0) {
              page--;
             try
             {
                FillTable();
             }
             catch(Exception ex)
             {}
          }
    }
    @FXML public void next(ActionEvent event)
    {
        page++;
        try
        {
            FillTable();
        }
        catch(Exception ex)
        {}
    }
    @FXML public void refresh(ActionEvent event)
    {
        try
        {
            FillTable();
        }
        catch(Exception ex)
        {

        }
    }

    public void initialize(Database database) throws IOException
    {
        this.database = database;
        pageSizeControl.setText(Integer.toString(pageSize));
        FillTable();
    }

    private void FillTable() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(database.getInstanceAddress());
        DB db = mongoClient.getDB(database.getDatabaseName());
        DBCollection collection = db.getCollection(database.CollectionName);
        DBObject dbObject = collection.findOne();
        Set<String> keySet = dbObject.keySet();

        for(final String key: keySet)
        {
            TableColumn<Hashtable,String> column = new TableColumn(key);
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Hashtable, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Hashtable, String> hashtableStringCellDataFeatures) {
                    return new SimpleStringProperty(hashtableStringCellDataFeatures.getValue().get(key).toString());
                }
            });
            tableView.getColumns().add(column);
        }
        pageSize = Integer.parseInt(pageSizeControl.getText());

        if (pageSize == 0 ) pageSize = 20;
        DBCursor cursor = collection.find().skip(page * pageSize).limit(pageSize);
        ObservableList<Hashtable> data = FXCollections.observableArrayList();
        int pages = (int)Math.floor(cursor.count()/ pageSize);
        String pageText = "Page " + page + " of " + pages;
        pageNumberControl.setText(pageText);
        while(cursor.hasNext())
        {
            DBObject obj = cursor.next();
            Hashtable table = new Hashtable();

            for(String set: obj.keySet())
            {

                if(obj.get(set) != null) {
                    table.put(set, obj.get(set).toString());
                }
                else
                {
                    table.put(set,"nil");
                }

            }
            data.add(table);
        }
        tableView.setItems(data);
    }
}
