package automatas;

import java.util.Hashtable;
import java.lang.String;
import java.util.ArrayList;

class AnalisisSemantico {

    public static int segunda = 0;
    private static Hashtable tabla = new Hashtable();
    private static ArrayList<Integer> intComp = new ArrayList();
    private static ArrayList<Integer> decComp = new ArrayList();
    private static ArrayList<Integer> strComp = new ArrayList();
    private static ArrayList<Integer> chrComp = new ArrayList();

    static TablaDeSimbolos tablaDeSimbolos[] = new TablaDeSimbolos[100];
    static int limiteDeTabla = 0;
    static int NoInsertar = 1;

    static TablaDeErrores tablaDeErrores[] = new TablaDeErrores[100];
    static int indiceTablaErrores = 0;

    public static void TablaSimbolos() {

        System.out.println("Tabla de simbolos" + "\n");
        System.out.println("\u001B[42;30m|\tRol\t|\tIdentificador\t|\tTipo\t|\tValor\t|\tLinea\t|");
        for (int i = 0; i < limiteDeTabla; i++) {
            System.out.println("|" + tablaDeSimbolos[i].getrol() + "\t\t|" + tablaDeSimbolos[i].getNombre() + "\t\t\t|" + tablaDeSimbolos[i].getTipo() + "\t\t|" + tablaDeSimbolos[i].getValor() + "\t\t|" + tablaDeSimbolos[i].getposicion() + "\t\t|");
        }
    }

    public static void TablaDeErrores() {
        System.out.println("\nTabla de errores" + "\n");
        System.out.println("\u001B[43;30m|Num\t|Tipo de error\t|Variable\t|Linea del error\t|Comenario\t\t\t\t\t\t\t\t\t\t\t|");
        for (int i = 0; i < indiceTablaErrores; i++) {
            System.out.println("|" + tablaDeErrores[i].numero + "\t|" + tablaDeErrores[i].getTipoError() + "\t|" + tablaDeErrores[i].getVariable() + "\t\t|" + tablaDeErrores[i].getLinea() + "\t\t\t|" + tablaDeErrores[i].getComentario());
        }
    }

    public static void InsertarSimbolo(Token identificador, int tipo) {
        NoInsertar = 1;
        boolean Validacion = false;
        int Linea = identificador.beginLine;
        String Nombre = identificador.image;

        String TipoValor = " ";
        if (tipo == 44 || tipo == 48) {
            TipoValor = "Int";
        } else if (tipo == 45 || tipo == 50) {
            TipoValor = "Double";
        } else if (tipo == 46) {
            TipoValor = "Char";
        } else if (tipo == 47 || tipo == 51) {
            TipoValor = "String";
        } else {
            TipoValor = "Valor no idefinido";
        }
        
        //04 Historia de usuario : Validar las variables ya declaradas  
        for (int i = 0; i < limiteDeTabla; i++) {
            if (Nombre.equals(tablaDeSimbolos[i].getNombre())) {
                if (TipoValor.equals(tablaDeSimbolos[i].getTipo())) {
                    Validacion = true;
                    System.out.println("\u001B[31m------------------------------------------------------------------");
                    System.out.println("\u001B[31m Error " + (indiceTablaErrores + 1) + ": Error Semantico");
                    System.out.println("\u001B[31m - La variable: " + Nombre + ", ya exite, en la linea: " + tablaDeSimbolos[i].getposicion() + " con el valor: " + tablaDeSimbolos[i].getValor());
                    System.out.println("\u001B[31m------------------------------------------------------------------");

                    tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Semantico", Nombre, Linea, "La variable a sido declarada anteriormente");
                    indiceTablaErrores++;
                    NoInsertar = 0;
                }
            }
        }

        if (Validacion == false) {
            tablaDeSimbolos[limiteDeTabla] = new TablaDeSimbolos("Local", Nombre, TipoValor, "null", Linea);
            tabla.put(identificador.image, tipo);
            limiteDeTabla++;
        }
    }

    public static void SetTables() {
        intComp.add(44); //int
        intComp.add(48); //numeros 0-9

        decComp.add(44); //int
        decComp.add(45); //dec
        decComp.add(48); //numero 0-9
        decComp.add(50); //decimal

        chrComp.add(46); //char
        chrComp.add(52); //caracteres

        strComp.add(47); //string 
        strComp.add(51); //cadena
    }

