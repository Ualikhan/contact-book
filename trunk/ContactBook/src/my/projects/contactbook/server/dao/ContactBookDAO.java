package my.projects.contactbook.server.dao;

import java.util.List;

import my.projects.contactbook.shared.model.Contact;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContactBookDAO {

	@Autowired
	private HibernateTemplate hibernate;
	
	public Contact get(Long id) {
		return hibernate.get(Contact.class, id);
	}
	
	public void delete(Contact entity) {
		hibernate.delete(entity);
	}
	
	public Long insert(Contact entity) {
		return (Long) hibernate.save(entity);
	}
	
	public void update(Contact entity) {
		 hibernate.update(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Contact> list() {
		String queryStr = "from " + Contact.class.getSimpleName() + " fetch all properties";
		Query query = getCurrentSession().createQuery(queryStr);
		return (List<Contact>)query.list();
	}
	
	private Session getCurrentSession() {
		return hibernate.getSessionFactory().getCurrentSession();
	}
}




