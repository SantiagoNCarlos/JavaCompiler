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

import org.apache.commons.lang3.tuple.Pair;
//#line 38 "Parser.java"




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
    0,    0,    0,    0,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    4,    4,    5,    5,    5,   11,
   11,   11,    6,    6,    6,   18,   18,   18,   18,   15,
   15,   15,   19,   19,    9,    9,    9,    9,   14,   20,
   20,   20,   20,   20,   21,   21,   21,   21,   21,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   23,   23,
   23,   23,   23,   23,   23,   24,   24,   24,   25,   25,
   25,   25,   25,    7,    7,    7,    7,    7,    7,   26,
   26,   26,   26,   26,   28,   28,   29,   29,    8,    8,
    8,   16,   16,   16,   30,   30,   30,   30,   30,   30,
   30,   32,   32,   33,   33,   33,   31,   31,   31,   34,
   34,   36,   36,   36,   36,   36,   36,   35,   35,   27,
   27,   38,   38,   38,   37,   37,   37,   37,   37,   37,
   39,   39,   39,   39,   39,   39,   40,   40,    3,    3,
    3,    3,   43,   41,   41,   44,   44,   44,   44,   42,
   42,   42,   42,   42,   42,   42,   45,   17,   13,   13,
   12,   12,   12,   12,   10,   10,   10,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    2,    1,    2,    1,    2,
    2,    2,    2,    2,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    2,    3,    1,    3,    3,    3,    3,    2,    3,
    3,    3,    3,    1,    3,    3,    3,    3,    1,    1,
    2,    2,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    1,    3,    2,
    3,    2,    2,    4,    6,    6,    4,    4,    2,    1,
    1,    3,    4,    3,    2,    1,    2,    1,    4,    4,
    3,    4,    4,    4,    5,    4,    3,    4,    2,    3,
    1,    1,    3,    2,    1,    1,    2,    1,    1,    2,
    1,    1,    1,    1,    1,    1,    1,    2,    2,    2,
    2,    4,    4,    3,    4,    6,    6,    4,    4,    2,
    1,    1,    3,    3,    4,    3,    2,    1,    4,    4,
    1,    1,    3,    2,    2,    1,    1,    1,    1,    3,
    3,    3,    3,    2,    2,    2,    1,    2,    3,    3,
    3,    4,    5,    6,    2,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   27,   28,   26,    0,   29,
    0,    0,    0,    0,    5,    7,    9,    0,    0,    0,
    0,   17,   18,   19,   20,    0,   22,   23,   24,   25,
    0,    0,    0,  141,   34,   39,    0,    0,    0,    0,
    0,    0,    0,    0,   80,    0,   79,   81,  145,    0,
  167,    0,   55,  165,   56,    0,   50,   57,  166,    0,
    0,    0,    0,    2,    0,    4,    6,    8,   10,   13,
   11,   14,   12,   15,   16,    0,   32,    0,    0,    0,
    0,   36,    0,    0,   49,  161,    0,  160,    0,    0,
    0,    0,  121,  120,   88,    0,    0,    0,   73,    0,
    0,  143,    0,    0,   52,   53,   51,  105,    0,    0,
  100,    0,   97,    0,    0,   91,    0,    1,   38,    0,
    0,    0,  117,  112,  115,  116,  108,    0,    0,  111,
    0,  113,  114,    0,    0,  149,  146,  147,    0,    0,
  148,    0,    0,    0,    0,  162,    0,   33,   60,   61,
   59,   64,   65,   62,   63,    0,   71,    0,   84,   82,
   87,   85,   69,   77,   74,    0,   78,  159,   96,    0,
  104,    0,   98,   90,   89,    0,    0,  132,  131,  130,
    0,   94,  107,  110,  119,  118,   93,   92,  140,  139,
  154,    0,    0,  156,  155,   42,    0,   43,    0,   47,
   45,   48,   46,  163,    0,   67,    0,   83,    0,   95,
  103,    0,    0,    0,    0,    0,  124,  153,  152,  151,
  150,  164,   76,   75,  133,    0,  137,  136,  134,  128,
  125,    0,  129,  123,  122,  135,    0,  127,  126,
};
final static short yydgoto[] = {                         13,
   14,   45,   16,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,   29,   30,   31,   40,   91,
   84,   85,  156,   92,   46,   47,   48,   97,   98,   32,
  128,  111,  112,  129,  179,  131,  132,  133,  180,  214,
   33,  139,   34,  140,  141,
};
final static short yysindex[] = {                       379,
   -3,  -34, -165,  227,  -38,    0,    0,    0,  -35,    0,
  -66,  503,    0,  529,    0,    0,    0,   -6,    3,  -29,
  -22,    0,    0,    0,    0,  -27,    0,    0,    0,    0,
  -21, -119,  -87,    0,    0,    0,  231,   89, -150,  -17,
  215,    6,  733,  122,    0,  435,    0,    0,    0,    4,
    0,  -30,    0,    0,    0, -213,    0,    0,    0,  -15,
  177,  469, -205,    0,  558,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  243,    0,  -17,  694,  694,
  762,    0,   41,   -1,    0,    0,   22,    0,   26, -170,
  433,   71,    0,    0,    0,  246, -106,  733,    0,   72,
  -92,    0, -146,   47,    0,    0,    0,    0,  202, -141,
    0,   73,    0,   80,  486,    0,  -17,    0,    0,   41,
  -40,  -35,    0,    0,    0,    0,    0,   11,  694,    0,
   21,    0,    0,  -97,   -3,    0,    0,    0,  583,   36,
    0,  265,  271,  275,  289,    0,  153,    0,    0,    0,
    0,    0,    0,    0,    0,  293,    0,   29,    0,    0,
    0,    0,    0,    0,    0,  435,    0,    0,    0,   91,
    0, -105,    0,    0,    0,  694,  452,    0,    0,    0,
  329,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   65,   79,    0,    0,    0,   -1,    0,   -1,    0,
    0,    0,    0,    0,  125,    0,   41,    0, -201,    0,
    0,  599,  694,  -85,  312,  354,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   43,    0,    0,    0,    0,
    0,  452,    0,    0,    0,    0, -194,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -114,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   87,    0,    0,    0,    0,
    0,    0,   18,    0,    0,    0,    0,    0,    0,  104,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,   34,    0,    0,    0,    0,    0,    0,    0, -109,
    0,    0,    0,    0,  184,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  105,    0,    0,
    0,    0,  108,   84,    0,    0,    0,    0,  -41,    0,
  172,  396,    0,    0,    0,    0,    0,  -76,    0,  412,
    0,    0,    0,   58,    0,    0,    0,    0,    0,   -9,
    0,   -8,    0,    0,    0,    0,  109,    0,    0,  111,
    0,    0,    0,    0,    0,    0,    0,    0,  -74,    0,
    0,    0,    0,    0,  112,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  117,    0,  148,    0,
    0,    0,    0,    0,    0,    0,  196,    0,    0,    0,
    0,    0,  -71,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  178,  745,   -4,    7,  -69,  699,    0,    0,  723,  752,
  805,    0,  849,  903,  -54,   56,  -51,   13,    8,   32,
  -45,   52,    0,  159,   17,  -33,  436,  110,    0,    0,
  124,  -50,    0, -159,  -72,    0,    0,    0,  328,   -7,
    0,    0,    0,   75,    0,
};
final static int YYTABLESIZE=1059;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         44,
  144,   61,  159,   80,   44,   44,  130,  130,  101,   68,
  114,  136,  101,   99,   74,  103,  213,  142,  160,  159,
   69,   75,   77,   63,  109,   62,  137,  188,  116,  138,
   43,  106,  102,   76,  106,   81,   38,   71,   78,  229,
  144,   90,   39,  104,  105,  145,   73,  102,   86,   94,
  109,   35,  213,  138,  223,   59,  184,   37,  170,  224,
   68,  238,  146,  106,  186,  147,  239,  167,   83,  136,
  117,   69,  110,  110,   58,   58,   58,   58,   58,  195,
   58,  175,  176,  142,  137,  143,  148,  138,   43,   87,
   49,   50,  103,   58,   58,   58,  197,  199,   54,   54,
   54,   54,   54,  130,   54,   88,   89,  120,  219,   88,
  168,  157,  163,  106,  102,  171,  172,   54,   54,   54,
  173,  110,  221,  144,   44,  144,   44,   44,   44,   86,
   21,  210,  209,   56,  191,  182,   79,  177,  181,  130,
  184,  101,  142,   44,   44,   44,   99,  158,   31,  159,
  211,   35,   30,  208,   37,  157,   58,   40,  187,   40,
   40,   40,   99,  164,    1,  222,   56,  236,  165,  166,
  228,    4,    5,    6,    7,    8,   40,   40,   40,   86,
   54,  109,   42,    3,  138,   10,   11,  207,   41,   65,
   41,   41,   41,  204,  192,  201,  203,   56,  205,    6,
    7,    8,  100,  134,  226,  158,   44,   41,   41,   41,
    0,   10,   68,  193,  159,   41,    1,  113,   60,  121,
   41,   41,    1,    4,    5,    6,    7,    8,  122,    4,
    5,    6,    7,    8,   42,   35,   66,   10,   11,   40,
   42,  108,  169,   10,   11,   36,  106,  102,    0,   70,
    6,    7,    8,   35,    0,    0,  144,  144,   72,   56,
  144,   93,   10,  144,  144,  144,  144,  144,  144,  144,
   41,   56,   36,  142,  142,   56,  185,  142,  144,  144,
  142,  142,  142,  142,  142,  142,  142,   56,    0,   58,
   58,  194,    0,   58,   68,  142,  142,   58,   58,   58,
   58,   58,   58,   58,   58,   58,   58,   58,   58,   56,
    0,   58,   58,   54,   54,   56,    0,   54,   66,   56,
  218,   54,   54,   54,   54,   54,   54,   54,   54,   54,
   54,   54,   54,   56,  220,   54,   54,   56,    0,   44,
   44,    0,   21,   44,    0,   52,   53,   44,   44,   44,
   44,   44,   44,   44,   44,   44,   44,   44,   44,  158,
   31,   44,   44,   35,   30,   55,   37,  157,    0,    0,
    0,    0,   40,   40,    0,    0,   40,    0,   52,   53,
   40,   40,   40,   40,   40,   40,   40,   40,   40,   40,
   40,   40,    0,    0,   40,   40,    0,    0,   55,    0,
    0,    0,    0,   41,   41,    0,    0,   41,    0,   52,
   53,   41,   41,   41,   41,   41,   41,   41,   41,   41,
   41,   41,   41,    0,    0,   41,   41,    0,   68,   55,
    0,   68,    0,  108,  176,   68,   68,   68,   68,   68,
   68,   68,    6,    7,    8,    0,   68,    0,    0,   68,
   68,  176,   66,    0,   10,   66,    0,    0,  108,   66,
   66,   66,   66,   66,   66,   66,    0,    6,    7,    8,
   66,   52,   53,   66,   66,  142,  176,  143,   96,   10,
    0,    0,   51,   52,   53,   54,   82,   52,   53,    0,
    0,   55,  154,  153,  155,    0,    0,    0,  119,   52,
   53,   12,    1,   55,  215,    0,    0,   55,  217,    4,
    5,    6,    7,    8,  127,  127,    0,    0,   70,   55,
  196,   52,   53,   10,   11,    0,  198,   52,   53,    0,
  200,   52,   53,  162,   72,    0,    0,    0,    0,    0,
    0,   55,  233,  235,  202,   52,   53,   55,  206,   52,
   53,   55,    0,    0,    0,    0,  178,   43,    0,  237,
    0,    0,    0,    0,  183,   55,    0,  230,    1,   55,
    0,  121,  231,  232,  176,    4,    5,    6,    7,    8,
  122,    0,    0,    0,    0,    1,   42,    0,  121,   10,
   11,   43,    4,    5,    6,    7,    8,  122,  216,    0,
    0,    0,    0,   42,    0,    0,   10,   11,   43,  234,
    1,  212,  178,  121,    0,    0,  178,    4,    5,    6,
    7,    8,  122,    0,    0,    0,    0,   64,   42,    0,
    0,   10,   11,    0,    0,    1,    0,    0,    2,    0,
    0,    3,    4,    5,    6,    7,    8,    9,  227,    0,
  178,  178,   70,   66,    0,   70,   10,   11,    0,   70,
   70,   70,   70,   70,   70,   70,    0,  178,   72,    0,
   70,   72,    0,   70,   70,   72,   72,   72,   72,   72,
   72,   72,  118,    0,    0,    0,   72,    0,    0,   72,
   72,    1,    0,    0,    0,    0,    0,    0,    4,    5,
    6,    7,    8,  149,  150,  151,  152,  190,    1,   42,
    0,  121,   10,   11,    0,    4,    5,    6,    7,    8,
  122,    0,    0,  225,    0,    1,   42,    0,    0,   10,
   11,    0,    4,    5,    6,    7,    8,    0,  115,    0,
    0,  174,    1,   42,   15,    0,   10,   11,    0,    4,
    5,    6,    7,    8,    0,    0,   15,    0,   67,    1,
   42,    0,    2,   10,   11,    3,    4,    5,    6,    7,
    8,    9,    0,    0,    0,    0,    0,  123,  123,    0,
   10,   11,    0,    0,    0,    1,    0,   95,    2,    0,
    0,    3,    4,    5,    6,    7,    8,    9,    0,    0,
    0,  124,  124,    0,    0,    0,   10,   11,    0,   67,
    0,    0,    0,    0,    1,    0,    0,    2,    0,  123,
    3,    4,    5,    6,    7,    8,    9,  123,    0,    0,
  125,  125,    0,    0,    0,   10,   11,    0,  189,  135,
   95,    0,  161,  124,    0,    3,    4,    5,    6,    7,
    8,  124,   57,    0,    0,    1,    0,    0,  121,    0,
   10,   11,    4,    5,    6,    7,    8,  122,    0,    0,
    0,    0,  125,    0,  123,  123,   10,   11,    0,  123,
  125,    0,    0,  126,  126,   57,   57,    0,    0,   57,
    0,    0,   57,    0,    0,    0,    0,    0,  124,  124,
    0,    0,    0,  124,  107,    0,   58,    0,    0,    0,
  123,  123,    0,  123,  123,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,  126,    0,  125,  125,    0,
  123,    0,  125,  126,  124,  124,    0,  124,  124,   58,
   58,    0,    0,   58,    0,    0,   58,    0,    0,    0,
    1,    0,    0,  121,  124,    0,    0,    4,    5,    6,
    7,    8,  122,  125,  125,    0,  125,  125,   42,    0,
    0,   10,   11,    0,    0,    0,    0,    0,   58,    0,
  126,  126,    0,  125,    0,  126,    0,    0,    0,    1,
   57,   57,   57,   57,    0,   57,    4,    5,    6,    7,
    8,    0,    0,    0,   57,    0,    0,   42,    0,    0,
   10,   11,    0,    0,    0,    0,  126,  126,  135,  126,
  126,    0,    0,    0,    0,    4,    0,    6,    7,    8,
    0,    0,    0,    0,    0,    0,  126,    0,    0,   10,
   11,    0,    0,    0,   58,   58,   58,   58,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,   58,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,  123,   40,   40,   79,   80,  123,   14,
   61,   81,   46,  123,   44,   46,  176,    0,  125,   61,
   14,   44,   44,   11,   40,    9,   81,  125,   62,   81,
  123,   41,   41,   61,   44,  123,   40,   44,   31,  125,
   42,   59,   46,  257,  258,   47,   44,   44,  125,   44,
  125,  257,  212,  125,  256,    4,  129,   61,  109,  261,
   65,  256,   41,  277,   44,   40,  261,  101,   37,  139,
   63,   65,   60,   61,   41,   42,   43,   44,   45,   44,
   47,  115,  123,   43,  139,   45,  257,  139,  123,   38,
  256,  257,   46,   60,   61,   62,  142,  143,   41,   42,
   43,   44,   45,  176,   47,  256,  257,   76,   44,  256,
  257,   41,   41,  123,  123,  257,   44,   60,   61,   62,
   41,  109,   44,  123,   41,  125,   43,   44,   45,   41,
   44,   41,  166,   45,  139,  125,  256,  121,  122,  212,
  213,  256,  125,   60,   61,   62,  256,   44,   44,  256,
  256,   44,   44,  125,   44,   44,  123,   41,  256,   43,
   44,   45,   41,  256,  257,   41,   45,  125,  261,  262,
  256,  264,  265,  266,  267,  268,   60,   61,   62,  256,
  123,  256,  275,    0,  256,  278,  279,  156,   41,   12,
   43,   44,   45,   41,  139,  144,  145,   45,  147,  266,
  267,  268,   44,   80,  212,   96,  123,   60,   61,   62,
   -1,  278,   41,  139,  256,  256,  257,   41,  257,  260,
  256,  256,  257,  264,  265,  266,  267,  268,  269,  264,
  265,  266,  267,  268,  275,  257,   41,  278,  279,  123,
  275,  257,   41,  278,  279,  276,  256,  256,   -1,  256,
  266,  267,  268,  257,   -1,   -1,  256,  257,  256,   45,
  260,  256,  278,  263,  264,  265,  266,  267,  268,  269,
  123,   45,  276,  256,  257,   45,  256,  260,  278,  279,
  263,  264,  265,  266,  267,  268,  269,   45,   -1,  256,
  257,  256,   -1,  260,  123,  278,  279,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,   45,
   -1,  278,  279,  256,  257,   45,   -1,  260,  123,   45,
  256,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,   45,  256,  278,  279,   45,   -1,  256,
  257,   -1,  256,  260,   -1,  257,  258,  264,  265,  266,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  256,
  256,  278,  279,  256,  256,  277,  256,  256,   -1,   -1,
   -1,   -1,  256,  257,   -1,   -1,  260,   -1,  257,  258,
  264,  265,  266,  267,  268,  269,  270,  271,  272,  273,
  274,  275,   -1,   -1,  278,  279,   -1,   -1,  277,   -1,
   -1,   -1,   -1,  256,  257,   -1,   -1,  260,   -1,  257,
  258,  264,  265,  266,  267,  268,  269,  270,  271,  272,
  273,  274,  275,   -1,   -1,  278,  279,   -1,  257,  277,
   -1,  260,   -1,  257,  123,  264,  265,  266,  267,  268,
  269,  270,  266,  267,  268,   -1,  275,   -1,   -1,  278,
  279,  123,  257,   -1,  278,  260,   -1,   -1,  257,  264,
  265,  266,  267,  268,  269,  270,   -1,  266,  267,  268,
  275,  257,  258,  278,  279,   43,  123,   45,   43,  278,
   -1,   -1,  256,  257,  258,  259,  256,  257,  258,   -1,
   -1,  277,   60,   61,   62,   -1,   -1,   -1,  256,  257,
  258,  123,  257,  277,  177,   -1,   -1,  277,  181,  264,
  265,  266,  267,  268,   79,   80,   -1,   -1,  123,  277,
  256,  257,  258,  278,  279,   -1,  256,  257,  258,   -1,
  256,  257,  258,   98,  123,   -1,   -1,   -1,   -1,   -1,
   -1,  277,  215,  216,  256,  257,  258,  277,  256,  257,
  258,  277,   -1,   -1,   -1,   -1,  121,  123,   -1,  232,
   -1,   -1,   -1,   -1,  129,  277,   -1,  256,  257,  277,
   -1,  260,  261,  262,  123,  264,  265,  266,  267,  268,
  269,   -1,   -1,   -1,   -1,  257,  275,   -1,  260,  278,
  279,  123,  264,  265,  266,  267,  268,  269,  270,   -1,
   -1,   -1,   -1,  275,   -1,   -1,  278,  279,  123,  256,
  257,  176,  177,  260,   -1,   -1,  181,  264,  265,  266,
  267,  268,  269,   -1,   -1,   -1,   -1,  125,  275,   -1,
   -1,  278,  279,   -1,   -1,  257,   -1,   -1,  260,   -1,
   -1,  263,  264,  265,  266,  267,  268,  269,  213,   -1,
  215,  216,  257,  125,   -1,  260,  278,  279,   -1,  264,
  265,  266,  267,  268,  269,  270,   -1,  232,  257,   -1,
  275,  260,   -1,  278,  279,  264,  265,  266,  267,  268,
  269,  270,  125,   -1,   -1,   -1,  275,   -1,   -1,  278,
  279,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
  266,  267,  268,  271,  272,  273,  274,  125,  257,  275,
   -1,  260,  278,  279,   -1,  264,  265,  266,  267,  268,
  269,   -1,   -1,  125,   -1,  257,  275,   -1,   -1,  278,
  279,   -1,  264,  265,  266,  267,  268,   -1,  270,   -1,
   -1,  256,  257,  275,    0,   -1,  278,  279,   -1,  264,
  265,  266,  267,  268,   -1,   -1,   12,   -1,   14,  257,
  275,   -1,  260,  278,  279,  263,  264,  265,  266,  267,
  268,  269,   -1,   -1,   -1,   -1,   -1,   79,   80,   -1,
  278,  279,   -1,   -1,   -1,  257,   -1,   43,  260,   -1,
   -1,  263,  264,  265,  266,  267,  268,  269,   -1,   -1,
   -1,   79,   80,   -1,   -1,   -1,  278,  279,   -1,   65,
   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,   -1,  121,
  263,  264,  265,  266,  267,  268,  269,  129,   -1,   -1,
   79,   80,   -1,   -1,   -1,  278,  279,   -1,  256,  257,
   96,   -1,   98,  121,   -1,  263,  264,  265,  266,  267,
  268,  129,    4,   -1,   -1,  257,   -1,   -1,  260,   -1,
  278,  279,  264,  265,  266,  267,  268,  269,   -1,   -1,
   -1,   -1,  121,   -1,  176,  177,  278,  279,   -1,  181,
  129,   -1,   -1,   79,   80,   37,   38,   -1,   -1,   41,
   -1,   -1,   44,   -1,   -1,   -1,   -1,   -1,  176,  177,
   -1,   -1,   -1,  181,   56,   -1,    4,   -1,   -1,   -1,
  212,  213,   -1,  215,  216,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   76,  121,   -1,  176,  177,   -1,
  232,   -1,  181,  129,  212,  213,   -1,  215,  216,   37,
   38,   -1,   -1,   41,   -1,   -1,   44,   -1,   -1,   -1,
  257,   -1,   -1,  260,  232,   -1,   -1,  264,  265,  266,
  267,  268,  269,  212,  213,   -1,  215,  216,  275,   -1,
   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,   76,   -1,
  176,  177,   -1,  232,   -1,  181,   -1,   -1,   -1,  257,
  142,  143,  144,  145,   -1,  147,  264,  265,  266,  267,
  268,   -1,   -1,   -1,  156,   -1,   -1,  275,   -1,   -1,
  278,  279,   -1,   -1,   -1,   -1,  212,  213,  257,  215,
  216,   -1,   -1,   -1,   -1,  264,   -1,  266,  267,  268,
   -1,   -1,   -1,   -1,   -1,   -1,  232,   -1,   -1,  278,
  279,   -1,   -1,   -1,  142,  143,  144,  145,   -1,  147,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  156,
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
"bloque : bloque_cambio_bloque_basico",
"bloque : bloque bloque_cambio_bloque_basico",
"sentencia : ejecucion ','",
"sentencia : declaracion ','",
"sentencia : ejecucion error",
"sentencia : declaracion error",
"bloque_cambio_bloque_basico : bloque_if ','",
"bloque_cambio_bloque_basico : bloque_while ','",
"ejecucion : asignacion",
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

