//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package sintactico;

import lexico.*; 
import herramientaerror.*;
import java.util.Hashtable;
import java.util.Vector;
import cod_intermedio.*;
import interfaces.*;

//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short ELSE=258;
public final static short THEN=259;
public final static short BEGIN=260;
public final static short END=261;
public final static short PRINT=262;
public final static short FUNCTION=263;
public final static short RETURN=264;
public final static short ID=265;
public final static short CTE=266;
public final static short FIN=267;
public final static short STRING=268;
public final static short INT=269;
public final static short COMPARADOR=270;
public final static short FOR=271;
public final static short LOWER_THAN_ELSE=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    3,    6,    6,    4,    4,
    5,    5,   10,   10,   10,    8,    8,    9,    9,    2,
    2,    2,    2,    7,    7,   14,   14,   14,   14,   14,
   16,   12,   12,   13,   13,   13,   13,   17,   17,   15,
   15,   15,   15,   15,   19,   24,   19,   23,   20,   20,
   20,   26,   22,   22,   22,   25,   25,   25,   27,   21,
   18,   18,   18,   28,   28,   28,   29,   29,   29,   29,
   11,   11,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    9,    8,    0,    2,    2,    3,
    0,    2,    0,    1,    1,    1,    2,    1,    3,    0,
    1,    2,    3,    0,    1,    1,    3,    2,    2,    3,
    4,    1,    2,    1,    1,    2,    2,    2,    5,    1,
    1,    1,    1,    2,    3,    0,    6,    2,    5,    4,
    3,    5,    5,    4,    3,    3,    2,    1,    3,    4,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    3,
    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    0,   16,    0,    0,
    3,    4,    0,    0,   32,    0,   40,   41,   42,   43,
    0,   58,    0,   48,    0,    0,    0,    0,    0,   17,
    0,    0,    1,   18,    0,   22,    0,   33,   44,    0,
    0,   71,    0,    0,   68,   69,    0,    0,    0,   66,
   55,    0,    0,   14,    0,   15,    0,    0,   51,    0,
   10,    0,   23,    0,    0,    0,   26,    0,   72,    0,
    0,    0,   56,    0,    0,    0,   54,    0,    0,   31,
   60,    0,    0,   50,   19,   29,    0,   28,   46,   70,
    0,    0,    0,   64,   65,   53,    0,   12,    0,   49,
   30,   27,    0,    7,    0,    0,   47,    0,    0,   52,
    0,    8,    0,    0,   34,   35,    6,    0,   38,    5,
   36,   37,    0,    0,   39,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   78,  108,  113,   13,   35,   55,
   45,   14,  114,   66,   67,   46,  116,   47,   17,   18,
   19,   20,   21,  103,   24,   59,   48,   49,   50,
};
final static short yysindex[] = {                         0,
    0, -178,  -31,  -37, -233,  -30, -215,    0,  -14, -211,
    0,    0, -205, -142,    0,    6,    0,    0,    0,    0,
 -193,    0,   41,    0, -188, -198,   32,   43,   41,    0,
 -182, -182,    0,    0,  -20,    0, -179,    0,    0, -118,
   49,    0,   41, -174,    0,    0,   28,   53,  -27,    0,
    0,  -19, -257,    0,   55,    0,  -16,   47,    0,   39,
    0, -155,    0,  -69, -145, -140,    0,    4,    0,   41,
   41,   41,    0,   41,   41,   54,    0,   83, -135,    0,
    0,   41, -118,    0,    0,    0, -125,    0,    0,    0,
    3,  -27,  -27,    0,    0,    0, -115,    0,   -1,    0,
    0,    0, -118,    0, -122,   41,    0, -162,   13,    0,
  -26,    0, -126, -114,    0,    0,    0,   41,    0,    0,
    0,    0,   16,   82,    0,
};
final static short yyrindex[] = {                         0,
    0, -113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -112,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  111,    0,    0,
    0,    0,    0,    0,  -98,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0, -103,  -24,    0,
    0,    0,  119,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -82,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   26,   -7,   10,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -100,    0,    0,
    0,    0,    0,  -93,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  158,   68,    0,    0,    0,  124,    0,    0,
  150,  120,    0,  -53,   11,   14,   76,   -6,    0,    0,
    0,    0,    0,    0,    0,  159,   88,   -9,   30,
};
final static int YYTABLESIZE=310;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         67,
   67,   67,   26,   67,   65,   67,   84,  105,   23,   28,
    7,    8,   15,  118,   74,   16,   63,   67,   63,   75,
   63,   76,   57,   62,   38,   32,   71,   16,   72,  100,
   29,   27,  119,   61,   63,   61,   68,   61,   61,   77,
   30,   71,   81,   72,   90,   71,   71,   72,   72,  107,
   62,   61,   62,   16,   62,   33,  124,  106,   71,   34,
   72,   92,   93,   91,   39,   40,   59,   51,   62,   52,
   71,   53,   72,   16,   15,   99,   63,   16,    3,   83,
   43,   67,   58,    4,    5,   44,    6,   44,   28,    7,
    8,   69,    9,   73,    3,   80,   16,   38,   63,    4,
   16,  111,    6,   94,   95,    7,    8,   82,    9,   85,
   88,  123,   96,   36,    3,   61,   16,   89,  115,    4,
    5,   16,    6,   97,  121,    7,    8,   16,    9,   98,
  101,    3,   62,  109,  120,  102,    4,  117,    3,    6,
  125,   64,    3,    4,  104,    9,    6,    4,   59,  111,
    6,   13,    9,   20,   21,   57,    9,    9,    9,   11,
   24,   65,    9,    9,    9,    9,    9,   25,    9,    9,
    9,   37,    9,   45,   45,  112,   79,   56,   45,   45,
   45,   45,   45,   87,   45,   45,   45,    3,   45,  122,
   60,   86,    4,  110,    0,    6,    0,    0,    0,    0,
    0,    9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   67,   67,   67,   67,   67,   67,
   67,   67,   67,   67,   22,   67,   67,   67,   67,   67,
   25,   63,   63,   63,   63,   63,   63,   63,   63,   63,
   63,   31,   63,   63,   63,   63,   63,    0,   61,   61,
   61,   61,   61,   61,   61,   61,   61,   61,    0,   61,
   61,   61,   61,   61,    0,   62,   62,   62,   62,   62,
   62,   62,   62,   62,   62,    0,   62,   62,   62,   62,
   62,   59,   59,   59,   59,   59,   59,   59,   59,   59,
   59,    0,   59,   59,   59,    3,   59,   70,   64,    0,
    4,    0,    0,    6,    0,   41,   42,   54,   42,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,  123,   47,   60,  123,   40,   40,
  268,  269,    2,   40,   42,    2,   41,   59,   43,   47,
   45,   41,   29,   44,   14,   40,   43,   14,   45,   83,
   61,  265,   59,   41,   59,   43,   43,   45,   59,   59,
  256,   43,   59,   45,   41,   43,   43,   45,   45,  103,
   41,   59,   43,   40,   45,  267,   41,   59,   43,  265,
   45,   71,   72,   70,   59,  259,   41,  256,   59,  268,
   43,   40,   45,   60,   64,   82,  256,   64,  257,   41,
   40,  123,  265,  262,  263,   45,  265,   45,   40,  268,
  269,  266,  271,   41,  257,   41,   83,   87,  123,  262,
   87,  264,  265,   74,   75,  268,  269,   61,  271,  265,
  256,  118,   59,  256,  257,  123,  103,  258,  108,  262,
  263,  108,  265,   41,  114,  268,  269,  114,  271,  265,
  256,  257,  123,  256,  261,  261,  262,  125,  257,  265,
   59,  260,  257,  262,  260,  271,  265,  262,  123,  264,
  265,   41,  271,  267,  267,  259,  271,  256,  257,   41,
  261,  123,  261,  262,  263,  264,  265,  261,  267,  268,
  269,   14,  271,  256,  257,  108,   53,   28,  261,  262,
  263,  264,  265,   64,  267,  268,  269,  257,  271,  114,
   32,  261,  262,  106,   -1,  265,   -1,   -1,   -1,   -1,
   -1,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,  259,  260,  261,
  262,  263,  264,  265,  256,  267,  268,  269,  270,  271,
  268,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  256,  267,  268,  269,  270,  271,   -1,  256,  257,
  258,  259,  260,  261,  262,  263,  264,  265,   -1,  267,
  268,  269,  270,  271,   -1,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  265,   -1,  267,  268,  269,  270,
  271,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,   -1,  267,  268,  269,  257,  271,  270,  260,   -1,
  262,   -1,   -1,  265,   -1,  265,  266,  265,  266,  271,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=272;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","ELSE","THEN","BEGIN","END","PRINT",
