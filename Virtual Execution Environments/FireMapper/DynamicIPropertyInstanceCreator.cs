using System;
using System.Reflection;
using System.Reflection.Emit;
using FireMapper;

public class DynamicIPropertyInstanceCreator
{
    AssemblyName assemblyName = new AssemblyName("DynamicIProperty");
    private AssemblyBuilder assemblyBuilder;
    private ModuleBuilder moduleBuilder;
    // contador auxiliar para evitar repetição de nomes
    private int counterName = 0;

    public DynamicIPropertyInstanceCreator()
    {
        assemblyBuilder = AssemblyBuilder.DefineDynamicAssembly(
            assemblyName,
            AssemblyBuilderAccess.RunAndSave);

        // For a single-module assembly, the module name is usually
        // the assembly name plus an extension.
        moduleBuilder = assemblyBuilder.DefineDynamicModule(assemblyName.Name, assemblyName.Name + ".dll");        
    }

    public IPropertyDomain CreateIPropertyFor(Type type, string key, string dataSource, string path, PropertyInfo prop){
        Type t = BuildDynamicIPropertyFor(type, key, dataSource, path, prop);
        
        PropertyDomainBase propToRet = (PropertyDomainBase)Activator.CreateInstance(t, new object[]{ });

        return propToRet;
    }


    private Type BuildDynamicIPropertyFor(Type type, string key, string dataSource, string path, PropertyInfo prop)
    {
       string typeName = type.Name + prop.Name + "DynProp" + ++counterName;
       TypeBuilder typeBuilder = moduleBuilder.DefineType(typeName, TypeAttributes.Public, typeof(PropertyDomainBase));

       BuildConstrutor(typeBuilder, prop, type, key, dataSource, path);
       BuildSetValue(typeBuilder, prop, type, key, dataSource, path);
       BuildGetValue(typeBuilder, prop, type);

       return typeBuilder.CreateType();
    }

    public void SaveModule(){
        assemblyBuilder.Save(assemblyName.Name + ".dll");
    }

    private void BuildConstrutor(TypeBuilder typeBuilder, PropertyInfo prop, Type type, string key, string dataSource, string path)
    {
        Type [] parameterTypes = {};
        ConstructorBuilder constr = typeBuilder.DefineConstructor(MethodAttributes.Public, CallingConventions.Standard, parameterTypes);
        Type t = typeof(PropertyDomainBase);

        bool primitive = false;
        if(prop.PropertyType.IsPrimitive || prop.PropertyType == typeof(string))
            primitive = true;

        ConstructorInfo baseConstr; 
        if(primitive)
            baseConstr = t.GetConstructor(new Type[]{typeof(string)});
        else
            baseConstr = t.GetConstructor(new Type[]{typeof(Type), typeof(string), typeof(string), typeof(string), typeof(string)});

        if(path == null) path = "empty";
        var getTypeFromHandle = typeof(Type).GetMethod("GetTypeFromHandle", new Type[1] { typeof(RuntimeTypeHandle) });
       
        ILGenerator ctorIl = constr.GetILGenerator();            

        ctorIl.Emit(OpCodes.Ldarg_0);                                                   // ldarg.0
        // PRIMITIVE CONSTR :: UniversityYearGetter|UniversityNameGetter (dummies) 
        if(primitive){
            ctorIl.Emit(OpCodes.Ldstr, prop.Name);                                      // ldstr      "Name"
            ctorIl.Emit(OpCodes.Call, baseConstr);                                      // call       instance void [FireMapper]PropertyDomainBase::.ctor(string)
        }
        // COMPLEX CONSTR :: DepartmentUniversityGetter (dummy)
        else{
            ctorIl.Emit(OpCodes.Ldtoken, prop.PropertyType);                            // ldtoken    [FireMapper]University
            ctorIl.Emit(OpCodes.Call, getTypeFromHandle);                               // call       class [mscorlib]System.Type [mscorlib]System.Type::GetTypeFromHandle(valuetype [mscorlib]System.RuntimeTypeHandle)
            ctorIl.Emit(OpCodes.Ldstr, prop.Name);                                      // ldstr      "University"
            ctorIl.Emit(OpCodes.Ldstr, key);                                            // ldstr      "Name"
            ctorIl.Emit(OpCodes.Ldstr, dataSource);                                     // ldstr      "FireDataSource"
            if(dataSource == "WeakDataSource")  ctorIl.Emit(OpCodes.Ldstr, path);       // ldstr      "path"
            else ctorIl.Emit(OpCodes.Ldnull);                                           // ldnull
            ctorIl.Emit(OpCodes.Call, baseConstr);                                      // call       instance void [FireMapper]PropertyDomainBase::.ctor(class [mscorlib]System.Type, string, string, string, string)                                                        
        }
        ctorIl.Emit(OpCodes.Ret);                                                        // ret
    }

