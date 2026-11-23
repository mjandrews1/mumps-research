# Target Lattice Roadmap: source-target language pairs beyond the MUMPS lineages

**Program-level planning note - not a pre-registered instrument.**
**Created:** 2026-11-23. Nothing in this file amends the current study's
pre-registered instruments or research design. Companion to
docs/REPLICATION_ROADMAP.md (2026-11-16); it extends the same
external-validity ladder from the corpus axis (RSM S27, GT.M S28) to the
target-language axis. Per the data-separation standing rule, no cell below
begins data collection before the current study's analysis window closes
(2027-12-31).

---

## Aim

Fill the source-target impedance lattice around the current study's cell
(legacy C/C++ -> managed JVM language, mid-far impedance) so that:

1. impedance distance becomes a measured covariate with corpus held
   fixed, and
2. the knowledge-capture mechanism claims (fast miss collapse after
   capture, stable reuse at coverage) are tested outside the MUMPS idiom.

## Selection criteria (inherited from the O'Kane study)

- Open, frozen, license-clean corpus (single lineage, pinnable sha).
- Freezable oracle: the source must run so behavior checks can be
  recorded.
- Unit count tractable for a single researcher (hundreds, not tens of
  thousands).
- Impedance profile deliberately chosen to test a registered claim.

## Candidate lattice (screened 2026-11-23)

| # | Source | Target | Impedance | Unique claim tested | Cost/risk |
|---|--------|--------|-----------|---------------------|-----------|
| L1 | O'Kane C/C++ (this corpus) | FreePascal | near | minimal-impedance floor; the carrier and unsigned-char families should vanish; goto and var-params change the control and out-param families | low |
| L2 | O'Kane C/C++ (this corpus) | Rust | far | completes the impedance spectrum with corpus and oracle held fixed; borrow families replace carrier families | medium |
| L3 | COBOL (GnuCOBOL NIST/samples; dialect pinned) | Kotlin | mid-far | the industry-migration cell; no pointers or preprocessor, so it isolates idiom-driven from memory-model-driven capture | medium |
| L4 | Fortran 77 (netlib kernels) | Rust | mid | densest decidable postconditions: the best verified-arm surface; deterministic numerical oracle | medium |
| L5 | Java 1.2-era legacy OSS | modern Kotlin | near-identity | saturation control: capture should stall if impedance, not habit, drives capture | low |
| L6 | Perl 5 (frozen 5.x core) | Python | mid | dynamic-to-dynamic; context-sensitivity families replace grammar-collision families | medium |
| L7 | Pascal/Delphi legacy | C# | mid | reverse-direction managed-to-managed control | low-moderate |
| L8 | Ada (GNAT ecosystem) | Rust | mid-far | strict-to-strict: does capture still fire without memory-model impedance? | moderate |
| L9 | Embedded C (AVR/PIC open firmware) | Rust embedded | mid-far | hardware-divergence families (I/O register folds, kin to the FILE* fold) | medium-low |
| L10 | x86 real-mode asm (boot-sector era) | C | far | hardest mechanism test; requires defining unit and check for a corpus with no function grammar - park behind a one-file pilot | high |
| L11 | Objective-C (frozen GNUstep) | Swift | mid | message-send idiom vs method-call; reference semantics | medium |

Excluded on purpose: HDL (Verilog/VHDL) - not an imperative translation
pair in this design's sense.

## Recommended commitment order

1. Same corpus, different target before different corpus, same target:
   L1 and L2 reuse this study's corpus, seed question, and oracles, so
   impedance becomes a within-corpus factor. Kotlin (this study)
   already populates the mid-far cell.
2. L5 (near-identity control) early because it is cheap and its
   negative result (near-total reuse, no captures at cold-start) is
   informative.
3. L3 (COBOL -> Kotlin) as the demand cell once the impedance claim
   has its answer.
4. L4, L6-L9 as the program matures; L10 only behind its pilot.

## Translator-stack freeze (LLM capability drift)

The defensive arms of this program are executed with machine assistance,
so translator capability is a hidden factor in every cell. Standing rules:

- The exact translator stack (models, versions, harness) is pinned and
  recorded at each cell's registration exactly like the oracle
  manifests, and held fixed for that cell's data collection.
- Cross-cell comparisons treat capability epoch as a covariate, not a
  confound to ignore: cells run in different epochs are compared on
  mechanism claims (capture convergence, instrument classification
  behavior), and capability-sensitive absolute rates (first-gate pass
  rates, consult density, time-to-green) are reported stratified by
  epoch.
- If a planned cell cannot hold its stack fixed for the collection
  window, that is logged as a deviation in the cell's append-only
  deviations log, never silently absorbed.
- A capability-epoch re-observation (one already-translated cell
  re-translated under a later stack, cold-start, with fresh
  randomization) is the registered instrument for measuring drift
  directly; it is a candidate follow-on study after L1/L2 answer the
  impedance question.

## Standing rules (carried over from REPLICATION_ROADMAP.md)

- Fresh pre-registration per cell; each cell catalogued in
  references/bibliography_scientific_study.md alongside S27/S28.
- The entry-seed inheritance policy (cold-start vs abstracted seed vs
  full seed from a prior study) is frozen per cell at its registration;
  for the same-corpus L1/L2 the seed question dominates all other
  design choices.
- The five arm definitions and the per-unit check vocabulary carry
  over unchanged; instrument CONTENT is expected to differ per cell
  (that is the point), instrument MECHANICS do not.
