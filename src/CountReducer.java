import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CountReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text index, Iterable<Text> relations, Context context)
            throws IOException, InterruptedException {

        int both = 0;
        int aOnly = 0;
        int bOnly = 0;

        for (Text relation : relations) {
            String value = relation.toString();

            if ("BOTH".equals(value)) {
                both++;
            } else if ("A_ONLY".equals(value)) {
                aOnly++;
            } else if ("B_ONLY".equals(value)) {
                bOnly++;
            }
        }

        int union = both + aOnly + bOnly;

        context.write(
            index,
            new Text(both + "\t" + aOnly + "\t" + bOnly + "\t" + union)
        );
    }
}