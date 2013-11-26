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
import java.util.Iterator;

//#line 29 "Parser.java"




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
    2,    0,    1,    1,    4,    4,    4,    6,   10,    7,
    9,    9,    5,    5,    8,    8,   14,   14,   14,   12,
   12,   13,   13,    3,    3,    3,    3,   11,   11,   18,
   18,   18,   18,   18,   20,   16,   16,   17,   17,   17,
   17,   21,   21,   19,   19,   19,   19,   19,   23,   28,
   23,   27,   24,   30,   31,   31,   31,   26,   26,   26,
   29,   29,   29,   34,   32,   33,   25,   22,   22,   22,
   35,   35,   35,   36,   36,   36,   36,   15,   15,
};
final static short yylen[] = {                            2,
    0,    4,    0,    2,    1,    4,    8,    5,    0,    3,
    0,    2,    2,    3,    0,    2,    0,    1,    1,    1,
    2,    1,    3,    0,    1,    2,    3,    0,    1,    1,
    3,    2,    2,    3,    4,    1,    2,    1,    1,    2,
    2,    2,    5,    1,    1,    1,    1,    2,    3,    0,
    6,    2,    2,    2,    4,    3,    2,    5,    4,    3,
    3,    2,    1,    0,    2,    3,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         1,
    0,    3,    0,    0,    0,    0,    0,    0,   20,    0,
    0,    4,    5,    0,    0,    0,   36,    0,   44,   45,
   46,   47,    0,    0,   63,    0,   52,    0,    0,    0,
    0,    0,   21,    0,    0,   54,    2,    9,   22,    0,
   26,    0,   37,   48,    0,    0,    0,   53,   30,    0,
   78,    0,    0,   76,   75,    0,    0,    0,   73,   60,
    0,    0,   18,    0,   19,    0,   57,    0,   64,    0,
   11,   14,    0,   27,    0,   33,    0,   32,    0,   79,
    0,    0,    0,   61,    0,    0,    0,   59,    0,    0,
   35,   67,    0,    0,    6,    0,   23,   50,   34,   31,
   77,    0,    0,    0,   71,   72,   58,    0,   16,   55,
   65,    0,   12,   10,    0,   38,   39,    0,    0,    0,
   42,   40,   41,   51,    0,    0,    7,    0,   43,
};
final static short yydgoto[] = {                          1,
    3,    2,   11,   12,   13,   14,   70,   89,   96,   71,
  114,   15,   40,   64,   54,   16,  115,   48,   49,   55,
  117,   56,   19,   20,   21,   22,   23,  118,   27,   24,
   36,   93,   57,   94,   58,   59,
};
final static short yysindex[] = {                         0,
    0,    0, -163,  -24,  -35, -250,  -23, -236,    0,  -21,
 -218,    0,    0, -200, -193, -195,    0,   16,    0,    0,
    0,    0, -182, -120,    0,  -10,    0, -176, -183,   51,
    1,  -10,    0,  -21, -169,    0,    0,    0,    0,  -16,
    0, -158,    0,    0, -120,  -73, -155,    0,    0,   63,
    0,  -10, -159,    0,    0,  -31,   68,    6,    0,    0,
  -14, -156,    0,   82,    0,   -4,    0,   69,    0, -132,
    0,    0, -134,    0, -126,    0, -178,    0,    9,    0,
  -10,  -10,  -10,    0,  -10,  -10,   75,    0,   97, -124,
    0,    0,   98,  -10,    0, -147,    0,    0,    0,    0,
    0,   26,    6,    6,    0,    0,    0,   23,    0,    0,
    0,  -27,    0,    0, -206,    0,    0, -120, -108,  -10,
    0,    0,    0,    0,   25,   47,    0,   84,    0,
};
final static short yyrindex[] = {                         0,
    0,    0, -114,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -111,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  116,    0,    0,    0,    0,    0,    0,    0,    0, -102,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,  -95,  -34,    0,    0,
    0,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -113,    0,    0,  -89,    0,    0,    0,    0,
    0,    3,  -19,  -12,    0,    0,    0,  -87,    0,    0,
    0,    0,    0,    0,  -81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  158,   90,    0,    0,    0,    0,    0,
    0,  125,    0,    0,  159,  145,    0,  -37,   20,   18,
   78,    5,    0,    0,  160,    0,    0,    0,    0,    0,
  162,    0,  100,    0,   37,   42,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         74,
   74,   74,   47,   74,   29,   74,   70,   75,   70,   56,
   70,   82,  120,   83,   30,   26,   31,   74,   35,   33,
   18,   68,   17,   68,   70,   68,   87,   73,   69,   52,
   69,  121,   69,   18,   53,   43,   66,   32,   82,   68,
   83,   18,   72,   66,   88,   53,   69,   85,   37,  101,
    4,   82,   86,   83,   92,    5,   79,  112,    7,   38,
   41,    4,   18,   18,   10,   17,    5,    6,   82,    7,
   83,   39,    8,    9,   44,   10,   45,   99,    4,   60,
  124,   74,  100,    5,   61,  102,    7,  128,   70,   82,
   62,   83,   10,    4,   18,   68,   43,   74,    5,    6,
   78,    7,   31,   68,    8,    9,   80,   10,   84,    4,
   69,    8,    9,   18,    5,  116,  112,    7,  103,  104,
    8,    9,   91,   10,  126,   66,  105,  106,   95,   32,
   97,   98,   18,  107,  122,   18,    4,  108,  110,   46,
  109,    5,  129,   56,    7,  119,   56,  125,   56,  127,
   10,   56,   24,   13,   13,   25,   17,   56,   13,   13,
   13,   13,   13,   62,   13,   13,   13,   15,   13,   49,
   49,   28,    8,   42,   49,   49,   49,   49,   49,   29,
   49,   49,   49,    4,   49,  113,   90,   76,    5,   65,
   77,    7,  123,  111,   69,   67,    0,   10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,    0,   74,   74,    0,
   74,    0,   70,   74,   70,   70,    0,   70,   74,   74,
   70,   25,   28,    0,   34,   70,   70,   68,   81,   68,
   68,    0,   68,    0,   69,   68,   69,   69,    0,   69,
   68,   68,   69,    0,   50,   51,    0,   69,   69,   66,
    0,   66,   66,    0,   66,   63,   51,   66,    0,    0,
    0,    0,    0,   66,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,   45,   43,  123,
   45,   43,   40,   45,  265,   40,   40,   59,   40,  256,
    3,   41,    3,   43,   59,   45,   41,   44,   41,   40,
   43,   59,   45,   16,   45,   16,   32,   61,   43,   59,
   45,   24,   59,   41,   59,   45,   59,   42,  267,   41,
  257,   43,   47,   45,   59,  262,   52,  264,  265,  260,
  256,  257,   45,   46,  271,   46,  262,  263,   43,  265,
   45,  265,  268,  269,   59,  271,  259,  256,  257,  256,
  118,  123,  261,  262,  268,   81,  265,   41,  123,   43,
   40,   45,  271,  257,   77,  265,   77,  256,  262,  263,
  256,  265,   40,  123,  268,  269,  266,  271,   41,  257,
  123,  268,  269,   96,  262,   96,  264,  265,   82,   83,
  268,  269,   41,  271,  120,  123,   85,   86,  261,   61,
  265,  258,  115,   59,  115,  118,  257,   41,   41,  260,
  265,  262,   59,  257,  265,  123,  260,  256,  262,  125,
  271,  265,  267,  256,  257,  267,   41,  271,  261,  262,
  263,  264,  265,  259,  267,  268,  269,   41,  271,  256,
  257,  261,  260,   16,  261,  262,  263,  264,  265,  261,
  267,  268,  269,  257,  271,   96,   62,  261,  262,   31,
   46,  265,  115,   94,   35,   34,   -1,  271,   -1,   -1,
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
"$$1 :",
"programa : $$1 declaraciones ejecutable FIN",
"declaraciones :",
"declaraciones : declaraciones declaracion",
"declaracion : declaracion_simple",
"declaracion : cabecera_funcion BEGIN cuerpo_funcion END",
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' error '}'",
"cabecera_funcion : FUNCTION ID '(' parametro_formal ')'",
"$$2 :",
"cuerpo_funcion : $$2 declaraciones_funcion ejecutable_funcion",
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
"$$3 :",
"sentencia_if : inicio_if THEN bloque ELSE $$3 bloque",
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
"$$4 :",
"comparacion_for : $$4 comparacion",
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

