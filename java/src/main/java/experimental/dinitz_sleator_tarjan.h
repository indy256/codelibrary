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

#ifndef LEMON_DINITZ_SLEATOR_TARJAN_H
#define LEMON_DINITZ_SLEATOR_TARJAN_H

/// \file 
/// \ingroup max_flow 
/// \brief Implementation the dynamic tree data structure of Sleator
/// and Tarjan.

#include <lemon/graph_utils.h>
#include <lemon/tolerance.h>
#include <lemon/dynamic_tree.h>

#include <vector>
#include <limits>
#include <fstream>


namespace lemon {

  /// \brief Default traits class of DinitzSleatorTarjan class.
  ///
  /// Default traits class of DinitzSleatorTarjan class.
  /// \param _Graph Graph type.
  /// \param _CapacityMap Type of capacity map.
  template <typename _Graph, typename _CapacityMap>
  struct DinitzSleatorTarjanDefaultTraits {

    /// \brief The graph type the algorithm runs on. 
    typedef _Graph Graph;

    /// \brief The type of the map that stores the edge capacities.
    ///
    /// The type of the map that stores the edge capacities.
    /// It must meet the \ref concepts::ReadMap "ReadMap" concept.
    typedef _CapacityMap CapacityMap;

    /// \brief The type of the length of the edges.
    typedef typename CapacityMap::Value Value;

    /// \brief The map type that stores the flow values.
    ///
    /// The map type that stores the flow values. 
    /// It must meet the \ref concepts::ReadWriteMap "ReadWriteMap" concept.
    typedef typename Graph::template EdgeMap<Value> FlowMap;

    /// \brief Instantiates a FlowMap.
    ///
    /// This function instantiates a \ref FlowMap. 
    /// \param graph The graph, to which we would like to define the flow map.
    static FlowMap* createFlowMap(const Graph& graph) {
      return new FlowMap(graph);
    }

    /// \brief The tolerance used by the algorithm
    ///
    /// The tolerance used by the algorithm to handle inexact computation.
    typedef Tolerance<Value> Tolerance;

  };

  /// \ingroup max_flow
  ///
  /// \brief Dinitz-Sleator-Tarjan algorithms class.
  ///
  /// This class provides an implementation of the \e
  /// Dinitz-Sleator-Tarjan \e algorithm producing a flow of maximum
  /// value in a directed graph. The DinitzSleatorTarjan algorithm is
  /// the fastest known max flow algorithms wich using blocking
  /// flow. It is an improvement of the Dinitz algorithm by using the
  /// \ref DynamicTree "dynamic tree" data structure of Sleator and
  /// Tarjan.
  ///
  /// This blocking flow algorithms builds a layered graph according
  /// to \ref Bfs "breadth-first search" distance from the target node
  /// in the reversed residual graph. The layered graph contains each
  /// residual edge which steps one level down. After that the
  /// algorithm constructs a blocking flow on the layered graph and
  /// augments the overall flow with it. The number of the levels in
  /// the layered graph is strictly increasing in each augmenting
  /// phase therefore the number of the augmentings is at most
  /// \f$n-1\f$.  The length of each phase is at most
  /// \f$O(m\log(n))\f$, that the overall time complexity is
  /// \f$O(nm\log(n))\f$.
  ///
  /// \param _Graph The directed graph type the algorithm runs on.
  /// \param _CapacityMap The capacity map type.
  /// \param _Traits Traits class to set various data types used by
  /// the algorithm.  The default traits class is \ref
  /// DinitzSleatorTarjanDefaultTraits.  See \ref
  /// DinitzSleatorTarjanDefaultTraits for the documentation of a
  /// Dinitz-Sleator-Tarjan traits class.
  ///
  /// \author Tamas Hamori and Balazs Dezso
#ifdef DOXYGEN
  template <typename _Graph, typename _CapacityMap, typename _Traits>
#else
  template <typename _Graph, 
	    typename _CapacityMap = typename _Graph::template EdgeMap<int>,
	    typename _Traits = 
	    DinitzSleatorTarjanDefaultTraits<_Graph, _CapacityMap> >
#endif
  class DinitzSleatorTarjan {
  public:

    typedef _Traits Traits;
    typedef typename Traits::Graph Graph;
    typedef typename Traits::CapacityMap CapacityMap;
    typedef typename Traits::Value Value; 

    typedef typename Traits::FlowMap FlowMap;
    typedef typename Traits::Tolerance Tolerance;


