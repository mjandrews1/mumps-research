# Research Proposal: Machine-Assisted C-to-Kotlin Translation

**Project Title:** Comparative Study of Machine-Assisted C-to-Kotlin Translation: A Five-Arm Investigation of Formal Verification (Dafny) and Declarative Heuristic Capture (Prolog)

**Principal Investigator:** Mark Andrews  
**Affiliation:** Flaming Yack Limited Company (Nebraska LLC)  
**Date:** November 13, 2026  
**Target Completion:** May 31, 2028

**Project Type:** Personal Research Project  
**Motivation:** Putting 40 years of progressively responsible experience in IT to the test. This is a matter of personal curiosity, intellectual adventure, self-renewal, and service to others. While aspiring to scholarly rigor, this work is not being conducted for academic credit as part of a formal degree program.

---

## Executive Summary

This research project proposes a five-arm comparative study of approaches to translating Dr. Kevin O'Kane's "Mumps II" compiler from C to Kotlin. The design crosses two factors:

**Factor 1 — Formal verification:** absent or present (Dafny).  
**Factor 2 — Heuristic handling:** ad hoc (logged only) / documented in a written cookbook / captured as reusable, machine-queried Prolog rules.

The third level of Factor 2 exists because "capturing heuristics in Prolog" bundles three distinct ingredients: the discipline of writing heuristics down, the declarative representation, and machine consultation of the rules during translation. The DOC arm isolates the first ingredient so the study can attribute any Prolog effect to the representation and query mechanism rather than to documentation discipline alone.

The five arms (each a distinct translation run):

| Arm | Dafny? | Heuristic handling | Purpose |
|-----|--------|--------------------|---------|
| **BASE** | No | Ad hoc (logged only) | Control; full-codebase translation; produces the frozen test oracle |
| **DOC** | No | Written Markdown cookbook, manually consulted | Isolates documentation discipline |
| **PRO** | No | Prolog rule base, machine-queried | Isolates declarative capture + machine consultation |
| **DAF** | Yes | Ad hoc (logged only) | Verification main effect |
| **BOTH** | Yes | Prolog rule base, machine-queried | Interaction of the two factors |

The design is a **partial 2×3 factorial** (the Dafny×cookbook cell is deliberately not run; it can be added if BOTH reveals an interaction). All arms translate the same pre-specified Study Subset of modules, in orders assigned by a Latin-square counterbalancing scheme so that no factor level is confounded with calendar time or accumulated researcher skill. Four primary contrasts are pre-specified: verification (DAF vs BASE), documentation discipline (DOC vs BASE), declarative mechanism (PRO vs DOC), and interaction ((BOTH − DAF) vs (PRO − BASE)).

The study aims to evaluate the effectiveness, correctness, effort, consistency, and auditability of each approach for translating legacy C code to a modern, type-safe language, and to measure whether a reusable Prolog heuristic base changes the cost or the value of formal verification.

---

## 1. Background and Motivation

### 1.1 The Mumps II Compiler

Dr. Kevin O'Kane's "Mumps II" is the Interpreter-Compiler Library component of his MUMPS distribution: a **compiler written in C** (~8,700 LOC across 21 `compiler-*.c` files plus headers) and an **interpreter written in C++** (~26,000 LOC across `interp-*.cpp` / `libmpscpp.cpp`), implementing the MUMPS (Massachusetts General Hospital Utility Multi-Programming System) programming language. The study therefore covers both C→Kotlin and C++→Kotlin translation; all other components of the distribution tarball are out of scope. Originally developed in the 1960s-1970s, MUMPS is a language designed for healthcare and database applications, with unique features including:

- Hierarchical global storage (sparse arrays)
- Implicit typing
- Pattern matching
- Built-in database operations

### 1.2 Why Translate to Kotlin?

Kotlin is chosen as the target language for several reasons:

1. **Modern Type Safety:** Kotlin's null safety, immutable data structures, and type system
2. **Interoperability:** Seamless Java interoperability (JVM-based)
3. **Industry Adoption:** Widespread use in Android and backend development
4. **Tooling:** Excellent IDE support, static analysis tools
5. **Performance:** Comparable to Java, suitable for compiler implementation

