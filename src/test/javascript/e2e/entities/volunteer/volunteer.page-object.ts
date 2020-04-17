import { element, by, ElementFinder } from 'protractor';

export class VolunteerComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-volunteer div table .btn-danger'));
  title = element.all(by.css('jhi-volunteer div h2#page-heading span')).first();
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

export class VolunteerUpdatePage {
  pageTitle = element(by.id('jhi-volunteer-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstNameInput = element(by.id('field_firstName'));
  lastNameInput = element(by.id('field_lastName'));
  emailInput = element(by.id('field_email'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  hireDateInput = element(by.id('field_hireDate'));

  supervisorSelect = element(by.id('field_supervisor'));
  organizationSelect = element(by.id('field_organization'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFirstNameInput(firstName: string): Promise<void> {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput(): Promise<string> {
    return await this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName: string): Promise<void> {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput(): Promise<string> {
    return await this.lastNameInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setHireDateInput(hireDate: string): Promise<void> {
    await this.hireDateInput.sendKeys(hireDate);
  }

  async getHireDateInput(): Promise<string> {
    return await this.hireDateInput.getAttribute('value');
  }

  async supervisorSelectLastOption(): Promise<void> {
    await this.supervisorSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async supervisorSelectOption(option: string): Promise<void> {
    await this.supervisorSelect.sendKeys(option);
  }

  getSupervisorSelect(): ElementFinder {
    return this.supervisorSelect;
  }

  async getSupervisorSelectedOption(): Promise<string> {
    return await this.supervisorSelect.element(by.css('option:checked')).getText();
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

export class VolunteerDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-volunteer-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-volunteer'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
