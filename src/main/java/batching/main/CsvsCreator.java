package batching.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 02/04/14
 * <p/>
 * This class creates a set of csvs so that they are compatible with the ensemble framework
 */
public class CsvsCreator {


   private final String outBaseFolder = "data";
   private String input = "data/input_nsec_100.csv";
   String header;

   public static void main(String[] args) throws Exception {

      new CsvsCreator().dumpCsvs();
   }

   /**
    * For each line in the csv, creates a folder with a one-line csv
    *
    * @throws Exception
    */
   private void dumpCsvs() throws Exception {
      BufferedReader br = new BufferedReader(new FileReader(new File(input)));
      String read;
      int i = 1000;
      while ((read = br.readLine()) != null) {
         createNewFile(read, Integer.toString(i++), "batch");
      }
      br.close();
   }

   /**
    * Creates a new one-line batch csv
    *
    * @param content
    * @param folderName
    * @param csvName
    * @throws Exception
    */
   private void createNewFile(String content, String folderName, String csvName) throws Exception {
      String folder = outBaseFolder + "/" + folderName;
      File f = new File(folder);
      System.out.println("Checking " + f);
      if (!f.exists()) {
         f.mkdir();
         System.out.println("Creating " + f);
      }
      File out = new File(folder + "/" + csvName + ".csv");
      System.out.println("Output " + out.getAbsolutePath());
      PrintWriter pw = new PrintWriter(new FileWriter(out));
      pw.println(header());
      pw.print(content(content));
      System.out.println("Writing " + content);
      pw.close();

   }

   /**
    * The header. Note that we prefer to use only "plain" params that do not get divided, multiplied or anything by the
    * radargun parser. This is a massive hack
    *
    * @return
    */
   private String header() {
      String header = "SLAVE_INDEX,avgNumPutsBySuccessfulLocalTx,avgPrepareCommandSize,localUpdateTxTotalResponseTime,avgGetsPerWrTransaction,MEM_USAGE,avgGetsPerROTransaction,localReadOnlyTxLocalServiceTime,localUpdateTxLocalServiceTime,avgClusteredGetCommandReplySize,percentageWriteTransactions,NUM_KEYS,replicationDegree,throughput,readOnlyTxTotalResponseTime,abortRate,localReadOnlyTxLocalResponseTime,percentageSuccessWriteTransactions";
     /* String slave = "SLAVE_INDEX";
      String lambda = Param.AvgNumPutsBySuccessfulLocalTx.getKey();
      String batch = Param.AvgPrepareCommandSize.getKey();
      String latency = Param.AvgGetsPerWrTransaction.getKey();
      return slave + "," + lambda + "," + batch + "," + latency;
      */
      return header;
   }

   private String content(String payload) {
      String content = Integer.toString(0) + ",".concat(payload);
      long otherLength = header().split(",").length - content.split(",").length;
      StringBuilder sb = new StringBuilder(content);

      for (int i = 0; i < otherLength; i++) {
         sb.append(",0");
      }
      return sb.toString();
   }
}