    public static String checkAsing(Token v1, Token v2, int tipo) {
        //variables en las cuales se almacenara el tipo de dato del identificador y de las asignaciones (ejemplo: n1(tipoIdent1) = 2(tipoIdent2) + 3(tipoIdent2))
        int tipoIdent1;
        int tipoIdent2;

        String Nombre = v1.image;
        String Valor = v2.image;

        //en este if se verifica que la varibale tenga asignado un tipo de dato
        // System.out.println("\033[32mToken 1:"+v1.image+" , token 2: "+v2.image +", tipo: "+tipo);
        if (v1.kind != 48 && v1.kind != 50) {
            try {
                //Si el TokenIzq.image existe dentro de la tabla de tokens, entonces tipoIdent1 toma el tipo de dato con el que TokenIzq.image fue declarado
                tipoIdent1 = (Integer) tabla.get(v1.image);
            } catch (Exception e) {

                //Si TokenIzq.image no se encuentra en la tabla en la cual se agregan los tokens, el token no ha sido declarado, y se manda un error	
                tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Sintatico", Nombre, v1.beginLine, "No se pudo crear la variable ya que falto estitular el tipo de dato al que pertenece");
                indiceTablaErrores++;
                return "\u001B[31m------------------------------------------------------------------\n \u001B[31mError " + indiceTablaErrores + ": Error Sintatico \n \u001B[31m- La variable: " + v1.image + " no ha sido declarado \n\u001B[31m------------------------------------------------------------------";
            }
        } else {
            tipoIdent1 = 0;
        }

        //TokenAsig.kind != 48 && TokenAsig.kind != 50 && TokenAsig.kind != 51 && TokenAsig.kind != 52
        if (v2.kind == 49) {
            /*Si el tipo de dato que se esta asignando, es algun identificador(kind == 49) se obtiene su tipo de la tabla de tokens para poder hacer las comparaciones*/
            try {
                tipoIdent2 = (Integer) tabla.get(v2.image);
            } catch (Exception e) {
                //si el identificador no existe manda el error
                return "\t dddOcurrio un error Semantico \n\t  -> valor = " + v2.image + " No ha sido declarado correctamente \n\t  -> Linea: " + v1.beginLine;
            }
        } //Si el dato es entero(48) o decimal(50) o caracter(51) o cadena(52)
        //tipoIdent2 = tipo_del_dato
        else if (v2.kind == 48 || v2.kind == 50 || v2.kind == 51 || v2.kind == 52) {
            tipoIdent2 = v2.kind;
        } else //Si no, se inicializa en algun valor "sin significado(con respecto a los tokens)", para que la variable este inicializada y no marque error al comparar
        {
            tipoIdent2 = 0;
        }

        //Int
        if (tipoIdent1 == 44) {

            boolean Validacion = false;
            if (intComp.contains(tipoIdent2)) {
                if (NoInsertar == 1) {
                    for (int i = 0; i < limiteDeTabla; i++) {
                        if (Nombre.equals(tablaDeSimbolos[i].getNombre())) {
                            tablaDeSimbolos[i].setValor(Valor);
                        }
                    }
                }

                //System.out.println("Token 1:"+v1.image+" , token 2: "+v2.image +", tipo: "+tipo);
                for (int i = 0; i < limiteDeTabla; i++) {

                    String Valor2 = Valor;
                    if (Valor2.equals(tablaDeSimbolos[i].getNombre()) && "Int".equals(tablaDeSimbolos[i].getTipo())) {

                        for (int f = 0; f < limiteDeTabla; f++) {

                            if (Nombre.equals(tablaDeSimbolos[f].getNombre())) {
                                tablaDeSimbolos[f].setValor(tablaDeSimbolos[i].getValor());
                            }
                        }
                    }
                }
                return " ";

            } else //Si el tipo de dato no es compatible manda el error
            {
                tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Semantico", Nombre, v1.beginLine, "No se puede aignar un valor de tipo cadena a una variable de tipo Int");
                indiceTablaErrores++;
                return "\u001B[31m------------------------------------------------------------------\n \u001B[31mError " + (indiceTablaErrores) + ": Error Semantico \n \u001B[31m- Datos incompatibles, la varible: " + v1.image + " no puede asignar el valor " + Valor + "\n\u001B[31m------------------------------------------------------------------";

            }
        } else if (tipoIdent1 == 45) //double
        {
            if (decComp.contains(tipoIdent2)) {

                for (int i = 0; i < limiteDeTabla; i++) {
                    if (Nombre.equals(tablaDeSimbolos[i].getNombre())) {
                        tablaDeSimbolos[i].setValor(Valor);
                    }
                }

                return " ";
            } else {
                tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Semantico", Nombre, v1.beginLine, "No se puede aignar un valor de tipo cadena a una variable de tipo Int");
                indiceTablaErrores++;
                return "\u001B[31m------------------------------------------------------------------\n \u001B[31mError " + (indiceTablaErrores) + ": Error Semantico \n \u001B[31m- Datos incompatibles, la varible: " + v1.image + " no puede tomar el valor " + Valor + "\n\u001B[31m------------------------------------------------------------------";

            }
        } else if (tipoIdent1 == 46) //char
        {
            /*variable segunda: cuenta cuantos datos se van a asignar al caracter: si a el caracter se le asigna mas de un dato (ej: 'a' + 'b') marca error 
            NOTA: no se utiliza un booleano ya que entraria en asignaciones pares o impares*/

            if (Valor.length() < 4) {
                if (chrComp.contains(tipoIdent2)) {
                    for (int i = 0; i < limiteDeTabla; i++) {
                        if (Nombre.equals(tablaDeSimbolos[i].getNombre())) {
                            tablaDeSimbolos[i].setValor(Valor);
                        }
                    }
                    return " ";
                } else {
                    return "\t Ocurrio un error Semantico \n\t  -> No se puede el valor : " + v2.image + " a Char \n\t  -> Linea: " + v1.beginLine;
                }
            } else //Si se esta asignando mas de un caracter manda el error 			
            {
                tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Semantico", Nombre, v1.beginLine, "Una variable de tipo char no puede tener mas un caracter o ser de tipo entero");
                indiceTablaErrores++;
                return "\u001B[31m------------------------------------------------------------------\n \u001B[31mError " + (indiceTablaErrores) + ": Error Semantico \n \u001B[31m- Datos incompatibles, la varible: " + v1.image + " es de tipo char, por lo que no puede contener mas de 1 caracter" + "\n\u001B[31m------------------------------------------------------------------";
            }

        } else if (tipoIdent1 == 47) //string
        {
            if (strComp.contains(tipoIdent2)) {

                for (int i = 0; i < limiteDeTabla; i++) {
                    if (Nombre.equals(tablaDeSimbolos[i].getNombre())) {
                        tablaDeSimbolos[i].setValor(Valor);
                    }
                }
                for (int i = 0; i < limiteDeTabla; i++) {

                    String Valor2 = Valor;
                    if (Valor2.equals(tablaDeSimbolos[i].getNombre()) && "String".equals(tablaDeSimbolos[i].getTipo())) {

                        for (int f = 0; f < limiteDeTabla; f++) {

                            if (Nombre.equals(tablaDeSimbolos[f].getNombre())) {
                                tablaDeSimbolos[f].setValor(tablaDeSimbolos[i].getValor());
                            }
                        }
                    }
                }
                return " ";
            } else {
                tablaDeErrores[indiceTablaErrores] = new TablaDeErrores(indiceTablaErrores + 1, "Semantico", Nombre, v1.beginLine, "Una variable de tipo cadena (String) no puede aceptar una varible entera (Int) u decimal");
                indiceTablaErrores++;
                return "\u001B[31m------------------------------------------------------------------\n \u001B[31mError " + (indiceTablaErrores) + ": Error Semantico \n \u001B[31m- Datos incompatibles, la varible: " + v1.image + " es de tipo cadena por lo que no puede aceptar una variable de tipo Int " + "\n\u001B[31m------------------------------------------------------------------";
            }
        } else {
            return "El Identificador " + v1.image + " no ha sido declarado" + " Linea: " + v1.beginLine;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    /*Metodo que verifica si un identificador ha sido declarado, 
		ej cuando se declaran las asignaciones: i++, i--)*/
    public static String checkVariable(Token checkTok) {
        try {
            //Intenta obtener el token a verificar(checkTok) de la tabla de los tokens
            int tipoIdent1 = (Integer) tabla.get(checkTok.image);
            return " ";
        } catch (Exception e) {
            //Si no lo puede obtener, manda el error
            return "Error: El identificador " + checkTok.image + " No ha sido declarado \r\nLinea: " + checkTok.beginLine;
        }
    }
    
    public static void CodigoIntermedio()
		{
			AnalisisCodigoIntermedio.Inicio(tablaDeSimbolos, limiteDeTabla);
		}
}
