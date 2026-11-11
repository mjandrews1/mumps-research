# MUMPS/M Programming Language Research Project

## Overview

This repository contains research materials and analysis for a study of the MUMPS (Massachusetts General Hospital Utility Multi-Programming System) programming language, also known as the M programming language. The research traces the language's origins, precursor languages, historical development, and continued relevance in healthcare informatics and beyond.

**Project Status**: Active Research - Expanded with Citation Pearl Growing Method  
**Maintainer**: Mark (Personal Research Project)  
**Affiliation**: Flaming Yack Limited Company (Nebraska LLC)  
**Research Methodology**: Citation Pearl Growing (Markey & Atherton, 1978; Hawkins & Wagers, 1982)  
**License**: [To be determined]

---

## 🎯 Major Update: Citation Pearl Growing Applied

This project has been **significantly expanded** using the **citation pearl growing** search technique to identify industry-specific applications of MUMPS across five domains:

### Domains Covered
✅ **Electronic Health Records (EHR)** - Epic, MEDITECH, VistA, InterSystems, Sunquest, Allscripts, GE Healthcare  
✅ **Laboratory Management** - Sunquest, Antrim, Quest Diagnostics, Dynacare, AmeriPath, DASA  
✅ **Financial Services** - Bank of England, Barclays Bank, Credit Unions, FIS  
✅ **Veterinary Medicine** - VMACS at UC Davis (VistA adaptation)  
✅ **Modern Implementations** - GT.M, YottaDB, Caché, IRIS, M21, MiniM, FreeM

**Result**: 86 timeline events, 69 bibliography sources, comprehensive industry analysis

---

## Project Description

This personal research project investigates the academic literature and historical development of the MUMPS/M programming language, with particular attention to:

1. **Historical Origins**: Tracing the development from precursor languages to modern implementations
2. **Precursor Languages**: JOSS, TELCOMP, STRINGCOMP, and their influence
3. **Key Events**: Important milestones in MUMPS history from 1963 to present
4. **Academic Literature**: Comprehensive bibliography of peer-reviewed publications
5. **Industry Applications**: Healthcare informatics, financial systems, laboratory management, veterinary medicine, and library automation
6. **Timeline Analysis**: Histogram of events and publications in 5-year bins from 1930 to present
7. **Methodology**: Application of citation pearl growing technique to expand research scope

---

## Repository Structure

```
mumps-research/
├── README.md                          # This file - COMPREHENSIVE documentation
├── data/
│   └── timeline.csv                  # 86 events from 1963-2026 in 5-year bins
├── analysis/
│   ├── README.md                     # Analysis documentation
│   ├── generate_histogram.py        # Python script (requires matplotlib)
│   ├── generate_histogram_simple.py # Python script (no dependencies)
│   ├── histogram_data.csv           # Generated histogram data (66 events)
│   ├── histogram_summary.txt        # Text summary of findings
│   └── precursors.md                 # EXPANDED with industry applications
└── references/
    └── bibliography.md               # 69+ sources with DOI links
```

---

## 📊 Key Findings Summary

### Language Lineage (1960-1967)

The **MUMPS** (Massachusetts General Hospital Utility Multi-Programming System) programming language has a clear precursor lineage:

```
JOSS (1963, RAND Corporation)
  ↓
TELCOMP (~1964, BBN)
  ↓
STRINGCOMP (1967, BBN)
  ↓
MUMPS (1966-1967, Massachusetts General Hospital)
  ↓
INDUSTRY APPLICATIONS:
  ├── Healthcare/EHR: Epic, MEDITECH, VistA, InterSystems, Sunquest
  ├── Laboratory: Sunquest, Quest, Dynacare, Antrim
  ├── Financial: Bank of England, Barclays, Credit Unions
  └── Veterinary: VMACS at UC Davis
```

**Core Development**:
- **Developers**: Neil Pappalardo, Robert A. Greenes, Curt Marble
- **Location**: Dr. Octo Barnett's lab at MGH, Boston
- **Platform**: DEC PDP-7 → PDP-9
- **Funding**: NIH-supported hospital information systems project