//#line 1301 "grammar.y"
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
public static int basicBlockCounter = 1;
public static Map<String, ArrayList<String>> propagatedConstantsValuesMap = new HashMap<>();
public static ArrayList<SyntaxNode> assignSentencesNodesList = new ArrayList<>();

private void checkSubtreeConstantPropagations(SyntaxNode node) {
    if (node != null) {
        if (node.isPropagated()) {
            if (node.getName().equals("=")) { // Asign node! We add this constant
              final String variableName = node.getLeftChild().getName().equalsIgnoreCase("acceso") ?
                      node.getLeftChild().getLeftChild().getName() :
                      node.getLeftChild().getName();

              node.setBlockOfPropagation(basicBlockCounter);

              ArrayList<String> valuesList = propagatedConstantsValuesMap.get(variableName);

              if (valuesList == null) {
                valuesList = new ArrayList<>();
              }

              valuesList.add(node.getPropagatedValue());
              propagatedConstantsValuesMap.put(variableName, valuesList);
            } else {
                final String variableName = node.getName().equalsIgnoreCase("acceso") ?
                      node.getLeftChild().getName() :
                      node.getName();
                ArrayList<String> valuesList = propagatedConstantsValuesMap.get(variableName);
                if (valuesList != null && valuesList.contains(node.getPropagatedValue())) {

                  // Propagation is correct. We morph node into a constant node.
                  node.setName(node.getPropagatedValue());
                  node.setType(node.getPropagatedValueType());
                  node.setRightChild(null);
                  node.setLeftChild(null);
                } else {
                  if (!node.getName().equalsIgnoreCase(node.getPropagatedValue())) {
                    // Propagation was made with a value from another basic block
                    // Add an assign node to the list, to ensure the variables enter the new basic block with correct values
                    node.setPropagated(false);

                    SyntaxNode variableNode = new SyntaxNode(node.getName(), node.getLeftChild(), node.getRightChild(), node.getType());
                    SyntaxNode assignSentence = new SyntaxNode("=", variableNode, new SyntaxNode(node.getPropagatedValue(), node.getPropagatedValueType()) );
                    assignSentence.setType(node.getType());
                    assignSentence.setPropagated(false);

                    if (!isDuplicate(assignSentence))
                      assignSentencesNodesList.add(assignSentence);
                  }
                }
            }
        }

        if (node.getLeftChild() != null &&
            !node.getLeftChild().getName().equalsIgnoreCase("if") &&
            !node.getLeftChild().getName().equalsIgnoreCase("while")) {
          checkSubtreeConstantPropagations(node.getLeftChild());
        }

        if (node.getRightChild() != null &&
            !node.getRightChild().getName().equalsIgnoreCase("if") &&
            !node.getRightChild().getName().equalsIgnoreCase("while")) {
          checkSubtreeConstantPropagations(node.getRightChild());
        }
    }
}

