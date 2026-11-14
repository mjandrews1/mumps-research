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
//  ; case_07_conditionals — If with &, !, comparisons, $Data, $Get 
//================================================================================= 

//================================================================================= 
//  Set x=5 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "x", (UNS) "5", svPtr); // Store result s274 

//================================================================================= 
//  If x>1&(x<10) Write "in range",! 
//================================================================================= 

//--------------
//       If
//--------------

	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (numcomp1((UNS) tmp0, 1) > 0) strmove((UNS) tmp1, (UNS) "1");
	else strmove((UNS) tmp1, (UNS) "0");
	if (sym_(SYMGET, (UNS) "x", (UNS) tmp2, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (numcomp1((UNS) tmp2,10)<0) strmove((UNS) tmp3, (UNS) "1");
	else strmove((UNS) tmp3, (UNS) "0");
	if (numcomp( (UNS) tmp1, (UNS) "0") && numcomp( (UNS) tmp3, (UNS) "0")) strmove( (UNS) tmp4, (UNS) "1"); else strmove( (UNS) tmp4, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp4 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"in range");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

//================================================================================= 
//  If x>1!(x>100) Write "or true",! 
//================================================================================= 

//--------------
//       If
//--------------

	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (numcomp1((UNS) tmp0, 1) > 0) strmove((UNS) tmp1, (UNS) "1");
	else strmove((UNS) tmp1, (UNS) "0");
	if (sym_(SYMGET, (UNS) "x", (UNS) tmp2, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (numcomp1((UNS) tmp2, 100) > 0) strmove((UNS) tmp3, (UNS) "1");
	else strmove((UNS) tmp3, (UNS) "0");
	if (numcomp1( (UNS) tmp1, 0) || numcomp1( (UNS) tmp3, 0)) strmove( (UNS) tmp4, (UNS) "1"); else strmove( (UNS) tmp4, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp4 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"or true");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

//================================================================================= 
//  If '(x<1) Write "not less",! 
//================================================================================= 

//--------------
//       If
//--------------

	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (numcomp1((UNS) tmp0,1)<0) strmove((UNS) tmp1, (UNS) "1");
	else strmove((UNS) tmp1, (UNS) "0");
	if (numcomp1( (UNS) tmp1, 0) == 0) 
		strmove( (UNS) tmp2, (UNS) "1");
	else strmove( (UNS) tmp2, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp2 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"not less");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

//================================================================================= 
//  Set (g,h)=0 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "g", (UNS) "0", svPtr); // Store result s274 
	sym_(SYMSTORE, (UNS) "h", (UNS) "0", svPtr); // Store result s274 

//================================================================================= 
//  If $Data(g)=0 Write "g undefined",! 
//================================================================================= 

//--------------
//       If
//--------------

	LocalData((unsigned char *) "g",(unsigned char *) tmp0,svPtr);
	if (strcmp((const char *) tmp0,(const char *) "0")==0) 
	strmove( (UNS) tmp1, (UNS) "1");
	else strmove( (UNS) tmp1, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp1 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"g undefined");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

//================================================================================= 
//  Write $Get(undefined,99),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	LocalGet( (unsigned char *) "undefined", (unsigned char *) tmp0, (unsigned char *) "99", svPtr); 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  If x=5 Write "eq",! 
//================================================================================= 

//--------------
//       If
//--------------

	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (strcmp((const char *) tmp0,(const char *) "5")==0) 
	strmove( (UNS) tmp1, (UNS) "1");
	else strmove( (UNS) tmp1, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp1 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"eq");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

//================================================================================= 
//  If x'=6 Write "neq",! 
//================================================================================= 

//--------------
//       If
//--------------

	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	if (strcmp((const char *) tmp0, (const char *) "6") != 0)
		strmove((UNS) tmp1, (UNS) "1");
	else strmove((UNS) tmp1, (UNS) "0");
	svPtr->tpx = 0; //Default $Test indicator
	if (numcomp2((UNS) tmp1 )) { // Test if true 
	svPtr->tpx = 1; // $test becomes true

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"neq");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	} 

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
