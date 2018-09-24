[![GitHub stars](https://img.shields.io/github/stars/indy256/codelibrary.svg?style=flat&label=star)](https://github.com/indy256/codelibrary/)
[![Build Status](https://travis-ci.org/indy256/codelibrary.svg?branch=master)](https://travis-ci.org/indy256/codelibrary)
[![License](https://img.shields.io/badge/license-UNLICENSE-green.svg)](https://github.com/indy256/codelibrary/blob/master/UNLICENSE)

#### Collection of algorithms and data structures in Java and C++ (for different languages see [codelibrary-sandbox](https://github.com/indy256/codelibrary-sandbox))

#### Data structures
+ [x] [Segment tree](java/structures/SegmentTreeIntervalAddMax.java)
+ [x] [Segment tree for sum with lowerBound operation](java/structures/SegmentTreeSumLowerBound.java)
+ [x] [Segment tree without recursion](java/structures/SegmentTreeSimple.java)
+ [x] [Fenwick tree](java/structures/FenwickTree.java)
+ [x] [Fenwick tree with extended operations](java/structures/FenwickTreeExtended.java)
+ [x] [Persistent tree](java/structures/PersistentTree.java)
+ [x] [Centroid decomposition](java/structures/CentroidDecomposition.java)
+ [x] [Heavy/light decomposition](java/structures/HeavyLight.java)
+ [x] [Link/cut tree](java/structures/LinkCutTree.java)
+ [x] [Link/cut tree for connectivity query](java/structures/LinkCutTreeConnectivity.java)
+ [x] [Link/cut tree for LCA query](java/structures/LinkCutTreeLca.java)
+ [x] [Binary heap](java/structures/BinaryHeap.java)
+ [x] [Binary heap with change priority](java/structures/BinaryHeapExtended.java)
+ [x] [Disjoint sets](java/structures/DisjointSets.java)
+ [x] [Treap with implicit key](java/structures/TreapImplicitKey.java)
+ [x] [Treap as BST](java/structures/TreapBst.java)
+ [x] [k-d tree for point query](java/structures/KdTreePointQuery.java)
+ [x] [k-d tree for rectangular query](java/structures/KdTreeRectQuery.java)
+ [x] [R-tree](java/structures/RTree.java)
+ [x] [Metric tree](java/structures/MetricTree.java)
+ [x] [Quadtree](java/structures/QuadTree.java)
+ [x] [Mergeable heap](java/structures/MergeableHeap.java)
+ [x] [Queue with minimum](java/structures/QueueMin.java)
+ [x] [Sparse table for RMQ](java/structures/RmqSparseTable.java)

#### Graph algorithms
+ [x] [Shortest paths](java/graphs/shortestpaths)
+ [x] [Maximum flow](java/graphs/flows)
+ [x] [Maximum matching](java/graphs/matchings)
+ [ ] Maximum weighted matching in general graph ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/38))
+ [x] [Spanning tree](java/graphs/spanningtree)
+ [x] [Connectivity](java/graphs/dfs)
+ [x] [Biconnectivity](java/graphs/dfs)
+ [x] [LCA](java/graphs/lca)
+ [ ] Planarity testing ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/28))
+ [ ] Dynamic graph connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/29))
+ [ ] Chu–Liu/Edmonds' algorithm ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/30))
+ [ ] Minimum augmentation to strong connectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/32))
+ [ ] Minimum augmentation to biconnectivity ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/33))

#### String algorithms
+ [x] [Knuth-Morris-Pratt algorithm](java/strings/Kmp.java)
+ [x] [Aho-Corasick algorithm](java/strings/AhoCorasick.java)
+ [x] [Suffix array](java/strings/SuffixArray.java)
+ [x] [suffix automata](java/strings/SuffixAutomaton.java)
+ [x] [Suffix tree](java/strings/SuffixTree.java)
+ [x] [Trie](java/strings/Trie.java)
+ [x] [Z-function](java/strings/ZFunction.java)
+ [x] [Hashing](java/strings/Hashing.java)
+ [x] [Parsing](java/parsing)
+ [ ] Implement palindrome tree ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/34))
+ [ ] Sorting strings in linear time ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/31))

#### Sorting algorithms
+ [x] [Sorting algorithms](java/sort/Sort.java)
+ [x] [N-th element](java/sort/NthElement.java)

#### Geometry algorithms
+ [x] [Segments intesection](java/geometry/SegmentsIntersection.java)
+ [x] [Line operations](java/geometry/LineGeometry.java)
+ [x] [Circle operations](java/geometry/CircleOperations.java)
+ [x] [Convex hull](java/geometry/ConvexHull.java)
+ [x] [Point in polygon query](java/geometry/PointInPolygon.java)
+ [x] [Closest pair of points](java/geometry/Closest2Points.java)
+ [x] [Furthest pair of points](cpp/geometry/diameter.cpp)
+ [ ] Implement quaternion ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/35))

#### Optimization
+ [x] [Simplex algorithm](java/optimization/Simplex.java)

#### Numerical algorithms
+ [x] [Long arithmetics](cpp/numeric/bigint.cpp)
+ [x] [Fast Fourier transform](java/numeric/FFT.java)
+ [x] [Karatsuba multiplication](java/numeric/KaratsubaMultiply.java)
+ [x] [Newton interpolation](java/numeric/NewtonInterpolation.java)
+ [x] [Laguerre's root-finding algorithm](cpp/numeric/polynom-roots.cpp)

#### Number theory
+ [x] [Primes and divisors](java/numbertheory/PrimesAndDivisors.java)
+ [x] [Factorization](java/numbertheory/Factorization.java)
+ [x] [Euclidean algorithm](java/numbertheory/Euclid.java)
+ [x] [Multiplicative function](java/numbertheory/MultiplicativeFunction.java)
+ [x] [Rational numbers](java/numbertheory/Rational.java)
+ [ ] Implement polynom class ([contribute a link or implementation](https://github.com/indy256/codelibrary/issues/36))

#### Combinatorics
+ [x] [Permutations](java/combinatorics/Permutations.java)
+ [x] [Combinations](java/combinatorics/Combinations.java)
+ [x] [Arrangements](java/combinatorics/Arrangements.java)
+ [x] [Partitions](java/combinatorics/Partitions.java)
+ [x] [Binomial coefficients](java/combinatorics/BinomialCoefficients.java)

#### Linear algebra
+ [x] [Gaussian elimination](java/linearalgebra/Gauss.java)
+ [x] [Determinant calculation](java/linearalgebra/Determinant.java)
+ [x] [Matrix operations](java/linearalgebra/Matrix.java)
