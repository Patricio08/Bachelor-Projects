using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using FireSource;


namespace FireMapper
{
    public abstract class AbstractFireDataMapper : IDataMapper
    {
        string PROJECT_ID = "ave-41d-g31";
        string FIRE_KEY;
        // dotnet test
        //string CREDENTIALS_PATH = "./../../../../Resources/ave-41d-g31-firebase-adminsdk-x8nee-ef8eb0d240.json";
        // dotnet run
        string CREDENTIALS_PATH = "./Resources/ave-41d-g31-firebase-adminsdk-x8nee-ef8eb0d240.json";
        // debug
        //string CREDENTIALS_PATH = "./../Resources/ave-41d-g31-firebase-adminsdk-x8nee-ef8eb0d240.json";

        Type type;
        string collection, typeDataSource, path;
        IDataSource dataSource;
        IDataMapper auxMapper;
        
        public AbstractFireDataMapper(Type type, string collection, string key, string typeDataSource, string path)
        {
            this.type = type;
            this.collection = collection;
            this.FIRE_KEY = key;
            this.path = path;

            this.typeDataSource = typeDataSource;
            if (typeDataSource.Equals("FireDataSource"))
                dataSource = new FireDataSource(
                    PROJECT_ID,
                    collection,
                    FIRE_KEY,
                    CREDENTIALS_PATH
                );
            else
            {
                dataSource = new WeakDataSource(key, collection, path);
            }

            GetFireCollection(this.type);
        }

        IEnumerable IDataMapper.GetAll()
        {
            IEnumerable<Dictionary<String, Object>> list = this.dataSource.GetAll();
            List<object> toReturn = new List<object>();                                 

            if (list == null)
                return null;

            foreach (Dictionary<String, Object> domains in list)
                toReturn.Add(GetObject(domains));
            
            return toReturn;
        }

        object IDataMapper.GetById(object keyValue)
        {
            Dictionary<String, Object> list = this.dataSource.GetById(keyValue);

            if (list == null)
                return null;

            return GetObject(list);
        }

        void IDataMapper.Add(object obj)
        {
            auxMapper = new FireDataMapper(this.type, this.collection, this.FIRE_KEY, this.typeDataSource, this.path);
            Dictionary<string, object> toAdd = new Dictionary<string, object>();
            Type t = obj.GetType();

            if (t.Name != collection)                           // verifica s estamos a usar o tipo correto de mapper para o tipo de dominio q queremos adicionar
            {
                Console.WriteLine("MISMATCH TYPE ERROR");
                throw new Exception();
            }

            PropertyInfo[] properties = t.GetProperties();
            foreach (PropertyInfo property in properties)
            {
                if (IsFireKey(property))                        // verifica se é chave = anotação FireKey
                {
                    object prop = property.GetValue(obj, null);

                    if (auxMapper.GetById(prop) != null)        // verifica na source se já existe uma entrada com esta chave 
                    {
                        Console.WriteLine("Fire key already exists in FireStore!");
                        throw new Exception();
                    }
                }
                if (ShouldAdd(property))                        // verifica o atributo FireIgnore
                {
                    toAdd.Add(property.Name, property.GetValue(obj, null));
                }
                else
                {
                    // Add only FireKey Key/ Value
                    object domainObject = property.GetValue(obj, null);

                    PropertyInfo keyProp = GetFireKey(domainObject.GetType());
                    object keyValue = keyProp.GetValue(domainObject, null);

                    // If keyValue doesn't exist, throw exception
                    if (GetObjectFromForeignKey(domainObject.GetType(), keyValue) == null)
                    {
                        Console.WriteLine("Foreign Key doesn't exist...");
                        throw new Exception();
                    }
                    toAdd.Add(property.Name, keyValue);
                }
            }
            this.dataSource.Add(toAdd);
        }

        void IDataMapper.Update(object obj)
        {
            Dictionary<string, object> toUpdate = new Dictionary<string, object>();
            Type t = obj.GetType();

            PropertyInfo[] properties = t.GetProperties();
            foreach (PropertyInfo property in properties)
            {
                if (ShouldAdd(property))
                {
                    toUpdate.Add(property.Name, property.GetValue(obj, null));
                }
                else
                {
                    // Add only FireKey Key/ Value
                    object domainObject = property.GetValue(obj, null);

                    PropertyInfo keyProp = GetFireKey(domainObject.GetType());
                    object keyValue = keyProp.GetValue(domainObject, null);

                    // If keyValue doesn't exist, throw exception
                    if (GetObjectFromForeignKey(domainObject.GetType(), keyValue) == null)
                    {
                        Console.WriteLine("Foreign Key doesn't exist...");
                        throw new Exception();
                    }
                    toUpdate.Add(property.Name, keyValue);
                }
            }
            this.dataSource.Update(toUpdate);
        }

