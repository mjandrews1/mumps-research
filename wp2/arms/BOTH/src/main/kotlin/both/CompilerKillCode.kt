package both

// Unit 274 (BOTH B9, core): compiler-kill.c::kill_code (132
// LOC). Batch_plan ordinal 274: core, LOC 132. The KILL
// command compiler emitter, the kill-side sibling of the
// set_code dispatcher (ReadCode seat; the retz KILL token rides
// the set_code entry). Four families: the bare KILL (xpx sits
// on space/NUL/tab - the _SymFree(0) emission), the
// @-indirection arm (the atTest/atCopy/INTERPRET macro seat
// sequence, the "k " seed and the post conditional 3053 fossil
// kept byte-identical), the specific-kill chain (the parse_
// compiler-twin seat, the cvar guard with the KILL_DCL_ERROR
// arm, the sym_(2,...) kill-specific emission with Extract1
// over the pd1 tail at sdlim, the k003 comma chain), and the
// two paren families: kill-all-except (the emitted nmes list
// build - the malloc domain folds to the emitted C text
// verbatim, the p1->name strlen+1 idiom and the
// ErrorMessage("Out of memory") pair are EMITTED bytes, not
// Kotlin calls; the vd atmp/keyfix branch and the quoted-vd
// alias; the kill2 loopback; the sym_(5) close) and
// kill-globals (k004: the v1d printable-tail scan building the
// 0x01-terminated t walk, the strcatx(gtmp) emission via the
// Extract2 seat (owner compiler-parse.c::Extract2, b2/b3
// pending - the vendor Extract2(t) is the offset-0 call);
// the GKILL/XNEXT Mglobal block kept byte-identical; the
// commented-out duplicate block before it kept verbatim as the
// vendor fossil, h013).
//
// The k004 while(1) scaffolding: every vendor exit from the
// loop body jumps to k002 OUT of the loop, so the loop
// back-edge is unreachable - the body renders once with the
// dead-control note (h013). The vendor label chain
// (k001/k002/k003/k004/kill2/kill_return) renders as the
// explicit label state machine below (k002 CAN chain back to
// k003 and from there to k004 again, so the revisit is live
// and rendered). Unsigned/UNS casts fold (h012). retz KILL
// stages the set_code SET pass (SET 0, compiler-macros.h:83;
// KILL 'k' :108).
//
// Quirks kept (ledgered):
//   - the bare-KILL arm's k001 tail is shared with the k002
//     fall-in (the post conditional 3208 fossil close).
//   - the kill_return vd quote-wrap writes back into the vd
//     global (caller-visible; the Ext vdG seat mutated, kept).
//   - the k003 specific-kill arm re-enters parse_ per comma
//     (the chain kept).
//   - the k002 pre-increment xd[++xpx] read lands before the
//     comma test (kept verbatim; the pre-increment shape).
//   - the t walk reads v1d from index 1 (the parse_ canonical
//     prefix skip, vendor Fortran-odd-array banner cited).
//   - i/j are declared mid-block in the vendor at-family
//     (K&R shape; locals fold).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: every fprintf(Out,...)
//     emission rides the CompilerOutState.out seat (the
//     epiOutZb precedent; the kcOutZb fold below).
//   h004 include_only - reused: the TU banner/includes are the
//     unit-242 ledger.
//   h007 char_buffer - reused: tmp/t/vd/pd1 ride the
//     CompilerGlobalsExt CharArray seats (xdG/v1dG/vdG/pd1
//     pins); cStringOf folds each emission handoff.
//   h012 numeric_domain - reused: the xpx Long seat narrows to
//     Int at the xd index (the C int domain).
//   h013 dead_store - hit: the commented duplicate block in
//     k004; the unreachable while(1) back-edge; i/j mid-block
//     locals. Ledgered.
//   h014 out_of_scope_call - reused: atTest rides its stub
//     (owner compiler-at-test.c::atTest, roster 109 batch 4);
//     parse_ rides the WriteCode no-arg stub (owner
//     compiler-parse.c::parse_, roster 277 - THIS batch, the
//     stub releases there); set_code rides the ReadCode stub
//     (owner compiler-set.c::set_code, roster 97 batch 4);
//     cvar/Extract1 ride the CompilerParseMisc renders;
//     Extract2 rides the MumpsOpen stub (owner
//     compiler-parse.c::Extract2, b2/b3 pending); the
//     INTERPRET macro folds onto the landed interpret seat
//     (the compiler-macros.h macro ledger, the MumpsMain
//     lowercase face).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars (the emitted C dollars ride
//     string literals only), trailing newline.
//   h016 idiom_adaptation - hit: the return-code constants
//     cite the package seats (PARSE_ERROR/ABRTX at WriteCode,
//     ARG_LIST_ERROR..TOO_MANY_LHS at ReadCode); KILL_DCL_ERROR
//     7 seats here (compiler-macros.h:75, first use); the SET 0
//     and KILL 'k' tokens seat as KC_SET/KC_KILL.
//   h017 naming - reused: kill_code keeps the vendor name.

