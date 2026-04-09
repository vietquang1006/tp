#!/bin/sh
# Thinh: I used AI assistance to help draft and refine this script,
# especially for aggregating Gradle checks, running all check-* scripts,
# and fixing the path-handling logic. I reviewed and adapted the final version.

# Run gradle + all check-* scripts, aggregate result

dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd) || exit 1
ret=0

# Run Gradle checks
echo "== Running Gradle checks ==\n"
if ! "$dir"/gradlew clean test jacocoTestReport checkstyleMain checkstyleTest check coverage; then
    ret=1
fi

# Run all check-* scripts, adapted from `.github/run-checks.sh`
echo "== Running shell checks ==\n"
for checkscript in "$dir"/.github/check-*; do
    if ! "$checkscript"; then
        ret=1
    fi
done

if [ "$ret" -eq 0 ]; then
    echo "== ✅  All tests ran successfully ==\n"
else
    echo "== ❌  One or more tests failed, please revise ==\n"
fi

exit $ret
