package ejercicio;

import java.io.*;
import java.util.*;

public class Analizar {

    // =========================================================
    // CONTADORES GLOBALES
    // correctas: cuenta las operaciones que se resolvieron bien.
    // incorrectas: cuenta las operaciones que dieron ERROR.
    // =========================================================
    static int correctas = 0, incorrectas = 0;

    // =========================================================
    // MODO DE DETALLE
    // S = muestra cada operacion en consola.
    // N = no muestra detalle, solo resumen final. Es el modo mas rapido.
    // A = guarda el detalle en archivo_detalle.txt, sin pintar en consola.
    // =========================================================
    static String modo = "N";

    // Este BufferedWriter solo se usa si el usuario selecciona la opcion A.
    static BufferedWriter detalle = null;

    public static void main(String[] args) {

        // Guarda el tiempo inicial para calcular cuanto tarda todo el proceso.
        long inicio = System.currentTimeMillis();

        // =====================================================
        // RUTAS DEL PROYECTO
        // entrada.txt: archivo que contiene las operaciones.
        // salida.txt: archivo donde se guardan los resultados.
        // archivo_detalle.txt: archivo opcional para guardar el detalle.
        // =====================================================
        String pathEntrada = "C:\\Users\\Fuentes Elmer\\Desktop\\Proyecto_Final_Calculadora\\src\\ejercicio\\entrada.txt";
        String pathSalida  = "C:\\Users\\Fuentes Elmer\\Desktop\\Proyecto_Final_Calculadora\\src\\ejercicio\\salida.txt";
        String pathDetalle = "C:\\Users\\Fuentes Elmer\\Desktop\\Proyecto_Final_Calculadora\\src\\ejercicio\\archivo_detalle.txt";

        try {
            Scanner sc = new Scanner(System.in);

            // =================================================
            // MENU DE OPCIONES
            // Aqui el usuario decide si quiere ver detalle o no.
            // Esto mejora velocidad porque evita imprimir millones
            // de lineas en consola cuando no es necesario.
            // =================================================
            System.out.println("Seleccione modo de detalle:");
            System.out.println("S = Mostrar operaciones en consola");
            System.out.println("N = No mostrar detalle, maxima velocidad");
            System.out.println("A = Guardar detalle en archivo_detalle.txt");
            System.out.print("Opcion: ");

            modo = sc.nextLine().trim().toUpperCase();

            // Si el usuario escribe algo distinto de S o A,
            // automaticamente se toma como N para maxima velocidad.
            if (!modo.equals("S") && !modo.equals("A")) modo = "N";

            // =================================================
            // LECTURA Y ESCRITURA CON BUFFER
            // Se usa buffer de 1 MB para mejorar rendimiento.
            // =================================================
            BufferedReader br = new BufferedReader(new FileReader(pathEntrada), 1024 * 1024);
            BufferedWriter bw = new BufferedWriter(new FileWriter(pathSalida), 1024 * 1024);

            // Si el usuario selecciona A, se crea archivo_detalle.txt.
            if (modo.equals("A")) {
                detalle = new BufferedWriter(new FileWriter(pathDetalle), 1024 * 1024);
            }

            System.out.println("--- INICIO DE PROCESAMIENTO CALCULADORA ---");
            System.out.println("=========================================================");

            String linea;

            // =================================================
            // PROCESAMIENTO PRINCIPAL
            // Lee entrada.txt linea por linea.
            // Cada linea se evalua como una operacion matematica.
            // =================================================
            
            while ((linea = br.readLine()) != null) {

                // Ignora lineas vacias.
                if (linea.trim().isEmpty()) continue;

                // Evalua la expresion usando el Lexer y la logica matematica.
                String resultado = evaluarExpresion(linea);

                // Clasifica si la operacion fue correcta o incorrecta.
                if (resultado.equals("ERROR")) incorrectas++;
                else correctas++;

                // Limpia espacios al inicio y final.
                String limpia = linea.trim();

                // Si la operacion no trae =, se agrega para mostrar formato correcto.
                if (!limpia.endsWith("=")) limpia += "=";

                // Une la operacion con su resultado final.
                String salida = limpia + " " + resultado;

                // salida.txt SIEMPRE se escribe, sin importar si el modo es S, N o A.
                bw.write(salida);
                bw.newLine();

                // El detalle depende del modo:
                // S = consola
                // A = archivo_detalle.txt
                // N = no hace nada
                escribirDetalle("Línea Procesada: " + salida);
                escribirDetalle("---------------------------------------------------------");
            }

            // Cierra archivos principales.
            br.close();
            bw.close();

            // Cierra archivo de detalle solo si fue creado.
            if (detalle != null) detalle.close();

            // Imprime una sola vez el resumen de simbolos y operaciones.
            imprimirResumenSimbolos();

            // Imprime resumen final con conteos y tiempo.
            imprimirResumenFinal(inicio);

        } catch (IOException e) {
            System.err.println("Error crítico: Verifique el archivo entrada.txt y las rutas.");
            e.printStackTrace();
        }
    }

