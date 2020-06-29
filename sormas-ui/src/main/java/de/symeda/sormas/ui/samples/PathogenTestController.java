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
package de.symeda.sormas.ui.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.vaadin.server.Page;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestFacade;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleIndexDto;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.CommitListener;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.DeleteListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class PathogenTestController {

	private PathogenTestFacade facade = FacadeProvider.getPathogenTestFacade();

	public PathogenTestController() {
	}

	public List<PathogenTestDto> getPathogenTestsBySample(SampleReferenceDto sampleRef) {
		return facade.getAllBySample(sampleRef);
	}

	public void create(

		SampleReferenceDto sampleRef,
		int caseSampleCount,
		Runnable callback,
		BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest) {
		SampleDto sampleDto = FacadeProvider.getSampleFacade().getSampleByUuid(sampleRef.getUuid());
		PathogenTestForm createForm = new PathogenTestForm(sampleDto, true, caseSampleCount);
		createForm.setValue(PathogenTestDto.build(sampleDto, UserProvider.getCurrent().getUser()));
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<PathogenTestForm>(
			createForm,
			UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_CREATE),
			createForm.getFieldGroup());

		editView.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				if (!createForm.getFieldGroup().isModified()) {
					savePathogenTest(createForm.getValue(), onSavedPathogenTest, null);
					callback.run();
				}
			}
		});

		VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingCreatePathogenTestResult));
	}

	public void showBulkTestResultComponent(Collection<? extends SampleIndexDto> selectedSamples, Disease disease) {

		if (selectedSamples.size() == 0) {
			new Notification(
				I18nProperties.getString(Strings.headingNoSamplesSelected),
				I18nProperties.getString(Strings.messageNoSamplesSelected),
				Type.WARNING_MESSAGE,
				false).show(Page.getCurrent());
			return;
		}

		SampleIndexDto firstSelectedSample = selectedSamples.stream().findFirst().orElse(null);
		SampleDto sampleDto = FacadeProvider.getSampleFacade().getSampleByUuid(firstSelectedSample.getUuid());
		
		// Create a temporary pathogenTest in order to use the CommitDiscardWrapperComponent
		PathogenTestDto bulkResultData = PathogenTestDto.build(sampleDto, UserProvider.getCurrent().getUser());
		
		//set defaults
		bulkResultData.setTestDateTime(new java.util.Date());
		bulkResultData.setTestedDisease(disease);
		bulkResultData.setTestResultVerified(true);

		PathogenTestForm form = new PathogenTestForm(sampleDto, true, 1);
		form.setValue(bulkResultData);
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<PathogenTestForm>(
			form,
			UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_CREATE),
			form.getFieldGroup());

		Window popupWindow = VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingCreateBulkTests));

		editView.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				PathogenTestDto updatedBulkResultData = form.getValue();
				bulkCreate(selectedSamples, updatedBulkResultData);
				
				popupWindow.close();
				Notification.show(I18nProperties.getString(Strings.messageTestsCreated), Type.HUMANIZED_MESSAGE);
			}
		});
	}
	
	private void bulkCreate(
		Collection<? extends SampleIndexDto> selectedSamples,
		PathogenTestDto updatedBulkResultData) {
		
		Collection<CaseDataDto> casesToClassify = new ArrayList<CaseDataDto>();
		Collection<CaseDataDto> casesToClone = new ArrayList<CaseDataDto>();
		Collection<SampleIndexDto> samplesToUpdate = new ArrayList<SampleIndexDto>();

		for (SampleIndexDto sample : selectedSamples) {
			
			updatedBulkResultData.setUuid(DataHelper.createUuid());
			updatedBulkResultData.setSample(sample.toReference());
			
			savePathogenTest(updatedBulkResultData, null, (action, caze) -> {
				switch (action) {
					case CONFIRM_CASE_CLASSIFICATION:
						casesToClassify.add(caze);
						break;
					case CLONE_CASE_WITH_NEW_DISEASE:
						casesToClone.add(caze);
						break;
				}
			});
			
			if (isSampleResultDifferentFromPathogenTest(sample, updatedBulkResultData)) {
				samplesToUpdate.add(sample);
			}
		}
		
		showConfirmCaseDialog(casesToClassify);
		showCaseCloningWithNewDiseaseDialog(casesToClone, updatedBulkResultData.getTestedDisease());
		ControllerProvider.getSampleController().showChangePathogenTestResultWindow(
			null, 
			samplesToUpdate.stream().map(sample -> sample.getUuid()).collect(Collectors.toList()), 
			updatedBulkResultData.getTestResult(), 
			null
		);
	}
	
	private boolean isSampleResultDifferentFromPathogenTest (SampleIndexDto sample, PathogenTestDto test) {
		return test != null
			&& test.getTestResult() != null
			&& Boolean.TRUE.equals(test.getTestResultVerified())
			&& test.getTestedDisease() == sample.getDisease()
			&& test.getTestResult() != sample.getPathogenTestResult();
	}
	
	public void edit(PathogenTestDto dto, int caseSampleCount, Runnable doneCallback, BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest) {
		// get fresh data
		PathogenTestDto newDto = facade.getByUuid(dto.getUuid());

		PathogenTestForm form =
			new PathogenTestForm(FacadeProvider.getSampleFacade().getSampleByUuid(dto.getSample().getUuid()), false, caseSampleCount);
		form.setValue(newDto);
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<PathogenTestForm>(
			form,
			UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_EDIT),
			form.getFieldGroup());

		Window popupWindow = VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingEditPathogenTestResult));

		editView.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				if (!form.getFieldGroup().isModified()) {
					savePathogenTest(form.getValue(), onSavedPathogenTest, null);
					doneCallback.run();
				}
			}
		});

		if (UserProvider.getCurrent().hasUserRole(UserRole.ADMIN)) {
			editView.addDeleteListener(new DeleteListener() {

				@Override
				public void onDelete() {
					FacadeProvider.getPathogenTestFacade().deletePathogenTest(dto.getUuid());
					UI.getCurrent().removeWindow(popupWindow);
					doneCallback.run();
				}
			}, I18nProperties.getCaption(PathogenTestDto.I18N_PREFIX));
		}
	}

	private void savePathogenTest(PathogenTestDto dto, BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest, BiConsumer<SavePathogenTest_NeededAction, CaseDataDto> onActionNeeded) {
		final SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(dto.getSample().getUuid());
		final CaseReferenceDto associatedCase = sample.getAssociatedCase();
		final ContactReferenceDto associatedContact = sample.getAssociatedContact();
		if (associatedCase != null) {
			CaseDataDto preSaveCaseDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(associatedCase.getUuid());
			facade.savePathogenTest(dto);
			CaseDataDto postSaveCaseDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(associatedCase.getUuid());
			showSaveNotification(preSaveCaseDto, postSaveCaseDto);

			Runnable confirmCaseCallback = () -> {
				if (dto.getTestedDisease() == postSaveCaseDto.getDisease()
					&& PathogenTestResultType.POSITIVE.equals(dto.getTestResult())
					&& dto.getTestResultVerified().booleanValue() == true
					&& postSaveCaseDto.getCaseClassification() != CaseClassification.CONFIRMED
					&& postSaveCaseDto.getCaseClassification() != CaseClassification.NO_CASE) {
					if (onActionNeeded != null)
						onActionNeeded.accept(SavePathogenTest_NeededAction.CONFIRM_CASE_CLASSIFICATION, postSaveCaseDto);
					else
						showConfirmCaseDialog(postSaveCaseDto);
				}
			};

			Runnable caseCloningCallback = () -> {
				if (dto.getTestedDisease() != postSaveCaseDto.getDisease()
					&& dto.getTestResult() == PathogenTestResultType.POSITIVE
					&& dto.getTestResultVerified().booleanValue() == true) {
					if (onActionNeeded != null)
						onActionNeeded.accept(SavePathogenTest_NeededAction.CLONE_CASE_WITH_NEW_DISEASE, postSaveCaseDto);
					else
						showCaseCloningWithNewDiseaseDialog(postSaveCaseDto, dto.getTestedDisease());
				}
			};

			if (onSavedPathogenTest != null) {
				onSavedPathogenTest.accept(dto, () -> {
					confirmCaseCallback.run();
					caseCloningCallback.run();
				});
			} else {
				confirmCaseCallback.run();
				caseCloningCallback.run();
			}
		} else if (associatedContact != null) {
			facade.savePathogenTest(dto);
			
			if (onSavedPathogenTest != null) {
				onSavedPathogenTest.accept(dto, () -> { });
			}
		}
	}
	
	private enum SavePathogenTest_NeededAction {
		CONFIRM_CASE_CLASSIFICATION,
		CLONE_CASE_WITH_NEW_DISEASE,
	}

	private void showCaseCloningWithNewDiseaseDialog(Collection<CaseDataDto> existingCasesDtos, Disease disease) {
		
		if (existingCasesDtos == null || existingCasesDtos.size() == 0)
			return;
		
		String caption = existingCasesDtos.size() > 1 ? Captions.caseCloneCasesWithNewDisease : Captions.caseCloneCaseWithNewDisease;
		
		String labelText = existingCasesDtos.size() > 1 ?
				String.format(I18nProperties.getString(Strings.messageCloneCasesWithNewDisease), existingCasesDtos.size())
				: I18nProperties.getString(Strings.messageCloneCaseWithNewDisease);

		VaadinUiUtil.showConfirmationPopup(
			I18nProperties.getCaption(caption) + " " + I18nProperties.getEnumCaption(disease) + "?",
			new Label(labelText),
			I18nProperties.getString(Strings.yes),
			I18nProperties.getString(Strings.no),
			800,
			e -> {
				if (e.booleanValue() == true) {
					CaseDataDto firstClonedCase = null;
					
					for (CaseDataDto existingCaseDto : existingCasesDtos) {
						CaseDataDto clonedCase = FacadeProvider.getCaseFacade().cloneCase(existingCaseDto);
						clonedCase.setCaseClassification(CaseClassification.NOT_CLASSIFIED);
						clonedCase.setClassificationUser(null);
						clonedCase.setDisease(disease);
						clonedCase.setEpidNumber(null);
						clonedCase.setReportDate(new Date());
						FacadeProvider.getCaseFacade().saveCase(clonedCase);
						
						if (firstClonedCase == null)
							firstClonedCase = clonedCase;
					}
					
					if (existingCasesDtos.size() == 1)
						ControllerProvider.getCaseController().navigateToCase(firstClonedCase.getUuid());
				}
			});
	}
	
	private void showCaseCloningWithNewDiseaseDialog(CaseDataDto existingCaseDto, Disease disease) {
		showCaseCloningWithNewDiseaseDialog(Arrays.asList(existingCaseDto), disease);
	}

	private void showConfirmCaseDialog(Collection<CaseDataDto> cases) {
		
		if (cases == null || cases.size() == 0)
			return;
		
		String labelText = cases.size() > 1 ?
				String.format(I18nProperties.getString(Strings.messageConfirmCasesAfterPathogenTest), cases.size())
				: I18nProperties.getString(Strings.messageConfirmCaseAfterPathogenTest);
		
		VaadinUiUtil.showConfirmationPopup(
			I18nProperties.getCaption(Captions.caseConfirmCase),
			new Label(labelText),
			I18nProperties.getString(Strings.yes),
			I18nProperties.getString(Strings.no),
			800,
			e -> {
				if (e.booleanValue() == true) {
					for (CaseDataDto caze : cases) {
						caze.setCaseClassification(CaseClassification.CONFIRMED);
						FacadeProvider.getCaseFacade().saveCase(caze);
					}
				}
			});

	}
	
	private void showConfirmCaseDialog(CaseDataDto caze) {
		showConfirmCaseDialog(Arrays.asList(caze));
	}

	private void showSaveNotification(CaseDataDto existingCaseDto, CaseDataDto newCaseDto) {

		if (existingCaseDto.getCaseClassification() != newCaseDto.getCaseClassification() && newCaseDto.getClassificationUser() == null) {
			Notification.show(
				String.format(I18nProperties.getString(Strings.messagePathogenTestSaved), newCaseDto.getCaseClassification().toString()),
				Type.TRAY_NOTIFICATION);
		} else {
			Notification.show(I18nProperties.getString(Strings.messagePathogenTestSavedShort), Type.TRAY_NOTIFICATION);
		}
	}

	public void deleteAllSelectedItems(Collection<Object> selectedRows, Runnable callback) {

		if (selectedRows.size() == 0) {
			new Notification(
				I18nProperties.getString(Strings.headingNoPathogenTestsSelected),
				I18nProperties.getString(Strings.messageNoPathogenTestsSelected),
				Type.WARNING_MESSAGE,
				false).show(Page.getCurrent());
		} else {
			VaadinUiUtil.showDeleteConfirmationWindow(
				String.format(I18nProperties.getString(Strings.confirmationDeletePathogenTests), selectedRows.size()),
				() -> {
					for (Object selectedRow : selectedRows) {
						FacadeProvider.getPathogenTestFacade().deletePathogenTest(((PathogenTestDto) selectedRow).getUuid());
					}
					callback.run();
					new Notification(
						I18nProperties.getString(Strings.headingPathogenTestsDeleted),
						I18nProperties.getString(Strings.messagePathogenTestsDeleted),
						Type.HUMANIZED_MESSAGE,
						false).show(Page.getCurrent());
				});
		}
	}
}