//#line 353 "gramatica.y"

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
    TablaSimbolos tablaSimbolos = this.anLexico.getTablaSimbolos();
    for(int i = 0; i < vars.size(); i++){
        ParserVal p = (ParserVal) vars.get(i);
        TypeableToken t = (TypeableToken) p.obj;
        if(t.getTipo() == Typeable.TIPO_RECIEN_DECLARADA) {
            String ambito = getNombreAmbitoActual();            // Obtengo el ambito actual para asignarle a un token recien declarada
            t.setTipo(tipo);
            boolean sobreescribir = true;
            IdTS oldId = new IdTS(t.getLexema(), t.getUso());   // Identificador del token antes de modificarle la key
            t.setAmbito(ambito);                                // Seteo nuevo ambito
            tablaSimbolos.removeSimbolo(oldId);                 // Remuevo el viejo identificador de la tabla de simbolos
            tablaSimbolos.addSimbolo(t, sobreescribir);         // Agrego usando el nuevo identificador
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
Vector<Integer> tercetosAmbito = new Vector<Integer>();

Hashtable<String, Integer> etFuncionesMapping = new Hashtable<String, Integer>();
Hashtable<String, Integer> retFuncionesMapping = new Hashtable<String, Integer>();

Hashtable<String, Integer> functionLabels = new Hashtable<String, Integer>();

boolean dentroDeFuncion = false;
String nombreFuncionActual = "none";
String nombreParametroFormalActual = null;
Vector<String> declaradasFuncion = new Vector<String>();
Vector<String> visibles = new Vector<String>();
Vector<Integer> tercetosFuncion = new Vector<Integer>();
int labelFuncion;
int saltoFuncion;


private void agregarIfPila() {
    pilaSaltos.add(Terceto.tercetos.size());                        // Agregar a la Pila de saltos
    new TercetoSalto("BF");                                         // Crear el Terceto
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}

private void eliminarIfPila() { //termina IF sin else
    Vector<Terceto> t = Terceto.tercetos;
    int indiceDesapilar = pilaSaltos.get(pilaSaltos.size()-1);      // Guardo indice a desapilar (Seria el ultimo)
    pilaSaltos.remove(pilaSaltos.size() -1);                        // Remuevo el ultimo de la pila
    new TercetoLabel();                                             // Se crea la etiqueta
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    ((TercetoSalto) t.get(indiceDesapilar)).setDirSalto(t.size());  // 
}

private void empezarElse() {
    int posB = Terceto.tercetos.size();
    new TercetoSalto("BI");
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
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
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    
    TercetoSalto ts = new TercetoSalto("BI");       // Crea un salto incondicional al que se debe apuntar a la condicion del for
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    Vector<Terceto> t = Terceto.tercetos;
    int desapilado = pilaSaltos.remove(pilaSaltos.size() -1);       // Desapila el salto por falsedad de este FOR que se encuentra en la condicion
    new TercetoLabel();                                             // Crea una etiqueta para apuntar el salto por falsedad anterior
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    ((TercetoSalto) t.get(desapilado)).setDirSalto(t.size());       // Apunta el salto por falsedad a la etiqueta recien creada

    int dirCondicion = pilaCondiciones.remove(pilaCondiciones.size() -1);       // 
    ts.setDirSalto(dirCondicion);
}

private void apilarCondicionFor(){
    new TercetoLabel();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    pilaCondiciones.add(Terceto.tercetos.size());
}

private void apilarFor(){
    pilaSaltos.add(Terceto.tercetos.size());
    new TercetoSalto("BF");
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
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
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
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
        tokenCte = new TokenLexemaDistinto("CTE", tt.getLexema());    // Devuelvo otra cosa
    }

    ((TypeableToken)tokenCte).setTipo(Typeable.TIPO_CTE_ENTERA);
    tokenCte.setUso(Uso.USO_CONSTANTE);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenRet, true);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenCte, true);
    new TercetoAsignacion((Typeable)tokenRet, (Typeable)tokenCte);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    //TypeableToken tokenRetAux = new TokenLexemaDistinto("RET", "0");
    //((TypeableToken)tokenCte).setTipo(Typeable.TIPO_INT);
    //tokenCte.setUso(Uso.USO_RET);
    boolean sobreescribir = true;
    this.anLexico.getTablaSimbolos().addSimbolo((Token)tokenCte, sobreescribir);
    Terceto t = new TercetoRetorno((Typeable)tokenCte);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}

