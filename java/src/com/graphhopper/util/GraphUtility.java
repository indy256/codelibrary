/*
 *  Licensed to Peter Karich under one or more contributor license 
 *  agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  Peter Karich licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except 
 *  in compliance with the License. You may obtain a copy of the 
 *  License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

import com.graphhopper.coll.MyBitSet;
import com.graphhopper.coll.MyBitSetImpl;
import com.graphhopper.storage.Directory;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphStorage;
import com.graphhopper.storage.LevelGraph;
import com.graphhopper.storage.RAMDirectory;
import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is introduced as a helper to avoid cluttering the Graph interface
 * with all the common methods. Most of the methods are useful for unit tests.
 *
 * @author Peter Karich,
 */
public class GraphUtility {

    private static Logger logger = LoggerFactory.getLogger(GraphUtility.class);

    /**
     * @throws could throw exception if uncatched problems like index out of
     * bounds etc
     */
    public static List<String> getProblems(Graph g) {
        List<String> problems = new ArrayList<String>();
        int nodes = g.nodes();
        int nodeIndex = 0;
        try {
            for (; nodeIndex < nodes; nodeIndex++) {
                double lat = g.getLatitude(nodeIndex);
                if (lat > 90 || lat < -90)
                    problems.add("latitude is not within its bounds " + lat);
                double lon = g.getLongitude(nodeIndex);
                if (lon > 180 || lon < -180)
                    problems.add("longitude is not within its bounds " + lon);
                int incom = count(g.getIncoming(nodeIndex));
                int out = count(g.getOutgoing(nodeIndex));
                int e = count(g.getEdges(nodeIndex));
                if (Math.max(out, incom) > e)
                    problems.add("count incoming or outgoing edges should be maximum "
                            + e + " but were:" + incom + "(in), " + out + "(out)");

                EdgeIterator iter = g.getEdges(nodeIndex);
                while (iter.next()) {
                    if (iter.node() >= nodes)
                        problems.add("edge of " + nodeIndex + " has a node " + iter.node() + " greater or equal to getNodes");
                    if (iter.node() < 0)
                        problems.add("edge of " + nodeIndex + " has a negative node " + iter.node());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("problem with node " + nodeIndex, ex);
        }

//        for (int i = 0; i < nodes; i++) {
//            new XFirstSearch().start(g, i, false);
//        }

        return problems;
    }

    /**
     * note/todo: this methods counts edges twice if both directions are
     * available
     */
    public static int countEdges(Graph g) {
        int counter = 0;
        int nodes = g.nodes();
        for (int i = 0; i < nodes; i++) {
            EdgeIterator iter = g.getOutgoing(i);
            while (iter.next()) {
                counter++;
            }
        }
        return counter;
    }

    public static int count(EdgeIterator iter) {
        return neighbors(iter).size();
    }

    public static List<Integer> neighbors(EdgeIterator iter) {
        List<Integer> list = new ArrayList<Integer>();
        while (iter.next()) {
            list.add(iter.node());
        }
        return list;
    }

    public static int count(RawEdgeIterator iter) {
        int counter = 0;
        while (iter.next()) {
            ++counter;
        }
        return counter;
    }

    public static int count(Iterable<?> iter) {
        int counter = 0;
        for (Object o : iter) {
            ++counter;
        }
        return counter;
    }

    public static boolean contains(EdgeIterator iter, int... locs) {
        TIntHashSet set = new TIntHashSet();

        while (iter.next()) {
            set.add(iter.node());
        }
        for (int l : locs) {
            if (!set.contains(l))
                return false;
        }
        return true;
    }

    public static EdgeIterator until(EdgeIterator edges, int node, int flags) {
        while (edges.next()) {
            if (edges.node() == node && edges.flags() == flags)
                return edges;
        }
        return EMPTY;
    }

    public static EdgeIterator until(EdgeIterator edges, int node) {
        while (edges.next()) {
            if (edges.node() == node)
                return edges;
        }
        return EMPTY;
    }

    /**
     * Added this helper method to avoid cluttering the graph interface. Good
     * idea?
     */
    public static EdgeIterator getEdges(Graph graph, int index, boolean out) {
        if (out)
            return graph.getOutgoing(index);
        else
            return graph.getIncoming(index);
    }

    public static void printInfo(final Graph g, int startNode, final int counts) {
        new XFirstSearch() {
            int counter = 0;

            @Override protected boolean goFurther(int nodeId) {
                System.out.println(getNodeInfo(g, nodeId));
                if (counter++ > counts)
                    return false;
                return true;
            }
        }.start(g, startNode, false);
    }

    public static String getNodeInfo(LevelGraph g, int nodeId) {
        EdgeSkipIterator iter = g.getOutgoing(nodeId);
        String str = nodeId + ":" + g.getLatitude(nodeId) + "," + g.getLongitude(nodeId) + "\n";
        while (iter.next()) {
            str += "  ->" + iter.node() + "(" + iter.skippedEdge() + " " + iter.edge() + ") \t"
                    + BitUtil.toBitString(iter.flags(), 8) + "\n";
        }
        return str;
    }

    public static String getNodeInfo(Graph g, int nodeId) {
        EdgeIterator iter = g.getOutgoing(nodeId);
        String str = nodeId + ":" + g.getLatitude(nodeId) + "," + g.getLongitude(nodeId) + "\n";
        while (iter.next()) {
            str += "  ->" + iter.node() + " (" + iter.distance() + ") pillars:"
                    + iter.wayGeometry().size() + ", edgeId:" + iter.edge()
                    + "\t" + BitUtil.toBitString(iter.flags(), 8) + "\n";
        }
        return str;
    }

    static Directory guessDirectory(GraphStorage store) {
        String location = store.directory().location();
        Directory outdir;
        boolean isStoring = ((RAMDirectory) store.directory()).isStoring();
        outdir = new RAMDirectory(location, isStoring);
        return outdir;
    }

    /**
     * @return the graph 'to'
     */
    // TODO very similar to createSortedGraph -> use a 'int map(int)' interface
    public static Graph copyTo(Graph from, Graph to) {
        int len = from.nodes();
        // important to avoid creating two edges for edges with both directions        
        MyBitSet bitset = new MyBitSetImpl(len);
        for (int oldNode = 0; oldNode < len; oldNode++) {
            bitset.add(oldNode);
            to.setNode(oldNode, from.getLatitude(oldNode), from.getLongitude(oldNode));
            EdgeIterator eIter = from.getEdges(oldNode);
            while (eIter.next()) {
                int adjacentNodeIndex = eIter.node();
                if (bitset.contains(adjacentNodeIndex))
                    continue;
                to.edge(oldNode, adjacentNodeIndex, eIter.distance(), eIter.flags()).wayGeometry(eIter.wayGeometry());
            }
        }
        return to;
    }

    public static int getToNode(Graph g, int edge, int endNode) {
        if (EdgeIterator.Edge.isValid(edge)) {
            EdgeIterator iterTo = g.getEdgeProps(edge, endNode);
            return iterTo.node();
        }
        return endNode;
    }
    public static final EdgeSkipIterator EMPTY = new EdgeSkipIterator() {
        @Override public int skippedEdge() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public void skippedEdge(int node) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public void distance(double dist) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public void flags(int flags) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public boolean next() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public int edge() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public int baseNode() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public int node() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public double distance() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public int flags() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public PointList wayGeometry() {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public void wayGeometry(PointList list) {
            throw new UnsupportedOperationException("Not supported. Edge is empty.");
        }

        @Override public boolean isEmpty() {
            return true;
        }
    };
}