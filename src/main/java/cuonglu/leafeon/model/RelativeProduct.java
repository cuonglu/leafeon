package cuonglu.leafeon.model;

import cuonglu.leafeon.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeProduct extends Product {
    private Integer weight;
}
