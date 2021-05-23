fn find_path(graph: &Vec<Vec<usize>>, u1: usize, matching: &mut Vec<usize>, vis: &mut Vec<bool>) -> bool {
    vis[u1] = true;
    for v in &graph[u1] {
        let u2 = matching[*v];
        if u2 == usize::MAX || (!vis[u2] && find_path(graph, u2, matching, vis)) {
            matching[*v] = u1;
            return true;
        }
    }
    return false;
}

pub fn max_matching(graph: &Vec<Vec<usize>>, n2: usize) -> (usize, Vec<usize>) {
    let n1 = graph.len();
    let mut matching = vec![usize::MAX; n2];
    let mut matches = 0;
    for u in 0..n1 {
        let mut vis = vec![false; n1];
        if find_path(graph, u, &mut matching, &mut vis) {
            matches += 1;
        }
    }
    return (matches, matching);
}

fn main() {
    let g = vec![vec![0, 1], vec![0]];
    let (cardinality, matching) = max_matching(&g, 2);
    assert_eq!(cardinality, 2);
    assert_eq!(matching, vec![1, 0]);
}
