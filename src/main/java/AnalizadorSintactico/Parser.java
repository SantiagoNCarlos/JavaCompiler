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






//#line 2 "grammar.y"
package AnalizadorSintactico;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Map;
import static java.lang.Math.abs;
import AnalizadorLexico.*;
import Utilities.Logger;
import AnalizadorLexico.SemanticActions.SemanticAction;
import AnalizadorLexico.Enums.DelimiterType;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;
//#line 33 "Parser.java"




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
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short END_IF=261;
public final static short ELSE=262;
public final static short CLASS=263;
public final static short PRINT=264;
public final static short VOID=265;
public final static short LONG=266;
public final static short USHORT=267;
public final static short FLOAT=268;
public final static short WHILE=269;
public final static short DO=270;
public final static short MENORIGUAL=271;
public final static short MAYORIGUAL=272;
public final static short IGUAL=273;
public final static short NEGADO=274;
public final static short RETURN=275;
public final static short MASMAS=276;
public final static short CTE_FLOTANTE=277;
public final static short UNEXISTENT_RES_WORD=278;
public final static short CHECK=279;
public final static short n=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    1,    1,    2,    2,
    2,    2,    4,    4,    4,    4,    4,   10,   10,   10,
    5,    5,    5,   17,   17,   17,   17,   14,   14,   14,
   18,   18,    6,    6,    6,    6,   13,   19,   19,   19,
   19,   19,   20,   20,   20,   20,   20,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   22,   22,   22,   22,
   22,   22,   22,   23,   23,   23,   24,   24,   24,   24,
   24,    7,    7,    7,    7,    7,    7,   25,   25,   25,
   25,   25,   27,   27,   28,   28,    8,    8,    8,   15,
   15,   15,   29,   29,   29,   29,   29,   29,   29,   31,
   31,   32,   32,   32,   30,   30,   30,   33,   33,   35,
   35,   35,   35,   35,   35,   34,   34,   26,   26,   37,
   37,   37,   36,   36,   36,   36,   36,   36,   38,   38,
   38,   38,   38,   38,   39,   39,    3,    3,    3,    3,
   42,   40,   40,   43,   43,   43,   43,   41,   41,   41,
   41,   41,   41,   41,   44,   16,   12,   12,   11,   11,
    9,    9,    9,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    1,    2,    2,    2,
    2,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    2,    2,
    3,    1,    3,    3,    3,    3,    2,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    1,    1,    2,    2,
    2,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    3,    1,    3,    2,    3,    2,
    2,    4,    6,    6,    4,    4,    2,    1,    1,    3,
    4,    3,    2,    1,    2,    1,    4,    4,    3,    4,
    4,    4,    5,    4,    3,    4,    2,    3,    1,    1,
    3,    2,    1,    1,    2,    1,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    2,    2,    2,    2,    4,
    4,    3,    4,    6,    6,    4,    4,    2,    1,    1,
    3,    3,    4,    3,    2,    1,    4,    4,    1,    1,
    3,    2,    2,    1,    1,    1,    1,    3,    3,    3,
    3,    2,    2,    2,    1,    2,    3,    3,    3,    5,
    2,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   25,   26,   24,    0,   27,
    0,    0,    0,    0,    5,    7,    0,    0,   13,   14,
   15,   16,   17,   18,    0,   20,   21,   22,   23,    0,
    0,    0,  139,   32,   37,    0,    0,    0,    0,    0,
    0,    0,    0,   78,    0,   77,   79,  143,    0,  163,
    0,   53,  161,   54,    0,   48,   55,  162,    0,    0,
    0,    0,    2,    0,    4,    6,    8,   11,    9,   12,
   10,    0,   30,    0,    0,    0,    0,   34,    0,    0,
   47,  159,  158,    0,    0,    0,    0,  119,  118,   86,
    0,    0,    0,   71,    0,    0,  141,    0,    0,   50,
   51,   49,  103,    0,    0,   98,    0,   95,    0,    0,
   89,    0,    1,   36,    0,    0,    0,  115,  110,  113,
  114,  106,    0,    0,  109,    0,  111,  112,    0,    0,
  147,  144,  145,    0,    0,  146,    0,    0,    0,    0,
    0,   31,   58,   59,   57,   62,   63,   60,   61,    0,
   69,    0,   82,   80,   85,   83,   67,   75,   72,    0,
   76,  157,   94,    0,  102,    0,   96,   88,   87,    0,
    0,  130,  129,  128,    0,   92,  105,  108,  117,  116,
   91,   90,  138,  137,  152,    0,    0,  154,  153,   40,
    0,   41,    0,   45,   43,   46,   44,  160,   65,    0,
   81,    0,   93,  101,    0,    0,    0,    0,    0,  122,
  151,  150,  149,  148,   74,   73,  131,    0,  135,  134,
  132,  126,  123,    0,  127,  121,  120,  133,    0,  125,
  124,
};
final static short yydgoto[] = {                         13,
   14,   44,   16,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,   29,   30,   39,   86,   80,
   81,  150,   87,   45,   46,   47,   92,   93,   31,  123,
  106,  107,  124,  173,  126,  127,  128,  174,  207,   32,
  134,   33,  135,  136,
};
final static short yysindex[] = {                       494,
  -27,  -40, -220,  207,  -38,    0,    0,    0,  -34,    0,
 -223,  577,    0,  594,    0,    0,   -6,    3,    0,    0,
    0,    0,    0,    0,  -22,    0,    0,    0,    0,   -9,
 -119, -101,    0,    0,    0,   89,    5, -164,   -7,  231,
    4,  750,   15,    0,  520,    0,    0,    0,    6,    0,
  -39,    0,    0,    0, -216,    0,    0,    0,  160,  180,
  345, -194,    0,  617,    0,    0,    0,    0,    0,    0,
    0,  138,    0,   -7,  774,  774,  826,    0,  -17,   12,
    0,    0,    0,   30, -191,  450,   39,    0,    0,    0,
  806, -115,  750,    0,   41,  290,    0, -145,   51,    0,
    0,    0,    0,  193, -152,    0,   63,    0,   76,  373,
    0,   -7,    0,    0,  -17,  -24,  -34,    0,    0,    0,
    0,    0,   14,  774,    0,    7,    0,    0, -102,  -27,
    0,    0,    0,  560,   21,    0,  217,  244,  248,  266,
   77,    0,    0,    0,    0,    0,    0,    0,    0,  272,
    0,   22,    0,    0,    0,    0,    0,    0,    0,  520,
    0,    0,    0,   82,    0, -116,    0,    0,    0,  774,
  540,    0,    0,    0,  390,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   54,   64,    0,    0,    0,
   12,    0,   12,    0,    0,    0,    0,    0,    0,  -17,
    0, -192,    0,    0,  640,  774, -100,  322,  414,    0,
    0,    0,    0,    0,    0,    0,    0,   25,    0,    0,
    0,    0,    0,  540,    0,    0,    0,    0, -170,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -118,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   65,    0,    0,    0,    0,    0,
    0,   18,    0,    0,    0,    0,    0,    0,   66,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
   34,    0,    0,    0,    0,    0,    0,    0, -114,    0,
    0,    0,    0,  167,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   86,    0,    0,    0,    0,   87,   84,
    0,    0,    0,  -41,    0,  165,  430,    0,    0,    0,
    0,    0,  -95,    0,  469,    0,    0,    0,   59,    0,
    0,    0,    0,    0,  -33,    0,  -10,    0,    0,    0,
    0,  104,    0,    0,  111,    0,    0,    0,    0,    0,
    0,    0,    0,  -93,    0,    0,    0,    0,    0,  114,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  108,    0,  133,    0,    0,    0,    0,    0,    0,  212,
    0,    0,    0,    0,    0,  -92,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  159,  703,   -2,  -62,  755,  777,    0,    0,  800,  827,
    0,  919,  941,  -60,   38,  -50,   29,   23,  -15,  -23,
   20,    0,  130,   49,   26,  410,   88,    0,    0,   99,
  -31,    0, -156,   -8,    0,    0,    0,  764,  -25,    0,
    0,    0,   47,    0,
};
final static int YYTABLESIZE=1105;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
  142,   60,  157,   76,   99,   43,   98,  104,   97,  154,
  104,   67,   37,  206,  131,   43,  132,  140,   38,  157,
   79,   77,  182,   58,  221,  137,  133,  138,  109,   84,
  100,  107,  136,   36,   73,   48,   49,   69,   72,   62,
   99,  100,    6,    7,    8,   82,   71,   89,  206,   97,
  180,   85,   74,  139,   10,   94,  115,   61,  140,   55,
  101,   67,   34,  215,  189,  142,  125,  125,  216,  141,
   96,  131,  164,  132,   56,   56,   56,   56,   56,  151,
   56,  157,   42,  133,  112,  230,  111,  105,  105,  104,
  231,   83,   84,   56,   56,   56,   98,  212,  170,   52,
   52,   52,   52,   52,  165,   52,  166,  214,   19,  156,
   83,  162,  100,  191,  193,  178,  167,  198,   52,   52,
   52,  161,  203,  142,   42,  142,   42,   42,   42,   29,
   33,  185,  105,   55,  200,  169,   75,   99,  176,  204,
  153,   97,  140,   42,   42,   42,  201,   28,   38,  228,
   38,   38,   38,  181,   35,  220,   56,  155,  195,  197,
   84,  125,  107,  136,  171,  175,    3,   38,   38,   38,
   64,  186,   95,   39,  129,   39,   39,   39,  152,  218,
  187,   52,   55,    0,    0,  202,    0,    0,    0,    0,
    0,    0,   39,   39,   39,    0,  125,  178,    0,  104,
    0,    0,    0,    0,    0,   66,   42,    0,    0,    0,
    0,    0,    0,    0,  157,   40,    1,    0,   59,    2,
  108,   40,  104,    4,    5,    6,    7,    8,    9,   34,
   38,   40,    1,  163,   41,  116,   35,   10,   11,    4,
    5,    6,    7,    8,  117,  100,    0,   34,   35,   68,
   41,   55,   64,   10,   11,   39,  142,  142,   70,   88,
  142,   55,  179,  142,  142,  142,  142,  142,  142,  142,
    0,   51,   52,  140,  140,   55,  188,  140,  142,  142,
  140,  140,  140,  140,  140,  140,  140,   66,   55,   56,
   56,   54,   55,   56,    0,  140,  140,   56,   56,   56,
   56,   56,   56,   56,   56,   56,   56,   56,   56,  211,
   55,   56,   56,    0,   52,   52,   55,    0,   52,  213,
   19,  156,   52,   52,   52,   52,   52,   52,   52,   52,
   52,   52,   52,   52,   64,    0,   52,   52,    0,   42,
   42,   29,   33,   42,   78,   51,   52,   42,   42,   42,
   42,   42,   42,   42,   42,   42,   42,   42,   42,   28,
    0,   42,   42,   38,   38,   54,   35,   38,    0,  155,
    0,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,   38,   38,    0,    0,   38,   38,    0,   39,   39,
    0,    0,   39,  114,   51,   52,   39,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,    0,    0,
   39,   39,   42,    0,   54,    0,  103,    0,    0,    0,
    0,   66,    0,    0,   66,    6,    7,    8,   66,   66,
   66,   66,   66,   66,   66,    0,  103,   10,    0,   66,
    0,    0,   66,   66,  170,    6,    7,    8,    0,  103,
    0,   91,    0,    0,    0,    0,    0,   10,    6,    7,
    8,    0,   50,   51,   52,   53,    0,   42,   64,    0,
   10,   64,  190,   51,   52,   64,   64,   64,   64,   64,
   64,   64,    0,   54,  122,  122,   64,   51,   52,   64,
   64,    0,  137,   54,  138,   42,    0,    0,    0,  192,
   51,   52,  156,  194,   51,   52,    0,   54,    0,  148,
  147,  149,  170,    0,    0,    0,    0,    0,    0,    0,
   54,  196,   51,   52,   54,  172,    0,  199,   51,   52,
    0,    0,    0,  177,    0,    0,  170,    0,    0,    0,
    0,    0,   54,    0,    0,  158,    1,    0,   54,    2,
  159,  160,   68,    4,    5,    6,    7,    8,    9,    0,
    0,    0,    0,    0,   41,    0,    0,   10,   11,    0,
    0,    0,    0,    0,    0,    0,    0,  222,    1,  205,
  172,  116,  223,  224,  172,    4,    5,    6,    7,    8,
  117,   70,    0,    0,    0,    0,   41,    0,    0,   10,
   11,    1,    0,    0,    2,    0,    0,    0,    4,    5,
    6,    7,    8,    9,  110,  219,   12,  172,  172,   41,
    0,    0,   10,   11,    0,    0,    0,    0,  168,    1,
    0,    0,    2,  172,    0,    0,    4,    5,    6,    7,
    8,    9,   42,    0,    0,    0,    1,   41,    0,  116,
   10,   11,    0,    4,    5,    6,    7,    8,  117,  209,
    0,    0,  170,    0,   41,    0,    0,   10,   11,  226,
    1,    0,    0,  116,    0,    0,    0,    4,    5,    6,
    7,    8,  117,    0,  184,    0,   68,    0,   41,   68,
    0,   10,   11,   68,   68,   68,   68,   68,   68,   68,
    0,   63,   15,    0,   68,    0,    0,   68,   68,    0,
    0,    0,    0,    0,   15,    0,   66,    0,   65,    0,
  143,  144,  145,  146,    0,   70,    0,    0,   70,    0,
    0,    0,   70,   70,   70,   70,   70,   70,   70,    0,
    0,  113,    0,   70,   90,    0,   70,   70,    0,    0,
    1,    0,    0,    2,    0,    0,    3,    4,    5,    6,
    7,    8,    9,    0,  217,    0,   66,    0,    0,    0,
    0,   10,   11,    0,    0,    0,    1,    0,    0,    2,
    0,    0,    0,    4,    5,    6,    7,    8,    9,    0,
    0,    0,    0,   90,   41,  155,    1,   10,   11,  116,
    0,    0,    0,    4,    5,    6,    7,    8,  117,    0,
    0,    0,    0,    0,   41,  183,  130,   10,   11,    2,
    0,    0,    3,    4,    5,    6,    7,    8,    9,  118,
  118,    0,    0,    1,    0,    0,    2,   10,   11,    3,
    4,    5,    6,    7,    8,    9,    0,    0,    0,    0,
    1,  119,  119,    2,   10,   11,    3,    4,    5,    6,
    7,    8,    9,    0,    0,    0,    0,    0,    0,    0,
  118,   10,   11,    1,  120,  120,    2,    0,  118,    3,
    4,    5,    6,    7,    8,    9,    0,    0,    0,    0,
    0,    0,  119,    0,   10,   11,    1,    0,    0,  116,
  119,  121,  121,    4,    5,    6,    7,    8,  117,    0,
    0,    0,    0,    0,    0,  120,    0,   10,   11,    0,
    0,    0,   56,  120,  118,  118,    0,    0,    0,  118,
    0,    0,    0,    0,  208,    0,    0,    0,  210,    0,
    0,    0,  121,    0,   57,    0,  119,  119,    0,    0,
  121,  119,    0,    0,   56,    0,    0,    0,   56,  118,
  118,   56,  118,  118,    0,    0,    0,    0,    0,  120,
  120,  225,  227,  102,  120,    0,   57,    0,  118,    0,
   57,  119,  119,   57,  119,  119,    0,  229,    0,    0,
   56,    0,    0,    0,    0,    0,  121,  121,    0,    0,
  119,  121,    0,    0,  120,  120,    1,  120,  120,    2,
    0,    0,   57,    4,    5,    6,    7,    8,    9,    0,
    0,    0,    0,  120,   41,    0,    0,   10,   11,    0,
    1,  121,  121,  116,  121,  121,    0,    4,    5,    6,
    7,    8,  117,    0,    0,    0,    0,    0,   41,    0,
  121,   10,   11,    0,    0,   56,   56,   56,   56,    0,
    0,    0,    1,    0,    0,    2,    0,    0,   56,    4,
    5,    6,    7,    8,    9,    0,    0,   57,   57,   57,
   57,    0,  130,   10,   11,    2,    0,    0,    0,    4,
   57,    6,    7,    8,    9,    0,    0,    0,    0,    0,
    0,    0,    0,   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,  123,  123,   40,   46,   41,  123,  125,
   44,   14,   40,  170,   77,   40,   77,    0,   46,   61,
   36,  123,  125,    4,  125,   43,   77,   45,   60,  125,
   41,  125,  125,   61,   44,  256,  257,   44,   61,   11,
  257,  258,  266,  267,  268,   41,   44,   44,  205,   44,
   44,   59,   30,   42,  278,   41,   72,    9,   47,   45,
  277,   64,  257,  256,   44,  257,   75,   76,  261,   40,
   45,  134,  104,  134,   41,   42,   43,   44,   45,   41,
   47,   41,  123,  134,   62,  256,   61,   59,   60,  123,
  261,  256,  257,   60,   61,   62,   46,   44,  123,   41,
   42,   43,   44,   45,  257,   47,   44,   44,   44,   44,
  256,  257,  123,  137,  138,  124,   41,   41,   60,   61,
   62,   96,   41,  123,   41,  125,   43,   44,   45,   44,
   44,  134,  104,   45,  150,  110,  256,  256,  125,  256,
  256,  256,  125,   60,   61,   62,  125,   44,   41,  125,
   43,   44,   45,  256,   44,  256,  123,   44,  139,  140,
  256,  170,  256,  256,  116,  117,    0,   60,   61,   62,
   12,  134,   43,   41,   76,   43,   44,   45,   91,  205,
  134,  123,   45,   -1,   -1,  160,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   61,   62,   -1,  205,  206,   -1,   40,
   -1,   -1,   -1,   -1,   -1,   41,  123,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  256,  257,   -1,  257,  260,
   41,  256,  256,  264,  265,  266,  267,  268,  269,  257,
  123,  256,  257,   41,  275,  260,  276,  278,  279,  264,
  265,  266,  267,  268,  269,  256,   -1,  257,  276,  256,
  275,   45,   41,  278,  279,  123,  256,  257,  256,  256,
  260,   45,  256,  263,  264,  265,  266,  267,  268,  269,
   -1,  257,  258,  256,  257,   45,  256,  260,  278,  279,
  263,  264,  265,  266,  267,  268,  269,  123,   45,  256,
  257,  277,   45,  260,   -1,  278,  279,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  256,
   45,  278,  279,   -1,  256,  257,   45,   -1,  260,  256,
  256,  256,  264,  265,  266,  267,  268,  269,  270,  271,
  272,  273,  274,  275,  123,   -1,  278,  279,   -1,  256,
  257,  256,  256,  260,  256,  257,  258,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  256,
   -1,  278,  279,  256,  257,  277,  256,  260,   -1,  256,
   -1,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,   -1,   -1,  278,  279,   -1,  256,  257,
   -1,   -1,  260,  256,  257,  258,  264,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,   -1,   -1,
  278,  279,  123,   -1,  277,   -1,  257,   -1,   -1,   -1,
   -1,  257,   -1,   -1,  260,  266,  267,  268,  264,  265,
  266,  267,  268,  269,  270,   -1,  257,  278,   -1,  275,
   -1,   -1,  278,  279,  123,  266,  267,  268,   -1,  257,
   -1,   42,   -1,   -1,   -1,   -1,   -1,  278,  266,  267,
  268,   -1,  256,  257,  258,  259,   -1,  123,  257,   -1,
  278,  260,  256,  257,  258,  264,  265,  266,  267,  268,
  269,  270,   -1,  277,   75,   76,  275,  257,  258,  278,
  279,   -1,   43,  277,   45,  123,   -1,   -1,   -1,  256,
  257,  258,   93,  256,  257,  258,   -1,  277,   -1,   60,
   61,   62,  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  277,  256,  257,  258,  277,  116,   -1,  256,  257,  258,
   -1,   -1,   -1,  124,   -1,   -1,  123,   -1,   -1,   -1,
   -1,   -1,  277,   -1,   -1,  256,  257,   -1,  277,  260,
  261,  262,  123,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,   -1,  275,   -1,   -1,  278,  279,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  170,
  171,  260,  261,  262,  175,  264,  265,  266,  267,  268,
  269,  123,   -1,   -1,   -1,   -1,  275,   -1,   -1,  278,
  279,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,  265,
  266,  267,  268,  269,  270,  206,  123,  208,  209,  275,
   -1,   -1,  278,  279,   -1,   -1,   -1,   -1,  256,  257,
   -1,   -1,  260,  224,   -1,   -1,  264,  265,  266,  267,
  268,  269,  123,   -1,   -1,   -1,  257,  275,   -1,  260,
  278,  279,   -1,  264,  265,  266,  267,  268,  269,  270,
   -1,   -1,  123,   -1,  275,   -1,   -1,  278,  279,  256,
  257,   -1,   -1,  260,   -1,   -1,   -1,  264,  265,  266,
  267,  268,  269,   -1,  125,   -1,  257,   -1,  275,  260,
   -1,  278,  279,  264,  265,  266,  267,  268,  269,  270,
   -1,  125,    0,   -1,  275,   -1,   -1,  278,  279,   -1,
   -1,   -1,   -1,   -1,   12,   -1,   14,   -1,  125,   -1,
  271,  272,  273,  274,   -1,  257,   -1,   -1,  260,   -1,
   -1,   -1,  264,  265,  266,  267,  268,  269,  270,   -1,
   -1,  125,   -1,  275,   42,   -1,  278,  279,   -1,   -1,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,  266,
  267,  268,  269,   -1,  125,   -1,   64,   -1,   -1,   -1,
   -1,  278,  279,   -1,   -1,   -1,  257,   -1,   -1,  260,
   -1,   -1,   -1,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,   91,  275,   93,  257,  278,  279,  260,
   -1,   -1,   -1,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,   -1,  275,  256,  257,  278,  279,  260,
   -1,   -1,  263,  264,  265,  266,  267,  268,  269,   75,
   76,   -1,   -1,  257,   -1,   -1,  260,  278,  279,  263,
  264,  265,  266,  267,  268,  269,   -1,   -1,   -1,   -1,
  257,   75,   76,  260,  278,  279,  263,  264,  265,  266,
  267,  268,  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  116,  278,  279,  257,   75,   76,  260,   -1,  124,  263,
  264,  265,  266,  267,  268,  269,   -1,   -1,   -1,   -1,
   -1,   -1,  116,   -1,  278,  279,  257,   -1,   -1,  260,
  124,   75,   76,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,   -1,   -1,  116,   -1,  278,  279,   -1,
   -1,   -1,    4,  124,  170,  171,   -1,   -1,   -1,  175,
   -1,   -1,   -1,   -1,  171,   -1,   -1,   -1,  175,   -1,
   -1,   -1,  116,   -1,    4,   -1,  170,  171,   -1,   -1,
  124,  175,   -1,   -1,   36,   -1,   -1,   -1,   40,  205,
  206,   43,  208,  209,   -1,   -1,   -1,   -1,   -1,  170,
  171,  208,  209,   55,  175,   -1,   36,   -1,  224,   -1,
   40,  205,  206,   43,  208,  209,   -1,  224,   -1,   -1,
   72,   -1,   -1,   -1,   -1,   -1,  170,  171,   -1,   -1,
  224,  175,   -1,   -1,  205,  206,  257,  208,  209,  260,
   -1,   -1,   72,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,  224,  275,   -1,   -1,  278,  279,   -1,
  257,  205,  206,  260,  208,  209,   -1,  264,  265,  266,
  267,  268,  269,   -1,   -1,   -1,   -1,   -1,  275,   -1,
  224,  278,  279,   -1,   -1,  137,  138,  139,  140,   -1,
   -1,   -1,  257,   -1,   -1,  260,   -1,   -1,  150,  264,
  265,  266,  267,  268,  269,   -1,   -1,  137,  138,  139,
  140,   -1,  257,  278,  279,  260,   -1,   -1,   -1,  264,
  150,  266,  267,  268,  269,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  278,  279,
};
}
final static short YYFINAL=13;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
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
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","END_IF","ELSE",
"CLASS","PRINT","VOID","LONG","USHORT","FLOAT","WHILE","DO","MENORIGUAL",
"MAYORIGUAL","IGUAL","NEGADO","RETURN","MASMAS","CTE_FLOTANTE",
"UNEXISTENT_RES_WORD","CHECK","n",
};
final static String yyrule[] = {
"$accept : programa",
"programa : '{' bloque '}'",
"programa : '{' '}'",
"programa : '{' bloque",
"programa : bloque '}'",
"bloque : sentencia",
"bloque : bloque sentencia",
"bloque : definicion_class",
"bloque : bloque definicion_class",
"sentencia : ejecucion ','",
"sentencia : declaracion ','",
"sentencia : ejecucion error",
"sentencia : declaracion error",
"ejecucion : asignacion",
"ejecucion : bloque_if",
"ejecucion : bloque_while",
"ejecucion : impresion",
"ejecucion : expresion",
"expresion : llamada",
"expresion : acceso",
"expresion : incremento",
"declaracion : declaracion_var",
"declaracion : declaracion_void",
"declaracion : declaracion_var_class",
"tipo : FLOAT",
"tipo : LONG",
"tipo : USHORT",
"tipo : UNEXISTENT_RES_WORD",
"declaracion_var : CHECK tipo lista_de_variables",
"declaracion_var : tipo lista_de_variables",
"declaracion_var : tipo ','",
"lista_de_variables : lista_de_variables ';' ID",
"lista_de_variables : ID",
"asignacion : ID '=' expresion_aritmetica",
"asignacion : ID '=' error",
"asignacion : acceso '=' expresion_aritmetica",
"asignacion : acceso '=' error",
"incremento : ID MASMAS",
"expresion_aritmetica : expresion_aritmetica '+' termino",
"expresion_aritmetica : expresion_aritmetica '-' termino",
"expresion_aritmetica : expresion_aritmetica '+' error",
"expresion_aritmetica : expresion_aritmetica '-' error",
"expresion_aritmetica : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : termino '*' error",
"termino : termino '/' error",
"termino : factor",
"factor : acceso",
"factor : '-' acceso",
"factor : '-' CTE",
"factor : '-' CTE_FLOTANTE",
"factor : '-' ID",
"factor : CTE",
"factor : CTE_FLOTANTE",
"factor : incremento",
"factor : ID",
"operador : IGUAL",
"operador : MENORIGUAL",
"operador : MAYORIGUAL",
"operador : '<'",
"operador : '>'",
"operador : NEGADO",
"operador : '='",
"condicion : expresion_aritmetica operador expresion_aritmetica",
"condicion : expresion_aritmetica operador error",
"condicion : expresion_aritmetica",
"condicion_parentesis : '(' condicion ')'",
"condicion_parentesis : error condicion",
"condicion_parentesis : error condicion ')'",
"condicion_parentesis : '(' condicion",
"condicion_parentesis : '(' ')'",
"bloque_if : IF condicion_parentesis bloque_condicion END_IF",
"bloque_if : IF condicion_parentesis bloque_condicion ELSE bloque_condicion END_IF",
"bloque_if : IF condicion_parentesis bloque_condicion ELSE bloque_condicion error",
"bloque_if : IF condicion_parentesis bloque_condicion error",
"bloque_if : IF condicion_parentesis bloque_condicion bloque_condicion",
"bloque_if : IF bloque_condicion",
"bloque_condicion : sentencia",
"bloque_condicion : sentencia_return",
"bloque_condicion : '{' bloque_sentencias_condicion '}'",
"bloque_condicion : '{' sentencia_return bloque_sentencias_condicion '}'",
"bloque_condicion : '{' bloque_sentencias_condicion error",
"bloque_sentencias_condicion : sentencias_concatenacion sentencia_return",
"bloque_sentencias_condicion : sentencias_concatenacion",
"sentencias_concatenacion : sentencias_concatenacion sentencia",
"sentencias_concatenacion : sentencia",
"bloque_while : WHILE condicion_parentesis DO bloque_condicion",
"bloque_while : WHILE condicion_parentesis DO error",
"bloque_while : WHILE condicion_parentesis bloque_condicion",
"declaracion_void : header_void '{' bloque_void '}'",
"declaracion_void : header_void '{' bloque_void error",
"declaracion_void : header_void error bloque_void '}'",
"header_void : VOID ID '(' func_parameters ')'",
"header_void : VOID ID '(' ')'",
"header_void : VOID '(' ')'",
"header_void : VOID '(' func_parameters ')'",
"header_void : VOID ID",
"header_void : VOID ID func_parameters",
"header_void : VOID",
"func_parameters : parametro",
"func_parameters : parametro ',' error",
"parametro : tipo ID",
"parametro : ID",
"parametro : tipo",
"bloque_void : sentencias_concatenacion_void sentencia_return",
"bloque_void : sentencia_return",
"bloque_void : sentencias_concatenacion_void",
"sentencias_concatenacion_void : sentencias_concatenacion_void sentencia_void",
"sentencias_concatenacion_void : sentencia_void",
"sentencias_void : asignacion",
"sentencias_void : bloque_if_void",
"sentencias_void : bloque_while_void",
"sentencias_void : impresion",
"sentencias_void : expresion",
"sentencias_void : declaracion",
"sentencia_void : sentencias_void ','",
"sentencia_void : sentencias_void error",
"sentencia_return : RETURN ','",
"sentencia_return : RETURN error",
"bloque_while_void : WHILE condicion_parentesis DO bloque_condicion_void",
"bloque_while_void : WHILE condicion_parentesis DO error",
"bloque_while_void : WHILE condicion_parentesis bloque_condicion_void",
"bloque_if_void : IF condicion_parentesis bloque_condicion_void END_IF",
"bloque_if_void : IF condicion_parentesis bloque_condicion_void ELSE bloque_condicion_void END_IF",
"bloque_if_void : IF condicion_parentesis bloque_condicion_void ELSE bloque_condicion_void error",
"bloque_if_void : IF condicion_parentesis bloque_condicion_void error",
"bloque_if_void : IF condicion_parentesis bloque_condicion_void bloque_condicion_void",
"bloque_if_void : IF bloque_condicion_void",
"bloque_condicion_void : sentencia_void",
"bloque_condicion_void : sentencia_return",
"bloque_condicion_void : '{' sentencia_return '}'",
"bloque_condicion_void : '{' bloque_sentencias_condicion_void '}'",
"bloque_condicion_void : '{' sentencia_return bloque_sentencias_condicion_void '}'",
"bloque_condicion_void : '{' bloque_sentencias_condicion_void error",
"bloque_sentencias_condicion_void : sentencias_concatenacion_void sentencia_return",
"bloque_sentencias_condicion_void : sentencias_concatenacion_void",
"definicion_class : header_class '{' bloque_class '}'",
"definicion_class : header_class '{' bloque_class error",
"definicion_class : forward_declaration",
"definicion_class : header_class",
"forward_declaration : CLASS ID ','",
"header_class : CLASS ID",
"header_class : CLASS error",
"sentencias_class : declaracion_var",
"sentencias_class : declaracion_var_class",
"sentencias_class : inheritance_by_composition",
"sentencias_class : ejecucion",
"bloque_class : bloque_class sentencias_class ','",
"bloque_class : bloque_class sentencias_class error",
"bloque_class : bloque_class declaracion_void ','",
"bloque_class : bloque_class declaracion_void error",
"bloque_class : bloque_class definicion_class",
"bloque_class : sentencias_class ','",
"bloque_class : sentencias_class error",
"inheritance_by_composition : ID",
"declaracion_var_class : ID lista_de_variables",
"acceso : ID '.' ID",
"acceso : ID '.' error",
"llamada : ID '(' ')'",
"llamada : ID '.' ID '(' ')'",
"impresion : PRINT CADENA",
"impresion : PRINT factor",
"impresion : PRINT error",
};

