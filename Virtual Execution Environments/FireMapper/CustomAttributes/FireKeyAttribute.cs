using System;

[AttributeUsage(AttributeTargets.Property, AllowMultiple=true)]
public class FireKeyAttribute : Attribute {
    
    private String key;
    
    public FireKeyAttribute(String key)
    {
        this.key = key;
    }

    public FireKeyAttribute()
    {
        
    }

}