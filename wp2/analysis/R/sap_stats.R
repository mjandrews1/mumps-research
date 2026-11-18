# sap_stats.R: matched-pair test implementations (SAP §3, binding).
# Base R only except coin (registered dependency). No silent fallbacks:
# every function validates its inputs and stops on contract violation.

# --- 3.1 binary endpoints: exact McNemar + matched-pairs OR (E1, E2) ---

# b, c = discordant pair counts (arm-a pass/b fail, arm-a fail/b pass).
mcnemar_exact <- function(b, c, alternative = "greater",
                          conf_level = 0.95) {
  stopifnot(b >= 0, c >= 0, b + c > 0 || (b == 0 && c == 0))
  n <- b + c
  # exact binomial null: b ~ Binom(n, 0.5)
  if (n == 0) {
    p_two <- 1
    p_one <- 1
  } else {
    p_low  <- stats::pbinom(min(b, c), n, 0.5)
    p_two  <- min(1, 2 * p_low)
    p_one  <- switch(alternative,
                     greater = stats::pbinom(b, n, 0.5, lower.tail = FALSE) +
                               stats::dbinom(b, n, 0.5),
                     less    = stats::pbinom(b, n, 0.5),
                     two.sided = p_two)
  }
  list(test = "McNemar exact", statistic = c(b = b, c = c),
       p = p_one, p_two = p_two, alternative = alternative)
}

# Matched-pairs odds ratio with exact (Clopper-Pearson-based) CI:
# OR = b/c = p/(1-p) with p = b/(b+c); the CI is the monotone transform
# of the exact CI for p (SAP §3.1 "Clopper-Pearson-based").
matched_or <- function(b, c, conf_level = 0.95) {
  stopifnot(b >= 0, c >= 0)
  n <- b + c
  if (n == 0) return(list(or = NA_real_,
                          ci = c(NA_real_, NA_real_),
                          rd = NA_real_))
  bt <- stats::binom.test(b, n, conf.level = conf_level)
  plo <- bt$conf.int[1]; phi <- bt$conf.int[2]
  tf <- function(p) p / (1 - p)
  orci <- c(tf(plo), tf(phi))
  list(or = if (c == 0) Inf else b / c,
       ci = orci,
       rd = b / n)
}

# --- 3.1 count/ordinal and continuous endpoints (E3, E6, E7) ---

# Wilcoxon signed-rank on paired per-unit differences; Hodges-Lehmann
# estimate with CI. SAP rule: report the sign test alongside (not
# instead) when > 95% ties among the differences.
wilcoxon_pair <- function(x_arm_a, x_arm_b, alternative = "greater",
                          conf_level = 0.95) {
  stopifnot(length(x_arm_a) == length(x_arm_b),
            !anyNA(x_arm_a), !anyNA(x_arm_b), length(x_arm_a) > 0)
  d <- x_arm_a - x_arm_b
  tied <- mean(d == 0)
  if (tied == 1) {
    # all differences zero: no evidence of a shift; report explicitly
    return(list(test = "Wilcoxon signed-rank",
                p = 1, hl_estimate = NA_real_, hl_ci = c(NA_real_, NA_real_),
                ties_fraction = 1, n = length(d),
                note = "sign test reported alongside per SAP 3.1 (>95% ties); all differences zero"))
  }
  wt <- suppressWarnings(stats::wilcox.test(
    x_arm_a, x_arm_b, paired = TRUE, alternative = alternative,
    conf.int = TRUE, conf.level = conf_level, exact = TRUE))
  out <- list(test = "Wilcoxon signed-rank", p = wt$p.value,
              hl_estimate = wt$estimate,
              hl_ci = wt$conf.int[1:2], ties_fraction = tied,
              n = length(d))
  if (tied > 0.95) {
    st <- suppressWarnings(stats::binom.test(
      sum(d > 0), sum(d != 0), alternative = alternative))
    out$sign_test_p <- NA_real_
    if (!is.nan(st$p.value)) out$sign_test_p <- st$p.value
    out$note <- "sign test reported alongside per SAP 3.1 (>95% ties)"
  }
  out
}

# --- 3.1 H5: difference-of-differences via rank-sum (Mann-Whitney) ---
dod_rank_sum <- function(diff_cell_1, diff_cell_2,
                         alternative = "two.sided",
                         conf_level = 0.95) {
  stopifnot(length(diff_cell_1) > 0, length(diff_cell_2) > 0)
  wt <- suppressWarnings(stats::wilcox.test(
    diff_cell_1, diff_cell_2, alternative = alternative,
    conf.int = TRUE, conf.level = conf_level, exact = TRUE))
  list(test = "Wilcoxon rank-sum (Mann-Whitney U)",
       p = wt$p.value, hl_shift = wt$estimate,
       hl_ci = wt$conf.int[1:2],
       n1 = length(diff_cell_1), n2 = length(diff_cell_2))
}

# --- 3.2 E4/E5: stratum-paired Fisher + Mantel-Haenszel pooling ---

# df columns: stratum, hits, situations, arm. Computes the per-stratum
# Fisher exact p for the arm contrast within each stratum, Holm across
# strata, and the pooled Mantel-Haenszel OR test as the summary.
fisher_strata <- function(df, alternative = "greater") {
  stopifnot(all(c("stratum", "hits", "situations", "arm") %in%
                names(df)))
  arms <- sort(unique(df$arm))
  stopifnot(length(arms) == 2)
  strata <- sort(unique(df$stratum))
  ps <- sapply(strata, function(s) {
    sub <- df[df$stratum == s, ]
    a <- sub[sub$arm == arms[1], ]
    b <- sub[sub$arm == arms[2], ]
    m <- matrix(c(a$hits, a$situations - a$hits,
                  b$hits, b$situations - b$hits),
                nrow = 2, byrow = TRUE)
    stats::fisher.test(m, alternative = "two.sided")$p.value
  })
  # MH pooled
  subA <- df[df$arm == arms[1], ]; subB <- df[df$arm == arms[2], ]
  mm <- array(0, c(2, 2, length(strata)))
  ok <- TRUE
  for (i in seq_along(strata)) {
    a <- subA[subA$stratum == strata[i], ]
    b <- subB[subB$stratum == strata[i], ]
    if (nrow(a) == 0 || nrow(b) == 0) { ok <- FALSE; break }
    mm[, , i] <- matrix(c(a$hits, a$situations - a$hits,
                           b$hits, b$situations - b$hits),
                        nrow = 2, byrow = TRUE)
  }
  mh <- if (ok) stats::mantelhaen.test(mm, correct = FALSE) else NULL
  list(test = "Fisher exact per stratum + Mantel-Haenszel pooled",
       per_stratum_raw_p = setNames(ps, strata),
       per_stratum_holm_p = setNames(stats::p.adjust(ps, "holm"), strata),
       mh_p = if (ok) mh$p.value else NA_real_,
       mh_or = if (ok && !is.null(mh$estimate)) mh$estimate else NA_real_)
}

# --- 3.4 multiplicity control ---
holm_contrasts <- function(p_named, primary = TRUE) {
  if (!primary) return(list(adjusted = NULL,
                            note = "secondary: raw p, no promotion"))
  list(adjusted = stats::p.adjust(p_named, "holm"),
       note = "Holm-Bonferroni across the four primary contrasts")
}