  private:

    GRAPH_TYPEDEFS(typename Graph);


    typedef typename Graph::template NodeMap<int> LevelMap;
    typedef typename Graph::template NodeMap<int> IntNodeMap;
    typedef typename Graph::template NodeMap<Edge> EdgeNodeMap;
    typedef DynamicTree<Value, IntNodeMap, Tolerance, false> DynTree;

  private:
    
    const Graph& _graph;
    const CapacityMap* _capacity;

    Node _source, _target;

    FlowMap* _flow;
    bool _local_flow;

    IntNodeMap* _level;
    EdgeNodeMap* _dt_edges;
    
    IntNodeMap* _dt_index;
    DynTree* _dt;

    std::vector<Node> _queue;

    Tolerance _tolerance;
    
    Value _flow_value;
    Value _max_value;


  public:

    typedef DinitzSleatorTarjan Create;

    ///\name Named template parameters

    ///@{

    template <typename _FlowMap>
    struct DefFlowMapTraits : public Traits {
      typedef _FlowMap FlowMap;
      static FlowMap *createFlowMap(const Graph&) {
	throw UninitializedParameter();
      }
    };

    /// \brief \ref named-templ-param "Named parameter" for setting
    /// FlowMap type
    ///
    /// \ref named-templ-param "Named parameter" for setting FlowMap
    /// type
    template <typename _FlowMap>
    struct DefFlowMap 
      : public DinitzSleatorTarjan<Graph, CapacityMap, 
			      DefFlowMapTraits<_FlowMap> > {
      typedef DinitzSleatorTarjan<Graph, CapacityMap, 
			     DefFlowMapTraits<_FlowMap> > Create;
    };

    template <typename _Elevator>
    struct DefElevatorTraits : public Traits {
      typedef _Elevator Elevator;
      static Elevator *createElevator(const Graph&, int) {
	throw UninitializedParameter();
      }
    };

    /// @}

    /// \brief \ref Exception for the case when the source equals the target.
    ///
    /// \ref Exception for the case when the source equals the target.
    ///
    class InvalidArgument : public lemon::LogicError {
    public:
      virtual const char* what() const throw() {
	return "lemon::DinitzSleatorTarjan::InvalidArgument";
      }
    };

  protected:
    
    DinitzSleatorTarjan() {}

  public:

    /// \brief The constructor of the class.
    ///
    /// The constructor of the class. 
    /// \param graph The directed graph the algorithm runs on. 
    /// \param capacity The capacity of the edges. 
    /// \param source The source node.
    /// \param target The target node.
    DinitzSleatorTarjan(const Graph& graph, const CapacityMap& capacity,
			Node source, Node target)
      : _graph(graph), _capacity(&capacity),
	_source(source), _target(target),
	_flow(0), _local_flow(false),
	_level(0), _dt_edges(0),
	_dt_index(0), _dt(0), _queue(),
	_tolerance(), _flow_value(), _max_value()
    {
      if (_source == _target) {
	throw InvalidArgument();
      }
    }

    /// \brief Destrcutor.
    ///
    /// Destructor.
    ~DinitzSleatorTarjan() {
      destroyStructures();
    }

    /// \brief Sets the capacity map.
    ///
    /// Sets the capacity map.
    /// \return \c (*this)
    DinitzSleatorTarjan& capacityMap(const CapacityMap& map) {
      _capacity = &map;
      return *this;
    }

    /// \brief Sets the flow map.
    ///
    /// Sets the flow map.
    /// \return \c (*this)
    DinitzSleatorTarjan& flowMap(FlowMap& map) {
      if (_local_flow) {
	delete _flow;
	_local_flow = false;
      }
      _flow = &map;
      return *this;
    }

    /// \brief Returns the flow map.
    ///
    /// \return The flow map.
    const FlowMap& flowMap() {
      return *_flow;
    }

    /// \brief Sets the source node.
    ///
    /// Sets the source node.
    /// \return \c (*this)
    DinitzSleatorTarjan& source(const Node& node) {
      _source = node;
      return *this;
    }

    /// \brief Sets the target node.
    ///
    /// Sets the target node.
    /// \return \c (*this)
    DinitzSleatorTarjan& target(const Node& node) {
      _target = node;
      return *this;
    }

    /// \brief Sets the tolerance used by algorithm.
    ///
    /// Sets the tolerance used by algorithm.
    DinitzSleatorTarjan& tolerance(const Tolerance& tolerance) const {
      _tolerance = tolerance;
      if (_dt) {
	_dt.tolerance(_tolerance);
      }
      return *this;
    } 

