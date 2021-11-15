import java.util.*;

public class Problem2 {

    ArrayList<Float>[] points;

    void generateInput(int iterations) {
        float[] ySquared = new float[iterations+1];
        float[] yMean = new float[iterations+1];
        float max = 100;
        float min = -100;
        float range = (max - min) + min;
        points = new ArrayList[iterations+1];
        Random random = new Random();
        for(int i=1;i<=iterations;i++) {
            points[i] = new ArrayList<>();
            int innerIterations = random.nextInt(iterations - 1) + 1;
            for(int j=0;j<innerIterations;j++) {
                int choice = (int) (Math.random() * 2);
                float y = random.nextFloat() * range;
                float randomizedY = choice == 1? y:-y;
                ySquared[i] += Math.pow(randomizedY, 2);
                yMean[i] += randomizedY;
                points[i].add(randomizedY);
            }
            yMean[i] /= points[i].size();
            System.out.println(i + " : " + points[i]);
        }
        System.out.println("Mean Y: " + Arrays.toString(yMean));
        System.out.println("Squared Y: " + Arrays.toString(ySquared));

        // Error matrix:
        float[][] error = new float[points.length][points.length];
        for(int i=1;i<points.length;i++) {
            for(int j=1;j<=i;j++) {
                if(i == j) {
                    error[i][j] = (float) (ySquared[i] - (points[i].size() * Math.pow(yMean[i], 2)));
                } else {
                    float totalYSquared = 0f;
                    float totalYMean = 0f;
                    int totalNumberOfPoints = 0;
                    for(int k=j;k<=i;k++) {
                        totalYSquared += ySquared[k];
                        totalYMean += points[k].size() * yMean[k];
                        totalNumberOfPoints += points[k].size();
                    }
                    totalYMean /= totalNumberOfPoints;
                    error[i][j] = (float) (totalYSquared - (totalNumberOfPoints * Math.pow(totalYMean, 2)));
                }
            }
        }
        System.out.println(Arrays.deepToString(error));
        float[] dp = new float[points.length];
        float penalty = 10f;
        for(int i=1;i<points.length;i++) {
            dp[i] = Integer.MAX_VALUE;
            for(int j=1;j<=i;j++) {
                dp[i] = Math.min(dp[i], penalty + dp[i-1] + error[i][j]);
            }
        }
        System.out.println(Arrays.toString(dp));
    }

    public static void main(String[] args) {
        Problem2 obj = new Problem2();
        obj.generateInput(10);
    }
}
