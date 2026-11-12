# Research Proposal: Machine-Assisted C-to-Kotlin Translation

**Project Title:** Comparative Study of Machine-Assisted C-to-Kotlin Translation: Formal Verification vs. Direct Translation

**Principal Investigator:** Mark Andrews  
**Affiliation:** Flaming Yack Limited Company (Nebraska LLC)  
**Date:** November 12, 2026  
**Target Completion:** December 31, 2030 (Extended from original deadline)

---

## Executive Summary

This research project proposes a comparative study of two approaches to translating Dr. Kevin O'Kane's "Mumps II" compiler from C to Kotlin:

- **Approach A:** Machine-assisted translation WITHOUT formal verification (Dafny)
- **Approach B:** Machine-assisted translation WITH formal verification using Dafny

The study aims to evaluate the effectiveness, correctness, effort required, and quality of each approach for translating legacy C code to a modern, type-safe language.

---

## 1. Background and Motivation

### 1.1 The Mumps II Compiler

Dr. Kevin O'Kane's "Mumps II" compiler is a C-based implementation of the MUMPS (Massachusetts General Hospital Utility Multi-Programming System) programming language. Originally developed in the 1960s-1970s, MUMPS is a language designed for healthcare and database applications, with unique features including:

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

Machine-assisted translation offers a path to modernize these codebases while preserving their functionality. However, the effectiveness of such translation, particularly when augmented with formal verification, has not been systematically studied.

### 1.4 Research Gap

While individual case studies exist, there is a lack of:

- **Systematic comparison** of translation approaches (with vs. without formal verification)
- **Quantitative metrics** for evaluating translation quality
- **Reproducible methodology** for translating legacy C to modern languages
- **Empirical data** on the benefits and costs of formal verification in translation

This research aims to fill these gaps.

---

## 2. Research Questions

### Primary Research Questions

1. **Translation Feasibility:** To what extent can machine-assisted tools successfully translate the Mumps II compiler from C to Kotlin?

2. **Verification Impact:** How does the use of formal verification (Dafny) affect the correctness, completeness, and effort required for translation?

3. **Quality Comparison:** What are the measurable differences in code quality (correctness, maintainability, type safety) between the two approaches?

4. **Effort Analysis:** What is the relative effort (time, complexity, human intervention) required for each approach?

### Secondary Research Questions

5. **Pattern Recognition:** Which C language constructs in Mumps II are most challenging to translate automatically?

6. **Dafny Effectiveness:** How effective is Dafny at identifying and preventing translation errors?

7. **Kotlin Suitability:** How well does Kotlin's type system and features support the translation of MUMPS-specific concepts?

8. **Verification Overhead:** What is the performance and resource overhead of using formal verification during translation?

---

## 3. Hypotheses

### Primary Hypotheses

**H1:** Machine-assisted translation WITHOUT formal verification will successfully translate 80-90% of the Mumps II compiler's C code, but will require significant manual intervention for complex constructs (pointers, memory management, macros).

**H2:** Machine-assisted translation WITH formal verification will achieve higher correctness (95%+ functional equivalence) but will require 2-3x more effort due to the verification process.

**H3:** The formally verified translation will have significantly fewer runtime errors and edge cases than the non-verified translation.

### Secondary Hypotheses

**H4:** The most challenging C constructs to translate will be: function pointers, complex macros, manual memory management, and low-level pointer arithmetic.

**H5:** Dafny will be most effective at preventing: null pointer dereferences, buffer overflows, type mismatches, and invariant violations.

**H6:** Kotlin's type system will adequately represent most MUMPS concepts, but may require workarounds for: global storage hierarchies, implicit typing, and certain database operations.

---

## 4. Objectives

### 4.1 Primary Objectives

1. **Develop Translation Methodologies:** Create reproducible methodologies for both approaches (with and without formal verification)

2. **Implement Translations:** Complete two independent translations of the Mumps II compiler to Kotlin

3. **Evaluate Quality:** Quantitatively and qualitatively assess the quality of each translation

4. **Compare Approaches:** Systematically compare the two approaches in terms of effort, correctness, and maintainability