private const val KILL_DCL_ERROR = 7 // compiler-macros.h:75
private const val KC_SET = 0 // compiler-macros.h:83 (the set_code SET pass)
private const val KC_KILL = 'k'.code // compiler-macros.h:108 (the retz token)
private const val KC_TAB = '\t' // the bare-KILL exit idiom

// the fprintf(Out, ...) fold (the epiOutZb precedent)
private fun kcOutZb(s: String) {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

internal fun kill_code(): Int {
    val g = CompilerGlobalsExt
    val xd = g.xdG

    fun at(off: Long): Char = xd[off.toInt()]

    // Kill all locals: xpx sits on space/NUL/tab
    if (at(g.xpxG) == ' ' || at(g.xpxG) == '\u0000' || at(g.xpxG) == KC_TAB) {
        kcOutZb("\t_SymFree(0,svPtr);\n")
        return kcK001() // goto k001;
    }

    // Check for indirection in statement
    g.at_flg = atTest(xd, g.pd1, g.lines, g.xpxG.toInt()).toShort()
    if (g.at_flg.toInt() == -1) return ABRTX // goto abrtx;

    if (g.at_flg.toInt() != 0) {
        val tmp = CharArray(1024)
        writeC(tmp, "k ")
        atCopy(g.at_flg.toInt(), tmp)

        // INTERPRET - the macro fold onto the landed interpret
        // seat (the compiler-macros.h ledger, see KDoc)
        interpret(cStringOf(tmp), svPtr)

        g.xpxG = g.at_flg.toLong()
        if (g.POST_flg.toInt() != 0) {
            kcOutZb("\t} // post conditional 3053\n")
            g.POST_flg = 0
        }
        return 0 // goto next_cmnd;
    }

    var label = 3 // k003
    while (true) {
        when (label) {

        3 -> { // k003
            if (at(g.xpxG) == '^') {
                label = 4 // goto k004;
                continue
            }

            if (at(g.xpxG) != '(') {
                // Kill specific
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
                if (cvar(cStringOf(g.v1dG).drop(1)) != 0) {
                    return KILL_DCL_ERROR // goto kill_dcl_err;
                }

                kcOutZb(
                    "\tsym_(2, (UNS) " +
                    Extract1(kcPd1Tail()) +
                    ", (UNS) \"\",svPtr); //...* kill specific\n"
                )

                if (at(g.xpxG) == ',') {
                    g.xpxG += 1
                    continue // goto k003;
                }

                label = 1 // goto k001;
                continue
            }

            // Kill all locals except...
            kcOutZb("\t{ \n\tstruct nmes *p1, *p2; \n")
            kcOutZb("\tsvPtr->nstart = NULL;\n")
            g.xpxG += 1

            // kill2 / kill_return chain (paren-argument walk)
            while (true) {
                // kill2:
                g.t0pxG = 1
                g.t2G = 1
                g.retz = KC_KILL.toShort()

                val returnCode = set_code(KC_SET) // goto set; process item
                if (returnCode == PARSE_ERROR) return PARSE_ERROR
                if (returnCode == ARG_LIST_ERROR) return ARG_LIST_ERROR
                if (returnCode == NO_GLOBAL_PERMITTED) return NO_GLOBAL_PERMITTED
                if (returnCode == MEM_ERROR) return MEM_ERROR
                if (returnCode == TOO_MANY_LHS) return TOO_MANY_LHS
                if (returnCode == ABRTX) return ABRTX

                // kill_return:
                if (cStringOf(g.vdG) == "atmp") {
                    kcOutZb("\tkeyfix(atmp); \n")
                } else {
                    val tmpq = "\"" + cStringOf(g.vdG) + "\""
                    writeC(g.vdG, tmpq)
                }

                kcOutZb("\tp1 = (struct nmes *) malloc(sizeof (struct nmes));\n")
                kcOutZb(
                    "\tif (p1 == NULL) ErrorMessage(\"Out of memory\", " +
                    "svPtr->LineNumber);\n"
                )
                kcOutZb(
                    "\tp1->name = (char *) malloc(strlen((const char *) " +
                    cStringOf(g.vdG) + ") + 1);\n"
                )
                kcOutZb(
                    "\tif (p1->name == NULL) ErrorMessage(\"Out of memory\", " +
                    "svPtr->LineNumber);\n"
                )
                kcOutZb(
                    "\tstrmove((UNS) p1->name, (UNS) " +
                    cStringOf(g.vdG) + ");\n"
                )
                kcOutZb("\tp1->next = svPtr->nstart;\n")
                kcOutZb("\tsvPtr->nstart = p1;\n")
                g.xpxG += 1

                if (at(g.xpxG) != ')' && at(g.xpxG) != '\u0000') {
                    g.xpxG += 1
                    continue // goto kill2;
                }

                kcOutZb("\tsym_(5, (UNS) \"\",(UNS) \"\", svPtr); // kill all except\n")
                kcOutZb("\t}\n")
                break // fall to k002
            }

            label = 2 // k002
        }

        4 -> { // k004: Kill globals
            g.t0pxG = 1
            g.ierr = parse_().toShort()
            if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;

            kcOutZb("\tstrmove( (UNS) gtmp, (UNS) \"\");\n")

            val t = CharArray(1024)
            val v1d = g.v1dG
            var i = 1
            var j = 0
            while (v1d[i] != '\u0000') {
                if (v1d[i] >= ' ' && v1d[i] <= '\u007f') {
                    t[j] = v1d[i]
                    j++
                    i++
                } else break
            }
            t[j] = '\u0001'
            j++
            t[j] = '\u0000'

            kcOutZb("\tstrcat((char *) gtmp,(char *) \"" + cStringOf(t) + "\"); \n")

            if (v1d[i] != '\u0000') {
                i++
                while (true) {
                    j = 0
                    while (v1d[i] <= '\u007f' && v1d[i] >= ' ') {
                        t[j] = v1d[i]
                        j++
                        i++
                    }
                    t[j] = '\u0000'
                    kcOutZb("\tstrcatx((char *) gtmp,(char *) " + Extract2(cStringOf(t)) + "); \n")
                    // the vendor commented-out duplicate block (the j
                    // walk with the 0x01 tail and the plain strcat
                    // emission) kept verbatim in the vendor fossil,
                    // h013
                    if (v1d[i] != '\u0000') {
                        i++
                        if (v1d[i] != '\u0000') continue else break
                    } else break
                }
            }

            kcOutZb("\t{ \n")
            kcOutZb("\tunsigned char tmp14[STR_MAX], tmp15[STR_MAX]; \n")
            kcOutZb("\tkeyfix(gtmp);\n")
            kcOutZb("\t_k = strlen((const char *) gtmp); \n") // original length
            kcOutZb("\tstrmove((UNS) tmp14, (UNS) gtmp); \n")
            kcOutZb("\tMglobal(GKILL, gtmp, tmp15, svPtr); \n") // kill first
            kcOutZb("\twhile(1) { \n")
            kcOutZb("\t\t_j = Mglobal(XNEXT, gtmp, tmp15, svPtr); \n") // kill others like it
            kcOutZb("\t\tif (_j && strncmp( (CCHR) tmp14, (CCHR) gtmp,_k) == 0) { \n")
            kcOutZb("\t\t\tMglobal(GKILL, gtmp, tmp15, svPtr); \n")
            kcOutZb("\t\t\tcontinue; \n")
            kcOutZb("\t\t\t} \n")
            kcOutZb("\t\tbreak; \n")
            kcOutZb("\t\t} \n")
            kcOutZb("\t}\n")
            // the vendor while(1) back-edge is unreachable: every
            // exit jumps out to k002 (h013 dead-control note);
            // body once

            label = 2 // k002
        }

        2 -> { // k002:
            g.xpxG += 1 // the vendor pre-increment read
            if (at(g.xpxG) == ',') {
                g.xpxG += 1
                label = 3 // goto k003;
            } else {
                label = 1 // fall to k001
            }
        }

        1 -> { // k001:
            return kcK001()
        }
        }
    }
}

// the shared k001 tail of kill_code: the post conditional 3208
// fossil close and the return to the command dispatcher.
private fun kcK001(): Int {
    if (CompilerGlobalsExt.POST_flg.toInt() != 0) {
        kcOutZb("\t} /* post conditional 3208*/\n")
        CompilerGlobalsExt.POST_flg = 0
    }
    return 0 // goto next_cmnd;
}

// the pd1 tail at sdlim (the vendor &pd1[sdlim] address fold):
// the c-string over the slice, offset-aware
private fun kcPd1Tail(): String {
    val off = CompilerGlobalsExt.sdlim.toInt()
    val pd1 = CompilerGlobalsExt.pd1
    var k = 0
    while (pd1[off + k] != '\u0000') k++
    return String(pd1, off, k)
}
