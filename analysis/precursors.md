# Precursor Languages to MUMPS/M Programming Language

## Overview

The MUMPS programming language did not emerge in isolation. It was influenced by a lineage of earlier interactive, time-sharing programming languages that pioneered conversational computing paradigms. This document traces the precursor languages that contributed to MUMPS development, extending back to the early 1960s and considering the broader historical context of computing before widespread computer availability.

## Pre-Computer Manual Processes (Pre-1930s)

Before the advent of electronic computers, medical and administrative processes relied on manual systems:

- **Paper-based medical records**: Patient information was maintained in physical files, requiring manual retrieval and updating
- **Tabulating machines**: Early mechanical devices for processing punch cards, used for census data and basic calculations
- **Manual indexing systems**: Libraries and medical facilities used card catalogs and index systems for information retrieval
- **Human computation**: Teams of human "computers" (often women) performed calculations for scientific and engineering projects

These manual processes established patterns of information organization that would later influence database design in MUMPS.

## Early Computing Influences (1930-1960)

### Pre-1960 Computing Paradigms

While not direct precursors, several developments laid groundwork for MUMPS concepts:

- **1940s: Stored-program computers**: The concept of storing programs in memory alongside data
- **1950s: Batch processing**: Early mainframe systems processed jobs in batches, but lacked interactivity
- **1950s-1960s: Time-sharing concepts**: Pioneering work at MIT and elsewhere on making computers accessible to multiple users simultaneously
- **Medical informatics**: Early attempts at computerizing hospital records in the 1950s and early 1960s

### JOSS Lineage

The most direct precursor lineage to MUMPS comes from the JOSS family of languages:

## 1. JOSS (JOHNNIAC Open Shop System) - 1963

**Developer**: RAND Corporation
**First Implemented**: 1963
**Platform**: JOHNNIAC computer (retired 1966, replaced by PDP-6)
**Significance**: One of the first interactive, time-sharing programming languages

### Key Features and Innovations

- **Interactive computation**: Users could enter commands and see immediate results
- **Line numbers**: Used as both editing instructions and branch targets
- **Conversational interface**: Built-in source code editor with direct/immediate mode
- **Boolean decisions**: Statements could be predicated by Boolean conditions
- **Multi-user support**: Supported hundreds of terminals (IBM Selectric-based)

### Historical Context

- Put online at RAND in 1963
- Proved extremely popular, quickly bogging down the JOHNNIAC machine
- By 1964, a replacement machine was sought
- JOHNNIAC retired in 1966 and replaced by PDP-6
- Used green ink for user input, black ink for computer responses
- Unknown commands elicited the response "Eh?"

### Influence on Later Languages

JOSS spawned numerous ports and offshoots:
- **Similar to original**: TELCOMP, STRINGCOMP, CAL, CITRAN, ISIS, PIL/I, JEAN, BOSS, INTERP
- **Distinctive directions**: FOCAL, **MUMPS**
- **Influence on**: BASIC interpreters (similar syntax, differing in details)

## 2. TELCOMP - ~1964

**Developer**: Bolt, Beranek and Newman (BBN)
**Developed**: Approximately 1964
**First Revenue**: October 1965
**Platform**: Initially PDP-1
**Status**: In use until at least 1974

### Development History

- Created after Cliff Shaw from RAND visited BBN labs in 1964 as part of an NIH survey
- BBN offered TELCOMP as a paid commercial service
- Service sold to On-Line Systems, Inc. (OLS) in 1972
- In the UK, offered by Time Sharing, Ltd. (TSL) - a partnership between BBN and Richard Evans

### Technical Characteristics

- **Interactive and conversational**: Direct descendant of JOSS
- **Commercial service**: First revenue in October 1965
- **Multiple versions**: TELCOMP I, TELCOMP II, TELCOMP III
- **Platform evolution**: Initially PDP-1, later other platforms including BBN's own TENEX operating system

### Influence

TELCOMP represented the first major commercial application of JOSS concepts and demonstrated the viability of interactive time-sharing as a business model.

## 3. STRINGCOMP - 1967

**Developer**: Bolt, Beranek and Newman (BBN)
**Developed**: 1967
**Classification**: One of three variants of JOSS II
**Related variants**: TELCOMP, FILECOMP

### Key Features

- **Extended string handling**: Augmented JOSS's mathematical focus with stronger string manipulation capabilities
- **Strong influence on MUMPS**: Directly contributed to the development of MUMPS

### Significance

STRINGCOMP represents a critical bridge between the mathematical orientation of JOSS and the database/string processing requirements that would define MUMPS. Its emphasis on string handling made it particularly suitable for text-based applications like medical records.

## 4. FILECOMP

**Developer**: Bolt, Beranek and Newman (BBN)
**Classification**: Variant of JOSS II
**Related variants**: TELCOMP, STRINGCOMP

### Purpose

- Designed for text file processing and composition
- Could read text files and compose them for typeset output

### Relationship to MUMPS

While less directly influential than STRINGCOMP, FILECOMP demonstrates the diversification of JOSS concepts into different application domains, some of which (text processing, file management) would be important in MUMPS.

## Direct Lineage to MUMPS (1966-1967)

### MUMPS Development Context

