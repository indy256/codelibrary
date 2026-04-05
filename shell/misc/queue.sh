#!/bin/bash
# Queue data structure implementation in Bash

declare -a QUEUE=()
QUEUE_FRONT=0

queue_enqueue() {
    QUEUE+=("$1")
}

queue_dequeue() {
    if ((QUEUE_FRONT >= ${#QUEUE[@]})); then
        echo "Error: queue is empty" >&2
        return 1
    fi
    echo "${QUEUE[QUEUE_FRONT]}"
    unset 'QUEUE[QUEUE_FRONT]'
    ((QUEUE_FRONT++))
}

queue_peek() {
    if ((QUEUE_FRONT >= ${#QUEUE[@]})); then
        echo "Error: queue is empty" >&2
        return 1
    fi
    echo "${QUEUE[QUEUE_FRONT]}"
}

queue_size() {
    echo $(( ${#QUEUE[@]} - QUEUE_FRONT ))
}

queue_is_empty() {
    ((QUEUE_FRONT >= ${#QUEUE[@]}))
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    queue_enqueue 10
    queue_enqueue 20
    queue_enqueue 30
    echo "Size: $(queue_size)"
    echo "Peek: $(queue_peek)"
    echo "Dequeue: $(queue_dequeue)"
    echo "Dequeue: $(queue_dequeue)"
    echo "Size: $(queue_size)"
fi
