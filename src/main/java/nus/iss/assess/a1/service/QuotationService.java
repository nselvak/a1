package nus.iss.assess.a1.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.assess.a1.model.PurchaseOrder;
import nus.iss.assess.a1.model.Quotation;

@Service
public class QuotationService {

    String url = "https://quotation.chuklee.com/quotation";

    // Read in payload information 
    public PurchaseOrder readInfo(String payload) {
        PurchaseOrder po = new PurchaseOrder();
        Map<String, Integer> endList = new HashMap<String, Integer>();
        
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject req = r.readObject();
            System.out.println(">>>>> service req readobject: " + req);
            

            // Setting basic information
            po.setNavigationId(req.getInt("navigationId"));
            po.setName(req.getString("name"));
            po.setEmail(req.getString("email"));
            po.setAddress(req.getString("address"));
            

            // Setting for item list
            JsonArray startList = req.getJsonArray("lineItems");

            // Check
            System.out.println(">>>>> service start arraylist: " + startList);

            startList.stream()
                .map(result -> (JsonObject) result)
                .forEach(
                    result -> endList.put(result.getString("item"),
                                         result.getInt("quantity"))
                );
        } catch (Exception ex) {
            System.err.printf(">>>> service Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }

        // Check
        System.out.println(">>>>> service end arraylist: " + endList);
        
        // final addition
        po.setLineItems(endList);
        return po;
    }


    // For task 4 get item names and return list
    public List<String> getItemNames(PurchaseOrder po) {
        List<String> items = new ArrayList<String>(po.getLineItems().keySet());

        // Check
        for(String i : items) { System.out.println(">>>>> service items: " + i);}

        return items;
    }


    //Task 5
    public Optional<Quotation> getQuotations(List<String> items) {

        // create array
        JsonArrayBuilder array = Json.createArrayBuilder();

        for(String i : items) {array.add(i);}
        JsonArray startArray = array.build();
        
        // HTTP call
        RequestEntity<String> req = RequestEntity
            .post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(startArray.toString(), String.class);

        RestTemplate template = new RestTemplate();

        // HTTP return request
        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            
            System.out.println(">>>>> service Result task 5: " + resp.toString());
            
            Quotation q = new Quotation();
            
            
            if(resp.getStatusCodeValue() == 200) {
                
                try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
                    JsonReader r = Json.createReader(is);
                    JsonObject requ = r.readObject();
    
                    q.setQuoteId(requ.getString("quoteId"));
                    JsonArray qArray = requ.getJsonArray("quotations");
                    
                    // check
                    System.out.println(">>>>> service QuoteID task 5: " + q.getQuoteId());

                    // cannot check quotations
                    //System.out.println(">>>>> quotations task 5: " + q.getQuotation();
    
                    qArray.stream()
                        .map(result -> (JsonObject) result)
                        .forEach( result -> {   
                            // retrieving the info required from url
                            q.addQuotation(result.getString("item"), (float) result.getJsonNumber("unitPrice").doubleValue());
                        });
                    
                    // print out fruits required
                    System.out.println(q.getQuotations().keySet().toString());
    
                    return Optional.of(q);
    
                } catch (Exception ex) {
                    System.err.printf(">>>> Service Error: %s\n", ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.err.printf(">>>> Service Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }


        return Optional.empty();
        
    }
    
}
