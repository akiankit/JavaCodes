package com.codechef.practic.easy;

import java.math.BigInteger;

class AMFIB {

    class Root {
        BigInteger root ;
        boolean isPerfect;
        public Root(BigInteger r , boolean i) {
            root = r;
            isPerfect = i;
        }
    }

    static AMFIB main;

    static BigInteger four ;

    static BigInteger five ;

    public static void main(String [] args ) {
        long st = System.currentTimeMillis();
        main = new AMFIB();
        System.out.println(checkSquare(new BigInteger("1234")));
        /*try{
            String str;         
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            BufferedOutputStream bos = new BufferedOutputStream(System.out);
            String eol = System.getProperty("line.separator");
            byte [] eolb = eol.getBytes();
            str  = br.readLine();
            int t = Integer.parseInt(str);
            four = new BigInteger("4");
            five = new BigInteger("5");
            for(int i = 0 ; i < t ; i++) {
                str = br.readLine();
                BigInteger a = new BigInteger(str);
                BigInteger a2 = calculateSquare(a).multiply(five);
                BigInteger a2m4 = a2.subtract(four);
                BigInteger a2p4 = a2.add(four);
                boolean ans = false;
                Root sqrt1 = checkSquare(a2m4);
                Root sqrt2 = checkSquare(a2p4);
                boolean one = sqrt1.isPerfect;
                boolean two = sqrt2.isPerfect;
                if(one | two) {
                    ans = true;
                } 
                if(ans) {
                    bos.write("YES".getBytes());
                } else {
                    bos.write("NO".getBytes());
                }
                bos.write(eolb);
            }
            bos.flush();
        }  catch(IOException ioe) {
            ioe.printStackTrace();
        }*/
        long en = System.currentTimeMillis();
        System.err.println("time taken = " + (en-st));
    }

    public static Root checkSquare(BigInteger b) {
        int length = b.toString().length();
        BigInteger start = BigInteger.ONE;
        BigInteger ten = new BigInteger("10");
        for(int i = 1 ; i < length/2 ; i++) {
            start = start.multiply(ten);
        }
        BigInteger end = start.multiply(ten).multiply(ten);
        while(end.subtract(start).compareTo(BigInteger.ONE)>0) {    
            BigInteger two = new BigInteger("2");
            BigInteger mid = start.add(end).divide(two);
            int comp = b.compareTo(calculateSquare(mid));
            if(comp==0) {
                return main.new Root(mid,true);
            } else if (comp>0) {
                start = mid;
            } else {
                end = mid;
            }
        }
        if(start.compareTo(end)==0) {
            int comp = b.compareTo(start.multiply(start));
            if(comp==0) {
                return main.new Root(start,true);
            } else if (comp<0){
                return main.new Root(start.subtract(BigInteger.ONE),false);
            } else {
                return main.new Root(start,false);
            }
        }
        if(start.add(BigInteger.ONE).compareTo(end)==0) {
            int comp = b.compareTo(start.multiply(start));
            if(comp==0) {
                return main.new Root(start,true);
            } else if (comp<0) {
                return main.new Root(start.subtract(BigInteger.ONE),false);
            }
            comp = b.compareTo(end.multiply(end));
            if(comp==0) {
                return main.new Root(end,true);
            }else if (comp<0){
                return main.new Root(end.subtract(BigInteger.ONE),false);
            }
        }
        return main.new Root(end.add(BigInteger.ONE),false);
    }
    
    public static BigInteger calculateSquare(BigInteger a) {
        return a.pow(2);
    }
    
}
