[FireCollection("Class")]
public record Class()
{
    public Class(string Class_Name, string Acronym, bool Optional, long ECTS, Course Course) : this()
    {
        this.Class_Name = Class_Name;
        this.Acronym = Acronym;
        this.Optional = Optional;
        this.ECTS = ECTS;
        this.Course = Course;
    }

    [property: FireKey] public string Class_Name { get; set; }
    public string Acronym { get; set; }
    public bool Optional { get; set; }
    public long ECTS { get; set; }
    public Course Course { get; set; }
}