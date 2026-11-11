#!/usr/bin/env python3
"""
Generate histogram data for MUMPS/M programming language publications and events
in 5-year bins from 1930 to present (without matplotlib dependency).

Usage:
    python3 generate_histogram_simple.py

Output:
    - Creates histogram_data.csv (raw data)
    - Creates histogram_summary.txt (text summary)
    - Prints summary to console
"""

import csv
from datetime import datetime
from collections import defaultdict
import os

# Configuration
TIMELINE_FILE = os.path.join(os.path.dirname(__file__), '..', 'data', 'timeline.csv')
OUTPUT_CSV = os.path.join(os.path.dirname(__file__), 'histogram_data.csv')
OUTPUT_TXT = os.path.join(os.path.dirname(__file__), 'histogram_summary.txt')

# 5-year bins from 1930 to present
START_YEAR = 1930
END_YEAR = datetime.now().year
BIN_SIZE = 5

# Categories to include
INCLUDED_CATEGORIES = ['Precursor Language', 'Core Language', 'Implementation', 
                      'Application', 'Publication', 'Historical Context', 
                      'Comparison', 'Current Status', 'Related Research']


def load_timeline_data(filepath):
    """Load timeline data from CSV file."""
    events = []
    with open(filepath, 'r') as f:
        reader = csv.DictReader(f)
        for row in reader:
            try:
                year = int(row['Year'])
                event = {
                    'year': year,
                    'bin_start': int(row['Bin_Start']),
                    'bin_end': int(row['Bin_End']),
                    'bin_label': row['Bin_Label'],
                    'event': row['Event'],
                    'category': row['Category'],
                    'source': row['Source']
                }
                events.append(event)
            except (ValueError, KeyError) as e:
                print(f"Warning: Skipping row due to error: {e}")
                continue
    return events


def create_bins(start_year, end_year, bin_size):
    """Create 5-year bins."""
    bins = []
    current = start_year
    while current <= end_year:
        bin_end = current + bin_size - 1
        if bin_end > end_year:
            bin_end = end_year
        bin_label = f"{current}-{bin_end}"
        bins.append({
            'start': current,
            'end': bin_end,
            'label': bin_label
        })
        current += bin_size
    return bins


def categorize_events(events, bins):
    """Categorize events into bins and count by category."""
    bin_category_counts = defaultdict(lambda: defaultdict(int))
    bin_total = defaultdict(int)
    bin_events = defaultdict(list)
    
    for event in events:
        year = event['year']
        category = event['category']
        
        # Find the bin for this year
        for bin_info in bins:
            if bin_info['start'] <= year <= bin_info['end']:
                bin_key = bin_info['label']
                bin_category_counts[bin_key][category] += 1
                bin_total[bin_key] += 1
                bin_events[bin_key].append(event)
                break
    
    return bin_category_counts, bin_total, bin_events


def save_histogram_data(output_file, bins, bin_total, bin_category_counts):
    """Save histogram data to CSV."""
    with open(output_file, 'w', newline='') as f:
        writer = csv.writer(f)
        
        # Header
        all_categories = sorted(set(cat for cats in bin_category_counts.values() for cat in cats.keys()))
        header = ['Bin_Start', 'Bin_End', 'Bin_Label', 'Total'] + all_categories
        writer.writerow(header)
        
        # Data rows
        for bin_info in bins:
            label = bin_info['label']
            total = bin_total.get(label, 0)
            categories = bin_category_counts.get(label, {})
            row = [
                bin_info['start'],
                bin_info['end'],
                label,
                total
            ]
            # Add counts for each category
            for cat in all_categories:
                row.append(categories.get(cat, 0))
            writer.writerow(row)
    
    print(f"Histogram data saved to: {output_file}")


