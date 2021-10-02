using System;
using Xunit;
using FireMapper; 


namespace FireSource.Tests
{
    public class FireSourceTest
    {
        private string file = "./../../../../Resources/Data_University.txt";
		[Fact]
        public void TestGetAll()
        {
            //Arrange
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", file);
            University[] toGetAllUni = new University[]{
                new University("ISEL-TXT", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa"),
                new University("IST-TXT", 1937, "Av. Rovisco Pais 1, 1049-001 Lisboa")
            };
            //Act
            University[] toGetAll = new University[2];
            int i=0;
            foreach (var item in weakTestMapperUni.GetAll())
            { 
                toGetAll[i++]=(University)item;
                
            }
            //Assert
            Assert.Equal(
                toGetAllUni,
                toGetAll
            );

        }
		
		[Fact]
    	public void TestGetById()
        {
            //Arrange
			IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", file);
            University toGetUni = new University("ISEL-TXT", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
            //Act
            object obj = weakTestMapperUni.GetById("ISEL-TXT");
        
            //Assert
            Assert.Equal(
                toGetUni,
                obj
            );

        }

       [Fact]
        public void TestAddAndDelete()
        {
            
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", file);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //check if already exists
            object obj = weakTestMapperUni.GetById("TestUni");
            Assert.True(obj==null);
            
            //Add the new one
            weakTestMapperUni.Add(toAddUni);
            obj = weakTestMapperUni.GetById("TestUni");
            Assert.Equal(
                toAddUni,
                obj
            );

            //Delete
            weakTestMapperUni.Delete("TestUni");
            obj = weakTestMapperUni.GetById("TestUni");
            Assert.True(obj==null);
        }

        [Fact]
        public void TestAdd2Times()
        {
            
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", file);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //check if already exists
            weakTestMapperUni.GetById("TestUni");
            
            //Add the new one
            weakTestMapperUni.Add(toAddUni);
            object obj = weakTestMapperUni.GetById("TestUni");
            Assert.Equal(
                toAddUni,
                obj
            );
            bool exce =false;
             try{
                 weakTestMapperUni.Add(toAddUni);
             }
             catch{
                 exce=true;
             }
             Assert.True(exce);

            //Delete
            weakTestMapperUni.Delete("TestUni");
        }
        
        [Fact]
        public void TestAddWithWrongType()
        {
            
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(Department), "Department", "Name", "WeakDataSource", file);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //Add the new one
            bool exce =false;
             try{
                 weakTestMapperUni.Add(toAddUni);
             }
             catch{
                 exce=true;
             }
             Assert.True(exce);
        }

    [Fact]
        public void TestUpdate()
        {
            
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", file);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            University toUpdateUni = new University("TestUni", 2000, "Rua de Test 2");
            //Add the new one
            weakTestMapperUni.Add(toAddUni);
            object obj = weakTestMapperUni.GetById("TestUni");
            
            //Update it
            weakTestMapperUni.Update(toUpdateUni);
            obj = weakTestMapperUni.GetById("TestUni");
            Assert.Equal(
                toUpdateUni,
                obj
            );

            //Delete
            weakTestMapperUni.Delete("TestUni");
        }
       
 
       
    }
}
