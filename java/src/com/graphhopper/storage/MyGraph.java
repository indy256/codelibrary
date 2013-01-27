package com.graphhopper.storage;

import com.graphhopper.util.EdgeSkipIterator;
import com.graphhopper.util.RawEdgeIterator;

import java.util.ArrayList;
import java.util.List;

public class MyGraph implements LevelGraph {

	List<List<Integer>> g = new ArrayList<>();
	List<List<Integer>> rg = new ArrayList<>();
	List<List<Double>> d = new ArrayList<>();
	List<Integer> nodeLevels = new ArrayList<>();

	@Override
	public void setLevel(int index, int level) {
		while (nodeLevels.size() <= index) nodeLevels.add(0);
		nodeLevels.set(index, level);
	}

	@Override
	public int getLevel(int index) {
		while (nodeLevels.size() <= index) nodeLevels.add(0);
		return nodeLevels.get(0);
	}

	@Override
	public int nodes() {
		return nodeLevels.size();
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, int flags) {
		return null;
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, boolean bothDirections) {
		return null;
	}

	@Override
	public EdgeSkipIterator getEdgeProps(int edgeId, int endNode) {
		return null;
	}

	@Override
	public EdgeSkipIterator getEdges(int nodeId) {
		return null;
	}

	@Override
	public EdgeSkipIterator getIncoming(int nodeId) {
		return null;
	}

	@Override
	public EdgeSkipIterator getOutgoing(int nodeId) {
		return null;
	}

	@Override
	public RawEdgeIterator allEdges() {
		return null;
	}
}
