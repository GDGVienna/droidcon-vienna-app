package at.droidcon.vienna2016.ui.schedule.pager;

import android.os.Build;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import at.droidcon.vienna2016.BuildConfig;
import at.droidcon.vienna2016.core.rxjava.TestSchedulerProxy;
import at.droidcon.vienna2016.data.app.DataProvider;
import at.droidcon.vienna2016.data.app.model.Schedule;
import rx.Observable;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SchedulePagerPresenterTest {

    @Mock SchedulePagerMvp.View view;
    @Mock DataProvider dataProvider;

    private SchedulePagerPresenter presenter;
    private final TestSchedulerProxy schedulerProxy = TestSchedulerProxy.get();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new SchedulePagerPresenter(view, dataProvider);
    }

    @Test
    public void should_load_schedule_when_view_is_created() {
        // Given
        Schedule schedule = new Schedule();
        when(dataProvider.getSchedule()).thenReturn(Observable.just(schedule));

        // When
        presenter.onViewCreated(null, null);
        schedulerProxy.advanceBy(1, TimeUnit.MINUTES);

        // Then
        assertThat(presenter.schedule).isSameAs(schedule);
        verify(view).displaySchedule(schedule);
    }

    @Test
    public void should_restore_schedule_when_state_changes() {
        // Given
        Schedule schedule = new Schedule();
        presenter.schedule = schedule;

        // When
        presenter.onViewCreated(null, new Bundle());

        // Then
        verify(view).displaySchedule(schedule);
    }
}
