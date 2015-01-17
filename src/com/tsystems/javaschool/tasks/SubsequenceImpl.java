package com.tsystems.javaschool.tasks;

import java.util.List;
import java.lang.Object;
import java.util.Arrays;

/**
 *
 * @author Prigodisch Nikolay
 */
public class SubsequenceImpl implements Subsequence{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Subsequence s = new SubsequenceImpl();
        boolean b = s.find(Arrays.asList("AB", "BC"), Arrays.asList("BD", "AB", "ABC",  "BC"));
        System.out.println(b);
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    public boolean find(List x, List y){
        if (x.size()<y.size()){
            int j=0;
            try {
            for (int i=0; i<x.size(); i++){
                while (y.get(j)!=x.get(i)){
                    j++;
                }
                if (y.get(j)==x.get(i)){
                    continue;
                } else {                    
                    return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e){}
}
    return false;
    }
}
