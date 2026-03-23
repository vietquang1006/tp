#!/bin/sh
# Run gradle + all check-* scripts, aggregate result

dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd) || exit 1
ret=0

# Run Gradle checks
echo "== Running Gradle checks == \n"
if ! "$dir"/gradlew clean test jacocoTestReport checkstyleMain checkstyleTest check coverage; then
    ret=1
fi

# Run all check-* scripts, adapted from `.github/run-checks.sh`
echo "== Running shell checks == \n"
for checkscript in "$dir"/.github/check-*; do
    if ! "$checkscript"; then
        ret=1
    fi
done

echo "== Done == \n"

exit $ret
