
package com.tsystems.javaschool.tasks;
import java.io.*;

/**
 *
 * @author Prigodisch Nikolay
 */
public class DuplicateFinderImpl implements DuplicateFinder {
    
    
    /**
         * Read the sourcefile
         * @param basefile sourcefile
         * @return strings in the sourcefile
         */
    public static String[] read(File basefile){
     

    String[] lines = new String[0];
        String[] buf;

        String s = "";
        int count = 0;


        try {
            BufferedReader br = new BufferedReader(new FileReader(basefile));

            while ((s = br.readLine()) != null) {
                buf = lines;
                lines = new String[count + 1];
                for (int i = 0; i < buf.length; i++) {
                    lines[i] = buf[i];
                }
                lines[count] = s;
                count++;
            }


            br.close();

        } catch (IOException e) {}
        return lines;
    }
    

    
        /**
         * sort and write strings in the targetfile
         * @param basefile 
         *         targetfile
         * @param s 
         *         strings to be written
         */
    public static void sort(File basefile, String[] s)  throws IOException {
        

        String[] lines = s;
        
        if (basefile.exists()){
            lines = read(basefile);
            
            for (int i=0; i<lines.length; i++) {lines[i] = lines[i].substring(0,lines[i].length()-3);}
            
            for (int i=0; i<s.length; i++) {lines = addToArray(lines, s[i]);}
            
        }
        
    

            
            java.util.Arrays.sort(lines);
            BufferedWriter myfile = new BufferedWriter(new FileWriter(basefile));
            int count=0;
        
        
        for (int i = 0; i < lines.length; i++){
        
            
            if (i==lines.length-1 && lines[i].equals(lines[i-1])){
                    count++;

                    myfile.write(lines[i]+"["+count+"]"+"\r\n");

                }
            
            if (i==lines.length-1 && !lines[i].equals(lines[i-1])){
                    count++;     
                    myfile.write(lines[i]+"["+count+"]"+"\r\n");
                    count=0;
                }

                try {
                if (lines[i].equals(lines[i+1])==true){

                    count++;

                }
                else if (lines[i].equals(lines[i+1])==false){
                    count++;
                    myfile.write(lines[i]+"["+count+"]"+"\r\n");
                    count=0;
                }
                
                
                } catch (ArrayIndexOutOfBoundsException e){}
        }
                myfile.close();


    }
    
    
    /**
         * add strings to array of strings
         * @param array 
         *         array of strings
         * @param s 
         *         strings to be added
         * @return array of strings
         */
    public static String[] addToArray(String[] array, String s) {
String[] ans = new String[array.length+1];
System.arraycopy(array, 0, ans, 0, array.length);
ans[ans.length - 1] = s;
return ans;
}
    
    @Override
    public boolean process (File sourceFile, File targetFile){
        try {
            if (sourceFile.exists()){
                sort(targetFile, read(sourceFile));
                return true;
            }
        } catch (IOException ex) {}
        return false;
    }
    
    

    public static void main(String[] args) {
        DuplicateFinder d = new DuplicateFinderImpl();
        System.out.println(d.process(new File("D:\\test1.txt"), new File("D:\\test2.txt")));
    }
}