### 1.3 Research Motivation

Legacy C codebases like Mumps II present several challenges:

- **Maintainability:** Difficult for modern developers to understand and modify
- **Safety:** Lack of modern memory safety and type safety features
- **Portability:** Dependence on specific platforms and compilers
- **Tooling:** Limited static analysis and verification capabilities

Machine-assisted translation offers a path to modernize these codebases while preserving their functionality. However, the effectiveness of such translation — particularly when augmented with formal verification, or with a declaratively captured, reusable base of translation heuristics — has not been systematically studied.

### 1.4 Research Gap

While individual case studies exist, there is a lack of:

- **Systematic comparison** of translation approaches (with vs. without formal verification; ad hoc vs. documented vs. machine-queried heuristics)
- **Quantitative metrics** for evaluating translation quality and heuristic reuse
- **Reproducible methodology** for translating legacy C to modern languages
- **Empirical data** on the benefits and costs of formal verification in translation
- **Empirical data** on whether capturing translation heuristics as reusable declarative rules improves consistency, auditability, and effort — and which ingredient of "capture" is responsible

This research aims to fill these gaps. The expanded literature review (see `LITERATURE_REVIEW.md`, revision r3, references [27]–[61]) covers Prolog-based rule systems, logic metaprogramming, and declarative heuristic capture as background for the second factor.

---

## 2. Research Questions

### Primary Research Questions

1. **Translation Feasibility:** To what extent can machine-assisted tools successfully translate the Mumps II compiler from C to Kotlin?

2. **Verification Impact:** How does the use of formal verification (Dafny) affect the correctness, completeness, and effort required for translation? (DAF vs BASE)

3. **Heuristic Capture Impact:** How does capturing translation heuristics declaratively in Prolog (for reuse) affect translation consistency, auditability, effort, and correctness, relative to ad hoc application — and how much of that effect is attributable to documentation discipline alone versus the declarative, machine-consulted representation? (PRO vs BASE; PRO vs DOC; DOC vs BASE)

4. **Factor Interaction:** Do the two factors interact — i.e., does a reusable Prolog heuristic base reduce the effort of Dafny verification, increase its value, or neither? (BOTH vs DAF relative to PRO vs BASE)

5. **Quality Comparison:** What are the measurable differences in code quality (correctness, maintainability, type safety) across the arms?

6. **Effort Analysis:** What is the relative effort (time, complexity, human intervention) required for each arm, and how does effort evolve as the rule base / cookbook matures?

### Secondary Research Questions

7. **Pattern Recognition:** Which C language constructs in Mumps II are most challenging to translate automatically?

8. **Dafny Effectiveness:** How effective is Dafny at identifying and preventing translation errors?

9. **Prolog Suitability:** How well does Prolog (SWI-Prolog) serve as a representation for translation heuristics — expressiveness, queryability, maintainability of the rule base?

10. **Kotlin Suitability:** How well does Kotlin's type system and features support the translation of MUMPS-specific concepts?

11. **Verification Overhead:** What is the performance and resource overhead of using formal verification during translation?

12. **Heuristic Reuse:** What fraction of captured translation rules are reused across modules, and does reuse rate correlate with reduced manual intervention? Does the same hold for cookbook lookups?

13. **Learning Effects:** How much does translator skill improve over the course of the study, and does the counterbalancing scheme successfully remove this from the factor estimates?

---

## 3. Hypotheses

### Primary Hypotheses

**H1 (Baseline coverage):** Machine-assisted translation without verification and without heuristic capture (BASE) will successfully translate 80-90% of the Mumps II compiler's C code, but will require significant manual intervention for complex constructs (pointers, memory management, macros).

**H2 (Verification effect):** Translation with formal verification (DAF) will achieve higher correctness (95%+ functional equivalence on translated portions) but will require 2-3x more effort per module than BASE.

**H3 (Documentation effect):** The cookbook arm (DOC) will show moderately higher across-module consistency than BASE, with modest effort overhead.

