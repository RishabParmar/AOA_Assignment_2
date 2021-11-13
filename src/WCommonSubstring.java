import java.util.Arrays;
import java.util.HashMap;

public class WCommonSubstring {

    HashMap<Character, Integer> weights = new HashMap<>();
    int[][] wMatrix;
    int penalty = -1;
    int maxValue = Integer.MIN_VALUE;
    int[] maxIndices = new int[2];

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
        for(int i=0;i<wMatrix.length;i++) {
            System.out.println(Arrays.toString(wMatrix[i]));
        }
        System.out.println(Arrays.toString(maxIndices));
    }

    void setWeights() {
        for(int i=1;i<=26;i++) {
            weights.put((char)(i+64), i);
        }
        System.out.println(weights);
    }

    public static void main(String[] args) {
        WCommonSubstring wCommonSubstring = new WCommonSubstring();
        wCommonSubstring.setWeights();
        String str1 = "ABCDDEEFGHI";
        String str2 = "AZCADMEEPGAI";
        wCommonSubstring.computeWACSubStringMatrixValues(str1, str2);
        System.out.println(wCommonSubstring.getBestString(str1, str2));
    }
}



//    String str1 = "XZMAZYS";  XZMAZ for penalty = -1
//    String str2 = "XAMAZS"; MAZ for penalty = -13