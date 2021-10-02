using System;

public class UniversityNameGetter : PropertyDomainBase{

    public UniversityNameGetter() : base("Name"){
        
    }

    public override object getValue(object source)
    {
        return ((University)source).Name;
    }

    public override void setValue(object source, object value)
    {
        //((University)source).GetType().GetProperty(getName()).SetValue(source, value.ToString());
        ((University)source).Name = value.ToString();
    
        //((University)source).Name = // cast tipo do value + value
    }
}