### 4.2 Secondary Objectives

5. **Identify Challenges:** Document specific challenges encountered during translation and verification

6. **Develop Best Practices:** Establish guidelines for future C-to-Kotlin translation projects

7. **Assess Tool Effectiveness:** Evaluate the effectiveness of machine-assisted tools and Dafny for this use case

8. **Contribute to Knowledge:** Publish findings to benefit the software engineering and formal methods communities

---

## 5. Methodology Overview

The research will follow a **comparative experimental design** with two parallel tracks:

```
Mumps II C Source Code
         │
    ┌────┴────┐
    ▼         ▼
Approach A  Approach B
 (No Dafny)  (With Dafny)
    ▼         ▼
Translation Translation
  Phase 1     Phase 1
    │         (with verification)
    ▼         ▼
Testing &   Testing & 
Validation  Validation
    │         (with proofs)
    ▼         ▼
  Results    Results
    │         ▼
    └──► Comparison & Analysis
```

### 5.1 Approach A: Direct Machine-Assisted Translation

**Steps:**
1. Preprocessing: Clean and normalize C source code
2. Static Analysis: Use tools to analyze code structure and dependencies
3. Automated Translation: Apply machine-assisted translation tools
4. Manual Intervention: Fix issues, handle edge cases, adapt to Kotlin idioms
5. Testing: Verify functional equivalence through testing
6. Refinement: Iteratively improve based on test results

**Tools:**
- Clang-based tools (for AST analysis)
- Tree-sitter (for syntax parsing)
- Custom translation scripts
- Kotlin compiler
- Test frameworks (JUnit, Kotlin Test)

### 5.2 Approach B: Translation with Formal Verification

**Steps:**
1. Preprocessing: Same as Approach A
2. Static Analysis: Enhanced with formal specification analysis
3. Translation with Verification: Translate while simultaneously building formal specifications
4. Formal Verification: Use Dafny to prove properties of translated code
5. Iterative Refinement: Fix verification failures and retest
6. Final Testing: Comprehensive testing with verified correctness guarantees

