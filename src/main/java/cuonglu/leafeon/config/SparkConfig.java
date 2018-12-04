package cuonglu.leafeon.config;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class SparkConfig {

    @Bean
    public SparkConf sparkConf() {

        SparkConf sparkConf = new SparkConf()
                .setAppName("Leafeon")
                .setMaster("local")
                .setJars(new String[]{
                        "./lib/leafeon-0.0.1-SNAPSHOT.jar",
                        "./lib/spark-streaming-kafka-0-10_2.11-2.3.2.jar",
                        "./lib/kafka-clients-2.0.0.jar"});
        sparkConf.initializeLogIfNecessary(false, true);
        return sparkConf;
    }

    @Bean
    public JavaStreamingContext javaStreamingContext() {
        return new JavaStreamingContext(sparkConf(), Durations.seconds(2));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
