# endpoints.R: endpoint readers and derived quantities (SAP §2 and §3.2).
# Readers fail loudly on missing files or schema violations (hardening
# precedent: never silently report zero events).

EVENT_KINDS <- c("reuse", "hit", "miss")

# Events CSV: kind,target,unit,timestamp; LF endings; header row 1.
# NOTE: the committed log's unit strings are file-level, and one
# (unit, target) string pair can carry several roster units in the same
# file (verified on the PRO log: 2300 row pairs share a string key).
# Rows are therefore counted as-is: the committed log already contains
# one reuse/hit pair per (roster unit, rule) per the capture protocol,
# so row counts are the situation counts. Never dedup by string key.
read_events <- function(path) {
  if (!file.exists(path))
    stop("missing events CSV: ", path, " (per SAP §5 the committed log is the only input; refusing to run)")
  ev <- utils::read.csv(path, stringsAsFactors = FALSE,
                        colClasses = "character")
  need <- c("kind", "target", "unit", "timestamp")
  if (!all(need %in% names(ev)))
    stop("events CSV schema violation: expected columns ", need,
         ", got ", names(ev))
  blank <- sum(is.na(ev$kind) | ev$kind == "")
  if (blank == 1) {
    # the one known committed artifact (PRO log row 1034 blank line)
    ev <- ev[!(is.na(ev$kind) | ev$kind == ""), ]
    warning("tolerated exactly one blank row (committed artifact); any more fails")
  }
  bad <- setdiff(unique(ev$kind), EVENT_KINDS)
  if (length(bad))
    stop("events CSV unknown kind values: ", bad)
  blank2 <- sum(is.na(ev$kind) | ev$kind == "")
  if (blank2 > 0)
    stop("events CSV has ", blank2 + blank, " blank kind rows; only one is a known artifact")
  attr(ev, "blank_rows") <- blank
  ev
}

# Roster: CRLF, rows 2-309 = the 308 units; strip CR on the last column.
read_units <- function(path = "roster/units.csv") {
  if (!file.exists(path)) stop("missing roster: ", path)
  u <- utils::read.csv(path, stringsAsFactors = FALSE)
  names(u) <- sub("\r$", "", names(u))
  for (nm in names(u)) u[[nm]] <- sub("\r$", "", u[[nm]])
  u
}

# Per-unit check export (SAP §2 E2; contract in wp2/analysis/README.md).
# Evaluated units only for E2; unevaluated reported, never imputed (§4).
read_checks <- function(path = "wp2/analysis/data/per_unit_checks.csv") {
  if (!file.exists(path))
    stop("missing per-unit check export: ", path,
         " (E2 requires the committed export; refusing to run)")
  ck <- utils::read.csv(path, stringsAsFactors = FALSE)
  need <- c("unit_id", "arm", "code_check", "output_check",
            "runtime_check", "target_check")
  if (!all(need %in% names(ck)))
    stop("per-unit check export schema violation: expected ", need)
  ck
}

# Deviation export (E3).
read_deviations <- function(path = "wp2/analysis/data/deviations.csv") {
  if (!file.exists(path))
    stop("missing deviation export: ", path, " (E3 requires the committed export)")
  dv <- utils::read.csv(path, stringsAsFactors = FALSE)
  need <- c("unit_id", "arm", "count", "notes")
  if (!all(need %in% names(dv)))
    stop("deviation export schema violation: expected ", need)
  if (any(dv$count < 0)) stop("negative deviation count")
  dv
}

# Pre-capture history exclusion list (SAP §3.2). Columns: unit, rule
# (plus an optional note); unit/rule values are exactly the strings in
# the arm's events CSV. Exclusion is by (unit, rule) match on the
# committed rows (SAP: excluded from E4/E5 numerators and denominators,
# reported descriptively in a footnote).
read_precapture <- function(path) {
  if (!file.exists(path))
    stop("missing pre-capture exclusion list: ", path)
  pc <- utils::read.csv(path, stringsAsFactors = FALSE)
  need <- c("unit", "rule")
  if (!all(need %in% names(pc)))
    stop("precapture schema violation: expected at least ", need)
  pc
}

# E4/E5 at study close with the SAP §3.2 rules applied. Row counts are
# situation counts (see read_events); pre-capture (unit, rule) rows are
# excluded; verification-only rows enter as logged (outcome real,
# coverage as recorded).
coverage_close <- function(ev, pc) {
  key <- paste(ev$unit, ev$target, sep = "\r###\r")  # collision-proof sep
  if (any(grepl("\r###\r", ev$unit)) || any(grepl("\r###\r", ev$target)))
    stop("unit/target separator collision in events CSV")
  pc_keys <- paste(pc$unit, pc$rule, sep = "\r###\r")
  ex <- key %in% pc_keys & ev$kind %in% c("hit", "miss")
  ex_reuse <- key %in% pc_keys & ev$kind == "reuse"
  sit <- ev[ev$kind %in% c("hit", "miss") & !ex, ]
  hits <- sum(sit$kind == "hit")
  misses <- sum(sit$kind == "miss")
  reuses <- sum(ev$kind == "reuse" & !ex_reuse)
  out <- list(hits = hits, misses = misses,
              situations = hits + misses, reuses = reuses,
              excluded_situation_rows = sum(ex),
              excluded_reuse_rows = sum(ex_reuse),
              rules_exercised = length(unique(ev$target[ev$kind == "hit"])))
  out$hit_rate <- if (out$situations > 0) hits / out$situations else NA_real_
  out$reuse_rate <- if (hits > 0) reuses / hits else NA_real_
  out
}

# Arm gating (README: never report absent arms as null results).
assert_arm_data <- function(arm, arms_with_data) {
  if (!arm %in% arms_with_data)
    stop("comparison not run: no data for arm ", arm,
         " (report as not-run, never as null (wp2/analysis/README.md))")
  invisible(TRUE)
}
