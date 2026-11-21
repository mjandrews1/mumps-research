package daf

// Unit 118 (DAF B4 T3, complex): include/pcre.h::<misc> (127 LOC): the
// third-party PCRE 4.5 public header residue (Cambridge copyright
// 1997-2003, the PUBLISHED/generic option defines, the pcre_compile
// prototype family, the struct offsets 'classes', pcre_callout /
// pcre_free / pcre_malloc / pcre_stack_free / pcre_stack_malloc
// function-pointer settings).
// Residue consult: BASE-log row 37 hit-adapt (third-party header
// residue: banner and decl text not reproduced here per the T1/T2
// residue precedent (rows 94/95/107): no Graded code-path body exists
// in this unit, so the render carries only the recorded carrier
// summary below); rows 6/22 hit-adapt (the file-scope pcre_malloc/
// pcre_free function-pointer globals would fold onto an object; no
// body rendered), row 30/18 family (the configuration constants
// PCRE_MAJOR/PCRE_MINOR/PCRE_DATE recorded as consts to pin the
// 4.5-stamped third-party build the interpreter's SQLITE_D reference
// build is compiled against).
// Dafny: EXEMPT (residue-only unit, no rendered body; journal row).

const val PCRE_MAJOR_D = 4  // vendor include/pcre.h PCRE_MAJOR 4
const val PCRE_MINOR_D = 5  // vendor include/pcre.h PCRE_MINOR 5 (a 4.5)

// PCRE_DATE (01-December-2003) and the PUBLISHED/generic extern'C'
// prototype set / pcre_compile-extract prototype family / option
// defines / pcre_callout struct hook / pcre_malloc-pcre_free
// function-pointer externs recorded here as third-party header live
// text, not re-declared: Graded code never exercises these
// declarations directly, and duplicating a third-party interface
// header would diverge from the T1/T2 residue
// precedent (BASE rows 37/94/95/107).
