using System;
using System.Collections.Generic;
using Google.Cloud.Firestore;

using FireMapper;
using System.IO;
using System.Reflection;

namespace AppFireStudents
{
    class Program
    {  
        // run weakData
        private static string FILE_PATH = "./Resources/Data_University.txt";
        // debug
        //private static string FILE_PATH = "./../Resources/Data_University.txt";


        // auxiliar para o BenchMarking
        // irá simular uma lista retornada pela source
        // desta forma maximizamos a comparação de desempenho entre os diferentes tipos de mapper
        static Dictionary<String,Object> mockList;

        // mapper do tipo Dynamic com WeakDataSource e do tipo FireData com WeakDataSource, auxiliar ao benchmarking GetObject
        static AbstractFireDataMapper abstractWeakDfmUni, abstractWeakTestMapperUni;
        // mapper auxiliar Benchmark ao GetById
        static IDataMapper fireWeakUni, dynWeakUni;
        // obtencao do gerador das classes dinamicas, utilizadas no benchmarking dummy class vs dynamic class
        static DynamicIPropertyInstanceCreator dynamicIPropertyInstanceCreator;
        static IPropertyDomain testDummyPrimitive, testCreatorPrimitive, testCreatorComplex, testDummyComplex, testCreatorStruct;

        static University toGetUni;
        static Department testDep;
        static UniversityStruct structUni;
       
        static void Main(string[] args)
        {   
            //// Demos que mostram as chamadas dos métodos CRUD nos diferentes tipos de dominio
            //// Diferentes versões, com diferentes mappers e sources
            //DemoFireMapperWeakSource();
            //DemoFireMapperFireSource();
            DemoDynamicMapperFireSource();
            //DemoFireMapperWeakSource();

            /*
            //// Teste de desempenho ao método GetObject, peça fundamental dos métodos de leitura GetById e GetAll
            //// este método é implementado de forma diferente consoante o mapper com que estamos a trabalhar (FireData ou Dynamic)
            SetupBenchGetObject();
            NBench.Bench(Program.BenchIsolatedFireMapper);
            NBench.Bench(Program.BenchIsolatedDynMapper);

            //// Teste de desempenho ao método GetByID
            //// Aqui o overhead é superior ao teste de desempenho acima demonstrado. Mais instancias,...
            SetupBenchGetById();
            NBench.Bench(Program.BenchWeakFireMapperReflectionUniversity);
            NBench.Bench(Program.BenchWeakFireMapperDynamicUniversity);
            
            //// Teste de desempenho na obtenção de valores primitivos entre uma classe Dummy e uma classe gerada dinamicamente
            SetupBenchDummyEmitPrimitive();
            NBench.Bench(Program.BenchDummyPrimitiveProperty);
            NBench.Bench(Program.BenchCreatorPrimitiveProperty);

            //// Teste de desempenho na obtenção de valores do tipo primitivo e de dominio entre uma classe Dummy e uma classe gerada dinamicamente
            SetupBenchDummyEmitComplex();
            NBench.Bench(Program.BenchDummyComplexProperty);
            NBench.Bench(Program.BenchCreatorComplexProperty);

            //// Teste de desempenho na obtenção de valores do tipo primitivo a uma struct
            SetupBenchStruct();
            NBench.Bench(Program.BenchStructType);*/
        }

