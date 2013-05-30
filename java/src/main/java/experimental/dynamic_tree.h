/* -*- C++ -*-
 *
 * This file is a part of LEMON, a generic C++ optimization library
 *
 * Copyright (C) 2003-2008
 * Egervary Jeno Kombinatorikus Optimalizalasi Kutatocsoport
 * (Egervary Research Group on Combinatorial Optimization, EGRES).
 *
 * Permission to use, modify and distribute this software is granted
 * provided that this copyright notice appears in all copies. For
 * precise terms see the accompanying LICENSE file.
 *
 * This software is provided "AS IS" with no warranty of any kind,
 * express or implied, and with no claim as to its suitability for any
 * purpose.
 *
 */

#ifndef LEMON_DYNAMIC_TREE_H
#define LEMON_DYNAMIC_TREE_H

/// \ingroup auxdata
/// \file
/// \brief The dynamic tree data structure of Sleator and Tarjan.
///

#include <vector>
#include <limits>
#include <lemon/tolerance.h>

namespace lemon {

  /// \ingroup auxdata
  ///
  /// \brief The dynamic tree data structure of Sleator and Tarjan.
  ///
  /// This class provides an implementation of the dynamic tree data
  /// structure for maintaining a set of node-disjoint rooted
  /// trees. Each item has an associated value, and the item with
  /// minimum value can be find in \f$O(\log(n)\f$ on the path from a
  /// node to the its root, and the items on such path can be
  /// increased or decreased globally with a certain value in the same
  /// running time. We regard a tree edge as directed toward the root,
  /// that is from child to parent. Its structure can be modified by
  /// two basic operations: \e link(v,w) adds an edge between a root v
  /// and a node w in a different component; \e cut(v) removes the
  /// edge between v and its parent.
  /// 
  /// \param _Value The value type of the items.  
  /// \param _ItemIntMap Converts item type of node to integer.
  /// \param _Tolerance The tolerance class to handle computation
  /// problems.
  /// \param _enableSize If true then the data structre manatain the
  /// size of each tree. The feature is used in \ref GoldbergTarjan
  /// algorithm. The default value is true.
  ///
  /// \author Hamori Tamas
#ifdef DOXYGEN
  template <typename _Value, typename _ItemIntMap, 
	    typename _Tolerance, bool _enableSize>
#else
  template <typename _Value, typename _ItemIntMap, 
	    typename _Tolerance = lemon::Tolerance<_Value>,
	    bool _enableSize = true>
#endif
  class DynamicTree {
  public:
    /// \brief The integer map on the items.
    typedef _ItemIntMap ItemIntMap;
    /// \brief The item type of nodes.
    typedef typename ItemIntMap::Key Item;
    /// \brief The value type of the algorithms.
    typedef _Value Value;
    /// \brief The tolerance used by the algorithm.
    typedef _Tolerance Tolerance;

  private:
  
    class ItemData;

    std::vector<ItemData> _data;
    ItemIntMap &_iim;
    Value _max_value;
    Tolerance _tolerance;

  public:
    /// \brief The constructor of the class.
    ///
    /// \param iim The integer map on the items. 
    /// \param tolerance Tolerance class.
    DynamicTree(ItemIntMap &iim, const Tolerance& tolerance = Tolerance())
      : _iim(iim), _max_value(std::numeric_limits<Value>::max()), 
	_tolerance(tolerance) {}
  
    ~DynamicTree() {}

    /// \brief Clears the data structure
    ///
    /// Clears the data structure
    void clear() {
      _data.clear();
    }

    /// \brief Sets the tolerance used by algorithm.
    ///
    /// Sets the tolerance used by algorithm.
    void tolerance(const Tolerance& tolerance) const {
      _tolerance = tolerance;
      return *this;
    } 
  
    /// \brief Returns the tolerance used by algorithm.
    ///
    /// Returns the tolerance used by algorithm.
    const Tolerance& tolerance() const {
      return tolerance;
    } 
  
    /// \brief Create a new tree containing a single node with cost zero.
    void makeTree(const Item &item) {
      _data[makePath(item)].successor = -1;
    }
    
    /// \brief Return the root of the tree containing node with itemtype
    /// \e item.
    Item findRoot(const Item &item) {
      return _data[findTail(expose(_iim[item]))].id;
    }
    
