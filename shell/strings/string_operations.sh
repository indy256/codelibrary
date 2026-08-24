#!/bin/bash
# Common string operations in Bash

# Reverse a string
string_reverse() {
    local str="$1"
    local len=${#str}
    local reversed=""
    for ((i = len - 1; i >= 0; i--)); do
        reversed+="${str:i:1}"
    done
    echo "$reversed"
}

# Check if string is palindrome
is_palindrome() {
    local str="$1"
    local reversed
    reversed=$(string_reverse "$str")
    [[ "$str" == "$reversed" ]]
}

# Count occurrences of substring
count_substring() {
    local string="$1"
    local substring="$2"
    local count=0
    local tmp="$string"
    while [[ "$tmp" == *"$substring"* ]]; do
        ((count++))
        tmp="${tmp#*"$substring"}"
    done
    echo "$count"
}

# Simple pattern matching
find_substring() {
    local haystack="$1"
    local needle="$2"
    local pos="${haystack%%"$needle"*}"
    if [[ "$pos" == "$haystack" ]]; then
        echo "-1"
        return 1
    fi
    echo "${#pos}"
    return 0
}

# Convert to uppercase
to_upper() {
    echo "${1^^}"
}

# Convert to lowercase
to_lower() {
    echo "${1,,}"
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    echo "Reverse 'hello': $(string_reverse 'hello')"

    if is_palindrome "racecar"; then
        echo "'racecar' is a palindrome"
    fi

    if ! is_palindrome "hello"; then
        echo "'hello' is not a palindrome"
    fi

    echo "Count 'ab' in 'ababab': $(count_substring 'ababab' 'ab')"
    echo "Find 'world' in 'hello world': $(find_substring 'hello world' 'world')"
    echo "Uppercase 'hello': $(to_upper 'hello')"
    echo "Lowercase 'HELLO': $(to_lower 'HELLO')"
fi
