package com.duc.ecommerce.controller;

import com.duc.ecommerce.dto.request.InvoiceCreateRequest;
import com.duc.ecommerce.dto.response.InvoiceResponse;
import com.duc.ecommerce.service.InvoiceService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceController {
    @Autowired
    InvoiceService service;

    @PostMapping("/create")
    public InvoiceResponse create (@RequestBody InvoiceCreateRequest request){
        return service.create(request);
    }

    @GetMapping("/read_one/{id}")
    public InvoiceResponse read (@PathVariable String id){
        return service.read(id);
    }
}
