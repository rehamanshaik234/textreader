import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) throws FileNotFoundException{
		String page1Path = "C:/Users/sreha/Downloads/Page1.txt";
		String page2Path = "C:/Users/sreha/Downloads/Page2.txt";
		String page3Path = "C:/Users/sreha/Downloads/Page3.txt";
		Map<String,String> indexData= new HashMap<>();
		Map<String,String> page1=getData(page1Path,"1");
		Map<String,String> page2=getData(page2Path,"2");
		Map<String,String> page3=getData(page3Path,"3");
		assert page2 != null;
		assert page1 != null;
		assert page3 != null;

		Set<String> commonKey123=getCommonKeySet3(page1,page2,page3);
		Set<String> commonKey12=getCommonKeySet2(page1,page2);
		Set<String> commonKey23=getCommonKeySet2(page3,page2);
		Set<String> commonKey31=getCommonKeySet2(page1,page3);
		commonKey12.removeAll(commonKey123);
		commonKey23.removeAll(commonKey123);
		commonKey31.removeAll(commonKey123);
		Set<String> commonKeys=commonKey123;
		commonKeys.addAll(commonKey12);
		commonKeys.addAll(commonKey23);
		commonKeys.addAll(commonKey31);
		Map<String,String> page1Data= getNonCommonWords(commonKeys,page1.keySet(),"1");
		Map<String,String> page2Data= getNonCommonWords(commonKeys,page2.keySet(),"2");
		Map<String,String> page3Data= getNonCommonWords(commonKeys,page3.keySet(),"3");
		Map<String,String> page123Data=pageData(commonKey123,"1,2,3");
		Map<String,String> page12Data=pageData(commonKey12,"1,2");
		Map<String,String> page23Data=pageData(commonKey23,"2,3");
		Map<String,String> page31Data=pageData(commonKey31,"1,3");
		indexData.putAll(page1Data);
		indexData.putAll(page2Data);
		indexData.putAll(page3Data);
		indexData.putAll(page123Data);
		indexData.putAll(page12Data);
		indexData.putAll(page23Data);
		indexData.putAll(page31Data);
		writeToFile(sortMap(excludeWords(indexData)));

	}

	static Map<String,String> getData(String filePath, String page){
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			List<String> lines=new ArrayList<String>();
			while ((line=reader.readLine())!=null) {
				lines.add(line);
			}
			Map<String,String> data=new HashMap<>();
			for (int i = 0; i < lines.size(); i++) {
				List<String> words=addWords(lines.get(i));
				for (int j = 0; j < words.size(); j++) {
					data.put(words.get(j),page);
				}
			}
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static List<String> addWords(String line){
		String pattern = "[a-zA-Z•]+";
		List<String> words=new ArrayList<>();
		// Create a Pattern object
		Pattern regex = Pattern.compile(pattern);

		// Create a Matcher object
		Matcher matcher = regex.matcher(line);

		// Find and print all matches
		while (matcher.find()) {
			String match = matcher.group();
			words.add(match.toLowerCase());
		}
		return words;
	}

	public static Set<String> getCommonKeySet3(Map<String, String> map1, Map<String, String> map2, Map<String, String> map3) {
		Set<String> commonKeys = new HashSet<>();

		// Get the union of the keys from all 3 maps
		Set<String> allKeys = new HashSet<>();
		allKeys.addAll(map1.keySet());
		allKeys.addAll(map2.keySet());
		allKeys.addAll(map3.keySet());

		// Iterate over the union of keys and check if the key is present in all 3 maps
		for (String key : allKeys) {
			if (map1.containsKey(key) && map2.containsKey(key) && map3.containsKey(key)) {
				commonKeys.add(key);
			}
		}

		return commonKeys;
	}

	public static Set<String> getCommonKeySet2(Map<String, String> map1, Map<String, String> map2) {
		Set<String> commonKeys = new HashSet<>();

		// Get the union of the keys from all 3 maps
		Set<String> allKeys = new HashSet<>();
		allKeys.addAll(map1.keySet());
		allKeys.addAll(map2.keySet());

		// Iterate over the union of keys and check if the key is present in all 3 maps
		for (String key : allKeys) {
			if (map1.containsKey(key) && map2.containsKey(key)) {
				commonKeys.add(key);
			}
		}

		return commonKeys;
	}

	public  static Map<String,String> getNonCommonWords(Set<String> common,Set<String> nonCommon,String page){
		Set<String> allKeys = new HashSet<>();
		for (String key: nonCommon) {
			if(!common.contains(key)){
				allKeys.add(key);
			}
		}
		Map<String,String> data=new HashMap<>();
		for (String key:allKeys) {
			data.put(key,page);
		}
		return data;
	}
	public static Map<String,String> pageData(Set<String> page,String value){
		Map<String,String > data=new HashMap<>();
		for (String key:page) {
			data.put(key,value);
		}
		return data;
	}

	public static Map<String,String> excludeWords(Map<String,String> data){
		String excludePath="C:/Users/sreha/Downloads/exclude-words.txt";
		Map<String,String> excludeWords=getData(excludePath,"3");
		assert excludeWords != null;
		Set<String> words=excludeWords.keySet();
		for (String excludeWord:words) {
			data.remove(excludeWord);
		}
		return data;
	}

	public static Map<String ,String > sortMap(Map<String,String> map){
		// Create a comparator that compares two keys in the alphabet
		Comparator<String> comparator = String::compareTo;

		// Sort the map by the comparator
		Map<String, String> sortedMap = new TreeMap<>(comparator);
		sortedMap.putAll(map);
		return  sortedMap;
	}

	public static void writeToFile(Map<String,String> data){
		String fileName = "C:/Users/sreha/Downloads/index.txt";

		// Create a file output stream
		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {

			// Create a buffered writer
			try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {

				bufferedWriter.write("Word : Page Numbers\n" +
						"-------------------\n");
				// Iterate over the map and write the key-value pairs to the file
				for (Map.Entry<String, String> entry : data.entrySet()) {
					bufferedWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
				}
				System.out.println("created File");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}