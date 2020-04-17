import { element, by, ElementFinder } from 'protractor';

export class OpportunityHistoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-opportunity-history div table .btn-danger'));
  title = element.all(by.css('jhi-opportunity-history div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class OpportunityHistoryUpdatePage {
  pageTitle = element(by.id('jhi-opportunity-history-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  ratingInput = element(by.id('field_rating'));
  commentsInput = element(by.id('field_comments'));
  languageSelect = element(by.id('field_language'));

  opportunitySelect = element(by.id('field_opportunity'));
  organizationSelect = element(by.id('field_organization'));
  volunteerSelect = element(by.id('field_volunteer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setEndDateInput(endDate: string): Promise<void> {
    await this.endDateInput.sendKeys(endDate);
  }

  async getEndDateInput(): Promise<string> {
    return await this.endDateInput.getAttribute('value');
  }

  async setRatingInput(rating: string): Promise<void> {
    await this.ratingInput.sendKeys(rating);
  }

  async getRatingInput(): Promise<string> {
    return await this.ratingInput.getAttribute('value');
  }

  async setCommentsInput(comments: string): Promise<void> {
    await this.commentsInput.sendKeys(comments);
  }

  async getCommentsInput(): Promise<string> {
    return await this.commentsInput.getAttribute('value');
  }

  async setLanguageSelect(language: string): Promise<void> {
    await this.languageSelect.sendKeys(language);
  }

  async getLanguageSelect(): Promise<string> {
    return await this.languageSelect.element(by.css('option:checked')).getText();
  }

  async languageSelectLastOption(): Promise<void> {
    await this.languageSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async opportunitySelectLastOption(): Promise<void> {
    await this.opportunitySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async opportunitySelectOption(option: string): Promise<void> {
    await this.opportunitySelect.sendKeys(option);
  }

  getOpportunitySelect(): ElementFinder {
    return this.opportunitySelect;
  }

  async getOpportunitySelectedOption(): Promise<string> {
    return await this.opportunitySelect.element(by.css('option:checked')).getText();
  }

  async organizationSelectLastOption(): Promise<void> {
    await this.organizationSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async organizationSelectOption(option: string): Promise<void> {
    await this.organizationSelect.sendKeys(option);
  }

  getOrganizationSelect(): ElementFinder {
    return this.organizationSelect;
  }

  async getOrganizationSelectedOption(): Promise<string> {
    return await this.organizationSelect.element(by.css('option:checked')).getText();
  }

  async volunteerSelectLastOption(): Promise<void> {
    await this.volunteerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async volunteerSelectOption(option: string): Promise<void> {
    await this.volunteerSelect.sendKeys(option);
  }

  getVolunteerSelect(): ElementFinder {
    return this.volunteerSelect;
  }

  async getVolunteerSelectedOption(): Promise<string> {
    return await this.volunteerSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class OpportunityHistoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-opportunityHistory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-opportunityHistory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
