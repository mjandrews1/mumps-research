# Bibliography: Machine-Assisted C/C++ -> Kotlin Translation Study

**Scientific strand of the mumps-research project.**
**Created:** 2026-11-15 by pruning a copy of references/bibliography.md
(commit 8abb800). The original file is unchanged and remains the
bibliography of the historical/industry research strand.

---

## Purpose and scope

This bibliography serves the scientific study: machine-assisted
translation of the Mumps II Interpreter-Compiler Library (compiler in C,
interpreter in C++) to Kotlin across five arms (BASE, DOC, PRO, DAF,
BOTH). It contains only the subject-side literature from the original
bibliography: the study subject itself, MUMPS language semantics and
standards, MUMPS implementation literature, legacy-language engineering
and modernization prior art, and formal-methods context for the
verification arms.

The peer-reviewed translation/verification literature (IEEE-numbered
[1]-[26]) lives in LITERATURE_REVIEW.md and is complementary to this
file, not duplicated by it.

Entries are numbered S1-S17. Each entry cites the section of
references/bibliography.md it was copied from. The original file has
duplicated entry numbers in several sections (two each of #12-#17,
#70-#74, #106-#108); this copy resolves them by citing section + title.

---

## A. Study Subject and Language Background

### S1. The MUMPS Programming Language
- Author: Kevin S. O'Kane
- Publisher: CreateSpace
- Date: 2008
- ISBN: 978-1440455323
- Summary: Comprehensive book on the MUMPS/M programming language, covering syntax, database features, and practical applications. Often cited in academic research on MUMPS.
- Study relevance: Author of the study subject. The Mumps II Interpreter-Compiler Library (the code being translated) is drawn from the O'Kane distribution; this book is the primary printed reference for the language the translated processor implements.
- Copied from: references/bibliography.md, Books section.

### S2. MUMPS (Wikipedia)
- Title: MUMPS
- URL: https://en.wikipedia.org/wiki/MUMPS
- Summary: Comprehensive history of MUMPS development at Massachusetts General Hospital (MGH), its creators (Neil Pappalardo, Robert A. Greenes, Curt Marble), technical features, and industry applications.
- Study relevance: Background reference for the language's technical features and vocabulary.
- Copied from: Primary Sources and Historical Documents, entry #1.

### S3. MUMPS Syntax (Wikipedia)
- Title: MUMPS syntax
- URL: https://en.wikipedia.org/wiki/MUMPS_syntax
- Summary: Technical details of MUMPS language syntax and standards. ANSI standards in 1977, 1980, 1984, 1990, and 1995.
- Study relevance: Quick-reference for language semantics during translation and oracle-case design.
- Copied from: Standards and Standardization section, "Wikipedia: MUMPS Syntax".

### S4. The Annotated M[UMPS] Standards
- URL: https://mumps-dev.github.io/jsr/Demo/AnnoStd%EF%B9%96Frame=Main.html
- Summary: Comprehensive documentation of MUMPS standards development timeline (1977-1995). Lists all ANSI and ISO standards with dates.
- Study relevance: The standards reference consulted under trigger T3 of the pre-registered capture criteria (wp1/capture_criteria.md) when a translation decision requires the language standard.
- Copied from: Standards and Standardization section, "The Annotated M[UMPS] Standards".

### S5. Hardhats MUMPS Resources
- URL: https://www.hardhats.org/m/Mmain.html
- Summary: Comprehensive MUMPS/M technology resource site maintained by Jacquard Systems Research. Provides a complete definition of the M Standard, past and present, as compiled by Ed de Moel. Includes extensive documentation, tools, and historical information.
- Study relevance: Machine-readable M Standard text; supports semantics checks of translated interpreter behavior against the standard.
- Copied from: Additional References section, entry #71.

---

## B. Formal Analyses of the MUMPS Language and Its Implementation

### S6. A Balanced View of MUMPS
- Authors: Wasserman, A. I., & Sherertz, D. D.
- Journal: ACM SIGPLAN Notices
- Volume: 11(3)
- Pages: 1-12
- Date: March 1976
- DOI: [10.1145/872740.807089](https://doi.org/10.1145/872740.807089)
- URL: https://dl.acm.org/doi/10.1145/872740.807089
- Summary: Seminal evaluation of MUMPS in terms of modern programming language theory. Examines the MUMPS programming system design for shared conversational access to hierarchically-organized databases. Evaluates suitability for producing reliable and maintainable software. Notes that "standard MUMPS is sufficiently rich to implement real-world applications mostly without resorting to vendor-specific extensions."
- Study relevance: The foundational formal analysis of the language whose compiler/interpreter is being translated; frames the semantic properties the translation must preserve.
- Copied from: Academic Publications, 1970s, entry #12 (first occurrence).

### S7. Some Critical Comments Regarding MUMPS
- Authors: Sherertz, D. D., Wasserman, A. I., & Allison, D. R.
- Conference: Proc. 1974 MUMPS Users' Group Meeting
- Pages: 173-190
- Date: 1974
- Source: Biomedical Computer Laboratory, Washington University, St. Louis
- Summary: Early critical analysis of MUMPS from a formal perspective, identifying limitations and areas for improvement in the language design.
- Study relevance: Documents known semantic pitfalls and design critiques of the language; useful when judging whether observed source-code quirks are language properties or implementation bugs (the faithful-quirk ledger).
- Copied from: Academic Publications, 1970s, entry #13 (first occurrence).

### S8. Preliminary MUMPS Language Specification
- Author: Conway, M. E.
- Document: MDC 1/3, Draft Proposal, Revised 7/12/74
- Publisher: MUMPS Development Committee
- Date: 1974 (Revised July 12, 1974)
- Summary: First formal attempt to specify the MUMPS language structure and syntax.
- Study relevance: Earliest formal specification in the lineage of the standard the study subject implements.
- Copied from: MUMPS Development Committee Technical Reports, entry #14 (first occurrence).

### S9. MUMPS Globals and their Implementation
- Authors: Wasserman, A. I., Sherertz, D. D., & Rogerson, C. L.
- Document: MDC 2/1
- Publisher: MUMPS Development Committee
- Date: 1975
- Summary: Formal analysis of the global storage mechanism central to MUMPS database capabilities. Addresses implementation approaches and concurrency considerations.
- Study relevance: The interpreter's global-handling code is a major translation surface; this is the formal treatment of those semantics.
- Copied from: MUMPS Development Committee Technical Reports, entry #15.

### S10. Implementation of the MUMPS Language Standard
- Authors: Wasserman, A. I., & Sherertz, D. D.
- Document: MDC 2/3
- Publisher: MUMPS Development Committee
- Date: 1975
- Summary: Addresses formal implementation of the MUMPS standard across different platforms, providing guidelines for consistent interpretation.
- Study relevance: Directly on-topic for the study: implementing/faithfully re-implementing MUMPS language semantics on a new platform (Kotlin/JVM).
- Copied from: MUMPS Development Committee Technical Reports, entry #16.

### S11. Design of a MUMPS interpreter
- Author: Brown, F. M.
- Journal: Software: Practice and Experience
- Volume: 11(12)
- Pages: 1293-1297
- Date: December 1981
- DOI: [10.1002/spe.4380111206](https://doi.org/10.1002/spe.4380111206)
- Summary: Peer-reviewed paper on the design of a MUMPS interpreter.
- Study relevance: The study subject is a MUMPS interpreter (C++) plus compiler (C); this is the classic peer-reviewed treatment of that exact problem.
- Copied from: Academic Publications, 1980s, entry #12 (second occurrence).

---

## C. Legacy-Language Engineering and Modernization Prior Art

### S12. Open Challenges in Incremental Coverage of Legacy Software Languages
- Author: Vadim Zaytsev
- Conference: Proceedings of the 3rd ACM SIGPLAN International Workshop on Programming Experience (PX/17)
- Date: October 2017
- Location: Vancouver, BC, Canada
- DOI: [10.1145/3167105](https://doi.org/10.1145/3167105)
- URL: https://grammarware.net/text/2017/coverage.pdf
- Summary: Motivates the incremental nature of software language engineering for legacy languages. Outlines challenges including lack of useful documentation, large scale of codebases, counter-intuitive language engineering principles, buggy reference implementations, and fragile workarounds. Relevant to MUMPS as a legacy language with these characteristics.
- Study relevance: The motivating frame for incremental, batch-by-batch language re-implementation; characterizes exactly the hazards the study's corpus exhibits (buggy reference implementation, fragile workarounds).
- Copied from: Academic Publications, 2017 section.

### S13. Leveraging LLMs for Legacy Code Modernization: Challenges and Opportunities for LLM-Generated Documentation
- Authors: Colin Diggs and 20+ co-authors
- Journal: arXiv preprint
- arXiv ID: [2411.14971](https://arxiv.org/abs/2411.14971)
- Date: November 2024
- URL: https://arxiv.org/html/2411.14971v1
- Summary: Investigates LLM utilization for generating documentation for legacy code using two datasets: an electronic health records (EHR) system written in MUMPS (WorldVistA implementation, focusing on Incomplete Records Tracking module) and IBM mainframe Assembly Language Code (ALC). Finds that LLM-generated comments for MUMPS are generally hallucination-free, complete, readable, and useful. Confirms MUMPS shows closer alignment with modern language constructs compared to ALC.
- Study relevance: The most recent academic work on machine-assisted modernization of MUMPS-family code; directly adjacent prior art for the machine-assisted translation study.
- Copied from: Academic Publications, 2024 section.

### S14. Chapter 12 - Veterans Health Administration's VistA MUMPS modernization pilot
- Authors: P. H. Newcomb and R. Couch
- Book: Information Systems Transformation
- Editors: W. M. Ulrich and P. H. Newcomb
- Publisher: Morgan Kaufmann
- Pages: 301-345
- Date: 2010
- ISBN: 978-0123749220
- DOI: [10.1016/B978-0-12-374922-0.00012-9](https://doi.org/10.1016/B978-0-12-374922-0.00012-9)
- Summary: Detailed case study of the VA's VistA MUMPS modernization efforts, including technical challenges, strategies, and outcomes.
- Study relevance: Documented modernization pilot for a large MUMPS codebase; cited by S13 as the key prior modernization case; informs the discussion of migration strategy and risk.
- Copied from: Book Chapters section.

### S15. NextGov: Defense and VA should convert old code to build new record system
- URL: https://www.nextgov.com/cxo-briefing/2009/04/defense-va-should-convert-old-code-to-build-new-record-system/43584/
- Date: April 2009
- Summary: Discussion of converting MUMPS code to Java for VA/DoD systems. "Defense and VA to study converting MUMPS code to Java as a possible inexpensive way to migrate to a new health record system."
- Study relevance: Prior non-academic case of planning machine-assisted migration of the MUMPS codebase family to a modern managed language; useful industry framing for the study's motivation section.
- Copied from: Government and Military Applications section.

---

## D. Formal Methods and Verification Context (DAF / BOTH arms)

### S16. A systematic literature review of the use of formal methods in medical software systems
- Authors: Bonfanti, S., Gargantini, A., & Mashkoor, A.
- Journal: Journal of Software: Evolution and Process
- Volume: 30(5)
- Date: February 2018
- DOI: [10.1002/smr.1943](https://doi.org/10.1002/smr.1943)
- URL: https://onlinelibrary.wiley.com/doi/10.1002/smr.1943
- Summary: Comprehensive systematic review of formal methods applied to medical software and systems. Finds that model checking is the predominant technique, with B tools, MATLAB/Simulink, and UPPAAL being most commonly used. Notes that formal methods are primarily used for modeling, with verification (especially model checking) playing a predominant role.
- Study relevance: Verification-method landscape context for the DAF (Dafny-assisted) and BOTH arms; situates deductive verification among the techniques applied to this software class.
- Copied from: Formal Methods and Verification section, entry #106 (first occurrence).

### S17. Software verification at Bell Labs: One line of development
- Author: Holzmann, G. J.
- Journal: Bell Labs Technical Journal
- Volume: 5(1)
- Pages: 35-45
- Date: August 2002
- DOI: [10.1002/bltj.2206](https://doi.org/10.1002/bltj.2206)
- URL: https://onlinelibrary.wiley.com/doi/10.1002/bltj.2206
- Summary: Historical perspective on software verification techniques at Bell Labs. Examines a line of research that led to widely used verification systems for distributed software. Provides context for the evolution of formal verification methods.
- Study relevance: Historical context for the automated-verification tradition the Dafny toolchain descends from; supports the study's verification-background section.
- Copied from: Formal Methods and Verification section, entry #108 (first occurrence).

---

## Pruning Record

**Source:** references/bibliography.md at commit 8abb800 (2026-11-15), copied
verbatim and pruned. Nothing was added; no entry text was altered.

**Inclusion criteria (all must relate to the scientific study):**

1. Study subject identification (the O'Kane Mumps II library).
2. MUMPS language semantics and standards (T3 external references under
   wp1/capture_criteria.md).
3. MUMPS implementation/interpreter literature (what the translated code
   implements).
4. Legacy-language engineering and modernization prior art.
5. Formal-methods context for the verification arms (DAF, BOTH).

**Removed categories and rationale:**

| Removed category | Approx. entries | Rationale |
|---|---|---|
| Precursor languages (JOSS, TELCOMP, STRINGCOMP, FILECOMP) | 4 | Language pre-history; never consulted for translation decisions. |
| Citation pearl growing methodology (Markey & Atherton 1978; Hawkins & Wagers 1982) | 2 | Methodology of the historical research strand, not of the scientific study. |
| Industry adoption and vendor histories (EHR, laboratory, financial, veterinary, library automation, space, government/military, expert systems, education, insurance) | ~80 | Market and industry evidence; no bearing on translation methodology or the study subject. |
| Modern implementations (GT.M, YottaDB, Caché, IRIS, DSM, MSM, MiniM, M21, FreeM, RSM) | ~15 | Not the study subject; the subject is the O'Kane Mumps II Interpreter-Compiler Library. |
| Tertiary web summaries and blogs | ~10 | Derived, low-authority sources; not citable in a scientific report. |
| Marginally related Scholar Gateway items (real-time functional languages, citation-network agents, zero-downtime updates, student motivation, space MBSE, perinatal/intern systems, Arden Syntax) | ~10 | Captured during pearl-growing expansion; unrelated to translation or verification. |
| Duplicate-numbered standards/vendor entries (second #70-#74, #106-#108 sets) | ~8 | Redundant with kept entries or vendor history. |

**Kept:** 17 of 128 listed entries. Renumbered S1-S17; original duplicate
numbering resolved by citing section + title in each provenance line.

**Maintenance:** New subject-side sources should be appended here with an
S-prefixed ID and a study-relevance line. The translation/verification
peer-reviewed set remains in LITERATURE_REVIEW.md.
