#!/bin/bash
# Prime number operations in Bash

# Check if a number is prime
is_prime() {
    local n=$1
    if ((n < 2)); then
        return 1
    fi
    if ((n < 4)); then
        return 0
    fi
    if ((n % 2 == 0 || n % 3 == 0)); then
        return 1
    fi
    local i=5
    while ((i * i <= n)); do
        if ((n % i == 0 || n % (i + 2) == 0)); then
            return 1
        fi
        ((i += 6))
    done
    return 0
}

# Sieve of Eratosthenes
sieve_of_eratosthenes() {
    local n=$1
    local -a is_prime
    for ((i = 0; i <= n; i++)); do
        is_prime[i]=1
    done
    is_prime[0]=0
    is_prime[1]=0

    for ((i = 2; i * i <= n; i++)); do
        if ((is_prime[i])); then
            for ((j = i * i; j <= n; j += i)); do
                is_prime[j]=0
            done
        fi
    done

    local primes=()
    for ((i = 2; i <= n; i++)); do
        if ((is_prime[i])); then
            primes+=("$i")
        fi
    done
    echo "${primes[*]}"
}

# GCD using Euclidean algorithm
gcd() {
    local a=$1 b=$2
    while ((b != 0)); do
        local t=$b
        b=$((a % b))
        a=$t
    done
    echo "$a"
}

# LCM
lcm() {
    local a=$1 b=$2
    local g
    g=$(gcd "$a" "$b")
    echo $(( a / g * b ))
}

# Factorial
factorial() {
    local n=$1
    local result=1
    for ((i = 2; i <= n; i++)); do
        result=$((result * i))
    done
    echo "$result"
}

# Usage example
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    echo "Primes up to 50: $(sieve_of_eratosthenes 50)"

    for n in 2 7 15 23 97; do
        if is_prime "$n"; then
            echo "$n is prime"
        else
            echo "$n is not prime"
        fi
    done

    echo "GCD(12, 8) = $(gcd 12 8)"
    echo "LCM(4, 6) = $(lcm 4 6)"
    echo "10! = $(factorial 10)"
fi
