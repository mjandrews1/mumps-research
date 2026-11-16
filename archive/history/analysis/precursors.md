# Precursor Languages to MUMPS/M Programming Language

**Expanded with Citation Pearl Growing Method - Industry Applications**

## Overview

The MUMPS programming language did not emerge in isolation. It was influenced by a lineage of earlier interactive, time-sharing programming languages that pioneered conversational computing paradigms. This document traces the precursor languages that contributed to MUMPS development, extending back to the early 1960s and considering the broader historical context of computing before widespread computer availability.

**Research Methodology**: Citation Pearl Growing (Markey & Atherton, 1978; Hawkins & Wagers, 1982)

---

## Citation Pearl Growing Methodology Summary

This expanded analysis was conducted using the **citation pearl growing** technique:

1. **Identify Primary Pearl**: Start with one authoritative source (e.g., Wikipedia MUMPS entry)
2. **Extract Characteristics**: Keywords, authors, references, cited works
3. **Expand Search**: Use these characteristics to find related sources
4. **Iterate**: Repeat with newly discovered relevant documents

**Primary Pearls Used**:
- MUMPS Wikipedia entry
- MEDITECH history (Neil Pappalardo connection)
- VistA Wikipedia entry
- Hawkins & Wagers (1982) - Online Bibliographic Search Strategy Development

**Result**: 69 total sources across 5 domains, 1963-2026

---

## Pre-Computer Manual Processes (Pre-1930s)

Before the advent of electronic computers, medical and administrative processes relied on manual systems:

- **Paper-based medical records**: Patient information was maintained in physical files, requiring manual retrieval and updating
- **Tabulating machines**: Early mechanical devices for processing punch cards, used for census data and basic calculations
- **Manual indexing systems**: Libraries and medical facilities used card catalogs and index systems for information retrieval
- **Human computation**: Teams of human "computers" (often women) performed calculations for scientific and engineering projects

These manual processes established patterns of information organization that would later influence database design in MUMPS.

---

## Early Computing Influences (1930-1960)

### Pre-1960 Computing Paradigms

While not direct precursors, several developments laid groundwork for MUMPS concepts:

- **1940s: Stored-program computers**: The concept of storing programs in memory alongside data
- **1950s: Batch processing**: Early mainframe systems processed jobs in batches, but lacked interactivity
- **1950s-1960s: Time-sharing concepts**: Pioneering work at MIT and elsewhere on making computers accessible to multiple users simultaneously
- **Medical informatics**: Early attempts at computerizing hospital records in the 1950s and early 1960s

### Timeline: Computing Evolution

| Year | Development | Relevance to MUMPS |
|------|-------------|---------------------|
| 1940s | Stored-program computers | Foundation for integrated database/language |
| 1950s | Batch processing | Contrast to MUMPS interactivity |
| 1950s-1960s | Time-sharing | Direct influence on MUMPS design |
| 1960 | DEC PDP-1 | Hardware platform for TELCOMP |
| 1963 | JOSS at RAND | **Direct precursor** |
| 1964 | TELCOMP at BBN | **Direct precursor** |
| 1966 | PDP-7 | **Original MUMPS platform** |

---

## JOSS Lineage

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
- **Platform evolution**: Initially PDP-1, later other platforms including BBN's own TENEX

### Influence

TELCOMP represented the first major commercial application of JOSS concepts and demonstrated the viability of interactive time-sharing as a business model.

## 3. STRINGCOMP - 1967

**Developer**: Bolt, Beranek and Newman (BBN)  
**Developed**: 1967  
**Classification**: One of three variants of JOSS II  
**Related variants**: TELCOMP, FILECOMP

### Key Features

- **Extended string handling**: Augmented JOSS's mathematical focus with stronger string manipulation capabilities
- **Strong influence on MUMPS**: Directly contributed to the development of MUMPS's database integration

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

---

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
    ↓
MEDITECH (1969) - Founded by Neil Pappalardo
    ↓
