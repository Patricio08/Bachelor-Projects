public struct UniversityStruct{
    [property: FireKey] public string Name {get; set; }
    public long Year {get; set; }
    public string Address {get; set; }

    public UniversityStruct(string name, long year, string address)
    {
        Name = name;
        Year = year;
        Address = address;
    }
}