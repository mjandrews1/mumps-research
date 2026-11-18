## export_per_unit_checks.R: seed data/per_unit_checks.csv (C-005 tooling)
## Generates the per-unit check export skeleton from the roster: one
## row per roster unit per requested arm, all four checks `todo`.
## Statuses are filled by the per-unit check backfill pass
## (C-001 cycle-10 buffer; definitions in wp2/PER_UNIT_CHECKS.md,
## registry C-002) and the file is committed once evaluated.
## All data is committed data (SAP 5); fails loudly otherwise.

args <- commandArgs(trailingOnly = TRUE)
arms <- if (length(args) >= 1) strsplit(args[[1]], ",")[[1]] else c("BASE", "PRO")

roster_path <- "wp1/study-subset/units.csv"
out_path <- "wp2/analysis/data/per_unit_checks.csv"

stopifnot(file.exists(roster_path))
roster <- read.csv(roster_path, stringsAsFactors = FALSE, check.names = FALSE)
if (!"id" %in% names(roster) || nrow(roster) != 308) {
  stop(sprintf("roster contract violation: expected column 'id' and 308 units, got columns [%s], %d rows",
               paste(names(roster), collapse = ","), nrow(roster)))
}
## Authoritative unit key: the units.csv physical row (header = line 1,
## units = rows 2-309; the row-count semantics registered under
## C-004). The `id` strings are file-level and NOT unique
## (e.g. libmpscpp.cpp::mdh_tree_level_add appears on 9 rows), so
## `unit_id` alone cannot key a row and `roster_row` is the join key.
roster_row <- seq_len(nrow(roster)) + 1L
## guard against a reordered or merged roster file
lines <- sub("\r$", "", readLines(roster_path, warn = FALSE))
first_field <- vapply(strsplit(lines[-1], ",", fixed = TRUE), `[[`, character(1), 1)
if (!identical(first_field, roster$id)) {
  stop("roster file row order does not match the parsed table; refusing to emit keys")
}
unit_ids <- trimws(roster$id)
valid_arms <- c("BASE", "DOC", "PRO", "DAF", "BOTH")
if (!all(arms %in% valid_arms)) {
  stop(sprintf("unknown arm(s): %s", paste(setdiff(arms, valid_arms), collapse = ",")))
}

rows <- do.call(rbind, lapply(arms, function(arm) {
  data.frame(roster_row = roster_row,
             unit_id = unit_ids,
             arm = arm,
             code_check = "todo",
             output_check = "todo",
             runtime_check = "todo",
             target_check = "todo",
             stringsAsFactors = FALSE)
}))

dir.create(dirname(out_path), showWarnings = FALSE, recursive = TRUE)
con <- file(out_path, open = "wb")
write.table(rows, con, sep = ",", row.names = FALSE, qmethod = "double", eol = "\n")
close(con)
message(sprintf("%s: wrote %d rows (%d units x %d arm(s): %s)",
                out_path, nrow(rows), length(unit_ids), length(arms),
                paste(arms, collapse = ",")))
