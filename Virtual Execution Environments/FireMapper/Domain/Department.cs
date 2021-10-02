[FireCollection("Department")]
public record Department()
{
    public Department(string Name, long NumberOfCourses, University University) : this()
    {
        this.Name = Name;
        this.NumberOfCourses = NumberOfCourses;
        this.University = University;
    }
    [property: FireKey] public string Name  { get; set; }

    public long NumberOfCourses { get; set; }

    [property: FireIgnore] public University University { get; set; }

}