# Characterization Oracle — Freeze Protocol

The oracle is the behavioral ground truth for all five arms
(RESEARCH_DESIGN §2.4 step 5): **build the original Mumps II
Interpreter-Compiler Library from source, capture observed behavior, freeze
it in version control before any Kotlin translation begins.** Grading of all
arms happens against this frozen artifact, which removes grading drift over
the 18-month timeline.

## What is captured

`cases/*.mps` — 15 deterministic characterization programs covering the
construct families the study samples: literals/IO, arithmetic operators
(`+ - * / \ # **`), string comparison operators (`[ ] ] ?`), `$Piece`,
`$Extract`/`$Find`, `$Justify`/`$FNumber`, conditionals (`& ! '` `$Data`
`$Get`), all `For` forms + `$Order` iteration, globals (`^`, `$Order`,
`$Query`, `$Data`, `$Get`, `Kill`, `Merge`), `$Translate`/`$ZConvert`,
pattern matching (`?`), indirection/`Xecute`, user functions (`Do`,
extrinsic `$$`), numeric canonicalization, `New` scoping.

Deliberately excluded (nondeterministic / environment-dependent, would
corrupt the freeze): `$Random`, `$Horolog`/`$ZHoro`, `Read` from stdin,
`Open`/`Use`/`Close` on external files, `Job`, `Hang`, sockets.

## Freeze procedure (run ONCE on the reference build)

```bash
./build-mumps-fleet.sh check                    # smoke test passes first
cd oracle
./run_oracle.sh <path-to-mumps2c> expected      # capture
python3 freeze_oracle.py expected               # writes manifest.json
git add oracle/ && git commit -m "M1: freeze characterization oracle (v1, 15 cases)"
```

From that commit forward the oracle is **immutable**. Later verification:

```bash
./run_oracle.sh <path-to-mumps2c> /tmp/fresh
python3 freeze_oracle.py /tmp/fresh --verify
```

## Grading rule

A translated unit in any arm passes a case iff the Kotlin program's
normalized stdout equals the frozen `.out` byte-for-byte. Divergences are
recorded as defects with the case id as evidence. **If the original build
itself later fails to verify** (e.g. new glibc changes float formatting),
that is a protocol deviation: log it in DEVIATIONS.md and do NOT re-freeze
silently.

## Extending the oracle

Additions between cycles are allowed (new cases, never edited old cases),
versioned `oracle_version += 1` in the manifest; earlier-arm results stay
graded against the version current when that batch was translated.