private boolean isDuplicate(SyntaxNode newNode) {
    for (SyntaxNode existingNode : assignSentencesNodesList) {
      if (existingNode.equals(newNode)) {
        return true;
      }
    }
    return false;
}

private SyntaxNode createAssignSubtree() {
  SyntaxNode responseNode = null;

  if (!assignSentencesNodesList.isEmpty()) {
    while (assignSentencesNodesList.size() > 1) {
      ArrayList<SyntaxNode> newNodes = new ArrayList<>();
      for (int i = 0; i < assignSentencesNodesList.size(); i += 2) {
        if (i + 1 < assignSentencesNodesList.size()) {
          newNodes.add(new SyntaxNode("Nodo Compuesto", assignSentencesNodesList.get(i), assignSentencesNodesList.get(i + 1)));
        } else {
          newNodes.add(assignSentencesNodesList.get(i));
        }
      }
      assignSentencesNodesList = newNodes;
    }

    responseNode = assignSentencesNodesList.get(0);
  }


  return responseNode;
}

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
//#line 1331 "Parser.java"
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
//#line 34 "grammar.y"
{
                padre = new SyntaxNode("root", (SyntaxNode) val_peek(1).obj , null);
            	verificarReglasCheck();
            }
break;
case 3:
//#line 39 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 4:
//#line 40 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un {.");}
break;
case 5:
//#line 45 "grammar.y"
{
                yyval = val_peek(0);
            }
