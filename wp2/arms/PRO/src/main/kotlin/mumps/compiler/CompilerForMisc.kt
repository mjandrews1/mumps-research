package mumps.compiler

/**
 * PRO-arm translation of compiler-for.c::<misc> (B5 tranche C,
 * unit 153, rank 17). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1-38 - the residual
 * chunk of compiler-for.c after the for_code() unit (B4 unit 95,
 * ForCode.kt, vendor 39-399): the GPL banner, the June 16, 2024
 * date comment, and the four include directives. No executable
 * code; the banner is kept verbatim below and each include maps
 * onto an existing carrier (unit-35 CompilerAbrtxMisc precedent,
 * the same banner family).
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): residual by identity - the
 * file's only extracted function is for_code (start 39, 361
 * lines, u95); the residual rows 1-38 carry units.csv row 154
 * (c, loc 30, comment_ratio 0.062, pointer_marks 0,
 * control_marks 2, score 5.2, rank 17, batch 5).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call  : every include resolves to an
 *    existing implementation - nothing new stubbed:
 *    stdio.h -> the java.io edges already realized in ForCode.kt
 *    (u95) and CompilerState.Final/Out;
 *    mumpsc/memsize.h -> size constants carried in CompilerState
 *    (PS, PS1 = PS+1, FBMAX, FORPARM, VERSION, DDAT/DKEY - the
 *    u95 FOR_limit/forxmax owners live on CompilerState :110/
 *    :111);
 *    mumpsc/compiler-macros.h -> no macro in this chunk; the
 *    per-unit macro consumers translate their own macros;
 *    mumpsc/compiler-globals-ext.h -> the extern declarations
 *    are carried by the CompilerState object (tpx, t2, pd1len,
 *    xpx, ierr, pd1, xd, v1d, MAIN_flg, FCN_flg, ExitCode,
 *    ExecName, pid, output_file, lines, Final, ...).
 *  - h017 naming             : the banner and date comment are
 *    kept verbatim; the file is named after the vendor source
 *    file (u35 precedent).
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

//	June 16, 2024

// vendor: #include <stdio.h> -> java.io edges in ForCode.kt (u95),
//         CompilerState.Final/Out
// vendor: #include <mumpsc/memsize.h> -> CompilerState size
//         constants (PS/PS1/FBMAX/FORPARM/VERSION/DDAT/DKEY)
// vendor: #include <mumpsc/compiler-macros.h> -> no macro in this
//         chunk; per-unit consumers translate their own macros
// vendor: #include <mumpsc/compiler-globals-ext.h> -> the
//         CompilerState extern carrier
