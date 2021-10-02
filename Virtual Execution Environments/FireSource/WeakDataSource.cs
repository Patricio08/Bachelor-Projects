using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using Google.Cloud.Firestore;

namespace FireSource
{
    public record WeakDataSource(
        string Key,
        string Collection,
        string FilePath) : IDataSource
    {
        public IEnumerable<Dictionary<string, object>> GetAll()
        {
            List<Dictionary<string, object>> toReturn = new List<Dictionary<string, object>>();

            using (StreamReader sr = new StreamReader(FilePath))
            {
                string ln;
                while ((ln = sr.ReadLine()) != null)
                {
                    Dictionary<string, object> aux_each = new Dictionary<string, object>();
                    if (ln == "{")
                    {
                        if (sr.ReadLine().Split(new string[]{"Type: "}, StringSplitOptions.None)[1] == this.Collection)
                        {               // inicio da descrição de um objeto
                            while ((ln = sr.ReadLine()) != "}")
                            {                                 // enquanto não acabar
                                string[] aux = ln.Split(new string[]{": "}, StringSplitOptions.None);
                                aux_each.Add(aux[0], aux[1]);
                            }

                            toReturn.Add(aux_each);
                        }
                    }
                }
                sr.Close();
            }
            return toReturn;
        }

        public Dictionary<string, object> GetById(object KeyValue)
        {
            Dictionary<string, object> toReturn = new Dictionary<string, object>();

            using (StreamReader sr = new StreamReader(this.FilePath))
            {
                string ln;

                while ((ln = sr.ReadLine()) != null)
                {
                    if (ln == "{")
                    {
                        ln = sr.ReadLine();
                        if (ln.Split(new string[]{"Type: "}, StringSplitOptions.None)[1] == this.Collection)
                        {               
                            ln = sr.ReadLine();
                            string[] aux = ln.Split(new string[]{": "}, StringSplitOptions.None);
                            if (aux[0] == this.Key)
                            {                        // confirmar a chave
                                if (aux[1] == KeyValue.ToString())
                                {                    // encontrar o KeyValue pedido
                                    toReturn.Add(aux[0], aux[1]);
                                    while ((ln = sr.ReadLine()) != "}")
                                    {               // completa o objeto
                                        aux = ln.Split(new string[]{": "}, StringSplitOptions.None);
                                        toReturn.Add(aux[0], aux[1]);                  // adicionar os restantes campos 
                                    }
                                }
                            }
                        }
                    }
                }
                sr.Close();
            }

            if (toReturn.Count < 1)
                return null;
            else
                return toReturn;

        }

        public void Add(Dictionary<string, object> obj)
        {
            if (obj != null)
            {
                using (StreamWriter writer = File.AppendText(this.FilePath))
                {
                    StringBuilder str = new StringBuilder();
                    str.Append('\n');
                    str.Append("{\n");
                    str.Append("Type: " + this.Collection + '\n');
                    foreach (KeyValuePair<string, object> toAdd in obj)
                    {
                        str.Append(toAdd.Key + ": " + toAdd.Value + '\n');
                    }
                    str.Append("}");
                    writer.WriteLine(str);
                }
            }
            else
            {
                Console.WriteLine("INVALID OBJECT!!!");
                throw new Exception();
            }
        }

        public void Update(Dictionary<string, object> obj)
        {
            string tempFile = Path.GetTempFileName();

            object aux = null;

            StringBuilder str_toUpdate = new StringBuilder();
            str_toUpdate.Append("\n{\n");
            str_toUpdate.Append("Type: " + this.Collection + '\n');
            foreach (KeyValuePair<string, object> toUpdate in obj)
            {
                str_toUpdate.Append(toUpdate.Key + ": " + toUpdate.Value + '\n');
                if (toUpdate.Key == this.Key)
                {
                    aux = toUpdate.Value;
                }
            }
            str_toUpdate.Append("}");

            using (StreamReader sr = new StreamReader(this.FilePath))
            using (StreamWriter sw = new StreamWriter(tempFile))
            {
                string ln;
                while ((ln = sr.ReadLine()) != null)
                {
                    StringBuilder str = new StringBuilder();
                    if (ln == "{")
                    {
                        str.Append("\n{\n");
                        ln = sr.ReadLine();
                        str.Append(ln + '\n');
                        if ((ln.Split(new string[]{"Type: "}, StringSplitOptions.None)[1] == this.Collection))
                        {
                            ln = sr.ReadLine();
                            if (ln.Split(new string[]{": "}, StringSplitOptions.None)[0] == this.Key && ln.Split(new string[]{": "}, StringSplitOptions.None)[1] == aux.ToString())
                            {
                                str.Clear();
                                str.Append(str_toUpdate);
                            }
                            else
                            {
                                str.Append(ln + '\n');
                                while ((ln = sr.ReadLine()) != "}")
                                {
                                    str.Append(ln + '\n');
                                }
                                str.Append("}");
                            }
                            sw.WriteLine(str);
                            str.Clear();
                        }
                        else
                        {
                            while ((ln = sr.ReadLine()) != "}")
                            {
                                str.Append(ln + '\n');
                            }
                            str.Append("}");
                            sw.WriteLine(str);
                            str.Clear();
                        }
                    }
                }
                sr.Close();
                sw.Close();

                File.Delete(this.FilePath);
                File.Move(tempFile, this.FilePath);
            }
        }

        public void Delete(object KeyValue)
        {
            if (KeyValue == null) {
                throw new NullReferenceException();
            }
            iterateAndChangeFile(KeyValue.ToString(), null);
        }

        void iterateAndChangeFile(string obj, string strToAdd)
        {
            string tempFile = Path.GetTempFileName();

            using (StreamReader sr = new StreamReader(this.FilePath))
            using (StreamWriter sw = new StreamWriter(tempFile))
            {
                string ln;
                while ((ln = sr.ReadLine()) != null)
                {
                    StringBuilder str = new StringBuilder();
                    if (ln == "{")
                    {
                        str.Append("\n{\n");
                        ln = sr.ReadLine();
                        str.Append(ln + '\n');
                        if ((ln.Split(new string[]{"Type: "}, StringSplitOptions.None)[1] == this.Collection))
                        {
                            ln = sr.ReadLine();
                            if (ln.Split(new string[]{": "}, StringSplitOptions.None)[0] == this.Key && ln.Split(new string[]{": "}, StringSplitOptions.None)[1] == obj)
                            {
                                //Take action
                                if (strToAdd != null) {
                                    toUpdate(str, strToAdd);
                                } else {
                                    toDelete(str);
                                }
                            }
                            else
                            {
                                str.Append(ln + '\n');
                                while ((ln = sr.ReadLine()) != "}")
                                {
                                    str.Append(ln + '\n');
                                }
                                str.Append("}");
                            }
                            sw.WriteLine(str);
                            str.Clear();
                        }
                        else
                        {
                            while ((ln = sr.ReadLine()) != "}")
                            {
                                str.Append(ln + '\n');
                            }
                            str.Append("}");
                            sw.WriteLine(str);
                            str.Clear();
                        }
                    }
                }
                sr.Close();
                sw.Close();

                File.Delete(this.FilePath);
                File.Move(tempFile, this.FilePath);
            }
        }

        void toDelete(StringBuilder str) {
            str.Clear();
        }
        void toUpdate(StringBuilder str, string strToAdd) {
            str.Clear();
            str.Append(strToAdd);
        }
    }
}    