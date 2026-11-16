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

## PN-4 — Grounding for the per-unit checks and test-suite instruments (2026-11-16)

The design names its verification methods without citing their sources;
the study bibliography now carries them (section E,
references/bibliography_scientific_study.md). Apply them as follows when
executing the checks and writing the report — no definition in
wp2/PER_UNIT_CHECKS.md and no design text changed:

- **output-check (frozen oracle).** The oracle is a whole-program-scale
  application of Feathers's characterization tests (S20); grading byte
  equivalence against the C baseline is the pseudo-oracle strategy from
  Barr et al.'s oracle-problem survey (S21). When the report discusses
  oracle limits, use S21's framing: the oracle characterizes the
  baseline's observed behavior, quirks included (link to S12's
  buggy-reference hazard, per PN-3).
- **runtime-check (property tests).** The property-test category descends
  from QuickCheck (S23): executable properties over generated inputs, not
  enumerated cases.
- **Differential testing (design §9.3, WP4).** Grounded in McKeeman
  (S22): the same inputs go to the C original and the Kotlin translation,
  and the outputs are compared.
- **Mutation testing (WP4).** Grounded in Jia & Harman (S24). Interpret
  the frozen oracle's mutation score as a measurement of how much
  divergence the oracle can actually detect; report it alongside the
  contrast estimates so readers can calibrate trust in oracle-based
  grading.

## PN-5 — Validity-methodology and machine-assistance grounding for the report (2026-11-16), extending PN-3

- **Pre-registration rationale (design §1.4, §9.2):** Nosek et al. (S25).
  The committed order assignments, pre-registered capture criteria, and
  pre-specified contrasts/subgroups are instances of separating
  confirmatory from exploratory analysis; the deviation/clarification
  logging discipline continues that separation.
- **Experiment design (design §1.1, §1.4, §10.3, §12.3):** Wohlin et al.
  (S26). Grounds within-subjects blocking by module, Latin-square
  counterbalancing, and the blocked models with covariates; its
  threats-to-validity taxonomy structures the final report's validity
  discussion alongside S12 (PN-3).
- **Machine-assisted premise:** Eniser et al. (S19) — the closest modern
  analogue: LLM translation of real-world C/C++ to Rust checked by
  I/O-equivalence (differential fuzzing in FLOURINE). Cite it in the
  motivation and discussion for both the premise and the
  equivalence-checking method, and use its results (best model 47%) as
  the automated-only baseline against which this study's
  human-in-the-loop, instrument-graded arms are discussed.
- **LLM translation errors:** Pan et al. (S18) — the 15-category
  translation-bug taxonomy; use it when categorizing graded-deviation
  entries in the qualitative synthesis, and cite it alongside S19 for
  the oracle-first motivation.
