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
    0,    1,    1,    3,    3,    3,    5,    6,    6,    4,
    4,    8,    8,   11,   11,   11,    9,    9,   10,   10,
    2,    2,    2,    2,    7,    7,   15,   15,   15,   15,
   15,   17,   13,   13,   14,   14,   14,   14,   18,   18,
   16,   16,   16,   16,   16,   20,   25,   20,   24,   21,
   29,   27,   28,   28,   28,   23,   23,   23,   26,   26,
   26,   30,   22,   19,   19,   19,   31,   31,   31,   32,
   32,   32,   32,   12,   12,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    5,    8,    5,    0,    2,    2,
    3,    0,    2,    0,    1,    1,    1,    2,    1,    3,
    0,    1,    2,    3,    0,    1,    1,    3,    2,    2,
    3,    4,    1,    2,    1,    1,    2,    2,    2,    5,
    1,    1,    1,    1,    2,    3,    0,    6,    2,    2,
    0,    3,    7,    6,    2,    5,    4,    3,    3,    2,
    1,    3,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    0,   17,   51,    0,
    3,    4,    0,    0,    0,   33,    0,   41,   42,   43,
   44,    0,    0,   61,    0,   49,    0,    0,    0,    0,
    0,   18,    0,    1,    8,   19,    0,   23,    0,   34,
   45,    0,    0,    0,   50,   27,    0,   74,    0,    0,
   71,   72,    0,    0,    0,   69,   58,    0,    0,   15,
    0,   16,    0,    0,    0,   52,    0,   11,    0,   24,
    0,   30,    0,   29,    0,   75,    0,    0,    0,   59,
    0,    0,    0,   57,    0,    0,   32,   63,   55,    0,
    0,    9,    0,    0,   35,   36,   20,   47,   31,   28,
   73,    0,    0,    0,   67,   68,   56,    0,   13,    0,
    0,   39,    5,   37,   38,    0,    0,    0,    0,   48,
    0,    0,    0,    6,    0,   40,   53,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   13,   67,   93,   85,   14,   37,
   61,   51,   15,   94,   45,   46,   52,   96,   53,   18,
   19,   20,   21,   22,  116,   26,   23,   66,   33,   54,
   55,   56,
};
final static short yysindex[] = {                         0,
    0, -135,  -24,  -35, -228,  -23, -213,    0,    0, -216,
    0,    0, -199, -195, -172,    0,   24,    0,    0,    0,
    0, -182, -120,    0,  -10,    0, -168, -173,   58,    1,
  -10,    0,  -21,    0,    0,    0,   -2,    0, -155,    0,
    0, -120, -190, -146,    0,    0,   72,    0,  -10, -134,
    0,    0,  -31,   77,   18,    0,    0,  -14, -205,    0,
   90,    0,  -11,  -21, -130,    0,  -89,    0, -127,    0,
 -112,    0, -203,    0,   33,    0,  -10,  -10,  -10,    0,
  -10,  -10,   89,    0,  109, -109,    0,    0,    0,   92,
  -20,    0, -104,  -79,    0,    0,    0,    0,    0,    0,
    0,   49,   18,   18,    0,    0,    0,   41,    0,  -10,
  -10,    0,    0,    0,    0, -120,  -86,    7,   98,    0,
   46,  -10,  113,    0,  133,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  -90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -83,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  140,
    0,    0,    0,    0,    0,    0, -148,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,  -72,  -34,    0,    0,    0,  147,    0,
    0,    0,    0,    0,    0,    0,  -71,    0,    0,    0,
 -102,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -70,    0,    0,    0,    0,    0,    0,
    0,    3,  -19,  -12,    0,    0,    0,  -67,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -113,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  174,  127,    0,    0,    0,    0,  136,    0,
    0,  166,  154,    0,  -29,    6,   13,  104,   -8,    0,
    0,    0,    0,    0,    0,    0,    0,  135,    0,   78,
   27,   43,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         70,
   70,   70,   44,   70,   28,   70,   66,   16,   66,   54,
   66,   78,   71,   79,   17,   25,   30,   70,   65,  111,
   40,   64,   63,   64,   66,   64,   83,   17,   65,   49,
   65,   78,   65,   79,   50,   17,   29,   31,  112,   64,
   75,   69,   32,   62,   84,   50,   65,   88,   16,   78,
   34,   79,   99,    3,   17,   17,   68,  100,    4,   81,
   35,    6,    7,    8,   82,  122,    3,    9,  102,   36,
   72,    4,   95,  101,    6,   78,   42,   79,   40,   17,
    9,   70,   41,   38,    3,   17,  120,   57,   66,    4,
    5,   78,    6,   79,   58,    7,    8,   59,    9,  114,
   70,  118,  119,   64,  103,  104,   17,   10,   10,   74,
   65,   30,   10,   10,   10,   10,   10,   80,   10,   10,
   10,    3,   10,  105,  106,   62,    4,    5,   17,    6,
   87,   76,    7,    8,   90,    9,    3,   97,  123,   43,
   78,    4,   79,   54,    6,   98,   54,  107,   54,  108,
    9,   54,  110,   46,   46,  109,  113,   54,   46,   46,
   46,   46,   46,  117,   46,   46,   46,    3,   46,  121,
  124,  126,    4,  127,   91,    6,   21,    3,    7,    8,
   14,    9,    4,   22,   91,    6,   60,   12,   39,   25,
   26,    9,    7,   92,   86,   62,   73,  115,   89,  125,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   70,    0,   70,   70,    0,
   70,    0,   66,   70,   66,   66,    0,   66,   70,   70,
   66,   24,   27,    0,   64,   66,   66,   64,   77,   64,
   64,    0,   64,    0,   65,   64,   65,   65,    0,   65,
   64,   64,   65,    0,   47,   48,    0,   65,   65,   62,
    0,   62,   62,    0,   62,   60,   48,   62,    0,    0,
    0,    0,    0,   62,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,    2,   43,  123,
   45,   43,   42,   45,    2,   40,   40,   59,   40,   40,
   15,   41,   31,   43,   59,   45,   41,   15,   41,   40,
   43,   43,   45,   45,   45,   23,  265,   61,   59,   59,
   49,   44,  256,   41,   59,   45,   59,   59,   43,   43,
  267,   45,  256,  257,   42,   43,   59,  261,  262,   42,
  260,  265,  268,  269,   47,   59,  257,  271,   77,  265,
  261,  262,   67,   41,  265,   43,  259,   45,   73,   67,
  271,  123,   59,  256,  257,   73,  116,  256,  123,  262,
  263,   43,  265,   45,  268,  268,  269,   40,  271,   94,
  256,  110,  111,  123,   78,   79,   94,  256,  257,  256,
  123,   40,  261,  262,  263,  264,  265,   41,  267,  268,
  269,  257,  271,   81,   82,  123,  262,  263,  116,  265,
   41,  266,  268,  269,  265,  271,  257,  265,   41,  260,
   43,  262,   45,  257,  265,  258,  260,   59,  262,   41,
  271,  265,   61,  256,  257,  265,  261,  271,  261,  262,
  263,  264,  265,  123,  267,  268,  269,  257,  271,  256,
  125,   59,  262,   41,  264,  265,  267,  257,  268,  269,
   41,  271,  262,  267,  264,  265,  259,   41,   15,  261,
  261,  271,  260,   67,   59,   30,   43,   94,   64,  122,
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
"declaracion : cabecera_funcion BEGIN declaraciones_funcion ejecutable_funcion END",
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' error '}'",
"cabecera_funcion : FUNCTION ID '(' parametro_formal ')'",
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
"$$2 :",
"comienzo_for : FOR $$2 condicion_for",
"condicion_for : '(' ID '=' expresion ';' comparacion ')'",
"condicion_for : '(' ID '=' expresion ';' comparacion",
"condicion_for : error condicion_for",
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