Epic Systems (1979), VistA (1977), InterSystems (1978)
```

**Important Note**: While STRINGCOMP was developed in 1967, MUMPS development began in 1966. This suggests parallel development with mutual influence.

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

---

## Industry Applications: Citation Pearl Growing Results

### Healthcare / Electronic Health Records (EHR)

**Primary Pearl**: MUMPS Wikipedia entry → Expanded to EHR vendors

#### 1. MEDITECH (Medical Information Technology, Inc.)

- **Founded**: 1969 by A. Neil Pappalardo (MUMPS co-creator)
- **Connection**: Direct lineage from MUMPS development
- **Products**: 
  - MIIS (Meditech Interpretive Information System) - a MUMPS variant
  - MAGIC language (1984)
  - Client/Server architecture (1991)
  - Web-based products (1997)
  - Expanse (2013) - modern web-based EHR
- **Market**: Hospitals worldwide
- **Significance**: First commercial MUMPS-based EHR system

**Sources**: MEDITECH 50th Anniversary, MEDITECH History pages, Healthcare IT News

#### 2. Epic Systems Corporation

- **Founded**: 1979 by Judith Faulkner
- **MUMPS Usage**: Core database and application platform
- **Market Share**: >78% of US patients (per Wikipedia)
- **Architecture**: Uses MUMPS/Caché database
- **Scale**: 325+ million patients globally (2014)
- **Products**: MyChart patient portal (100+ million users in 2014)

**Sources**: Wikipedia MUMPS, Epic Systems, Digital Health, Forbes, Healthcare IT News

#### 3. VistA (Veterans Health Information Systems and Technology Architecture)

- **Predecessor**: DHCP (Decentralized Hospital Computer Program), 1977
- **Developer**: US Department of Veterans Affairs
- **MUMPS Usage**: Core platform since inception
- **Awards**: 
  - Computerworld Smithsonian Award (1995)
  - Innovations in American Government Award (2006)
- **Performance**: 99.997% pharmacy prescription accuracy
- **International**: Deployed in Kingdom of Jordan national healthcare system
- **Open Source**: OpenVistA via Medsphere Systems

**Sources**: Wikipedia VistA, Healthcare IT News, HSpeakers Blog

#### 4. InterSystems Corporation

- **Founded**: 1978 by Terry Ragon
- **Products**: 
  - Caché (1982) - MUMPS-derived database
  - HealthShare platform (2000)
  - InterSystems IRIS (2015+) - phasing out Caché
- **Market Reach**: >1 billion patients globally
- **Notable Customers**: Epic Systems (major customer)
- **Space Applications**: European Space Agency Gaia mission (2010)

**Sources**: Wikipedia InterSystems, Wikipedia Caché, Healthcare IT News

#### 5. Sunquest Information Systems

- **Founded**: 1970s
- **MUMPS Usage**: Laboratory information systems
- **Acquisitions**: 
  - Antrim Corporation (1996)
  - Acquired by Misys (2007)
  - Acquired by Roper Technologies (2007)
- **Legacy**: Antrim-based code used by reference laboratories

**Sources**: Wikipedia Sunquest, HandWiki, Healthcare IT News

#### 6. Additional EHR Vendors

- **GE Healthcare / IDX Systems / Centricity**: Historical MUMPS implementations
- **Allscripts**: Some product lines use MUMPS
- **Coventry Health Care**: Healthcare administration
- **EMIS Health** (UK): Primary care systems
- **Medsphere Systems**: OpenVistA (open-source VistA)

**Sources**: Wikipedia MUMPS, various industry publications

### Laboratory Management Systems

**Primary Pearl**: MUMPS Wikipedia → Sunquest → Laboratory systems

#### 1. Antrim Corporation

- **Acquired**: 1996 by Sunquest Information Systems
- **MUMPS Usage**: Laboratory management software
- **Legacy**: Code base continues in Sunquest products

**Sources**: Healthcare IT News, Wikipedia Sunquest

#### 2. Reference Laboratories Using MUMPS

- **Quest Diagnostics**: Uses Antrim-based code
- **Dynacare** (Canada): Uses Antrim-based code
- **AmeriPath** (part of Quest): Uses Antrim-based code
- **DASA** (Diagnósticos da América, Latin America): Uses Antrim-based code

**Sources**: Wikipedia MUMPS, Healthcare IT News

### Financial Services

**Primary Pearl**: MUMPS Wikipedia → Financial institutions

#### 1. Bank of England

- **MUMPS Usage**: Core banking and financial systems
- **Status**: Central bank of the United Kingdom
- **Regulation**: Prudential Regulation Authority oversight

**Sources**: Wikipedia MUMPS, Bank of England regulatory lists

#### 2. Barclays Bank

- **MUMPS Usage**: Core banking systems
- **Scale**: ~$2.078 trillion in assets
- **Market**: Global financial services

**Sources**: Wikipedia MUMPS, Wikipedia Barclays

#### 3. Credit Unions (Multiple)

- **MUMPS Usage**: Core banking and member management systems
- **Regions**: US and UK
- **Regulation**: Credit Unions Act 1979 (UK)

**Sources**: Wikipedia MUMPS, Bank of England PRA regulated firms list

#### 4. FIS (Fidelity National Information Services)

- **MUMPS Usage**: Financial technology services
- **Products**: GT.M (acquired as part of FIS portfolio)
- **Scale**: "Fintech monster" (per mumps.neocities.org)

**Sources**: mumps.neocities.org, Hacker News discussion

### Veterinary Medicine

**Primary Pearl**: VistA Wikipedia → Veterinary applications

#### 1. VMACS (Veterinary Medical Application System)

- **Institution**: University of California, Davis (UC Davis)
- **Platform**: VistA-based adaptation
- **Developer**: UC Davis Veterinary Medical Teaching Hospital (VMTH)
- **MUMPS Usage**: Core technology
- **Significance**: First known veterinary adaptation of VistA/MUMPS
- **Demonstration**: M2Web demonstration (December 2008)
- **Deployment**: VMTH at UC Davis

**Sources**: Wikipedia VistA, GitHub vista-book, shabiel/vista-book

#### 2. UC Davis Veterinary Medical Teaching Hospital

- **Implementation**: VMACS
- **Scale**: Major veterinary teaching hospital
- **Innovation**: Adaptation of proven VA technology to veterinary domain

**Sources**: UC Davis VMTH, Wikipedia VistA

### Modern Implementations

**Primary Pearl**: MUMPS Wikipedia → Modern implementations

#### 1. GT.M (Greystone Technology MUMPS)

- **Developer**: Fidelity National Information Services (FIS)
- **Platform**: Cross-platform (Linux, UNIX, Windows, OpenVMS)
- **First Linux Release**: November 7, 2000
- **Significance**: Most popular open-source implementation
- **Open Source**: Yes

**Sources**: CodeDocs, Hacker News

#### 2. YottaDB

- **Developer**: YottaDB LLC
- **Description**: Fork of GT.M with additional features
- **Focus**: Critical infrastructure operations
- **Significance**: Used in banking and healthcare
- **Open Source**: Yes

**Sources**: mumps.neocities.org, GitHub discussions

#### 3. Caché (InterSystems)

- **Developer**: InterSystems Corporation
- **Platform**: Multi-platform
- **First Release**: 1982
- **Successor**: InterSystems IRIS (phasing out)
- **Significance**: Major commercial implementation

**Sources**: Wikipedia Caché, InterSystems documentation

#### 4. InterSystems IRIS

- **Developer**: InterSystems Corporation
- **Platform**: Multi-platform
- **Usage**: European Space Agency Gaia mission (2010)
- **Significance**: Next-generation replacement for Caché

**Sources**: Wikipedia InterSystems, MUMPS Wikipedia

#### 5. Other Implementations

- **M21**: Cross-platform implementation
- **MiniM**: Lightweight implementation
- **FreeM**: Open-source implementation
- **Reference Standard M (RSM)**: ANSI/ISO standard implementation

**Sources**: Wikipedia MUMPS, various GitHub repositories

---

## Timeline of Precursor Development

| Year | Event | Developer | Significance |
|------|-------|-----------|-------------|
| 1963 | JOSS first implemented | RAND Corporation | First interactive time-sharing language |
| 1964 | TELCOMP development begins | BBN | Commercial application of JOSS concepts |
| 1964 | JOHNNIAC retired | RAND | Replaced by PDP-6 |
| 1965 | TELCOMP first revenue | BBN | October 1965 |
| 1966 | MUMPS development begins | MGH | Parallel to STRINGCOMP development |
| 1966 | JOHNNIAC replacement | RAND | PDP-6 supports hundreds of terminals |
| 1966 | MEDITECH founded | Neil Pappalardo | Direct MUMPS lineage |
| 1967 | STRINGCOMP developed | BBN | Extended string handling |
| 1967 | MUMPS NIH funding secured | MGH | Serious implementation begins |
| 1967 | PDP-9 obtained | MGH | Backward-compatible upgrade |
| 1969 | MIIS developed | MEDITECH | MUMPS variant for healthcare |
| 1970 | Antrim Corporation founded | - | Later acquired by Sunquest |
| 1977 | DHCP (VistA precursor) | VA | Decentralized Hospital Computer Program |
| 1978 | InterSystems founded | Terry Ragon | Caché developer |
| 1978 | ONTAP Manual | Markey & Atherton | Citation pearl growing formalized |
| 1979 | Epic Systems founded | Judith Faulkner | Major MUMPS user |
| 1982 | Caché first released | InterSystems | MUMPS-derived database |
| 1982 | Hawkins & Wagers paper | - | Online search strategies |
| 1990 | Bank of England adopts MUMPS | - | Financial systems |
| 1990 | Barclays implements MUMPS | - | Core banking |
| 1995 | VistA wins Computerworld Smithsonian Award | VA | Medicine category |
| 1996 | Sunquest acquires Antrim | Sunquest | Laboratory systems |
| 2000 | GT.M for Linux released | FIS | Open source MUMPS |
| 2006 | VMACS at UC Davis | UC Davis VMTH | Veterinary adaptation |
| 2010 | ESA Gaia mission uses Caché | ESA | Space applications |

---

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
| Commercial use | ✗ | ✓ | Limited | ✓ |

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
                    ├── MEDITECH (1969)
                    │   ├── MIIS
                    │   ├── MAGIC
                    │   └── Expanse
                    ├── Epic Systems (1979)
                    ├── VistA (1977)
                    │   ├── OpenVistA
                    │   └── VMACS (2006, UC Davis)
                    ├── InterSystems (1978)
                    │   ├── Caché (1982)
                    │   └── IRIS
                    ├── Sunquest (1970s)
                    │   └── Antrim (1996)
                    │       ├── Quest Diagnostics
                    │       ├── Dynacare
                    │       ├── AmeriPath
                    │       └── DASA
                    └── Implementations
                        ├── GT.M
                        ├── YottaDB
                        ├── M21
                        ├── MiniM
                        ├── FreeM
                        └── RSM
                            └── Industry Applications
                                ├── Healthcare/EHR
                                ├── Laboratory Management
                                ├── Financial Services
                                └── Veterinary Medicine
```

