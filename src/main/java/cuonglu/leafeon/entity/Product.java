package cuonglu.leafeon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @Column(length = 7)
    private String id;

    @Column(length = 100)
    private String name;

    private int price;

    private Date createDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category categoryId;
}