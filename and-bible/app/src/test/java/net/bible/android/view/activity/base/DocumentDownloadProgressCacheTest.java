package net.bible.android.view.activity.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import net.bible.android.activity.BuildConfig;
import net.bible.android.activity.R;
import net.bible.android.view.util.widget.TwoLineListItemWithImage;
import net.bible.service.download.FakeSwordBookFactory;

import org.crosswire.common.progress.JobManager;
import org.crosswire.common.progress.Progress;
import org.crosswire.jsword.book.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Martin Denham [mjdenham at gmail dot com]
 * @see gnu.lgpl.License for license details.<br>
 * The copyright to this program is held by it's author.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DocumentDownloadProgressCacheTest {

	private DocumentDownloadProgressCache documentDownloadProgressCache;

	private TestData testData;

	@Before
	public void setUp() throws Exception {
		documentDownloadProgressCache = new DocumentDownloadProgressCache();
		testData = new TestData();
	}

	@Test
	public void updateProgress() throws Exception {

		// prepare an item
		documentDownloadProgressCache.documentShown(testData.document, testData.progressBar);

		// update progress and check
		documentDownloadProgressCache.updateProgress(testData.progress);
		assertThat(testData.progressBar.getProgress(), equalTo(33));

		// update progress again and recheck
		testData.progress.setWork(100);
		documentDownloadProgressCache.updateProgress(testData.progress);
		assertThat(testData.progressBar.getProgress(), equalTo(100));
	}

	@Test
	public void documentHidden() throws Exception {
		// prepare an item
		documentDownloadProgressCache.documentShown(testData.document, testData.progressBar);
		documentDownloadProgressCache.updateProgress(testData.progress);


		// Hide item and check there is no associated progressBar
		documentDownloadProgressCache.documentHidden(testData.document);

		//TODO check progressBar disassociated e.g. by adding isProgressBar(Book)
	}

	@Test
	public void documentShown() throws Exception {
		assertThat(testData.progressBar.getVisibility(), equalTo(View.GONE));

		// prepare an item
		documentDownloadProgressCache.documentShown(testData.document, testData.progressBar);
		// for shown items the progress bar is not visible until percent is set
		assertThat(testData.progressBar.getVisibility(), equalTo(View.GONE));
	}

	private class TestData {

		String initials = "KJV";
		Book document;
		Progress progress = JobManager.createJob("INSTALL_BOOK-"+initials, "Installing King James Version", null);
		TwoLineListItemWithImage twoLineListItemWithImage;
		ProgressBar progressBar;

		{
			try {
				document = FakeSwordBookFactory.createFakeRepoBook(initials, "[KJV]\nDescription=My Test Book", "");
				progress.setTotalWork(100);
				progress.setWork(33);

				Activity activity = Robolectric.buildActivity(Activity.class).create().get();
				twoLineListItemWithImage = (TwoLineListItemWithImage) LayoutInflater.from(activity).inflate(R.layout.list_item_2_image, null);
				progressBar = twoLineListItemWithImage.getProgressBar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}
}