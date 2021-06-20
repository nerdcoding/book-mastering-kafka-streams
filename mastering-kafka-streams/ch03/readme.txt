
Create input topic:
    kafka-topics.sh --bootstrap-server 192.168.5.22:9092 --create --topic users --partitions 3 --replication-factor 2

Create output topic:
    kafka-topics.sh --bootstrap-server 192.168.5.22:9092 --create --topic crypto-sentiment --partitions 3 --replication-factor 2

Add Tweet Json into input topic:
    kafka-console-producer.sh --bootstrap-server 192.168.5.22:9092 --topic users < example-tweet.json

Show EntitySentiment Avro in output topic:
    kafka-console-consumer.sh --bootstrap-server 192.168.5.22:9092 --from-beginning --topic crypto-sentiment