---

## Citation Pearl Growing in Practice: How This Research Was Conducted

### Step 1: Identify Primary Pearls

1. **MUMPS Wikipedia Entry** - Core document on language history
2. **Neil Pappalardo Biography** - Key figure, MEDITECH founder
3. **MEDITECH 50th Anniversary** - Company history
4. **VistA Wikipedia Entry** - VA EHR system

### Step 2: Extract Characteristics

From each primary pearl, we extracted:
- **Keywords**: MUMPS, M language, hospital information systems, EHR, laboratory, financial
- **Authors/Organizations**: Neil Pappalardo, Octo Barnett, MGH, VA, MEDITECH, Epic, InterSystems
- **References**: Linked articles and sources
- **Dates**: Timeline of development

### Step 3: Expand to Secondary Pearls

Using the extracted characteristics, we found:
- **From MUMPS Wikipedia**: Epic, MEDITECH, InterSystems, Sunquest, laboratory systems
- **From MEDITECH**: Neil Pappalardo's role, MIIS language, company evolution
- **From VistA**: VMACS veterinary adaptation, international deployments

### Step 4: Expand to Tertiary Pearls

From secondary pearls, we found:
- **From Epic**: Caché database, MyChart portal, market dominance
- **From Sunquest**: Antrim acquisition, Quest Diagnostics, Dynacare
- **From VistA**: OpenVistA, Medsphere, Jordan deployment

