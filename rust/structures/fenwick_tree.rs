use std::ops::AddAssign;

struct Fenwick<T> {
    t: Vec<T>
}

impl<T: AddAssign + Copy + From<i32>> Fenwick<T> {
    fn new(n: usize) -> Self {
        Fenwick { t: vec![0.into(); n] }
    }

    pub fn add(&mut self, mut i: usize, value: T) {
        while i < self.t.len() {
            self.t[i] += value;
            i |= i + 1;
        }
    }

    // sum[0..i]
    pub fn sum(&self, mut i: i32) -> T {
        let mut res: T = 0.into();
        while i >= 0 {
            res += self.t[i as usize];
            i = (i & (i + 1)) - 1;
        }
        return res;
    }
}

fn main() {
    let mut t = Fenwick::<i32>::new(3);
    t.add(0, 4);
    t.add(1, 5);
    t.add(2, 5);
    t.add(2, 5);
    assert_eq!(t.sum(0), 4);
    assert_eq!(t.sum(2), 19);
}