"FUNCTION","RETURN","ID","CTE","FIN","STRING","INT","COMPARADOR","FOR",
"LOWER_THAN_ELSE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : declaraciones ejecutable FIN",
"declaraciones :",
"declaraciones : declaraciones declaracion",
"declaracion : declaracion_simple",
"declaracion : FUNCTION ID '(' parametro_formal ')' BEGIN declaraciones_funcion ejecutable_funcion END",
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' error '}'",
"declaraciones_funcion :",
"declaraciones_funcion : declaraciones_funcion declaracion_simple",
"declaracion_simple : tipo lista_variables",
"declaracion_simple : tipo lista_variables ';'",
"parametro_formal :",
"parametro_formal : tipo ID",
"parametro_real :",
"parametro_real : ID",
"parametro_real : constante",
"tipo : INT",
"tipo : STRING error",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"ejecutable :",
"ejecutable : sentencias",
"ejecutable : sentencias error",
"ejecutable : sentencias declaracion error",
"ejecutable_funcion :",
"ejecutable_funcion : sentencias_funcion",
"bloque : sentencia",
"bloque : BEGIN sentencias END",
"bloque : '{' error",
"bloque : BEGIN END",
"bloque : BEGIN sentencias error",
"llamado_funcion : ID '(' parametro_real ')'",
"sentencias : sentencia",
"sentencias : sentencias sentencia",
"sentencias_funcion : sentencia",
"sentencias_funcion : return_funcion",
"sentencias_funcion : sentencias_funcion sentencia",
"sentencias_funcion : sentencias_funcion return_funcion",
"return_funcion : RETURN ';'",
"return_funcion : RETURN '(' expresion ')' ';'",
"sentencia : sentencia_if",
"sentencia : sentencia_for",
"sentencia : sentencia_asignacion",
"sentencia : sentencia_print",
"sentencia : llamado_funcion ';'",
"sentencia_if : inicio_if THEN bloque",
"$$1 :",
"sentencia_if : inicio_if THEN bloque ELSE $$1 bloque",
"inicio_if : IF condicion",
"sentencia_for : FOR '(' condicion_for ')' bloque",
"sentencia_for : FOR '(' condicion_for bloque",
"sentencia_for : FOR error condicion_for",
"condicion_for : ID '=' expresion ';' comparacion",
"sentencia_print : PRINT '(' STRING ')' ';'",
"sentencia_print : PRINT '(' STRING ';'",
"sentencia_print : PRINT STRING error",
"condicion : '(' comparacion ')'",
"condicion : '(' comparacion",
"condicion : error",
"comparacion : expresion COMPARADOR expresion",
"sentencia_asignacion : ID '=' expresion ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : constante",
"factor : llamado_funcion",
"factor : '(' expresion ')'",
"constante : CTE",
"constante : '-' CTE",
};

