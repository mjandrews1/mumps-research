# Research Design: Machine-Assisted C-to-Kotlin Translation

**Project:** Comparative Study of Machine-Assisted C-to-Kotlin Translation: A Five-Arm Investigation of Formal Verification (Dafny) and Declarative Heuristic Capture (Prolog)  
**Document Type:** Research Design  
**Version:** 1.2  
**Date:** November 13, 2026  
**Author:** Mark Andrews

**Project Type:** Personal Research Project  
**Note:** This is a matter of personal curiosity, intellectual adventure, self-renewal, and service to others. The researcher brings 40 years of progressively responsible IT experience to this endeavor, aspiring to scholarly rigor while not conducting this work for academic credit.

---

## Table of Contents

1. [Study Design Overview](#1-study-design-overview)
2. [Subject of Study: Mumps II Interpreter-Compiler Library](#2-subject-of-study-mumps-ii-interpreter-compiler-library)
3. [Arm BASE: Direct Machine-Assisted Translation](#3-arm-base-direct-machine-assisted-translation)
4. [Arm DOC: Documented Heuristic Cookbook](#4-arm-doc-documented-heuristic-cookbook)
5. [Arms PRO and BOTH: Declarative Heuristic Capture in Prolog](#5-arms-pro-and-both-declarative-heuristic-capture-in-prolog)
6. [Arm DAF: Dafny-Verified Translation](#6-arm-daf-dafny-verified-translation)
7. [Methodology Common to All Arms](#7-methodology-common-to-all-arms)
8. [Detailed Work Packages](#8-detailed-work-packages)
9. [Quality Assurance](#9-quality-assurance)
10. [Data Collection and Metrics](#10-data-collection-and-metrics)
11. [Analysis Framework](#11-analysis-framework)
12. [Timeline and Milestones](#12-timeline-and-milestones)
13. [Resource Requirements](#13-resource-requirements)
14. [Risk Management](#14-risk-management)
15. [Success Criteria](#15-success-criteria)
16. [Conclusion](#16-conclusion)

---

## 1. Study Design Overview

### 1.1 Design Type

**Partial 2×3 factorial comparative experimental design** with five arms (five distinct translation runs):

| Factor | Levels |
|--------|--------|
| **Factor 1: Formal verification** | Absent / Present (Dafny) |
| **Factor 2: Heuristic handling** | Ad hoc (logged only) / Written cookbook (manually consulted) / Prolog rules (machine-consulted) |

| Arm | Dafny? | Heuristic handling | Runs on |
|-----|--------|--------------------|---------|
| **BASE** | No | Ad hoc, logged | Study Subset + full codebase |
| **DOC** | No | Markdown cookbook, manually consulted | Study Subset |
| **PRO** | No | Prolog rule base, machine-queried | Study Subset |
| **DAF** | Yes | Ad hoc, logged | Study Subset |
| **BOTH** | Yes | Prolog rule base, machine-queried | Study Subset |

The Dafny×cookbook cell is deliberately not run. The DOC arm exists solely to decompose the heuristic-capture effect: any PRO-vs-BASE difference could be due to (a) the discipline of writing heuristics down, (b) the declarative representation, or (c) machine consultation of the rules during translation. DOC holds (a) constant without (b) and (c), so PRO vs DOC isolates the representation-plus-consultation mechanism, and DOC vs BASE measures documentation discipline alone. If BOTH reveals an interaction, the omitted cell becomes an optional follow-up.

**Pre-specified primary contrasts:**

1. **Verification effect:** DAF vs BASE (H2)
2. **Documentation discipline:** DOC vs BASE (H3)
3. **Declarative mechanism:** PRO vs DOC (H4)
4. **Interaction:** (BOTH − DAF) vs (PRO − BASE) (H5)

The remaining pairwise contrasts are reported as secondary.

This is a **within-subjects design**: every arm translates the same modules, so module serves as a blocking factor and each module provides a matched observation per arm.

### 1.2 Independent and Dependent Variables

**Independent Variables:**
- Formal verification (No vs. Yes — Dafny)
- Heuristic handling (ad hoc / cookbook / Prolog rules)

**Dependent Variables:**
- Translation correctness (functional equivalence against the frozen oracle)
- Code quality metrics (complexity, maintainability, defects)
- Translation-decision consistency (same construct → same translation across modules)
- Effort metrics (time, manual interventions, iterations, capture/lookup time)
- Resource usage (CPU, memory, verification time)
- Heuristic reuse rate (fraction of constructs resolved by rule or cookbook entry)
- Auditability (fraction of constructs traceable to a recorded rule/entry)
- Developer experience (subjective feedback)

**Measured covariates (not treatments):**
- Batch position (researcher learning / fatigue)
- Rule-base / cookbook maturity at time of translation
- Module complexity stratum

### 1.3 Research Paradigm

**Mixed Methods:** Combining quantitative and qualitative approaches

- **Quantitative:** Metrics collection, contrast estimation, performance measurements
- **Qualitative:** Challenge documentation, pattern identification, best practices

**Approach:** Sequential explanatory design - collect quantitative data first, then use qualitative data to explain findings

### 1.4 Validity Safeguards (design-critical)

1. **Latin-square counterbalancing:** Each arm translates the Study Subset batches in a different pre-assigned cyclic order (Section 12), so no factor level is confounded with calendar time or accumulated researcher skill. Order assignments are committed in writing before the pilot and are not revised thereafter.
2. **Frozen oracle:** Characterization tests derived from the C baseline are fixed and version-controlled before any arm begins; all arms are graded against the same unchanging oracle.
3. **Pre-registered capture criteria:** What qualifies as a "reusable heuristic" is documented before the pilot; the DOC cookbook and the PRO rule base are held to identical criteria.
4. **Micro-replication:** In each arm, 3 randomly drawn modules are re-translated a second time near the end of the study, giving an honest within-arm variance estimate.
5. **Rule-base provenance:** The rule base is seeded from the pilot batch only; thereafter it evolves only through PRO/BOTH work, with every change versioned and dated. Rule-base maturity per module is logged as a covariate.

---

## 2. Subject of Study: Mumps II Interpreter-Compiler Library

### 2.1 Overview

**Name:** Mumps II Interpreter-Compiler Library (the "Mumps II compiler" of this study)  
**Author:** Dr. Kevin O'Kane  
**Language:** Compiler in C; interpreter in C++  
**Purpose:** Implementation of the MUMPS programming language
**Source:** O'Kane distribution tarball, component
`mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library/` (the
tarball ships several other components — e.g. the legacy Mumps interpreter —
which are explicitly out of scope)

### 2.2 Source Code Characteristics

| Aspect | Details |
|--------|---------|
| **Language** | Compiler: C; Interpreter: C++ (the study therefore covers C→Kotlin *and* C++→Kotlin translation; units are language-tagged to enable per-language analysis) |
| **Size** | Compiler ~8,700 LOC across 21 `compiler-*.c` files (largest: `compiler-parse.c` 2,508; `compiler-mumps.c` 1,998; `compiler-sys1.c` 1,091) plus `interp-bmgsubs.c` (380) and `rdbms-if.c` (388); interpreter ~26,000 LOC in C++ (`libmpscpp.cpp` 10,577; `interp-interp.cpp` 2,618; `interp-zfcns.cpp` 2,524; etc.); 41 headers (~4,700 LOC) under `include/mumpsc/` |
| **Modules** | 48 top-level C/C++ source files + 41 headers; autoconf `*.in` templates and packaging payloads (`usr/`, `DEBIAN/`, SVN `Checkout/` demos) excluded from the study corpus |
| **Complexity** | Medium to high (compiler implementation; large C++ interpreter library) |
| **Features** | Lexical analysis, parsing, code generation, interpreter runtime, GTK wrapper layer, RDBMS interface |
| **Dependencies** | Standard C/C++ libraries; PCRE (vendored); optional GTK |

### 2.3 Key C Constructs to Handle

Based on typical compiler implementations, expected challenges include:

1. **Pointers and Memory Management**
   - Pointer arithmetic
   - Dynamic memory allocation (malloc, free)
   - Manual memory management patterns

2. **Data Structures**
   - Abstract syntax trees (AST)
   - Symbol tables
   - Hash tables, linked lists

3. **Control Flow**
   - Complex conditional logic
   - Recursive functions
   - Goto statements (if used)

4. **Preprocessor Directives**
   - Macros
   - Conditional compilation (#ifdef)
   - Include files

5. **Input/Output**
   - File operations
   - Standard I/O
   - Custom formatting

6. **MUMPS-Specific**
   - Global storage representation
   - Implicit typing handling
   - Database operation mapping

### 2.4 Code Preprocessing (shared by all arms)

Before any translation, the following preprocessing steps are applied identically in all arms:

1. **Code Cleanup**
   - Remove compiler-specific extensions
   - Standardize coding style
   - Resolve warnings
   - Remove dead code

2. **Dependency Analysis**
   - Identify all source files and dependencies
   - Create dependency graph
   - Identify external dependencies

3. **Modularization**
   - Group related functions
   - Create clear module boundaries
   - Document module interfaces

4. **Canonicalization** (supports the heuristic arms)
   - Normalize C syntax patterns (whitespace, macro expansion, type simplification) so heuristics attach to canonical constructs rather than surface syntax (Newman et al. [36])

5. **Oracle Creation and Freeze**
   - Build and run the original C code
   - Create characterization tests capturing observed behavior
   - **Freeze and version-control the test suite before any Kotlin translation begins**

6. **Stratification and Batching**
   - Stratify modules by complexity (simple / core / complex-pointer-heavy)
   - Group into ~10 balanced batches (the Study Subset)
   - Pre-assign each arm's batch order by the Latin-square scheme (Section 12.3)

---

## 3. Arm BASE: Direct Machine-Assisted Translation

### 3.1 Overview

**Goal:** Translate the Mumps II compiler from C to Kotlin using machine-assisted tools, with heuristics applied ad hoc.

**Characteristics:**
- Focus on functional equivalence
- Emphasis on automation
- Manual intervention as needed; **every heuristic decision logged** (construct, decision, rationale) but applied informally
- Testing-based validation against the frozen oracle
- Control arm for all contrasts

**Distinct actions:**
- Only arm targeting full-codebase coverage (the remainder beyond the Study Subset is documented as non-study work and excluded from cross-arm analysis)
- Produces the characterization-test oracle (frozen before other arms begin study work)
- Its pilot-batch heuristic log seeds the DOC cookbook and PRO rule base

### 3.2 Methodology

**Phase 1 (with all arms, Months 1-3):** shared preparation, oracle freeze, pilot batch. See Section 7 and Section 12.

**Per-batch workflow:**

1. **Automated Translation**
   - Apply automated tools to the batch
   - Handle straightforward cases automatically
   - Flag complex cases for manual review

2. **Manual Intervention**
   - Resolve pointer-to-array translations
   - Handle memory management (GC vs. manual)
   - Adapt C idioms to Kotlin idioms
   - Fix type mismatches
   - Log every heuristic decision

3. **Iterative Refinement**
   - Run oracle tests after the batch
   - Fix compilation and runtime errors
   - Refactor for Kotlin best practices

**Translation Patterns (starting points):**

| C Construct | Kotlin Equivalent |
|-------------|-------------------|
| `int x;` | `var x: Int` |
| `char* str;` | `String` or `ByteArray` |
| `malloc/free` | Automatic or manual with mutable collections |
| `struct` | `data class` or `class` |
| `typedef` | `typealias` |
| `NULL` | `null` (with null safety) |
| `printf` | `println` or string templates |
| `#define` macros | `const val`, inline functions, or compile-time functions |

**Challenges and Solutions:**

| Challenge | Solution |
|-----------|----------|
| Pointer arithmetic | Use indices, ranges, or custom wrapper classes |
| Manual memory management | Use Kotlin's GC, or implement reference counting |
| Macros | Replace with functions, constants, or inline code |
| Global variables | Use singleton objects or pass as parameters |
| Function pointers | Use function types or interfaces |
| Union types | Use sealed classes or `Any` type |

**Deliverables:**
- Kotlin translation (full codebase)
- Heuristic decision log
- Translation log (challenges, decisions)
- Test results
- Compilation-ready code

---

## 4. Arm DOC: Documented Heuristic Cookbook

### 4.1 Overview

**Goal:** Same workflow as BASE, but recurring translation heuristics are **written down** in a structured Markdown cookbook that is **manually consulted** before each manual decision.

**Characteristics:**
- Isolates documentation discipline from representation and machine consultation
- The cookbook captures the same heuristics, under the same pre-registered criteria, as the PRO rule base
- Cookbook lookups are logged with the same instrumentation as Prolog rule hits, so reuse rates are directly comparable between DOC and PRO

### 4.2 Cookbook Design

**Entry schema (parallel to the Prolog rule schema):**

```markdown
### H-042: Pointer arithmetic to index expression
- Canonical C pattern: pointer_arithmetic(base, offset)
- Kotlin target: index_expression(base_idx + offset)
- Rationale: Kotlin arrays replace C pointer arithmetic
- Conditions: array not null; offset in bounds
- Provenance: batch 3, 2027-03; reused 11 times
```

**Rules of the arm:**
1. Before any manual decision, consult the cookbook
2. If an entry matches, apply it and log the hit (entry id, module, date)
3. If no entry matches, decide manually and log it; if the same decision recurs, capture a new entry under the pre-registered criteria
4. The cookbook is never queried mechanically — the human does the lookup (that is the point of this arm)

**Deliverables:**
- Kotlin translation (Study Subset)
- Heuristic cookbook (versioned, with provenance)
- Lookup/reuse statistics
- Translation log

---

## 5. Arms PRO and BOTH: Declarative Heuristic Capture in Prolog

### 5.1 Overview

**Goal:** Capture translation heuristics declaratively in Prolog for reuse. PRO runs without verification; BOTH combines the rule base with the Dafny workflow of Section 6.

**Characteristics:**
- Every recurring translation decision is expressed as a reusable, queryable Prolog rule
- The rule base is machine-consulted during translation; manual decisions are only made when no rule matches
- Full auditability: every translated construct traces to a rule (with rationale) or a logged one-off decision
- In BOTH, rule hits constrain the Kotlin form before verification begins, and verification outcomes feed back into the rule base

### 5.2 Rule Base Design

**Rule schema (initial; refined during pilot):**

```prolog
% translation_rule(Id, CanonicalCPattern, KotlinTarget, Rationale, Conditions)
translation_rule(r042,
    pointer_arithmetic(base, offset),
    index_expression(base_idx + offset),
    "Kotlin arrays replace C pointer arithmetic",
    [array_not_null, offset_in_bounds]).

translation_rule(r017,
    macro_function_like(name, args),
    inline_function(name, args),
    "Function-like macros become inline functions",
    [no_side_effects_in_args]).
```

**Design principles:**

1. **Canonical patterns:** Rules attach to canonicalized C constructs (Section 2.4), not surface syntax (Newman et al. [36])
2. **Rules vs. strategies:** Reusable translation rules are separated from one-off translation strategies; only genuinely recurring, parameterizable decisions become rules (rule/strategy separation per Balland [34]; Drabent [60])
3. **Provenance:** Each rule records rationale, conditions, the module and date that motivated it, and reuse count
4. **Idempotent capture:** A heuristic is captured only when it has been applied (or considered) more than once
5. **Shared criteria with DOC:** the same pre-registered capture criteria govern the cookbook and the rule base, so the DOC-vs-PRO contrast measures representation and consultation mechanism, not different notions of what to capture

### 5.3 Arm PRO Workflow (per construct)

```
1. Canonicalize the C construct
2. Query the rule base (find_rule/1)
3. If a rule matches:
     apply the rule's Kotlin target
     record reuse (rule id, module, date)
4. If no rule matches:
     make a manual decision and log it (as in BASE)
     if the same decision recurs, capture it as a new rule
5. Run oracle tests for the batch and iterate
```

### 5.4 Arm BOTH Workflow

The DAF workflow (Section 6) with two changes:

1. Step 1 of each function's translation begins with a rule-base query; rule hits constrain the Kotlin form before verification begins
2. Verification outcomes feed back into the rule base: if a rule's target repeatedly fails verification, the rule is amended (with the amendment recorded), making the rule base verification-aware. This feedback loop is itself measured (amendment counts over time)

**Deliverables:**
- Kotlin translations (Study Subset) for PRO and BOTH
- Prolog rule base (documented, versioned, with provenance and maturity log)
- Reuse, consistency, and auditability statistics
- For BOTH: verification-awareness statistics (rule amendments triggered by failed proofs; verification iterations with vs. without rule support, contrasted per-module with DAF)

---

## 6. Arm DAF: Dafny-Verified Translation

### 6.1 Overview

**Goal:** Translate with formal verification using Dafny, with heuristics applied ad hoc (logged, as in BASE).

**Characteristics:**
- Translation with simultaneous verification
- Proof of correctness properties
- Formal specifications
- Verified functional equivalence

### 6.2 Methodology

**Learning path (shared with BOTH, Months 1-3):**

1. **Dafny Basics**
   - Dafny syntax and semantics
   - Specification language
   - Proof tactics and strategies
   - Verification workflow

2. **C Verification with Dafny**
   - CIVL (Concurrent Intermediate Verification Language)
   - Dafny's C integration
   - Verification of C code

3. **Formal Verification Concepts**
   - Preconditions and postconditions
   - Loop invariants
   - Data structure specifications

**Specification types:**

1. **Function Specifications**
   ```dafny
   function Method1(input: int): int
     requires input >= 0
     ensures input <= Method1 <= input * 2
   ```

2. **Data Structure Specifications** — invariants for symbol tables, properties of AST nodes, constraints on global state

3. **Module Specifications** — module-level invariants, interface contracts, integration properties

4. **System Specifications** — overall compiler correctness, soundness and completeness properties, termination guarantees

**Per-batch verified translation process:**

```
For each C function in the batch:
1. Translate to Kotlin
2. Add formal specifications
3. Verify with Dafny
4. Fix verification failures
5. Repeat until verified or the module is reclassified as "verification-deferred"
```

**Verification strategies:**
- **Loop Invariants:** For iterative algorithms
- **Pre/Post Conditions:** For function contracts
- **Data Invariants:** For data structure integrity
- **Termination Measures:** For recursion and loops

**Handling challenges:**
- **Pointers:** Use Dafny's heap model
- **Memory Management:** Verify memory safety properties
- **Macros:** Unfold or abstract macros
- **Global State:** Use module-level specifications

**Translation patterns with verification:**

| C Construct | Kotlin + Dafny |
|-------------|----------------|
| Pointer dereference | Use `requires` for non-null, `ensures` for valid state |
| Memory allocation | Verify allocation succeeds, track ownership |
| Array indexing | Use `requires 0 <= i < arr.Length` |
| Function calls | Check preconditions, rely on postconditions |

**Deliverables:**
- Kotlin translation (Study Subset) with specifications
- Verification logs
- Proof obligations and resolutions
- Verification metrics (conditions count, success rate, time per module, iterations per function)

---

## 7. Methodology Common to All Arms

### 7.1 Code Structure Analysis

All arms use the same code structure analysis to ensure comparability.

**Analysis Tools:** Clang-based static analysis, custom analysis scripts, dependency graph generators

**Analysis Deliverables:** Function call graphs, data flow diagrams, control flow graphs, complexity metrics (cyclomatic, Halstead, etc.)

### 7.2 Kotlin Target Specification

All arms target the same Kotlin version and style.

**Kotlin Version:** Latest stable version at project start  
**Style Guide:** Kotlin official style guide  
**Conventions:** Use idiomatic Kotlin; prefer immutable data; use null safety; follow functional programming patterns where appropriate

### 7.3 Testing Framework

All arms use the same testing framework and the same frozen oracle.

**Testing Tools:** JUnit 5 (unit testing), Kotest (property-based testing), JaCoCo (coverage), custom test comparators

**Test Categories:** Unit, integration, system, regression, property tests — all derived from or extending the frozen characterization suite

### 7.4 Batch Discipline

All arms translate the same Study Subset batches, one batch per monthly cycle, each in its pre-assigned order (Section 12.3). Per-batch metrics are recorded identically in every arm so that batch = block in the analysis. Deviations from the pre-assigned order (unavoidable rework, blocked progress) are logged as data, not silently repaired.

### 7.5 Metrics Collection

All arms collect the same core metrics (Section 10).

---

## 8. Detailed Work Packages

### WP1: Preparation and Learning (Months 1-2: Nov-Dec 2026)

**Objective:** Prepare shared infrastructure and learn necessary technologies

**Tasks:**
- [ ] Obtain and analyze Mumps II C source code
- [ ] Set up development environment (Kotlin, Dafny, SWI-Prolog, Z3)
- [ ] Learn Kotlin basics; begin Dafny and Prolog familiarization
- [ ] Select and configure tools
- [ ] Preprocess and canonicalize the C source (Section 2.4)
- [ ] Build and run the C baseline; create characterization tests; **freeze the oracle**
- [ ] Stratify modules; define ~10 Study Subset batches
- [ ] **Commit Latin-square arm order assignments in writing**
- [ ] Pre-register heuristic capture criteria (shared by DOC and PRO)
- [ ] Randomly draw 3 micro-replication modules per arm (drawn now, translated at the end)

**Deliverables:** Environment setup documentation; frozen oracle; stratification and batch plan; committed order assignments; capture criteria document

**Effort:** ~100-120 hours

### WP2: Pilot Batch in All Arms (Month 3: Jan 2027)

**Objective:** Translate the pilot batch five ways; seed knowledge artifacts; refine instrumentation

**Tasks:**
- [ ] Translate the pilot batch in BASE, DOC, PRO, DAF, BOTH
- [ ] Seed the cookbook and the rule base from the pilot heuristic log (identical criteria)
- [ ] Build and test Prolog query tooling: `find_rule/1`, `record_reuse/2`, `coverage_report/0`
- [ ] Instrument cookbook lookup logging
- [ ] Calibrate per-batch time tracking
- [ ] Refine rule schema and cookbook entry format

**Deliverables:** Pilot translations (5 arms); cookbook v0; rule base v0; instrumentation report

**Effort:** ~80-100 hours

### WP3: Interleaved Study-Subset Translation (Months 4-14: Feb 2027-Jan 2028)

**Objective:** Translate the Study Subset in all five arms under Latin-square counterbalancing

**Tasks (recurring monthly cycle):**
- [ ] Advance each arm by one batch in its pre-assigned order (BASE, DOC, PRO, DAF, BOTH each touch every batch over the study)
- [ ] Rotate which arm leads the cycle (so "fresh attention" is balanced across arms)
- [ ] Record per-batch metrics identically in all arms (time, interventions, reuses, verification stats)
- [ ] DOC: maintain cookbook; log lookups
- [ ] PRO/BOTH: maintain rule base; log hits/misses and maturity
- [ ] DAF/BOTH: verify each batch before closing it
- [ ] BASE: additionally translate non-study remainder modules (excluded from cross-arm analysis)
- [ ] Monthly cycle review: deviations logged, order assignments unchanged

**Deliverables:** Study Subset translations × 5; knowledge artifacts (cookbook, rule base) with growth curves; per-batch metrics database

**Effort:** ~900-1,000 hours

### WP4: Micro-replication and Completion (Months 15-16: Feb-Mar 2028)

**Objective:** Within-arm variance estimation and completion of testing

**Tasks:**
- [ ] Re-translate the 3 pre-drawn modules per arm (second pass; recorded separately)
- [ ] Complete any deferred batches; log deviations
- [ ] Comprehensive testing of all five translations against the frozen oracle
- [ ] Differential testing vs. the C baseline
- [ ] Mutation testing of the test suite

**Deliverables:** Micro-replication data; test results per arm; functional equivalence assessments

**Effort:** ~150-180 hours

### WP5: Analysis (Month 17: Apr 2028)

**Objective:** Estimate contrasts and interpret findings

**Tasks:**
- [ ] Assemble the metrics database; verify completeness
- [ ] Estimate the four primary contrasts (and secondary pairwise contrasts)
- [ ] Fit blocked models (module as block; batch position and knowledge-artifact maturity as covariates)
- [ ] Learning-curve analysis (H11): confirm counterbalancing removed order effects
- [ ] Consistency and reuse analyses across arms
- [ ] Qualitative synthesis: challenges, patterns, lessons

**Deliverables:** Comparative analysis report; statistical analysis; findings documentation

**Effort:** ~100-120 hours

### WP6: Final Documentation (Month 18: May 2028)

**Objective:** Finalize project documentation and deliverables

**Tasks:**
- [ ] Write research report
- [ ] Clean up and organize code, cookbook, and rule base
- [ ] Finalize documentation
- [ ] Archive project materials
- [ ] Open source code (if possible)

**Deliverables:** Research report; complete code repository; project documentation; summary of findings

**Effort:** ~80-100 hours

---

## 9. Quality Assurance

### 9.1 Quality Control Measures

1. **Code Reviews**
   - Regular code reviews of translations
   - Peer review of specifications and proofs
   - Peer review of cookbook and rule base design
   - External validation of results

2. **Testing**
   - Single frozen oracle for all arms
   - Comprehensive test coverage
   - Automated test execution
   - Continuous integration

3. **Verification**
   - Formal verification (DAF, BOTH)
   - Static analysis
   - Property checking

4. **Knowledge Artifact Quality (DOC, PRO, BOTH)**
   - Every rule/entry carries rationale and conditions
   - Periodic review of cookbook and rule base for contradictions and redundancy
   - All changes versioned with provenance
   - DOC and PRO held to identical capture criteria

5. **Design Integrity**
   - Order assignments committed before the pilot and never revised
   - Deviations from protocol logged as data
   - Oracle frozen before any translation

6. **Documentation**
   - Complete documentation of all steps
   - Decision rationale documentation
   - Change logging

### 9.2 Scientific Rigor

1. **Reproducibility**
   - Version control for all code and data
   - Documented build and test processes
   - Versioned rule base and cookbook with provenance
   - Committed order assignments (pre-registration)

2. **Validity**
   - Construct validity: DOC arm ensures the Prolog effect is attributed to the right ingredient
   - Internal validity: Latin-square counterbalancing removes learning/time confounds; frozen oracle removes grading drift
   - External validity: acknowledged single-case limitation
   - Reliability: micro-replication provides within-arm variance estimates

3. **Triangulation**
   - Multiple data sources (metrics, logs, verification output)
   - Multiple analysis methods (blocked models, contrasts, curves)
   - Multiple validation approaches

### 9.3 Validation Strategy

| Validation Type | BASE | DOC | PRO | DAF | BOTH |
|-----------------|------|-----|-----|-----|------|
| Functional Testing (frozen oracle) | ✅ | ✅ | ✅ | ✅ | ✅ |
| Unit Testing | ✅ | ✅ | ✅ | ✅ | ✅ |
| Integration Testing | ✅ | ✅ | ✅ | ✅ | ✅ |
| Formal Verification | ❌ | ❌ | ❌ | ✅ | ✅ |
| Differential Testing vs C | ✅ | ✅ | ✅ | ✅ | ✅ |
| Regression Testing | ✅ | ✅ | ✅ | ✅ | ✅ |
| Property Testing | ✅ | ✅ | ✅ | ✅ | ✅ |
| Traceability Audit | ❌ | ✅ | ✅ | ❌ | ✅ |
| Micro-replication (3 modules ×2) | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## 10. Data Collection and Metrics

### 10.1 Metrics Framework

**Six Dimensions of Measurement:**

1. **Translation Effectiveness** (all arms)
   - Percentage of code translated automatically
   - Manual intervention rate
   - Translation accuracy

2. **Code Quality** (all arms)
   - Defect density
   - Test coverage
   - Complexity metrics
   - Maintainability index
   - Cross-module translation-decision consistency

3. **Effort** (all arms)
   - Time spent per batch
   - Number of iterations
   - Developer hours
   - Knowledge-artifact maintenance time (DOC: cookbook upkeep; PRO/BOTH: rule capture and upkeep)

4. **Verification** (DAF, BOTH)
   - Verification success rate
   - Proof obligation count
   - Verification time
   - Proof complexity
   - Verification iterations per function (interaction metric)

5. **Heuristic Capture and Reuse** (DOC, PRO, BOTH)
   - Artifact size and growth curve (entries / rules)
   - Reuse rate per batch (hits / decisions)
   - Fraction of constructs resolved by rule/entry vs. manually
   - Rule amendments triggered by verification failures (BOTH)
   - Auditability: fraction of constructs traceable to a recorded rule/entry

6. **Learning and Process** (all arms)
   - Batch position vs. effort (learning curve)
   - Order-assignment deviations
   - Within-arm variance (from micro-replication)

### 10.2 Data Collection Plan

| Metric | Collection Method | Frequency |
|--------|-------------------|-----------|
| LOC (C) | cloc, custom scripts | Per batch, final |
| LOC (Kotlin) | cloc, custom scripts | Per batch, final |
| Translation time | Time tracking | Per session |
| Manual changes | Git commits | Per change |
| Test coverage | JaCoCo | Per test run |
| Defect count | Issue tracking | Continuous |
| Heuristic decisions (all arms) | Decision log | Per decision |
| Cookbook lookups | Lookup log | Per lookup |
| Rule hits / misses | Prolog query tooling | Per construct |
| Rule capture time | Time tracking | Per rule |
| Verification count / time | Dafny logs, timing | Per verification |
| Batch position / deviations | Cycle log | Per batch |

### 10.3 Data Analysis

**Statistical Analysis:**
- Descriptive statistics per arm and per batch
- Blocked models: module as block; arm (factor levels) as treatment; batch position and artifact maturity as covariates
- Pre-specified primary contrasts (Section 1.1) with per-module pairing
- Learning-curve regression (effort ~ batch position) to confirm counterbalancing adequacy (H11)
- Within-arm variance from micro-replication, reported alongside contrast estimates
- Correlation analysis (reuse rate vs. manual intervention rate)
- Exploratory secondary pairwise contrasts

**Qualitative Analysis:**
- Pattern identification; challenge categorization
- Solution effectiveness assessment; lessons learned
- Rule base and cookbook evolution narrative

### 10.4 Expected Data Outputs

1. **Raw Data:** Metrics database (CSV, JSON); log files; test results; cookbook/rule-base version history; Dafny verification logs; cycle logs
2. **Processed Data:** Statistical summaries; visualizations (growth curves, learning curves, cell × metric tables)
3. **Analysis Reports:** Quantitative, qualitative, and integrated findings

---

## 11. Analysis Framework

### 11.1 Comparative Analysis

| Dimension | Arms | Comparison Method |
|-----------|------|-------------------|
| Translation Time | All five | Primary contrasts; blocked models |
| Manual Effort | All five | Primary contrasts; blocked models |
| Code Quality | All five | Statistical tests on per-module measures |
| Defect Rate | All five | Ratio, difference; pooled verified vs. non-verified |
| Test Coverage | All five | Statistical test |
| Verification | DAF, BOTH | Descriptive; per-module iterations contrast |
| Knowledge Reuse | DOC, PRO, BOTH | Growth and reuse curves; DOC vs PRO hit rates |
| Consistency | All five | Statistical test on decision logs |
| Correctness | All five | Oracle equivalence; proof-based for verified arms |

### 11.2 Hypothesis Testing

**Pre-specified primary contrasts:**

1. **H2: Verification effect (DAF vs BASE)**
   - Null: No difference in correctness/effort
   - Test: paired per-module comparisons; blocked model with arm term

2. **H3: Documentation discipline (DOC vs BASE)**
   - Null: No difference in consistency/marginal effort
   - Test: paired per-module comparisons

3. **H4: Declarative mechanism (PRO vs DOC)**
   - Null: No difference attributable to representation + machine consultation beyond documentation
   - Test: paired per-module comparisons; reuse-rate comparison

4. **H5: Interaction — (BOTH − DAF) vs (PRO − BASE)**
   - Null: Factor effects are additive
   - Test: difference-of-differences on per-module effort and correctness

**Secondary contrasts:**

5. **H6 (corner comparison):** pooled verified arms (DAF, BOTH) vs pooled non-verified arms (BASE, DOC, PRO) on defect/edge-case counts — Mann-Whitney U
6. **PRO vs BASE** and **DOC vs BOTH-adjacent checks** as exploratory pairwise contrasts

Note: with a single researcher and single codebase, per-module measures are the unit of analysis and tests are exploratory/inferential support rather than definitive; micro-replication variance is reported to calibrate expectations.

**Pre-registered subgroup analysis — source language (lang):**

Units carry a `lang` tag (C compiler sources vs. C++ interpreter sources; see §2.2 and the selection tool's `units.csv`). The primary endpoint is pooled over both languages; the following subgroup analyses are pre-registered, not post-hoc:

- **S1 (Language difficulty):** per-language descriptive comparison of BASE-arm outcomes (C→Kotlin vs. C++→Kotlin) to characterize whether the two source languages differ in baseline translatability.
- **S2 (Treatment × language):** the four primary contrasts (H2–H5) re-estimated within each language subgroup, reported with interaction terms (arm × lang). Subgroup results are secondary: reported regardless of outcome, no cherry-picking; the pooled primary contrast stands regardless.
- **S3 (Batch composition):** batch-level c/cpp mix (range in the reference run: 3/24 … 14/17) is recorded as a covariate in the blocked models to confirm language imbalance across batches does not drive effects.

Rationale: the interpreter half of the corpus is C++ (~26k LOC, 212 of 308 function units), so the study is by construction a dual-language translation study; pre-registering the language dimension keeps it an asset (subgroup analysis) rather than a confound.

### 11.3 Cost-Benefit Analysis

**Cost Factors:**
- Development time (per arm)
- Learning curves (Dafny, Prolog, Kotlin)
- Tool acquisition and setup (all open source)
- Computational resources
- Knowledge-artifact capture and maintenance effort (cookbook vs. rule base)
- Maintenance effort

**Benefit Factors:**
- Code quality; correctness guarantees; maintainability
- Documentation quality
- Reusability (rule base / cookbook portability to future translation projects)
- Auditability

**Analysis:**
- Cost per line of code (per arm)
- Cost per defect prevented
- Marginal cost of verification with vs. without a heuristic base
- Marginal cost of the Prolog mechanism over the cookbook (the design's distinctive comparison)
- ROI calculation; break-even analysis

---

## 12. Timeline and Milestones

### 12.1 Overall Timeline

```
Months 1-2 (Nov-Dec 2026):  Shared Preparation
├── Environment setup (Kotlin, Dafny, SWI-Prolog, Z3)
├── Preprocess, canonicalize, stratify C source
├── Create and FREEZE the characterization-test oracle
├── Commit Latin-square arm order assignments
├── Pre-register capture criteria; draw micro-replication modules
└── Kotlin / Dafny / Prolog learning

Month 3 (Jan 2027):  Pilot Batch (all five arms)
├── Translate pilot batch in BASE, DOC, PRO, DAF, BOTH
├── Seed cookbook and rule base (identical criteria)
└── Calibrate instrumentation

Months 4-14 (Feb 2027-Jan 2028):  Interleaved Cycles
├── Monthly cycle: each arm advances one batch in its
│   pre-assigned order; lead arm rotates
├── Knowledge artifacts maintained and versioned
├── BASE: non-study remainder translated in parallel
└── Deviations logged as data

Months 15-16 (Feb-Mar 2028):  Micro-replication & Testing
├── Re-translate 3 drawn modules per arm (second pass)
└── Comprehensive testing vs frozen oracle

Month 17 (Apr 2028):  Analysis
├── Contrast estimation, blocked models, learning curves
└── Findings documentation

Month 18 (May 2028):  Finalization
├── Complete comparative analysis
├── Finalize documentation and deliverables
└── Project completion
```

### 12.2 Key Milestones

| Milestone | Target Date | Description |
|-----------|-------------|-------------|
| M1 | Dec 31, 2026 | Preparation complete: environments set up; oracle frozen; batches defined; order assignments committed |
| M2 | Jan 31, 2027 | Pilot batch translated in all five arms; cookbook and rule base seeded; instrumentation calibrated |
| M3 | Jul 31, 2027 | ~Half of the Study Subset cycles complete in all arms; knowledge artifacts show measurable growth |
| M4 | Jan 31, 2028 | Study Subset complete in all five arms |
| M5 | Mar 31, 2028 | Micro-replication done; testing of all arms complete against the frozen oracle |
| M6 | Apr 30, 2028 | Analysis complete; contrasts and learning-curve checks reported |
| M7 | May 31, 2028 | Project complete: final report, documentation, deliverables |

### 12.3 Latin-Square Order Assignments (committed before pilot)

Batches B1–B10 (Study Subset, stratified); each arm translates every batch; arm k starts at position k and proceeds cyclically, so each batch occupies each position exactly once across arms:

| Cycle → | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
|---------|---|---|---|---|---|---|---|---|---|----|
| BASE | B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 |
| DOC | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 |
| PRO | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 |
| DAF | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 | B3 |
| BOTH | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 | B3 | B4 |

Additionally, the arm receiving "fresh attention" first in each cycle rotates (BASE, DOC, PRO, DAF, BOTH, repeat), so no arm systematically gets the researcher's freshest hours. (The exact number of batches is fixed at M1; the table generalizes by cyclic rotation. If the Study Subset resolves to a different batch count, the same cyclic construction applies.)

**Rationale:** In the previous sequential-block schedule, the Prolog factor was perfectly correlated with calendar time — a "Prolog helps" result could equally have been "the researcher got better." The cyclic design makes every arm encounter every batch at every stage of the learning curve, on average identically.

### 12.4 Cycle-Level Schedule

| Cycle | Period | Activity |
|-------|--------|----------|
| 1-3 | Feb-Apr 2027 | Interleaved batches 1-3 per arm order; BASE remainder work begins |
| 4-7 | May-Aug 2027 | Interleaved batches 4-7; DAF/BOTH verification workload peaks |
| 8-11 | Sep-Dec 2027 | Interleaved batches 8-10 + catch-up; rule base / cookbook mature |
| 12-13 | Jan-Feb 2028 | Complete deferred batches; begin micro-replication |
| 14 | Mar 2028 | Micro-replication and comprehensive testing complete |
| 15 | Apr 2028 | Analysis |
| 16 | May 2028 | Final documentation |

---

## 13. Resource Requirements

### 13.1 Human Resources

| Role | Required Hours | Notes |
|------|----------------|-------|
| Principal Investigator | ~1,400-1,600 | Part-time over 18 months (~20-25 hours/week) |
| Technical Reviewer | ~50 | External validation (optional) |
| Peer Reviewer | ~25 | Informal review |
| Total | ~1,475-1,675 | |

### 13.2 Hardware Resources

| Resource | Specification | Quantity |
|----------|---------------|----------|
| Development Machine | MacBook Pro (current) | 1 |
| Additional Machine | Linux server for verification | 1 (optional) |
| Storage | For code, data, backups | 100GB+ |

### 13.3 Software Resources

| Software | Purpose | Source | License |
|----------|---------|--------|---------|
| Kotlin + Gradle + OpenJDK | Target language toolchain | Homebrew | Open Source |
| Dafny (4.x) | Formal verification | Homebrew | MIT |
| Z3 | SMT solver (Dafny backend) | Homebrew | MIT |
| SWI-Prolog | Heuristic rule base | Homebrew | BSD-2/GPL |
| Clang/LLVM + CMake | C analysis and baseline build | Xcode CLT / Homebrew | Open Source |
| Tree-sitter | Syntax parsing | Homebrew | Open Source |
| JUnit 5 / Kotest / JaCoCo | Testing and coverage | Gradle | Open Source |
| cloc | LOC metrics | Homebrew | Open Source |
| Git + GitHub CLI | Version control | Homebrew | Open Source |
| IDE (VS Code / IntelliJ CE) | Development | Homebrew (cask) | Free/Open Source |

### 13.4 Financial Resources

| Item | Estimated Cost | Notes |
|------|----------------|-------|
| Additional hardware | $0-$2,000 | Optional server |
| Software licenses | $0 | All open source |
| Conference fees | $500-$2,000 | Optional presentations |
| Publication fees | $0-$1,500 | Open access options |
| Total | $500-$5,500 | Best case - worst case |

---

## 14. Risk Management

### 14.1 Risk Identification

| Risk | Likelihood | Impact | Category |
|------|------------|--------|----------|
| Complexity of Mumps II code | High | High | Technical |
| Dafny learning curve | High | High | Skill |
| Interleaving discipline breaks down (arms drift sequential) | Medium | High | Methodological |
| Rule base / cookbook design difficulty | Medium | Medium | Technical |
| Capture overhead exceeds benefit | Medium | Medium | Methodological |
| Verification intractability | Medium | High | Technical |
| Tool limitations | Medium | High | Technical |
| Time overrun | Medium | Medium | Schedule |
| Resource limitations | Low | Medium | Resource |
| Competing priorities | Medium | Medium | Personal |
| Health issues | Low | High | Personal |

### 14.2 Risk Mitigation Strategies

#### High-Impact Risks

**Risk: Complexity of Mumps II code**
- **Mitigation:** Start with simple batches in the stratification; build expertise gradually
- **Contingency:** Trim the Study Subset (pre-specified so the trim is visible); BASE full coverage is the only elastic element
- **Success Criteria:** Study Subset completed in all arms

**Risk: Dafny learning curve**
- **Mitigation:** Dedicated learning in Months 1-3; pilot batch verifies simple modules first
- **Contingency:** Reclassify intractable modules as "verification-deferred"; DAF/BOTH correctness then rests on the frozen oracle like other arms, and the deferral itself is reported data
- **Success Criteria:** Verify simple modules within 4 months

**Risk: Interleaving discipline breaks down**
- **Mitigation:** Order assignments committed in writing before the pilot; monthly cycle reviews; deviation logging is cheap and honest
- **Contingency:** If cycles collapse into blocks, the batch-position covariate (logged throughout) still allows post-hoc adjustment of estimates; report the design deviation prominently
- **Success Criteria:** ≤10% of batch translations occur out of assigned order

**Risk: Rule base / cookbook design difficulty**
- **Mitigation:** Simple schema first; seeded from pilot batch; identical capture criteria for DOC and PRO
- **Contingency:** Simplify schema; the DOC-vs-PRO contrast remains valid even with a modest rule base
- **Success Criteria:** Working rule base with query tooling by Month 4

**Risk: Capture overhead exceeds benefit**
- **Mitigation:** Track capture vs. reuse effort from the pilot; capture only recurring heuristics
- **Contingency:** A null or negative result is itself a publishable finding; document honestly
- **Success Criteria:** Capture effort accounted per rule/entry; reuse rates measurable

**Risk: Verification intractability**
- **Mitigation:** Abstraction, modular verification, incremental approach
- **Contingency:** Focus verification on critical components; record deferrals
- **Success Criteria:** Verify at least 50% of the DAF study subset

#### Schedule and Personal Risks

**Risk: Time overrun**
- **Mitigation:** Realistic timeline; monthly cycle reviews; the Study Subset can be trimmed without destroying the design
- **Contingency:** Reduce batch count; keep all five arms balanced rather than dropping an arm
- **Success Criteria:** M3 milestone met within 2 months of target

**Risk: Competing priorities**
- **Mitigation:** Dedicated time blocks, priority setting, progress tracking
- **Contingency:** Reduce scope, extend timeline
- **Success Criteria:** Consistent 10+ hours/week on project

**Risk: Resource limitations**
- **Mitigation:** Efficient tools; cloud resources if needed
- **Contingency:** Reduce scope
- **Success Criteria:** All essential resources available

**Risk: Health issues**
- **Mitigation:** Regular breaks, healthy work habits
- **Contingency:** Extended timeline, reduced scope
- **Success Criteria:** Maintain consistent progress

### 14.3 Risk Monitoring

**Monthly Risk Review (folded into the cycle review):**
- Assess risk likelihood and impact
- Evaluate mitigation effectiveness
- Update contingency plans
- Document risk status

**Risk Indicators:**
- Delays in cycle completion
- Increasing complexity of translation
- Verification failures or growing deferral list
- Rule base / cookbook stagnation (no new entries, low reuse)
- Order-assignment deviations accumulating
- Resource shortages
- Health problems

---

## 15. Success Criteria

### 15.1 Primary Success Criteria

1. **Translation Completion**
   - BASE: 80%+ of the Mumps II compiler translated to Kotlin
   - Study Subset: completed in all five arms (per-module deferrals reported)

2. **Functional Equivalence**
   - All translations produce equivalent outputs on the frozen oracle
   - No critical functionality lost in translation

3. **Quality Differentiation**
   - Verified arms have fewer defects than non-verified arms
   - DOC and PRO arms have higher decision consistency than BASE
   - PRO vs DOC contrast estimated and interpreted

4. **Contrast Analysis**
   - All four primary contrasts estimated with per-module pairing
   - Learning-curve check confirms counterbalancing adequacy (H11)
   - Within-arm variance reported (micro-replication)

5. **Knowledge Artifacts**
   - Versioned Prolog rule base and cookbook with provenance
   - Reuse and coverage statistics for both

6. **Publication**
   - At least one peer-reviewed publication
   - Conference presentation
   - Open source code release (if possible)

### 15.2 Secondary Success Criteria

1. **Tool Contributions**
   - Improvements to translation tools
   - Contributions to Dafny or related tools
   - Reusable Prolog rule base and query tooling

2. **Methodology Development**
   - Reproducible methodology for C-to-Kotlin translation
   - Guidelines for formal verification of legacy code
   - Guidelines for declarative heuristic capture in translation
   - Best practices documentation

3. **Community Impact**
   - Adoption of methodology by others
   - Citations of published work
   - Contributions to open source community

---

## 16. Conclusion

This research design compares machine-assisted C-to-Kotlin translation across five counterbalanced arms that cross formal verification (Dafny) with three levels of heuristic handling (ad hoc, documented cookbook, machine-queried Prolog rules). Because every arm translates the same pre-specified module batches under a Latin-square order assignment and a frozen oracle, the study can attribute differences to the treatments rather than to researcher learning or grading drift — and, through the DOC control, can say *which ingredient* of heuristic capture is responsible for any observed benefit.

The study is designed to be:
- **Rigorous:** Pre-specified contrasts, blocked analysis, committed order assignments, micro-replication
- **Reproducible:** Complete documentation, version control, provenance for every heuristic artifact
- **Practical:** A real-world codebase with real challenges
- **Useful:** Actionable insights on verification and knowledge capture for legacy modernization

The principal challenges — the complexity of the Mumps II codebase, the Dafny learning curve, and the discipline of maintaining five interleaved arms — are mitigated by the stratified batches, deferral policy, cycle reviews, and the fact that the Study Subset can shrink without destroying the design.

---

## Appendix A: Acronyms

| Acronym | Meaning |
|---------|---------|
| AST | Abstract Syntax Tree |
| CIVL | Concurrent Intermediate Verification Language |
| MUMPS | Massachusetts General Hospital Utility Multi-Programming System |
| JVM | Java Virtual Machine |
| LOC | Lines of Code |
| ROI | Return on Investment |
| SMT | Satisfiability Modulo Theories (Z3 is an SMT solver) |

---

## Appendix B: File Naming Convention

All project files will follow this naming convention:

- `RESEARCH_PROPOSAL_C_To_Kotlin.md` - This proposal
- `RESEARCH_DESIGN_C_To_Kotlin.md` - This design
- `ARM_BASE_*` - Files related to arm BASE
- `ARM_DOC_*` - Files related to arm DOC
- `ARM_PRO_*` - Files related to arm PRO
- `ARM_DAF_*` - Files related to arm DAF
- `ARM_BOTH_*` - Files related to arm BOTH
- `COOKBOOK_*` - Heuristic cookbook files
- `RULEBASE_*` - Prolog rule base files and documentation
- `ORACLE_*` - Frozen characterization test suite files
- `ANALYSIS_*` - Analysis files
- `DATA_*` - Data files
- `DOCS_*` - Documentation files

*(The earlier `APPROACH_A_*` / `APPROACH_B_*` and cell-based conventions are superseded; legacy files may be renamed at commit time.)*

---

## Appendix C: Arm Overview

| Arm | Dafny? | Heuristic handling | Translates | Role |
|-----|--------|--------------------|-----------|------|
| BASE | No | Ad hoc, logged | Study Subset + full codebase | Control; oracle producer |
| DOC | No | Cookbook, manually consulted | Study Subset | Documentation-discipline control |
| PRO | No | Prolog rules, machine-queried | Study Subset | Declarative capture main effect |
| DAF | Yes | Ad hoc, logged | Study Subset | Verification main effect |
| BOTH | Yes | Prolog rules, machine-queried | Study Subset | Interaction |

Primary contrasts: DAF vs BASE; DOC vs BASE; PRO vs DOC; (BOTH − DAF) vs (PRO − BASE). The Dafny×cookbook cell is not run (optional follow-up if an interaction is found).

---

## Appendix D: Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-11-12 | Initial version (two-arm A/B design) | Mark Andrews |
| 1.1 | 2026-11-13 | Expanded to 2×2 factorial, six-arm design; added Prolog heuristic capture factor | Mark Andrews |
| 1.2 | 2026-11-13 | Redesigned arms for validity: five distinct runs (BASE, DOC, PRO, DAF, BOTH); added DOC control arm to decompose the heuristic-capture effect; replaced sequential blocks with Latin-square counterbalanced interleaved cycles; added frozen-oracle requirement, pre-registered capture criteria, order-assignment commitment, micro-replication, knowledge-artifact maturity covariate, and learning-curve analysis | Mark Andrews |
| 1.2a | 2026-11-13 | §2 corrected: subject confirmed as the Mumps II Interpreter-Compiler Library component of the O'Kane distribution (compiler in C ~8.7k LOC, interpreter in C++ ~26k LOC, 41 headers); documented that the interpreter is C++, so the study covers C→Kotlin and C++→Kotlin; documented corpus exclusions (`*.in`, packaging payloads, SVN `Checkout/` demos) | Mark Andrews |
| 1.2b | 2026-11-14 | §11.2: pre-registered the source-language (`lang`) subgroup analyses S1–S3 — dual-language design embraced as a subgroup dimension; primary contrasts remain pooled, batch c/cpp mix recorded as covariate | Mark Andrews |

---

*Document Status: Draft*  
*Next Review: At M1 (oracle freeze and order-assignment commitment)*  
*Repository: /home/mark/projects/mumps-research*
