package kdg.chono;

import java.io.BufferedReader;
import java.io.IOException;

public class Stage {
	public static final String delimiter = ",";
	static int arrayint[][] = new int[20][15];
	public static int[][] Stageset(BufferedReader br) {

		try {	         
	         String line = "";
	         String[] tempArr;
	         
	         for(int i=0;i<20; i++) {
	        	 line = br.readLine();
	            tempArr = line.split(delimiter);
	               for(int j=0;j<15;j++) {
	            	   arrayint[i][j] = Integer.parseInt(tempArr[j]);
	               }

	         }
	         br.close();
	         } catch(IOException ioe) {
	            ioe.printStackTrace();
	         }
	return arrayint; 
	}
	
}

