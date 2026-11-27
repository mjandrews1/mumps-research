package both

// include/mumpsc/libmpscpp.h::<misc> - roster 305, batch B10,
// both_seq 182, c, complex stratum, loc 369, score 201.7. The
// libmpscpp.h banner/include/macro/prototype residue: everything
// in the header that function extraction could not claim (the
// header has no function bodies past the small _MDH_ helpers and
// the class definitions, so the whole guard-to-endif span seats
// here as the layout-owner ledger).
//
// Consults: h019 hit (class_member_carrier: the MumpsGlobalException,
// indirect, Hash and Syn class layouts and the StopList typedef
// belong to this header; their members render through their own
// body-owner units - the seats stay package-level); h016 reuse
// (the macro table below keeps vendor names and fold targets);
// h009 reuse (KDoc-only residue carrier, the MstringClassMisc.kt
// and ErrorsMisc.kt precedent); h017 reuse (no Kotlin names are
// introduced beyond the two unseated exception constants below;
// every other declaration either already has an arm seat or is
// ledgered to its defining TU).
//
// Header ledger (include/mumpsc/libmpscpp.h, banner dated April
// 29, 2026; GNU GPL notice, Kevin C. O'Kane):
// - include guard MUMPSLIB_H, define MPSCPP.
// - system includes: readline/history, iostream, math, stdlib,
//   stdarg, string, cctype, set, algorithm, assert, stdexcept,
//   fstream, gtk/gtk.h, gtk/gtkx.h; project includes mumpsc/
//   btree.h, global.h, arith.h.
// - _MDH_ block: char* _text_function(int) returns 0; int
//   _label_lookup(char*) returns -1; the function pointers
//   __label_lookup / __text_function initialized to them.
// - using namespace std; extern int GlobalExceptionCode (the
//   BothCRuntime.kt:35 seat).
// - FTELL/FSEEK fold to ftello/fseeko (the largefile ftell/seek
//   pair; no arm seat, POSIX macro).
//
// Exception code table (GlobalExceptionCode values; the header
// is the LAYOUT OWNER of the whole table - seats noted):
//   UNSPECIFIED 0          seated MpstimeMain.kt:92
//   INVALID_CONVERSION 1   seated BothCRuntime.kt:31
//   GLOBAL_NOT_FOUND 2     seated BothCRuntime.kt:30
//   VARIABLE_NOT_FOUND 3   NO prior seat - seated below
//   DATA_RANGE_ERROR 4     seated MpstimeMain.kt:93
//   NUMERIC_RANGE 5        seated MpstimeMain.kt:94
//   INPUT_ERROR 6          seated MpstimeMain.kt:95
//   PATTERN_ERROR 7        NO prior seat - seated below
//   INTERPRETER_ERROR 8   seated MpstimeMain.kt:96
//   SYMTAB_ERROR 9         seated MpstimeMain.kt:97
//   INPUT_LENGTH 10        seated BothCRuntime.kt:283
//
// BIGLONG folds to long; LOG2 folds to log2 (the comment notes
// older compilers without log2 use log; the vendor keeps log2).
//
// Macro ledger (folds and seats):
// - $test -> __test() (testZb seat, LibmpscppMisc6.kt:148);
//   $perl -> __perl; $SymGet -> __symget; $SymPut -> __symput;
//   $lock -> __lock; $name -> ref.Name(); $pattern -> __pattern;
//   $query -> __Query(str,0,svPtr); $queryX -> __Query(str,pat,
//   svPtr); the __query/__queryX bodies below the SymPut block
//   fold the same way; __name(ref) -> ref.Name().
// - char_global(name,...) / int_global(name,...) ->
//   name.MakeGlobal(0/1,#name,#__VA_ARGS__,__VA_ARGS__).
// - Xecute(exp) -> xecute(__command__(#exp)).
// - write(...) -> svPtr->tpx=WriteMumps1(svPtr,#__VA_ARGS__) (the
//   hosted arm; the !__STDC_HOSTED__ arm write(x) folds the same
//   with a single argument - both arms kept, h018 config note:
//   hosted is the reference config).
// - BTREE(code,key,data) -> Mglobal(code,key,data,svPtr).
// - get_global(ref,rslt) -> (svPtr->tpx=Mglobal(RETRIEVE,ref,
//   rslt,svPtr),rslt) - the getGlobalC seat (Dice.kt:62).
// - set_global(ref,rslt) -> the STORE twin (svPtr->tpx fold,
//   LibmpscppMisc6.kt:183 idiom).
// - GlobalClose -> Mglobal(CLOSE,"","",svPtr).
// - ltoa(out,in) -> sprintf(out, percent-ld, in) - the Mltoa
//   seat (unit 295, BothCRuntime); ftoa(out,in) -> sprintf(out,
//   percent-f, in) - no arm seat, ledger.
// - string(x) -> char x[STR_MAX] (the STR_MAX buffer idiom).
// - command(ref) -> __command__(#ref).
// - SYNMAX 20000 seated at BothStubs.kt:140 (the SynInit syn[]
//   table cap).
//
// Class ledgers:
// - MumpsGlobalException: ctor sets message "Global reference
//   not found"; char* what() returns it. Layout owner = THIS
//   unit; the arm seat is the BothCRuntime.kt:41 class (message
//   member ledgered there; release note: the B-era seat now
//   carries the owner pin roster 305).
// - class indirect: friend indirect operator*(string); indirect
//   operator=(string); string reference; private string
//   xreference. No member seats yet (layout owner here).
// - class Hash: Hash(string="hash",long=100000,string="new");
//   ~Hash(); string operator=(string); mstring operator=
//   (mstring); Hash& operator()(string); Hash& operator()
//   (mstring); operator const char*(); private string key,
//   data; size_t size; FILE* hashkey; FILE* hashdata; string
//   ref. Layout owner = THIS unit; the operator const char*
//   member body is roster 296 (LibmpscppLine1149.kt seat, this
//   batch); the remaining members (operator= pair, operator()
//   pair, ctor/dtor) have no seats yet.
// - typedef set<string, less<string>> StopList; struct Syn
//   { char* word; char* alias; }; - layout owner here, no seats
//   (the SYN family bodies live with their defining TU).
//
// Prototype ledger (every declaration below the class block;
// owner = the defining TU unit, seats where the arm carries
// them - no new Kotlin is emitted for these, per the residue
// carrier precedent):
// - embed1(const char*) - NOTE: distinct from interp-sql.cpp::
//   embed (roster 51, seated B9); embed1 has no body seat yet.
// - mdh GTK wrapper family: mdh_tree_level_add (GtkTreeStore*,
//   int, col1) plus the 2/3/4/5-column overloads - the overload
//   family whose 9-column mislabel the B10 sweep corrected
//   (unit 290 seat, MdhTreeLevelAdd.kt: the six-column face);
//   mdh_dialog_new_with_buttons; mdh_range_get_adjustment;
//   mdh_calendar_get_date; mdh_toggle_button_get_active;
//   mdh_color_chooser_get_rgba; mdh_font_chooser_get_font;
//   mdh_switch_get_active; mdh_toggle_button_set_active;
//   mdh_entry_get_text; mdh_entry_set_text;
//   mdh_text_buffer_set_text; mdh_label_set_text;
//   mdh_tree_selection_get_selected; mdh_tree_store_clear;
//   mdh_spin_button_get_value; mdh_spin_button_set_value;
//   mdh_widget_hide; mdh_widget_show (all GTK-mediated, no arm
//   seats; the GTK carrier question is ledgered to the mdh
//   residue, non-roster).
// - SQL family: SQL_Disconnect; SQL_Connect(char*/string/
//   mstring); SQL_Format(mstring/string/char*/()); SQL_Command
//   (mstring/string/char*); SQL_Table((); (mstring,int=10);
//   (string,int=10); (char*,int=10)); SQL_Message; SQL_Open;
//   SQL_Native. No seats (SQL TU residue, non-roster).
// - Ztell/Zseek (FILE*, mstring&) and (FILE*, global&); the
//   ZSeek/ZTell re-declarations repeat the pair verbatim (the
//   case-duplicated prototypes kept in the ledger). No seats.
// - SymGet(mstring/char*/global); SymPut nine overloads;
//   TokenInit(mstring&); Token(); __symput(const char*, const
//   char*). No seats.
// - Perl(mstring&,mstring&); Perl(mstring&,char*); perl_pm(
//   const char*,const char*,const int,struct MSV*). No seats.
// - wstem(char*,int) - seated (unit 294, BothStubs flip this
//   batch); GlobalQuery(unsigned char*,unsigned char*,
//   unsigned char*,struct MSV*); Interpret(const char*, struct
//   MSV*) - the Interpret seat (Interpret.kt); _piece(20-arg
//   face) - the _pieceZb seat (roster 114 pin, Piece.kt);
//   cleanLocks/makeLock; CleanLocks/CleanAllLocks.
// - _extract; _find; _horolog(unsigned char*) - seated (unit
//   247, Horolog.kt); _justify; fcn(MSV*); _select (the
//   20-arg face, unit 303 seat InterpSelectMisc.kt).
// - bmg family: extern C bmg_setup/bmg_search/bmg_fsearch; the
//   bmg_fullsearch(char*,char*) native face plus the string/
//   mstring/global overloads - seated (unit 302,
//   InterpBmgsubsMisc.kt; the BifsZfcn bmgFullsearchZb delegate
//   flip, roster 302 pin corrected this batch).
// - pmw four overloads; pm(const unsigned char*, const
//   unsigned char*, struct MSV*). No seats.
// - stem(char*,int); stem(string&); stem(mstring&); Stem(
//   mstring&); Dump(mstring&/string/char*); Restore(mstring&/
//   string/char*) - no seats.
// - nodash(char*); Score(char,char) - the Score carrier ledger
//   (DAF notes a Score carrier in the misc-8 walk; BOTH renders
//   independently); prnt(int,char*,char*,int,int,int); init();
//   Cost(char*,char*,int,int).
// - sw(char*,char*,int=0,int=0,int=-1,int=-1,int=-2) plus the
//   mstring and string overloads - seated (roster 61 pin,
//   BothStubs).
// - chash(const char*); lhash(const char*) -> unsigned long;
//   __Query(char*,char,struct MSV*); KiLL(char*,struct MSV*) -
//   seated (roster 37 pin); RunMumps(struct MSV*, const char*,
//   char*, ...); xecute(char*/string/mstring); __test() - the
//   testZb seat; Shred(char*,int)/Shred(mstring,int)/Shred(
//   string,int); ShredQuery same trio - no seats yet (the
//   misc-8 walk releases bodies, roster 306 this batch).
// - Justify four overloads (const char*/global/string/mstring,
//   struct MSV*, char*, ...) - seated (unit 278, GlobalJustify
//   this batch); WriteMumps1(struct MSV*, char*) - the write
//   macro target, WriteCode seat; HitRatio(void) - seated
//   (LibmpscppMisc6.kt:108); EncodeHTML(char*)/EncodeHTML(
//   mstring&) - seated (roster 5 pin); __command__(char*).
// - mcvt(char*/long/float/double/int) - no seats (the mcvt
//   family); cvt(long/float/double/int) - seated (the cvt
//   overloads, LibmpscppMisc6.kt:85-88).
// - GlobalSet seven overloads; GlobalGet(char*)/GlobalGet(
//   string)/GlobalGet(mstring); GlobalOrder(char*,int)/
//   (string,int)/(mstring,int); GlobalData(char*/string/
//   mstring) - no seats (the globalClass family, roster 89
//   layout owner pending).
// - Transpose(char*,char*)/(string,string)/(mstring,mstring);
//   Multiply(string,string,string) - no seats.
// - Qlength(mstring&)/Qlength(char*); Query(mstring&)/Query(
//   const char*); Qsubscript(mstring&,mstring&)/(mstring&,int)/
//   (const char*,int) - the Qlength/Qsub prose pins were fixed
//   to roster 93 in the B9 sweep; seats per the C-015 files.
// - VariableNotFound(long, char*); strcatx(unsigned char*,
//   unsigned char*) inline; strcatz(unsigned char*, unsigned
//   char*); ErrorMessage(const char*, long); sym_(int, unsigned
//   char*, unsigned char*, struct MSV*) - the sym_ PROTO sits
//   here (layout owner this unit); the BODY is roster 307,
//   rendered this batch (InterpSym.kt); zfcn(unsigned char*,
//   unsigned char*, struct MSV*) - the zfcn seat (RpmMisc walk);
//   numcomp(unsigned char*, unsigned char*) and numcomp1(
//   unsigned char*, long) - the numcomp residue (non-roster,
//   the roster-67 renumber family); mult1(const char*, long,
//   char*); add1(char*, long, char*) - NOTE the header declares
//   the long middle parameter while the mstring operator call
//   sites pass int (the LibmpscppMisc4.kt stub carries the
//   int form, h014); the commented-out GlobalOrder prototype
//   (the // void GlobalOrder(...) line) kept as residue.
// - GlobalQS; _SymFree(int, struct MSV*); LocalData; LocalOrder;
//   LocalQS; LocalNext; LocalGet; GlobalNext; GlobalGet;
//   GlobalData (the unsigned-char/MSV faces); Merge(unsigned
//   char*, unsigned char*, struct MSV*) - seated (roster 237
//   pin); getstr1(FILE*, unsigned char[]); expx(const char*,
//   const char*, char*); divi(const char*, const char*, char*).
// - Horolog() - the mstring face (roster 247 seat is the
//   unsigned char* _horolog; this no-arg mstring face has no
//   seat yet); ScanAlnum(FILE*, int min=3, int max=25) and
//   ScanAlnum(istream, int min=3, int max=25) - the roster 77
//   coincidence was verified correct in the B9 sweep (kept).
//
// Render: KDoc-only residue carrier (no runtime code beyond the
// two unseated exception constants below; every other seat is
// named above with its file:line pin).

// The two GlobalExceptionCode values with no prior arm seat (the
// other nine are seated, see the table in the header KDoc).
internal const val VARIABLE_NOT_FOUND = 3 // libmpscpp.h:72
internal const val PATTERN_ERROR = 7 // libmpscpp.h:76
