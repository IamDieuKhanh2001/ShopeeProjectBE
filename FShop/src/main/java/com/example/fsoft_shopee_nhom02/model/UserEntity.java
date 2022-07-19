package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Timestamp dob;
    private String gender;
    private String avatar;
    // Tạo quan hệ với AddressEntity
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AddressEntity> addressEntityList =new ArrayList<>();

    public List<AddressEntity> getAddressEntityList() {
        return addressEntityList;
    }

    public void setAddressEntityList(List<AddressEntity> addressEntityList) {
        this.addressEntityList = addressEntityList;
    }

    // Tạo quan hệ với RoleEntity
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles", //Tên bảng trung gian
            joinColumns = @JoinColumn(name = "userID"), //Khóa chính để liên kết với bảng Users
            inverseJoinColumns = @JoinColumn(name = "roleId")) //Khóa chính để liên kết với bảng Roles
    @JsonManagedReference
    private Set<RoleEntity> roleEntitySet; //Liên kết với mapedBy ở RoleEntity

    // Tạo quan hệ với CartEntity
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartId", referencedColumnName = "id")
    private CartEntity cartEntity; // Tương ứng với mappedBy ở CartEntity

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
