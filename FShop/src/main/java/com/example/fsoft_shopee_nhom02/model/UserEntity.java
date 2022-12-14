package com.example.fsoft_shopee_nhom02.model;

import com.example.fsoft_shopee_nhom02.auth.AuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseClassEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp dob;
    private String gender;
    private String avatar;
    @Column(name="status")
    private String status = "Active";
    @Enumerated(EnumType.STRING)
    @Column(name="auth_provider")
    private AuthenticationProvider auth_provider = AuthenticationProvider.LOCAL;

    // Relationship with table AddressEntity
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AddressEntity> addressEntityList =new ArrayList<>();

    public UserEntity(String name, String username, String email, String avatar, AuthenticationProvider auth_provider, Timestamp create_date) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.auth_provider = auth_provider;
        this.setCreatedDate(create_date);
    }

    public List<AddressEntity> getAddressEntityList() {
        return addressEntityList;
    }

    public void setAddressEntityList(List<AddressEntity> addressEntityList) {
        this.addressEntityList = addressEntityList;
    }

    // Relationship with table OrderEntity (1:n)
    @OneToMany(mappedBy = "userEntities", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderEntity> orderEntities =new ArrayList<>();

    // Relationship with table RoleEntity
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles", // table link two relationship
            joinColumns = @JoinColumn(name = "userID"), // Key is link with table Users
            inverseJoinColumns = @JoinColumn(name = "roleId")) //Key is link with table Roles
    private Set<RoleEntity> roleEntitySet; //link with mapedBy in table RoleEntity

    // Relationship with table CartEntity
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartId", referencedColumnName = "id")
    private CartEntity cartEntity; // mappedBy in table CartEntity

    public CartEntity getCartEntity() {
        return cartEntity;
    }

    public void setCartEntity(CartEntity cartEntity) {
        this.cartEntity = cartEntity;
    }

    // Constructor, Getter, Setter
    public UserEntity() {
    }

    public UserEntity(Long id, String name, String username, String password, String phone, String email, Timestamp dob, String gender, String avatar) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.avatar = avatar;
    }

    public UserEntity(Long id, String name, String username, String password, String phone, String email, Timestamp dob, String gender, String avatar, AuthenticationProvider auth_provider, List<AddressEntity> addressEntityList, List<OrderEntity> orderEntities, Set<RoleEntity> roleEntitySet, CartEntity cartEntity) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.avatar = avatar;
        this.auth_provider = auth_provider;
        this.addressEntityList = addressEntityList;
        this.orderEntities = orderEntities;
        this.roleEntitySet = roleEntitySet;
        this.cartEntity = cartEntity;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuthenticationProvider getAuth_provider() {
        return auth_provider;
    }

    public void setAuth_provider(AuthenticationProvider auth_provider) {
        this.auth_provider = auth_provider;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<RoleEntity> getRoleEntitySet() {
        return roleEntitySet;
    }

    public void setRoleEntitySet(Set<RoleEntity> roleEntitySet) {
        this.roleEntitySet = roleEntitySet;
    }
}
