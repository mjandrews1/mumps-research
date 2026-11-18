## backfill_checks.R: guarded workflow helper for the per-unit check
## backfill pass (C-001 cycle-10 buffer; definitions registered by
## C-002 in wp2/PER_UNIT_CHECKS.md). It STAMPS evidence a human has
## produced; it never infers a check result from a grade, an oracle
## run, or any other input. The evidence workflow is documented in
## wp2/analysis/README.md ("Backfill tooling").
##
## Subcommands:
##   report                              evaluated fraction per arm x check
##   verify-roster                       every (roster_row, arm) row's
##                                       unit_id matches the roster id
##   set <arm> <check> <roster_row> <status> [--force]
##                                       stamp one cell; status in
##                                       {todo, evaluated-pass, evaluated-fail};
##                                       refuses to overwrite a non-todo
##                                       cell unless --force is given
##
## All writes keep the export format exactly (write.table, qmethod
## double, LF endings), keying stays roster_row-first per C-005.

args <- commandArgs(trailingOnly = TRUE)
export_path <- "wp2/analysis/data/per_unit_checks.csv"
roster_path <- "wp1/study-subset/units.csv"
checks <- c("code_check", "output_check", "runtime_check", "target_check")
vocab <- c("todo", "evaluated-pass", "evaluated-fail")
arms_valid <- c("BASE", "DOC", "PRO", "DAF", "BOTH")

read_export <- function() {
  if (!file.exists(export_path))
    stop("missing ", export_path, " (run R/export_per_unit_checks.R first)")
  ck <- utils::read.csv(export_path, stringsAsFactors = FALSE)
  need <- c("roster_row", "unit_id", "arm", checks)
  if (!all(need %in% names(ck)))
    stop("per-unit check export schema violation: expected ", paste(need, collapse = ","))
  if (anyDuplicated(ck[, c("roster_row", "arm")]) > 0)
    stop("duplicate (roster_row, arm) rows in the export")
  ck
}

write_export <- function(ck) {
  con <- file(export_path, open = "wb")
  write.table(ck, con, sep = ",", col.names = TRUE, row.names = FALSE,
              qmethod = "double", eol = "\n")
  close(con)
}

cmd <- if (length(args) >= 1) args[1] else ""
force <- "--force" %in% args

if (cmd == "report") {
  ck <- read_export()
  for (a in unique(ck$arm)) {
    sub <- ck[ck$arm == a, ]
    cat(sprintf("arm %s: %d units\n", a, nrow(sub)))
    for (j in checks) {
      tab <- table(sub[[j]])
      ev <- sum(tab[c("evaluated-pass", "evaluated-fail")])
      frac <- if (nrow(sub) > 0) ev / nrow(sub) else NA_real_
      cat(sprintf("  %-13s evaluated %d (%.1f%%): pass %d fail %d todo %d\n",
                  j, ev, 100 * frac,
                  if ("evaluated-pass" %in% names(tab)) tab[["evaluated-pass"]] else 0L,
                  if ("evaluated-fail" %in% names(tab)) tab[["evaluated-fail"]] else 0L,
                  if ("todo" %in% names(tab)) tab[["todo"]] else 0L))
    }
  }
} else if (cmd == "verify-roster") {
  stopifnot(file.exists(roster_path))
  roster <- read.csv(roster_path, stringsAsFactors = FALSE, check.names = FALSE)
  if (!"id" %in% names(roster) || nrow(roster) != 308)
    stop("roster contract violation")
  ck <- read_export()
  for (i in seq_len(nrow(ck))) {
    rid <- trimws(roster$id[ck$roster_row[i] - 1])
    if (!identical(trimws(ck$unit_id[i]), rid))
      stop(sprintf("row %d: roster_row %d (arm %s) unit_id '%s' != roster id '%s'",
                   i, ck$roster_row[i], ck$arm[i], ck$unit_id[i], rid))
  }
  cat("verify-roster OK: all", nrow(ck), "export rows match the roster\n")
} else if (cmd == "set") {
  if (length(args) < 5)
    stop("usage: backfill_checks.R set <arm> <check> <roster_row> <status> [--force]")
  a <- args[2]; j <- args[3]; rr <- as.integer(args[4]); st <- args[5]
  if (!(a %in% arms_valid)) stop("unknown arm: ", a)
  if (!(j %in% checks)) stop("unknown check column: ", j, " (use one of ", paste(checks, collapse=", "), ")")
  if (!(st %in% vocab)) stop("unknown status: ", st, " (vocabulary: ", paste(vocab, collapse = ", "), ")")
  if (is.na(rr)) stop("roster_row must be an integer (units.csv physical row, 2-309)")
  ck <- read_export()
  hit <- which(ck$arm == a & ck$roster_row == rr)
  if (length(hit) != 1)
    stop(sprintf("expected exactly 1 row for arm %s roster_row %d, found %d",
                 a, rr, length(hit)))
  cur <- ck[hit, j]
  if (cur != "todo" && !force)
    stop(sprintf("refusing to overwrite %s (%s arm %s roster_row %d): non-todo cell; use --force to correct a stamp",
                 j, cur, a, rr))
  ck[hit, j] <- st
  write_export(ck)
  cat(sprintf("set %s %s roster_row %d: %s -> %s\n", a, j, rr, cur, st))
} else {
  stop("unknown command '", cmd, "' (use report | verify-roster | set)")
}
