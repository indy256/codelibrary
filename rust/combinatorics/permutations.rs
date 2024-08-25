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
    use rstest::rstest;

    #[rstest]
    #[case(vec![0], vec![0], None)]
    #[case(vec![0, 1, 2], vec![0, 2, 1], Some(()))]
    #[case(vec![0, 3, 2, 1], vec![1, 0, 2, 3], Some(()))]
    #[case(vec![2, 1, 0], vec![2, 1, 0], None)]
    fn basic_test(
        #[case] mut permutation: Vec<usize>,
        #[case] expected_next_permutation: Vec<usize>,
        #[case] expected_result: Option<()>,
    ) {
        let result = next_permutation(&mut permutation);
        assert_eq!(permutation, expected_next_permutation);
        assert_eq!(result, expected_result);
    }
}
