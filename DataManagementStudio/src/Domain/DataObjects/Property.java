package Domain.DataObjects;

public class Property {
    private String PropertyName;
    private String PropertyValue;

    public Property(String propertyName,String propertyValue)
    {
        PropertyName = propertyName;
        PropertyValue = propertyValue;
    }

    public String getPropertyName(){
        return PropertyName;
    }

    public String getPropertyValue() {
        return PropertyValue;
    }
}
