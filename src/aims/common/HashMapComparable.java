package aims.common;

import java.util.Comparator;
import java.util.*;


public class HashMapComparable implements Comparator{
	public List sortingHashMap(Map m){
	
		 List list = new LinkedList(m.entrySet()); 
		 Collections.sort(list,new HashMapComparable());
		 return list;
	 }
	 public int compare(Object o1, Object o2) {                
		 return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());           
	 } 
		public Map sortHashMap(Map m){
			
			 List list =sortingHashMap(m); 
			 Map sortedMap = new LinkedHashMap();
				for (Iterator it = list.iterator(); it.hasNext();) {
				     Map.Entry entry = (Map.Entry)it.next();
				     sortedMap.put(entry.getKey(), entry.getValue());
				}
				return sortedMap;
		 }
}
