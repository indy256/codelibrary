#!/bin/bash
# Quicksort implementation in Bash

quicksort() {
    local -n arr=$1
    local low=$2
    local high=$3

    if ((low >= high)); then
        return
    fi

    local pivot=${arr[high]}
    local i=$((low - 1))

    for ((j = low; j < high; j++)); do
        if ((arr[j] <= pivot)); then
            ((i++))
            local tmp=${arr[i]}
            arr[i]=${arr[j]}
            arr[j]=$tmp
        fi
    done

    ((i++))
    local tmp=${arr[i]}
    arr[i]=${arr[high]}
    arr[high]=$tmp

    quicksort arr $low $((i - 1))
    quicksort arr $((i + 1)) $high
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(38 27 43 3 9 82 10)
    echo "Original: ${data[*]}"
    quicksort data 0 $(( ${#data[@]} - 1 ))
    echo "Sorted:   ${data[*]}"
fi
