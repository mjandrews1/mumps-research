# MUMPS Research: Machine-Assisted C/C++ -> Kotlin Translation Study

This repository hosts the scientific study **"Comparative Study of
Machine-Assisted C-to-Kotlin Translation: A Five-Arm Investigation of Formal
Verification (Dafny) and Declarative Heuristic Capture (Prolog)"**: a
controlled translation of the Mumps II Interpreter-Compiler Library (compiler
in C, ~8.7k LOC; interpreter in C++, ~26k LOC) from Dr. Kevin O'Kane's
GPL Mumps distribution, to Kotlin.

**Researcher:** Mark Andrews
**Status:** Active — WP1 complete; WP2 pilot batch B1 closed 2026-11-15
(31/31 units graded-ok in BASE and PRO, coverage 97.2% hit / 95.4% reuse);
batch B2 in progress.
**Target completion:** **December 31, 2027** — governed by [CADENCE.md](CADENCE.md).

## Study design

Five arms translate the same pre-registered corpus:

| Arm | Knowledge representation |
|---|---|
| BASE | Direct machine-assisted translation (no captured heuristics) |
| DOC | Markdown cookbook of translation heuristics |
| PRO | Prolog rule base (find_rule/1, record_reuse/2, coverage_report/0) |
| DAF | Dafny-assisted translation with formal verification |
| BOTH | Dafny + Prolog combined |

Pre-registered before the pilot (do not revise; deviations are logged as data):

- Sampling protocol, seed 20261113: 308 function units, 10 balanced batches (`tools/`)
- Frozen interpreter and translator oracles (`wp1/oracle/`, `wp2/translator-oracle/`)
- Latin-square arm-to-batch order assignments (`wp1/order_assignments.md`)
- Heuristic capture criteria, triggers T1-T3, gates G1-G4 (`wp1/capture_criteria.md`)
- Per-arm micro-replication draws (`tools/`, `micro_replication.csv`)

## Key documents

| Document | Purpose |
|---|---|
| [RESEARCH_PROPOSAL_C_To_Kotlin.md](RESEARCH_PROPOSAL_C_To_Kotlin.md) | Proposal and motivation |
| [RESEARCH_DESIGN_C_To_Kotlin.md](RESEARCH_DESIGN_C_To_Kotlin.md) | **Authoritative methodology** |
| [LITERATURE_REVIEW.md](LITERATURE_REVIEW.md) | Peer-reviewed translation/verification literature ([1]-[26]) |
| [METHODOLOGICAL_RIGOR_REVIEW.md](METHODOLOGICAL_RIGOR_REVIEW.md) | Rigor review and recommendations |
| [docs/research_narrative.md](docs/research_narrative.md) | Narrative of the design and plan |
| [references/bibliography_scientific_study.md](references/bibliography_scientific_study.md) | Pruned subject-side bibliography (S1-S17) |
| [CADENCE.md](CADENCE.md) | Authoritative schedule decision (completion 2027-12-31) |
| [TRACKING_README.md](TRACKING_README.md) | Tracking system (260 target hours, Q1-Q5 milestones) |

## Repository structure

```
wp1/          Preparation: fleet build script, frozen oracle + manifest,
              pre-registered order assignments and capture criteria
wp2/          Pilot and batches: per-arm translations (BASE/DOC/PRO/DAF/BOTH),
              heuristic logs, translator oracle, batch tracking
tools/        select_study_subset.py sampling tool + committed protocol files
docs/         Research narrative
references/   Scientific-study bibliography
archive/      Superseded material (see below)
```

## Archive

- [archive/history/](archive/history/) — the MUMPS/M **historical and industry
  research strand** (citation pearl growing study): timeline of 123 events
  (1963-2026), 10 industry domains, ANSI/ISO standards, 20+ dialects, expert
  systems, and the 128-entry bibliography.
- [archive/research_program/](archive/research_program/) — superseded 2029/2030
  "MUMPS/M distributions" program design documents, archived with
  supersession banners.
- [archive/ops/](archive/ops/) — infrastructure incident notes (2026-11-11),
  archived with internal addresses and domain names redacted.

## Schedule

[CADENCE.md](CADENCE.md) governs: completion 2027-12-31; Latin-square cycles
1-10 map to December 2026 - September 2027; October - December 2027 is the
analysis and final-report window. See also
[TRACKING_README.md](TRACKING_README.md), [PRINTABLE_CALENDAR.md](PRINTABLE_CALENDAR.md),
and [TRACKING_SPREADSHEET.csv](TRACKING_SPREADSHEET.csv).

## License

- **Code** (`wp1/`, `wp2/`, `tools/`): **GNU GPL v2** — the Kotlin
  translations are derivative works of O'Kane's GPLv2-licensed Mumps II
  Interpreter-Compiler Library.
- **Research content** (documents, data, analyses): **CC BY-NC 4.0**.

See [LICENSE-DETAILS.md](LICENSE-DETAILS.md) for the full licensing statement.

## Citation

```
Mark Andrews (2026). Comparative Study of Machine-Assisted C-to-Kotlin
Translation: A Five-Arm Investigation of Formal Verification (Dafny) and
Declarative Heuristic Capture (Prolog). Personal research project.
https://github.com/mjandrews1/mumps-research
```

## Contact

**Maintainer:** Mark Andrews ([@mjandrews1](https://github.com/mjandrews1))
