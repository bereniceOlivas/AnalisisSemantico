package automatas;

import java.text.DecimalFormat;
import java.util.Scanner;

public class AnalisisCodigoIntermedio {

    static DecimalFormat df = new DecimalFormat("#.00");
    static Scanner Leer = new Scanner(System.in);
    static TablaDeResultadoCodigoIntermedio DatosDeTablaDeSimboloCodigoIntermedio[] = new TablaDeResultadoCodigoIntermedio[20];
    static String[] FuncionDivEstado;
    static TablaDeVariablesCodigoIntermedio ValorVariable[] = new TablaDeVariablesCodigoIntermedio[10];

    public static void Inicio(TablaDeSimbolos[] Datos, int ContadorGlobal) {
        System.out.println();
        System.out.print("\033[34mEscribe una Expresion a evaluar: ");
        /*
        A = Num1 * Num4 + ( Num2 - Num1 / Num5 ) / Num6
        A = Num1 * Num4 + ( Num2 * Num1 - Num5 ) / Num6
        A = Num1 * Num4 + ( Num2 * Num1 / Num5 ) / Num6 
        
        A = Num1 * Num4 + ( Num2 + Num1 - Num5 ) / Num6 
        Num1 + Num2 - ( Num3 - Num4 / Num1 ) * Num6
         */

        String expresion = Leer.nextLine();
        String[] expresionPrincipal = expresion.split(" ");

        InicializacionValorVariable();
        InsercionVariablesValores(expresionPrincipal, Datos, ContadorGlobal);
        ImpresionTablaValores();

        String[] expresionEvaluada = new String[expresionPrincipal.length - 2];
        String variableResultado = busquedaVariableResultado(expresionEvaluada, expresionPrincipal);
        FuncionDivEstado = new String[expresionEvaluada.length];
        FuncionDivEstadoIniciacion(FuncionDivEstado);
        ImpresionFuncionDiv(expresionEvaluada);

        InicializacionTriplos();
        ProcesoTriplos(expresionEvaluada, DatosDeTablaDeSimboloCodigoIntermedio);

        int resultadoFinal = 0;
        resultadoFinal = ImpresionTriplos(resultadoFinal);
        System.out.println("\nEl resultado es " + variableResultado + " = " + (DatosDeTablaDeSimboloCodigoIntermedio[resultadoFinal].getIdentificador()) + " (" + df.format(DatosDeTablaDeSimboloCodigoIntermedio[resultadoFinal].getResultado()) + ")");
//       System.out.println("\nEl resultado es "+variableResultado+" = "+);

//        FuncionDivEstadoI(FuncionDivEstado);
    }

    private static void ProcesoTriplos(String[] expresionEvaluada, TablaDeResultadoCodigoIntermedio[] DatosDeTablaDeSimboloCodigoIntermedio) {
        String Identificador;
        int PrincipioParentesisN1 = 0;
        int FinalParentesisN1 = 0;

        //Solo busca los parentesis nivel1
        for (int x = 0; x < expresionEvaluada.length; x++) {
            Identificador = expresionEvaluada[x];
            if (Identificador.equals("(")) {
                PrincipioParentesisN1 = x;
//                C = x;
                for (int y = x; y < expresionEvaluada.length; y++) {
                    Identificador = expresionEvaluada[y];
                    if (Identificador.equals(")")) {
                        FinalParentesisN1 = y;
                        break;
                    }
                }
            }
        }

        //Buscara y la informacion que este en el parentesis
        BuscarInformacionParentesisN1(PrincipioParentesisN1, FinalParentesisN1, expresionEvaluada);
        SiguenteOperador(PrincipioParentesisN1, FinalParentesisN1, expresionEvaluada);

    }

