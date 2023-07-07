/*
 * To change this license header, choose License Headers in
Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br;
/**
 *
 * @author lucio
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.rmi.RemoteException;
import java.util.Arrays;


public class Principal {
    
    
  public static void read(IMensagem stub,String opcao){
    Mensagem mensagem = new Mensagem("", opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'read'
    System.out.println(resposta.getMensagem());
    } catch (RemoteException e) {
        System.out.println("Erro ao pedir metodo ao servidor");
      }
  }
  public static void write(IMensagem stub,String opcao,Scanner leitura){
    System.out.print("Digite o índice: ");
    String fortune = leitura.next();
	fortune += leitura.nextLine();
            		
    Mensagem mensagem = new Mensagem(fortune, opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
    System.out.println(resposta.getMensagem());
    } catch (RemoteException e) {
        System.out.println("Erro ao pedir metodo ao servidor");
      }
  }
public static void overwrite(IMensagem stub,String opcao,Scanner leitura){
    System.out.print("Digite o índice: ");
    String fortune = leitura.next();
	fortune += leitura.nextLine();
    fortune +=";";
    System.out.print("Digite o novo valor: ");
	fortune += leitura.nextLine();
   fortune +=";";
    System.out.print("Digite seu CEP: ");
	fortune += leitura.nextLine();

    Mensagem mensagem = new Mensagem(fortune, opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
    System.out.println(resposta.getMensagem());
    } catch (RemoteException e) {
        System.out.println("Erro ao pedir metodo ao servidor");
      }
    
  }
    
 public static void main(String[] args) {
 
 
 	
        try {
                        
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);

            
        	//Escolhe um peer aleatorio da lista de peers para conectar
            SecureRandom sr = new SecureRandom();
    		
            IMensagem stub = null;
            Peer peer = null;
            List<Peer> listaPeers = Arrays.asList(Peer.values());
    		boolean conectou=false;
    		while(!conectou){
    			peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    			try{    				
    				stub = (IMensagem) registro.lookup(peer.getNome());
    				conectou=true;
    			} catch(java.rmi.ConnectException e){
    				System.out.println(peer.getNome() + " indisponivel. ConnectException. Tentanto o proximo...");
    			} catch(java.rmi.NotBoundException e){
    				System.out.println(peer.getNome() + " indisponivel. NotBoundException. Tentanto o proximo...");
    			}
    		}
            System.out.println("Conectado no peer: " + peer.getNome());            
    		
    		
            String opcao="";
            Scanner leitura = new Scanner(System.in);
            do {
            	System.out.println("1) Ver Sensores Disponíveis");
            	System.out.println("2) Colocar Sensor a venda");
		System.out.println("3) Dar lance em um sensor já existente");
                System.out.println("Web Services auxiliares");
                System.out.println("4) Converta as moedas de acordo com a cotação do real");
            	System.out.println("x) Exit");
            	System.out.print(">> ");
            	opcao = leitura.next();
            	switch(opcao){
            	case "1": {
            		read(stub,opcao);
            		break;
            	}
            	case "2": {
            		//Monta a mensagem                		
            		write(stub,opcao,leitura);
            		break;
            	}
		case "3": {
			overwrite(stub,opcao,leitura);
			break;
		}
                case "4": {
                        System.out.println("Enviando requesição para o servidor");
                        String soapEndpointUrl = "http://localhost:8080/SoapServer/Consulta?wsdl";
                        String soapAction = "http://localhost:8080/SoapServer/Consulta";
 //Soma 1+2
 //float c2=2;
 
 System.out.println("Digite o valor na sua moeda: ");            
    		

 Scanner le2 = new Scanner(System.in);
 
 String CAMPO1 = leitura.next();
 
 System.out.println("Digite o valor da sua moeda em Reais: ");
 String CAMPO2 = leitura.next();;

 SoapClient sc = new SoapClient(CAMPO1,CAMPO2);
 sc.callSoapWebService(soapEndpointUrl, soapAction);
 //System.out.println("Fim!");
                        break;
                }
            	}
            } while(!opcao.equals("x"));
                        
        } catch(Exception e) {
            e.printStackTrace();
        }
     
    }
}