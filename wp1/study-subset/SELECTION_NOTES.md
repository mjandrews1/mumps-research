# Study Subset selection notes (2026-11-14)

## Canonical selection input: PRISTINE source tree

The unit selection (seed 20261113, settings per `protocol.json`) MUST be
run against a pristine extraction of the frozen tarball
(`mumps-14-may-2026.tgz`, sha256
`621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe`),
NOT against a tree that has been built in place.

## Why (observed 2026-11-14, before the pilot)

Running the vendor build (`configure` + `make -B`) inside the source tree
modifies sources (e.g. `compiler-mumps.c` hash changes) and introduces
21 extra translation units: a selection pass over the built tree yielded
329 units (c=117), while the pristine tree yields **308 units
(c=96, cpp=212; strata 102/102/104)** — matching the committed reference
run `tools/examples/EXAMPLE_protocol_seed-20261113.json` and the study
design. The pre-registered reference ran on a smaller extraction (89
scanned files vs 124 in the full tree); all 89 files appear in the full
tree with identical hashes (except built-tree `compiler-mumps.c`), and
the extra files contribute no units, so the unit pool is identical.

Conclusion: the reference assignment is valid; the built tree is not a
valid selection input. No protocol deviation — this was resolved before
the pilot batch, same as the oracle freeze corrections.

## Reproduction recipe

```bash
tar xzf mumps-14-may-2026.tgz -C /tmp/clean \
  mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library
python3 tools/select_study_subset.py \
  --source /tmp/clean/mumps/Mumps-Language-Processors/Mumps-Interpreter-Compiler-Library \
  --unit function --batches 10 --seed 20261113 --out wp1/study-subset
```

Verify: `protocol.json` equals
`tools/examples/EXAMPLE_protocol_seed-20261113.json` except the
`generated` timestamp and `source_dir` fields; `source_hashes` identical.
Independently regenerated and verified 2026-11-14 from the frozen
tarball (byte-identical `units.csv` / `batch_plan.md` /
`micro_replication.csv`; identical assignment).

The archived outputs in this directory were generated exactly this way
on the m1-build reference container and are the working Study Subset.
Regeneration with the same seed is verification; any other seed or any
non-pristine source tree is a protocol deviation and must be logged.
