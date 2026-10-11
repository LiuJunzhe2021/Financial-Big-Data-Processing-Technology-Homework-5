import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class Main{
    public static void
main(String[] args) throws
Exception{
          if(args.length!=4)
    {
System.err.println("数据格式不正确");
System.exit(1);
    }
    //任务1，识别每只股票出现情况
    Job job1=Job.getInstance(new Configuration(),"stock relation");
    job1.setJarByClass(Main.class);
    MultipleInputs.addInputPath(job1,new Path(args[0]),TextInputFormat.class,AMapper.class);
    MultipleInputs.addInputPath(job1,new Path(args[1]),TextInputFormat.class,BMapper.class);
job1.setReducerClass(RelationReducer.class);
job1.setMapOutputKeyClass(Text.class);
job1.setMapOutputValueClass(Text.class);
job1.setOutputKeyClass(Text.class);
job1.setOutputValueClass(Text.class);
job1.setNumReduceTasks(1);
FileOutputFormat.setOutputPath(job1,new Path(args[2]));
if(!job1.waitForCompletion(true)){
    System.exit(1);
}
// 任务2
Job job2=Job.getInstance(new Configuration(),"stock counts");
job2.setJarByClass(Main.class);
job2.setInputFormatClass(TextInputFormat.class);
job2.setMapperClass(CountMapper.class);
job2.setReducerClass(CountReducer.class);
job2.setMapOutputKeyClass(Text.class);
job2.setMapOutputValueClass(Text.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);
        job2.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job2, new Path(args[2]));
        FileOutputFormat.setOutputPath(job2, new Path(args[3]));

        System.exit(job2.waitForCompletion(true) ? 0 : 1);
}
}
