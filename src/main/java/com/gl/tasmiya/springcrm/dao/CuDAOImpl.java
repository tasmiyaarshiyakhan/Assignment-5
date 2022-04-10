package com.greatlearning.tasmiya.springcrm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.veeshostak.springcrm.entity.Customer;

// @repository required for DAO
// component scan will find  the repository, handle exception translation
@Repository 
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;
			
	@Override
	public List<Customer> getCustomers() {
		
		// get the current hibernate session (Note: session factory was injected using @autowired)
		Session currentSession = sessionFactory.getCurrentSession();

		// create a query
		Query<Customer> theQuery = 
				currentSession.createQuery("FROM Customer c ORDER BY c.firstName ASC, c.lastName ASC", Customer.class);
		
		// execute query and get list
		List<Customer> customers = theQuery.getResultList();
				
		// return the results		
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		
		// spring saveOrUpdate() determines to save or update
		// save: insert new customer if new pk
		// update: update existing customer if pk exists
		currentSession.saveOrUpdate(theCustomer);
		
	}

	@Override
	public Customer getCustomer(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// retrieve from db using the primary key
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete from db with pk(id)
		Query theQuery = 
				currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
	
	    // get the current hibernate session
	    Session currentSession = sessionFactory.getCurrentSession();
	    
	    Query theQuery = null;
	    
	    if (theSearchName != null && theSearchName.trim().length() > 0) {
	
	        // search for firstName or lastName
	    	
	        // the "like" clause and the "%" wildcard characters,
	    		// allow us to search for substrings (ex. pat returns paterson, patel, ...)
	        theQuery =currentSession.createQuery("FROM Customer c WHERE firstName LIKE :theName or lastName LIKE :theName", Customer.class);
	        theQuery.setParameter("theName", "%" + theSearchName + "%");
	
	    }
	    else {
	        // theSearchName is empty, get all customers
	        theQuery =currentSession.createQuery("from Customer", Customer.class);            
	    }
	    
	    // execute query and get result list
	    List<Customer> customers = theQuery.getResultList();
	                 
	    return customers;
	    
	}

}



