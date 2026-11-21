package daf

// Unit 102 (DAF B4 T1): rdbms-if.c residue (<misc>, 32 physical LOC):
// the SQL/PostgreSQL preprocessor lattice plus includes and the one
// extern (ierr). BASE-log rows 39/78 consulted (hit-adapt): the
// compile-time config lattice resolves to the REFERENCE set (SQL
// undefined -> false; ALLOW_POSTGRES=0 disables POSTGRES even where
// HAVE_PGSQL_H names an include dir), with the dead branches carried
// as documented consts, never as live code. extern int ierr is the
// file-scope singleton state (BASE-log rows 6/22 globals->singleton,
// hit-reuse; the rdbms runtime readers bind it when their units
// render).
// Dafny: EXEMPT (config/extern residue; journal row).

object RdbmsConfigD {
    const val SQL = false            // #ifdef SQL: undefined in the reference build
    const val ALLOW_POSTGRES = 0     // #if ALLOW_POSTGRES==0 -> POSTGRES undef
    const val POSTGRES = false       // disabled by ALLOW_POSTGRES==0
    const val HAVE_PGSQL_H_IS_NO = true // HAVE_PGSQL_H==NO in the reference config
}

object RdbmsRuntimeD {
    var ierr = 0 // extern int ierr (the only file-scope global)
}
