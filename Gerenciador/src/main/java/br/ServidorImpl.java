/**
  * Laboratorio 4  
  * Autor: Guido Margonar Moreira
  * Ultima atualizacao: 06/05/2023
  */
package br;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class ServidorImpl implements IMensagem{
    private File lockFile;
	ArrayList<Peer> alocados;
    public final static Path lockFilePath = Paths.get("/home/usuario/Vídeos/Trabalho Multidisciplinar Sistemas Distribuidos/ProjetoFinal/Gerenciador/Gerenciador/src/main/java/br/lock.txt");
	
    public ServidorImpl() {
          alocados = new ArrayList<>();
          lockFile = new File(lockFilePath.toString());
    }
    
    //Cliente: invoca o metodo remoto 'enviar'
    //Servidor: invoca o metodo local 'enviar'
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
        	System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
		}
        return resposta;
    }    
    
    public String parserJSON(String json) throws IOException {
	String jsonString = json.replaceAll("\n", "");//json.replaceAll("\\s+", "");
        
        // Criar um mapa para armazenar os valores
        Map < String, Object > values = new HashMap < String, Object > ();

        // Verificar se a string começa e termina com chaves

        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
          // Remover as chaves da string
          jsonString = jsonString.substring(1, jsonString.length() - 1);

          // Dividir a string em pares chave-valor
          String[] keyValuePairs = jsonString.split(",");

          // Para cada par chave-valor, extrair a chave e o valor
          for (String pair: keyValuePairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].replaceAll("\"", "");
            String value = keyValue[1];

            // Verificar se o valor é uma string
            if (value.startsWith("\"") && value.endsWith("\"")) {
              // Adicionar a string sem aspas ao mapa
              	values.put(key, value.substring(1, value.length() - 1));
            }else if(value.startsWith("[") && value.endsWith("]")){
		values.put(key, value.substring(2, value.length() - 2));
	}

          }
        }
        String method = (String) values.get("method");
	String args = (String) values.get("args");
        
        //Enquanto existir arquivo de tranca ele fica esperando
        while(lockFile.exists()){
            System.out.print("Esperando");
        }
        
        //Arquivo de Tranca Criado
       lockFile.createNewFile();
      String resultado = "";
      Principal fr = new Principal();
    if(method.equals("read")){
      	String f = fr.read();
        resultado = "{\n" + "\"result\": "+ f +"\n" + "}";
System.out.println("Fortuna enviada: "+f);
      }
    else if (method.equals("write")){
    //Nova fortuna
      
      String res = fr.write(args);
      resultado = "{\n" + "\"result\": "+ res +"\n" + "}";
	System.out.println("Adicionada fortuna: "+res);
    }else if(method.equals("writeA")){
    //Nova fortuna
 	
      String res = fr.overwrite(args);
      resultado = "{\n" + "\"result\": "+ res +"\n" + "}";
	System.out.println("Adicionada fortuna: "+res);
    }
    
    //Tranca Liberada
    lockFile.delete();
		return resultado;
	}
    
    public void iniciar(){

    try {
    		//TODO: Adquire aleatoriamente um 'nome' do arquivo Peer.java
    		List<Peer> listaPeers = Arrays.asList(Peer.values());
    		
    		Registry servidorRegistro;
    		try {
    			servidorRegistro = LocateRegistry.createRegistry(1099);
    		} catch (java.rmi.server.ExportException e){ //Registro jah iniciado 
    			System.out.print("Registro ja iniciado. Usar o ativo.\n");
    		}
    		servidorRegistro = LocateRegistry.getRegistry(); //Registro eh unico para todos os peers
    		String [] listaAlocados = servidorRegistro.list();
    		for(int i=0; i<listaAlocados.length;i++)
    			System.out.println(listaAlocados[i]+" ativo.");
    		
    		SecureRandom sr = new SecureRandom();
    		Peer peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    		
    		int tentativas=0;
    		boolean repetido = true;
    		boolean cheio = false;
    		while(repetido && !cheio){
    			repetido=false;    			
    			peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    			for(int i=0; i<listaAlocados.length && !repetido; i++){
    				
    				if(listaAlocados[i].equals(peer.getNome())){
    					System.out.println(peer.getNome() + " ativo. Tentando proximo...");
    					repetido=true;
    					tentativas=i+1;
    				}    			  
    				
    			}
    			//System.out.println(tentativas+" "+listaAlocados.length);
    			    			
    			//Verifica se o registro estah cheio (todos alocados)
    			if(listaAlocados.length>0 && //Para o caso inicial em que nao ha servidor alocado,
    					                     //caso contrario, o teste abaixo sempre serah true
    				tentativas==listaPeers.size()){ 
    				cheio=true;
    			}
    		}
    		
    		if(cheio){
    			System.out.println("Sistema cheio. Tente mais tarde.");
    			System.exit(1);
    		}
    		
            IMensagem skeleton  = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind(peer.getNome(), skeleton);
            System.out.print(peer.getNome() +" Servidor RMI: Aguardando conexoes...");
                        
        } catch(Exception e) {
            e.printStackTrace();
        }        

    }
    
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }    
}
