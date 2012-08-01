package my.projects.contactbook.client;

import java.io.Serializable;
import java.util.List;

import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("myservices/contactBookService")
public interface GreetingService extends RemoteService {
	//public Contact get(Long id);
	public List<Contact> list(int pageNum);
	public List<Contact> listByQuery(String query,int pageNum);
	public void update(Contact contact);
	public Long insert(Contact contact);
	public void delete(Contact contact);	
	public List<Country> listCountry();
	public Contact get(Long id);
	Country getCountry(String name);
	City getCity(String name);
	public int listSize();
}
