%{
package sintactico;

import lexico.*; 
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

%%

programa: declaraciones ejecutable FIN
;

declaraciones: 
| declaraciones declaracion;
;

declaracion: declaracion_simple
| FUNCTION ID '(' parametro ')' '{' declaraciones_funcion ejecutable '}'
;

declaraciones_funcion: 
| declaraciones_funcion declaracion_simple
;

declaracion_simple: tipo lista_variables ';'
;

parametro: 
| tipo ID
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

bloque: '{' sentencias '}'
;

cuerpo_funcion: declaraciones_funcion ejecutable
;

llamado_funcion: ID '(' parametro ')' ';'
;

sentencias: sentencia
| sentencias sentencia
;

sentencia: sentencia_if
| sentencia_asignacion
;

sentencia_if: IF condicion THEN bloque parte_else
;

parte_else: 
| ELSE bloque
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
    anLexico = al;
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
	Conversor.put( "else", ELSE);
	Conversor.put( "print", PRINT);
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