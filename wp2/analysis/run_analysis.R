# run_analysis.R: orchestrator (SAP §5). Run from the repo root:
#   Rscript wp2/analysis/run_analysis.R
# Fails loudly on missing inputs; never reports an absent arm as a null
# result. Writes results/ tables plus a result log recording, for every
# test: test name, raw p, adjusted p where SAP 3.4 applies, effect
# estimate with 95% CI, N analyzed, N excluded, N deferred.

for (f in c("R/sap_stats.R", "R/endpoints.R"))
  source(file.path("wp2/analysis", f))

res_dir <- "wp2/analysis/results"
dir.create(res_dir, showWarnings = FALSE, recursive = TRUE)
LOG <- file.path(res_dir, "result_log.txt")
con <- file(LOG, open = "wt")
`%||%` <- function(a, b) if (is.null(a)) b else a
log_line <- function(...) cat(..., "\n", file = con, sep = "")

log_line("WP2 SAP analysis run (UTC):",
         format(as.POSIXlt(Sys.time(), tz = "UTC")))

report_test <- function(arm_pair, endpoint, tst, effect = NULL,
                        ci = NULL, n_analyzed, n_excluded = 0,
                        n_deferred = 0, note = "") {
  log_line(sprintf("test: %s | endpoint: %s | arms: %s vs %s",
                   tst$test %||% "unknown", endpoint,
                   arm_pair[1], arm_pair[2]))
  log_line(sprintf("  raw p: %s", format(tst$p %||% NA)))
  if (!is.null(effect) && !is.null(ci))
    log_line(sprintf("  effect: %.4f  CI95: [%.4f, %.4f]",
                     effect, ci[1], ci[2]))
  log_line(sprintf("  N analyzed: %d | N excluded: %d | N deferred: %d",
                   n_analyzed, n_excluded, n_deferred))
  if (nzchar(note)) log_line("  note:", note)
}

## ---- E4/E5: per-arm coverage at study close (SAP 3.2 rules) ----------
arms_with_data <- character(0)
EVENTS <- c(PRO = "wp2/logs/PRO_knowledge_events.csv",
            BOTH = "wp2/logs/BOTH_knowledge_events.csv",
            DOC = "wp2/logs/DOC_knowledge_events.csv")
for (arm in names(EVENTS)) {
  if (!file.exists(EVENTS[[arm]])) {
    log_line(sprintf("%s coverage: NOT RUN (no events CSV at %s)",
                     arm, EVENTS[[arm]]))
    next
  }
  ev <- read_events(EVENTS[[arm]])
  units <- read_units()
  pc_file <- file.path("wp2/analysis/data",
                       paste0("precapture_", tolower(arm), "_seed.csv"))
  if (!file.exists(pc_file))
    stop(arm, " pre-capture seed list missing: no arm with misses may be analyzed without one (SAP 3.2)")
  pc <- read_precapture(pc_file)
  cov <- coverage_close(ev, pc)
  log_line(sprintf(paste0("%s study-close coverage: %d rules exercised, ",
                           "%d situations (%d hits, %d misses), ",
                           "hit rate %.4f, reuse rate %.4f; ",
                           "%d pre-capture situation rows excluded ",
                           "(descriptive footnote per SAP 3.2), ",
                           "%d blank rows tolerated as committed artifact"),
                   arm, cov$rules_exercised, cov$situations,
                   cov$hits, cov$misses, cov$hit_rate,
                   cov$reuse_rate, cov$excluded_situation_rows,
                   attr(ev, "blank_rows")))
  write.csv(data.frame(
    metric = c("hits", "situations", "misses", "reuses",
               "hit_rate", "reuse_rate", "excluded_situation_rows",
               "rules_exercised"),
    value = c(cov$hits, cov$situations, cov$misses, cov$reuses,
             cov$hit_rate, cov$reuse_rate, cov$excluded_situation_rows,
             cov$rules_exercised)),
    file.path(res_dir, paste0(tolower(arm), "_coverage_close.csv")),
    row.names = FALSE)
  arms_with_data <- c(arms_with_data, arm)
}

## ---- Cross-arm contrasts: gated on arm data presence -----------------
# E2 (per-unit checks): runs for an arm pair when both arms appear in
# the committed per-unit check export; absent arms are reported as
# NOT RUN, never as null results. Primary contrasts (H2-H5) get Holm
# adjustment at assembly; the log records raw p for each.
CHECKS <- "wp2/analysis/data/per_unit_checks.csv"
if (file.exists(CHECKS)) {
  ck_all <- read_checks(CHECKS)
  arms_present <- unique(ck_all$arm)
} else {
  ck_all <- NULL
  arms_present <- character(0)
  log_line("E2 contrasts: NOT RUN (no per-unit check export yet;",
           "DOC/DAF/BOTH outcomes pending)")
}
PAIRS <- list(H4 = c("PRO", "DOC"), H2 = c("DAF", "BASE"),
              H3 = c("DOC", "BASE"))
for (hp in names(PAIRS)) {
  pair <- PAIRS[[hp]]
  if (!all(pair %in% arms_present) || is.null(ck_all)) {
    log_line(sprintf("contrast %s (%s vs %s): NOT RUN (arm data pending)",
                     hp, pair[1], pair[2]))
    next
  }
  a1 <- ck_all[ck_all$arm == pair[1], ]
  a2 <- ck_all[ck_all$arm == pair[2], ]
  m <- merge(a1, a2, by = "unit_id", suffixes = c("_1", "_2"))
  ev_cols <- c(paste0("target_check_", 1:2))
  m <- m[apply(m[, ev_cols], 1, function(r)
    all(r %in% c("evaluated-pass", "evaluated-fail"))), , drop = FALSE]
  b <- sum(m$target_check_1 == "evaluated-pass" &
             m$target_check_2 == "evaluated-fail")
  c_n <- sum(m$target_check_1 == "evaluated-fail" &
               m$target_check_2 == "evaluated-pass")
  if (b + c_n == 0) {
    log_line(sprintf("contrast %s: no discordant pairs among %d evaluated; exact McNemar degenerate; report descriptively",
                     hp, nrow(m)))
    next
  }
  tst <- mcnemar_exact(b, c_n, alternative = "greater")
  eff <- matched_or(b, c_n)
  report_test(pair, "E2 target-check", tst, effect = eff$or,
              ci = eff$ci, n_analyzed = nrow(m), note = paste(
    "primary contrast", hp, "Holm adjustment applied at assembly"))
}

close(con)
cat("Analysis run complete. Result log:", LOG, "\n")
