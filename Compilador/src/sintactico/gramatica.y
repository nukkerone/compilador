%{
package sintactico;

import lexico.*; 
import herramientaerror.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;
import cod_intermedio.*;
import interfaces.*;

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

%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%%

programa: declaraciones ejecutable FIN
;

declaraciones:
| declaraciones declaracion
;

declaracion: declaracion_simple
| cabecera_funcion BEGIN declaraciones_funcion ejecutable_funcion END  { 
    this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
}
| FUNCTION ID '(' parametro_formal ')' '{' error '}' {this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

cabecera_funcion: FUNCTION ID '(' parametro_formal ')' { 
    this.iniciarFuncion((Token)$2.obj);
}
;

declaraciones_funcion: 
| declaraciones_funcion declaracion_simple
;

declaracion_simple: tipo lista_variables {this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| tipo lista_variables ';'  { 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)$2.obj;					 
    asignarTipo($1.ival, v);
}
;

parametro_formal: 
| tipo ID
;

parametro_real:
| ID   
| constante         
;

tipo: INT              { $$.ival = Typeable.TIPO_INT; }
| STRING error         {this.eventoError.add("Declaracion invalida", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

lista_variables: ID         { 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add(clone($1));
    $$.obj = vars;
}
| lista_variables ',' ID    { 
    Vector<ParserVal> vars = (Vector<ParserVal>) $1.obj;
    vars.add(clone($3));
    $$.obj = vars;
}
;

ejecutable: 
| sentencias
| sentencias error  {this.eventoError.add("Sentencia mal formada", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| sentencias declaracion error    {this.eventoError.add("No puede haber sentencias declarativas fuera de la zona de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

ejecutable_funcion: 
| sentencias_funcion
;

bloque: sentencia
| BEGIN sentencias END
| '{' error {this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
| BEGIN END
| BEGIN sentencias error { this.eventoError.add("Bloque sin token de cerrado 'end'", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

llamado_funcion: ID '(' parametro_real ')'      { 
    this.eventoError.add("Llamado a funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.llamadoFuncion((Token) $1.obj);
}
;

sentencias: sentencia
| sentencias sentencia
;

sentencias_funcion: sentencia
| return_funcion
| sentencias_funcion sentencia
| sentencias_funcion return_funcion
;

return_funcion: RETURN ';' { this.finalizarFuncion(); }
| RETURN '(' expresion ')' ';' { this.finalizarFuncion(); }
;

sentencia: sentencia_if
| sentencia_for
| sentencia_asignacion
| sentencia_print
| llamado_funcion ';'
;

sentencia_if: inicio_if THEN bloque      %prec LOWER_THAN_ELSE       { 
    this.eventoError.add("Sentencia If Incompleta", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.eliminarIfPila();
}
| inicio_if THEN bloque ELSE {empezarElse();} bloque                                  { 
    this.eventoError.add("Sentencia If completa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    this.terminarElse();
}
;

inicio_if:  IF condicion  { agregarIfPila(); }
;

sentencia_for: comienzo_for bloque     {
 this.eventoError.add("Sentencia For", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
 this.desapilarFor();
}
;     

comienzo_for: FOR {apilarCondicionFor();}  condicion_for  {apilarFor();}
;   

condicion_for: '(' ID '=' expresion ';' comparacion ')'
| '(' ID '=' expresion ';' comparacion  { this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| error condicion_for         { this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

sentencia_print: PRINT '(' STRING ')' ';'              { 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add($3);
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    $$.obj= new TercetoPrint((Typeable)$3.obj);
}
| PRINT '(' STRING ';'    { this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| PRINT STRING error        { this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

condicion: '(' comparacion ')' 
| '(' comparacion       { this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| error                 { this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

comparacion: expresion COMPARADOR expresion {
    new TercetoComparacion((Token) $2.obj, (Typeable)$1.obj, (Typeable)$3.obj);
}
;

sentencia_asignacion: ID '=' expresion ';'  { 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    new TercetoAsignacion((Typeable)$1.obj, (Typeable)$3.obj);
}
;

expresion : expresion '+' termino   { 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoSuma((Typeable)$1.obj, (Typeable)$3.obj);
}
| expresion '-' termino { 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    $$.obj= new TercetoResta((Typeable)$1.obj, (Typeable)$3.obj);
}
| termino
;

termino : termino '*' factor    { 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoMultiplicacion((Typeable)$1.obj, (Typeable)$3.obj);
}
| termino '/' factor            { 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoDivision((Typeable)$1.obj, (Typeable)$3.obj);
}
| factor
;

factor: ID
| constante
| llamado_funcion
| '(' expresion ')' { $$ = $2; }
;

constante: CTE          { 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add($1);
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
| '-' CTE                       { 
    this.eventoError.add("Identificada constante negativa", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add($2);
    this.asignarTipo(Typeable.TIPO_CTE_ENTERA, vars); 
}
;

%%

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
Stack<Token> pilaParametros = new Stack<Token>(); 

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