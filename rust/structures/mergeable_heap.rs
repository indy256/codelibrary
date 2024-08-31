use std::cell::RefCell;
use std::mem::{replace, swap, take};
use std::rc::Rc;

struct Heap<V> {
    value: V,
    left: Option<Rc<RefCell<Heap<V>>>>,
    right: Option<Rc<RefCell<Heap<V>>>>,
}

impl<V: PartialOrd + Clone> Heap<V> {
    fn new(value: V) -> Option<Rc<RefCell<Heap<V>>>> {
        Some(Rc::new(RefCell::new(Self {
            value,
            left: None,
            right: None,
        })))
    }
    fn merge<'a>(
        mut a: &'a Option<Rc<RefCell<Heap<V>>>>,
        mut b: &'a Option<Rc<RefCell<Heap<V>>>>,
    ) -> Option<Rc<RefCell<Heap<V>>>> {
        if a.is_none() {
            return b.clone();
        }
        if b.is_none() {
            return a.clone();
        }
        if a.as_ref()?.borrow().value > b.as_ref()?.borrow().value {
            swap(&mut a, &mut b);
        }
        if rand::random() {
            let mut ra = a.as_ref()?.borrow_mut();
            let l = take(&mut ra.left);
            let r = take(&mut ra.right);
            ra.left = r;
            ra.right = l;
        }
        let m = Self::merge(replace(&mut &a.as_ref()?.borrow_mut().left, &None), b);
        a.as_ref()?.borrow_mut().left = m;
        a.clone()
    }

    fn remove_min(heap: &Option<Rc<RefCell<Heap<V>>>>) -> (Option<Rc<RefCell<Heap<V>>>>, V) {
        let h = heap.as_ref().unwrap().borrow();
        (Self::merge(&h.left, &h.right), h.value.clone())
    }

    fn add(heap: &Option<Rc<RefCell<Heap<V>>>>, value: V) -> Option<Rc<RefCell<Heap<V>>>> {
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
