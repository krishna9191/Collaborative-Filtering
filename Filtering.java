import java.io.IOException;
import java.util.Scanner;



public class Filtering {

	public static void main(String[] args) throws IOException
	{

		Scanner input = new Scanner(System.in);
		System.out.println("Enter the path of folder  which contains  test and training data sets Example: E:\\  ");
		String path = input.next();
		DataSet training=new DataSet(DataSet.getSize(path+"TrainingRatings.txt"));
		DataSet test=new DataSet(DataSet.getSize(path+"TestingRatings.txt"));
		training.getContent1(path+"TrainingRatings.txt");
	
		System.out.println(test.userId.length);
		System.out.println(training.userId.length);
		test.getContent(path+"TestingRatings.txt");
		
		training.findMean();
		double absolute=0,meanSquare=0;
		int i=0;
		for( i=0;i<training.movieId.length;i++)
			{
				int movieId=test.movieId[i];
				double userId=test.userId[i];
				double rating=test.rating[i];
				double predicted=training.getPredictedRating(movieId,userId);
				if(predicted<0)
					predicted*=-1;
				if(rating-predicted<0)
					absolute+= predicted-rating;
				else
					absolute+=rating-predicted;
				meanSquare+=Math.pow(rating-predicted,2);
			System.out.println(i+"   "+absolute+"   "+meanSquare);
			}

		System.out.println("absolute error = "+absolute);
		System.out.println("root mean Square error = "+Math.sqrt(meanSquare));
		absolute=absolute/i;
		meanSquare=meanSquare/i;
		System.out.println("absolute error = "+absolute);
		System.out.println("root mean Square error = "+Math.sqrt(meanSquare));
		
		input.close();
	}
	
}