//#line 878 "grammar.y"
public static Logger logger = Logger.getInstance();
public static String ambito = "global"; //ver esto dsp
public static boolean error = false;
public static SyntaxNode padre = null;
public static List<SyntaxNode> arbolFunciones = new ArrayList<SyntaxNode>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static Boolean flagAmbitoCambiado = false;

private boolean metodoExisteEnClase(String tipoClase, String nombreMetodo) {
    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (attribute.getUso().equals(UsesType.FUNCTION) && attribute.getToken().contains(tipoClase)) {
            // Extraer el nombre del método del token
            String metodo = attribute.getToken().split(":")[0];
            // Verificar si el nombre del método coincide con el nombre del método buscado
            if (metodo.equals(nombreMetodo)) {
                return true;
            }
        }
    }
    return false;
}
private boolean metodoExiste(String tipoClase, String nombreMetodo) {
    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (attribute.getUso().equals(UsesType.FUNCTION) && attribute.getToken().contains(tipoClase)) {
            // Extraer el nombre del mÃ©todo del token
            String metodo = attribute.getToken();
            System.out.println("comparo: " + metodo + " con " + nombreMetodo + "mi ambito es: " + Parser.ambito);
            // Verificar si el nombre del mÃ©todo coincide con el nombre del mÃ©todo buscado
            if (metodo.equals(nombreMetodo)) {
                return true;
            }
        }
    }
    return false;
}
void addFuncion(SyntaxNode f){
	if (!arbolFunciones.contains(f))
		arbolFunciones.add(f);
}

