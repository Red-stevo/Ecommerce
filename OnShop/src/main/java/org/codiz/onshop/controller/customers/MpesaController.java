package org.codiz.onshop.controller.customers;
import org.codiz.onshop.service.MpesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {
    @Autowired
    private MpesaService mpesaService;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam String phoneNumber, @RequestParam String amount) {
        String response = mpesaService.lipaNaMpesa(phoneNumber, amount);
        return ResponseEntity.ok(response);
    }
}