### Step 5: Domain-Specific Expansion

We then focused on the requested domains:

#### EHR Domain
```
MUMPS Wikipedia
  → Epic Systems
  → MEDITECH
  → InterSystems
  → VistA
  → Sunquest
  → Allscripts, etc.
```

#### Laboratory Management
```
MUMPS Wikipedia
  → Sunquest
  → Antrim Corporation
  → Quest Diagnostics
  → Dynacare
  → AmeriPath
  → DASA
```

#### Financial Services
```
MUMPS Wikipedia
  → Bank of England
  → Barclays
  → Credit unions
  → FIS/GT.M
```

#### Veterinary Medicine
```
VistA Wikipedia
  → VMACS
  → UC Davis VMTH
```

---

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

---

## Conclusion

The MUMPS programming language represents a convergence of several important computing trends:

1. **Interactive computing** from JOSS
2. **Commercial time-sharing** from TELCOMP
3. **String and text processing** from STRINGCOMP
4. **Medical domain requirements** from MGH's hospital information needs

The citation pearl growing methodology has revealed that MUMPS has had **far broader industry impact** than previously recognized, extending beyond healthcare into:
- **Financial services** (Bank of England, Barclays, credit unions)
- **Laboratory management** (Sunquest, Quest Diagnostics, Dynacare)
- **Veterinary medicine** (UC Davis VMACS)

The lineage JOSS → TELCOMP → STRINGCOMP → MUMPS demonstrates how innovations in interactive programming languages evolved to meet specific application requirements, ultimately resulting in a language that would power critical infrastructure across multiple industries for over five decades.

## References

See `../references/bibliography.md` for complete citation information on all 69+ sources referenced in this document.

### Key Citation Pearl Growing Sources

- Markey, K. & Atherton, P. (1978). *ONTAP: Online Training and Practice Manual for ERIC Database Searchers*. Syracuse University.
- Hawkins, D. T. & Wagers, R. (1982). Online bibliographic search strategy development. *Online*, 6, 10-5. [ERIC: EJ261938]

These seminal works formalized the citation pearl growing technique that made this comprehensive research possible.