public static List<SyntaxNode> getArbolFunciones() {
	return arbolFunciones;
}

public boolean esTipoClaseValido(String nombreClase) {
    String tipo = getTypeSymbolTableVariables(nombreClase);
    return (tipo.equals("Clase"));
}
void verificarReglasCheck() {
    Map<String, Attribute> table = SymbolTable.getInstance().getTableMap();
    for (Map.Entry<String, Attribute> entry : table.entrySet()) {
        Attribute entrada = entry.getValue();
        if (entrada.isEsCheck()) {
            if (entrada.getAmbitosUsoIzquierdo().size() > 1 && entrada.isUsadaDerecho()) {
                logger.logDebugSyntax("La variable: " + entrada.getToken() + " cumple con las condiciones de CHECK");
                System.out.println("La variable: " + entrada.getToken() + " cumple con las condiciones de CHECK");
            } else {
                // Reportar incumplimiento
                logger.logDebugSyntax("La variable: " + entrada.getToken() + " NO cumple con las condiciones de CHECK");
                System.out.println("La variable: " + entrada.getToken() + " NO cumple con las condiciones de CHECK");
            }
        }
    }
}

public void setYylval(ParserVal yylval) {
    this.yylval = yylval;
}

public String getTypeSymbolTable(String lexema){
    var x = valorSimbolo(lexema);
    if (x.equals( "Error"))
        yyerror("La variable no esta declarada");
    return x;
}

