import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
public class RelationReducer extends Reducer<Text,Text,Text,Text>{
    @Override
    protected void reduce(Text key,Iterable<Text> values,Context context)
    throws IOException,
    InterruptedException{
        boolean inA=false;
        boolean inB=false;

        for(Text value : values){
            if("A".equals(value.toString())){
                inA=true;
            }
            else if("B".equals(value.toString())){
                inB=true;
            }
        }

        String relation;
        if(inA && inB){
            relation="BOTH";
        }
        else if (inA){
            relation="A_ONLY";
        }
        else{
            relation="B_ONLY";
        }
        context.write(key,new Text(relation));
    }
}