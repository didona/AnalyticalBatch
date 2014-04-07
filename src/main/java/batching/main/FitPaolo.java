package batching.main;

import batching.BatchingOutputOracle;
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
 * @since 01/04/14
 */
public class FitPaolo {
   private final static double seqMin = 1000;
   private final static double seqMax = 10000;
   private final static double seqS = 500;
   private final static double minBookingRatio = 2;
   private final static double maxBookingRatio = 10;
   private final static double bookingRatioS = 1;
   private final static double cMin = 1;
   private final static double cMax = 10;
   private final static double cS = 1;
   private final static double netMin = 0;
   private final static double netMax = 50000;
   private final static double netS = 500;

   private final static boolean cento = false;
   private final static String file = cento ? "data/batch_100.data" : "data/batch_500.data";


   public static void main(String[] args) throws Exception {
      PrintWriter pw = new PrintWriter(new FileWriter(new File(cento ? "data/out_100.csv" : "data/out_500.csv")));
      pw.println("seq,book,c,net,mape,rmse");
      if (cento) {
         BatchingPaoloAnalyticalOracle.cutOff100();
      } else {
         BatchingPaoloAnalyticalOracle.cutOff500();
      }
      for (double s = seqMin; s <= seqMax; s += seqS) {
         for (double b = minBookingRatio; b <= maxBookingRatio; b += bookingRatioS) {
            for (double c = cMin; c <= cMax; c += cS) {
               for (double n = netMin; n <= netMax; n += netS) {
                  double book = 1D / (s * b);
                  double err[] = err(n, s, book, c);
                  pw.println(s + "," + book + "," + c + "," + n + "," + err[0] + "," + err[1]);
               }
            }
         }
      }
      pw.close();
   }

   private static double err(double r, double p) {

      return Math.abs(r - p) / r;
   }

   private static double[] err(double net, double seq, double b, double c) throws Exception {
      System.out.println(net + " " + seq + " " + b + " " + c);
      BatchingPaoloAnalyticalOracle.overrideDEFS(net, seq, c, b);
      BufferedReader br = new BufferedReader(new FileReader(new File(file)));
      String read;
      double err[] = new double[]{0, 0};
      int count = 0;
      while ((read = br.readLine()) != null) {
         BatchingInputOracle bio = inputFromLine(read);
         BatchingPaoloAnalyticalOracle paolo = new BatchingPaoloAnalyticalOracle();
         BatchingOutputOracle boo = (BatchingOutputOracle) paolo.forecast(bio);
         double pred = boo.responseTime(0);
         count++;
         err[0] += err(bio.getRealSelfDeliveryTime(), pred);    //for mape
         err[1] += Math.pow(bio.getRealSelfDeliveryTime() - pred, 2);      //for rmse
         //System.out.println(bio.getArrivalRate() + " = " + bio.getBatchingLevel() + " = " + bio.getRealSelfDeliveryTime() + " = " + bio.getRealSelfDeliveryTime() + " = " + pred);
      }
      err[0] = err[0] / count;
      err[1] = Math.sqrt(err[1] / count);

      return err;
   }


   private static BatchingInputOracle inputFromLine(String s) {
      String[] split = s.split(";");
      double l = Double.parseDouble(split[0]);
      double d = Integer.parseInt(split[1]);
      double r = Double.parseDouble(split[2]);
      return new BatchingInputOracle(l, d, r, -1);
   }
}
