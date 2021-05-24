struct DisjointSets {
    p: Vec<usize>
}

impl DisjointSets {
    pub fn new(n: usize) -> Self {
        DisjointSets { p: (0..n).collect() }
    }

    pub fn root(&mut self, x: usize) -> usize {
        if x != self.p[x] {
            self.p[x] = self.root(self.p[x]);
        }
        self.p[x]
    }

    pub fn unite(&mut self, a: usize, b: usize) {
        let a = self.root(a);
        let b = self.root(b);
        self.p[b] = a;
    }
}

fn main() {
    let mut ds = DisjointSets::new(3);
    ds.unite(0, 2);
    assert_eq!(ds.root(0), 0);
    assert_eq!(ds.root(1), 1);
    assert_eq!(ds.root(2), 0);
}