    /// \brief Return the the value of nodes in the tree containing
    /// node with itemtype \e item.
    int findSize(const Item &item) {
      return _data[expose(_iim[item])].size;
    }
    
    /// \brief Return the minimum cost containing node.
    /// 
    /// Return into \e d the minimum cost on the tree path from \e item
    /// to findRoot(item).  Return the last item (closest to its root)
    /// on this path of the minimum cost.
    Item findCost(const Item &item, Value& d){
      return _data[findPathCost(expose(_iim[item]),d)].id;
    }
    
    /// \brief Add \e x value to the cost of every node on the path from
    /// \e item to findRoot(item).
    void addCost(const Item &item, Value x) {
      addPathCost(expose(_iim[item]), x);
    }
    
    /// \brief Combine the trees containing nodes \e item1 and \e item2
    /// by adding an edge from \e item1 \e item2.
    /// 
    /// This operation assumes that \e item1 is root and \e item2 is in
    /// a different tree.
    void link(const Item &item1, const Item &item2){
      int v = _iim[item1];
      int w = _iim[item2];
      int p = expose(w);
      join(-1, expose(v), p);
      _data[v].successor = -1;
      _data[v].size += _data[p].size;

    }    
    
    /// \brief Divide the tree containing node \e item into two trees by
    /// deleting the edge out of \e item.
    /// 
    /// This operation assumes that \e item is not a tree root.
    void cut(const Item &item) {
      int v = _iim[item];
      int p, q;
      expose(v);
      split(p, v, q);
      if (p != -1) {
	_data[p].successor = v;
      }
      _data[v].size -= _data[q].size;
      if (q != -1) {
	_data[q].successor = _data[v].successor;
      }
      _data[v].successor = -1;
    }

    ///\brief 
    Item parent(const Item &item){
      return _data[_iim[item].p].id;
    }

    ///\brief Return the upper bound of the costs.
    Value maxValue() const {
      return _max_value;
    }
    
  private:

    int makePath(const Item &item) {
      _iim.set(item, _data.size());
      ItemData v(item);
      _data.push_back(v);
      return _iim[item];
    }

    int findPath(int v) {
      splay(v);
      return v;
    }
    
    int findPathCost(int p, Value &d) {
      while ((_data[p].right != -1 && 
	      !_tolerance.less(0, _data[_data[p].right].dmin)) || 
	     (_data[p].left != -1 && _tolerance.less(0, _data[p].dcost))) {
	if (_data[p].right != -1 && 
	    !_tolerance.less(0, _data[_data[p].right].dmin)) {
	  p = _data[p].right;
	} else if (_data[p].left != -1 && 
		   !_tolerance.less(0, _data[_data[p].left].dmin)) {
	  p = _data[p].left;
	}
      }
      splay(p);
      d = _data[p].dmin;
      return p; 
    }

    int findTail(int p){
      while (_data[p].right != -1) {
	p = _data[p].right;
      }
      splay(p);
      return p;
    }
    
    void addPathCost(int p, Value x) {
      if (!_tolerance.less(x, _max_value)) {
	_data[p].dmin = x;
	_data[p].dcost = x;
      } else if (!_tolerance.less(-x, _max_value)) {
	_data[p].dmin = 0;
	_data[p].dcost = 0;
      } else {
	_data[p].dmin += x;
      }
    }

    void join(int p, int v, int q) {
      Value min = _max_value;
      Value pmin = _max_value;
      Value vmin = _data[v].dmin;
      Value qmin = _max_value;
      if (p != -1){
	pmin = _data[p].dmin;
      }
      if (q != -1){
	qmin = _data[q].dmin;
      }
        
      if (_tolerance.less(vmin, qmin)) {
	if (_tolerance.less(vmin,pmin)) {
	  min = vmin;
	} else {
	  min = pmin;
	}
      } else if (_tolerance.less(qmin,pmin)) {
	min = qmin;
      } else {
	min = pmin;
      }

      if (p != -1){
	_data[p].parent = v;
	_data[p].dmin -= min;
      }
      if (q!=-1){
	_data[q].parent = v;
	if (_tolerance.less(_data[q].dmin,_max_value)) {
	  _data[q].dmin -= min;
	}
      }
      _data[v].left = p;
      _data[v].right = q;
      if (_tolerance.less(min,_max_value)) {
	_data[v].dcost = _data[v].dmin - min;
      }
      _data[v].dmin = min;
    }

