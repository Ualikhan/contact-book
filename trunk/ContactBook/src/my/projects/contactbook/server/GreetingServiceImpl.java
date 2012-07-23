package my.projects.contactbook.server;

import java.util.List;

import my.projects.contactbook.client.GreetingService;
import my.projects.contactbook.server.dao.ContactBookDAO;
import my.projects.contactbook.server.util.GWTUtil;
import my.projects.contactbook.shared.FieldVerifier;
import my.projects.contactbook.shared.model.Contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@Service("contactBookService")
public class GreetingServiceImpl implements GreetingService {


	@Autowired
	private ContactBookDAO dao;

	@Transactional(readOnly=false)
	@Override
	public void delete(Contact contact) {
		dao.delete(contact);
	}

	@Transactional(readOnly=true)
	@Override
	public Contact get(Long id) {
		Contact contact = dao.get(id);
		if(contact != null) {
			contact.setPhones(GWTUtil.makeGWTSafe(contact.getPhones()));
		}
	
		return contact;
	}

	@Transactional(readOnly=true)
	@Override
	public List<Contact> list() {
		List<Contact> contacts = dao.list();
		for(Contact contact : contacts) {
			contact.setPhones(GWTUtil.makeGWTSafe(contact.getPhones()));
		}
		return contacts;
	}

	@Transactional(readOnly=false)
	@Override
	public void update(Contact contact) {
		dao.update(contact);
	}
	
	@Transactional(readOnly=false)
	@Override
	public Long insert(Contact contact) {
		return dao.insert(contact);
	}
}



