[![GitHub stars](https://img.shields.io/github/stars/indy256/codelibrary.svg?style=flat&label=star)](https://github.com/indy256/codelibrary/)
[![Java CI](https://github.com/indy256/codelibrary/actions/workflows/java.yml/badge.svg)](https://github.com/indy256/codelibrary/actions/workflows/java.yml)
[![C++ CI](https://github.com/indy256/codelibrary/actions/workflows/cpp.yml/badge.svg)](https://github.com/indy256/codelibrary/actions/workflows/cpp.yml)
[![Rust CI](https://github.com/indy256/codelibrary/actions/workflows/rust.yml/badge.svg)](https://github.com/indy256/codelibrary/actions/workflows/rust.yml)
[![License](https://img.shields.io/badge/license-UNLICENSE-green.svg)](https://github.com/indy256/codelibrary/blob/main/UNLICENSE)

### Collection of algorithms and data structures in C++, Java, Kotlin, Python and Rust

#### Data structures

- [x] Segment tree [**c++**](cpp/structures/segment_tree.h) [**java**](java/structures/SegmentTree.java) [**kotlin**](kotlin/SegmentTree.kt)
- [x] Segment tree without recursion [**c++**](cpp/structures/segment_tree_without_recursion.cpp) [**java**](java/structures/SegmentTreeWithoutRecursion.java)
- [x] 2d tree [**c++**](cpp/structures/tree_2d.cpp) [**java**](java/structures/Tree2d.java)
- [x] Fenwick tree [**c++**](cpp/structures/fenwick_tree.cpp) [**java**](java/structures/FenwickTree.java) [**kotlin**](kotlin/FenwickTree.kt) [**rust**](rust/structures/fenwick_tree.rs)
- [x] Fenwick tree with extended operations [**c++**](cpp/structures/fenwick_tree_interval.cpp) [**java**](java/structures/FenwickTreeExtended.java)
- [x] Persistent tree [**java**](java/structures/PersistentTree.java) [**kotlin**](kotlin/PersistentTree.kt) [**rust**](rust/structures/persistent_tree.rs)
- [x] Centroid decomposition [**c++**](cpp/structures/centroid_decomposition.cpp) [**java**](java/structures/CentroidDecomposition.java)
- [x] Heavy/light decomposition [**c++**](cpp/structures/heavy_light_decomposition.cpp) [**java**](java/structures/HeavyLight.java)
- [x] Link/cut tree [**c++**](cpp/structures/link_cut_tree.cpp) [**java**](java/structures/LinkCutTree.java)
- [x] Link/cut tree for connectivity query [**java**](java/structures/LinkCutTreeConnectivity.java)
- [x] Link/cut tree for LCA query [**java**](java/structures/LinkCutTreeLca.java)
- [x] Binary heap [**java**](java/structures/BinaryHeap.java)
- [x] Binary heap with change priority [**c++**](cpp/structures/binary_heap.cpp) [**java**](java/structures/BinaryHeapExtended.java)
- [x] Disjoint sets [**c++**](cpp/structures/disjoint_sets.cpp) [**java**](java/structures/DisjointSets.java) [**rust**](rust/structures/disjoint_sets.rs)
- [x] Treap [**c++**](cpp/structures/treap.h) [**java**](java/structures/Treap.java) [**kotlin**](kotlin/Treap.kt) [**rust**](rust/structures/treap.rs)
- [x] Treap with indexed key [**c++**](cpp/structures/treap_indexed.cpp) [**java**](java/structures/TreapIndexed.java)
- [x] k-d tree for point query [**c++**](cpp/structures/kd_tree.cpp) [**java**](java/structures/KdTreePointQuery.java)
- [x] k-d tree for rectangular query [**java**](java/structures/KdTreeRectQuery.java)
- [x] R-tree [**java**](java/structures/RTree.java)
- [x] Metric tree [**java**](java/structures/MetricTree.java)
- [x] Quadtree [**java**](java/structures/QuadTree.java)
- [x] Mergeable heap [**java**](java/structures/MergeableHeap.java) [**rust**](rust/structures/mergeable_heap.rs)
- [x] Queue with minimum [**c++**](cpp/structures/queue_min.cpp) [**java**](java/structures/QueueMin.java)
- [x] Sparse table [**c++**](cpp/structures/sparse-table.cpp) [**java**](java/structures/RmqSparseTable.java) [**java**](java/graphs/lca/LcaSparseTable.java)
- [x] Sparse segment tree [**c++**](cpp/structures/sparse-segment-tree.cpp)
- [x] Wavelet tree [**c++**](cpp/structures/wavelet_tree.cpp) [**java**](java/structures/WaveletTree.java)
- [x] Mo's algorithm [**java**](java/structures/MosAlgorithm.java)
- [x] Mo's algorithm with point updates [**c++**](cpp/structures/mos_with_updates.cpp)

#### Graph algorithms

- [x] Shortest paths [**c++**](cpp/graphs/shortestpaths) [**java**](java/graphs/shortestpaths)
- [x] Maximum flow [**c++**](cpp/graphs/flows) [**java**](java/graphs/flows)
- [x] Maximum matching [**c++**](cpp/graphs/matchings) [**java**](java/graphs/matchings)
- [x] Spanning tree [**c++**](cpp/graphs/spanningtree) [**java**](java/graphs/spanningtree)
- [x] Connectivity [**c++**](cpp/graphs/dfs) [**java**](java/graphs/dfs)
- [x] Biconnectivity [**java**](java/graphs/dfs/Biconnectivity.java)
- [x] LCA Schieber-Vishkin algorithm [**c++**](cpp/graphs/lca/lca_rmq_schieber_vishkin.cpp) [**java**](java/graphs/lca/LcaSchieberVishkin.java)
- [x] LCA [**java**](java/graphs/lca)
- [ ] Planarity testing ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/28))
- [ ] Dynamic graph connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/29))
- [ ] Chu–Liu/Edmonds' algorithm ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/30))
- [ ] Minimum augmentation to strong connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/32))
- [ ] Minimum augmentation to biconnectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/33))

#### String algorithms

- [x] Knuth-Morris-Pratt algorithm [**c++**](cpp/strings/kmp.cpp) [**java**](java/strings/Kmp.java)
- [x] Aho-Corasick algorithm [**c++**](cpp/strings/aho-corasick.cpp) [**java**](java/strings/AhoCorasick.java)
- [x] Suffix array and lcp array. Radix sort algorithm in O(n\*log(n)) [**c++**](cpp/strings/suffix-array.cpp) [**java**](java/strings/SuffixArray.java)
- [x] Suffix array. Algorithm DC3 in O(n) [**c++**](cpp/strings/suffix-array-dc3.cpp) [**java**](java/strings/SuffixArrayDC3.java)
- [x] Suffix array. Algorithm SA-IS in O(n) [**c++**](cpp/strings/suffix-array-sa-is.cpp)
- [x] Suffix automaton [**c++**](cpp/strings/suffix-automaton.cpp) [**java**](java/strings/SuffixAutomaton.java)
- [x] Suffix tree Ukkonen's algorithm [**c++**](cpp/strings/suffix_tree_ukkonen.cpp) [**java**](java/strings/SuffixTree.java)
- [x] Suffix tree Breslauer-Italiano algorithm [**c++**](cpp/strings/suffix_tree_breslauer_italiano.cpp)
- [x] Trie [**java**](java/strings/Trie.java)
- [x] Z-function [**c++**](cpp/strings/z-function.cpp) [**java**](java/strings/ZFunction.java)
- [x] Hashing [**c++**](cpp/strings/hashing.cpp) [**java**](java/strings/Hashing.java)
- [x] Parsing [**java**](java/parsing) [**c++**](cpp/parsing)
- [x] Palindrome tree [**java**](java/strings/PalindromeTree.java)
- [ ] Sorting strings in linear time ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/31))

