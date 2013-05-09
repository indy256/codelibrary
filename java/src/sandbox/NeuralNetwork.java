package sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {

	static class Utils {
		public static double sigmoid(double x) {
			return 1 / (1 + Math.exp(-x));
		}
	}

	public int[] layers;
	public int totalNo;
	public double[] v;
	public double[][] weights;
	public int[][] inputs;
	public double[] error;

	public NeuralNetwork(int[] layers) {
		this.layers = layers;
		totalNo = 0;
		for (int x : layers) {
			totalNo += x;
		}

		weights = new double[totalNo][];
		inputs = new int[totalNo][];
		error = new double[totalNo];
		v = new double[totalNo];

		int hashCode = 0;
		for (int x : layers) {
			hashCode = hashCode * 17 + x;
		}
		Random rand = new Random(hashCode);

		int lastLayer = 0;
		int curLayer = 0;
		for (int i = 0; i < layers.length; i++) {
			int lastLayerSize = i == 0 ? 0 : layers[i - 1];
			for (int j = 0; j < layers[i]; j++) {
				weights[curLayer + j] = new double[lastLayerSize];
				inputs[curLayer + j] = new int[lastLayerSize];
				for (int k = 0; k < lastLayerSize; k++) {
					weights[curLayer + j][k] = (double) rand.nextDouble() * 2 - 1;
					inputs[curLayer + j][k] = lastLayer + k;
				}
				// weights[curLayer + j][lastLayerSize] = rand.NextDouble() * 2 - 1;
				// inputs[curLayer + j][lastLayerSize] = -1;
			}
			lastLayer = curLayer;
			curLayer += layers[i];
		}
	}

	public void setWeights(double[] newWeights) {
		int p = 0;
		for (int i = 0; i < totalNo; i++)
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = newWeights[p++];
		if (newWeights.length != p)
			System.err.println("Error: wrong init of setWeights");
	}

	public double[] getWeights() {
		List<Double> rv = new ArrayList<>();
		for (int i = 0; i < totalNo; i++)
			for (double w : weights[i]) {
				rv.add(w);
			}
		double[] res = new double[rv.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = rv.get(i);
		}
		return res;
	}

	public double[] compute(double[] data) {
		Arrays.fill(v, 0);

		for (int i = 0; i < layers[0]; i++)
			v[i] = data[i];

		for (int i = layers[0]; i < totalNo; i++) {
			// for (int j = 0; j < weights[i].Length; j++)
			// v[i] += weights[i][j] * (inputs[i][j] < 0 ? 1 : v[inputs[i][j]]);
			for (int j = 0; j < weights[i].length; j++)
				v[i] += weights[i][j] * v[inputs[i][j]];
			v[i] = Utils.sigmoid(v[i]);
		}

		int outputsNo = layers[layers.length - 1];
		double[] rv = new double[outputsNo];
		for (int i = 0; i < outputsNo; i++)
			rv[i] = v[totalNo - outputsNo + i];

		return rv;
	}
}
