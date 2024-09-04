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
import AnalizadorLexico.Enums.TokenType;
import AnalizadorLexico.SemanticActions.SemanticAction;
import AnalizadorLexico.Enums.DelimiterType;
import AnalizadorLexico.Enums.UsesType;
import ArbolSintactico.SyntaxNode;
import java.util.HashMap;
//#line 36 "Parser.java"




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
   11,   11,    9,    9,    9,
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
    3,    2,    2,    2,    1,    2,    3,    3,    3,    4,
    5,    6,    2,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   25,   26,   24,    0,   27,
    0,    0,    0,    0,    5,    7,    0,    0,   13,   14,
   15,   16,   17,   18,    0,   20,   21,   22,   23,    0,
    0,    0,  139,   32,   37,    0,    0,    0,    0,    0,
    0,    0,    0,   78,    0,   77,   79,  143,    0,  165,
    0,   53,  163,   54,    0,   48,   55,  164,    0,    0,
    0,    0,    2,    0,    4,    6,    8,   11,    9,   12,
   10,    0,   30,    0,    0,    0,    0,   34,    0,    0,
   47,  159,    0,  158,    0,    0,    0,    0,  119,  118,
   86,    0,    0,    0,   71,    0,    0,  141,    0,    0,
   50,   51,   49,  103,    0,    0,   98,    0,   95,    0,
    0,   89,    0,    1,   36,    0,    0,    0,  115,  110,
  113,  114,  106,    0,    0,  109,    0,  111,  112,    0,
    0,  147,  144,  145,    0,    0,  146,    0,    0,    0,
    0,  160,    0,   31,   58,   59,   57,   62,   63,   60,
   61,    0,   69,    0,   82,   80,   85,   83,   67,   75,
   72,    0,   76,  157,   94,    0,  102,    0,   96,   88,
   87,    0,    0,  130,  129,  128,    0,   92,  105,  108,
  117,  116,   91,   90,  138,  137,  152,    0,    0,  154,
  153,   40,    0,   41,    0,   45,   43,   46,   44,  161,
    0,   65,    0,   81,    0,   93,  101,    0,    0,    0,
    0,    0,  122,  151,  150,  149,  148,  162,   74,   73,
  131,    0,  135,  134,  132,  126,  123,    0,  127,  121,
  120,  133,    0,  125,  124,
};
final static short yydgoto[] = {                         13,
   14,   44,   16,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,   29,   30,   39,   87,   80,
   81,  152,   88,   45,   46,   47,   93,   94,   31,  124,
  107,  108,  125,  175,  127,  128,  129,  176,  210,   32,
  135,   33,  136,  137,
};
final static short yysindex[] = {                       493,
  -27,  -40, -164,  207,  -38,    0,    0,    0,  -34,    0,
  -70,  582,    0,  606,    0,    0,   -6,    3,    0,    0,
    0,    0,    0,    0,  -31,    0,    0,    0,    0,   -9,
 -119,  -95,    0,    0,    0,  217,   15, -149,  -14,  231,
    4,  778,   89,    0,  510,    0,    0,    0,    5,    0,
  -39,    0,    0,    0, -214,    0,    0,    0,  160,  180,
  332, -236,    0,  626,    0,    0,    0,    0,    0,    0,
    0,  244,    0,  -14,  817,  817,  849,    0,   -4,  -15,
    0,    0,   11,    0,   24, -202,  450,   17,    0,    0,
    0,  833, -115,  778,    0,   26,  316,    0, -145,   28,
    0,    0,    0,    0,  193, -172,    0,   43,    0,   56,
  380,    0,  -14,    0,    0,   -4,  -24,  -34,    0,    0,
    0,    0,    0,  -11,  817,    0,    7,    0,    0, -102,
  -27,    0,    0,    0,  559,   21,    0,  248,  266,  272,
  275,    0,  138,    0,    0,    0,    0,    0,    0,    0,
    0,  297,    0,   -8,    0,    0,    0,    0,    0,    0,
    0,  510,    0,    0,    0,   82,    0, -124,    0,    0,
    0,  817,  533,    0,    0,    0,  397,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   54,   65,    0,
    0,    0,  -15,    0,  -15,    0,    0,    0,    0,    0,
   94,    0,   -4,    0, -219,    0,    0,  643,  817, -100,
  363,  414,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   22,    0,    0,    0,    0,    0,  533,    0,    0,
    0,    0, -190,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -118,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   66,    0,    0,    0,    0,    0,
    0,   18,    0,    0,    0,    0,    0,    0,   87,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
   34,    0,    0,    0,    0,    0,    0,    0, -114,    0,
    0,    0,    0,  150,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  104,    0,    0,    0,    0,  111,   84,
    0,    0,    0,    0,  -41,    0,  165,  434,    0,    0,
    0,    0,    0,  -92,    0,  470,    0,    0,    0,   59,
    0,    0,    0,    0,    0,  -33,    0,  -10,    0,    0,
    0,    0,  114,    0,    0,  115,    0,    0,    0,    0,
    0,    0,    0,    0,  -89,    0,    0,    0,    0,    0,
  128,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  108,    0,  133,    0,    0,    0,    0,    0,
    0,    0,  212,    0,    0,    0,    0,    0,  -85,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  154,  913,   -2,  -62,  742,  801,    0,    0,  812,  822,
    0,  920,  928,  -55,   38,  -51,   80,   -1,   10,  -23,
   20,    0,  132,   63,   25,  441,   92,    0,    0,  110,
  -46,    0, -155,   -7,    0,    0,    0, -123,  -20,    0,
    0,    0,   55,    0,
};
final static int YYTABLESIZE=1128;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
  142,   60,  157,   76,   99,   43,   99,  104,   97,  156,
  104,   67,   37,  110,  132,   43,  209,  140,   38,  157,
   34,  133,  184,   58,  225,  134,  140,   77,   74,   72,
  100,  141,   84,   36,   73,  107,  219,   69,  138,  136,
  139,  220,  100,  101,   86,   79,   71,   90,   98,  211,
  182,  142,  209,  213,  144,   82,   83,  153,  166,   55,
  113,   67,  102,  143,  191,  234,  159,  126,  126,   97,
  235,   61,  132,   99,   56,   56,   56,   56,   56,  133,
   56,  116,   42,  134,  167,  112,  168,  229,  231,  104,
   62,   48,   49,   56,   56,   56,  169,  215,  172,   52,
   52,   52,   52,   52,  233,   52,   84,   85,  217,   19,
   84,  164,  100,  178,  193,  195,  204,  180,   52,   52,
   52,  163,  206,  142,   42,  142,   42,   42,   42,   95,
  156,  207,  187,   55,  218,  171,   75,   99,  106,  106,
  155,   97,  140,   42,   42,   42,  232,   29,   38,    3,
   38,   38,   38,  183,   33,  224,   56,   28,   35,  197,
  199,  203,  201,   84,  126,   64,  107,   38,   38,   38,
  136,  155,  188,   39,   96,   39,   39,   39,  200,  173,
  177,   52,   55,  154,  106,  130,  205,  222,    0,  189,
    0,    0,   39,   39,   39,    6,    7,    8,    0,  105,
  126,  180,    0,    0,    0,   66,   42,   10,    0,    0,
    0,    0,    0,    0,  157,   40,    1,    0,   59,    2,
  109,   40,  104,    4,    5,    6,    7,    8,    9,   34,
   38,   40,    1,  165,   41,  117,   35,   10,   11,    4,
    5,    6,    7,    8,  118,  100,    0,   34,   35,   68,
   41,   55,   64,   10,   11,   39,  142,  142,   70,   89,
  142,   55,  181,  142,  142,  142,  142,  142,  142,  142,
    0,   51,   52,  140,  140,   55,  190,  140,  142,  142,
  140,  140,  140,  140,  140,  140,  140,   66,   55,   56,
   56,   54,   55,   56,    0,  140,  140,   56,   56,   56,
   56,   56,   56,   56,   56,   56,   56,   56,   56,  214,
   55,   56,   56,    0,   52,   52,   55,    0,   52,   55,
  216,   19,   52,   52,   52,   52,   52,   52,   52,   52,
   52,   52,   52,   52,   64,    0,   52,   52,    0,   42,
   42,   55,  156,   42,    0,   51,   52,   42,   42,   42,
   42,   42,   42,   42,   42,   42,   42,   42,   42,   29,
    0,   42,   42,   38,   38,   54,   33,   38,    0,   28,
   35,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,   38,   38,  155,    0,   38,   38,    0,   39,   39,
    0,    0,   39,    0,   51,   52,   39,   39,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,    0,    0,
   39,   39,    0,    0,   54,    0,  104,    0,    0,    0,
    0,   66,    0,    0,   66,    6,    7,    8,   66,   66,
   66,   66,   66,   66,   66,    0,  104,   10,   42,   66,
    0,    0,   66,   66,    0,    6,    7,    8,    0,  104,
    0,    0,    0,    0,   42,    0,    0,   10,    6,    7,
    8,    0,   50,   51,   52,   53,    0,    0,   64,    0,
   10,   64,   78,   51,   52,   64,   64,   64,   64,   64,
   64,   64,   92,   54,    0,  172,   64,   51,   52,   64,
   64,    0,  138,   54,  139,    0,    0,    0,    0,  115,
   51,   52,   42,  192,   51,   52,    0,   54,    0,  150,
  149,  151,    0,    0,    0,  123,  123,    0,    0,  172,
   54,  194,   51,   52,   54,    0,    0,  196,   51,   52,
  198,   51,   52,    0,  158,    0,  172,    0,    0,    0,
    0,    0,   54,    0,    0,    0,    0,    0,   54,    0,
    0,   54,  202,   51,   52,    0,   68,  174,    0,    0,
    0,    0,    0,    0,    0,  179,    0,    0,    0,    0,
    0,  160,    1,   54,    0,    2,  161,  162,    0,    4,
    5,    6,    7,    8,    9,    0,    0,    0,    1,    0,
   41,    2,   70,   10,   11,    4,    5,    6,    7,    8,
    9,  111,    0,    0,    0,    0,   41,    0,    0,   10,
   11,    0,  208,  174,    0,   12,    0,  174,  226,    1,
    0,    0,  117,  227,  228,    0,    4,    5,    6,    7,
    8,  118,   42,    0,    0,  170,    1,   41,    0,    2,
   10,   11,    0,    4,    5,    6,    7,    8,    9,  223,
    0,  174,  174,    1,   41,  172,  117,   10,   11,    0,
    4,    5,    6,    7,    8,  118,  212,    0,  174,  230,
    1,   41,    0,  117,   10,   11,    0,    4,    5,    6,
    7,    8,  118,  186,    0,    0,    0,    0,   41,    0,
   68,   10,   11,   68,    0,    0,    0,   68,   68,   68,
   68,   68,   68,   68,    0,    0,   63,    0,   68,    0,
    0,   68,   68,    0,    0,    0,    0,    0,    0,    0,
  145,  146,  147,  148,    0,    0,   70,    0,    0,   70,
   65,    0,    0,   70,   70,   70,   70,   70,   70,   70,
    0,    0,    0,    0,   70,    0,    0,   70,   70,    1,
  114,    0,    2,    0,    0,    3,    4,    5,    6,    7,
    8,    9,    0,    0,    0,    0,    1,  221,    0,    2,
   10,   11,    0,    4,    5,    6,    7,    8,    9,    0,
    0,    0,    0,    0,   41,    0,    0,   10,   11,    1,
    0,    0,  117,    0,    0,    0,    4,    5,    6,    7,
    8,  118,    0,    0,    0,    0,    0,   41,    0,    0,
   10,   11,    0,    0,  185,  131,  119,  119,    2,    0,
    0,    3,    4,    5,    6,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,   10,   11,    1,    0,
    0,    2,    0,    0,    3,    4,    5,    6,    7,    8,
    9,    0,    0,    0,    0,    0,    0,    0,  119,   10,
   11,    0,    1,    0,    0,    2,  119,    0,    3,    4,
    5,    6,    7,    8,    9,  120,  120,    0,    0,    0,
    0,    0,    1,   10,   11,    2,  121,  121,    3,    4,
    5,    6,    7,    8,    9,    0,  122,  122,    0,    1,
    0,    0,  117,   10,   11,    0,    4,    5,    6,    7,
    8,  118,   15,  119,  119,    0,    0,  120,  119,    0,
   10,   11,    0,   56,   15,  120,   66,    0,  121,    0,
    0,   57,    0,    0,    0,    0,  121,    0,  122,    0,
    0,    0,    0,    0,    0,    0,  122,    0,    0,  119,
  119,    0,  119,  119,   91,   56,   56,    0,    0,   56,
    0,    0,   56,   57,   57,    0,    0,   57,    0,  119,
   57,    0,  120,  120,  103,    0,   66,  120,    0,    0,
    0,    0,    0,  121,  121,    0,    0,    0,  121,    0,
    0,   56,    0,  122,  122,    0,    0,    0,  122,   57,
    0,    0,    0,    0,   91,    0,  157,    0,  120,  120,
    0,  120,  120,    0,    0,    0,    0,    0,    0,  121,
  121,    0,  121,  121,    0,    0,    0,    0,  120,  122,
  122,    0,  122,  122,    1,    0,    0,    2,    0,  121,
    0,    4,    5,    6,    7,    8,    9,    0,    0,  122,
    0,    0,   41,    0,    0,   10,   11,   56,   56,   56,
   56,    0,   56,    0,    0,   57,   57,   57,   57,    0,
   57,   56,    0,    1,    0,    0,  117,    0,    0,   57,
    4,    5,    6,    7,    8,  118,    0,    0,    0,    1,
    0,   41,    2,    0,   10,   11,    4,    5,    6,    7,
    8,    9,    0,    0,    0,  131,    0,    0,    2,    0,
   10,   11,    4,    0,    6,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,  123,  123,   40,   46,   41,  123,  125,
   44,   14,   40,   60,   77,   40,  172,    0,   46,   61,
  257,   77,  125,    4,  125,   77,   42,  123,   30,   61,
   41,   47,  125,   61,   44,  125,  256,   44,   43,  125,
   45,  261,  257,  258,   59,   36,   44,   44,   44,  173,
   44,   41,  208,  177,  257,   41,   37,   41,  105,   45,
   62,   64,  277,   40,   44,  256,   41,   75,   76,   45,
  261,    9,  135,   46,   41,   42,   43,   44,   45,  135,
   47,   72,  123,  135,  257,   61,   44,  211,  212,  123,
   11,  256,  257,   60,   61,   62,   41,   44,  123,   41,
   42,   43,   44,   45,  228,   47,  256,  257,   44,   44,
  256,  257,  123,  125,  138,  139,  125,  125,   60,   61,
   62,   97,   41,  123,   41,  125,   43,   44,   45,   41,
   44,  256,  135,   45,   41,  111,  256,  256,   59,   60,
  256,  256,  125,   60,   61,   62,  125,   44,   41,    0,
   43,   44,   45,  256,   44,  256,  123,   44,   44,  140,
  141,  152,  143,  256,  172,   12,  256,   60,   61,   62,
  256,   44,  135,   41,   43,   43,   44,   45,   41,  117,
  118,  123,   45,   92,  105,   76,  162,  208,   -1,  135,
   -1,   -1,   60,   61,   62,  266,  267,  268,   -1,   40,
  208,  209,   -1,   -1,   -1,   41,  123,  278,   -1,   -1,
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
   45,  278,  279,   -1,  256,  257,   45,   -1,  260,   45,
  256,  256,  264,  265,  266,  267,  268,  269,  270,  271,
  272,  273,  274,  275,  123,   -1,  278,  279,   -1,  256,
  257,   45,  256,  260,   -1,  257,  258,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  256,
   -1,  278,  279,  256,  257,  277,  256,  260,   -1,  256,
  256,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  256,   -1,  278,  279,   -1,  256,  257,
   -1,   -1,  260,   -1,  257,  258,  264,  265,  266,  267,
  268,  269,  270,  271,  272,  273,  274,  275,   -1,   -1,
  278,  279,   -1,   -1,  277,   -1,  257,   -1,   -1,   -1,
   -1,  257,   -1,   -1,  260,  266,  267,  268,  264,  265,
  266,  267,  268,  269,  270,   -1,  257,  278,  123,  275,
   -1,   -1,  278,  279,   -1,  266,  267,  268,   -1,  257,
   -1,   -1,   -1,   -1,  123,   -1,   -1,  278,  266,  267,
  268,   -1,  256,  257,  258,  259,   -1,   -1,  257,   -1,
  278,  260,  256,  257,  258,  264,  265,  266,  267,  268,
  269,  270,   42,  277,   -1,  123,  275,  257,  258,  278,
  279,   -1,   43,  277,   45,   -1,   -1,   -1,   -1,  256,
  257,  258,  123,  256,  257,  258,   -1,  277,   -1,   60,
   61,   62,   -1,   -1,   -1,   75,   76,   -1,   -1,  123,
  277,  256,  257,  258,  277,   -1,   -1,  256,  257,  258,
  256,  257,  258,   -1,   94,   -1,  123,   -1,   -1,   -1,
   -1,   -1,  277,   -1,   -1,   -1,   -1,   -1,  277,   -1,
   -1,  277,  256,  257,  258,   -1,  123,  117,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,  256,  257,  277,   -1,  260,  261,  262,   -1,  264,
  265,  266,  267,  268,  269,   -1,   -1,   -1,  257,   -1,
  275,  260,  123,  278,  279,  264,  265,  266,  267,  268,
  269,  270,   -1,   -1,   -1,   -1,  275,   -1,   -1,  278,
  279,   -1,  172,  173,   -1,  123,   -1,  177,  256,  257,
   -1,   -1,  260,  261,  262,   -1,  264,  265,  266,  267,
  268,  269,  123,   -1,   -1,  256,  257,  275,   -1,  260,
  278,  279,   -1,  264,  265,  266,  267,  268,  269,  209,
   -1,  211,  212,  257,  275,  123,  260,  278,  279,   -1,
  264,  265,  266,  267,  268,  269,  270,   -1,  228,  256,
  257,  275,   -1,  260,  278,  279,   -1,  264,  265,  266,
  267,  268,  269,  125,   -1,   -1,   -1,   -1,  275,   -1,
  257,  278,  279,  260,   -1,   -1,   -1,  264,  265,  266,
  267,  268,  269,  270,   -1,   -1,  125,   -1,  275,   -1,
   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  271,  272,  273,  274,   -1,   -1,  257,   -1,   -1,  260,
  125,   -1,   -1,  264,  265,  266,  267,  268,  269,  270,
   -1,   -1,   -1,   -1,  275,   -1,   -1,  278,  279,  257,
  125,   -1,  260,   -1,   -1,  263,  264,  265,  266,  267,
  268,  269,   -1,   -1,   -1,   -1,  257,  125,   -1,  260,
  278,  279,   -1,  264,  265,  266,  267,  268,  269,   -1,
   -1,   -1,   -1,   -1,  275,   -1,   -1,  278,  279,  257,
   -1,   -1,  260,   -1,   -1,   -1,  264,  265,  266,  267,
  268,  269,   -1,   -1,   -1,   -1,   -1,  275,   -1,   -1,
  278,  279,   -1,   -1,  256,  257,   75,   76,  260,   -1,
   -1,  263,  264,  265,  266,  267,  268,  269,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,  257,   -1,
   -1,  260,   -1,   -1,  263,  264,  265,  266,  267,  268,
  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  117,  278,
  279,   -1,  257,   -1,   -1,  260,  125,   -1,  263,  264,
  265,  266,  267,  268,  269,   75,   76,   -1,   -1,   -1,
   -1,   -1,  257,  278,  279,  260,   75,   76,  263,  264,
  265,  266,  267,  268,  269,   -1,   75,   76,   -1,  257,
   -1,   -1,  260,  278,  279,   -1,  264,  265,  266,  267,
  268,  269,    0,  172,  173,   -1,   -1,  117,  177,   -1,
  278,  279,   -1,    4,   12,  125,   14,   -1,  117,   -1,
   -1,    4,   -1,   -1,   -1,   -1,  125,   -1,  117,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,  208,
  209,   -1,  211,  212,   42,   36,   37,   -1,   -1,   40,
   -1,   -1,   43,   36,   37,   -1,   -1,   40,   -1,  228,
   43,   -1,  172,  173,   55,   -1,   64,  177,   -1,   -1,
   -1,   -1,   -1,  172,  173,   -1,   -1,   -1,  177,   -1,
   -1,   72,   -1,  172,  173,   -1,   -1,   -1,  177,   72,
   -1,   -1,   -1,   -1,   92,   -1,   94,   -1,  208,  209,
   -1,  211,  212,   -1,   -1,   -1,   -1,   -1,   -1,  208,
  209,   -1,  211,  212,   -1,   -1,   -1,   -1,  228,  208,
  209,   -1,  211,  212,  257,   -1,   -1,  260,   -1,  228,
   -1,  264,  265,  266,  267,  268,  269,   -1,   -1,  228,
   -1,   -1,  275,   -1,   -1,  278,  279,  138,  139,  140,
  141,   -1,  143,   -1,   -1,  138,  139,  140,  141,   -1,
  143,  152,   -1,  257,   -1,   -1,  260,   -1,   -1,  152,
  264,  265,  266,  267,  268,  269,   -1,   -1,   -1,  257,
   -1,  275,  260,   -1,  278,  279,  264,  265,  266,  267,
  268,  269,   -1,   -1,   -1,  257,   -1,   -1,  260,   -1,
  278,  279,  264,   -1,  266,  267,  268,  269,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,
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
"llamada : ID '(' factor ')'",
"llamada : ID '.' ID '(' ')'",
"llamada : ID '.' ID '(' factor ')'",
"impresion : PRINT CADENA",
"impresion : PRINT factor",
"impresion : PRINT error",
};

