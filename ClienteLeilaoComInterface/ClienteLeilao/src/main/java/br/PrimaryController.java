package br;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.rmi.RemoteException;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PrimaryController {
   
    @FXML
    private ListView show_list;
    
    @FXML
    private TextField tf_nomes;

    @FXML
    private TextField tf_Dinheiro;
    @FXML
    private TextField tf_taxa;
    @FXML
    private TextField tf_CEP;
    @FXML
    private TextField tf_vals;
    @FXML
    private TextField tf_ind;
    @FXML
    private TextField tf_valLance;
    @FXML
    private TextField tf_CEPlance;
    
    

    
  public static String read(IMensagem stub,String opcao){
    Mensagem mensagem = new Mensagem("", opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'read'
    return resposta.getMensagem();
    } catch (RemoteException e) {
        return "Erro ao pedir metodo ao servidor";
      }
  }
  public static void write(IMensagem stub,String opcao,String leitura){

            		
    Mensagem mensagem = new Mensagem(leitura, opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
    System.out.println(resposta.getMensagem());
    } catch (RemoteException e) {
        System.out.println("Erro ao pedir metodo ao servidor");
      }
  }
public static void overwrite(IMensagem stub,String opcao,String leitura){
 
 
    Mensagem mensagem = new Mensagem(leitura, opcao);
    try{
    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
    System.out.println(resposta.getMensagem());
    } catch (RemoteException e) {
        System.out.println("Erro ao pedir metodo ao servidor");
      }
    
  }
    /*@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }*/
    @FXML
    private void bt_read() throws IOException {
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
            ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(read(stub,"1"));
            show_list.setItems(items);
            
    }
    @FXML
    private void bt_write() throws IOException {
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
            
            String leitura = tf_vals.getText() +";"+tf_CEP.getText()+";"+ tf_nomes.getText();
            System.out.print(leitura);
            write(stub,"2",leitura);
    }
    @FXML
    private void bt_lance() throws IOException {
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
            
            String leitura = tf_ind.getText() +";"+tf_valLance.getText()+";"+ tf_CEPlance.getText();
            System.out.print(leitura);
           overwrite(stub,"3",leitura);
    }
    @FXML
    private void bt_convert() throws IOException {
       System.out.println("Enviando requesição para o servidor");
                        String soapEndpointUrl = "http://localhost:8080/SoapServer/Consulta?wsdl";
                        String soapAction = "http://localhost:8080/SoapServer/Consulta";

 String CAMPO1 = tf_Dinheiro.getText();
 

 String CAMPO2 = tf_taxa.getText();

 SoapClient sc = new SoapClient(CAMPO1,CAMPO2);
 sc.callSoapWebService(soapEndpointUrl, soapAction);
 
    }
}
