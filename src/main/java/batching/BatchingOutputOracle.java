package batching;

import eu.cloudtm.autonomicManager.oracles.OutputOracle;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 29/03/14
 */
public class BatchingOutputOracle implements OutputOracle {

   private double selfDeliveryTime;

   public BatchingOutputOracle(double selfDeliveryTime) {
      this.selfDeliveryTime = selfDeliveryTime;
   }

   @Override
   public double throughput(int i) {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public double abortRate(int i) {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public double responseTime(int i) {
      if (i == 1) {
         return selfDeliveryTime;
      } else {
         return 0D;
      }
   }

   @Override
   public double getConfidenceThroughput(int i) {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public double getConfidenceAbortRate(int i) {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public double getConfidenceResponseTime(int i) {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public boolean equals(Object o) {
      return o instanceof BatchingOutputOracle && ((BatchingOutputOracle) o).selfDeliveryTime == this.selfDeliveryTime;
   }
}
