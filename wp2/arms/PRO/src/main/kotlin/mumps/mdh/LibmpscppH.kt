package mumps.mdh

/**
 * PRO-arm translation of include/mumpsc/libmpscpp.h::<misc>
 * (batch 10 unit, roster row 282, file rank 290). Translated
 * independently from the pristine vendor source (mumps-14-may-
 * 2026.tgz, sha256 621deb25...ffdbe). The study extractor claims
 * no function bodies from a .h, so the misc unit is the header
 * residue (all 441 lines; u57/u67 GlobalClassH precedents).
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): include/mumpsc/libmpscpp.h::
 * <misc>, c, loc 369, comment_ratio 0.016, pointer_marks 53,
 * control_marks 5, score 201.7, batch 10 - exact match to
 * wp1/study-subset/units.csv row 282.
 *
 * Unit scope: the top master include of the mdh library - its
 * banner include block (readline/iostream/gtk/mumpsc/btree.h/
 * global.h/arith.h), the _MDH_ translation-unit block, the
 * GlobalExceptionCode roster, the <dollar>-macro block, the
 * get_global/set_global/BTREE/ltoa/ftoa family macros, the mdh_
 * GTK prototype surface, the library prototypes (SQL, SymGet/
 * SymPut, Piece, xecute, Shred, SYN, StopLookup, sw, bmg_), the
 * class declarations (MumpsGlobalException, indirect, Hash, the
 * mstring/global forward decls), the StopList typedef, struct
 * Syn and SYNMAX, and the interpreter externs. This file is the
 * declaration-side translation: Kotlin has no forward
 * declarations and every data carrier listed below already
 * landed with its owning unit (u67 GlobalClassH precedent - no
 * new stubs are raised, one-owner policy h004/h014).
 *
 * OWNER ROSTER (declaration group -> owning seat):
 *   - include block: the per-header carriers (BtreeH.kt,
 *     GlobalClassH.kt, ArithH.kt, u57/u67 units; gtk prototypes ->
 *     MdhStubs gtk_* stubs, GtkTreeAdd u-batch unit; the
 *     readline/history edge is a libc fold noted at its Joomla
 *     use sites) - h018 preprocessor text, verification only.
 *   - _MDH_ block (the one code-bearing region): the inline
 *     _text_function(int)->NULL and _label_lookup(char*)->-1
 *     bodies with the __label_lookup/__text_function function-
 *     pointer bindings. Carried as a disposition ledger per the
 *     interp-sym.cpp::<misc> precedent (batch 9, roster row
 *     259): the double-underscore vendor names are roster text
 *     only, no Kotlin identifier is emitted, no arm call site
 *     dereferences either pointer (InterpFcnMisc u202/
 *     ZcommandsCode u192/SymMisc u259 precedents; compiler main
 *     emits the C-source text of the pair, Epi.kt u-ledger).
 *   - GlobalExceptionCode values 0-10 (UNSPECIFIED, INVALID_
 *     CONVERSION, GLOBAL_NOT_FOUND, VARIABLE_NOT_FOUND,
 *     DATA_RANGE_ERROR, NUMERIC_RANGE, INPUT_ERROR, PATTERN_
 *     ERROR, INTERPRETER_ERROR, SYMTAB_ERROR, INPUT_LENGTH):
 *     MdhState.globalExceptionCode is the seat (u31); the
 *     value roster is carried as file-private consts at the
 *     throwing sites (MpstimeMain u253 roster note precedent).
 *   - BIGLONG long / LOG2 log2 / FTELL ftello / FSEEK fseeko /
 *     MPSCPP: preprocessor folds (h018),folded at their use
 *     sites (Long/Double folds); no carriers.
 *   - <dollar>-macro block: <dollar>test->__test() (Misc6
 *     __test seat), <dollar>perl->__perl (Perl seats, Misc5),
 *     <dollar>SymGet/<dollar>SymPut (SymGet/SymPut x3 and
 *     __symput, Misc9), <dollar>lock __lock /
 *     <dollar>name __Name / <dollar>pattern / <dollar>query /
 *     <dollar>queryX (__Query seats; MdhStubs docCorrelate-family
 *     precedent) - h018 ledgers, the mumps-side macro names are
 *     C convenience aliases (h015).
 *   - char_global/int_global -> MakeGlobal (GlobalCarrier u14
 *     seat); Xecute(exp) -> xecute(__command__(exp)) and
 *     command(ref) -> __command__ (both owners landed THIS batch:
 *     xecute/WriteMumps1/__command__ in LibmpscppMisc8.kt, row
 *     281); write(...) -> svPtr->tpx=WriteMumps1(...) (row 281).
 *   - BTREE/code/get_global/set_global/GlobalClose macros:
 *     RESOLVED macro seats in MdhStubs (get_global/set_global,
 *     u218); Mglobal is the interp seat (mglobal, u31 family).
 *   - ltoa/ftoa sprintf macros and string(x) STR_MAX: per-use
 *     folds (String.format seats; STR_MAX 32000 strmax.h:3
 *     file-private pattern).
 *   - mdh_* GTK prototype surface: gtk_* stubs in MdhStubs
 *     carry the call sites; mdh_tree_level_add is roster row 298
 *     (verification against MdhTreeLevelAdd.kt this batch);
 *     mdh_dialog_new_with_buttons/mdh_range_get_adjustment/
 *     mdh_calendar_get_date/mdh_toggle_button family and friends are wrapper... are wrapper
 *     bodies outside the corpus - h014 ledger.
 *   - Piece free function: DONE (row 281, LibmpscppMisc8.kt).
 *   - SQL_* family: DONE (LibmpscppMisc7.kt SQL seats).
 *   - Ztell/Zseek/ZSeek/ZTell, Token/TokenInit, Perl x2,
 *     Dump/Restore x3, stem x3, lhash: DONE (LibmpscppMisc5.kt);
 *     chash rides its lhash sibling as a not-corpus wrapper
 *     (h014 ledger).
 *   - SymGet x3 / SymPut x9 / __symput: DONE (LibmpscppMisc9).
 *   - Horolog() mstring form: the mdh seat is row 278
 *     (Horolog.kt, the _horolog body; the mstring-returning
 *     wrapper is a not-corpus inline fold, h014 ledger);
 *     ScanAlnum: DONE (ScanAlnum.kt).
 *   - wstem/Interpret/_piece/_extract/_find/_horolog/pmw/pm/
 *     bmg_ family plus stem(x,int)/per lcpp/LocalName/nodash/Score/prnt/
 *     init/Cost/sw x3: seat rosters as in their owner files
 *     (rows 277-281 land _horolog, bmg wrappers, nodash and
 *     Score this batch; sw cores DONE Sw.kt; pm stub remains).
 *   - Justify global/mstring pointer-ladder prototypes: the
 *     mstring::Justify member landed row 281; the four C vararg
 *     Justify() forms are B4+ owner ladder seats (roster).
 *   - GlobalSet/GlobalGet/GlobalOrder/GlobalData/Transpose/
 *     Multiply families: DONE seats (LibmpscppMisc5 GlobalSet/
 *     GlobalGet; GlobalOrder row 266; GlobalData row 262;
 *     Transpose.kt; Multiply.kt + GlobalMultiply.kt).
 *   - class MumpsGlobalException: DONE carrier (MdhStubs, u31).
 *   - class indirect: DONE (LibmpscppMisc1.kt minimal carrier,
 *     u31 fold); class Hash: DONE (LibmpscppMisc1.kt).
 *   - StopList set<string>: DONE (StopInit.kt stop set, u174);
 *     StopInit/StopLookup/SYN/SynInit/SYNMAX/struct Syn: DONE
 *     (StopInit.kt, SynInit.kt, rows 281 wrappers).
 *   - VariableNotFound/strcatx/strcatz/ErrorMessage/sym_/zfcn/
 *     numcomp/numcomp1/mult1/add1/GlobalQS/_SymFree/Local Data and friends
 *     Local and Global order/data/get/next/qs seats, Merge/getstr1/expx/divi/_justify/fcn/_select:
 *     interp-side seats (InterpSysfuncMisc, CstringOps,
 *     Merge, Escan, Justify, Fcn, InterpStubs; q.v. those
 *     files); sym_/pm remain MdhStubs/InterpStubs stubs.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext          : every file-scope carrier named
 *    above already rides its owning singleton/class seat; this
 *    header clone declares no new Kotlin top-level state.
 *  - h012 unsigned_byte_domain : the (unsigned char*) extern
 *    surfaces map to CharArray byte views at the seats.
 *  - h014 out_of_scope_call    : declaration-only header - the
 *    roster above delegates every body to its owning unit;
 *    wrappers outside the corpus ride their stub ledgers.
 *  - h015 grammar_collision    : the <dollar>-prefixed macro
 *    names have no Kotlin identifier point (roster text, the
 *    DOLLAR-char fold is kept at their use sites); extern "C"
 *    bindings are plain top-level seats.
 *  - h018 preprocessor_conditional: #ifndef MUMPSLIB_H / _MDH_
 *    / __STDC_HOSTED__ arms are translation-unit selectors with
 *    no Kotlin analog (per-file split, h004 convention); MPSCPP
 *    arms nothing in the arm - verification text.
 *
 * Vendor notes (translated faithfully, not \\\"fixed\\\"):
 *  - The <dollar>SymPut alias binds the mstring, char-star and global C
 *    reference overloads that Kotlin models as distinct value
 *    overloads (SymPut seats, u97); the write macro's tpx
 *    side-effect survives at the WriteMumps1 seat shape.
 *  - The default-argument note: sw's int=0 defaults (and
 *    Shred/ScanAlnum min/max defaults) are C default-argument
 *    forms; the Kotlin seats render all arguments explicitly
 *    at call sites (no default params introduced, h011).
 */
