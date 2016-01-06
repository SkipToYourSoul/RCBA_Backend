package myutil;

import java.util.*;

/**
 * Created by 叶 on 2015/5/15.
 *
 */
public class Container {
    public void sortContainer(){
        List<Map.Entry<String,Double>> mappingList = null;
        Map<String,Double> informationGain = new HashMap<String, Double>();
        mappingList = new ArrayList<Map.Entry<String,Double>>(informationGain.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> mapping1, Map.Entry<String, Double> mapping2) {
                return -mapping1.getValue().compareTo(mapping2.getValue());
            }
        });
        for(Map.Entry<String,Double> mapping:mappingList){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }
    }

    public void traverseMap(){
        Map<String,String> entityMap = new HashMap<String,String>();
        entityMap.put("a","b");
        Iterator<Map.Entry<String, String>> iter = entityMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String, String> entry = iter.next();
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
}