//#line 1177 "grammar.y"
public static Logger logger = Logger.getInstance();
public static String ambito = "global";
public static boolean error = false;
public static SyntaxNode padre = null;
public static List<SyntaxNode> arbolFunciones = new ArrayList<SyntaxNode>();
public static ArrayList<String> lista_variables = new ArrayList<>();
public static Boolean flagAmbitoCambiado = false;
public static Map<String, String> classFullNames = new HashMap<>();
public static Map<String, ArrayList<String>> compositionMap = new HashMap<>();
public static String currentClass = "";
public static boolean isConditionOrWhileUsed = false;

private boolean metodoExisteEnClase(String classType, String methodName) {
    ArrayList<String> parentClasses = compositionMap.get(classFullNames.get(classType));

    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (parentClasses == null) {
          parentClasses = new ArrayList<>();
        }

        parentClasses.add(classType);

        boolean containsClass = false;
        for (String parentClass : parentClasses) { // Buscamos en clase y en sus padres
            if (attribute.getToken().contains(parentClass.split(":")[0])) {
                containsClass = true;
                break;
            }
        }

        if (attribute.getUso() != null && attribute.getUso().equals(UsesType.FUNCTION) && containsClass) {
            // Extraer el nombre del método del token
            String metodo = attribute.getToken().split(":")[0];
            // Verificar si el nombre del método encontrado coincide con el nombre del método buscado
            if (metodo.equals(methodName)) {
                return true;
            }
        }
    }
    return false;
}

