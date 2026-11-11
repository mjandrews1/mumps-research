# Analysis Files

This directory contains analysis scripts and documentation for the MUMPS/M programming language research project.

## Files

### 1. `generate_histogram.py`

A Python script that generates a histogram visualization and data file from the timeline.csv data.

**Features:**
- Creates 5-year bins from 1930 to present
- Counts events by category for each bin
- Generates a PNG visualization
- Outputs raw data to CSV
- Prints summary statistics

**Usage:**
```bash
cd /home/mark/projects/mumps-research/analysis
python3 generate_histogram.py
```

**Outputs:**
- `histogram.png` - Visual histogram of events by 5-year period
- `histogram_data.csv` - Raw data with counts by category for each bin

**Requirements:**
- Python 3.x
- matplotlib (for visualization)
- csv module (standard library)

### 2. `precursors.md`

Comprehensive analysis of the precursor languages to MUMPS/M, including:

- **Pre-computer manual processes** (1930s and earlier)
- **JOSS (JOHNNIAC Open Shop System)** - 1963, RAND Corporation
- **TELCOMP** - ~1964, Bolt, Beranek and Newman (BBN)
- **STRINGCOMP** - 1967, BBN
- **FILECOMP** - JOSS II variant for text processing

The document traces the complete lineage from early computing concepts through to MUMPS development at Massachusetts General Hospital.

## Data Sources

All analysis is based on:
- Timeline data from `../data/timeline.csv`
- Bibliography in `../references/bibliography.md`
- Web sources (Wikipedia, CodeDocs, etc.)
- Academic literature from Scholar Gateway and PubMed

## Running the Analysis

To regenerate all analysis outputs:

```bash
# Make sure you're in the analysis directory
cd /home/mark/projects/mumps-research/analysis

# Install Python dependencies if needed
pip install matplotlib

# Run the histogram generator
python3 generate_histogram.py
```

## Interpretation Guide

### Histogram Data

The histogram shows the distribution of MUMPS/M-related events and publications across time in 5-year bins. Key periods to note:

- **1960-1964**: Emergence of JOSS and early TELCOMP development
- **1965-1969**: MUMPS creation, STRINGCOMP development, early implementations
- **1970-1974**: Consolidation and early applications
- **1975-1979**: Growth in medical informatics applications
- **1980-1984**: Academic publications and system implementations
- **1995-1999**: Continued development and integration
- **2000-2004**: Modern implementations (GT.M, Caché)
- **2020-Present**: Continued use in healthcare and space applications

### Category Breakdown

Events are categorized as:
- **Precursor Language**: JOSS, TELCOMP, STRINGCOMP
- **Core Language**: MUMPS/M development
- **Implementation**: Specific implementations and ports
- **Application**: Real-world uses in healthcare, finance, etc.
- **Publication**: Academic papers and technical reports
- **Historical Context**: Related historical events
- **Comparison**: Comparative studies with other languages
- **Current Status**: Modern implementations and usage
- **Related Research**: Broader research in related areas

## Notes

- The histogram data is derived from the timeline.csv file, which contains curated events with dates and categories
- Bin ranges are inclusive (e.g., 1965-1969 includes all years from 1965 through 1969)
- Events are counted in the bin corresponding to their year
- Category counts are mutually exclusive (each event belongs to one primary category)
