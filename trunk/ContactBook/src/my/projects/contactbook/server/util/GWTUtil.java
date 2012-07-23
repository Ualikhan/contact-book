package my.projects.contactbook.server.util;

import java.util.ArrayList;
import java.util.List;

public class GWTUtil {

	public static <T> ArrayList<T> makeGWTSafe(List<T> list) {
		if(list instanceof ArrayList) {
			return (ArrayList<T>)list;
		} else {
			ArrayList<T> newList = new ArrayList<T>();
			newList.addAll(list);
			return newList;
		}
	}
	
}

