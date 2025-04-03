package hcmuaf.nlu.edu.vn.testproject.models;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String address;
    private String phone;
    private String email;
    private int status;

    // Constructors
    public Supplier() {}

    public Supplier(int supplierId, String supplierName, String address, String phone, String email, int status) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    // Getters và Setters
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}