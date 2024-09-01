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

    fn merge(a: Option<Box<Heap<V>>>, b: Option<Box<Heap<V>>>) -> Option<Box<Heap<V>>> {
        if a.is_none() {
            return b;
        }
        if b.is_none() {
            return a;
        }
        let mut ra = a.unwrap();
        let mut rb = b.unwrap();
        if ra.value > rb.value {
            swap(&mut ra, &mut rb);
        }
        if rand::random() {
            swap(&mut ra.left, &mut ra.right);
        }
        ra.left = Self::merge(ra.left, Some(rb));
        Some(ra)
    }

    fn remove_min(heap: Option<Box<Heap<V>>>) -> (Option<Box<Heap<V>>>, V) {
        let h = heap.unwrap();
        (Self::merge(h.left, h.right), h.value)
    }

    fn add(heap: Option<Box<Heap<V>>>, value: V) -> Option<Box<Heap<V>>> {
        Self::merge(heap, Heap::new(value))
    }
}

#[cfg(test)]
mod tests {
    use crate::structures::mergeable_heap::Heap;
    use rand::seq::SliceRandom;
    use rand::thread_rng;
    use rstest::rstest;

    #[rstest]
    #[case(&mut [])]
    #[case(&mut [0])]
    #[case(&mut [1, 1])]
    #[case(&mut [3, 1, 2])]
    fn basic_test(#[case] seq: &mut [u32]) {
        test(seq);
    }

    #[test]
    fn big_test1() {
        let mut values = (0..10_000).collect::<Vec<u32>>();
        values.shuffle(&mut thread_rng());
        test(&mut values);
    }

    #[test]
    fn big_test2() {
        let mut values = vec![0; 10_000];
        values.shuffle(&mut thread_rng());
        test(&mut values);
    }

    fn test(seq: &mut [u32]) {
        let mut heap = seq.iter().fold(None, |h, v| Heap::add(h, v));
        let mut heap_sorted_values = Vec::new();
        while heap.is_some() {
            let (updated_heap, min_value) = Heap::remove_min(heap);
            heap = updated_heap;
            heap_sorted_values.push(*min_value);
        }
        seq.sort();
        assert_eq!(heap_sorted_values, seq);
    }
}
