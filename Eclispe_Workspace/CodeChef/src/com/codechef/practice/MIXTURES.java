
package com.codechef.practice;

public class MIXTURES {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] p = new int[]{};
        MatrixChainOrder(p);
    }

    // Matrix Ai has dimension p[i-1] x p[i] for i = 1..n
    static void MatrixChainOrder(int p[]) {
        // length[p] = n + 1
        int n = p.length - 1;
        int[][] m = new int[n+1][n+1];
        // m[i,j] = Minimum number of scalar multiplications (i.e., cost)
        // needed to compute the matrix A[i]A[i+1]...A[j] = A[i..j]
        // cost is zero when multiplying one matrix
        for (int i = 1; i <= n; i++)
            m[i][i] = 0;

        for (int L = 2; L <= n; L++) { // L is chain length
            for (int i = 1; i <= n - L + 1; i++) {
                int j = i + L - 1;
                m[i][j] = Integer.MAX_VALUE;
                for (int k = i; k <= j - 1; k++) {
                    // q = cost/scalar multiplications
                    int q = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (q < m[i][j]) {
                        m[i][j] = q;
                    }
                }
            }
        }
    }
}
