#!/bin/bash
# Linear Search implementation in Bash

linear_search() {
    local -n arr=$1
    local target=$2
    local n=${#arr[@]}

    for ((i = 0; i < n; i++)); do
        if ((arr[i] == target)); then
            echo "$i"
            return 0
        fi
    done

    echo "-1"
    return 1
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(10 23 45 70 11 15)
    echo "Array: ${data[*]}"
    result=$(linear_search data 70)
    echo "Search for 70: index $result"
    result=$(linear_search data 99)
    echo "Search for 99: index $result"
fi
