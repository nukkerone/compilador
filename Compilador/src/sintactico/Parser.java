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




package sintactico;

//#line 2 "gramatica.y"
import lexico.*; 
import java.util.Hashtable;

//#line 21 "Parser.java"




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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    4,    5,    5,    2,    2,
    6,    6,    7,    7,    7,    7,   13,   13,   14,   14,
   15,   15,    8,    8,    8,   16,   10,    9,    9,   17,
   11,   12,   12,   12,   12,   18,   18,   18,   19,   19,
   19,   20,   20,   20,
};
final static short yylen[] = {                            2,
    2,    0,    2,    3,    1,    1,    1,    3,    1,    2,
    1,    1,    1,    1,    1,    1,    3,    2,    1,    1,
    1,    1,    5,    3,    3,    3,    5,    4,    3,    3,
    3,    5,    4,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    3,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    6,    5,    0,    0,    3,
    0,    9,   11,   12,   13,   14,   15,   16,    0,    0,
    0,    0,    0,    0,   10,    7,    0,   42,   43,    0,
    0,    0,    0,   41,    0,   22,    0,    0,    0,   24,
   35,    0,    0,   20,   25,   19,   31,    4,    0,    0,
   26,    0,    0,    0,    0,    0,   18,    0,    0,    0,
   28,    8,   44,    0,    0,    0,   39,   40,   17,   23,
   27,   32,
};
final static short yydgoto[] = {                          1,
    2,    9,   10,   11,   27,   12,   13,   14,   15,   16,
   17,   18,   46,   39,   40,   20,   31,   32,   33,   34,
};
final static short yysindex[] = {                         0,
    0, -233,  -31,  -40,  -46,    0,    0,  -31, -190,    0,
 -243,    0,    0,    0,    0,    0,    0,    0,  -35, -120,
   -7, -228,  -35, -120,    0,    0,  -26,    0,    0,  -35,
   14,  -41,  -19,    0, -118,    0,    0,    0, -200,    0,
    0,   18,  -29,    0,    0,    0,    0,    0, -204,   28,
    0,  -35,  -35,  -35,  -35,  -35,    0, -108, -120,    6,
    0,    0,    0,   -6,  -19,  -19,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,   31,   41,    0,    0,
    0,   43,   53,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   63,
    0,    0,    0,   33,   11,   21,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,   42,    0,    0,    0,   -1,  -14,  -12,    0,    0,
    0,    0,   56,  -11,    0,   70,    0,   -3,  -28,   -5,
};
final static int YYTABLESIZE=334;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   38,   53,   35,   54,   30,   37,   57,   25,   19,   44,
   36,   45,   47,   53,   23,   54,   69,   49,   36,   43,
   37,   26,   55,    3,   65,   66,   50,   56,    4,   61,
   11,    5,   48,   41,    6,    7,   53,    8,   54,   42,
   21,   38,   34,   38,   44,   38,   70,   71,   64,   67,
   68,   36,   29,   36,   51,   36,   25,   59,   60,   38,
   62,   37,   33,   37,   72,   37,    3,    1,   63,   36,
   53,    4,   54,   30,    5,   38,   58,   24,    0,   37,
    8,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   36,    3,    0,    3,    0,
    0,    4,    0,    4,    5,   37,    5,    0,    3,    0,
    8,    0,    8,    4,    0,   11,    5,    0,    0,    0,
    0,    0,    8,    0,    0,   21,    0,   34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   21,   52,   28,
   29,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   38,   38,    0,
    0,    0,   38,    0,    0,   38,    0,   36,   36,    0,
   38,   38,   36,    0,    0,   36,    0,   37,   37,    0,
   36,   36,   37,    0,    0,   37,    0,   11,   20,    0,
   37,   37,   11,    0,    0,   11,    0,   21,   19,   34,
   34,   11,   21,    0,   34,   21,    0,   34,    0,   29,
   29,   21,    0,   34,   29,    0,    0,   29,    0,   33,
   33,    0,    0,   29,   33,    0,    0,   33,    0,    0,
    0,    0,    0,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   43,  123,   45,   40,   20,  125,    9,   40,   24,
    0,   24,   24,   43,   61,   45,  125,   44,   20,   23,
    0,  265,   42,  257,   53,   54,   30,   47,  262,   59,
    0,  265,   59,   41,  268,  269,   43,  271,   45,  268,
    0,   41,    0,   43,   59,   45,   59,   59,   52,   55,
   56,   41,    0,   43,   41,   45,   58,  258,   41,   59,
  265,   41,    0,   43,   59,   45,  257,    0,   41,   59,
   43,  262,   45,   41,  265,   20,   35,    8,   -1,   59,
  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,  257,   -1,  257,   -1,
   -1,  262,   -1,  262,  265,  125,  265,   -1,  257,   -1,
  271,   -1,  271,  262,   -1,  125,  265,   -1,   -1,   -1,
   -1,   -1,  271,   -1,   -1,  125,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  270,  265,
  266,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,   -1,  257,  258,   -1,
  270,  271,  262,   -1,   -1,  265,   -1,  257,  258,   -1,
  270,  271,  262,   -1,   -1,  265,   -1,  257,  258,   -1,
  270,  271,  262,   -1,   -1,  265,   -1,  257,  258,  257,
  258,  271,  262,   -1,  262,  265,   -1,  265,   -1,  257,
  258,  271,   -1,  271,  262,   -1,   -1,  265,   -1,  257,
  258,   -1,   -1,  271,  262,   -1,   -1,  265,   -1,   -1,
   -1,   -1,   -1,  271,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=271;
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
};
final static String yyrule[] = {
"$accept : programa",
"programa : declaraciones sentencias",
"declaraciones :",
"declaraciones : declaraciones declaracion",
"declaracion : tipo lista_variables ';'",
"tipo : INT",
"tipo : STRING",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"sentencias : sentencia",
"sentencias : sentencias sentencia",
"sentencia : sentencia_completa",
"sentencia : sentencia_incompleta",
"sentencia_completa : asignacion",
"sentencia_completa : sentencia_if_completo",
"sentencia_completa : sentencia_while_completa",
"sentencia_completa : sentencia_print",
"bloque : '{' sentencias '}'",
"bloque : '{' '}'",
"bloque_o_completa : bloque",
"bloque_o_completa : sentencia_completa",
"bloque_o_sentencia : bloque",
"bloque_o_sentencia : sentencia",
"sentencia_incompleta : IF condicion bloque_o_completa ELSE sentencia_incompleta",
"sentencia_incompleta : IF condicion bloque_o_sentencia",
"sentencia_incompleta : FOR condicion sentencia_incompleta",
"condicion : '(' comparacion ')'",
"sentencia_if_completo : IF condicion bloque_o_completa ELSE bloque_o_completa",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' expresion",
"comparacion : expresion COMPARADOR expresion",
"sentencia_while_completa : FOR condicion bloque_o_completa",
"sentencia_print : PRINT '(' STRING ')' ';'",
"sentencia_print : PRINT '(' STRING ')'",
"sentencia_print : PRINT '(' STRING",
"sentencia_print : PRINT STRING ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '(' expresion ')'",
};

