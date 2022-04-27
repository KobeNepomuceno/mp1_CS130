import java.util.*;

public class Main {
	private static int numVars;
	private static boolean noDuplicates = false;
	private static List<Row> rowContainer = new ArrayList<Row>();
	private static String mintermResult = "";
	public static String getBinary(String s) {
		int i = Integer.parseInt(s);
		return Integer.toBinaryString(i);
	}
	
	public static String normalize(String s, int length) {
		if(s.length() < length) {
			do {
				s = "0" + s;
			}while(s.length() < length);
		}
		return s;
	}
	
	private static boolean containsPair(List<Row> r) {
		Row r1;
		Row r2;
		boolean dupli = false;
		for(int i = 0; i < r.size(); i++) {
			r1 = r.get(i);
			for(int j = 0; j < r.size(); j++) {
				r2 = r.get(j);
				if(isPair(r1.getBinary(), r2.getBinary()) && i != j)
					dupli = true;
			}
		}
		return dupli;
	}
	
	private static void removeDuplicate(List<Row> r) {
		if(hasDuplicates(r)) {
			String s1 = "";
			String s2 = "";
			for(int i = 0; i < r.size(); i++) {
				Row r1 = r.get(i);
				s1 = r1.getBinary();
				for(int j = 0; j < r.size(); j++) {
					Row r2 = r.get(j);
					s2 = r2.getBinary();
					if(s1.equals(s2) && !r1.equals(r2)) {
						r.remove(j);
						break;
					}
				}
			}
		}
	}
	
	public static String[] isValidVars(Scanner sc) {
		String content = sc.nextLine();
		String inputs[] = content.split(" ");
		for(int i = 0; i < inputs.length; i++) {
			if(inputs[i].length() > 1 || !Character.isAlphabetic(inputs[i].charAt(0))) {
				System.out.print("Please enter a single letter per variable only: ");
				return isValidVars(sc);
			}				
		}
		return inputs;		
	}
	
