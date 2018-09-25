package documentation_examples.model;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

import java.util.Date;

@Row(colsOrder = {"Firstname", "Lastname", "id", "birthDate"})
@Data
public class User {

    @Column(name = "Firstname")
    private String firstName;

    @Column(name = "Lastname")
    private String lastName;

    @Column
    private long id;

    @Column
    private Date birthDate;
}
