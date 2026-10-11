import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable offset, Text line, Context context)
            throws IOException, InterruptedException {

        String[] fields = line.toString().trim().split("\\s+");
        if (fields.length != 3) {
            return;
        }

        String index = fields[0];
        String relation = fields[2];

        context.write(new Text(index), new Text(relation));
    }
}