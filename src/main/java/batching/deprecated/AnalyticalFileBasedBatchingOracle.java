package batching.deprecated;

import batching.BatchingOutputOracle;
import batching.offline.BatchingInputOracle;
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

   private final static String file = "data/batch_500.data";
   private Map<BatchingInputOracle, BatchingOutputOracle> map = new HashMap<BatchingInputOracle, BatchingOutputOracle>();
   private final static String sep = ",";
   private final static int L = 0;
   private final static int B = 1;
   private final static int P = 2;
   private final static int R = 3;

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
      String[] split = s.split(sep);
      BatchingInputOracle bio = new BatchingInputOracle(Double.parseDouble(split[L]), Double.parseDouble(split[B]), Double.parseDouble(split[R]), 0);
      BatchingOutputOracle boo = new BatchingOutputOracle(Double.parseDouble(split[P]));
      map.put(bio, boo);
   }

   @Override
   public OutputOracle forecast(InputOracle inputOracle) throws OracleException {
      BatchingInputOracle bio = (BatchingInputOracle) inputOracle;
      return map.get(bio);
   }
}
