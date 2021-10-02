using System.Reflection;

public interface IPropertyDomain{

	public string getName();
	public object getValue(object source);
	public void setValue(object source, object value);

}
