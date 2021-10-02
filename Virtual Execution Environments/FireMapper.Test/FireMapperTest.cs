using System;
using Xunit;
using FireMapper; 


namespace FireMapper.Test
{
    public class FireMapperTest
    {
        [Fact]
        public void TestGetAll()
        {
            //Arrange
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            University[] toGetAllUni = new University[]{
                new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa"),
                new University("IST", 1937, "Av. Rovisco Pais 1, 1049-001 Lisboa")
            };
            //Act
            University[] toGetAll = new University[2];
            int i=0;
            foreach (var item in testMapper.GetAll())
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
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            University toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
            //Act
            object obj = testMapper.GetById("ISEL");
        
            //Assert
            Assert.Equal(
                toGetUni,
                obj
            );

        }

        [Fact]
        public void TestGetByIdWithNoAnotationType()
        {   
            /// OLD VERSION
            //IDataMapper fireTestMapperUni = new FireDataMapper(typeof(UniNoAnotation), "UniNoAnotation", "Name", "FireDataSource", null);
            //University toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
            //object obj = fireTestMapperUni.GetById("ISEL");
            //Assert.True(obj ==null);

            Assert.Throws<System.Exception>(() => {
                IDataMapper fireTestMapperUni = new FireDataMapper(typeof(UniNoAnotation), "UniNoAnotation", "Name", "FireDataSource", null);
            });

        }
         [Fact]
        public void TestGetByIdWith2FireKeys()
        {
            
            IDataMapper fireTestMapperUni = new FireDataMapper(typeof(Uni2FireKeys), "Uni2FireKeys", "Name", "FireDataSource", null);
            University toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
           
            object obj = fireTestMapperUni.GetById("ISEL");
            
             Assert.True(obj ==null);

        }
        [Fact]
        public void TestGetByIdWithInvalidID()
        {
            //Arrange
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            //Act
            object obj = testMapper.GetById("XPTO");
        
            //Assert
            Assert.True(
                obj==null
            );
        }

        [Fact]
        public void TestAddAndDelete()
        {
            
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //check if already exists
            object obj = testMapper.GetById("TestUni");
            Assert.True(obj==null);
            
            //Add the new one
            testMapper.Add(toAddUni);
            obj = testMapper.GetById("TestUni");
            Assert.Equal(
                toAddUni,
                obj
            );

            //Delete
            testMapper.Delete("TestUni");
            obj = testMapper.GetById("TestUni");
            Assert.True(obj==null);
        }
    
        [Fact]
        public void TestAdd2Times()
        {
            
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //check if already exists
            testMapper.GetById("TestUni");
            
            //Add the new one
            testMapper.Add(toAddUni);
            object obj = testMapper.GetById("TestUni");
            Assert.Equal(
                toAddUni,
                obj
            );
            bool exce =false;
             try{
                 testMapper.Add(toAddUni);
             }
             catch{
                 exce=true;
             }
             Assert.True(exce);

            //Delete
            testMapper.Delete("TestUni");
        }
        
        [Fact]
        public void TestAddWithWrongType()
        {
            
            IDataMapper testMapper = new FireDataMapper(typeof(Department), "Department", "Name", "FireDataSource", null);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            //Add the new one
            bool exce =false;
             try{
                 testMapper.Add(toAddUni);
             }
             catch{
                 exce=true;
             }
             Assert.True(exce);
        }

        [Fact]
        public void TestUpdate()
        {
            
            IDataMapper testMapper = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            University toAddUni = new University("TestUni", 1998, "Rua de Test");
            University toUpdateUni = new University("TestUni", 2000, "Rua de Test 2");
            //Add the new one
            testMapper.Add(toAddUni);
            object obj = testMapper.GetById("TestUni");
            
            //Update it
            testMapper.Update(toUpdateUni);
            obj = testMapper.GetById("TestUni");
            Assert.Equal(
                toUpdateUni,
                obj
            );

            //Delete
            testMapper.Delete("TestUni");
        }
    }
}
