## Summary
This example shows how to use BigDL train a model on [Standford Treebank
dataset](https://nlp.stanford.edu/sentiment/index.html) dataset using binary TreeLSTM and [Glove](https://nlp.stanford.edu/projects/glove/)
word embedding vectors.   Tree-LSTM is a kind of recursive neural networks, which describes in the paper 
[Improved Semantic Representations From Tree-Structured Long Short-Term Memory Networks](https://arxiv.org/abs/1503.00075)
 by Kai Sheng Tai, Richard Socher, and Christopher Manning.

The dataset is a corpus of ~10K one-sentence movie reviews from Rotten Tomatoes. Each sentence has been parsed into constituency trees, which is
kind of binary trees with a word at each leaf. After pre-processing, every node has been tagged a label whose range from -2 to 2, representing 
the sentiment of the word or phrase. The value from -2 to 2 corresponds to highly negative, moderately negative, neutral, moderately positive and
highly positive. The root of the tree represents the sentiment of the entire sentence.

## Steps to run this example:
First run the following script

```{r, engine='sh'}
./fetch_and_preprocess.sh
```

The treebank dataset and the Glove word embedding vectors will be downloaded to
`/tmp/.bigdl/dataset/` directory, after that the treebank will be split into three folders
corresponding to train, dev, and test in an appropriate format.

Next just run the following command to run the code:

* Spark local:

```{r, engine='sh'}
        spark-submit --master "local[physical_core_number]" --driver-memory 20g      \
                   --class com.intel.analytics.bigdl.example.treeLSTMSentiment.Train \
                   bigdl-VERSION-jar-with-dependencies.jar --batchSize 128           
```

* Spark cluster:
      * Standalone:

```{r, engine='sh'}
        MASTER=xxx.xxx.xxx.xxx:xxxx
        spark-submit --master ${MASTER} --driver-memory 5g --executor-memory 5g      \
                   --total-executor-cores 32 --executor-cores 8                      \
                   --class com.intel.analytics.bigdl.example.treeLSTMSentiment.Train \
                   bigdl-VERSION-jar-with-dependencies.jar --batchSize 128           
```
        
       * Yarn client:
        
```{r, engine='sh'}
                spark-submit --master yarn --driver-memory 5g --executor-memory 5g           \
                           --num-executor 4 --executor-cores 8                               \
                           --class com.intel.analytics.bigdl.example.treeLSTMSentiment.Train \
                           bigdl-VERSION-jar-with-dependencies.jar --batchSize 128           
```

      * NOTE: The total batch is: 128 and the batch per node is 128/nodeNum.
            You can also have also set regularizer rate, learning rate, lstm hiddensize,
            dropout probability and epoch number by adding one of the options below:
            
```{r, engine='sh'}
             --baseDir           # where is the data, default is '/tmp/.bigdl/dataset/'
             --batchSize         # number of batch size, default is 128             
             --hiddenSize        # number of TreeLSTM hidden size, default is 250
             --learingRate       # number of learning rate, default is 0.05
             --regRate           # number of L2 regularization rate, default is 1e-4
             --p                 # number of dropout probability rate, default is 0
             --epoch             # number of epochs, default is 10
```
