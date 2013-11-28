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
    2,    4,    0,    1,    1,    5,    5,    5,    7,   11,
    8,   10,   10,    6,    6,    9,    9,   15,   15,   15,
   13,   13,   14,   14,    3,    3,    3,    3,   12,   12,
   19,   19,   19,   19,   19,   21,   17,   17,   18,   18,
   18,   18,   22,   22,   20,   20,   20,   20,   20,   24,
   29,   24,   28,   25,   31,   32,   32,   32,   27,   27,
   27,   30,   30,   30,   35,   33,   34,   26,   23,   23,
   23,   36,   36,   36,   37,   37,   37,   37,   16,   16,
};
final static short yylen[] = {                            2,
    0,    0,    5,    0,    2,    1,    4,    8,    5,    0,
    3,    0,    2,    2,    3,    0,    2,    0,    1,    1,
    1,    2,    1,    3,    0,    1,    2,    3,    0,    1,
    1,    3,    2,    2,    3,    4,    1,    2,    1,    1,
    2,    2,    2,    5,    1,    1,    1,    1,    2,    3,
    0,    6,    2,    2,    2,    4,    3,    2,    5,    4,
    3,    3,    2,    1,    0,    2,    3,    4,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         1,
    0,    4,    0,    0,    0,    0,    0,    0,   21,    0,
    2,    5,    6,    0,    0,    0,   37,    0,   45,   46,
   47,   48,    0,    0,   64,    0,   53,    0,    0,    0,
    0,    0,   22,    0,    0,   55,    0,   10,   23,    0,
   27,    0,   38,   49,    0,    0,    0,   54,   31,    0,
   79,    0,    0,   77,   76,    0,    0,    0,   74,   61,
    0,    0,   19,    0,   20,    0,   58,    0,   65,    3,
    0,   12,   15,    0,   28,    0,   34,    0,   33,    0,
   80,    0,    0,    0,   62,    0,    0,    0,   60,    0,
    0,   36,   68,    0,    0,    7,    0,   24,   51,   35,
   32,   78,    0,    0,    0,   72,   73,   59,    0,   17,
   56,   66,    0,   13,   11,    0,   39,   40,    0,    0,
    0,   43,   41,   42,   52,    0,    0,    8,    0,   44,
};
final static short yydgoto[] = {                          1,
    3,    2,   11,   37,   12,   13,   14,   71,   90,   97,
   72,  115,   15,   40,   64,   54,   16,  116,   48,   49,
   55,  118,   56,   19,   20,   21,   22,   23,  119,   27,
   24,   36,   94,   57,   95,   58,   59,
};
final static short yysindex[] = {                         0,
    0,    0, -109,  -24,  -35, -226,  -20, -206,    0,  -21,
    0,    0,    0, -217, -205, -177,    0,   -7,    0,    0,
    0,    0, -197, -120,    0,  -10,    0, -183, -191,   41,
    1,  -10,    0,  -21, -181,    0, -168,    0,    0,   -6,
    0, -155,    0,    0, -120,  -83, -153,    0,    0,   65,
    0,  -10, -156,    0,    0,  -31,   76,    7,    0,    0,
  -14, -213,    0,   82,    0,  -11,    0,   75,    0,    0,
 -123,    0,    0, -122,    0, -112,    0, -132,    0,   52,
    0,  -10,  -10,  -10,    0,  -10,  -10,   91,    0,  114,
 -108,    0,    0,  124,  -10,    0,  -96,    0,    0,    0,
    0,    0,   16,    7,    7,    0,    0,    0,   44,    0,
    0,    0,  -17,    0,    0, -130,    0,    0, -120,  -86,
  -10,    0,    0,    0,    0,   46,   55,    0,  117,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  -90,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -87,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  140,    0,    0,    0,    0,    0,    0,    0,    0, -193,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,  -76,  -34,    0,    0,
    0,  143,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -149,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -113,    0,    0,  -75,    0,    0,    0,
    0,    0,    3,  -19,  -12,    0,    0,    0,  -73,    0,
    0,    0,    0,    0,    0,  -72,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,    0,  169,   93,    0,    0,    0,    0,
    0,    0,  129,    0,    0,  161,  147,    0,  -32,    5,
   12,   78,  -15,    0,    0,  160,    0,    0,    0,    0,
    0,  162,    0,  102,    0,  -18,   77,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   75,   75,   47,   75,   29,   75,   71,   17,   71,   57,
   71,   83,   76,   84,   18,   26,   66,   75,   35,   31,
   43,   69,  121,   69,   71,   69,   88,   18,   70,   52,
   70,   83,   70,   84,   53,   18,   80,   74,   30,   69,
   32,  122,   38,   67,   89,   53,   70,   93,   86,   33,
   17,   44,   73,   87,    8,    9,   18,   18,   83,   39,
   84,   45,   14,   14,  104,  105,  103,   14,   14,   14,
   14,   14,   60,   14,   14,   14,   61,   14,   41,    4,
   62,   75,   43,   68,    5,    6,  125,    7,   71,   18,
    8,    9,  102,   10,   83,  129,   84,   83,   70,   84,
   75,  117,   79,   69,   31,  127,   50,   50,   18,   81,
   70,   50,   50,   50,   50,   50,   85,   50,   50,   50,
  123,   50,   92,  100,    4,   67,    4,   18,  101,    5,
   18,    5,    7,  113,    7,   32,    4,   96,   10,   46,
   10,    5,   98,   57,    7,   99,   57,    4,   57,  108,
   10,   57,    5,    6,  109,    7,  110,   57,    8,    9,
    4,   10,  106,  107,  111,    5,  120,  113,    7,  126,
  128,    8,    9,    4,   10,  130,   25,   77,    5,   26,
   18,    7,   63,   16,   42,   29,    9,   10,   30,  114,
   91,   65,   78,  124,   69,   67,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   75,    0,   75,   75,    0,
   75,    0,   71,   75,   71,   71,    0,   71,   75,   75,
   71,   25,   28,    0,   34,   71,   71,   69,   82,   69,
   69,    0,   69,    0,   70,   69,   70,   70,    0,   70,
   69,   69,   70,    0,   50,   51,    0,   70,   70,   67,
    0,   67,   67,    0,   67,   63,   51,   67,    0,    0,
    0,    0,    0,   67,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,    3,   43,  123,
   45,   43,   45,   45,    3,   40,   32,   59,   40,   40,
   16,   41,   40,   43,   59,   45,   41,   16,   41,   40,
   43,   43,   45,   45,   45,   24,   52,   44,  265,   59,
   61,   59,  260,   41,   59,   45,   59,   59,   42,  256,
   46,   59,   59,   47,  268,  269,   45,   46,   43,  265,
   45,  259,  256,  257,   83,   84,   82,  261,  262,  263,
  264,  265,  256,  267,  268,  269,  268,  271,  256,  257,
   40,  123,   78,  265,  262,  263,  119,  265,  123,   78,
  268,  269,   41,  271,   43,   41,   45,   43,  267,   45,
  256,   97,  256,  123,   40,  121,  256,  257,   97,  266,
  123,  261,  262,  263,  264,  265,   41,  267,  268,  269,
  116,  271,   41,  256,  257,  123,  257,  116,  261,  262,
  119,  262,  265,  264,  265,   61,  257,  261,  271,  260,
  271,  262,  265,  257,  265,  258,  260,  257,  262,   59,
  271,  265,  262,  263,   41,  265,  265,  271,  268,  269,
  257,  271,   86,   87,   41,  262,  123,  264,  265,  256,
  125,  268,  269,  257,  271,   59,  267,  261,  262,  267,
   41,  265,  259,   41,   16,  261,  260,  271,  261,   97,
   62,   31,   46,  116,   35,   34,   95,   -1,   -1,   -1,
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
"$$2 :",
"programa : $$1 declaraciones ejecutable $$2 FIN",
"declaraciones :",
"declaraciones : declaraciones declaracion",
"declaracion : declaracion_simple",
"declaracion : cabecera_funcion BEGIN cuerpo_funcion END",
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' error '}'",
"cabecera_funcion : FUNCTION ID '(' parametro_formal ')'",
"$$3 :",
"cuerpo_funcion : $$3 declaraciones_funcion ejecutable_funcion",
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
"$$4 :",
"sentencia_if : inicio_if THEN bloque ELSE $$4 bloque",
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
"$$5 :",
"comparacion_for : $$5 comparacion",
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

