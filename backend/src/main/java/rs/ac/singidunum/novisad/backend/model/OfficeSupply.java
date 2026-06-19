package rs.ac.singidunum.novisad.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OfficeSupply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String name;
	private int quantity;
	private int issuedQuantity;

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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getIssuedQuantity() {
		return issuedQuantity;
	}
	public void setIssuedQuantity(int issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public OfficeSupply(Long id, String name, int quantity, int issuedQuantity) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.issuedQuantity = issuedQuantity;
	}

	public OfficeSupply() {
		super();
	}

}
