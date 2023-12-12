package com.example.demo.controllers.main;

import com.example.demo.models.*;
import com.example.demo.services.application.ApplicationService;
import com.example.demo.services.applicationType.ApplicationTypeService;
import com.example.demo.services.child.ChildService;
import com.example.demo.services.gender.GenderService;
import com.example.demo.services.groundsForFinPayment.GroundsForFinPaymentService;
import com.example.demo.services.materialPayment.MaterialPaymentService;
import com.example.demo.services.meetingMinute.MeetingMinuteService;
import com.example.demo.services.phoneNumber.PhoneNumberService;
import com.example.demo.services.position.PositionService;
import com.example.demo.services.publicOrganization.PublicOrganizationService;
import com.example.demo.services.unionMember.UnionMemberService;

import com.example.demo.services.user.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    PhoneNumber newPhoneNumber;
    @Autowired
    UserService userService;
    @Autowired
    ApplicationTypeService applicationTypeService;
    @Autowired
    GenderService genderService;
    @Autowired
    UnionMemberService unionMemberService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    GroundsForFinPaymentService groundsForFinPaymentService;
    @Autowired
    MaterialPaymentService materialPaymentService;
    @Autowired
    MeetingMinuteService meetingMinuteService;
    @Autowired
    PhoneNumberService phoneNumberService;
    @Autowired
    PublicOrganizationService publicOrganizationService;
    @Autowired
    PositionService positionService;
    @Autowired
    ChildService childService;

    @GetMapping({"/mainPage/index"})
    public String mainPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("newUnionMember", new UnionMember());
        model.addAttribute("updateUnionMember", new UnionMember());
        model.addAttribute("genders", genderService.readAll());
        model.addAttribute("positions", positionService.readAll());
        model.addAttribute("unionMembers", unionMemberService.readAll());
        List<UnionMember> unionMembersTable = unionMemberService.readAll();
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "mainPage/index";
    }

    @PostMapping("/mainPage/index/add")
    public String mainPageAdd(Model model, @ModelAttribute("newUnionMember") UnionMember newUnionMember, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        newUnionMember.setGender(genderService.readById(newUnionMember.getGender().getGenderId()));
        if (newUnionMember.getPosition().getPositionId() != null)
            newUnionMember.setPosition(positionService.readById(newUnionMember.getPosition().getPositionId()));
        newUnionMember.getPhoneNumbers().get(0).setUnionMember(newUnionMember);
        unionMemberService.create(newUnionMember);
        savePhoneNumber(newUnionMember);
        return "redirect:/mainPage/index";
    }

    @PostMapping("/mainPage/index/update/{id}")
    public String mainPageUpdate(Model model, @ModelAttribute("updateUnionMember") UnionMember updateUnionMember, Principal user, @PathVariable("id") Long unionMemberId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        updateUnionMember.setGender(genderService.readById(updateUnionMember.getGender().getGenderId()));
        if (updateUnionMember.getPosition().getPositionId() != null)
            updateUnionMember.setPosition(positionService.readById(updateUnionMember.getPosition().getPositionId()));
        updateUnionMember.getPhoneNumbers().get(0).setUnionMember(updateUnionMember);
        if (!Objects.equals(unionMemberService.readByName("").getUnionMemberId(), unionMemberId)) {
            unionMemberService.update(unionMemberId, updateUnionMember);
        }
        savePhoneNumber(updateUnionMember);
        return "redirect:/mainPage/index";
    }

    @GetMapping("/mainPage/index/delete/{id}")
    public String mainPageDelete(Model model, Principal user, @PathVariable("id") Long unionMemberId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        if (!Objects.equals(unionMemberService.readByName("").getUnionMemberId(), unionMemberId)) {
            unionMemberService.delete(unionMemberId);
        }
        return "redirect:/mainPage/index";
    }

    @GetMapping({"/childrenPage/index"})
    public String childrenPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("genders", genderService.readAll());
        model.addAttribute("newChild", new Child());
        model.addAttribute("updateChild", new Child());
        model.addAttribute("unionMembers", unionMemberService.readAll());
        model.addAttribute("parentsChildren", getParentChildList());
        List<UnionMember> unionMembersTable = unionMemberService.readAll();
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "childrenPage/index";
    }

    @PostMapping("/childrenPage/index/add")
    public String childrenPageAdd(Model model, @ModelAttribute("newChild") Child newChild, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        UnionMember unionMember = new UnionMember();
        unionMember.setUnionMemberId(0L);
        newChild.getUnionMembers().remove(unionMember);
        childService.create(newChild);
        return "redirect:/childrenPage/index";
    }

    @PostMapping("/childrenPage/index/update/{id}")
    public String childrenPageUpdate(Model model, @ModelAttribute("updateChild") Child updateChild, Principal user, @PathVariable("id") Long childId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        updateChild.setGender(genderService.readById(updateChild.getGender().getGenderId()));
        childService.update(childId, updateChild);
        return "redirect:/childrenPage/index";
    }

    @GetMapping("/childrenPage/index/delete/{id}")
    public String childrenPageDelete(Model model, Principal user, @PathVariable("id") Long childId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        childService.delete(childId);
        return "redirect:/childrenPage/index";
    }

    @GetMapping({"/unionMemPubOrgPage/index"})
    public String unionMemPubOrgPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("unionMembersPublicOrganizations", getPublicOrgUnionMemberList());
        model.addAttribute("unionMembers", unionMemberService.readAll());
        List<UnionMember> unionMembersTable = unionMemberService.readAll();
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        model.addAttribute("newMemberOrg", new PublicOrgUnionMember());
        model.addAttribute("updateMemberOrg", new PublicOrgUnionMember());
        model.addAttribute("publicOrganizations", publicOrganizationService.readAll());
        return "unionMemPubOrgPage/index";
    }

    @PostMapping({"/unionMemPubOrgPage/index/add"})
    public String unionMemPubOrgPageAdd(Model model, @ModelAttribute("newMemberOrg") PublicOrgUnionMember newMemberOrg, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        UnionMember unionMember = unionMemberService.readById(newMemberOrg.getUnionMember().getUnionMemberId());
        unionMember.getPublicOrganizations().add(newMemberOrg.getPublicOrganization());
        unionMemberService.update(unionMember.getUnionMemberId(), unionMember);
        return "redirect:/unionMemPubOrgPage/index";
    }

    @GetMapping("/unionMemPubOrgPage/index/delete/{unionMemberId}/{publicOrganizationId}")
    public String unionMemPubOrgPageDelete(Model model, Principal user, @PathVariable("unionMemberId") Long unionMemberId, @PathVariable("publicOrganizationId") Long publicOrganizationId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        UnionMember unionMember = unionMemberService.readById(unionMemberId);
        PublicOrganization publicOrganization = publicOrganizationService.readById(publicOrganizationId);
        unionMember.getPublicOrganizations().remove(publicOrganization);
        unionMemberService.update(unionMemberId, unionMember);
        return "redirect:/unionMemPubOrgPage/index";
    }

    @GetMapping({"/publicOrganizationsPage/index"})
    public String publicOrganizationsPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("publicOrganizations", publicOrganizationService.readAll());
        model.addAttribute("newPublicOrganization", new PublicOrganization());
        model.addAttribute("updatePublicOrganization", new PublicOrganization());
        return "publicOrganizationsPage/index";
    }

    @PostMapping("/publicOrganizationsPage/index/add")
    public String publicOrganizationsPageAdd(Model model, @ModelAttribute("newPublicOrganization") PublicOrganization newPublicOrganization, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        publicOrganizationService.create(newPublicOrganization);
        return "redirect:/publicOrganizationsPage/index";
    }

    @PostMapping("/publicOrganizationsPage/index/update/{id}")
    public String publicOrganizationsPageUpdate(Model model, @ModelAttribute("updatePublicOrganization") PublicOrganization updatePublicOrganization, Principal user, @PathVariable("id") Long publicOrganizationId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
//        updatePublicOrganization.setGender(genderService.readById(updateChild.getGender().getGenderId()));
        publicOrganizationService.update(publicOrganizationId, updatePublicOrganization);
        return "redirect:/publicOrganizationsPage/index";
    }

    @GetMapping("/publicOrganizationsPage/index/delete/{id}")
    public String publicOrganizationsPageDelete(Model model, Principal user, @PathVariable("id") Long publicOrganizationId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        publicOrganizationService.delete(publicOrganizationId);
        return "redirect:/publicOrganizationsPage/index";
    }

    @GetMapping({"/positionsPage/index"})
    public String positionsPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("newPosition", new Position());
        model.addAttribute("updatePosition", new Position());
        model.addAttribute("positions", positionService.readAll());
        List<Position> positionsTable = positionService.readAll();
        positionsTable.remove(positionService.readByTitle(""));
        model.addAttribute("positionsTable", positionsTable);
        return "positionsPage/index";
    }

    @PostMapping("/positionsPage/index/add")
    public String positionsPageAdd(Model model, @ModelAttribute("newPosition") Position newPosition, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        positionService.create(newPosition);
        return "redirect:/positionsPage/index";
    }

    @PostMapping("/positionsPage/index/update/{id}")
    public String positionsPageUpdate(Model model, @ModelAttribute("updatePosition") Position updatePosition, Principal user, @PathVariable("id") Long positionId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
//        updatePosition.ssetGender(genderService.readById(updateChild.getGender().getGenderId()));
        if (!Objects.equals(positionService.readByPositionTitle("").getPositionId(), positionId)) {
            positionService.update(positionId, updatePosition);
        }
        return "redirect:/positionsPage/index";
    }

    @GetMapping("/positionsPage/index/delete/{id}")
    public String positionsPageDelete(Model model, Principal user, @PathVariable("id") Long positionId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        positionService.delete(positionId);
        return "redirect:/positionsPage/index";
    }

    List<Application> applications;

    boolean inSearch = false;

    @GetMapping({"/unionMembersApplicationsPage/index"})
    public String unionMembersApplicationsPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        if(applications == null || !inSearch){
            applications = applicationService.readAll();
        }
        model.addAttribute("applications", applications);
        model.addAttribute("newApplication", new Application());
        model.addAttribute("updateApplication", new Application());
        model.addAttribute("searchUnionMember", new UnionMember());
        model.addAttribute("unionMembers", unionMemberService.readAll());
        model.addAttribute("applicationTypes", applicationTypeService.readAll());
        model.addAttribute("materialPayments", materialPaymentService.readAll());
        model.addAttribute("meetingMinutes", meetingMinuteService.readAll());
        List<UnionMember> unionMembersTable = unionMemberService.readAll();
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        inSearch = false;
        return "unionMembersApplicationsPage/index";
    }

    @PostMapping("/unionMembersApplicationsPage/index/add")
    public String unionMembersApplicationsPageAdd(Model model, @ModelAttribute("newApplication") Application newApplication, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationService.create(newApplication);
        return "redirect:/unionMembersApplicationsPage/index";
    }

    @PostMapping("/unionMembersApplicationsPage/index/update/{id}")
    public String unionMembersApplicationsPageUpdate(Model model, @ModelAttribute("updateApplication") Application updateApplication, Principal user, @PathVariable("id") Long applicationId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationService.update(applicationId, updateApplication);
        return "redirect:/unionMembersApplicationsPage/index";
    }

    @GetMapping("/unionMembersApplicationsPage/index/delete/{id}")
    public String unionMembersApplicationsPageDelete(Model model, Principal user, @PathVariable("id") Long applicationId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationService.delete(applicationId);
        return "redirect:/unionMembersApplicationsPage/index";
    }

    @GetMapping({"/searchApplicationPage/index/findBySurname"})
    public String searchApplicationPageBySurname(Model model, Principal user, @ModelAttribute("searchUnionMember") UnionMember searchUnionMember) {
        inSearch=true;
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applications = applicationService.readByUnionMemberSurname(searchUnionMember.getSurname());
        model.addAttribute("applications", applications);
        return "redirect:/unionMembersApplicationsPage/index";
    }

    @GetMapping({"/applicationTypesPage/index"})
    public String applicationTypesPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("applicationTypes", applicationTypeService.readAll());
        model.addAttribute("newApplicationType", new ApplicationType());
        model.addAttribute("updateApplicationType", new ApplicationType());
        return "applicationTypesPage/index";
    }

    @PostMapping("/applicationTypesPage/index/add")
    public String applicationTypesPageAdd(Model model, @ModelAttribute("newApplicationType") ApplicationType newApplicationType, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationTypeService.create(newApplicationType);
        return "redirect:/applicationTypesPage/index";
    }

    @PostMapping("/applicationTypesPage/index/update/{id}")
    public String applicationTypesPageUpdate(Model model, @ModelAttribute("updateApplicationType") ApplicationType updateApplicationType, Principal user, @PathVariable("id") Long applicationTypeId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationTypeService.update(applicationTypeId, updateApplicationType);
        return "redirect:/applicationTypesPage/index";
    }

    @GetMapping("/applicationTypesPage/index/delete/{id}")
    public String applicationTypesPageDelete(Model model, Principal user, @PathVariable("id") Long applicationTypeId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        applicationService.delete(applicationTypeId);
        return "redirect:/applicationTypesPage/index";
    }

    @GetMapping({"/paymentsAmountPage/index"})
    public String paymentsAmountPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("materialPayments", materialPaymentService.readAll());
        model.addAttribute("grounds", groundsForFinPaymentService.readAll());
        model.addAttribute("newMaterialPayment", new MaterialPayment());
        model.addAttribute("updateMaterialPayment", new MaterialPayment());
        return "paymentsAmountPage/index";
    }

    @PostMapping("/paymentsAmountPage/index/add")
    public String paymentsAmountPageAdd(Model model, @ModelAttribute("newMaterialPayment") MaterialPayment newMaterialPayment, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        newMaterialPayment.setGroundsForFinPayment(groundsForFinPaymentService.readById(newMaterialPayment.getGroundsForFinPayment().getGroundId()));
        materialPaymentService.create(newMaterialPayment);
        return "redirect:/paymentsAmountPage/index";
    }

    @PostMapping("/paymentsAmountPage/index/update/{id}")
    public String paymentsAmountPageUpdate(Model model, @ModelAttribute("updateMaterialPayment") MaterialPayment updateMaterialPayment, Principal user, @PathVariable("id") Long materialPaymentId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        materialPaymentService.update(materialPaymentId, updateMaterialPayment);
        return "redirect:/paymentsAmountPage/index";
    }

    @GetMapping("/paymentsAmountPage/index/delete/{id}")
    public String paymentsAmountPageDelete(Model model, Principal user, @PathVariable("id") Long materialPaymentId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        materialPaymentService.delete(materialPaymentId);
        return "redirect:/paymentsAmountPage/index";
    }

    @GetMapping({"/groundsForFinPayPage/index"})
    public String groundsForFinPayPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("groundsForFinPayments", groundsForFinPaymentService.readAll());
        model.addAttribute("newPayGround", new GroundsForFinPayment());
        model.addAttribute("updatePayGround", new GroundsForFinPayment());
        return "groundsForFinPayPage/index";
    }

    @PostMapping("/groundsForFinPayPage/index/add")
    public String groundsForFinPayPageAdd(Model model, @ModelAttribute("newPayGround") GroundsForFinPayment newPayGround, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        groundsForFinPaymentService.create(newPayGround);
        return "redirect:/groundsForFinPayPage/index";
    }

    @PostMapping("/groundsForFinPayPage/index/update/{id}")
    public String groundsForFinPayPageUpdate(Model model, @ModelAttribute("updatePayGround") GroundsForFinPayment updatePayGround, Principal user, @PathVariable("id") Long groundId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        groundsForFinPaymentService.update(groundId, updatePayGround);
        return "redirect:/groundsForFinPayPage/index";
    }

    @GetMapping("/groundsForFinPayPage/index/delete/{id}")
    public String groundsForFinPayPageDelete(Model model, Principal user, @PathVariable("id") Long groundId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        groundsForFinPaymentService.delete(groundId);
        return "redirect:/groundsForFinPayPage/index";
    }

    @GetMapping({"/meetingMinutesPage/index"})
    public String meetingMinutesPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("unionMembers", unionMemberService.readAll());
        System.out.println(unionMemberService.readAll());
        model.addAttribute("newMeetingMinute", new MeetingMinute());
        model.addAttribute("updateMeetingMinute", new MeetingMinute());
        model.addAttribute("meetingMinutes", meetingMinuteService.readAll());
        List<MeetingMinute> meetingMinutesTable = meetingMinuteService.readAll();
        meetingMinutesTable.remove(meetingMinuteService.readByMeetingMinuteNumber(0));
        model.addAttribute("meetingMinutesTable", meetingMinutesTable);
        return "meetingMinutesPage/index";
    }

    @PostMapping("/meetingMinutesPage/index/add")
    public String meetingMinutesPageAdd(Model model, @ModelAttribute("newMeetingMinute") MeetingMinute newMeetingMinute, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        meetingMinuteService.create(newMeetingMinute);
        return "redirect:/meetingMinutesPage/index";
    }

    @PostMapping("/meetingMinutesPage/index/update/{id}")
    public String meetingMinutesPageUpdate(Model model, @ModelAttribute("updateMeetingMinute") MeetingMinute updateMeetingMinute, Principal user, @PathVariable("id") Long meetingMinuteId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        meetingMinuteService.update(meetingMinuteId, updateMeetingMinute);
        return "redirect:/meetingMinutesPage/index";
    }

    @GetMapping("/meetingMinutesPage/index/delete/{id}")
    public String meetingMinutesPageDelete(Model model, Principal user, @PathVariable("id") Long meetingMinuteId) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        meetingMinuteService.delete(meetingMinuteId);
        return "redirect:/meetingMinutesPage/index";
    }

    List<UnionMember> unionMembersTable;

    @GetMapping({"/searchMemberPage/index"})
    public String searchMemberPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("positions", positionService.readAll());
        model.addAttribute("searchUnionMember", new UnionMember());
        model.addAttribute("unionMembers", unionMemberService.readAll());
        List<Position> positionsTable = positionService.readAll();
        positionsTable.remove(positionService.readByTitle(""));
        model.addAttribute("positionsTable", positionsTable);
        if(unionMembersTable == null){
            unionMembersTable = new ArrayList<>();
        }
        unionMembersTable.remove(unionMemberService.readByName(""));
        System.out.println(unionMembersTable);
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "searchMemberPage/index";
    }

    @GetMapping({"/searchMemberPage/index/findByPosition"})
    public String searchMemberPageByPosition(Model model, Principal user, @ModelAttribute("searchUnionMember") UnionMember searchUnionMember) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("positions", positionService.readAll());
        unionMembersTable = unionMemberService.readByPosition(searchUnionMember.getPosition().getPositionId());
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "redirect:/searchMemberPage/index";
    }

    @GetMapping({"/searchMemberPage/index/findBySurname"})
    public String searchMemberPageBySurname(Model model, Principal user, @ModelAttribute("searchUnionMember") UnionMember searchUnionMember) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("positions", positionService.readAll());
        unionMembersTable = unionMemberService.readBySurname(searchUnionMember.getSurname());
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "redirect:/searchMemberPage/index";
    }

    TableMode tableMode;

    @GetMapping({"/reportsPage/index"})
    public String reportsPage(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        model.addAttribute("parentsChildren", getChildren13());
        if(tableMode ==null){
            tableMode = new TableMode();
        }
        model.addAttribute("tableMode", tableMode);
        List<UnionMember> unionMembersTable = unionMemberService.readPensioners();
        unionMembersTable.remove(unionMemberService.readByName(""));
        model.addAttribute("unionMembersTable", unionMembersTable);
        return "reportsPage/index";
    }

    @GetMapping({"/reportsPage/index/children13"})
    public String reportsPageChildren13(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        tableMode.setMode(1);
        return "redirect:/reportsPage/index";
    }

    @GetMapping({"/reportsPage/index/pensioners"})
    public String reportsPagePensioners(Model model, Principal user) {
        model.addAttribute("checkUser", userService.findByUsername(user.getName()));
        tableMode.setMode(2);
        return "redirect:/reportsPage/index";
    }

    public List<ParentChild> getChildren13(){
        List<ParentChild> parentsChildren = getParentChildList();
        List<ParentChild> result = new ArrayList<>();
        for(ParentChild parentChild : parentsChildren){
            if(new Date().getTime() - parentChild.getChild().getBirthdate().getTime() < 441806400000L){
                result.add(parentChild);
            }
        }
        return result;
    }

    public List<ParentChild> getParentChildList() {
        List<UnionMember> unionMembers = unionMemberService.readAll();
        List<ParentChild> parentsChildren = new ArrayList<>();
        for (UnionMember unionMember : unionMembers) {
            if (!unionMember.getName().equals("")) {
                Set<Child> children = unionMember.getChildren();
                for (Child child : children) {
                    parentsChildren.add(new ParentChild(unionMember, child));
                }
            }
        }
        return parentsChildren;
    }

    public List<PublicOrgUnionMember> getPublicOrgUnionMemberList() {
        List<PublicOrganization> publicOrganizations = publicOrganizationService.readAll();
        List<PublicOrgUnionMember> publicOrgUnionMemberList = new ArrayList<>();
        for (PublicOrganization publicOrganization : publicOrganizations) {
            Set<UnionMember> unionMembers = publicOrganization.getUnionMembers();
            for (UnionMember unionMember : unionMembers) {
                publicOrgUnionMemberList.add(new PublicOrgUnionMember(publicOrganization, unionMember));
            }
        }
        return publicOrgUnionMemberList;
    }

    public PhoneNumber savePhoneNumber(UnionMember unionMember) {
        return phoneNumberService.create(unionMember.getPhoneNumbers().get(0));
    }

}
