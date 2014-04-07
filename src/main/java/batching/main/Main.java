package batching.main;

import batching.offline.BatchingAnalyticalOracle;
import batching.offline.BatchingInputOracle;
import batching.offline.BatchingPaoloAnalyticalOracle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 31/03/14
 */
public class Main {

   static double[] lambdas = new double[]{1, 2, 5, 10,
                                          100, 200, 500, 1000,
                                          1250, 1500, 1750, 2000,
                                          2250, 2500, 2750, 3000,
                                          3250, 3500, 3750, 4000,
                                          4250, 4500, 4750, 5000,
                                          5250, 5500, 5750, 6000,
                                          6250, 6500, 6750, 7000,
                                          7500, 8000, 8500, 9000,
                                          9500, 10000, 10500, 11000,
                                          11500, 12000, 12500, 13000};
   static double[] batches = new double[]{1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64};

   static String file = "data/batch_500.data";
   static String out = "data/batch_train.csv";


   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new FileReader(new File(file)));
      PrintWriter pw = new PrintWriter(new FileWriter(new File(out)));
      //pw.println(Param.AvgTxArrivalRate.getKey()+","+Param.AvgPrepareCommandSize+","+)
      String read;
      System.out.println("Arr" + " = " + "B" + " = " + "R = R " + " = " + "P" + " = PaolP");
      BatchingPaoloAnalyticalOracle.overrideDEFS(1500, 7000, 2, 1.79e-5);
      while ((read = br.readLine()) != null) {
         BatchingInputOracle bio = inputFromLine(read);
         double pred = (new BatchingAnalyticalOracle().forecast(bio)).responseTime(0);
         double paoloPred = (new BatchingPaoloAnalyticalOracle().forecast(bio)).responseTime(0);
         System.out.println(bio.getArrivalRate() + " = " + bio.getBatchingLevel() + " = " + bio.getRealSelfDeliveryTime() + " = " + bio.getRealSelfDeliveryTime() + " = " + pred + "  = " + paoloPred);
      }
   }


   private static BatchingInputOracle inputFromLine(String s) {
      String[] split = s.split(";");
      double l = Double.parseDouble(split[0]);
      double d = Integer.parseInt(split[1]);
      double r = Double.parseDouble(split[2]);
      return new BatchingInputOracle(l, d, r, -1);
   }
}
