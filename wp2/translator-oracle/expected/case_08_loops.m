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
//  ; case_08_loops — For forms, Quit, $Order over local array 
//================================================================================= 

//================================================================================= 
//  For i=1:1:5 Write i," " 
//================================================================================= 

//--------------
//       For
//--------------

	strcpy(for1_lim, "");
	strmove((UNS) for1_init, (UNS) "1"); // init value m2534
	strmove( (UNS) for1_incr, (UNS) "1"); // inc/dec m2575
	strmove((UNS) for1_lim, (UNS) "5"); // limit value

	if (strchr((char *) for1_incr, '.')) // inc/dec is float
		for1F = atof(for1_incr) < 0.0 ? 1 : 0; // incr or decr?
	else // inc/dec is integer
		for1F = atol(for1_incr) < 0 ? 1 : 0; // incr or decr?

	for ( sym_(SYMSTORE, (UNS) "i", (UNS) for1_init, svPtr); // init 2618

		for1F ? // loop limit test direction
			numcomp((UNS) sym_(11, (UNS) "i", 
				 _dummy, svPtr),
				(UNS) for1_lim) >= 0 // GE limit value?
			: // ternary operator
			numcomp((UNS) sym_(11, (UNS) "i", 
				_dummy,svPtr),
				(UNS) for1_lim) <= 0; // LE limit value

		add( sym_(11, (UNS) "i", _dummy, svPtr), // inc/dec loop var
			for1_incr, (char *) _dummy),
		sym_(SYMSTORE, (UNS) "i", (UNS) _dummy, svPtr) ) // store rslt

		if ((_i = setjmp(for1J)) == 0) goto For1; // else continue

	else if (_i == 100) goto For1Exit; // f273

	goto For1Exit; // bypass block 

For1: // for loop body entry point B

	do { // for loop body
		svPtr->LineNumber = 2; 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "i", (UNS) tmp0, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "i"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp0);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	} while (0); longjmp(for1J, _i); // FOR loop 1 close 

For1Exit: ; // exit for loop 


//================================================================================= 
//  Write ! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  For j=10:-2:0 Write j," " 
//================================================================================= 

//--------------
//       For
//--------------

	strcpy(for2_lim, "");
	strmove((UNS) for2_init, (UNS) "10"); // init value m2534
	mult((char *) "2","-1",(char *) tmp0);
	strmove( (UNS) for2_incr, (UNS) tmp0); // inc/dec m2575
	strmove((UNS) for2_lim, (UNS) "0"); // limit value

	if (strchr((char *) for2_incr, '.')) // inc/dec is float
		for2F = atof(for2_incr) < 0.0 ? 1 : 0; // incr or decr?
	else // inc/dec is integer
		for2F = atol(for2_incr) < 0 ? 1 : 0; // incr or decr?

	for ( sym_(SYMSTORE, (UNS) "j", (UNS) for2_init, svPtr); // init 2618

		for2F ? // loop limit test direction
			numcomp((UNS) sym_(11, (UNS) "j", 
				 _dummy, svPtr),
				(UNS) for2_lim) >= 0 // GE limit value?
			: // ternary operator
			numcomp((UNS) sym_(11, (UNS) "j", 
				_dummy,svPtr),
				(UNS) for2_lim) <= 0; // LE limit value

		add( sym_(11, (UNS) "j", _dummy, svPtr), // inc/dec loop var
			for2_incr, (char *) _dummy),
		sym_(SYMSTORE, (UNS) "j", (UNS) _dummy, svPtr) ) // store rslt

		if ((_i = setjmp(for2J)) == 0) goto For2; // else continue

	else if (_i == 100) goto For2Exit; // f273

	goto For2Exit; // bypass block 

For2: // for loop body entry point B

	do { // for loop body
		svPtr->LineNumber = 4; 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "j", (UNS) tmp1, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "j"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	} while (0); longjmp(for2J, _i); // FOR loop 1 close 

For2Exit: ; // exit for loop 


//================================================================================= 
//  Write ! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Set k=0 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "k", (UNS) "0", svPtr); // Store result s274 

//================================================================================= 
//  For  Set k=k+1 Quit:k>4  Write k," " 
//================================================================================= 

