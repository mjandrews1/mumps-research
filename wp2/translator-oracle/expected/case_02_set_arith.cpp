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
//  ; case_02_set_arith — Set, operators: + - * / \ ** # 
//================================================================================= 

//================================================================================= 
//  Set a=7,b=3 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "a", (UNS) "7", svPtr); // Store result s274 
	sym_(SYMSTORE, (UNS) "b", (UNS) "3", svPtr); // Store result s274 

//================================================================================= 
//  Write a+b,!,a-b,!,a*b,!,a/b,!,a\b,!,a#b,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp1, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	add((char *) tmp0, (char *) tmp1, (char *) tmp2); 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp2);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp3, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp4, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	sub((char *) tmp3,(char *) tmp4,(char *) tmp5);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp5);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp6, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp7, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	mult((char *) tmp6, (char *) tmp7, (char *) tmp8);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp8);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp9, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp10, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	divx((char *) tmp9,(char *) tmp10,(char *) tmp11);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp11);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp12, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp13, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	divi((const char *) tmp12,(const char *) tmp13,(char *) tmp14);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp14);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "a", (UNS) tmp15, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "a"); // not found
	if (sym_(SYMGET, (UNS) "b", (UNS) tmp16, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "b"); // not found
	_i=atoi((char *) tmp16);
	_j=atoi((char *) tmp15);
	_i=_j%_i;
	Mltoa(_i, tmp17);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp17);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Set c=2**10 
//================================================================================= 

//--------------
//       Set
//--------------

	expx((const char *) "2", (const char *) "10", (char *) tmp1);
	sym_(SYMSTORE, (UNS) "c", (UNS) tmp1, svPtr); // Store result s274 

//================================================================================= 
//  Write c,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "c", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "c"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Set d=-7 
//================================================================================= 

//--------------
//       Set
//--------------

	mult((char *) "7","-1",(char *) tmp1);
	sym_(SYMSTORE, (UNS) "d", (UNS) tmp1, svPtr); // Store result s274 

//================================================================================= 
//  Write d\3,!,(-d)\3,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "d", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "d"); // not found
	divi((const char *) tmp0,(const char *) "3",(char *) tmp1);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (sym_(SYMGET, (UNS) "d", (UNS) tmp2, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "d"); // not found
	mult((char *) tmp2,"-1",(char *) tmp3);
	divi((const char *) tmp3,(const char *) "3",(char *) tmp4);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp4);
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