private boolean metodoExiste(String classType, String funcName) {
    // Check for recursion...  put method name last!
    final int index = funcName.indexOf(':');
    final String swappedClass = funcName.substring(index + 1) + ":" + funcName.substring(0, index);

    if (swappedClass.equals(classType)) {
      return true;
    }

    for (Map.Entry<String, Attribute> entry : SymbolTable.getTableMap().entrySet()) {
        Attribute attribute = entry.getValue();

        if (attribute.getUso() != null && attribute.getUso().equals(UsesType.FUNCTION) && attribute.getToken().contains(classType)) {
            // Extraer el nombre del metodo del token
            String metodo = attribute.getToken();
            // Verificar si el nombre del metodo encontrado coincide con el nombre del metodo buscado
            if (metodo.equals(funcName)) {
                return true;
            }
        }
    }
    return false;
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
		yyerror("La variable o funcion no fue hallada en el ambito actual.");
    }
    return "Error";
}

private String getTypeSymbolTableVariablesEnAcceso(String sval, String sval2) {
    final String objectType = getTypeSymbolTableVariables(sval2);
    final String ambitoActual = ":" + Parser.ambito;

    ArrayList<String> composedClassesList = compositionMap.get(classFullNames.get(objectType));

    if (composedClassesList == null) {
      composedClassesList = new ArrayList<>();
    }

    composedClassesList.add(objectType);

    for (String composedClass : composedClassesList) {
      final String nombreCompleto = sval + ambitoActual + ":" + composedClass.split(":")[0];
      final String foundType = hallarTipoEnAmbito(nombreCompleto);
      if (!foundType.isEmpty()) {
          return foundType;
      }
    }

    // Si llegamos aquí, significa que no se encontró el tipo de la variable
    if (!falloNombre(sval)) {
      yyerror("La variable o funcion no es reconocible en el ambito actual.");
    }
    return "Error";
}

