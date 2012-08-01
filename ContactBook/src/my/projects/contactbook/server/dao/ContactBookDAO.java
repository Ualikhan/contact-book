package my.projects.contactbook.server.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.OrderBy;

import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;

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
		return hibernate.get(Contact.class,id);
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
	public List<Contact> list(int pageNum) {
		String queryStr = "SELECT c FROM Contact c order by c.id";
		System.out.println(queryStr);
		Query query = getCurrentSession().createQuery(queryStr).setFirstResult(pageNum).setMaxResults(10);
		return (List<Contact>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public int listSize() {
		String queryStr = "SELECT c FROM Contact c order by c.id";
		System.out.println(queryStr);
		Query query = getCurrentSession().createQuery(queryStr);
		return query.list().size();
	}
	
	@SuppressWarnings("unchecked")
	public List<Contact> listByQuery(String q,int pageNum) {
		Query query;
		q=q.trim().replaceAll(" +", " ");
		System.out.println(q);
		if(q.contains(" ")){
			String first=q.substring(0, q.indexOf(" "));
			String second=q.substring(q.indexOf(" ")+1);
		String queryStr = "SELECT c FROM Contact c order by c.id  where ((c.name like '%"+first+"%') AND (c.surname like '%"+second+"%')) OR ((c.name like '%"+second+"%') AND (c.surname like '%"+first+"%'))  order by c.id";
		System.out.println(queryStr);
		query = getCurrentSession().createQuery(queryStr).setFirstResult(pageNum).setMaxResults(10);
		}
		else{
			
			String queryStr = "SELECT c FROM Contact c order by c.id  where (c.name like '%"+q+"%') OR (c.surname like '%"+q+"%') order by c.id";
			System.out.println(queryStr);
			query = getCurrentSession().createQuery(queryStr).setFirstResult(pageNum).setMaxResults(10);
		}
		return (List<Contact>)query.list();
	}
	
	public Country getCountry(String name) {
		String queryStr = "from " + Country.class.getSimpleName() + " fetch all properties where name='"+name+"'";
		System.out.println(queryStr);
		Query query = getCurrentSession().createQuery(queryStr);
		return (Country)query.list().get(0);
		
	}
	
	public City getCity(String name) {
		String queryStr = "from " + City.class.getSimpleName() + " fetch all properties where name='"+name+"'";
		System.out.println(queryStr);
		Query query = getCurrentSession().createQuery(queryStr);
		return (City)query.list().get(0);
		
	}
	
	public void deleteCountry(Country entity) {
		hibernate.delete(entity);
	}
	
	public Long insertCountry(Country entity) {
		return (Long) hibernate.save(entity);
	}
	
	public void updateCountry(Country entity) {
		 hibernate.update(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Country> listCountry() {
		String queryStr = "from " + Country.class.getSimpleName() + " fetch all properties order by id";
		System.out.println(queryStr);
		Query query = getCurrentSession().createQuery(queryStr);
		return (List<Country>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Country> listByQueryCountry(String q) {
		Query query;
		q=q.trim().replaceAll(" +", " ");
		System.out.println(q);
		String queryStr = "from " + Country.class.getSimpleName() + " fetch all properties  where name like '%"+q+"%' order by id";
		query = getCurrentSession().createQuery(queryStr);
		
		return (List<Country>)query.list();
	}
	
	private Session getCurrentSession() {
		return hibernate.getSessionFactory().getCurrentSession();
	}

}




