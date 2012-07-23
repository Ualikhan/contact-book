package my.projects.contactbook.client;

import java.util.List;

import my.projects.contactbook.shared.model.Contact;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("myservices/contactBookService")
public interface GreetingService extends RemoteService {
	public Contact get(Long id);
	public List<Contact> list();
	public void update(Contact contact);
	public Long insert(Contact contact);
	public void delete(Contact contact);
	
}