public String hallarTipoEnAmbito(String nombreCompleto) {
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
    return "";
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
    yyerror("La variable o funcion '" + sval + "' no es reconocible en el ambito actual.");
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
            if (isPositive) {
              delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.maxShort) : String.valueOf(DelimiterType.maxLong);
            } else {
              delimiter = type_cte.equals(UsesType.USHORT) ? String.valueOf(DelimiterType.minShort) : String.valueOf(DelimiterType.minLong);
            }

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

public static void checkFunctionCall(String funcName, String parameterName) {
  boolean functionFound = false;
  boolean hasParameter = parameterName != null && !parameterName.isEmpty();

  for (Map.Entry<String, Attribute> functionVariable : SymbolTable.getTableMap().entrySet()) {
    if ((functionVariable.getKey().startsWith(funcName + ":") || functionVariable.getKey().contains(funcName)) && functionVariable.getValue().getUso().equals(UsesType.FUNCTION)) {
      functionFound = true;
      var funcAttr = functionVariable.getValue();
      var paramAttr = funcAttr.getParameter();

      if (paramAttr == null && hasParameter) {
        yyerror("Esta funcion no admite el pasaje de parametros.");
        return;
      }

      if (paramAttr != null && !hasParameter) {
        yyerror("Faltan parametros para llamar a esta funcion.");
        return;
      }

      if (hasParameter) {
        var paramEntry = SymbolTable.getInstance().getAttribute(parameterName);
        if (paramEntry.isPresent() && !paramAttr.getType().equals(paramEntry.get().getType())) {
          yyerror("El parametro es del tipo incorrecto.");
          return;
        }
      }

      // Si llegamos aquí, la llamada a la función es correcta
      return;
    }
  }

  if (!functionFound) {
    yyerror("La funcion '" + funcName + "' no esta definida.");
  }
}

private void parseAndAddToClassMap(String input) {
    String[] parts = input.split(":");
    if (parts.length > 1) {
        String key = parts[0];
        classFullNames.put(key, input);
    } else {
        System.out.println("Input string format is incorrect: " + input);
    }
}

private String swapComponents(String input) {
    String[] parts = input.split(":");
    if (parts.length == 2) {
        return parts[1] + ":" + parts[0];
    } else {
        System.out.println("Input string format is incorrect: " + input);
        return input;  // Devuelve la cadena original si el formato es incorrecto
    }
}

private ArrayList<Attribute> getClassMembers(final String className) {
    ArrayList<Attribute> entries = new ArrayList<>(SymbolTable.getTableMap().values());
    ArrayList<Attribute> members = new ArrayList<>();

    // Find entry related to class definition
    final String classAmbit = swapComponents(className.contains(":") ? className : classFullNames.get(className));

    // Get all members from a class
    for (Attribute entry : entries) {
        if (entry.getToken().contains(classAmbit) && entry.getUso().equals(UsesType.VARIABLE)) {
            members.add(entry);
        }
    }
    return members;
}

private Attribute getMemberVarAttribute(SyntaxNode accessNode) {
  if (accessNode != null) {
    final String varName = accessNode.getLeftChild().getName();
    final String memberName = accessNode.getRightChild().getName();
    //final String memberType = accessNode.getRightChild().getType();

    for (Attribute entry : SymbolTable.getTableMap().values()) {
      final String token = entry.getToken();
      if (token.startsWith(memberName) && token.contains(varName + ":")) {
        return entry;
      }
    }
  }
  return null;
}

private int getInheritanceDepth(String className, int currentDepth) {
  if (currentDepth > 3) {
    return currentDepth;
  }

  ArrayList<String> parentClasses = compositionMap.get(className);
  if (parentClasses == null || parentClasses.isEmpty()) {
    return currentDepth;
  }

  int maxDepth = currentDepth;
  for (String parentClass : parentClasses) {
    int depth = getInheritanceDepth(parentClass, currentDepth + 1);
    maxDepth = Math.max(maxDepth, depth);
  }

  return maxDepth;
}
//#line 1238 "Parser.java"
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
//#line 32 "grammar.y"
{ padre = new SyntaxNode("root", (SyntaxNode) val_peek(1).obj , null);
            					verificarReglasCheck();}
break;
case 3:
//#line 35 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 4:
//#line 36 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un {.");}
break;
case 5:
//#line 40 "grammar.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 41 "grammar.y"
{ if ((SyntaxNode) val_peek(0).obj != null)
            							yyval = new ParserVal(new SyntaxNode("Bloque de sentencias", (SyntaxNode) val_peek(1).obj, (SyntaxNode) val_peek(0).obj)); }