private int instanciarTercetosFuncion() {

    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    return dirEtFuncion;
}

private void llamadoFuncion(Token identificador) {
    
    Integer labelIndex = functionLabels.get(identificador.getLexema());


    if (labelIndex != null) {
        TercetoLabel functionLabel = (TercetoLabel)Terceto.tercetos.get(labelIndex);
        new TercetoCall(functionLabel);
        agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    } else {
        // @TODO No tendria que pasar esto, logear error mas adelante
    }

}

private void nombresDuplicadosCheck(TypeableToken tokenNombreFuncion) {
    // @TODO hacer checkeo por nombre de funcion duplicados
    nombreFuncionActual = tokenNombreFuncion.getLexema();
    if (!this.anLexico.getTablaSimbolos().contains(new IdTS(nombreFuncionActual, Uso.USO_FUNCION))) {
        tokenNombreFuncion.setUso(Uso.USO_FUNCION);
        this.anLexico.getTablaSimbolos().addSimbolo(tokenNombreFuncion, true);
    } else {
        this.eventoError.add("Funcion con nombre " + nombreFuncionActual + " ya se encuentra declarada", 99, "Semantico", "Error" );
    }
}

private void limpiarVector(int desde, int hasta) {
    int cantidad = hasta - desde;
    for (int i = 1; i <= cantidad ; i++ ) {
        Terceto.tercetos.removeElementAt(Terceto.tercetos.size()-1);
    }
 }

