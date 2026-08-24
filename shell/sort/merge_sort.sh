#!/bin/bash
# Merge Sort implementation in Bash

merge_sort() {
    local -n arr=$1
    local n=${#arr[@]}

    if ((n <= 1)); then
        return
    fi

    local mid=$((n / 2))

    local left=("${arr[@]:0:mid}")
    local right=("${arr[@]:mid}")

    merge_sort left
    merge_sort right

    local i=0 j=0 k=0
    local left_n=${#left[@]}
    local right_n=${#right[@]}

    while ((i < left_n && j < right_n)); do
        if ((left[i] <= right[j])); then
            arr[k]=${left[i]}
            ((i++))
        else
            arr[k]=${right[j]}
            ((j++))
        fi
        ((k++))
    done

    while ((i < left_n)); do
        arr[k]=${left[i]}
        ((i++))
        ((k++))
    done

    while ((j < right_n)); do
        arr[k]=${right[j]}
        ((j++))
        ((k++))
    done
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    data=(38 27 43 3 9 82 10)
    echo "Original: ${data[*]}"
    merge_sort data
    echo "Sorted:   ${data[*]}"
fi
