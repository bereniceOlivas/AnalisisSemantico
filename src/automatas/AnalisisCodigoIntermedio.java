package automatas;

import java.text.DecimalFormat;
import java.util.Scanner;

public class AnalisisCodigoIntermedio {
    static DecimalFormat df = new DecimalFormat("#.00");
    static Scanner Leer = new Scanner(System.in);
    static TablaDeResultadoCodigoIntermedio DatosNivel1[] = new TablaDeResultadoCodigoIntermedio[20];
    static String[] FuncionDivEstado;
    static TablaDeVariablesCodigoIntermedio ValorVariable[] = new TablaDeVariablesCodigoIntermedio[10];

    public static void Inicio(TablaDeSimbolos[] Datos, int ContadorGlobal) {
        System.out.println();
        System.out.print("\033[34mEscribe una Expresion a evaluar: ");

        /*
        Num1 * Num4 + ( Num2 - Num1 / Num5 ) / Num6 
        Num1 + Num2 - ( Num3 - Num4 / Num1 ) * Num6
        */
        
        
        
        String Funcion = Leer.nextLine();
        String[] FuncionDiv = Funcion.split(" ");
        System.out.println();
        FuncionDivEstado = new String[FuncionDiv.length];
        FuncionDivEstadoIniciacion(FuncionDivEstado);

        Inicializacion();
        InsercionVariablesValores(FuncionDiv, Datos, ContadorGlobal);
        InicializacionTriplos();

        //ImpresionTablaValores();
        //System.out.println();
        ImpresionFuncionDiv(FuncionDiv);
        System.out.println();
        ProcesoTriplos(FuncionDiv, DatosNivel1);
        ImpresionTriplos();
        System.out.println();
      
    }


    private static void ProcesoTriplos(String[] FuncionDiv, TablaDeResultadoCodigoIntermedio[] DatosNivel1) {
        String Identificador;
        int PrincipioParentesisN1 = 0, FinalParentesisN1 = 0, C;

        //Solo busca los parentesis nivel1
        for (int x = 0; x < FuncionDiv.length; x++) {
            Identificador = FuncionDiv[x];
            if (Identificador.equals("(")) {
                PrincipioParentesisN1 = x;
                C = x;
                for (int y = C; y < FuncionDiv.length; y++) {
                    Identificador = FuncionDiv[y];
                    if (Identificador.equals(")")) {
                        FinalParentesisN1 = y;
                    }
                    if (FinalParentesisN1 > 0) {
                        break;
                    }

                }
            }
        }

        //Buscara y la informacion que este en el parentesis
        BuscarInformacionParentesisN1(PrincipioParentesisN1, FinalParentesisN1, FuncionDiv);

        SiguenteOperador(PrincipioParentesisN1, FinalParentesisN1, FuncionDiv);

    }

