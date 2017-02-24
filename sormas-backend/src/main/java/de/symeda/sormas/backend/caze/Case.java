package de.symeda.sormas.backend.caze;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.InvestigationStatus;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.facility.Facility;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.region.Community;
import de.symeda.sormas.backend.region.District;
import de.symeda.sormas.backend.region.Region;
import de.symeda.sormas.backend.symptoms.Symptoms;
import de.symeda.sormas.backend.task.Task;
import de.symeda.sormas.backend.user.User;

@Entity(name="cases")
public class Case extends AbstractDomainObject {
	
	private static final long serialVersionUID = -2697795184663562129L;

	public static final String CASE_CLASSIFICATION = "caseClassification";
	public static final String INVESTIGATION_STATUS = "investigationStatus";
	public static final String PERSON = "person";
	public static final String DISEASE = "disease";
	public static final String HEALTH_FACILITY = "healthFacility";
	public static final String REPORTING_USER = "reportingUser";
	public static final String REPORT_DATE = "reportDate";
	public static final String INVESTIGATED_DATE = "investigatedDate";
	public static final String SURVEILLANCE_OFFICER = "surveillanceOfficer";
	public static final String CASE_OFFICER = "caseOfficer";
	public static final String CONTACT_OFFICER = "contactOfficer";
	public static final String SYMPTOMS = "symptoms";
	public static final String TASKS = "tasks";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String HOSPITALIZATION = "hospitalization";

	private Person person;
	private String description;
	private Disease disease;
	private CaseClassification caseClassification;
	private InvestigationStatus investigationStatus;
	private Hospitalization hospitalization;
	
	private Region region;
	private District district;
	private Community community;
	private Facility healthFacility;
	
	private User reportingUser;
	private Date reportDate;
	private Date investigatedDate;
	private Date suspectDate;
	private Date confirmedDate;
	private Date negativeDate;
	private Date postiveDate;
	private Date noCaseDate;
	private Date recoveredDate;
	
	private Location illLocation;
	
	private User surveillanceOfficer;
	private User caseOfficer;
	private User contactOfficer;
	
	private Symptoms symptoms;
	
	private List<Task> tasks;
	
	@ManyToOne(cascade = {})
	@JoinColumn(nullable=false)
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Column(length=512)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Enumerated(EnumType.STRING)
	public Disease getDisease() {
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public CaseClassification getCaseClassification() {
		return caseClassification;
	}
	public void setCaseClassification(CaseClassification caseStatus) {
		this.caseClassification = caseStatus;
	}
	
	@ManyToOne(cascade = {})
	@JoinColumn(nullable=false)
	public User getReportingUser() {
		return reportingUser;
	}
	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getInvestigatedDate() {
		return investigatedDate;
	}
	public void setInvestigatedDate(Date investigatedDate) {
		this.investigatedDate = investigatedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSuspectDate() {
		return suspectDate;
	}
	public void setSuspectDate(Date suspectDate) {
		this.suspectDate = suspectDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getConfirmedDate() {
		return confirmedDate;
	}
	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getNegativeDate() {
		return negativeDate;
	}
	public void setNegativeDate(Date negativeDate) {
		this.negativeDate = negativeDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPostiveDate() {
		return postiveDate;
	}
	public void setPostiveDate(Date postiveDate) {
		this.postiveDate = postiveDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getNoCaseDate() {
		return noCaseDate;
	}
	public void setNoCaseDate(Date noCaseDate) {
		this.noCaseDate = noCaseDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecoveredDate() {
		return recoveredDate;
	}
	public void setRecoveredDate(Date recoveredDate) {
		this.recoveredDate = recoveredDate;
	}
	
	@ManyToOne(cascade = {})
	public Facility getHealthFacility() {
		return healthFacility;
	}
	public void setHealthFacility(Facility healthFacility) {
		this.healthFacility = healthFacility;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public Location getIllLocation() {
		if (illLocation == null) {
			illLocation = new Location();
		}
		return illLocation;
	}
	public void setIllLocation(Location illLocation) {
		this.illLocation = illLocation;
	}
	
	@ManyToOne(cascade = {})
	public User getSurveillanceOfficer() {
		return surveillanceOfficer;
	}
	public void setSurveillanceOfficer(User surveillanceOfficer) {
		this.surveillanceOfficer = surveillanceOfficer;
	}

	@ManyToOne(cascade = {})
	public User getCaseOfficer() {
		return caseOfficer;
	}
	public void setCaseOfficer(User caseOfficer) {
		this.caseOfficer = caseOfficer;
	}

	@ManyToOne(cascade = {})
	public User getContactOfficer() {
		return contactOfficer;
	}
	public void setContactOfficer(User contactOfficer) {
		this.contactOfficer = contactOfficer;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Symptoms getSymptoms() {
		if (symptoms == null) {
			symptoms = new Symptoms();
		}
		return symptoms;
	}
	public void setSymptoms(Symptoms symptoms) {
		this.symptoms = symptoms;
	}
	
	@ManyToOne(cascade = {})
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}

	@ManyToOne(cascade = {})
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}

	@ManyToOne(cascade = {})
	public Community getCommunity() {
		return community;
	}
	public void setCommunity(Community community) {
		this.community = community;
	}
	
	// It's necessary to do a lazy fetch here because having three eager fetching one to one relations
	// produces an error where two non-xa connections are opened
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Hospitalization getHospitalization() {
		if (hospitalization == null) {
			hospitalization = new Hospitalization();
			hospitalization.setHealthFacility(this.healthFacility);
		}
		return hospitalization;
	}
	public void setHospitalization(Hospitalization hospitalization) {
		this.hospitalization = hospitalization;
	}
	
	@Override
	public String toString() {
		return getPerson().toString() + " (" + DataHelper.getShortUuid(getUuid()) + ")";
	}
	
	@OneToMany(cascade = {}, mappedBy = Task.CAZE)
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}
	public void setInvestigationStatus(InvestigationStatus investigationStatus) {
		this.investigationStatus = investigationStatus;
	}
}
