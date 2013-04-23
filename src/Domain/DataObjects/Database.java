package Domain.DataObjects;

public class Database {
    private String DatabaseName;
    private String InstanceAddress;
    private String DatabasePort;
    private String CredentialName;
    private String CredentialPassword;

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

    @Override
    public String toString()
    {
        if (CollectionName != null)
            return CollectionName;
        else
            return DatabaseName;
    }
}