private String validarTipos(SyntaxNode obj, SyntaxNode obj1, boolean esAsignacion) {
    if (obj == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": ID no existe");
        return "obj is null";
    }
    if (obj1 == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": ID no existe.");
        return "obj1 is null";
    }
    if (obj.getType() == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": Uno de los tipos es nulo.");
        return "obj type is null";
    }
    if (obj1.getType() == null) {
        yyerror("Linea " + LexicalAnalyzer.getLine() + ": Uno de los tipos es nulo.");
        return "obj1 type is null";
    }

    // Propagación de un error anterior si son distintos o si ya hay un error
    if (obj.getType().equals("Error") || obj1.getType().equals("Error")) {
        Parser.error = true;
        return "Error";
    }

    boolean esEnteroObj = esTipoEntero(obj.getType());
    boolean esEnteroObj1 = esTipoEntero(obj1.getType());
    boolean esFlotanteObj = obj.getType().equals(UsesType.FLOAT);
    boolean esFlotanteObj1 = obj1.getType().equals(UsesType.FLOAT);
    
    if (esEnteroObj && esEnteroObj1) { 
    	if (!obj.getType().equals(obj1.getType()))//determino que es op entre long y ushort
        	return UsesType.LONG;
        else
        	return obj.getType();
    }
    // Manejo de conversiones y errores para asignaciones
    if (esAsignacion) {
        if (esEnteroObj && esFlotanteObj1) {
            yyerror("Incompatibilidad de tipos: no se puede asignar FLOAT a tipo entero en linea " + LexicalAnalyzer.getLine());
            return "Error";
        }
        if (esFlotanteObj && esEnteroObj1) {
            // Conversión implícita de entero a flotante
            return UsesType.FLOAT;
        }
    }

    // Manejo de conversiones y errores para operaciones
    if (!esAsignacion) {
        if ((esEnteroObj && esFlotanteObj1) || (esFlotanteObj && esEnteroObj1)) {
            // Conversión implícita de entero a flotante, tal vez hay que logearlo
            return UsesType.FLOAT;
        }
    }

    // Verificación de igualdad de tipos
    if (!obj.getType().equals(obj1.getType())) {
        yyerror("Los tipos no coinciden en linea " + LexicalAnalyzer.getLine());
        return "Error";
    }

    return obj.getType(); // Retorna el tipo de ambos.
}