    // =========================================================
   // El Lexer generado con JFlex lee la linea y devuelve tokens
    // como NUMERO, SUMA, RESTA, MULTIPLICACION, PARENTESIS_A,
    // PARENTESIS_C, IGUAL o ERROR.
    // VARIABLE DESDE MAIN DONDE SE ALMACENAN LAS LINEAS 
    // SI EL VALOR POR EL LEXER ES VALIDO LO ENVIA AL METODO CALCULAR
    // =========================================================
    public static String evaluarExpresion(String expr) {

        // Guarda los valores reales que se usaran para calcular.
        ArrayList<String> valores = new ArrayList<String>();

        // Guarda los nombres de tokens para mostrar detalle si el usuario lo pide.
        ArrayList<String> tablaLinea = new ArrayList<String>();

        // Cuenta cuantas operaciones aritmeticas hay en la linea.
        int operaciones = 0;

        try {
            // El Lexer analiza la expresion recibida como texto.
            Lexer lexer = new Lexer(new StringReader(expr));

            while (true) {

                // yylex() devuelve el siguiente token encontrado.
                Token t = lexer.yylex();

                // Si ya no hay mas tokens, termina el analisis.
                if (t == null) break;

                // Si el Lexer encuentra algo invalido, retorna ERROR.
                if (t == Token.ERROR) {
                    escribirDetalle("Tabla de Símbolos -> [ERROR LÉXICO DETECTADO]");
                    return "ERROR";
                }

                // yytext() devuelve el texto exacto reconocido por el Lexer.
                String valor = lexer.yytext();

                // Guarda el nombre del token para la tabla de simbolos.
                tablaLinea.add(nombreToken(t));

                // Cuenta operaciones matematicas. Maximo permitido: 3.
                if (t == Token.SUMA || t == Token.RESTA || t == Token.MULTIPLICACION) operaciones++;

                // El igual se reconoce, pero no se usa para calcular.
                if (t != Token.IGUAL) valores.add(valor);
            }

            // Muestra o guarda la tabla de simbolos dependiendo del modo.
            escribirDetalle("Tabla de Símbolos -> " + tablaLinea);

            // =================================================
            // Aqui se controla que solo se permitan maximo
            // 3 operaciones aritmeticas por expresion.
            // =================================================
            if (operaciones > 3 || valores.isEmpty()) return "ERROR";

            // Si todo esta correcto, pasa a calcular.
            return calcular(valores);

        } catch (Exception e) {
            return "ERROR";
        }
    }

    // =========================================================
    // Este metodo traduce cada Token a un nombre facil de entender.
    //
    // +  = SUMA
    // -  = RESTA
    // *  = MULTIPLICACION
    // (  = PARENTESIS_A
    // )  = PARENTESIS_C
    // =  = IGUAL
    // =========================================================
    static String nombreToken(Token t) {

        switch (t) {
            case NUMERO: return "NUMERO";
            case SUMA: return "SUMA";
            case RESTA: return "RESTA";
            case MULTIPLICACION: return "MULTIPLICACION";
            case PARENTESIS_A: return "PARENTESIS_A";
            case PARENTESIS_C: return "PARENTESIS_C";
            case IGUAL: return "IGUAL";
            default: return "ERROR";
        }
    }

