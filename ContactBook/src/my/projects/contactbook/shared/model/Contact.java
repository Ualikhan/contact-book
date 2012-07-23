package my.projects.contactbook.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import my.projects.contactbook.shared.Phone;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Entity
public class Contact implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	private String surname;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
	
	@ElementCollection
	@Cascade(CascadeType.ALL)
	private List<Phone> phones;
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	public void addPhone(Phone phone){
		if(this.phones==null)
			this.phones=new ArrayList<Phone>();
		this.phones.add(phone);
	}
	
	public Contact() {}
		
	@Transient
	public String getPhonesAsString() {
		String phones = "";
		boolean first = true;
		for(Phone phone : getPhones()) {
			if(!first)
				phones += ",";
			if(phone != null)
				phones += phone; 
			first = false;
		}
		return phones;
	}
	
}	

