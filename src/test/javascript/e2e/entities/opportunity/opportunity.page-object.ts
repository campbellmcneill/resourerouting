import { element, by, ElementFinder } from 'protractor';

export class OpportunityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-opportunity div table .btn-danger'));
  title = element.all(by.css('jhi-opportunity div h2#page-heading span')).first();
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

export class OpportunityUpdatePage {
  pageTitle = element(by.id('jhi-opportunity-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  opportunityTitleInput = element(by.id('field_opportunityTitle'));
  opportunityDescriptionInput = element(by.id('field_opportunityDescription'));
  weeklyTimeCommitmentInput = element(by.id('field_weeklyTimeCommitment'));
  durationInput = element(by.id('field_duration'));

  taskSelect = element(by.id('field_task'));
  volunteerSelect = element(by.id('field_volunteer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setOpportunityTitleInput(opportunityTitle: string): Promise<void> {
    await this.opportunityTitleInput.sendKeys(opportunityTitle);
  }

  async getOpportunityTitleInput(): Promise<string> {
    return await this.opportunityTitleInput.getAttribute('value');
  }

  async setOpportunityDescriptionInput(opportunityDescription: string): Promise<void> {
    await this.opportunityDescriptionInput.sendKeys(opportunityDescription);
  }

  async getOpportunityDescriptionInput(): Promise<string> {
    return await this.opportunityDescriptionInput.getAttribute('value');
  }

  async setWeeklyTimeCommitmentInput(weeklyTimeCommitment: string): Promise<void> {
    await this.weeklyTimeCommitmentInput.sendKeys(weeklyTimeCommitment);
  }

  async getWeeklyTimeCommitmentInput(): Promise<string> {
    return await this.weeklyTimeCommitmentInput.getAttribute('value');
  }

  async setDurationInput(duration: string): Promise<void> {
    await this.durationInput.sendKeys(duration);
  }

  async getDurationInput(): Promise<string> {
    return await this.durationInput.getAttribute('value');
  }

  async taskSelectLastOption(): Promise<void> {
    await this.taskSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async taskSelectOption(option: string): Promise<void> {
    await this.taskSelect.sendKeys(option);
  }

  getTaskSelect(): ElementFinder {
    return this.taskSelect;
  }

  async getTaskSelectedOption(): Promise<string> {
    return await this.taskSelect.element(by.css('option:checked')).getText();
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

export class OpportunityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-opportunity-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-opportunity'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
