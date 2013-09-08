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

expresion : expresion '+' termino |
            expresion '-' termino |
            termino
;

termino :   termino '*' factor |
            termino '/' factor |
            factor
;

factor :    ID  |
            CTE |
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
	Conversor.put("IDENTIFICADOR", ID);
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