    // =========================================================
    // CONTROL DE DETALLE
    //
    // S = imprime en consola.
    // A = guarda en archivo_detalle.txt.
    // N = no imprime ni guarda detalle.
    //
    // Esto no cambia la logica del programa; solo controla
    // donde se muestra o guarda la informacion detallada.
    // =========================================================
    static void escribirDetalle(String texto) {

        try {
            if (modo.equals("S")) {
                System.out.println(texto);
            } else if (modo.equals("A") && detalle != null) {
                detalle.write(texto);
                detalle.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo archivo_detalle.txt");
        }
    }

    // =========================================================
    // RESUMEN DE SIMBOLOS
    // Se imprime solo una vez al final.
    // No imprime todos los numeros en vertical.
    // =========================================================
    static void imprimirResumenSimbolos() {

        System.out.println("\nTABLA: OPERACIONES");
        System.out.println("+        SUMA");
        System.out.println("-        RESTA");
        System.out.println("*        MULTIPLICACION");

        System.out.println("\nTABLA: SIMBOLOS");
        System.out.println("(        PARENTESIS_A");
        System.out.println(")        PARENTESIS_C");
        System.out.println("=        IGUAL");
    }

    // =========================================================
    // RESUMEN FINAL
    // Muestra:
    // - total de correctas
    // - total de incorrectas
    // - total analizado
    // - tiempo transcurrido en minutos y segundos
    // =========================================================
    static void imprimirResumenFinal(long inicio) {

        long totalSegundos = (System.currentTimeMillis() - inicio) / 1000;
        long minutos = totalSegundos / 60;
        long segundos = totalSegundos % 60;

        System.out.println("\n=========================================================");
        System.out.println("RESUMEN DE EVALUACIÓN FINAL:");
        System.out.println("Total de expresiones CORRECTAS (validas): " + correctas);
        System.out.println("Total de expresiones INCORRECTAS (invalidas): " + incorrectas);
        System.out.println("Total de operaciones analizadas: " + (correctas + incorrectas));
        System.out.println("Tiempo transcurrido: " + minutos + " minutos y " + segundos + " segundos");

        if (modo.equals("A")) {
            System.out.println("Detalle guardado en: archivo_detalle.txt");
        }

        System.out.println("=========================================================");
        System.out.println("--- PROCESO FINALIZADO CON ÉXITO ---");
    }

    // =========================================================
    // Si hay parentesis, primero busca el cierre ")"
    // y luego busca hacia atras su apertura "(".
    // Si falta uno de los dos, devuelve ERROR.
    // =========================================================
    static String calcular(ArrayList<String> lista) {

        try {
            // Primero se resuelven los parentesis.
            while (lista.contains("(")) {

                int fin = lista.indexOf(")");
                int ini = -1;   // -1 si no encontro nada

                // Busca el parentesis de apertura mas cercano hacia atras.
                for (int i = fin; i >= 0; i--) {
                    if (lista.get(i).equals("(")) {
                        ini = i;
                        break;
                    }
                }

                // Si no encontro apertura o cierre, la expresion es invalida.
                if (ini == -1 || fin == -1) return "ERROR";

                // Extrae lo que esta dentro del parentesis.
                ArrayList<String> sub = new ArrayList<String>(lista.subList(ini + 1, fin));

                // Resuelve la operacion interna.
                String r = operar(sub);

                if (r.equals("ERROR")) return "ERROR";

                // Reemplaza todo el parentesis por el resultado obtenido.
                lista.subList(ini, fin + 1).clear();
                lista.add(ini, r);
            }

            // Cuando ya no hay parentesis, resuelve lo restante.
            return operar(lista);

        } catch (Exception e) {
            return "ERROR";
        }
    }

    // =========================================================
    //
    // Orden de resolucion:
    // 1. Primero multiplicaciones. 
    // 2. Luego sumas y restas de izquierda a derecha.
    //
    // Ejemplo:
    // 1 + 2 * 3 =
    // Primero 2 * 3 = 6
    // Luego 1 + 6 = 7
    // =========================================================
    static String operar(ArrayList<String> t) {

        try {
            if (t.isEmpty()) return "ERROR";

            // =================================================
            // PRIMERO: MULTIPLICACIONES
            // Busca cada "*" y multiplica el numero anterior
            // por el numero siguiente.
            // =================================================
            for (int i = 0; i < t.size(); i++) {

                if (t.get(i).equals("*")) {

                    // No puede haber multiplicacion al inicio o al final.
                    if (i == 0 || i == t.size() - 1) return "ERROR";

                    int a = Integer.parseInt(t.get(i - 1));
                    int b = Integer.parseInt(t.get(i + 1));

                    // Reemplaza: numero * numero por el resultado.
                    t.subList(i - 1, i + 2).clear();
                    t.add(i - 1, String.valueOf(a * b));

                    // Retrocede para seguir evaluando correctamente.
                    i--;
                }
            }


            
            // No se permite que la expresion inicie con + o -.
            if (t.isEmpty() || t.get(0).equals("+") || t.get(0).equals("-")) return "ERROR";

            // =================================================
            // SEGUNDO: SUMAS Y RESTAS
            // Se toma el primer numero como base.
            // Luego se avanza de dos en dos:
            // operador, numero.
            // =================================================
            int total = Integer.parseInt(t.get(0));

            for (int i = 1; i < t.size(); i += 2) {

                // Si falta un numero despues del operador, es ERROR.
                if (i + 1 >= t.size()) return "ERROR";
                // lectura tipo operación (i) simbolos
                String op = t.get(i);
                int n = Integer.parseInt(t.get(i + 1)); // como tenemos la posición 1 esta ocupada pasamos en numero impares

                if (op.equals("+")) total += n; 
                else if (op.equals("-")) total -= n;
                else return "ERROR";
            }

            return String.valueOf(total);

        } catch (Exception e) {
            return "ERROR";
        }
    }
}