#### Sorting algorithms

- [x] Sorting algorithms [**c++**](cpp/sort/sort.cpp) [**java**](java/sort/Sort.java)
- [x] N-th element [**java**](java/sort/NthElement.java)

#### Geometry algorithms

- [x] Segments intersection [**c++**](cpp/geometry/segments_intersection.cpp) [**java**](java/geometry/SegmentsIntersection.java)
- [x] Line operations [**java**](java/geometry/LineGeometry.java)
- [x] Circle operations [**java**](java/geometry/CircleOperations.java)
- [x] Convex hull [**c++**](cpp/geometry/convex_hull.cpp) [**java**](java/geometry/ConvexHull.java)
- [x] Point in polygon query [**c++**](cpp/geometry/point_in_polygon.cpp) [**java**](java/geometry/PointInPolygon.java)
- [x] Closest pair of points [**java**](java/geometry/Closest2Points.java)
- [x] Furthest pair of points [**c++**](cpp/geometry/diameter.cpp)
- [ ] Implement quaternion ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/35))

#### Optimization

- [x] Simplex algorithm [**java**](java/optimization/Simplex.java)

#### Numerical algorithms

- [x] Fast Fourier transform (FFT) [**c++**](cpp/numeric/fft.h) [**java**](java/numeric/FFT.java)
- [x] Long arithmetics [**c++**](cpp/numeric/bigint.cpp)
- [x] Fast subset convolution [**java**](java/numeric/SubsetConvolution.java)
- [x] Fast Walsh-Hadamar transform [**java**](java/numeric/WalshHadamarTransform.java)
- [x] Karatsuba multiplication [**java**](java/numeric/KaratsubaMultiply.java)
- [x] Newton interpolation [**java**](java/numeric/NewtonInterpolation.java)
- [x] Laguerre's root-finding algorithm [**c++**](cpp/numeric/polynom-roots.cpp)

