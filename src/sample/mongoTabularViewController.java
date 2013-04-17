package sample;

import com.mongodb.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;


public class MongoTabularViewController {

    @FXML
    public TableView tableView;

    public void initialize(String client) throws IOException
    {
        MongoClient mongoClient = new MongoClient(client);
        DB db = mongoClient.getDB("review-test");
        DBCollection collection = db.getCollection("BazaarVoiceReviews");
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

        DBCursor cursor = collection.find().limit(30);
        ObservableList<Hashtable> data = FXCollections.observableArrayList();
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
