use std::rc::Rc;

pub struct Node {
    left: Option<Rc<Node>>,
    right: Option<Rc<Node>>,
    sum: i32,
}

impl Node {
    fn new(sum: i32) -> Self {
        Self {
            left: None,
            right: None,
            sum,
        }
    }

    fn new_parent(left: &Rc<Node>, right: &Rc<Node>) -> Self {
        Self {
            left: Some(left.clone()),
            right: Some(right.clone()),
            sum: left.sum + right.sum,
        }
    }
}

pub struct PersistentTree;
impl PersistentTree {
    pub fn build(left: i32, right: i32) -> Rc<Node> {
        let node = if left == right {
            Node::new(0)
        } else {
            let mid = (left + right) >> 1;
            Node::new_parent(&Self::build(left, mid), &Self::build(mid + 1, right))
        };
        Rc::new(node)
    }

    pub fn sum(from: i32, to: i32, root: &Node, left: i32, right: i32) -> i32 {
        if from > right || left > to {
            0
        } else if from <= left && right <= to {
            root.sum
        } else {
            let mid = (left + right) >> 1;
            Self::sum(from, to, root.left.as_ref().unwrap(), left, mid)
                + Self::sum(from, to, root.right.as_ref().unwrap(), mid + 1, right)
        }
    }

    pub fn set(pos: i32, value: i32, root: &Node, left: i32, right: i32) -> Rc<Node> {
        let node = if left == right {
            Node::new(value)
        } else {
            let mid = (left + right) >> 1;
            if pos <= mid {
                Node::new_parent(
                    &Self::set(pos, value, root.left.as_ref().unwrap(), left, mid),
                    root.right.as_ref().unwrap(),
                )
            } else {
                Node::new_parent(
                    root.left.as_ref().unwrap(),
                    &Self::set(pos, value, root.right.as_ref().unwrap(), mid + 1, right),
                )
            }
        };
        Rc::new(node)
    }
}

#[cfg(test)]
mod tests {
    use crate::structures::persistent_tree::PersistentTree;

    #[test]
    fn basic_test() {
        let n = 10;
        let t1 = PersistentTree::build(0, n - 1);
        let t2 = PersistentTree::set(0, 1, &t1, 0, n - 1);
        assert_eq!(PersistentTree::sum(0, n - 1, &t1, 0, n - 1), 0);
        assert_eq!(PersistentTree::sum(0, n - 1, &t2, 0, n - 1), 1);
    }
}
