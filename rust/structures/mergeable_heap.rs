use std::mem::swap;
use std::rc::Rc;

struct Heap<V> {
    value: V,
    left: Option<Rc<Heap<V>>>,
    right: Option<Rc<Heap<V>>>,
}

impl<V: PartialOrd + Clone> Heap<V> {
    fn new(value: V) -> Option<Rc<Heap<V>>> {
        Some(Rc::new(Self {
            value,
            left: None,
            right: None,
        }))
    }
    fn merge(a: &Option<Rc<Heap<V>>>, b: &Option<Rc<Heap<V>>>) -> Option<Rc<Heap<V>>> {
        if a.is_none() {
            return b.clone();
        }
        if b.is_none() {
            return a.clone();
        }
        let mut ra = a.clone()?;
        let mut rb = b.clone()?;
        if ra.value > rb.value {
            swap(&mut ra, &mut rb);
        }
        if rand::random() {
            ra = Rc::new(Heap {
                value: ra.value.clone(),
                left: ra.right.clone(),
                right: ra.left.clone(),
            });
        }
        Some(Rc::new(Heap {
            value: ra.value.clone(),
            left: Self::merge(&ra.left.clone(), &Some(rb)),
            right: ra.right.clone(),
        }))
    }

    fn remove_min(heap: &Option<Rc<Heap<V>>>) -> (Option<Rc<Heap<V>>>, V) {
        let h = heap.as_ref().unwrap();
        (
            Self::merge(&h.as_ref().left, &h.as_ref().right),
            h.as_ref().value.clone(),
        )
    }

    fn add(heap: &Option<Rc<Heap<V>>>, value: V) -> Option<Rc<Heap<V>>> {
        Self::merge(heap, &Heap::new(value))
    }
}

#[cfg(test)]
mod tests {
    use crate::structures::mergeable_heap::Heap;

    #[test]
    fn basic_test() {
        let mut h = None;
        h = Heap::add(&h, 3);
        h = Heap::add(&h, 1);
        h = Heap::add(&h, 2);
        let mut values = Vec::new();
        while h.is_some() {
            let (heap, min_value) = Heap::remove_min(&h);
            values.push(min_value);
            h = heap;
        }
        assert_eq!(values, [1, 2, 3]);
    }
}