### Technical Innovations
- Integrated key-value database with direct disk access
- Multitasking capabilities (advanced for 1966)
- Hierarchical file system
- Medical domain focus
- Portability between hardware platforms

---

## 📈 Expanded Histogram Summary

### 5-Year Bin Distribution (66 Events)

| Period | Events | Key Highlights |
|--------|--------|----------------|
| **1960-1964** | 2 | JOSS, MUMPS begins |
| **1965-1969** | 12 | MUMPS created, STRINGCOMP, MEDITECH founded, early implementations |
| **1970-1974** | 3 | Antrim founded, DHCP/VistA begins |
| **1975-1979** | 6 | VistA deployed, ONTAP manual (citation pearl growing formalized) |
| **1980-1984** | 8 | Caché released, Epic founded, InterSystems founded, Hawkins & Wagers paper |
| **1985-1989** | 1 | MEDITECH Client/Server |
| **1990-1994** | 2 | Financial systems (Bank of England, Barclays), VistA widespread |
| **1995-1999** | 5 | VistA awards, Sunquest acquires Antrim, GT.M for Linux pending |
| **2000-2004** | 2 | GT.M for Linux released, Sunquest acquired by Misys |
| **2005-2009** | 7 | VistA awards, VMACS at UC Davis, YottaDB forked, Epic growth |
| **2010-2014** | 5 | ESA Gaia mission, Epic dominance, MEDITECH Expanse |
| **2015-2019** | 3 | InterSystems IRIS, MEDITECH Expanse 2.0, financial systems continued |
| **2020-2024** | 8 | Current status dominance, industry analysis articles |
| **2025-2026** | 2 | Recent research, Pappalardo death |

**Category Distribution**:
- **Implementation**: 29 (43.9%) - Largest category
- **Application**: 12 (18.2%) - Industry deployments
- **Publication**: 5 (7.6%) - Academic literature
- **Current Status**: 5 (7.6%) - Modern implementations
- **Precursor Language**: 4 (6.1%) - JOSS, TELCOMP, STRINGCOMP
- **Historical Context**: 4 (6.1%) - Background information
- **Related Research**: 4 (6.1%) - Contemporary studies
- **Core Language**: 2 (3.0%) - MUMPS development
- **Comparison**: 1 (1.5%) - Comparative studies

---

## 🏥 Industry Applications: Citation Pearl Growing Results

### Healthcare / Electronic Health Records (EHR)

MUMPS-based systems dominate the EHR landscape:

| Vendor | Founded | MUMPS Usage | Market Share/Reach | Key Facts |
|--------|---------|-------------|-------------------|-----------|
| **MEDITECH** | 1969 | Core platform | 1,000+ hospitals | Founded by Neil Pappalardo (MUMPS co-creator); MIIS language; Expanse web EHR |
| **Epic Systems** | 1979 | Core database | >78% US patients | 325M+ patients globally; MyChart portal (100M+ users) |
| **VistA** | 1977 | Core platform | All VA hospitals | VA system; 99.997% pharmacy accuracy; Jordan national healthcare |
| **InterSystems** | 1978 | Caché database | >1B patients | HealthShare platform; ESA Gaia mission; Epic major customer |
| **Sunquest** | 1970s | Laboratory systems | Global labs | Acquired Antrim (1996); serves Quest, Dynacare |
| **Allscripts** | - | Some products | US hospitals | Enterprise EHR solutions |
| **GE Healthcare** | - | Historical | Legacy systems | IDX/Centricity heritage |
| **EMIS Health** | - | Primary care | UK market | NHS primary care systems |

**Awards & Recognition**:
- VistA: Computerworld Smithsonian Award (1995), Innovations in American Government Award (2006)
- Epic: Market leader in US healthcare
- MEDITECH: 50+ years of continuous innovation

### Laboratory Management Systems

MUMPS powers major laboratory information systems:

| Company | Acquired/Founded | MUMPS Usage | Current Status |
|---------|----------------|-------------|---------------|
| **Antrim Corporation** | 1970s | Laboratory management | Acquired by Sunquest (1996) |
| **Sunquest Information Systems** | 1970s | LIS platform | Acquired by Misys (2007), then Roper |
| **Quest Diagnostics** | - | Antrim-based code | Major US reference lab |
| **Dynacare** | - | Antrim-based code | Canadian reference lab |
| **AmeriPath** | - | Antrim-based code | Part of Quest Diagnostics |
| **DASA** | - | Antrim-based code | Latin American labs |

**Industry Impact**: Many reference laboratories worldwide use MUMPS-based systems derived from Antrim Corporation's code base.

### Financial Services

MUMPS in banking and finance:

| Institution | Region | MUMPS Usage | Notes |
|-------------|--------|-------------|-------|
| **Bank of England** | UK | Core systems | Central bank of UK |
| **Barclays Bank** | UK/Global | Core systems | ~$2.078T in assets |
| **Credit Unions** | US/UK | Member management | Multiple institutions |
| **FIS (Fidelity National Information Services)** | Global | Financial tech | GT.M developer; "fintech monster" |

**Significance**: MUMPS provides reliable, high-performance database capabilities for critical financial infrastructure.

### Veterinary Medicine

MUMPS adaptations for veterinary care:

| System | Institution | Platform | Date | Notes |
|--------|-------------|----------|------|-------|
| **VMACS** | UC Davis VMTH | VistA-based | 2006 | Veterinary Medical Application System |
| **M2Web Demonstration** | UC Davis | VMACS | Dec 2008 | Web interface for veterinary system |

**Significance**: First known adaptation of VistA/MUMPS to veterinary domain; demonstrates the flexibility of MUMPS technology across healthcare specialties.

### Modern Implementations

Current MUMPS implementations (2020s):

| Implementation | Developer | Platform | First Release | Notes |
|---------------|-----------|----------|---------------|-------|
| **GT.M** | FIS | Cross-platform | 2000 (Linux) | Most popular open-source |
| **YottaDB** | YottaDB LLC | Cross-platform | 2005 | Fork of GT.M; critical infrastructure |
| **Caché** | InterSystems | Multi-platform | 1982 | Phasing out for IRIS |
| **InterSystems IRIS** | InterSystems | Multi-platform | 2015+ | Next-generation replacement |
| **M21** | M/21 Systems | Cross-platform | - | ANSI/ISO compliant |
| **MiniM** | MiniM Systems | Lightweight | - | Embedded systems |
| **FreeM** | FreeM Project | Open source | - | Community-driven |
| **RSM** | MUMPS DC | Standard | - | Reference implementation |

**Market Reach**: These implementations serve billions of patients, millions of financial transactions, and critical infrastructure worldwide.

---

## 📚 Academic Literature

**Bibliography**: 69+ sources with proper attribution and DOI links

### Key Publications