    /// \brief Returns the tolerance used by algorithm.
    ///
    /// Returns the tolerance used by algorithm.
    const Tolerance& tolerance() const {
      return tolerance;
    } 

  private:
        
    void createStructures() {
      if (!_flow) {
	_flow = Traits::createFlowMap(_graph);
	_local_flow = true;
      }
      if (!_level) {
	_level = new LevelMap(_graph);
      }
      if (!_dt_index && !_dt) {
	_dt_index = new IntNodeMap(_graph);
	_dt = new DynTree(*_dt_index, _tolerance);
      }
      if (!_dt_edges) {
	_dt_edges = new EdgeNodeMap(_graph);
      }
      _queue.resize(countNodes(_graph));
      _max_value = _dt->maxValue();
    }

    void destroyStructures() {
      if (_local_flow) {
	delete _flow;
      }
      if (_level) {
	delete _level;
      }
      if (_dt) {
	delete _dt;
      }
      if (_dt_index) {
	delete _dt_index;
      }
      if (_dt_edges) {
	delete _dt_edges;
      }
    }

    bool createLayeredGraph() {

      for (NodeIt n(_graph); n != INVALID; ++n) {
	_level->set(n, -2);
      }
      
      int level = 0;

      _queue[0] = _target;
      _level->set(_target, level);

      int first = 0, last = 1, limit = 0;
      
      while (first != last && (*_level)[_source] == -2) {
	if (first == limit) {
	  limit = last;
	  ++level;
	}
	
	Node n = _queue[first++];
	  
	for (OutEdgeIt e(_graph, n); e != INVALID; ++e) {
	  Node v = _graph.target(e);
	  if ((*_level)[v] != -2) continue;
	  Value rem = (*_flow)[e];
	  if (!_tolerance.positive(rem)) continue;
	  _level->set(v, level);
	  _queue[last++] = v;
	}
	
	for (InEdgeIt e(_graph, n); e != INVALID; ++e) {
	  Node v = _graph.source(e);
	  if ((*_level)[v] != -2) continue;
	  Value rem = (*_capacity)[e] - (*_flow)[e];
	  if (!_tolerance.positive(rem)) continue;
	  _level->set(v, level);
	  _queue[last++] = v;
	}
      }
      return (*_level)[_source] != -2;
    }

    void initEdges() {
      for (NodeIt n(_graph); n != INVALID; ++n) {
	_graph.firstOut((*_dt_edges)[n], n);
      }
    }
        
    
    void augmentPath() {
      Value rem;
      Node n = _dt->findCost(_source, rem);
      _flow_value += rem;
      _dt->addCost(_source, - rem);

      _dt->cut(n);
      _dt->addCost(n, _max_value);

      Edge e = (*_dt_edges)[n];
      if (_graph.source(e) == n) {
	_flow->set(e, (*_capacity)[e]);
	
	_graph.nextOut(e);
	if (e == INVALID) {
	  _graph.firstIn(e, n);
	}
      } else {
	_flow->set(e, 0);
	_graph.nextIn(e);
      }
      _dt_edges->set(n, e);

    }

    bool advance(Node n) {
      Edge e = (*_dt_edges)[n];
      if (e == INVALID) return false;

      Node u;
      Value rem;      
      if (_graph.source(e) == n) {
	u = _graph.target(e);
	while ((*_level)[n] != (*_level)[u] + 1 || 
	       !_tolerance.positive((*_capacity)[e] - (*_flow)[e])) {
	  _graph.nextOut(e);
	  if (e == INVALID) break;
	  u = _graph.target(e);
	}
	if (e != INVALID) {
	  rem = (*_capacity)[e] - (*_flow)[e];
	} else {
	  _graph.firstIn(e, n);
	  if (e == INVALID) {
	    _dt_edges->set(n, INVALID);
	    return false;
	  }
	  u = _graph.source(e);
	  while ((*_level)[n] != (*_level)[u] + 1 ||
		 !_tolerance.positive((*_flow)[e])) {
	    _graph.nextIn(e);
	    if (e == INVALID) {
	      _dt_edges->set(n, INVALID);
	      return false;
	    }
	    u = _graph.source(e);
	  }
	  rem = (*_flow)[e];
	}
      } else {
	u = _graph.source(e);
	while ((*_level)[n] != (*_level)[u] + 1 ||
	       !_tolerance.positive((*_flow)[e])) {
	  _graph.nextIn(e);
	  if (e == INVALID) {
	    _dt_edges->set(n, INVALID);
	    return false;
	  }
	  u = _graph.source(e);
	}
	rem = (*_flow)[e];
      }

      _dt->addCost(n, - std::numeric_limits<Value>::max());
      _dt->addCost(n, rem);
      _dt->link(n, u);
      _dt_edges->set(n, e);
      return true;
    }

