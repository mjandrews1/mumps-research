# DAF spec directory

One `<unit>.dfy` per Dafny-verified unit (the roster row's unit id, e.g.
`interp-arith.cpp::numcomp` -> safe file per the journal conventions).
The Dafny method is a SPEC of the Kotlin function; the Kotlintranslation
lands first and the spec is verified against the reference semantics,
never the reverse. Exempted units have no file here - the exemption lives
in the arm journal.
