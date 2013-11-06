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
   15,   15,   15,   15,   19,   24,   19,   23,   20,   28,
   26,   26,   26,   27,   22,   22,   22,   25,   25,   25,
   29,   21,   18,   18,   18,   30,   30,   30,   31,   31,
   31,   31,   11,   11,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    9,    8,    0,    2,    2,    3,
    0,    2,    0,    1,    1,    1,    2,    1,    3,    0,
    1,    2,    3,    0,    1,    1,    3,    2,    2,    3,
    4,    1,    2,    1,    1,    2,    2,    2,    5,    1,
    1,    1,    1,    2,    3,    0,    6,    2,    2,    0,
    3,    4,    3,    5,    5,    4,    3,    3,    2,    1,
    3,    4,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    3,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,    0,   16,    0,    0,
    3,    4,    0,    0,   32,    0,   40,   41,   42,   43,
    0,    0,   60,    0,   48,    0,    0,    0,    0,    0,
   17,    0,    0,    0,    1,   18,    0,   22,    0,   33,
   44,    0,    0,    0,   49,   26,    0,   73,    0,    0,
   70,   71,    0,    0,    0,   68,   57,    0,    0,   14,
    0,   15,    0,    0,   53,    0,   51,   10,    0,   23,
    0,   29,    0,   28,    0,   74,    0,    0,    0,   58,
    0,    0,    0,   56,    0,    0,   31,   62,    0,   52,
   19,   46,   30,   27,   72,    0,    0,    0,   66,   67,
   55,    0,   12,    0,    0,    7,    0,    0,   47,    0,
    0,   54,    0,    8,    0,    0,   34,   35,    6,    0,
   38,    5,   36,   37,    0,    0,   39,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   85,  110,  115,   13,   37,   61,
   51,   14,  116,   45,   46,   52,  118,   53,   17,   18,
   19,   20,   21,  105,   25,   22,   65,   34,   54,   55,
   56,
};
final static short yysindex[] = {                         0,
    0, -102,  -24,  -35, -257,   -8, -228,    0,  -21, -229,
    0,    0, -220, -202,    0,  -11,    0,    0,    0,    0,
 -191, -103,    0,  -10,    0, -185, -184,   45,    1,  -10,
    0, -168, -168, -168,    0,    0,   -7,    0, -153,    0,
    0, -103,  -87, -151,    0,    0,   70,    0,  -10, -150,
    0,    0,  -31,   81,   30,    0,    0,  -20, -210,    0,
   86,    0,   -2,   69,    0, -103,    0,    0, -133,    0,
 -112,    0, -137,    0,   38,    0,  -10,  -10,  -10,    0,
  -10,  -10,   93,    0,  115, -100,    0,    0,  -10,    0,
    0,    0,    0,    0,    0,   35,   30,   30,    0,    0,
    0, -110,    0,    6, -103,    0,  -83,  -10,    0, -156,
   46,    0,  -17,    0,  -80,  -85,    0,    0,    0,  -10,
    0,    0,    0,    0,   90,  117,    0,
};
final static short yyrindex[] = {                         0,
    0,  -84,    0,    0,    0,    0,    0,    0,  -78,    0,
    0,    0,    0,  -82,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  141,    0,
    0,    0,    0,    0,    0,    0, -169,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,  -71,  -34,    0,    0,    0,  148,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    3,  -19,  -12,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -70,
    0,    0,    0,    0,    0,  -69,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  176,   83,    0,    0,    0,  135,    0,    0,
  166,  153,    0,  -32,   13,   48,   82,  -13,    0,    0,
    0,    0,    0,    0,    0,    0,   41,    0,   89,   39,
   57,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
   69,   69,   45,   69,   27,   69,   65,   28,   65,   71,
   65,   78,  107,   79,   15,   24,   63,   69,   33,   44,
   83,   63,  120,   63,   65,   63,   40,   31,   64,   49,
   64,   29,   64,   90,   50,   75,   69,   35,   84,   63,
   78,  121,   79,   61,   36,   50,   64,   41,   78,   16,
   79,   68,   30,   38,    3,   15,   88,    7,    8,    4,
    5,   16,    6,   96,  108,    7,    8,   42,    9,   16,
   57,   81,  109,   66,   67,  104,   82,   78,   95,   79,
   78,   69,   79,   58,   59,   40,    9,    9,   65,   16,
   16,    9,    9,    9,    9,    9,   64,    9,    9,    9,
    3,    9,   70,   63,   74,    4,  125,  113,    6,   29,
   64,    7,    8,   16,    9,   76,   97,   98,   93,    3,
   16,   80,  117,   94,    4,   61,   87,    6,  123,   89,
  126,   91,   78,    9,   79,   45,   45,   99,  100,   45,
   45,   45,   45,   45,   45,   92,   45,   45,   45,  106,
   45,  101,   16,    3,    3,  102,   43,   16,    4,    4,
    5,    6,    6,   16,  103,    7,    8,    9,    9,    3,
  119,    3,  111,   72,    4,  127,    4,    6,  113,    6,
  122,   13,   20,    9,   21,    9,   50,   59,   11,   39,
   24,   25,  114,   86,   62,   73,  112,  124,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   69,    0,   69,   69,    0,
   69,    0,   65,   69,   65,   65,    0,   65,   69,   69,
   65,   23,   26,    0,   32,   65,   65,   63,   77,   63,
   63,    0,   63,    0,   64,   63,   64,   64,    0,   64,
   63,   63,   64,    0,   47,   48,    0,   64,   64,   61,
    0,   61,   61,    0,   61,   60,   48,   61,    0,    0,
    0,    0,    0,   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,  123,   45,   40,   47,   41,  265,   43,   42,
   45,   43,  123,   45,    2,   40,   30,   59,   40,  123,
   41,   41,   40,   43,   59,   45,   14,  256,   41,   40,
   43,   40,   45,   66,   45,   49,   44,  267,   59,   59,
   43,   59,   45,   41,  265,   45,   59,   59,   43,    2,
   45,   59,   61,  256,  257,   43,   59,  268,  269,  262,
  263,   14,  265,   77,   59,  268,  269,  259,  271,   22,
  256,   42,  105,   33,   34,   89,   47,   43,   41,   45,
   43,  123,   45,  268,   40,   73,  256,  257,  123,   42,
   43,  261,  262,  263,  264,  265,  265,  267,  268,  269,
  257,  271,  256,  123,  256,  262,  120,  264,  265,   40,
  123,  268,  269,   66,  271,  266,   78,   79,  256,  257,
   73,   41,  110,  261,  262,  123,   41,  265,  116,   61,
   41,  265,   43,  271,   45,  256,  257,   81,   82,  260,
  261,  262,  263,  264,  265,  258,  267,  268,  269,  260,
  271,   59,  105,  257,  257,   41,  260,  110,  262,  262,
  263,  265,  265,  116,  265,  268,  269,  271,  271,  257,
  125,  257,  256,  261,  262,   59,  262,  265,  264,  265,
  261,   41,  267,  271,  267,  271,  265,  259,   41,   14,
  261,  261,  110,   59,   29,   43,  108,  116,   -1,   -1,
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
"sentencia_for : comienzo_for bloque",
"$$2 :",
"comienzo_for : FOR $$2 condicion_for",
"comienzo_for : FOR '(' condicion_for bloque",
"comienzo_for : FOR error condicion_for",
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

//#line 206 "gramatica.y"

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
//#line 508 "Parser.java"
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
{ 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)val_peek(1).obj;					 
    AsignarTipo(val_peek(2).ival, v);
}
break;
case 16:
//#line 68 "gramatica.y"
{ yyval.ival = Typeable.TIPO_int; }
break;
case 17:
//#line 69 "gramatica.y"
{this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 18:
//#line 72 "gramatica.y"
{ 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 19:
//#line 77 "gramatica.y"
{ 
    Vector<ParserVal> vars = (Vector<ParserVal>) val_peek(2).obj;
    vars.add(clone(val_peek(0)));
    yyval.obj = vars;
}
break;
case 22:
//#line 86 "gramatica.y"
{this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 23:
//#line 87 "gramatica.y"
{this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 28:
//#line 96 "gramatica.y"
{this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 30:
//#line 98 "gramatica.y"
{ this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 31:
//#line 101 "gramatica.y"
{ this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 45:
//#line 125 "gramatica.y"
{ this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 46:
//#line 126 "gramatica.y"
{empezarElse();}
break;
case 47:
//#line 126 "gramatica.y"
{ 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
break;
case 48:
//#line 132 "gramatica.y"
{ agregarIfPila(); }
break;
case 49:
//#line 135 "gramatica.y"
{
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
break;
case 50:
//#line 141 "gramatica.y"
{apilarCondicionFor();}
break;
case 51:
//#line 141 "gramatica.y"
{apilarFor();}
break;
case 52:
//#line 142 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 53:
//#line 143 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 55:
//#line 149 "gramatica.y"
{ 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoPrint((Typeable)val_peek(2).obj);
}
break;
case 56:
//#line 153 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 57:
//#line 154 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 59:
//#line 158 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 60:
//#line 159 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 61:
//#line 162 "gramatica.y"
{
    new TercetoComparacion((Token) val_peek(1).obj, (Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 62:
//#line 167 "gramatica.y"
{ 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    new TercetoAsignacion((Typeable)val_peek(3).obj, (Typeable)val_peek(1).obj);
}
break;
case 63:
//#line 173 "gramatica.y"
{ 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoSuma((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 64:
//#line 177 "gramatica.y"
{ 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    yyval.obj= new TercetoResta((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 66:
//#line 184 "gramatica.y"
{ 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoMultiplicacion((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 67:
//#line 188 "gramatica.y"
{ 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    yyval.obj= new TercetoDivision((Typeable)val_peek(2).obj, (Typeable)val_peek(0).obj);
}
break;
case 72:
//#line 198 "gramatica.y"
{ yyval = val_peek(1); }
break;
case 74:
//#line 202 "gramatica.y"
{ this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
//#line 831 "Parser.java"
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
