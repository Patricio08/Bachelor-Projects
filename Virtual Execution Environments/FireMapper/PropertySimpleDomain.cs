using System;
using System.Reflection;

public class PropertySimpleDomain : IPropertyDomain
{
    private PropertyInfo prop;
    public PropertySimpleDomain(PropertyInfo prop)
    {
        this.prop = prop;  
    }

    public string getName()
    {
        return prop.Name;
    }

    public object getValue(object source)
    {
        return prop.GetValue(source);
    }

    public void setValue(object source, object value)
    {
        prop.SetValue(source, Convert.ChangeType(value, prop.PropertyType));
    }

}