package batching.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Diego Didona
 * @email didona@gsd.inesc-id.pt
 * @since 07/04/14
 */
public class Cutter {
   private String in, out;
   double max;

   public Cutter(String in, String out, double max) {
      this.in = in;
      this.out = out;
      this.max = max;
   }


   public void cut() throws Exception {
      BufferedReader br = new BufferedReader(new FileReader(new File(in)));
      PrintWriter pw = new PrintWriter(new FileWriter(new File(out)));
      String read;
      while ((read = br.readLine()) != null) {
         pw.println(cutLine(read, max));
      }
      pw.close();
   }

   //The value is at the end
   private String cutLine(String read, double maxValue) {
      String[] split = read.split(";");
      double val = Double.parseDouble(split[split.length - 1]);
      if (val < maxValue)
         return read;
      val = maxValue;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < split.length - 1; i++) {
         sb.append(split[i]);
         sb.append(";");
      }
      sb.append(val);
      return sb.toString();

   }


   public static void main(String[] args) throws Exception {
      Cutter cut = new Cutter("data/batch_100.data", "data/batch_100.data", 100000);
      cut.cut();
   }
}
