import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  OpportunityHistoryComponentsPage,
  OpportunityHistoryDeleteDialog,
  OpportunityHistoryUpdatePage
} from './opportunity-history.page-object';

const expect = chai.expect;

describe('OpportunityHistory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let opportunityHistoryComponentsPage: OpportunityHistoryComponentsPage;
  let opportunityHistoryUpdatePage: OpportunityHistoryUpdatePage;
  let opportunityHistoryDeleteDialog: OpportunityHistoryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OpportunityHistories', async () => {
    await navBarPage.goToEntity('opportunity-history');
    opportunityHistoryComponentsPage = new OpportunityHistoryComponentsPage();
    await browser.wait(ec.visibilityOf(opportunityHistoryComponentsPage.title), 5000);
    expect(await opportunityHistoryComponentsPage.getTitle()).to.eq('resourceroutingApp.opportunityHistory.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(opportunityHistoryComponentsPage.entities), ec.visibilityOf(opportunityHistoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create OpportunityHistory page', async () => {
    await opportunityHistoryComponentsPage.clickOnCreateButton();
    opportunityHistoryUpdatePage = new OpportunityHistoryUpdatePage();
    expect(await opportunityHistoryUpdatePage.getPageTitle()).to.eq('resourceroutingApp.opportunityHistory.home.createOrEditLabel');
    await opportunityHistoryUpdatePage.cancel();
  });

  it('should create and save OpportunityHistories', async () => {
    const nbButtonsBeforeCreate = await opportunityHistoryComponentsPage.countDeleteButtons();

    await opportunityHistoryComponentsPage.clickOnCreateButton();

    await promise.all([
      opportunityHistoryUpdatePage.setStartDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      opportunityHistoryUpdatePage.setEndDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      opportunityHistoryUpdatePage.setRatingInput('5'),
      opportunityHistoryUpdatePage.setCommentsInput('comments'),
      opportunityHistoryUpdatePage.languageSelectLastOption(),
      opportunityHistoryUpdatePage.opportunitySelectLastOption(),
      opportunityHistoryUpdatePage.organizationSelectLastOption(),
      opportunityHistoryUpdatePage.volunteerSelectLastOption()
    ]);

    expect(await opportunityHistoryUpdatePage.getStartDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected startDate value to be equals to 2000-12-31'
    );
    expect(await opportunityHistoryUpdatePage.getEndDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected endDate value to be equals to 2000-12-31'
    );
    expect(await opportunityHistoryUpdatePage.getRatingInput()).to.eq('5', 'Expected rating value to be equals to 5');
    expect(await opportunityHistoryUpdatePage.getCommentsInput()).to.eq('comments', 'Expected Comments value to be equals to comments');

    await opportunityHistoryUpdatePage.save();
    expect(await opportunityHistoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await opportunityHistoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last OpportunityHistory', async () => {
    const nbButtonsBeforeDelete = await opportunityHistoryComponentsPage.countDeleteButtons();
    await opportunityHistoryComponentsPage.clickOnLastDeleteButton();

    opportunityHistoryDeleteDialog = new OpportunityHistoryDeleteDialog();
    expect(await opportunityHistoryDeleteDialog.getDialogTitle()).to.eq('resourceroutingApp.opportunityHistory.delete.question');
    await opportunityHistoryDeleteDialog.clickOnConfirmButton();

    expect(await opportunityHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
