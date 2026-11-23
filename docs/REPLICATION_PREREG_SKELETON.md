**Program-level planning note — not a pre-registered instrument.**
**Created:** 2026-11-21. Nothing in this file amends the current study's
pre-registered instruments; it is the design skeleton both replications
will freeze from. Companion to docs/REPLICATION_ROADMAP.md (2026-11-16).


# Pre-Registration Skeleton: RSM and GT.M Translation Replications

Status: DRAFT skeleton, 2026-11-21. Not yet a pre-registration. Bracketed
slots `[LIKE THIS]` are decisions to fix before freezing each document.
Shared core sections are written once; each study freezes its own instance.
Target: freeze RSM pre-registration by `[Q4 2027]`; GT.M by `[Q4 2028]`.

## 1. Study identity

- Title: Machine-assisted translation of `<implementation>` to Kotlin:
  a five-arm replication in the Mumps research program.
- Lineage slot: 2 of 3 (RSM) / 3 of 3 (GT.M). Study 1 of 3 = O'Kane
  Mumps II (the current study; analysis window Oct-Dec 2027, untouched
  by this skeleton).
- Subject software (pin at freeze):
  - RSM: Reference Standard M, `[repo@tag or commit]`, branch
    `[mainline | m-2021-standard]` (decision D-1), source tarball
    sha256 `[...]`.
  - GT.M: latest FIS GT.M, `[version]`, provenance `[standalone
    release | embedded in YottaDB vX.Y]` (resolved policy; pin
    specifics at freeze), source distribution sha256 `[...]`.
- License handling: AGPL v3 both subjects; translated-artifact
    distribution terms = `[DECISION D-2: internal-only |
    AGPL-compliant public release]`, recorded before cycle 1.
- Registration venue: `[OSF | Zenodo | repo tag]`: immutable,
  timestamped deposit of this document plus the instruments in
  sections 4-6. Amendments after freeze go in an append-only
  deviations log, never edited in place.
- Translator-stack freeze (2026-11-23): the machine-assistant stack
  used for translation `(models + versions + harness configuration)`
  is recorded at freeze with the same rigor as the oracle sha256s and
  held fixed for the study's entire data-collection window. A stack
  change mid-collection is a logged deviation, never silently
  absorbed. Rationale: the defensive arms are executed with machine
  assistance, so translator capability is a hidden factor in every
  cell; capability-sensitive absolute rates are interpretable only
  against a frozen stack.

## 2. Research questions and estimands

Primary estimand (E1): the effect of translation arm (BASE vs DOC vs
PRO vs DAF vs BOTH) on per-unit first-gate pass rate in the
`<implementation>` lineage, as a risk difference on the sampled unit
set.

- E2 (secondary, mechanism): distribution of consult outcomes
  (hit-reuse / hit-adapt / miss) by arm.
- E3 (secondary, cost): minutes from unit open to first green gate
  (time-to-green) by arm, from the machine-written event ledger.
- E4 (secondary, fidelity): oracle-pass equivalence rate by arm
  against the frozen M-oracle of section 5, with pre-specified
  tolerance bounds.
