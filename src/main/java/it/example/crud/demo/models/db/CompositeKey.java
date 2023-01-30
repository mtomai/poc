package it.example.crud.demo.models.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CompositeKey implements Serializable {

    @Column(name = "role", nullable = false)
    private String role;
    @Column(name = "operation", nullable = false)
    private String operation;
}