break;
case 7:
//#line 45 "grammar.y"
{yyval = val_peek(0);}
break;
case 8:
//#line 46 "grammar.y"
{ if ((SyntaxNode) val_peek(0).obj != null)
      										yyval = new ParserVal(new SyntaxNode("Bloque de sentencias21", (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj)); }
break;
case 9:
//#line 51 "grammar.y"
{ yyval = val_peek(1); }
break;
case 11:
//#line 53 "grammar.y"
{ /*$$ = $1;*/
                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                }
break;
case 12:
//#line 56 "grammar.y"
{ logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','."); }
break;
case 13:
//#line 60 "grammar.y"
{ yyval = val_peek(0);}
break;
case 14:
//#line 61 "grammar.y"
{ yyval = val_peek(0);}
break;
case 15:
//#line 62 "grammar.y"
{ yyval = val_peek(0);}
break;
case 16:
//#line 63 "grammar.y"
{ yyval = val_peek(0);}
break;
case 17:
//#line 64 "grammar.y"
{ yyval = val_peek(0);}
break;
case 18:
//#line 68 "grammar.y"
{ yyval = val_peek(0);}
break;
case 19:
//#line 69 "grammar.y"
{ yyval = val_peek(0);}
break;
case 20:
//#line 70 "grammar.y"
{ yyval = val_peek(0);}
break;
case 21:
//#line 77 "grammar.y"
{
                              logger.logDebugSyntax("Declaración1 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 24:
//#line 85 "grammar.y"
{yyval = val_peek(0);}
break;
case 25:
//#line 86 "grammar.y"
{yyval = val_peek(0);}
break;
case 26:
//#line 87 "grammar.y"
{yyval = val_peek(0);}
break;
case 27:
//#line 88 "grammar.y"
{yyval = val_peek(0);}
break;
case 28:
//#line 94 "grammar.y"
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
//#line 116 "grammar.y"
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
//#line 136 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la variable.");
	       	}
break;
case 31:
//#line 142 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode("ListaVariables", (SyntaxNode)val_peek(2).obj, new SyntaxNode(val_peek(0).sval)));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 32:
//#line 146 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 33:
//#line 154 "grammar.y"
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



                                var t = SymbolTable.getInstance();

                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent()) {
                                    Attribute entry = entrada.get();

                                    entry.addAmbito(ambito);

                                    if (rightSyntaxNode != null) {
                                      Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();
                                      if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { /* If its a leaf node, then the value its a constant*/
                                          entry.setActive(true);
                                          entry.setValue(rightSyntaxNode.getName());
                                      } else {
                                          var asign = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);
                                          asign.setType(tipo_validado);
                                          yyval = new ParserVal(asign);

                                          entry.setActive(false);
                                          entry.setValue(null);
                                      }
                                    }
                                }
                            }
                        }
                    }
                    else {
	                    yyerror("La variable declarada '" + val_peek(2).sval+ "' no existe");
                    }
            }
break;
case 34:
//#line 200 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 35:
//#line 203 "grammar.y"
{
                    logger.logDebugSyntax("Asignación en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode accessNode = (SyntaxNode) val_peek(2).obj;
                    SyntaxNode rightSyntaxNode = (SyntaxNode) val_peek(0).obj;

                    String tipo_validado = validarTipos(accessNode, rightSyntaxNode, true);

                    if (!tipo_validado.equals("Error")) {

                        var assign = new SyntaxNode("=", accessNode, rightSyntaxNode);
                        assign.setType(tipo_validado);
                        yyval = new ParserVal(assign);

                        Attribute memberAttr = getMemberVarAttribute(accessNode);

                        if (memberAttr != null && rightSyntaxNode != null) {
                            Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();

                            if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { /* If its a leaf node, then the value its a constant*/
                              memberAttr.setActive(true);
                              memberAttr.setValue(rightSyntaxNode.getName());
                              assign.setPropagated(true);
                            } else {
                              memberAttr.setActive(false);
                              memberAttr.setValue(null);
                              assign.setPropagated(false);
                            }
                        }
                    }
            }
break;
case 36:
//#line 231 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 37:
//#line 237 "grammar.y"
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

                        SyntaxNode asign = new SyntaxNode("=", variableNode, incrementNode, symbolType);

                        yyval = new ParserVal(asign);

                        var t = SymbolTable.getInstance();
                        t.insertAttribute(new Attribute (CTE, incrementValue, symbolType, UsesType.CONSTANT, LexicalAnalyzer.getLine()));
                        
                        var entrada = t.getAttribute(nombreCompleto);

                        if (entrada.isPresent()){
                            entrada.get().addAmbito(ambito);
                            entrada.get().setUsadaDerecho(true);
                        }
                    }
		    }
break;
case 38:
//#line 284 "grammar.y"
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
//#line 303 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {
                            yyval = new ParserVal(new SyntaxNode("-", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj, tipo_validado));

                            if ((SyntaxNode) val_peek(0).obj != null && ((SyntaxNode) val_peek(0).obj).isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(((SyntaxNode)val_peek(0).obj).getName());

                                if (!nombreCompleto.equals("Error")) {
                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    entrada.ifPresent(attribute -> attribute.setUsadaDerecho(true));
                                }
                            }
                        }
			}
break;
case 40:
//#line 321 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 41:
//#line 322 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 42:
//#line 323 "grammar.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 327 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {

                            SyntaxNode factorNode = (SyntaxNode) val_peek(0).obj;

                            if (factorNode != null) {
                                if (factorNode.isLeaf()){
                                    String nombreCompleto = getNameSymbolTableVariables(factorNode.getName());

                                    if (!nombreCompleto.equals("Error")) {
                                        var t = SymbolTable.getInstance();
                                        var entrada = t.getAttribute(nombreCompleto);
                                        if (entrada.isPresent()) {
                                            Attribute entry = entrada.get();
                                            entry.setUsadaDerecho(true);

                                            if (entry.isActive() && !Parser.isConditionOrWhileUsed) {
                                                final String value = entry.getValue();

                                                var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, new SyntaxNode(value, entry.getType()));
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            } else {
                                                var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);
                                        if (memberAttr != null && memberAttr.isActive() && !Parser.isConditionOrWhileUsed) {
                                            final String value = memberAttr.getValue();

                                            var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, new SyntaxNode(value, memberAttr.getType()));
                                            x.setType(tipo_validado);
                                            yyval = new ParserVal(x);
                                        } else {
                                            var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                                            x.setType(tipo_validado);
                                            yyval = new ParserVal(x);
                                        }
                                    }
                                }
                            }
                        }
            }
break;
case 44:
//#line 377 "grammar.y"
{
                        String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);

                        if (!tipo_validado.equals("Error")) {
                            SyntaxNode factorNode = (SyntaxNode) val_peek(0).obj;

                            if (factorNode != null) {
                                if (factorNode.isLeaf()){
                                    String nombreCompleto = getNameSymbolTableVariables(factorNode.getName());

                                    if (!nombreCompleto.equals("Error")) {
                                        var t = SymbolTable.getInstance();
                                        var entrada = t.getAttribute(nombreCompleto);
                                        if (entrada.isPresent()) {
                                            Attribute entry = entrada.get();
                                            entry.setUsadaDerecho(true);

                                            if (entry.isActive() && !Parser.isConditionOrWhileUsed) {
                                                final String value = entry.getValue();

                                                var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, new SyntaxNode(value, entry.getType()));
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            } else {
                                                var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);
                                        if (memberAttr != null && memberAttr.isActive() && !Parser.isConditionOrWhileUsed) {
                                            final String value = memberAttr.getValue();

                                            var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, new SyntaxNode(value, memberAttr.getType()));
                                            x.setType(tipo_validado);
                                            yyval = new ParserVal(x);
                                        } else {
                                            var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, (SyntaxNode)  val_peek(0).obj);
                                            x.setType(tipo_validado);
                                            yyval = new ParserVal(x);
                                        }
                                    }
                                }
                            }
                        }
            }
break;
case 45:
//#line 426 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
   			}
break;
case 46:
//#line 429 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
            }
