# MUMPS/M Programming Language — Historical Research (Archived Strand)

This directory contains the **historical and industry research strand** of the
MUMPS research program: a literature study of the MUMPS/M (Massachusetts General
Hospital Utility Multi-Programming System) programming language, built with the
**citation pearl growing** methodology (Markey & Atherton, 1978; Hawkins &
Wagers, 1982).

**Moved here from the repository root on 2026-11-15** so that the active
scientific study (machine-assisted C/C++ -> Kotlin translation of the Mumps II
Interpreter-Compiler Library) is the repository's focus. Paths below are
relative to this directory.

## Contents

| Path | Contents |
|---|---|
| `data/timeline.csv` | 123 events, 1963-2026, in 5-year bins, with categories and sources |
| `analysis/` | Domain analyses: standards (ANSI/ISO), 10 industry domains, 20+ dialects, expert systems and Arden Syntax, successes and failures, precursors (JOSS/TELCOMP/STRINGCOMP), formal methods, chronology summary |
| `analysis/generate_histogram*.py` | Scripts regenerating the histogram data from timeline.csv |
| `references/bibliography.md` | 128-entry bibliography with pearl classification and DOI links |

To regenerate analysis outputs:

```bash
cd archive/history/analysis
python3 generate_histogram_simple.py  # no dependencies
python3 generate_histogram.py        # with matplotlib
```

## Key findings

- **Standards**: 8 ANSI standards (1977-1995), 3 ISO/IEC standards (1992-1999), reaffirmed 2005/2010.
- **Industry adoption**: healthcare/EHR (Epic, MEDITECH, VistA, InterSystems), laboratory, financial services, library automation, space (ESA Gaia), government/military, veterinary.
- **Implementations**: 20+ commercial and open-source dialects (GT.M, YottaDB, Cache, IRIS, FreeM, and historical DEC/DataTree/Micronetics systems).
- **Correction of prior research**: library automation did NOT bypass MUMPS; Dataphase, OCLC LS/2 and LS/2000, and VUBIS used it explicitly.

## Methodology

Citation pearl growing: each authoritative source (a "pearl") seeds further
sources. Classification: primary pearls (20), secondary (55+), tertiary (65+).
See `references/bibliography.md` for the full annotated list.

## License

Research content is licensed under **CC BY-NC 4.0** — see the repository root
[LICENSE-DETAILS.md](../../LICENSE-DETAILS.md).