    void split(int &p, int v, int &q){
      splay(v);
      p = -1;
      if (_data[v].left != -1){
	p = _data[v].left;
	_data[p].dmin += _data[v].dmin;
	_data[p].parent = -1;
	_data[v].left = -1;
      }
      q = -1;
      if (_data[v].right != -1) {
	q=_data[v].right;
	if (_tolerance.less(_data[q].dmin, _max_value)) {
	  _data[q].dmin += _data[v].dmin;
	}
	_data[q].parent = -1;
	_data[v].right = -1;
      } 
      if (_tolerance.less(_data[v].dcost, _max_value)) {
	_data[v].dmin += _data[v].dcost;
	_data[v].dcost = 0;
      } else {
	_data[v].dmin = _data[v].dcost;
      }
    }
 
    int expose(int v) {
      int p, q, r, w;
      p = -1;
      while (v != -1) {
	w = _data[findPath(v)].successor;
	split(q, v, r);
	if (q != -1) {
	  _data[q].successor = v;
	}
	join(p, v, r);
	p = v;
	v = w;
      }
      _data[p].successor = -1;
      return p;
    }

    void splay(int v) {
      while (_data[v].parent != -1) {
	if (v == _data[_data[v].parent].left) {
	  if (_data[_data[v].parent].parent == -1) {
	    zig(v);
	  } else {
	    if (_data[v].parent == _data[_data[_data[v].parent].parent].left) {
	      zig(_data[v].parent);
	      zig(v);
	    } else {
	      zig(v);
	      zag(v);
	    }
	  }
	} else {
	  if (_data[_data[v].parent].parent == -1) {
	    zag(v);
	  } else {
	    if (_data[v].parent == _data[_data[_data[v].parent].parent].left) {
	      zag(v);
	      zig(v);
	    } else {
	      zag(_data[v].parent);
	      zag(v);
	    }
	  }
	}
      }
    }


    void zig(int v) {
      Value min = _data[_data[v].parent].dmin;
      int a = _data[v].parent;
        
      Value aa = _data[a].dcost;
      if (_tolerance.less(aa, _max_value)) { 
	aa += min;
      }


      int b = v;
      Value ab = min + _data[b].dmin;
      Value bb = _data[b].dcost;
      if (_tolerance.less(bb, _max_value)) { 
	bb += ab;
      }

      int c = -1;
      Value cc = _max_value;
      if (_data[a].right != -1) {
	c = _data[a].right;
	cc = _data[c].dmin;
	if (_tolerance.less(cc, _max_value)) {
	  cc += min;
	}
      }

      int d = -1;
      Value dd = _max_value;
      if (_data[v].left != -1){
	d = _data[v].left;
	dd = ab + _data[d].dmin;
      }

      int e = -1;
      Value ee = _max_value;
      if (_data[v].right != -1) {
	e = _data[v].right;
	ee = ab + _data[e].dmin;
      }

      Value min2;
      if (_tolerance.less(0, _data[b].dmin) || 
	  (e != -1 && !_tolerance.less(0, _data[e].dmin))) {
	min2 = min;
      } else {
	if (_tolerance.less(aa, cc)) {
	  if (_tolerance.less(aa, ee)) {
	    min2 = aa;
	  } else {
	    min2 = ee;
	  }
	} else if (_tolerance.less(cc, ee)) {
	  min2 = cc;
	} else {
	  min2 = ee;
	}
      }
        
      _data[a].dcost = aa;
      if (_tolerance.less(aa, _max_value)) { 
	_data[a].dcost -= min2;
      }
      _data[a].dmin = min2;
      if (_tolerance.less(min2,_max_value)) { 
	_data[a].dmin -= min; 
      }
      _data[a].size -= _data[b].size;
      _data[b].dcost = bb;
      if (_tolerance.less(bb, _max_value)) { 
	_data[b].dcost -= min;
      }
      _data[b].dmin = min;
      _data[b].size += _data[a].size;
      if (c != -1) {
	_data[c].dmin = cc;
	if (_tolerance.less(cc, _max_value)) {
	  _data[c].dmin -= min2;
	}
      }
      if (d != -1) {
	_data[d].dmin = dd - min;
	_data[a].size += _data[d].size;
	_data[b].size -= _data[d].size;
      }
      if (e != -1) {
	_data[e].dmin = ee - min2;
      }
        
      int w = _data[v].parent;
      _data[v].successor = _data[w].successor;
      _data[w].successor = -1;
      _data[v].parent = _data[w].parent;
      _data[w].parent = v;
      _data[w].left = _data[v].right;
      _data[v].right = w;
      if (_data[v].parent != -1){
	if (_data[_data[v].parent].right == w) {
	  _data[_data[v].parent].right = v;
	} else {
	  _data[_data[v].parent].left = v;
	}
      }
      if (_data[w].left != -1){
	_data[_data[w].left].parent = w;
      }
    }