    private void BuildGetValue(TypeBuilder typeBuilder, PropertyInfo prop, Type domain)
    {
          MethodBuilder mb = typeBuilder.DefineMethod(
            "getValue", 
            MethodAttributes.Public | MethodAttributes.Virtual | MethodAttributes.HideBySig, 
            typeof(object), 
            new Type[] { typeof(object) }
        );

        var getNameMethod = prop.GetGetMethod();

        ILGenerator ilGen = mb.GetILGenerator();

        ilGen.Emit(OpCodes.Ldarg_1);                                                        // ldarg.1
        // Verificação tipos de valor -> structs
        if(domain.IsValueType){ 
            LocalBuilder loc = ilGen.DeclareLocal(domain);
            ilGen.Emit(OpCodes.Unbox_Any, domain);
            ilGen.Emit(OpCodes.Stloc_0);
            ilGen.Emit(OpCodes.Ldloca_S, loc);
            ilGen.Emit(OpCodes.Call, getNameMethod);
            if(prop.PropertyType.IsPrimitive){
                ilGen.Emit(OpCodes.Box, prop.PropertyType);
            }
        }
        else{
            ilGen.Emit(OpCodes.Castclass, domain);                                              // castclass  [FireMapper]Department
            ilGen.Emit(OpCodes.Callvirt, getNameMethod);                                        // callvirt   instance class [FireMapper]University [FireMapper]Department::get_University()
            if(prop.PropertyType.IsPrimitive){
                ilGen.Emit(OpCodes.Box, prop.PropertyType);                                     // box        [mscorlib]System.Int64
            }
        }
        ilGen.Emit(OpCodes.Ret);                                                                // ret
    }

    private void BuildSetValue(TypeBuilder typeBuilder, PropertyInfo prop, Type domain, string key, string dataSource, string path)
    {
        MethodBuilder mb = typeBuilder.DefineMethod(
            "setValue", 
            MethodAttributes.Public | MethodAttributes.Virtual | MethodAttributes.HideBySig, 
            typeof(void), 
            new Type[] { typeof(object), typeof(object) }
        );

        var toStringMethod = typeof(object).GetMethod("ToString");
        var setNameMethod = prop.GetSetMethod();
        var getTypeFromHandle = typeof(Type).GetMethod("GetTypeFromHandle", new Type[1] { typeof(RuntimeTypeHandle) });
        var convertChangeType = typeof(Convert).GetMethod("ChangeType", new Type[2] { typeof(object), typeof(Type)});
        var getById = typeof(IDataMapper).GetMethod("GetById");

        FieldInfo fldMapper = typeof(PropertyDomainBase).GetField("dynFireMapper");

        ILGenerator ilGen = mb.GetILGenerator();

        ilGen.Emit(OpCodes.Ldarg_1);                                            // ldarg.1
        ilGen.Emit(OpCodes.Castclass, domain);                                  // castclass  [FireMapper]University
        if(!prop.PropertyType.IsPrimitive && !(prop.PropertyType == typeof(string))){
            ////// COMPLEX SETVALUE :: DepartmentUniversityGetter (dummy)                  
            ilGen.Emit(OpCodes.Ldarg_0);                                        // ldarg.0
            ilGen.Emit(OpCodes.Ldfld, fldMapper);                               // ldfld      class [FireMapper]FireMapper.IDataMapper [FireMapper]PropertyDomainBase::dynFireMapper
            ilGen.Emit(OpCodes.Ldarg_2);                                        // ldarg.2
            ilGen.Emit(OpCodes.Callvirt, getById);                              // callvirt   instance object [FireMapper]FireMapper.IDataMapper::GetById(object)
            ilGen.Emit(OpCodes.Castclass, prop.PropertyType);                   // castclass  [FireMapper]University
        }
        else{                            
            ilGen.Emit(OpCodes.Ldarg_2);                                        // ldarg.2
            // até este ponto é comum Primitive & String
            if(!prop.PropertyType.IsPrimitive)  ////// object SETVALUE :: UniversityNameGetter (dummy)
                ilGen.Emit(OpCodes.Callvirt, toStringMethod);                   // callvirt   instance string [mscorlib]System.Object::ToString()
            else{                               ////// primitive SETVALUE :: UniversityYearGetter (dummy)
                ilGen.Emit(OpCodes.Ldtoken, prop.PropertyType);                 //  ldtoken    [mscorlib]System.Int64
                ilGen.Emit(OpCodes.Call, getTypeFromHandle);                    //  call       class [mscorlib]System.Type [mscorlib]System.Type::GetTypeFromHandle(valuetype [mscorlib]System.RuntimeTypeHandle)
                ilGen.Emit(OpCodes.Call, convertChangeType);                    //  call       object [mscorlib]System.Convert::ChangeType(object,class [mscorlib]System.Type)
                ilGen.Emit(OpCodes.Unbox_Any, prop.PropertyType);               //  unbox.any  [mscorlib]System.Int64
            }
        }     
        // COMUM EM TODOS
        ilGen.Emit(OpCodes.Callvirt, setNameMethod);                            // callvirt   instance void [FireMapper]Department::set_University(class [FireMapper]University)
        ilGen.Emit(OpCodes.Ret);                                                // ret
    }   
}