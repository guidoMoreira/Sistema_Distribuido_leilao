/**
  * Laboratorio 4  
  * Autor: Lucio Agostinho Rocha
  * Ultima atualizacao: 04/04/2023
  */
package br;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


public class Principal {

	public final static Path path = Paths
			.get("/home/usuario/Vídeos/Trabalho Multidisciplinar Sistemas Distribuidos/ProjetoFinal/Gerenciador/Gerenciador/src/main/java/br/registro.txt");
	private int NUM_FORTUNES = 0;

	private FileReader fr; 
	
	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				}// fim while

				//System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				int lineCount = 0;

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					//System.out.println(fortune.toString());

					//System.out.println(lineCount);
				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public String read(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			String result=";";
			
			
			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				for(int i = 0; i < NUM_FORTUNES; i++){
				int lineSelected = i;


				result += hm.get(lineSelected);
				result +=";";
				}
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return result;
		}

		public String write(HashMap<Integer, String> hm, String fortune)
				throws FileNotFoundException {
			String[] partes = fortune.split(";");

			//int id = Integer.parseInt(partes[0]);
			float valor = Float.parseFloat(partes[0].replace(",", "."));
			String CEP = partes[1];
			String nome = partes[2];	

			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					path.toString(),true)); //true=append
			try (BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(os))) {
				/*String old = hm.get(id);
				String[] partesold = fortune.split(";");
				int ido = Integer.parseInt(partes[0]);
				float valoro = Float.parseFloat(partes[1].replace(",", "."));
	
				if(valor > valoro){*/
				Scanner input = new Scanner(System.in);
				//System.out.print("Add fortune: ");
				//String fortune = input.next();
				
				String fort = Integer.toString(NUM_FORTUNES)+ ";"+fortune;
				NUM_FORTUNES++;
				hm.put(NUM_FORTUNES, fort);

				//System.out.println(hm.get(NUM_FORTUNES));
				
				//Append file
				bw.append(fort+"\n%\n");
				return fort;
				/*}else{
					return "Valor muito baixo";
				}*/

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
				return "Erro na leitura do arquivo";
			}
			
		}
		public String overwrite(HashMap<Integer, String> hm, String fortune)
				throws FileNotFoundException {
			String[] partes = fortune.split(";");

			int id = Integer.parseInt(partes[0]);
			float valor = Float.parseFloat(partes[1].replace(",", "."));
			String CEP = partes[2];
				

			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					path.toString(),true)); //true=append
			if(id < NUM_FORTUNES){
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
				String old = hm.get(id);
				String[] partesold = old.split(";");
				int ido = Integer.parseInt(partesold[0]);
				float valoro = Float.parseFloat(partesold[1].replace(",", "."));
				String nom = partesold[3];
				System.out.print(valoro);
				if(valor > valoro){
				Scanner input = new Scanner(System.in);
				//System.out.print("Add fortune: ");
				//String fortune = input.next();
				
				NUM_FORTUNES++;

				hm.replace(id, fortune+";"+nom);

				//System.out.println(hm.get(NUM_FORTUNES));
				
                                
				//Append file
                                PrintWriter writer = new PrintWriter(path.toString());
                                writer.print("");
                                writer.close();
                                for(int i = 0; i < NUM_FORTUNES;i++){
                                    if(i != id)
                                        bw.append(hm.get(i)+"%\n");
                                    else
                                        bw.append(hm.get(id)+"%\n");
                                }
					return fortune+";"+nom;
				}else{
					return "Valor muito baixo";
				}

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
				return "Erro na leitura do arquivo";
			}
			}else{
				return "Id muito alto";
}
		}
	}

	public String write(String fortune){
		fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			return fr.write(hm, fortune);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
      return "Arquivo não encontrado";
		}
	}
	public String overwrite(String nV){
		fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			return fr.overwrite(hm, nV);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
      return "Arquivo não encontrado";
		}

	}
	public String read(){
		String result="-1";
		
		fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			result = fr.read(hm);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
        

}
