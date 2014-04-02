package batching.offline;

import batching.BatchingOutputOracle;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 31/03/14
 */
public class BatchingAnalyticalOracle implements Oracle {

   private final static double net = 900D;  //0  in the paper  (not present)
   private final static double alfa = 1D; //Not in the paper
   private final static double alfa_2 = 1.5D;// 1.5D; //1 in the paper   (not present)
   private final static double s = 4800D; // T_1st in the paper
   private final static double book = 1D / (s * 10.5);  // 1/T_add in the paper
   private final static double c = 2; //It is just "2" in the paper

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {
      if (input instanceof BatchingInputOracle) {
         BatchingInputOracle bio = (BatchingInputOracle) input;
         return new BatchingOutputOracle(analyticalSelfDelivery(bio.getArrivalRate(), bio.getBatchingLevel()));
      } else {
         double lambda = (Double) (input.getParam(Param.AvgNumPutsBySuccessfulLocalTx));
         double batchingLevel = (Double) input.getParam(Param.AvgPrepareCommandSize);
         return new BatchingOutputOracle(analyticalSelfDelivery(lambda, batchingLevel));
      }
   }

   protected double analyticalSelfDelivery(double lambda, double b) {

      return fb3(net, s, lambda, alfa, alfa_2, book, c, b);
   }

   private double paper(double lambda, double b) {
      double _lambda = lambda / 1000000D;//usec
      double arrival = _lambda / b;
      double service = s + (b - 1.0D) * 1D / book + (b - 1.0D) / (2.0D * _lambda);
      //System.out.println("Mex/sec " + lambda + " mex/batch " + b + " batch/usec " + arrival + " usec/batch " + 1D / service);
      //System.out.println("arrival " + arrival + " 1/service " + 1.0D / service);
      return 1.0D / ((1.0D / service) - arrival);
   }


   private double fb3(double net_, double seqRate_, double arrRate_, double alfa_, double alfa2_, double bookKeeping_, double c_, double b_) {
      double denDen = (1D / seqRate_) + (b_ - 1D) / (c * arrRate_) + Math.pow(bookKeeping_ * (b_ - 1D), alfa2_);
      double den = Math.pow(1D / denDen - (arrRate_ / b_), alfa_);
      return net + 1e6 / den;
   }
}
