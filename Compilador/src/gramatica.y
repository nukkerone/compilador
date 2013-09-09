%{
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
programa : declaraciones sentencias
 ;
declaraciones : /* no hay mas declaraciones */
|  declaraciones declaracion
;

declaracion : tipo lista_variables ';' { /* eventos.EventNewRule("Declaracion de vars", analizador_lexico.getNumLinea()); */}
;

tipo : INT | STRING
;

lista_variables: ID
| lista_variables ',' ID 
;

sentencias : sentencia 
| sentencias sentencia 
;

sentencia : sentencia_completa {/* eventos.EventNewRule("Sentencia Completa", analizador_lexico.getNumLinea()); */}
| sentencia_incompleta { /* eventos.EventNewRule("Sentencia Incompleta", analizador_lexico.getNumLinea()); */}
//| error ';' {/*eventos.EventError("sentencia incorrecta", analizador_lexico.getNumLinea());
//				huboError = true; */}
;

sentencia_completa: asignacion
| sentencia_if_completo
| sentencia_while_completa
| sentencia_print
;

bloque: '{' sentencias '}'
| '{' '}'
;

bloque_o_completa: bloque
| sentencia_completa
;

bloque_o_sentencia: bloque
| sentencia
;

sentencia_incompleta : IF condicion bloque_o_completa ELSE sentencia_incompleta
| IF condicion bloque_o_sentencia
|FOR condicion sentencia_incompleta
;

condicion : '(' comparacion ')'
;

sentencia_if_completo : IF condicion bloque_o_completa ELSE bloque_o_completa
;


asignacion: ID '=' expresion ';'
| ID '=' expresion {/* eventos.EventError("falta un punto y coma", analizador_lexico.getNumLinea());
				huboError = true; */}
;

comparacion : expresion COMPARADOR expresion
;

sentencia_while_completa: FOR condicion bloque_o_completa
;

sentencia_print: PRINT '(' STRING ')' ';'
| PRINT '(' STRING ')' {/* eventos.EventError("falta un ';'", analizador_lexico.getNumLinea());
				huboError = true; */}
| PRINT '(' STRING {/* eventos.EventError("falta un ')'", analizador_lexico.getNumLinea());
				huboError = true; */}
| PRINT STRING ')' {/* eventos.EventError("falta un '('", analizador_lexico.getNumLinea());
				huboError = true; */}
;

expresion : expresion '+' termino
| expresion '-' termino
| termino
;

termino : termino '*' factor
| termino '/' factor
| factor
;

factor : ID
| CTE
| '(' expresion ')'
;


%%

boolean finished = false;

int yylex()
{
    Token t= anLexico.getNextToken();

    if(anLexico.hasNext() && t == null)
            if (finished)
                    return 0;
            else{
                    finished = true;
                    return FIN;
            }

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
        System.out.println("par:"+s);
}

AnalizadorLexico anLexico;
public void addAnalizadorLexico( AnalizadorLexico al)
{
    anLexico = al;
}

static Hashtable<String, Short> Conversor;
static {
	Conversor = new Hashtable<String, Short>();
	
	Conversor.put("CADENA", STRING);
	Conversor.put("INTEGER", CTE);
	Conversor.put("ID", ID);
	Conversor.put("if", IF);
	Conversor.put( "else", ELSE);
	Conversor.put( "print", PRINT);
	Conversor.put( "for", FOR);
	Conversor.put( "int", INT);
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