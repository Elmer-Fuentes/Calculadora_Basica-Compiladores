package ejercicio;

//Importa la clase File para poder manejar archivos desde Java.
import java.io.File;

public class Genera {

	public static void main(String[] args) {
        // Esta es la ruta donde se encuentra el archivo Lexer.flex.
        // Lexer.flex contiene las reglas léxicas creadas para JFlex.
		
		String path = "C:\\Users\\Fuentes Elmer\\Desktop\\Proyecto_Final_Calculadora\\src\\ejercicio\\Lexer.flex";
		   // Llama al método que genera el Lexer.java a partir del archivo Lexer.flex.
		generarLexer(path);
	}

	public static void generarLexer(String path) {
		   // Convierte la ruta en un archivo que Java puede reconocer.
		File archivo = new File(path);
		// JFlex toma el archivo Lexer.flex y genera automáticamente Lexer.java.
        // Ese Lexer.java es el analizador léxico que después usa Analizar.java.
        JFlex.Main.generate(archivo);
    }
	
}
