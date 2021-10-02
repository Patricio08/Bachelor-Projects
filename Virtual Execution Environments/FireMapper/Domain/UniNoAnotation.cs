
public record UniNoAnotation()
{
    public UniNoAnotation(string Name, long Year, string Address) : this() {
        this.Name = Name;
        this.Year = Year;
        this.Address = Address;
    }

    [property: FireKey] public string Name {get; set; }
    public long Year {get; set; }
    public string Address {get; set; }
}