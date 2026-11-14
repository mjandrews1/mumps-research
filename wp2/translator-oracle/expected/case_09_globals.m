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
//  ; case_09_globals — ^globals, $Order, $Query, $Data, Kill, Merge 
//================================================================================= 

//================================================================================= 
//  Kill ^orc 
//================================================================================= 

//--------------
//       Kill
//--------------

	strmove( (UNS) gtmp, (UNS) "");
	strcat((char *) gtmp,(char *) "^orc"); 
	{ 
	unsigned char tmp14[STR_MAX], tmp15[STR_MAX]; 
	keyfix(gtmp);
	_k = strlen((const char *) gtmp); 
	strmove((UNS) tmp14, (UNS) gtmp); 
	Mglobal(GKILL, gtmp, tmp15, svPtr); 
	while(1) { 
		_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); 
		if (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { 
			Mglobal(GKILL, gtmp, tmp15, svPtr); 
			continue; 
			} 
		break; 
		} 
	}

//================================================================================= 
//  Set ^orc("x")=1,^orc("y")=2,^orc("z")=3 
//================================================================================= 

//--------------
//       Set
//--------------

	strmove((UNS) tmp0, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp0, (char *) "x"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956

	{ 
	f = Mglobal(STORE, tmp0, "1", svPtr); 
	} 

	strmove((UNS) tmp1, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp1, (char *) "y"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956

	{ 
	f = Mglobal(STORE, tmp1, "2", svPtr); 
	} 

	strmove((UNS) tmp2, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp2, (char *) "z"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956

	{ 
	f = Mglobal(STORE, tmp2, "3", svPtr); 
	} 


//================================================================================= 
//  Set ^orc("sub","a")=10,^orc("sub","b")=20 
//================================================================================= 

//--------------
//       Set
//--------------

	strmove((UNS) tmp0, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp0, (char *) "sub"); // b
	strcat((char *) catx, (char *) "\xd0"); //a3956
	catx = strxcat((char *) tmp0, (char *) "a"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956

	{ 
	f = Mglobal(STORE, tmp0, "10", svPtr); 
	} 

	strmove((UNS) tmp1, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp1, (char *) "sub"); // b
	strcat((char *) catx, (char *) "\xd0"); //a3956
	catx = strxcat((char *) tmp1, (char *) "b"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956

	{ 
	f = Mglobal(STORE, tmp1, "20", svPtr); 
	} 


//================================================================================= 
//  Set sub="" 
//================================================================================= 

//--------------
//       Set
//--------------

	sym_(SYMSTORE, (UNS) "sub", (UNS) "", svPtr); // Store result s274 

//================================================================================= 
//  For  Set sub=$Order(^orc(sub)) Quit:sub=""  Write sub,"->",^orc(sub)," " 
//================================================================================= 

//--------------
//       For
//--------------

	while(1) {  /* Argumentless FOR */

//--------------
//       Set
//--------------

	strmove((UNS) tmp2, (UNS) "^orc\x01"); // init p674
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp1, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	strcatx((UNS) tmp2, (UNS) tmp1);
	GlobalOrder((unsigned char *) tmp2, (unsigned char *) tmp3, 
		(unsigned char *) NULL, svPtr); // p2236
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
		"->");
	strmove((UNS) tmp8, (UNS) "^orc\x01"); // init p674
	if (sym_(SYMGET, (UNS) "sub", (UNS) tmp7, svPtr) == NULL) // get value 
		VariableNotFound1(svPtr->LineNumber, (UNS) "sub"); // not found
	strcatx((UNS) tmp8, (UNS) tmp7);
	if (!Mglobal(RETRIEVE, tmp8, tmp9, svPtr)) // retrieve global
		VariableNotFound(svPtr->LineNumber, tmp8);
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
//  Write $Data(^orc("x"))," ",$Data(^orc("sub")),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove((UNS) tmp0, (UNS) "^orc\x01"); // init p674
	strcatx((UNS) tmp0, (UNS) "x");
	GlobalData(tmp0,tmp1,svPtr);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	strmove((UNS) tmp2, (UNS) "^orc\x01"); // init p674
	strcatx((UNS) tmp2, (UNS) "sub");
	GlobalData(tmp2,tmp3,svPtr);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp3);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Write $Query(^orc(""))," ",$Query(^orc("x")),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove((UNS) tmp0, (UNS) "^orc\x01"); // init p674
	strcatx((UNS) tmp0, (UNS) "");
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$query\xce");
	if (!Mglobal(RETRIEVE, tmp0, tmp1, svPtr)) // retrieve global
		VariableNotFound(svPtr->LineNumber, tmp0);
	strxcat((char *) catx ,(char *) tmp1);
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp2, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp2);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		" ");
	strmove((UNS) tmp3, (UNS) "^orc\x01"); // init p674
	strcatx((UNS) tmp3, (UNS) "x");
	strmove( (UNS) &gtmp[1], (UNS) "");
	catx = strxcat((char *) &gtmp[1],"$query\xce");
	if (!Mglobal(RETRIEVE, tmp3, tmp4, svPtr)) // retrieve global
		VariableNotFound(svPtr->LineNumber, tmp3);
	strxcat((char *) catx ,(char *) tmp4);
	strxcat((char *) catx,"\xcf");
	strmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);
	fcn(svPtr);
	strmove((UNS) tmp5, (UNS) &svPtr->bd[1]);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp5);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Kill ^orc("y") 