//#line 219 "gramatica.y"

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

private void AsignarTipo(int tipo, Vector vars) {
	for(int i = 0; i < vars.size(); i++){
		ParserVal p = (ParserVal) vars.get(i);
		TypeableToken t= (TypeableToken) p.obj;
		if(t.getTipo() == Typeable.TIPO_RECIEN_DECLARADA)
			t.setTipo(tipo);
		else
                    this.eventoError.add("Variable redeclarada " + t.getLexema(), p.ival, "Semantico", "Error" );			
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
Hashtable<String, Integer> etFuncionesMapping = new Hashtable<String, Integer>();
Hashtable<String, Integer> retFuncionesMapping = new Hashtable<String, Integer>();
String ultimoNombreFuncion;

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
    TercetoSalto ts = new TercetoSalto("BI");

    Vector<Terceto> t = Terceto.tercetos;
    int desapilado = pilaSaltos.remove(pilaSaltos.size() -1);
    new TercetoLabel();
    ((TercetoSalto) t.get(desapilado)).setDirSalto(t.size());

    int dirCondicion = pilaCondiciones.remove(pilaCondiciones.size() -1);
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

private void iniciarFuncion(Token identificador) {
    String nombreFuncion = identificador.getLexema();
    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();

    this.etFuncionesMapping.put(nombreFuncion, dirEtFuncion);
    this.ultimoNombreFuncion = nombreFuncion;
}

private void finalizarFuncion() {
    int dirRetFuncion = Terceto.tercetos.size();
    new TercetoSalto("BF");
    this.retFuncionesMapping.put(this.ultimoNombreFuncion, dirRetFuncion);
}

private int instanciarTercetosFuncion() {

    int dirEtFuncion = Terceto.tercetos.size();
    new TercetoLabel();
    return dirEtFuncion;
}

private void llamadoFuncion(Token identificador) {
    
    String nombreFuncion = identificador.getLexema();
    TercetoSalto saltoAFuncion = new TercetoSalto("BF");

    int indexEtiquetaFuncion = this.etFuncionesMapping.get(nombreFuncion);

    saltoAFuncion.setDirSalto(indexEtiquetaFuncion);
    
    int retIndex = Terceto.tercetos.size();
    new TercetoLabel();     // Creo una etiqueta para volver a este punto

    int saltoARetornoIndex = this.retFuncionesMapping.get(nombreFuncion).intValue();
    TercetoSalto saltoARetorno = (TercetoSalto) Terceto.tercetos.get(saltoARetornoIndex);
    saltoARetorno.setDirSalto(retIndex);    // Seteo la direccion de salto del retorno de la funcion para volver a este punto


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
//#line 553 "Parser.java"
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
//#line 43 "gramatica.y"
{ 
    this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
}
break;
case 6:
//#line 46 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 7:
//#line 49 "gramatica.y"
{ 
    this.iniciarFuncion((Token)val_peek(3).obj);
}
break;
case 10:
//#line 58 "gramatica.y"
{this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 11:
//#line 59 "gramatica.y"
{ 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)val_peek(1).obj;					 
    AsignarTipo(val_peek(2).ival, v);
}
break;
case 17:
//#line 75 "gramatica.y"
{ yyval.ival = Typeable.TIPO_int; }
break;
case 18:
//#line 76 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 19:
//#line 79 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 20:
//#line 84 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 23:
//#line 93 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 24:
//#line 94 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 29:
//#line 103 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 31:
//#line 105 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 32:
//#line 108 "gramatica.y"
{ 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.llamadoFuncion((Token) val_peek(3).obj);
}
break;
case 39:
//#line 124 "gramatica.y"
{ this.finalizarFuncion(); }
break;
case 40:
//#line 125 "gramatica.y"
{ this.finalizarFuncion(); }
break;
case 46:
//#line 135 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
break;
case 47:
//#line 139 "gramatica.y"
{empezarElse();}
break;
case 48:
//#line 139 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 49:
//#line 145 "gramatica.y"
{ agregarIfPila(); }
break;
case 50:
//#line 148 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 51:
//#line 154 "gramatica.y"
{apilarCondicionFor();}
break;
case 52:
//#line 154 "gramatica.y"
{apilarFor();}
break;
case 54:
//#line 158 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 55:
//#line 159 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 56:
//#line 162 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
}
break;
case 57:
//#line 166 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 58:
//#line 167 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 60:
//#line 171 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 61:
//#line 172 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 62:
//#line 175 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 63:
//#line 180 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
}
break;
case 64:
//#line 186 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 65:
//#line 190 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 67:
//#line 197 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 68:
//#line 201 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 73:
//#line 211 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 75:
//#line 215 "gramatica.y"
{ this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
//#line 902 "Parser.java"
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