#### Number theory

- [x] Primes and divisors [**java**](java/numbertheory/PrimesAndDivisors.java) [**c++**](cpp/numbertheory/primes_and_divisors.cpp)
- [x] Factorization [**java**](java/numbertheory/Factorization.java) [**c++**](cpp/numbertheory/factorization.cpp)
- [x] Euclidean algorithm [**java**](java/numbertheory/Euclid.java) [**c++**](cpp/numbertheory/euclid.cpp)
- [x] Primitive root [**c++**](cpp/numbertheory/primitive_root.cpp)
- [x] Discrete logarithm [**c++**](cpp/numbertheory/discrete_log.cpp)
- [x] Discrete root [**c++**](cpp/numbertheory/discrete_root.cpp)
- [x] Multiplicative function [**java**](java/numbertheory/MultiplicativeFunction.java)
- [x] Rational numbers [**java**](java/numbertheory/Rational.java)
- [x] Polynom class [**c++**](cpp/numbertheory/polynom.h)
- [x] Linear recurrence and Berlekamp-Massey algorithm [**c++**](cpp/numbertheory/linear_recurrence.cpp)
- [x] Modular operations [**c++**](cpp/numbertheory/modint.h)

#### Combinatorics

- [x] Permutations [**java**](java/combinatorics/Permutations.java)
- [x] Combinations [**java**](java/combinatorics/Combinations.java)
- [x] Arrangements [**java**](java/combinatorics/Arrangements.java)
- [x] Partitions [**java**](java/combinatorics/Partitions.java)
- [x] Set Partitions [**java**](java/combinatorics/SetPartitions.java)
- [x] Bracket sequences [**java**](java/combinatorics/BracketSequences.java)
- [x] Binomial coefficients [**java**](java/combinatorics/BinomialCoefficients.java)
- [x] Prufer code [**java**](java/combinatorics/PruferCode.java)

#### Linear algebra

- [x] Gaussian elimination [**c++**](cpp/linearalgebra/gauss.cpp) [**java**](java/linearalgebra/Gauss.java) [**kotlin**](kotlin/Gauss.kt)
- [x] Determinant calculation [**java**](java/linearalgebra/Determinant.java)
- [x] Matrix operations [**c++**](cpp/linearalgebra/matrix.h) [**java**](java/linearalgebra/Matrix.java)
