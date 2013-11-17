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
    0,    1,    1,    3,    3,    3,    5,    6,    8,    8,
    4,    4,    7,    7,   12,   12,   12,   10,   10,   11,
   11,    2,    2,    2,    2,    9,    9,   16,   16,   16,
   16,   16,   18,   14,   14,   15,   15,   15,   15,   19,
   19,   17,   17,   17,   17,   17,   21,   26,   21,   25,
   22,   28,   29,   29,   29,   24,   24,   24,   27,   27,
   27,   32,   30,   31,   23,   20,   20,   20,   33,   33,
   33,   34,   34,   34,   34,   13,   13,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    4,    8,    5,    2,    0,    2,
    2,    3,    0,    2,    0,    1,    1,    1,    2,    1,
    3,    0,    1,    2,    3,    0,    1,    1,    3,    2,
    2,    3,    4,    1,    2,    1,    1,    2,    2,    2,
    5,    1,    1,    1,    1,    2,    3,    0,    6,    2,
    2,    2,    4,    3,    2,    5,    4,    3,    3,    2,
    1,    0,    2,    3,    4,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    0,   18,    0,    0,
    3,    4,    0,    0,    0,   34,    0,   42,   43,   44,
   45,    0,    0,   61,    0,   50,    0,    0,    0,    0,
    0,   19,    0,    0,   52,    1,    9,   20,    0,   24,
    0,   35,   46,    0,    0,    0,   51,   28,    0,   76,
    0,    0,   73,   74,    0,    0,    0,   71,   58,    0,
    0,   16,    0,   17,    0,   55,    0,   62,    0,    0,
   12,    0,   25,    0,   31,    0,   30,    0,   77,    0,
    0,    0,   59,    0,    0,    0,   57,    0,    0,   33,
   65,    0,    0,    5,    0,   10,    8,    0,   36,   37,
   21,   48,   32,   29,   75,    0,    0,    0,   69,   70,
   56,    0,   14,   53,   63,    0,   40,   38,   39,    0,
    0,    0,   49,    0,    0,    6,   41,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   13,   69,   88,   70,   97,   14,
   39,   63,   53,   15,   98,   47,   48,   54,  100,   55,
   18,   19,   20,   21,   22,  120,   26,   23,   35,   92,
   56,   93,   57,   58,
};
final static short yysindex[] = {                         0,
    0,  -89,  -24,  -35, -244,  -27, -224,    0,  -21, -215,
    0,    0, -199, -211, -162,    0,    8,    0,    0,    0,
    0, -187, -120,    0,  -10,    0, -178, -181,   48,    1,
  -10,    0,  -21, -167,    0,    0,    0,    0,   -6,    0,
 -151,    0,    0, -120, -132, -148,    0,    0,   70,    0,
  -10, -150,    0,    0,  -31,   79,   13,    0,    0,  -14,
 -226,    0,   82,    0,    5,    0,   67,    0, -130, -206,
    0, -127,    0, -123,    0, -144,    0,   91,    0,  -10,
  -10,  -10,    0,  -10,  -10,   84,    0,  100, -117,    0,
    0,  109,  -10,    0,  -20,    0,    0, -172,    0,    0,
    0,    0,    0,    0,    0,  -28,   13,   13,    0,    0,
    0,   30,    0,    0,    0,  -10,    0,    0,    0, -120,
  -92,  140,    0,   45,  112,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -95,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -90,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  134,
    0,    0,    0,    0,    0,    0,    0,    0, -188,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,  -81,  -34,    0,    0,    0,
  143,    0,    0,    0,    0,    0,    0,    0,    0,  -75,
    0,    0,    0, -102,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -113,    0,    0,    0,    0,    0,  -74,    0,    0,
    0,    0,    0,    0,    0,    3,  -19,  -12,    0,    0,
    0,  -72,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  174,  120,    0,    0,    0,    0,    0,  130,
    0,    0,  162,  148,    0,  -36,   21,   26,   96,    6,
    0,    0,  161,    0,    0,    0,    0,    0,  163,    0,
  104,    0,   33,   72,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         72,
   72,   72,   46,   72,   28,   72,   68,   74,   68,   54,
   68,   81,   30,   82,   81,   25,   82,   72,   34,  116,
   29,   66,   16,   66,   68,   66,   86,   17,   67,   51,
   67,   32,   67,   31,   52,   42,   65,   72,  117,   66,
   17,    7,    8,   64,   87,   52,   67,   81,   17,   82,
    3,   36,   71,   38,   84,    4,   78,   95,    6,   85,
   37,    7,    8,   91,    9,   16,   43,   11,   11,   17,
   17,   44,   11,   11,   11,   11,   11,   59,   11,   11,
   11,   72,   11,  123,    3,  106,   60,   61,   68,    4,
   99,   95,    6,   40,    3,   17,   42,   67,    9,    4,
    5,   17,    6,   66,   73,    7,    8,   77,    9,   30,
   67,  103,    3,  107,  108,   79,  104,    4,  118,   83,
    6,  122,   90,   17,    3,   64,    9,   31,   75,    4,
   94,  105,    6,   81,  102,   82,    3,  101,    9,   45,
  112,    4,  111,   54,    6,   17,   54,  113,   54,  114,
    9,   54,  121,   47,   47,  109,  110,   54,   47,   47,
   47,   47,   47,  124,   47,   47,   47,    3,   47,  126,
  127,   22,    4,    5,   15,    6,   23,   60,    7,    8,
  125,    9,   81,   13,   82,   26,   27,    7,   41,   96,
   89,   64,   76,  119,   68,   66,  115,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   72,    0,   72,   72,    0,
   72,    0,   68,   72,   68,   68,    0,   68,   72,   72,
   68,   24,   27,    0,   33,   68,   68,   66,   80,   66,
   66,    0,   66,    0,   67,   66,   67,   67,    0,   67,
   66,   66,   67,    0,   49,   50,    0,   67,   67,   64,
    0,   64,   64,    0,   64,   62,   50,   64,    0,    0,
    0,    0,    0,   64,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,   44,   43,  123,
   45,   43,   40,   45,   43,   40,   45,   59,   40,   40,
  265,   41,    2,   43,   59,   45,   41,    2,   41,   40,
   43,  256,   45,   61,   45,   15,   31,   44,   59,   59,
   15,  268,  269,   41,   59,   45,   59,   43,   23,   45,
  257,  267,   59,  265,   42,  262,   51,  264,  265,   47,
  260,  268,  269,   59,  271,   45,   59,  256,  257,   44,
   45,  259,  261,  262,  263,  264,  265,  256,  267,  268,
  269,  123,  271,  120,  257,   80,  268,   40,  123,  262,
   70,  264,  265,  256,  257,   70,   76,  265,  271,  262,
  263,   76,  265,  123,  256,  268,  269,  256,  271,   40,
  123,  256,  257,   81,   82,  266,  261,  262,   98,   41,
  265,  116,   41,   98,  257,  123,  271,   61,  261,  262,
  261,   41,  265,   43,  258,   45,  257,  265,  271,  260,
   41,  262,   59,  257,  265,  120,  260,  265,  262,   41,
  271,  265,  123,  256,  257,   84,   85,  271,  261,  262,
  263,  264,  265,  256,  267,  268,  269,  257,  271,  125,
   59,  267,  262,  263,   41,  265,  267,  259,  268,  269,
   41,  271,   43,   41,   45,  261,  261,  260,   15,   70,
   61,   30,   45,   98,   34,   33,   93,   -1,   -1,   -1,
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
"cuerpo_funcion : declaraciones_funcion ejecutable_funcion",
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
"$$2 :",
"comparacion_for : $$2 comparacion",
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

//#line 273 "gramatica.y"

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
    String nombreFuncion = identificador.getLexema();
    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();

    this.etFuncionesMapping.put(nombreFuncion, dirEtFuncion);
    //this.ultimoNombreFuncion = nombreFuncion;
}

