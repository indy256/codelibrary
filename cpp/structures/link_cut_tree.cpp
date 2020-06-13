#include <bits/stdc++.h>

using namespace std;

// LinkCut tree with path queries. Query complexity is O(log(n)) amortized.
// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
struct Node {
    long long node_value;
    long long sub_tree_sum;
    long long add;
    bool revert;

    int size;
    Node *left;
    Node *right;
    Node *parent;

    Node(long long value)
        : node_value(value),
          sub_tree_sum(value),
          add(0),
          revert(false),
          size(1),
          left(nullptr),
          right(nullptr),
          parent(nullptr) {}

    // tests whether x is a root of a splay tree
    bool isRoot() { return parent == nullptr || (parent->left != this && parent->right != this); }

    void apply(long long v) {
        node_value += v;
        sub_tree_sum += v * size;
        add += v;
    }

    void push() {
        if (revert) {
            revert = false;
            Node *t = left;
            left = right;
            right = t;
            if (left != nullptr)
                left->revert = !left->revert;
            if (right != nullptr)
                right->revert = !right->revert;
        }
        if (add != 0) {
            if (left != nullptr)
                left->apply(add);
            if (right != nullptr)
                right->apply(add);
            add = 0;
        }
    }

    void pull() {
        sub_tree_sum = node_value + get_sub_tree_sum(left) + get_sub_tree_sum(right);
        size = 1 + get_size(left) + get_size(right);
    }

    static long long get_sub_tree_sum(Node *root) { return root == nullptr ? 0 : root->sub_tree_sum; }

    static int get_size(Node *root) { return root == nullptr ? 0 : root->size; }
};

void connect(Node *ch, Node *p, int is_left_child) {
    if (ch != nullptr)
        ch->parent = p;
    if (is_left_child != 2) {
        if (is_left_child)
            p->left = ch;
        else
            p->right = ch;
    }
}

// rotates edge (x, x.parent)
//        g            g
//       /            /
//      p            x
//     / \    ->    / \
//    x  p.r      x.l  p
//   / \              / \
// x.l x.r          x.r p.r
void rotate(Node *x) {
    Node *p = x->parent;
    Node *g = p->parent;
    bool isRootP = p->isRoot();
    bool left_child_x = (x == p->left);

    // create 3 edges: (x.r(l),p), (p,x), (x,g)
    connect(left_child_x ? x->right : x->left, p, left_child_x);
    connect(p, x, !left_child_x);
    connect(x, g, isRootP ? 2 : (p == g->left ? 1 : 0));
    p->pull();
}

// brings x to the root, balancing tree
//
// zig-zig case
//        g                                  x
//       / \               p                / \
//      p  g.r rot(p)    /   \     rot(x) x.l  p
//     / \      -->    x       g    -->       / \
//    x  p.r          / \     / \           x.r  g
//   / \            x.l x.r p.r g.r             / \
// x.l x.r                                    p.r g.r
//
// zig-zag case
//      g               g
//     / \             / \               x
//    p  g.r rot(x)   x  g.r rot(x)    /   \
//   / \      -->    / \      -->    p       g
// p.l  x           p  x.r          / \     / \
//     / \         / \            p.l x.l x.r g.r
//   x.l x.r     p.l x.l
void splay(Node *x) {
    while (!x->isRoot()) {
        Node *p = x->parent;
        Node *g = p->parent;
        if (!p->isRoot())
            g->push();
        p->push();
        x->push();
        if (!p->isRoot())
            rotate((x == p->left) == (p == g->left) ? p /*zig-zig*/ : x /*zig-zag*/);
        rotate(x);
    }
    x->push();
    x->pull();
}

// makes node x the root of the virtual tree, and also x becomes the leftmost node in its splay tree
Node *expose(Node *x) {
    Node *last = nullptr;
    for (Node *y = x; y != nullptr; y = y->parent) {
        splay(y);
        y->left = last;
        last = y;
    }
    splay(x);
    return last;
}

void make_root(Node *x) {
    expose(x);
    x->revert = !x->revert;
}

bool connected(Node *x, Node *y) {
    if (x == y)
        return true;
    expose(x);
    // now x.parent is null
    expose(y);
    return x->parent != nullptr;
}

void link(Node *x, Node *y) {
    assert(!connected(x, y));
    make_root(x);
    x->parent = y;
}

void cut(Node *x, Node *y) {
    make_root(x);
    expose(y);
    // check that exposed path consists of a single edge (y,x)
    assert(y->right == x && x->left == nullptr);
    y->right->parent = nullptr;
    y->right = nullptr;
}

long long query(Node *from, Node *to) {
    make_root(from);
    expose(to);
    return Node::get_sub_tree_sum(to);
}

void modify(Node *from, Node *to, long long delta) {
    make_root(from);
    expose(to);
    to->apply(delta);
}

// usage example
int main() {
    Node *n1 = new Node(1);
    Node *n2 = new Node(2);
    link(n1, n2);
    long long q = query(n1, n2);
    cout << q << endl;
    cut(n1, n2);
}
