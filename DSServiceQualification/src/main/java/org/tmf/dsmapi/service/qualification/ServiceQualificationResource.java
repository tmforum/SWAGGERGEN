///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.tmf.dsmapi.service.qualification;
//
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.ejb.SessionContext;
//import javax.ejb.Stateless;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriInfo;
//import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
//import org.tmf.dsmapi.commons.utils.Jackson;
//import org.tmf.dsmapi.commons.utils.URIParser;
//import org.tmf.dsmapi.service.qualification.enity.ServiceQualificationReqEntity;
//import org.tmf.dsmapi.service.qualification.enity.ServiceQualificationReqItemEntity;
//import org.tmf.dsmapi.service.qualification.event.ServiceQualificationEventPublisherLocal;
//import org.tmf.dsmapi.service.qualification.model.ServiceQualificationItemRsp;
//import org.tmf.dsmapi.service.qualification.model.ServiceQualificationReq;
//import org.tmf.dsmapi.service.qualification.model.ServiceQualificationRsp;
//import org.tmf.dsmapi.service.qualification.model.ServiceQualificationRsp.QualificationStateEnum;
//import org.tmf.dsmapi.service.qualification.model.ServiceReq;
//import org.tmf.dsmapi.service.qualification.template.Template;
//import org.tmf.dsmapi.service.qualification.template.TemplateFactory;
//import org.tmf.dsmapi.service.qualification.template.TemplateNotFoundException;
//
///**
// *
// * @author Lin
// */
//@Stateless
////@Path("/v1/serviceQualification")
//public class ServiceQualificationResource {
//
//    @EJB
//    ServiceQualificationReqFacade serviceQualificationReqFacade;
//
//    @Resource
//    private SessionContext sessionContext;
//
//    @EJB
//    ServiceQualificationEventPublisherLocal publisher;
//
//    @GET
//    @Produces({"application/json"})
//    public List<ServiceQualificationReqEntity> findAll() {
//        return serviceQualificationReqFacade.findAll();
//    }
//
//    /**
//     *
//     * @param req
//     * @param info
//     * @return
//     * @throws UnknownResourceException
//     */
//    @POST
//    @Consumes({"application/json"})
//    @Produces({"application/json"})
//    public Response post(ServiceQualificationReq req, @Context UriInfo info) throws UnknownResourceException {
//
//        if (req == null) {
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
//        }
//        ServiceQualificationReqEntity serviceQualificationReqEntity = new ServiceQualificationReqEntity();
//        serviceQualificationReqEntity.setQualificationState(QualificationStateEnum.DONE);
//        Date now = new Date();
//        req.setInteractionDate(now);
//        req.setEligibilityDate(now);
//
//        List<ServiceQualificationItemRsp> itemRspList = null;
//        try {
//
//            itemRspList = renderServiceQualificationRsp(serviceQualificationReqEntity);
//            serviceQualificationReqFacade.create(serviceQualificationReqEntity);
//            serviceQualificationReqEntity.setHref(info.getAbsolutePath() + "/" + serviceQualificationReqEntity.getId());
//            serviceQualificationReqFacade.edit(serviceQualificationReqEntity);
//
//            publisher.createNotification(req, now);
//        } catch (Exception e) {
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
//        }
//
//        ServiceQualificationRsp rsp = ServiceQualificationConverter.convert(req);
//        rsp.setServiceQualificationItem(itemRspList);
//
//        // 201 OK
//        return Response.created(null).
//                entity(rsp).
//                build();
//    }
//
//    @GET
//    @Path("{id}")
//    @Produces({"application/json"})
//    public Response get(@PathParam("id") String id, @Context UriInfo info) throws UnknownResourceException {
//
//        // search queryParameters
//        MultivaluedMap<String, String> queryParameters = info.getQueryParameters();
//
//        Map<String, List<String>> mutableMap = new HashMap();
//        for (Map.Entry<String, List<String>> e : queryParameters.entrySet()) {
//            mutableMap.put(e.getKey(), e.getValue());
//        }
//
//        // fields to filter view
//        Set<String> fieldSet = URIParser.getFieldsSelection(mutableMap);
//
//        ServiceQualificationReqEntity req = serviceQualificationReqFacade.find(id);
//        List<ServiceQualificationItemRsp> itemRspList = null;
//        try {
//            itemRspList = renderServiceQualificationRsp(req);
//        } catch (Exception ex) {
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
//        }
//
//        Response response;
//        ServiceQualificationRsp rsp = req.convert();
//        rsp.setServiceQualificationItem(itemRspList);
//        if (fieldSet.isEmpty() || fieldSet.contains(URIParser.ALL_FIELDS)) {
//            response = Response.ok(rsp).build();
//        } else {
//            fieldSet.add(URIParser.ID_FIELD);
//            ObjectNode node = Jackson.createNode(rsp, fieldSet);
//            response = Response.ok(node).build();
//        }
//        return response;
//    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({"application/json"})
//    @Produces({"application/json"})
//    public Response update(@PathParam("id") Long id, ServiceQualificationReq request) throws UnknownResourceException {
//        Response response = null;
//        ServiceQualificationReqEntity serviceQualification = serviceQualificationReqFacade.find(id);
//        if (serviceQualification != null) {
//            serviceQualification.setId(id);
//            serviceQualificationReqFacade.edit(serviceQualification);
//            // 200 OK + location
//            response = Response.status(Response.Status.OK).entity(request).build();
//
//        } else {
//            // 404 not found
//            response = Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return response;
//    }
//
//    /**
//     *
//     * @param req
//     * @return
//     * @throws Exception
//     */
//    private List<ServiceQualificationItemRsp> renderServiceQualificationRsp(ServiceQualificationReqEntity req) throws Exception {
//        String templateName;
//        Template template;
//        ServiceReq serviceReq;
//        List<ServiceQualificationItemRsp> itemRspList = null;
//        List<ServiceQualificationReqItemEntity> items = req.getServiceQualificationItem();
//
//        String serviceCategoryId;
//        for (ServiceQualificationReqItemEntity item : items) {
//            if (item == null) {
//                continue;
//            }
//            serviceReq = item.getService();
//            if (serviceReq == null) {
//                continue;
//            }
//
//            if (item.getService().getServiceSpecification() != null) {
//                serviceCategoryId = item.getService().getServiceSpecification().getServiceCategoryId();
//            } else {
//                serviceCategoryId = "internetService";
//            }
//
//            templateName = TemplateFactory.getInstance().getTemplateName(serviceCategoryId);
//            if (templateName != null) {
//                template = (Template) sessionContext.lookup("java:module/" + templateName);
//                if (template != null) {
//                    itemRspList = template.mactch(ServiceQualificationConverter.convertSQRequest(req), serviceReq);
//                }
//            } else {
//                throw new TemplateNotFoundException();
//            }
//        }
//
//        return itemRspList;
//    }
//}