        void IDataMapper.Delete(object keyValue)
        {
            auxMapper = new FireDataMapper(this.type, this.collection, this.FIRE_KEY, this.typeDataSource, this.path);

            if (auxMapper.GetById(keyValue) == null)
            {
                Console.WriteLine("KeyValue does not correspond to any " + this.collection);
                throw new Exception();
            }

            this.dataSource.Delete(keyValue);
        }

        public object GetObject(Dictionary<String, Object> list)
        {
            // estrutura de dados da etapa 0.
            List<IPropertyDomain> type_props = new List<IPropertyDomain>();
            
            PropertyInfo[] properties = type.GetProperties();
            // método abstrato que gere dinamicamente implementações da estrutura de dados acima.
            type_props = IPropertyCreation(type, FIRE_KEY, typeDataSource, path);

            // obter o construtor do tipo de dominio
            ConstructorInfo[] ctor = type.GetConstructors();
            // indice 1, porque o indice 0 é o construtor sem parametros criado pelo tipo record.
            List<ParameterInfo> parameters = ctor[1].GetParameters().ToList();

            // inicializar valores dos parametros do ctor
            object[] test = new object[parameters.Count];
            for (int i = 0; i < parameters.Count; ++i)
            {
                test[i] = null;
            }

            // obter uma referência que depois de ter as suas propriedades com os valores corretos, será retornada.
            object target = ctor[1].Invoke(test);

            // ler as entradas do Dicionario recebido como parâmetro no método,
            // fazer a transformação+adição destas entradas nas propriedades do tipo correspondente
            foreach (KeyValuePair<string, object> entry in list)
            {
                foreach (ParameterInfo param in parameters)
                {
                    if (param.Name == entry.Key)
                    {
                        // garantir que todas as propriedades da estrutura de dados têm o seu valor correto adicionado
                        foreach (IPropertyDomain prop in type_props)
                        {
                            if (prop.getName() == entry.Key)
                                // o setValue terá a mesma função, 
                                // mas será executado de maneira diferente consoante o tipo de mapper e o tipo de propriedade com que estmamos a lidar.
                                prop.setValue(target, entry.Value);
                        }
                    }
                }
            }
            return target;
        }

        // método abstrato que será implemmentado de maneira diferente consoante o tipo de mapper (FireDataMapper ou DynamicFireMapper)
        protected abstract List<IPropertyDomain> IPropertyCreation(Type type, string key, string typeDataSource, string path);


        // retorna o nome da collection através do atributo FireCollection
        // verifica se o tipo de dominio contem a anotação FireCollection e se corresponde ao 'type' passado nos parâmetros.
        public string GetFireCollection(Type type)
        {
            var toReturn = type.GetCustomAttributes(typeof(FireCollectionAttribute), true).FirstOrDefault() as FireCollectionAttribute;
            if (toReturn == null)
            {
                Console.WriteLine("No FireCollection Annotation");
                throw new Exception();
            }
            else if (toReturn.Name != type.Name)
            {
                Console.WriteLine("Mismatch FireCollection Annotation");
                throw new Exception();
            }
            return toReturn.Name;
        }


        // retorna a FireKey do dominio dado (type) e lança exceção caso exista + que uma chave.
        PropertyInfo GetFireKey(Type type)
        {
            PropertyInfo[] props = type.GetProperties();
            int counter = 0;
            PropertyInfo ret = null;
            foreach (var prop in props)
            {
                if (Attribute.GetCustomAttribute(prop, typeof(FireKeyAttribute)) != null)
                {
                    ++counter;
                    if (counter > 1)
                    {
                        Console.WriteLine("There is more than one FireKey anotation");
                        throw new Exception();
                    }
                    ret = prop;
                }
            }
            if (ret != null)
                return ret;

            Console.WriteLine("There is no FireKey anotation");
            throw new NullReferenceException();
        }


        object GetObjectFromForeignKey(Type type, object value)
        {
            // Get property FireKey
            PropertyInfo foreignKeyProp = GetFireKey(type);

            IDataMapper fireMapper = new FireDataMapper(type, GetFireCollection(type), foreignKeyProp.Name, this.typeDataSource, this.path);
            return fireMapper.GetById(value);
        }


        // verifica o atributo FireIgnore.
        bool ShouldAdd(PropertyInfo property)
        {
            return Attribute.GetCustomAttribute(property, typeof(FireIgnoreAttribute)) != null ? false : true;
        }


        // verifica se é chave = anotação FireKey.
        bool IsFireKey(PropertyInfo property)
        {
            return Attribute.GetCustomAttribute(property, typeof(FireKeyAttribute)) != null ? true : false;
        }

    }
}