package mumps.interp

/**
 * Stubs for interpreter-side units not yet translated (BASE arm, pilot).
 * Replaced by real translations when each unit's turn comes in batch order.
 */

/**
 * C++: struct MSV — the Mumps interpreter state vector (svPtr).
 * A class (instance state), NOT a singleton: the interpreter may host
 * multiple state vectors. Members are added unit by unit.
 */
class MsvState

/** C: int btreeX(int g, unsigned char key[], unsigned char bd[], struct MSV * svPtr) */
fun btreeX(g: Int, key: String, bd: StringBuilder, svPtr: MsvState): Int =
    TODO("unit interp-native-btree.cpp::btreeX not yet translated (wrapper btree is unit 4)")
