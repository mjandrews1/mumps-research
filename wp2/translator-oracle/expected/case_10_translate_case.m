// ==============================================================
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>
//#+ <TS>

#undef _MDH_
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <setjmp.h>
#include <unistd.h>
#include <signal.h>
#include <sys/time.h>
#include <mumpsc/inline.h>
#include <mumpsc/defines.h>
#include <mumpsc/strmax.h>
#include <mumpsc/libmpscpp.h>
#include <mumpsc/sym.h>
#include <mumpsc/builtin.h>
#include <mumpsc/compiler.h>
#define FORPARM STR_MAX  /* Size of For loop parameter */
#define CSTR (char *)
#define UNS unsigned char *
#define uns unsigned char *
#define CCHR const char *
#include <mumpsc/keyfix.h>
#include <mumpsc/globalOrder.h>
#include <mumpsc/stateVector.h>
int (*__label_lookup)(char *);
const char * (*__text_function)(int);
void fcn(MSV *);
void zfcn(unsigned char *, unsigned char *, struct MSV *); /* z functions */
char * sym_(int, unsigned char *, unsigned char *, struct MSV *); /* symbol table */
char * makeLock(char *, char *, char *, char, char *); /* global array lock service */
char * cleanLocks(char *); /* global array lock service */
char * releaseLock(char *, char *, char *); /* global array lock service */
char * eraseLocks(char *); /* global array lock service */
void add(const char *a,const char *b,char *c); /* math function */
void sub(const char *a,const char *b,char *c); /* math function */
void mult(const char *a,const char *b,char *c); /* math function */
void divx(const char *a,const char *b,char *c); /* math function */
void divi(const char *a,const char *b,char *c); /* math function */
void _fnumber(unsigned char *,unsigned char *,
       unsigned char *,unsigned char *, MSV *); /* math function */
int DBSem = 0; /* deadlock prevention */
int pm (unsigned char [], const unsigned char pattern[], struct MSV * svPtr); /* pattern match */

static inline int floatstring(const char * p) { 
 int f=0;  
 for (; *p!=0; p++) {  
	 if (    isdigit(*p) || 
	 	*p == '-' ||  
		*p == '+' ) continue;  
 	if (    *p=='.' ||  
		tolower(*p)=='e' ) { 
		f=1;  
		continue;  
		} 
	return 0; 
	}  
 return f;  
 } 
 
inline int numcomp2(unsigned char *aa) { 
 if (floatstring((char *) aa)) return (atof( (char *) aa) != 0.0);  
 return atoi((char *) aa);  
 }

inline char * strxcat(char * a, char *b)  
{ while (*a) a++; while( (*(a) = *(b)) ) { a++; b++; } return a; }

inline char * strcatz(char * out, char *in)  
{ while (*out) out++; while (*(out) = *(in)) { out++; in++; } *(out++) = '';  
*out = 0; return (char *) out; } 

void SyncClock() {
    DBSem = 1;
    }

unsigned int set_alarm (unsigned int seconds) {
    struct itimerval old, xnew;
    xnew.it_interval.tv_usec = 0;
    xnew.it_interval.tv_sec = 0 ; // interval between alarms
    xnew.it_value.tv_usec = 0;
    xnew.it_value.tv_sec = (long int) 0;
    if (setitimer (ITIMER_REAL, &xnew, &old) < 0)
        return 0;
    else
        return old.it_value.tv_sec;
    }

#include <mumpsc/global.h>  /* Global array definitions */
  /* Interface to Mglobal(): */
  /* g = command */
  /* key = key sought or stored */
  /* bd = value returned or stored */
  /* svPtr = address of state vector */
#ifndef SUB
#include <mumpsc/fcns.h>  /* System support functions */
#include <mumpsc/sysfunc.h>  /* Many common global variables */
#endif

//================================================================================= 
//  ; case_10_translate_case — $Translate, case conversion 
//================================================================================= 

//================================================================================= 
//  ; (revised pre-freeze 2026-11-14: $ZConvert not present in this build; 
//================================================================================= 

//================================================================================= 
//  ; case conversion expressed via $Translate) 
//================================================================================= 

//================================================================================= 
//  Write $Translate("aBcD","abcd","ABCD"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$translate\xce");
	strxcat((char *) catx ,(char *) "aBcD");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "abcd");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "ABCD");
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp0, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write $Translate("MUMPS ii","ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$translate\xce");
	strxcat((char *) catx ,(char *) "MUMPS ii");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "abcdefghijklmnopqrstuvwxyz");
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp0, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write $Translate("strip-me","-",""),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$translate\xce");
	strxcat((char *) catx ,(char *) "strip-me");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "-");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "");
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp0, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Set s="Hello" 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "s", (UNS) "Hello", svPtr); // Store result s274 

//================================================================================= 
//  Write $Translate(s,"abcdefghijklmnopqrstuvwxyz","ABCDEFGHIJKLMNOPQRSTUVWXYZ"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$translate\xce");
	if (sym_(SYMGET, (UNS) "s", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "s"); // not found
	strxcat((char *) catx ,(char *) tmp0);
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "abcdefghijklmnopqrstuvwxyz");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp1, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write $Translate(s,"ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$translate\xce");
	if (sym_(SYMGET, (UNS) "s", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "s"); // not found
	strxcat((char *) catx ,(char *) tmp0);
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	strxcat((char *) catx,"\xd0");
	strxcat((char *) catx ,(char *) "abcdefghijklmnopqrstuvwxyz");
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp1, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Quit 
//================================================================================= 

//--------------
//       Quit
//--------------

	if (svPtr->Rargsx) { // call-by-reference? 
		int i; 
		unsigned char *tmp = new unsigned char[STR_MAX]; 
		for (i = 0; i < svPtr->Rargsx; i++) { 
		sym_(1, (UNS) svPtr->RefParm[i], (UNS) tmp,svPtr); 
		sym_(SYMSTORE, (UNS) svPtr->Rargs[i], (UNS) tmp, svPtr); 
		} 
		} 
	strmove((UNS) _ReturnVal, (UNS) ""); 
	if (_doStackN[_doStx]) {  _doStackN[_doStx] = 0; } // Q1001 
	if (_doStx > 0) { longjmp(_doStack[_doStx],99); } 
	else ErrorMessage("Too Many Quits", svPtr->LineNumber); 