    private static void SiguenteOperador(int PrincipioParentesisN1, int FinalParentesisN1, String[] FuncionDiv) {

        String Antes = FuncionDiv[PrincipioParentesisN1 - 1];
        String Despues = FuncionDiv[FinalParentesisN1 + 1];
        String Identificador;

        if (Despues.equals("*") || Despues.equals("/")) {
            for (int A = 0; A < 10; A++) {
                Identificador = DatosNivel1[A].getValor1();
                if (Identificador.equals("0")) {

                    DatosNivel1[A].setValor2(FuncionDiv[FinalParentesisN1 + 2]);
                    DatosNivel1[A].setValor1(FuncionDivEstado[FinalParentesisN1]);
                    DatosNivel1[A].setOperacion(FuncionDiv[FinalParentesisN1 + 1]);

                    for (int i = PrincipioParentesisN1; i <= FinalParentesisN1; i++) {
                        FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                    }

                    //Insercion de valor
                    String V1 = DatosNivel1[A].getValor1();
                    String V2 = FuncionDiv[FinalParentesisN1 + 2];
                    String Operacion = FuncionDiv[FinalParentesisN1 + 1];
                    String Temp = "";

                    double Valor1 = 0;
                    double Valor2 = 0;

                    for (int v1 = 0; v1 < 10; v1++) {
                        Temp = DatosNivel1[v1].getIdentificador();
                        if (Temp.equals(V1)) {
                            Valor1 = DatosNivel1[v1].getResultado();
                        }
                    }

                    for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                        Temp = ValorVariable[v1].getNombreaVariable();
                        if (Temp.equals(V2)) {
                            Valor2 = ValorVariable[v1].getValor();
                        }
                    }
                    Operacion(Valor1, Valor2, Operacion, A);

                    break;
                }
            }
            //A + B - ( C - D / A ) * F

            int Ubicacion = 3;
            for (int x = 0; x < PrincipioParentesisN1; x++) {
                Identificador = FuncionDiv[x];
                if (Identificador.equals("*") || Identificador.equals("/")) {
                    Ubicacion = x;
                }
            }

            String B = FuncionDivEstado[Ubicacion + 1];
            boolean notEqual = !B.equals("0");
            Identificador = FuncionDiv[1];
            if (notEqual == true) {

                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {

                        DatosNivel1[A].setValor1(FuncionDiv[Ubicacion - 1]);
                        DatosNivel1[A].setValor2(FuncionDivEstado[Ubicacion + 1]);
                        DatosNivel1[A].setOperacion(FuncionDiv[Ubicacion]);

                        for (int i = Ubicacion - 1; i <= FinalParentesisN1 + 2; i++) {
                            FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                        }

                        String V1 = FuncionDiv[Ubicacion - 1];
                        String V2 = DatosNivel1[A].getValor2();
                        String Operacion = FuncionDiv[Ubicacion];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosNivel1[v1].getResultado();
                            }
                        }

                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V1)) {
                                Valor1 = ValorVariable[v1].getValor();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);

                        break;
                    }
                }

                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {

                        DatosNivel1[A].setValor1(FuncionDiv[0]);
                        DatosNivel1[A].setValor2(FuncionDivEstado[2]);
                        DatosNivel1[A].setOperacion(FuncionDiv[1]);

                        for (int i = 0; i < FuncionDivEstado.length; i++) {
                            FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                        }
                        String V1 = FuncionDiv[0];
                        String V2 = DatosNivel1[A].getValor2();
                        String Operacion = FuncionDiv[1];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosNivel1[v1].getResultado();
                            }
                        }

                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V1)) {
                                Valor1 = ValorVariable[v1].getValor();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);

                        break;
                    }

                }

            } else if (Identificador.equals("*") || Identificador.equals("/")) {
                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosNivel1[A].setValor1(FuncionDiv[Ubicacion - 1]);
                        DatosNivel1[A].setValor2(FuncionDiv[Ubicacion + 1]);
                        DatosNivel1[A].setOperacion(FuncionDiv[Ubicacion]);

                        for (int i = Ubicacion - 1; i <= Ubicacion + 1; i++) {
                            FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                        }

                        String V1 = FuncionDiv[0];
                        String V2 = FuncionDiv[2];
                        String Operacion = FuncionDiv[1];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        /*for(int v1=0; v1 < 10; v1++){
							Temp = DatosNivel1[v1].getIdentificador();
							if(Temp.equals(V2))
							{
								Valor2=DatosNivel1[v1].getResultado();	
							}
						}*/
                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V2)) {
                                Valor2 = ValorVariable[v1].getValor();
                            }
                        }
                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V1)) {
                                Valor1 = ValorVariable[v1].getValor();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }

                }

                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosNivel1[A].setValor1(FuncionDivEstado[2]);
                        DatosNivel1[A].setValor2(FuncionDivEstado[4]);
                        DatosNivel1[A].setOperacion(FuncionDiv[3]);

                        for (int i = 0; i < FuncionDivEstado.length; i++) {
                            FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                        }
                        String V1 = DatosNivel1[A].getValor1();

                        String V2 = DatosNivel1[A].getValor2();

                        String Operacion = FuncionDiv[3];

                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V1)) {
                                Valor1 = DatosNivel1[v1].getResultado();
                            }
                        }
                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosNivel1[v1].getResultado();
                            }
                        }

                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }
                }

            } else {

            }

        } else if (Antes.equals("*") || Antes.equals("/")) {
            for (int A = 0; A < 10; A++) {
                Identificador = DatosNivel1[A].getValor1();
                if (Identificador.equals("0")) {

                    DatosNivel1[A].setValor1(FuncionDiv[PrincipioParentesisN1 - 2]);
                    DatosNivel1[A].setValor2(FuncionDivEstado[PrincipioParentesisN1]);
                    DatosNivel1[A].setOperacion(FuncionDiv[PrincipioParentesisN1 - 1]);

                    for (int i = PrincipioParentesisN1 - 2; i <= FinalParentesisN1; i++) {
                        FuncionDivEstado[i] = DatosNivel1[A].getIdentificador();
                    }
                    break;
                }
            }
        } else {

        }

    }

    private static void Operacion(double Valor1, double Valor2, String Operacion, int A) {

        //System.out.println("------"+Valor1+" ----- "+Valor2);
        double Resultado = 0;
        if (Operacion.equals("/")) {
            Resultado = Valor1 / Valor2;
            DatosNivel1[A].setResultado(Resultado);
        } else if (Operacion.equals("*")) {
            Resultado = Valor1 * Valor2;
            DatosNivel1[A].setResultado(Resultado);
        } else if (Operacion.equals("-")) {
            Resultado = Valor1 - Valor2;
            DatosNivel1[A].setResultado(Resultado);
        } else {
            Resultado = Valor1 + Valor2;
            DatosNivel1[A].setResultado(Resultado);
        }

    }

    public static void BuscarInformacionParentesisN1(int P, int F, String[] FuncionDiv) {
        int Operador = 100;
        int Operador2 = 100;
        String Identificador;
        for (int UbicacionOperador = P; UbicacionOperador <= F; UbicacionOperador++) {
            Identificador = FuncionDiv[UbicacionOperador];
            if (Identificador.equals("/") || Identificador.equals("*")) {
                Operador = UbicacionOperador;
            } else {
                if (Identificador.equals("-") || Identificador.equals("+")) {
                    Operador2 = UbicacionOperador;
                }
            }
        }

        if (Operador < 100) {
            int PosicionArg = 0;

            for (int A = 0; A < 10; A++) {
                Identificador = DatosNivel1[A].getValor1();
                if (Identificador.equals("0")) {
                    PosicionArg = A;
                    DatosNivel1[A].setValor1(FuncionDiv[Operador - 1]);
                    DatosNivel1[A].setValor2(FuncionDiv[Operador + 1]);
                    DatosNivel1[A].setOperacion(FuncionDiv[Operador]);

                    //Insercion de valor
                    String V1 = FuncionDiv[Operador - 1];
                    String V2 = FuncionDiv[Operador + 1];
                    String Operacion = FuncionDiv[Operador];
                    String Temp = "";

                    int Valor1 = 0;
                    int Valor2 = 0;
//				double Resultado=0;

                    for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                        Temp = ValorVariable[v1].getNombreaVariable();
                        if (Temp.equals(V1)) {
                            Valor1 = ValorVariable[v1].getValor();
                        }
                    }

                    for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                        Temp = ValorVariable[v1].getNombreaVariable();
                        if (Temp.equals(V2)) {
                            Valor2 = ValorVariable[v1].getValor();
                        }
                    }
                    Operacion(Valor1, Valor2, Operacion, A);
                    break;
                }
            }
            if ((Operador + 2) == F) {
                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosNivel1[A].setValor1(FuncionDiv[Operador - 3]);
                        DatosNivel1[A].setOperacion(FuncionDiv[Operador - 2]);
                        DatosNivel1[A].setValor2(DatosNivel1[PosicionArg].getIdentificador());

                        for (int u = P; u <= F; u++) {
                            FuncionDivEstado[u] = DatosNivel1[A].getIdentificador();
                        }

                        //Insercion de valor
                        String V1 = FuncionDiv[Operador - 3];
                        String Operacion = FuncionDiv[Operador - 2];
                        String V2 = DatosNivel1[PosicionArg].getIdentificador();
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;
//						double Resultado=0;

                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V1)) {
                                Valor1 = ValorVariable[v1].getValor();
                            }
                        }

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosNivel1[v1].getResultado();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }
                }
            } else {
                for (int A = 0; A < 10; A++) {

                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosNivel1[A].setValor2(FuncionDiv[Operador + 3]);
                        DatosNivel1[A].setOperacion(FuncionDiv[Operador + 2]);
                        DatosNivel1[A].setValor1(DatosNivel1[PosicionArg].getIdentificador());

                        for (int u = P; u <= F; u++) {
                            FuncionDivEstado[u] = DatosNivel1[A].getIdentificador();
                        }

                        //Insercion de Datos
                        String V2 = FuncionDiv[Operador + 3];
                        String Operacion = FuncionDiv[Operador + 2];
                        String V1 = DatosNivel1[PosicionArg].getIdentificador();
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;
//					double Resultado=0;

                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V2)) {
                                Valor2 = ValorVariable[v1].getValor();
                            }
                        }

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V1)) {
                                Valor1 = DatosNivel1[v1].getResultado();
                            }
                        }

                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }
                }
            }

        } else {
            int PosicionArg = 0;
            for (int A = 0; A < 10; A++) {
                Identificador = DatosNivel1[A].getValor1();
                if (Identificador.equals("0")) {
                    PosicionArg = A;
                    DatosNivel1[A].setValor1(FuncionDiv[Operador2 - 1]);
                    DatosNivel1[A].setValor2(FuncionDiv[Operador2 + 1]);
                    DatosNivel1[A].setOperacion(FuncionDiv[Operador2]);

                    //Insercion de Datos
                    String V1 = FuncionDiv[Operador2 - 1];
                    String Operacion = FuncionDiv[Operador2];
                    String V2 = FuncionDiv[Operador2 + 1];
                    String Temp = "";

                    double Valor1 = 0;
                    double Valor2 = 0;
//						double Resultado=0;

                    for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                        Temp = ValorVariable[v1].getNombreaVariable();
                        if (Temp.equals(V1)) {
                            Valor1 = ValorVariable[v1].getValor();
                        }
                    }

                    for (int v1 = 0; v1 < 10; v1++) {
                        Temp = ValorVariable[v1].getNombreaVariable();
                        if (Temp.equals(V2)) {
                            Valor2 = DatosNivel1[v1].getResultado();
                        }
                    }

                    Operacion(Valor1, Valor2, Operacion, A);
                    break;
                }
            }
            if ((Operador2 + 2) == F) {
                for (int A = 0; A < 10; A++) {
                    Identificador = DatosNivel1[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosNivel1[A].setValor1(FuncionDiv[Operador2 - 3]);
                        DatosNivel1[A].setOperacion(FuncionDiv[Operador2 - 2]);
                        DatosNivel1[A].setValor2(DatosNivel1[PosicionArg].getIdentificador());

                        //Insercion de valor
                        String V1 = FuncionDiv[Operador2 - 3];
                        String Operacion = FuncionDiv[Operador2 - 2];
                        String V2 = DatosNivel1[PosicionArg].getIdentificador();
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;
//						double Resultado=0;

                        for (int v1 = 0; v1 < ValorVariable.length; v1++) {
                            Temp = ValorVariable[v1].getNombreaVariable();
                            if (Temp.equals(V1)) {
                                Valor1 = ValorVariable[v1].getValor();
                            }
                        }

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosNivel1[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosNivel1[v1].getResultado();
                            }
                        }

                        Operacion(Valor1, Valor2, Operacion, A);

                        break;
                    }
                }
            }

        }

    }

    public static void Inicializacion() {
        for (int b = 0; b < ValorVariable.length; b++) {
            ValorVariable[b] = new TablaDeVariablesCodigoIntermedio("0", 0);
        }
    }

    public static void InicializacionTriplos() {

        DatosNivel1[0] = new TablaDeResultadoCodigoIntermedio("Arg1", "0", "0", "0", 0);
        DatosNivel1[1] = new TablaDeResultadoCodigoIntermedio("Arg2", "0", "0", "0", 0);
        DatosNivel1[2] = new TablaDeResultadoCodigoIntermedio("Arg3", "0", "0", "0", 0);
        DatosNivel1[3] = new TablaDeResultadoCodigoIntermedio("Arg4", "0", "0", "0", 0);
        DatosNivel1[4] = new TablaDeResultadoCodigoIntermedio("Arg5", "0", "0", "0", 0);
        DatosNivel1[5] = new TablaDeResultadoCodigoIntermedio("Arg6", "0", "0", "0", 0);
        DatosNivel1[6] = new TablaDeResultadoCodigoIntermedio("Arg7", "0", "0", "0", 0);
        DatosNivel1[7] = new TablaDeResultadoCodigoIntermedio("Arg8", "0", "0", "0", 0);
        DatosNivel1[8] = new TablaDeResultadoCodigoIntermedio("Arg9", "0", "0", "0", 0);
        DatosNivel1[9] = new TablaDeResultadoCodigoIntermedio("Arg0", "0", "0", "0", 0);

        /*for(int x=0; x < 10; x++)
			{
				System.out.println(DatosNivel1[x].getIdentificador());
			}*/
    }

    private static void InsercionVariablesValores(String[] FuncionDiv, TablaDeSimbolos[] Datos, int ContadorDatos) {
        String Valor;
        boolean A;
        for (int x = 0; x < FuncionDiv.length; x++) {
            Valor = FuncionDiv[x];
            if (Valor.matches("[A-Z]+.*")) {
                for (int a = 0; a < ValorVariable.length; a++) {
                    A = false;

                    for (int c = 0; c < ValorVariable.length; c++) {
                        if (ValorVariable[c].getNombreaVariable().equals(Valor)) {
                            A = true;
                        }
                    }

                    if (ValorVariable[a].getNombreaVariable().equals("0") && A == false) {
                        ValorVariable[a].setNombreaVariable(Valor);

                        for (int y = 0; y < ContadorDatos; y++) {
                            String DatoTabla = Datos[y].getNombre();

                            if (DatoTabla.equals(Valor)) {
                                int result = Integer.parseInt(Datos[y].getValor());
                                ValorVariable[a].setValor(result);
                            }
                        }

                        break;
                    }
                }

            }
        }

    }

    private static void ImpresionFuncionDiv(String[] FuncionDiv) {
        System.out.println("");
        System.out.println("Descomposicion de la Funcion");
        System.out.println();
        for (int x = 0; x < FuncionDiv.length; x++) {
            System.out.println(x + " = " + FuncionDiv[x]);
        }
        System.out.println();
    }

    public static void ImpresionTablaValores() {
        String Idenficicador = null;
        System.out.println();
        System.out.println("");
        System.out.println("------------->  Tabla de valores de la funcion    ");
        System.out.println();
        System.out.println("N Variable \t Valor");
        for (int b = 0; b < ValorVariable.length; b++) {
            Idenficicador = ValorVariable[b].getNombreaVariable();
            boolean notEqual = !Idenficicador.equals("0");
            if (notEqual == true) {
                System.out.println(" " + ValorVariable[b].getNombreaVariable() + "\t\t   " + ValorVariable[b].getValor());
            }
        }
    }

    public static void ImpresionTriplos() {
        
         
         
        System.out.println("\nTabla de resultado           ");
        String Idenficicador = null;
        System.out.println();
        System.out.println("Idetificador\tValor 1 \tOperador\tValor 2\t\tResultado ");
        for (int x = 0; x < 10; x++) {
            Idenficicador = DatosNivel1[x].getValor1();
            boolean notEqual = !Idenficicador.equals("0");

            if (notEqual == true) {
                System.out.println("   " + DatosNivel1[x].getIdentificador() + "\t\t " + DatosNivel1[x].getValor1() + "\t\t" + DatosNivel1[x].getOperacion() + "\t\t" + DatosNivel1[x].getValor2() + "\t\t" + df.format(DatosNivel1[x].getResultado()));
            }
        }

    }

    private static void ImpresionEstado(String[] FuncionDivEstado) {
        System.out.println("N\t Estado");
        for (int a = 0; a < FuncionDivEstado.length; a++) {
            System.out.println(a + "\t" + FuncionDivEstado[a]);
        }

    }

    private static void FuncionDivEstadoIniciacion(String[] FuncionDivEstado) {
        for (int a = 0; a < FuncionDivEstado.length; a++) {
            FuncionDivEstado[a] = "0";
        }
    }

}
