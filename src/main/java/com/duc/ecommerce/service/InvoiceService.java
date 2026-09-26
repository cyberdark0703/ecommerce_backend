package com.duc.ecommerce.service;

import com.duc.ecommerce.dto.request.InvoiceCreateRequest;
import com.duc.ecommerce.dto.response.InvoiceResponse;
import com.duc.ecommerce.dto.response.ProductResponse;
import com.duc.ecommerce.entity.Invoice;
import com.duc.ecommerce.entity.Product;
import com.duc.ecommerce.entity.User;
import com.duc.ecommerce.exception.EcommerceException;
import com.duc.ecommerce.exception.ErrorCode;
import com.duc.ecommerce.mapper.InvoiceMapper;
import com.duc.ecommerce.mapper.ProductMapper;
import com.duc.ecommerce.repository.InvoiceRepository;
import com.duc.ecommerce.repository.ProductRepository;
import com.duc.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class InvoiceService {
    @Autowired
    InvoiceRepository repository;
    InvoiceMapper mapper;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Transactional
    public InvoiceResponse create(InvoiceCreateRequest request){
        //int x = 1/0;
        Invoice invoice = new Invoice();

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username).orElseThrow(()-> new EcommerceException(ErrorCode.USER_NOT_FOUND));
        invoice.setUser(user);

        List<Product> products = new ArrayList<>();
        for (String productName : request.getProducts_name()){
            Product product = productRepository.findByNameContainingIgnoreCase(productName)
                    .orElseThrow(()-> new EcommerceException(ErrorCode.PRODUCT_NOT_FOUND));
            products.add(product);
        }
        invoice.setProducts(products);

        LocalDateTime time = LocalDateTime.now();
        invoice.setBorn(time);

        int tong =0;
        for (Product product : products){
            tong+=product.getPrice();
        }
        invoice.setTotal_price(tong);

        /*System.out.println(time);
        System.out.println(tong);*/

        repository.save(invoice);

        InvoiceResponse invoiceResponse = new InvoiceResponse();

        String invoiceId = "invoice_"+invoice.getId();
        invoiceResponse.setId(invoiceId);
        invoiceResponse.setUser_name(invoice.getUser().getName());

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : invoice.getProducts()){
            ProductResponse productResponse = productMapper.toProductResponse(product);
            String product_id = "product_" + product.getId();
            productResponse.setId(product_id);

            productResponse.setCategory_name(product.getCategory().getName());
            productResponses.add(productResponse);
        }
        invoiceResponse.setProducts(productResponses);

        invoiceResponse.setBorn(invoice.getBorn());
        invoiceResponse.setTotal_price(invoice.getTotal_price());

        return invoiceResponse;
    }

    public InvoiceResponse read (String id){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        System.out.println("CURRENT USER = " + username);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        System.out.println("IS ADMIN = " + isAdmin);

        Invoice invoice = repository.findById(id).orElseThrow(()-> new EcommerceException(ErrorCode.INVOICE_NOT_FOUND));

        // Kiểm tra Invoice có thuộc về user đang đăng nhập không
        if (!isAdmin && !username.equals(invoice.getUser().getName())) {
            throw new EcommerceException(ErrorCode.ACCESS_DENIED);
        }

        InvoiceResponse invoiceResponse = new InvoiceResponse();

        String invoiceId = "invoice_"+invoice.getId();
        invoiceResponse.setId(invoiceId);
        invoiceResponse.setUser_name(invoice.getUser().getName());

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : invoice.getProducts()){
            ProductResponse productResponse = productMapper.toProductResponse(product);
            String product_id = "product_" + product.getId();
            productResponse.setId(product_id);

            productResponse.setCategory_name(product.getCategory().getName());
            productResponses.add(productResponse);
        }
        invoiceResponse.setProducts(productResponses);

        invoiceResponse.setBorn(invoice.getBorn());
        invoiceResponse.setTotal_price(invoice.getTotal_price());

        /*System.out.println(invoice.getBorn());
        System.out.println(invoice.getTotal_price());*/

        return invoiceResponse;

    }
}