break;
case 47:
//#line 432 "grammar.y"
{
   			            /* $$ = $1;*/
   			            SyntaxNode node = (SyntaxNode) val_peek(0).obj;

                        if (node != null) {
                            if (node.isLeaf()){
                                String nombreCompleto = getNameSymbolTableVariables(node.getName());

                                if (!nombreCompleto.equals("Error")) {

                                    var t = SymbolTable.getInstance();
                                    var entrada = t.getAttribute(nombreCompleto);
                                    if (entrada.isPresent()) {
                                        Attribute entry = entrada.get();
                                        entry.setUsadaDerecho(true);

                                        if (entry.isActive() && !Parser.isConditionOrWhileUsed) {
                                            final String value = entry.getValue();

                                            yyval = new ParserVal(new SyntaxNode(value, entry.getType()));
                                        } else {
                                            yyval = val_peek(0);
                                        }
                                    }
                                }
                            } else {
                                if (node.getName().equalsIgnoreCase("acceso")) {
                                    Attribute memberAttr = getMemberVarAttribute(node);
                                    if (memberAttr != null && memberAttr.isActive() && !Parser.isConditionOrWhileUsed) {
                                        final String value = memberAttr.getValue();
                                        yyval = new ParserVal(new SyntaxNode(value, memberAttr.getType()));
                                    } else {
                                        yyval = val_peek(0);
                                    }
                                }
                            }
                        }
   			}
break;
case 48:
//#line 473 "grammar.y"
{yyval = val_peek(0);}
break;
case 49:
//#line 474 "grammar.y"
{yyval = val_peek(1);}
break;
case 50:
//#line 475 "grammar.y"
{
      		            String value = val_peek(0).sval;

      		            value = processInteger(value, false);

      		            if (!value.equals("Error")) {
      		                var symbolType = getTypeSymbolTableVariables(value);

                            if (!symbolType.equals("Error")) {
                                String finalValue = "";
                                if (symbolType.equals(UsesType.USHORT)) {
                                    finalValue = value;
                                } else {
                                    finalValue = (value.contains("-") ? "" : "-") + value;
                                    SymbolTable.addSymbol(finalValue, TokenType.CONSTANT, symbolType, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                                }
                                yyval = new ParserVal(new SyntaxNode(finalValue, symbolType)); /* Crear un nodo para la constante*/
                            }
      		            }

      		}
break;
case 51:
//#line 496 "grammar.y"
{
                        String value = val_peek(0).sval;

                        value = processFloat(value, false);

                        if (!value.equals("Error")) {
                            SymbolTable.addSymbol("-" + value, TokenType.CONSTANT, UsesType.FLOAT, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                            yyval = new ParserVal(new SyntaxNode("-" + value, UsesType.FLOAT)); /* Crear un nodo para la constante*/
                        }
            }
break;
case 52:
//#line 506 "grammar.y"
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
//#line 516 "grammar.y"
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
//#line 529 "grammar.y"
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
//#line 542 "grammar.y"
{
                        yyval=val_peek(0);
            }
break;
case 56:
//#line 545 "grammar.y"
{
                        var type = getTypeSymbolTableVariables(val_peek(0).sval);
                        var name = getNameSymbolTableVariables(val_peek(0).sval);

                        if (!type.equals("Error") && !name.equals("Error")) {
                            yyval =  new ParserVal(new SyntaxNode(name, type));
                        } else{
                          /*  $$ =  new ParserVal( new SyntaxNode("parametroNoEncontrado", type));*/
                            yyerror("No se encontro esta variable en un ambito adecuado");}
            }
break;
case 57:
//#line 559 "grammar.y"
{ yyval = new ParserVal("=="); }
break;
case 58:
//#line 560 "grammar.y"
{ yyval = new ParserVal("<="); }
break;
case 59:
//#line 561 "grammar.y"
{ yyval = new ParserVal(">="); }
break;
case 60:
//#line 562 "grammar.y"
{ yyval = new ParserVal("<"); }
break;
case 61:
//#line 563 "grammar.y"
{ yyval = new ParserVal(">"); }
break;
case 62:
//#line 564 "grammar.y"
{ yyval = new ParserVal("!!"); }
break;
case 63:
//#line 565 "grammar.y"
{
			        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": no puede realizarse una asignacion. Quizas queria poner '=='?");
			        }
break;
case 64:
//#line 571 "grammar.y"
{
                                    String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);
                                    if (!tipo_validado.equals("Error")) {
                                        yyval = new ParserVal(new SyntaxNode(val_peek(1).sval, (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, tipo_validado));
                                    }
            }
break;
case 65:
//#line 577 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 66:
//#line 578 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 67:
//#line 582 "grammar.y"
{ yyval = val_peek(1); }
break;
case 68:
//#line 583 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '()'.");}
break;
case 69:
//#line 584 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '('.");}
break;
case 70:
//#line 585 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un ')'."); yyval = val_peek(0); }
break;
case 71:
//#line 586 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta establecer una condicion.");}
break;
case 72:
//#line 590 "grammar.y"
{
			    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(1).obj, null);
                yyval = new ParserVal(new SyntaxNode("IF", (SyntaxNode)val_peek(2).obj, thenSyntaxNode, null));
                logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
                                                                 }
break;
case 73:
//#line 595 "grammar.y"
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
//#line 606 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 75:
//#line 607 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 76:
//#line 608 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");}
break;
case 77:
//#line 609 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");}
break;
case 78:
//#line 613 "grammar.y"
{ yyval = val_peek(0); }
break;
case 79:
//#line 614 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 80:
//#line 615 "grammar.y"
{ yyval = val_peek(1) ; }
break;
case 81:
//#line 616 "grammar.y"
{
                                                                    yyval = val_peek(1) ;
                                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");
                                                                    }
break;
case 82:
//#line 620 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 83:
//#line 624 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 84:
//#line 625 "grammar.y"
{yyval = val_peek(0);}
break;
case 85:
//#line 629 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("SecuenciaSentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj)); }
break;
case 86:
//#line 630 "grammar.y"
{ yyval = val_peek(0); }
break;
case 87:
//#line 634 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
                    isConditionOrWhileUsed = false;
            }
break;
case 88:
//#line 638 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 89:
//#line 641 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
            }
break;
case 90:
//#line 649 "grammar.y"
{
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    String nombreFuncion = val_peek(3).sval;
                    arbolFunciones.add(new SyntaxNode("DeclaracionFuncion=" + nombreFuncion, (SyntaxNode) val_peek(1).obj, null));
                    checkFlagAmbito();
            }
break;
case 91:
//#line 655 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '}'");
            }
break;
case 92:
//#line 658 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '{'");
            }
break;
case 93:
//#line 664 "grammar.y"
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

		            Attribute attr = new Attribute(Parser.ID, nombreParametro + ":" + Parser.ambito + ":" + val_peek(3).sval, tipoParametro, UsesType.PARAMETER, LexicalAnalyzer.getLine());
		            t.insertAttribute(attr);

		            var entradaFuncionInsertada = t.getAttribute(val_peek(3).sval + ":" + Parser.ambito);
		            entradaFuncionInsertada.get().setParameter(attr);
		        }

                String nombreFuncionSyntaxNode = val_peek(3).sval + ":" + Parser.ambito;
                yyval.sval = nombreFuncionSyntaxNode;
		        nuevoAmbito(val_peek(3).sval);
		    }
break;
case 94:
//#line 695 "grammar.y"
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
//#line 712 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 96:
//#line 715 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 97:
//#line 718 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 98:
//#line 721 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 99:
//#line 724 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion.");
            }
break;
case 100:
//#line 730 "grammar.y"
{ yyval = val_peek(0); }
break;
case 101:
//#line 731 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": Parametro mal declarado. Solo es posible declarar un único parámetro");}
break;
case 102:
//#line 735 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval, val_peek(1).sval));
            }
break;
case 103:
//#line 738 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el tipo del parametro.");
            }
break;
case 104:
//#line 741 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre del parametro.");
            }
