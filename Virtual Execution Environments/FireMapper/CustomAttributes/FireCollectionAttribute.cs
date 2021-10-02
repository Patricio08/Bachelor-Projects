using System;

[AttributeUsage(AttributeTargets.All, AllowMultiple = false)]
public class FireCollectionAttribute : Attribute
{

    private String name;

    public FireCollectionAttribute(String name)
    {
        this.name = name;
    }

    public FireCollectionAttribute()
    {

    }

    public string Name
    {
        get { return name; }
    }

}