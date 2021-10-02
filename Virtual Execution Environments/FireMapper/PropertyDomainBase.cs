using System;
using System.Reflection;
using FireMapper;

public abstract class PropertyDomainBase : IPropertyDomain
{
    public string name, key, dataSource, path;

    public IDataMapper dynFireMapper;

    public PropertyDomainBase(string name)
    {
        this.name = name;
    }

    public PropertyDomainBase(Type type, string name, string key, string dataSource, string path)
    {
        this.name = name;
        this.key = key;
        this.dataSource = dataSource;
        this.path = path;

        this.dynFireMapper =  new DynamicFireMapper(typeof(University), "University", key, dataSource, path);
    }

    public string getName()
    {
        return name;
    }

    public abstract object getValue(object source);
    public abstract void setValue(object source, object value);
}