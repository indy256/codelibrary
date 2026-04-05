#!/bin/bash
# Bubble Sort implementation in Bash

bubble_sort() {
    local -n arr=$1
    local n=${#arr[@]}
    local swapped

    for ((i = 0; i < n - 1; i++)); do
        swapped=0
        for ((j = 0; j < n - i - 1; j++)); do
            if ((arr[j] > arr[j + 1])); then
                local tmp=${arr[j]}
                arr[j]=${arr[j + 1]}
                arr[j + 1]=$tmp
                swapped=1
            fi
        done
        if ((swapped == 0)); then
            break
        fi
    done
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(64 34 25 12 22 11 90)
    echo "Original: ${data[*]}"
    bubble_sort data
    echo "Sorted:   ${data[*]}"
fi
