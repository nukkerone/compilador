%{
package sintactico;

import lexico.*; 
import herramientaerror.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;
import cod_intermedio.*;
import interfaces.*;
import java.util.Iterator;

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

programa: {
    TypeableToken tokenParamReal;
    tokenParamReal = new TokenLexemaDistinto("ID", "_PARAM");
    ((TypeableToken)tokenParamReal).setTipo(Typeable.TIPO_INT);
    this.anLexico.getTablaSimbolos().addSimbolo(tokenParamReal, true);

} declaraciones ejecutable {
    setAmbitoTercetos("main", true);
    checkearVisibilidad("main");
}
FIN
;

declaraciones:
| declaraciones declaracion
;

declaracion: declaracion_simple
| cabecera_funcion BEGIN cuerpo_funcion END  { 
    finalizarFuncion(null);
    this.eventoError.add("Declaración de Funcion", this.anLexico.getNroLinea(), "Sintactico", "Regla" );

    String nombreFuncion = ((TypeableToken) $1.obj).getLexema();

    functionLabels.put(nombreFuncion, labelFuncion);
    checkearVisibilidad(nombreFuncion);
    
    Vector<Integer> tercetosAmbitoAux = new Vector<Integer>(tercetosAmbito);
    tercetosAmbito = new Vector<Integer>(tercetosFuncion);
    setAmbitoTercetos(nombreFuncion, false);
    tercetosAmbito = new Vector<Integer>(tercetosAmbitoAux);
    tercetosFuncion = new Vector<Integer>();

    int indexLabel = Terceto.tercetos.size();
    new TercetoLabel();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    // Actualizar direccion de salto incondicional del inicio de la funcion
    TercetoSalto saltoIncondicionalFinFuncion = (TercetoSalto) Terceto.tercetos.get(saltoFuncion);
    saltoIncondicionalFinFuncion.setDirSalto(indexLabel + 1);   // Uso mas + 1 porque sino dentro del generar assembler de un salto BI se rompe, ya que siempre espera una posicion adelante y por lo tanto le resta 1, asi qe este + 1 se cancela
    dentroDeFuncion = false;
}
| FUNCTION ID '(' parametro_formal ')' '{' error '}' {this.eventoError.add("No se puede iniciar bloque con llave", this.anLexico.getNroLinea() , "Sintactico", "Error"); }
;

cabecera_funcion: FUNCTION ID '(' parametro_formal ')' { 
    nombresDuplicadosCheck((TypeableToken)$2.obj);

    this.iniciarFuncion((Token)$2.obj);
    dentroDeFuncion = true;
    declaradasFuncion.clear();

    // Crear copia del parametro
    TypeableToken paramFormal = (TypeableToken)$4.obj;
    if (paramFormal != null) {
        nombreParametroFormalActual = ((Token)$4.obj).getLexema();
    }
    
    $$ = $2;
}
;

cuerpo_funcion: { 

    // @TODO implementar un terceto salto BI, que no se para que es
    saltoFuncion = Terceto.tercetos.size();
    new TercetoSalto("BI");
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);


    // Creacion del label de la funcion
    Terceto t = new TercetoLabel(); 
    labelFuncion = t.getPosicion();
    // Creacion del terceto push de la funcion
    new TercetoPush();
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    // Eliminar RET basura de la tabla de simbolos, sino se empiezan a acumular
    //this.anLexico.getTablaSimbolos().removeSimbolo("_RET", false);

    TablaSimbolos tablaSimbolos = this.anLexico.getTablaSimbolos();
    if (nombreParametroFormalActual != null) {
        TypeableToken paramFormal = (TypeableToken) tablaSimbolos.getSimbolo(new IdTS(nombreParametroFormalActual, Uso.USO_VARIABLE));
        //if (paramFormal != null) {
            TypeableToken paramReal = (TypeableToken) this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_PARAM", Uso.USO_VARIABLE));
            //if (paramReal != null) {
                Vector<ParserVal> v = new Vector<ParserVal>();      // Solo para asignarTipo a este parametro, tambien se setea ambito dentro, es una replicacion de las declaraciones de variables, pero para el parametro de una funcion
                ParserVal p1 = new ParserVal(paramFormal);
                v.add(clone(p1));
                asignarTipo(Typeable.TIPO_INT, v);      // Como siempre son variables tipo INT
                new TercetoAsignacion(paramFormal, paramReal);
                agregarTercetoAAmbito(Terceto.tercetos.size()-1);
                declaradasFuncion.add(paramFormal.getLexema());
            //}
        //}
    }
    nombreParametroFormalActual = null;
    
    
} declaraciones_funcion ejecutable_funcion
;

declaraciones_funcion: 
| declaraciones_funcion declaracion_simple
;

