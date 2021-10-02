[FireCollection("Uni2FireKeys")]
public record Uni2FireKeys()
{
    public Uni2FireKeys(string Name, long Year, string Address) : this() {
        this.Name = Name;
        this.Year = Year;
        this.Address = Address;
    }

    [property: FireKey] public string Name {get; set; }
    [property: FireKey]public long Year {get; set; }
    public string Address {get; set; }
 }