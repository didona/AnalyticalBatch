package batching;

import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 29/03/14
 */
public class AnalyticalFileBasedBatchingOracle implements Oracle {

   private final static String file = "data/batch.data";
   private Map<BatchingInputOracle, BatchingOutputOracle> map = new HashMap<BatchingInputOracle, BatchingOutputOracle>();

   public AnalyticalFileBasedBatchingOracle() {
      try {
         BufferedReader br = new BufferedReader(new FileReader(new File(file)));
         String read;
         while ((read = br.readLine()) != null) {
            parseAndAdd(read);
         }
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(-1);
      }
   }

   private void parseAndAdd(String s) {
      //TODO parse the string, create In and Out, put it into the map
   }

   @Override
   public OutputOracle forecast(InputOracle inputOracle) throws OracleException {
      BatchingInputOracle bio = (BatchingInputOracle) inputOracle;
   }
}