//--------------
//       For
//--------------

	while(1) {  /* Argumentless FOR */

//--------------
//       Set
//--------------

	if (sym_(SYMGET, (UNS) "k", (UNS) tmp1, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "k"); // not found
	add( (char *) tmp1, "1", (char *) tmp2 ); 
	sym_(SYMSTORE, (UNS) "k", (UNS) tmp2, svPtr); // Store result s274 
	if (sym_(SYMGET, (UNS) "k", (UNS) tmp3, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "k"); // not found
	if (numcomp1((UNS) tmp3, 4) > 0) strmove((UNS) tmp4, (UNS) "1");
	else strmove((UNS) tmp4, (UNS) "0");
	if (atoi( CSTR tmp4)) { // m859 postconditional

//--------------
//       Quit
//--------------

	break; } // post conditional quit m2386 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "k", (UNS) tmp5, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "k"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp5);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	} 

//================================================================================= 
//  Write ! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Kill l 
//================================================================================= 

//--------------
//       Kill
//--------------

	sym_(2, (UNS) "l", (UNS) "",svPtr); //...* kill specific

//================================================================================= 
//  Set l(1)="a",l(3)="c",l(7)="g" 
//================================================================================= 

//--------------
//       Set
//--------------

	{	char tmp[STR_MAX];  // expand lhs array reference
		int i;
		strmove((UNS) tmp, (UNS) "l"); // array name
		i=strlen(tmp);
		tmp[i]=206; // coded open paren
		tmp[i+1]=0;
	strcat((char *) tmp,(char *) "1"); // index
		i=strlen(tmp);
		tmp[i]=207; // coded comma or close paren
		tmp[i+1]=0;
	strmove((UNS) atmp, (UNS) tmp); // preserve expanded array reference
		}
	sym_(SYMSTORE, (UNS) atmp, (UNS) "a", svPtr); // Store result s270 
	{	char tmp[STR_MAX];  // expand lhs array reference
		int i;
		strmove((UNS) tmp, (UNS) "l"); // array name
		i=strlen(tmp);
		tmp[i]=206; // coded open paren
		tmp[i+1]=0;
	strcat((char *) tmp,(char *) "3"); // index
		i=strlen(tmp);
		tmp[i]=207; // coded comma or close paren
		tmp[i+1]=0;
	strmove((UNS) atmp, (UNS) tmp); // preserve expanded array reference
		}
	sym_(SYMSTORE, (UNS) atmp, (UNS) "c", svPtr); // Store result s270 
	{	char tmp[STR_MAX];  // expand lhs array reference
		int i;
		strmove((UNS) tmp, (UNS) "l"); // array name
		i=strlen(tmp);
		tmp[i]=206; // coded open paren
		tmp[i+1]=0;
	strcat((char *) tmp,(char *) "7"); // index
		i=strlen(tmp);
		tmp[i]=207; // coded comma or close paren
		tmp[i+1]=0;
	strmove((UNS) atmp, (UNS) tmp); // preserve expanded array reference
		}
	sym_(SYMSTORE, (UNS) atmp, (UNS) "g", svPtr); // Store result s270 

//================================================================================= 
//  Set sub="" 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "sub", (UNS) "", svPtr); // Store result s274 

//================================================================================= 
//  For  Set sub=$Order(l(sub)) Quit:sub=""  Write sub,"=",l(sub)," " 
//================================================================================= 

//--------------
//       For
//--------------

	while(1) {  /* Argumentless FOR */

//--------------
//       Set
//--------------

{ // local array setup
	int i;
	strmove((UNS) tmp2, (UNS) "l");
	i=strlen((const char *) tmp2);
	tmp2[i]=206;
	tmp2[i+1]=0;
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp1, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	strcat((char *) tmp2,(char *) tmp1);
	i=strlen((const char *) tmp2);
	tmp2[i]=207;
	tmp2[i+1]=0;
	}
	LocalOrder((unsigned char *) tmp2, (unsigned char *) tmp3, (unsigned char *) NULL, svPtr); 
	sym_(SYMSTORE, (UNS) "sub", (UNS) tmp3, svPtr); // Store result s274 
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp4, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	if (strcmp((const char *) tmp4,(const char *) "")==0) 
	strmove( (UNS) tmp5, (UNS) "1");
	else strmove( (UNS) tmp5, (UNS) "0");
	if (atoi( CSTR tmp5)) { // m859 postconditional

//--------------
//       Quit
//--------------

	break; } // post conditional quit m2386 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp6, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp6);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		"=");
{ // local array setup
	int i;
	strmove((UNS) tmp8, (UNS) "l");
	i=strlen((const char *) tmp8);
	tmp8[i]=206;
	tmp8[i+1]=0;
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp7, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	strcat((char *) tmp8,(char *) tmp7);
	i=strlen((const char *) tmp8);
	tmp8[i]=207;
	tmp8[i+1]=0;
	}
	if (sym_(SYMGET, (UNS) tmp8, (UNS) tmp9, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) tmp8); // not found
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp9);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	} 

//================================================================================= 
//  Write ! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
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
