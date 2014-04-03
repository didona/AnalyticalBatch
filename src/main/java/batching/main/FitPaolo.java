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
   private final static double seqMin = 3000;
   private final static double seqMax = 8000;
   private final static double seqS = 1000;
   private final static double minBookingRatio = 2;
   private final static double maxBookingRatio = 10;
   private final static double bookingRatioS = 1;
   private final static double cMin = 1;
   private final static double cMax = 10;
   private final static double cS = 1;
   private final static double netMin = 0;
   private final static double netMax = 10000;
   private final static double netS = 500;
   static String file = "data/batch.data";


   public static void main(String[] args) throws Exception {
      PrintWriter pw = new PrintWriter(new FileWriter(new File("data/out.csv")));
      pw.println("seq,book,c,net,err");
      for (double s = seqMin; s <= seqMax; s += seqS) {
         for (double b = minBookingRatio; b <= maxBookingRatio; b += bookingRatioS) {
            for (double c = cMin; c <= cMax; c += cS) {
               for (double n = netMin; n <= netMax; n += netS) {
                  double book = 1D / (s * b);
                  pw.println(s + "," + book + "," + c + "," + n + "," + err(n, s, book, c));
               }
            }
         }
      }
      pw.close();
   }

   private static double relErr(double r, double p) {
      return Math.abs(r - p) / r;
   }

   private static double err(double net, double seq, double b, double c) throws Exception {
      System.out.println(net + " " + seq + " " + b + " " + c);
      BatchingPaoloAnalyticalOracle.overrideDEFS(net, seq, c, b);
      BufferedReader br = new BufferedReader(new FileReader(new File(file)));
      String read;
      double err = 0;
      int count = 0;
      while ((read = br.readLine()) != null) {
         BatchingInputOracle bio = inputFromLine(read);
         BatchingPaoloAnalyticalOracle paolo = new BatchingPaoloAnalyticalOracle();
         BatchingOutputOracle boo = (BatchingOutputOracle) paolo.forecast(bio);
         double pred = boo.responseTime(0);
         count++;
         err += relErr(bio.getRealSelfDeliveryTime(), pred);
         //System.out.println(bio.getArrivalRate() + " = " + bio.getBatchingLevel() + " = " + bio.getRealSelfDeliveryTime() + " = " + bio.getRealSelfDeliveryTime() + " = " + pred + "  = " + paoloPred);
      }
      return err / count;
   }


   private static BatchingInputOracle inputFromLine(String s) {
      String[] split = s.split(";");
      double l = Double.parseDouble(split[0]);
      double d = Integer.parseInt(split[1]);
      double r = Double.parseDouble(split[2]);
      return new BatchingInputOracle(l, d, r, -1);
   }
}
