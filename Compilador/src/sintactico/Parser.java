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
import java.util.Stack;
import cod_intermedio.*;
import interfaces.*;

//#line 28 "Parser.java"




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
    0,    1,    1,    3,    3,    3,    5,    9,    6,    8,
    8,    4,    4,    7,    7,   13,   13,   13,   11,   11,
   12,   12,    2,    2,    2,    2,   10,   10,   17,   17,
   17,   17,   17,   19,   15,   15,   16,   16,   16,   16,
   20,   20,   18,   18,   18,   18,   18,   22,   27,   22,
   26,   23,   29,   30,   30,   30,   25,   25,   25,   28,
   28,   28,   33,   31,   32,   24,   21,   21,   21,   34,
   34,   34,   35,   35,   35,   35,   14,   14,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    4,    8,    5,    0,    3,    0,
    2,    2,    3,    0,    2,    0,    1,    1,    1,    2,
    1,    3,    0,    1,    2,    3,    0,    1,    1,    3,
    2,    2,    3,    4,    1,    2,    1,    1,    2,    2,
    2,    5,    1,    1,    1,    1,    2,    3,    0,    6,
    2,    2,    2,    4,    3,    2,    5,    4,    3,    3,
    2,    1,    0,    2,    3,    4,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    0,   19,    0,    0,
    3,    4,    0,    0,    0,   35,    0,   43,   44,   45,
   46,    0,    0,   62,    0,   51,    0,    0,    0,    0,
    0,   20,    0,    0,   53,    1,    8,   21,    0,   25,
    0,   36,   47,    0,    0,    0,   52,   29,    0,   77,
    0,    0,   75,   74,    0,    0,    0,   72,   59,    0,
    0,   17,    0,   18,    0,   56,    0,   63,    0,   10,
   13,    0,   26,    0,   32,    0,   31,    0,   78,    0,
    0,    0,   60,    0,    0,    0,   58,    0,    0,   34,
   66,    0,    0,    5,    0,   22,   49,   33,   30,   76,
    0,    0,    0,   70,   71,   57,    0,   15,   54,   64,
    0,   11,    9,    0,   37,   38,    0,    0,    0,   41,
   39,   40,   50,    0,    0,    6,    0,   42,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   13,   69,   88,   95,   70,  113,
   14,   39,   63,   53,   15,  114,   47,   48,   54,  116,
   55,   18,   19,   20,   21,   22,  117,   26,   23,   35,
   92,   56,   93,   57,   58,
};
final static short yysindex[] = {                         0,
    0, -109,  -24,  -35, -248,  -20, -218,    0,  -21, -219,
    0,    0, -208, -204, -163,    0,    8,    0,    0,    0,
    0, -187, -120,    0,  -10,    0, -178, -184,   47,    1,
  -10,    0,  -21, -173,    0,    0,    0,    0,   -1,    0,
 -158,    0,    0, -120,  -83, -155,    0,    0,   63,    0,
  -10, -159,    0,    0,  -31,   68,    7,    0,    0,  -14,
 -198,    0,   69,    0,   -6,    0,   54,    0, -139,    0,
    0, -136,    0, -127,    0, -206,    0,   87,    0,  -10,
  -10,  -10,    0,  -10,  -10,   75,    0,   97, -119,    0,
    0,  109,  -10,    0,  -96,    0,    0,    0,    0,    0,
   17,    7,    7,    0,    0,    0,   32,    0,    0,    0,
  -27,    0,    0,  -81,    0,    0, -120,  -99,  -10,    0,
    0,    0,    0,   38,   98,    0,  105,    0,
};
final static short yyrindex[] = {                         0,
    0, -102,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -100,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  129,
    0,    0,    0,    0,    0,    0,    0,    0, -188,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,  -88,  -34,    0,    0,    0,
  136,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -144,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -113,    0,    0,  -76,    0,    0,    0,    0,    0,
    3,  -19,  -12,    0,    0,    0,  -80,    0,    0,    0,
    0,    0,    0,  -75,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  172,   94,    0,    0,    0,    0,    0,    0,
  130,    0,    0,  162,  148,    0,  -29,   21,   19,   80,
  -23,    0,    0,  161,    0,    0,    0,    0,    0,  163,
    0,  104,    0,    4,    6,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         73,
   73,   73,   46,   73,   28,   73,   69,   65,   69,   55,
   69,   81,  119,   82,   74,   25,   29,   73,   34,   30,
   17,   67,   16,   67,   69,   67,   86,   78,   68,   51,
   68,  120,   68,   17,   52,   42,   81,   32,   82,   67,
   31,   17,   72,   65,   87,   52,   68,   36,   84,   98,
    3,   37,   91,   85,   99,    4,  101,   71,    6,   81,
   38,   82,   17,   17,    9,   16,   43,   12,   12,    7,
    8,   44,   12,   12,   12,   12,   12,   59,   12,   12,
   12,   73,   12,   60,  102,  103,   61,  123,   69,  104,
  105,   67,   40,    3,   17,  125,   42,   73,    4,    5,
   77,    6,   30,   67,    7,    8,   79,    9,   83,   90,
   68,   48,   48,   17,   31,  115,   48,   48,   48,   48,
   48,   94,   48,   48,   48,   65,   48,  100,   96,   81,
   97,   82,   17,  106,  121,   17,    3,  107,  127,   45,
   81,    4,   82,   55,    6,  108,   55,    3,   55,  109,
    9,   55,    4,    5,  118,    6,  124,   55,    7,    8,
    3,    9,  126,  128,   23,    4,   24,  111,    6,   16,
   61,    7,    8,    3,    9,    3,   14,   75,    4,    7,
    4,    6,  111,    6,   27,   28,   41,    9,  112,    9,
   89,   64,   76,  122,   68,   66,  110,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   73,    0,   73,   73,    0,
   73,    0,   69,   73,   69,   69,    0,   69,   73,   73,
   69,   24,   27,    0,   33,   69,   69,   67,   80,   67,
   67,    0,   67,    0,   68,   67,   68,   68,    0,   68,
   67,   67,   68,    0,   49,   50,    0,   68,   68,   65,
    0,   65,   65,    0,   65,   62,   50,   65,    0,    0,
    0,    0,    0,   65,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,   31,   43,  123,
   45,   43,   40,   45,   44,   40,  265,   59,   40,   40,
    2,   41,    2,   43,   59,   45,   41,   51,   41,   40,
   43,   59,   45,   15,   45,   15,   43,  256,   45,   59,
   61,   23,   44,   41,   59,   45,   59,  267,   42,  256,
  257,  260,   59,   47,  261,  262,   80,   59,  265,   43,
  265,   45,   44,   45,  271,   45,   59,  256,  257,  268,
  269,  259,  261,  262,  263,  264,  265,  256,  267,  268,
  269,  123,  271,  268,   81,   82,   40,  117,  123,   84,
   85,  265,  256,  257,   76,  119,   76,  256,  262,  263,
  256,  265,   40,  123,  268,  269,  266,  271,   41,   41,
  123,  256,  257,   95,   61,   95,  261,  262,  263,  264,
  265,  261,  267,  268,  269,  123,  271,   41,  265,   43,
  258,   45,  114,   59,  114,  117,  257,   41,   41,  260,
   43,  262,   45,  257,  265,  265,  260,  257,  262,   41,
  271,  265,  262,  263,  123,  265,  256,  271,  268,  269,
  257,  271,  125,   59,  267,  262,  267,  264,  265,   41,
  259,  268,  269,  257,  271,  257,   41,  261,  262,  260,
  262,  265,  264,  265,  261,  261,   15,  271,   95,  271,
   61,   30,   45,  114,   34,   33,   93,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,  259,  260,   -1,
  262,   -1,  257,  265,  259,  260,   -1,  262,  270,  271,
  265,  256,  268,   -1,  256,  270,  271,  257,  270,  259,
  260,   -1,  262,   -1,  257,  265,  259,  260,   -1,  262,
  270,  271,  265,   -1,  265,  266,   -1,  270,  271,  257,
   -1,  259,  260,   -1,  262,  265,  266,  265,   -1,   -1,
   -1,   -1,   -1,  271,
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
"declaracion : cabecera_funcion BEGIN cuerpo_funcion END",
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' error '}'",
"cabecera_funcion : FUNCTION ID '(' parametro_formal ')'",
"$$1 :",
"cuerpo_funcion : $$1 declaraciones_funcion ejecutable_funcion",
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
"$$2 :",
"sentencia_if : inicio_if THEN bloque ELSE $$2 bloque",
"inicio_if : IF condicion",
"sentencia_for : comienzo_for bloque",
"comienzo_for : FOR condicion_for",
"condicion_for : '(' sentencia_asignacion comparacion_for ')'",
"condicion_for : '(' sentencia_asignacion comparacion_for",
"condicion_for : error condicion_for",
"sentencia_print : PRINT '(' STRING ')' ';'",
"sentencia_print : PRINT '(' STRING ';'",
"sentencia_print : PRINT STRING error",
"condicion : '(' comparacion ')'",
"condicion : '(' comparacion",
"condicion : error",
"$$3 :",
"comparacion_for : $$3 comparacion",
"comparacion : expresion COMPARADOR expresion",
"sentencia_asignacion : ID '=' expresion ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : llamado_funcion",
"factor : constante",
"factor : '(' expresion ')'",
"constante : CTE",
"constante : '-' CTE",
};

