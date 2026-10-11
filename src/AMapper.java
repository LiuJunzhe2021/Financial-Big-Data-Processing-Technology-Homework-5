import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AMapper extends
Mapper< LongWritable,Text,Text,Text>{
    @Override
    protected void
    map(LongWritable offset,Text line, Context context)
    throws IOException,
    InterruptedException{
        String[] fields=line.toString().trim().split("\\s+");
        if(fields.length !=2){
            return;
        }
        String index=fields[0];
        String stock=fields[1];
        context.write(
            new Text (index+"\t"+stock),
            new Text ("A")
        );
    }
}