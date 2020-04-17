import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OpportunityComponentsPage, OpportunityDeleteDialog, OpportunityUpdatePage } from './opportunity.page-object';

const expect = chai.expect;

describe('Opportunity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let opportunityComponentsPage: OpportunityComponentsPage;
  let opportunityUpdatePage: OpportunityUpdatePage;
  let opportunityDeleteDialog: OpportunityDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Opportunities', async () => {
    await navBarPage.goToEntity('opportunity');
    opportunityComponentsPage = new OpportunityComponentsPage();
    await browser.wait(ec.visibilityOf(opportunityComponentsPage.title), 5000);
    expect(await opportunityComponentsPage.getTitle()).to.eq('resourceroutingApp.opportunity.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(opportunityComponentsPage.entities), ec.visibilityOf(opportunityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Opportunity page', async () => {
    await opportunityComponentsPage.clickOnCreateButton();
    opportunityUpdatePage = new OpportunityUpdatePage();
    expect(await opportunityUpdatePage.getPageTitle()).to.eq('resourceroutingApp.opportunity.home.createOrEditLabel');
    await opportunityUpdatePage.cancel();
  });

  it('should create and save Opportunities', async () => {
    const nbButtonsBeforeCreate = await opportunityComponentsPage.countDeleteButtons();

    await opportunityComponentsPage.clickOnCreateButton();

    await promise.all([
      opportunityUpdatePage.setOpportunityTitleInput('opportunityTitle'),
      opportunityUpdatePage.setOpportunityDescriptionInput('opportunityDescription'),
      opportunityUpdatePage.setWeeklyTimeCommitmentInput('5'),
      opportunityUpdatePage.setDurationInput('5'),
      // opportunityUpdatePage.taskSelectLastOption(),
      opportunityUpdatePage.volunteerSelectLastOption()
    ]);

    expect(await opportunityUpdatePage.getOpportunityTitleInput()).to.eq(
      'opportunityTitle',
      'Expected OpportunityTitle value to be equals to opportunityTitle'
    );
    expect(await opportunityUpdatePage.getOpportunityDescriptionInput()).to.eq(
      'opportunityDescription',
      'Expected OpportunityDescription value to be equals to opportunityDescription'
    );
    expect(await opportunityUpdatePage.getWeeklyTimeCommitmentInput()).to.eq('5', 'Expected weeklyTimeCommitment value to be equals to 5');
    expect(await opportunityUpdatePage.getDurationInput()).to.eq('5', 'Expected duration value to be equals to 5');

    await opportunityUpdatePage.save();
    expect(await opportunityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await opportunityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Opportunity', async () => {
    const nbButtonsBeforeDelete = await opportunityComponentsPage.countDeleteButtons();
    await opportunityComponentsPage.clickOnLastDeleteButton();

    opportunityDeleteDialog = new OpportunityDeleteDialog();
    expect(await opportunityDeleteDialog.getDialogTitle()).to.eq('resourceroutingApp.opportunity.delete.question');
    await opportunityDeleteDialog.clickOnConfirmButton();

    expect(await opportunityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
