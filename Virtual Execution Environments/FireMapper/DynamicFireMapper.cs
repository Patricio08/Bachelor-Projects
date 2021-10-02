using System;
using System.Collections.Generic;
using System.Reflection;
using FireSource;

namespace FireMapper{
    public class DynamicFireMapper : AbstractFireDataMapper
    {
        public DynamicFireMapper(Type type, string collection, string key, string typeDataSource, string path) 
        : base(type, collection, key, typeDataSource, path)
        {
        }

        static DynamicIPropertyInstanceCreator dynamicIPropertyInstanceCreator = new DynamicIPropertyInstanceCreator();
        
        protected override List<IPropertyDomain> IPropertyCreation(Type type, string key, string typeDataSource, string path)
        {
            List<IPropertyDomain> toRet  = new List<IPropertyDomain>();

            IPropertyDomain aux;
            foreach (PropertyInfo prop in type.GetProperties())                 // fill toRet with IProperty via Creator
            {
                // obtenção dinâmica da propriedade
                aux = dynamicIPropertyInstanceCreator.CreateIPropertyFor(type, key, typeDataSource, path, prop);
                toRet.Add(aux);
            }
            // known issue: gravação de dll está a gerar um erro: 'Não é possivel alterar a assemblagem'
            //dynamicIPropertyInstanceCreator.SaveModule();

            return toRet;
        }
    }
}

