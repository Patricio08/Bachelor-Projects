using System;

[AttributeUsage(AttributeTargets.Property, AllowMultiple=true)]
public class FireIgnoreAttribute : Attribute {
    
    private String name;
    
    public FireIgnoreAttribute(String name)
    {
        this.name = name;
    }

    public FireIgnoreAttribute()
    {
        
    }

}