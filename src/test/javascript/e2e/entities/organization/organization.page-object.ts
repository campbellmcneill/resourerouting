import { element, by, ElementFinder } from 'protractor';

export class OrganizationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-organization div table .btn-danger'));
  title = element.all(by.css('jhi-organization div h2#page-heading span')).first();
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

export class OrganizationUpdatePage {
  pageTitle = element(by.id('jhi-organization-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  organizationNameInput = element(by.id('field_organizationName'));

  locationSelect = element(by.id('field_location'));
  opportunitySelect = element(by.id('field_opportunity'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setOrganizationNameInput(organizationName: string): Promise<void> {
    await this.organizationNameInput.sendKeys(organizationName);
  }

  async getOrganizationNameInput(): Promise<string> {
    return await this.organizationNameInput.getAttribute('value');
  }

  async locationSelectLastOption(): Promise<void> {
    await this.locationSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async locationSelectOption(option: string): Promise<void> {
    await this.locationSelect.sendKeys(option);
  }

  getLocationSelect(): ElementFinder {
    return this.locationSelect;
  }

  async getLocationSelectedOption(): Promise<string> {
    return await this.locationSelect.element(by.css('option:checked')).getText();
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

export class OrganizationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-organization-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-organization'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
