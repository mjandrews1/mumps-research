# selftest.R: known-answer checks for sap_stats implementations.
# Must pass before any confirmatory use (SAP §5). Exits nonzero on failure.
source("wp2/analysis/R/sap_stats.R")

fail <- 0
check <- function(name, cond) {
  ok <- isTRUE(cond)
  cat(if (ok) "PASS" else "FAIL", "-", name, "\n")
  if (!ok) fail <<- fail + 1
  invisible(ok)
}

# exact McNemar: b=1, c=15 -> two-sided p = 2*pbinom(1,16,0.5)
t1 <- mcnemar_exact(1, 15)
check("McNemar exact two-sided p (b=1,c=15)",
      abs(t1$p_two - 2 * pbinom(1, 16, 0.5)) < 1e-12)
t2 <- mcnemar_exact(15, 1, alternative = "greater")
check("McNemar one-sided greater non-significant when b>>c",
      t2$p > 0.99)

# matched OR: symmetric case -> OR=1
o1 <- matched_or(10, 10)
check("matched OR symmetric = 1", abs(o1$or - 1) < 1e-12 &&
        abs(o1$ci[2] - 1) > 0)
# monotone transform CI sanity: b=14,c=2 vs binom CI
o2 <- matched_or(14, 2)
bt <- binom.test(14, 16)
check("OR equals p/(1-p) transform",
      abs(o2$or - (14/16)/(2/16)) < 1e-12 &&
        abs(o2$ci[1] - (bt$conf.int[1]/(1 - bt$conf.int[1]))) < 1e-12)

# Wilcoxon paired: identical vectors -> ties fraction 1, note attached
w1 <- wilcoxon_pair(c(1, 2, 3), c(1, 2, 3))
check("Wilcoxon all-ties triggers sign-test note",
      w1$ties_fraction == 1 && !is.null(w1$note))
# known small example: a beats b consistently -> small p
w2 <- suppressWarnings(wilcoxon_pair(c(5, 6, 7, 8, 9),
                                     c(1, 2, 3, 4, 5),
                                     alternative = "greater"))
check("Wilcoxon consistent superiority p small", w2$p < 0.05)

# dod rank-sum: shifted samples
d1 <- dod_rank_sum(rnorm(50, 1), rnorm(50, 0))
check("dod rank-sum returns HL shift near 1", abs(d1$hl_shift - 1) < 0.5)

# Fisher strata + MH: constructed with an effect in every stratum
df <- data.frame(
  stratum = rep(c("simple", "core", "complex"), 2),
  hits = c(90, 80, 60, 81, 70, 45),
  situations = c(100, 100, 100, 100, 100, 100),
  arm = rep(c("PRO", "DOC"), each = 3))
f1 <- fisher_strata(df)
check("Fisher strata: 3 strata evaluated",
      length(f1$per_stratum_raw_p) == 3 &&
        all(is.finite(f1$per_stratum_raw_p)))
check("MH pooled computed", is.finite(f1$mh_p))

# Holm
h <- holm_contrasts(c(H2 = 0.03, H3 = 0.01, H4 = 0.04, H5 = 0.20))
check("Holm ordering", all(abs(unname(h$adjusted) -
                                  p.adjust(c(0.03, 0.01, 0.04, 0.20),
                                           "holm")) < 1e-12))

if (fail > 0) {
  cat("SELFTEST FAILED:", fail, "check(s)\n")
  quit(status = 1)
}
cat("SELFTEST PASSED\n")
