package batching.offline;

import batching.BatchingOutputOracle;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 29/03/14
 */
public class BatchingAnalyticalOfflineOracle extends BatchingAnalyticalOracle {

   @Override
   public OutputOracle forecast(InputOracle input) throws OracleException {
      return new BatchingOutputOracle(((BatchingInputOracle) input).getRealSelfDeliveryTime());
   }
}
