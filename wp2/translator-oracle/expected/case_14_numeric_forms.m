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
//  ; case_14_numeric_forms — numeric canonicalization and edge cases 
//================================================================================= 

//================================================================================= 
//  Write 0,!,.5,!,007.250,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"0");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		".5");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"7.25");
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Set x="12abc"+0 
//================================================================================= 

//--------------
//       Set
//--------------

	add( (char *) "12abc", "0", (char *) tmp1 ); 
	sym_(SYMSTORE, (UNS) "x", (UNS) tmp1, svPtr); // Store result s274 

//================================================================================= 
//  Write x,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "x", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "x"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write ""+0,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	add( (char *) "", "0", (char *) tmp0 ); 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write $Justify(2/3,0,6),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	divx((char *) "2",(char *) "3",(char *) tmp0);
	_justify( (UNS) tmp1, (UNS) tmp0, (UNS)"0", (UNS)"6");
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write 1>0,!,0>1,! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (numcomp1((UNS) "1", 0) > 0) strmove((UNS) tmp0, (UNS) "1");
	else strmove((UNS) tmp0, (UNS) "0");
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 
	if (numcomp1((UNS) "0", 1) > 0) strmove((UNS) tmp1, (UNS) "1");
	else strmove((UNS) tmp1, (UNS) "0");
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write "a"_""_""_"b",! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (strlen((const char *) "a") +
	strlen((const char *) "") + 1 
	> STR_MAX)
	ErrorMessage("String too long", svPtr->LineNumber);
	strmove( (UNS) tmp0, (UNS) "a");
	strcat( (char *) tmp0, (char *) "");
	if (strlen((const char *) tmp0) +
	strlen((const char *) "") + 1 
	> STR_MAX)
	ErrorMessage("String too long", svPtr->LineNumber);
	strmove( (UNS) tmp1, (UNS) tmp0);
	strcat( (char *) tmp1, (char *) "");
	if (strlen((const char *) tmp1) +
	strlen((const char *) "b") + 1 
	> STR_MAX)
	ErrorMessage("String too long", svPtr->LineNumber);
	strmove( (UNS) tmp2, (UNS) tmp1);
	strcat( (char *) tmp2, (char *) "b");
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp2);
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