private void checkearVisibilidad(String nombreFuncion) {

}

private void crearAmbito(String nombreFuncion) {
    // @TODO para cada variable declarada en la seccion declaracion de la funcion, asignar ambito en TABLA DE SIMBOLOS
    // Y asignarle USO_VARIABLE
    TablaSimbolos tablaSimbolos = this.anLexico.getTablaSimbolos();

    for (int i=0; i<declaradasFuncion.size(); i++) {
        String nombreVar = (String) declaradasFuncion.elementAt(i);
        Token t = tablaSimbolos.getSimbolo(new IdTS(nombreVar, Uso.USO_VARIABLE));

        if (t != null) {    // Name mangling: actualizar el nombre de las variables con el respectivo ambito
            t.setAmbito('_' + nombreFuncion);
            tablaSimbolos.removeSimbolo(new IdTS(nombreVar, Uso.USO_VARIABLE));
            tablaSimbolos.addSimbolo(t, true);
        }
    }

}

private void setAmbitoTercetos(String ambito, boolean main) {
    Iterator it = tercetosAmbito.iterator();
    while (it.hasNext()) {
        Integer index = (Integer)it.next();
        Terceto t = Terceto.tercetos.elementAt(index.intValue());
        Typeable param1 = t.getParametro1();
        Typeable param2 = t.getParametro2();

        if (param1 != null && param1 instanceof TypeableToken) {
            Token tokenParam1 = (Token) param1;
            String posibleDeclarada = tokenParam1.getLexema();
            if (!main && declaradasFuncion.contains(posibleDeclarada.replace("_main", "_" + ambito)) && 
                    !posibleDeclarada.contains("_" + ambito)) {
                //if (!posibleDeclarada.contains("_")) {
                    tokenParam1.setAmbito("_" + ambito);
                //}
            }

            if (main && !posibleDeclarada.contains("_")) {
                tokenParam1.setAmbito("_main");
            }
        }
        
        if (param2 != null && param2 instanceof TypeableToken) {
            Token tokenParam2 = (Token) param2;
            String posibleDeclarada = tokenParam2.getLexema();
            if (!main && declaradasFuncion.contains(posibleDeclarada.replace("_main", "_" + ambito)) && 
                    !posibleDeclarada.contains("_" + ambito)) {
                //if (!posibleDeclarada.contains("_")) {
                    tokenParam2.setAmbito("_" + ambito);
                //}
            }

            if (main && !posibleDeclarada.contains("_")) {
                tokenParam2.setAmbito("_main");
            }
        }

    }
}