**H4 (Declarative mechanism effect):** The Prolog arm (PRO) will show higher consistency and a lower marginal manual-intervention rate in later batches than DOC — i.e., machine consultation of rules adds value beyond writing them down — at the cost of up-front rule-capture effort.

**H5 (Interaction):** BOTH will require the highest total effort of any arm, but the reusable heuristic base will reduce the incremental cost of Dafny verification relative to DAF (the factors are less than additive in effort), and will yield the highest correctness and auditability.

**H6 (Corner comparison):** The verified arms (DAF, BOTH) will have significantly fewer runtime errors and edge cases than the non-verified arms (BASE, DOC, PRO).

### Secondary Hypotheses

**H7:** The most challenging C constructs to translate will be: function pointers, complex macros, manual memory management, and low-level pointer arithmetic.

**H8:** Dafny will be most effective at preventing: null pointer dereferences, buffer overflows, type mismatches, and invariant violations.

**H9:** Kotlin's type system will adequately represent most MUMPS concepts, but may require workarounds for: global storage hierarchies, implicit typing, and certain database operations.

**H10:** Most recurring translation heuristics can be expressed as reusable Prolog rules; the rule base will exhibit a reuse rate above 50% for constructs occurring in three or more modules, and the cookbook will contain a comparable share of the same heuristics (its content is a subset of what the rule base can enforce mechanically).

**H11 (Learning effects):** Per-module effort will decline with researcher experience in every arm; the Latin-square counterbalancing will absorb this trend into the block structure rather than the factor estimates.

---

## 4. Objectives

### 4.1 Primary Objectives

1. **Develop Translation Methodologies:** Create reproducible methodologies for each arm (with/without formal verification × ad hoc/cookbook/Prolog heuristic handling)

2. **Implement Translations:** Complete five translation runs of a pre-specified Study Subset of Mumps II modules (BASE additionally covers the full codebase)

3. **Evaluate Quality:** Quantitatively and qualitatively assess the quality of each translation

4. **Compare Arms:** Estimate the four pre-specified primary contrasts plus the remaining pairwise comparisons, in terms of effort, correctness, consistency, and maintainability

5. **Build and Evaluate a Heuristic Rule Base and Cookbook:** Capture translation heuristics as a reusable, queryable Prolog rule base (PRO, BOTH) and a written cookbook (DOC); measure reuse rate, consistency effect, and maintenance cost of each

### 4.2 Secondary Objectives

6. **Identify Challenges:** Document specific challenges encountered during translation, verification, and heuristic capture

7. **Develop Best Practices:** Establish guidelines for future C-to-Kotlin translation projects, including when declarative heuristic capture pays off

8. **Assess Tool Effectiveness:** Evaluate the effectiveness of machine-assisted tools, Dafny, and SWI-Prolog for this use case

9. **Contribute to Knowledge:** Publish findings to benefit the software engineering, formal methods, and logic programming communities

---

## 5. Methodology Overview

The research follows a **partial factorial comparative experimental design** with five arms. Because all arms translate the same modules, this is a within-subjects design: every module is translated five ways, so module serves as a blocking factor.

```
                      Factor 2: heuristic handling
                  Ad hoc        Cookbook       Prolog rules
                ┌─────────────┬─────────────┬─────────────┐
   Factor 1:  No│  ARM BASE   │  ARM DOC    │  ARM PRO    │
   Dafny        │  (control)  │             │             │
                ├─────────────┼─────────────┼─────────────┤
             Yes│  ARM DAF    │ (not run)   │  ARM BOTH   │
                │             │             │             │
                └─────────────┴─────────────┴─────────────┘
```

The Dafny×cookbook cell is deliberately omitted: DOC exists only to explain the PRO effect, and the interaction question concerns verification combined with the machine-consulted rule base. If BOTH reveals an interaction, the omitted cell becomes an optional follow-up.

### 5.1 Arm BASE: Direct Machine-Assisted Translation (control)

