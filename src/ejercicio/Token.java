package ejercicio;

//Token.java contiene la lista de palabras clave que usa el analizador léxico.
//Cada Token representa un tipo de elemento que el Lexer puede reconocer
//dentro de una operación matemática.

public enum Token {
	 // Representa cualquier número válido encontrado en la operación.
    // Ejemplo: 10, 50, 839
    NUMERO,
    // Representa el símbolo +
    // Se usa para realizar sumas.
    SUMA,
    // Representa el símbolo -
    // Se usa para realizar restas.
    RESTA,
    // Representa el símbolo *
    // Se usa para realizar multiplicaciones.
    MULTIPLICACION,
 
    // Representa el paréntesis de apertura (
    // Sirve para iniciar una operación con prioridad.
    PARENTESIS_A,
    // Representa el paréntesis de cierre )
    // Sirve para cerrar una operación con prioridad.
    PARENTESIS_C,
    // Representa el símbolo =
    // Indica el final visual de la operación.
    IGUAL,
    // Representa un carácter inválido o una operación mal escrita.
    // Cuando aparece este Token, el programa devuelve ERROR.
    ERROR
}