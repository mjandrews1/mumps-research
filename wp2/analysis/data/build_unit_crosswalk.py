#!/usr/bin/env python3
"""Build wp2/analysis/data/unit_id_crosswalk.csv (C-013 canonical
cross-walk, PI-ruled 2026-11-27). Derived ONLY from the
pre-registered instruments: wp1/study-subset/batch_plan.md (unit
order per batch), wp1/study-subset/units.csv (row ordinals), and
wp1/order_assignments.json (per-arm committed batch orders per
C-001). Run from the repo root. Join discipline (C-013 item 2)
remains binding: unit_id + batch, never the bare numeric id; 28
unit_id strings recur across batches (the roster key is the
ENTRY, unit_id + batch)."""
import json, re, csv, collections

batches = {}
cur = None
for line in open('wp1/study-subset/batch_plan.md'):
    m = re.match(r'^### (B\d+)$', line.strip())
    if m: cur = m.group(1); batches[cur] = []; continue
    m = re.match(r'^- `([^`]+)` \[(c|cpp)\] — (\w+), LOC (\d+), score ([\d.]+)', line)
    if m and cur: batches[cur].append((m.group(1), m.group(2), m.group(3), int(m.group(4)), float(m.group(5))))
assert sum(len(v) for v in batches.values()) == 308

uc = collections.defaultdict(list)
with open('wp1/study-subset/units.csv', newline='') as f:
    r = csv.reader(f); next(r)
    for i, row in enumerate(r, start=1):
        uc[(row[0], row[-1])].append(i)  # data row ordinal; row 1 = first unit (header consumed)

uc_count = {k: len(v) for k, v in uc.items()}
bp_count = collections.Counter((u[0], b[1:]) for b in batches for u in batches[b])
mismatch = [k for k in bp_count if uc_count.get(k, 0) != bp_count[k]]
print("units.csv rows:", sum(uc_count.values()), "mismatched (unit,batch) keys:", len(mismatch), mismatch[:5])

oa = json.load(open('wp1/order_assignments.json'))['assignments']
rows = []
gid = 0
for b in ['B%d' % i for i in range(1, 11)]:
    for e in batches[b]:
        gid += 1
        rows.append({'global_unit_id': gid, 'unit_id': e[0], 'lang': e[1], 'stratum': e[2],
                     'loc': e[3], 'score': e[4], 'batch': b})
for arm, blist in oa.items():
    n = 0
    for b in blist:
        for r in [x for x in rows if x['batch'] == b]:
            n += 1
            r[arm.lower() + '_seq'] = n
    assert n == 308, (arm, n)

used = collections.Counter()
miss = 0
for r in rows:
    key = (r['unit_id'], r['batch'][1:])
    cands = uc.get(key, [])
    idx = used[key]; used[key] += 1
    if idx < len(cands): r['units_csv_row'] = cands[idx]
    else: r['units_csv_row'] = ''; miss += 1
print("rows without units_csv_row:", miss)
rows.sort(key=lambda r: r['global_unit_id'])
cols = ['global_unit_id', 'units_csv_row', 'unit_id', 'lang', 'stratum', 'loc', 'score', 'batch',
        'base_seq', 'doc_seq', 'pro_seq', 'daf_seq', 'both_seq']
with open('wp2/analysis/data/unit_id_crosswalk.csv', 'w', newline='') as f:
    w = csv.DictWriter(f, fieldnames=cols, lineterminator='\n')
    w.writeheader()
    for r in rows: w.writerow({c: r.get(c) for c in cols})
print("written", len(rows), "rows")
by = {r['global_unit_id']: r for r in rows}
for g in [216, 237, 240, 241, 244, 245, 246]:
    print(g, by[g]['unit_id'], '| DOC', by[g]['doc_seq'], '| DAF', by[g]['daf_seq'], '| BOTH', by[g]['both_seq'])
for r in rows:
    if r['units_csv_row'] in (43, 77):
        print('record', r['units_csv_row'], '=', r['unit_id'], 'batch', r['batch'])