break;
case 6:
//#line 48 "grammar.y"
{
                if (val_peek(0).obj != null) {
                    /*basicBlockCounter++;*/

                    /*propagatedConstantsValuesMap.clear();*/
                    /*assignSentencesNodesList.clear();*/

                    /*checkSubtreeConstantPropagations((SyntaxNode) $2.obj);*/
                    checkSubtreeConstantPropagations(new SyntaxNode("checking node", (SyntaxNode) val_peek(1).obj, (SyntaxNode) val_peek(0).obj));

                    SyntaxNode assignNodes = createAssignSubtree();
                    SyntaxNode completeNode = (assignNodes == null) ?
                            (SyntaxNode) val_peek(0).obj :
                            new SyntaxNode("Bloque de sentencias con asignaciones", assignNodes, (SyntaxNode) val_peek(0).obj);

                    yyval = new ParserVal(new SyntaxNode("Bloque de sentencias", (SyntaxNode) val_peek(1).obj, completeNode));
                }
            }
break;
case 7:
//#line 66 "grammar.y"
{
      		    yyval = val_peek(0);
            }
break;
case 8:
//#line 69 "grammar.y"
{
      		    if (val_peek(0).obj != null) {
      		        propagatedConstantsValuesMap.clear();
                    assignSentencesNodesList.clear();
                    checkSubtreeConstantPropagations((SyntaxNode) val_peek(1).obj);

                    SyntaxNode assignNodes = createAssignSubtree();
                    SyntaxNode completeNode = (assignNodes == null) ?
                            (SyntaxNode) val_peek(1).obj :
                            new SyntaxNode("Bloque de sentencias con asignaciones 2", assignNodes, (SyntaxNode) val_peek(1).obj);

                    yyval = new ParserVal(new SyntaxNode("Bloque de sentencias21", (SyntaxNode) val_peek(0).obj, completeNode));
                }
            }