    void retreat(Node n) {
      _level->set(n, -1);
      
      for (OutEdgeIt e(_graph, n); e != INVALID; ++e) {
	Node u = _graph.target(e);
	if ((*_dt_edges)[u] == e && _dt->findRoot(u) == n) {
	  Value rem;
	  _dt->findCost(u, rem);
	  _flow->set(e, rem);
	  _dt->cut(u);
	  _dt->addCost(u, - rem);
	  _dt->addCost(u, _max_value);
	}
      }
      for (InEdgeIt e(_graph, n); e != INVALID; ++e) {
	Node u = _graph.source(e);
	if ((*_dt_edges)[u] == e && _dt->findRoot(u) == n) {
	  Value rem;
	  _dt->findCost(u, rem);
	  _flow->set(e, (*_capacity)[e] - rem);
	  _dt->cut(u);
	  _dt->addCost(u, - rem);
	  _dt->addCost(u, _max_value);
	}
      }
    }

    void extractTrees() {
      for (NodeIt n(_graph); n != INVALID; ++n) {
	
	Node w = _dt->findRoot(n);
      
	while (w != n) {
      
	  Value rem;      
	  Node u = _dt->findCost(n, rem);

	  _dt->cut(u);
	  _dt->addCost(u, - rem);
	  _dt->addCost(u, _max_value);
	  
	  Edge e = (*_dt_edges)[u];
	  _dt_edges->set(u, INVALID);
	  
	  if (u == _graph.source(e)) {
	    _flow->set(e, (*_capacity)[e] - rem);
	  } else {
	    _flow->set(e, rem);
	  }
	  
	  w = _dt->findRoot(n);
	}      
      }
    }


  public:
    
    /// \name Execution control The simplest way to execute the
    /// algorithm is to use the \c run() member functions.
    /// \n
    /// If you need more control on initial solution or
    /// execution then you have to call one \ref init() function and then
    /// the start() or multiple times the \c augment() member function.  
    
    ///@{

    /// \brief Initializes the algorithm
    /// 
    /// It sets the flow to empty flow.
    void init() {
      createStructures();

      _dt->clear();
      for (NodeIt n(_graph); n != INVALID; ++n) {
        _dt->makeTree(n);
        _dt->addCost(n, _max_value);
      }

      for (EdgeIt it(_graph); it != INVALID; ++it) {
        _flow->set(it, 0);
      }
      _flow_value = 0;
    }
    
    /// \brief Initializes the algorithm
    /// 
    /// Initializes the flow to the \c flowMap. The \c flowMap should
    /// contain a feasible flow, ie. in each node excluding the source
    /// and the target the incoming flow should be equal to the
    /// outgoing flow.
    template <typename FlowMap>
    void flowInit(const FlowMap& flowMap) {
      createStructures();

      _dt->clear();
      for (NodeIt n(_graph); n != INVALID; ++n) {
        _dt->makeTree(n);
        _dt->addCost(n, _max_value);
      }

      for (EdgeIt e(_graph); e != INVALID; ++e) {
	_flow->set(e, flowMap[e]);
      }
      _flow_value = 0;
      for (OutEdgeIt jt(_graph, _source); jt != INVALID; ++jt) {
        _flow_value += (*_flow)[jt];
      }
      for (InEdgeIt jt(_graph, _source); jt != INVALID; ++jt) {
        _flow_value -= (*_flow)[jt];
      }
    }

