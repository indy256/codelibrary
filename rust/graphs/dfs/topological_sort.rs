fn dfs(graph: &[Vec<usize>], used: &mut [bool], order: &mut Vec<usize>, u: usize) {
    used[u] = true;
    for v in &graph[u] {
        if !used[*v] {
            dfs(graph, used, order, *v);
        }
    }
    order.push(u);
}

pub fn topological_sort(graph: &[Vec<usize>]) -> Vec<usize> {
    let n = graph.len();
    let mut used = vec![false; n];
    let mut order = Vec::<usize>::new();
    for i in 0..n {
        if !used[i] {
            dfs(graph, &mut used, &mut order, i);
        }
    }
    order.reverse();
    return order;
}

#[cfg(test)]
mod tests {
    use crate::topological_sort;

    #[test]
    fn basic_test() {
        let g = vec![vec![0], vec![], vec![0, 1]];
        let order = topological_sort(&g);
        assert_eq!(order, [2, 1, 0]);
    }
}
