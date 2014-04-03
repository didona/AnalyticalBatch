package batching.offline;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 01/04/14
 */
public class BatchingPaoloAnalyticalOracle extends BatchingAnalyticalOracle {

   private double num1;
   private double num2;
   private double num3;
   private double num4;
   private double num5;
   private double num6;

   private double net;
   private double seqRate;
   private double c;
   private double bookKeepRate;
   private double minRateToStartBatching;
   private double maxThroughput;

   private static double DEF_NET = 200;
   private static double DEF_SEQ_RATE = 5000;
   private static double DEF_C = 2;
   private static double DEF_BOOK_RATE = 1.0D / 26000D;


   private static final int MAX_BATCHING_VALUE = 128;
   private static final int MAX_RATE_VALUE = 20000;

   public static void paperValues() {
      overrideDEFS(200, 5000, 2, 1D / 26000D);
   }

   public static void optimalValues() {
      overrideDEFS(2500, 8000, 3, 4.16e-5);
   }

   public static void mediumValues() {
      overrideDEFS(2000, 7000, 2, 1.42e-5);
   }


   public static void overrideDEFS(double net, double seq, double c, double book) {
      DEF_NET = net;
      DEF_SEQ_RATE = seq;
      DEF_C = c;
      DEF_BOOK_RATE = book;
   }

   public BatchingPaoloAnalyticalOracle() {
      net = DEF_NET;
      seqRate = DEF_SEQ_RATE;
      c = DEF_C;
      bookKeepRate = DEF_BOOK_RATE;

      precomputeNumQuantities();

   }

   @Override
   protected double analyticalSelfDelivery(double lambda, double b) {
      return getVal(lambda, (int) b);
   }

   private void precomputeNumQuantities() {
      minRateToStartBatching = computeMinRateToBatch();
      maxThroughput = computeMaxThroughput();

      num1 = c * (-1.0D + bookKeepRate * seqRate);
      num2 = bookKeepRate * c * seqRate;
      num3 = seqRate - c * seqRate;
      num4 = bookKeepRate * c;
      num5 = Math.pow(seqRate, 2.0D);
      num6 = 1.0D + -c;
   }

   /**
    * Return the minimum arrival rate after which it is convenient to start batching messages
    *
    * @return
    */
   private double computeMinRateToBatch() {
      return (bookKeepRate * seqRate * seqRate) / 2.0D
            +
            0.5D * Math.sqrt(
                  (4.0D * seqRate * seqRate + bookKeepRate * bookKeepRate * c * Math.pow(seqRate, 4))
                        /
                        c
            );
   }

   /**
    * Return the max throughput according to the model
    *
    * @return
    */
   private double computeMaxThroughput() {
      return Math.max(seqRate, (-1.0D + c) / (bookKeepRate * c));
   }

   public int getOptimalBatchingValue(double rate) {

      // it is checked whether it's above the 95% of the max_throughput. At max throughput the max batching factor in fact is infinite
      if (rate > maxThroughput * .99D)
         return MAX_BATCHING_VALUE;

      if (rate < minRateToStartBatching)
         return 1;


      return Math.min(
            (int) Math.ceil(
                  (seqRate + rate * num1)
                        /
                        (num3 + rate * num2)
                        +
                        Math.sqrt(
                              (c * Math.pow(seqRate + rate * num1, 2.0D))
                                    /
                                    (
                                          (1.0D + rate * num4)
                                                *
                                                Math.pow((num6 + rate * num4), 2.0D)
                                                *
                                                num5
                                    )
                        )
            )
            ,
            MAX_BATCHING_VALUE
      );

           /*
                   num1= c * (-1.0D + bookKeepRate * seqRate);
                   num2= bookKeepRate * c  * seqRate;
                   num3=seqRate -  c  * seqRate;
                   num4=bookKeepRate *c;
                   num5=Math.pow(seqRate, 2.0D);
                   num6= 1.0D +  -c ;
             return Math.min(
                   (int) Math.ceil(
                               ( seqRate + rate * c * (-1.0D + bookKeepRate * seqRate) )
                                       /
                                ( ( 1.0D + (-1.0D + rate*bookKeepRate) * c ) * seqRate )
                                    +
                                Math.sqrt(
                                            ( c * Math.pow( seqRate + rate * c *  (-1.0D + bookKeepRate*seqRate) , 2.0D ) )
                                                    /
                                            (
                                                    (1.0D + rate * bookKeepRate *c )
                                                        *
                                                    Math.pow( ( 1.0D + ( -1.0D + rate * bookKeepRate ) * c) , 2.0D)
                                                        *
                                                    Math.pow(seqRate, 2.0D)
                                            )
                                        )
                        )
                    ,
                    MAX_BATCHING_VALUE
                   );
   */
   }


   /**
    * Returns the value of the desired using bilinear interpolation if the (rate,batch) coordinates fall into the known
    * region. Otherwise it returns the value of the closer cell in the region, without attempting extrapolation.
    *
    * @param rate
    * @param batch
    */
   public double getVal(double rate, int batch) {
      double ret;
      if (rate <= 0)
         ret = 0;
      if (batch < 1)
         ret = 0;
      if (1.0D / (1.0D / seqRate + ((double) batch - 1.0D) / (c * rate) + (bookKeepRate) * ((double) batch - 1.0D)) - rate / (double) batch < 1.0D)
         ret = 500000;
      ret = net + 1000000.0D / (1.0D / (1.0D / seqRate + ((double) batch - 1.0D) / (c * rate) + (bookKeepRate) * ((double) batch - 1.0D)) - rate / (double) batch);

      if (ret > 500000 || ret < 0)
         ret = 500000;

      return ret;
   }


   public void dumpGrid() {
      try {
         // Create file
         FileWriter fstream = new FileWriter("analyticalDataGrid");
         BufferedWriter out = new BufferedWriter(fstream);
         for (int rate = 1; rate < MAX_RATE_VALUE; rate += 1000)
            for (int batch = 1; batch < MAX_BATCHING_VALUE; batch += batch)
               //out.write(rate+" "+batch+" "+getVal(rate,batch)+"\n");
               System.out.print(rate + " " + batch + " " + getVal(rate, batch) + "\n");
         //Close the output stream
         out.close();
      } catch (Exception e) {//Catch exception if any
         System.err.println("Error: " + e.getMessage());
      }

      //System.out.println("optimal["+msgRates[rate]+"]:"+getOptimalBatchingValue(rate));
   }

   public void dumpOptimal() {
      for (int rate = 1000; rate <= 14000; rate += 1000)
         System.out.println(rate + " " + getOptimalBatchingValue(rate));
      //System.out.println(getOptimalBatchingValue(rate));
   }

   public void stressTest() {
      long t = System.nanoTime();
      for (int rate = 1; rate <= 14000; rate += 1)
         getOptimalBatchingValue(rate);
      t = System.nanoTime() - t;
      System.out.println(t / 14000);
   }

}
