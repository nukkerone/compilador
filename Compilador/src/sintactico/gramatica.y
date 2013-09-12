%{
package sintactico;

import lexico.*; 
import herramientaerror.*;
import java.util.Hashtable;

%}

/** YACC Declarations **/
%token IF
%token ELSE
%token THEN
%token BEGIN
%token END
%token PRINT
%token FUNCTION
%token RETURN
%token ID
%token CTE
%token FIN
%token STRING
%token INT
%token COMPARADOR
%token FOR

%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%%

programa: declaraciones ejecutable FIN
| declaraciones error FIN { this.eventoError.add("No se encuentran sentencias ejecutables para el programa", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| declaraciones ejecutable error { this.eventoError.add("No se encontro el fin de linea", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

declaraciones: 
| declaraciones declaracion;
;

declaracion: declaracion_simple
| FUNCTION ID '(' parametro_formal ')' '{' declaraciones_funcion ejecutable_funcion '}'
;

declaraciones_funcion: 
| declaraciones_funcion declaracion_simple
;

declaracion_simple: tipo lista_variables ';'
;

parametro_formal: 
| tipo ID
;

parametro_real:
| ID
;

tipo: INT 
| STRING
;

lista_variables: ID
| lista_variables ',' ID
;

ejecutable: 
| sentencias
;

ejecutable_funcion: 
| sentencias_funcion
;

bloque: sentencia 
| '{' sentencias '}'
| '{' '}'
;

llamado_funcion: ID '(' parametro_real ')' ';'
;

sentencias: sentencia
| sentencias sentencia
;

sentencias_funcion: sentencia
| return_funcion
| sentencias_funcion sentencia
| sentencias_funcion return_funcion
;

return_funcion: RETURN ';'
| RETURN '(' expresion ')' ';'
;

sentencia: sentencia_if
| sentencia_for
| sentencia_asignacion
| sentencia_print
| llamado_funcion
;

sentencia_if: IF condicion THEN bloque      %prec LOWER_THAN_ELSE
| IF condicion THEN bloque ELSE bloque
;

sentencia_for: FOR '(' condicion_for ')' bloque;

condicion_for: ID '=' expresion ';' comparacion
;

sentencia_print: PRINT '(' STRING ')' ';';
;

condicion: '(' comparacion ')'
;

comparacion: expresion COMPARADOR expresion
;

sentencia_asignacion: ID '=' expresion ';'
;

expresion : expresion '+' termino
| expresion '-' termino
| termino
;

termino : termino '*' factor
| termino '/' factor
| factor
;

factor: ID
| CTE
| STRING
| '(' expresion ')'
;

%%

boolean finished = false;

int yylex()
{
    if (finished) {
        return 0;
    }
    
    boolean hasNext = anLexico.hasNext();
    if(!hasNext) {
        finished = true;
        return FIN;
    }

    Token t= anLexico.getNextToken();
    yylval = new ParserVal(t); //seteamos el token, como objeto de yylval
    yylval.ival = anLexico.getNroLinea();
    if(t== null)
            return YYERRCODE;
    Short s = (Short) Conversor.get(t.getPalabraReservada());

    return s.intValue();
}

void yyerror(String s)
{
    if(s.contains("under"))
        System.out.println("Error :"+s);
}

AnalizadorLexico anLexico;
public void addAnalizadorLexico( AnalizadorLexico al)
{
    this.anLexico = al;
}

EventoError eventoError;
public void addEventoError( EventoError e ) {
    this.eventoError = e;
}

public int parse() {
    return yyparse();
}

static Hashtable<String, Short> Conversor;
static {
	Conversor = new Hashtable<String, Short>();
	
	Conversor.put("string", STRING);
        Conversor.put("int", INT);
        Conversor.put("function", FUNCTION);
	Conversor.put("CTE", CTE);
	Conversor.put("ID", ID);
	Conversor.put("if", IF);
        Conversor.put("then", THEN);
	Conversor.put("else", ELSE);
	Conversor.put("print", PRINT);
        Conversor.put("return", RETURN);
	Conversor.put( "for", FOR);
	Conversor.put( "<=", COMPARADOR);
	Conversor.put( "==", COMPARADOR);
	Conversor.put( ">=", COMPARADOR);
	Conversor.put( "!=", COMPARADOR);
	Conversor.put( "<", COMPARADOR);
	Conversor.put( ">", COMPARADOR);
	Conversor.put( ";", new Short((short) ';'));
	Conversor.put( ",", new Short((short) ','));
	Conversor.put( "=", new Short((short) '='));
	Conversor.put( "{", new Short((short)'{'));
	Conversor.put( "}", new Short((short)'}'));
	Conversor.put( "(", new Short((short)'('));
	Conversor.put( ")", new Short((short)')'));
	Conversor.put( "+", new Short((short)'+'));
	Conversor.put( "-", new Short((short)'-'));
	Conversor.put( "*", new Short((short)'*'));
	Conversor.put( "/", new Short((short)'/'));	
}