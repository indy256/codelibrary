pub fn next_permutation(p: &mut [usize]) -> Option<()> {
    let a = (0..p.len() - 1).rfind(|&i| p[i] < p[i + 1])?;
    let b = (a + 1..p.len()).rfind(|&i| p[a] < p[i]).unwrap();
    p.swap(a, b);
    p[a + 1..].reverse();
    Some(())
}

#[cfg(test)]
mod tests {
    use crate::next_permutation;

    #[test]
    fn basic_test() {
        let mut p: [usize; 4] = [0, 3, 2, 1];
        let res = next_permutation(&mut p);
        assert_eq!(p, [1, 0, 2, 3]);
        assert_eq!(res, Some(()));
    }
}
