package com.in5bv.rourb.controller;

import com.in5bv.rourb.entity.*;
import com.in5bv.rourb.repository.ClientRepository;
import com.in5bv.rourb.repository.SaleDetailRepository;
import com.in5bv.rourb.repository.SaleRepository;
import com.in5bv.rourb.repository.UserRepository;
import com.in5bv.rourb.service.ProductServiceImplement;
import com.in5bv.rourb.service.SaleDetailServiceImplement;
import com.in5bv.rourb.service.SaleServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ProductServiceImplement productService;
    private final SaleServiceImplement saleService;
    private final SaleDetailServiceImplement saleDetailService;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;

    public ApiController(ProductServiceImplement productService,
                         SaleServiceImplement saleService,
                         SaleDetailServiceImplement saleDetailService,
                         ClientRepository clientRepository,
                         UserRepository userRepository,
                         SaleRepository saleRepository,
                         SaleDetailRepository saleDetailRepository) {
        this.productService = productService;
        this.saleService = saleService;
        this.saleDetailService = saleDetailService;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
    }


    @GetMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> getProducts() {
        List<Map<String, Object>> result = productService.getAllProducts().stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idProduct", p.getIdProduct());
            m.put("productCode", p.getProductCode());
            m.put("productName", p.getProductName());
            m.put("price", p.getPrice());
            m.put("stock", p.getStock());
            m.put("stateProduct", p.getStateProduct() != null ? p.getStateProduct().name() : null);
            m.put("imageUrl", p.getImageUrl());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/active")
    public ResponseEntity<List<Map<String, Object>>> getActiveProducts() {
        List<Map<String, Object>> result = productService.getAllProducts().stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("idProduct", p.getIdProduct());
                    m.put("productName", p.getProductName());
                    m.put("price", p.getPrice());
                    m.put("stock", p.getStock());
                    m.put("imageUrl", p.getImageUrl());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Products product) {
        try {
            Products saved = productService.saveProduct(product);
            return ResponseEntity.ok(Map.of("id", saved.getIdProduct(), "message", "Producto creado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Products product) {
        try {
            product.setIdProduct(id);
            Products updated = productService.updateProduct(product);
            return ResponseEntity.ok(Map.of("message", "Producto actualizado", "id", updated.getIdProduct()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProducts(id);
            return ResponseEntity.ok(Map.of("message", "Producto eliminado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/products/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());

            Files.write(path, file.getBytes());

            return ResponseEntity.ok("http://localhost:8088/uploads/" + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error");
        }
    }

    @GetMapping("/sales")
    public ResponseEntity<List<Map<String, Object>>> getSales() {
        List<Map<String, Object>> result = saleService.getAllSales().stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idSale", s.getIdSale());
            m.put("saleCode", s.getSaleCode());
            m.put("saleDate", s.getSaleDate());
            m.put("total", s.getTotal());
            m.put("stateSale", s.getStateSale() != null ? s.getStateSale().name() : null);
            if (s.getClient() != null) {
                m.put("clientId", s.getClient().getIdClient());
                m.put("clientName", s.getClient().getClientName() + " " + s.getClient().getClientLastName());
            }
            if (s.getUser() != null) {
                m.put("userId", s.getUser().getIdUser());
                m.put("userName", s.getUser().getUsername());
            }
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sales")
    public ResponseEntity<?> createSale(@RequestBody Map<String, Object> body) {
        try {
            Sales sale = new Sales();
            sale.setSaleCode(body.get("saleCode") != null ? Long.valueOf(body.get("saleCode").toString()) : null);
            sale.setTotal(body.get("total") != null ? new java.math.BigDecimal(body.get("total").toString()) : java.math.BigDecimal.ZERO);
            sale.setSaleDate(new java.util.Date());
            if (body.get("stateSale") != null) {
                sale.setStateSale(StateSale.valueOf(body.get("stateSale").toString()));
            }
            if (body.get("clientId") != null) {
                Clients c = clientRepository.findById(Integer.valueOf(body.get("clientId").toString()))
                        .orElseThrow(() -> new RuntimeException("Client not found"));
                sale.setClient(c);
            }
            if (body.get("userId") != null) {
                Users u = userRepository.findById(Integer.valueOf(body.get("userId").toString()))
                        .orElseThrow(() -> new RuntimeException("User not found"));
                sale.setUser(u);
            }
            Sales saved = saleService.saveSales(sale);
            return ResponseEntity.ok(Map.of("id", saved.getIdSale(), "message", "Venta creada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/sales/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        try {
            Sales existing = saleService.getSaleById(id);
            if (body.get("stateSale") != null) existing.setStateSale(StateSale.valueOf(body.get("stateSale").toString()));
            if (body.get("total") != null) existing.setTotal(new java.math.BigDecimal(body.get("total").toString()));
            if (body.get("saleCode") != null) existing.setSaleCode(Long.valueOf(body.get("saleCode").toString()));
            Sales updated = saleService.updateSales(id, existing);
            return ResponseEntity.ok(Map.of("message", "Venta actualizada", "id", updated.getIdSale()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/sales/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable Integer id) {
        try {
            saleService.deleteSales(id);
            return ResponseEntity.ok(Map.of("message", "Venta eliminada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/sale-details")
    public ResponseEntity<List<Map<String, Object>>> getSaleDetails() {
        List<Map<String, Object>> result = saleDetailService.getAllSaleDetails().stream().map(d -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idSaleDetail", d.getIdSaleDetail());
            m.put("detailSaleCode", d.getDetailSaleCode());
            m.put("amount", d.getAmount());
            m.put("unitaryPrice", d.getUnitaryPrice());
            m.put("subtotal", d.getSubtotal());
            if (d.getProduct() != null) {
                m.put("productId", d.getProduct().getIdProduct());
                m.put("productName", d.getProduct().getProductName());
            }
            if (d.getSale() != null) {
                m.put("saleId", d.getSale().getIdSale());
                m.put("saleCode", d.getSale().getSaleCode());
                if (d.getSale().getClient() != null) {
                    m.put("clientName", d.getSale().getClient().getClientName() + " " + d.getSale().getClient().getClientLastName());
                }
            }
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sale-details")
    public ResponseEntity<?> createSaleDetail(@RequestBody Map<String, Object> body) {
        try {
            SaleDetails detail = new SaleDetails();
            detail.setDetailSaleCode(body.get("detailSaleCode") != null ? Integer.valueOf(body.get("detailSaleCode").toString()) : null);
            detail.setAmount(Integer.valueOf(body.get("amount").toString()));
            detail.setUnitaryPrice(new java.math.BigDecimal(body.get("unitaryPrice").toString()));
            detail.setSubtotal(new java.math.BigDecimal(body.get("subtotal").toString()));
            if (body.get("productId") != null) {
                Products p = productService.getProductById(Integer.valueOf(body.get("productId").toString()));
                detail.setProduct(p);
            }
            if (body.get("saleId") != null) {
                Sales s = saleService.getSaleById(Integer.valueOf(body.get("saleId").toString()));
                detail.setSale(s);
            }
            SaleDetails saved = saleDetailService.saveSaleDetails(detail);
            return ResponseEntity.ok(Map.of("id", saved.getIdSaleDetail(), "message", "Detalle creado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/sale-details/{id}")
    public ResponseEntity<?> updateSaleDetail(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        try {
            SaleDetails existing = saleDetailService.getSaleDetailById(id);
            if (body.get("amount") != null) existing.setAmount(Integer.valueOf(body.get("amount").toString()));
            if (body.get("unitaryPrice") != null) existing.setUnitaryPrice(new java.math.BigDecimal(body.get("unitaryPrice").toString()));
            if (body.get("subtotal") != null) existing.setSubtotal(new java.math.BigDecimal(body.get("subtotal").toString()));
            SaleDetails updated = saleDetailService.updateSaleDetails(id, existing);
            return ResponseEntity.ok(Map.of("message", "Detalle actualizado", "id", updated.getIdSaleDetail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/sale-details/{id}")
    public ResponseEntity<?> deleteSaleDetail(@PathVariable Integer id) {
        try {
            saleDetailService.deleteSaleDetails(id);
            return ResponseEntity.ok(Map.of("message", "Detalle eliminado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/clients")
    public ResponseEntity<List<Map<String, Object>>> getClients() {
        List<Map<String, Object>> result = clientRepository.findAll().stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idClient", c.getIdClient());
            m.put("clientDpi", c.getClientDpi());
            m.put("clientName", c.getClientName());
            m.put("clientLastName", c.getClientLastName());
            m.put("address", c.getAddress());
            m.put("state", c.getState() != null ? c.getState().name() : null);
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> createClient(@RequestBody Map<String, Object> body) {
        try {
            Clients client = new Clients();
            client.setClientDpi(body.get("clientDpi") != null ? Long.valueOf(body.get("clientDpi").toString()) : null);
            client.setClientName(body.get("clientName") != null ? body.get("clientName").toString() : null);
            client.setClientLastName(body.get("clientLastName") != null ? body.get("clientLastName").toString() : null);
            client.setAddress(body.get("address") != null ? body.get("address").toString() : null);

            if (body.get("state") != null) {
                client.setState(State.ACTIVE);
            }

            Clients saved = clientRepository.save(client);
            return ResponseEntity.ok(Map.of(
                    "id", saved.getIdClient(),
                    "idClient", saved.getIdClient(),
                    "message", "Cliente creado"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/clients/buyers")
    public ResponseEntity<List<Map<String, Object>>> getBuyers() {
        List<Map<String, Object>> result = saleRepository.findAll().stream()
                .filter(s -> s.getClient() != null)
                .collect(Collectors.groupingBy(s -> s.getClient().getIdClient()))
                .values().stream().map(sales -> {
                    Clients c = sales.get(0).getClient();
                    Map<String, Object> m = new HashMap<>();
                    m.put("idClient", c.getIdClient());
                    m.put("clientName", c.getClientName() + " " + c.getClientLastName());
                    m.put("totalSales", sales.size());
                    m.put("sales", sales.stream().map(s -> {
                        Map<String, Object> sm = new HashMap<>();
                        sm.put("idSale", s.getIdSale());
                        sm.put("saleCode", s.getSaleCode());
                        sm.put("total", s.getTotal());
                        sm.put("saleDate", s.getSaleDate());
                        sm.put("stateSale", s.getStateSale() != null ? s.getStateSale().name() : null);
                        // sale details
                        List<Map<String, Object>> details = saleDetailRepository.findAll().stream()
                                .filter(d -> d.getSale() != null && d.getSale().getIdSale().equals(s.getIdSale()))
                                .map(d -> {
                                    Map<String, Object> dm = new HashMap<>();
                                    dm.put("productName", d.getProduct() != null ? d.getProduct().getProductName() : "");
                                    dm.put("amount", d.getAmount());
                                    dm.put("subtotal", d.getSubtotal());
                                    return dm;
                                }).collect(Collectors.toList());
                        sm.put("details", details);
                        return sm;
                    }).collect(Collectors.toList()));
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getUsers() {
        List<Map<String, Object>> result = userRepository.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("idUser", u.getIdUser());
            m.put("username", u.getUsername());
            m.put("email", u.getEmail());
            m.put("role", u.getRol() != null ? u.getRol().name() : null);
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}

