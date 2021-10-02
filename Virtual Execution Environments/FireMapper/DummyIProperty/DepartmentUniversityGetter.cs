using FireMapper;

public class DepartmentUniversityGetter : PropertyDomainBase
{
    //public IDataMapper dynFireMapper;

    public DepartmentUniversityGetter(string name, string key, string dataSource, string path) : base(typeof(University),"University", "Name", "FireDataSource", null)
    {
        //dynFireMapper = new DynamicFireMapper(typeof(University), "University", key, dataSource, path);
    }
    public override object getValue(object source)
    {
        return ((Department)source).University;
    }

    public override void setValue(object source, object value)
    {
        //IDataMapper dynfireMapper = new DynamicFireMapper(typeof(University), "University", key, dataSource, path);
        
        ((Department)source).University = (University)dynFireMapper.GetById(value);
    }
}