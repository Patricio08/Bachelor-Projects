using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using FireSource;


namespace FireMapper
{
    public class FireDataMapper : AbstractFireDataMapper
    {
        Type type;
        string typeDataSource, path;

        public FireDataMapper(Type type,
            string collection,
            string key,
            string typeDataSource,
            string path) : base(type, collection, key, typeDataSource, path) { 
                this.type = type;
                this.typeDataSource = typeDataSource;
                this.path = path;
            }

        protected override List<IPropertyDomain> IPropertyCreation(Type type, string key, string typeDataSource, string path)
        {
            List<IPropertyDomain> toRet  = new List<IPropertyDomain>();

            foreach (PropertyInfo prop in type.GetProperties())
            {
                if (prop.PropertyType.IsPrimitive || prop.PropertyType == typeof(string))
                    toRet.Add(new PropertySimpleDomain(prop));
                else                            // é classe de dominio           
                    toRet.Add(new PropertyComplexDomain(prop, typeDataSource,path));
            }

            return toRet;
        }
    }
}