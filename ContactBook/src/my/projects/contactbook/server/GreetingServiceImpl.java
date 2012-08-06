package my.projects.contactbook.server;

import java.io.Serializable;
import java.util.List;

import my.projects.contactbook.client.GreetingService;
import my.projects.contactbook.server.dao.ContactBookDAO;
import my.projects.contactbook.server.util.GWTUtil;
import my.projects.contactbook.shared.FieldVerifier;
import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;

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
	public int getContactListSize() {
		int listSize = dao.listSize();
		return listSize;
	}
	

	@Transactional(readOnly=true)
	@Override
	public List<Contact> getContactList(int pageNum) {
		List<Contact> contacts = dao.list();
		for(Contact contact : contacts) {
			contact.setPhones(GWTUtil.makeGWTSafe(contact.getPhones()));
		}
		return contacts;
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<Contact> getContactListByQuery(String query,int pageNum) {
		List<Contact> contacts = dao.listByQuery(query,pageNum);
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



	@Transactional(readOnly=true)
	@Override
	public List<Country> listCountry(int pageNum) {
		List<Country> countries = dao.listCountry(pageNum);
		for(Country country : countries) {
			country.setCities(GWTUtil.makeGWTSafe(country.getCities()));
		}
		return countries;
	}


	@Transactional(readOnly=true)
	@Override
	public Contact get(Long id) {
		// TODO Auto-generated method stub
		Contact t=dao.get(id);
		return t;
	}

	
	@Transactional(readOnly=true)
	public Country getCountry(String name) {
		// TODO Auto-generated method stub
		Country country=dao.getCountry(name);
		country.setCities(GWTUtil.makeGWTSafe(country.getCities()));
		return country;
	}

	@Transactional(readOnly=true)
	public City getCity(String name) {
		// TODO Auto-generated method stub
		City city=dao.getCity(name);
		return city;
	}
}