private boolean esTipoEntero(String tipo) {
    return tipo.equals(UsesType.CONSTANT) || tipo.equals(UsesType.LONG) || tipo.equals(UsesType.USHORT);
}

public void nuevoAmbito(String ambito){
    Parser.ambito = Parser.ambito + ":" + ambito ;
    this.flagAmbitoCambiado = true;
}

private void checkFlagAmbito() {
	if (flagAmbitoCambiado) {
		flagAmbitoCambiado = false;
		eliminarAmbito();
	}
}

public void eliminarAmbito() {
    if (Parser.ambito.contains(":")) {
        // Elimina la Ãºltima parte del Ã¡mbito
        Parser.ambito = Parser.ambito.substring(0, Parser.ambito.lastIndexOf(':'));
		flagAmbitoCambiado = false;
    }
}

public String valorSimbolo(String lexema) {
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    Optional<Attribute> entrada = tablaSimbolos.getAttribute(lexema);

    if (entrada.isPresent()) {
        return entrada.get().getType();
    } else {
        return "Error";
    }
}

private String getTypeSymbolTableVariables(String sval) {
    String ambitoActual = ":" + Parser.ambito;
    String nombreCompleto = sval + ambitoActual;

    while (!nombreCompleto.isEmpty()) {
        String tipo = valorSimbolo(nombreCompleto);
        if (!tipo.equals("Error")) {
            return tipo;
        }
        // Recortar el ámbito para buscar en el ámbito padre
        if (nombreCompleto.contains(":")) {
            nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
        } else {
            nombreCompleto = "";
        }
    }
    // Si llegamos aquí, significa que no se encontró el tipo de la variable
    if (!falloNombre(sval)) {
		yyerror("El tipo de la variable no fue hallado en un ámbito apropiado");
    }
    return "Error";
}

private String getTypeSymbolTableVariablesEnAcceso(String sval, String sval2) {
    String ambitoActual = ":" + Parser.ambito;
    String nombreCompleto = sval + ambitoActual + ":" +sval2;
    while (!nombreCompleto.isEmpty()) {
        String tipo = valorSimbolo(nombreCompleto);
        if (!tipo.equals("Error")) {
            return tipo;
        }
        // Recortar el ámbito para buscar en el ámbito padre
        if (nombreCompleto.contains(":")) {
            nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
        } else {
            nombreCompleto = "";
        }
    }
    // Si llegamos aquí, significa que no se encontró el tipo de la variable
    if (!falloNombre(sval)) {
		yyerror("El tipo de la variable no fue hallado en un ámbito apropiado");
    }
    return "Error";
}

private Boolean falloNombre(String sval){
      var pila = ":" + Parser.ambito;

      while(!pila.isBlank() && valorSimbolo(sval + pila).equals("Error")){
        pila = pila.substring(1);
        if (pila.contains(":"))
          pila = pila.substring(pila.indexOf(':'));
        else
          pila = "";
      }
      return (valorSimbolo(sval + pila).equals("Error"));
}

private String getNameSymbolTableVariables(String sval) {
    sval = sval.replace("#", "");

    String ambitoActual = ":" + Parser.ambito;
    String nombreCompleto = sval + ambitoActual;
    while (!nombreCompleto.isEmpty()) {
        if (!valorSimbolo(nombreCompleto).equals("Error")) {
            return nombreCompleto;
        }
        // Recortar el ámbito para buscar en el ámbito padre
        if (nombreCompleto.contains(":")) {
            nombreCompleto = nombreCompleto.substring(0, nombreCompleto.lastIndexOf(':'));
        } else {
            nombreCompleto = "";
        }
    }
    // Si llegamos aquí, significa que no se encontró la variable
    yyerror("El tipo de la variable '" + sval + "' no fue hallado en un ámbito apropiado");
    return "Error";
}

public static boolean isNegativeIntInRange(long valor, String type) {
    return switch (type) {
        case (UsesType.USHORT) -> !(valor > 0);
        case (UsesType.LONG) -> !(valor > abs(DelimiterType.minLong));
        default -> !(valor > 0);
    };
}

public static boolean isPositiveIntInRange(long valor, String type) {
    return switch (type) {
        case (UsesType.USHORT) -> !(valor > DelimiterType.maxShort);
        case (UsesType.LONG) -> !(valor > DelimiterType.maxLong);
        default -> !(valor > DelimiterType.maxShort);
    };
}

public String processInteger(String value, boolean isPositive) {
    String suffix = null;
    String keyInMap = value;
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    Optional<Attribute> entrada = tablaSimbolos.getAttribute(value);
    String delimiter = "";

    String[] parts = value.split("_", 2);
    if (value.contains("_")) {
        value = parts[0];
        suffix = parts[1];
    }

    if (entrada.isPresent()) {
        final String type_cte = entrada.get().getType();

        try {
            final long valor = Long.parseLong(value);

            boolean outOfRange;


            if (isPositive) {
                outOfRange = !isPositiveIntInRange(valor, type_cte);
                delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.maxShort) : String.valueOf(DelimiterType.maxLong);
            } else {
                outOfRange = !isNegativeIntInRange(valor, type_cte);
                delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.minShort) : String.valueOf(DelimiterType.minLong);
            }

            if (outOfRange) {
                logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo " + type_cte + " fuera de los rangos permitidos. SerÃ¡ truncado");
                suffix = (suffix == null) ? "" : ("_" + suffix);
                value = delimiter + suffix;
                entrada.get().setToken(value);
                tablaSimbolos.updateKey(keyInMap, value);
            }
        } catch (NumberFormatException e) {
            logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo " + type_cte + " fuera de los rangos permitidos. Serï¿½ truncado");
            suffix = (suffix == null) ? "" : ("_" + suffix);
            value = delimiter + suffix;
            entrada.get().setToken(value);
            tablaSimbolos.updateKey(keyInMap, value);
        }

        if (!value.contains("_"))
            value = value + "_" + suffix;

        return value;
    } else {
        return "Error";
    }
}

public static boolean isFloatInRange(double valor) {
    if (valor == DelimiterType.zero ) {
    	return true;
    }
    return !(valor > DelimiterType.maxFloatPositive || valor < DelimiterType.minFloatPositive);
}

public String processFloat(String value, boolean isPositive) {
    SymbolTable tablaSimbolos = SymbolTable.getInstance();
    String keyInMap = value;

    Optional<Attribute> entrada = tablaSimbolos.getAttribute(value);

    if (entrada.isPresent()) {
        try {
            final double valor = Double.parseDouble(value);

            if (!isFloatInRange(valor)) {
                logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo FLOAT fuera de los rangos permitidos. Serï¿½ truncado");
                value = String.valueOf(isPositive ? DelimiterType.maxFloatPositive : DelimiterType.minFloatNegative);
                entrada.get().setToken(value);
                tablaSimbolos.updateKey(keyInMap, value);
            }
        } catch (NumberFormatException e) {
            logger.logWarningLexical("Warning linea " + LexicalAnalyzer.getLine() + ": constante de tipo FLOAT fuera de los rangos permitidos. Serï¿½ truncado");
            value = String.valueOf(isPositive ? DelimiterType.maxFloatPositive : DelimiterType.minFloatNegative);
            entrada.get().setToken(value);
            tablaSimbolos.updateKey(keyInMap, value);
        }
        return value;
    } else {
        return "Error";
    }
}

public static void yyerror(String mensaje) {
    System.out.println("## ERROR ## " + mensaje);
    Parser.error = true;
}

public static int yylex() {
    int tokenID = 0;
    Reader reader = LexicalAnalyzer.reader;
    LexicalAnalyzer.currentState = 0;

    while (true) {
        try {
            if (LexicalAnalyzer.endOfFile(reader)) {
                    break;
            }
            char character = LexicalAnalyzer.getNextCharWithoutAdvancing(reader);
            tokenID = LexicalAnalyzer.changeState(reader, character);
            if (tokenID != SemanticAction.active_state) {
                Parser parser = LexicalAnalyzer.getParser();
                parser.yylval = new ParserVal(LexicalAnalyzer.currentToken.toString());
                LexicalAnalyzer.currentToken.delete(0, LexicalAnalyzer.currentToken.length());
                return tokenID;
            }
       } catch (IOException e) {
                e.printStackTrace();
       }
    }
    return tokenID;
}
//#line 1076 "Parser.java"
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
//#line 29 "grammar.y"
{ padre = new SyntaxNode("root", (SyntaxNode) val_peek(1).obj , null);
            					verificarReglasCheck();}
