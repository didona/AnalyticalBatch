package batching.deprecated;

import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 29/03/14
 */
public class AnalyticalBatchingMetaOracle implements Oracle {

   private final static AnalyticalFileBasedBatchingOracle realModel = new AnalyticalFileBasedBatchingOracle();

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {
      return realModel.forecast(input);
   }
}
