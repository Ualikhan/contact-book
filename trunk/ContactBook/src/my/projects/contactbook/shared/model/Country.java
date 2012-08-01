package my.projects.contactbook.shared.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class Country implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NAME",nullable=false,unique=true)
	private String name;
	
	@Column(name="CODE",nullable=false)
	private int code;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="country")
	List<City> cities;
	
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public List<City> getCities() {
		return cities;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
