#!/bin/bash
# Matrix operations in Bash

# Matrix is stored as a flat array with dimensions
# Format: rows cols val1 val2 val3 ...

# Get element at (row, col)
matrix_get() {
    local -a mat=($1)
    local row=$2 col=$3
    local cols=${mat[1]}
    echo "${mat[$((2 + row * cols + col))]}"
}

# Matrix multiply
matrix_multiply() {
    local -a a=($1)
    local -a b=($2)
    local ar=${a[0]} ac=${a[1]}
    local br=${b[0]} bc=${b[1]}

    if ((ac != br)); then
        echo "Error: incompatible dimensions" >&2
        return 1
    fi

    local result=("$ar" "$bc")
    for ((i = 0; i < ar; i++)); do
        for ((j = 0; j < bc; j++)); do
            local sum=0
            for ((k = 0; k < ac; k++)); do
                local va=${a[$((2 + i * ac + k))]}
                local vb=${b[$((2 + k * bc + j))]}
                sum=$((sum + va * vb))
            done
            result+=("$sum")
        done
    done
    echo "${result[*]}"
}

# Print matrix
matrix_print() {
    local -a mat=($1)
    local rows=${mat[0]} cols=${mat[1]}
    for ((i = 0; i < rows; i++)); do
        for ((j = 0; j < cols; j++)); do
            printf "%4d " "${mat[$((2 + i * cols + j))]}"
        done
        echo
    done
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    a="2 3 1 2 3 4 5 6"
    b="3 2 7 8 9 10 11 12"
    echo "Matrix A:"
    matrix_print "$a"
    echo "Matrix B:"
    matrix_print "$b"
    result=$(matrix_multiply "$a" "$b")
    echo "A * B:"
    matrix_print "$result"
fi