//#line 299 "gramatica.y"

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

private ParserVal clone(ParserVal originParserVal){
    ParserVal newParserVal = new ParserVal();
    newParserVal.obj = originParserVal.obj;
    newParserVal.ival = originParserVal.ival;
    return newParserVal;
}

private void asignarTipo(int tipo, Vector vars) {
    for(int i = 0; i < vars.size(); i++){
        ParserVal p = (ParserVal) vars.get(i);
        TypeableToken t = (TypeableToken) p.obj;
        if(t.getTipo() == Typeable.TIPO_RECIEN_DECLARADA) {
            t.setTipo(tipo);
            boolean sobreescribir = true;
            this.anLexico.getTablaSimbolos().addSimbolo(t, sobreescribir);
        }
        else {
            this.eventoError.add("Variable redeclarada " + t.getLexema(), p.ival, "Semantico", "Error" );
        }
    }
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
Vector<Typeable> pilaIndicesFor = new Vector<Typeable>();
Stack<Token> pilaParametros = new Stack<Token>(); 

Hashtable<String, Integer> etFuncionesMapping = new Hashtable<String, Integer>();
Hashtable<String, Integer> retFuncionesMapping = new Hashtable<String, Integer>();

Hashtable<String, Integer> functionLabels = new Hashtable<String, Integer>();

boolean dentroDeFuncion = false;
//int indiceUltimaFuncion;
boolean saltarFuncion = false;
int labelFuncion;
int saltoFuncion;


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

private void desapilarFor(){
    Typeable indice = pilaIndicesFor.remove(pilaIndicesFor.size() - 1);
    Typeable tokenCte = new TokenLexemaDistinto("CTE", "1");
    Typeable tSuma = new TercetoSuma(indice, tokenCte);
    new TercetoAsignacion(indice, tSuma);
    
    TercetoSalto ts = new TercetoSalto("BI");       // Crea un salto incondicional al que se debe apuntar a la condicion del for

    Vector<Terceto> t = Terceto.tercetos;
    int desapilado = pilaSaltos.remove(pilaSaltos.size() -1);       // Desapila el salto por falsedad de este FOR que se encuentra en la condicion
    new TercetoLabel();                                             // Crea una etiqueta para apuntar el salto por falsedad anterior
    ((TercetoSalto) t.get(desapilado)).setDirSalto(t.size());       // Apunta el salto por falsedad a la etiqueta recien creada

    int dirCondicion = pilaCondiciones.remove(pilaCondiciones.size() -1);       // 
    ts.setDirSalto(dirCondicion);
}

private void apilarCondicionFor(){
    new TercetoLabel();
    pilaCondiciones.add(Terceto.tercetos.size());
}

private void apilarFor(){
    pilaSaltos.add(Terceto.tercetos.size());
    new TercetoSalto("BF");
}

private void apilarIndiceFor(Terceto t) {
    Typeable indice = t.getParametro1();
    pilaIndicesFor.add(indice);
}

private void iniciarFuncion(Token identificador) {
    // @TODO chekear si esta etiqueta que se crea no está antes del salto incondicion para evitar la ejecucion inicial de la funcion sin ser llamada
    //identificador.setUso(Uso.USO_FUNCION);
    String nombreFuncion = identificador.getLexema();

    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();
    //indiceUltimaFuncion = dirEtFuncion;

    this.etFuncionesMapping.put(nombreFuncion, dirEtFuncion);
    //this.ultimoNombreFuncion = nombreFuncion;
}

private void finalizarFuncion(TypeableToken tt) {
    Token tokenRet, tokenCte;
    tokenRet = new TokenLexemaDistinto("ID", "_RET");
    ((TypeableToken)tokenRet).setTipo(Typeable.TIPO_INT);

    if (tt == null) {
        tokenCte = new TokenLexemaDistinto("CTE", "0");    // Devuelvo 0 por defecto
    } else {
        tokenCte = new TokenLexemaDistinto("CTE", tt.getLexema());    // Devuelvo 0 por defecto
    }

    ((TypeableToken)tokenCte).setTipo(Typeable.TIPO_CTE_ENTERA);
    tokenCte.setUso(Uso.USO_CONSTANTE);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenRet, true);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenCte, true);
    new TercetoAsignacion((Typeable)tokenRet, (Typeable)tokenCte);

    //TypeableToken tokenRetAux = new TokenLexemaDistinto("RET", "0");
    //((TypeableToken)tokenCte).setTipo(Typeable.TIPO_INT);
    //tokenCte.setUso(Uso.USO_RET);
    boolean sobreescribir = true;
    this.anLexico.getTablaSimbolos().addSimbolo((Token)tokenCte, sobreescribir);
    Terceto t = new TercetoRetorno((Typeable)tokenCte);
}