**Steps:**
1. Preprocessing: Clean and normalize C source code (shared, identical in all arms)
2. Static Analysis: Use tools to analyze code structure and dependencies
3. Automated Translation: Apply machine-assisted translation tools
4. Manual Intervention: Fix issues, handle edge cases, adapt to Kotlin idioms — every heuristic decision is **logged** (construct, decision, rationale) but applied ad hoc
5. Testing: Verify functional equivalence through testing
6. Refinement: Iteratively improve based on test results

**Distinct actions:** BASE is the only arm targeting full-codebase coverage; its characterization tests, frozen before other arms begin work, serve as the common oracle for all arms. Its heuristic log seeds the DOC cookbook and the PRO rule base (from the pilot batch only).

**Tools:** Clang-based tools (AST analysis), Tree-sitter (syntax parsing), custom translation scripts, Kotlin compiler, JUnit/Kotlin Test.

### 5.2 Arm DOC: Documented Heuristic Cookbook

Identical workflow to BASE, except that recurring translation heuristics are **written down** in a structured Markdown cookbook (construct pattern → Kotlin target → rationale → conditions), and the cookbook is **manually consulted** before each manual decision. The cookbook captures the same heuristics, under the same pre-registered capture criteria, as the PRO rule base — differing only in representation and in who does the lookup (human vs. machine). Cookbook lookups are logged with the same instrumentation as Prolog rule hits, so reuse rates are comparable.

### 5.3 Arm PRO: Declarative Heuristic Capture in Prolog

Identical workflow to BASE, except that:

1. **Canonicalize:** Normalize the C construct (whitespace, macro expansion, type simplification) so heuristics attach to canonical patterns, not surface syntax (Newman et al. [36])
2. **Capture:** Express each heuristic as a Prolog rule separating reusable translation rules from one-off translation strategies (rule/strategy distinction per Balland [34]; Drabent [60])
3. **Consult:** During translation, query the rule base for each construct before making a manual decision; if a rule matches, apply and record reuse
4. **Audit:** Every translated construct is traceable to either a Prolog rule (with rationale) or a logged one-off decision

**Tools:** SWI-Prolog, custom query scripts integrated into the translation pipeline, rule-base analysis tooling (reuse counts, coverage queries).

### 5.4 Arms DAF and BOTH: Translation with Formal Verification

**DAF** follows the BASE workflow with simultaneous Dafny verification: build formal specifications while translating, prove properties, fix verification failures, retest. Heuristics remain ad hoc (logged).

**BOTH** follows the DAF workflow with the Prolog rule base of Section 5.3: rule hits constrain the Kotlin form before verification begins, and verification outcomes feed back into the rule base (a rule whose target repeatedly fails verification is amended, with the amendment recorded), making the rule base verification-aware.

**Tools:** All tools from BASE, plus the Dafny theorem prover and its specification language.

### 5.5 Counterbalancing and Study Subset

- **Study Subset:** After preprocessing, modules are stratified by complexity (simple / core / complex-pointer-heavy) and grouped into ~10 balanced batches. All five arms translate the entire Study Subset; BASE alone also translates the remainder of the codebase (documented as non-study work).
- **Latin-square order assignment:** Each arm translates the batches in a different cyclic order (arm k starts at batch k), so each batch appears in each position exactly once across arms. Researcher learning therefore affects all arms symmetrically and is absorbed by the batch position term in analysis rather than by any factor level.
- **Frozen oracle:** Characterization tests derived from the C baseline are fixed and version-controlled before any arm begins; all arms are graded against the same unchanging oracle.
- **Micro-replication:** In each arm, 3 randomly drawn modules are re-translated a second time near the end of the study, providing an honest within-arm variance estimate.

---

## 6. Expected Outcomes and Deliverables

### 6.1 Academic Deliverables

1. **Research Papers:**
   - Main paper: "Comparative Study of Machine-Assisted C-to-Kotlin Translation: Formal Verification and Declarative Heuristic Capture in a Five-Arm Design"
   - Supporting papers on specific aspects (tool evaluation, heuristic rule base design, challenges, etc.)

2. **Technical Reports:**
   - Methodology documentation (all arms)
   - Translation guide for C-to-Kotlin
   - Dafny verification guide for legacy code
   - Prolog heuristic-capture guide for translation projects