break;
case 9:
//#line 83 "grammar.y"
{
                yyval = val_peek(0);
            }
break;
case 10:
//#line 86 "grammar.y"
{
                if (val_peek(0).obj != null) {
                    basicBlockCounter++;

                    propagatedConstantsValuesMap.clear();
                    assignSentencesNodesList.clear();

                    checkSubtreeConstantPropagations((SyntaxNode) val_peek(0).obj);

                    SyntaxNode assignNodes = createAssignSubtree();
                    SyntaxNode completeNode = (assignNodes == null) ?
                            (SyntaxNode) val_peek(0).obj :
                            new SyntaxNode("Bloque de sentencias con asignaciones", assignNodes, (SyntaxNode) val_peek(0).obj);



                    propagatedConstantsValuesMap.clear();
                    assignSentencesNodesList.clear();
                    checkSubtreeConstantPropagations((SyntaxNode) val_peek(1).obj);

                    SyntaxNode assignNodesLeftBlock = createAssignSubtree();
                    SyntaxNode completeLeftNode = (assignNodes == null) ?
                            (SyntaxNode) val_peek(1).obj :
                            new SyntaxNode("Bloque de sentencias con asignaciones 2", assignNodesLeftBlock, (SyntaxNode) val_peek(1).obj);

                    yyval = new ParserVal(new SyntaxNode("Bloque de sentencias - Cambio de bloque", completeLeftNode, completeNode));

                    propagatedConstantsValuesMap.clear();
                    assignSentencesNodesList.clear();
                }
            }
break;
case 11:
//#line 120 "grammar.y"
{ yyval = val_peek(1); }
break;
case 13:
//#line 122 "grammar.y"
{ /*$$ = $1;*/
                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                }