	private static boolean isNumber(String str)
	{		
		try 
		{
			Integer.parseInt(str);
		}catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	private static boolean isPair(String s1, String s2) {
		int diff = 0;
		boolean pair = false;
		int index = 0;
		for(int i = 0; i < s1.length(); i++) {
			if(s1.charAt(i) != s2.charAt(i)) {
				diff++;
				index = i;
			}
		}
		pair = diff == 1 ? true: false;
		if(pair) {
			StringBuilder sb = new StringBuilder(s1);
			sb.setCharAt(index, '-');
			mintermResult = sb.toString();
		}
		return pair;
	}
	private static String sortMinterms(String minterms) {
		StringTokenizer st = new StringTokenizer(minterms, ",");
		int[] values = new int[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()) {
			values[i] = Integer.parseInt(st.nextToken());
			i++;
		}
		Arrays.sort(values);	
		
		StringBuilder sorted = new StringBuilder();
		for(int val: values) {
			sorted.append(val).append(",");
		}
		return  sorted.substring(0, sorted.length() - 1);
	}
	private static void comparison(List<Row> r) {
		String s1 = "";
		String s2 = "";
		for(Row r1 : r) {
			s1 = r1.getBinary();
			for(int i = 0; i < r.size(); i++) {
				s2 = r.get(i).getBinary();
				//if s1 and s2 has only 1 difference
				if(isPair(s1, s2)) {
					r1.setUsed(true);
					r.get(i).setUsed(true);
					String min = r1.getMinterm() + "," + r.get(i).getMinterm();
					min = sortMinterms(min);
					Row row = new Row(min, mintermResult, Math.min(r1.getGroup(), r.get(i).getGroup()), false);
					rowContainer.add(row);
					//break;
					
				}
			}
		}
	}
	
	private static void getUnused(List<Row> r) {
		for(Row r1 : r) {
			if(!r1.isUsed()) {
				rowContainer.add(r1);
			}
		}
	}
	
	private static boolean hasDuplicates(List<Row> r) {
		boolean dupli = false;
		for(int i = 0; i < r.size(); i++) {
			for(int j = 0; j < r.size(); j++) {
				if(r.get(i).getMinterm().equals(r.get(j).getMinterm()) && i != j) {
					dupli = true;
				}
			}
		}
		//dupli = false;
		return dupli;
	}
	
	public static String[] isValidMinterm(Scanner sc) {
		String content = sc.nextLine();		
		String inputs[] = content.split(" ");
		int max = (int) (Math.pow(2, numVars) - 1);
		for(int i = 0; i < inputs.length; i++) {
			if(!isNumber(inputs[i])) {
				System.out.print("Please enter numerical values only (space separated): ");
				return isValidMinterm(sc);
			}
			else if(Integer.parseInt(inputs[i]) < 0 || Integer.parseInt(inputs[i]) > max) {
				System.out.print("Minterm values must be within the range 0 to " + max +" only, please enter new input: " );
				return isValidMinterm(sc);
			}
		}
		return inputs;
	}
	
	private static String toTerm(String binary, String[] vars) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < binary.length(); i++) {
			if(binary.charAt(i) == '1') {
				output.append(vars[i]);
			}
			else if(binary.charAt(i) == '0') {
				output.append(vars[i] + "'");
			}
		}
		return output.toString();
	}
	private static boolean isIn(String input, String value) {
		StringTokenizer st = new StringTokenizer(input, ",");
		int[] values = new int[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()) {
			values[i] = Integer.parseInt(st.nextToken());
			i++;
		}
		for(int current: values) {
			if(value != "+" && current == Integer.parseInt(value)) {
				return true;
			}
		}
		return false;
	}
	public static void main(String args[]) {
		System.out.print("Enter function variables: ");
		Scanner s = new Scanner(System.in);
		String vars[] = isValidVars(s);
		numVars = vars.length == 1 ? 0 : vars.length ;
		
		System.out.print("Enter minterms: ");		
		String inputs[] = isValidMinterm(s);
		String bin[] = new String[inputs.length];
		
		//gets binary for each decimal input
		for(int i = 0; i < inputs.length; i++) {
			bin[i] = getBinary(inputs[i]);
		}
		
		//left padding with zeroes
		for(int i = 0; i < bin.length; i++) {
			bin[i] = normalize(bin[i], vars.length);
		}
		s.close();
		Map<String, String> minterms = new HashMap<String, String>();
		for(int i = 0; i < inputs.length; i++) {
			minterms.put(inputs[i], bin[i]);
		}
		System.out.println(minterms.toString());
		Object keys[] = minterms.keySet().toArray();
		List<Row> rows = new ArrayList<Row>();
		for(int i = 0; i < keys.length; i++) {
			int count = 0;
			String key = keys[i].toString();
			String value = minterms.get(key);
			for(int j = 0; j < value.length(); j++) {
				if(value.charAt(j) == '1') {
					count++;
				}
			}			
			Row r = new Row(key, value, count, false);
			rows.add(r);
		}
		//Sorts rows according to group
		Collections.sort(rows, Comparator.comparing(Row::getGroup));
		for(Row r: rows) {
			System.out.println(r.getMinterm() + "\t" + r.getBinary() + "\t" + r.getGroup());
		}
		int counter = 1;
		ArrayList<Row> primeImplicants = new ArrayList<>();
		do {
			if(rowContainer.size() != 0)
				removeDuplicate(rowContainer);
			System.out.println();
			
			System.out.println("COMPARISON: " + counter);
			System.out.println("-------------------------------------------------------");
			
			
			comparison(rows);
			removeDuplicate(rowContainer);
			getUnused(rows);			
					
			//check for pairs, if pairs found continue loop else stop
			if(!containsPair(rowContainer)) {
				noDuplicates = true;
			}
			
			for(Row r : rowContainer) {
				System.out.println(r.getMinterm() + "\t" + r.getBinary() + "\t" + r.getGroup());
				if(noDuplicates) {
					primeImplicants.add(r);
				}
			}	
			
			//take found pairs as new input then free buffer list for rows
			rows = new ArrayList<Row>(rowContainer);
			rowContainer.clear();
			counter++;			
			
		}while(!noDuplicates);
		
		//makes prime implicant table
		while(hasDuplicates(primeImplicants)) {
			removeDuplicate(primeImplicants);
		}
		System.out.println();
		System.out.println("-------------------------------------------------------");
		String[][] PItable = new String[primeImplicants.size() + 1][inputs.length + 1];
		int spacing = primeImplicants.get(0).getMinterm().length() + 5;
		for(int i = 0; i < PItable.length; i++) {
			for(int j = 0; j < PItable[0].length; j++) {
				if(i == 0 && j == 0) {
					System.out.printf("%-" + spacing + "s" , "");
				}
				else if(i == 0) {
					PItable[i][j] = inputs[j - 1];
					System.out.printf("%-5s" , PItable[i][j]);
				}
				else if(j == 0) {
					PItable[i][j] = primeImplicants.get(i - 1).getMinterm();
					System.out.printf("%-" + spacing + "s", PItable[i][j]);
				}
				else {
					PItable[i][j] = isIn(PItable[i][0],(PItable[0][j])) ? "X" : " ";
					System.out.printf("%-5s", PItable[i][j]);
				}				
			}
			System.out.println();
		}
		StringBuilder output = new StringBuilder();
		for(int i = 1; i < PItable[0].length; i++) {
			if(PItable[0][i] != "+" ) {
				int counter2 = 0;
				int tracker = 0;
				for(int j = 1; j < PItable.length; j++) {
					if(PItable[j][i] == "X") {
						counter2++;
						tracker = j;					
					}
				}
				if(counter2 == 1) {
					String PI = PItable[tracker][0];
					String PIbin = primeImplicants.get(tracker - 1).getBinary();
					output.append(toTerm(PIbin, vars)).append("+");
					for(int j = 1; j < PItable[0].length; j++) {
						if(isIn(PI, PItable[0][j])) {
							PItable[0][j] = "+";
						}
					}
					PItable[tracker][0] = "+";
				}				
			}	
		}
		ArrayList<String> remaining = new ArrayList<>();
		for(int i = 1; i < PItable[0].length; i++) {	
			if(PItable[0][i] != "+" ) {
				remaining.add(PItable[0][i]);
			}
		}	
		while(remaining.size() != 0) {
			String PIbin = "";
			int tracker = 0;
			for(int i = 1; i < PItable.length; i++) {
				String temp = PItable[i][0];
				if(temp != "+" ) {
					int counter3 = 0;
					int current = 0;
					for(int j = 0; j < remaining.size(); j++) {
						if(isIn(temp, remaining.get(j))) {
							counter3++;
						}
					}
					if(counter3 > current) {
						current = counter3;
						PIbin = primeImplicants.get(i- 1).getBinary();
						tracker = i;
					}
				}
			}
			output.append(toTerm(PIbin, vars)).append("+");			
			for(int j = remaining.size() - 1; j >= 0 ; j--) {
				if(isIn(PItable[tracker][0], remaining.get(j))) {
					remaining.remove(j);
				}
			}
			PItable[tracker][0] = "+";			
		}
		System.out.println();
		System.out.println("-------------------------------------------------------");
		String answer = (output.length() == 1 && !Character.isAlphabetic(output.charAt(0))) ? "1" : output.substring(0, output.length() - 1); 
		System.out.println("Output: " + answer);
	}
}