private int instanciarTercetosFuncion() {

    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();
    return dirEtFuncion;
}

private void llamadoFuncion(Token identificador) {
    
    Integer labelIndex = functionLabels.get(identificador.getLexema());

    if (labelIndex != null) {
        TercetoLabel functionLabel = (TercetoLabel)Terceto.tercetos.get(labelIndex);
        new TercetoCall(functionLabel);
    } else {
        // @TODO No tendria que pasar esto, logear error mas adelante
    }
    
    /*
    String nombreFuncion = identificador.getLexema();
    TercetoSalto saltoAFuncion = new TercetoSalto("BF");

    int indexEtiquetaFuncion = this.etFuncionesMapping.get(nombreFuncion);

    saltoAFuncion.setDirSalto(indexEtiquetaFuncion);
    
    int retIndex = Terceto.tercetos.size();
    new TercetoLabel();     // Creo una etiqueta para volver a este punto

    int saltoARetornoIndex = this.retFuncionesMapping.get(nombreFuncion).intValue();
    TercetoSalto saltoARetorno = (TercetoSalto) Terceto.tercetos.get(saltoARetornoIndex);
    saltoARetorno.setDirSalto(retIndex);    // Seteo la direccion de salto del retorno de la funcion para volver a este punto
    */

}

private void nombresDuplicadosCheck(TypeableToken tokenNombreFuncion) {
    // @TODO hacer checkeo por nombre de funcion duplicados
    String nombreFuncion = tokenNombreFuncion.getLexema();
    if (!this.anLexico.getTablaSimbolos().contains(new IdTS(nombreFuncion, Uso.USO_FUNCION))) {
        tokenNombreFuncion.setUso(Uso.USO_FUNCION);
        this.anLexico.getTablaSimbolos().addSimbolo(tokenNombreFuncion, true);
    } else {
        this.eventoError.add("Funcion con nombre " + nombreFuncion + " ya se encuentra declarada", 99, "Semantico", "Error" );
        saltarFuncion = true;
    }
}

