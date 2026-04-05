#!/bin/bash
# Insertion Sort implementation in Bash

insertion_sort() {
    local -n arr=$1
    local n=${#arr[@]}

    for ((i = 1; i < n; i++)); do
        local key=${arr[i]}
        local j=$((i - 1))
        while ((j >= 0 && arr[j] > key)); do
            arr[j + 1]=${arr[j]}
            ((j--))
        done
        arr[j + 1]=$key
    done
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(12 11 13 5 6)
    echo "Original: ${data[*]}"
    insertion_sort data
    echo "Sorted:   ${data[*]}"
fi