    /// \brief Initializes the algorithm
    /// 
    /// Initializes the flow to the \c flowMap. The \c flowMap should
    /// contain a feasible flow, ie. in each node excluding the source
    /// and the target the incoming flow should be equal to the
    /// outgoing flow.  
    /// \return %False when the given flowMap does not contain
    /// feasible flow.
    template <typename FlowMap>
    bool checkedFlowInit(const FlowMap& flowMap) {
      createStructures();

      _dt->clear();
      for (NodeIt n(_graph); n != INVALID; ++n) {
        _dt->makeTree(n);
        _dt->addCost(n, _max_value);
      }

      for (EdgeIt e(_graph); e != INVALID; ++e) {
	_flow->set(e, flowMap[e]);
      }
      for (NodeIt it(_graph); it != INVALID; ++it) {
        if (it == _source || it == _target) continue;
        Value outFlow = 0;
        for (OutEdgeIt jt(_graph, it); jt != INVALID; ++jt) {
          outFlow += (*_flow)[jt];
        }
        Value inFlow = 0;
        for (InEdgeIt jt(_graph, it); jt != INVALID; ++jt) {
          inFlow += (*_flow)[jt];
        }
        if (_tolerance.different(outFlow, inFlow)) {
          return false;
        }
      }
      for (EdgeIt it(_graph); it != INVALID; ++it) {
        if (_tolerance.less((*_flow)[it], 0)) return false;
        if (_tolerance.less((*_capacity)[it], (*_flow)[it])) return false;
      }
      _flow_value = 0;
      for (OutEdgeIt jt(_graph, _source); jt != INVALID; ++jt) {
        _flow_value += (*_flow)[jt];
      }
      for (InEdgeIt jt(_graph, _source); jt != INVALID; ++jt) {
        _flow_value -= (*_flow)[jt];
      }
      return true;
    }

    /// \brief Executes the algorithm
    ///
    /// It runs augmenting phases by adding blocking flow until the
    /// optimal solution is reached.
    void start() {
      while (augment());
    }

    /// \brief Augments the flow with a blocking flow on a layered
    /// graph.
    /// 
    /// This function builds a layered graph and then find a blocking
    /// flow on this graph. The number of the levels in the layered
    /// graph is strictly increasing in each augmenting phase
    /// therefore the number of the augmentings is at most \f$ n-1
    /// \f$.  The length of each phase is at most \f$ O(m \log(n))
    /// \f$, that the overall time complexity is \f$ O(nm \log(n)) \f$.
    /// \return %False when there is not residual path between the
    /// source and the target so the current flow is a feasible and
    /// optimal solution.
    bool augment() {
      Node n;

      if (createLayeredGraph()) {
	
	Timer bf_timer;
	initEdges();

	n = _dt->findRoot(_source);
	while (true) {
	  Edge e;
	  if (n == _target) {
	    augmentPath();
	  } else if (!advance(n)) {
	    if (n != _source) {
	      retreat(n);
	    } else {
	      break;
	    }
	  }
	  n = _dt->findRoot(_source);
	}     
	extractTrees();

	return true;
      } else {
	return false;
      }
    }
    
    /// \brief runs the algorithm.
    /// 
    /// It is just a shorthand for:
    ///
    ///\code 
    /// ek.init();
    /// ek.start();
    ///\endcode
    void run() {
      init();
      start();
    }

    /// @}

    /// \name Query Functions 
    /// The result of the Dinitz-Sleator-Tarjan algorithm can be
    /// obtained using these functions.
    /// \n
    /// Before the use of these functions,
    /// either run() or start() must be called.
    
    ///@{

    /// \brief Returns the value of the maximum flow.
    ///
    /// Returns the value of the maximum flow by returning the excess
    /// of the target node \c t. This value equals to the value of
    /// the maximum flow already after the first phase.
    Value flowValue() const {
      return _flow_value;
    }


    /// \brief Returns the flow on the edge.
    ///
    /// Sets the \c flowMap to the flow on the edges. This method can
    /// be called after the second phase of algorithm.
    Value flow(const Edge& edge) const {
      return (*_flow)[edge];
    }

    /// \brief Returns true when the node is on the source side of minimum cut.
    ///

    /// Returns true when the node is on the source side of minimum
    /// cut. This method can be called both after running \ref
    /// startFirstPhase() and \ref startSecondPhase().
    bool minCut(const Node& node) const {
      return (*_level)[node] == -2;
    }

    /// \brief Returns a minimum value cut.
    ///
    /// Sets \c cut to the characteristic vector of a minimum value cut
    /// It simply calls the minMinCut member.
    /// \retval cut Write node bool map. 
    template <typename CutMap>
    void minCutMap(CutMap& cutMap) const {
      for (NodeIt n(_graph); n != INVALID; ++n) {
	cutMap.set(n, (*_level)[n] == -2);
      }
      cutMap.set(_source, true);
    }    

    /// @}

  };
}

#endif