break;
case 3:
//#line 32 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 4:
//#line 33 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un {.");}
break;
case 5:
//#line 37 "grammar.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 38 "grammar.y"
{ if ((SyntaxNode) val_peek(0).obj != null)
            							yyval = new ParserVal(new SyntaxNode("Bloque de sentencias", (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj)); }
break;
case 7:
//#line 42 "grammar.y"
{yyval = val_peek(0);}
break;
case 8:
//#line 43 "grammar.y"
{ if ((SyntaxNode) val_peek(0).obj != null)
      										yyval = new ParserVal(new SyntaxNode("Bloque de sentencias21", (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj)); }
break;
case 9:
//#line 48 "grammar.y"
{ yyval = val_peek(1); }
break;
case 11:
//#line 50 "grammar.y"
{ /*$$ = $1;*/
                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                }
break;
case 12:
//#line 53 "grammar.y"
{ logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','."); }
break;
case 13:
//#line 57 "grammar.y"
{ yyval = val_peek(0);}
break;
case 14:
//#line 58 "grammar.y"
{ yyval = val_peek(0);}
break;
case 15:
//#line 59 "grammar.y"
{ yyval = val_peek(0);}
break;
case 16:
//#line 60 "grammar.y"
{ yyval = val_peek(0);}
break;
case 17:
//#line 61 "grammar.y"
{ yyval = val_peek(0);}
break;
case 18:
//#line 65 "grammar.y"
{ yyval = val_peek(0);}
break;
case 19:
//#line 66 "grammar.y"
{ yyval = val_peek(0);}
break;
case 20:
//#line 67 "grammar.y"
{ yyval = val_peek(0);}
break;
case 21:
//#line 74 "grammar.y"
{
                              logger.logDebugSyntax("Declaración1 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 24:
//#line 82 "grammar.y"
{yyval = val_peek(0);}
break;
case 25:
//#line 83 "grammar.y"
{yyval = val_peek(0);}
break;
case 26:
//#line 84 "grammar.y"
{yyval = val_peek(0);}
break;
case 27:
//#line 85 "grammar.y"
{yyval = val_peek(0);}
break;
case 28:
//#line 91 "grammar.y"
{
			        var t = SymbolTable.getInstance();
			        String tipoVariable = val_peek(1).sval; 
			        for (String varName : lista_variables) {
			            String nombreCompleto = varName;
			            var entrada = t.getAttribute(nombreCompleto);
			            if (entrada.isPresent()) {
			                if (entrada.get().getType() == null) {
			                    entrada.get().setUso(UsesType.VARIABLE);
			                    entrada.get().setType(tipoVariable); /* Asigna el tipo correcto*/
			                    entrada.get().setEsCheck(true);
			                } else {
			                    yyerror("La variable declarada ya existe " + (varName.contains(":") ? varName.substring(0, varName.indexOf(':')) : "en ambito global"));
			                }
			             }
		                 else {
			                /* Agregar la variable con CHECK a la tabla de símbolos*/
			                t.insertAttribute(new Attribute(Parser.ID, nombreCompleto, tipoVariable, UsesType.VARIABLE, LexicalAnalyzer.getLine(), true));
			            }
			        }
			        lista_variables.clear();
		        }
break;
case 29:
//#line 113 "grammar.y"
{
                    var t = SymbolTable.getInstance();
                    String tipoVariable = val_peek(1).sval;
                    for (String varName : lista_variables) {
                        String nombreCompleto = varName;
                        var entrada = t.getAttribute(nombreCompleto);
                        if (entrada.isPresent()) {
                            if (entrada.get().getType() == null) {
                                entrada.get().setUso(UsesType.VARIABLE);
                                entrada.get().setType(tipoVariable); /* Asigna el tipo correcto*/
                            } else {
                                yyerror("La variable declarada ya existe " + (varName.contains(":") ? varName.substring(0, varName.indexOf(':')) : "en ambito global"));
                            }
                        } else {
                            t.insertAttribute(new Attribute(Parser.ID, nombreCompleto, tipoVariable, UsesType.VARIABLE, LexicalAnalyzer.getLine()));
                        }
                    }
                    lista_variables.clear();
		        
		    }
break;
case 30:
//#line 133 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la variable.");
	       	}
break;
case 31:
//#line 139 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode("ListaVariables", (SyntaxNode)val_peek(2).obj, new SyntaxNode(val_peek(0).sval)));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 32:
//#line 143 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 33:
//#line 151 "grammar.y"
{
                    logger.logDebugSyntax("Asignacion en la linea " + LexicalAnalyzer.getLine());

                    var symbolType = getTypeSymbolTableVariables(val_peek(2).sval);

                    if (!symbolType.equals("Error")) {
                        String nombreCompleto = getNameSymbolTableVariables(val_peek(2).sval);

                        if (!nombreCompleto.equals("Error")) {
                            SyntaxNode leftSyntaxNode = new SyntaxNode(nombreCompleto, symbolType);
                            SyntaxNode rightSyntaxNode = (SyntaxNode) val_peek(0).obj;

                            String tipo_validado = validarTipos(leftSyntaxNode, rightSyntaxNode, true);

                            if (!tipo_validado.equals("Error")) {

                                var asign = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);
                                asign.setType(tipo_validado);
                                yyval = new ParserVal(asign);

                                var t = SymbolTable.getInstance();

                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent()) {
                                    entrada.get().addAmbito(ambito);
                                }
                            }
                        }
                    }
            }
break;
case 34:
//#line 181 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 35:
//#line 184 "grammar.y"
{
                    logger.logDebugSyntax("Asignación en la linea " + LexicalAnalyzer.getLine());
            }
break;
case 36:
//#line 187 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 37:
//#line 193 "grammar.y"
{
                    logger.logDebugSyntax("Incremento en la linea " + LexicalAnalyzer.getLine());

                    var symbolType = getTypeSymbolTableVariables(val_peek(1).sval);
                    String nombreCompleto = getNameSymbolTableVariables(val_peek(1).sval);

                    if (!symbolType.equals("Error") && !nombreCompleto.equals("Error")) {
                        SyntaxNode variableNode = new SyntaxNode(nombreCompleto, symbolType);
			            /* Determinar el valor de incremento basado en el tipo de la variable*/
			            String incrementValue;
			            switch (symbolType) {
			                case UsesType.LONG:
			                    incrementValue = "1_l";
			                    break;
			                case UsesType.FLOAT:
			                    incrementValue = "1.0";
			                    break;
			                case UsesType.USHORT:
			                    incrementValue = "1_us";
			                    break;
			                default:
			                    incrementValue = "1"; /* Valor por defecto si no es ninguno de los anteriores*/
			                    break;
			            }
			
			            SyntaxNode incrementValueNode = new SyntaxNode(incrementValue, UsesType.CONSTANT); /* Nodo para el valor de incremento*/
			            SyntaxNode incrementNode = new SyntaxNode("++", variableNode, incrementValueNode, symbolType);

                        yyval = new ParserVal(incrementNode);

                        var t = SymbolTable.getInstance();
                        t.insertAttribute(new Attribute (CTE, incrementValue, symbolType, symbolType, LexicalAnalyzer.getLine()));
                        
                        var entrada = t.getAttribute(nombreCompleto);

                        if (entrada.isPresent()){
                            entrada.get().addAmbito(ambito);
                            entrada.get().setUsadaDerecho(true);
                        }
                    }
		    }
break;
case 38:
//#line 238 "grammar.y"
{
                    String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                    if (!tipo_validado.equals("Error")) {

                        yyval = new ParserVal(new SyntaxNode("+", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj, tipo_validado));

                        if ((SyntaxNode) val_peek(0).obj != null && ((SyntaxNode) val_peek(0).obj).isLeaf()){
                            String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)val_peek(0).obj).getName());

                            if (!nombreCompleto.equals("Error")) {
                                var t = SymbolTable.getInstance();
                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent())
                                    entrada.get().setUsadaDerecho(true);
                            }
                        }
                    }
			}
break;
case 39:
//#line 257 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {
                            yyval = new ParserVal(new SyntaxNode("-", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj, tipo_validado));

                            if ((SyntaxNode) val_peek(0).obj != null && ((SyntaxNode) val_peek(0).obj).isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)val_peek(0).obj).getName());

                                if (!nombreCompleto.equals("Error")) {
                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent())
                                        entrada.get().setUsadaDerecho(true);
                                }
                            }
                        }
			}
break;
case 40:
//#line 275 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 41:
//#line 276 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 42:
//#line 277 "grammar.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 281 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {
                            var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                            x.setType(tipo_validado);
                            yyval = new ParserVal(x);

                            if ((SyntaxNode) val_peek(0).obj != null && ((SyntaxNode) val_peek(0).obj).isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)val_peek(0).obj).getName());

                                if (!nombreCompleto.equals("Error")) {
                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent())
                                        entrada.get().setUsadaDerecho(true);
                                }
                            }
                        }
            }
break;
case 44:
//#line 301 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {
                            var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                            x.setType(tipo_validado);
                            yyval = new ParserVal(x);

                            if ((SyntaxNode) val_peek(0).obj != null && ((SyntaxNode) val_peek(0).obj).isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)val_peek(0).obj).getName());

                                if (!nombreCompleto.equals("Error")) {
                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent())
                                        entrada.get().setUsadaDerecho(true);
                                }
                            }
                        }
            }
break;
case 45:
//#line 321 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
   			}
break;
case 46:
//#line 324 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
            }
break;
case 47:
//#line 327 "grammar.y"
{
   			            /* $$ = $1;*/
   			            SyntaxNode node = (SyntaxNode) val_peek(1).obj;

                        if (node != null && node.isLeaf()){
                            String nombreCompleto = getNameSymbolTableVariables(node.getName());
                            /*String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)$1.obj).getName());*/

                            if (!nombreCompleto.equals("Error")) {
                                yyval = val_peek(0);

                                var t = SymbolTable.getInstance();
                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent())
                                    entrada.get().setUsadaDerecho(true);
                            }
                        }
   			}
