# MUMPS/M Programming Language Research Project

## Overview

This repository contains research materials and analysis for a study of the MUMPS (Massachusetts General Hospital Utility Multi-Programming System) programming language, also known as the M programming language. The research traces the language's origins, precursor languages, historical development, and continued relevance in healthcare informatics and beyond.

**Project Status**: Active Research
**Maintainer**: Mark (Personal Research Project)
**Affiliation**: Flaming Yack Limited Company (Nebraska LLC)
**License**: [To be determined]

## Project Description

This personal research project investigates the academic literature and historical development of the MUMPS/M programming language, with particular attention to:

1. **Historical Origins**: Tracing the development from precursor languages to modern implementations
2. **Precursor Languages**: JOSS, TELCOMP, STRINGCOMP, and their influence
3. **Key Events**: Important milestones in MUMPS history from 1966 to present
4. **Academic Literature**: Comprehensive bibliography of peer-reviewed publications
5. **Applications**: Healthcare informatics, financial systems, and other domains
6. **Timeline Analysis**: Histogram of events and publications in 5-year bins from 1930 to present

## Repository Structure

```
mumps-research/
├── README.md                    # This file
├── data/
│   └── timeline.csv            # Structured timeline of events (1930-present)
├── analysis/
│   ├── README.md               # Analysis documentation
│   ├── generate_histogram.py   # Python script to generate histogram
│   ├── histogram.png           # Generated histogram visualization (run script)
│   ├── histogram_data.csv      # Raw histogram data (run script)
│   └── precursors.md           # Analysis of precursor languages
├── docs/
│   └── [Future documentation]
└── references/
    └── bibliography.md         # Comprehensive bibliography with DOIs
```

## Quick Start

### Prerequisites

- Python 3.x (for analysis scripts)
- Python packages: `matplotlib`

### Setup

```bash
# Clone or navigate to the repository
cd /home/mark/projects/mumps-research

# Install dependencies
pip install matplotlib

# Generate analysis outputs
cd analysis
python3 generate_histogram.py
```

### Outputs

After running the analysis script, you will have:
- `analysis/histogram.png` - Visual histogram of events by 5-year period
- `analysis/histogram_data.csv` - Raw data for further analysis
- Console output with summary statistics

## Key Findings Summary

### Language Origins

**MUMPS** (Massachusetts General Hospital Utility Multi-Programming System) or **M** was developed in **1966-1967** at Massachusetts General Hospital (MGH) in Boston by:
- Neil Pappalardo
- Robert A. Greenes  
- Curt Marble

The development occurred in **Dr. Octo Barnett's lab** as part of an NIH-supported hospital information systems project.

### Precursor Lineage

MUMPS evolved from a lineage of interactive, time-sharing languages:

```
JOSS (1963, RAND Corporation)
  ↓
TELCOMP (~1964, BBN)
  ↓
STRINGCOMP (1967, BBN)
  ↓
MUMPS (1966-1967, MGH)
```

**JOSS** (JOHNNIAC Open Shop System) was one of the first interactive time-sharing languages, pioneering:
- Conversational user interfaces
- Line-number based editing
- Boolean decisions
- Built-in source code editor

**TELCOMP** was BBN's commercial adaptation of JOSS, first generating revenue in October 1965.

**STRINGCOMP** added extended string handling capabilities, directly influencing MUMPS's database integration.

### Technical Innovations

MUMPS introduced several groundbreaking features for its era:

1. **Integrated Database**: Direct, high-speed read-write access to permanent disk storage
2. **Multitasking**: Advanced feature for mini-computers of the era
3. **Portability**: Designed to be portable between different hardware platforms
4. **Hierarchical File System**: Standardized data interaction, abstracting disk operations
5. **Medical Domain Focus**: Specifically designed for hospital and healthcare applications

### Historical Timeline (Key Events)

| Year | Event |
|------|-------|
| 1963 | JOSS implemented at RAND Corporation |
| 1964 | TELCOMP development begins at BBN |
| 1965 | TELCOMP first revenue (October) |
| 1966 | MUMPS development begins at MGH |
| 1966 | Original MUMPS on DEC PDP-7 |
| 1967 | STRINGCOMP developed at BBN |
| 1967 | NIH funding secured for MUMPS project |
| 1967 | PDP-9 obtained; MUMPS used in admissions and lab systems |
| 1972 | TELCOMP service sold to OLS |
| 1981 | MUMPS interpreter design published |
| 1981 | Micro MUMPS for microcomputers published |
| 1996 | Arden Syntax to MUMPS compiler described |
| 2000 | GT.M for Linux released |
| 2006 | VA VistA wins government award (uses MUMPS) |
| 2010 | ESA uses InterSystems Caché for Gaia mission |
| 2020s | Continued use in Epic, MEDITECH, and other EHR systems |

### Modern Usage

MUMPS-based systems remain dominant in healthcare informatics:

- **Epic Systems**: >78% of US patients' health information
- **MEDITECH**: Major hospital information systems
- **GE Healthcare**: Formerly IDX Systems and Centricity
- **Veterans Affairs**: DHCP and VistA systems
- **Indian Health Service**: Nationwide healthcare system
- **Department of Defense**: CHCS hospital system
- **Financial Sector**: Bank of England, Barclays Bank, and many credit unions
- **Space Applications**: European Space Agency's Gaia mission

### Academic Publications

Key peer-reviewed publications include:

1. **Brown, F.M. (1981)** - Design of a MUMPS interpreter
2. **Watanabe et al. (1981)** - Micro MUMPS for microcomputers
3. **Jennett & Warford (1982)** - Computerized perinatal data system using MIIS
4. **McCauley et al. (1996)** - Arden Syntax to MUMPS compiler
5. **Mugarza et al. (2020)** - Safety-critical systems framework (related research)

Complete bibliography with DOIs available in `references/bibliography.md`.

## Methodology

### Research Approach

1. **Literature Search**: Used Scholar Gateway and PubMed to find peer-reviewed publications
2. **Historical Research**: Consulted Wikipedia, CodeDocs, and technical documentation
3. **Timeline Construction**: Created structured timeline from 1930 to present
4. **Categorization**: Events categorized by type (language, implementation, application, etc.)
5. **Analysis**: Generated histogram visualization and statistical summaries

### Data Sources

- **Scholar Gateway**: Peer-reviewed academic literature (Wiley publications)
- **PubMed**: Biomedical and life sciences literature
- **Wikipedia**: Historical overviews and cross-references
- **CodeDocs**: Technical documentation and implementation details
- **GitHub**: Source code and implementation repositories
- **Becker Medical Library**: Historical medical computing resources

### Time Bins

The research uses **5-year bins** from 1930 to present for analysis:
- 1930-1934, 1935-1939, ..., 2020-2024, 2025-2029
- Events are counted in the bin corresponding to their year
- Category breakdowns show distribution across language development, applications, publications, etc.

## Repository Contents

### Data Files

**`data/timeline.csv`**
- Structured CSV with columns: Year, Bin_Start, Bin_End, Bin_Label, Event, Category, Source
- 30+ events from 1963 to 2026
- Multiple sources including Wikipedia, Scholar Gateway, PubMed
- Categorized by type for analysis

### Analysis Files

**`analysis/generate_histogram.py`**
- Python script to generate visualizations
- Creates histogram.png and histogram_data.csv
- Configurable bin size and date ranges
- Category filtering capabilities

**`analysis/precursors.md`**
- Detailed analysis of JOSS, TELCOMP, STRINGCOMP
- Historical context and influence pathways
- Technical evolution comparison
- Language family tree

**`analysis/histogram.png`** (Generated)
- Visual histogram of events by 5-year period
- Color-coded by category
- High-resolution (300 DPI) for publication quality

**`analysis/histogram_data.csv`** (Generated)
- Raw data for custom analysis
- Counts by category for each bin
- Can be imported into spreadsheets or other tools

### Reference Files

**`references/bibliography.md`**
- Comprehensive bibliography with >20 sources
- Proper DOI links for all academic publications
- Categorized by decade and type
- PubMed and Scholar Gateway attribution as required

## Future Work

### Planned Additions

1. **Extended Precursor Research**: Deeper investigation into:
   - JOSS implementation details and influence
   - Other JOSS derivatives (CAL, CITRAN, ISIS, etc.)
   - Connection to FOCAL and BASIC languages

2. **Modern Implementations**: Analysis of:
   - GT.M, Caché, InterSystems IRIS
   - YottaDB, M21, MiniM, FreeM
   - Open source implementations

3. **Application Case Studies**: Detailed examination of:
   - Epic Systems architecture
   - VA VistA implementation
   - Financial sector usage
   - Space mission applications

4. **Comparative Analysis**: Comparison with:
   - Other database languages
   - SQL and relational databases
   - NoSQL databases
   - Modern healthcare IT approaches

5. **Publication Metadata**: Enhanced bibliography with:
   - Citation counts
   - Impact factors
   - Author affiliation networks
   - Co-citation analysis

### Potential Outputs

- Extended timeline with more events
- Network diagrams of language influence
- Comparative technical analysis
- Application domain case studies
- Publishing-ready figures and tables

## Contributing

This is a personal research project, but contributions are welcome:

1. **Additional Sources**: Suggestions for relevant publications or historical documents
2. **Corrections**: Updates to dates, names, or historical facts
3. **Analysis Improvements**: Better visualization or statistical methods
4. **Documentation**: Clarifications or additional context

Please contact the maintainer (Mark) with contributions or suggestions.

## License and Citation

### Attribution

All information from **PubMed** and **Scholar Gateway** is properly attributed with:
- Clear identification of the source
- DOI links where available
- Compliance with terms of service

### Suggested Citation

For citing this research repository:

```
Mark. (2026). MUMPS/M Programming Language Research Project. 
Git Repository: /home/mark/projects/mumps-research
Flaming Yack Limited Company.
```

Or in BibTeX format:

```bibtex
@misc{mumps_research_2026,
  author = {Mark},
  title = {MUMPS/M Programming Language Research Project},
  year = {2026},
  howpublished = {Git Repository},
  note = {Flaming Yack Limited Company},
  url = {file:///home/mark/projects/mumps-research}
}
```

### Data License

The data and analysis in this repository are made available for:
- Academic research
- Personal study
- Non-commercial use

Commercial use requires permission from the maintainer.

## Acknowledgments

- **Wikipedia contributors** for comprehensive historical information
- **Wiley Scholar Gateway** for access to peer-reviewed literature
- **PubMed** for biomedical literature access
- **CodeDocs** for technical documentation
- **RAND Corporation, BBN, MGH** for pioneering early computing systems
- **Octo Barnett, Neil Pappalardo, Robert Greenes, Curt Marble** for creating MUMPS

## Contact

**Maintainer**: Mark
**Email**: [To be added]
**Affiliation**: Flaming Yack Limited Company (Nebraska LLC)
**Repository**: /home/mark/projects/mumps-research

---

*Last updated: 2026-11-11*
*Repository initialized: 2026-11-11*