        private static void SetupBenchGetObject(){
            mockList = new Dictionary<string, object>();
            mockList.Add("Name", "ISEL-TXT");
            mockList.Add("Year", "1852");
            mockList.Add("Address", "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");

            abstractWeakDfmUni = new DynamicFireMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);
            abstractWeakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);

        }

        private static void BenchIsolatedFireMapper(){
            abstractWeakTestMapperUni.GetObject(mockList);
        }

        private static void BenchIsolatedDynMapper(){
            abstractWeakDfmUni.GetObject(mockList);
        }

        
        private static void SetupBenchGetById(){
            fireWeakUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);
            dynWeakUni = new DynamicFireMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);
        }
        private static void BenchWeakFireMapperReflectionUniversity() {
           fireWeakUni.GetById("ISEL-TXT");
        }
        private static void BenchWeakFireMapperDynamicUniversity() {
            dynWeakUni.GetById("ISEL-TXT");
        }


        private static void SetupBenchDummyEmitPrimitive(){
            testDummyPrimitive = new UniversityNameGetter();
            dynamicIPropertyInstanceCreator = new DynamicIPropertyInstanceCreator();
            testCreatorPrimitive = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(University),"Name","WeakDataSource",FILE_PATH, typeof(University).GetProperty("Name"));
        
            toGetUni = new University("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
        }

        private static void BenchDummyPrimitiveProperty(){
            testDummyPrimitive.getValue(toGetUni);
        }

        private static void BenchCreatorPrimitiveProperty(){
           testCreatorPrimitive.getValue(toGetUni);
        }


        private static void SetupBenchDummyEmitComplex(){
            testDep = new Department("ADEETC", 4, toGetUni);
            dynamicIPropertyInstanceCreator = new DynamicIPropertyInstanceCreator();
            testCreatorComplex =  dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(Department),"University","WeakDataSource", FILE_PATH, typeof(Department).GetProperty("University"));
            testDummyComplex = new DepartmentUniversityGetter("University", "Name", "WeakDataSource", FILE_PATH);
        }

        private static void BenchDummyComplexProperty(){
            testDummyComplex.getValue(testDep);
        }

        private static void BenchCreatorComplexProperty(){
            testCreatorComplex.getValue(testDep);
        }


        private static void SetupBenchStruct(){
            structUni = new UniversityStruct("ISEL", 1852, "R. Conselheiro Emídio Navarro 1, 1959-007 Lisboa");
            testCreatorStruct = dynamicIPropertyInstanceCreator.CreateIPropertyFor(typeof(UniversityStruct),"Name","WeakDataSource",FILE_PATH, typeof(UniversityStruct).GetProperty("Name"));

        }
        private static void BenchStructType(){
            testCreatorStruct.getValue(structUni);
        }



        private static void DemoFireMapperWeakSource(){
            // SETUP
            // FireDataMappers para os diferentes dominios
            IDataMapper weakTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);
            IDataMapper weakTestMapperDep = new FireDataMapper(typeof(Department), "Department", "Name", "WeakDataSource", FILE_PATH);

            // Dominios a adicionar
            University weakToAddUni = new University("Beira", 2020, "Panteão Nacional");
            Department weakToAddDep = new Department("TxtDep", 95, weakToAddUni);

            // Dominios a atualizar
            University weakToUpdateUni = new University("Beira", 2021, "Rua do Update");
            University toUpdateUni2 = new University("INEXSISTENTE", 2021, "Olival");       // para demonstração de tratamento de erros
            Department weakToUpdateDep = new Department("TxtDep", 5, weakToUpdateUni);

            //ACT
            // Chamada GetAll()
            Console.WriteLine("\n:: Utilização do FireDataMapper e WeakDataSource ::");
            Console.WriteLine(":: GetAll University ::");
            foreach (var item in weakTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: GetAll Department ::");
            foreach (var item in weakTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada GetId()
            Console.WriteLine("\n:: GetID University com chave 'ISEL-TXT' ::");
            object uni = weakTestMapperUni.GetById("ISEL-TXT");
            if(uni == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(uni);

            Console.WriteLine("\n:: GetID Department com chave 'ADEETC-TXT' e University 'ISEL-TXT' como membro ::");
            object dep = weakTestMapperDep.GetById("ADEETC-TXT");
            if(dep == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(dep);

            
            // Chamada Método Add()
            Console.WriteLine("\n:: Add University 'weakToAddUni' ::");
            weakTestMapperUni.Add(weakToAddUni);
	        foreach (var item in weakTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: Add Department 'weakToAddDep com membro 'weakToAddUni' ::");
            weakTestMapperDep.Add(weakToAddDep);
            foreach (var item in weakTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);
            
               
            // Chamada Método Update()
            Console.WriteLine("\n:: Update University com a chave de 'weakToUpdateUni' com informação de 'weakToUpdateUni' ::");
            weakTestMapperUni.Update(weakToUpdateUni);
            foreach (var item in weakTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: Update Department com a chave de 'weakToUpdateDep' com informação de 'weakToUpdateDep' ::");
            weakTestMapperDep.Update(weakToUpdateDep);
            foreach (var item in weakTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);
            
            
            // Chamada Método Delete()
            Console.WriteLine("\n:: Delete University e Department adicionados ::");
            weakTestMapperUni.Delete("Beira");
            weakTestMapperDep.Delete("TxtDep");
            foreach (var item in weakTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);
            foreach (var item in weakTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);
            
            Console.WriteLine("\n:: Fim Demo FireDataMapper e WeakDataSource ::");
        }

        private static void DemoFireMapperFireSource(){
            // SETUP
            // FireDataMappers para os diferentes dominios
            IDataMapper fireTestMapperUni = new FireDataMapper(typeof(University), "University", "Name", "FireDataSource", null);
            IDataMapper fireTestMapperDep = new FireDataMapper(typeof(Department), "Department", "Name", "FireDataSource", null);

            // Dominios a adicionar
            University toAddUni = new University("FCPORTO", 1893, "Estadio do Dragao");
            Department toAddDep = new Department("Teste", 1, toAddUni);

            // Dominios a atualizar
            University toUpdateUni = new University("FCPORTO", 2021, "Olival");
            University toUpdateUni2 = new University("INEXSISTENTE", 2021, "Olival");                 // para demonstração de tratamento de erros
            Department toUpdateDep = new Department("Teste", 2, toAddUni);

            //ACT
            // Chamada GetAll()
            Console.WriteLine("\n:: Utilização do FireDataMapper e FireDataSource ::");
            Console.WriteLine(":: GetAll University ::");
            foreach (var item in fireTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: GetAll Department ::");
            foreach (var item in fireTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada GetId()
            Console.WriteLine("\n:: GetID University com chave 'ISEL' ::");
            object uni = fireTestMapperUni.GetById("ISEL");
            if(uni == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(uni);

            Console.WriteLine("\n:: GetID Department com chave 'ADEETC' e University 'ISEL' como membro ::");
            object dep = fireTestMapperDep.GetById("ADEETC");
            if(dep == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(dep);


            // Chamada Método Add()
            Console.WriteLine("\n:: Add University 'toAddUni' ::");
            fireTestMapperUni.Add(toAddUni);
	        foreach (var item in fireTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: Add Department 'toAddDep com membro 'toAddUni' ::");
            fireTestMapperDep.Add(toAddDep);
            foreach (var item in fireTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada Método Update()
            Console.WriteLine("\n:: Update University com a chave de 'toUpdateUni' com informação de 'toUpdateUni' ::");
            fireTestMapperUni.Update(toUpdateUni);
            foreach (var item in fireTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: Update Department com a chave de 'toUpdateDep' com informação de 'toUpdateDep' ::");
            fireTestMapperDep.Update(toUpdateDep);
            foreach (var item in fireTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada Método Delete()
            Console.WriteLine("\n:: Delete University e Deparment adicionados ::");
            fireTestMapperUni.Delete("FCPORTO");
            fireTestMapperDep.Delete("Teste");
            foreach (var item in fireTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);
            foreach (var item in fireTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: Fim Demo FireDataMapper e FireDataSource ::");
        }

        private static void DemoDynamicMapperFireSource(){
            // SETUP
            // DynamicMappers para os diferentes dominios
            IDataMapper dynTestMapperUni = new DynamicFireMapper(typeof(University), "University", "Name", "FireDataSource", null);
            IDataMapper dynTestMapperDep = new DynamicFireMapper(typeof(Department), "Department", "Name", "FireDataSource", null);

            // Dominios a adicionar
            University toAddUni = new University("FCPORTO", 1893, "Estadio do Dragao");
            Department toAddDep = new Department("Teste", 1, toAddUni);

            // Dominios a atualizar
            University toUpdateUni = new University("FCPORTO", 2021, "Olival");
            University toUpdateUni2 = new University("INEXSISTENTE", 2021, "Olival");                 // para demonstração de tratamento de erros
            Department toUpdateDep = new Department("Teste", 2, toAddUni);

            //ACT
            // Chamada GetAll()
            Console.WriteLine("\n:: Utilização do FireDataMapper e FireDataSource ::");
            Console.WriteLine(":: GetAll University ::");
            foreach (var item in dynTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: GetAll Department ::");
            foreach (var item in dynTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada GetId()
            Console.WriteLine("\n:: GetID University com chave 'ISEL' ::");
            object uni = dynTestMapperUni.GetById("ISEL");
            if(uni == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(uni);

            Console.WriteLine("\n:: GetID Department com chave 'ADEETC' e University 'ISEL' como membro ::");
            object dep = dynTestMapperDep.GetById("ADEETC");
            if(dep == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(dep);


            Console.WriteLine("\n:: Fim Demo DynDataMapper e FireDataSource ::");
        }
        
        private static void DemoDynamicMapperWeakSource(){
            // SETUP
            // DynamicMappers para os diferentes dominios
            IDataMapper dynWeakTestMapperUni = new DynamicFireMapper(typeof(University), "University", "Name", "WeakDataSource", FILE_PATH);
            IDataMapper dynWeakTestMapperDep = new DynamicFireMapper(typeof(Department), "Department", "Name", "WeakDataSource", FILE_PATH);

            // Dominios a adicionar
            University weakToAddUni = new University("Beira", 2020, "Panteão Nacional");
            Department weakToAddDep = new Department("TxtDep", 95, weakToAddUni);

            // Dominios a atualizar
            University weakToUpdateUni = new University("Beira", 2021, "Rua do Update");
            University toUpdateUni2 = new University("INEXSISTENTE", 2021, "Olival");       // para demonstração de tratamento de erros
            Department weakToUpdateDep = new Department("TxtDep", 5, weakToUpdateUni);

            //ACT
            // Chamada GetAll()
            Console.WriteLine("\n:: Utilização do FireDataMapper e WeakDataSource ::");
            Console.WriteLine(":: GetAll University ::");
            foreach (var item in dynWeakTestMapperUni.GetAll()) 
    		    Console.WriteLine(item);

            Console.WriteLine("\n:: GetAll Department ::");
            foreach (var item in dynWeakTestMapperDep.GetAll()) 
    		    Console.WriteLine(item);


            // Chamada GetId()
            Console.WriteLine("\n:: GetID University com chave 'ISEL-TXT' ::");
            object uni = dynWeakTestMapperUni.GetById("ISEL-TXT");
            if(uni == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(uni);

            Console.WriteLine("\n:: GetID Department com chave 'ADEETC-TXT' e University 'ISEL-TXT' como membro ::");
            object dep = dynWeakTestMapperDep.GetById("ADEETC-TXT");
            if(dep == null) 
        	    Console.WriteLine("Invalid ID");
            else 
                Console.WriteLine(dep);


            Console.WriteLine("\n:: Fim Demo DynanmicFireMapper e WeakDataSource ::");
        }
        

        

        /*
*/

    }
}