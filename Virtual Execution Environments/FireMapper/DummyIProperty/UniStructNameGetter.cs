public class UniStructNameGetter : PropertyDomainBase{

    public UniStructNameGetter() : base("Name"){
        
    }

    public override object getValue(object source)
    {
        return ((UniversityStruct)source).Name;
    }

    public override void setValue(object source, object value)
    {
        var p2 = (UniversityStruct)source;
        p2.Name = value.ToString();
        source = (object)p2;
        
        //((University)source).GetType().GetProperty(getName()).SetValue(source, value.ToString());
        //((UniversityStruct)source).Name = value.ToString();
    
        //((University)source).Name = // cast tipo do value + value
    }
}