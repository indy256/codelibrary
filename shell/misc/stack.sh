#!/bin/bash
# Stack data structure implementation in Bash

declare -a STACK=()

stack_push() {
    STACK+=("$1")
}

stack_pop() {
    local n=${#STACK[@]}
    if ((n == 0)); then
        echo "Error: stack underflow" >&2
        return 1
    fi
    local top="${STACK[n - 1]}"
    unset 'STACK[n - 1]'
    echo "$top"
}

stack_peek() {
    local n=${#STACK[@]}
    if ((n == 0)); then
        echo "Error: stack is empty" >&2
        return 1
    fi
    echo "${STACK[n - 1]}"
}

stack_size() {
    echo "${#STACK[@]}"
}

stack_is_empty() {
    ((${#STACK[@]} == 0))
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    stack_push 10
    stack_push 20
    stack_push 30
    echo "Size: $(stack_size)"
    echo "Peek: $(stack_peek)"
    echo "Pop: $(stack_pop)"
    echo "Pop: $(stack_pop)"
    echo "Size: $(stack_size)"
fi
