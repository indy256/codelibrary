use rand::Rng;
use std::cmp::max;
use std::ops::AddAssign;

struct Treap<K, V> {
    value: V,
    mx: V,
    add: V,
    // keys should be unique
    key: K,
    prio: i64,
    size: usize,
    left: Option<Box<Treap<K, V>>>,
    right: Option<Box<Treap<K, V>>>,
}

impl<K: Copy + PartialOrd, V: Copy + PartialEq + AddAssign + Ord + Default> Treap<K, V> {
    fn new(key: K, value: V) -> Option<Box<Treap<K, V>>> {
        Some(Box::new(Self {
            key,
            value,
            mx: value,
            add: V::default(),
            prio: rand::thread_rng().gen_range(0..1_000_000_000),
            size: 1,
            left: None,
            right: None,
        }))
    }

    fn apply(&mut self, v: V) {
        self.value += v;
        self.mx += v;
        self.add += v;
    }

    fn push(&mut self) {
        if self.add != V::default() {
            if self.left.is_some() {
                self.left.as_mut().unwrap().apply(self.add);
            }
            if self.right.is_some() {
                self.right.as_mut().unwrap().apply(self.add);
            }
            self.add = V::default();
        }
    }

    fn pull(&mut self) {
        self.size = 1 + Self::size(&self.left) + Self::size(&self.right);
        self.mx = Self::mx(Self::mx(self.value, &self.left), &self.right);
    }

    pub fn size(node: &Option<Box<Treap<K, V>>>) -> usize {
        node.as_ref().map_or(0, |t| t.size)
    }

    fn mx(value: V, node: &Option<Box<Treap<K, V>>>) -> V {
        node.as_ref().map_or(value, |t| max(t.mx, value))
    }

    #[allow(clippy::type_complexity)]
    pub fn split(
        root: Option<Box<Treap<K, V>>>,
        min_right: K,
    ) -> (Option<Box<Treap<K, V>>>, Option<Box<Treap<K, V>>>) {
        Self::inner_split(root, min_right, |a, b| a >= b)
    }

    #[allow(clippy::type_complexity)]
    pub fn strict_split(
        root: Option<Box<Treap<K, V>>>,
        min_right: K,
    ) -> (Option<Box<Treap<K, V>>>, Option<Box<Treap<K, V>>>) {
        Self::inner_split(root, min_right, |a, b| a > b)
    }

    #[allow(clippy::type_complexity)]
    fn inner_split<F: Fn(K, K) -> bool>(
        root: Option<Box<Treap<K, V>>>,
        min_right: K,
        cmp: F,
    ) -> (Option<Box<Treap<K, V>>>, Option<Box<Treap<K, V>>>) {
        if root.is_none() {
            return (None, None);
        }
        let mut node = root.unwrap();
        node.push();
        if cmp(node.key, min_right) {
            let (l, r) = Self::inner_split(node.left, min_right, cmp);
            node.left = r;
            node.pull();
            (l, Some(node))
        } else {
            let (l, r) = Self::inner_split(node.right, min_right, cmp);
            node.right = l;
            node.pull();
            (Some(node), r)
        }
    }

    pub fn merge(
        a: Option<Box<Treap<K, V>>>,
        b: Option<Box<Treap<K, V>>>,
    ) -> Option<Box<Treap<K, V>>> {
        if a.is_none() {
            return b;
        }
        if b.is_none() {
            return a;
        }
        let mut left = a.unwrap();
        let mut right = b.unwrap();
        left.push();
        right.push();
        if left.prio > right.prio {
            left.right = Self::merge(left.right, Some(right));
            left.pull();
            Some(left)
        } else {
            right.left = Self::merge(Some(left), right.left);
            right.pull();
            Some(right)
        }
    }

    pub fn insert(root: Option<Box<Treap<K, V>>>, key: K, value: V) -> Option<Box<Treap<K, V>>> {
        let (l, r) = Self::split(root, key);
        Self::merge(Self::merge(l, Treap::new(key, value)), r)
    }

    pub fn remove(root: Option<Box<Treap<K, V>>>, key: K) -> Option<Box<Treap<K, V>>> {
        let (l, r) = Self::split(root, key);
        Self::merge(l, Self::strict_split(r, key).1)
    }

    pub fn modify(
        root: Option<Box<Treap<K, V>>>,
        ll: K,
        rr: K,
        delta: V,
    ) -> Option<Box<Treap<K, V>>> {
        let (l1, r1) = Self::strict_split(root, rr);
        let (l2, mut r2) = Self::split(l1, ll);
        #[allow(clippy::option_map_unit_fn)]
        r2.as_mut().map(|t| t.apply(delta));
        Self::merge(Self::merge(l2, r2), r1)
    }

    pub fn query(root: Option<Box<Treap<K, V>>>, ll: K, rr: K) -> (Option<Box<Treap<K, V>>>, V) {
        let (l1, r1) = Self::strict_split(root, rr);
        let (l2, r2) = Self::split(l1, ll);
        let mx = r2.as_ref().map_or(V::default(), |t| t.mx);
        (Self::merge(Self::merge(l2, r2), r1), mx)
    }
}

#[cfg(test)]
mod tests {
    use crate::structures::treap::Treap;

    #[test]
    fn basic_test() {
        let mut treap: Option<Box<Treap<u32, u32>>> = None;
        treap = Treap::insert(treap, 1, 10);
        treap = Treap::insert(treap, 2, 20);
        let (t, q1) = Treap::query(treap, 1, 2);
        treap = t;
        assert_eq!(q1, 20);

        treap = Treap::modify(treap, 1, 2, 100);

        let (t, q2) = Treap::query(treap, 1, 2);
        treap = t;
        assert_eq!(q2, 120);

        treap = Treap::remove(treap, 2);

        let (_t, q3) = Treap::query(treap, 1, 2);
        assert_eq!(q3, 110);
    }
}
