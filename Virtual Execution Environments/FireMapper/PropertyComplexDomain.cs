using System;
using System.Reflection;
using FireMapper;
using FireSource;

public class PropertyComplexDomain : IPropertyDomain
{
    private PropertyInfo prop;
	private readonly Type prop_type;
	private readonly string prop_name;
	private readonly string source;
    private readonly string path;
    
    IDataMapper fireMapper;

    public PropertyComplexDomain(PropertyInfo prop, string source, string path)			// SOURCE PARA DEPOIS IR BUSCAR OBJETOS DE DOMINIO
    {
        this.prop = prop;
		this.prop_type = prop.PropertyType;
		this.prop_name = prop.Name;
		this.source = source;
        this.path = path;

        PropertyInfo foreignKeyProp = GetFireKey(prop_type);
        fireMapper = new FireDataMapper(prop_type, prop_name, foreignKeyProp.Name, source, path);

    }
    public string getName()
    {
        return prop_name;
    }

    public object getValue(object source)
    {
        return prop.GetValue(source);
    }

    public void setValue(object obj, object newValue)
    {
        //PropertyInfo foreignKeyProp = GetFireKey(prop_type);
        //IDataMapper fireMapper = new FireDataMapper(prop_type, prop_name, foreignKeyProp.Name, source, path);

        prop.SetValue(obj, fireMapper.GetById(newValue));

    }
    
     private PropertyInfo GetFireKey(Type type)
    {
        
            PropertyInfo[] props = type.GetProperties();
            int counter = 0;
            PropertyInfo ret = null;
            foreach (var prop in props)
            {
                if (Attribute.GetCustomAttribute(prop, typeof(FireKeyAttribute)) != null)
                {
                    ++counter;
                    if (counter > 1)
                    {
                        Console.WriteLine("There is more than one FireKey anotation");
                        throw new Exception();
                    }
                    ret = prop;
                }
            }
            if (ret != null)
                return ret;

            Console.WriteLine("There is no FireKey anotation");
            throw new NullReferenceException();
    }

}