fn gcd(a: i32, b: i32) -> i32 {
    return if b == 0 { a.abs() } else { gcd(b, a % b) };
}

// returns { gcd(a,b), x, y } such that gcd(a,b) = a*x + b*y
fn euclid(mut a: i64, mut b: i64) -> [i64; 3] {
    let mut x: i64 = 1;
    let mut y: i64 = 0;
    let mut x1: i64 = 0;
    let mut y1: i64 = 1;
    // invariant: a=a_orig*x+b_orig*y, b=a_orig*x1+b_orig*y1
    while b != 0 {
        let q = a / b;
        let _x1 = x1;
        let _y1 = y1;
        let _b = b;
        x1 = x - q * x1;
        y1 = y - q * y1;
        b = a - q * b;
        x = _x1;
        y = _y1;
        a = _b;
    }
    return if a > 0 { [a, x, y] } else { [-a, -x, -y] };
}

fn main() {
    assert_eq!(gcd(15, 20), 5);
    assert_eq!(euclid(6, 9), [3, -1, 1]);
}