**Tools:**
- All tools from Approach A
- Dafny theorem prover
- Formal specification language (Dafny's specification syntax)
- Verification-aware translation tools

---

## 6. Expected Outcomes and Deliverables

### 6.1 Academic Deliverables

1. **Research Papers:**
   - Main paper: "Comparative Study of Machine-Assisted C-to-Kotlin Translation: Formal Verification vs. Direct Translation"
   - Supporting papers on specific aspects (tool evaluation, challenges, etc.)

2. **Technical Reports:**
   - Methodology documentation
   - Translation guide for C-to-Kotlin
   - Dafny verification guide for legacy code

3. **Presentations:**
   - Conference presentations
   - Workshop tutorials
   - Community talks

### 6.2 Technical Deliverables

1. **Source Code:**
   - Mumps II compiler in original C (baseline)
   - Approach A: Translated Kotlin version (without formal verification)
   - Approach B: Translated Kotlin version (with formal verification)
   - Test suites for both versions

2. **Tools and Scripts:**
   - Translation automation scripts
   - Verification scripts and specifications
   - Analysis tools and utilities

3. **Documentation:**
   - Translation process documentation
   - Verification methodology documentation
   - User guides and best practices

### 6.3 Data Deliverables

1. **Metrics Database:**
   - Translation effort metrics (time, lines of code, manual interventions)
   - Quality metrics (defects, test coverage, verification results)
   - Performance metrics (execution time, memory usage)

2. **Comparison Analysis:**
   - Statistical comparison of both approaches
   - Cost-benefit analysis of formal verification
   - Recommendations for future projects

---

## 7. Significance and Impact

### 7.1 Academic Significance

This research will contribute to:

- **Software Engineering:** Empirical data on translation approaches and formal verification
- **Formal Methods:** Practical application of Dafny to legacy code modernization
- **Programming Languages:** Insights into C-to-Kotlin translation patterns
- **Software Maintenance:** Evidence-based recommendations for legacy system modernization

### 7.2 Practical Significance

Benefits for practitioners:

- **Guidance:** Evidence-based recommendations for choosing translation approaches
- **Methodology:** Reproducible methodologies for legacy code modernization
- **Tool Evaluation:** Assessment of machine-assisted translation and verification tools
- **Best Practices:** Guidelines for translating C to Kotlin specifically

### 7.3 Broader Impact

- **Industry:** Help organizations modernize legacy C codebases more effectively
- **Education:** Training materials and case studies for software engineering education
- **Open Source:** Contributions to open source tools and methodologies
- **Standards:** Potential influence on software modernization standards

---

## 8. Scope and Limitations

### 8.1 Scope

**Included:**
- Mumps II compiler C source code (complete translation)
- Two independent translation approaches
- Comprehensive testing and validation
- Quality and effort metrics collection
- Comparative analysis

**Excluded:**
- Other MUMPS implementations (focus on Mumps II only)
- Translation to other languages (Kotlin only)
- Runtime performance optimization (correctness focus)
- Full reimplementation from scratch (translation focus)

### 8.2 Limitations

1. **Single Case Study:** Results may not generalize to all C codebases
2. **Tool Dependencies:** Reliance on specific tools (Clang, Dafny, etc.)
3. **Expertise:** Researcher's learning curve for formal verification
4. **Time Constraints:** Part-time research over extended period
5. **Resource Constraints:** Limited computational resources for verification

---

## 9. Feasibility

### 9.1 Researcher Qualifications

- **Domain Knowledge:** Extensive knowledge of MUMPS/M language and history
- **Research Experience:** Experience with systematic literature review (Citation Pearl Growing)
- **Technical Background:** Familiarity with C, growing knowledge of Kotlin
- **Learning Capacity:** Demonstrated ability to learn new technologies (Dafny, formal methods)

### 9.2 Resource Availability

- **Source Code:** Mumps II compiler C source available
- **Tools:** Clang, Dafny, Kotlin compiler, and development tools accessible
- **Computing:** Sufficient computing resources for translation and verification
- **Time:** Extended timeline (4 years) allows for part-time work

### 9.3 Methodological Feasibility

- **Approach A:** Well-established machine-assisted translation techniques
- **Approach B:** Dafny has been successfully applied to C verification (via CIVL and other tools)
- **Testing:** Standard software testing methodologies applicable
- **Analysis:** Statistical and comparative analysis methods well-understood

---

## 10. Timeline Overview

| Phase | Duration | Activities |
|-------|----------|------------|
| **Preparation** | Months 1-3 | Tool setup, C code analysis, learning Kotlin/Dafny |
| **Approach A Translation** | Months 4-12 | Direct translation without verification |
| **Approach B Translation** | Months 13-24 | Translation with formal verification |
| **Testing & Validation** | Months 25-30 | Comprehensive testing of both approaches |
| **Analysis & Comparison** | Months 31-36 | Data analysis, comparison, reporting |
| **Documentation & Publication** | Months 37-48 | Documentation, papers, presentations |

**Total Duration:** 48 months (4 years)  
**Effort Estimate:** ~11-13 hours/week average

---

## 11. Risk Assessment and Mitigation

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| Tool incompatibility | Medium | High | Test tools early, have alternatives |
| Verification complexity | High | High | Start with small modules, build expertise |
| Translation errors | High | Medium | Comprehensive testing, iterative refinement |
| Time overrun | Medium | Medium | Extended timeline, modular approach |
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

### Related Work
- C-to-Java translation studies
- Legacy code modernization case studies
- Formal verification of compilers
- Machine-assisted translation tools

---

## 13. Appendix: Glossary

- **MUMPS:** Massachusetts General Hospital Utility Multi-Programming System - a programming language
- **Mumps II:** Dr. Kevin O'Kane's implementation of MUMPS in C
- **Kotlin:** Modern, statically-typed programming language for the JVM
- **Dafny:** Automatic program verifier for functional correctness
- **Formal Verification:** Mathematical proof that software meets its specification
- **Machine-Assisted Translation:** Use of tools to automate parts of code translation

---

*Document Version: 1.0*  
*Last Updated: November 12, 2026*  
*Repository: /home/mark/projects/mumps-research*