    void zag(int v) {

      Value min = _data[_data[v].parent].dmin;

      int a = _data[v].parent;
      Value aa = _data[a].dcost;
      if (_tolerance.less(aa, _max_value)) { 
	aa += min;
      }
        
      int b = v;
      Value ab = min + _data[b].dmin;
      Value bb = _data[b].dcost;
      if (_tolerance.less(bb, _max_value)) {
	bb += ab;
      }

      int c = -1;
      Value cc = _max_value;
      if (_data[a].left != -1){
	c = _data[a].left;
	cc = min + _data[c].dmin;
      }

      int d = -1;
      Value dd = _max_value;
      if (_data[v].right!=-1) {
	d = _data[v].right;
	dd = _data[d].dmin;
	if (_tolerance.less(dd, _max_value)) {
	  dd += ab;
	}
      }

      int e = -1;
      Value ee = _max_value;
      if (_data[v].left != -1){
	e = _data[v].left;
	ee = ab + _data[e].dmin;
      }

      Value min2;
      if (_tolerance.less(0, _data[b].dmin) || 
	  (e != -1 && !_tolerance.less(0, _data[e].dmin))) {
	min2 = min;
      } else {
	if (_tolerance.less(aa, cc)) {
	  if (_tolerance.less(aa, ee)) {
	    min2 = aa;
	  } else {
	    min2 = ee;
	  }
	} else if (_tolerance.less(cc, ee)) {
	  min2 = cc;
	} else {
	  min2 = ee;
	}
      }
      _data[a].dcost = aa;
      if (_tolerance.less(aa, _max_value)) { 
	_data[a].dcost -= min2;
      }
      _data[a].dmin = min2;
      if (_tolerance.less(min2, _max_value)) {
	_data[a].dmin -= min;
      }
      _data[a].size -= _data[b].size;
      _data[b].dcost = bb;
      if (_tolerance.less(bb, _max_value)) { 
	_data[b].dcost -= min;
      }
      _data[b].dmin = min;
      _data[b].size += _data[a].size;
      if (c != -1) {
	_data[c].dmin = cc - min2;
      }
      if (d != -1) {
	_data[d].dmin = dd;
	_data[a].size += _data[d].size;
	_data[b].size -= _data[d].size;
	if (_tolerance.less(dd, _max_value)) {
	  _data[d].dmin -= min;
	}
      }
      if (e != -1) {
	_data[e].dmin = ee - min2;
      }
        
      int w = _data[v].parent;
      _data[v].successor = _data[w].successor;
      _data[w].successor = -1;
      _data[v].parent = _data[w].parent;
      _data[w].parent = v;
      _data[w].right = _data[v].left;
      _data[v].left = w;
      if (_data[v].parent != -1){
	if (_data[_data[v].parent].left == w) {
	  _data[_data[v].parent].left = v;
	} else {
	  _data[_data[v].parent].right = v;
	}
      }
      if (_data[w].right != -1){
	_data[_data[w].right].parent = w;
      }
    }

  private:

    class ItemData {
    public:
      Item id;
      int size;
      int successor;
      int parent;
      int left;
      int right;
      Value dmin;
      Value dcost;
        
