public class UniStructYearGetter : PropertyDomainBase{

    public UniStructYearGetter() : base("Year"){
        
    }

    public override object getValue(object source)
    {
        return ((UniversityStruct)source).Year;
    }

    public override void setValue(object source, object value)
    {
        //((University)source).GetType().GetProperty(getName()).SetValue(source, value.ToString());
        //((UniversityStruct)source).Name = value.ToString();
    
        //((University)source).Name = // cast tipo do value + value
    }
}