    private static void SiguenteOperador(int PrincipioParentesisN1, int FinalParentesisN1, String[] expresionEvaluada) {

        String Antes = expresionEvaluada[PrincipioParentesisN1 - 1];
        String Despues = expresionEvaluada[FinalParentesisN1 + 1];
        String Identificador;

        if (Despues.equals("*") || Despues.equals("/")) {
            for (int A = 0; A < 10; A++) {
                Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                if (Identificador.equals("0")) {

                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(expresionEvaluada[FinalParentesisN1 + 2]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(FuncionDivEstado[FinalParentesisN1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[FinalParentesisN1 + 1]);

                    for (int i = PrincipioParentesisN1; i <= FinalParentesisN1; i++) {
                        FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                    }

                    //Insercion de valor
                    String V1 = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    String V2 = expresionEvaluada[FinalParentesisN1 + 2];
                    String Operacion = expresionEvaluada[FinalParentesisN1 + 1];
                    String Temp = "";

                    double Valor1 = 0;
                    double Valor2 = 0;

                    for (int v1 = 0; v1 < 10; v1++) {
                        Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                        if (Temp.equals(V1)) {
                            Valor1 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
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
                Identificador = expresionEvaluada[x];
                if (Identificador.equals("*") || Identificador.equals("/")) {
                    Ubicacion = x;
                }
            }

            String B = FuncionDivEstado[Ubicacion + 1];
            boolean notEqual = !B.equals("0");
            Identificador = expresionEvaluada[1];
            if (notEqual == true) {

                for (int A = 0; A < 10; A++) {
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {

                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Ubicacion - 1]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(FuncionDivEstado[Ubicacion + 1]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Ubicacion]);

                        for (int i = Ubicacion - 1; i <= FinalParentesisN1 + 2; i++) {
                            FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }

                        String V1 = expresionEvaluada[Ubicacion - 1];
                        String V2 = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor2();
                        String Operacion = expresionEvaluada[Ubicacion];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
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
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {

                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[0]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(FuncionDivEstado[2]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[1]);

                        for (int i = 0; i < FuncionDivEstado.length; i++) {
                            FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }
                        String V1 = expresionEvaluada[0];
                        String V2 = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor2();
                        String Operacion = expresionEvaluada[1];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
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
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Ubicacion - 1]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(expresionEvaluada[Ubicacion + 1]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Ubicacion]);

                        for (int i = Ubicacion - 1; i <= Ubicacion + 1; i++) {
                            FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }

                        String V1 = expresionEvaluada[0];
                        String V2 = expresionEvaluada[2];
                        String Operacion = expresionEvaluada[1];
                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        /*for(int v1=0; v1 < 10; v1++){
							Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
							if(Temp.equals(V2))
							{
								Valor2=DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();	
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
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(FuncionDivEstado[2]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(FuncionDivEstado[4]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[3]);

                        for (int i = 0; i < FuncionDivEstado.length; i++) {
                            FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }
                        String V1 = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();

                        String V2 = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor2();

                        String Operacion = expresionEvaluada[3];

                        String Temp = "";

                        double Valor1 = 0;
                        double Valor2 = 0;

                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V1)) {
                                Valor1 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
                            }
                        }
                        for (int v1 = 0; v1 < 10; v1++) {
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
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
                Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                if (Identificador.equals("0")) {

                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[PrincipioParentesisN1 - 2]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(FuncionDivEstado[PrincipioParentesisN1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[PrincipioParentesisN1 - 1]);

                    for (int i = PrincipioParentesisN1 - 2; i <= FinalParentesisN1; i++) {
                        FuncionDivEstado[i] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
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
            DatosDeTablaDeSimboloCodigoIntermedio[A].setResultado(Resultado);
        } else if (Operacion.equals("*")) {
            Resultado = Valor1 * Valor2;
            DatosDeTablaDeSimboloCodigoIntermedio[A].setResultado(Resultado);
        } else if (Operacion.equals("-")) {
            Resultado = Valor1 - Valor2;
            DatosDeTablaDeSimboloCodigoIntermedio[A].setResultado(Resultado);
        } else {
            Resultado = Valor1 + Valor2;
            DatosDeTablaDeSimboloCodigoIntermedio[A].setResultado(Resultado);
        }

    }

    public static void BuscarInformacionParentesisN1(int P, int F, String[] expresionEvaluada) {
        int Operador = 100;
        int Operador2 = 100;
        String Identificador;
        for (int UbicacionOperador = P; UbicacionOperador <= F; UbicacionOperador++) {
            Identificador = expresionEvaluada[UbicacionOperador];
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
                Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                if (Identificador.equals("0")) {
                    PosicionArg = A;
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Operador - 1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(expresionEvaluada[Operador + 1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Operador]);

                    //Insercion de valor
                    String V1 = expresionEvaluada[Operador - 1];
                    String V2 = expresionEvaluada[Operador + 1];
                    String Operacion = expresionEvaluada[Operador];
                    String Temp = "";

                    int Valor1 = 0;
                    int Valor2 = 0;
//				double Resultado=0;

                    for (int x = 0; x < ValorVariable.length; x++) {
                        Temp = ValorVariable[x].getNombreaVariable();
                        if (Temp.equals(V1)) {
                            Valor1 = ValorVariable[x].getValor();
                        }

                        if (Temp.equals(V2)) {
                            Valor2 = ValorVariable[x].getValor();
                        }
                    }
                    Operacion(Valor1, Valor2, Operacion, A);
                    break;
                }
            }
            if ((Operador + 2) == F) {
                for (int A = 0; A < 10; A++) {
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Operador - 3]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Operador - 2]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador());

                        for (int u = P; u <= F; u++) {
                            FuncionDivEstado[u] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }

                        //Insercion de valor
                        String V1 = expresionEvaluada[Operador - 3];
                        String Operacion = expresionEvaluada[Operador - 2];
                        String V2 = DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador();
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
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }
                }
            } else {
                for (int A = 0; A < 10; A++) {

                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(expresionEvaluada[Operador + 3]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Operador + 2]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador());

                        for (int u = P; u <= F; u++) {
                            FuncionDivEstado[u] = DatosDeTablaDeSimboloCodigoIntermedio[A].getIdentificador();
                        }

                        //Insercion de Datos
                        String V2 = expresionEvaluada[Operador + 3];
                        String Operacion = expresionEvaluada[Operador + 2];
                        String V1 = DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador();
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
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V1)) {
                                Valor1 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
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
                Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                if (Identificador.equals("0")) {
                    PosicionArg = A;
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Operador2 - 1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(expresionEvaluada[Operador2 + 1]);
                    DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Operador2]);

                    //Insercion de Datos
                    String V1 = expresionEvaluada[Operador2 - 1];
                    String Operacion = expresionEvaluada[Operador2];
                    String V2 = expresionEvaluada[Operador2 + 1];
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
                            Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
                        }
                    }

                    Operacion(Valor1, Valor2, Operacion, A);
                    break;
                }
            }
            if ((Operador2 + 2) == F) {
                for (int A = 0; A < 10; A++) {
                    Identificador = DatosDeTablaDeSimboloCodigoIntermedio[A].getValor1();
                    if (Identificador.equals("0")) {
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor1(expresionEvaluada[Operador2 - 3]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setOperacion(expresionEvaluada[Operador2 - 2]);
                        DatosDeTablaDeSimboloCodigoIntermedio[A].setValor2(DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador());

                        //Insercion de valor
                        String V1 = expresionEvaluada[Operador2 - 3];
                        String Operacion = expresionEvaluada[Operador2 - 2];
                        String V2 = DatosDeTablaDeSimboloCodigoIntermedio[PosicionArg].getIdentificador();
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
                            Temp = DatosDeTablaDeSimboloCodigoIntermedio[v1].getIdentificador();
                            if (Temp.equals(V2)) {
                                Valor2 = DatosDeTablaDeSimboloCodigoIntermedio[v1].getResultado();
                            }
                        }
                        Operacion(Valor1, Valor2, Operacion, A);
                        break;
                    }
                }
            }

        }

    }

    public static void InicializacionValorVariable() {
        for (int b = 0; b < ValorVariable.length; b++) {
            ValorVariable[b] = new TablaDeVariablesCodigoIntermedio("0", 0);
        }
    }

    public static void InicializacionTriplos() {
        DatosDeTablaDeSimboloCodigoIntermedio[0] = new TablaDeResultadoCodigoIntermedio("Resul1", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[1] = new TablaDeResultadoCodigoIntermedio("Resul2", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[2] = new TablaDeResultadoCodigoIntermedio("Resul3", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[3] = new TablaDeResultadoCodigoIntermedio("Resul4", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[4] = new TablaDeResultadoCodigoIntermedio("Resul5", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[5] = new TablaDeResultadoCodigoIntermedio("Resul6", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[6] = new TablaDeResultadoCodigoIntermedio("Resul7", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[7] = new TablaDeResultadoCodigoIntermedio("Resul8", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[8] = new TablaDeResultadoCodigoIntermedio("Resul9", "0", "0", "0", 0);
        DatosDeTablaDeSimboloCodigoIntermedio[9] = new TablaDeResultadoCodigoIntermedio("Resul0", "0", "0", "0", 0);
    }

    private static void InsercionVariablesValores(String[] expresionEvaluada, TablaDeSimbolos[] Datos, int ContadorDatos) {
        String Valor;
        boolean A;
        for (int x = 0; x < expresionEvaluada.length; x++) {
            Valor = expresionEvaluada[x];
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

    private static void ImpresionFuncionDiv(String[] expresionEvaluada) {
        System.out.println("");
        System.out.println("Descomposicion de la expresion");
        System.out.println();
        for (int x = 0; x < expresionEvaluada.length; x++) {
            System.out.println(x + 1 + " -> " + expresionEvaluada[x]);
        }
        System.out.println();
    }

    public static void ImpresionTablaValores() {
        String Idenficicador = null;
        System.out.println();
        System.out.println("");
        System.out.println("------------->  Tabla de valores de la expresion    ");
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

    public static int ImpresionTriplos(int resultadoFinal) {

        System.out.println("\nTabla de resultado           ");
        String Idenficicador = null;
        System.out.println();
        System.out.println("\tIdetificador\t \tArg 1\t\tOperador\tArg 2\t\tResultado ");
        for (int x = 0; x < 10; x++) {
            Idenficicador = DatosDeTablaDeSimboloCodigoIntermedio[x].getValor1();
            boolean notEqual = !Idenficicador.equals("0");

            if (notEqual == true) {
                System.out.println("Paso" + (x + 1) + "\t   " + DatosDeTablaDeSimboloCodigoIntermedio[x].getIdentificador() + "\t=\t " + DatosDeTablaDeSimboloCodigoIntermedio[x].getValor1() + "\t\t" + DatosDeTablaDeSimboloCodigoIntermedio[x].getOperacion() + "\t\t" + DatosDeTablaDeSimboloCodigoIntermedio[x].getValor2() + "\t\t" + df.format(DatosDeTablaDeSimboloCodigoIntermedio[x].getResultado()));
                resultadoFinal = x;
            }
        }
        return resultadoFinal;

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

    private static String busquedaVariableResultado(String[] expresionEvaluada, String[] expresionPrincipal) {
        String variableResultado = "Sin valor";
        variableResultado = expresionPrincipal[0];
        for (int x = 0; x < expresionEvaluada.length; x++) {
            expresionEvaluada[x] = expresionPrincipal[x + 2];
//            System.out.println(expresionEvaluada[x]);
        }

        return variableResultado;
    }
    
     private static void FuncionDivEstadoI(String[] FuncionDivEstado) {
        for (int a = 0; a < FuncionDivEstado.length; a++) {
            System.out.println(FuncionDivEstado[a]); 
        }
    }

}
