import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;


public class DataSet {

	int[] movieId;
	double[]userId;
	double[] rating;
	HashMap<Integer,ArrayList<Integer>> movieMap=new HashMap<Integer,ArrayList<Integer>>();
	HashMap<Double,ArrayList<Integer>> userMap=new HashMap<Double,ArrayList<Integer>>();
	HashMap<Double,Double> mean = new HashMap<Double,Double>();
	
	
	public DataSet(int size) {

	this.movieId=new int[size];
	this.rating=new double[size];
	this.userId=new double[size];	
	
	}






	void getContent(String string) throws IOException {

		 BufferedReader br = new BufferedReader(new FileReader(string));
         String line = "";
         StringTokenizer s2 = null;
         int i=0;
         while ((line = br.readLine()) != null) {

             //use comma as token separator
             s2 = new StringTokenizer(line, ",");
        
 			if(s2.hasMoreTokens()) {
			 	this.movieId[i]=Integer.parseInt(s2.nextToken());
				this.userId[i]=Double.parseDouble(s2.nextToken());
				this.rating[i]=Double.parseDouble(s2.nextToken());
			
				
				i++;
           }

         
    	 }
         br.close();


	}
	
	void getContent1(String string) throws IOException {

		 BufferedReader br = new BufferedReader(new FileReader(string));
        String line = "";
        StringTokenizer s2 = null;
        int i=0;
        while ((line = br.readLine()) != null) {

            //use comma as token separator
            s2 = new StringTokenizer(line, ",");
       
			if(s2.hasMoreTokens()) {
			 	this.movieId[i]=Integer.parseInt(s2.nextToken());
				this.userId[i]=Double.parseDouble(s2.nextToken());
				this.rating[i]=Double.parseDouble(s2.nextToken());
				if(movieMap.containsKey(movieId[i]))
					{	
						ArrayList<Integer> m=movieMap.get(movieId[i]);
						m.add(i);
					}
				else
					{	
						ArrayList<Integer> m=new ArrayList<Integer>();
						m.add(i);
						movieMap.put(this.movieId[i], m);
					}
				
				if(userMap.containsKey(userId[i]))
				{	
					ArrayList<Integer> m=userMap.get(userId[i]);
					m.add(i);
				}
			else
				{	
					ArrayList<Integer> m=new ArrayList<Integer>();
					m.add(i);
					userMap.put(this.userId[i], m);
				}
				
				i++;
          }

        
   	 }
        br.close();


	}
	
	public static int getSize(String string) throws IOException {
		int size=0;
		 BufferedReader br = new BufferedReader(new FileReader(string));
         String line = "";
         while ((line = br.readLine()) != null) 
        	 size++;

         br.close();
		return size;
		
	}



	public double correlation(double userId1, double userId2) 
	{
		double c=0, num=0,termA=0,termI=0,denom1=0,denom2=0;
		Set<Integer> movieSet=  movieMap.keySet();
		Iterator<Integer> itr=movieSet.iterator();
		while(itr.hasNext())
			{	int movie=(int) itr.next();
				termA =( getValue(userId1,movie) -mean.get(userId1));
				
				termI=(getValue(userId2,movie)-mean.get(userId2));
				
				num+=(termA*termI);
				denom1 += termA*termA;
				denom2 +=termI*termI;
				
			}
		
		c=num/(Math.sqrt(denom2*denom1));
		if(denom1*denom2 ==0)
			{return 0;
				
			}
		return c;
	}


	private Double getValue(double userId1, int movie) {
		double value=mean.get(userId1);
		ArrayList<Integer> userLikes= userMap.get(userId1);
		Iterator<Integer> itr1=userLikes.iterator();
		while(itr1.hasNext())
		{
			int index=(int) itr1.next();
			if(movieId[index]==movie)
				{	value = rating[index];
					break;
				}
		}
		return value;
	}

	public double getPredictedRating(int movieId2, double userId2) {
		double prediction=0,sum=0;
		int i=0;
		Double k=0.0;//getK(userId2);
		//System.out.print(k+"   ");
		ArrayList<Integer> userSet=  movieMap.get(movieId2);
		Iterator<Integer> itr=userSet.iterator();
		while(itr.hasNext())
		{
			int index = (int) itr.next();
			double diff=this.getValue(userId[index], movieId2)-mean.get(userId[index]);
			
			double c=correlation(userId[index],userId2);
			k+=c;
			i++;
			sum += (c*diff);
			
		}
		k=k/i;
		prediction=mean.get(userId2)+k*sum;
		return prediction;
	}

	public void findMean() {
		Set<Double> userKeySet= (Set<Double>) userMap.keySet();
		Iterator<Double> itr=userKeySet.iterator();
		while(itr.hasNext())
			{
				Double key=itr.next();
				ArrayList<Integer> userLikes=userMap.get(key);
			
			  Iterator<Integer> itr1=userLikes.iterator();
				double m=0;
				int i=0;
				while(itr1.hasNext())
				{
					int index = (int) itr1.next();
					m +=rating[index];
					i++;
				}
				mean.put(key, m/i);
			}		
		
		
	}

	public Double getK(double userId) {
		Set<Double> userKeySet= (Set<Double>) userMap.keySet();
		Iterator<Double> itr=userKeySet.iterator();
		Double userId2=0.0;
		int i=1;
		Double k=0.0;
		while(itr.hasNext())
			{	userId2=itr.next();
				k+=correlation(userId,userId2);
				i++;
			}
		k=k/i;
		return k;
	}

}
