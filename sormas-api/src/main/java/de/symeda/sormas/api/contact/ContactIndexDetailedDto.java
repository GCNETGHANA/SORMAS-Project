package de.symeda.sormas.api.contact;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.user.UserReferenceDto;

public class ContactIndexDetailedDto extends ContactIndexDto {
	
	private static final long serialVersionUID = 577830364406605991L;
	
	public static final String SEX = "sex";
	public static final String APPROXIMATE_AGE = "approximateAge";
	public static final String DISTRICT_NAME = "districtName";
	public static final String CITY = "city";
	public static final String ADDRESS = "address";
	public static final String POSTAL_CODE = "postalCode";
	public static final String PHONE = "phone";
	public static final String REPORTING_USER = "reportingUser";

	private Sex sex;
	private String approximateAge;
	private String districtName;
	private String city;
	private String address;
	private String postalCode;
	private String phone;
	private UserReferenceDto reportingUser;

	public ContactIndexDetailedDto(String uuid, String firstName, String lastName, String cazeUuid, Disease disease, String diseaseDetails,
								   String caseFirstName, String caseLastName, String regionUuid, String districtUuid,
								   Date lastContactDate, ContactCategory contactCategory, ContactProximity contactProximity,
								   ContactClassification contactClassification, ContactStatus contactStatus,
								   FollowUpStatus followUpStatus, Date followUpUntil, String contactOfficerUuid, Date reportDateTime,
								   CaseClassification caseClassification,
								   Sex sex, Integer approximateAge, ApproximateAgeType approximateAgeType,
								   String districtName, String city, String address, String postalCode, String phone,
								   String reportingUserUuid, String reportingUserFirstName, String reportingUserLastName, int visitCount) {
		super(uuid, firstName, lastName, cazeUuid, disease, diseaseDetails, caseFirstName, caseLastName,
				regionUuid, districtUuid, lastContactDate, contactCategory, contactProximity, contactClassification,
				contactStatus, followUpStatus, followUpUntil, contactOfficerUuid, reportDateTime, caseClassification, visitCount);
		this.sex = sex;
		this.approximateAge = ApproximateAgeType.ApproximateAgeHelper.formatApproximateAge(approximateAge, approximateAgeType);
		this.districtName = districtName;
		this.city = city;
		this.address = address;
		this.postalCode = postalCode;
		this.phone = phone;
		this.reportingUser = new UserReferenceDto(reportingUserUuid, reportingUserFirstName, reportingUserLastName,  null);
	}

	public Sex getSex() {
		return sex;
	}

	public String getApproximateAge() {
		return approximateAge;
	}

	public String getDistrictName() {
		return districtName;
	}

	public String getCity() {
		return city;
	}

	public String getAddress() {
		return address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}
	
}