- E5 (cross-study, registered now, tested only after study 3 closes):
  random-effects meta-analysis of E1 across the three lineages
  (O'Kane, RSM, GT.M), lineage as sampling unit, common metric set
  only, heterogeneity (I-squared) reported.

Hypotheses are stated as pre-specified margins on the arm-vs-BASE
contrasts for E1: DOC `[margin]`, PRO `[margin]`, DAF `[margin]`,
BOTH `[margin]`; sidedness `[DECISION D-3]`; Holm correction across
the four contrasts. All other quantities are reported descriptively
with confidence intervals.

## 3. Design

Five arms, fractional unit coverage:

- BASE translates the full sampled unit set (anchors the oracle and
  the learning-curve model).
- DOC / PRO / DAF / EACH translate `[70-90]`-unit stratified random
  subsamples of that set. Full factorial is deliberately abandoned:
  a mixed-effects model with unit as a random effect recovers the arm
  contrasts at roughly a quarter of the cost (this skeleton's
  motivating simplification).
- Blocking and randomization mechanics:
  - Stratify sampled units by `[batch | LOC tertile + complexity
    rank]`; randomize unit order within stratum with seed
    `[<uint64>]` from `[crypto-real RNG procedure]`.
  - Assign units to tranches by a Latin-square-style schedule so
    tranche-by-stratum difficulty is balanced (the DAF square design
    of study 1 becomes the standard balancing device across arms).
  - Cycle index is recorded per unit and enters all models as a fixed
    effect (learning-curve covariate E6, descriptive): time is
    modeled, not confounded.
- Each replication is registered fresh: no seed, order, or capture
  criterion carries over from study 1; study 1 instruments are
  templates, not commitments.

## 4. Unit-set construction (fresh per study)

- Source scope: `[C files only | C + generated C]`; exclusions mirror
  study 1 (`*.in`, `*/usr/*`, `*/autom4te.cache/*`, `*/DEBIAN/*`,
  `*/Checkout/*`).
- Unit granularity: function, plus the header-`<misc>` chunk rule
  (unclaimed lines + functions below LOC-15 folded back by the
  chunker), identical mechanics to study 1, pre-specified.
- Census `[N]` units; the sampled subset with per-unit stratum labels
  is generated by script from the frozen seed and is immutable from
  freeze onward.
- Doc/rule-arm capture criteria on a pure C codebase: `[DECISION D-4
  with worked examples: .h prototypes and macro counters as
  DOC-capturable, etc.]`.
- Semantic delta: RSM is strict ANSI/MDC X11.1-1995; oracle semantics
  in section 5 are re-derived per lineage, never copied.

## 5. Instruments (frozen before cycle 1)

- M-oracle: `[suite freeze, M1-equivalent]`, derived from
  `[source]`, pass criteria `[exact-output | tolerance bounds +
  rationale]`, frozen with sha256.
- Consult instrument: one consult per unit per arm; the rubric
  (hit-reuse / hit-adapt / miss) rewritten as operational definitions
  with worked examples anchored to study 1 ledgers.
- Inter-rater reliability (new, binding): a random 20% of consults
  per arm is dual-annotated; Cohen's kappa reported and must reach
  `[threshold, e.g. >= 0.7]` for the ledger to serve as E2;
  adjudication rules for disagreements fixed in advance.
- Gate: whole-arm kotlinc silence (0 errors / 0 warnings) + gradle
  build, same thresholds as study 1.

## 6. Data pipeline (single source of truth)

- One machine-written ledger per event type; all derived artifacts
  (events CSV, consult grammar rows, fragment tables) are generated
  by script from the ledger, never hand-authored or duplicated.
  Appends are checksum-verified; regeneration is byte-identity-gated.
- CI gate on every push: ASCII check, consult grammar regex, ledger
  regen round-trip, byte-prefix verification, repo-root push-path
  enforcement.
- Prose is reserved for adjudications, deviations, and
  non-derivable disposition notes; narrative duplication of ledger
  content is out of scope by design.

## 7. Deviations and analytic handling

- Deviations logged append-only at push time (study 1 precedent,
  the EOF-store disclosure pattern).
- Pre-specified sensitivity analysis S1: re-fit E1 excluding or
  down-weighting units with disclosed semantic deviations; S1 is
  reported adjacent to E1 and never overrides it.
- Exclusion rules `[pre-committed list]`; no post-hoc exclusions.

## 8. Analysis plan

- E1: mixed-effects logistic regression on first-gate pass/fail;
  fixed effects arm + cycle index; random intercept for unit; risk
  differences with 95% CIs; exact-binomial per-arm fallback if the
  model fails; Holm across the four contrasts.
- E2: ordinal/multinomial model by arm or descriptive with CIs,
  gated by the section-5 kappa.
- E3: survival analysis (Cox / AFT) on time-to-green with arm and
  cycle covariates; hazard ratios reported.
- E6 (descriptive): learning-curve slopes per arm from cycle index.
  Interpretation pre-specification (2026-11-23): the within-study
  batch-position slope is INSTRUMENT learning, holding translator
  capability fixed (the stack is frozen at section 1); any
  cross-study slope comparison across capability epochs is excluded,
  and capability-sensitive absolute rates are reported stratified by
  stack epoch.
- Missing data: abandoned units count as gate failures
  (conservative), never dropped.
- E5: run only after study 3 closes.
- Environment frozen (renv lockfile / pinned requirements),
  analysis scripts committed before the analysis window opens.

## 9. Timeline

- RSM: freeze `[Q4 2027]`, oracle freeze `[M1-prime]`, cycles
  `[12 months]`, analysis `[3 months]`, buffer `[1 month]`.
- GT.M: same shape shifted `[+12 months]`; feasibility gate after
  RSM cycle 6 with pre-specified continue / amend / stop criteria
  `[DECISION D-5]`.

## 10. Open decisions to close before freeze

- D-1: RSM branch (mainline vs m-2021-standard).
- D-2: artifact distribution terms under AGPL.
- D-3: one- vs two-sided E1 margins.
- D-4: doc-arm capture criteria for pure C.
- D-5: GT.M continuation gate criteria.
- D-6: subsample size per arm (power analysis from study 1 BASE
  variance once its analysis closes; target detection of
  `[risk difference]` at `[alpha]` / `[power]`).
- D-7: annotation budget and personnel for the E2 IRR sample.
- D-8 (2026-11-23): capability-epoch policy - what the frozen stack is
  for each study, and whether the capability-epoch re-observation
  study (same corpus re-translated cold-start under a later stack,
  fresh randomization; cf. docs/TARGET_LATTICE_ROADMAP.md
  "Translator-stack freeze") is registered now as a follow-on or
  deferred.

## 11. Changelog

- 2026-11-21: initial skeleton drafted from the same-day design
  review; the running O'Kane study is unchanged and unaffected.
- 2026-11-23: translator-stack freeze added to study identity; E6
  instrument-learning interpretation pre-specified; open decision
  D-8 (capability-epoch policy) added. Planning-level only; the
  running O'Kane study and its pre-registered instruments are
  untouched (RESEARCH_DESIGN_C_To_Kotlin.md is NOT amended; if the
  current study ever needs the same pre-specification it enters as a
  registry entry in wp2/PROTOCOL_CLARIFICATIONS.md, not here).