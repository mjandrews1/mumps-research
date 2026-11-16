#!/usr/bin/env python3
"""
Generate histogram of MUMPS/M programming language publications and events
in 5-year bins from 1930 to present.

Usage:
    python3 generate_histogram.py

Output:
    - Creates histogram.png (visualization)
    - Creates histogram_data.csv (raw data)
    - Prints summary to console
"""

import csv
import matplotlib
matplotlib.use('Agg')  # Non-interactive backend for saving files
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
from collections import defaultdict
import os

# Configuration
TIMELINE_FILE = os.path.join(os.path.dirname(__file__), '..', 'data', 'timeline.csv')
OUTPUT_IMAGE = os.path.join(os.path.dirname(__file__), 'histogram.png')
OUTPUT_CSV = os.path.join(os.path.dirname(__file__), 'histogram_data.csv')

# 5-year bins from 1930 to present
START_YEAR = 1930
END_YEAR = datetime.now().year
BIN_SIZE = 5

# Categories to include (can be filtered)
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
    bin_data = defaultdict(lambda: defaultdict(int))
    bin_category_counts = defaultdict(lambda: defaultdict(int))
    bin_total = defaultdict(int)
    
    for event in events:
        year = event['year']
        category = event['category']
        
        # Find the bin for this year
        for bin_info in bins:
            if bin_info['start'] <= year <= bin_info['end']:
                bin_key = bin_info['label']
                bin_data[bin_key][category] += 1
                bin_category_counts[bin_key][category] += 1
                bin_total[bin_key] += 1
                break
    
    return bin_data, bin_category_counts, bin_total


def generate_histogram_data(bins, bin_total):
    """Generate data for histogram."""
    data = []
    for bin_info in bins:
        label = bin_info['label']
        start = bin_info['start']
        count = bin_total.get(label, 0)
        data.append({
            'bin_start': start,
            'bin_end': bin_info['end'],
            'bin_label': label,
            'count': count
        })
    return data


def plot_histogram(bins, bin_total, bin_category_counts, output_file):
    """Create and save histogram visualization."""
    # Prepare data
    labels = [b['label'] for b in bins]
    counts = [bin_total.get(b['label'], 0) for b in bins]
    
    # Create figure
    plt.figure(figsize=(16, 10))
    
    # Bar chart
    bars = plt.bar(labels, counts, color='steelblue', edgecolor='black')
    
    # Add value labels on bars
    for bar in bars:
        height = bar.get_height()
        if height > 0:
            plt.text(bar.get_x() + bar.get_width()/2., height,
                    f'{int(height)}',
                    ha='center', va='bottom', fontsize=9)
    
    # Styling
    plt.title('MUMPS/M Programming Language: Publications and Events by 5-Year Period (1930-Present)',
              fontsize=14, fontweight='bold', pad=20)
    plt.xlabel('Year Range', fontsize=12, labelpad=10)
    plt.ylabel('Number of Events/Publications', fontsize=12, labelpad=10)
    plt.xticks(rotation=45, ha='right', fontsize=10)
    plt.yticks(fontsize=10)
    plt.grid(axis='y', alpha=0.3, linestyle='--')
    
    # Adjust layout
    plt.tight_layout()
    
    # Save
    plt.savefig(output_file, dpi=300, bbox_inches='tight')
    plt.close()
    
    print(f"Histogram saved to: {output_file}")


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


def print_summary(bins, bin_total, bin_category_counts, events):
    """Print summary statistics."""
    print("\n" + "="*80)
    print("MUMPS/M PROGRAMMING LANGUAGE RESEARCH - HISTOGRAM SUMMARY")
    print("="*80)
    print(f"\nData source: {TIMELINE_FILE}")
    print(f"Total events in timeline: {len(events)}")
    print(f"Date range: {START_YEAR} - {END_YEAR}")
    print(f"Bin size: {BIN_SIZE} years")
    print(f"Number of bins: {len(bins)}")
    
    print("\n" + "-"*80)
    print("EVENTS BY PERIOD:")
    print("-"*80)
    
    total_events = 0
    for bin_info in bins:
        label = bin_info['label']
        count = bin_total.get(label, 0)
        total_events += count
        if count > 0:
            categories = bin_category_counts.get(label, {})
            cat_str = ", ".join([f"{k}={v}" for k, v in sorted(categories.items())])
            print(f"  {label}: {count} events [{cat_str}]")
    
    print("\n" + "-"*80)
    print(f"TOTAL: {total_events} events across {len(bins)} bins")
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
    
    # Create bins
    bins = create_bins(START_YEAR, END_YEAR, BIN_SIZE)
    print(f"Created {len(bins)} bins from {START_YEAR} to {END_YEAR}.")
    
    # Categorize events
    bin_data, bin_category_counts, bin_total = categorize_events(filtered_events, bins)
    
    # Generate histogram data
    histogram_data = generate_histogram_data(bins, bin_total)
    
    # Save outputs
    save_histogram_data(OUTPUT_CSV, bins, bin_total, bin_category_counts)
    plot_histogram(bins, bin_total, bin_category_counts, OUTPUT_IMAGE)
    
    # Print summary
    print_summary(bins, bin_total, bin_category_counts, filtered_events)
    
    print("\n" + "="*80)
    print("DONE: Histogram and data files generated successfully!")
    print("="*80)


if __name__ == '__main__':
    main()
