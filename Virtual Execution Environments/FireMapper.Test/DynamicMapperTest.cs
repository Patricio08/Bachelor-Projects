using System;
using Xunit;
using FireMapper;
using System.Reflection;
using System.Collections.Generic;

namespace FireMapper.Test
{
    public class DynamicMapperTest
    {
        static DynamicIPropertyInstanceCreator dynamicIPropertyInstanceCreator = new DynamicIPropertyInstanceCreator();

        University toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
 
        UniversityStruct structUni = new UniversityStruct("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
        
        Department toGetDep = new Department("ADEETC",5,new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa"));

        [Fact]
        public void TestGenerateIGetterForUniversityName()
        {

            IPropertyDomain aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(University),"Name","FireDataSource",null, typeof(University).GetProperty("Name"));

            Assert.Equal("ISEL", aux.getValue(toGetUni));

        }

        [Fact]
        public void TestGenerateIGetterForUniversityYear()
        {

            IPropertyDomain aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(University),"Year","FireDataSource",null, typeof(University).GetProperty("Year"));

            Assert.Equal((long)1852, aux.getValue(toGetUni));

        }
        
        [Fact]
        public void TestGenerateIGetterForDepartmentComplex()
        {

            IPropertyDomain aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(Department),"University","FireDataSource",null, typeof(Department).GetProperty("University"));

            Assert.Equal(toGetUni, aux.getValue(toGetDep));

        }

        [Fact]
        public void TestGenerateIGetterForUniversityStructName()
        {

            IPropertyDomain aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(UniversityStruct),"Name","FireDataSource",null, typeof(UniversityStruct).GetProperty("Name"));

            Assert.Equal("ISEL", aux.getValue(structUni));

        }
        [Fact]
        public void TestGenerateIGetterForUniversityStructYear()
        {

            IPropertyDomain aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(UniversityStruct),"Year","FireDataSource",null, typeof(UniversityStruct).GetProperty("Year"));

            Assert.Equal((long)1852, aux.getValue(structUni));

        }
    }
}


