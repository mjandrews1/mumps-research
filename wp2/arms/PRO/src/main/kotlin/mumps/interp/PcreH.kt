package mumps.interp

/**
 * PRO-arm translation of include/pcre.h::<misc> (B4 unit 99,
 * rank 262). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 * Whole-header residual (units.csv: loc 127, comment_ratio 0.216,
 * pointer_marks 24, control_marks 13, score 82.5, batch 4 - exact
 * match): the header has no function bodies - only the version/
 * option/error/info/config macros, the pcre_extra and
 * pcre_callout_block carrier structs, the PCRE_DATA_SCOPE function
 * pointer globals, and the exported PCRE function declarations.
 *
 * The header is included by libmpscpp.cpp:55 but the corpus makes
 * no pcre_/PCRE_ calls, so the 19 exported function declarations
 * resolve to nothing in the arm yet (see h014) - they are
 * documented below, not stubbed (a stub is raised when a call
 * site lands, the ScanParse precedent).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : the PCRE_DATA_SCOPE function
 *    pointer globals (pcre_malloc/pcre_free/pcre_stack_malloc/
 *    pcre_stack_free/pcre_callout) become the PcreDataScope
 *    singleton properties, registered with the vendor lines
 *    (BtreeHeaderState precedent, unit 55).
 *  - h010 numeric_promotion  : unsigned long int flags/
 *    match_limit map to Long; size_t is not referenced by any
 *    translated signature (the declaration-only header leaves the
 *    malloc prototypes as documented comments).
 *  - h012 unsigned_byte_domain: the const char * subject field of
 *    the callout block maps to a CharArray? (byte domain carrier).
 *  - h014 out_of_scope_call : NO stubs raised - the header carries
 *    declarations only (pcre_compile, pcre_config, pcre_copy_
 *    named_substring, pcre_copy_substring, pcre_exec, pcre_free_
 *    substring, pcre_free_substring_list, pcre_fullinfo, pcre_get_
 *    named_substring, pcre_get_stringnumber, pcre_get_substring,
 *    pcre_get_substring_list, pcre_info, pcre_maketables,
 *    pcre_study, pcre_version - the external PCRE C library); no
 *    call site exists in the arm corpus, so each resolves to
 *    nothing until a translated caller lands.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new public PCRE_* consts, Pcre, PcreExtra,
 *    PcreCalloutBlock, PcreDataScope - no collisions (the
 *    BtreeH/CPPSYSFUNC headers hold no PCRE names).
 *  - h017 naming            : pcre.h -> PcreH wrapper file; the
 *    vendor struct names ride carrier classes (pcre is an opaque
 *    typedef of the private struct real_pcre).
 *  - h018 preprocessor_conditional: the macro cascade is resolved
 *    at the reference configuration - the Win32 DLL block is
 *    commented out in the vendor and stays commented; the
 *    VPCOMPAT #ifndef selects the PCRE_DATA_SCOPE extern arm
 *    (PCRE_DATA_SCOPE = extern, non-VPCOMPAT live; the VPCOMPAT
 *    extern arm is the excluded one); the __cplusplus extern "C"
 *    brace is a C linkage annotation with no Kotlin rendering.
 *    The _PCRE_H include guard has no Kotlin rendering (the
 *    package/class system is the guard); the stdlib.h include
 *    needed only for size_t is likewise not rendered.
 *
 * MISSED TRIGGER none: no conditional in the header selects
 * between translated behaviors - the reference build resolves the
 * data-scope macros as above.
 */

