import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class main {
	// folder file and our filtred file
	final static String Folder_mail_files = "EML_a_trier/";
	final static File filtredWord = new File("./filtredWord.txt");

	public static void main(String[] args) throws FileNotFoundException {
		// ok list mail and ko list mail
		List<String> OK_Mail = new ArrayList<>();
		List<String> KO_Mail = new ArrayList<>();

		Vector vector = new Vector<>();

		// create a list that contain our relevant words
		Scanner s = new Scanner(filtredWord);
		ArrayList<String> filtred_word_list = new ArrayList<String>();
		while (s.hasNext()) {
			filtred_word_list.add(s.next());
			vector.add(1);
		}
		s.close();

		// browse all files
		File file_folder_list = new File(Folder_mail_files);
		File[] list_eml_files = file_folder_list.listFiles();

		// Loop on eml files
		for (File emlFile : list_eml_files) {
			// Gets the file content
			String content = new Scanner(emlFile).useDelimiter("\\Z").next();

			// content is set to lowercase
			content = content.toLowerCase();
			// removes spcial char
			content = deletePunctuation(content);

			Vector v1 = new Vector();
			// count the occurence of contents word that are equal to word
			for (String word : filtred_word_list) {
				int nbre_occurence = countOccurenceWord(content, word);
				v1.add(nbre_occurence);
			}
			// We classify the email
			if (cosV1V2(vector, v1) > 0.77) {
				if (!OK_Mail.contains(emlFile)) {
					OK_Mail.add(emlFile.getName());
				}

				System.out.println(v1);

			} else {
				if (!KO_Mail.contains(emlFile)) {
					KO_Mail.add(emlFile.getName());
				}

			}

		}
		System.out.println("******************   Le constat general  ********************");

		System.err.println("il y'a :  " + OK_Mail.size() + " de Mail Ok ");
		System.err.println("il y'a :  " + KO_Mail.size() + " de Mail KO ");
		System.out.println();

		// display ok Mail
		for (String file : OK_Mail) {
			System.out.println(file);
		}

	} // fin main

	public static String deletePunctuation(String str) {
		str = str.replace("é", "e");
		str = str.replace("è", "e");
		str = str.replace("ê", "e");
		str = str.replace("à", "a");
		str = str.replace("ù", "u");
		str = str.replace("ç", "c");
		str = str.replace("ô", "o");
		str = str.replace("î", "i");
		str = str.replace("ï", "i");
		str = str.replace("û", "u");
		return str;
	}

	public static int countOccurenceWord(String contentEmlFile, String word) {
		int count = 0;
		String[] words = contentEmlFile.split(" ");

		for (String w : words) {
			if (w.equals(word)) {
				count++;
			}
		}
		return count;
	}

	public static double cosV1V2(Vector v1, Vector v2) {

		double normV1 = 0.0;
		double normv2 = 0.0;
		double produit_scalaire = 0.0;

		for (int i = 0; i < v2.size(); i++) {
			normV1 += Math.pow((int) v2.get(i), 2);
			normv2 += Math.pow((int) v2.get(i), 2);
			produit_scalaire += ((int) v2.get(i) * (int) v2.get(i));
		}
		normV1 = Math.sqrt(normV1);
		normv2 = Math.sqrt(normv2);
		double cos = 0.0;
		if (normv2 != 0) {
			cos = produit_scalaire / (normV1 * normv2);
		}
		return cos;
	}

}
