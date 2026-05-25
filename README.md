# Calculadora Básica

Proyecto desarrollado en Java que implementa un analizador léxico y sintáctico, semantico para procesar expresiones aritméticas mediante archivos de entrada. La calculadora valida jerarquía de operaciones, manejo de paréntesis y gestión de errores.

## 🚀 Arquitectura del Proyecto

El proyecto utiliza una arquitectura de tres capas para transformar texto plano en resultados matemáticos:

1.  **Capa de Definición (`Lexer.flex` y `Token.java`)**: 
    * `Lexer.flex` contiene las reglas léxicas (expresiones regulares) para identificar números y operadores.
    * `Token.java` actúa como el diccionario que clasifica dichos elementos (ej. `SUMA`, `NUMERO`).
2.  **Capa de Generación (`Genera.java`)**: 
    * Clase encargada de compilar la especificación de JFlex para producir el ejecutable `Lexer.java`.
3.  **Capa de Procesamiento (`Analizar.java`)**: 
    * Núcleo del proyecto. Lee `entrada.txt`, procesa los tokens y aplica la lógica matemática para generar `salida.txt`.

## 🛠️ Requisitos
* **Java SDK** (Desarrollado con Java 8).
* **IDE**: Eclipse recomendado.
* **Librerías**: JFlex.jar (incluida en el proyecto).

## 📋 Guía de Uso

1.  **Entrada**: Ubica tu archivo `entrada.txt` en `src/ejercicio/`. Escribe tus operaciones, una por línea.
2.  **Ejecución**: 
    * Localiza la clase `Analizar.java`.
    * Haz clic derecho -> **Run As** -> **Java Application**.
3.  **Configuración**: Al iniciar, selecciona el modo de detalle:
    * `S`: Mostrar operaciones en consola.
    * `N`: Máxima velocidad (sin detalles).
    * `A`: Guardar desglose detallado en `archivo_detalle.txt`.

## ⚙️ Resolución de Errores
El sistema procesa cada línea de forma independiente. Si una expresión no cumple con la sintaxis o tiene errores lógicos (división por cero, paréntesis desbalanceados, caracteres inválidos), el sistema marcará el resultado como `ERROR` en `salida.txt` sin detener el procesamiento del resto del archivo.

---
*Desarrollado como proyecto académico para Ingeniería en Sistemas - Universidad Mariano Gálvez de Guatemala.*
