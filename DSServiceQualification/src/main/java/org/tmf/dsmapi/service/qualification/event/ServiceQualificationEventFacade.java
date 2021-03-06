/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.service.qualification.event;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.tmf.dsmapi.commons.facade.AbstractFacade;

/**
 *
 * @author Lin
 */
@Stateless
public class ServiceQualificationEventFacade extends AbstractFacade<ServiceQualificationEvent>{
    
    @PersistenceContext(unitName = "DSServiceQualificationPU")
    private EntityManager em;
    /**
     *
     */
    public ServiceQualificationEventFacade() {
        super(ServiceQualificationEvent.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
