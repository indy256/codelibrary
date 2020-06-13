package numeric;

// Fast Fourier transform
// https://cp-algorithms.com/algebra/fft.html
public class FFT_slow {
    public static int[] multiplyBigint(int[] a, int[] b) {
        int need = a.length + b.length;
        int n = Integer.highestOneBit(need - 1) << 1;
        double[] aReal = new double[n];
        double[] aImag = new double[n];
        double[] bReal = new double[n];
        double[] bImag = new double[n];
        for (int i = 0; i < a.length; i++) aReal[i] = a[i];
        for (int i = 0; i < b.length; i++) bReal[i] = b[i];
        FFT.fft(aReal, aImag, false);
        FFT.fft(bReal, bImag, false);
        for (int i = 0; i < n; i++) {
            double real = aReal[i] * bReal[i] - aImag[i] * bImag[i];
            aImag[i] = aImag[i] * bReal[i] + bImag[i] * aReal[i];
            aReal[i] = real;
        }
        FFT.fft(aReal, aImag, true);
        int[] result = new int[need];
        for (int i = 0, carry = 0; i < need; i++) {
            result[i] = (int) (aReal[i] + 0.5) + carry;
            carry = result[i] / 10;
            result[i] %= 10;
        }
        return result;
    }
}
