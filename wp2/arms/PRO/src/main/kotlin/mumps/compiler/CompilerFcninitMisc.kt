package mumps.compiler

/**
 * PRO-arm translation of compiler-fcninit.c::<misc> (B6 tranche
 * C, unit 186, rank 16). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1-39 - the residual
 * chunk of compiler-fcninit.c after the fcninit() unit (batch-7
 * roster row 195, complex, NOT yet rendered): the GPL banner, the
 * May 15, 2024 date comment, and the four include directives. No
 * executable code; the banner is kept verbatim below and each
 * include maps onto an existing carrier (unit-153 CompilerForMisc
 * precedent, the same banner family).
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): residual by identity - the
 * file's only extracted function is fcninit (start 40, 53 tool
 * lines; the units.csv row 195 boundary is 41 loc, complex rank
 * 230, batch 7); the residual rows carry units.csv row 186 (c,
 * loc 30, comment_ratio 0.062, pointer_marks 0, control_marks 2,
 * score 5.2, rank 16, batch 6) - exact match.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : every include resolves to an
 *    existing implementation - nothing new stubbed:
 *    stdio.h -> the java.io edges realized in the compiler
 *    writers (Out/Sysout carriers; fcninit's own consumers land
 *    with batch-7 row 195);
 *    mumpsc/memsize.h -> the MemsizeH.kt carrier landed this
 *    batch (u177): PS/PS1/STR_MAX-class size constants and the
 *    Linux platform arm;
 *    mumpsc/compiler-macros.h -> no macro in this chunk; the
 *    per-unit macro consumers translate their own macros;
 *    mumpsc/compiler-globals-ext.h -> the extern declarations
 *    are carried by the CompilerState object (FunctionName,
 *    Out, pid, output_file, lines, ...).
 *  - h017 naming             : the banner and date comment are
 *    kept verbatim; the file is named after the vendor source
 *    file (u35/u153 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - No #ifdef/#ifndef occurs in this chunk, so rule h018 is not
 *    triggered here (h018 resolves preprocessor conditionals
 *    against the reference build config; the conditionals inside
 *    memsize.h itself belong to that header's own unit 177).
 *  - The banner carries the vendor's full GPL notice; the
 *    vendor's tab-indented date comment appears below as
 *    written.
 */

/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
+
+     Mumps Bioinformatics Software Library
+     Copyright (C) 2003, 2004, 2008, 2015, 2017,
+     2022, 2024  by Kevin C. O'Kane
+
+     Kevin C. O'Kane
+     kc.okane@gmail.com
+     okane@uni.edu
+     https://threadsafebooks.com/
+     https://www.cs.uni.edu/~okane
+
+ This program is free software; you can redistribute it and/or modify
+ it under the terms of the GNU General Public License as published by
+ the Free Software Foundation; either version 2 of the License, or
+ (at your option) any later version.
+
+ This program is distributed in the hope that it will be useful,
+ but WITHOUT ANY WARRANTY; without even the implied warranty of
+ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
+ GNU General Public License for more details.
+
+ You should have received a copy of the GNU General Public License
+ along with this program; if not, write to the Free Software
+ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
+
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

//	May 15, 2024

// #include <stdio.h> -> java.io edges in the compiler writers (h014).
// #include <mumpsc/memsize.h> -> MemsizeH.kt carrier (u177).
// #include <mumpsc/compiler-macros.h> -> per-unit macro consumers (h014).
// #include <mumpsc/compiler-globals-ext.h> -> CompilerState externs (h014).

// Owner roster: fcninit() itself is batch-7 roster row 195
// (complex, rank 230) - not rendered here (h013 family roster
// note; lands with batch 7).
