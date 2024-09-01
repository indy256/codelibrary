// invariant: f[lo] == false, f[hi] == true
pub fn binary_search_first_true<F: Fn(i32) -> bool>(
    f: F,
    from_inclusive: i32,
    to_inclusive: i32,
) -> i32 {
    let mut lo = from_inclusive - 1;
    let mut hi = to_inclusive + 1;
    while hi - lo > 1 {
        let mid = (lo + hi) / 2;
        if !f(mid) {
            lo = mid;
        } else {
            hi = mid;
        }
    }
    hi
}

#[cfg(test)]
mod tests {
    use crate::misc::binary_search::binary_search_first_true;
    use rstest::rstest;

    #[rstest]
    #[case(100, 0)]
    #[case(100, 1)]
    #[case(100, 50)]
    #[case(100, 100)]
    fn basic_test(#[case] n: i32, #[case] pos: i32) {
        assert_eq!(binary_search_first_true(|i| { i >= pos }, 0, n), pos);
    }
}
