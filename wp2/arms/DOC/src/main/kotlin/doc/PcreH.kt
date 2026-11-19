package doc

// Unit 118 (B4 tranche 3): include/pcre.h::<misc> (batch 4,
// complex stratum; LOC 127, score 82.5, rank 262). Whole-file
// residual of the Cambridge PCRE 4.5 header (the
// configure-built pcre.h; "do not edit, change pcre.in" banner
// kept): constants, the two data carriers, the indirect store/
// callout roster, and the exported function prototype ledger.
// H-018 notes: the _WIN32 DLL arm is COMMENTED OUT in the
// vendor (the trailing #define PCRE_DATA_SCOPE extern is the
// live arm); VPCOMPAT is undefined so the #ifndef VPCOMPAT arm
// of the indirect function roster is live (the extern arm is
// journaled). Declarations render as a LEDGER (the arm has no
// pcre call sites; the rpm family consults the pattern tables
// only). PCRE_MAJOR 4 / PCRE_MINOR 5 / PCRE_DATE the version
// roster note. No carriers raised; no stubs (nothing calls).
// Naming per H-017.

// Options (offset bit flags)
private const val PCRE_CASELESS = 0x0001
private const val PCRE_MULTILINE = 0x0002
private const val PCRE_DOTALL = 0x0004
private const val PCRE_EXTENDED = 0x0008
private const val PCRE_ANCHORED = 0x0010
private const val PCRE_DOLLAR_ENDONLY = 0x0020
private const val PCRE_EXTRA = 0x0040
private const val PCRE_NOTBOL = 0x0080
private const val PCRE_NOTEOL = 0x0100
private const val PCRE_UNGREEDY = 0x0200
private const val PCRE_NOTEMPTY = 0x0400
private const val PCRE_UTF8 = 0x0800
private const val PCRE_NO_AUTO_CAPTURE = 0x1000
private const val PCRE_NO_UTF8_CHECK = 0x2000

// Exec-time and get/set-time error codes
private const val PCRE_ERROR_NOMATCH = -1
private const val PCRE_ERROR_NULL = -2
private const val PCRE_ERROR_BADOPTION = -3
private const val PCRE_ERROR_BADMAGIC = -4
private const val PCRE_ERROR_UNKNOWN_NODE = -5
private const val PCRE_ERROR_NOMEMORY = -6
private const val PCRE_ERROR_NOSUBSTRING = -7
private const val PCRE_ERROR_MATCHLIMIT = -8
private const val PCRE_ERROR_CALLOUT = -9
private const val PCRE_ERROR_BADUTF8 = -10
private const val PCRE_ERROR_BADUTF8_OFFSET = -11

// Request types for pcre_fullinfo()
private const val PCRE_INFO_OPTIONS = 0
private const val PCRE_INFO_SIZE = 1
private const val PCRE_INFO_CAPTURECOUNT = 2
private const val PCRE_INFO_BACKREFMAX = 3
private const val PCRE_INFO_FIRSTBYTE = 4
private const val PCRE_INFO_FIRSTCHAR = 4 // backwards compatibility
private const val PCRE_INFO_FIRSTTABLE = 5
private const val PCRE_INFO_LASTLITERAL = 6
private const val PCRE_INFO_NAMEENTRYSIZE = 7
private const val PCRE_INFO_NAMECOUNT = 8
private const val PCRE_INFO_NAMETABLE = 9
private const val PCRE_INFO_STUDYSIZE = 10

// Request types for pcre_config()
private const val PCRE_CONFIG_UTF8 = 0
private const val PCRE_CONFIG_NEWLINE = 1
private const val PCRE_CONFIG_LINK_SIZE = 2
private const val PCRE_CONFIG_POSIX_MALLOC_THRESHOLD = 3
private const val PCRE_CONFIG_MATCH_LIMIT = 4
private const val PCRE_CONFIG_STACKRECURSE = 5

// Bit flags for the pcre_extra structure
private const val PCRE_EXTRA_STUDY_DATA = 0x0001
private const val PCRE_EXTRA_MATCH_LIMIT = 0x0002
private const val PCRE_EXTRA_CALLOUT_DATA = 0x0004

// typedef struct pcre_extra (opaque-flag carrier; the void*
// study_data/callout_data seats ride the Any? domain)
private class pcre_extra_unused {
    var flags: Long = 0
    var study_data: Any? = null
    var match_limit: Long = 0
    var callout_data: Any? = null
}

// typedef struct pcre_callout_block (version-0 carrier)
private class pcre_callout_block_unused {
    var version: Int = 0
    var callout_number: Int = 0
    var offset_vector: IntArray? = null
    var subject: String = ""
    var subject_length: Int = 0
    var start_match: Int = 0
    var current_position: Int = 0
    var capture_top: Int = 0
    var capture_last: Int = 0
    var callout_data: Any? = null
}

// The exported PCRE function prototype roster (ledger, no seats
// raised - the arm has no pcre call sites):
//   pcre_compile / pcre_config / pcre_copy_named_substring /
//   pcre_copy_substring / pcre_exec / pcre_free_substring /
//   pcre_free_substring_list / pcre_fullinfo /
//   pcre_get_named_substring / pcre_get_stringnumber /
//   pcre_get_substring / pcre_get_substring_list / pcre_info /
//   pcre_maketables / pcre_study / pcre_version
// Indirect store/callout roster (the live #ifndef VPCOMPAT arm):
//   pcre_malloc / pcre_free / pcre_stack_malloc /
//   pcre_stack_free / pcre_callout
private val pcreProtoLedger = listOf(
    "pcre_compile", "pcre_config", "pcre_copy_named_substring",
    "pcre_copy_substring", "pcre_exec", "pcre_free_substring",
    "pcre_free_substring_list", "pcre_fullinfo",
    "pcre_get_named_substring", "pcre_get_stringnumber",
    "pcre_get_substring", "pcre_get_substring_list",
    "pcre_info", "pcre_maketables", "pcre_study", "pcre_version",
    "pcre_malloc", "pcre_free", "pcre_stack_malloc",
    "pcre_stack_free", "pcre_callout"
)