break;
case 14:
//#line 125 "grammar.y"
{ logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','."); }
break;
case 15:
//#line 129 "grammar.y"
{ yyval = val_peek(1); }
break;
case 16:
//#line 130 "grammar.y"
{ yyval = val_peek(1); }
break;
case 17:
//#line 134 "grammar.y"
{ yyval = val_peek(0);}
break;
case 18:
//#line 135 "grammar.y"
{ yyval = val_peek(0);}
break;
case 19:
//#line 136 "grammar.y"
{ yyval = val_peek(0);}
break;
case 20:
//#line 140 "grammar.y"
{ yyval = val_peek(0);}
break;
case 21:
//#line 141 "grammar.y"
{ yyval = val_peek(0);}
break;
case 22:
//#line 142 "grammar.y"
{ yyval = val_peek(0);}
break;
case 23:
//#line 149 "grammar.y"
{
                              logger.logDebugSyntax("Declaración1 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 24:
//#line 152 "grammar.y"
{ basicBlockCounter++; }
break;
case 26:
//#line 157 "grammar.y"
{yyval = val_peek(0);}
break;
case 27:
//#line 158 "grammar.y"
{yyval = val_peek(0);}
break;
case 28:
//#line 159 "grammar.y"
{yyval = val_peek(0);}
break;
case 29:
//#line 160 "grammar.y"
{yyval = val_peek(0);}
break;
case 30:
//#line 166 "grammar.y"
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
case 31:
//#line 188 "grammar.y"
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
case 32:
//#line 208 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la variable.");
	       	}
break;
case 33:
//#line 214 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode("ListaVariables", (SyntaxNode)val_peek(2).obj, new SyntaxNode(val_peek(0).sval)));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 34:
//#line 218 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval));
                    lista_variables.add(val_peek(0).sval +":" +  Parser.ambito);
            }
break;
case 35:
//#line 226 "grammar.y"
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

                                /*var asign = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);*/
                                /*asign.setType(tipo_validado);*/
                                /*$$ = new ParserVal(asign);*/

                                var t = SymbolTable.getInstance();

                                var entrada = t.getAttribute(nombreCompleto);
                                if (entrada.isPresent()) {
                                    Attribute entry = entrada.get();

                                    entry.addAmbito(ambito);

                                    var asignNode = new SyntaxNode("=", leftSyntaxNode, rightSyntaxNode);
                                    asignNode.setType(tipo_validado);
                                    yyval = new ParserVal(asignNode);

                                    if (rightSyntaxNode != null) {
                                      Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();
                                      if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { /* If its a leaf node, then the value its a constant*/
                                          entry.setActive(true);
                                          entry.setValue(rightSyntaxNode.getName());
                                          entry.setConstantValueBlock(basicBlockCounter);

                                          asignNode.setPropagated(true);
                                          asignNode.setBlockOfPropagation(basicBlockCounter);
                                          asignNode.setPropagatedValue(rightSyntaxNode.getName());
                                          asignNode.setPropagatedValueType(rightSyntaxNode.getType());
                                      } else {
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
case 36:
//#line 282 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 37:
//#line 285 "grammar.y"
{
                    logger.logDebugSyntax("Asignación en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode accessNode = (SyntaxNode) val_peek(2).obj;
                    SyntaxNode rightSyntaxNode = (SyntaxNode) val_peek(0).obj;

                    String tipo_validado = validarTipos(accessNode, rightSyntaxNode, true);

                    if (!tipo_validado.equals("Error")) {
                        /*var asign = new SyntaxNode("=", accessNode, rightSyntaxNode);*/
                        /*asign.setType(tipo_validado);*/
                        /*$$ = new ParserVal(asign);*/

                        Attribute memberAttr = getMemberVarAttribute(accessNode);

                        if (memberAttr != null && rightSyntaxNode != null) {
                            Optional<Attribute> childNodeValue = rightSyntaxNode.getNodeValue();

                            var asignNode = new SyntaxNode("=", accessNode, rightSyntaxNode);
                            asignNode.setType(tipo_validado);
                            yyval = new ParserVal(asignNode);

                            if (rightSyntaxNode.isLeaf() && childNodeValue.isPresent() && childNodeValue.get().getUso() == UsesType.CONSTANT) { /* If its a leaf node, then the value its a constant*/
                              memberAttr.setActive(true);
                              memberAttr.setValue(rightSyntaxNode.getName());
                              memberAttr.setConstantValueBlock(basicBlockCounter);

                              asignNode.setPropagated(true);
                              asignNode.setBlockOfPropagation(basicBlockCounter);
                              asignNode.setPropagatedValue(rightSyntaxNode.getName());
                              asignNode.setPropagatedValueType(rightSyntaxNode.getType());
                            } else {
                              memberAttr.setActive(false);
                              memberAttr.setValue(null);
                            }
                        }
                    }
            }
break;
case 38:
//#line 323 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una expresion aritmética");
            }
break;
case 39:
//#line 329 "grammar.y"
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
case 40:
//#line 376 "grammar.y"
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
case 41:
//#line 395 "grammar.y"
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
case 42:
//#line 413 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 43:
//#line 414 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un termino");}
break;
case 44:
//#line 415 "grammar.y"
{ yyval = val_peek(0); }
break;
case 45:
//#line 419 "grammar.y"
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

                                            var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, factorNode);
                                            x.setType(tipo_validado);
                                            yyval = new ParserVal(x);

                                            if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                                final String value = entry.getValue();

                                                factorNode.setPropagated(true);
                                                factorNode.setBlockOfPropagation(basicBlockCounter);
                                                factorNode.setPropagatedValue(value);
                                                factorNode.setPropagatedValueType(entry.getType());
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);

                                        var x = new SyntaxNode("*", (SyntaxNode) val_peek(2).obj, factorNode);
                                        x.setType(tipo_validado);
                                        yyval = new ParserVal(x);

                                        if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = memberAttr.getValue();

                                            factorNode.setPropagated(true);
                                            factorNode.setBlockOfPropagation(basicBlockCounter);
                                            factorNode.setPropagatedValue(value);
                                            factorNode.setPropagatedValueType(memberAttr.getType());
                                        }
                                    }
                                }
                            }
                        }
            }
break;
case 46:
//#line 472 "grammar.y"
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

                                            if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                                final String value = entry.getValue();

                                                factorNode.setPropagated(true);
                                                factorNode.setBlockOfPropagation(basicBlockCounter);
                                                factorNode.setPropagatedValue(value);
                                                factorNode.setPropagatedValueType(entry.getType());

                                                /*var x = new SyntaxNode("/", (SyntaxNode) $1.obj, new SyntaxNode(value, entry.getType()));*/
                                                var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, factorNode);
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            } else {
                                                var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, factorNode);
                                                x.setType(tipo_validado);
                                                yyval = new ParserVal(x);
                                            }
                                        }
                                    }
                                } else {
                                    if (factorNode.getName().equalsIgnoreCase("acceso")) {
                                        Attribute memberAttr = getMemberVarAttribute(factorNode);
                                        if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = memberAttr.getValue();

                                            factorNode.setPropagated(true);
                                            factorNode.setBlockOfPropagation(basicBlockCounter);
                                            factorNode.setPropagatedValue(value);
                                            factorNode.setPropagatedValueType(memberAttr.getType());

                                            /*var x = new SyntaxNode("/", (SyntaxNode) $1.obj, new SyntaxNode(value, memberAttr.getType()));*/
                                            var x = new SyntaxNode("/", (SyntaxNode) val_peek(2).obj, factorNode);
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
case 47:
//#line 533 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
   			}
break;
case 48:
//#line 536 "grammar.y"
{
   			            logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un factor");
            }
break;
case 49:
//#line 539 "grammar.y"
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

                                        if (entry.isActive() && entry.getConstantValueBlock() == basicBlockCounter) {
                                            final String value = entry.getValue();

                                            SyntaxNode factorNode = (SyntaxNode) val_peek(0).obj;

                                            factorNode.setPropagated(true);
                                            factorNode.setBlockOfPropagation(basicBlockCounter);
                                            factorNode.setPropagatedValue(value);
                                            factorNode.setPropagatedValueType(entry.getType());

                                            /*$$ = new ParserVal(new SyntaxNode(value, entry.getType()));*/
                                            yyval = new ParserVal(factorNode);
                                        } else {
                                            yyval = val_peek(0);
                                        }
                                    }
                                }
                            } else {
                                if (node.getName().equalsIgnoreCase("acceso")) {
                                    Attribute memberAttr = getMemberVarAttribute(node);
                                    if (memberAttr != null && memberAttr.isActive() && memberAttr.getConstantValueBlock() == basicBlockCounter) {
                                        final String value = memberAttr.getValue();

                                        SyntaxNode factorNode = (SyntaxNode) val_peek(0).obj;

                                        factorNode.setPropagated(true);
                                        factorNode.setBlockOfPropagation(basicBlockCounter);
                                        factorNode.setPropagatedValue(value);
                                        factorNode.setPropagatedValueType(memberAttr.getType());

                                        yyval = new ParserVal(factorNode);
                                        /*$$ = new ParserVal(new SyntaxNode(value, memberAttr.getType()));*/
                                    } else {
                                        yyval = val_peek(0);
                                    }
                                }
                            }
                        }
   			}
