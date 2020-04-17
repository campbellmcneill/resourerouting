import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VolunteerComponentsPage, VolunteerDeleteDialog, VolunteerUpdatePage } from './volunteer.page-object';

const expect = chai.expect;

describe('Volunteer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let volunteerComponentsPage: VolunteerComponentsPage;
  let volunteerUpdatePage: VolunteerUpdatePage;
  let volunteerDeleteDialog: VolunteerDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Volunteers', async () => {
    await navBarPage.goToEntity('volunteer');
    volunteerComponentsPage = new VolunteerComponentsPage();
    await browser.wait(ec.visibilityOf(volunteerComponentsPage.title), 5000);
    expect(await volunteerComponentsPage.getTitle()).to.eq('resourceroutingApp.volunteer.home.title');
    await browser.wait(ec.or(ec.visibilityOf(volunteerComponentsPage.entities), ec.visibilityOf(volunteerComponentsPage.noResult)), 1000);
  });

  it('should load create Volunteer page', async () => {
    await volunteerComponentsPage.clickOnCreateButton();
    volunteerUpdatePage = new VolunteerUpdatePage();
    expect(await volunteerUpdatePage.getPageTitle()).to.eq('resourceroutingApp.volunteer.home.createOrEditLabel');
    await volunteerUpdatePage.cancel();
  });

  it('should create and save Volunteers', async () => {
    const nbButtonsBeforeCreate = await volunteerComponentsPage.countDeleteButtons();

    await volunteerComponentsPage.clickOnCreateButton();

    await promise.all([
      volunteerUpdatePage.setFirstNameInput('firstName'),
      volunteerUpdatePage.setLastNameInput('lastName'),
      volunteerUpdatePage.setEmailInput('email'),
      volunteerUpdatePage.setPhoneNumberInput('phoneNumber'),
      volunteerUpdatePage.setHireDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      volunteerUpdatePage.supervisorSelectLastOption(),
      volunteerUpdatePage.organizationSelectLastOption()
    ]);

    expect(await volunteerUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await volunteerUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await volunteerUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await volunteerUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await volunteerUpdatePage.getHireDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected hireDate value to be equals to 2000-12-31'
    );

    await volunteerUpdatePage.save();
    expect(await volunteerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await volunteerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Volunteer', async () => {
    const nbButtonsBeforeDelete = await volunteerComponentsPage.countDeleteButtons();
    await volunteerComponentsPage.clickOnLastDeleteButton();

    volunteerDeleteDialog = new VolunteerDeleteDialog();
    expect(await volunteerDeleteDialog.getDialogTitle()).to.eq('resourceroutingApp.volunteer.delete.question');
    await volunteerDeleteDialog.clickOnConfirmButton();

    expect(await volunteerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
