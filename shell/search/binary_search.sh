#!/bin/bash
# Binary Search implementation in Bash

binary_search() {
    local -n arr=$1
    local target=$2
    local lo=0
    local hi=$(( ${#arr[@]} - 1 ))

    while ((lo <= hi)); do
        local mid=$(( (lo + hi) / 2 ))
        if ((arr[mid] == target)); then
            echo "$mid"
            return 0
        elif ((arr[mid] < target)); then
            lo=$((mid + 1))
        else
            hi=$((mid - 1))
        fi
    done

    echo "-1"
    return 1
}

# Binary search: find first index where arr[i] >= target
binary_search_lower_bound() {
    local -n arr=$1
    local target=$2
    local lo=0
    local hi=${#arr[@]}

    while ((lo < hi)); do
        local mid=$(( (lo + hi) / 2 ))
        if ((arr[mid] < target)); then
            lo=$((mid + 1))
        else
            hi=$mid
        fi
    done

    echo "$lo"
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(2 5 8 12 16 23 38 56 72 91)
    echo "Array: ${data[*]}"
    result=$(binary_search data 23)
    echo "Search for 23: index $result"
    result=$(binary_search data 50)
    echo "Search for 50: index $result"
    result=$(binary_search_lower_bound data 20)
    echo "Lower bound for 20: index $result"
fi