break;
case 50:
//#line 597 "grammar.y"
{yyval = val_peek(0);}
break;
case 51:
//#line 598 "grammar.y"
{yyval = val_peek(1);}
break;
case 52:
//#line 599 "grammar.y"
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
                                    finalValue = "-" + value;
                                    SymbolTable.addSymbol(finalValue, TokenType.CONSTANT, symbolType, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                                }
                                yyval = new ParserVal(new SyntaxNode(finalValue, symbolType)); /* Crear un nodo para la constante*/
                            }
      		            }

      		}
break;
case 53:
//#line 620 "grammar.y"
{
                        String value = val_peek(0).sval;

                        value = processFloat(value, false);

                        if (!value.equals("Error")) {
                            SymbolTable.addSymbol("-" + value, TokenType.CONSTANT, UsesType.FLOAT, UsesType.CONSTANT, LexicalAnalyzer.getLine());
                            yyval = new ParserVal(new SyntaxNode("-" + value, UsesType.FLOAT)); /* Crear un nodo para la constante*/
                        }
            }
break;
case 54:
//#line 630 "grammar.y"
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
case 55:
//#line 640 "grammar.y"
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
case 56:
//#line 653 "grammar.y"
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
case 57:
//#line 666 "grammar.y"
{
                        yyval=val_peek(0);
            }
break;
case 58:
//#line 669 "grammar.y"
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
case 59:
//#line 683 "grammar.y"
{ yyval = new ParserVal("=="); }
break;
case 60:
//#line 684 "grammar.y"
{ yyval = new ParserVal("<="); }
break;
case 61:
//#line 685 "grammar.y"
{ yyval = new ParserVal(">="); }
break;
case 62:
//#line 686 "grammar.y"
{ yyval = new ParserVal("<"); }
break;
case 63:
//#line 687 "grammar.y"
{ yyval = new ParserVal(">"); }
break;
case 64:
//#line 688 "grammar.y"
{ yyval = new ParserVal("!!"); }
break;
case 65:
//#line 689 "grammar.y"
{
			        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": no puede realizarse una asignacion. Quizas queria poner '=='?");
			        }
break;
case 66:
//#line 695 "grammar.y"
{
                                    String tipo_validado = validarTipos((SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, false);
                                    if (!tipo_validado.equals("Error")) {
                                        yyval = new ParserVal(new SyntaxNode(val_peek(1).sval, (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(0).obj, tipo_validado));
                                    }
            }
break;
case 67:
//#line 701 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 68:
//#line 702 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": condicion no valida.");}
break;
case 69:
//#line 706 "grammar.y"
{ yyval = val_peek(1); }
break;
case 70:
//#line 707 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '()'.");}
break;
case 71:
//#line 708 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '('.");}
break;
case 72:
//#line 709 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un ')'."); yyval = val_peek(0); }
break;
case 73:
//#line 710 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta establecer una condicion.");}
break;
case 74:
//#line 714 "grammar.y"
{
			    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(1).obj, null);
                yyval = new ParserVal(new SyntaxNode("IF", (SyntaxNode)val_peek(2).obj, thenSyntaxNode, null));
                logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());
                                                                 }
break;
case 75:
//#line 719 "grammar.y"
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
case 76:
//#line 730 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 77:
//#line 731 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");}
break;
case 78:
//#line 732 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");}
break;
case 79:
//#line 733 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");}
break;
case 80:
//#line 737 "grammar.y"
{ yyval = val_peek(0); }
break;
case 81:
//#line 738 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 82:
//#line 739 "grammar.y"
{ yyval = val_peek(1) ; }
break;
case 83:
//#line 740 "grammar.y"
{
                                                                    yyval = val_peek(1) ;
                                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");
                                                                    }
break;
case 84:
//#line 744 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 85:
//#line 748 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": RETURN fuera de función.");}
break;
case 86:
//#line 749 "grammar.y"
{yyval = val_peek(0);}
break;
case 87:
//#line 753 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("SecuenciaSentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj)); }
break;
case 88:
//#line 754 "grammar.y"
{ yyval = val_peek(0); }
break;
case 89:
//#line 758 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 90:
//#line 762 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 91:
//#line 765 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
            }
break;
case 92:
//#line 773 "grammar.y"
{
                    logger.logDebugSyntax("VOID en la linea " + LexicalAnalyzer.getLine());
                    String nombreFuncion = val_peek(3).sval;
                    arbolFunciones.add(new SyntaxNode("DeclaracionFuncion=" + nombreFuncion, (SyntaxNode) val_peek(1).obj, null));
                    checkFlagAmbito();
            }
break;
case 93:
//#line 779 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '}'");
            }
break;
case 94:
//#line 782 "grammar.y"
{
                    logger.logErrorSyntax("VOID en la linea " + LexicalAnalyzer.getLine() + ": falta un '{'");
            }
break;
case 95:
//#line 788 "grammar.y"
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
case 96:
//#line 819 "grammar.y"
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
case 97:
//#line 836 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 98:
//#line 839 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion");
            }
break;
case 99:
//#line 842 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 100:
//#line 845 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": faltan los parentesis '(' ')'.");
            }
break;
case 101:
//#line 848 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre de la funcion.");
            }
break;
case 102:
//#line 854 "grammar.y"
{ yyval = val_peek(0); }
break;
case 103:
//#line 855 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": Parametro mal declarado. Solo es posible declarar un único parámetro");}
break;
case 104:
//#line 859 "grammar.y"
{
                    yyval = new ParserVal(new SyntaxNode(val_peek(0).sval, val_peek(1).sval));
            }
break;
case 105:
//#line 862 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el tipo del parametro.");
            }
break;
case 106:
//#line 865 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el nombre del parametro.");
            }
break;
case 107:
//#line 871 "grammar.y"
{
        SyntaxNode sentenciasSyntaxNode = (SyntaxNode)val_peek(1).obj;
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        if (sentenciasSyntaxNode != null)
        	yyval = new ParserVal(new SyntaxNode("Bloque de sentencias1", sentenciasSyntaxNode, returnSyntaxNode));
    }