// Options (vendor lines 49-61)
/** PCRE_CASELESS 0x0001. */
const val PCRE_CASELESS = 0x0001
/** PCRE_MULTILINE 0x0002. */
const val PCRE_MULTILINE = 0x0002
/** PCRE_DOTALL 0x0004. */
const val PCRE_DOTALL = 0x0004
/** PCRE_EXTENDED 0x0008. */
const val PCRE_EXTENDED = 0x0008
/** PCRE_ANCHORED 0x0010. */
const val PCRE_ANCHORED = 0x0010
/** PCRE_DOLLAR_ENDONLY 0x0020. */
const val PCRE_DOLLAR_ENDONLY = 0x0020
/** PCRE_EXTRA 0x0040. */
const val PCRE_EXTRA = 0x0040
/** PCRE_NOTBOL 0x0080. */
const val PCRE_NOTBOL = 0x0080
/** PCRE_NOTEOL 0x0100. */
const val PCRE_NOTEOL = 0x0100
/** PCRE_UNGREEDY 0x0200. */
const val PCRE_UNGREEDY = 0x0200
/** PCRE_NOTEMPTY 0x0400. */
const val PCRE_NOTEMPTY = 0x0400
/** PCRE_UTF8 0x0800. */
const val PCRE_UTF8 = 0x0800
/** PCRE_NO_AUTO_CAPTURE 0x1000. */
const val PCRE_NO_AUTO_CAPTURE = 0x1000
/** PCRE_NO_UTF8_CHECK 0x2000. */
const val PCRE_NO_UTF8_CHECK = 0x2000

// Exec-time and get/set-time error codes (vendor lines 65-76)
/** PCRE_ERROR_NOMATCH (-1). */
const val PCRE_ERROR_NOMATCH = -1
/** PCRE_ERROR_NULL (-2). */
const val PCRE_ERROR_NULL = -2
/** PCRE_ERROR_BADOPTION (-3). */
const val PCRE_ERROR_BADOPTION = -3
/** PCRE_ERROR_BADMAGIC (-4). */
const val PCRE_ERROR_BADMAGIC = -4
/** PCRE_ERROR_UNKNOWN_NODE (-5). */
const val PCRE_ERROR_UNKNOWN_NODE = -5
/** PCRE_ERROR_NOMEMORY (-6). */
const val PCRE_ERROR_NOMEMORY = -6
/** PCRE_ERROR_NOSUBSTRING (-7). */
const val PCRE_ERROR_NOSUBSTRING = -7
/** PCRE_ERROR_MATCHLIMIT (-8). */
const val PCRE_ERROR_MATCHLIMIT = -8
/** PCRE_ERROR_CALLOUT (-9) - never used by PCRE itself (vendor comment). */
const val PCRE_ERROR_CALLOUT = -9
/** PCRE_ERROR_BADUTF8 (-10). */
const val PCRE_ERROR_BADUTF8 = -10
/** PCRE_ERROR_BADUTF8_OFFSET (-11). */
const val PCRE_ERROR_BADUTF8_OFFSET = -11

// Request types for pcre_fullinfo() (vendor lines 80-90)
/** PCRE_INFO_OPTIONS 0. */
const val PCRE_INFO_OPTIONS = 0
/** PCRE_INFO_SIZE 1. */
const val PCRE_INFO_SIZE = 1
/** PCRE_INFO_CAPTURECOUNT 2. */
const val PCRE_INFO_CAPTURECOUNT = 2
/** PCRE_INFO_BACKREFMAX 3. */
const val PCRE_INFO_BACKREFMAX = 3
/** PCRE_INFO_FIRSTBYTE 4. */
const val PCRE_INFO_FIRSTBYTE = 4
/** PCRE_INFO_FIRSTCHAR 4 - backwards-compatibility alias of FIRSTBYTE (kept verbatim). */
const val PCRE_INFO_FIRSTCHAR = 4
/** PCRE_INFO_FIRSTTABLE 5. */
const val PCRE_INFO_FIRSTTABLE = 5
/** PCRE_INFO_LASTLITERAL 6. */
const val PCRE_INFO_LASTLITERAL = 6
/** PCRE_INFO_NAMEENTRYSIZE 7. */
const val PCRE_INFO_NAMEENTRYSIZE = 7
/** PCRE_INFO_NAMECOUNT 8. */
const val PCRE_INFO_NAMECOUNT = 8
/** PCRE_INFO_NAMETABLE 9. */
const val PCRE_INFO_NAMETABLE = 9
/** PCRE_INFO_STUDYSIZE 10. */
const val PCRE_INFO_STUDYSIZE = 10

