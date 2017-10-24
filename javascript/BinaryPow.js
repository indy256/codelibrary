function pow(a, b) {
    var res = 1;
    while (b > 0) {
        if ((b & 1) !== 0) {
            res = res * a;
        }
        a = a * a;
        b = b >> 1;
    }
    return res;
}

// tests
console.log(1024 === pow(2, 10));