private void finalizarFuncion(TypeableToken tt) {
    Typeable tokenCte;

    if (tt == null) {
        tokenCte = new TokenLexemaDistinto("CTE", "0");    // Devuelvo 0 por defecto
    } else {
        tokenCte = new TokenLexemaDistinto("CTE", tt.getLexema());    // Devuelvo 0 por defecto
    }

    Terceto t = new TercetoRetorno(tokenCte);
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
//#line 601 "Parser.java"
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
    /*Terceto t = new Terceto(new Elemento("ret"),new Elemento("-"),new Elemento("0"),new GenerarRet(admin)); */
    /*Typeable tokenCte = new TokenLexemaDistinto("CTE", "0");    // Devuelvo 0 por defecto*/
    /*Terceto t = new TercetoRetorno(null);*/
    finalizarFuncion(null);

    String nombreFuncion = ((TypeableToken) val_peek(3).obj).getLexema();
    functionLabels.put(nombreFuncion, labelFuncion);

    this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 

    int indexLabel = Terceto.tercetos.size();
    new TercetoLabel();
    /* Actualizar direccion de salto incondicional del inicio de la funcion*/
    TercetoSalto saltoIncondicionalInicio = (TercetoSalto) Terceto.tercetos.get(saltoFuncion);
    saltoIncondicionalInicio.setDirSalto(indexLabel + 1);   /* Uso mas + 1 porque sino dentro del generar assembler de un salto BI se rompe, ya que siempre espera una posicion adelante y por lo tanto le resta 1, asi qe este + 1 se cancela*/

}
break;
case 6:
//#line 62 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 7:
//#line 65 "gramatica.y"
{ 
    this.iniciarFuncion((Token)val_peek(3).obj);
    yyval = val_peek(3);
}
break;
case 8:
//#line 71 "gramatica.y"
{
    dentroDeFuncion = true;

    /* @TODO implementar un terceto salto BI, que no se para que es*/
    saltoFuncion = Terceto.tercetos.size();
    new TercetoSalto("BI");

    /* Creacion del label de la funcion*/
    Terceto t = new TercetoLabel(); 
    labelFuncion = t.getPosicion();
    /* Creacion del terceto push de la funcion*/
    new TercetoPush();
}
break;
case 11:
//#line 90 "gramatica.y"
{this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 12:
//#line 91 "gramatica.y"
{ 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)val_peek(1).obj;					 
    asignarTipo(val_peek(2).ival, v);
}
break;
case 18:
//#line 107 "gramatica.y"
{ yyval.ival = Typeable.TIPO_INT; }
break;
case 19:
//#line 108 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 20:
//#line 111 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 21:
//#line 116 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 24:
//#line 125 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 25:
//#line 126 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 30:
//#line 135 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 32:
//#line 137 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 33:
//#line 140 "gramatica.y"
{ 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.llamadoFuncion((Token) val_peek(3).obj);
}
break;
case 40:
//#line 156 "gramatica.y"
{ 
    this.finalizarFuncion(null); 
}
break;
case 41:
//#line 159 "gramatica.y"
{
    this.finalizarFuncion((TypeableToken) val_peek(3).obj);
}
break;
case 47:
//#line 171 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
break;
case 48:
//#line 175 "gramatica.y"
{empezarElse();}
break;
case 49:
//#line 175 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 50:
//#line 181 "gramatica.y"
{ agregarIfPila(); }
break;
case 51:
//#line 184 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 52:
//#line 190 "gramatica.y"
{apilarFor();}
break;
case 53:
//#line 193 "gramatica.y"
{ 
    ParserVal sentAsig = val_peek(2); 
    apilarIndiceFor((Terceto) sentAsig.obj);
}
break;
case 54:
//#line 197 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 55:
//#line 198 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 56:
//#line 201 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(2));
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
}
break;
case 57:
//#line 208 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 58:
//#line 209 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 60:
//#line 213 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 61:
//#line 214 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 62:
//#line 217 "gramatica.y"
{ apilarCondicionFor(); }
break;
case 64:
//#line 220 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 65:
//#line 225 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj = new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
}
break;
case 66:
//#line 231 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 67:
//#line 235 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 69:
//#line 242 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 70:
//#line 246 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 75:
//#line 256 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 76:
//#line 259 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
case 77:
//#line 264 "gramatica.y"
{ 
    this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(val_peek(0));
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
break;
//#line 1009 "Parser.java"
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
