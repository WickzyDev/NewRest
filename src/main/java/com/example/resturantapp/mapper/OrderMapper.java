package com.example.resturantapp.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.resturantapp.dto.OrderDTO;
import com.example.resturantapp.models.Customer;
import com.example.resturantapp.models.Order;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface OrderMapper {

    @Mapping(target = "customerid", expression = "java(order.getCustomer() != null ? order.getCustomer().getCustomerid() : null)")
    OrderDTO toDTO(Order order);

    List<OrderDTO> toDTOList(List<Order> orders);

    @Mapping(source = "customerid", target = "customer", qualifiedByName = "mapCustomerIdToCustomer")
    Order toEntity(OrderDTO orderDTO);

    @Named("mapCustomerIdToCustomer")
    default Customer mapCustomerIdToCustomer(Long customerid) {
        if (customerid == null)
            return null;
        Customer customer = new Customer();
        customer.setCustomerid(customerid);
        return customer;
    }
}