// Request types for pcre_config() (vendor lines 94-100)
/** PCRE_CONFIG_UTF8 0. */
const val PCRE_CONFIG_UTF8 = 0
/** PCRE_CONFIG_NEWLINE 1. */
const val PCRE_CONFIG_NEWLINE = 1
/** PCRE_CONFIG_LINK_SIZE 2. */
const val PCRE_CONFIG_LINK_SIZE = 2
/** PCRE_CONFIG_POSIX_MALLOC_THRESHOLD 3. */
const val PCRE_CONFIG_POSIX_MALLOC_THRESHOLD = 3
/** PCRE_CONFIG_MATCH_LIMIT 4. */
const val PCRE_CONFIG_MATCH_LIMIT = 4
/** PCRE_CONFIG_STACKRECURSE 5. */
const val PCRE_CONFIG_STACKRECURSE = 5

// Bit flags for the pcre_extra structure (vendor lines 104-107)
/** PCRE_EXTRA_STUDY_DATA 0x0001. */
const val PCRE_EXTRA_STUDY_DATA = 0x0001
/** PCRE_EXTRA_MATCH_LIMIT 0x0002. */
const val PCRE_EXTRA_MATCH_LIMIT = 0x0002
/** PCRE_EXTRA_CALLOUT_DATA 0x0004. */
const val PCRE_EXTRA_CALLOUT_DATA = 0x0004

// Version macros (vendor lines 21-23)
/** PCRE_MAJOR 4. */
const val PCRE_MAJOR = 4
/** PCRE_MINOR 5. */
const val PCRE_MINOR = 5
/** PCRE_DATE "01-December-2003". */
const val PCRE_DATE = "01-December-2003"

/**
 * vendor line 111: 'struct real_pcre;' - declaration only; the
 * definition is private to the PCRE library. The typedef
 * 'typedef struct real_pcre pcre;' rides this opaque carrier.
 */
class Pcre

/**
 * vendor lines 117-122: 'struct pcre_extra' - the structure for
 * passing additional data to pcre_exec(), defined so as to be
 * extensible (vendor comment).
 */
class PcreExtra {
    /** Bits for which fields are set. */
    var flags: Long = 0
    /** Opaque data from pcre_study(). */
    var study_data: Any? = null
    /** Maximum number of calls to match(). */
    var match_limit: Long = 0
    /** Data passed back in callouts. */
    var callout_data: Any? = null
}

/**
 * vendor lines 128-144: 'struct pcre_callout_block' - the structure
 * for passing out data via the pcre_callout function; a structure so
 * that new fields can be added on the end in future versions
 * (vendor comment).
 */
class PcreCalloutBlock {
    /** Identifies version of block. */
    var version: Int = 0
    // ------------------------ Version 0 -------------------------------
    /** Number compiled into pattern. */
    var callout_number: Int = 0
    /** The offset vector. */
    var offset_vector: IntArray? = null
    /** The subject being matched (h012 byte domain). */
    var subject: CharArray? = null
    /** The length of the subject. */
    var subject_length: Int = 0
    /** Offset to start of this match attempt. */
    var start_match: Int = 0
    /** Where we currently are. */
    var current_position: Int = 0
    /** Max current capture. */
    var capture_top: Int = 0
    /** Most recently closed capture. */
    var capture_last: Int = 0
    /** Data passed in with the call. */
    var callout_data: Any? = null
    // ------------------------------------------------------------------
}

/**
 * h004: the PCRE_DATA_SCOPE function pointer globals (vendor lines
 * 149-153, the non-VPCOMPAT live arm) - indirection for store get
 * and free functions; these can be set to alternative malloc/free
 * functions if required (vendor comment). Rendered as nullable
 * function-type properties; the vendor's Win32-DLL magic comment is
 * kept out (see the h018 note in the header KDoc).
 */
object PcreDataScope {
    /** PCRE_DATA_SCOPE void *(*pcre_malloc)(size_t). */
    var pcre_malloc: ((Int) -> Any?)? = null
    /** PCRE_DATA_SCOPE void (*pcre_free)(void *). */
    var pcre_free: ((Any?) -> Unit)? = null
    /** PCRE_DATA_SCOPE void *(*pcre_stack_malloc)(size_t). */
    var pcre_stack_malloc: ((Int) -> Any?)? = null
    /** PCRE_DATA_SCOPE void (*pcre_stack_free)(void *). */
    var pcre_stack_free: ((Any?) -> Unit)? = null
    /** PCRE_DATA_SCOPE int (*pcre_callout)(pcre_callout_block *). */
    var pcre_callout: ((PcreCalloutBlock) -> Int)? = null
}
