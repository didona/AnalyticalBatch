package batching;

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

   private double arrivalRate;
   private double batchingLevel;

   public BatchingInputOracle(double arrivalRate, double batchingLevel) {
      this.arrivalRate = arrivalRate;
      this.batchingLevel = batchingLevel;
   }

   public double getArrivalRate() {
      return arrivalRate;
   }

   public double getBatchingLevel() {
      return batchingLevel;
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
}