private String getNombreAmbitoActual() {
    String ambito = "_main"; 
    if (dentroDeFuncion) {
        ambito = "_" + nombreFuncionActual;
    }

    return ambito;
}

private void agregarTercetoAAmbito(int indexTerceto) {
    if (dentroDeFuncion) {
        tercetosFuncion.add(indexTerceto);
    } else {
        tercetosAmbito.add(indexTerceto);
    }
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
//#line 726 "Parser.java"
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
case 1:
//#line 37 "gramatica.y"
{
    TypeableToken tokenParamReal;
    tokenParamReal = new TokenLexemaDistinto("ID", "_PARAM");
    ((TypeableToken)tokenParamReal).setTipo(Typeable.TIPO_INT);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenParamReal, true);

}
break;
case 6:
//#line 52 "gramatica.y"
{ 
    finalizarFuncion(null);
    this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" );

    String nombreFuncion = ((TypeableToken) val_peek(3).obj).getLexema();

    /*crearAmbito(nombreFuncion);*/

    functionLabels.put(nombreFuncion, labelFuncion);
    checkearVisibilidad(nombreFuncion);
    
    Vector<Integer> tercetosAmbitoAux = tercetosAmbito;
    tercetosAmbito = tercetosFuncion;
    setAmbitoTercetos(nombreFuncion, false);
    tercetosAmbito = tercetosAmbito;

    int indexLabel = Terceto.tercetos.size();
    new TercetoLabel();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    /* Actualizar direccion de salto incondicional del inicio de la funcion*/
    TercetoSalto saltoIncondicionalFinFuncion = (TercetoSalto) Terceto.tercetos.get(saltoFuncion);
    saltoIncondicionalFinFuncion.setDirSalto(indexLabel + 1);   /* Uso mas + 1 porque sino dentro del generar assembler de un salto BI se rompe, ya que siempre espera una posicion adelante y por lo tanto le resta 1, asi qe este + 1 se cancela*/
    dentroDeFuncion = false;
}
break;
case 7:
//#line 76 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 8:
//#line 79 "gramatica.y"
{ 
    nombresDuplicadosCheck((TypeableToken)val_peek(3).obj);

    this.iniciarFuncion((Token)val_peek(3).obj);
    dentroDeFuncion = true;
    declaradasFuncion.clear();

    /* Crear copia del parametro*/
    TypeableToken paramFormal = (TypeableToken)val_peek(1).obj;
    if (paramFormal != null) {
        nombreParametroFormalActual = ((Token)val_peek(1).obj).getLexema();
    }
    
    yyval = val_peek(3);
}
break;
case 9:
//#line 96 "gramatica.y"
{ 

    /* @TODO implementar un terceto salto BI, que no se para que es*/
    saltoFuncion = Terceto.tercetos.size();
    new TercetoSalto("BI");
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);


    /* Creacion del label de la funcion*/
    Terceto t = new TercetoLabel(); 
    labelFuncion = t.getPosicion();
    /* Creacion del terceto push de la funcion*/
    new TercetoPush();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    /* Eliminar RET basura de la tabla de simbolos, sino se empiezan a acumular*/
    /*this.anLexico.getTablaSimbolos().removeSimbolo("_RET", false);*/

    TablaSimbolos tablaSimbolos = this.anLexico.getTablaSimbolos();
    if (nombreParametroFormalActual != null) {
        TypeableToken paramFormal = (TypeableToken) tablaSimbolos.getSimbolo(new IdTS(nombreParametroFormalActual, Uso.USO_VARIABLE));
        /*if (paramFormal != null) {*/
            TypeableToken paramReal = (TypeableToken) this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_PARAM", Uso.USO_VARIABLE));
            /*if (paramReal != null) {*/
                Vector<ParserVal> v = new Vector<ParserVal>();      /* Solo para asignarTipo a este parametro, tambien se setea ambito dentro, es una replicacion de las declaraciones de variables, pero para el parametro de una funcion*/
                ParserVal p1 = new ParserVal(paramFormal);
                v.add(clone(p1));
                asignarTipo(Typeable.TIPO_INT, v);      /* Como siempre son variables tipo INT*/
                new TercetoAsignacion(paramFormal, paramReal);
                agregarTercetoAAmbito(Terceto.tercetos.size()-1);
                declaradasFuncion.add(paramFormal.getLexema());
            /*}*/
        /*}*/
    }
    nombreParametroFormalActual = null;
    
    
}
break;
case 13:
//#line 139 "gramatica.y"
{
    this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error");
}
break;
case 14:
//#line 142 "gramatica.y"
{ 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)val_peek(1).obj;                    

    asignarTipo(val_peek(2).ival, v);
    if (dentroDeFuncion) {
        for (int i=0; i<v.size(); i++) {
            ParserVal p= (ParserVal) ((Vector) v).elementAt(i);
            String nombreVar = ((Token) p.obj).getLexema();
            declaradasFuncion.add(nombreVar);
        }
    }
    
}
break;
case 16:
//#line 159 "gramatica.y"
{
    yyval = val_peek(0);
}
break;
case 20:
//#line 169 "gramatica.y"
{ yyval.ival = Typeable.TIPO_INT; }
break;
case 21:
//#line 170 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 22:
//#line 173 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 23:
//#line 178 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 26:
//#line 187 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 27:
//#line 188 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 32:
//#line 197 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 34:
//#line 199 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 35:
//#line 202 "gramatica.y"
{ 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    if (val_peek(1).obj != null) {
        TypeableToken tokenAux = ((TypeableToken)val_peek(1).obj);
        
        TypeableToken tokenParamReal = (TypeableToken) this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_PARAM", Uso.USO_VARIABLE));
        new TercetoAsignacion((Typeable)tokenParamReal, (Typeable)tokenAux);
        agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    }
    this.llamadoFuncion((Token) val_peek(3).obj);
    
    Token tt = this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_RET", Uso.USO_VARIABLE));
    yyval = new ParserVal(tt);
}
break;
case 42:
//#line 229 "gramatica.y"
{ 
    this.finalizarFuncion(null); 
}
break;
case 43:
//#line 232 "gramatica.y"
{
    this.finalizarFuncion((TypeableToken) val_peek(3).obj);
}
break;
case 49:
//#line 244 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
break;
case 50:
//#line 248 "gramatica.y"
{empezarElse();}
break;
case 51:
//#line 248 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 52:
//#line 254 "gramatica.y"
{ agregarIfPila(); }
break;
case 53:
//#line 257 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 54:
//#line 263 "gramatica.y"
{apilarFor();}
break;
case 55:
//#line 266 "gramatica.y"
{ 
    ParserVal sentAsig = val_peek(2); 
    apilarIndiceFor((Terceto) sentAsig.obj);
}
break;
case 56:
//#line 270 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 57:
//#line 271 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 58:
//#line 274 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(2));
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 59:
//#line 282 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 60:
//#line 283 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 62:
//#line 287 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 63:
//#line 288 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 64:
//#line 291 "gramatica.y"
{ apilarCondicionFor(); }
break;
case 66:
//#line 294 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 67:
//#line 300 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj = new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 68:
//#line 307 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 69:
//#line 312 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 71:
//#line 320 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 72:
//#line 325 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 77:
//#line 336 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 78:
//#line 339 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
case 79:
//#line 344 "gramatica.y"
{ 
    this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
//#line 1220 "Parser.java"
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
