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
;

declaraciones:
| declaraciones declaracion
;

declaracion: declaracion_simple
| FUNCTION ID '(' parametro_formal ')' BEGIN declaraciones_funcion ejecutable_funcion END
| FUNCTION ID '(' parametro_formal ')' '{' error '}' {this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

declaraciones_funcion: 
| declaraciones_funcion declaracion_simple
;

declaracion_simple: tipo lista_variables {this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| tipo lista_variables ';'  { this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
;

parametro_formal: 
| tipo ID
;

parametro_real:
| ID            
;

tipo: INT
| STRING error         {this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

lista_variables: ID
| lista_variables ',' ID
;

ejecutable: 
| sentencias
| sentencias error
| sentencias declaracion error    {this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

ejecutable_funcion: 
| sentencias_funcion
;

bloque: sentencia
| BEGIN sentencias END
| '{' error '}' {this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| BEGIN END
| BEGIN sentencias error { this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

llamado_funcion: ID '(' parametro_real ')' ';'      { this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
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

sentencia_if: IF condicion THEN bloque      %prec LOWER_THAN_ELSE       { this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| IF condicion THEN bloque ELSE bloque                                  { this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
;

sentencia_for: FOR '(' condicion_for ')' bloque     { this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| FOR '(' condicion_for bloque    { this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| FOR error condicion_for         { this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;        

condicion_for: ID '=' expresion ';' comparacion
;

sentencia_print: PRINT '(' STRING ')' ';'              { this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| PRINT '(' STRING ';'    { this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| PRINT STRING error        { this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

condicion: '(' comparacion ')'
| '(' comparacion       { this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| error                 { this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

comparacion: expresion COMPARADOR expresion
;

sentencia_asignacion: ID '=' expresion ';'  { this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
;

expresion : expresion '+' termino   { this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| expresion '-' termino { this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| termino
;

termino : termino '*' factor    { this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| termino '/' factor            { this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
| factor
;

factor: ID
| constante
| '(' expresion ')'
;

constante: CTE
| '-' CTE                       { this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
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
        Conversor.put("begin", BEGIN);
	Conversor.put( "end", END);
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