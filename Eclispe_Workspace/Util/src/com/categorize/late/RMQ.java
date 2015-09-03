package com.categorize.late;

public class RMQ {

    // https://www.topcoder.com/community/data-science/data-science-tutorials/range-minimum-query-and-lowest-common-ancestor/#Range_Minimum_Query_(RMQ)
    /*public static void main(String[] args) {
        int a[] = {2,4,3,1,6,7,8,9,2,7};
        RmqSparseTable table = new RmqSparseTable(a);
        System.out.println(table.getMin(0, 4));
    }*/
    public static void main(String[] args) {
        int a[] = {2,4,3,1,6,7,8,9,2,7};
        RMQUsingBlocks rmq = new RMQUsingBlocks();
        int[] M = rmq.createPreprocessArray(a);
        for(int i=0;i<M.length;i++) {
            System.out.print(M[i]+" ");
        }
        System.out.println();
        
        for(int i=0;i<a.length;i++){
            for(int j=i;j<a.length;j++){
                System.out.println("Min b/w i=" + i + " j=" + j + " is=" + a[rmq.getMinIndex(a, i, j, M)]);
            }
        }
    }

}

class RmqSparseTable {

    int[] logTable;
    int[][] rmq;
    int[] a;

    public RmqSparseTable(int[] a) {
      this.a = a;
      int n = a.length;

      logTable = new int[n + 1];
      for (int i = 2; i <= n; i++)
        logTable[i] = logTable[i >> 1] + 1;

      System.out.println("logtable");
      for(int i=0;i<=n;i++)
          System.out.print(logTable[i]+" ");
      System.out.println();
      rmq = new int[logTable[n] + 1][n];

      for (int i = 0; i < n; ++i)
        rmq[0][i] = i;

      for (int k = 1; (1 << k) < n; ++k) {
        for (int i = 0; i + (1 << k) <= n; i++) {
          int x = rmq[k - 1][i];
          int y = rmq[k - 1][i + (1 << k - 1)];
          rmq[k][i] = a[x] <= a[y] ? x : y;
        }
      }
      
      for(int i=0;i<rmq.length;i++){
          for(int j=0;j<rmq[i].length;j++)
              System.out.print(rmq[i][j]+" ");
          System.out.println();
      }
    }
    
    

    public int getMin(int i, int j) {
      int k = logTable[j - i];
      int x = rmq[k][i];
      int y = rmq[k][j - (1 << k) + 1];
      return a[x] <= a[y] ? a[x] : a[y];
    }

}

class RMQUsingBlocks {
    
    public int getMin(int[] a, int l, int r, int[] M) {
        int min = a[l];
        if (l == r)
            return min;
        int blockSize = (int)Math.sqrt(a.length);
        while (l % blockSize != 0) {
            if (min > a[l])
                min = a[l];
            l++;
        }
        while (l % blockSize == 0 && (l + blockSize - 1) < r) {
            if (min > M[l / blockSize])
                min = M[l / blockSize];
            l = l + blockSize;
        }
        while (l <= r) {
            if (min > a[l])
                min = a[l];
            l++;

        }
        return min;
    }

    public int getMinIndex(int[] a, int l, int r, int[] M) {
        int min = l;
        if (l == r)
            return min;
        int blockSize = (int)Math.sqrt(a.length);
        while (l % blockSize != 0) {
            if (a[min] > a[l])
                min = l;
            l++;
        }
        while (l % blockSize == 0 && (l + blockSize - 1) < r) {
            if (a[min] > M[l / blockSize]) {
                min = l;
                int k = l + 1;
                while (k % blockSize != 0) {
                    if (a[min] >= a[k])
                        min = k;
                    k++;
                }
            }
            l = l + blockSize;
        }
        while (l <= r) {
            if (a[min] > a[l])
                min = l;
            l++;

        }
        return min;
   }

    
    
    public int[] createPreprocessArray(int[] a) {
        int blockSize = (int)Math.sqrt(a.length);
        int size = a.length % blockSize == 0 ? a.length / blockSize : a.length / blockSize + 1;
        int[] M = new int[size];
        int min = a[0];
        for (int i = 0, k = 0, l = 0; i < a.length; i++, k++) {
            if (k == blockSize) {
                k = 0;
                M[l++] = min;
                min = a[i];
            }
            if (min > a[i])
                min = a[i];
            if (i == a.length - 1)
                M[l++] = min;
        }
        return M;
    }
}