break;
case 108:
//#line 877 "grammar.y"
{
        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias2", null, returnSyntaxNode));
    }
break;
case 109:
//#line 881 "grammar.y"
{
        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una sentencia 'RETURN' en el bloque 'void'.");
        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias3", (SyntaxNode)val_peek(0).obj, null));
    }
break;
case 110:
//#line 887 "grammar.y"
{
        /* Aquí, simplemente expandimos la secuencia de sentencias sin un nodo intermedio*/
        yyval = new ParserVal(new SyntaxNode("Sentencias", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
    }
break;
case 111:
//#line 891 "grammar.y"
{
        yyval = val_peek(0);
    }
break;
case 112:
//#line 897 "grammar.y"
{ yyval = val_peek(0); }
break;
case 113:
//#line 898 "grammar.y"
{ yyval = val_peek(0); }
break;
case 114:
//#line 899 "grammar.y"
{ yyval = val_peek(0); }
break;
case 115:
//#line 900 "grammar.y"
{ yyval = val_peek(0); }
break;
case 116:
//#line 901 "grammar.y"
{ yyval = val_peek(0); }
break;
case 117:
//#line 902 "grammar.y"
{ yyval = val_peek(0); }
break;
case 119:
//#line 907 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 121:
//#line 912 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 122:
//#line 916 "grammar.y"
{
                    logger.logDebugSyntax("Bloque WHILE en la linea " + LexicalAnalyzer.getLine());
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(2).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 123:
//#line 920 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta definir un bloque de sentencias.");
            }
break;
case 124:
//#line 923 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra 'DO'.");
                    yyval = new ParserVal(new SyntaxNode("while", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(0).obj));
            }
break;
case 125:
//#line 930 "grammar.y"
{
                    logger.logDebugSyntax("Bloque IF en la linea " + LexicalAnalyzer.getLine());

                    SyntaxNode thenSyntaxNode = new SyntaxNode("THEN", (SyntaxNode)val_peek(1).obj, null);
                    yyval = new ParserVal(new SyntaxNode("IF", (SyntaxNode)val_peek(2).obj, thenSyntaxNode, null));
            }
break;
case 126:
//#line 936 "grammar.y"
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
case 127:
//#line 951 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 128:
//#line 954 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta la palabra reservada END_IF.");
            }
break;
case 129:
//#line 957 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta palabra reservada ELSE");
            }
break;
case 130:
//#line 960 "grammar.y"
{
                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta condicion o parentesis para la condicion.");
            }
break;
case 131:
//#line 966 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 132:
//#line 967 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias4", null, returnSyntaxNode));
		    }
break;
case 133:
//#line 971 "grammar.y"
{
		        SyntaxNode returnSyntaxNode = new SyntaxNode("RETURN");
		        yyval = new ParserVal(new SyntaxNode("Bloque de sentencias5", null, returnSyntaxNode));
		    }
break;
case 134:
//#line 975 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj)); }
break;
case 135:
//#line 976 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(1).obj, (SyntaxNode)val_peek(2).obj)); }
break;
case 136:
//#line 977 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un '}'.");}
break;
case 137:
//#line 981 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("BloqueSentenciasConReturn", (SyntaxNode)val_peek(0).obj)); }
break;
case 138:
//#line 982 "grammar.y"
{ yyval = new ParserVal(new SyntaxNode("Sentencia", (SyntaxNode)val_peek(0).obj)); }
break;
case 139:
//#line 987 "grammar.y"
{ 
            		/*$$ = $3;*/

                    /*String nombreClase = $1.sval;*/
                    /*arbolFunciones.add(new SyntaxNode("DeclaracionClass=" + nombreClase, (SyntaxNode) $3.obj, null));*/
                    /*checkFlagAmbito();*/

                    eliminarAmbito(); /* Restaura el ámbito anterior al salir de la clase*/
            		}
break;
case 140:
//#line 996 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta un }.");}
break;
case 142:
//#line 999 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");}
break;
case 143:
//#line 1003 "grammar.y"
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
case 144:
//#line 1028 "grammar.y"
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
case 145:
//#line 1059 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");}
break;
case 146:
//#line 1063 "grammar.y"
{
                              logger.logDebugSyntax("Declaración2 de variables en la linea " + LexicalAnalyzer.getLine());
                            }
break;
case 149:
//#line 1068 "grammar.y"
{
                        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": la sentencia declarada no es permitida fuera de un metodo.");
                        }
break;
case 150:
//#line 1074 "grammar.y"
{
               if ((SyntaxNode) val_peek(1).obj != null)
                    yyval = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(1).obj));
            }
break;
case 151:
//#line 1078 "grammar.y"
{
                                                logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                             }
break;
case 152:
//#line 1081 "grammar.y"
{
                if ((SyntaxNode) val_peek(1).obj != null)
                    yyval = new ParserVal(new SyntaxNode("BloqueClass", (SyntaxNode) val_peek(2).obj, (SyntaxNode) val_peek(1).obj));
            }
break;
case 153:
//#line 1085 "grammar.y"
{
                                                    logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                                   }
break;
case 154:
//#line 1088 "grammar.y"
{
		        yyval = new ParserVal(new SyntaxNode("BloqueClase" , (SyntaxNode) val_peek(0).obj, (SyntaxNode) val_peek(1).obj));
		    }
break;
case 155:
//#line 1091 "grammar.y"
{yyval = val_peek(1);}
break;
case 156:
//#line 1092 "grammar.y"
{
                                     logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta una ','.");
                                     yyval = val_peek(1);
                                  }
break;
case 157:
//#line 1099 "grammar.y"
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
case 158:
//#line 1130 "grammar.y"
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
case 159:
//#line 1175 "grammar.y"
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
case 160:
//#line 1190 "grammar.y"
{
		        logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el ID");
		    }
break;
case 161:
//#line 1199 "grammar.y"
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
case 162:
//#line 1214 "grammar.y"
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
case 163:
//#line 1242 "grammar.y"
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
case 164:
//#line 1260 "grammar.y"
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
case 165:
//#line 1289 "grammar.y"
{
                                    logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
                                    yyval = new ParserVal( new SyntaxNode("Print", new SyntaxNode(val_peek(0).sval, "CADENA"), null, "CADENA"));
                               }
break;
case 166:
//#line 1293 "grammar.y"
{
			                        logger.logDebugSyntax("Sentencia PRINT en la linea " + LexicalAnalyzer.getLine());
			                        yyval = new ParserVal( new SyntaxNode("Print", (SyntaxNode) val_peek(0).obj, null, "Factor" ));
			                     }
break;
case 167:
//#line 1297 "grammar.y"
{logger.logErrorSyntax("Linea " + LexicalAnalyzer.getLine() + ": falta el contenido de la impresion.");}
break;
//#line 3063 "Parser.java"
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