3. **Presentations:**
   - Conference presentations
   - Workshop tutorials
   - Community talks

### 6.2 Technical Deliverables

1. **Source Code:**
   - Mumps II compiler in original C (baseline)
   - Kotlin translations from all five arms (Study Subset; BASE also full codebase)
   - Test suites for all versions

2. **Heuristic Knowledge Artifacts:**
   - SWI-Prolog rule base with provenance (from PRO and BOTH)
   - Heuristic cookbook (from DOC)
   - Heuristic decision logs (from BASE and DAF)
   - Reuse, coverage, and consistency analyses

3. **Tools and Scripts:**
   - Translation automation scripts
   - Verification scripts and specifications
   - Prolog query/audit tooling
   - Analysis tools and utilities

4. **Documentation:**
   - Translation process documentation
   - Verification methodology documentation
   - Heuristic capture methodology documentation
   - User guides and best practices

### 6.3 Data Deliverables

1. **Metrics Database:**
   - Translation effort metrics (time, lines of code, manual interventions, capture/lookup time)
   - Quality metrics (defects, test coverage, verification results)
   - Consistency and reuse metrics (rule/cookbook reuse rate, decision consistency)
   - Performance metrics (execution time, memory usage)
   - Learning-curve data (batch position vs. effort)

2. **Comparison Analysis:**
   - Statistical comparison of all arms and the pre-specified contrasts
   - Cost-benefit analysis of verification, of heuristic capture (and its ingredients), and of their combination
   - Recommendations for future projects

---

## 7. Significance and Impact

### 7.1 Academic Significance

This research will contribute to:

- **Software Engineering:** Empirical data on translation approaches, formal verification, and declarative knowledge capture — including a rare controlled decomposition of *why* knowledge capture helps
- **Formal Methods:** Practical application of Dafny to legacy code modernization
- **Logic Programming:** A documented, reusable case study of Prolog as a heuristic knowledge base in a real translation project
- **Programming Languages:** Insights into C-to-Kotlin translation patterns
- **Software Maintenance:** Evidence-based recommendations for legacy system modernization

### 7.2 Practical Significance

Benefits for practitioners:

- **Guidance:** Evidence-based recommendations for choosing translation approaches, including whether to invest in a reusable heuristic base and in what form
- **Methodology:** Reproducible methodologies for legacy code modernization
- **Tool Evaluation:** Assessment of machine-assisted translation, verification, and logic programming tools
- **Best Practices:** Guidelines for translating C to Kotlin specifically

### 7.3 Broader Impact

- **Industry:** Help organizations modernize legacy C codebases more effectively
- **Education:** Training materials and case studies for software engineering education
- **Open Source:** Contributions to open source tools and methodologies, including a reusable translation heuristic rule base
- **Standards:** Potential influence on software modernization standards

---

## 8. Scope and Limitations

### 8.1 Scope

**Included:**
- Mumps II compiler C source code (full translation in BASE; Study Subset in all arms)
- Five arms crossing two factors
- Comprehensive testing and validation against a frozen oracle
- Quality, effort, consistency, reuse, and learning-curve metrics
- Comparative analysis with pre-specified contrasts

**Excluded:**
- Other MUMPS implementations (focus on Mumps II only)
- Translation to other languages (Kotlin only)
- Runtime performance optimization (correctness focus)
- Full reimplementation from scratch (translation focus)
- Automated learning of Prolog rules (rules are hand-captured; rule mining is future work)
- The Dafny×cookbook cell (optional follow-up if an interaction is found)

### 8.2 Limitations

1. **Single Case Study:** Results may not generalize to all C codebases
2. **Single Researcher:** All arms translated by the same person; counterbalancing mitigates but cannot eliminate individual learning effects
3. **Tool Dependencies:** Reliance on specific tools (Clang, Dafny, SWI-Prolog, etc.)
4. **Expertise:** Researcher's learning curve for formal verification and logic programming
5. **Time Constraints:** Part-time research over an extended period
6. **Resource Constraints:** Limited computational resources for verification
7. **Heuristic Subjectivity:** What counts as a "reusable heuristic" involves judgment; mitigated by pre-registered capture criteria and auditable rule base / cookbook
8. **Pseudo-replication:** With one researcher and one codebase, per-module measures are the unit of analysis; inferential tests are exploratory support rather than definitive (micro-replication partially addresses this)

