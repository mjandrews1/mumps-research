# Literature Review: Machine-Assisted C Source Code Translation and Formal Verification

**Project:** Mumps II Compiler - C to Kotlin Translation Research  
**Researcher:** Mark J. Andrews  
**Date:** 12 November 2026  
**Search Conducted:** 12 November 2026  

---

## Executive Summary

This literature review examines scholarly and technical publications related to machine-assisted C source code translation, formal verification methodologies, and legacy code modernization. The review focuses on three primary research areas relevant to translating Dr. Kevin O'Kane's Mumps II compiler from C to Kotlin: (a) machine-assisted translation without formal verification tools, (b) machine-assisted translation with formal verification using Dafny theorem prover, and (c) related work in legacy code modernization and compiler translation. Results are drawn from peer-reviewed academic literature via Wiley Scholar Gateway and commercial/literature sources.

**Sources:** Wiley Scholar Gateway (8-10 peer-reviewed articles per search, 2012-2025), with additional context from industry reports and tool documentation.

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Methodology](#2-methodology)
3. [C to Kotlin Translation - Machine-Assisted Approaches](#3-c-to-kotlin-translation---machine-assisted-approaches)
4. [Formal Verification with Dafny](#4-formal-verification-with-dafny)
5. [Legacy C Code Modernization](#5-legacy-c-code-modernization)
6. [Commercial and Lay Literature](#6-commercial-and-lay-literature)
7. [Synthesis and Gaps](#7-synthesis-and-gaps)
8. [References](#8-references)

---

## 1. Introduction

The Mumps II compiler, originally developed by Dr. Kevin O'Kane, represents a significant body of C source code implementing the M/Mumps programming language. Translating this legacy code to Kotlin presents unique challenges: preserving semantic fidelity across language paradigms, ensuring memory safety in a garbage-collected environment, and maintaining the compiler's functional correctness. This review explores existing research on automated and semi-automated code translation, with particular attention to formal verification techniques using tools like Microsoft's Dafny theorem prover.

---

## 2. Methodology

**Search Strategy:**
- **Academic Sources:** Wiley Scholar Gateway semantic searches (2012-2025)
- **Keywords:** "C to Kotlin translation", "legacy code modernization", "formal verification", "Dafny theorem prover", "automatic program verification", "compiler translation"
- **Selection Criteria:** Peer-reviewed papers with DOIs, direct relevance to C code translation or formal verification
- **Citation Format:** IEEE Style (Author, Initials, Year, Title, Journal, Volume, Pages, DOI)

**Inclusion Criteria:**
1. Peer-reviewed academic papers
2. Technical reports from recognized research institutions
3. Commercial tool documentation with scholarly references
4. Direct relevance to: C code translation, formal verification, legacy modernization

---

## 3. C to Kotlin Translation - Machine-Assisted Approaches

### 3.1 Domain-Specific Translation Tools

The most directly relevant work to machine-assisted C translation involves domain-specific tools that leverage AI/ML techniques for code understanding and transformation.

**Key Finding:** While direct C-to-Kotlin translators are scarce in academic literature, several approaches demonstrate methodologies applicable to the Mumps II translation project.

- **[1]** B. Han, C. Li, H. Deng, G. Liu, and Z. Zheng, "Domain-specific translation tool from structured text to C source code with code readability enhancement in programmable logic controllers," *Concurrency and Computation: Practice and Experience*, vol. 36, no. 15, Apr. 2024. doi: 10.1002/cpe.8100
  - *Relevance:* Demonstrates AI-enhanced code translation with LLM-based semantic understanding and resampling techniques. While ST-to-C rather than C-to-Kotlin, the methodology of using LLMs for type annotation, variable mapping, and semantic description is directly applicable.
  - *Method:* Four-phase process: type annotation, semantic description with resampling, code enhancement draft, and annotation-enriched refinement.
  - *Key Insight:* Iterative LLM analysis improves translation accuracy by building layered understanding of program functionality.

- **[2]** A. Moss, R. Schluntz, and P. A. Buhr, "Cforall: Adding modern programming language features to C," *Software: Practice and Experience*, vol. 48, no. 12, pp. 2111-2146, Aug. 2018. doi: 10.1002/spe.2624
  - *Relevance:* Cforall (C-for-all) extends C with modern features while maintaining backward compatibility, demonstrating source-to-source translation methodology.
  - *Method:* Translator converts Cforall to GCC-compatible C, preserving semantics while adding features like overloading, polymorphism, and multiple return values.
  - *Key Insight:* Evolutionary extension rather than replacement allows incremental modernization of legacy codebases.

### 3.2 Object-Oriented Model Inference from C

Since Kotlin is an object-oriented language, research on inferring OO models from procedural C code provides valuable methodology.

- **[3]** I. Cassol and G. Arévalo, "A methodology to infer and refactor an object-oriented model from C applications," *Software: Practice and Experience*, vol. 48, no. 3, pp. 550-577, Oct. 2017. doi: 10.1002/spe.2549
  - *Relevance:* Directly addresses the challenge of transforming procedural C into OO structures, which is essential for C-to-Kotlin translation.
  - *Method:* M2K methodology with two phases: Source Code Analysis (automatic) using ModelMapper tool, and Expert Mapping (manual) to generate class candidates.
  - *Key Insight:* Generates "Class Candidate Diagrams" (CCDs) that group variables and functions into cohesive software entities, providing a bridge from procedural to OO paradigms.
  - *Validation:* 14 real-world C case studies demonstrate feasibility.

### 3.3 Automated Code Generation from Formal Models

- **[4]** N. Ge, A. Dieumegard, E. Jenn, and L. Voisin, "Correct-by-construction specification to verified code," *Journal of Software: Evolution and Process*, vol. 30, no. 10, Jun. 2018. doi: 10.1002/smr.1959
  - *Relevance:* Demonstrates translation from formal Event-B models to C code with correctness guarantees.
  - *Method:* Uses intermediate High-Level Language (HLL) with automatic code generation for scheduled systems and manual implementation for third-party functions.
  - *Key Insight:* Correctness guaranteed through equivalence proofs between HLL models and generated C code.

---

## 4. Formal Verification with Dafny

### 4.1 Dafny in Education and Research

- **[5]** G. Caso, D. Garbervetsky, and D. Gorin, "Integrated program verification tools in education," *Software: Practice and Experience*, vol. 43, no. 4, pp. 403-418, Jul. 2012. doi: 10.1002/spe.2143
  - *Relevance:* Demonstrates Dafny's use in educational settings for program verification, highlighting its suitability for classroom usage.
  - *Method:* Dafny uses Boogie intermediate language to generate Verification Conditions (VCs) passed to Z3 automated prover.
  - *Key Insight:* Dafny's Visual Studio plugin provides integrated verification environment; however, requires understanding of ghost fields and dynamic frames methodology.

### 4.2 Formal Verification of C Code

- **[6]** X. Yin et al., "SafeOSL: Ensuring memory safety of C via ownership-based intermediate language," *Software: Practice and Experience*, vol. 52, no. 5, pp. 1114-1142, Dec. 2021. doi: 10.1002/spe.3057
  - *Relevance:* Addresses memory safety verification for C code using formal methods, applicable to verifying translated Kotlin code properties.
  - *Method:* Transforms C programs into OSL (Ownership System Language) programs, then verifies using K-framework.
  - *Key Insight:* Borrows Rust's ownership system to ensure memory safety, detecting errors like double-free and use-after-free.
  - *Validation:* Effective in detecting memory errors in C programs; intermediate language reusable for other languages.

### 4.3 Formal Verification Frameworks

- **[7]** C. T. Cook, Y. Sun, and M. Sitaraman, "Experience report: evolution of a web-integrated software development and verification environment," *Software: Practice and Experience*, vol. 45, no. 6, pp. 857-872, Feb. 2014. doi: 10.1002/spe.2259
  - *Relevance:* Compares Dafny with VeriFast for web-based verification, highlighting Dafny's full-strength programming language approach.
  - *Method:* Dafny generates VCs via Boogie to Z3 prover; VeriFast uses interactive prover with Z3 backend.
  - *Key Insight:* Dafny provides browser-based verification without local installation, suitable for distributed research.

---

## 5. Legacy C Code Modernization

### 5.1 Large-Scale Migration Projects

- **[8]** M. T. Schuts, R. T. van Aarssen, P. M. Tielemans, and J. J. Vinju, "Large-scale semi-automated migration of legacy C/C++ test code," *Software: Practice and Experience*, vol. 52, no. 7, pp. 1543-1580, Mar. 2022. doi: 10.1002/spe.3082
  - *Relevance:* Industrial experience report on semi-automated migration of 100,000+ lines of legacy test code.
  - *Method:* Uses metaprogramming to automate analysis and transformation; Clang-based parsing for C/C++ code.
  - *Key Insight:* Automation reduces risk of introducing errors and avoids rework; requires learning new metaprogramming skills but enables bespoke analysis tools.
  - *Tools:* ClangMR (Clang + MapReduce), TXL, DMS, Stratego/XT for pattern recognition and substitution.

- **[9]** W. Lucas et al., "Embracing modern C++ features: An empirical assessment on the KDE community," *Journal of Software: Evolution and Process*, vol. 36, no. 5, Aug. 2023. doi: 10.1002/smr.2605
  - *Relevance:* Demonstrates community practices for modernizing legacy codebases, applicable to C-to-Kotlin migration.
  - *Method:* Survey of KDE developers' modernization practices for C++ code.
  - *Key Insight:* Tools like Clazy and Clang-tidy automate modernization; developers perform rejuvenation incrementally during bug fixes and feature implementation.
  - *Validation:* Widespread adoption of lambda expressions, auto-typed variables, and range-based for loops in modernized code.

### 5.2 Model-Driven Engineering Approaches

- **[10]** S. Bonfanti, A. Gargantini, and A. Mashkoor, "Design and validation of a C++ code generator from Abstract State Machines specifications," *Journal of Software: Evolution and Process*, vol. 32, no. 2, Nov. 2019. doi: 10.1002/smr.2205
  - *Relevance:* Demonstrates model-to-code transformation with automated testing, applicable to compiler translation projects.
  - *Method:* Asm2C++ tool generates C++ code from ASM specifications; automatically generates unit tests and scenarios.
  - *Key Insight:* Model-driven engineering provides seamless transformation from abstract models to implementation with rigorous quality assurance.

---

## 6. Commercial and Lay Literature

### 6.1 Commercial Tools and Frameworks

- **[11]** JetBrains, "Kotlin Multiplatform Mobile (KMM)," 2020-2026. [Online]. Available: https://kotlinlang.org/lp/mobile/
  - *Type:* Commercial framework documentation
  - *Relevance:* Kotlin's official multiplatform support enables sharing code between Android, iOS, and other platforms.

- **[12]** Microsoft Research, "Dafny: A Language with Built-in Verification", 2010-2026. [Online]. Available: https://dafny.org/
  - *Type:* Tool documentation
  - *Relevance:* Official Dafny documentation and tutorials provide guidance on formal specification and verification.

- **[13]** Google, "Android NDK and Native Code", 2026. [Online]. Available: https://developer.android.com/ndk
  - *Type:* Commercial documentation
  - *Relevance:* Demonstrates integration of C/C++ code with Android (Kotlin/Java) environments.

### 6.2 Industry Reports and Case Studies

- **[14]** Y. Choi, "Model checking Trampoline OS: a case study on safety analysis for automotive software," *Software Testing, Verification and Reliability*, vol. 24, no. 1, pp. 38-60, Aug. 2012. doi: 10.1002/stvr.1482
  - *Relevance:* Demonstrates formal verification of operating system code using SPIN model checker.

- **[15]** N. Barnes et al., "AI@NICTA," *AI Magazine*, vol. 33, no. 3, pp. 115-127, Sep. 2012. doi: 10.1609/aimag.v33i3.2430
  - *Relevance:* Describes Goanna static analysis tool for C/C++ and seL4 microkernel verification.

---

## 7. Synthesis and Gaps

### 7.1 Key Themes

**1. AI/ML-Enhanced Translation:**
- LLM-based approaches show promise for semantic understanding and code transformation
- Resampling techniques improve translation accuracy through iterative analysis
- Type annotation and variable mapping are critical for preserving semantic fidelity

**2. Formal Verification Methodologies:**
- Dafny provides accessible program verification with Z3 backend
- Intermediate languages (HLL, OSL) enable correctness guarantees through equivalence proofs
- Memory safety verification remains a critical concern for C code translation

**3. Legacy Modernization Practices:**
- Semi-automated migration with metaprogramming reduces error introduction
- Incremental modernization during maintenance activities is common practice
- Community adoption of modern features follows available tooling support

**4. Model-Driven Engineering:**
- Abstract models provide platform-independent specifications
- Automated code generation with correctness guarantees reduces manual errors
- Testing and validation frameworks must accompany code generation

### 7.2 Research Gaps

**Identified Gap:** Limited academic literature on direct C-to-Kotlin translation.

**Mitigation Strategies:**
1. **Adapt existing methodologies:** Apply ST-to-C translation techniques (Han et al.) in reverse for C-to-Kotlin
2. **Leverage OO inference:** Use M2K methodology (Cassol & Arevalo) to generate Kotlin class structures from C procedural code
3. **Formal verification integration:** Combine Dafny verification with translation pipeline to ensure semantic equivalence
4. **Incremental modernization:** Follow industrial practices (Schuts et al., Lucas et al.) for large-scale migration

### 7.3 Implications for Mumps II Translation Project

**For Translation Without Dafny (Track A):**
- Use LLM-based semantic analysis to understand Mumps II C code
- Apply M2K methodology to infer Kotlin class structures
- Implement iterative refinement with resampling techniques
- Validate through extensive testing and comparison with original behavior

**For Translation With Dafny (Track B):**
- Develop formal specifications in Dafny for critical Mumps II components
- Use intermediate representation (similar to HLL or OSL) for verification
- Generate Kotlin code with correctness guarantees through equivalence proofs
- Verify memory safety properties using ownership-based approaches
- Integrate Dafny verification into CI/CD pipeline

**Common Considerations:**
- Address language paradigm differences (procedural to OO)
- Handle memory management (manual in C, automatic in Kotlin)
- Preserve compiler-specific optimizations and behaviors
- Maintain compatibility with M/Mumps language standard

---

## 8. References

*(Note: All citations follow IEEE Style: Author, Initials, Year, "Title," Journal, Volume, Pages, DOI/URL)*

### Academic Papers - C Translation and Modernization

[1] B. Han, C. Li, H. Deng, G. Liu, and Z. Zheng, "Domain-specific translation tool from structured text to C source code with code readability enhancement in programmable logic controllers," *Concurrency and Computation: Practice and Experience*, vol. 36, no. 15, Apr. 2024. doi: 10.1002/cpe.8100

[2] A. Moss, R. Schluntz, and P. A. Buhr, "Cforall: Adding modern programming language features to C," *Software: Practice and Experience*, vol. 48, no. 12, pp. 2111-2146, Aug. 2018. doi: 10.1002/spe.2624

[3] I. Cassol and G. Arévalo, "A methodology to infer and refactor an object-oriented model from C applications," *Software: Practice and Experience*, vol. 48, no. 3, pp. 550-577, Oct. 2017. doi: 10.1002/spe.2549

[4] N. Ge, A. Dieumegard, E. Jenn, and L. Voisin, "Correct-by-construction specification to verified code," *Journal of Software: Evolution and Process*, vol. 30, no. 10, Jun. 2018. doi: 10.1002/smr.1959

### Academic Papers - Formal Verification

[5] G. Caso, D. Garbervetsky, and D. Gorin, "Integrated program verification tools in education," *Software: Practice and Experience*, vol. 43, no. 4, pp. 403-418, Jul. 2012. doi: 10.1002/spe.2143

[6] X. Yin, Z. Huang, S. Kan, G. Shen, Z. Chen, Y. Liu, and F. Wang, "SafeOSL: Ensuring memory safety of C via ownership-based intermediate language," *Software: Practice and Experience*, vol. 52, no. 5, pp. 1114-1142, Dec. 2021. doi: 10.1002/spe.3057

[7] C. T. Cook, Y. Sun, and M. Sitaraman, "Experience report: evolution of a web-integrated software development and verification environment," *Software: Practice and Experience*, vol. 45, no. 6, pp. 857-872, Feb. 2014. doi: 10.1002/spe.2259

[8] M. T. Schuts, R. T. van Aarssen, P. M. Tielemans, and J. J. Vinju, "Large-scale semi-automated migration of legacy C/C++ test code," *Software: Practice and Experience*, vol. 52, no. 7, pp. 1543-1580, Mar. 2022. doi: 10.1002/spe.3082

[9] W. Lucas, F. Carvalho, R. C. Nunes, R. Bonifácio, J. Saraiva, and P. Accioly, "Embracing modern C++ features: An empirical assessment on the KDE community," *Journal of Software: Evolution and Process*, vol. 36, no. 5, Aug. 2023. doi: 10.1002/smr.2605

[10] S. Bonfanti, A. Gargantini, and A. Mashkoor, "Design and validation of a C++ code generator from Abstract State Machines specifications," *Journal of Software: Evolution and Process*, vol. 32, no. 2, Nov. 2019. doi: 10.1002/smr.2205

### Academic Papers - Formal Methods and Verification Case Studies

[11] Y. Choi, "Model checking Trampoline OS: a case study on safety analysis for automotive software," *Software Testing, Verification and Reliability*, vol. 24, no. 1, pp. 38-60, Aug. 2012. doi: 10.1002/stvr.1482

[12] N. Barnes, P. Baumgartner, T. Caetano, H. Durrant-Whyte, G. Klein, P. Sanderson, A. Sattar, P. Stuckey, S. Thiebaux, P. Van Hentenryck, and T. Walsh, "AI@NICTA," *AI Magazine*, vol. 33, no. 3, pp. 115-127, Sep. 2012. doi: 10.1609/aimag.v33i3.2430

[13] P. Kang, "Function call interception techniques," *Software: Practice and Experience*, vol. 48, no. 3, pp. 385-401, May 2017. doi: 10.1002/spe.2501

### Additional Supporting Literature

[14] W. Khan, F. A. Khan, A. Derhab, A. Alhudhaif, and H. Chen, "CoCEC: An Automatic Combinational Circuit Equivalence Checker Based on the Interactive Theorem Prover," *Complexity*, vol. 2021, May 2021. doi: 10.1155/2021/5525539

[15] S. W. Nabi, W. Vanderbauwhede, and J. Kalomiros, "Automatic Pipelining and Vectorization of Scientific Code for FPGAs," *International Journal of Reconfigurable Computing*, vol. 2019, Nov. 2019. doi: 10.1155/2019/7348013

[16] W. Zhou, Y. Zhao, Y. Zhang, Y. Wang, and M. Yin, "A comprehensive survey of UPPAAL-assisted formal modeling and verification," *Software: Practice and Experience*, vol. 55, no. 2, pp. 272-297, Sep. 2024. doi: 10.1002/spe.3372

---

**Document Status:** Complete - Ready for Git commit and push to mumps-research repository
**Next Steps:** Review and integrate findings into research design methodology
**File Location:** LITERATURE_REVIEW.md (committed to mumps-research Git repository)

---

## 9. Additional Monographic Sources

This section contains primary, secondary, and tertiary monographic sources for extended literature review. Sources are categorized by their relevance to the C-to-Kotlin translation research project.

### 9.1 Primary Monographic Sources

**GitHub and Version Control:**

[17] P. Bell and B. Beer, *Introducing GitHub*. O'Reilly Media, 2014.

[18] C. Dawson and B. Straub, *Building Tools with GitHub*. O'Reilly Media, 2014.

[19] A. Ayodele, *GitHub Foundations Certification Guide*. Packt Publishing, 2023.

### 9.2 Secondary Monographic Sources

**Software Engineering Fundamentals:**

[20] S. McConnell, *Code Complete: A Practical Handbook of Software Construction*, 2nd ed. Microsoft Press, 2004.

**Kotlin Language Resources:**

[21] P.-Y. Saumont, *The Joy of Kotlin*. Manning Publications, 2019.

[22] D. Jemerov and S. Isakova, *Kotlin in Action*. Manning Publications, 2017.

### 9.3 Tertiary Monographic Sources

**Software Design Patterns:**

[23] B. A. Forouzan, *Data Structures and Algorithms with Object Oriented Design Patterns in C++*. Cengage Learning, 2010.

[24] M. Fowler, *Analysis Patterns: Reusable Object Models*. Addison-Wesley, 1996.

[25] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley, 1994.

[26] A. Shalloway and J. R. Trott, *Design Patterns Explained: A New Perspective on Object Oriented Design*, 2nd ed. Addison-Wesley, 2004.

---

**Note:** Additional monographic sources will be added as they become available. This section serves as a placeholder for expanding the bibliography with books and extended works relevant to the research project.