//#line 356 "gramatica.y"

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
//#line 727 "Parser.java"
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
case 2:
//#line 43 "gramatica.y"
{
    setAmbitoTercetos("main", true);
}
break;
case 7:
//#line 54 "gramatica.y"
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
case 8:
//#line 78 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 9:
//#line 81 "gramatica.y"
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
case 10:
//#line 98 "gramatica.y"
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
case 14:
//#line 141 "gramatica.y"
{
    this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error");
}
break;
case 15:
//#line 144 "gramatica.y"
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
case 17:
//#line 161 "gramatica.y"
{
    yyval = val_peek(0);
}
break;
case 18:
//#line 166 "gramatica.y"
{ yyval = new ParserVal(null); }
break;
case 21:
//#line 171 "gramatica.y"
{ yyval.ival = Typeable.TIPO_INT; }
break;
case 22:
//#line 172 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 23:
//#line 175 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 24:
//#line 180 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 27:
//#line 189 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 28:
//#line 190 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 33:
//#line 199 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 35:
//#line 201 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 36:
//#line 204 "gramatica.y"
{ 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Typeable typeableParam = ((Typeable)val_peek(1).obj);
    if (typeableParam != null) {
        TypeableToken tokenAux = ((TypeableToken)typeableParam);
        
        TypeableToken tokenParamReal = (TypeableToken) this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_PARAM", Uso.USO_VARIABLE));
        new TercetoAsignacion((Typeable)tokenParamReal, (Typeable)tokenAux);
        agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    }
    this.llamadoFuncion((Token) val_peek(3).obj);
    
    Token tt = this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_RET", Uso.USO_VARIABLE));
    yyval = new ParserVal(tt);
}
break;
case 43:
//#line 232 "gramatica.y"
{ 
    this.finalizarFuncion(null); 
}
break;
case 44:
//#line 235 "gramatica.y"
{
    this.finalizarFuncion((TypeableToken) val_peek(3).obj);
}
break;
case 50:
//#line 247 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
break;
case 51:
//#line 251 "gramatica.y"
{empezarElse();}
break;
case 52:
//#line 251 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 53:
//#line 257 "gramatica.y"
{ agregarIfPila(); }
break;
case 54:
//#line 260 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 55:
//#line 266 "gramatica.y"
{apilarFor();}
break;
case 56:
//#line 269 "gramatica.y"
{ 
    ParserVal sentAsig = val_peek(2); 
    apilarIndiceFor((Terceto) sentAsig.obj);
}
break;
case 57:
//#line 273 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 58:
//#line 274 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 59:
//#line 277 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(2));
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 60:
//#line 285 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 61:
//#line 286 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 63:
//#line 290 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 64:
//#line 291 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 65:
//#line 294 "gramatica.y"
{ apilarCondicionFor(); }
break;
case 67:
//#line 297 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 68:
//#line 303 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj = new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 69:
//#line 310 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 70:
//#line 315 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 72:
//#line 323 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 73:
//#line 328 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
break;
case 78:
//#line 339 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 79:
//#line 342 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
case 80:
//#line 347 "gramatica.y"
{ 
    this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
//#line 1232 "Parser.java"
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