declaracion_simple: tipo lista_variables {
    this.eventoError.add("Falta ';' al final de declaracion", this.anLexico.getNroLinea() , "Sintactico", "Error");
}
| tipo lista_variables ';'  { 
    this.eventoError.add("Declaración de variables", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> v = (Vector<ParserVal>)$2.obj;                    

    asignarTipo($1.ival, v);
    if (dentroDeFuncion) {
        for (int i=0; i<v.size(); i++) {
            ParserVal p= (ParserVal) ((Vector) v).elementAt(i);
            String nombreVar = ((Token) p.obj).getLexema();
            declaradasFuncion.add(nombreVar);
        }
    }
    
}
;

parametro_formal: 
| tipo ID {
    $$ = $2;
}
;

parametro_real: { $$ = new ParserVal(null); }
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
    Typeable typeableParam = ((Typeable)$3.obj);
    if (typeableParam != null) {
        TypeableToken tokenAux = ((TypeableToken)typeableParam);
        
        TypeableToken tokenParamReal = (TypeableToken) this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_PARAM", Uso.USO_VARIABLE));
        new TercetoAsignacion((Typeable)tokenParamReal, (Typeable)tokenAux);
        agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    }
    this.llamadoFuncion((Token) $1.obj);
    
    Token tt = this.anLexico.getTablaSimbolos().getSimbolo(new IdTS("_RET", Uso.USO_VARIABLE));
    $$ = new ParserVal(tt);
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

return_funcion: RETURN ';' { 
    this.finalizarFuncion(null); 
}
| RETURN '(' expresion ')' ';' {
    this.finalizarFuncion((Typeable) $3.obj);
}
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

comienzo_for: FOR  condicion_for  {apilarFor();}
;   

condicion_for: '(' sentencia_asignacion comparacion_for ')' { 
    ParserVal sentAsig = $2; 
    apilarIndiceFor((Terceto) sentAsig.obj);
}
| '(' sentencia_asignacion comparacion_for  { this.eventoError.add("Falta cerrar parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| error condicion_for         { this.eventoError.add("Falta abrir parentesis a sentencia FOR", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

sentencia_print: PRINT '(' STRING ')' ';'              { 
    this.eventoError.add("Sentencia Print", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    Vector<ParserVal> vars = new Vector<ParserVal>();
    vars.add($3);
    this.asignarTipo(Typeable.TIPO_CADENA, vars); 
    $$.obj= new TercetoPrint((Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
| PRINT '(' STRING ';'    { this.eventoError.add("Falta cerrar parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| PRINT STRING error        { this.eventoError.add("Falta abrir parentesis a sentencia PRINT", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

condicion: '(' comparacion ')' 
| '(' comparacion       { this.eventoError.add("Falta cierre parentesis en la condicion", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
| error                 { this.eventoError.add("Falta abrir parentesis en condición", this.anLexico.getNroLinea(), "Sintactico", "Error" ); }
;

comparacion_for: { apilarCondicionFor(); } comparacion
;

comparacion: expresion COMPARADOR expresion {
    new TercetoComparacion((Token) $2.obj, (Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
;

sentencia_asignacion: ID '=' expresion ';'  { 
    this.eventoError.add("Asignacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj = new TercetoAsignacion((Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    Token t = (Token)$1.obj;
    ParserVal pVal = new ParserVal(t);
    pVal.sval = t.getLexema();
    pVal.ival = t.getNroLinea();
    if (dentroDeFuncion) {
        varFuncionUsadas.add(pVal);
    } else {
        varGlobalesUsadas.add(pVal);
    }
    this.anLexico.getTablaSimbolos().removeSimbolo(new IdTS(t.getLexema(), Uso.USO_VARIABLE));
}
;

expresion : expresion '+' termino   { 
    this.eventoError.add("Operación de suma", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoSuma((Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
| expresion '-' termino { 
    this.eventoError.add("Operación de resta", this.anLexico.getNroLinea(), "Sintactico", "Regla" );
    $$.obj= new TercetoResta((Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
| termino
;

termino : termino '*' factor    { 
    this.eventoError.add("Operación de multiplicacion", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoMultiplicacion((Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
| termino '/' factor            { 
    this.eventoError.add("Operación de division", this.anLexico.getNroLinea(), "Sintactico", "Regla" ); 
    $$.obj= new TercetoDivision((Typeable)$1.obj, (Typeable)$3.obj);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
}
| factor
;

factor: ID {
    Token t = (Token)$1.obj;
    ParserVal pVal = new ParserVal(t);
    pVal.sval = t.getLexema();
    pVal.ival = t.getNroLinea();
    if (dentroDeFuncion) {
        varFuncionUsadas.add(pVal);
    } else {
        varGlobalesUsadas.add(pVal);
    }
    this.anLexico.getTablaSimbolos().removeSimbolo(new IdTS(t.getLexema(), Uso.USO_VARIABLE));
}
| llamado_funcion
| constante
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

Hashtable<String, Integer> functionLabels = new Hashtable<String, Integer>();


Vector<ParserVal> varGlobalesUsadas = new Vector<ParserVal>();
Vector<ParserVal> varFuncionUsadas = new Vector<ParserVal>();
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

}

private void finalizarFuncion(Typeable tt) {
    Token tokenRetReal;
    Typeable retorno;
    tokenRetReal = new TokenLexemaDistinto("ID", "_RET");
    ((TypeableToken)tokenRetReal).setTipo(Typeable.TIPO_INT);

    if (tt == null) {
        retorno = new TokenLexemaDistinto("CTE", "0");    // Devuelvo 0 por defecto
        ((TypeableToken)retorno).setTipo(Typeable.TIPO_CTE_ENTERA);
    } else {
        retorno = tt;
    }

    
    this.anLexico.getTablaSimbolos().addSimbolo(tokenRetReal, true);
    new TercetoAsignacion((Typeable)tokenRetReal, (Typeable)retorno);
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);

    boolean sobreescribir = true;
    //this.anLexico.getTablaSimbolos().addSimbolo((Token)retorno, sobreescribir);
    Terceto t = new TercetoRetorno((Typeable) retorno);     // Al pedo enviar el retorno como parametro, porque el terceto asignacion se genero aca afuera
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
        
        // Eliminar el identificador (Uso Variable) de la TS, porque ya debería estar declarado como funcion (Uso Funcion)
    } else {
        // @TODO Generar error de funcion no declarada
    }
    agregarTercetoAAmbito(Terceto.tercetos.size()-1);
    this.anLexico.getTablaSimbolos().removeSimbolo(new IdTS(identificador.getLexema(), Uso.USO_VARIABLE));

}

private void nombresDuplicadosCheck(TypeableToken tokenNombreFuncion) {
    // @TODO hacer checkeo por nombre de funcion duplicados
    nombreFuncionActual = tokenNombreFuncion.getLexema();
    if (!this.anLexico.getTablaSimbolos().contains(new IdTS(nombreFuncionActual, Uso.USO_FUNCION))) {
        tokenNombreFuncion.setUso(Uso.USO_FUNCION);
        this.anLexico.getTablaSimbolos().addSimbolo(tokenNombreFuncion, true);
    } else {
        this.eventoError.add("Funcion con nombre " + nombreFuncionActual + " ya se encuentra declarada", tokenNombreFuncion.getNroLinea(), "Semantico", "Error" );
    }
}

private void limpiarVector(int desde, int hasta) {
    int cantidad = hasta - desde;
    for (int i = 1; i <= cantidad ; i++ ) {
        Terceto.tercetos.removeElementAt(Terceto.tercetos.size()-1);
    }
 }

private void checkearVisibilidad(String ambito) {
    Vector<ParserVal> toCheck = new Vector<ParserVal>();
    TablaSimbolos ts = this.anLexico.getTablaSimbolos();
    if (ambito == "main") {
        toCheck = varGlobalesUsadas;
    } else {
        toCheck = varFuncionUsadas;
    }

    for (int i=0; i<toCheck.size(); i++) {
        ParserVal pVal= (ParserVal) ((Vector) toCheck).elementAt(i);
        String variable = pVal.sval;
        int linea =  pVal.ival;
        Token t = ts.getSimbolo(new IdTS(variable + "_" + ambito, Uso.USO_VARIABLE));

        if (t == null) {
            if (ambito == "main") { // caso de variables globales
                this.eventoError.add("Variable '" + variable + "' no se encuentra disponible en el ambito: " + ambito, linea, "Semantico", "Error" );
            } else {    // Busco si está en el ambito global, porque es el caso de una funcion
                Token tAux = ts.getSimbolo(new IdTS(variable + "_main", Uso.USO_VARIABLE));
                if (tAux == null) {
                    this.eventoError.add("Variable '" + variable + "' no se encuentra disponible en el ambito: " + ambito, linea, "Semantico", "Error" );
                }
            }
        }
    }
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
            posibleDeclarada = posibleDeclarada.replace("_main", "");
            if (!main && !tokenParam1.getLexema().contains("_" + ambito)) {
                if (declaradasFuncion.contains(posibleDeclarada + "_" + ambito)) {
                    tokenParam1.setAmbito("_" + ambito);
                } else {
                    tokenParam1.setAmbito("_main");
                }
            }

            if (main && !posibleDeclarada.contains("_")) {
                tokenParam1.setAmbito("_main");
            }
        }
        
        if (param2 != null && param2 instanceof TypeableToken) {
            Token tokenParam2 = (Token) param2;
            String posibleDeclarada = tokenParam2.getLexema();
            posibleDeclarada = posibleDeclarada.replace("_main", "");
            if (!main && !tokenParam2.getLexema().contains("_" + ambito)) {
                if (declaradasFuncion.contains(posibleDeclarada + "_" + ambito)) {
                    tokenParam2.setAmbito("_" + ambito);
                } else {
                    tokenParam2.setAmbito("_main");
                }
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