- **Developers**: Neil Pappalardo, Robert A. Greenes, and Curt Marble
- **Location**: Dr. Octo Barnett's lab at Massachusetts General Hospital (MGH), Boston
- **Timeframe**: 1966-1967
- **Hardware**: DEC PDP-7 (original), then backward-compatible PDP-9
- **Context**: NIH-supported hospital information systems project

### The Influence Pathway

```
RAND Corporation JOSS (1963)
    ↓
BBN TELCOMP (1964-1965)
    ↓
BBN STRINGCOMP (1967)
    ↓
MGH MUMPS (1966-1967)
```

**Important Note**: While STRINGCOMP was developed in 1967, MUMPS development began in 1966. This suggests either:
1. The MUMPS team had early knowledge of STRINGCOMP concepts, or
2. The development of STRINGCOMP and MUMPS occurred in parallel with mutual influence, or
3. The lineage relationship is more complex than a simple chronological sequence

### What MUMPS Inherited

From the JOSS lineage, MUMPS inherited:

1. **Interactive nature**: Direct manipulation of data and immediate feedback
2. **Conversational interface**: User-friendly command structure
3. **Database integration**: Building on STRINGCOMP's string handling, MUMPS integrated database access directly into the language
4. **Portability focus**: Unlike many systems of the era, MUMPS was designed to be portable between machines

### MUMPS Innovations

MUMPS added unique features not present in its predecessors:

1. **Integrated key-value database**: Direct, high-speed read-write access to permanent disk storage
2. **Multitasking**: Advanced feature for the era, allowing concurrent processes
3. **Hierarchical database file system**: Standardized interaction with data, abstracting disk operations
4. **Medical domain focus**: Specifically designed for hospital and medical record applications

## Timeline of Precursor Development

| Year | Event | Developer | Significance |
|------|-------|-----------|-------------|
| 1963 | JOSS first implemented | RAND Corporation | First interactive time-sharing language |
| 1964 | TELCOMP development begins | BBN | Commercial application of JOSS concepts |
| 1964 | JOHNNIAC retired | RAND | Replaced by PDP-6 |
| 1965 | TELCOMP first revenue | BBN | October 1965 |
| 1966 | MUMPS development begins | MGH | Parallel to STRINGCOMP development |
| 1966 | JOHNNIAC replacement | RAND | PDP-6 supports hundreds of terminals |
| 1967 | STRINGCOMP developed | BBN | Extended string handling |
| 1967 | MUMPS NIH funding secured | MGH | Serious implementation begins |
| 1967 | PDP-9 obtained | MGH | Backward-compatible upgrade |
| 1972 | TELCOMP service sold | BBN → OLS | Business transition |
| 1974 | TELCOMP in use | Various | Last confirmed usage |

## Technical Evolution

### From JOSS to MUMPS

| Feature | JOSS | TELCOMP | STRINGCOMP | MUMPS |
|---------|------|---------|------------|-------|
| Interactive | ✓ | ✓ | ✓ | ✓ |
| Line numbers | ✓ | ✓ | ✓ | ✓ |
| Boolean decisions | ✓ | ✓ | ✓ | ✓ |
| String handling | Basic | Basic | Extended | Advanced |
| Database integration | ✗ | ✗ | ✗ | ✓ |
| Multitasking | ✗ | ✗ | ✗ | ✓ |
| Medical focus | ✗ | ✗ | ✗ | ✓ |
| Portability | ✗ | ✗ | ✗ | ✓ |

### Language Family Tree

```
Early Computing (1940s-1950s)
├── Batch Processing Systems
│
└── Time-Sharing Concepts
    └── JOSS (1963, RAND)
        ├── CAL
        ├── CITRAN
        ├── ISIS
        ├── PIL/I
        ├── JEAN
        ├── BOSS
        ├── INTERP
        ├── FOCAL
        └── BBN Variants
            ├── TELCOMP (1964)
            ├── STRINGCOMP (1967)
            └── FILECOMP
                └── MUMPS (1966-1967, MGH)
                    ├── GT.M
                    ├── Caché
                    ├── InterSystems IRIS
                    ├── YottaDB
                    ├── M21
                    ├── MiniM
                    ├── FreeM
                    └── Reference Standard M
```

## Pre-Precursor: Manual Processes

Before any of these electronic systems, medical and administrative work relied on:

1. **Paper Records (1930s-1960s)**
   - Physical patient files in manila folders
   - Handwritten notes and charts
   - Manual indexing and cross-referencing
   - Typewritten reports and forms

2. **Mechanical Tabulation (1930s-1950s)**
   - Punch card systems (Hollerith cards)
   - Tabulating machines for statistical analysis
   - Sorter and collator machines for organizing data

3. **Early Hospital Information Systems (1950s-1960s)**
   - Experimental systems using batch processing
   - Mainframe-based patient indexing
   - Limited interactivity and real-time access

## Conclusion

The MUMPS programming language represents a convergence of several important computing trends:

1. **Interactive computing** from JOSS
2. **Commercial time-sharing** from TELCOMP
3. **String and text processing** from STRINGCOMP
4. **Medical domain requirements** from MGH's hospital information needs

The lineage JOSS → TELCOMP → STRINGCOMP → MUMPS demonstrates how innovations in interactive programming languages evolved to meet specific application requirements, ultimately resulting in a language that would dominate health information systems for decades.

## References

See `../references/bibliography.md` for complete citation information on all sources referenced in this document.
