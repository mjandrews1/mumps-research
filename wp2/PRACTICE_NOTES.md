# Practice Notes

Non-protocol refinements to research practice. Entries here refine how
existing, pre-registered instruments are applied, or how the eventual
report is framed — they change no instrument itself. Anything that would
touch a pre-registered instrument (design, order assignments, capture
criteria, oracle) is a protocol event and belongs in
`wp2/PROTOCOL_CLARIFICATIONS.md` instead, never here.

Derived from the study bibliography
(`references/bibliography_scientific_study.md`) on 2026-11-16.

---

## PN-1 — Faithful-quirk ledger: record the deciding reference (2026-11-16)

When a source-code quirk is judged to be a language property rather than
an implementation bug (or vice versa), record which reference informed
the judgment: the standards texts (S4, Annotated M[UMPS] Standards; S5,
Hardhats machine-readable M Standard) or the formal critique (S7,
Sherertz/Wasserman/Allison 1974). The design mandates the faithful-quirk
ledger (sections 1.4 and 7.4); a provenance field on its entries makes
the judgments auditable for the final report without changing the
ledger's pre-registered role.

## PN-2 — Runtime-check edge design for global-handling units (2026-11-16)

Batches from B2 onward contain a dense run of `libmpscpp.cpp::global::*`
units (Extract, Find, Piece, Min, IDF, Multiply, GlobalOrder, TreePrint,
GlobalArray, ...). When designing runtime-check edge cases (per the
definition in `wp2/PER_UNIT_CHECKS.md`, registry C-002), derive them
from the formal treatment of global semantics in S9 (Wasserman, Sherertz
& Rogerson, "MUMPS Globals and their Implementation," 1975): traversal
order, $DATA/$ORDER semantics, collation, and locking/concurrency. This
operationalizes the existing runtime-check definition; it does not
change it. Precedent: unit 10 (gcvt semantics verified against the
reference binary); the standards texts (S4, S5) serve the same role for
standard-defined semantics.

## PN-3 — Related-work positioning for the final report (2026-11-16)

- S13 (Diggs et al., 2024 — LLM-generated documentation for WorldVistA
  MUMPS): the closest academic neighbor. Anchor the motivation and
  discussion sections against it, noting that the DOC arm's artifact is
  a translation-heuristic cookbook, not code comments, so S13's
  evaluation dimensions inform framing, not the DOC grading scheme.
- S14 (VistA modernization pilot) and S15 (MUMPS-to-Java conversion
  planning): prior MUMPS-modernization attempts for the motivation and
  discussion sections.
- S12 (Zaytsev 2017): cite for the incremental-coverage hazards the
  design already guards against — buggy reference implementation (the
  frozen oracle characterizes the C baseline's observed behavior, quirks
  included) and fragile workarounds (deviation logging) — in the
  threats-to-validity discussion.
- S16 (Bonfanti et al., 2018): positions Dafny deductive verification
  against the model-checking mainstream in this software class; supports
  the DAF/BOTH novelty framing.
