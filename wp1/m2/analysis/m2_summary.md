# WP1/M2 — Translator C++ Output Characterization (summary)

Static characterization of the mumps2c translator's C++ output
for the frozen-oracle cases (capture layout: flat wp2/translator-oracle).
Behavioral authority: the frozen interpreter oracle (wp1/oracle/expected/).

Oracle anchoring: 15 cases characterized, 15 frozen-oracle cases

| Case | C++ files | Total LOC | Non-blank LOC | Labels | API symbols |
|------|-----------|-----------|---------------|--------|------------|
| case_01_literals | 1 | 241 | 217 | 0 | 4 |
| case_02_set_arith | 1 | 304 | 274 | 0 | 4 |
| case_03_strings | 1 | 364 | 315 | 0 | 5 |
| case_04_piece | 1 | 283 | 253 | 0 | 4 |
| case_05_extract_find | 1 | 305 | 268 | 0 | 5 |
| case_06_justify_fnumber | 1 | 258 | 228 | 0 | 4 |
| case_07_conditionals | 1 | 380 | 329 | 0 | 4 |
| case_08_loops | 1 | 532 | 446 | 2 | 4 |
| case_09_globals | 1 | 526 | 457 | 0 | 8 |
| case_10_translate_case | 1 | 312 | 280 | 0 | 5 |
| case_11_pattern | 1 | 319 | 276 | 0 | 5 |
| case_12_indirection | 1 | 250 | 217 | 0 | 4 |
| case_13_user_functions | 0 | 0 | 0 | 0 | 0 |
| case_14_numeric_forms | 1 | 300 | 267 | 0 | 4 |
| case_15_hang_io_ops | 1 | 233 | 206 | 0 | 4 |

## Corpus totals

- Generated C++ files: 14
- Total LOC: 4607 (non-blank 4033)
- String literals: 916; numeric literals: 783
- Goto labels: 2

## Control-flow construct counts (corpus)

- break: 7
- case: 2
- continue: 34
- do: 2
- else: 60
- for: 37
- goto: 6
- if: 323
- return: 112
- switch: 0
- while: 65

## Runtime API surface (symbol → files using it)

- `Global`: 14
- `global`: 14
- `globalOrder`: 14
- `mumpsc`: 14
- `Mumps`: 2
- `GlobalData`: 1
- `GlobalGet`: 1
- `GlobalOrder`: 1
- `MDH`: 1
- `MUMPS`: 1
- `globals`: 1
