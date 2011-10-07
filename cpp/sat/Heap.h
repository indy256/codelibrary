/******************************************************************************************[Heap.h]
MiniSat -- Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
**************************************************************************************************/

#ifndef Heap_h
#define Heap_h


//=================================================================================================


static inline int left  (int i) { return i+i; }
static inline int right (int i) { return i+i + 1; }
static inline int parent(int i) { return i >> 1; }

template<class C>
class Heap {
  public:
    C        comp;
    vec<int> heap;     // heap of ints
    vec<int> indices;  // int -> index in heap

    inline void percolateUp(int i)
    {
        int x = heap[i];
        while (parent(i) != 0 && comp(x,heap[parent(i)])){
            heap[i]          = heap[parent(i)];
            indices[heap[i]] = i;
            i                = parent(i);
        }
        heap   [i] = x;
        indices[x] = i;
    }

    inline void percolateDown(int i)
    {
        int x = heap[i];
        while (left(i) < heap.size()){
            int child = right(i) < heap.size() && comp(heap[right(i)],heap[left(i)]) ? right(i) : left(i);
            if (!comp(heap[child],x)) break;
            heap[i]          = heap[child];
            indices[heap[i]] = i;
            i                = child;
        }
        heap   [i] = x;
        indices[x] = i;
    }

    bool ok(int n) { return n >= 0 && n < (int)indices.size(); }

  public:
    Heap(C c) : comp(c) { heap.push(-1); }

    void setBounds (int size) { assert(size >= 0); indices.growTo(size,0); }
    bool inHeap    (int n)    { assert(ok(n)); return indices[n] != 0; }
    void increase  (int n)    { assert(ok(n)); assert(inHeap(n)); percolateUp(indices[n]); }
    bool empty     ()         { return heap.size() == 1; }

    void insert(int n) {
        assert(ok(n));
        indices[n] = heap.size();
        heap.push(n);
        percolateUp(indices[n]); }

    int  getmin() {
        int r            = heap[1];
        heap[1]          = heap.last();
        indices[heap[1]] = 1;
        indices[r]       = 0;
        heap.pop();
        if (heap.size() > 1)
            percolateDown(1);
        return r; }

    bool heapProperty() {
        return heapProperty(1); }

    bool heapProperty(int i) {
        return (size_t)i >= heap.size()
            || ((parent(i) == 0 || !comp(heap[i],heap[parent(i)])) && heapProperty(left(i)) && heapProperty(right(i))); }
};


//=================================================================================================
#endif
