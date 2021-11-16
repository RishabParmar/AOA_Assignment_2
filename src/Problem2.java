import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

public class Problem2 {

    ArrayList<Float>[] points;
    float[] ySquared;
    float[] yMean;
    float[][] error;
    int totalPoints;

    void calculatePartitions() {
        float[] dp = new float[points.length];
        float penalty = 500f;
        int[] arr = new int[points.length+1];
        for(int i=1, index = i;i<points.length;i++) {
            dp[i] = Integer.MAX_VALUE;
            for(int j=1;j<=i;j++) {
                float currentScore = penalty + dp[j-1] + error[i][j];
                if(dp[i] > currentScore) {
                    index = j;
                    dp[i] = currentScore;
                }
            }
            arr[i] = index;
        }
        List<List<Integer>> result = new ArrayList<>();
        for(int i = arr.length-2; i >= 1;) {
            result.add(0, Arrays.asList(arr[i], i));
            i = arr[i]-1;
        }
        // For printing the final output: Partitions/ Intervals:
//        for(List<Integer> i : result) System.out.println(i);
    }

    void computeError() {
        // Error matrix:
        error = new float[points.length][points.length];
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
        // System.out.println(Arrays.deepToString(error));
    }

    void generateInput(int iterations) {
        totalPoints = 0;
        ySquared = new float[iterations+1];
        yMean = new float[iterations+1];
        float max = 100;
        float min = -100;
        float range = (max - min) + min;
        points = new ArrayList[iterations+1];
        Random random = new Random();
        for(int i=1;i<=iterations;i++) {
            points[i] = new ArrayList<>();
            int innerIterations = random.nextInt(iterations - 1) + 1;
            totalPoints += innerIterations;
            for(int j=0;j<innerIterations;j++) {
                int choice = (int) (Math.random() * 2);
                float y = random.nextFloat() * range;
                float randomizedY = choice == 1? y:-y;
                ySquared[i] += Math.pow(randomizedY, 2);
                yMean[i] += randomizedY;
                points[i].add(randomizedY);
            }
            yMean[i] /= points[i].size();
//            System.out.println(i + " : " + points[i]);
        }
//        System.out.println("Total points: " + totalPoints);
//        System.out.println("Mean Y: " + Arrays.toString(yMean));
//        System.out.println("Squared Y: " + Arrays.toString(ySquared));
    }

    public static void main(String[] args) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Problem2 obj = new Problem2();
        for(int i = 5;i <= 750;i++) {
            obj.generateInput(i);
            long startTime = System.nanoTime();
            obj.computeError();
            obj.calculatePartitions();
            long endTime = System.nanoTime();
            dataset.addValue(endTime - startTime, "", Integer.toString(obj.totalPoints));
        }
        // Plotting the graph:
        PlotLineGraph plotLineGraph = new PlotLineGraph("Interval Based Constant Best Approximation");
        plotLineGraph.plot(dataset, "Interval Based Constant Best Approximation graph" , "Number of Points (Input size)", "Execution Time(in ns)");
    }
}
