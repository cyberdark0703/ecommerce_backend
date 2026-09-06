package com.duc.ecommerce.mapper;

import com.duc.ecommerce.dto.request.InvoiceCreateRequest;
import com.duc.ecommerce.dto.response.InvoiceResponse;
import com.duc.ecommerce.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toInvoice (InvoiceCreateRequest request);
    InvoiceResponse toInvoiceResponse (Invoice invoice);

}
