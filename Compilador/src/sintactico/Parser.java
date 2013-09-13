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

//#line 24 "Parser.java"




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
    0,    1,    1,    3,    3,    6,    6,    4,    4,    5,
    5,   10,   10,    8,    8,    9,    9,    2,    7,    7,
   13,   13,   13,   15,   11,   11,   12,   12,   12,   12,
   16,   16,   14,   14,   14,   14,   14,   18,   18,   19,
   19,   19,   23,   21,   21,   21,   22,   22,   22,   24,
   20,   17,   17,   17,   25,   25,   25,   26,   26,   26,
   26,   27,   27,
};
final static short yylen[] = {                            2,
    3,    0,    2,    1,    9,    0,    2,    2,    3,    0,
    2,    0,    1,    1,    1,    1,    3,    1,    0,    1,
    1,    3,    2,    5,    1,    2,    1,    1,    2,    2,
    2,    5,    1,    1,    1,    1,    1,    4,    6,    5,
    4,    3,    5,    5,    4,    3,    3,    2,    1,    3,
    4,    3,    3,    1,    3,    3,    1,    1,    1,    1,
    3,    1,    2,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    0,   15,   14,    0,    0,
    3,    4,    0,    0,   25,   37,   33,   34,   35,   36,
   49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,   16,    0,   26,   58,   62,   60,    0,    0,    0,
    0,    0,   57,   59,    0,   46,    0,    0,   13,    0,
    0,    0,   42,    0,    9,    0,    0,   63,    0,    0,
    0,   47,    0,    0,    0,    0,   21,    0,   45,    0,
    0,    0,   51,    0,    0,   41,   17,   61,    0,    0,
    0,   55,   56,   23,    0,    0,   44,    0,   11,   24,
    0,   40,   22,   39,    6,    0,    0,   43,    0,    7,
    0,    0,   27,   28,    0,   31,    5,   29,   30,    0,
    0,   32,
};
final static short yydgoto[] = {                          1,
    2,   10,   11,   12,   70,   97,  101,   13,   33,   50,
   14,  102,   66,   67,   16,  104,   40,   17,   18,   19,
   20,   23,   53,   41,   42,   43,   44,
};
final static short yysindex[] = {                         0,
    0, -204,  -34,  -28, -215,  -30,    0,    0,  -29, -210,
    0,    0, -199, -211,    0,    0,    0,    0,    0,    0,
    0,  -31, -190, -177, -196,   51, -176,  -31, -170, -170,
    0,    0,   -7,    0,    0,    0,    0,  -31, -165,  -42,
   64,   41,    0,    0, -118,    0,  -18, -213,    0,   65,
  -21,   46,    0,    8,    0, -156,   49,    0,  -31,  -31,
  -31,    0,  -31,  -31, -117, -148,    0,   52,    0,   74,
 -149,   58,    0,  -31, -118,    0,    0,    0,   -1,   41,
   41,    0,    0,    0,  -89, -118,    0,   -5,    0,    0,
  -16,    0,    0,    0,    0,  -31, -194,    0,  -19,    0,
   -6, -184,    0,    0,  -31,    0,    0,    0,    0,   57,
   61,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -146,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   81,    0,    0,    0,
    0,    0, -105,    0,    0,    0,    0,    0,    0,    0,
 -136,  -41,    0,    0,    0,    0,    0,   83,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -90,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    4,  -26,
  -11,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    3,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   28,    0,    0,    0,   82,    0,    0,
   67,    0,  -47,   11,    0,   31,  -12,    0,    0,    0,
    0,    0,  104,   39,   25,   40,    0,
};
final static int YYTABLESIZE=279;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   60,   54,   61,   54,   65,   22,   76,   84,   38,   27,
   30,   25,   15,   39,   52,   51,   52,   54,   52,    8,
  105,   60,   68,   61,   34,   57,   60,   92,   61,   53,
   28,   53,   52,   53,   38,   93,   56,   73,   94,  106,
   69,   60,   96,   61,   50,    3,   79,   53,   75,   26,
    4,   55,    3,    6,    7,    8,   31,    4,    5,    9,
    6,   91,    3,    7,    8,   32,    9,    4,   45,   99,
    6,   47,    3,    7,    8,   15,    9,    4,   46,   99,
    6,   54,   63,   54,   80,   81,    9,   64,   49,   78,
   48,   60,  110,   61,   52,   34,   52,  111,   52,   60,
   58,   61,   82,   83,   62,   72,   74,  103,   77,   86,
   87,   53,  108,   53,   88,   89,   90,   95,  107,  112,
   18,   12,   48,   10,  100,   19,   50,   20,   50,   71,
   65,   85,  109,   54,   98,    0,    0,    0,    3,    3,
    0,    0,    0,    4,    4,    0,    6,    6,    0,    0,
    0,    8,    9,    9,    0,    0,    8,    8,    8,    8,
    0,    0,    8,    8,    0,    8,   38,    3,    0,    0,
    0,   38,    4,   38,   38,    6,   38,    0,    0,    0,
   38,    9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   54,   54,   54,    0,    0,
   54,   21,   54,   54,    0,   54,   29,   59,   54,   54,
   52,   52,   52,   35,   36,   52,   37,   52,   52,   24,
   52,    0,    0,   52,   52,   53,   53,   53,    0,    0,
   53,    0,   53,   53,    0,   53,    0,    0,   53,   53,
   50,   50,   50,    0,    3,   50,    0,   50,   50,    4,
   50,    0,    6,    0,   50,    0,    0,    0,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   43,   43,   45,   45,  123,   40,   54,  125,   40,   40,
   40,   40,    2,   45,   41,   28,   43,   59,   45,  125,
   40,   43,   41,   45,   14,   38,   43,   75,   45,   41,
   61,   43,   59,   45,  125,  125,   44,   59,   86,   59,
   59,   43,   59,   45,   41,  257,   59,   59,   41,  265,
  262,   59,  257,  265,  268,  269,  267,  262,  263,  271,
  265,   74,  257,  268,  269,  265,  271,  262,  259,  264,
  265,  268,  257,  268,  269,   65,  271,  262,  256,  264,
  265,  123,   42,  125,   60,   61,  271,   47,  265,   41,
   40,   43,  105,   45,  265,   85,  123,   41,  125,   43,
  266,   45,   63,   64,   41,   41,   61,   97,  265,  258,
   59,  123,  102,  125,   41,  265,   59,  123,  125,   59,
  267,   41,  259,   41,   97,  125,  123,  125,  125,   48,
  123,   65,  102,   30,   96,   -1,   -1,   -1,  257,  257,
   -1,   -1,   -1,  262,  262,   -1,  265,  265,   -1,   -1,
   -1,  257,  271,  271,   -1,   -1,  262,  263,  264,  265,
   -1,   -1,  268,  269,   -1,  271,  257,  257,   -1,   -1,
   -1,  262,  262,  264,  265,  265,  267,   -1,   -1,   -1,
  271,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,
  262,  256,  264,  265,   -1,  267,  256,  270,  270,  271,
  257,  258,  259,  265,  266,  262,  268,  264,  265,  268,
  267,   -1,   -1,  270,  271,  257,  258,  259,   -1,   -1,
  262,   -1,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  257,  258,  259,   -1,  257,  262,   -1,  264,  265,  262,
  267,   -1,  265,   -1,  271,   -1,   -1,   -1,  271,
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
"declaracion : FUNCTION ID '(' parametro_formal ')' '{' declaraciones_funcion ejecutable_funcion '}'",
"declaraciones_funcion :",
"declaraciones_funcion : declaraciones_funcion declaracion_simple",
"declaracion_simple : tipo lista_variables",
"declaracion_simple : tipo lista_variables ';'",
"parametro_formal :",
"parametro_formal : tipo ID",
"parametro_real :",
"parametro_real : ID",
"tipo : INT",
"tipo : STRING",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"ejecutable : sentencias",
"ejecutable_funcion :",
"ejecutable_funcion : sentencias_funcion",
"bloque : sentencia",
"bloque : '{' sentencias '}'",
"bloque : '{' '}'",
"llamado_funcion : ID '(' parametro_real ')' ';'",
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
"sentencia : llamado_funcion",
"sentencia_if : IF condicion THEN bloque",
"sentencia_if : IF condicion THEN bloque ELSE bloque",
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
"factor : STRING",
"factor : '(' expresion ')'",
"constante : CTE",
"constante : '-' CTE",
};

//#line 152 "gramatica.y"

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
//#line 419 "Parser.java"
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
case 8:
//#line 47 "gramatica.y"
{this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
break;
case 24:
//#line 79 "gramatica.y"
{ this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 38:
//#line 103 "gramatica.y"
{ this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 39:
//#line 104 "gramatica.y"
{ this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 40:
//#line 107 "gramatica.y"
{ this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 41:
//#line 108 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 42:
//#line 109 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 44:
//#line 115 "gramatica.y"
{ this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 45:
//#line 116 "gramatica.y"
{ this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 46:
//#line 117 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 48:
//#line 121 "gramatica.y"
{ this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 49:
//#line 122 "gramatica.y"
{ this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
break;
case 51:
//#line 128 "gramatica.y"
{ this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 52:
//#line 131 "gramatica.y"
{ this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 53:
//#line 132 "gramatica.y"
{ this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 55:
//#line 136 "gramatica.y"
{ this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
case 56:
//#line 137 "gramatica.y"
{ this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); }
break;
//#line 636 "Parser.java"
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
