import java.util.*;

public class Problem2_2 {
    double[][] error;
    double[] pointMean;
    double[] pointSqaureSum;
    int[] points;
    double[] minimumArray;
    Random random = new Random();

    double[][] generateRandomizedPoints(int x, int pts) {
        double[][] res = new double[pts][2];
        int i = 0;
        while(i < pts) {
            res[i++] = (new double[]{random.nextInt(x)+1, (random.nextBoolean() ? 1 : -1)*random.nextInt(10000)/100.0});
        }
        return res;
    }

    double error(int x, int y) {
        if(error[x][y] > 0) {
            return error[x][y];
        }
        double sumSquares = 0;
        double average = 0;
        int totalCount = 0;
        for(int i = x; i <= y; i++) {
            sumSquares += pointSqaureSum[i];
            totalCount += points[i];
            average += points[i]* pointMean[i];
        }
        if(totalCount == 0) {
            return 0;
        }
        double computedValue = sumSquares - (average/totalCount)*average;
        error[x][y] = computedValue;
        return error[x][y];
    }

    double computeMinimumError(int i, double penalty, int[] currentResult) {
        if(minimumArray[i] > 0) {
            return minimumArray[i];
        }
        if(i == 1) {
            minimumArray[i] = error(1, 1);
            currentResult[i] = 1;
            return minimumArray[i];
        }
        double errorNewInterval = computeMinimumError(i-1, penalty, currentResult) + penalty + error(i, i);
        double errorMergeInterval = minimumArray[i-1] + error(currentResult[i-1], i) - error(currentResult[i-1], i-1);
        if(errorNewInterval >= errorMergeInterval) {
            currentResult[i] = currentResult[i-1];
        } else{
            currentResult[i] = i;
        }
        minimumArray[i] = Math.min(errorNewInterval, errorMergeInterval);
        return minimumArray[i];
    }

    void initializeData(double[][] points) {
        Arrays.sort(points, Comparator.comparingInt(a -> (int)a[0]));
        this.points = new int[1+(int)points[points.length-1][0]];
        error = new double[this.points.length][this.points.length];
        pointMean = new double[this.points.length];
        pointSqaureSum = new double[this.points.length];

        for(double[] coordinates : points) {
            this.points[(int)coordinates[0]]++;
            pointMean[(int)coordinates[0]] += coordinates[1];
            pointSqaureSum[(int)coordinates[0]] += coordinates[1]*coordinates[1];
        }
        minimumArray = new double[this.points.length];
        int i = 1;
        while(i < pointMean.length) {
            if(this.points[i] > 0) {
                pointMean[i] /= this.points[i];
            }
            i++;
        }
    }

    List<List<Integer>> computeOptimaLIntervals(double[][] points, double penalty) {
        initializeData(points);
        List<List<Integer>> intervals =  new ArrayList<>();
        int[] computation = new int[(int)points[points.length-1][0]+1];
        computeMinimumError((int)points[points.length-1][0], penalty, computation);
        int i = computation.length-1;
        while(i >= 1) {
            intervals.add(0, Arrays.asList(computation[i], i));
            i = computation[i]-1;
        }
        return intervals;
    }

    public void initializeStaticInput(double[][] inputPoints) {
        inputPoints[0] = new double[]{1,4.5};
        inputPoints[1] = new double[]{1,2.9};
        inputPoints[2] = new double[]{2,-3.1};
        inputPoints[3] = new double[]{3,1.2};
        inputPoints[4] = new double[]{3,3.36};
        inputPoints[5] = new double[]{4,0.2};
        inputPoints[6] = new double[]{4,-52};
        inputPoints[7] = new double[]{4,-41.6};
        inputPoints[8] = new double[]{5,-6};
        inputPoints[9] = new double[]{5,28};
    }

    public static void main(String[] args) {
        Problem2_2 obj = new Problem2_2();
        double[][] inputPoints = new double[10][2];
        double penalty = 100;
        obj.initializeStaticInput(inputPoints);

        System.out.println("Memoization based Interval finding: ");
        System.out.println("Points: ");
        for(int i=0;i<inputPoints.length;i++) {
            System.out.print("For x: " + i + " y co ordinates: { ");
            for(int j=0;j<inputPoints[0].length;j++) {
                System.out.print(inputPoints[i][j] + " ");
            }
            System.out.print("}");
            System.out.println();
        }
        System.out.println("\nPenalty: " + penalty);
        System.out.println("\nInterval found are: ");
        List<List<Integer>> res =  obj.computeOptimaLIntervals(inputPoints, penalty);
        for(List<Integer> current: res) {
            System.out.println(current);
        }

//         For plotting the graph
//        PlotLineGraph plot = new PlotLineGraph("Interval Based Constant Based Approximation");
//        DefaultCategoryDataset d = new DefaultCategoryDataset();
//        for(int i = 10; i < 50000; i += 10) {
//            long startTime = System.nanoTime();
//            List<List<Integer>> res =  obj.computeOptimaLIntervals(obj.generateRandomizedPoints(100, i), penalty);
//            long endTime = System.nanoTime();
//            d.addValue((endTime-startTime), "", Integer.toString(i));
//        }
//        plot.plot(d, "Interval Based Constant Based Approximation", "Input size (number of points)", "Running time(in ns)");
    }
}
