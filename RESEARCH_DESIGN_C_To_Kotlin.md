# Research Design: Machine-Assisted C-to-Kotlin Translation

**Project:** Comparative Study of Machine-Assisted C-to-Kotlin Translation  
**Document Type:** Research Design  
**Version:** 1.0  
**Date:** November 12, 2026  
**Author:** Mark Andrews

---

## Table of Contents

1. [Study Design Overview](#study-design-overview)
2. [Subject of Study: Mumps II Compiler](#subject-of-study-mumps-ii-compiler)
3. [Approach A: Direct Machine-Assisted Translation](#approach-a-direct-machine-assisted-translation)
4. [Approach B: Translation with Formal Verification](#approach-b-translation-with-formal-verification)
5. [Methodology Common to Both Approaches](#methodology-common-to-both-approaches)
6. [Detailed Work Packages](#detailed-work-packages)
7. [Quality Assurance](#quality-assurance)
8. [Data Collection and Metrics](#data-collection-and-metrics)
9. [Analysis Framework](#analysis-framework)
10. [Timeline and Milestones](#timeline-and-milestones)
11. [Resource Requirements](#resource-requirements)
12. [Risk Management](#risk-management)

---

## 1. Study Design Overview

### 1.1 Design Type

**Comparative Experimental Design** with two parallel treatment groups:

- **Treatment A:** Machine-assisted C-to-Kotlin translation WITHOUT formal verification
- **Treatment B:** Machine-assisted C-to-Kotlin translation WITH formal verification using Dafny

This is a **within-subjects comparison** where the same C source code (Mumps II compiler) is translated using both approaches, allowing direct comparison of outcomes.

### 1.2 Independent and Dependent Variables

**Independent Variables:**
- Translation approach (A vs. B)
- Use of formal verification (No vs. Yes)

**Dependent Variables:**
- Translation correctness (functional equivalence)
- Code quality metrics (complexity, maintainability, defects)
- Effort metrics (time, manual interventions, iterations)
- Resource usage (CPU, memory, verification time)
- Developer experience (subjective feedback)

### 1.3 Research Paradigm

**Mixed Methods:** Combining quantitative and qualitative approaches

- **Quantitative:** Metrics collection, statistical analysis, performance measurements
- **Qualitative:** Challenge documentation, pattern identification, best practices

**Approach:** Sequential explanatory design - collect quantitative data first, then use qualitative data to explain findings

---

## 2. Subject of Study: Mumps II Compiler

### 2.1 Overview

**Name:** Mumps II Compiler  
**Author:** Dr. Kevin O'Kane  
**Language:** C  
**Purpose:** Implementation of the MUMPS programming language

### 2.2 Source Code Characteristics

| Aspect | Details |
|--------|---------|
| **Language** | C (ANSI C or similar) |
| **Size** | Estimated 5,000-50,000 lines (to be confirmed) |
| **Modules** | Multiple source files, header files |
| **Complexity** | Medium to high (compiler implementation) |
| **Features** | Lexical analysis, parsing, code generation, optimization |
| **Dependencies** | Standard C libraries, possibly third-party components |

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

### 2.4 Code Preprocessing

Before translation, the following preprocessing steps will be applied to both approaches:

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

4. **Testing Baseline**
   - Establish existing test suite (if available)
   - Create characterization tests
   - Document expected behavior

---

## 3. Approach A: Direct Machine-Assisted Translation

### 3.1 Overview

**Goal:** Translate Mumps II compiler from C to Kotlin using machine-assisted tools WITHOUT formal verification.

**Characteristics:**
- Focus on functional equivalence
- Emphasis on automation
- Manual intervention as needed
- Testing-based validation

### 3.2 Detailed Methodology

#### Phase A1: Preparation and Analysis (Months 1-2)

**Objective:** Understand the codebase and prepare for translation

**Tasks:**

1. **Code Inventory**
   - List all source files
   - Count lines of code
   - Identify file types (source, header, build scripts)
   - Document build process

2. **Dependency Mapping**
   ```bash
   # Use tools to create dependency graph
   clang -M *.c > dependencies.txt
   # or use custom scripts
   ```

3. **Complexity Analysis**
   - Identify most complex functions
   - Find deeply nested control structures
   - Locate pointer-intensive code
   - Identify macro usage patterns

4. **Build and Test**
   - Set up build environment
   - Compile original C code
   - Run existing tests (if any)
   - Create baseline tests

**Deliverables:**
- Code inventory report
- Dependency graph
- Complexity assessment
- Working build environment
- Baseline test suite

#### Phase A2: Tool Selection and Setup (Month 2-3)

**Objective:** Select and configure translation tools

**Tool Categories:**

1. **Parsing/Analysis**
   - Clang/LLVM (for AST analysis)
   - Tree-sitter (for syntax parsing)
   - Custom lexer/parser (if needed)

2. **Translation**
   - Custom translation scripts
   - Existing C-to-Kotlin tools (if available)
   - IDE-based refactoring tools

3. **Code Generation**
   - Kotlin code generators
   - Template engines
   - Custom code emitters

4. **Testing**
   - JUnit/Kotlin Test
   - Property-based testing (Kotest)
   - Test coverage tools (JaCoCo)

**Selection Criteria:**
- Open source availability
- Documentation quality
- Community support
- Integration capability

**Deliverables:**
- Tool evaluation report
- Selected tool configuration
- Initial translation pipeline

#### Phase A3: Translation Execution (Months 3-12)

**Objective:** Translate C code to Kotlin

**Process:**

1. **Pilot Translation**
   - Select 1-2 simple modules
   - Translate manually to establish patterns
   - Identify translation challenges
   - Refine translation rules

2. **Automated Translation**
   - Apply automated tools to remaining code
   - Handle straightforward cases automatically
   - Flag complex cases for manual review

3. **Manual Intervention**
   - Resolve pointer-to-array translations
   - Handle memory management (GC vs. manual)
   - Adapt C idioms to Kotlin idioms
   - Fix type mismatches

4. **Iterative Refinement**
   - Run tests after each module
   - Fix compilation errors
   - Address runtime errors
   - Refactor for Kotlin best practices

**Translation Patterns:**

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
- Complete Kotlin translation (Approach A)
- Translation log (challenges, decisions)
- Test results
- Compilation-ready code

#### Phase A4: Testing and Validation (Months 12-15)

**Objective:** Verify functional equivalence

**Testing Strategy:**

1. **Unit Testing**
   - Test individual functions
   - Verify return values
   - Check edge cases

2. **Integration Testing**
   - Test module interactions
   - Verify data flow
   - Check interface compatibility

3. **System Testing**
   - Test complete compiler
   - Compile sample MUMPS programs
   - Verify output correctness

4. **Regression Testing**
   - Re-run original C tests
   - Compare outputs
   - Identify discrepancies

**Validation Approach:**

1. **Equivalence Testing**
   - Input-output equivalence
   - Behavior equivalence
   - Performance equivalence

2. **Differential Testing**
   - Compare outputs between C and Kotlin versions
   - Identify and investigate differences
   - Document known differences

3. **Mutation Testing**
   - Introduce faults and check detection
   - Measure test suite effectiveness

**Deliverables:**
- Complete test suite
- Test results report
- Functional equivalence assessment
- List of known differences

---

## 4. Approach B: Translation with Formal Verification

### 4.1 Overview

**Goal:** Translate Mumps II compiler from C to Kotlin WITH formal verification using Dafny.

**Characteristics:**
- Translation with simultaneous verification
- Proof of correctness properties
- Formal specifications
- Guaranteed functional equivalence

### 4.2 Detailed Methodology

#### Phase B1: Dafny Setup and Learning (Months 1-3)

**Objective:** Learn Dafny and set up verification environment

**Learning Path:**

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
   - Temporal logic

**Resources:**
- Dafny documentation and tutorials
- Formal verification textbooks
- Online courses (e.g., Software Foundations)
- Case studies of verified C code

**Deliverables:**
- Dafny learning notes
- Verification environment setup
- Simple verification examples

#### Phase B2: Specification Development (Months 3-6)

**Objective:** Develop formal specifications for the Mumps II compiler

**Specification Types:**

1. **Function Specifications**
   ```dafny
   function Method1(input: int): int
     requires input >= 0
     ensures input <= Method1 <= input * 2
   ```

2. **Data Structure Specifications**
   - Invariants for symbol tables
   - Properties of AST nodes
   - Constraints on global state

3. **Module Specifications**
   - Module-level invariants
   - Interface contracts
   - Integration properties

4. **System Specifications**
   - Overall compiler correctness
   - Soundness and completeness properties
   - Termination guarantees

**Specification Process:**

1. **Identify Critical Properties**
   - What must be true about inputs?
   - What must be true about outputs?
   - What invariants must hold?
   - What are the error conditions?

2. **Develop Specifications**
   - Start with simple functions
   - Build up to complex modules
   - Use abstract specifications where needed

3. **Verify Specifications**
   - Check that specifications are well-formed
   - Verify that specifications are satisfiable
   - Refine based on verification feedback

**Deliverables:**
- Complete specification document
- Dafny specification files
- Specification verification results

#### Phase B3: Verified Translation (Months 6-18)

**Objective:** Translate and verify the Mumps II compiler

**Process:**

1. **Module-by-Module Translation**
   - Translate one module at a time
   - Verify each module before proceeding
   - Build up verified components

2. **Translation with Verification**
   ```
   For each C function:
   1. Translate to Kotlin
   2. Add formal specifications
   3. Verify with Dafny
   4. Fix verification failures
   5. Repeat until verified
   ```

3. **Verification Strategies**
   - **Loop Invariants:** For iterative algorithms
   - **Pre/Post Conditions:** For function contracts
   - **Data Invariants:** For data structure integrity
   - **Termination Measures:** For recursion and loops

4. **Handling Challenges**
   - **Pointers:** Use Dafny's heap model
   - **Memory Management:** Verify memory safety properties
   - **Macros:** Unfold or abstract macros
   - **Global State:** Use module-level specifications

**Translation Patterns with Verification:**

| C Construct | Kotlin + Dafny |
|-------------|----------------|
| Pointer dereference | Use `requires` for non-null, `ensures` for valid state |
| Memory allocation | Verify allocation succeeds, track ownership |
| Array indexing | Use `requires 0 <= i < arr.Length` |
| Function calls | Check preconditions, rely on postconditions |

**Deliverables:**
- Partially translated and verified code
- Verification logs
- Proof obligations and resolutions
- Verification statistics

#### Phase B4: Integration and Verification (Months 18-21)

**Objective:** Integrate verified components and verify system properties

**Integration Process:**

1. **Component Integration**
   - Integrate verified modules
   - Verify interface compatibility
   - Check system invariants

2. **System-Level Verification**
   - Verify compiler soundness
   - Verify compiler completeness
   - Verify termination

3. **Property Verification**
   - Verify type safety
   - Verify memory safety
   - Verify functional correctness

**Verification Metrics:**
- Number of verification conditions
- Proof success rate
- Verification time per module
- Total verification effort

**Deliverables:**
- Complete verified translation
- System verification report
- Verification metrics
- Proof documentation

#### Phase B5: Testing and Validation (Months 21-24)

**Objective:** Validate the verified translation

**Testing Approach:**

1. **Verification-Based Testing**
   - Use verified properties as test oracles
   - Generate test cases from specifications
   - Check that tests pass verification

2. **Traditional Testing**
   - Same as Approach A
   - Compare results with Approach A
   - Identify verification gaps

3. **Verification Coverage**
   - Measure what percentage of code is verified
   - Identify unverified components
   - Assess risk of unverified code

**Deliverables:**
- Verified test results
- Validation report
- Coverage analysis

---

## 5. Methodology Common to Both Approaches

### 5.1 Code Structure Analysis

Both approaches will use the same code structure analysis to ensure comparability.

**Analysis Tools:**
- Clang-based static analysis
- Custom analysis scripts
- Dependency graph generators

**Analysis Deliverables:**
- Function call graphs
- Data flow diagrams
- Control flow graphs
- Complexity metrics (cyclomatic, Halstead, etc.)

### 5.2 Kotlin Target Specification

Both approaches will target the same Kotlin version and style.

**Kotlin Version:** Latest stable version at project start  
**Style Guide:** Kotlin official style guide  
**Conventions:**
- Use idiomatic Kotlin
- Prefer immutable data
- Use null safety
- Follow functional programming patterns where appropriate

### 5.3 Testing Framework

Both approaches will use the same testing framework for comparability.

**Testing Tools:**
- JUnit 5 (for unit testing)
- Kotest (for property-based testing)
- JaCoCo (for code coverage)
- Custom test comparators

**Test Categories:**
1. **Unit Tests:** Individual functions
2. **Integration Tests:** Module interactions
3. **System Tests:** Complete compiler
4. **Regression Tests:** Known behaviors
5. **Property Tests:** Formal properties

### 5.4 Metrics Collection

Both approaches will collect the same metrics for comparison.

**Code Metrics:**
- Lines of code (C vs. Kotlin)
- Cyclomatic complexity
- Halstead metrics
- Function count
- Variable count
- Comment ratio

**Translation Metrics:**
- Translation time per module
- Manual intervention count
- Automated translation percentage
- Manual intervention percentage

**Quality Metrics:**
- Defect count
- Test coverage percentage
- Test success rate
- Verification success rate (Approach B only)

---

## 6. Detailed Work Packages

### WP1: Preparation and Learning (Months 1-3)

**Objective:** Prepare for translation and learn necessary technologies

**Tasks:**
- [ ] Obtain and analyze Mumps II C source code
- [ ] Set up development environment
- [ ] Learn Kotlin (refresh/advanced)
- [ ] Learn Dafny (for Approach B)
- [ ] Select and configure tools
- [ ] Create baseline build and test
- [ ] Document code structure

**Deliverables:**
- Environment setup documentation
- Learning notes
- Code analysis report
- Baseline test suite

**Effort:** ~120 hours

### WP2: Approach A - Direct Translation (Months 4-15)

**Objective:** Complete translation without formal verification

**Tasks:**
- [ ] Preprocess source code
- [ ] Translate simple modules
- [ ] Develop translation patterns
- [ ] Translate complex modules
- [ ] Handle edge cases
- [ ] Refine translation
- [ ] Test and validate

**Deliverables:**
- Approach A translation (Kotlin)
- Translation log
- Test results
- Validation report

**Effort:** ~600 hours

### WP3: Approach B - Verified Translation (Months 16-24)

**Objective:** Complete translation with formal verification

**Tasks:**
- [ ] Develop formal specifications
- [ ] Translate with verification
- [ ] Prove correctness properties
- [ ] Integrate verified components
- [ ] Verify system properties
- [ ] Test and validate

**Deliverables:**
- Approach B translation (Kotlin + Dafny)
- Formal specifications
- Verification logs
- Proof documentation
- Validation report

**Effort:** ~900 hours (estimated 50% more than Approach A)

### WP4: Comparative Analysis (Months 25-30)

**Objective:** Compare both approaches and analyze results

**Tasks:**
- [ ] Collect metrics from both approaches
- [ ] Perform statistical analysis
- [ ] Identify differences and patterns
- [ ] Assess quality metrics
- [ ] Evaluate effort and cost
- [ ] Document findings

**Deliverables:**
- Comparative analysis report
- Statistical analysis
- Findings documentation
- Recommendations

**Effort:** ~300 hours

### WP5: Documentation and Publication (Months 31-48)

**Objective:** Document results and publish findings

**Tasks:**
- [ ] Write research papers
- [ ] Create presentations
- [ ] Develop documentation
- [ ] Prepare code for release
- [ ] Submit to conferences/journals
- [ ] Present findings

**Deliverables:**
- Research papers (2-3)
- Conference presentations
- Documentation
- Open source code (if possible)

**Effort:** ~400 hours

---

## 7. Quality Assurance

### 7.1 Quality Control Measures

1. **Code Reviews**
   - Regular code reviews of translations
   - Peer review of specifications and proofs
   - External validation of results

2. **Testing**
   - Comprehensive test coverage
   - Automated test execution
   - Continuous integration

3. **Verification**
   - Formal verification (Approach B)
   - Static analysis
   - Property checking

4. **Documentation**
   - Complete documentation of all steps
   - Decision rationale documentation
   - Change logging

### 7.2 Scientific Rigor

1. **Reproducibility**
   - Version control for all code and data
   - Documented build and test processes
   - Preserved experimental conditions

2. **Validity**
   - Construct validity: Are we measuring what we claim?
   - Internal validity: Are results caused by the treatment?
   - External validity: Do results generalize?
   - Reliability: Are measurements consistent?

3. **Triangulation**
   - Multiple data sources
   - Multiple analysis methods
   - Multiple validation approaches

### 7.3 Validation Strategy

| Validation Type | Approach A | Approach B |
|-----------------|-----------|-----------|
| Functional Testing | ✅ | ✅ |
| Unit Testing | ✅ | ✅ |
| Integration Testing | ✅ | ✅ |
| Formal Verification | ❌ | ✅ |
| Differential Testing | ✅ | ✅ |
| Regression Testing | ✅ | ✅ |
| Property Testing | ✅ | ✅ |

---

## 8. Data Collection and Metrics

### 8.1 Metrics Framework

**Four Dimensions of Measurement:**

1. **Translation Effectiveness**
   - Percentage of code translated automatically
   - Manual intervention rate
   - Translation accuracy

2. **Code Quality**
   - Defect density
   - Test coverage
   - Complexity metrics
   - Maintainability index

3. **Effort**
   - Time spent per module
   - Number of iterations
   - Developer hours

4. **Verification** (Approach B only)
   - Verification success rate
   - Proof obligation count
   - Verification time
   - Proof complexity

### 8.2 Data Collection Plan

| Metric | Collection Method | Frequency |
|--------|-------------------|-----------|
| LOC (C) | cloc, custom scripts | Per module, final |
| LOC (Kotlin) | cloc, custom scripts | Per module, final |
| Translation time | Time tracking | Per session |
| Manual changes | Git commits | Per change |
| Test coverage | JaCoCo | Per test run |
| Defect count | Issue tracking | Continuous |
| Verification count | Dafny logs | Per verification |
| Verification time | Timing | Per verification |

### 8.3 Data Analysis

**Statistical Analysis:**
- Descriptive statistics (means, medians, standard deviations)
- Comparative statistics (t-tests, Mann-Whitney U tests)
- Correlation analysis
- Regression analysis

**Qualitative Analysis:**
- Pattern identification
- Challenge categorization
- Solution effectiveness assessment
- Lesson learned extraction

### 8.4 Expected Data Outputs

1. **Raw Data:**
   - Metrics database (CSV, JSON)
   - Log files
   - Test results

2. **Processed Data:**
   - Statistical summaries
   - Visualizations (charts, graphs)
   - Comparison tables

3. **Analysis Reports:**
   - Quantitative analysis report
   - Qualitative analysis report
   - Integrated findings report

---

## 9. Analysis Framework

### 9.1 Comparative Analysis

**Comparison Dimensions:**

| Dimension | Approach A | Approach B | Comparison Method |
|-----------|-----------|-----------|-------------------|
| Translation Time | Measured | Measured | Ratio, difference |
| Manual Effort | Measured | Measured | Ratio, difference |
| Code Quality | Metrics | Metrics | Statistical test |
| Defect Rate | Measured | Measured | Ratio, difference |
| Test Coverage | Measured | Measured | Statistical test |
| Verification | N/A | Measured | Descriptive |
| Correctness | Tested | Verified | Equivalence test |

### 9.2 Hypothesis Testing

**Statistical Tests:**

1. **H1: Translation Coverage**
   - Null: No difference in automatic translation percentage
   - Alternative: Approach A has higher automatic translation percentage
   - Test: Two-sample t-test

2. **H2: Effort Difference**
   - Null: No difference in total effort
   - Alternative: Approach B requires more effort
   - Test: Paired t-test (same code translated both ways)

3. **H3: Quality Difference**
   - Null: No difference in defect density
   - Alternative: Approach B has lower defect density
   - Test: Mann-Whitney U test (non-parametric)

### 9.3 Cost-Benefit Analysis

**Cost Factors:**
- Development time
- Learning curve
- Tool acquisition and setup
- Computational resources
- Maintenance effort

**Benefit Factors:**
- Code quality
- Correctness guarantees
- Maintainability
- Documentation quality
- Reusability

**Analysis:**
- Cost per line of code
- Cost per defect prevented
- ROI calculation
- Break-even analysis

---

## 10. Timeline and Milestones

### 10.1 Overall Timeline

```
Year 1 (2026-2027):  Preparation, Approach A Translation
├── Months 1-3:  Preparation and Learning
├── Months 4-12: Approach A Translation Execution
└── Months 12-15: Approach A Testing and Validation

Year 2 (2027-2028):  Approach B Translation
├── Months 13-18: Approach B Translation with Verification
├── Months 18-21: Approach B Integration and Verification
└── Months 21-24: Approach B Testing and Validation

Year 3 (2028-2029):  Analysis and Documentation
├── Months 25-30: Comparative Analysis
└── Months 31-36: Initial Documentation and Publication

Year 4 (2029-2030):  Finalization
├── Months 37-45: Additional Analysis and Refinement
└── Months 46-48: Final Documentation and Publication
```

### 10.2 Key Milestones

| Milestone | Target Date | Description |
|-----------|-------------|-------------|
| M1 | Month 3 | Preparation complete, tools selected |
| M2 | Month 12 | Approach A translation complete |
| M3 | Month 15 | Approach A testing complete |
| M4 | Month 18 | Approach B specifications complete |
| M5 | Month 24 | Approach B translation complete |
| M6 | Month 30 | Comparative analysis complete |
| M7 | Month 36 | First paper submitted |
| M8 | Month 48 | Project complete, final report |

### 10.3 Detailed Schedule

| Month | Approach A | Approach B | Analysis | Documentation |
|-------|------------|------------|----------|---------------|
| 1-3 | Preparation | Learning | | |
| 4-6 | Simple modules | Spec dev | | |
| 7-9 | Complex modules | Verified translation | | |
| 10-12 | Edge cases | Complex modules | | |
| 13-15 | Testing | Integration | | |
| 16-18 | | Verification | | |
| 19-21 | | Testing | | |
| 22-24 | | | Metrics collection | |
| 25-27 | | | Statistical analysis | |
| 28-30 | | | Pattern analysis | Paper outline |
| 31-33 | | | | Paper writing |
| 34-36 | | | | Paper submission |
| 37-39 | | | | Refinement |
| 40-42 | | | | Additional papers |
| 43-45 | | | | Final review |
| 46-48 | | | | Final report |

---

## 11. Resource Requirements

### 11.1 Human Resources

| Role | Required Hours | Notes |
|------|----------------|-------|
| Principal Investigator | ~1,800 | Part-time over 4 years |
| Technical Reviewer | ~100 | External validation |
| Peer Reviewer | ~50 | Paper review |
| Total | ~1,950 | |

### 11.2 Hardware Resources

| Resource | Specification | Quantity |
|----------|---------------|----------|
| Development Machine | MacBook Pro (current) | 1 |
| Additional Machine | Linux server for verification | 1 (optional) |
| Storage | For code, data, backups | 100GB+ |

### 11.3 Software Resources

| Software | Purpose | License |
|----------|---------|---------|
| Kotlin | Target language | Open Source |
| Dafny | Formal verification | Open Source |
| Clang/LLVM | Code analysis | Open Source |
| JUnit | Testing | Open Source |
| Kotest | Property testing | Open Source |
| JaCoCo | Coverage | Open Source |
| Git | Version control | Open Source |
| IDE | Development | Open Source/Free |

### 11.4 Financial Resources

| Item | Estimated Cost | Notes |
|------|----------------|-------|
| Additional hardware | $0-$2,000 | Optional server |
| Software licenses | $0 | All open source |
| Conference fees | $500-$2,000 | Optional presentations |
| Publication fees | $0-$1,500 | Open access options |
| Total | $500-$5,500 | Best case - worst case |

---

## 12. Risk Management

### 12.1 Risk Identification

| Risk | Likelihood | Impact | Category |
|------|------------|--------|----------|
| Complexity of Mumps II code | High | High | Technical |
| Dafny learning curve | High | High | Skill |
| Tool limitations | Medium | High | Technical |
| Time overrun | Medium | Medium | Schedule |
| Resource limitations | Low | Medium | Resource |
| Verification intractability | Medium | High | Technical |
| Competing priorities | Medium | Medium | Personal |
| Health issues | Low | High | Personal |

### 12.2 Risk Mitigation Strategies

#### High-Impact Technical Risks

**Risk: Complexity of Mumps II code**
- **Mitigation:** Start with simpler modules, build expertise gradually
- **Contingency:** Focus on representative subset if full translation is infeasible
- **Success Criteria:** At least 80% of code translated

**Risk: Dafny learning curve**
- **Mitigation:** Dedicated learning phase, use tutorials and examples
- **Contingency:** Start with Approach A, then add verification incrementally
- **Success Criteria:** Ability to verify simple modules within 3 months

**Risk: Tool limitations**
- **Mitigation:** Evaluate multiple tools, have backup options
- **Contingency:** Develop custom tools if needed
- **Success Criteria:** Working translation pipeline by Month 3

**Risk: Verification intractability**
- **Mitigation:** Use abstraction, modular verification, incremental approach
- **Contingency:** Focus verification on critical components only
- **Success Criteria:** Verify at least 50% of codebase

#### Schedule Risks

**Risk: Time overrun**
- **Mitigation:** Realistic timeline, modular approach, regular progress tracking
- **Contingency:** Adjust scope, extend timeline, reduce deliverables
- **Success Criteria:** Major milestones met within 6 months of target

**Risk: Competing priorities**
- **Mitigation:** Dedicated time blocks, priority setting, progress tracking
- **Contingency:** Reduce scope, extend timeline
- **Success Criteria:** Consistent 10+ hours/week on project

#### Resource Risks

**Risk: Resource limitations**
- **Mitigation:** Use efficient tools, cloud resources if needed
- **Contingency:** Reduce scope, focus on most important aspects
- **Success Criteria:** All essential resources available

**Risk: Health issues**
- **Mitigation:** Regular breaks, healthy work habits
- **Contingency:** Extended timeline, reduced scope
- **Success Criteria:** Maintain consistent progress

### 12.3 Risk Monitoring

**Monthly Risk Review:**
- Assess risk likelihood and impact
- Evaluate mitigation effectiveness
- Update contingency plans
- Document risk status

**Risk Indicators:**
- Delays in milestone completion
- Increasing complexity of translation
- Verification failures
- Resource shortages
- Health problems

---

## 13. Success Criteria

### 13.1 Primary Success Criteria

1. **Translation Completion**
   - Approach A: 80%+ of Mumps II compiler translated to Kotlin
   - Approach B: 50%+ of Mumps II compiler translated and verified

2. **Functional Equivalence**
   - Both translations produce equivalent outputs for test cases
   - No critical functionality lost in translation

3. **Quality Improvement**
   - Approach B has fewer defects than Approach A
   - Both translations have better quality than original C code

4. **Comparative Analysis**
   - Complete data collection for both approaches
   - Statistical analysis of differences
   - Clear findings and recommendations

5. **Publication**
   - At least one peer-reviewed publication
   - Conference presentation
   - Open source code release (if possible)

### 13.2 Secondary Success Criteria

1. **Tool Contributions**
   - Improvements to translation tools
   - Contributions to Dafny or related tools
   - New tools or scripts developed

2. **Methodology Development**
   - Reproducible methodology for C-to-Kotlin translation
   - Guidelines for formal verification of legacy code
   - Best practices documentation

3. **Community Impact**
   - Adoption of methodology by others
   - Citations of published work
   - Contributions to open source community

---

## 14. Conclusion

This research design outlines a comprehensive approach to comparing machine-assisted C-to-Kotlin translation with and without formal verification. By systematically applying two different methodologies to the same codebase (Mumps II compiler), we can empirically determine the benefits, costs, and trade-offs of formal verification in legacy code modernization.

The study is designed to be:
- **Rigorous:** Using established scientific methods and statistical analysis
- **Reproducible:** With complete documentation and version control
- **Practical:** Focusing on a real-world codebase with real challenges
- **Useful:** Providing actionable insights for software engineering practice

While the project faces challenges, particularly the complexity of the Mumps II codebase and the learning curve for formal verification, the extended timeline and modular approach provide adequate time for successful completion.

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

---

## Appendix B: File Naming Convention

All project files will follow this naming convention:

- `RESEARCH_PROPOSAL_C_To_Kotlin.md` - This proposal
- `RESEARCH_DESIGN_C_To_Kotlin.md` - This design
- `APPROACH_A_* ` - Files related to Approach A
- `APPROACH_B_* ` - Files related to Approach B
- `ANALYSIS_* ` - Analysis files
- `DATA_* ` - Data files
- `DOCS_* ` - Documentation files

---

## Appendix C: Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-11-12 | Initial version | Mark Andrews |

---

*Document Status: Draft*  
*Next Review: After tool selection and environment setup*  
*Repository: /home/mark/projects/mumps-research*
