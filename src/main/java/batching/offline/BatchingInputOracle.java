package batching.offline;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 29/03/14
 */
public class BatchingInputOracle implements InputOracle {

   private double arrivalRate = -1;
   private double batchingLevel = -1;
   private double realSelfDeliveryTime = -1;
   private double predSelfDeliveryTime = -1;

   public BatchingInputOracle(double arrivalRate, double batchingLevel, double realSelfDeliveryTime, double predSelfDeliveryTime) {
      this.arrivalRate = arrivalRate;
      this.batchingLevel = batchingLevel;
      this.realSelfDeliveryTime = realSelfDeliveryTime;
      this.predSelfDeliveryTime = predSelfDeliveryTime;
   }

   public BatchingInputOracle(double l, double b) {
      this.arrivalRate = l;
      this.batchingLevel = b;
   }

   public double getArrivalRate() {
      return arrivalRate;
   }

   public double getBatchingLevel() {
      return batchingLevel;
   }

   public double getRealSelfDeliveryTime() {
      return realSelfDeliveryTime;
   }

   public double getPredSelfDeliveryTime() {
      return predSelfDeliveryTime;
   }

   @Override
   public Object getParam(Param param) {
      if (true) throw new UnsupportedOperationException("Not implemented");
      switch (param) {
         case AvgTxArrivalRate:
            return arrivalRate;
         case AvgPrepareCommandSize:
            return batchingLevel;
         default:
            throw new IllegalArgumentException(param + " not found");
      }
   }

   @Override
   public Object getEvaluatedParam(EvaluatedParam evaluatedParam) {
      throw new UnsupportedOperationException("Not implemented");
   }

   @Override
   public Object getForecastParam(ForecastParam forecastParam) {
      throw new UnsupportedOperationException("Not implemented");
   }

   @Override
   public boolean equals(Object o) {
      return o instanceof BatchingInputOracle && ((BatchingInputOracle) o).batchingLevel == this.batchingLevel &&
            ((BatchingInputOracle) o).arrivalRate == this.arrivalRate &&
            ((BatchingInputOracle) o).realSelfDeliveryTime == this.realSelfDeliveryTime;
   }
}