break;
case 105:
//#line 747 "grammar.y"
{
        SyntaxNode sentenciasSyntaxNode = (SyntaxNode)val_peek(1).obj;
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        if (sentenciasSyntaxNode != null)
        	yyval = new ParserVal(new SyntaxNode("Bloque de sentencias1", sentenciasSyntaxNode, returnSyntaxNode));
    }
break;
case 106:
//#line 753 "grammar.y"
{
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias2", null, returnSyntaxNode));
    }
break;
case 107:
//#line 757 "grammar.y"
{
        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una sentencia 'RETURN' en el bloque 'void'.");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias3", (SyntaxNode)val_peek(0).obj, null));
    }
break;
case 108:
//#line 763 "grammar.y"
{
        /* Aquí, simplemente expandimos la secuencia de sentencias sin un nodo intermedio*/
        yyval = new ParserVal(new SyntaxNode("Sentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
    }
break;
case 109:
//#line 767 "grammar.y"
{
        yyval = val_peek(0);
    }
break;
case 110:
//#line 773 "grammar.y"
{ yyval = val_peek(0); }
break;
case 111:
//#line 774 "grammar.y"
{ yyval = val_peek(0); }
break;
case 112:
//#line 775 "grammar.y"
{ yyval = val_peek(0); }
break;
case 113:
//#line 776 "grammar.y"
{ yyval = val_peek(0); }
break;
case 114:
//#line 777 "grammar.y"
{ yyval = val_peek(0); }
break;
case 115:
//#line 778 "grammar.y"
{ yyval = val_peek(0); }
break;
case 117:
//#line 783 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 119:
//#line 788 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 120:
//#line 792 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 121:
//#line 796 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 122:
//#line 799 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 123:
//#line 806 "grammar.y"
{
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(1).obj, null);
                    yyval = new ParserVal(new SyntaxNode("IF", (SyntaxNode)val_peek(2).obj, thenSyntaxNode, null));
            }
break;
case 124:
//#line 812 "grammar.y"
{
                    /* Crear nodos para 'then' y 'else'*/
                    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(3).obj, null);
                    SyntaxNode elseSyntaxNode = new SyntaxNode("ELSE", (SyntaxNode)val_peek(1).obj, null);

                    /* Crear un nodo para el cuerpo del 'if' que contiene 'then' y 'else'*/
                    SyntaxNode camino = new SyntaxNode("Camino", thenSyntaxNode, elseSyntaxNode);

                    /* Crear el nodo 'if' con la condici?n y el cuerpo como hijos*/
                    SyntaxNode ifSyntaxNode = new SyntaxNode("IF", (SyntaxNode) val_peek(4).obj, camino);

                    /* Crear un nodo 'if' con la condición y los nodos 'then' y 'else' como hijos*/
                    yyval = new ParserVal(ifSyntaxNode);
                    logger.logDebugSyntax("Bloque IF-ELSE en la linea " + LexicalAnalyzer.getLine());
            }
break;
case 125:
//#line 827 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 126:
//#line 830 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 127:
//#line 833 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");
            }
break;
case 128:
//#line 836 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");
            }
break;
case 129:
//#line 842 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 130:
//#line 843 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias4", null, returnSyntaxNode));
		    }
break;
case 131:
//#line 847 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias5", null, returnSyntaxNode));
		    }
break;
case 132:
//#line 851 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj)); }
break;
case 133:
//#line 852 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(2).obj)); }
break;
case 134:
//#line 853 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 135:
//#line 857 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(0).obj)); }
break;
case 136:
//#line 858 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 137:
//#line 863 "grammar.y"
{ 
            		/*$$ = $3;*/

                    /*String nombreClase = $1.sval;*/
                    /*arbolFunciones.add(new SyntaxNode("DeclaracionClass=" + nombreClase, (SyntaxNode) $3.obj, null));*/
                    /*checkFlagAmbito();*/

                    eliminarAmbito(); /* Restaura el ámbito anterior al salir de la clase*/
            		}
break;
case 138:
//#line 872 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 140:
//#line 875 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 141:
//#line 879 "grammar.y"
{
                logger.logDebugSyntax("FORWARD DECLARATION en la linea " + LexicalAnalyzer.getLine());
                String nombreClase = val_peek(1).sval + ":" + Parser.ambito;

                /* Registrar la clase en la tabla de simbolos*/
                SymbolTable tablaSimbolos = SymbolTable.getInstance();

                Optional<Attribute> classAttr = tablaSimbolos.getAttribute(nombreClase);

                if (classAttr.isPresent()) {
                    yyerror("La clase " + nombreClase + " ya fue declarada.");
                } else {
                    Attribute newAttr = new Attribute(Parser.ID, nombreClase, "Clase", UsesType.CLASE, LexicalAnalyzer.getLine());
                    newAttr.setWasForwardDeclared(true);
                    tablaSimbolos.insertAttribute(newAttr);

                    parseAndAddToClassMap(newAttr.getToken());

                    currentClass = nombreClase;

                    yyval.sval = nombreClase;
                }
            }
break;
case 142:
//#line 904 "grammar.y"
{
		        logger.logDebugSyntax("CLASE en la linea " + LexicalAnalyzer.getLine());
		        String nombreClase = val_peek(0).sval + ":" + Parser.ambito;
		
		        /* Registrar la clase en la tabla de simbolos*/
		        SymbolTable tablaSimbolos = SymbolTable.getInstance();

                Optional<Attribute> classAttr = tablaSimbolos.getAttribute(nombreClase);

                if (classAttr.isPresent()) {
                  if (classAttr.get().isWasForwardDeclared()) {
                    currentClass = nombreClase;
                    nuevoAmbito(val_peek(0).sval); /* Agrega el nuevo ambito de la clase*/
                    yyval.sval = nombreClase;
                    classAttr.get().setWasForwardDeclared(false);
                  } else {
                    yyerror("La clase " + nombreClase + " ya fue declarada previamente.");
                  }
                } else {
                  Attribute newAttr = new Attribute(Parser.ID, nombreClase, "Clase", UsesType.CLASE, LexicalAnalyzer.getLine());
                  tablaSimbolos.insertAttribute(newAttr);

                  parseAndAddToClassMap(newAttr.getToken());

                  currentClass = nombreClase;

                  nuevoAmbito(val_peek(0).sval); /* Agrega el nuevo ambito de la clase*/

                  yyval.sval = nombreClase;
                }
		    }
break;
case 143:
//#line 935 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");}
break;
case 144:
//#line 939 "grammar.y"
{
                              logger.logDebugSyntax("Declaración2 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 147:
//#line 944 "grammar.y"
{
                        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": la sentencia declarada no es permitida fuera de un metodo.");
                        }
break;
case 148:
//#line 950 "grammar.y"
{
               if ((SyntaxNode) val_peek(1).obj != null)
                    yyval = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(1).obj));
            }
break;
case 149:
//#line 954 "grammar.y"
{
                                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                             }
break;
case 150:
//#line 957 "grammar.y"
{
                if ((SyntaxNode) val_peek(1).obj != null)
                    yyval = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(1).obj));
            }
break;
case 151:
//#line 961 "grammar.y"
{
                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                                   }
