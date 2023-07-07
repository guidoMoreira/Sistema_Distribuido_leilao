//Exemplo de sala

package br.ws;


import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;

/**
 *
 * @author root
 */
@WebService(serviceName = "Consulta")
public class SoapService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "read")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello      " + txt + " !";
    }

    @WebMethod(operationName = "convert")
    public float somar(@WebParam(name = "campo1") String CAMPO1, @WebParam(name = "campo2") String CAMPO2) {
            return Float.parseFloat(CAMPO1) * Float.parseFloat(CAMPO2);
        
        
        
    }
}