break;
case 48:
//#line 348 "grammar.y"
{yyval = val_peek(0);}
break;
case 49:
//#line 349 "grammar.y"
{yyval = val_peek(1);}
break;
case 50:
//#line 350 "grammar.y"
{
      		            String value = val_peek(0).sval;

      		            value = processInteger(value, false);

      		            if (!value.equals("Error")) {
      		                var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                yyval = new ParserVal(new SyntaxNode(value, symbolType)); /* Crear un nodo para la constante*/
                            }
      		            }

      		}
break;
case 51:
//#line 364 "grammar.y"
{
                        String value = val_peek(0).sval;

                        value = processFloat(value, false);

                        if (!value.equals("Error")) {
                            var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                yyval = new ParserVal(new SyntaxNode(value, symbolType)); /* Crear un nodo para la constante*/
                            }
                        }
            }
break;
case 52:
//#line 377 "grammar.y"
{
                         var type = getTypeSymbolTableVariables(val_peek(0).sval);
                         var name = getNameSymbolTableVariables(val_peek(0).sval);

                         if (!type.equals("Error") && !name.equals("Error"))
                             yyval =  new ParserVal( new SyntaxNode(name, type));
                         else{
                           /*  $$ =  new ParserVal( new SyntaxNode("parametroNoEncontrado", type));*/
                             yyerror("No se encontro esta variable en un ambito adecuado");}
            }
break;
case 53:
//#line 387 "grammar.y"
{
                        String value = val_peek(0).sval;

                        value = processInteger(value, true);

                        if (!value.equals("Error")) {
                            var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                yyval = new ParserVal(new SyntaxNode(value, symbolType)); /* Crear un nodo para la constante*/
                            }
			            }
			}
break;
case 54:
//#line 400 "grammar.y"
{
      		            String value = val_peek(0).sval;

                        value = processFloat(value, true);

                        if (!value.equals("Error")) {
                            var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                yyval = new ParserVal(new SyntaxNode(value, symbolType)); /* Crear un nodo para la constante*/
                            }
				        }
	        }
break;
case 55:
//#line 413 "grammar.y"
{
                        yyval=val_peek(0);
            }
break;
case 56:
//#line 416 "grammar.y"
{
                        var type = getTypeSymbolTableVariables(val_peek(0).sval);
                        var name = getNameSymbolTableVariables(val_peek(0).sval);

                        if (!type.equals("Error") && !name.equals("Error"))
                            yyval =  new ParserVal( new SyntaxNode(name, type));
                        else{
                          /*  $$ =  new ParserVal( new SyntaxNode("parametroNoEncontrado", type));*/
                            yyerror("No se encontro esta variable en un ambito adecuado");}
            }
break;
case 57:
//#line 430 "grammar.y"
{ yyval = new ParserVal("=="); }
break;
case 58:
//#line 431 "grammar.y"
{ yyval = new ParserVal("<="); }
break;
case 59:
//#line 432 "grammar.y"
{ yyval = new ParserVal(">="); }
break;
case 60:
//#line 433 "grammar.y"
{ yyval = new ParserVal("<"); }
break;
case 61:
//#line 434 "grammar.y"
{ yyval = new ParserVal(">"); }
break;
case 62:
//#line 435 "grammar.y"
{ yyval = new ParserVal("!!"); }
break;
case 63:
//#line 436 "grammar.y"
{
			        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": no puede realizarse una asignacion. Quizas queria poner '=='?");
			        }
break;
case 64:
//#line 442 "grammar.y"
{
                                    String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);
                                    if (!tipo_validado.equals("Error")) {
                                        yyval = new ParserVal(new SyntaxNode(val_peek(1).sval, (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, tipo_validado));
                                    }
            }
break;
case 65:
//#line 448 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 66:
//#line 449 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 67:
//#line 453 "grammar.y"
{ yyval = val_peek(1); }
break;
case 68:
//#line 454 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '()'.");}
break;
case 69:
//#line 455 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '('.");}
break;
case 70:
//#line 456 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un ')'."); yyval = val_peek(0); }
break;
case 71:
//#line 457 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta establecer una condicion.");}
break;
case 72:
//#line 461 "grammar.y"
{
			    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(1).obj, null);
                yyval = new ParserVal(new SyntaxNode("IF", (SyntaxNode)val_peek(2).obj, thenSyntaxNode, null));
                logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
                                                                 }
break;
case 73:
//#line 466 "grammar.y"
{ /* Usar "then" para distinguir los caminos... Then es el camino principal!*/
		        SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(3).obj, null);
		        SyntaxNode elseSyntaxNode = new SyntaxNode("ELSE", (SyntaxNode)val_peek(1).obj, null);
		        SyntaxNode camino = new SyntaxNode("Camino", thenSyntaxNode, elseSyntaxNode);
		        SyntaxNode ifSyntaxNode = new SyntaxNode("IF", (SyntaxNode) val_peek(4).obj, camino);

		
		        /* Crear un nodo 'if' con la condición y los nodos 'then' y 'else' como hijos*/
		        yyval = new ParserVal(ifSyntaxNode);
		        logger.logDebugSyntax("Bloque IF-ELSE en la linea " + LexicalAnalyzer.getLine());
		    }
break;
case 74:
//#line 477 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 75:
//#line 478 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 76:
//#line 479 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");}
break;
case 77:
//#line 480 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");}
break;
case 78:
//#line 484 "grammar.y"
{ yyval = val_peek(0); }
break;
case 79:
//#line 485 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 80:
//#line 486 "grammar.y"
{ yyval = val_peek(1) ; }
break;
case 81:
//#line 487 "grammar.y"
{
                                                                    yyval = val_peek(1) ;
                                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");
                                                                    }
break;
case 82:
//#line 491 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 83:
//#line 495 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 84:
//#line 496 "grammar.y"
{yyval = val_peek(0);}
break;
case 85:
//#line 500 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("SecuenciaSentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj)); }
break;
case 86:
//#line 501 "grammar.y"
{ yyval = val_peek(0); }
break;
case 87:
//#line 505 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 88:
//#line 509 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 89:
//#line 512 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
            }
break;
case 90:
//#line 520 "grammar.y"
{
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    String nombreFuncion = val_peek(3).sval;
                    arbolFunciones.add(new SyntaxNode("Raiz de la funcion: " + nombreFuncion, (SyntaxNode) val_peek(1).obj, null));
                    checkFlagAmbito();
            }
break;
case 91:
//#line 526 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '}'");
            }
break;
case 92:
//#line 529 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '{'");
            }
break;
case 93:
//#line 535 "grammar.y"
{
		        logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
		        var t = SymbolTable.getInstance();
		        var entradaFuncion = t.getAttribute(val_peek(3).sval + ":" + Parser.ambito);
		        if (entradaFuncion.isPresent()) {
		            if (entradaFuncion.get().getUso() != null) {
		                yyerror("El nombre de la funcion " + val_peek(3).sval + " ya fue utilizado en este ambito");
		            } else {
		                entradaFuncion.get().setUso(UsesType.FUNCTION);
		            }
		        } else {
		            t.insertAttribute(new Attribute(Parser.ID, val_peek(3).sval + ":" + Parser.ambito, UsesType.FUNCTION, UsesType.FUNCTION, LexicalAnalyzer.getLine()));
		        }
		
		        /* Agregar el parámetro a la tabla de símbolos*/
		        if (val_peek(1).obj != null) {
		            SyntaxNode parametroNode = (SyntaxNode) val_peek(1).obj;
		            String nombreParametro = parametroNode.getName();
		            String tipoParametro = parametroNode.getType();
		            t.insertAttribute(new Attribute(Parser.ID, nombreParametro + ":" + Parser.ambito + ":" + val_peek(3).sval, tipoParametro, UsesType.FUNCTION, LexicalAnalyzer.getLine()));
		        }
		
		        nuevoAmbito(val_peek(3).sval);
		    }
break;
case 94:
//#line 559 "grammar.y"
{
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    var t = SymbolTable.getInstance();
                    var entradaFuncion = t.getAttribute(val_peek(2).sval + ":" + Parser.ambito);
                    if (entradaFuncion.isPresent()) {
                        if (entradaFuncion.get().getUso() != null) {
                            yyerror("El nombre de la funcion " + val_peek(2).sval + " ya fue utilizado en este ambito");
                        } else {
                            entradaFuncion.get().setUso(UsesType.FUNCTION);
                        }
                    } else {
                        t.insertAttribute(new Attribute(Parser.ID, val_peek(2).sval + ":" +  Parser.ambito, UsesType.FUNCTION, UsesType.FUNCTION, LexicalAnalyzer.getLine()));
                    }
                    String nombreFuncionSyntaxNode = val_peek(2).sval + ":" + Parser.ambito;
                    yyval.sval = nombreFuncionSyntaxNode;
                    nuevoAmbito(val_peek(2).sval);
            }
break;
case 95:
//#line 576 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 96:
//#line 579 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 97:
//#line 582 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 98:
//#line 585 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 99:
//#line 588 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion.");
            }
