use std::mem::swap;

struct Heap<V> {
    value: V,
    left: Option<Box<Heap<V>>>,
    right: Option<Box<Heap<V>>>,
}

impl<V: PartialOrd> Heap<V> {
    fn new(value: V) -> Option<Box<Heap<V>>> {
        Some(Box::new(Self {
            value,
            left: None,
            right: None,
        }))
    }

    pub fn merge(a: Option<Box<Heap<V>>>, b: Option<Box<Heap<V>>>) -> Option<Box<Heap<V>>> {
        if a.is_none() {
            return b;
        }
        if b.is_none() {
            return a;
        }
        let mut ha = a.unwrap();
        let mut hb = b.unwrap();
        if ha.value > hb.value {
            swap(&mut ha, &mut hb);
        }
        if rand::random() {
            swap(&mut ha.left, &mut ha.right);
        }
        ha.left = Self::merge(ha.left, Some(hb));
        Some(ha)
    }

    pub fn insert(heap: Option<Box<Heap<V>>>, value: V) -> Option<Box<Heap<V>>> {
        Self::merge(heap, Heap::new(value))
    }

    pub fn remove_min(heap: Option<Box<Heap<V>>>) -> (Option<Box<Heap<V>>>, V) {
        let h = heap.unwrap();
        (Self::merge(h.left, h.right), h.value)
    }
}

#[cfg(test)]
mod tests {
    use crate::structures::mergeable_heap::Heap;
    use proptest::collection::vec;
    use proptest::{prop_assert_eq, proptest};
    use rand::seq::SliceRandom;
    use rand::thread_rng;
    use rstest::rstest;

    #[rstest]
    #[case(&mut [])]
    #[case(&mut [0])]
    #[case(&mut [1, 1])]
    #[case(&mut [3, 1, 2])]
    fn basic_test(#[case] seq: &mut [u32]) {
        let heap_sorted_values = sort_with_heap(seq);
        seq.sort();
        assert_eq!(heap_sorted_values, seq);
    }

    #[test]
    fn big_test1() {
        let mut seq = (0..10_000).collect::<Vec<u32>>();
        seq.shuffle(&mut thread_rng());
        let heap_sorted_values = sort_with_heap(&seq);
        seq.sort();
        assert_eq!(heap_sorted_values, seq);
    }

    #[test]
    fn big_test2() {
        let mut seq = vec![0; 10_000];
        seq.shuffle(&mut thread_rng());
        let heap_sorted_values = sort_with_heap(&seq);
        seq.sort();
        assert_eq!(heap_sorted_values, seq);
    }

    proptest! {
        #[test]
        fn test_random(mut seq in vec(0u32..3, 0..10)) {
            let heap_sorted_values = sort_with_heap(&seq);
            seq.sort();
            prop_assert_eq!(heap_sorted_values, seq);
        }
    }

    fn sort_with_heap(seq: &[u32]) -> Vec<u32> {
        let mut heap = seq.iter().fold(None, Heap::insert);
        let mut heap_sorted_values = Vec::new();
        while heap.is_some() {
            let (updated_heap, min_value) = Heap::remove_min(heap);
            heap = updated_heap;
            heap_sorted_values.push(*min_value);
        }
        heap_sorted_values
    }
}
