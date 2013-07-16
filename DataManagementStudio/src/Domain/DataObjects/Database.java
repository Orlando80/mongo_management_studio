package Domain.DataObjects;

import java.util.Hashtable;
import java.util.Set;

public class Database {
    private String DatabaseName;
    private String InstanceAddress;
    private String DatabasePort;
    private String CredentialName;
    private String CredentialPassword;
    private Set<Property> Properties;

    public String CollectionName;

    public Database(String databaseName, String instanceAddress,String databasePort, String credentialName, String credentialPassword)
    {
        DatabaseName = databaseName;
        InstanceAddress = instanceAddress;
        DatabasePort = databasePort;
        CredentialName = credentialName;
        CredentialPassword = credentialPassword;
    }

    public String getDatabaseName()
    {
        return DatabaseName;
    }

    public String getInstanceAddress()
    {
        return InstanceAddress;
    }

    public String getDatabasePort()
    {
        return DatabasePort;
    }

    public String getCredentialName()
    {
        return CredentialName;
    }

    public String getCredentialPassword()
    {
        return CredentialPassword;
    }

    public void setProperties(Set<Property> value)
    {
        Properties = value;
    }

    public Set<Property> getProperties()
    {
        return Properties;
    }


    @Override
    public String toString()
    {
        if (CollectionName != null)
            return CollectionName;
        else
            return DatabaseName;
    }
}