//#line 188 "gramatica.y"

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

Vector<Integer> pilaSaltos = new Vector<Integer>();
Vector<Integer> pilaCondiciones = new Vector<Integer>();

private void agregarIfPila() {
    pilaSaltos.add(Terceto.tercetos.size());                        // Agregar a la Pila de saltos
    new TercetoSalto("BF");                                         // Crear el Terceto
}

private void eliminarIfPila() { //termina IF sin else
    Vector<Terceto> t = Terceto.tercetos;
    int indiceDesapilar = pilaSaltos.get(pilaSaltos.size()-1);      // Guardo indice a desapilar (Seria el ultimo)
    pilaSaltos.remove(pilaSaltos.size() -1);                        // Remuevo el ultimo de la pila
    new TercetoLabel();                                             // Se crea la etiqueta
    ((TercetoSalto) t.get(indiceDesapilar)).setDirSalto(t.size());  // 
}

private void empezarElse() {
    int posB = Terceto.tercetos.size();
    new TercetoSalto("BI");
    eliminarIfPila();
    pilaSaltos.add(posB);
}

private void terminarElse() {
	eliminarIfPila();
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
//#line 470 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 6:
//#line 44 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 9:
//#line 51 "gramatica.y"
{this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 10:
//#line 52 "gramatica.y"
{ this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 17:
//#line 65 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 22:
//#line 74 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 23:
//#line 75 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 28:
//#line 84 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 30:
//#line 86 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 31:
//#line 89 "gramatica.y"
{ this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 45:
//#line 113 "gramatica.y"
{ this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 46:
//#line 114 "gramatica.y"
{empezarElse();}
break;
case 47:
//#line 114 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 48:
//#line 120 "gramatica.y"
{ agregarIfPila(); }
break;
case 49:
//#line 123 "gramatica.y"
{ this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 50:
//#line 124 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 51:
//#line 125 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 53:
//#line 131 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
}
break;
case 54:
//#line 135 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 55:
//#line 136 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 57:
//#line 140 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 58:
//#line 141 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 59:
//#line 144 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 60:
//#line 149 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
}
break;
case 61:
//#line 155 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 62:
//#line 159 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 64:
//#line 166 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 65:
//#line 170 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 70:
//#line 180 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 72:
//#line 184 "gramatica.y"
{ this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
//#line 758 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