    public:
      ItemData(const Item &item)
	: id(item), size(1), successor(), parent(-1), 
	  left(-1), right(-1), dmin(0), dcost(0) {}
    };
     
  };

  template <typename _Value, typename _ItemIntMap, typename _Tolerance>
  class DynamicTree<_Value, _ItemIntMap, _Tolerance, false> {
  public:
    typedef _ItemIntMap ItemIntMap;
    typedef typename ItemIntMap::Key Item;
    typedef _Value Value;
    typedef _Tolerance Tolerance;

  private:
  
    class ItemData;

    std::vector<ItemData> _data;
    ItemIntMap &_iim;
    Value _max_value;
    Tolerance _tolerance;

  public:
    DynamicTree(ItemIntMap &iim, const Tolerance& tolerance = Tolerance())
      : _iim(iim), _max_value(std::numeric_limits<Value>::max()), 
	_tolerance(tolerance) {}
  
    ~DynamicTree() {}

    void clear() {
      _data.clear();
    }

    void tolerance(const Tolerance& tolerance) const {
      _tolerance = tolerance;
      return *this;
    } 
  
    const Tolerance& tolerance() const {
      return tolerance;
    } 
  
    void makeTree(const Item &item) {
      _data[makePath(item)].successor = -1;
    }
    
    Item findRoot(const Item &item) {
      return _data[findTail(expose(_iim[item]))].id;
    }
    
    Item findCost(const Item &item, Value& d){
      return _data[findPathCost(expose(_iim[item]),d)].id;
    }
    
    void addCost(const Item &item, Value x){
      addPathCost(expose(_iim[item]), x);
    }
    
    void link(const Item &item1, const Item &item2){
      int v = _iim[item1];
      int w = _iim[item2];
      int p = expose(w);
      join(-1, expose(v), p);
      _data[v].successor = -1;
    }    
    
    void cut(const Item &item) {
      int v = _iim[item];
      int p, q;
      expose(v);
      split(p, v, q);
      if (p != -1) {
	_data[p].successor = v;
      }
      if (q != -1) {
	_data[q].successor = _data[v].successor;
      }
      _data[v].successor = -1;
    }

    Item parent(const Item &item){
      return _data[_iim[item].p].id;
    }

    Value maxValue() const {
      return _max_value;
    }
    
  private:

    int makePath(const Item &item) {
      _iim.set(item, _data.size());
      ItemData v(item);
      _data.push_back(v);
      return _iim[item];
    }

    int findPath(int v) {
      splay(v);
      return v;
    }
    
    int findPathCost(int p, Value &d) {
      while ((_data[p].right != -1 && 
	      !_tolerance.less(0, _data[_data[p].right].dmin)) || 
	     (_data[p].left != -1 && _tolerance.less(0, _data[p].dcost))) {
	if (_data[p].right != -1 && 
	    !_tolerance.less(0, _data[_data[p].right].dmin)) {
	  p = _data[p].right;
	} else if (_data[p].left != -1 && 
		   !_tolerance.less(0, _data[_data[p].left].dmin)){
	  p = _data[p].left;
	}
      }
      splay(p);
      d = _data[p].dmin;
      return p; 
    }

    int findTail(int p) {
      while (_data[p].right != -1) {
	p = _data[p].right;
      }
      splay(p);
      return p;
    }
    
    void addPathCost(int p, Value x) {
      if (!_tolerance.less(x, _max_value)) {
	_data[p].dmin = x;_data[p].dcost = x;
      } else if (!_tolerance.less(-x, _max_value)) {
	_data[p].dmin = 0;
	_data[p].dcost = 0;
      } else {
	_data[p].dmin += x;
      }
    }

    void join(int p, int v, int q) {
      Value min = _max_value;
      Value pmin = _max_value;
      Value vmin = _data[v].dmin;
      Value qmin = _max_value;
      if (p != -1){
	pmin = _data[p].dmin;
      }
      if (q != -1){
	qmin = _data[q].dmin;
      }
        
      if (_tolerance.less(vmin, qmin)) {
	if (_tolerance.less(vmin,pmin)) {
	  min = vmin;
	} else {
	  min = pmin;
	}
      } else if (_tolerance.less(qmin,pmin)) {
	min = qmin;
      } else {
	min = pmin;
      }

      if (p != -1){
	_data[p].parent = v;
	_data[p].dmin -= min;
      }
      if (q != -1){
	_data[q].parent = v;
	if (_tolerance.less(_data[q].dmin,_max_value)) {
	  _data[q].dmin -= min;
	}
      }
      _data[v].left = p;
      _data[v].right = q;
      if (_tolerance.less(min, _max_value)) {
	_data[v].dcost = _data[v].dmin - min;
      }
      _data[v].dmin = min;
    }

