import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class WCommonSubstring {

    HashMap<Character, Integer> weights = new HashMap<>();
    int[][] wMatrix;
    int penalty = -1;
    int maxValue = Integer.MIN_VALUE;
    int[] maxIndices;

    String computeRandomString(int stringLen) {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(random.nextInt(stringLen-6) + 6)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    String[] generateRandomStrings(int stringLen) {
        String[] inputStrings = new String[2];
        inputStrings[0] = computeRandomString(stringLen);
        inputStrings[1] = computeRandomString(stringLen);
        return inputStrings;
    }

    String getBestString(String s1, String s2) {
        String result = "";
        int i = maxIndices[0];
        int j = maxIndices[1];
        int totalDifference = wMatrix[i][j];
        while(totalDifference != 0) {
            result = s1.charAt(i) + result;
            if(s1.charAt(i) == s2.charAt(j)) totalDifference -= weights.get(s1.charAt(i));
            else totalDifference -= penalty;
            i--;
            j--;
        }
        return result;
    }

    void computeWACSubStringMatrixValues(String str1, String str2) {
        wMatrix = new int[str1.length()][str2.length()];
        for(int i=0;i<str1.length();i++) {
            char outer = str1.charAt(i);
            for(int j=0;j<str2.length();j++) {
                char inner = str2.charAt(j);
                if(i == 0 || j == 0) {
                    if(inner == outer) wMatrix[i][j] = weights.get(inner);
                    else wMatrix[i][j] = penalty;
                } else {
                    if(inner == outer) {
                        wMatrix[i][j] = Math.max(weights.get(inner), weights.get(inner) + wMatrix[i-1][j-1]);
                    }
                    else {
                        wMatrix[i][j] = Math.max(penalty, penalty + wMatrix[i-1][j-1]);
                    }
                }
                if(wMatrix[i][j] > maxValue) {
                    maxValue = wMatrix[i][j];
                    maxIndices[0] = i;
                    maxIndices[1] = j;
                }
            }
        }
//        System.out.println("Matrix formed: ");
//        for(int i=0;i<wMatrix.length;i++) {
//            System.out.println(Arrays.toString(wMatrix[i]));
//        }
//        System.out.println(wMatrix.length + " : " + wMatrix[0].length);
//        System.out.println(Arrays.toString(maxIndices));
    }

    void setWeights() {
        for(int i=1;i<=26;i++) {
            weights.put((char)(i+64), i);
        }
        System.out.println("Weights: " + weights);
        System.out.println("Penalty: " + penalty);
    }

    void initializeVariables() {
        wMatrix = null;
        maxIndices = new int[2];
        maxValue = Integer.MIN_VALUE;
    }

    public static void main(String[] args) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        WCommonSubstring wCommonSubstring = new WCommonSubstring();
        wCommonSubstring.setWeights();
        for(int i=40;i<=50;i++) {
            wCommonSubstring.initializeVariables();
            String[] inputStrings = wCommonSubstring.generateRandomStrings(i);
            String str1 = inputStrings[0];
            String str2 = inputStrings[1];
            long startTime = System.nanoTime();
//            String str1 = "ABCBCAACCB";
//            String str2 = "ABCAABCAA";
            System.out.println("Input Strings: " + str1 + " , " + str2);
            wCommonSubstring.computeWACSubStringMatrixValues(str1, str2);
            System.out.println("Weighted Approximate Common Substring: " + wCommonSubstring.getBestString(str1, str2));
//            wCommonSubstring.getBestString(str1, str2);
            long endTime = System.nanoTime();
            dataset.addValue(endTime - startTime, "", Integer.toString(str1.length() * str2.length()));
        }
        // Plotting the graph:
        PlotLineGraph plotLineGraph = new PlotLineGraph("Weighted Approximate Common Substring");
        plotLineGraph.plot(dataset, "Weighted Approx. Common Substring graph" , "Input size(string 1 size * string 2 size)", "Execution Time(in ns)");
    }
}



//    String str1 = "XZMAZYS";  XZMAZ for penalty = -1
//    String str2 = "XAMAZS"; MAZ for penalty = -13