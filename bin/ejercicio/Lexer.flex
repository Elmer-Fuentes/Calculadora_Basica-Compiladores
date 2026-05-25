package ejercicio;
import static ejercicio.Token.*;

%%

/* 1. CONFIGURACIÓN DEL ANALIZADOR */
%class Lexer
%type Token
%unicode

/* 2. DEFINICIONES DE EXPRESIONES REGULARES */
Numero = 0 | [1-9][0-9]*        /* 0 o más  (*)    */   
Espacios = [ \t]+    			/*   \t agrupa espacio o tabulaciones (+) debe tener   1 o mas */ 

%%

/* 3. ESPECIFICACIÓN DE REGLAS LÉXICAS */
"+" { return SUMA; }	
"-" { return RESTA; }
"*" { return MULTIPLICACION; }
"(" { return PARENTESIS_A; }
")" { return PARENTESIS_C; }
"=" { return IGUAL; }
{Numero} { return NUMERO; }
{Espacios} { /* Ignorar */ }
. { return ERROR; }