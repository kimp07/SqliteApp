package org.alex.sqlite.entity;

import org.alex.sqliteapp.db.Entity;
import org.alex.sqliteapp.db.EntityField;
import org.alex.sqliteapp.db.EntityObject;

@EntityObject(
        query = "SELECT * FROM GOODS",
        countQuery = "SELECT count(*) FROM GOODS"
)
public class Good implements Entity {

    @EntityField(fieldName = "_id")
    private Long id;
    @EntityField(fieldName = "NAME")
    private String name;
    @EntityField(fieldName = "FULLNAME")
    private String fullName;
    @EntityField(fieldName = "VAT")
    private Integer vat;
    @EntityField(fieldName = "BARCODE")
    private String barCode;

    public Good() {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
;

}
