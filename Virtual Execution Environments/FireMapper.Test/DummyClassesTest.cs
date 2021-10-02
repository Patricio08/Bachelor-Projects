using System;
using Xunit;
using FireMapper;
using System.Reflection;
using System.Collections.Generic;

namespace FireMapper.Test
{
    public class DummyClassesTest
    {
        
        University toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
		University toGetUni2 = new University("IST", 1937, "Av. Rovisco Pais 1, 1049-001 Lisboa");
		
        UniversityStruct structUni = new UniversityStruct("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
        
        Department toGetDep = new Department("ADEETC",5,new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa"));

        [Fact]
        public void TestUniversityNameGetter()
        {

            UniversityNameGetter uniNameGetter = new UniversityNameGetter();

			Assert.Equal("ISEL", uniNameGetter.getValue(toGetUni));
			uniNameGetter.setValue(toGetUni,"ISEL-2");
			Assert.Equal("ISEL-2", uniNameGetter.getValue(toGetUni));
			uniNameGetter.setValue(toGetUni,"ISEL");
        }

		[Fact]
        public void TestUniversityYearGetter()
        {

            UniversityYearGetter uniYearGetter = new UniversityYearGetter("Year");

			Assert.Equal((long)1852,  uniYearGetter.getValue(toGetUni));
			uniYearGetter.setValue(toGetUni,(long)1998);
			Assert.Equal((long)1998, uniYearGetter.getValue(toGetUni));
			uniYearGetter.setValue(toGetUni,(long)1852);
        }

		[Fact]
        public void TestUniversityStructNameGetter()
        {

            UniStructNameGetter UniStructNameGetter = new UniStructNameGetter();

			Assert.Equal("ISEL", UniStructNameGetter.getValue(structUni));
			
			
        }
		[Fact]
        public void TestUniversityStructYearGetter()
        {

            UniStructYearGetter UniStructYearGetter = new UniStructYearGetter();

			Assert.Equal((long)1852, UniStructYearGetter.getValue(structUni));	
        }

		[Fact]
        public void TestDepartmentUniversityGetter()
        {

            DepartmentUniversityGetter departmentUniversityGetter = new DepartmentUniversityGetter("Name","University", "FireDataSource",null);

			Assert.Equal(toGetUni, departmentUniversityGetter.getValue(toGetDep));	
			departmentUniversityGetter.setValue(toGetDep,"IST");
			Assert.Equal(toGetUni2, departmentUniversityGetter.getValue(toGetDep));
			departmentUniversityGetter.setValue(toGetDep,"ISEL");
        }

    }
}