break;
case 100:
//#line 594 "grammar.y"
{ yyval = val_peek(0); }
break;
case 101:
//#line 595 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": Parametro mal declarado. Solo es posible declarar un único parámetro");}
break;
case 102:
//#line 599 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval, val_peek(1).sval));
            }
break;
case 103:
//#line 602 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el tipo del parametro.");
            }
break;
case 104:
//#line 605 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre del parametro.");
            }
break;
case 105:
//#line 611 "grammar.y"
{
        SyntaxNode sentenciasSyntaxNode = (SyntaxNode)val_peek(1).obj;
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        if (sentenciasSyntaxNode != null)
        	yyval = new ParserVal(new SyntaxNode("Bloque de sentencias1", sentenciasSyntaxNode, returnSyntaxNode));
    }
break;
case 106:
//#line 617 "grammar.y"
{
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias2", null, returnSyntaxNode));
    }
break;
case 107:
//#line 621 "grammar.y"
{
        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una sentencia 'RETURN' en el bloque 'void'.");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias3", (SyntaxNode)val_peek(0).obj, null));
    }
break;
case 108:
//#line 627 "grammar.y"
{
        /* Aquí, simplemente expandimos la secuencia de sentencias sin un nodo intermedio*/
        yyval = new ParserVal(new SyntaxNode("Sentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
    }
break;
case 109:
//#line 631 "grammar.y"
{
        yyval = val_peek(0);
    }
break;
case 110:
//#line 637 "grammar.y"
{ yyval = val_peek(0); }
break;
case 111:
//#line 638 "grammar.y"
{ yyval = val_peek(0); }
break;
case 112:
//#line 639 "grammar.y"
{ yyval = val_peek(0); }
break;
case 113:
//#line 640 "grammar.y"
{ yyval = val_peek(0); }
break;
case 114:
//#line 641 "grammar.y"
{ yyval = val_peek(0); }
break;
case 115:
//#line 642 "grammar.y"
{ yyval = val_peek(0); }
break;
case 117:
//#line 647 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 119:
//#line 652 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 120:
//#line 656 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 121:
//#line 660 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 122:
//#line 663 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 123:
//#line 670 "grammar.y"
{
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("if", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(1).obj, null));
            }
break;
case 124:
//#line 674 "grammar.y"
{
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());

                    /* Crear nodos para 'then' y 'else'*/
                    SyntaxNode thenSyntaxNode = new SyntaxNode("then", (SyntaxNode)val_peek(3).obj, null);
                    SyntaxNode elseSyntaxNode = new SyntaxNode("else", (SyntaxNode)val_peek(1).obj, null);

                    /* Crear un nodo para el cuerpo del 'if' que contiene 'then' y 'else'*/
                    SyntaxNode cuerpoIf = new SyntaxNode("cuerpoIf", thenSyntaxNode, elseSyntaxNode);

                    /* Crear el nodo 'if' con la condici?n y el cuerpo como hijos*/
                    yyval = new ParserVal(new SyntaxNode("if", (SyntaxNode)val_peek(4).obj, cuerpoIf));
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
            }
break;
case 125:
//#line 688 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 126:
//#line 691 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 127:
//#line 694 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");
            }
break;
case 128:
//#line 697 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");
            }
break;
case 129:
//#line 703 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 130:
//#line 704 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias4", null, returnSyntaxNode));
		    }
break;
case 131:
//#line 708 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias5", null, returnSyntaxNode));
		    }
break;
case 132:
//#line 712 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj)); }
break;
case 133:
//#line 713 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(2).obj)); }
break;
case 134:
//#line 714 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 135:
//#line 718 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(0).obj)); }
break;
case 136:
//#line 719 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 137:
//#line 724 "grammar.y"
{ 
            		yyval = val_peek(1);
            		eliminarAmbito(); /* Restaura el ámbito anterior al salir de la clase*/
            		}
break;
case 138:
//#line 728 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 140:
//#line 731 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 141:
//#line 734 "grammar.y"
{
            			logger.logDebugSyntax("FORWARD DECLARATION en la linea " + LexicalAnalyzer.getLine());}
break;
case 142:
//#line 737 "grammar.y"
{
		        logger.logDebugSyntax("CLASE en la linea " + LexicalAnalyzer.getLine());
		        String nombreClase = val_peek(0).sval + ":" + Parser.ambito;
		
		        /* Registrar la clase en la tabla de sÃ­mbolos*/
		        SymbolTable tablaSimbolos = SymbolTable.getInstance();
		        tablaSimbolos.insertAttribute(new Attribute(Parser.ID, nombreClase, "Clase", UsesType.CLASE, LexicalAnalyzer.getLine()));
		
		        nuevoAmbito(val_peek(0).sval); /* Agrega el nuevo Ã¡mbito de la clase*/
		    }
break;
case 143:
//#line 747 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");}
break;
case 144:
//#line 751 "grammar.y"
{
                              logger.logDebugSyntax("Declaración2 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 147:
//#line 756 "grammar.y"
{
                        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": la sentencia declarada no es permitida fuera de un metodo.");
                        }
break;
case 149:
//#line 764 "grammar.y"
{
                                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                             }
break;
case 150:
//#line 767 "grammar.y"
{
                                                    }
break;
case 151:
//#line 769 "grammar.y"
{
                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                                   }
break;
case 152:
//#line 772 "grammar.y"
{
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentenci7as" , (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj));
		    }
break;
case 153:
//#line 775 "grammar.y"
{yyval = val_peek(1);}
break;
case 154:
//#line 776 "grammar.y"
{
                                     logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                     yyval = val_peek(1);
                                  }
break;
case 155:
//#line 783 "grammar.y"
{ logger.logDebugSyntax("Herencia por composicion en la linea " + LexicalAnalyzer.getLine());}
break;
case 156:
//#line 787 "grammar.y"
{
			                            logger.logDebugSyntax("Declaración 3de variables en la linea " + LexicalAnalyzer.getLine());
			                                    if (!esTipoClaseValido(val_peek(1).sval)) {
										            yyerror("Tipo de clase no declarado: " + val_peek(1).sval);
										        }
										        else {
								                    var t = SymbolTable.getInstance();
								                    String tipoVariable = val_peek(1).sval;
								                    for (String varName : lista_variables) {
								                        String nombreCompleto = varName;
                            							t.insertAttribute(new Attribute(Parser.ID, nombreCompleto, tipoVariable, UsesType.VARIABLE, LexicalAnalyzer.getLine()));
													}
                    								lista_variables.clear();
										        }
			                          }
break;
case 157:
//#line 805 "grammar.y"
{
		        logger.logDebugSyntax("Acceso en la linea " + LexicalAnalyzer.getLine());

		        String varType = getTypeSymbolTableVariablesEnAcceso(val_peek(0).sval, val_peek(2).sval);

		        SyntaxNode objetoNode = new SyntaxNode(val_peek(2).sval, "Clase");

		        if (!varType.equals("Error")) {
		            SyntaxNode propiedadNode = new SyntaxNode(val_peek(0).sval, varType);
		            SyntaxNode accesoNode = new SyntaxNode("Acceso", objetoNode, propiedadNode, varType);
		            yyval = new ParserVal(accesoNode);
		        }

		    }
break;
case 158:
//#line 819 "grammar.y"
{
		        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");
		    }
break;
case 159:
//#line 828 "grammar.y"
{
                        logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
        				String nombreMetodo = getNameSymbolTableVariables(val_peek(2).sval);
                        if (!nombreMetodo.equals("Error")) {
				            if (metodoExiste(Parser.ambito, nombreMetodo)) {
				            	SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoMetodo", new SyntaxNode(nombreMetodo));
				                logger.logDebugSyntax("Llamado a método en la linea " + LexicalAnalyzer.getLine());
                        		yyval = new ParserVal(llamadaSyntaxNode);
				            	
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe " );
				            }

                        }
		    }
break;
case 160:
//#line 843 "grammar.y"
{
                        /*SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion", new SyntaxNode($1.sval), (SyntaxNode)$3.obj);*/
				        String nombreInstancia = getNameSymbolTableVariables(val_peek(4).sval);
				        String tipoClase = getTypeSymbolTableVariables(nombreInstancia);
                        if (!tipoClase.equals("Error")) {
				            /* Verificar si el método existe en la clase*/
				            String nombreMetodo = val_peek(2).sval;
				            if (metodoExisteEnClase(tipoClase, nombreMetodo)) {
				                SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoMetodoClase", new SyntaxNode(nombreInstancia), new SyntaxNode(nombreMetodo));
				                logger.logDebugSyntax("Llamado a método de clase en la linea " + LexicalAnalyzer.getLine());
				                yyval = new ParserVal(llamadaSyntaxNode);
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe en la clase " + tipoClase);
				            }
				        } else {
				            yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": " + nombreInstancia + " no es una instancia de una clase.");
				        }
		    }
break;
case 161:
//#line 866 "grammar.y"
{
                                    logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
                                    yyval = new ParserVal( new SyntaxNode("Print", new SyntaxNode(val_peek(0).sval, "CADENA"), null, "CADENA"));
                               }
break;
case 162:
//#line 870 "grammar.y"
{
			                        logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
			                        yyval = new ParserVal( new SyntaxNode("Print", (SyntaxNode) val_peek(0).obj, null, "Factor" ));
			                     }
break;
case 163:
//#line 874 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el contenido de la impresion.");}
break;
//#line 2372 "Parser.java"
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
