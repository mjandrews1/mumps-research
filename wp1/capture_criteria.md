# Pre-Registered Heuristic Capture Criteria (shared by arms DOC and PRO)

**Committed: 2026-11-14, before the pilot batch.** These criteria are
identical for both heuristic arms; only the *representation* differs — DOC
captures them as a Markdown cookbook, PRO as a Prolog rule base. Anything
not satisfying these criteria is NOT captured (it stays in the arm's work
journal as an ad-hoc note). Source of the need: rule/strategy separation
(Drabent [60]); canonicalization before capture (Newman et al. [36];
Balland [34]) — see LITERATURE_REVIEW.md §6/6.6.

## 1. Capture trigger (when a heuristic becomes an entry)

A translation decision is captured as a knowledge artifact iff **any** of:

- **T1 (recurrence):** the same source construct was resolved ≥2 times in
  the current arm, or ≥2 times across prior batches of this arm; or
- **T2 (effort):** resolving the construct cost >15 minutes of active work
  in one unit (a candidate is allowed to mature once before T2 alone fires); or
- **T3 (risk):** the construct's resolution required consulting an external
  reference (Dafny docs, Kotlin docs, MUMPS standard), because future
  encounters will need the same reference.

Anything captured must also pass the **quality gates** (§3). If it fails a
gate, it stays as a journal note and MAY be promoted later.

## 2. Entry schema (identical fields in cookbook and rule base)

| Field | Meaning |
|-------|---------|
| id | Stable, never reused (`H-###` in DOC; `rule(hNNN, ...)` in PRO) |
| trigger | Canonical source pattern that activates the heuristic, in canonicalized C/C++ form (post §2.4 normalization) |
| condition | Guards/limits — when the trigger does NOT apply |
| action | The translation decision, as a rewrite on canonical constructs |
| example | One real instance from the corpus (file :: unit id) |
| provenance | Arm + batch + date of first capture |
| verification | One of: `oracle-verified` (a case in the frozen oracle exercises it), `unit-tested`, `untested` — must be upgraded or the entry is flagged in coverage reports |

## 3. Quality gates (both arms, checked at capture time)

- **G1 (generality):** the entry must state its applicability over the
  canonical construct, not a specific unit. If it only helps the exact unit
  it came from, it is a note, not an entry.
- **G2 (canonical form):** trigger and action are written against the
  canonicalized source (§2.4 step 4), never against raw surface syntax, so
  both arms can match them mechanically.
- **G3 (no oracle contamination):** an entry may reference the oracle as a
  verification target but must never embed oracle outputs as constants.
- **G4 (deduplication):** before adding, the author queries the existing
  artifact for an entry with the same canonical trigger; if found, the old
  entry is revised (version incremented) instead of a new one created.

## 4. Reuse semantics (logged identically in DOC and PRO)

- **Hit:** a knowledge artifact was consulted before/during editing a unit.
- **Reuse:** an entry's action was applied without modification.
- **Adapt:** an entry was applied after modification (counts as hit + 1
  adaptation, not reuse).
- **Miss:** a triggering situation occurred and no entry existed.
- Instrumentation: DOC logs hits via lookup logging on the cookbook file;
  PRO logs via `find_rule/1` / `record_reuse/2` / `coverage_report/0`
  (WP2 deliverable).

## 5. Pre-registered metrics definitions

- **Capture rate** = entries created ÷ (T1∪T2∪T3) triggering situations.
- **Hit rate** = hits ÷ triggering situations.
- **Reuse rate** = reuse ÷ hits.
- **Coverage** = fraction of the batch's canonical constructs matched by ≥1
  entry (from `coverage_report/0` in PRO; the cookbook equivalent is a
  manual checklist per batch, recorded in the journal).

These metrics are compared between DOC and PRO for H4 (representation
effect), and their growth curves feed the knowledge-artifact maturity
covariate (§11).
