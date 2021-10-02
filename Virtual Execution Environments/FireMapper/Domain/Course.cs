[FireCollection("Course")]
public record Course()
{
    public Course(string Course_Name, string Coordinator, Department Department) : this()
    {
        this.Course_Name = Course_Name;
        this.Coordinator = Coordinator;
        this.Department = Department;
    }
    [property: FireKey] public string Course_Name { get; set; }
    public string Coordinator { get; set; }
    public Department Department { get; set; }
}