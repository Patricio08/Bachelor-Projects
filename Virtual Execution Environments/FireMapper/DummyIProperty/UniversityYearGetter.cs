using System;

public class UniversityYearGetter : PropertyDomainBase
{
    public UniversityYearGetter(string name) : base("Year")
    {
    }

    public override object getValue(object source)
    {
        return ((University)source).Year;
    }

    public override void setValue(object source, object value)
    {
         ((University)source).Year = (long)Convert.ChangeType(value, typeof(long));
    }
}