    void split(int &p, int v, int &q){
      splay(v);
      p = -1;
      if (_data[v].left != -1){
	p = _data[v].left;
	_data[p].dmin += _data[v].dmin;
	_data[p].parent = -1;
	_data[v].left = -1;
      }
      q = -1;
      if (_data[v].right != -1) {
	q=_data[v].right;
	if (_tolerance.less(_data[q].dmin, _max_value)) {
	  _data[q].dmin += _data[v].dmin;
	}
	_data[q].parent = -1;
	_data[v].right = -1;
      } 
      if (_tolerance.less(_data[v].dcost, _max_value)) {
	_data[v].dmin += _data[v].dcost;
	_data[v].dcost = 0;
      } else {
	_data[v].dmin = _data[v].dcost;
      }
    }
 
    int expose(int v) {
      int p, q, r, w;
      p = -1;
      while (v != -1) {
	w = _data[findPath(v)].successor;
	split(q, v, r);
	if (q != -1) {
	  _data[q].successor = v;
	}
	join(p, v, r);
	p = v;
	v = w;
      }
      _data[p].successor = -1;
      return p;
    }

    void splay(int v) {
      while (_data[v].parent != -1) {
	if (v == _data[_data[v].parent].left) {
	  if (_data[_data[v].parent].parent == -1) {
	    zig(v);
	  } else {
	    if (_data[v].parent == _data[_data[_data[v].parent].parent].left) {
	      zig(_data[v].parent);
	      zig(v);
	    } else {
	      zig(v);
	      zag(v);
	    }
	  }
	} else {
	  if (_data[_data[v].parent].parent == -1) {
	    zag(v);
	  } else {
	    if (_data[v].parent == _data[_data[_data[v].parent].parent].left) {
	      zag(v);
	      zig(v);
	    } else {
	      zag(_data[v].parent);
	      zag(v);
	    }
	  }
	}
      }
    }


    void zig(int v) {
      Value min = _data[_data[v].parent].dmin;
      int a = _data[v].parent;
        
      Value aa = _data[a].dcost;
      if (_tolerance.less(aa, _max_value)) { 
	aa+= min;
      }


      int b = v;
      Value ab = min + _data[b].dmin;
      Value bb = _data[b].dcost;
      if (_tolerance.less(bb, _max_value)) { 
	bb+= ab;
      }

      int c = -1;
      Value cc = _max_value;
      if (_data[a].right != -1) {
	c = _data[a].right;
	cc = _data[c].dmin;
	if (_tolerance.less(cc, _max_value)) {
	  cc+=min;
	}
      }

      int d = -1;
      Value dd = _max_value;
      if (_data[v].left != -1){
	d = _data[v].left;
	dd = ab + _data[d].dmin;
      }

      int e = -1;
      Value ee = _max_value;
      if (_data[v].right != -1) {
	e = _data[v].right;
	ee = ab + _data[e].dmin;
      }

      Value min2;
      if (_tolerance.less(0, _data[b].dmin) || 
	  (e != -1 && !_tolerance.less(0, _data[e].dmin))) {
	min2 = min;
      } else {
	if (_tolerance.less(aa, cc)) {
	  if (_tolerance.less(aa, ee)) {
	    min2 = aa;
	  } else {
	    min2 = ee;
	  }
	} else if (_tolerance.less(cc, ee)) {
	  min2 = cc;
	} else {
	  min2 = ee;
	}
      }
        
      _data[a].dcost = aa;
      if (_tolerance.less(aa, _max_value)) { 
	_data[a].dcost -= min2;
      }
      _data[a].dmin = min2;
      if (_tolerance.less(min2,_max_value)) { 
	_data[a].dmin -= min; 
      }
      _data[b].dcost = bb;
      if (_tolerance.less(bb, _max_value)) { 
	_data[b].dcost -= min;
      }
      _data[b].dmin = min;
      if (c != -1) {
	_data[c].dmin = cc;
	if (_tolerance.less(cc, _max_value)) {
	  _data[c].dmin -= min2;
	}
      }
      if (d != -1) {
	_data[d].dmin = dd - min;
      }
      if (e != -1) {
	_data[e].dmin = ee - min2;
      }
        
      int w = _data[v].parent;
      _data[v].successor = _data[w].successor;
      _data[w].successor = -1;
      _data[v].parent = _data[w].parent;
      _data[w].parent = v;
      _data[w].left = _data[v].right;
      _data[v].right = w;
      if (_data[v].parent != -1){
	if (_data[_data[v].parent].right == w) {
	  _data[_data[v].parent].right = v;
	} else {
	  _data[_data[v].parent].left = v;
	}
      }
      if (_data[w].left != -1){
	_data[_data[w].left].parent = w;
      }
    }


