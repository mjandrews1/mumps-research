## export_doc_events.R: regenerate DOC_knowledge_events.csv per the C-005
## mapping (registry C-005, wp2/PROTOCOL_CLARIFICATIONS.md). Runs at each
## DOC batch close and any time before analysis; the output is fully
## regenerated from the arm journal, so the run is idempotent and the
## journal stays the single source of truth. No instrument is modified.
## Duplicate consult rows key on (unit, entry, timestamp) per C-008
## (registry entry; roster id strings are file-level and not unique).
##
## Source rows are DOC consult lines in wp2/arms/ARM_WORKING_NOTES.md of
## the exact single-line grammar registered in wp2/analysis/README.md
## ("DOC consult rows in the arm journal"):
##
##   - DOC consult: unit <unit_id>, entry <H-nnn>, outcome
##     <hit-reuse|hit-adapt|miss>, timestamp <ISO8601>
##
## Mapping (C-005): hit-reuse and hit-adapt each emit a reuse row plus a
## hit row (adaptation counts as reuse); miss emits a miss row. Output:
## wp2/logs/DOC_knowledge_events.csv, LF endings, header exactly
## kind,target,unit,timestamp. Zero consults -> header-only file
## (present, never omitted). Fails loudly otherwise.

args <- commandArgs(trailingOnly = TRUE)
journal_path <- "wp2/arms/ARM_WORKING_NOTES.md"
out_path <- "wp2/logs/DOC_knowledge_events.csv"
roster_path <- "wp1/study-subset/units.csv"

stopifnot(file.exists(journal_path), file.exists(roster_path))
roster <- read.csv(roster_path, stringsAsFactors = FALSE, check.names = FALSE)
if (!"id" %in% names(roster) || nrow(roster) != 308)
  stop("roster contract violation: expected column 'id' and 308 units")
roster_ids <- trimws(roster$id)

## find DOC consult lines; the grammar is fixed so a strict regex both
## selects and rejects: anything matching the prefix but not the full
## grammar is a fatal error (typo discipline; nothing is skipped).
lines <- readLines(journal_path, warn = FALSE)
lines <- sub("\r$", "", lines)
prefix_re <- "^- DOC consult:"
prefix_hits <- grep(prefix_re, lines, value = TRUE)
full_re <- paste0("^- DOC consult: unit ([^,]+), entry (H-[0-9]{3,}), ",
                  "outcome (hit-reuse|hit-adapt|miss), ",
                  "timestamp ([0-9]{4}-[0-9]{2}-[0-9]{2}[T ][0-9]{2}:[0-9]{2}(:[0-9]{2})?[^ ]*)$")
bad <- prefix_hits[!grepl(full_re, prefix_hits)]
if (length(bad) > 0) {
  stop("malformed DOC consult row(s) in the journal (grammar registered in ",
       "wp2/analysis/README.md):\n", paste(bad, collapse = "\n"))
}

## parse the full-grammar rows
parse_one <- function(l) {
  m <- regmatches(l, regexec(full_re, l))[[1]]
  list(unit = trimws(m[2]), entry = m[3], outcome = m[4], ts = m[5])
}
consults <- lapply(prefix_hits, parse_one)

## validations: unit must be a roster id; one consult per (unit, entry)
unknown <- vapply(consults, function(c) !(c$unit %in% roster_ids), logical(1))
if (any(unknown)) {
  stop("DOC consult unit id(s) not in the roster:\n",
       paste(unique(consults[unknown]$unit), collapse = ", "))
}
## C-008: roster id strings are file-level and NOT unique (one id
## string appears on 9 roster rows; analysis README "Input contracts"
## #3/#5: unit_id is descriptive only and never a join key, and
## string-level dedup is forbidden because it would collapse distinct
## units). The C-005 rule "one consult per (roster unit, cookbook
## entry)" therefore cannot be checked at string level: two distinct
## roster units sharing an id string legitimately consult the same
## entry once each. The duplicate check keys on (unit, entry,
## timestamp) instead: the consult timestamp is captured per unit, so
## a repeated (unit, entry) pair at distinct timestamps is two roster
## units (e.g. the mdh_tree_level_add overloads of batches 1 and 2)
## and both are emitted; an exact repeat of all three fields is a true
## duplicate and still fails loudly.
keys <- vapply(consults, function(c) paste(c$unit, c$entry, c$ts, sep = "\r"), character(1))
if (anyDuplicated(keys) > 0) {
  d <- keys[which(duplicated(keys))]
  dk <- vapply(d, function(k) gsub("\r", " / ", k, fixed = TRUE), character(1))
  stop("duplicate consult for (unit, entry, timestamp); C-005/C-008 ",
       "allow one consult per (roster unit, cookbook entry); identical ",
       "repeats are duplicates:\n", paste(dk, collapse = "\n"))
}

## map to event rows per C-005
rows <- list()
for (c in consults) {
  if (c$outcome %in% c("hit-reuse", "hit-adapt")) {
    rows[[length(rows) + 1]] <- c("reuse", c$entry, c$unit, c$ts)
    rows[[length(rows) + 1]] <- c("hit", c$entry, c$unit, c$ts)
  } else {
    rows[[length(rows) + 1]] <- c("miss", c$entry, c$unit, c$ts)
  }
}
ev <- if (length(rows) == 0) {
  data.frame(kind = character(0), target = character(0),
            unit = character(0), timestamp = character(0),
            stringsAsFactors = FALSE)
} else {
  data.frame(kind = vapply(rows, `[[`, character(1), 1),
            target = vapply(rows, `[[`, character(1), 2),
            unit = vapply(rows, `[[`, character(1), 3),
            timestamp = vapply(rows, `[[`, character(1), 4),
            stringsAsFactors = FALSE)
}

## write LF-exact CSV with the exact header
con <- file(out_path, open = "wb")
write.table(ev, con, sep = ",", col.names = TRUE, row.names = FALSE,
            qmethod = "double", eol = "\n")
close(con)
message(sprintf("%s: %d consult row(s) -> %d event row(s)",
                out_path, length(consults), nrow(ev)))
