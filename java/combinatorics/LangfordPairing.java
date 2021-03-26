package combinatorics;
import java.util.*;

//https://en.wikipedia.org/wiki/Langford_pairing
public class LangfordPairing {
    //time complexity O(n^2)
    //return number of Langford Pairing
    public static int findLangfordPairing(int[] arr, int n) {
        if (n == 0) {
            System.out.println(Arrays.toString(arr)); // output all LangfordPairing array
            return 1;
        }
        int res = 0;
        for (int i=0;i<arr.length-n-1;i++) {
            if(arr[i] == 0 && arr[i+n+1] == 0) { // find the valid answer
                arr[i] = n; arr[i+n+1] = n;
                res += findLangfordPairing(arr, n-1);
                arr[i] = 0; arr[i+n+1] = 0; //reset the memo
            }
        }
        return res;
    }

    public static int LangfordPairing(int n){
        int[] arr = new int[2*n];
        return findLangfordPairing(arr, n);
    }

    public static void main(String[] args) {
        int n = 4;
        System.out.println(LangfordPairing(n));
    }
}
