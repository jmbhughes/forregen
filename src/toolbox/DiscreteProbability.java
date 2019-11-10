package toolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/***
 * A discete data distribution
 * Updated from https://stackoverflow.com/questions/35701316/discrete-probability-distribution-in-java
 * @author Marcus Hughes
 *
 * @param <T>
 */
class DiscreteDistribution<T>{
    List<Double> probs = new ArrayList<>();
    List<T> events = new ArrayList<>();
    double sumProb;
    Random rand = new Random();

    /**
     * Create a discrete distribution
     * @param probs the probability associated with each element
     */
    public DiscreteDistribution(Map<T,Double> probs){
        for(T event : probs.keySet()){
            sumProb += probs.get(event);
            events.add(event);
            this.probs.add(probs.get(event));
        }
    }

    /**
     * @return one sample from the distribution
     */
    public T sample(){
        double prob = rand.nextDouble()*sumProb;
        int i;
        for(i=0; prob>0; i++){
            prob-= probs.get(i);
        }
        return events.get(i-1);
    }
}