use std::collections::BinaryHeap;

// https://cp-algorithms.com/graph/dijkstra_sparse.html
// O(E*log(V)) time and O(E) memory
pub fn dijkstra_heap(graph: &Vec<Vec<(usize, i32)>>, s: usize) -> (Vec<i32>, Vec<usize>) {
    let n = graph.len();
    let mut prio = vec![i32::MAX; n];
    let mut pred = vec![usize::MAX; n];
    let mut q = BinaryHeap::<(i32, usize)>::new();
    prio[s] = 0;
    q.push((0, s));
    while let Some((d, u)) = q.pop() {
        if -d != prio[u] { continue; }
        for (v, len) in &graph[u] {
            let nprio = prio[u] + len;
            if prio[*v] > nprio {
                prio[*v] = nprio;
                pred[*v] = u;
                q.push((-nprio, *v));
            }
        }
    }
    return (prio, pred);
}

fn main() {
    let g = vec![vec![(1, 10), (2, 8)], vec![(2, -5)], vec![]];
    let (prio, pred) = dijkstra_heap(&g, 0);
    assert_eq!(prio, vec![0, 10, 5]);
    assert_eq!(pred, vec![usize::MAX, 0, 1]);
}
