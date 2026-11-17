package mumps.compiler

/**
 * PRO-arm translation of compiler-localarray.c::<misc> (B5
 * tranche C, unit 154, rank 21 - the FINAL B5 batch row).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1-37 - the residual chunk of
 * compiler-localarray.c after the LocalArray() unit (B1 unit
 * 12, LocalArray.kt, vendor 38-92): the GPL banner, the May 12,
 * 2024 date comment, and the four include directives. No
 * executable code; the banner is kept verbatim below and each
 * include maps onto an existing carrier (units 35/153
 * CompilerAbrtxMisc/CompilerForMisc precedent, the same banner
 * family).
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): residual by identity - the
 * file's only extracted function is LocalArray (u12); the
 * residual rows 1-37 carry units.csv row 155 (c, loc 30,
 * comment_ratio 0.062, pointer_marks 0, control_marks 2,
 * score 5.2, rank 21, batch 5).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : every include resolves to an
 *    existing implementation - nothing new stubbed:
 *    stdio.h -> the java.io edges already realized in
 *    LocalArray.kt (u12) and CompilerState.Final/Out;
 *    mumpsc/memsize.h -> size constants carried in
 *    CompilerState (PS, PS1 = PS+1, FBMAX, FORPARM, VERSION,
 *    DDAT/DKEY);
 *    mumpsc/compiler-macros.h -> no macro in this chunk; the
 *    per-unit macro consumers translate their own macros;
 *    mumpsc/compiler-globals-ext.h -> the extern declarations
 *    are carried by the CompilerState object (tpx, t2, pd1len,
 *    xpx, ierr, pd1, xd, v1d, MAIN_flg, FCN_flg, ExitCode,
 *    ExecName, pid, output_file, lines, Final, ...).
 *  - h017 naming             : the banner and date comment are
 *    kept verbatim; the file is named after the vendor source
 *    file (u35/u153 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - No #ifdef/#ifndef occurs in this chunk, so rule h018 is not
 *    triggered here (h018 resolves preprocessor conditionals
 *    against the reference build config; the conditionals inside
 *    memsize.h itself belong to that header's own units).
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

//	May 12, 2024

// vendor: #include <stdio.h> -> java.io edges in LocalArray.kt
//         (u12), CompilerState.Final/Out
// vendor: #include <mumpsc/memsize.h> -> CompilerState size
//         constants (PS/PS1/FBMAX/FORPARM/VERSION/DDAT/DKEY)
// vendor: #include <mumpsc/compiler-macros.h> -> no macro in
//         this chunk; per-unit consumers translate their own
//         macros
// vendor: #include <mumpsc/compiler-globals-ext.h> -> the
//         CompilerState extern carrier