---

## 9. Feasibility

### 9.1 Researcher Qualifications

- **Domain Knowledge:** Extensive knowledge of MUMPS/M language and history
- **Research Experience:** Experience with systematic literature review (Citation Pearl Growing)
- **Technical Background:** Familiarity with C, growing knowledge of Kotlin, prior exposure to Prolog and logic programming concepts
- **Learning Capacity:** Demonstrated ability to learn new technologies (Dafny, formal methods, SWI-Prolog)

### 9.2 Resource Availability

- **Source Code:** Mumps II compiler C source available
- **Tools:** Clang, Dafny, SWI-Prolog, Z3, Kotlin compiler, and development tools accessible (all installable via Homebrew)
- **Computing:** Sufficient computing resources for translation and verification
- **Time:** Concentrated 18-month timeline (Nov 2026 - May 2028) with intensive effort (~20-25 hours/week)

### 9.3 Methodological Feasibility

- **BASE/DOC:** Well-established machine-assisted translation techniques; the cookbook is a documentation discipline, not a technical risk
- **PRO/BOTH:** SWI-Prolog is mature and well-documented; rule capture is a structured documentation discipline with modest tooling needs
- **DAF/BOTH:** Dafny has been successfully applied to C verification (via CIVL and other tools)
- **Counterbalancing:** The Latin-square scheme requires only that batches be defined in advance and orders pre-assigned; no special tooling
- **Analysis:** Factorial and blocked statistical methods well-understood

---

## 10. Timeline Overview

The 18-month timeline interleaves all five arms rather than running them in sequential blocks, so that researcher learning affects all arms symmetrically:

| Phase | Duration | Activities |
|-------|----------|------------|
| **Preparation** | Months 1-2 (Nov-Dec 2026) | Tool setup (Kotlin, Dafny, SWI-Prolog, Z3); obtain Mumps II source; module stratification and batch definition; Latin-square arm-order assignment; create and freeze the characterization-test oracle; pre-register capture criteria |
| **Pilot** | Month 3 (Jan 2027) | Translate the pilot batch in all five arms; seed cookbook and rule base; refine instrumentation |
| **Interleaved Translation** | Months 4-14 (Feb 2027-Jan 2028) | Monthly cycles: each arm advances one batch through its pre-assigned order; BASE additionally translates remaining non-study modules |
| **Micro-replication & Testing** | Months 15-16 (Feb-Mar 2028) | Re-translate 3 random modules per arm; comprehensive testing of all arms against the frozen oracle |
| **Comparative Analysis** | Month 17 (Apr 2028) | Data collection, contrast estimation, findings documentation |
| **Final Documentation** | Month 18 (May 2028) | Write research report, clean up code, finalize deliverables |

**Total Duration:** 18 months (November 12, 2026 - May 31, 2028)  
**Effort Estimate:** ~20-25 hours/week average (intensive but manageable for a personal project)

---

## 11. Risk Assessment and Mitigation

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| Tool incompatibility | Medium | High | Test tools early, have alternatives |
| Verification complexity | High | High | Start with small batches, build expertise; reduce DAF/BOTH subset if needed |
| Rule base / cookbook design difficulty | Medium | Medium | Pre-registered capture criteria; seed from pilot batch; iterate |
| Capture overhead exceeds benefit | Medium | Medium | Track capture vs. reuse effort from Month 3; a null or negative result is itself a publishable finding |
| Interleaving discipline breaks down (arms drift sequential) | Medium | High | Monthly cycle review; batch order assignments are pre-committed; deviation is logged as data |
| Translation errors | High | Medium | Frozen oracle, comprehensive testing, iterative refinement |
| Time overrun | Medium | Medium | Study Subset is pre-specified and can be trimmed; BASE full coverage is the only elastic element |
| Resource limitations | Low | Medium | Use cloud resources if needed |
| Source code issues | Medium | Medium | Analyze code quality early, preprocess as needed |

