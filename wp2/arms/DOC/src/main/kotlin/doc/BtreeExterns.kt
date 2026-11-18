package doc

// Externs of interp-native-btree.cpp:92-101 used by unit 50
// (scanblocks). One owner file per the cookbook one-owner-per-C-entity
// rule; the owning units (the btree maximize-search unit of a later
// batch) assign them through the block scan.

// interp-native-btree.cpp:56
const val UNMODIFIED = 0

// defines.h:120 MMARK 0xff; built as Int for the ByteArray domain
const val MMARK = 0xff

// btree.h:51
const val ADSIZE = 8

// BigKey: the running maximum key (unsigned char[STR_MAX] extern); the
// byte domain is preserved because the btree blocks are raw bytes.
val BigKey = ByteArray(STR_MAX)

// the extern assignment state of the btree search:
var BigKeyBlock = 0
var BigKeyOffset = 0
var BigKeyParent = 0
var BigKeyParentOffset = 0
var BigKeyLower = 0
var BigKeyData: Long = 0
