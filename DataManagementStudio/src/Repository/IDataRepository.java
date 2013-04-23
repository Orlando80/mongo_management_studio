package Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: orlando
 * Date: 23/04/2013
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
public interface IDataRepository {
    List<String> getDbs(String connectionString);

    List<String> getCollectionNames(String connectionString, String db);
}
