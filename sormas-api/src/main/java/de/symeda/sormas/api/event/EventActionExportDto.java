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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.event;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.action.ActionPriority;
import de.symeda.sormas.api.action.ActionStatus;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.Order;

public class EventActionExportDto implements Serializable {

	private static final long serialVersionUID = 8231951545991794808L;

	public static final String I18N_PREFIX = "EventActionExport";

	private String eventUuid;
	private String eventTitle;
	private String eventDesc;
	private String eventDate;
	private EventStatus eventStatus;
	private EventInvestigationStatus eventInvestigationStatus;
	private String actionTitle;
	private Date actionCreationDate;
	private Date actionChangeDate;
	private ActionStatus actionStatus;
	private ActionPriority actionPriority;
	private UserReferenceDto actionReplyingUser;

	public EventActionExportDto(
		String eventUuid,
		String eventTitle,
		String eventDesc,
		Date eventStartDate,
		Date eventEndDate,
		EventStatus eventStatus,
		EventInvestigationStatus eventInvestigationStatus,
		String actionTitle,
		Date actionCreationDate,
		Date actionChangeDate,
		ActionStatus actionStatus,
		ActionPriority actionPriority,
		String actionReplyingUserUuid,
		String actionReplyingUserFirstName,
		String actionReplyingUserLastName) {

		this.eventUuid = eventUuid;
		this.eventTitle = eventTitle;
		this.eventDesc = eventDesc;
		this.eventDate = EventHelper.buildEventDateString(eventStartDate, eventEndDate);
		this.eventStatus = eventStatus;
		this.eventInvestigationStatus = eventInvestigationStatus;
		this.actionTitle = actionTitle;
		this.actionCreationDate = actionCreationDate;
		this.actionChangeDate = actionChangeDate;
		this.actionStatus = actionStatus;
		this.actionPriority = actionPriority;
		this.actionReplyingUser = new UserReferenceDto(actionReplyingUserUuid, actionReplyingUserFirstName, actionReplyingUserLastName, null);
	}

	@Order(0)
	public String getEventUuid() {
		return eventUuid;
	}

	@Order(1)
	public String getEventTitle() {
		return eventTitle;
	}

	@Order(2)
	public String getEventDesc() {
		return eventDesc;
	}

	@Order(3)
	public String getEventDate() {
		return eventDate;
	}

	@Order(4)
	public EventStatus getEventStatus() {
		return eventStatus;
	}

	@Order(5)
	public EventInvestigationStatus getEventInvestigationStatus() {
		return eventInvestigationStatus;
	}

	@Order(6)
	public String getActionTitle() {
		return actionTitle;
	}

	@Order(7)
	public Date getActionCreationDate() {
		return actionCreationDate;
	}

	@Order(8)
	public Date getActionChangeDate() {
		return actionChangeDate;
	}

	@Order(9)
	public ActionStatus getActionStatus() {
		return actionStatus;
	}

	@Order(10)
	public ActionPriority getActionPriority() {
		return actionPriority;
	}

	@Order(11)
	public UserReferenceDto getActionReplyingUser() {
		return actionReplyingUser;
	}
}