/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.contact;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.BaseCriteria;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.facility.FacilityReferenceDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.api.utils.IgnoreForUrl;

public class ContactCriteria extends BaseCriteria implements Serializable {

	private static final long serialVersionUID = 5114202107622217837L;

	private UserRole reportingUserRole;
	private Disease disease;
	private CaseReferenceDto caze;
	private RegionReferenceDto caseRegion;
	private DistrictReferenceDto caseDistrict;
	private FacilityReferenceDto caseFacility;
	private UserReferenceDto contactOfficer;
	private ContactClassification contactClassification;
	private ContactStatus contactStatus;
	private FollowUpStatus followUpStatus;
	private Date reportDateFrom;
	private Date reportDateTo;
	private Date followUpUntilFrom;
	private Date followUpUntilTo;
	private Date lastContactDateFrom;
	private Date lastContactDateTo;
	private Boolean deleted = Boolean.FALSE;
	private String nameUuidCaseLike;
	private EntityRelevanceStatus relevanceStatus;
	private Boolean onlyHighPriorityContacts;
	private PersonReferenceDto person;
	
	public UserRole getReportingUserRole() {
		return reportingUserRole;
	}

	public ContactCriteria reportingUserRole(UserRole reportingUserRole) {
		this.reportingUserRole = reportingUserRole;
		return this;
	}

	public Disease getDisease() {
		return disease;
	}

	public ContactCriteria disease(Disease disease) {
		this.disease = disease;
		return this;
	}

	public CaseReferenceDto getCaze() {
		return caze;
	}

	public ContactCriteria caze(CaseReferenceDto caze) {
		this.caze = caze;
		return this;
	}

	public RegionReferenceDto getCaseRegion() {
		return caseRegion;
	}

	public ContactCriteria caseRegion(RegionReferenceDto caseRegion) {
		this.caseRegion = caseRegion;
		return this;
	}

	public DistrictReferenceDto getCaseDistrict() {
		return caseDistrict;
	}

	public ContactCriteria caseDistrict(DistrictReferenceDto caseDistrict) {
		this.caseDistrict = caseDistrict;
		return this;
	}

	public FacilityReferenceDto getCaseFacility() {
		return caseFacility;
	}

	public ContactCriteria caseFacility(FacilityReferenceDto caseFacility) {
		this.caseFacility = caseFacility;
		return this;
	}

	public UserReferenceDto getContactOfficer() {
		return contactOfficer;
	}

	public ContactCriteria contactOfficer(UserReferenceDto contactOfficer) {
		this.contactOfficer = contactOfficer;
		return this;
	}

	public ContactClassification getContactClassification() {
		return contactClassification;
	}

	public ContactCriteria contactClassification(ContactClassification contactClassification) {
		this.contactClassification = contactClassification;
		return this;
	}

	public ContactStatus getContactStatus() {
		return contactStatus;
	}

	public ContactCriteria contactStatus(ContactStatus contactStatus) {
		this.contactStatus = contactStatus;
		return this;
	}

	public FollowUpStatus getFollowUpStatus() {
		return followUpStatus;
	}

	public ContactCriteria followUpStatus(FollowUpStatus followUpStatus) {
		this.followUpStatus = followUpStatus;
		return this;
	}
	
	public ContactCriteria reportDateBetween(Date reportDateFrom, Date reportDateTo) {
		this.reportDateFrom = reportDateFrom;
		this.reportDateTo = reportDateTo;
		return this;
	}

	public ContactCriteria reportDateFrom(Date reportDateFrom) {
		this.reportDateFrom = reportDateFrom;
		return this;
	}

	public Date getReportDateFrom() {
		return reportDateFrom;
	}

	public ContactCriteria reportDateTo(Date reportDateTo) {
		this.reportDateTo = reportDateTo;
		return this;
	}

	public Date getReportDateTo() {
		return reportDateTo;
	}
	
	public ContactCriteria lastContactDateBetween(Date lastContactDateFrom, Date lastContactDateTo) {
		this.lastContactDateFrom = lastContactDateFrom;
		this.lastContactDateTo = lastContactDateTo;
		return this;
	}

	public ContactCriteria lastContactDateFrom(Date lastContactDateFrom) {
		this.lastContactDateFrom = lastContactDateFrom;
		return this;
	}

	public Date getLastContactDateFrom() {
		return lastContactDateFrom;
	}

	public ContactCriteria lastContactDateTo(Date lastContactDateTo) {
		this.lastContactDateTo = lastContactDateTo;
		return this;
	}

	public Date getLastContactDateTo() {
		return lastContactDateTo;
	}
	
	public ContactCriteria followUpUntilBetween(Date followUpUntilFrom, Date followUpUntilTo) {
		this.followUpUntilFrom = followUpUntilFrom;
		this.followUpUntilTo = followUpUntilTo;
		return this;
	}

	public ContactCriteria followUpUntilFrom(Date followUpUntilFrom) {
		this.followUpUntilFrom = followUpUntilFrom;
		return this;
	}
	
	public Date getFollowUpUntilFrom() {
		return followUpUntilFrom;
	}

	public ContactCriteria followUpUntilTo(Date followUpUntilTo) {
		this.followUpUntilTo = followUpUntilTo;
		return this;
	}
	
	public Date getFollowUpUntilTo() {
		return followUpUntilTo;
	}
	
	public ContactCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}
	
	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}
	
	public ContactCriteria deleted(Boolean deleted) {
		this.deleted = deleted;
		return this;
	}

	@IgnoreForUrl
	public Boolean getDeleted() {
		return deleted;
	}
	
	/**
	 * returns all entries that match ALL of the passed words
	 */
	public ContactCriteria nameUuidCaseLike(String nameUuidCaseLike) {
		this.nameUuidCaseLike = nameUuidCaseLike;
		return this;
	}

	@IgnoreForUrl
	public String getNameUuidCaseLike() {
		return nameUuidCaseLike;
	}

	public Boolean getOnlyHighPriorityContacts() {
		return onlyHighPriorityContacts;
	}

	public ContactCriteria onlyHighPriorityContacts(Boolean onlyHighPriorityContacts) {
		this.onlyHighPriorityContacts = onlyHighPriorityContacts;
		return this;
	}	
	
	public ContactCriteria person(PersonReferenceDto person) {
		this.person = person;
		return this;
	}
	
	public PersonReferenceDto getPerson() {
		return person;
	}
}