//================================================================================= 

//--------------
//       Kill
//--------------

	strmove( (UNS) gtmp, (UNS) "");
	strcat((char *) gtmp,(char *) "^orc"); 
	strcatx((char *) gtmp,(char *) "y");
	{ 
	unsigned char tmp14[STR_MAX], tmp15[STR_MAX]; 
	keyfix(gtmp);
	_k = strlen((const char *) gtmp); 
	strmove((UNS) tmp14, (UNS) gtmp); 
	Mglobal(GKILL, gtmp, tmp15, svPtr); 
	while(1) { 
		_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); 
		if (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { 
			Mglobal(GKILL, gtmp, tmp15, svPtr); 
			continue; 
			} 
		break; 
		} 
	}

//================================================================================= 
//  Write $Get(^orc("y"),"gone"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove((UNS) tmp0, (UNS) "^orc\x01"); // init p674
	strcatx((UNS) tmp0, (UNS) "y");
	GlobalGet( (unsigned char *) tmp0, (unsigned char *) tmp1, (unsigned char *) "gone", svPtr); 
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Merge ^orc2("copy")=^orc("sub") 
//================================================================================= 

//--------------
//       Merge
//--------------


//--------------
//       Set
//--------------

	strmove((UNS) tmp0, (UNS) "orc2\xce"); // M3701
	catx = strxcat((char *) tmp0, (char *) "copy"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956
	strcpy((char *) tmp1, (const char *) atmp); /* hold result */
	strmove((UNS) tmp2, (UNS) "orc\xce"); // M3701
	catx = strxcat((char *) tmp2, (char *) "sub"); // b
	strcat((char *) catx, (char *) "\xcf"); //a3956
	f=Merge(tmp0, tmp2, svPtr); 
	if (f != 0) ErrorMessage("Merge failed ", svPtr->LineNumber); 

//================================================================================= 
//  Write ^orc2("copy","b"),! 
//================================================================================= 

//--------------
//       Write
//--------------

	if (svPtr->out_file[svPtr->io]==NULL) 
		ErrorMessage("Write to input file", svPtr->LineNumber); // m1944 
	strmove((UNS) tmp0, (UNS) "^orc2\x01"); // init p674
	strcatx((UNS) tmp0, (UNS) "copy");
	strcatx((UNS) tmp0, (UNS) "b");
	if (!Mglobal(RETRIEVE, tmp0, tmp1, svPtr)) // retrieve global
		VariableNotFound(svPtr->LineNumber, tmp0);
	svPtr->hor[svPtr->io] += fprintf(svPtr->out_file[svPtr->io], "%s", 
		tmp1);
	fprintf(svPtr->out_file[svPtr->io], "\n"); 
	svPtr->hor[svPtr->io]=0; svPtr->ver[svPtr->io]++; 

//================================================================================= 
//  Kill ^orc,^orc2 
//================================================================================= 

//--------------
//       Kill
//--------------

	strmove( (UNS) gtmp, (UNS) "");
	strcat((char *) gtmp,(char *) "^orc"); 
	{ 
	unsigned char tmp14[STR_MAX], tmp15[STR_MAX]; 
	keyfix(gtmp);
	_k = strlen((const char *) gtmp); 
	strmove((UNS) tmp14, (UNS) gtmp); 
	Mglobal(GKILL, gtmp, tmp15, svPtr); 
	while(1) { 
		_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); 
		if (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { 
			Mglobal(GKILL, gtmp, tmp15, svPtr); 
			continue; 
			} 
		break; 
		} 
	}
	strmove( (UNS) gtmp, (UNS) "");
	strcat((char *) gtmp,(char *) "^orc2"); 
	{ 
	unsigned char tmp14[STR_MAX], tmp15[STR_MAX]; 
	keyfix(gtmp);
	_k = strlen((const char *) gtmp); 
	strmove((UNS) tmp14, (UNS) gtmp); 
	Mglobal(GKILL, gtmp, tmp15, svPtr); 
	while(1) { 
		_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); 
		if (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { 
			Mglobal(GKILL, gtmp, tmp15, svPtr); 
			continue; 
			} 
		break; 
		} 
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
