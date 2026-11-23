# Replication Roadmap: Reference Standard M (RSM) and GT.M

**Program-level planning note — not a pre-registered instrument.**
**Created:** 2026-11-16. Nothing in this file amends the current study's
pre-registered instruments (wp1/order_assignments.md,
wp1/capture_criteria.md, oracle manifests) or the research design.

---

## Aim

Repeat the five-arm machine-assisted translation design (BASE, DOC, PRO,
DAF, BOTH) on two further MUMPS implementations:

1. **Reference Standard M (RSM)** — first replication, planned start
   **early 2028**, after the current study's analysis window closes
   (December 2027).
2. **GT.M** — second replication, after the RSM replication.

Each replication is a separate study with its own registration, data
collection, and report.

## Rationale

The current study translates one implementation lineage: the O'Kane Mumps
II Interpreter-Compiler Library, a compact academic C/C++ codebase. The
two replication subjects extend the external-validity ladder across the
MUMPS implementation landscape:

- **O'Kane Mumps II** — compact academic implementation (current study)
- **RSM** — standards-track, open-source implementation in C (S27)
- **GT.M** — industrial production engine, banking and EHR (S28)

If the five-arm effect estimates hold across these lineages, the design's
claims generalize beyond one implementation; if they do not, the
replications bound the claims.

## Standing rules

- **Fresh pre-registration per replication.** Order assignments, capture
  criteria, oracle manifests, and unit-set design are re-registered
  before any replication data collection. No replication pre-registration
  is opened until the current study's analysis window closes (2027-12-31).
- **Fixed elements.** The five arm definitions and the per-unit check
  vocabulary (wp2/PER_UNIT_CHECKS.md: code/output/runtime/target-check)
  carry over unchanged, so effect estimates remain comparable across
  studies.
- **Data separation.** No replication data collection begins before the
  current study's analysis window closes.
- **Practice separation.** This roadmap is the single planning home for
  the replication program; wp2/PRACTICE_NOTES.md remains reserved for
  refinements to current-study practice.

## Translator capability drift (2026-11-23)

The five-arm effects are conditional on a "translator still needs the
instrument" regime: as machine-assistant capability grows, consult
density and miss firing drop, and the instrument arms are expected to
converge behaviorally (contrast attenuation) even where instrument
value is unchanged. Standing rules for the replication program:

- Stack freeze: each study pins its machine-assistant stack
  (models, versions, harness) at registration like an oracle manifest
  and holds it for the whole data-collection window
  (REPLICATION_PREREG_SKELETON.md section 1).
- Epoch as covariate, not confound: cross-study comparisons lean on
  mechanism claims (capture convergence, instrument classification);
  capability-sensitive absolute rates are reported stratified by
  stack epoch.
- Registered drift instrument: the capability-epoch re-observation
  (one already-translated corpus re-translated cold-start under a
  later stack, fresh randomization) is the direct measure of drift;
  candidate follow-on after the target-lattice L1/L2 cells answer
  the impedance question (docs/TARGET_LATTICE_ROADMAP.md).
- Arm asymmetry noted for planning: the verified-spec arm (DAF
  lineage) carries the most drift-resistant claims (verification is
  independent of translator competence); the memory-instrument arms
  (DOC/PRO lineage) depend on the translator having gaps. Feed into
  the D-8 capability-epoch policy before the RSM freeze.
- The BASE arm's role strengthens under drift: it is the
  capability-anchored control arm, so BASE variance and pass rates
  must be power-parameterized from study 1 (D-6) before any
  replication margin is fixed.

## Decisions

- **GT.M, not YottaDB (2026-11-16).** The second replication subject is
  GT.M proper, not the YottaDB fork. Version policy: use the latest
  available GT.M version, even if the newest engine code is embedded in
  a YottaDB release; record the exact version, its source distribution,
  and whether it is standalone or embedded, at pre-registration. (As of
  2026-11-16, FIS GT.M release notes run through V7.2-002.)

## Open decisions

1. **Unit sampling at scale.** GT.M's codebase is far larger than the
   O'Kane library; the 308-unit sampling design may need a stratified
   variant.
2. **License handling.** RSM is AGPL-3.0; GT.M has a GPL v2/v3/AGPL v3
   lineage. Translated artifacts are derivative works; license the
   replication outputs accordingly (cf. LICENSE-DETAILS.md for the GPLv2
   pattern used in the current study).
3. **RSM version pinning.** Mainline vs m-2021-standard branch (MDC
   Type A extensions); pin at pre-registration.
4. **Subject-language scope.** The current study covers C + C++; RSM is
   C. GT.M's implementation language is not yet independently verified
   — verify and pin at pre-registration.
5. **Oracle construction.** Each subject needs its own frozen
   characterization-test oracle built from its runtime behavior.

A concrete pre-registration skeleton now exists at
docs/REPLICATION_PREREG_SKELETON.md (2026-11-21). It operationalizes
these open decisions as slots D-1 through D-7 and adds the design
simplifications under review: fractional arm coverage with mixed-model
inference, seed-committed stratified randomization, the cross-arm
Latin-square schedule, cycle-index modeling, and a single-source-of-truth
ledger pipeline. Decisions remain open until each pre-registration is
frozen; this roadmap stays the planning home.

## Existing groundwork

- **m-semantics** — Dafny semantics core for the RSM porting track
  (464 verified proofs, v2.2.0, October 2026;
  https://github.com/mjandrews1/m-semantics)
- **JavaM** — JvMumps + mJVM matching RSM's full M surface
- **m-lsp** — language server rebuilt for RSM (rsm issue #27)
- **License compliance** — open question tracked as m-semantics issue #123

## Bibliography

The replication subjects are catalogued as S27 (RSM) and S28 (GT.M) in
references/bibliography_scientific_study.md, section F.