private void limpiarVector(int desde, int hasta) {
    int cantidad = hasta - desde;
    for (int i = 1; i <= cantidad ; i++ ) {
        Terceto.tercetos.removeElementAt(Terceto.tercetos.size()-1);
    }
 }

private void crearAmbito(String nombreFuncion) {
    // @TODO para cada variable declarada en la seccion declaracion de la funcion, asignar ambito!!
    // Y asignarle USO_VARIABLE
}

private void apilarParametro(Token identificador) {

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
//#line 643 "Parser.java"
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
case 5:
//#line 44 "gramatica.y"
{ 
    if (!saltarFuncion) {
        finalizarFuncion(null);
        this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" );

        String nombreFuncion = ((TypeableToken) val_peek(3).obj).getLexema();

        crearAmbito(nombreFuncion);
        functionLabels.put(nombreFuncion, labelFuncion);
        /*checkearVisibilidad(nombreFuncion);*/
        /*dentro_de_funcion = false;*/
        /*setAmbitoTercetos(nombreFuncion, false);*/

         

        int indexLabel = Terceto.tercetos.size();
        new TercetoLabel();
        /* Actualizar direccion de salto incondicional del inicio de la funcion*/
        TercetoSalto saltoIncondicionalFinFuncion = (TercetoSalto) Terceto.tercetos.get(saltoFuncion);
        saltoIncondicionalFinFuncion.setDirSalto(indexLabel + 1);   /* Uso mas + 1 porque sino dentro del generar assembler de un salto BI se rompe, ya que siempre espera una posicion adelante y por lo tanto le resta 1, asi qe este + 1 se cancela*/
        nombresDuplicadosCheck((TypeableToken)val_peek(3).obj);
    }

    
}
break;
case 6:
//#line 69 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 7:
//#line 72 "gramatica.y"
{ 
    nombresDuplicadosCheck((TypeableToken)val_peek(3).obj);

    if (!saltarFuncion) {
        this.iniciarFuncion((Token)val_peek(3).obj);
        yyval = val_peek(3);
    }
}
break;
case 8:
//#line 82 "gramatica.y"
{ 
    if (!saltarFuncion) {
        dentroDeFuncion = true;

        /* @TODO implementar un terceto salto BI, que no se para que es*/
        saltoFuncion = Terceto.tercetos.size();
        new TercetoSalto("BI");

        /* Creacion del label de la funcion*/
        Terceto t = new TercetoLabel(); 
        labelFuncion = t.getPosicion();
        /* Creacion del terceto push de la funcion*/
        new TercetoPush();
        /* Eliminar RET basura de la tabla de simbolos, sino se empiezan a acumular*/
        /*this.anLexico.getTablaSimbolos().removeSimbolo("_RET", false);*/
    }
    
}
break;
case 12:
//#line 106 "gramatica.y"
{
    if (!saltarFuncion) {
        this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error");
    }
}
break;
case 13:
//#line 111 "gramatica.y"
{ 
    if (!saltarFuncion) {
        this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
        Vector<ParserVal> v = (Vector<ParserVal>)val_peek(1).obj;                     
        asignarTipo(val_peek(2).ival, v);
        this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error");
    }
}
break;
case 19:
//#line 130 "gramatica.y"
{ yyval.ival = Typeable.TIPO_INT; }
break;
case 20:
//#line 131 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 21:
//#line 134 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 22:
//#line 139 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 25:
//#line 148 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 26:
//#line 149 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 31:
//#line 158 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 33:
//#line 160 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 34:
//#line 163 "gramatica.y"
{ 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.llamadoFuncion((Token) val_peek(3).obj);
    
    Token tt = this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_RET", Uso.USO_VARIABLE));
    yyval = new ParserVal(tt);
}
break;
case 41:
//#line 182 "gramatica.y"
{ 
    this.finalizarFuncion(null); 
}
break;
case 42:
//#line 185 "gramatica.y"
{
    this.finalizarFuncion((TypeableToken) val_peek(3).obj);
}
break;
case 48:
//#line 197 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
break;
case 49:
//#line 201 "gramatica.y"
{empezarElse();}
break;
case 50:
//#line 201 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 51:
//#line 207 "gramatica.y"
{ agregarIfPila(); }
break;
case 52:
//#line 210 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 53:
//#line 216 "gramatica.y"
{apilarFor();}
break;
case 54:
//#line 219 "gramatica.y"
{ 
    ParserVal sentAsig = val_peek(2); 
    apilarIndiceFor((Terceto) sentAsig.obj);
}
break;
case 55:
//#line 223 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 56:
//#line 224 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 57:
//#line 227 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(2));
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
}
break;
case 58:
//#line 234 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 59:
//#line 235 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 61:
//#line 239 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 62:
//#line 240 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 63:
//#line 243 "gramatica.y"
{ apilarCondicionFor(); }
break;
case 65:
//#line 246 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 66:
//#line 251 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj = new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
}
break;
case 67:
//#line 257 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 68:
//#line 261 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 70:
//#line 268 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 71:
//#line 272 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 76:
//#line 282 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 77:
//#line 285 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
case 78:
//#line 290 "gramatica.y"
{ 
    this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
//#line 1077 "Parser.java"
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