//#line 118 "gramatica.y"

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

public int parse() {
    return yyparse();
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
//#line 380 "Parser.java"
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
case 4:
//#line 31 "gramatica.y"
{ /* eventos.EventNewRule("Declaracion de vars", analizador_lexico.getNumLinea()); */}
break;
case 11:
//#line 45 "gramatica.y"
{/* eventos.EventNewRule("Sentencia Completa", analizador_lexico.getNumLinea()); */}
break;
case 12:
//#line 46 "gramatica.y"
{ /* eventos.EventNewRule("Sentencia Incompleta", analizador_lexico.getNumLinea()); */}
break;
case 29:
//#line 82 "gramatica.y"
{/* eventos.EventError("falta un punto y coma", analizador_lexico.getNumLinea());
				huboError = true; */}
break;
case 33:
//#line 93 "gramatica.y"
{/* eventos.EventError("falta un ';'", analizador_lexico.getNumLinea());
				huboError = true; */}
break;
case 34:
//#line 95 "gramatica.y"
{/* eventos.EventError("falta un ')'", analizador_lexico.getNumLinea());
				huboError = true; */}
break;
case 35:
//#line 97 "gramatica.y"
{/* eventos.EventError("falta un '('", analizador_lexico.getNumLinea());
				huboError = true; */}
break;
//#line 561 "Parser.java"
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