break;
case 152:
//#line 964 "grammar.y"
{
		        yyval = new ParserVal(new SyntaxNode("BloqueClase" , (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj));
		    }
break;
case 153:
//#line 967 "grammar.y"
{yyval = val_peek(1);}
break;
case 154:
//#line 968 "grammar.y"
{
                                     logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                     yyval = val_peek(1);
                                  }
break;
case 155:
//#line 975 "grammar.y"
{
              if (!esTipoClaseValido(val_peek(0).sval)) {
                yyerror("Tipo de clase no declarado: " + val_peek(0).sval);
              } else {
                logger.logDebugSyntax("Herencia por composicion en la linea " + LexicalAnalyzer.getLine());
                ArrayList<String> inheritanceClasses = compositionMap.get(currentClass);
                final String classFullName = getNameSymbolTableVariables(val_peek(0).sval);

                if (inheritanceClasses == null) {
                  inheritanceClasses = new ArrayList<>();
                  compositionMap.put(currentClass, inheritanceClasses);
                }

                /* Verificar la profundidad de la herencia antes de añadir la nueva clase*/
                int inheritanceDepth = getInheritanceDepth(classFullName, 1);
                if (inheritanceDepth >= 3) {
                  yyerror("Error: Se excede el límite de niveles de herencia (máximo 3) al heredar de " + val_peek(0).sval);
                } else {
                  inheritanceClasses.add(classFullName);

                  /* Verificar si al añadir esta clase se excede el límite para la clase actual*/
                  if (getInheritanceDepth(currentClass, 0) >= 3) {
                    yyerror("Error: Se excede el límite de niveles de herencia (máximo 3) para la clase " + currentClass);
                    inheritanceClasses.remove(classFullName); /* Revertir la adición*/
                  }
                }
              }
            }
break;
case 156:
//#line 1006 "grammar.y"
{
                    logger.logDebugSyntax("Declaración 3de variables en la linea " + LexicalAnalyzer.getLine());
                    if (!esTipoClaseValido(val_peek(1).sval)) {
                        yyerror("Tipo de clase no declarado: " + val_peek(1).sval);
                    }
                    else {
                        var t = SymbolTable.getInstance();
                        String tipoVariable = val_peek(1).sval;

                        ArrayList<Attribute> classAttributes = getClassMembers(tipoVariable);
                        ArrayList<String> parentClasses = compositionMap.get(classFullNames.get(tipoVariable));

                        if (parentClasses != null) {
                          for (String parentClass : parentClasses) {
                            classAttributes.addAll(getClassMembers(parentClass));
                          }
                        }

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
                                Attribute classVarAttr = new Attribute(Parser.ID, varName, tipoVariable, UsesType.CLASS_VAR, LexicalAnalyzer.getLine());
                                t.insertAttribute(classVarAttr);

                                for (Attribute attr : classAttributes) {
                                    final String attrName = attr.getToken() + ":" + varName;
                                    Attribute memberAttr = new Attribute(Parser.ID, attrName, attr.getType(), attr.getUso(), LexicalAnalyzer.getLine());
                                    t.insertAttribute(memberAttr);
                                }
                            }
                        }
                        lista_variables.clear();
                    }
                  }
break;
case 157:
//#line 1051 "grammar.y"
{
		        logger.logDebugSyntax("Acceso en la linea " + LexicalAnalyzer.getLine());

		        String varType = getTypeSymbolTableVariablesEnAcceso(val_peek(0).sval, val_peek(2).sval);

		        if (!varType.equals("Error")) {
		            SyntaxNode objetoNode = new SyntaxNode(val_peek(2).sval, "Clase");
		            SyntaxNode propiedadNode = new SyntaxNode(val_peek(0).sval, varType);
		            SyntaxNode accesoNode = new SyntaxNode("Acceso", objetoNode, propiedadNode, varType);
		            yyval = new ParserVal(accesoNode);
		        } else {
		            yyerror("La variable " + val_peek(2).sval + " no fue declarada.");
		        }

		    }
break;
case 158:
//#line 1066 "grammar.y"
{
		        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");
		    }
break;
case 159:
//#line 1075 "grammar.y"
{
                        logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
        				String nombreMetodo = getNameSymbolTableVariables(val_peek(2).sval);
                        if (!nombreMetodo.equals("Error")) {
				            if (metodoExiste(Parser.ambito, nombreMetodo)) {
				            	SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion", new SyntaxNode(nombreMetodo));
				                logger.logDebugSyntax("Llamado a método en la linea " + LexicalAnalyzer.getLine());
                        		yyval = new ParserVal(llamadaSyntaxNode);

				            	checkFunctionCall(nombreMetodo, null);
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + val_peek(2).sval + " no existe " );
				            }
                        }
		    }
break;
case 160:
//#line 1090 "grammar.y"
{
   			        /*    SyntaxNode node = (SyntaxNode) val_peek(1).obj;*/
   			            logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
   			            SyntaxNode node = (SyntaxNode) val_peek(1).obj;

                        if (node != null){ /*  && node.isLeaf()*/
                            String nombreMetodo = getNameSymbolTableVariables(val_peek(3).sval);
                            /*String nombreCompleto = getNameSymbolTableVariables(node.getName());*/

                            if (!nombreMetodo.equals("Error")) {
                                if (metodoExiste(Parser.ambito, nombreMetodo)) {

                                    SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoFuncion",
                                                                                    new SyntaxNode(nombreMetodo),
                                                                                    new SyntaxNode("parametro", (SyntaxNode) val_peek(1).obj, null),
                                                                                    getTypeSymbolTableVariables(val_peek(3).sval));
                                    logger.logDebugSyntax("Llamado a método en la linea " + LexicalAnalyzer.getLine());
                                    yyval = new ParserVal(llamadaSyntaxNode);

                                    checkFunctionCall(nombreMetodo, ((SyntaxNode) val_peek(1).obj).getType());
                                } else {
                                    yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + val_peek(3).sval + " no existe " );
                                }
                            }
                        } else {
                            yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": Parametro inexistente." );
                        }
            }
break;
case 161:
//#line 1118 "grammar.y"
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

				                checkFunctionCall(nombreMetodo, null);
				            } else {
				                yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe en la clase " + tipoClase);
				            }
				        }
		    }
break;
case 162:
//#line 1136 "grammar.y"
{
				        logger.logDebugSyntax("Llamado en la linea " + LexicalAnalyzer.getLine());
   			            SyntaxNode node = (SyntaxNode) val_peek(1).obj;

                        if (node != null){ /*  && node.isLeaf()*/
                            final String nombreInstancia = getNameSymbolTableVariables(val_peek(5).sval);
                            final String tipoClase = getTypeSymbolTableVariables(nombreInstancia);

                            if (!tipoClase.equals("Error")) {
                                /* Verificar si el método existe en la clase*/
                                String nombreMetodo = val_peek(3).sval;
                                if (metodoExisteEnClase(tipoClase, nombreMetodo)) {
                                    SyntaxNode llamadaSyntaxNode = new SyntaxNode("LlamadoMetodoClase",
                                                                                new SyntaxNode(nombreInstancia),
                                                                                new SyntaxNode(nombreMetodo, new SyntaxNode("parametro", node, null), null),
                                                                                getTypeSymbolTableVariables(val_peek(5).sval));
                                    logger.logDebugSyntax("Llamado a método de clase en la linea " + LexicalAnalyzer.getLine());
                                    yyval = new ParserVal(llamadaSyntaxNode);
                                    checkFunctionCall(nombreMetodo, node.getType());
                                } else {
                                    yyerror("Error en la línea " + LexicalAnalyzer.getLine() + ": El método " + nombreMetodo + " no existe en la clase " + tipoClase);
                                }
				            }
                        }
		    }
break;
case 163:
//#line 1165 "grammar.y"
{
                                    logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
                                    yyval = new ParserVal( new SyntaxNode("Print", new SyntaxNode(val_peek(0).sval, "CADENA"), null, "CADENA"));
                               }
break;
case 164:
//#line 1169 "grammar.y"
{
			                        logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
			                        yyval = new ParserVal( new SyntaxNode("Print", (SyntaxNode) val_peek(0).obj, null, "Factor" ));
			                     }
break;
case 165:
//#line 1173 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el contenido de la impresion.");}
break;
//#line 2840 "Parser.java"
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
