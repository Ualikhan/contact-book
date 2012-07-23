package my.projects.contactbook.shared;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import my.projects.contactbook.shared.model.Contact;

public class Phone implements Serializable{

	private Long id;
	public Long getId() {
		return id;
	   }
    
	public String getNumber() {
		return number;
	}
    public void setNumber(String number) {
		this.number = number;
	}
	private  String number;
    
	 public String getType() {
			return type;
		}
	    public void setType(String type) {
			this.type = type;
		}
	private  String type;
    
   public Phone(){}

   @Override
   public String toString(){
	   return this.number+"("+this.type+")";
   }
  }