---

## 12. References

### Primary Source
- O'Kane, Kevin. "Mumps II Compiler" - C source code

### Background References
- MUMPS Language Standards (ANSI X11.1, ISO 11756)
- Kotlin Language Documentation
- Dafny Theorem Prover Documentation
- Leino, K. Rustan M. "Dafny: An Automatic Program Verifier for Functional Correctness"
- SWI-Prolog Documentation
- See `LITERATURE_REVIEW.md` (revision r3), especially:
  - Section 6 (Prolog and declarative heuristic capture, refs [27]–[38], incl. Balland [34], Newman et al. [36])
  - Section 6.6 (arXiv preprints, refs [39]–[60], incl. Drabent [60])
  - Section 10 (monographs, refs [62]–[68])

### Related Work
- C-to-Java translation studies
- Legacy code modernization case studies
- Formal verification of compilers
- Machine-assisted translation tools
- Rule-based and logic-metaprogramming systems

---

## 13. Appendix: Glossary

- **MUMPS:** Massachusetts General Hospital Utility Multi-Programming System - a programming language
- **Mumps II:** Dr. Kevin O'Kane's implementation of MUMPS in C
- **Kotlin:** Modern, statically-typed programming language for the JVM
- **Dafny:** Automatic program verifier for functional correctness
- **Prolog / SWI-Prolog:** Logic programming language / its leading open-source implementation, used here as the declarative rule base for translation heuristics
- **Heuristic capture:** Recording a translation heuristic as a reusable, auditable rule rather than applying it informally
- **Arm:** A distinct translation run combining specific factor levels (this study has five arms)
- **Contrast:** A pre-specified comparison between arms; four primary contrasts are defined
- **Partial factorial design:** Experimental design crossing factors where not every factor-level combination is run
- **Latin square / counterbalancing:** Assignment of treatment orders so each condition appears in each position exactly once, removing order/learning confounds
- **Study Subset:** The pre-specified set of modules translated by every arm
- **Oracle freeze:** Fixing the characterization tests before translation begins so all arms are graded identically
- **Formal Verification:** Mathematical proof that software meets its specification
- **Machine-Assisted Translation:** Use of tools to automate parts of code translation

---

*Document Version: 1.2*  
*Last Updated: November 13, 2026*  
*Repository: /home/mark/projects/mumps-research*

---

*Change log (v1.0 → v1.1):* Expanded from a two-arm (A/B) design to a two-factor, six-arm factorial design adding declarative heuristic capture in Prolog as Factor 2.

*Change log (v1.1 → v1.2):* Restructured the arms for validity. Arms 1/3/5 (previously all the same baseline cell) replaced by five genuinely distinct runs: BASE, DOC, PRO, DAF, BOTH. Added the DOC arm (Markdown heuristic cookbook) to decompose the heuristic-capture effect into documentation discipline vs. declarative machine-consulted representation. Replaced sequential arm scheduling with Latin-square counterbalanced, interleaved cycles (removes learning/time confound). Added frozen-oracle requirement, pre-registered capture criteria, rule-base maturity as a measured covariate, and micro-replication (3 re-translated modules per arm) for within-arm variance. Updated research questions (added Q13), hypotheses (renumbered; added H3 documentation effect, H11 learning effects), timeline, risks, and glossary accordingly.

*Correction (v1.2a, 2026-11-13):* §1.1 updated — the study subject is the Mumps II **Interpreter-Compiler Library** component of the O'Kane distribution (compiler in C, interpreter in C++), not the whole tarball; the interpreter being C++ means the study covers C→Kotlin **and** C++→Kotlin translation. Matched change in the design document (§2, version history 1.2a).

*Update (v1.2b, 2026-11-14):* The dual-language scope is embraced as a pre-registered subgroup dimension: source-language (`lang`) subgroup analyses S1–S3 added to the design document §11.2. Primary contrasts remain pooled; subgroup results are reported regardless of outcome.
