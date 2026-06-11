package com.example.resturantapp.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.resturantapp.models.Admin;
import com.example.resturantapp.models.Customer;
import com.example.resturantapp.models.MenuCategory;
import com.example.resturantapp.models.MenuItem;
import com.example.resturantapp.models.Order;
import com.example.resturantapp.models.Order.OrderStatus;
import com.example.resturantapp.models.Order.OrderType;
import com.example.resturantapp.models.OrderItem;
import com.example.resturantapp.models.OrderItem.PortionType;
import com.example.resturantapp.models.Role;
import com.example.resturantapp.models.Staff;
import com.example.resturantapp.repository.AdminRepo;
import com.example.resturantapp.repository.CustomerRepo;
import com.example.resturantapp.repository.MenuCategoryRepo;
import com.example.resturantapp.repository.MenuItemRepo;
import com.example.resturantapp.repository.OrderRepo;
import com.example.resturantapp.repository.StaffRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepo adminRepo;
    private final MenuCategoryRepo menuCategoryRepo;
    private final MenuItemRepo menuItemRepo;
    private final StaffRepo staffRepo;
    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Handle old admin email
        Optional<Admin> oldAdmin = adminRepo.findByEmail("admin@restaurant1.com");
        if (oldAdmin.isPresent()) {
            Admin admin = oldAdmin.get();
            admin.setEmail("admin@restaurant.com");
            adminRepo.save(admin);
            System.out.println("Updated admin email to: admin@restaurant.com");
        } else if (!adminRepo.existsByEmail("admin@restaurant.com")) {
            Admin admin = new Admin();
            admin.setEmail("admin@restaurant.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setRestaurantname("Restaurant1");
            adminRepo.save(admin);
            System.out.println("Default admin user created: admin@restaurant.com / admin123");
        }

        if (!staffRepo.existsByEmail("staff@restaurant.com")) {
            Staff staff = new Staff();
            staff.setEmail("staff@restaurant.com");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setRole(Role.STAFF);
            staffRepo.save(staff);
            System.out.println("Default staff user created: staff@restaurant.com / staff123");
        }

        // Seed sample menu categories if none exist
        if (menuCategoryRepo.count() == 0) {
            MenuCategory appetizers = new MenuCategory();
            appetizers.setCategoryname("Appetizers");
            menuCategoryRepo.save(appetizers);

            MenuCategory mains = new MenuCategory();
            mains.setCategoryname("Main Course");
            menuCategoryRepo.save(mains);

            MenuCategory desserts = new MenuCategory();
            desserts.setCategoryname("Desserts");
            menuCategoryRepo.save(desserts);

            System.out.println("Sample menu categories seeded: Appetizers, Main Course, Desserts");
        }

        // Seed sample menu items if none exist
        if (menuItemRepo.count() == 0) {
            // Get the category IDs (assuming order of insertion)
            MenuCategory appetizers = menuCategoryRepo.findByCategoryname("Appetizers");
            MenuCategory mains = menuCategoryRepo.findByCategoryname("Main Course");
            MenuCategory desserts = menuCategoryRepo.findByCategoryname("Desserts");

            if (menuCategoryRepo.findByCategoryname("Drinks") == null) {
                MenuCategory drinks = new MenuCategory();
                drinks.setCategoryname("Drinks");
                menuCategoryRepo.save(drinks);
            }
            MenuCategory drinks = menuCategoryRepo.findByCategoryname("Drinks");

            if (appetizers != null) {
                MenuItem fries = new MenuItem();
                fries.setMenuitemname("French Fries");
                fries.setDescription("Crispy golden fries");
                fries.setCategory(appetizers);
                fries.setImageurl("/img/menu/1.jpg");
                fries.setPricesmall(5.0);
                fries.setPricemedium(7.0);
                menuItemRepo.save(fries);
            }

            if (mains != null) {
                MenuItem burger = new MenuItem();
                burger.setMenuitemname("Cheese Burger");
                burger.setDescription("Juicy beef patty with cheese");
                burger.setCategory(mains);
                burger.setImageurl("/img/menu/2.jpg");
                burger.setPricesmall(10.0);
                burger.setPricemedium(12.0);
                burger.setPricelarge(15.0);
                menuItemRepo.save(burger);
            }

            if (desserts != null) {
                MenuItem cake = new MenuItem();
                cake.setMenuitemname("Chocolate Cake");
                cake.setDescription("Rich chocolate dessert");
                cake.setCategory(desserts);
                cake.setImageurl("/img/menu/3.jpg");
                cake.setPricesmall(6.0);
                menuItemRepo.save(cake);
            }

            if (drinks != null) {
                MenuItem mojito = new MenuItem();
                mojito.setMenuitemname("Fresh Mojito");
                mojito.setDescription("Refreshing mint and lime");
                mojito.setCategory(drinks);
                mojito.setImageurl("/img/menu/4.jpg");
                mojito.setPricesmall(8.0);
                menuItemRepo.save(mojito);

                MenuItem coffee = new MenuItem();
                coffee.setMenuitemname("Iced Latte");
                coffee.setDescription("Premium coffee with milk");
                coffee.setCategory(drinks);
                coffee.setImageurl("/img/menu/5.jpg");
                coffee.setPricesmall(6.5);
                menuItemRepo.save(coffee);
            }


            System.out.println("Sample menu items seeded: French Fries, Cheese Burger, Chocolate Cake");
        }

        // Seed sample customers if none exist
        if (customerRepo.count() == 0) {
            Customer customer1 = new Customer();
            customer1.setName("John Doe");
            customer1.setPhonenumber("1234567890");
            customer1.setRole(Role.CUSTOMER);
            customerRepo.save(customer1);

            Customer customer2 = new Customer();
            customer2.setName("Jane Smith");
            customer2.setPhonenumber("0987654321");
            customer2.setRole(Role.CUSTOMER);
            customerRepo.save(customer2);

            System.out.println("Sample customers seeded: John Doe, Jane Smith");
        }

        // Seed sample orders if none exist
        if (orderRepo.count() == 0) {
            Customer customer1 = customerRepo.findByPhonenumber("1234567890").orElse(null);
            Customer customer2 = customerRepo.findByPhonenumber("0987654321").orElse(null);

            MenuItem fries = menuItemRepo.findByMenuitemname("French Fries");
            MenuItem burger = menuItemRepo.findByMenuitemname("Cheese Burger");
            MenuItem cake = menuItemRepo.findByMenuitemname("Chocolate Cake");

            if (customer1 != null && fries != null && burger != null) {
                Order order1 = new Order();
                order1.setCustomer(customer1);
                order1.setCustomername("John Doe");
                order1.setPhonenumber("1234567890");
                order1.setOrdertype(OrderType.DINE_IN);
                order1.setTablenumber("5");
                order1.setStatus(OrderStatus.PENDING);

                List<OrderItem> items1 = new ArrayList<>();
                OrderItem item1 = new OrderItem();
                item1.setOrder(order1);
                item1.setMenuitem(fries);
                item1.setPortiontype(PortionType.MEDIUM);
                item1.setQuantity(2);
                item1.setPrice(7.0);
                items1.add(item1);

                OrderItem item2 = new OrderItem();
                item2.setOrder(order1);
                item2.setMenuitem(burger);
                item2.setPortiontype(PortionType.LARGE);
                item2.setQuantity(1);
                item2.setPrice(15.0);
                items1.add(item2);

                order1.setItems(items1);
                order1.setTotalprice(7.0 * 2 + 15.0);
                orderRepo.save(order1);
            }

            if (customer2 != null && cake != null) {
                Order order2 = new Order();
                order2.setCustomer(customer2);
                order2.setCustomername("Jane Smith");
                order2.setPhonenumber("0987654321");
                order2.setOrdertype(OrderType.TAKE_AWAY);
                order2.setStatus(OrderStatus.PREPARING);

                List<OrderItem> items2 = new ArrayList<>();
                OrderItem item3 = new OrderItem();
                item3.setOrder(order2);
                item3.setMenuitem(cake);
                item3.setPortiontype(PortionType.SMALL);
                item3.setQuantity(1);
                item3.setPrice(6.0);
                items2.add(item3);

                order2.setItems(items2);
                order2.setTotalprice(6.0);
                orderRepo.save(order2);
            }

            System.out.println("Sample orders seeded");
        }
    }
}