def save_summary_text(output_file, bins, bin_total, bin_category_counts, bin_events, total_events):
    """Save text summary to file."""
    with open(output_file, 'w') as f:
        f.write("="*80 + "\n")
        f.write("MUMPS/M PROGRAMMING LANGUAGE RESEARCH - HISTOGRAM SUMMARY\n")
        f.write("="*80 + "\n\n")
        
        f.write(f"Data source: {TIMELINE_FILE}\n")
        f.write(f"Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"Total events in timeline: {total_events}\n")
        f.write(f"Date range: {START_YEAR} - {END_YEAR}\n")
        f.write(f"Bin size: {BIN_SIZE} years\n")
        f.write(f"Number of bins: {len(bins)}\n\n")
        
        f.write("-"*80 + "\n")
        f.write("EVENTS BY PERIOD:\n")
        f.write("-"*80 + "\n\n")
        
        for bin_info in bins:
            label = bin_info['label']
            count = bin_total.get(label, 0)
            if count > 0:
                categories = bin_category_counts.get(label, {})
                events = bin_events.get(label, [])
                
                f.write(f"{label}: {count} event(s)\n")
                f.write("-" * 40 + "\n")
                
                # Category breakdown
                for cat, cat_count in sorted(categories.items()):
                    f.write(f"  {cat}: {cat_count}\n")
                
                f.write("\n  Events:\n")
                for event in events:
                    f.write(f"    - {event['year']}: {event['event']} ({event['category']})\n")
                f.write("\n")
        
        f.write("-"*80 + "\n")
        f.write(f"TOTAL: {total_events} events across {len(bins)} bins\n")
        f.write("-"*80 + "\n\n")
        
        # Category summary
        f.write("-"*80 + "\n")
        f.write("BY CATEGORY:\n")
        f.write("-"*80 + "\n\n")
        
        category_totals = defaultdict(int)
        for bin_label, categories in bin_category_counts.items():
            for cat, count in categories.items():
                category_totals[cat] += count
        
        for cat, count in sorted(category_totals.items(), key=lambda x: x[1], reverse=True):
            percentage = (count / total_events * 100) if total_events > 0 else 0
            f.write(f"  {cat}: {count} ({percentage:.1f}%)\n")
        
        f.write("\n" + "="*80 + "\n")
        f.write("END OF SUMMARY\n")
        f.write("="*80 + "\n")
    
    print(f"Summary text saved to: {output_file}")


def print_summary(bins, bin_total, bin_category_counts, bin_events, total_events):
    """Print summary statistics to console."""
    print("\n" + "="*80)
    print("MUMPS/M PROGRAMMING LANGUAGE RESEARCH - HISTOGRAM SUMMARY")
    print("="*80)
    print(f"\nData source: {TIMELINE_FILE}")
    print(f"Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"Total events in timeline: {total_events}")
    print(f"Date range: {START_YEAR} - {END_YEAR}")
    print(f"Bin size: {BIN_SIZE} years")
    print(f"Number of bins: {len(bins)}")
    
    print("\n" + "-"*80)
    print("EVENTS BY PERIOD:")
    print("-"*80)
    
    for bin_info in bins:
        label = bin_info['label']
        count = bin_total.get(label, 0)
        if count > 0:
            categories = bin_category_counts.get(label, {})
            cat_str = ", ".join([f"{k}={v}" for k, v in sorted(categories.items())])
            print(f"  {label}: {count} event(s) [{cat_str}]")
    
    print("\n" + "-"*80)
    print(f"TOTAL: {total_events} events across {len([b for b in bins if bin_total.get(b['label'], 0) > 0])} active bins")
    print("-"*80)
    
    # Category summary
    print("\n" + "-"*80)
    print("BY CATEGORY:")
    print("-"*80)
    
    category_totals = defaultdict(int)
    for bin_label, categories in bin_category_counts.items():
        for cat, count in categories.items():
            category_totals[cat] += count
    
    for cat, count in sorted(category_totals.items(), key=lambda x: x[1], reverse=True):
        percentage = (count / total_events * 100) if total_events > 0 else 0
        print(f"  {cat}: {count} ({percentage:.1f}%)")


def main():
    """Main function."""
    # Load data
    print("Loading timeline data...")
    events = load_timeline_data(TIMELINE_FILE)
    print(f"Loaded {len(events)} events from timeline.")
    
    # Filter events by category if needed
    filtered_events = [e for e in events if e['category'] in INCLUDED_CATEGORIES]
    print(f"Using {len(filtered_events)} events after category filtering.")
    
    total_events = len(filtered_events)
    
    # Create bins
    bins = create_bins(START_YEAR, END_YEAR, BIN_SIZE)
    print(f"Created {len(bins)} bins from {START_YEAR} to {END_YEAR}.")
    
    # Categorize events
    bin_category_counts, bin_total, bin_events = categorize_events(filtered_events, bins)
    
    # Save outputs
    save_histogram_data(OUTPUT_CSV, bins, bin_total, bin_category_counts)
    save_summary_text(OUTPUT_TXT, bins, bin_total, bin_category_counts, bin_events, total_events)
    
    # Print summary
    print_summary(bins, bin_total, bin_category_counts, bin_events, total_events)
    
    print("\n" + "="*80)
    print("DONE: Histogram data and summary files generated successfully!")
    print("="*80)
    print("\nGenerated files:")
    print(f"  - {OUTPUT_CSV}")
    print(f"  - {OUTPUT_TXT}")
    print("\nNote: For visual histogram, install matplotlib and run generate_histogram.py")


if __name__ == '__main__':
    main()
