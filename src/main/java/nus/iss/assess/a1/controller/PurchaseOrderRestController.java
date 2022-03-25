package nus.iss.assess.a1.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import nus.iss.assess.a1.model.PurchaseOrder;
import nus.iss.assess.a1.model.Quotation;
import nus.iss.assess.a1.service.QuotationService;

@RestController
@RequestMapping(path="/api")
public class PurchaseOrderRestController {

    @Autowired QuotationService quoteSvc;

    @PostMapping(path = "/po")
    public ResponseEntity<String> poSub(@RequestBody String payload) {

        // Check
        System.out.println(">>>>> controller payload: " + payload);
        PurchaseOrder po = quoteSvc.readInfo(payload);

        List<String> items = quoteSvc.getItemNames(po);

        // Check
        System.out.println(">>>>> controller items: " + items);

        Optional<Quotation> quote = quoteSvc.getQuotations(items);
        // Check
        System.out.println(">>>>> controller quote: " + quote.toString());

        // Task 6
        if(quote.isPresent()) {
            float total = 0;

            // Incremental price
            for(String i : items) {
                total = total + po.getQuantity(i)*quote.get().getQuotation(i);
                // Check
                System.out.println(">>>>> controller total: " + total);
            }

            // final result 
            JsonObject end = Json.createObjectBuilder()
                .add("invoiceId", quote.get().getQuoteId())
                .add("name", po.getName())
                .add("total", total)
                .build();

            return ResponseEntity.ok(end.toString());
            
        } else {
            JsonObject end = Json.createObjectBuilder().build();
            
            return ResponseEntity.badRequest()
                .body(end.toString());
        }
    }
    
}