    void zag(int v) {

      Value min = _data[_data[v].parent].dmin;

      int a = _data[v].parent;
      Value aa = _data[a].dcost;
      if (_tolerance.less(aa, _max_value)) { 
	aa += min;
      }
        
      int b = v;
      Value ab = min + _data[b].dmin;
      Value bb = _data[b].dcost;
      if (_tolerance.less(bb, _max_value)) {
	bb += ab;
      }

      int c = -1;
      Value cc = _max_value;
      if (_data[a].left != -1){
	c = _data[a].left;
	cc = min + _data[c].dmin;
      }

      int d = -1;
      Value dd = _max_value;
      if (_data[v].right!=-1) {
	d = _data[v].right;
	dd = _data[d].dmin;
	if (_tolerance.less(dd, _max_value)) {
	  dd += ab;
	}
      }

      int e = -1;
      Value ee = _max_value;
      if (_data[v].left != -1){
	e = _data[v].left;
	ee = ab + _data[e].dmin;
      }

      Value min2;
      if (_tolerance.less(0, _data[b].dmin) || 
	  (e != -1 && !_tolerance.less(0, _data[e].dmin))) {
	min2 = min;
      } else {
	if (_tolerance.less(aa, cc)) {
	  if (_tolerance.less(aa, ee)) {
	    min2 = aa;
	  } else {
	    min2 = ee;
	  }
	} else if (_tolerance.less(cc, ee)) {
	  min2 = cc;
	} else {
	  min2 = ee;
	}
      }
      _data[a].dcost = aa;
      if (_tolerance.less(aa, _max_value)) { 
	_data[a].dcost -= min2;
      }
      _data[a].dmin = min2;
      if (_tolerance.less(min2, _max_value)) {
	_data[a].dmin -= min;
      }
      _data[b].dcost = bb;
      if (_tolerance.less(bb, _max_value)) { 
	_data[b].dcost -= min;
      }
      _data[b].dmin = min;
      if (c != -1) {
	_data[c].dmin = cc - min2;
      }
      if (d != -1) {
	_data[d].dmin = dd;
	if (_tolerance.less(dd, _max_value)) {
	  _data[d].dmin -= min;
	}
      }
      if (e != -1) {
	_data[e].dmin = ee - min2;
      }
        
      int w = _data[v].parent;
      _data[v].successor = _data[w].successor;
      _data[w].successor = -1;
      _data[v].parent = _data[w].parent;
      _data[w].parent = v;
      _data[w].right = _data[v].left;
      _data[v].left = w;
      if (_data[v].parent != -1){
	if (_data[_data[v].parent].left == w) {
	  _data[_data[v].parent].left = v;
	} else {
	  _data[_data[v].parent].right = v;
	}
      }
      if (_data[w].right != -1){
	_data[_data[w].right].parent = w;
      }
    }

  private:

    class ItemData {
    public:
      Item id;
      int successor;
      int parent;
      int left;
      int right;
      Value dmin;
      Value dcost;
        
    public:
      ItemData(const Item &item)
	: id(item), successor(), parent(-1), 
	  left(-1), right(-1), dmin(0), dcost(0) {}
    };
     
  };

}

#endif