1. **Brown, F.M. (1981)** - Design of a MUMPS interpreter (DOI: [10.1002/spe.4380111206](https://doi.org/10.1002/spe.4380111206))
2. **Watanabe et al. (1981)** - Micro MUMPS for microcomputers (DOI: [10.1002/spe.4380111208](https://doi.org/10.1002/spe.4380111208))
3. **Jennett & Warford (1982)** - Computerized perinatal data system (DOI: [10.3109/00016348209156153](https://doi.org/10.3109/00016348209156153))
4. **McCauley et al. (1996)** - Arden Syntax to MUMPS compiler (DOI: [10.1006/cbmr.1996.0037](https://doi.org/10.1006/cbmr.1996.0037))

### Citation Pearl Growing Methodology Sources

5. **Markey, K. & Atherton, P. (1978)** - *ONTAP: Online Training and Practice Manual for ERIC Database Searchers* (Syracuse University)
   - **Significance**: One of the earliest formal descriptions of citation pearl growing
   - **Pearl Status**: PRIMARY PEARL

6. **Hawkins, D. T. & Wagers, R. (1982)** - "Online Bibliographic Search Strategy Development" (*Online*, 6, 10-5) [ERIC: EJ261938]
   - **Significance**: Seminal paper describing four search strategies: building block, successive fractions, **citation pearl growing**, interactive scanning
   - **Pearl Status**: PRIMARY PEARL

All sources are properly cited with:
- DOI hyperlinks where available
- PubMed attribution where required
- Complete bibliographic information

---

## 🎯 Methodology: Citation Pearl Growing

### What is Citation Pearl Growing?

**Citation pearl growing** (also called simply **pearl growing**) is a bibliographic search technique where a single relevant, authoritative source—the "pearl"—is used as a seed to discover additional relevant sources. The metaphor comes from how small bits of sand accumulate to form a beautiful pearl.

**Key Concept**: Start with one good source, extract its characteristics (keywords, authors, references), and use these to find more relevant materials.

### How It Was Applied to This Research

#### Step 1: Identify Primary Pearls
- MUMPS Wikipedia entry
- Neil Pappalardo biography
- MEDITECH history
- VistA Wikipedia entry

#### Step 2: Extract Characteristics
- **Keywords**: MUMPS, M language, EHR, laboratory, financial, veterinary
- **Authors/Orgs**: Neil Pappalardo, Octo Barnett, MGH, VA, MEDITECH, Epic, InterSystems
- **References**: Linked articles and citations
- **Dates**: Historical timeline

#### Step 3: Expand to Secondary Pearls
- From MUMPS Wikipedia → Epic, MEDITECH, InterSystems, Sunquest, laboratory systems
- From MEDITECH → Neil Pappalardo's role, MIIS language, company evolution
- From VistA → VMACS veterinary adaptation, international deployments

#### Step 4: Domain-Specific Expansion

**EHR Domain**:
```
MUMPS Wikipedia → Epic Systems, MEDITECH, VistA, InterSystems, Sunquest, Allscripts
```

**Laboratory Management**:
```
MUMPS Wikipedia → Sunquest → Antrim → Quest Diagnostics, Dynacare, AmeriPath, DASA
```

**Financial Services**:
```
MUMPS Wikipedia → Bank of England, Barclays, Credit Unions, FIS/GT.M
```

**Veterinary Medicine**:
```
VistA Wikipedia → VMACS → UC Davis VMTH
```

### Results

- **Total Sources**: 69 entries
- **Primary Pearls**: 6
- **Secondary Pearls**: 25+
- **Tertiary Pearls**: 38+
- **Domains Covered**: 5 (EHR, Laboratory, Financial, Veterinary, Implementations)
- **Time Period**: 1963-2026

---

## 🔧 How to Use

### To regenerate analysis:
```bash
cd /home/mark/projects/mumps-research/analysis
python3 generate_histogram_simple.py  # No dependencies needed
```

### For visual histogram (requires matplotlib):
```bash
pip install matplotlib
python3 generate_histogram.py
```

### To view results:
- `analysis/histogram_summary.txt` - Human-readable summary
- `analysis/histogram_data.csv` - Raw data for spreadsheets
- `data/timeline.csv` - Source timeline data (86 events)

---

## 📋 File Contents

### Data Files

**`data/timeline.csv`**
- Structured CSV with columns: Year, Bin_Start, Bin_End, Bin_Label, Event, Category, Source
- **86 events** from 1963 to 2026
- Multiple sources including Wikipedia, Scholar Gateway, industry publications
- Categorized by type for analysis
- **Expanded with industry-specific applications**

### Analysis Files

**`analysis/generate_histogram.py`**
- Python script to generate visualizations
- Creates histogram.png and histogram_data.csv
- Configurable bin size and date ranges
- Category filtering capabilities

**`analysis/generate_histogram_simple.py`**
- Python script without matplotlib dependency
- Creates histogram_data.csv and histogram_summary.txt
- Same functionality without visualization

**`analysis/histogram_data.csv`** (Generated)
- Raw data with counts by category for each bin
- **66 events** after filtering
- Can be imported into spreadsheets or other tools

**`analysis/histogram_summary.txt`** (Generated)
- Human-readable summary of findings
- Events by period and category
- Percentage breakdowns

**`analysis/precursors.md`** (EXPANDED)
- Detailed analysis of JOSS, TELCOMP, STRINGCOMP lineage
- **NEW**: Industry applications across all domains
- **NEW**: Citation pearl growing methodology explanation
- **NEW**: Complete timeline and family tree
- Historical context and technical evolution comparison

### Reference Files

**`references/bibliography.md`** (EXPANDED)
- **69+ sources** with proper attribution and DOI links
- **NEW**: Industry-specific sources for each domain
- **NEW**: Citation pearl growing methodology sources
- **NEW**: Primary/Secondary/Tertiary pearl classification
- Categorized by type and domain
- PubMed and Scholar Gateway attribution as required

---

## 📈 Key Metrics

| Metric | Count | Notes |
|--------|-------|-------|
| Total Events | 86 | In timeline.csv |
| Filtered Events | 66 | After category filtering |
| Active Bins | 14 | Out of 20 total bins (1930-2026) |
| Bibliography Sources | 69+ | In references/bibliography.md |
| Primary Pearls | 6 | Core documents |
| Secondary Pearls | 25+ | Derived from primary |
| Tertiary Pearls | 38+ | Derived from secondary |
| Domains Covered | 5 | EHR, Laboratory, Financial, Veterinary, Implementations |
| Time Span | 63 years | 1963-2026 |
| Git Commits | 1+ | Initial commit + expansions |

---

## 💡 Insights from Citation Pearl Growing

### 1. Healthcare Dominance
- MUMPS-based EHR systems serve **>78% of US patients** (Epic alone)
- **VistA** (VA system) has **99.997% pharmacy prescription accuracy**
- **MEDITECH** has been innovating for **50+ years** continuously

### 2. Laboratory Leadership
- **Sunquest/Antrim** code base powers **major reference laboratories**
- **Quest Diagnostics**, **Dynacare**, **AmeriPath** all use MUMPS-based systems
- **Global reach** across US, Canada, Latin America

### 3. Financial Reliability
- **Bank of England** and **Barclays** use MUMPS for **core banking**
- **Credit unions** rely on MUMPS for **member management**
- **FIS/GT.M** provides **critical financial infrastructure**

### 4. Veterinary Innovation
- **UC Davis VMACS** is the **first veterinary adaptation** of VistA
- Demonstrates **cross-domain applicability** of MUMPS
- Proves MUMPS can be adapted to **specialized healthcare needs**

### 5. Implementation Diversity
- **8 major implementations** currently active
- **Open source** options available (GT.M, YottaDB, FreeM)
- **Commercial** options available (Caché, IRIS)
- **Cross-platform** support (Linux, UNIX, Windows, OpenVMS)

---

## 🎯 Conclusion

The MUMPS programming language, developed in 1966-1967 at Massachusetts General Hospital, has had a **profound and lasting impact** across multiple industries. Through the application of **citation pearl growing** methodology, this research has revealed that MUMPS powers:

- **Healthcare**: The majority of US electronic health records
- **Laboratories**: Major reference laboratories worldwide
- **Finance**: Core banking systems at major institutions
- **Veterinary**: Specialized animal health information systems

Despite being called "outdated" or "arcane" by some, MUMPS continues to power **critical infrastructure** that serves **billions of people** and processes **millions of transactions** daily. Its **integrated database/language design**, **reliability**, and **performance** have made it the technology of choice for applications where **data integrity** and **system stability** are paramount.

This research demonstrates the **power of citation pearl growing** as a methodology for comprehensive literature reviews, allowing the discovery of connections across domains that might otherwise be missed.

---

## 📚 Future Work

### Planned Additions

1. **Library Automation Domain**: Investigate specific library systems using MUMPS (if any exist beyond general mentions)
2. **Detailed Case Studies**: In-depth analysis of specific implementations (e.g., Epic architecture, VA VistA deployment)
3. **Comparative Analysis**: Technical comparison with SQL, NoSQL, and other database technologies
4. **International Implementations**: Beyond US and UK, particularly in Europe and Asia
5. **Performance Benchmarks**: Comparative performance studies of MUMPS vs. modern databases
6. **Economic Impact**: Analysis of MUMPS contribution to healthcare cost savings and efficiency
7. **Interviews**: Primary source interviews with MUMPS developers and users

### Potential Outputs

- Extended timeline with more events
- Network diagrams of language influence and industry connections
- Comparative technical analysis papers
- Application domain case studies
- Publishing-ready figures and tables
- Interactive web-based timeline

---

## 🤝 Contributing

This is a personal research project, but contributions are welcome:

1. **Additional Sources**: Suggestions for relevant publications or historical documents
2. **Corrections**: Updates to dates, names, or historical facts
3. **Analysis Improvements**: Better visualization or statistical methods
4. **Documentation**: Clarifications or additional context
5. **Domain Expertise**: Industry-specific knowledge (healthcare, finance, laboratory, veterinary)

Please contact the maintainer (Mark) with contributions or suggestions.

---

## 📜 License and Citation

### Attribution

All information from **PubMed** and **Scholar Gateway** is properly attributed with:
- Clear identification of the source
- DOI links where available
- Compliance with terms of service

### Suggested Citation

For citing this research repository:

```
Mark. (2026). MUMPS/M Programming Language Research Project: An Application of Citation Pearl Growing Methodology.
Git Repository: /home/mark/projects/mumps-research
Flaming Yack Limited Company.
Research Method: Citation Pearl Growing (Markey & Atherton, 1978; Hawkins & Wagers, 1982)
```

Or in BibTeX format:

```bibtex
@misc{mumps_research_2026,
  author = {Mark},
  title = {MUMPS/M Programming Language Research Project: An Application of Citation Pearl Growing Methodology},
  year = {2026},
  howpublished = {Git Repository},
  note = {Flaming Yack Limited Company. Research Method: Citation Pearl Growing (Markey & Atherton, 1978; Hawkins & Wagers, 1982)},
  url = {file:///home/mark/projects/mumps-research}
}
```

### Data License

The data and analysis in this repository are made available for:
- Academic research
- Personal study
- Non-commercial use

Commercial use requires permission from the maintainer.

---

## 🙏 Acknowledgments

### Citation Pearl Growing Pioneers
- **Markey, Karen & Atherton, Pauline** (1978) - Formalized the citation pearl growing technique
- **Hawkins, D. T. & Wagers, R.** (1982) - Described the methodology in "Online Bibliographic Search Strategy Development"

### Primary Sources
- **Wikipedia contributors** for comprehensive historical information
- **Wiley Scholar Gateway** for access to peer-reviewed literature
- **PubMed** for biomedical literature access
- **CodeDocs** for technical documentation

### Industry Information
- **Epic Systems** for EHR leadership data
- **MEDITECH** for historical information and company milestone
- **InterSystems** for implementation details
- **Sunquest Information Systems** for laboratory system information
- **Healthcare IT News** for industry reporting
- **UC Davis VMTH** for veterinary applications
- **Bank of England, Barclays** for financial sector confirmation

### Historical Figures
- **Octo Barnett** - MGH laboratory director who enabled MUMPS development
- **Neil Pappalardo** - MUMPS co-creator and MEDITECH founder
- **Robert Greenes & Curt Marble** - MUMPS co-creators
- **Terry Ragon** - InterSystems founder
- **Judith Faulkner** - Epic Systems founder

---

## 📞 Contact

**Maintainer**: Mark  
**Email**: [To be added]  
**Affiliation**: Flaming Yack Limited Company (Nebraska LLC)  
**Repository**: /home/mark/projects/mumps-research  
**Research Method**: Citation Pearl Growing

---

*Last updated: 2026-11-11*  
*Repository initialized: 2026-11-11*  
*Major expansion with citation pearl growing: 2026-11-11*  
*Total events: 86*  
*Total sources: 69+*  
*Domains covered: 5*
